package com.kev95p.pruebakotlin.data.api


import android.content.Context
import android.util.Log
import com.google.gson.JsonParser
import com.kev95p.pruebakotlin.data.dao.PokemonDao
import com.kev95p.pruebakotlin.data.dblocal.AppDatabase
import com.kev95p.pruebakotlin.data.dto.PokemonDto
import com.kev95p.pruebakotlin.utils.SingletonHolder
import io.reactivex.Observable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request

class PokemonApi private constructor(private val context: Context) {

    private val cacheSize = (5 * 1024 * 1024).toLong()
    private var client: OkHttpClient
    private val pokemonDao = AppDatabase.getDatabase(context)
        .pokemonDao()

    init {
        val cache = Cache(context.cacheDir, cacheSize)
        client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val request = chain.request()
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                chain.proceed(request)
            }
            .build()
    }

    private fun fetchFromApi(limit: Int, offset: Int): ArrayList<PokemonDto?> {
        val request = Request.Builder()
            .url(Endpoint.allPokemon + "?limit=$limit&offset=$offset")
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val allPokemonResult = JsonParser().parse(response.body?.string()).asJsonObject
            val arrayResult = allPokemonResult.getAsJsonArray("results")

            val pokemonList = ArrayList<PokemonDto?>()

            arrayResult.forEach { item ->
                val detailUrl = item.asJsonObject.get("url").asString.dropLast(1)
                val detailRequest = Request.Builder().url(detailUrl)
                    .build()
                val detailResponse = client.newCall(detailRequest).execute()
                if (detailResponse.isSuccessful) {
                    val detailPokemon =
                        JsonParser().parse(detailResponse.body?.string()).asJsonObject

                    val type1 = detailPokemon.getAsJsonArray("types")
                        .get(0).asJsonObject.getAsJsonObject("type")
                        .get("name").asString
                    val type2 = if (detailPokemon.getAsJsonArray("types")
                            .size() > 1
                    ) (detailPokemon.getAsJsonArray("types")
                        .get(1).asJsonObject.getAsJsonObject("type")
                        .get("name").asString) else null

                    var sprite = detailPokemon.getAsJsonObject("sprites")
                        .get("front_default").asString

                    var imageUrl:String? = sprite
                    if( detailPokemon.getAsJsonObject("sprites").has("other")) {
                        val other = detailPokemon.getAsJsonObject("sprites").getAsJsonObject("other")
                        if (other.has("official-artwork")) {
                            val officialArtwork = other.getAsJsonObject("official-artwork")
                            if (officialArtwork.has("front_default")) {
                                imageUrl = officialArtwork.get("front_default").asString
                            }
                        }
                    }

                    val hp = detailPokemon.getAsJsonArray("stats")[0].asJsonObject
                        .get("base_stat").asInt
                    val atk = detailPokemon.getAsJsonArray("stats")[1].asJsonObject
                        .get("base_stat").asInt
                    val def = detailPokemon.getAsJsonArray("stats")[2].asJsonObject
                        .get("base_stat").asInt
                    val spAtk = detailPokemon.getAsJsonArray("stats")[3].asJsonObject
                        .get("base_stat").asInt
                    val spDef = detailPokemon.getAsJsonArray("stats")[4].asJsonObject
                        .get("base_stat").asInt
                    val speed = detailPokemon.getAsJsonArray("stats")[5].asJsonObject
                        .get("base_stat").asInt

                    val pokemonData = PokemonDto(
                        detailPokemon.get("id").asInt,
                        detailPokemon.get("name").asString,
                        imageUrl,
                        "",
                        type1,
                        type2,
                        sprite,
                        hp,
                        atk,
                        def,
                        spAtk,
                        spDef,
                        speed
                    )
                    detailResponse.close()
                    pokemonList.add(pokemonData)
                }else{
                    Log.e("PokemonApi","Error en request detalle pokemon $detailResponse")
                }
            }

            response.close()
            return pokemonList
        } else {
            Log.e("PokemonApi","Error en request lista pokemon")
            return ArrayList()
        }


    }

    private fun saveLocally(list: ArrayList<PokemonDto?>) {
        list.forEach { pokemonDto ->
            if (pokemonDto != null) {
                AppDatabase.getDatabase(context)
                    .pokemonDao().insert(pokemonDto)
            }
        }
    }

    fun getAllPokemon(limit: Int, offset: Int): Observable<ArrayList<PokemonDto?>> {
        return Observable.create { emitter ->
            //fetch from local
            var total = 0;
            val pokemonList = pokemonDao.getAll()
            if (pokemonList.isNotEmpty()) {
                total = pokemonList.size
                emitter.onNext(pokemonList as ArrayList<PokemonDto?>)
            } else {
                //fetch from api
                val listFromApi = fetchFromApi(50, 0)
                emitter.onNext(listFromApi)
                saveLocally(listFromApi)
                total = listFromApi.size
            }
            //load more data in background
            while(total in 50..897){
                val listFromApi = fetchFromApi(100, total)
                emitter.onNext(listFromApi)
                saveLocally(listFromApi)
                total += listFromApi.size
                Log.d("PokemonApi","Total fetch in background: $total")
            }
            Log.d("PokemonApi","All pokemon loaded")
            emitter.onComplete()
        }
    }

    companion object : SingletonHolder<PokemonApi, Context>({
        PokemonApi(it)
    })
}
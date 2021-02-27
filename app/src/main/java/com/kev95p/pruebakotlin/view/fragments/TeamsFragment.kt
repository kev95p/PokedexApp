package com.kev95p.pruebakotlin.view.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kev95p.pruebakotlin.R
import com.kev95p.pruebakotlin.data.dto.TeamDto
import com.kev95p.pruebakotlin.interfaces.Teams
import com.kev95p.pruebakotlin.presenter.TeamsPresenterImpl
import com.kev95p.pruebakotlin.view.activities.AddTeamActivity
import com.kev95p.pruebakotlin.view.activities.LoginActivity
import com.kev95p.pruebakotlin.view.adapters.TeamsListAdapter

class TeamsFragment : Fragment(), Teams.View {

    private var presenter: Teams.Presenter? = null

    private lateinit var teamsList: RecyclerView
    private lateinit var teamsListAdapter: TeamsListAdapter
    private lateinit var teamsData: ArrayList<TeamDto?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = activity?.let { TeamsPresenterImpl(this, it?.applicationContext) }
        presenter?.getTeams()
        teamsList = view.findViewById(R.id.teamsList)
        teamsList.setHasFixedSize(true)
        teamsData = ArrayList()
        teamsListAdapter = activity?.baseContext?.let { TeamsListAdapter(teamsData, it) }!!
        teamsList.adapter = teamsListAdapter
        teamsListAdapter.onClickListener = object : TeamsListAdapter.OnClickListener{
            override fun onItemClick(team: TeamDto) {
                Log.d("TeamFragment",team.toString())
                val  i = Intent(context,AddTeamActivity::class.java)
                i.putExtra("key",team.key)
                startActivity(i)
            }

            override fun onItemLongClick(team: TeamDto) {
                val dialogBuilder = AlertDialog.Builder(context);
                dialogBuilder.setTitle("Delete team")
                dialogBuilder.setMessage("Are you sure to delete this team")
                dialogBuilder.setPositiveButton("Ok") { dlg, _ ->
                    presenter?.deleteTeam(team)
                    dlg.dismiss()
                }

                dialogBuilder.setNegativeButton("Cancel"){dlg,_ ->
                    run {
                        dlg.cancel()
                    }
                }

                val dialog = dialogBuilder.create()
                dialog.show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.team_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_team_action){
            val intent = Intent(activity,AddTeamActivity::class.java)
            startActivity(intent)
        }
        else if(item.itemId == R.id.close_session_team){
            val shared = activity?.applicationContext?.getSharedPreferences("pref",0)
            val editor = shared?.edit()
            editor?.remove("currentUser")
            editor?.apply()

            val newIntent = Intent(activity?.applicationContext, LoginActivity::class.java)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(newIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun receiveTeams(data: ArrayList<TeamDto?>) {
        teamsData.clear()
        teamsData.addAll(data)
        teamsListAdapter.notifyDataSetChanged()
    }

}
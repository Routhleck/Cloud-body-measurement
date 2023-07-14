package com.cloudsports.actiondetect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudsports.actiondetect.adapter.SportsHistoryAdapter
import com.cloudsports.actiondetect.data.Sport
import com.cloudsports.actiondetect.model.GlobalVariable

class SportsHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_history)

        val sports = GlobalVariable.sportsHistoryList ?: listOf<Sport>()

        viewManager = LinearLayoutManager(this)
        viewAdapter = SportsHistoryAdapter(sports)

        recyclerView = findViewById<RecyclerView>(R.id.sports_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}

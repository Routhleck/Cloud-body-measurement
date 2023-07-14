package com.cloudsports.actiondetect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.adapter.ActionAdapter
import com.cloudsports.actiondetect.data.Action

class SportCenterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sport_center, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        val actions = listOf(
            Action(R.drawable.sports_pullup, "引体向上", name = "pullUp"),
            Action(R.drawable.sports_pushup, "俯卧撑", name = "pushUp"),
            Action(R.drawable.sports_situp, "仰卧起坐", name = "sitUp"),
            Action(R.drawable.sports_squat, "深蹲", name = "squat"),
            // 添加更多的 actions
        )
        val adapter = ActionAdapter(actions)
        recyclerView.adapter = adapter

        return view
    }
}


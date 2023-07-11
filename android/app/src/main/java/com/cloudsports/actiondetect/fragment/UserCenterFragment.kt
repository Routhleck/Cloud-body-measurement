package com.cloudsports.actiondetect.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cloudsports.actiondetect.LoginActivity
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.SportsHistoryActivity
import com.cloudsports.actiondetect.TestGradeActivity

class UserCenterFragment : Fragment() {

    private lateinit var tvContent: TextView
    private lateinit var tvSportsRecord: TextView
    private lateinit var tvSportsCount: TextView
    private lateinit var account: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_center, container, false)

        tvContent = view.findViewById(R.id.tv_content)
        tvSportsRecord = view.findViewById(R.id.tv_sports_record)
        tvSportsCount = view.findViewById(R.id.tv_sports_count)

        // I assume that the 'account' information is passed via arguments to the Fragment
        account = arguments?.getString("account")!!

        tvContent.text = "欢迎你：$account"

        val btnTestGrade: Button = view.findViewById(R.id.btn_test_grade)
        val btnSportsHistory: Button = view.findViewById(R.id.btn_sports_history)
        val btnUserCenterLogout: Button = view.findViewById(R.id.btn_user_center_logout)

        // setup click listeners
        btnTestGrade.setOnClickListener {
            val intent = Intent(context, TestGradeActivity::class.java)
            startActivity(intent)
        }

        btnSportsHistory.setOnClickListener {
            val intent = Intent(context, SportsHistoryActivity::class.java)
            startActivity(intent)
        }

        btnUserCenterLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            // This line is to make sure that HomeActivity is finished when logout
            activity?.finish()
        }

        return view
    }

    companion object {
        fun newInstance(account: String): UserCenterFragment {
            val fragment = UserCenterFragment()
            val args = Bundle().apply {
                putString("account", account)
            }
            fragment.arguments = args
            return fragment
        }
    }
}


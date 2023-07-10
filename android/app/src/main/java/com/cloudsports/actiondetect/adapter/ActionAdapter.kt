package com.cloudsports.actiondetect.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cloudsports.actiondetect.DetectActivity
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.data.Action

class ActionAdapter(private val actions: List<Action>) :
    RecyclerView.Adapter<ActionAdapter.ActionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_action, parent, false)
        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = actions[position]
        holder.bind(action)

        // 使用 Glide 进行圆角转换
        Glide.with(holder.itemView)
            .load(action.imageResId)
            .transform(RoundedCorners(20)) // 设置圆角半径，可以根据需要进行调整
            .into(holder.imageView)

        // 设置点击事件
        holder.imageView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetectActivity::class.java)
            intent.putExtra("action_name", action.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return actions.size
    }

    inner class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(action: Action) {
            imageView.setImageResource(action.imageResId)
            textView.text = action.text
        }
    }
}

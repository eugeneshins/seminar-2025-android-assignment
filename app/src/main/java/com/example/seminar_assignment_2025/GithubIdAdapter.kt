package com.example.seminar_assignment_2025

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class GithubIdAdapter(private val githubIds: List<String>) : RecyclerView.Adapter<GithubIdAdapter.ViewHolder>() {

    // 1. ViewHolder: 아이템 뷰(item_github_row.xml)의 내용물을 담는 그릇
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val githubIdTextView: TextView = view.findViewById(R.id.github_id_text_item)

        init {
            // itemView는 ViewHolder가 담고 있는 아이템 뷰 전체(LinearLayout)를 가리킵니다.
            itemView.setOnClickListener {
                // 1. Intent를 실행하려면 Context가 필요합니다. itemView에서 얻어올 수 있습니다.
                val context = itemView.context

                // 2. 웹페이지를 열기 위한 Intent를 생성합니다.
                val intent = Intent(Intent.ACTION_VIEW, "https://github.com/dgddgd314".toUri())

                // 3. Context를 이용해 Intent를 실행합니다.
                context.startActivity(intent)
            }
        }

    }

    // 2. onCreateViewHolder: 새로운 ViewHolder(그릇)를 생성 (레이아웃 인플레이트)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_github_row, parent, false)
        return ViewHolder(view)
    }

    // 3. onBindViewHolder: ViewHolder(그릇)에 데이터를 담아주는 역할
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val githubId = githubIds[position]
        holder.githubIdTextView.text = githubId
        // (필요하다면) 여기에 클릭 리스너를 추가할 수도 있습니다.
    }

    // 4. getItemCount: 전체 아이템의 개수를 반환
    override fun getItemCount(): Int {
        return githubIds.size
    }
}
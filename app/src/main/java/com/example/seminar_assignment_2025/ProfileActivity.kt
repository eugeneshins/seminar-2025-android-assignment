package com.example.seminar_assignment_2025

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        // 1. ID로 뒤로가기 버튼(ImageView)을 찾습니다.
        val toolbarBackButton = findViewById<ImageView>(R.id.toolbar_back_button)

        // 2. 버튼에 클릭 리스너를 설정합니다.
        toolbarBackButton.setOnClickListener {
            // 3. 클릭 시 finish()를 호출하여 현재 화면을 종료합니다.
            finish()
        }

        // 1. XML에 있는 TextView를 ID로 찾아 변수에 연결합니다.
        val slackUrlTextView = findViewById<TextView>(R.id.slack_url_text)

        // 2. MainActivity가 보낸 Intent(소포)를 받습니다.
        val receivedIntent = intent

        // 3. 소포에서 "EXTRA_SLACK_URL" 이름표로 데이터를 꺼냅니다.
//        val slackUrl = receivedIntent.getStringExtra(MainActivity.EXTRA_SLACK_URL)

        // 4. 꺼낸 데이터를 TextView에 텍스트로 설정합니다.
//        slackUrlTextView.text = slackUrl
        slackUrlTextView.text = "GG"
        // 1. RecyclerView를 위한 데이터 준비 (ID 10개)
        val githubIdList = List(10) { "dgddgd314" } // "qdrptd"를 10번 반복하는 리스트 생성

        // 2. XML에서 RecyclerView 찾기
        val recyclerView = findViewById<RecyclerView>(R.id.github_recycler_view)

        // 3. 어댑터 생성 및 데이터 전달
        val adapter = GithubIdAdapter(githubIdList)

        // 4. RecyclerView에 어댑터와 레이아웃 매니저 설정
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // 아이템을 세로로 나열

    }
}
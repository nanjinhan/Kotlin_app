package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.ActivityRamenListBinding

class RamenListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRamenListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRamenListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.title_dex)

        // 2열 그리드로 배치 (도감 느낌). 책 목록과 달리 가로 2칸씩 채움
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = RamenAdapter(RamenData.list) { ramen ->
            // 상세로 라면 '이름'을 Intent에 담아 전달 (13장 엑스트라)
            val intent = Intent(this, RamenDetailActivity::class.java)
            intent.putExtra("name", ramen.name)
            startActivity(intent)
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_dex
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { goTo(MainActivity::class.java); true }
                R.id.nav_fav -> { goTo(FavoriteActivity::class.java); true }
                else -> true
            }
        }
    }

    private fun goTo(cls: Class<*>) {
        val i = Intent(this, cls)
        i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)
    }
}
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // 드로어 토글 (햄버거로 서랍 열기 — 가산점)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.d_dex -> startActivity(Intent(this, RamenListActivity::class.java))
                R.id.d_fav -> startActivity(Intent(this, FavoriteActivity::class.java))
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        // "라면 도감 열기" 버튼
        binding.btnOpenDex.setOnClickListener {
            startActivity(Intent(this, RamenListActivity::class.java))
        }

        // 하단 탭바: 현재 홈 선택 상태로 두고, 다른 탭 누르면 해당 화면으로 이동
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dex -> { startActivity(Intent(this, RamenListActivity::class.java)); true }
                R.id.nav_fav -> { startActivity(Intent(this, FavoriteActivity::class.java)); true }
                else -> true
            }
        }
    }

    // 홈에 돌아올 때마다 "마지막으로 본 라면" 갱신 (SharedPreferences 활용)
    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("ramen_dex", MODE_PRIVATE)
        val last = prefs.getString("last_ramen", null)
        binding.tvLast.text = if (last != null) "마지막으로 본 라면: $last" else "아직 본 라면이 없어요"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                // 앱 정보 대화상자 (AlertDialog 가산점)
                AlertDialog.Builder(this)
                    .setTitle("앱 정보")
                    .setMessage("라면도감 v1.0\n맛있는 라면을 한눈에 보는 도감 앱\n\n제작: 박진한 (224543)")
                    .setPositiveButton("확인", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
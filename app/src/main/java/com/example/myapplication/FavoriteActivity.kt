package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.title_fav)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        setupBottomNav()
    }

    // 다시 돌아올 때마다 찜 목록을 새로 불러옴 (찜 추가/해제 반영)
    override fun onResume() {
        super.onResume()
        val favs = FavoriteManager.getFavorites(this)
        val list = RamenData.list.filter { favs.contains(it.name) }

        binding.tvEmpty.visibility = if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.recyclerView.adapter = RamenAdapter(list) { ramen ->
            val intent = Intent(this, RamenDetailActivity::class.java)
            intent.putExtra("name", ramen.name)
            startActivity(intent)
        }
        binding.bottomNav.selectedItemId = R.id.nav_fav
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { goTo(MainActivity::class.java); true }
                R.id.nav_dex -> { goTo(RamenListActivity::class.java); true }
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
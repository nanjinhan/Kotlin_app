package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.ActivityFavoriteBinding

/**
 * FavoriteActivity — 찜한 라면만 모아 보여주는 화면.
 * 도감과 같은 2열 그리드 카드로 표시하며, 찜한 게 없으면 안내 문구를 보여준다.
 */
class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.title_fav)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // 툴바에 '←' 뒤로가기 화살표 표시

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)    // 2열 그리드
        setupBottomNav()
    }

    /**
     * 화면이 다시 보일 때마다 찜 목록을 새로 불러온다.
     * (상세 화면에서 찜을 추가/해제하고 돌아오면 그 변화가 바로 반영되도록 onResume 에서 갱신)
     */
    override fun onResume() {
        super.onResume()
        val favs = FavoriteManager.getFavorites(this)                       // 찜한 라면 '이름' 집합
        val list = RamenData.list.filter { favs.contains(it.name) }         // 전체 중 찜한 것만 추림

        // 찜한 게 없으면 안내 문구를 보이고, 있으면 숨긴다
        binding.tvEmpty.visibility = if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        // 어댑터 연결 + 카드 클릭 시 상세로 이동(이름 전달)
        binding.recyclerView.adapter = RamenAdapter(list) { ramen ->
            val intent = Intent(this, RamenDetailActivity::class.java)
            intent.putExtra("name", ramen.name)
            startActivity(intent)
        }
        binding.bottomNav.selectedItemId = R.id.nav_fav                     // 하단 탭에서 '찜' 선택 표시
    }

    /**
     * 하단 탭바 동작 연결. 이 화면은 '찜' 탭이므로 다른 탭을 누르면 그 화면으로 이동.
     */
    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { goTo(MainActivity::class.java); true }
                R.id.nav_dex -> { goTo(RamenListActivity::class.java); true }
                R.id.nav_recipe -> { goTo(RecipeListActivity::class.java); true } // 레시피 게시판으로 이동(신규)
                R.id.nav_my -> { goTo(MyPageActivity::class.java); true }
                else -> true
            }
        }
    }

    /** 다른 화면으로 이동(이미 떠 있으면 앞으로 끌어와 중복 생성 방지) */
    private fun goTo(cls: Class<*>) {
        val i = Intent(this, cls)
        i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)
    }

    // 툴바의 '←' 뒤로가기 화살표를 눌렀을 때: 이 화면을 닫고 이전 화면으로 돌아간다
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
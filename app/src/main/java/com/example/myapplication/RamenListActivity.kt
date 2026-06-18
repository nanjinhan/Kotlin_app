package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.ActivityRamenListBinding

/**
 * RamenListActivity — 라면 도감 목록 화면.
 * 12종의 라면을 2열 그리드 카드로 보여주고, 카드를 누르면 상세 화면으로 이동한다.
 */
class RamenListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRamenListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRamenListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // 메인 카테고리 아이콘에서 넘어온 경우 그 분류로 거른다(없으면 전체).
        val category = intent.getStringExtra("category")
        val data = if (category == null || category == "전체") {
            RamenData.list
        } else {
            RamenData.list.filter { it.matchesCategory(category) }
        }
        // 카테고리로 들어왔으면 제목을 "○○ 라면"으로, 아니면 기본 도감 제목으로
        supportActionBar?.title =
            if (category != null && category != "전체") "$category 라면" else getString(R.string.title_dex)

        // 2열 그리드로 배치(도감 느낌). 가로로 2칸씩 채우며 아래로 쌓인다.
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        // 위에서 고른 data 를 어댑터에 넘기고, 카드를 누르면 상세로 이동
        binding.recyclerView.adapter = RamenAdapter(data) { ramen ->
            // 상세 화면으로 라면 '이름'을 Intent 에 담아 전달(받는 쪽에서 그 이름으로 데이터를 찾음)
            val intent = Intent(this, RamenDetailActivity::class.java)
            intent.putExtra("name", ramen.name)
            startActivity(intent)
        }

        setupBottomNav()
    }

    /**
     * 하단 탭바 동작 연결. 이 화면은 '도감' 탭이므로 selectedItemId 로 도감을 선택 상태로 두고,
     * 다른 탭을 누르면 그 화면으로 이동한다.
     */
    private fun setupBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_dex
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { goTo(MainActivity::class.java); true }
                R.id.nav_recipe -> { goTo(RecipeListActivity::class.java); true } // 레시피 게시판으로 이동(신규)
                R.id.nav_fav -> { goTo(FavoriteActivity::class.java); true }
                R.id.nav_my -> { goTo(MyPageActivity::class.java); true }
                else -> true
            }
        }
    }

    /**
     * 다른 화면으로 이동하는 공통 함수.
     * REORDER_TO_FRONT + SINGLE_TOP 플래그로 이미 떠 있는 화면이면 새로 만들지 않고 앞으로 끌어온다
     * → 탭을 오갈 때 화면이 중복으로 쌓이는 것을 방지.
     */
    private fun goTo(cls: Class<*>) {
        val i = Intent(this, cls)
        i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)
    }
}
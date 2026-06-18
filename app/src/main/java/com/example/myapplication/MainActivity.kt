package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // 드로어 토글 (햄버거 ≡)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setupDrawer()

        // 오늘의 추천 배너: 별점 높은 라면 중 하나를 보여줌
        val today = RamenData.list.maxByOrNull { it.rating } ?: RamenData.list[0]
        binding.tvBannerName.text = today.name
        binding.tvBannerDesc.text = today.description
        val bannerRes = resources.getIdentifier(today.imageName, "drawable", packageName)
        if (bannerRes != 0) binding.ivBanner.setImageResource(bannerRes)
        binding.bannerBox.setOnClickListener { openDetail(today.name) }

        // 인기 라면 가로 스크롤 (별점 상위 6개)
        val popular = RamenData.list.sortedByDescending { it.rating }.take(6)
        binding.popularRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.popularRecycler.adapter = PopularAdapter(popular) { openDetail(it.name) }

        // "전체 보기" → 도감으로
        binding.tvSeeAll.setOnClickListener { startActivity(Intent(this, RamenListActivity::class.java)) }

        // 카테고리 클릭 → 도감으로 이동 (간단히 전체 도감 열기)
        val cats = listOf(binding.cat1, binding.cat2, binding.cat3, binding.cat4, binding.cat5)
        cats.forEach { it.setOnClickListener { startActivity(Intent(this, RamenListActivity::class.java)) } }

        setupBottomNav()
    }

    private fun openDetail(name: String) {
        val i = Intent(this, RamenDetailActivity::class.java)
        i.putExtra("name", name)
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("ramen_dex", MODE_PRIVATE)
        val last = prefs.getString("last_ramen", null)
        binding.tvLast.text = if (last != null) "마지막으로 본 라면: $last" else "아직 본 라면이 없어요"
        binding.bottomNav.selectedItemId = R.id.nav_home
    }

    // 드로어 메뉴 클릭 처리
    private fun setupDrawer() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.d_dex -> startActivity(Intent(this, RamenListActivity::class.java))
                R.id.d_recipe -> startActivity(Intent(this, RecipeListActivity::class.java))
                R.id.d_fav -> startActivity(Intent(this, FavoriteActivity::class.java))
                R.id.d_guide -> showGuide()   // 맵기 색 안내
                R.id.d_info -> showInfo()      // 앱 정보
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    // 맵기 색 안내 다이얼로그
    private fun showGuide() {
        AlertDialog.Builder(this)
            .setTitle("맵기 색 안내")
            .setMessage("도감 카드의 테두리 색으로 맵기를 표현합니다.\n\n🟢 초록 : 순한맛\n🟡 라임 : 약간 매움\n🟠 주황 : 보통\n🔴 빨강 : 매움\n🔴 진빨강 : 아주 매움")
            .setPositiveButton("확인", null)
            .show()
    }

    private fun showInfo() {
        AlertDialog.Builder(this)
            .setTitle("앱 정보")
            .setMessage("라면도감 v1.0\n맛있는 라면을 한눈에 보는 도감 앱\n\n제작: 박진한 (224543)")
            .setPositiveButton("확인", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu); return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> { showInfo(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dex -> { startActivity(Intent(this, RamenListActivity::class.java)); true }
                R.id.nav_recipe -> { startActivity(Intent(this, RecipeListActivity::class.java)); true }
                R.id.nav_fav -> { startActivity(Intent(this, FavoriteActivity::class.java)); true }
                R.id.nav_my -> { startActivity(Intent(this, MyPageActivity::class.java)); true }
                else -> true
            }
        }
    }
}
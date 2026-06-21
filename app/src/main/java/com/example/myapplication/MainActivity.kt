package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

        // 오늘의 추천 배너는 onResume()에서 매번 새로 구성한다(아래 setupBanner 참고).
        // → 홈 화면에 들어오거나 다른 화면에서 돌아올 때마다 추천 레시피가 랜덤으로 바뀐다.

        // 인기 라면 가로 스크롤 (별점 상위 6개)
        val popular = RamenData.list.sortedByDescending { it.rating }.take(6)
        binding.popularRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.popularRecycler.adapter = PopularAdapter(popular) { openDetail(it.name) }

        // "전체 보기" → 도감으로
        binding.tvSeeAll.setOnClickListener { startActivity(Intent(this, RamenListActivity::class.java)) }

        // 카테고리 클릭 → 해당 분류로 거른 도감 열기 (아이콘 순서와 동일하게 매핑)
        val cats = listOf(
            binding.cat1 to "매운맛",
            binding.cat2 to "치즈",
            binding.cat3 to "해물",
            binding.cat4 to "계란",
            binding.cat5 to "전체"
        )
        cats.forEach { (view, category) ->
            view.setOnClickListener { openCategory(category) }
        }

        setupBottomNav()
    }

    // 오늘의 추천 배너 구성: 사진이 있는 레시피 중 "오늘 날짜" 기준으로 하나를 골라 보여준다.
    private fun setupBanner() {
        RecipeManager.seedIfFirst(this)   // 레시피가 비어 있지 않도록 보장(첫 실행 대비)
        // drawable 에 실제 사진이 있는 레시피만 후보로 추림
        val withImage = RecipeManager.getAll(this).filter {
            it.imageName.isNotEmpty() &&
                resources.getIdentifier(it.imageName, "drawable", packageName) != 0
        }
        if (withImage.isNotEmpty()) {
            // 사진이 있는 레시피 중 하나를 무작위로 고른다.
            // (이전에는 '날짜' 기준이라 하루 종일 같은 레시피(계란탱 신라면)만 떴음
            //  → 이제 홈에 들어올 때마다 랜덤으로 바뀐다)
            val pick = withImage.random()
            // 별점·추천수는 그 레시피가 다루는 라면의 데이터에서 가져온다
            val ramen = RamenData.list.firstOrNull { it.name == pick.ramenName }
            bindBanner(
                title = pick.title,                                   // 레시피 이름 (예: 계란탱 신라면)
                intro = pick.tip.ifEmpty { "#${pick.ramenName} 레시피" }, // 한 줄 소개
                rating = ramen?.rating ?: 4.7,
                count = ramen?.recommendCount ?: 1500,
                imageName = pick.imageName
            )
            binding.bannerBox.setOnClickListener { openRecipe(pick.id) }
        } else {
            // 사진 있는 레시피가 하나도 없으면 별점 1위 라면으로 대체(안전장치)
            val today = RamenData.list.maxByOrNull { it.rating } ?: RamenData.list[0]
            bindBanner(today.name, today.description, today.rating, today.recommendCount, today.imageName)
            binding.bannerBox.setOnClickListener { openDetail(today.name) }
        }
    }

    // 배너의 텍스트/별점/추천수/이미지를 한 번에 채운다
    private fun bindBanner(title: String, intro: String, rating: Double, count: Int, imageName: String) {
        binding.tvBannerName.text = title
        binding.tvBannerDesc.text = intro
        binding.tvBannerRating.text = "★ $rating"
        // "추천 1,500" 형태(끝의 "명"은 뺐다). 글자 수를 줄여 🔥인기·★별점·추천수가 한 줄에 모두 들어가게 함.
        binding.tvBannerCount.text = "추천 ${"%,d".format(count)}"
        val res = resources.getIdentifier(imageName, "drawable", packageName)
        if (res != 0) binding.ivBanner.setImageResource(res) else binding.ivBanner.setImageDrawable(null)
    }

    private fun openDetail(name: String) {
        val i = Intent(this, RamenDetailActivity::class.java)
        i.putExtra("name", name)
        startActivity(i)
    }

    // 배너 클릭 → 그 레시피 상세로 이동
    private fun openRecipe(id: Long) {
        val i = Intent(this, RecipeDetailActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }

    // 카테고리 아이콘 → 그 분류로 거른 도감 목록 화면 열기
    private fun openCategory(category: String) {
        val i = Intent(this, RamenListActivity::class.java)
        i.putExtra("category", category)
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("ramen_dex", MODE_PRIVATE)
        val last = prefs.getString("last_ramen", null)
        binding.tvLast.text = if (last != null) "마지막으로 본 라면: $last" else "아직 본 라면이 없어요"
        binding.bottomNav.selectedItemId = R.id.nav_home
        // 홈에 들어올 때마다 오늘의 추천 배너를 새 레시피로 갱신(랜덤)
        setupBanner()
    }

    // 드로어 메뉴 클릭 처리
    private fun setupDrawer() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.d_dex -> startActivity(Intent(this, RamenListActivity::class.java))
                R.id.d_recipe -> startActivity(Intent(this, RecipeListActivity::class.java))
                R.id.d_fav -> startActivity(Intent(this, FavoriteActivity::class.java))
                R.id.d_guide -> showGuide()          // 맵기 색 안내
                R.id.d_lang -> showLanguageDialog()  // 언어 선택(데모용 — 실제 전환은 안 함)
                R.id.d_info -> showInfo()            // 앱 정보
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

    /**
     * 언어 선택 다이얼로그(데모용).
     *
     * ▸ 실제로 앱의 언어(로케일)를 바꾸지는 않는다. "외국인도 쓸 수 있어 보이게" 하는 형식상 기능이다.
     * ▸ 목록에서 언어를 고르면 토스트로 "선택됨"만 안내한다.
     *   (진짜 다국어 지원을 하려면 res/values-en, values-ja 등 문자열 리소스와
     *    AppCompatDelegate.setApplicationLocales() 적용이 필요 — 추후 확장 포인트)
     */
    private fun showLanguageDialog() {
        // 화면에 보여줄 언어 목록(표시용 라벨)
        val languages = arrayOf("한국어", "English", "日本語", "中文")
        AlertDialog.Builder(this)
            .setTitle("언어 / Language")
            .setItems(languages) { _, which ->
                // which = 사용자가 고른 항목의 인덱스
                Toast.makeText(
                    this,
                    "${languages[which]} 선택됨 (데모 — 실제 전환은 추후 지원)",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("닫기", null)
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
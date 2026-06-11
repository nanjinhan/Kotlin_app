package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

/**
 * MainActivity — 앱의 홈(첫 화면).
 *
 * 들어 있는 요소(과제 평가 포인트):
 *   - 드로어(서랍) 메뉴: 햄버거 아이콘으로 좌측 서랍을 열어 도감/찜으로 이동
 *   - 하단 탭바(BottomNavigation): 홈/도감/레시피/찜 이동
 *   - 옵션 메뉴(우측 상단 ⋮): "앱 정보" 다이얼로그
 *   - "마지막으로 본 라면"을 SharedPreferences 에서 읽어 표시
 */
class MainActivity : AppCompatActivity() {
    // 뷰바인딩: activity_main.xml 의 뷰들을 binding.아이디 로 안전하게 접근(findViewById 불필요)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)   // 레이아웃을 메모리에 부풀림
        setContentView(binding.root)                            // 그 화면을 실제로 표시
        setSupportActionBar(binding.toolbar)                    // 상단 툴바를 액션바로 등록

        // 드로어 토글: 툴바의 햄버거(≡) 아이콘과 좌측 서랍(DrawerLayout)을 연결한다.
        // 아이콘을 누르면 서랍이 열리고, 열림/닫힘 상태를 toggle 이 자동으로 동기화한다.
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 서랍 안 메뉴 항목을 누르면 해당 화면으로 이동한 뒤 서랍을 닫는다
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.d_dex -> startActivity(Intent(this, RamenListActivity::class.java))
                R.id.d_fav -> startActivity(Intent(this, FavoriteActivity::class.java))
            }
            binding.drawerLayout.closeDrawers()     // 이동 후 서랍 닫기
            true
        }

        // "라면 도감 열기" 큰 버튼 → 도감 목록 화면으로 이동
        binding.btnOpenDex.setOnClickListener {
            startActivity(Intent(this, RamenListActivity::class.java))
        }

        // 하단 탭바: 지금은 홈 화면이므로 '홈'을 선택 상태로 두고,
        // 다른 탭을 누르면 그 화면으로 이동한다. (각 분기는 true 를 반환해 "선택됨" 처리)
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dex -> { startActivity(Intent(this, RamenListActivity::class.java)); true }
                R.id.nav_recipe -> { startActivity(Intent(this, RecipeListActivity::class.java)); true } // 레시피 게시판으로 이동(신규)
                R.id.nav_fav -> { startActivity(Intent(this, FavoriteActivity::class.java)); true }
                else -> true
            }
        }
    }

    /**
     * 화면이 다시 보일 때마다 "마지막으로 본 라면"을 갱신한다.
     * 상세 화면에서 라면을 보면 그 이름을 SharedPreferences("last_ramen")에 저장해 두는데,
     * 홈으로 돌아올 때 그 값을 읽어 안내 문구로 보여준다.
     */
    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("ramen_dex", MODE_PRIVATE)
        val last = prefs.getString("last_ramen", null)
        binding.tvLast.text = if (last != null) "마지막으로 본 라면: $last" else "아직 본 라면이 없어요"
    }

    // 우측 상단 옵션 메뉴(⋮)를 만든다 (option_menu.xml 사용)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu); return true
    }

    // 옵션 메뉴 항목을 눌렀을 때의 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                // "앱 정보" → 확인 버튼만 있는 간단한 안내 대화상자(AlertDialog) 표시
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
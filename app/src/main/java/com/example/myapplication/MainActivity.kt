package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

// 앱의 중심 화면. 환영 문구, 툴바, 내비게이션 드로어, 옵션 메뉴를 담당
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 우리가 만든 Toolbar를 액션바로 등록 (14장)
        setSupportActionBar(binding.toolbar)

        // 드로어 토글: 툴바의 햄버거(≡) 아이콘으로 서랍 메뉴를 열고 닫는다 (가산점)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 드로어 메뉴 항목 클릭 처리
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_book_list ->
                    startActivity(Intent(this, BookListActivity::class.java))
                R.id.nav_home ->
                    Toast.makeText(this, "현재 홈 화면입니다", Toast.LENGTH_SHORT).show()
            }
            binding.drawerLayout.closeDrawers() // 선택 후 서랍 닫기
            true
        }

        // "도서 목록 보기" 버튼 → 목록 화면으로 이동 (8장 클릭 이벤트 + 13장 인텐트)
        binding.btnBookList.setOnClickListener {
            startActivity(Intent(this, BookListActivity::class.java))
        }
    }

    // 화면이 다시 보일 때마다 "마지막으로 본 책"을 갱신해서 표시 (SharedPreferences 가산점)
    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("buildbook", MODE_PRIVATE)
        val last = prefs.getString("last_book", null)
        binding.tvLastBook.text =
            if (last != null) "마지막으로 본 책: $last" else "아직 둘러본 책이 없어요"
    }

    // 옵션 메뉴 XML을 툴바에 올림 (필수 ⑤)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    // 옵션 메뉴 항목 클릭 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                Toast.makeText(this, "홈 화면입니다", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_info -> {
                // 앱 정보 대화상자 (AlertDialog 가산점, 15장)
                AlertDialog.Builder(this)
                    .setTitle("앱 정보")
                    .setMessage("빌드북 v1.0\n개발자와 메이커를 위한 IT 전문 서점\n\n제작: 박진한 (224543)")
                    .setPositiveButton("확인", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
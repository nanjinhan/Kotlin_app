package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityBookDetailBinding

// 목록에서 선택한 도서 한 권의 상세 정보를 보여 주는 화면
class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 이전 화면(목록)에서 Intent로 전달받은 도서 정보를 꺼낸다 (13장 엑스트라 받기)
        // ?: 는 값이 없을 때 기본값을 주는 null 안전 처리 (코드 품질)
        val title = intent.getStringExtra("title") ?: ""
        val author = intent.getStringExtra("author") ?: ""
        val price = intent.getIntExtra("price", 0)
        val pubDate = intent.getStringExtra("pubDate") ?: ""
        val coverColor = intent.getStringExtra("coverColor") ?: "#2E5D4B"
        val description = intent.getStringExtra("description") ?: ""

        supportActionBar?.title = title

        // 받은 데이터를 화면 각 뷰에 표시 (문자열 템플릿 활용)
        binding.tvCover.text = title
        binding.tvCover.setBackgroundColor(Color.parseColor(coverColor))
        binding.tvTitle.text = title
        binding.tvAuthor.text = "저자: $author"
        binding.tvPrice.text = "%,d원".format(price)
        binding.tvPubDate.text = "출판일: $pubDate"
        binding.tvDescription.text = description

        // 방금 본 책을 저장해 둔다 → 메인 화면에서 "마지막으로 본 책"으로 표시 (가산점)
        val prefs = getSharedPreferences("buildbook", MODE_PRIVATE)
        prefs.edit().putString("last_book", title).apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
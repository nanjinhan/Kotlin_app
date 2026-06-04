package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityBookListBinding

// 도서들을 RecyclerView 카드 목록으로 보여 주고, 카드를 누르면 상세로 이동시키는 화면
class BookListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "도서 목록"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // 툴바 왼쪽 뒤로가기(←) 화살표

        // RecyclerView를 세로 목록으로 배치하고 어댑터를 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = BookAdapter(BookData.books) { book ->
            // 카드 클릭 → 상세 화면으로 이동하며 도서 정보를 Intent에 담아 전달 (13장 엑스트라)
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra("title", book.title)
            intent.putExtra("author", book.author)
            intent.putExtra("price", book.price)
            intent.putExtra("pubDate", book.pubDate)
            intent.putExtra("coverColor", book.coverColor)
            intent.putExtra("description", book.description)
            startActivity(intent)
        }
    }

    // 툴바 뒤로가기 화살표를 누르면 이전 화면으로 돌아감
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
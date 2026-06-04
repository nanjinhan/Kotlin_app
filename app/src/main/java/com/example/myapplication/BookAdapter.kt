package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemBookBinding

// 도서 목록 데이터를 화면의 카드 하나하나에 연결해 주는 다리 역할 (13장 어댑터)
class BookAdapter(
    private val books: List<Book>,
    private val onClick: (Book) -> Unit  // 카드 클릭 시 실행할 동작을 밖에서 전달받음
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // ViewHolder: 카드 한 장의 뷰들을 담아 두는 그릇 (재활용해서 스크롤 성능 향상)
    inner class BookViewHolder(val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    // 카드 레이아웃(item_book.xml)을 메모리에 올려 ViewHolder를 만든다
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    // 실제 데이터(book)를 카드의 각 뷰에 채워 넣는다
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.binding.tvTitle.text = book.title
        holder.binding.tvAuthor.text = book.author
        // 가격에 천 단위 콤마 + "원" (문자열 템플릿/포맷 활용 → 코드 품질)
        holder.binding.tvPrice.text = "%,d원".format(book.price)
        // 표지: 제목을 표지 색 배경 위에 표시 (이미지 파일 없이 표지 표현)
        holder.binding.tvCover.text = book.title
        holder.binding.tvCover.setBackgroundColor(Color.parseColor(book.coverColor))

        // 카드의 일부가 아닌 '전체'에 클릭을 걸어 어디를 눌러도 상세로 이동 (터치 영역 일치 UX)
        holder.binding.cardRoot.setOnClickListener { onClick(book) }
    }

    override fun getItemCount(): Int = books.size
}
package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemPopularBinding

// 메인 화면 "인기 라면" 가로 스크롤 카드 어댑터
class
PopularAdapter(
    private val items: List<Ramen>,
    private val onClick: (Ramen) -> Unit
) : RecyclerView.Adapter<PopularAdapter.VH>() {

    inner class VH(val binding: ItemPopularBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        val ctx = holder.itemView.context
        holder.binding.tvName.text = r.name
        holder.binding.tvDesc.text = r.description
        holder.binding.tvRating.text = "★ ${r.rating}"
        holder.binding.tvCount.text = "${r.recommendCount}명"

        // 맵기 칩: 매운맛/순한맛에 따라 배경·글자색 다르게
        holder.binding.tvSpicyChip.text = r.spicyLabel()
        if (r.isSpicy()) {
            holder.binding.tvSpicyChip.setBackgroundResource(R.drawable.chip_spicy)
            holder.binding.tvSpicyChip.setTextColor(Color.parseColor("#E23B2E"))
        } else {
            holder.binding.tvSpicyChip.setBackgroundResource(R.drawable.chip_mild)
            holder.binding.tvSpicyChip.setTextColor(Color.parseColor("#5B8C2A"))
        }

        // 이미지: drawable에 있으면 표시, 없으면 연회색
        val resId = ctx.resources.getIdentifier(r.imageName, "drawable", ctx.packageName)
        if (resId != 0) {
            holder.binding.ivImage.setImageResource(resId)
            holder.binding.ivImage.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.binding.ivImage.setImageDrawable(null)
            holder.binding.ivImage.setBackgroundColor(Color.parseColor("#F2F2F2"))
        }

        holder.binding.cardRoot.setOnClickListener { onClick(r) }
    }

    override fun getItemCount() = items.size
}
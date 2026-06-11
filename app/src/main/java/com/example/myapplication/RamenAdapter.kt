package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRamenBinding

// 라면 목록을 도감 카드로 그려주는 어댑터
class RamenAdapter(
    private val items: List<Ramen>,
    private val onClick: (Ramen) -> Unit
) : RecyclerView.Adapter<RamenAdapter.VH>() {

    inner class VH(val binding: ItemRamenBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRamenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ramen = items[position]
        val ctx = holder.itemView.context

        holder.binding.tvName.text = ramen.name
        holder.binding.tvSub.text = "${ramen.maker} · 맵기 ${ramen.spicy}"

        // 맵기 단계에 따라 카드 테두리 색을 바꿈 (한눈에 매운 정도 파악 — 도감 핵심 UX)
        holder.binding.cardRoot.strokeColor = Color.parseColor(RamenData.spicyColor(ramen.spicy))

        // 이미지: drawable에 같은 이름의 파일이 있으면 표시, 없으면 맵기 색 배경으로 대체
        val resId = ctx.resources.getIdentifier(ramen.imageName, "drawable", ctx.packageName)
        if (resId != 0) {
            holder.binding.ivImage.setImageResource(resId)
            holder.binding.ivImage.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.binding.ivImage.setImageDrawable(null)
            holder.binding.ivImage.setBackgroundColor(Color.parseColor(RamenData.spicyColor(ramen.spicy)))
        }

        // 카드 전체에 클릭 (어디를 눌러도 상세로 이동)
        holder.binding.cardRoot.setOnClickListener { onClick(ramen) }
    }

    override fun getItemCount() = items.size
}
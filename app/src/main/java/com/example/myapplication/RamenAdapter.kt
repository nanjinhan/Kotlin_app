package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRamenBinding

/**
 * RamenAdapter — 라면 목록(RecyclerView)과 데이터(List<Ramen>)를 이어주는 다리.
 * 도감 화면과 찜 화면이 같은 어댑터를 함께 사용한다(보여줄 목록만 다르게 넘김 → 재사용 ↑).
 *
 * @param items   화면에 표시할 라면 목록
 * @param onClick 카드를 눌렀을 때 실행할 동작(어떤 동작인지는 호출하는 화면이 정함)
 */
class RamenAdapter(
    private val items: List<Ramen>,
    private val onClick: (Ramen) -> Unit
) : RecyclerView.Adapter<RamenAdapter.VH>() {

    /** 카드 1개의 뷰를 들고 있는 뷰홀더(ItemRamenBinding = item_ramen.xml 의 뷰바인딩) */
    inner class VH(val binding: ItemRamenBinding) : RecyclerView.ViewHolder(binding.root)

    /** 새 카드가 필요할 때: item_ramen.xml 을 부풀려 뷰홀더 생성 */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRamenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    /** position 번째 라면 데이터를 그 칸의 카드에 채워 넣음(스크롤 시 재활용되며 반복 호출) */
    override fun onBindViewHolder(holder: VH, position: Int) {
        val ramen = items[position]
        val ctx = holder.itemView.context

        holder.binding.tvName.text = ramen.name
        holder.binding.tvSub.text = "${ramen.maker} · 맵기 ${ramen.spicy}"

        // 맵기 단계에 따라 카드 테두리 색을 바꾼다 → 한눈에 매운 정도를 구분(도감 핵심 UX)
        holder.binding.cardRoot.strokeColor = Color.parseColor(RamenData.spicyColor(ramen.spicy))

        // 썸네일: drawable 에 imageName 과 같은 이름의 파일이 있으면 표시,
        //         없으면(=0) 맵기 색을 배경으로 채워 빈칸처럼 보이지 않게 한다.
        val resId = ctx.resources.getIdentifier(ramen.imageName, "drawable", ctx.packageName)
        if (resId != 0) {
            holder.binding.ivImage.setImageResource(resId)
            holder.binding.ivImage.setBackgroundColor(Color.TRANSPARENT)
        } else {
            holder.binding.ivImage.setImageDrawable(null)
            holder.binding.ivImage.setBackgroundColor(Color.parseColor(RamenData.spicyColor(ramen.spicy)))
        }

        // 카드 전체에 클릭 연결 → 어디를 눌러도 상세로 이동(실제 동작은 onClick 으로 위임)
        holder.binding.cardRoot.setOnClickListener { onClick(ramen) }
    }

    /** 그릴 카드 개수 = 목록 크기 */
    override fun getItemCount() = items.size
}
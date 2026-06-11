package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRecipeBinding

/**
 * RecipeAdapter — 레시피 "목록(RecyclerView)"과 "데이터(List<Recipe>)"를 이어주는 다리.
 *
 * ▸ RecyclerView 는 화면에 보이는 만큼의 카드만 만들어 재활용(recycle)하기 때문에
 *   수백 개 글이 있어도 부드럽게 스크롤된다. 어댑터는 그 각 칸에 어떤 데이터를 그릴지 알려준다.
 *
 * ▸ 생성자 매개변수
 *   - items   : 화면에 보여줄 레시피 목록
 *   - onClick : 카드를 눌렀을 때 실행할 동작(람다). "무엇을 할지"는 호출하는 화면이 정한다.
 *               → 어댑터는 클릭을 감지만 하고 처리는 위임 → 재사용성 ↑ (코드 품질)
 */
class RecipeAdapter(
    private val items: List<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.VH>() {

    /**
     * VH(ViewHolder) — 카드 1개의 뷰(아이템 레이아웃)를 들고 있는 그릇.
     * ItemRecipeBinding 은 item_recipe.xml 을 코드에서 안전하게 다루게 해주는 뷰바인딩 객체.
     */
    inner class VH(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * 새 카드(뷰홀더)가 필요할 때 호출. item_recipe.xml 을 부풀려(inflate) 뷰홀더를 만든다.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    /**
     * position 번째 데이터를 그 위치의 카드에 채워 넣을 때 호출(스크롤할 때마다 재활용되며 불림).
     */
    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]                 // 이 칸에 그릴 레시피
        val ctx = holder.itemView.context

        // 텍스트 채우기
        holder.binding.tvTitle.text = r.title           // 제목
        holder.binding.tvRamen.text = "#${r.ramenName}"  // 어떤 라면인지 (# 해시태그처럼 표시)
        holder.binding.tvAuthor.text = "by ${r.author}"  // 작성자

        // 썸네일 이미지 처리:
        // imageName 이 비어있지 않으면 같은 이름의 drawable 리소스 id 를 찾는다. 없으면 0.
        val resId = if (r.imageName.isNotEmpty())
            ctx.resources.getIdentifier(r.imageName, "drawable", ctx.packageName) else 0
        if (resId != 0) {
            // 이미지 파일이 실제로 있으면 그 이미지를 표시하고 배경색은 투명 처리
            holder.binding.ivThumb.setImageResource(resId)
            holder.binding.ivThumb.setBackgroundColor(Color.TRANSPARENT)
        } else {
            // 이미지가 없으면 이미지를 비우고 연회색(#F2F2F2) 배경으로 대체 → 빈칸처럼 안 보이게(주황 면적 축소)
            holder.binding.ivThumb.setImageDrawable(null)
            holder.binding.ivThumb.setBackgroundColor(Color.parseColor("#F2F2F2"))
        }

        // 카드 전체에 클릭 연결 → 어디를 눌러도 상세로 이동(실제 동작은 onClick 으로 위임)
        holder.binding.cardRoot.setOnClickListener { onClick(r) }
    }

    /** 목록에 그릴 카드 개수 = 데이터 개수 */
    override fun getItemCount() = items.size
}
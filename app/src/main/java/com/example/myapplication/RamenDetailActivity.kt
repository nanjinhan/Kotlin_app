package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRamenDetailBinding

/**
 * RamenDetailActivity — 라면 한 개의 상세 정보 화면.
 *
 * 흐름: 목록에서 카드를 누르면 라면 '이름'이 Intent 로 넘어온다
 *       → 그 이름으로 RamenData 에서 원본 데이터를 찾아 화면에 채운다
 *       → 찜하기(하트) 토글, "마지막으로 본 라면" 저장도 여기서 처리.
 */
class RamenDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRamenDetailBinding
    private lateinit var ramen: Ramen       // 지금 보고 있는 라면

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRamenDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // 툴바에 '←' 뒤로가기 표시

        // 목록에서 받은 이름으로 해당 라면을 찾는다. 없으면 안전하게 첫 번째 라면으로 대체.
        val name = intent.getStringExtra("name") ?: ""
        ramen = RamenData.list.find { it.name == name } ?: RamenData.list[0]
        supportActionBar?.title = ramen.name

        // 대표 이미지: drawable 에 파일이 있으면 표시, 없으면 맵기 색 배경으로 대체
        val resId = resources.getIdentifier(ramen.imageName, "drawable", packageName)
        if (resId != 0) {
            binding.ivImage.setImageResource(resId)
            binding.ivImage.setBackgroundColor(Color.TRANSPARENT)
        } else {
            binding.ivImage.setBackgroundColor(Color.parseColor(RamenData.spicyColor(ramen.spicy)))
        }

        // 라면 정보를 각 텍스트뷰에 채운다
        binding.tvName.text = ramen.name
        binding.tvMeta.text = "${ramen.maker} · ${ramen.category} · ${ramen.packageType}"
        // 맵기를 고추(🌶) 이모지 개수로 표시(최소 1개는 보이게 coerceAtLeast(1))
        binding.tvSpicy.text = "맵기 " + "🌶".repeat(ramen.spicy.coerceAtLeast(1))
        binding.tvRating.text = "★ ${ramen.rating}"
        binding.tvPrice.text = ramen.price
        binding.tvTags.text = ramen.tags
        binding.tvCook.text = ramen.cook
        binding.tvFeature.text = ramen.feature
        binding.tvRecommend.text = ramen.recommend
        binding.tvStore.text =
            "편의점 ${if (ramen.convenience) "O" else "X"}   ·   마트 ${if (ramen.mart) "O" else "X"}"

        // 찜 하트의 초기 상태를 현재 찜 여부에 맞춰 표시
        updateHeart(FavoriteManager.isFavorite(this, ramen.name))
        // 하트 버튼을 누르면 찜을 토글하고, 아이콘/문구를 갱신한 뒤 안내 토스트를 띄운다
        binding.btnFav.setOnClickListener {
            val nowFav = FavoriteManager.toggle(this, ramen.name)
            updateHeart(nowFav)
            Toast.makeText(this, if (nowFav) "찜했어요!" else "찜을 해제했어요", Toast.LENGTH_SHORT).show()
        }

        // "마지막으로 본 라면"으로 이 라면 이름을 저장 → 홈 화면이 이 값을 읽어 보여줌
        getSharedPreferences("ramen_dex", MODE_PRIVATE)
            .edit().putString("last_ramen", ramen.name).apply()
    }

    /** 찜 여부에 따라 하트 아이콘(채움/빈)과 글자("찜 완료"/"찜하기")를 바꾼다 */
    private fun updateHeart(fav: Boolean) {
        binding.ivHeart.setImageResource(
            if (fav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        )
        binding.tvFav.text = if (fav) "찜 완료" else "찜하기"
    }

    // 툴바의 '←' 뒤로가기: 화면 닫기
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
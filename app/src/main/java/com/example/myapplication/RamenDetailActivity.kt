package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRamenDetailBinding

class RamenDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRamenDetailBinding
    private lateinit var ramen: Ramen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRamenDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 목록에서 받은 이름으로 해당 라면 데이터를 찾는다 (Intent 엑스트라 받기)
        val name = intent.getStringExtra("name") ?: ""
        ramen = RamenData.list.find { it.name == name } ?: RamenData.list[0]
        supportActionBar?.title = ramen.name

        // 이미지: drawable에 파일 있으면 표시, 없으면 맵기 색으로 대체
        val resId = resources.getIdentifier(ramen.imageName, "drawable", packageName)
        if (resId != 0) {
            binding.ivImage.setImageResource(resId)
            binding.ivImage.setBackgroundColor(Color.TRANSPARENT)
        } else {
            binding.ivImage.setBackgroundColor(Color.parseColor(RamenData.spicyColor(ramen.spicy)))
        }

        binding.tvName.text = ramen.name
        binding.tvMeta.text = "${ramen.maker} · ${ramen.category} · ${ramen.packageType}"
        // 맵기를 고추(🌶) 개수로 표시
        binding.tvSpicy.text = "맵기 " + "🌶".repeat(ramen.spicy.coerceAtLeast(1))
        binding.tvRating.text = "★ ${ramen.rating}"
        binding.tvPrice.text = ramen.price
        binding.tvTags.text = ramen.tags
        binding.tvCook.text = ramen.cook
        binding.tvFeature.text = ramen.feature
        binding.tvRecommend.text = ramen.recommend
        binding.tvStore.text =
            "편의점 ${if (ramen.convenience) "O" else "X"}   ·   마트 ${if (ramen.mart) "O" else "X"}"

        // 찜 하트 초기 상태
        updateHeart(FavoriteManager.isFavorite(this, ramen.name))
        binding.btnFav.setOnClickListener {
            val nowFav = FavoriteManager.toggle(this, ramen.name) // 찜 토글 (SharedPreferences)
            updateHeart(nowFav)
            Toast.makeText(this, if (nowFav) "찜했어요!" else "찜을 해제했어요", Toast.LENGTH_SHORT).show()
        }

        // 마지막으로 본 라면 저장 (홈 화면에 표시됨)
        getSharedPreferences("ramen_dex", MODE_PRIVATE)
            .edit().putString("last_ramen", ramen.name).apply()
    }

    // 찜 여부에 따라 하트 아이콘과 글자를 바꿈
    private fun updateHeart(fav: Boolean) {
        binding.ivHeart.setImageResource(
            if (fav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        )
        binding.tvFav.text = if (fav) "찜 완료" else "찜하기"
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
package com.example.myapplication

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRecipeDetailBinding

/**
 * RecipeDetailActivity — 레시피 한 개의 상세 내용을 보여주는 화면.
 *
 * 흐름: 게시판에서 카드를 누르면 그 글의 id 가 Intent 로 넘어온다
 *       → id 로 원본 Recipe 를 찾아 화면에 채운다
 *       → "레시피 삭제" 버튼으로 확인 후 삭제할 수 있다.
 */
class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private var recipeId: Long = 0      // 지금 보고 있는 레시피의 id (삭제할 때 사용)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // 툴바에 '←' 뒤로가기 표시

        // 게시판에서 넘겨준 id 를 꺼낸다(없으면 기본값 0)
        recipeId = intent.getLongExtra("id", 0)
        // id 로 원본 레시피를 찾는다
        val recipe = RecipeManager.findById(this, recipeId)
        // 혹시 못 찾으면(이미 삭제됨 등) 화면을 바로 닫는다 — 빈 화면 방지(안정성)
        if (recipe == null) { finish(); return }

        // 찾은 레시피 내용을 화면 곳곳에 채운다
        supportActionBar?.title = recipe.title
        binding.tvTitle.text = recipe.title
        binding.tvRamen.text = "#${recipe.ramenName}"
        binding.tvAuthor.text = "by ${recipe.author}"
        // 내용이 비어 있으면 "-" 로 대체해 빈칸처럼 보이지 않게(ifEmpty)
        binding.tvIngredients.text = recipe.ingredients.ifEmpty { "-" }
        binding.tvSteps.text = recipe.steps.ifEmpty { "-" }
        binding.tvTip.text = recipe.tip.ifEmpty { "-" }

        // 대표 이미지: imageName 으로 drawable 을 찾아 있으면 표시, 없으면 연한 주황 배경으로 대체
        val resId = if (recipe.imageName.isNotEmpty())
            resources.getIdentifier(recipe.imageName, "drawable", packageName) else 0
        if (resId != 0) binding.ivImage.setImageResource(resId)
        else binding.ivImage.setBackgroundColor(Color.parseColor("#F2F2F2"))

        // 추천 영역 초기화: 현재 추천수와 "이미 추천했는지"를 화면에 반영
        updateRecommendUi()
        // 추천 버튼 클릭 → 추천/취소를 토글하고 화면을 다시 그린다
        binding.btnRecommend.setOnClickListener {
            RecipeManager.toggleRecommend(this, recipeId)
            updateRecommendUi()
        }

        // "레시피 삭제" 버튼: 실수 방지를 위해 확인 대화상자(AlertDialog)를 띄운 뒤 삭제
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("삭제")
                .setMessage("이 레시피를 삭제할까요?")
                .setPositiveButton("삭제") { _, _ ->
                    RecipeManager.delete(this, recipeId)    // 실제 삭제
                    finish()                                // 화면 닫기 → 게시판 목록에서 사라짐
                }
                .setNegativeButton("취소", null)             // 취소는 아무 동작 없이 닫힘
                .show()
        }
    }

    /**
     * 추천 영역(버튼 + 추천수)을 현재 상태에 맞게 다시 그린다.
     * - 추천수: "추천 2,085" 처럼 천 단위 콤마를 찍어 표시
     * - 버튼: 이미 추천했으면 "👍 추천함"(회색), 아직이면 "👍 추천"(주황)
     */
    private fun updateRecommendUi() {
        val count = RecipeManager.getRecommendCount(this, recipeId)
        val done = RecipeManager.hasRecommended(this, recipeId)
        binding.tvRecommendCount.text = "추천 ${"%,d".format(count)}"
        binding.btnRecommend.text = if (done) "👍 추천함" else "👍 추천"
        // 추천한 상태면 회색, 아니면 주황(accent)으로 버튼 배경색을 바꿔 눌렀는지 한눈에 보이게 함
        val tint = if (done) Color.parseColor("#9E9E9E") else Color.parseColor("#FF6B2C")
        binding.btnRecommend.backgroundTintList = ColorStateList.valueOf(tint)
    }

    // 툴바의 '←' 뒤로가기: 화면 닫기
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
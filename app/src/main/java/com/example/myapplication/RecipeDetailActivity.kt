package com.example.myapplication

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

    // 툴바의 '←' 뒤로가기: 화면 닫기
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
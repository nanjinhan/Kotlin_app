package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityRecipeWriteBinding

/**
 * RecipeWriteActivity — 새 레시피를 작성하는 화면(입력 폼).
 *
 * 흐름: 입력칸을 채우고 "레시피 등록" 버튼 → 필수값 검사 → Recipe 객체 생성
 *       → RecipeManager.add() 로 저장 → 화면을 닫으면 게시판(onResume)에서 자동 갱신.
 */
class RecipeWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "레시피 작성"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // 툴바에 '←' 뒤로가기 화살표 표시

        // "레시피 등록" 버튼을 눌렀을 때 실행할 동작
        binding.btnSave.setOnClickListener {
            // 각 입력칸의 글자를 읽어와 앞뒤 공백 제거
            val ramen = binding.etRamen.text.toString().trim()
            val title = binding.etTitle.text.toString().trim()
            val author = binding.etAuthor.text.toString().trim()

            // 필수 항목 검사: 라면 이름과 제목이 비어 있으면 저장하지 않고 안내 후 종료
            // (빈 글이 저장되는 것을 막아 데이터 품질을 지킴)
            if (ramen.isEmpty() || title.isEmpty()) {
                Toast.makeText(this, "라면 이름과 제목은 필수예요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener      // 버튼 람다를 빠져나감(아래 저장 코드 실행 안 함)
            }

            // 입력값으로 Recipe 객체 한 개를 만든다
            val recipe = Recipe(
                id = System.currentTimeMillis(),                 // 현재 시각(밀리초)을 고유 id 로 사용 → 글마다 안 겹침
                ramenName = ramen,
                title = title,
                author = if (author.isEmpty()) "익명" else author, // 작성자를 비우면 "익명"으로 저장
                ingredients = binding.etIngredients.text.toString().trim(),
                steps = binding.etSteps.text.toString().trim(),
                tip = binding.etTip.text.toString().trim(),
                imageName = ""                                   // 이미지는 이번 버전에선 비움(추후 drawable 추가 시 사용)
            )
            RecipeManager.add(this, recipe)                      // 목록 맨 앞에 추가하여 저장
            Toast.makeText(this, "레시피가 등록됐어요!", Toast.LENGTH_SHORT).show()
            finish()                                             // 이 화면을 닫고 게시판으로 복귀 → onResume 에서 목록 갱신됨
        }
    }

    // 툴바의 '←' 뒤로가기 화살표를 눌렀을 때: 그냥 화면을 닫는다
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
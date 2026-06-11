package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityRecipeListBinding

/**
 * RecipeListActivity — 레시피 게시판의 "메인 화면".
 *
 * 담당 기능 4가지:
 *   1) 저장된 레시피 목록을 카드로 보여준다 (RecyclerView + RecipeAdapter)
 *   2) 검색창에 라면 이름을 치면 실시간으로 목록을 걸러준다 (TextWatcher)
 *   3) 인기 검색어 칩을 누르면 그 단어로 바로 검색된다
 *   4) + 버튼(FloatingActionButton)으로 글쓰기 화면으로 이동한다
 */
class RecipeListActivity : AppCompatActivity() {
    // 뷰바인딩: activity_recipe_list.xml 의 뷰들을 코드에서 binding.아이디 로 안전하게 접근
    private lateinit var binding: ActivityRecipeListBinding

    // 화면에 처음 불러온 "전체 레시피". 검색은 이 원본을 걸러서 보여주므로 원본은 그대로 보관
    private var allRecipes = listOf<Recipe>()

    // 인기 검색어 (데모용 고정값. 실제 서비스라면 서버의 검색 통계와 연동될 부분)
    private val popularKeywords = listOf("신라면", "불닭볶음면", "짜파게티")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)        // 상단 툴바를 액션바로 사용
        supportActionBar?.title = "라면 레시피"

        RecipeManager.seedIfFirst(this)             // 첫 실행이면 예시 레시피 4개를 미리 넣어 둠

        // 게시판은 세로로 한 줄씩 쌓이는 리스트 → LinearLayoutManager 사용
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        setupPopular()                              // 인기 검색어 버튼 3개 세팅

        // 검색창에 글자가 바뀔 때마다 호출되는 감시자(TextWatcher)를 붙인다 → 실시간 필터(자동완성식)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            // 글자가 다 바뀐 뒤 호출 → 현재 입력값으로 목록을 거른다
            override fun afterTextChanged(s: Editable?) { applyFilter(s?.toString() ?: "") }
            // 아래 두 개는 인터페이스상 꼭 있어야 하지만 이번엔 쓰지 않음(비워 둠)
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })

        // + 버튼 → 레시피 작성 화면으로 이동
        binding.fabWrite.setOnClickListener {
            startActivity(Intent(this, RecipeWriteActivity::class.java))
        }

        setupBottomNav()                            // 하단 탭바 세팅
    }

    /**
     * 화면이 다시 보일 때마다 호출(다른 화면 갔다 돌아올 때 포함).
     * 글을 새로 쓰거나 삭제하고 돌아오면 목록이 바뀌었을 수 있으므로 여기서 다시 불러와 갱신한다.
     */
    override fun onResume() {
        super.onResume()
        allRecipes = RecipeManager.getAll(this)                      // 최신 목록 다시 로드
        applyFilter(binding.etSearch.text?.toString() ?: "")        // 현재 검색어 기준으로 다시 그림
        binding.bottomNav.selectedItemId = R.id.nav_recipe          // 하단 탭에서 '레시피'가 선택돼 보이게
    }

    /**
     * 입력어로 레시피를 거르는 핵심 검색 로직.
     * - 검색어가 비어 있으면: 인기 검색어 박스를 보여주고 전체 목록 표시
     * - 검색어가 있으면: 인기 검색어 박스를 숨기고, 라면 이름에 그 단어가 포함된 글만 표시
     */
    private fun applyFilter(query: String) {
        val q = query.trim()                        // 앞뒤 공백 제거
        if (q.isEmpty()) {
            binding.popularBox.visibility = View.VISIBLE
            showList(allRecipes)
        } else {
            binding.popularBox.visibility = View.GONE
            // ramenName 안에 입력어가 들어있는 레시피만 골라냄 (대소문자 구분 없이)
            val filtered = allRecipes.filter { it.ramenName.contains(q, ignoreCase = true) }
            showList(filtered)
        }
    }

    /**
     * 주어진 목록을 RecyclerView 에 실제로 그린다.
     * 목록이 비었으면 "레시피가 없어요" 안내 문구를 대신 보여준다.
     */
    private fun showList(list: List<Recipe>) {
        binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        // 어댑터를 새로 연결. 카드를 누르면 그 글의 id 를 상세 화면으로 전달
        binding.recyclerView.adapter = RecipeAdapter(list) { recipe ->
            val i = Intent(this, RecipeDetailActivity::class.java)
            i.putExtra("id", recipe.id)             // 상세 화면이 이 id 로 원본 글을 다시 찾음
            startActivity(i)
        }
    }

    /**
     * 인기 검색어 버튼 3개에 글자를 채우고, 누르면 검색창에 그 단어를 입력한다.
     * (검색창에 글자가 들어가면 위의 TextWatcher 가 자동으로 목록을 걸러준다)
     */
    private fun setupPopular() {
        val buttons = listOf(binding.kw1, binding.kw2, binding.kw3)
        popularKeywords.forEachIndexed { idx, word ->
            buttons[idx].text = "${idx + 1}. $word"                 // "1. 신라면" 형태로 순위처럼 표시
            buttons[idx].setOnClickListener { binding.etSearch.setText(word) }
        }
    }

    /**
     * 하단 탭바 동작 연결. 이 화면은 '레시피' 탭이므로, 다른 탭을 누르면 해당 화면으로 이동한다.
     * ('레시피' 항목 자체는 따로 분기하지 않고 else 로 흘려보냄 — 이미 이 화면이라서)
     */
    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { goTo(MainActivity::class.java); true }
                R.id.nav_dex -> { goTo(RamenListActivity::class.java); true }
                R.id.nav_fav -> { goTo(FavoriteActivity::class.java); true }
                else -> true
            }
        }
    }

    /**
     * 다른 화면으로 이동하는 공통 함수.
     * REORDER_TO_FRONT + SINGLE_TOP 플래그로, 이미 떠 있는 화면이면 새로 만들지 않고 앞으로 끌어온다
     * → 탭을 오갈 때 화면이 끝없이 쌓이는 것을 방지 (메모리/뒤로가기 동작 깔끔)
     */
    private fun goTo(cls: Class<*>) {
        val i = Intent(this, cls)
        i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)
    }
}
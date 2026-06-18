package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMyPageBinding

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "마이페이지"

        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        // 찜 개수 / 내가 쓴 레시피 수 표시 (SharedPreferences 데이터 활용)
        val favCount = FavoriteManager.getFavorites(this).size
        val recipeCount = RecipeManager.getAll(this).size
        binding.tvFavCount.text = "$favCount"
        binding.tvRecipeCount.text = "$recipeCount"
        binding.bottomNav.selectedItemId = R.id.nav_my
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { goTo(MainActivity::class.java); true }
                R.id.nav_dex -> { goTo(RamenListActivity::class.java); true }
                R.id.nav_recipe -> { goTo(RecipeListActivity::class.java); true }
                R.id.nav_fav -> { goTo(FavoriteActivity::class.java); true }
                else -> true
            }
        }
        binding.btnGoFav.setOnClickListener { goTo(FavoriteActivity::class.java) }
        binding.btnGoRecipe.setOnClickListener { goTo(RecipeListActivity::class.java) }
    }

    private fun goTo(cls: Class<*>) {
        val i = Intent(this, cls)
        i.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(i)
    }
}
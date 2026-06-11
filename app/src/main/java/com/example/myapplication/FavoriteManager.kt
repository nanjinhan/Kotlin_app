package com.example.myapplication

import android.content.Context

// 찜 목록을 SharedPreferences에 저장/관리하는 도우미 (기능을 한 곳에 모아 코드 품질↑)
object FavoriteManager {
    private const val PREF = "ramen_dex"
    private const val KEY = "favorites"

    // 찜한 라면 이름 집합을 가져옴
    fun getFavorites(ctx: Context): MutableSet<String> {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        // getStringSet의 결과는 직접 수정하면 안 되므로 복사본을 만든다
        return prefs.getStringSet(KEY, emptySet())?.toMutableSet() ?: mutableSetOf()
    }

    fun isFavorite(ctx: Context, name: String): Boolean = getFavorites(ctx).contains(name)

    // 찜 토글: 이미 있으면 빼고, 없으면 추가. 결과(현재 찜 여부)를 반환
    fun toggle(ctx: Context, name: String): Boolean {
        val set = getFavorites(ctx)
        val nowFavorite = if (set.contains(name)) { set.remove(name); false }
                          else { set.add(name); true }
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putStringSet(KEY, set).apply()
        return nowFavorite
    }
}
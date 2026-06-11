package com.example.myapplication

import android.content.Context

/**
 * FavoriteManager — "찜(즐겨찾기)" 기능의 저장소(데이터 담당).
 *
 * ▸ object(싱글턴)로 만들어 찜 관련 로직을 한 곳에 모았다 → 어디서든 FavoriteManager.toggle(...) 로 사용.
 * ▸ 저장 방식: 찜한 라면들의 "이름"만 SharedPreferences 에 Set(집합) 형태로 보관한다.
 *   (레시피와 달리 단순 문자열 집합이라 JSON 없이 putStringSet 으로 충분)
 * ▸ 레시피·마지막 본 라면과 같은 "ramen_dex" 통(파일)을 공유하되, 키만 "favorites" 로 구분.
 */
object FavoriteManager {
    private const val PREF = "ramen_dex"     // SharedPreferences 파일 이름(앱 전체 공용)
    private const val KEY = "favorites"      // 찜 목록을 저장할 키

    /**
     * 찜한 라면 이름들의 집합을 가져온다.
     * 주의: getStringSet 이 돌려준 Set 은 직접 수정하면 안 되는(읽기전용) 객체이므로,
     *       toMutableSet() 으로 "수정 가능한 복사본"을 만들어 반환한다.
     */
    fun getFavorites(ctx: Context): MutableSet<String> {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY, emptySet())?.toMutableSet() ?: mutableSetOf()
    }

    /** 특정 라면이 지금 찜되어 있는지 여부 */
    fun isFavorite(ctx: Context, name: String): Boolean = getFavorites(ctx).contains(name)

    /**
     * 찜 토글: 이미 찜한 라면이면 빼고(false), 아니면 추가(true)한다.
     * 바뀐 집합을 저장하고, "지금 찜된 상태인지"를 반환한다(상세 화면이 하트 아이콘을 갱신할 때 사용).
     */
    fun toggle(ctx: Context, name: String): Boolean {
        val set = getFavorites(ctx)
        val nowFavorite = if (set.contains(name)) { set.remove(name); false }
                          else { set.add(name); true }
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putStringSet(KEY, set).apply()
        return nowFavorite
    }
}
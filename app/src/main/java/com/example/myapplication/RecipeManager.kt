package com.example.myapplication

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * RecipeManager — 레시피 게시판의 "저장소(데이터 담당)".
 *
 * ▸ object 키워드:
 *   - 코틀린에서 "앱 전체에 단 하나만 존재하는 객체(싱글턴)"를 만든다.
 *   - 어디서든 RecipeManager.add(...) 처럼 바로 부를 수 있어, 저장 로직을 한 곳에 모을 수 있다. (코드 품질 ↑)
 *
 * ▸ 저장 방식: SharedPreferences + JSON
 *   - SharedPreferences 는 "키-값"으로 작은 데이터를 앱 내부에 저장하는 안드로이드 기본 기능.
 *   - 레시피는 여러 개라서 값이 복잡 → 목록 전체를 JSON 문자열 한 덩어리로 바꿔서 통째로 저장한다.
 *   - 외부 라이브러리(Gson 등) 없이 안드로이드에 기본 포함된 org.json 만 사용한다.
 *
 * ▸ 앱을 완전히 꺼도 데이터가 유지된다 (CRUD: 생성/조회/수정/삭제 중 생성·조회·삭제를 지원).
 */
object RecipeManager {
    // SharedPreferences 파일 이름. 다른 기능(찜, 마지막 본 라면)과 같은 "ramen_dex" 통을 공유한다.
    private const val PREF = "ramen_dex"
    // 레시피 목록(JSON 문자열)을 저장할 때 쓰는 키
    private const val KEY = "recipes"
    // "예시 레시피를 이미 한 번 넣었는지" 표시하는 키 (true/false)
    // 예시 레시피 내용(사진 등)을 바꿨을 때 뒤 숫자를 올리면 다음 실행 시 예시가 한 번 다시 심긴다.
    private const val SEEDED = "recipes_seeded_v3"

    /**
     * 앱을 처음 켰을 때 단 한 번만 예시 레시피 4개를 넣어 둔다.
     * → 게시판이 텅 비어 보이지 않게 하기 위함. (이미 넣었으면 그냥 빠져나감)
     */
    fun seedIfFirst(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        // 이미 한 번 넣었다면(SEEDED == true) 아무것도 하지 않고 종료
        if (prefs.getBoolean(SEEDED, false)) return

        // 처음 보여줄 예시 레시피들 (id 는 1~4 고정값 사용 — 사용자 글은 시각 기반이라 겹치지 않음)
        val samples = listOf(
            Recipe(1, "짜파게티", "짜파구리 황금레시피", "라면덕후",
                "짜파게티 1개, 너구리 1개, 소고기 약간, 대파",
                "1) 두 라면을 함께 5분 끓인다\n2) 물을 8스푼만 남기고 버린다\n3) 스프를 넣고 비빈 뒤 볶은 소고기를 올린다",
                "소고기는 다진 채끝으로 하면 영화 그 맛이 난다", "recipe_chapaguri"),
            Recipe(2, "신라면", "계란탱 신라면", "자취생김씨",
                "신라면 1개, 계란 1개, 대파, 청양고추",
                "1) 물 550ml를 끓인다\n2) 면·스프 넣고 3분\n3) 계란을 풀지 말고 통째로 넣어 1분 더",
                "마지막에 청양고추를 넣으면 칼칼함이 산다", "recipe_shin_egg"),
            Recipe(3, "불닭볶음면", "마요 까르보 불닭", "맵찔이탈출",
                "불닭볶음면 1개, 마요네즈, 슬라이스 치즈 1장",
                "1) 면을 5분 삶고 물을 버린다\n2) 소스 절반만 넣는다\n3) 치즈와 마요를 올려 비빈다",
                "소스를 반만 넣어야 맵기를 잡으면서 맛은 산다", "recipe_buldak_mayo"),
            Recipe(4, "안성탕면", "된장 안성탕면", "국물요정",
                "안성탕면 1개, 된장 半스푼, 두부, 애호박",
                "1) 물에 된장 반 스푼을 풀어 끓인다\n2) 면·스프를 넣는다\n3) 두부와 애호박을 넣고 4분",
                "된장을 살짝 더하면 구수함이 두 배", "recipe_ansung_doenjang"),
            Recipe(5, "까르보불닭볶음면", "베이컨 까르보불닭", "고소대장",
                "까르보불닭 1개, 베이컨 3줄, 슬라이스 치즈 1장, 우유 3스푼",
                "1) 면을 5분 삶고 물을 버린다\n2) 베이컨을 바삭하게 굽는다\n3) 소스·크림분말에 우유를 넣고 비빈 뒤 베이컨·치즈를 올린다",
                "우유를 조금 넣으면 더 부드럽고 매운맛도 잡힌다", "recipe_carbo_bacon"),
            Recipe(6, "진짬뽕", "해물 가득 진짬뽕", "바다사나이",
                "진짬뽕 1개, 오징어·홍합·새우 한 줌, 대파",
                "1) 해물을 먼저 살짝 끓여 육수를 낸다\n2) 면·스프를 넣고 4분\n3) 대파를 올려 마무리",
                "냉동 해물믹스를 쓰면 간편하게 시원한 국물이 난다", "recipe_jjambbong_seafood"),
            Recipe(7, "팔도비빔면", "참치 비빔면", "여름입맛",
                "팔도비빔면 1개, 참치캔 1/2, 깻잎, 계란후라이 1개",
                "1) 면을 4~5분 삶고 찬물에 헹군다\n2) 비빔장과 참치를 넣고 비빈다\n3) 깻잎과 계란후라이를 올린다",
                "참치 기름을 살짝 넣으면 고소함이 배가된다", "recipe_bibim_tuna"),
            Recipe(8, "신라면", "신라면 라볶이", "분식왕",
                "신라면 1개, 떡 한 줌, 어묵 2장, 설탕 1스푼",
                "1) 물 500ml에 스프와 설탕을 풀어 끓인다\n2) 떡·어묵을 넣고 3분\n3) 면을 넣고 3분 더 끓여 졸인다",
                "설탕 1스푼이 들어가야 분식집 라볶이 맛이 난다", "recipe_shin_rabokki")
        )
        // 예시 목록을 저장하고, "이제 넣었음(SEEDED=true)" 표시를 남긴다 → 다음부터는 위 if 에서 걸러짐
        save(ctx, samples)
        prefs.edit().putBoolean(SEEDED, true).apply()
    }

    /**
     * 저장된 모든 레시피를 불러온다. (조회: Read)
     * JSON 문자열 → JSONArray → Recipe 객체 목록으로 되돌리는(파싱) 과정.
     */
    fun getAll(ctx: Context): MutableList<Recipe> {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        // 저장된 값이 없으면 빈 배열 "[]" 을 기본값으로 사용 (null 방지)
        val raw = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(raw)               // 문자열을 JSON 배열로 해석
        val list = mutableListOf<Recipe>()
        // 배열을 처음부터 끝까지 돌면서 JSON 객체 1개 → Recipe 1개로 변환
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(Recipe(
                o.getLong("id"), o.getString("ramenName"), o.getString("title"),
                o.getString("author"), o.getString("ingredients"),
                o.getString("steps"), o.getString("tip"),
                o.optString("imageName", "")   // imageName 은 없을 수도 있어 optString(기본값 "")으로 안전하게 읽음
            ))
        }
        return list
    }

    /**
     * 레시피 목록 전체를 JSON 문자열로 바꿔 저장한다. (private: 외부에서 직접 못 부르고 add/delete 를 통해서만 사용)
     * Recipe 객체 목록 → JSONArray → 문자열로 직렬화하는 과정.
     */
    private fun save(ctx: Context, list: List<Recipe>) {
        val arr = JSONArray()
        for (r in list) {
            val o = JSONObject()
            // 각 필드를 "키: 값" 형태로 JSON 객체에 담는다
            o.put("id", r.id); o.put("ramenName", r.ramenName); o.put("title", r.title)
            o.put("author", r.author); o.put("ingredients", r.ingredients)
            o.put("steps", r.steps); o.put("tip", r.tip); o.put("imageName", r.imageName)
            arr.put(o)
        }
        // 완성된 JSON 배열을 문자열로 만들어 SharedPreferences 에 저장
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY, arr.toString()).apply()
    }

    /**
     * 새 레시피를 추가한다. (생성: Create)
     * 목록의 맨 앞(0번)에 끼워 넣어 → 최신 글이 게시판 맨 위에 보이게 한다.
     */
    fun add(ctx: Context, recipe: Recipe) {
        val list = getAll(ctx); list.add(0, recipe); save(ctx, list)
    }

    /**
     * id 가 일치하는 레시피를 목록에서 지운다. (삭제: Delete)
     */
    fun delete(ctx: Context, id: Long) {
        val list = getAll(ctx); list.removeAll { it.id == id }; save(ctx, list)
    }

    /**
     * id 로 레시피 1개를 찾아 돌려준다. 없으면 null.
     * (상세 화면이 목록에서 받은 id 로 원본 글을 다시 찾아올 때 사용)
     */
    fun findById(ctx: Context, id: Long): Recipe? = getAll(ctx).find { it.id == id }

    // ─────────────────────────────────────────────────────────────────────
    //  레시피 "추천(좋아요)" 기능
    //
    //  ▸ 각 레시피마다 추천수를 따로 저장한다(키: "reco_count_<id>").
    //  ▸ 사용자가 이미 추천을 눌렀는지도 저장한다(키: "reco_done_<id>").
    //    → 버튼을 한 번 누르면 +1(추천), 다시 누르면 -1(취소)로 토글된다.
    //  ▸ 레시피 본문(JSON)은 건드리지 않고 별도 키에만 저장 → 기존 저장 구조를 안 바꿔 안전하다.
    // ─────────────────────────────────────────────────────────────────────
    private const val KEY_RECO_COUNT = "reco_count_"   // 추천수 저장 키 접두사
    private const val KEY_RECO_DONE = "reco_done_"     // "내가 추천했는지" 저장 키 접두사

    /**
     * 추천수의 "처음 시작값"을 레시피 id 로부터 만들어 낸다.
     * - 아직 한 번도 추천이 눌리지 않은 글이라도 "추천 2,085" 처럼 그럴듯한 숫자가 보이게 하기 위함.
     * - id 가 같으면 항상 같은 값이 나오므로(난수가 아님) 화면을 다시 봐도 숫자가 들쭉날쭉하지 않는다.
     */
    private fun baseCount(id: Long): Int = 800 + (Math.abs(id * 1234567L) % 4000L).toInt()

    /** 현재 추천수를 돌려준다. (저장된 값이 없으면 위 baseCount 로 만든 시작값을 사용) */
    fun getRecommendCount(ctx: Context, id: Long): Int {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_RECO_COUNT + id, baseCount(id))
    }

    /** 내가 이 레시피를 이미 추천했는지 여부(true/false). 버튼 모양/문구를 바꾸는 데 쓴다. */
    fun hasRecommended(ctx: Context, id: Long): Boolean {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_RECO_DONE + id, false)
    }

    /**
     * 추천 버튼을 눌렀을 때 호출.
     * - 아직 추천 안 했으면 → 추천수 +1, "추천함"으로 표시
     * - 이미 추천했으면   → 추천수 -1, 추천 취소
     * 바뀐 추천수를 돌려준다(화면에 바로 반영하기 위함).
     */
    fun toggleRecommend(ctx: Context, id: Long): Int {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val done = prefs.getBoolean(KEY_RECO_DONE + id, false)
        // 저장된 추천수가 없으면 시작값에서 출발
        var count = prefs.getInt(KEY_RECO_COUNT + id, baseCount(id))
        if (done) count-- else count++          // 취소면 -1, 추천이면 +1
        prefs.edit()
            .putBoolean(KEY_RECO_DONE + id, !done)   // 추천 상태를 반대로 뒤집어 저장
            .putInt(KEY_RECO_COUNT + id, count)      // 바뀐 추천수 저장
            .apply()
        return count
    }
}
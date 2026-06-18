package com.example.myapplication

// 시안의 "n명 추천" 숫자: 데이터에 없으므로 별점 기반으로 그럴듯하게 생성 (데모용)
val Ramen.recommendCount: Int
    get() = ((rating * 2000).toInt() + name.length * 137) % 9000 + 1000

// 맵기 칩에 표시할 짧은 라벨
fun Ramen.spicyLabel(): String = if (spicy >= 3) "매운맛" else "순한맛"

// 매운맛이면 true (칩 색 구분용)
fun Ramen.isSpicy(): Boolean = spicy >= 3

// 메인 화면 카테고리(매운맛/치즈/해물/계란/전체) 분류.
// 데이터 클래스를 바꾸지 않고 이름·태그·설명·맵기를 보고 해당 카테고리에 속하는지 판단한다.
fun Ramen.matchesCategory(category: String): Boolean {
    val text = "$name $tags $description $feature"
    return when (category) {
        "매운맛" -> spicy >= 4
        "치즈" -> text.contains("치즈")
        "해물" -> listOf("해물", "짬뽕", "오징어", "우동", "다시마", "무파마").any { text.contains(it) }
        "계란" -> text.contains("계란")
        else -> true   // "전체" 및 그 외는 모두 포함
    }
}
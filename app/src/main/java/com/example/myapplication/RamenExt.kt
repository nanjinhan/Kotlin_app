package com.example.myapplication

// 시안의 "n명 추천" 숫자: 데이터에 없으므로 별점 기반으로 그럴듯하게 생성 (데모용)
val Ramen.recommendCount: Int
    get() = ((rating * 2000).toInt() + name.length * 137) % 9000 + 1000

// 맵기 칩에 표시할 짧은 라벨
fun Ramen.spicyLabel(): String = if (spicy >= 3) "매운맛" else "순한맛"

// 매운맛이면 true (칩 색 구분용)
fun Ramen.isSpicy(): Boolean = spicy >= 3
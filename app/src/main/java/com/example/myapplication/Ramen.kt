package com.example.myapplication

// 라면 한 종류의 정보를 담는 데이터 클래스 (코드 품질: data class)
data class Ramen(
    val name: String,         // 라면명
    val maker: String,        // 제조사
    val category: String,     // 종류 (국물/볶음/비빔/짜장/짬뽕/컵 등)
    val packageType: String,  // 봉지 / 컵
    val spicy: Int,           // 맵기 0~5 (테두리 색과 별 표시에 사용)
    val price: String,        // 참고 가격
    val rating: Double,       // 별점
    val tags: String,         // 맛 태그
    val description: String,  // 대표 맛 설명
    val cook: String,         // 끓이는 법
    val feature: String,      // 특징 (한 줄)
    val recommend: String,    // 추천 상황
    val convenience: Boolean, // 편의점 구매 가능
    val mart: Boolean,        // 마트 구매 가능
    val imageName: String     // 이미지 파일명 (res/drawable 에 넣을 이름, 확장자 제외)
)
package com.example.myapplication

/**
 * Ramen — 라면 한 종류의 모든 정보를 담는 데이터 클래스.
 *
 * ▸ data class 라서 값을 담는 그릇 역할에 딱 맞고, equals/toString 등을 자동 제공한다.
 * ▸ 이 객체들의 목록이 [RamenData] 에 들어 있고, 도감/상세/찜 화면이 모두 이 데이터를 사용한다.
 */
data class Ramen(
    val name: String,         // 라면명 (목록·상세 제목, 찜 식별 기준)
    val maker: String,        // 제조사 (예: 농심, 오뚜기, 삼양식품)
    val category: String,     // 종류 (국물/볶음/비빔/짜장/짬뽕/컵 등)
    val packageType: String,  // 포장 형태 (봉지 / 컵)
    val spicy: Int,           // 맵기 0~5 (카드 테두리 색과 상세의 고추 개수에 사용)
    val price: String,        // 참고 가격 (범위 문자열)
    val rating: Double,       // 별점
    val tags: String,         // 맛 태그 (쉼표로 구분된 한 줄)
    val description: String,  // 대표 맛 설명
    val cook: String,         // 끓이는 법
    val feature: String,      // 특징 (한 줄)
    val recommend: String,    // 추천 상황 (언제 먹으면 좋은지)
    val convenience: Boolean, // 편의점 구매 가능 여부
    val mart: Boolean,        // 마트 구매 가능 여부
    val imageName: String     // 대표 이미지 drawable 파일명(확장자 제외). 없으면 맵기 색 배경으로 대체
)
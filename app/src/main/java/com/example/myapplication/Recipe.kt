package com.example.myapplication

/**
 * Recipe — "레시피 게시판" 글 한 개의 정보를 담는 데이터 클래스.
 *
 * ▸ data class 를 쓰는 이유:
 *   - equals()/hashCode()/toString()/copy() 를 코틀린이 자동으로 만들어 준다.
 *   - "값을 담기만 하는 그릇" 역할에 딱 맞아 코드가 짧고 안전해진다. (코드 품질 ↑)
 *
 * ▸ 이 객체 하나가 게시판의 카드 1개 = 상세 화면 1개 에 대응된다.
 * ▸ 실제 저장은 [RecipeManager] 가 이 객체를 JSON 문자열로 바꿔 SharedPreferences 에 넣는다.
 */
data class Recipe(
    val id: Long,            // 고유 번호. 글마다 겹치지 않게 "작성 시각(System.currentTimeMillis())"을 사용 → 삭제·검색의 기준이 됨
    val ramenName: String,   // 어떤 라면에 대한 레시피인지 (예: "짜파게티"). 게시판 검색은 이 값을 기준으로 함
    val title: String,       // 레시피 제목 (예: "짜파구리 황금레시피")
    val author: String,      // 작성자 이름 (비우면 작성 화면에서 "익명"으로 채움)
    val ingredients: String, // 재료 (여러 줄 가능)
    val steps: String,       // 끓이는 법 / 조리 순서 (여러 줄 가능)
    val tip: String,         // 나만의 팁 (여러 줄 가능)
    val imageName: String    // 대표 이미지의 drawable 파일명(확장자 제외). 없으면 "" → 카드/상세에서 주황 배경으로 대체
)
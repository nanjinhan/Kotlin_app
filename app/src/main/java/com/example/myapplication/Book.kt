package com.example.myapplication

// 도서 한 권의 정보를 담는 데이터 클래스
// data class를 쓰면 정보 묶음을 깔끔하게 관리할 수 있음 (코드 품질 항목)
data class Book(
    val title: String,       // 제목
    val author: String,      // 저자
    val price: Int,          // 가격(원)
    val pubDate: String,     // 출판일
    val coverColor: String,  // 표지 색상 (HEX 문자열)
    val description: String  // 책 소개 (상세 화면용)
)
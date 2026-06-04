# 빌드북 (BuildBook)

개발자와 메이커를 위한 IT 전문 도서 쇼핑몰 앱
모바일 응용 소프트웨어 기말 프로젝트 / 박진한 (224543)

## 앱 소개
- 앱 이름: 빌드북 (BuildBook)
- 테마: 크림 + 딥그린의 차분한 서점 감성
- 기획 의도: AI·메이커를 전공·활동으로 삼는 제 관심사를 살려, 일반 서점이 아닌
  개발서·아두이노·IoT 등 IT 전문 도서를 다루는 쇼핑몰로 기획했습니다.

## 구현 기능
- [x] 스플래시 화면 (2.5초 후 자동 전환, Handler)
- [x] 메인 화면 (로고·환영 문구, 툴바, 내비게이션 드로어)
- [x] 도서 목록 화면 (RecyclerView + CardView, 도서 6권)
- [x] 도서 상세 화면 (Intent로 데이터 전달)
- [x] 옵션 메뉴 (홈으로 / 앱 정보)
- [x] (가산점) NavigationDrawer
- [x] (가산점) SharedPreferences로 마지막 본 도서 저장
- [x] (가산점) AlertDialog (앱 정보)

## 개발 환경
- Android Studio / Kotlin
- minSdk 26, targetSdk 36
- ViewBinding, Material Components, RecyclerView, ConstraintLayout

## 실행 방법
1. Android Studio에서 프로젝트 열기
2. Gradle Sync 후 ▶ Run (에뮬레이터 또는 실기기, Android 8.0 이상)

## AI 사용 내역
- 사용 도구: Claude / Claude Code (Anthropic)
- 사용 범위: 앱 전체 구조 설계 및 초기 코드 생성에 활용했으며,
  생성된 코드를 직접 빌드·실행·수정하고 동작을 확인하며 학습했습니다.
- 해당 위치: 전체 .kt 소스 및 레이아웃 XML (수업에서 배운 ViewBinding,
  Intent, RecyclerView, 메뉴, AlertDialog 개념을 바탕으로 구성)

## 어려웠던 점
- (직접 작성) 예: 액티비티 간 데이터 전달 시 Intent 엑스트라 키 이름을 맞추는 부분에서
  처음에 값이 안 넘어왔는데, 보내는 쪽과 받는 쪽의 키 문자열을 통일해 해결했습니다.
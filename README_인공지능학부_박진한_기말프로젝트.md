# 🍜 라면도감 (RAMEN DEX)

맛있는 라면을 한눈에 보는 도감 + 레시피 게시판 앱
모바일 응용 소프트웨어(Kotlin) 기말 프로젝트 · 박진한 (224543)

---

## 1. 앱 소개

- **앱 이름**: 라면도감 (RAMEN DEX)
- **테마**: 흰색·크림 배경 + 빨강/주황 포인트의 따뜻한 라이트 테마. 라면 카드 테두리 색으로 맵기를 표현.
- **기획 의도**:
  - 한국에 처음 온 외국인이 어떤 라면이 있고 맛·맵기는 어떤지 한눈에 보고 고를 수 있도록, 라면을 "도감"처럼 정리했습니다.
  - 영화 기생충의 "소고기 짜파구리"처럼 라면을 색다르게 즐기는 조합·꿀팁을 공유해, 매번 똑같이 먹어 질린 자취생도 새로운 방법으로 먹어볼 수 있게 했습니다.
  - 즉 "라면 정보 도감 + 라면 레시피 커뮤니티"를 합쳐, 처음 접하는 사람부터 매일 먹는 사람까지 모두에게 도움이 되는 앱을 목표로 했습니다.

> ※ 과제 안내의 "나만의 BookMarket"은 예시이며, 교수님께 주제 변경을 확인받아
> 도서 쇼핑몰과 동일한 화면 구조(목록·상세·메뉴·드로어)를 유지한 채 주제만 "라면 도감"으로 구현했습니다.

---

## 2. 구현 기능 (체크리스트)

### 필수 화면 (5개)
- [O] ① 스플래시 화면 — 2.5초 표시 후 메인으로 자동 전환 (Handler 사용)
- [O] ② 메인(홈) 화면 — 앱 로고·환영 배너 + 툴바 + 내비게이션 드로어
- [O] ③ 도서(라면) 목록 화면 — RecyclerView + CardView, 라면 19종, 클릭 시 상세 이동
- [O] ④ 도서(라면) 상세 화면 — 이미지·이름·제조사·맵기·가격 등 표시, Intent로 데이터 전달
- [O] ⑤ 옵션 메뉴 — 툴바 메뉴 아이콘 → 앱 정보(AlertDialog) 등 항목 표시

### 가산점 기능 (+5)
- [O] NavigationDrawer + BottomNavigationView 둘 다 구현 (+3)
- [O] SharedPreferences로 "마지막 본 라면" 저장 (+2)
- [O] AlertDialog 활용 (앱 정보·삭제 확인·맵기 색 안내) (+2)

### 추가 구현 (완성도)
- [O] 찜(즐겨찾기) 기능 — 상세에서 ★ 토글, 찜 목록 화면에서 모아보기
- [O] 레시피 게시판 — 작성·조회·삭제(CRUD), 예시 레시피 자동 제공
- [O] 레시피 검색 — 라면 이름으로 실시간 필터링 + 인기 검색어 칩
- [O] 레시피 추천(좋아요) — 추천수 토글 및 표시
- [O] 마이페이지 — 찜 수 / 내 레시피 수 요약

---

## 3. 개발 환경

| 항목 | 내용 |
|---|---|
| IDE | Android Studio |
| 언어 | Kotlin (모든 소스 .kt, Java 파일 없음) |
| 최소 지원 | Android 8.0 (minSdk 26) |
| 타겟 | Android 16 (targetSdk 36) |
| UI 연결 | ViewBinding |
| 목록 | RecyclerView + Adapter |
| 저장소 | SharedPreferences (+ org.json) |
| 사용 라이브러리 | Material Components, RecyclerView (네트워크/DB 라이브러리 없음) |

리소스 분리: 문자열은 `strings.xml`, 색상은 `colors.xml`, 스타일은 `styles.xml`로 관리.

---

## 4. 실행 방법

1. Android Studio에서 프로젝트 폴더를 연다.
2. Gradle Sync가 끝날 때까지 기다린다.
3. 에뮬레이터 또는 실제 기기(Android 8.0 이상)를 선택한다.
4. ▶ Run 버튼으로 실행한다.

특이사항:
- 라면 이미지는 `app/src/main/res/drawable/` 에 정해진 파일명(예: `ramen_shin`)으로 넣으면 자동 표시되며, 없을 경우 맵기 색 배경으로 대체되어 빌드/실행에는 영향이 없습니다.
- 에뮬레이터가 완전히 부팅된 뒤(홈 화면 표시 후) Run 해야 설치 오류가 발생하지 않습니다.

---

## 5. AI 사용 내역

- **사용 도구**: Claude / Claude Code (Anthropic)
- **사용 여부**: 사용함
- **활용 방식**:
  - 앱의 전체 설계와 초기 코드 생성, 빌드 오류·디버깅 해결에 AI를 활용했습니다.
  - 단순히 결과물을 받아 쓰는 방식이 아니라, **수업에서 배운 개념(ViewBinding, Intent, RecyclerView, 메뉴, AlertDialog 등)을 제가 직접 코드에 반영해보고**, 그 코드를 두고 "이 부분은 이렇게 하는 게 맞는지" AI와 토론하며 함께 다듬는 방식으로 진행했습니다.
  - 즉 제가 작성·수정한 코드를 AI가 다시 검토·보완하고, 그 결과를 제가 또 확인하는 과정을 반복했습니다. (전체 `.kt` 소스 및 레이아웃 XML에 해당)
- **제가 직접 한 부분(AI 미사용 영역)**:
  - 앱의 **기획·콘셉트·타겟 설정**(외국인·자취생을 위한 라면 도감+레시피 커뮤니티)
  - **화면 구성과 화면 전환 흐름 설계**, 라면 데이터 선정, 이미지 삽입, 색상·레이아웃 등 디자인 방향 결정
  - 어떤 기능을 넣을지(찜·게시판·검색·추천)에 대한 기획 전반

---

## 6. 어려웠던 점과 해결 방법

### (1) 화면 전환 시 화면이 중복으로 쌓이는 문제
하단 탭(홈·도감·레시피·찜·마이)을 여러 번 누르니 같은 화면이 계속 새로 생성되어 뒤로가기를 여러 번 눌러야 하는 문제가 있었습니다.
→ 인텐트 플래그 `FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_SINGLE_TOP`를 주고, 메인 화면들을 `AndroidManifest.xml`에서 `android:launchMode="singleTop"`으로 선언해, 이미 떠 있는 화면은 새로 만들지 않고 앞으로 끌어오도록 해결했습니다. 이후 화면이 1개씩만 유지되고 뒤로가기 흐름도 자연스러워졌습니다.

### (2) 텍스트가 한 줄로 안 맞고 옆으로 깨지거나 줄바꿈되는 문제
메인 배너의 "🔥인기 · ★별점 · 추천수"를 한 줄에 맞추려고 글자 크기와 위치를 조정했는데, 막상 에뮬레이터에서 보니 글자 영역이 좁아 옆으로 잘리거나 아래로 줄바꿈되어 레이아웃이 깨졌습니다.
→ 원인은 배너 오른쪽 라면 사진이 차지하는 폭이 너무 넓어 왼쪽 텍스트 영역이 부족했던 것이었습니다. 사진 폭을 줄이고(150dp→128dp) 텍스트 안쪽 여백과 칩 간격을 줄여 가로 공간을 확보했으며, 추천수 문구에서 불필요한 글자를 빼 한 줄에 모두 들어가도록 해결했습니다. XML 미리보기와 실제 에뮬레이터 화면이 다를 수 있다는 점을 배웠습니다.

### (3) 수업 자료(교재)의 예시를 적용하려다 막혔던 점
처음에 제공받은 교재 정리 자료(`android_kotlin_project_full_single_file_embedded_images.md`)의 화면 예시를 참고해, 메뉴·툴바와 옵션 메뉴를 그 구성대로 만들려고 했습니다. 그러나 자료의 일부 예시는 기본 액션바를 사용하는 방식이라, 제가 만든 `NoActionBar` 테마 + 직접 만든 Toolbar 구조와 충돌해 메뉴가 두 번 뜨거나 햄버거 아이콘이 표시되지 않는 문제가 있었습니다.
→ 테마를 `NoActionBar`로 통일하고 `setSupportActionBar(toolbar)`로 직접 만든 툴바를 액션바로 등록한 뒤, `ActionBarDrawerToggle`로 드로어 햄버거를 연결해 해결했습니다. 교재 예시를 그대로 복사하기보다 내 앱의 테마 구조에 맞게 변형해야 한다는 것을 익혔습니다.

### (4) 찜·레시피가 앱을 끄면 사라지는 문제
처음에는 찜한 라면과 작성한 레시피가 메모리에만 저장되어, 앱을 종료하고 다시 실행하면 모두 초기화되는 문제가 있었습니다.
→ `SharedPreferences`에 데이터를 저장하도록 바꿔 해결했습니다. 찜은 단순 문자열이라 `StringSet`으로, 레시피는 항목이 많아 `org.json`으로 JSON 문자열로 직렬화해 저장했습니다. 이렇게 하니 앱을 꺼도 데이터가 유지되고, 다른 화면(찜 목록·마이페이지)에서도 같은 데이터를 불러올 수 있게 되었습니다.

### (5) 화면·파일이 많아지면서 생긴 관리의 어려움
앱을 발전시키며 화면이 9개까지 늘고 어댑터·저장소 파일까지 많아지자, 파일이 많아 생기는 여러 문제를 겪었습니다.
- **파일 간 참조가 꼬임**: A 화면에서 B 화면으로 이동할 때 클래스 이름·경로가 하나라도 어긋나면 빌드가 실패했습니다. (예: `RamenListActivity`로 이름을 바꿨는데 다른 곳에서 옛 이름을 부르고 있어 오류)
- **AndroidManifest 등록 누락**: 새 화면(Activity)을 만들 때마다 `AndroidManifest.xml`에 등록해야 하는데, 파일이 많아 등록을 빠뜨려 해당 화면으로 이동하면 앱이 죽는 문제가 있었습니다.
- **리소스·ID 이름 충돌**: 레이아웃·색상·문자열이 많아지며 같은 이름을 두 번 쓰거나 ViewBinding ID가 헷갈렸습니다.
- **수정 위치 찾기 어려움**: 색 하나를 바꿔도 `colors.xml`·테마·레이아웃 등 여러 군데에 흩어져 있어 모두 찾아 고쳐야 했습니다.
- **기존 기능을 안 깨고 새 기능 얹기**: 레시피 게시판·메인 화면 업그레이드를 추가할 때, 잘 동작하던 도감·찜 코드를 건드리지 않도록 "어디까지만 수정할지" 범위를 정하는 데 신경 써야 했습니다.

→ 새 화면을 만들 때마다 **(1) Manifest 등록 → (2) 화면 이동 코드의 클래스 이름 → (3) ViewBinding ID** 를 함께 점검하는 순서를 정해두고, 새 기능은 가능한 한 별도 파일로 분리해 기존 코드를 최소한으로 건드리도록 하여 관리 부담을 줄였습니다.

---

## 7. 프로젝트 구조

총 **Kotlin 17개 · 레이아웃 13개 · drawable(모양 11 + 이미지 23) · 메뉴 3개**로 구성되어 있습니다.

```
app/src/main/
├── AndroidManifest.xml          # 9개 화면(Activity) 등록, 스플래시를 시작점(LAUNCHER)으로 지정
│
├── java/com/example/myapplication/      ── Kotlin 17개 ──
│   │  [데이터 모델]
│   ├── Ramen.kt                 # 라면 1종의 정보를 담는 data class
│   ├── RamenData.kt             # 라면 19종 고정 데이터 + 맵기 색 함수
│   ├── RamenExt.kt              # 라면 확장 기능(추천수 생성·맵기 라벨·카테고리 분류)
│   ├── Recipe.kt                # 레시피 글 1개를 담는 data class
│   │
│   │  [저장소 — SharedPreferences, object 싱글턴]
│   ├── FavoriteManager.kt       # 찜 저장·조회·토글 (StringSet)
│   ├── RecipeManager.kt         # 레시피 저장·조회·삭제·추천 (org.json)
│   │
│   │  [화면 — Activity 9개]
│   ├── SplashActivity.kt        # 스플래시
│   ├── MainActivity.kt          # 홈(배너·인기 라면·카테고리·드로어·하단탭)
│   ├── RamenListActivity.kt     # 라면 도감(2열 그리드)
│   ├── RamenDetailActivity.kt   # 라면 상세 + 찜
│   ├── FavoriteActivity.kt      # 찜한 라면 목록
│   ├── RecipeListActivity.kt    # 레시피 게시판(검색·인기검색어)
│   ├── RecipeWriteActivity.kt   # 레시피 작성 폼
│   ├── RecipeDetailActivity.kt  # 레시피 상세 + 추천·삭제
│   ├── MyPageActivity.kt        # 마이페이지(찜·레시피 수 요약)
│   │
│   │  [어댑터 — 목록↔데이터 연결]
│   ├── RamenAdapter.kt          # 도감·찜 그리드 카드
│   ├── PopularAdapter.kt        # 홈 인기 라면 가로 스크롤 카드
│   └── RecipeAdapter.kt         # 레시피 게시판 리스트 카드
│
└── res/
    ├── layout/      ── 13개 ──
    │   ├── activity_splash / activity_main / activity_ramen_list / activity_ramen_detail
    │   ├── activity_favorite / activity_recipe_list / activity_recipe_write
    │   ├── activity_recipe_detail / activity_my_page
    │   ├── item_ramen / item_popular / item_recipe   # 카드 아이템
    │   └── nav_header                                # 드로어 헤더
    │
    ├── values/
    │   ├── strings.xml          # 문자열 리소스 분리
    │   ├── colors.xml           # 색상 리소스 분리
    │   ├── styles.xml           # 입력칸·칩·카테고리 등 공통 스타일
    │   ├── dimens.xml           # 간격(여백) 토큰
    │   └── themes.xml           # 앱 테마(NoActionBar)
    │
    ├── menu/                    # bottom_nav_menu · drawer_menu · option_menu
    │
    └── drawable/
        ├── (모양 XML) badge_red · banner_bg · card_round · chip_bg/mild/spicy
        │              circle_cream · input_box_bg · pill_bg · section_bg
        │              splash_card_bg · splash_circle      # 배경·배지·칩 도형
        └── (이미지) ramen_*.jpg/webp (라면 사진) · recipe_*.jpg (레시피 사진)
```

### 핵심 설계 3줄 요약
1. **데이터 모델**(Ramen·Recipe)은 값만 담는 `data class`.
2. **저장소**(FavoriteManager·RecipeManager)는 `object`(싱글턴)로 저장 로직을 한곳에 모음.
3. **어댑터**는 클릭을 감지만 하고 실제 동작은 `onClick` 람다로 화면에 위임 → 재사용성↑.

---

*제작: 박진한 (224543) · 모바일 응용 소프트웨어(Kotlin) 기말 프로젝트*

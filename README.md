# Protest Alert

**시위 알리미**는 대한민국에서 예정된 집회 및 시위 일정을 확인할 수 있도록 돕는 Android 애플리케이션입니다.

사용자는 관심 지역을 설정하고, 해당 지역의 집회 일정을 날짜별로 확인할 수 있습니다. 출퇴근, 운전, 외출 계획을 세울 때 집회로 인한 혼잡 가능성을 미리 파악하는 것을 목표로
하며, 수익을 추구하지 않는 비영리 프로젝트입니다.

<a href='https://play.google.com/store/apps/details?id=io.github.helpingstar.protest_alert
'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height='80px'/></a>


## Features

- 관심 지역 설정 및 변경
- 관심 지역의 집회·시위 일정 조회
- 날짜별 일정 그룹화 및 오늘 표시
- 집회 장소, 시간, 예상 참가자 수, 관할 경찰서 정보 표시
- 서버 데이터 동기화 및 로컬 저장
- 오프라인 상태에서 이전에 동기화된 데이터 조회
- 설정 화면을 통한 문의·건의 및 개인정보처리방침 접근

## Inspired by Now in Android

이 프로젝트는 Google의 공식 샘플 앱인 [Now in Android](https://github.com/android/nowinandroid)에 큰 영향을 받았으며,
아키텍처와 구현 패턴을 이 앱의 도메인에 맞게 참고했습니다.

- Kotlin과 Jetpack Compose 기반 UI
- `app`, `feature`, `core`, `sync` 중심의 모듈 구조
- ViewModel 기반 UI state 관리
- Repository, DataSource, model mapping을 분리한 데이터 계층
- Room, DataStore, WorkManager 기반 offline-first 데이터 흐름
- Hilt 기반 의존성 주입
- Gradle convention plugin 기반 빌드 설정

## Architecture

Now in Android가 앱을 기능과 책임에 따라 모듈화한 것처럼, Protest Alert도 다음과 같은 구조로 구성합니다.

- `app`: 앱 진입점, 최상위 내비게이션, 앱 공통 UI 상태
- `feature:schedule`: 집회 일정 조회 화면
- `feature:settings`: 관심 지역 설정 및 앱 설정 화면
- `core:data`: Repository와 동기화 로직
- `core:network`: Supabase 기반 네트워크 데이터 소스
- `core:database`: Room 기반 로컬 데이터베이스
- `core:datastore`: 사용자 설정 저장
- `core:model`: 앱 전역 도메인 모델
- `core:designsystem`, `core:ui`: 공통 Compose UI와 디자인 시스템
- `sync:work`: WorkManager 기반 백그라운드 동기화

## Development

Android Studio에서 프로젝트를 열고 `app` 구성을 실행합니다.

주요 개발 기준은 다음과 같습니다.

- UI는 Jetpack Compose와 Material 3 기반으로 작성합니다.
- 데이터는 서버에서 동기화한 뒤 로컬 저장소를 통해 화면에 노출합니다.
- 사용자가 선택한 관심 지역은 DataStore에 저장합니다.
- 백그라운드 동기화는 WorkManager를 통해 수행합니다.

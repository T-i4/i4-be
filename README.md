# ✨배달 및 포장 음식 주문 관리 플랫폼

## 1️⃣ 프로젝트 목적/상세

### 1. 모든 도메인 공통 기능<br>
   * CRUD 기능
   * 검색 기능
     * 정렬: 생성일순, 수정일순
     * 페이지 노출 건수 : 10건/30건/50건 (기본 10건)
   * 접근 권한 관리
     * 고객, 가게 주인, 관리자, 마스터 
   * 데이터 감사 로그
     *  모든 정보에 생성일, 생성 아이디, 수정일, 수정 아이디, 삭제일, 삭제 아이디를 포함
### 2. 사용자 인증 기능<br>
   * 회원가입
     * username은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성
     * password는  최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자
     * 사용자 권한 (CUSTOMER, OWNER, MANAGER, MASTER)
   * 로그인
     * JWT 인증 : Acess Token, Refresh Token 발급 및 관리
### 3. 가게 시스템 <br>
   * 관리자 권한만 가게 등록
     * 이후 가게 주인 지정 가능
   * 가게 주인이 자신이 소유한 가게의 주문 내역 조회 가능
   * 가게 검색 시 평점이 같이 조회되도록 구현
### 4. 리뷰 및 평점 시스템<br>
   * 주문 기반 리뷰 저장(주문 상태 COMPLETED)
     * 평점 저장(1~5점 평점)   
   * 주문 한 건당 한 건의 리뷰 생성 가능
   * 특정 가게 및 사용자의 리뷰 조회 가능
     
### 5. 상품 시스템<br>
   * 단일 상품 조회
     * 유저가 상품을 장바구니에 담기 위한 개별 상품 조회
     * 가게 주인 상품 변경을 위한 개별 상품 조회
   * 상품 다중 상태 변경 기능
     * 가게 주인이 자신이 소유한 가게 상품 목록 조회 후 상품별 판매 상태 변경
### 6. 장바구니 시스템<br>
### 7. 주문 시스템<br>
   * 주문 상태 관리(COMPLETED, CANCELED, PENDING)
   * 주문 취소(COMPLETED -> CANCELED) 
     
### 8. 결제 시스템<br>
   * 카드 결제만 지원(PG사 연동 통해 결제 정보 저장)
   * 결제 내역 관리를 위해 결제 테이블 운영
### 9. AI Gemini 시스템<br>
   * Google Ai Gemini API를 활용
     * 가게 주인이 상품 설명 쉽게 작성 가능
   * 사용자별 응답 내역 조회 가능
     * 응답 로그 DB 저장  

## 2️⃣ ERD 및 API 명세서 
* ERD
![ERD](https://github.com/user-attachments/assets/c8ae5cf3-2d4a-42d5-9220-2b7579a25f88)
<Br>
* API 명세서
https://teamsparta.notion.site/API-1a52dc3ef51480ed84e3efb6f288a58c


## 3️⃣ 기술 스택
💻 사용 기술 스택
- Backend : Spring Boot 3.3.8, JPA, QueryDSL, JWT, Spring Security <br>
- DB : PostgreSQL, Redis
- AI : Google Generative Language API(Gemini-1.5)
- Development Tools : Git/GitHub, IntelliJ IDEA
- Cloud : AWS EC2, RDS

## 4️⃣ 서비스 구성 및 실행 방법
- 🔧요구 사항:
  - Java 17
  - Redis (Redis 7.xx 버전)
  - PostgreSQL
  - Gradle
- 🔧설치 및 실행 방법<br>

1. 저장소 클론
 ``` 
   git clone https://github.com/sparta-i4/i4-be.git
  ```
2. 프로젝트 디렉토리 이동
 ```
cd i4-be
```
  3. Gradle을 이용해 의존성 설치 및 빌드
 ```
./gradlew build
```
  4. 애플리케이션 실행
 ```
./gradlew bootRun
```
  5. application.yml 파일 설정

 ```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database_name
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: create
    show-sql: true
  data:
    redis:
      host: localhost
      port: 6379

jwt:
  secret:
    key: "jwt 시크릿 키 입력"

google:
  api:
    key: "Gemini API Key 입력"
```


## 5️⃣ 팀원 역할분담
| 기능                     | 담당자  |
|-----------------|--------|
| 가게, 리뷰, AI 추천   | 김채원  |
| 유저, 주소      | 송예지  |
| 상품, 장바구니, 배포    | 박상욱  |
| 주문, 결제      | 박준혁  |




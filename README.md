# 💞 Soulmate

## 🧠 프로젝트 소개

**Soulmate**는 사용자에게 깊은 이해와 공감을 제공하는 감성 기반 일기 서비스입니다.  
AI 채팅을 통해 사용자의 하루와 감정을 섬세히 읽어내고, 사용자가 작성한 일기에 댓글을 달아 진심 어린 피드백과 응원을 제공합니다.  

> 사용자가 혼자일 때도 곁을 지키며, 말하지 않아도 마음을 알아주는 진정한 동반자가 되고자 합니다.

---

## 👯‍♀️ BE 팀원별 역할

### 👩‍💻 김혜린: 백엔드, 프론트엔드
- [BE] 카카오 OAuth 회원가입/로그인 구현
- [BE] ERD 다이어그램 만들기
- [BE] 날짜 및 캐릭터 선택 구현
- [BE] DB 엔티티 클래스 구현
- [BE] 에러 핸들링 설정
- [BE] 해시태그 종류 불러오기 
- [BE] AI 코멘트 저장

---
### 🦝 박은효: 백엔드, 디자인
- [BE] 날짜 및 캐릭터 선택 구현 
- [BE] DB 엔티티 클래스 구현 
- [BE] 일기 직접 작성 기능 구현 
- [BE] 일기 목록/개별 일기 조회 기능 
- [BE] 해시태그 별 다이어리 불러오기 
- [BE] 코멘트 즐겨찾기 및 모아보기 

---
### 🐰 박채린: PM, AI, 백엔드(보조) 
- [BE] 서버 배포 및 데이터베이스 설정 (EC2 + RDS + HTTPS) 
- [AI] GPT API를 활용하여 채팅 API 만들기 
- [AI] 채팅 내용 기반 일기 생성 API 만들기 
- [BE] CORS 설정 
- [BE] 일기 수정 및 삭제 

## 🔗 Soulmate 관련 링크

- 🔗 **프론트엔드 배포 URL**: [SoulMate 🧠💗](https://withsoulmate.netlify.app/)
- 🛠️ **백엔드 테스트 URL**: [SoulMate API test 🧪](https://soulmate.o-r.kr/api/test)
- 💻 **GitHub**:
  - 프론트엔드: [FE repo 👀](https://github.com/OpenKetchupSource/Web)
  - 백엔드: [BE repo 👩🏻‍💻](https://github.com/OpenKetchupSource/Server)
- 📘 **Notion 문서**: [프로젝트 문서 바로가기](https://rainbow-uncle-f19.notion.site/1c4e29af6ad5806c903be9dfaa2a4152?pvs=74)

---
## 🛠️ 기술 스택 요약

| 분류 | 기술 |
|------|------|
| Frontend | React 19, Vite, Zustand, Styled-components, TypeScript |
| Backend | Spring Boot, JPA, MySQL, OAuth (Kakao), EC2/RDS |
| AI | OpenAI GPT API 기반 코멘트 및 일기 생성 |
| 기타 | GitHub Actions, Netlify, Swagger, Notion, Figma |

---

## 📁 FE 프로젝트 구조
```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃  ┗ 📂com.openketchupsource.soulmate
 ┃ ┃     ┣ 📂apiPayload
 ┃ ┃     ┃  ┣ 📂exception
 ┃ ┃     ┃  ┃  ┣ 📂handler
 ┃ ┃     ┃  ┃  ┃  ┣ 📜DiaryHandler.java
 ┃ ┃     ┃  ┃  ┃  ┣ 📜LoginHandler.java
 ┃ ┃     ┃  ┃  ┃  ┣ 📜SettingHandler.java
 ┃ ┃     ┃  ┃  ┣ 📜ExceptionAdvice.java
 ┃ ┃     ┃  ┃  ┗ 📜GeneralException.java
 ┃ ┃     ┃  ┃
 ┃ ┃     ┃  ┣ 📂form
 ┃ ┃     ┃  ┃  ┣ 📂status
 ┃ ┃     ┃  ┃  ┃  ┣ 📜ErrorStatus.java
 ┃ ┃     ┃  ┃  ┃  ┗ 📜SuccessStatus.java
 ┃ ┃     ┃  ┃  ┣ 📜BaseCode.java
 ┃ ┃     ┃  ┃  ┣ 📜ReasonDTO.java
 ┃ ┃     ┃  ┗ 📜ApiResponse.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂auth
 ┃ ┃     ┃  ┣ 📂jwt
 ┃ ┃     ┃  ┃  ┣ 📜JwtTokenProvider.java
 ┃ ┃     ┃  ┃  ┗ 📜JwtValidationType.java
 ┃ ┃     ┃  ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃     ┃  ┣ 📜MemberAuthentication.java
 ┃ ┃     ┃  ┗ 📜PrincipalHandler.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂config 
 ┃ ┃     ┃  ┣ 📜SecurityConfig.java
 ┃ ┃     ┃  ┗ 📜WebConfig.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂controller 
 ┃ ┃     ┃  ┣ 📂chat
 ┃ ┃     ┃  ┃  ┗ 📜ChatController.java
 ┃ ┃     ┃  ┣ 📂diary
 ┃ ┃     ┃  ┃  ┣ 📜CommentController.java
 ┃ ┃     ┃  ┃  ┣ 📜DiaryController.java
 ┃ ┃     ┃  ┃  ┗ 📜HashTagController.java
 ┃ ┃     ┃  ┣ 📂login
 ┃ ┃     ┃  ┃  ┣ 📜AuthCheckController.java
 ┃ ┃     ┃  ┃  ┣ 📜HealthCheckController.java
 ┃ ┃     ┃  ┃  ┗ 📜KakaoLoginController.java
 ┃ ┃     ┃  ┣ 📂member
 ┃ ┃     ┃  ┃  ┗ 📜SettingController.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂converter
 ┃ ┃     ┃  ┗ 📜CommentConverter.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂domain
 ┃ ┃     ┃  ┣ 📂common
 ┃ ┃     ┃  ┃  ┗ 📜BaseTimeEntity.java
 ┃ ┃     ┃  ┣ 📂mapping
 ┃ ┃     ┃  ┃  ┗ 📜DiaryToHashtag.java
 ┃ ┃     ┃  ┣ 📜Character.java
 ┃ ┃     ┃  ┣ 📜Chat.java
 ┃ ┃     ┃  ┣ 📜ChatMessage.java
 ┃ ┃     ┃  ┣ 📜Comment.java
 ┃ ┃     ┃  ┣ 📜Diary.java
 ┃ ┃     ┃  ┣ 📜HashTag.java
 ┃ ┃     ┃  ┗ 📜Member.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂dto
 ┃ ┃     ┃  ┣ 📂chat
 ┃ ┃     ┃  ┃  ┣ 📜ChatInitResponseDto.java
 ┃ ┃     ┃  ┃  ┣ 📜ChatMessageDto.java
 ┃ ┃     ┃  ┃  ┣ 📜ChatReply2ClientDto.java
 ┃ ┃     ┃  ┃  ┣ 📜ChatRequestDto.java
 ┃ ┃     ┃  ┃  ┗ 📜ChatResponseDto.java
 ┃ ┃     ┃  ┣ 📂diary
 ┃ ┃     ┃  ┃  ┣ 📜ClientDiaryCreateRequest.java
 ┃ ┃     ┃  ┃  ┣ 📜ClientDiaryResponse.java
 ┃ ┃     ┃  ┃  ┣ 📜ClientDiaryUpdateRequest.java
 ┃ ┃     ┃  ┃  ┣ 📜ClientGptDiaryCreateRequest.java
 ┃ ┃     ┃  ┃  ┣ 📜CommentListResponse.java
 ┃ ┃     ┃  ┃  ┣ 📜CommentRequest.java
 ┃ ┃     ┃  ┃  ┣ 📜CommentResponse.java
 ┃ ┃     ┃  ┃  ┣ 📜DiaryListResponse.java
 ┃ ┃     ┃  ┃  ┣ 📜DiaryResponse.java
 ┃ ┃     ┃  ┃  ┣ 📜GptDiaryPrompt.java
 ┃ ┃     ┃  ┃  ┣ 📜GptDiaryResponse.java
 ┃ ┃     ┃  ┃  ┣ 📜HashTagDTO.java
 ┃ ┃     ┃  ┃  ┗ 📜StoredCommentResponse.java
 ┃ ┃     ┃  ┣ 📂kakao
 ┃ ┃     ┃  ┃  ┣ 📜SocialLoginRequest.java
 ┃ ┃     ┃  ┃  ┗ 📜SocialLoginResponse.java
 ┃ ┃     ┃  ┗ 📜TokenResponse.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂external.oauth
 ┃ ┃     ┃  ┣ 📂kakao
 ┃ ┃     ┃  ┃  ┣  📂clientInfo
 ┃ ┃     ┃  ┃  ┃  ┣ 📜KakaoAccount.java
 ┃ ┃     ┃  ┃  ┃  ┗ 📜Profile.java
 ┃ ┃     ┃  ┃  ┣  📂request
 ┃ ┃     ┃  ┃  ┃  ┣ 📜KakaoInfoRequest.java
 ┃ ┃     ┃  ┃  ┃  ┗ 📜KakaoTokenRequest.java
 ┃ ┃     ┃  ┃  ┣  📂response
 ┃ ┃     ┃  ┃  ┃  ┣ 📜KakaoInfoResponse.java
 ┃ ┃     ┃  ┃  ┃  ┗ 📜KakaoTokenResponse.java
 ┃ ┃     ┃  ┗ 📜KakaoProperties.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂repository
 ┃ ┃     ┃  ┣ 📂character
 ┃ ┃     ┃  ┃  ┗ 📜CharacterRepository.java
 ┃ ┃     ┃  ┣ 📂chat
 ┃ ┃     ┃  ┃  ┣ 📜ChatMessageRepository.java
 ┃ ┃     ┃  ┃  ┗ 📜ChatRepository.java
 ┃ ┃     ┃  ┣ 📂diary
 ┃ ┃     ┃  ┃  ┣ 📜CommentRepository.java
 ┃ ┃     ┃  ┃  ┣ 📜DiaryRepository.java
 ┃ ┃     ┃  ┃  ┗ 📜HashTagRepository.java
 ┃ ┃     ┃  ┗ 📂member
 ┃ ┃     ┃     ┗ 📜MemberRepository.java
 ┃ ┃     ┃
 ┃ ┃     ┣ 📂service
 ┃ ┃     ┃  ┣ 📂chat
 ┃ ┃     ┃  ┃  ┗ 📜ChatAIService.java
 ┃ ┃     ┃  ┣ 📂diary
 ┃ ┃     ┃  ┃  ┣ 📜CommentService.java
 ┃ ┃     ┃  ┃  ┗ 📜DiaryService.java
 ┃ ┃     ┃  ┣ 📂kakao
 ┃ ┃     ┃  ┃  ┣ 📜KakaoLoginService.java
 ┃ ┃     ┃  ┃  ┗ 📜LoginService.java
 ┃ ┃     ┃  ┗ 📂member
 ┃ ┃     ┃     ┣ 📜MemberService.java
 ┃ ┃     ┃     ┗ 📜SettingService.java
 ┃ ┃     ┃
 ┃ ┃     ┗ 📜SoulmateApplication.java
 ┃ ┃     
 ┃ ┃
 ┃ ┗ 📂resources
 ┃    ┣ 📜application.properties
 ┃    ┗ 📜application.yml 
 ┗ 📂test.java.com.openketchupsource.soulmate
    ┣ 📂service.kakao
    ┃  ┗ 📜LoginService.Test
    ┃
    ┗ 📜SoulmateApplicationTests
```

## 🧱 Build 환경
### Gradle + Spring Boot + Java 21
Spring Boot는 빠른 백엔드 애플리케이션 개발을 위한 프레임워크이며, Gradle은 의존성 관리와 빌드 자동화를 지원합니다.
이 프로젝트는 Java 21과 Spring Boot 3.2.5, MySQL, JWT, OpenFeign, Kakao OAuth, OpenAI API를 사용합니다.
- Build Tool: Gradle (Groovy DSL)
- JDK Version: Java 21 (java.toolchain.languageVersion)
- Framework: Spring Boot 3.2.5
- Dependency Management: io.spring.dependency-management, Spring Cloud BOM (2023.0.1)
- Database: MySQL (로컬 13306 포트)
- ORM: Spring Data JPA + Hibernate (ddl-auto: update, MySQLDialect)
- OAuth: Kakao Login 지원 (application.yml에 client-id 및 redirect-uri 설정)
- Security: Spring Security + JWT 인증 (jjwt 0.11.5)
- API 연동: OpenFeign, OpenAI API 연동
- 테스트 도구: JUnit + Spring Security Test
- 로컬 개발 지원: H2 인메모리 DB, application-SECRET-KEY.properties를 통한 profile 분기
---

## 📦 설치 및 실행
### 0. Github clone
`git clone https://github.com/OpenKetchupSource/Server.git` <br>
`cd Server`

deploy 브랜치에서 배포 

### 1. 빌드 및 실행 (JDK 21과 MySQL이 설치되어 있어야 합니다.)
   - Gradle 빌드: 
   `./gradlew build`
   - 서버 실행: 
   `java -jar build/libs/soulmate-0.0.1-SNAPSHOT.jar`
   - 또는 IntelliJ에서 SoulmateApplication.java 우클릭 -> Run 

## 📜 사용 라이브러리 및 설정 요약
### 🔧 주요 의존성 (dependencies)
| 라이브러리 |	설명 |
| -- | -- |
|spring-boot-starter-web|	REST API 서버 구축|
|spring-boot-starter-data-jpa|	JPA 기반 ORM 처리|
|spring-boot-starter-security|	인증 및 보안 설정|
|spring-boot-starter-validation|	@Valid 기반 입력값 검증|
|lombok|	반복 코드 제거 (getter/setter 등)|
|mysql-connector-j|	MySQL DB 연결|
|spring-cloud-starter-openfeign|	외부 API 호출 (OpenAI, Kakao 등)|
|jjwt (api, impl, jackson)|	JWT 인증 토큰 발급/검증|
|jackson-databind|	JSON 직렬화/역직렬화|

### 🧪 테스트 및 개발용 의존성
| 라이브러리 |	설명 |
| -- | -- |
|spring-boot-starter-test|	단위 테스트 (JUnit)|
|spring-security-test|	인증 테스트 지원|
|h2|	로컬 개발용 인메모리 DB|

### 🛠 설정 파일 정리
1. application.yml
- DB 접속 정보: MySQL, 포트 13306
- 프로파일 분리: SECRET-KEY 파일에서 민감 정보 주입
- JWT 키, Kakao OAuth, OpenAI API 키 포함

2. application-SECRET-KEY.properties
- .gitignore로 관리
- ${database-password}, ${jwt.secret} 등 외부 변수 설정

### 📂 주요 Gradle 스크립트 (build.gradle)
| 명령어 |	설명 |
| -- | -- |
|./gradlew build|	프로젝트 전체 빌드|
|./gradlew test|	테스트 실행|
|java -jar ...|	실행 가능한 JAR 실행|

## 🪪 라이선스
MIT License

# Made with 💗 by OpenKetchupSource

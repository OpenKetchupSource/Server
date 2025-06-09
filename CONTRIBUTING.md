# Contributing to OpenKetchupSource Server

먼저, 저희 프로젝트에 관심을 가져주셔서 감사합니다!

본 문서는 OpenKetchupSource Server 프로젝트에 기여하고자 하는 모든 분들을 위한 가이드입니다. 원활하고 일관된 협업을 위해 아래 사항을 준수해주시기 바랍니다.

---

## 📚 프로젝트 소개

본 프로젝트는 OpenKetchupSource 플랫폼의 Backend Server로, Spring Boot 기반의 RESTful API 서비스를 제공합니다. 안정성과 확장성을 고려하여 개발되었습니다.

## 🛠️ 기여 방법

### 1. 이슈 확인 및 작성
- **이슈 확인**: 작업 전, [Issues](https://github.com/OpenKetchupSource/Server/issues) 탭을 확인하여 유사한 이슈가 이미 등록되어 있는지 확인해주세요.
- **이슈 작성**: 새로운 버그나 기능 제안은 `New Issue` 버튼을 클릭하여 등록해주세요.
    - 제목은 명확하고 간결하게 작성
    - '이슈 템플릿' 템플릿을 사용

### 2. 브랜치 전략
- 기본 브랜치: `main`
- 기능 개발 브랜치 규칙:
  - 이슈 생성
  - 이슈 브랜치 생성
  - 이슈 브랜치에서 작업
  - 작업 후 pull request
  - main으로 merge
  - 배포는 deploy 브랜치에서 진행합니다


### 3. 커밋 메시지 작성
커밋 메시지는 다음 규칙을 따라주세요:
- feat : 기능 추가
- fix : 버그 수정
- docs : 문서화 작업
- chore : 기타 변경

- 본문에 작업 내용, 관련 이슈 번호 (`Closes #이슈번호`)를 명시
- 코드 리뷰를 위해 **자세한 설명**과 **테스트 방법** 포함

> ⚠️ **PR이 승인되기 전까지 `main` 브랜치에 직접 Push하지 않습니다.**

### 4. 코드 스타일
- Java: Google Java Style Guide 준수
- Controller, Service, Repository 계층 구분
- 가능한 한 **테스트 코드** 작성 (단위 테스트 권장)
- 불필요한 코드, 주석 제거
- `@Transactional`, `@Valid` 등 Spring 기본 Annotation 적극 활용

### 5. 커뮤니케이션
- 모든 커뮤니케이션은 GitHub Issue 또는 PR 내 코멘트로 진행합니다.
- 질문이나 제안은 언제든 환영합니다!

---

## 🤝 첫 기여를 위한 도움말
- GitHub Flow를 따릅니다.
- Fork 후 작업해도 좋습니다.
- 작은 변경이라도 PR 제출을 권장합니다.
- 친절한 코드 리뷰를 지향합니다.

---

## 📄 라이선스
본 프로젝트는 [MIT License](./LICENSE)를 따릅니다.

---

**여러분의 기여가 OpenKetchupSource를 더 나은 프로젝트로 만듭니다. 많은 참여 부탁드립니다! 🚀**


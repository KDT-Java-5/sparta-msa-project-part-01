# 스파르타 MSA 과정 개별 프로젝트

> 스파르타 MSA 과정 **Part 01 Week 01 개별 프로젝트**의 시작 코드입니다.
> 교안 예시 코드(`sparta-msa-lesson-part-01`)의 `week-02` 구성을 바탕으로, 공통 설정과 응답·예외 처리만 갖춘 상태에서 도메인을 직접 구현합니다.

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.11-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.2-6DB33F?logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.14.4-02303A?logo=gradle&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?logo=flyway&logoColor=white)

---

## 📚 목차

- [제출 방법](#-제출-방법)
- [시작 코드에 포함된 것](#-시작-코드에-포함된-것)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [도메인 추가 가이드](#-도메인-추가-가이드)
- [시작하기](#-시작하기)

---

## 📅 제출 방법

### 1️⃣ Branch 생성 및 작업

제공된 주차별 Git repository에서 **신규 Branch**를 생성한 뒤 작업합니다.

> **Branch 이름 형식**
> ```
> work/{팀번호}-{영문 이름}
> ```
> 예: `work/1-john-doe`


### 2️⃣ Commit 및 Push

작업 내용을 **작업용 브랜치**에 Commit하고 Push합니다.


### 3️⃣ PR 요청

작업이 완료되면 **작업용 브랜치**에서 **제출용 브랜치**로 PR(Pull Request)을 생성합니다.

> **제출용 브랜치 이름 형식**
> ```
> project/{영문 이름}
> ```
> 예: `project/john-doe`


### 4️⃣ PR 리뷰 및 병합

리뷰가 완료되면 **제출용 브랜치**에 PR을 병합합니다.


### 🔄 전체 흐름 요약

```
신규 Branch 생성 (work/...)
        ↓
     작업 진행
        ↓
  Commit & Push
        ↓
PR 생성 (work/... → project/...)
        ↓
    리뷰 완료
        ↓
  PR 병합 ✅
```

---

## 📦 시작 코드에 포함된 것

| 구분 | 내용 | 관련 파일 |
|---|---|---|
| **환경 구성** | PostgreSQL 연결, Flyway 마이그레이션, SQL 로그 | `application.yml`, `db/migration/V1__init_table.sql` |
| **API 문서** | Swagger UI + JWT Bearer 인증 스키마 | `global/config/SwaggerConfig.java` |
| **QueryDSL** | `JPAQueryFactory` Bean 등록, Q클래스는 `build/generated/querydsl`에 생성 | `global/config/QueryDslConfig.java`, `build.gradle` |
| **비밀번호 암호화** | `BCryptPasswordEncoder` Bean 등록 | `global/config/SecurityConfig.java` |
| **공통 응답** | `ApiResponse.ok(data)` / `ApiResponse.fail(...)` 형태로 응답 통일 | `global/response/ApiResponse.java` |
| **예외 처리** | `DomainException` + `DomainExceptionCode`, 검증 오류·서버 오류 전역 처리 | `global/exception/*` |

`domain` 패키지는 비어 있습니다. 여기에 프로젝트 도메인을 구현하세요.

---

## 🛠 기술 스택

| 분류 | 사용 기술 |
|---|---|
| **Language / Build** | Java 21, Gradle 8.14 |
| **Framework** | Spring Boot 3.3, Spring Cloud 2023.0 |
| **Web Server** | Undertow (Tomcat 대체) |
| **Persistence** | Spring Data JPA, QueryDSL 5.0, Flyway |
| **Database** | PostgreSQL |
| **Communication** | Spring Cloud OpenFeign, Spring Retry |
| **Validation** | Spring Validation (Hibernate Validator) |
| **Mapping** | MapStruct 1.5, Lombok |
| **API Docs** | springdoc-openapi (Swagger UI) |
| **Monitoring** | Spring Boot Actuator |
| **Test** | JUnit 5, Spring Boot Test |

---

## 📁 프로젝트 구조

```
sparta-msa-project-week-01
├── build.gradle
├── settings.gradle
├── gradle/wrapper
└── src
    ├── main
    │   ├── java/com/sparta/msa/project
    │   │   ├── domain                        👈 도메인 구현 위치
    │   │   ├── global
    │   │   │   ├── config
    │   │   │   │   ├── QueryDslConfig.java
    │   │   │   │   ├── SecurityConfig.java
    │   │   │   │   └── SwaggerConfig.java
    │   │   │   ├── constants
    │   │   │   │   └── Constants.java
    │   │   │   ├── exception
    │   │   │   │   ├── DomainException.java
    │   │   │   │   ├── DomainExceptionCode.java
    │   │   │   │   └── GlobalExceptionHandler.java
    │   │   │   └── response
    │   │   │       └── ApiResponse.java
    │   │   └── ProjectApplication.java
    │   └── resources
    │       ├── db/migration
    │       │   └── V1__init_table.sql
    │       └── application.yml
    └── test/java/com/sparta/msa/project
        └── ProjectApplicationTests.java
```

---

## 🧭 도메인 추가 가이드

교안 코드와 같은 계층 구조를 따릅니다. 예) `product` 도메인

```
domain/product
├── controller   ProductController.java      @RestController, ApiResponse로 응답
├── dto
│   ├── request  ProductRequest.java         @Valid 검증 어노테이션
│   └── response ProductResponse.java
├── entity       Product.java                @Entity
├── mapper       ProductMapper.java          MapStruct @Mapper(componentModel = "spring")
├── repository   ProductRepository.java      JpaRepository
│                ProductQueryRepository.java QueryDSL (동적 쿼리)
└── service      ProductService.java
```

1. **테이블 추가** — `db/migration`에 `V2__create_xxx_table.sql` 형태로 새 마이그레이션을 추가합니다. (이미 적용된 파일은 수정하지 않습니다)
2. **예외 코드 추가** — `DomainExceptionCode`에 도메인 예외를 정의하고 `throw new DomainException(...)`으로 사용합니다.
3. **응답 형식** — 컨트롤러는 `ApiResponse.ok(...)`로 감싸서 반환합니다.

```json
// 성공
{ "data": { "id": 1, "name": "노트북" } }

// 실패
{ "error": { "errorCode": "NOT_FOUND_PRODUCT", "errorMessage": "상품 정보를 찾을 수 없습니다." } }
```

---

## ▶️ 시작하기

### 1. 사전 준비

- **JDK 21**
- **PostgreSQL** — `localhost:5432`에 `sparta_project` 데이터베이스 생성

| 항목 | 값 |
|---|---|
| URL | `jdbc:postgresql://localhost:5432/sparta_project` |
| Username | `postgres` |
| Password | `postgres` |

> 교안 코드가 사용하는 `sparta` DB와 분리해야 Flyway 이력이 충돌하지 않습니다.

```bash
# Docker로 PostgreSQL 실행 (선택)
docker run -d --name sparta-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=sparta_project \
  -p 5432:5432 postgres

# 이미 PostgreSQL 컨테이너가 있다면 DB만 추가
docker exec -it <container-name> psql -U postgres -c "CREATE DATABASE sparta_project;"
```

### 2. 클론 및 빌드

```bash
git clone <repository-url>
cd sparta-msa-project-week-01

./gradlew build
```

### 3. 실행

```bash
./gradlew bootRun
```

| 주소 | 설명 |
|---|---|
| http://localhost:8080 | 애플리케이션 |
| http://localhost:8080/swagger-ui/index.html | Swagger UI |
| http://localhost:8080/actuator/health | 헬스 체크 |

### 4. 테스트

```bash
./gradlew test
```

---

<div align="center">

**Happy Coding! 🎉**

</div>

# Schedule API

## 프로젝트 설명
일정 관리 REST API 서버

## 주요 기능
- 일정 등록 (제목, 내용, 작성자명, 비밀번호)
- 일정 전체 조회 (작성자명 필터링, 수정일 내림차순 정렬)
- 일정 단건 조회
- 일정 수정 (제목, 작성자명만, 비밀번호 확인 필요)
- 일정 삭제 (비밀번호 확인 필요)
- **DTO 패턴 적용** (비밀번호 응답 제외)

---

## ERD

### Schedule 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 일정 ID |
| title | VARCHAR(20) | NOT NULL | 제목 |
| content | VARCHAR(200) | NOT NULL | 내용 |
| name | VARCHAR(50) | NOT NULL | 작성자명 |
| password | VARCHAR(255) | NOT NULL | 비밀번호 |
| create_at | DATETIME | NOT NULL | 생성일시 |
| update_at | DATETIME | | 수정일시 |

**비고**:
- password는 응답(Response)에 포함되지 않음 (DTO 패턴)
- create_at은 JPA Auditing으로 자동 설정
- update_at은 수정 시 자동 갱신

---

## API 명세서

### 1. 일정 등록
- **Method**: `POST`
- **URL**: `/api/schedules`
- **Request Body** (ScheduleRequest):
```json
{
  "title": "회의",
  "content": "팀 미팅",
  "name": "홍길동",
  "password": "1234"
}
```
- **Response** (ScheduleResponse, 201 Created):
```json
{
  "id": 1,
  "title": "회의",
  "content": "팀 미팅",
  "name": "홍길동",
  "createAt": "2025-04-13T10:00:00",
  "updateAt": null
}
```
- **비고**: 응답에 `password` 필드는 포함되지 않음 (DTO 패턴)

---

### 2. 전체 일정 조회
- **Method**: `GET`
- **URL**: `/api/schedules`
- **Query Parameters** (선택):
    - `name` (String): 작성자명 필터
- **예시**:
    - 전체 조회: `GET /api/schedules`
    - 작성자 필터: `GET /api/schedules?name=홍길동`
- **Response** (List<ScheduleResponse>, 200 OK):
```json
[
  {
    "id": 2,
    "title": "프로젝트 마감",
    "content": "최종 제출",
    "name": "김철수",
    "createAt": "2025-04-13T09:00:00",
    "updateAt": "2025-04-13T14:00:00"
  },
  {
    "id": 1,
    "title": "회의",
    "content": "팀 미팅",
    "name": "홍길동",
    "createAt": "2025-04-13T10:00:00",
    "updateAt": "2025-04-13T11:00:00"
  }
]
```
- **정렬**: `updateAt` 내림차순 (최신 수정 일정이 먼저)
- **비고**:
    - 응답에 `password` 필드는 포함되지 않음
    - `name` 파라미터가 있으면 해당 작성자의 일정만, 없으면 전체 조회

---

### 3. 단건 일정 조회
- **Method**: `GET`
- **URL**: `/api/schedules/{id}`
- **Path Variable**:
    - `id` (Long): 일정 ID
- **예시**: `GET /api/schedules/1`
- **Response** (ScheduleResponse, 200 OK):
```json
{
  "id": 1,
  "title": "회의",
  "content": "팀 미팅",
  "name": "홍길동",
  "createAt": "2025-04-13T10:00:00",
  "updateAt": "2025-04-13T11:00:00"
}
```
- **비고**: 응답에 `password` 필드는 포함되지 않음

---

### 4. 일정 수정
- **Method**: `PUT`
- **URL**: `/api/schedules/{id}`
- **Path Variable**:
    - `id` (Long): 일정 ID
- **Query Parameters**:
    - `password` (String): 비밀번호
- **예시**: `PUT /api/schedules/1?password=1234`
- **Request Body** (ScheduleRequest):
```json
{
  "title": "수정된 제목",
  "name": "수정된 작성자명"
}
```
- **Response** (ScheduleResponse, 200 OK):
```json
{
  "id": 1,
  "title": "수정된 제목",
  "content": "팀 미팅",
  "name": "수정된 작성자명",
  "createAt": "2025-04-13T10:00:00",
  "updateAt": "2025-04-13T15:30:00"
}
```
- **비고**:
    - `title`과 `name`만 수정 가능
    - `content`는 수정 불가
    - 비밀번호 불일치 시 400 에러
    - 응답에 `password` 필드는 포함되지 않음

---

### 5. 일정 삭제
- **Method**: `DELETE`
- **URL**: `/api/schedules/{id}`
- **Path Variable**:
    - `id` (Long): 일정 ID
- **Query Parameters**:
    - `password` (String): 비밀번호
- **예시**: `DELETE /api/schedules/1?password=1234`
- **Response**:
    - 성공: `200 OK` (응답 본문 없음)

---

## 에러 응답

### 일정을 찾을 수 없음
- **HTTP Status**: `400 Bad Request`
- **Response**:
```json
{
  "message": "일정 없음"
}
```

### 비밀번호 불일치
- **HTTP Status**: `400 Bad Request`
- **Response**:
```json
{
  "message": "비밀번호 불일치"
}
```

---

## 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **ORM**: Spring Data JPA
- **Database**: H2 (개발용)
- **Build Tool**: Gradle
- **Util**: Lombok

---

## 📂 프로젝트 구조
src/main/java/com/nodeajva/schedule/
├── controller/
│   └── ScheduleController.java
├── service/
│   └── ScheduleService.java
├── repository/
│   └── ScheduleRepository.java
├── entity/
│   └── ScheduleEntity.java
└── dto/
├── ScheduleRequest.java      (요청 DTO)
└── ScheduleResponse.java     (응답 DTO)

---

## 🔧 기술적 특징

### DTO 패턴
- **Request DTO**: 클라이언트 → 서버 데이터 전송
- **Response DTO**: 서버 → 클라이언트 데이터 전송
- **장점**:
    - 비밀번호 같은 민감정보 자동 제외
    - Entity 구조 변경해도 API 안정적
    - 계층 간 역할 명확히 분리

### 3 Layer Architecture
Controller (DTO ↔ Entity 변환)
↓
Service (비즈니스 로직, Entity 사용)
↓
Repository (DB 접근, Entity 사용)

**장점**:
1. 관심사 분리 (Separation of Concerns)
2. 유지보수성 향상
3. 테스트 용이성
4. 재사용성

### JPA Auditing
- `@CreatedDate`: 생성일시 자동 설정
- `@LastModifiedDate`: 수정일시 자동 갱신
- `@EnableJpaAuditing`으로 활성화

---

## 실행 방법

```bash
# Gradle로 실행
./gradlew bootRun

# 또는 JAR 빌드 후 실행
./gradlew build
java -jar build/libs/schedule-0.0.1-SNAPSHOT.jar
```

---

## 테스트
- **H2 Console**: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: (비어있음)
- **API Base URL**: http://localhost:8080/api/schedules

---

## 📝 과제 질문 답변

### 1. 3 Layer Architecture를 적용한 이유

**구조**:
Controller (Presentation Layer)
↓
Service (Business Logic Layer)
↓
Repository (Data Access Layer)

**각 Layer의 역할**:
- **Controller**: HTTP 요청/응답 처리, DTO ↔ Entity 변환
- **Service**: 비즈니스 로직 (비밀번호 검증, 데이터 가공)
- **Repository**: 데이터베이스 접근 (CRUD)

**장점**:
1. **관심사 분리(Separation of Concerns)**: 각 계층이 명확한 역할
2. **유지보수성**: 한 계층의 변경이 다른 계층에 최소 영향
3. **테스트 용이성**: 각 계층을 독립적으로 테스트 가능
4. **재사용성**: Service 로직을 여러 Controller에서 재사용

**예시**:
- DB 변경 (H2 → MySQL) → Repository만 수정
- API 형식 변경 → Controller만 수정
- 비밀번호 검증 로직 변경 → Service만 수정

---

### 2. 어노테이션 설명

#### @RequestParam
- **역할**: URL의 쿼리 파라미터를 메소드 파라미터로 받음
- **예시**: `GET /api/schedules?name=홍길동&page=1`
```java
@GetMapping
public List<Schedule> find(
    @RequestParam(required = false) String name
) { ... }
```
- **특징**:
    - `required = false`: 선택적 파라미터
    - `defaultValue` 설정 가능
    - 검색, 필터링에 주로 사용

#### @PathVariable
- **역할**: URL 경로의 일부를 메소드 파라미터로 받음
- **예시**: `GET /api/schedules/5`
```java
@GetMapping("/{id}")
public Schedule findById(@PathVariable Long id) { ... }
```
- **특징**:
    - RESTful URL 설계에 적합
    - 리소스 식별에 사용
    - 필수값 (required = true 기본)

#### @RequestBody
- **역할**: HTTP 요청 본문(Body)의 JSON을 객체로 변환
- **예시**: `POST /api/schedules`
```java
@PostMapping
public Schedule save(@RequestBody ScheduleEntity schedule) { ... }
```
- **특징**:
    - JSON → Java 객체 자동 변환
    - POST, PUT 요청에 주로 사용
    - Content-Type: application/json 필요

**비교표**:
| 어노테이션 | 위치 | 예시 | 용도 |
|-----------|------|------|------|
| @RequestParam | 쿼리 스트링 | `?name=value` | 검색, 필터링 |
| @PathVariable | URL 경로 | `/api/users/123` | 리소스 식별 |
| @RequestBody | 요청 본문 | `{"name": "value"}` | 데이터 생성/수정 |

---

## 5. 긴 Repository 메서드명 문제

### 문제 상황
Spring Data JPA의 메서드명 규칙으로 쿼리를 생성하다보니 메서드명이 너무 길어짐

```java
List<Schedule> findAllByOrderByCreatedAtDesc();
```

**문제점**:
- 읽기 어려움
- 타이핑 불편
- 가독성 저하
- Service에서 호출 시 코드가 장황함

### 시도한 해결 방법

#### 1. 메서드명 그대로 사용
```java
schedules = scheduleRepository.findAllByOrderByCreatedAtDesc();
```
- ✅ 동작은 정상
- ❌ 여전히 길고 불편함

#### 2. Pageable/Sort 사용 고려
```java
Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
List<Schedule> schedules = scheduleRepository.findAll(sort);
```
- ✅ Repository 수정 불필요
- ❌ Service 코드가 복잡해짐
- ❌ 정렬 로직이 Service에 노출됨

### 최종 해결: @Query 사용

**ScheduleRepository.java**
```java
@Query("SELECT s FROM Schedule s ORDER BY s.createdAt DESC")
List<Schedule> findRecent();
```

**ScheduleService.java**
```java
public List<ScheduleResponse> findRecent() {
    List<Schedule> schedules = scheduleRepository.findRecent();
    // ... 변환 로직
}
```

**장점**:
- ✅ 메서드명 간결: `findRecent()`
- ✅ JPQL로 명확한 쿼리 작성
- ✅ 가독성 향상
- ✅ 실무에서도 많이 사용하는 패턴
- ✅ 복잡한 쿼리도 작성 가능

**JPQL (Java Persistence Query Language)**:
- SQL과 유사하지만 엔티티 기반
- 테이블명/컬럼명 대신 클래스명/필드명 사용
- 예: `Schedule` (클래스), `createdAt` (필드)

### 배운 점

1. **Spring Data JPA 메서드명의 한계**
   - 간단한 쿼리: 메서드명 규칙이 편리함
   - 복잡한 쿼리: 메서드명이 과도하게 길어짐
   - `findAllByOrderByCreatedAtDesc()` 같은 장황한 이름 발생

2. **@Query의 활용**
   - JPQL로 직접 쿼리 작성 가능
   - 메서드명을 의미있게 짧게 지을 수 있음
   - SQL과 유사한 문법으로 학습 용이

3. **JPQL vs SQL 비교**
   | 항목 | SQL | JPQL |
   |------|-----|------|
   | 대상 | 테이블 | 엔티티 클래스 |
   | 이름 | `schedules` (테이블) | `Schedule` (클래스) |
   | 필드 | `created_at` (컬럼) | `createdAt` (필드) |
   | 예시 | `SELECT * FROM schedules` | `SELECT s FROM Schedule s` |

4. **실무 가이드라인**
   - 간단한 조회: 메서드명 규칙 사용 (`findById`, `findByUserId`)
   - 복잡한 조회: `@Query` 사용
   - 매우 복잡한 쿼리: QueryDSL, Specification 고려
   - 메서드명이 4단어 이상이면 `@Query` 검토

5. **가독성 개선 효과**
```java
   // Before
   scheduleRepository.findAllByOrderByCreatedAtDesc();
   
   // After  
   scheduleRepository.findRecent();
```
   - 의도가 명확함
   - 타이핑 편리
   - 유지보수 용이

---

## 6. Repository 조회 시 반복되는 예외 처리 코드

### 문제 상황
모든 Service에서 엔티티 조회 시 동일한 예외 처리 패턴이 반복됨

```java
// UserService
User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException());

// ScheduleService  
Schedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new ScheduleNotFoundException());

// CommentService
Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CommentNotFoundException());
```

**문제점**:
- 같은 패턴이 여러 곳에 중복
- 코드가 장황함 (3줄)
- DRY 원칙 위반
- 오타 가능성

### 해결 방법: Repository default 메서드 추가

**Java 8 인터페이스 default 메서드 활용**

#### UserRepository.java
```java
public interface UserRepository extends JpaRepository<User, Long> {

    default User load(Long id) {
        return findById(id).orElseThrow(UserNotFoundException::new);
    }
}
```

#### ScheduleRepository.java
```java
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    default Schedule load(Long id) {
        return findById(id).orElseThrow(ScheduleNotFoundException::new);
    }
}
```

**Service에서 사용**:
```java
// Before (3줄)
User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException());

// After (1줄)
User user = userRepository.load(userId);
```

**장점**:
- ✅ 코드 중복 제거 (DRY)
- ✅ 가독성 향상 (3줄 → 1줄)
- ✅ 예외 처리 로직 캡슐화
- ✅ 오타 위험 감소
- ✅ 메서드명 간결 (`load`)

### 배운 점

1. **Java 8 인터페이스 default 메서드**
   - 인터페이스에 구현 메서드 작성 가능
   - Repository 확장 없이 기능 추가
   - JpaRepository 기본 메서드와 조합 가능

2. **메서드명 선택 과정**
   - `getById`: Getter와 혼동 가능성
   - `findByIdOrThrow`: 너무 길고 장황함
   - `load`: 짧고 명확, Hibernate 용어와 일관성

3. **고급 문법 캡슐화**
   - Optional, orElseThrow, 람다를 Repository에 숨김
   - Service는 간단한 메서드 호출만
   - "소설책처럼 읽히는 코드" 구현

4. **DRY 원칙 적용**
   - 3번 이상 반복되는 패턴 발견
   - 공통 로직을 한 곳에 모음
   - 유지보수성 향상

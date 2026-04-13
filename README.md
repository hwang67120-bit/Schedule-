# Schedule API

## 프로젝트 설명
일정 관리 REST API

## 기능
- 일정 등록
- 일정 조회 (전체/단건)
- 일정 수정 (비밀번호 확인)
- 일정 삭제 (비밀번호 확인)

## ERD
![ERD](erd.png)

또는

### Schedule 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PK, AI | 일정 ID |
| title | VARCHAR(20) | NOT NULL | 제목 |
| content | VARCHAR(200) | NOT NULL | 내용 |
| name | VARCHAR(50) | NOT NULL | 작성자 |
| password | VARCHAR(100) | NOT NULL | 비밀번호 |
| created_at | DATETIME | NOT NULL | 생성일시 |
| updated_at | DATETIME | | 수정일시 |

## API 명세
### 1. 일정 등록
- **URL**: `POST /api/schedules`
- **Request Body**:
```json
{
  "title": "제목",
  "content": "내용",
  "name": "작성자",
  "password": "1234"
}
```
- **Response**:
```json
{
  "id": 1,
  "title": "제목",
  "content": "내용",
  "name": "작성자",
  "createdAt": "2025-04-10T10:00:00",
  "updatedAt": null
}
```

### 2. 전체 조회
- **URL**: `GET /api/schedules`
- **Response**: Schedule 배열

### 3. 단건 조회
- **URL**: `GET /api/schedules/{id}`
- **Response**: Schedule 객체

### 4. 수정
- **URL**: `PUT /api/schedules/{id}?password=1234`
- **Request Body**:
```json
{
  "title": "수정 제목",
  "content": "수정 내용"
}
```

### 5. 삭제
- **URL**: `DELETE /api/schedules/{id}?password=1234`

## 기술 스택
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Lombok

## 실행 방법
```bash
./gradlew bootRun
```

## 테스트
- H2 Console: http://localhost:8080/h2-console
- API Base URL: http://localhost:8080/api/schedules
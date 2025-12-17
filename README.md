Online Bookstore API Project
이 프로젝트는 Spring Boot 기반의 온라인 서점 백엔드 API 서비스입니다. 사용자는 회원가입 후 도서를 검색하고, 장바구니에 담아 주문할 수 있으며, 리뷰 및 찜 목록 기능을 이용할 수 있습니다.

1. 프로젝트 및 코드 설명
기술 스택

Language: Java 17

Framework: Spring Boot 3.x

Database: MySQL 8.0

Build Tool: Gradle

Docs: Swagger (OpenAPI 3.0)

주요 기능 (Domain)

User: 회원가입, JWT 로그인, 회원정보 수정, 회원 탈퇴

Book: 도서 등록, 도서 검색(키워드), 페이징 조회, 상세 조회

Cart: 장바구니 담기, 수량 변경, 삭제

Order: 주문 생성, 주문 내역 조회, 주문 취소

Review: 구매한 도서 리뷰 작성 및 조회

Wishlist: 관심 도서 등록 및 해제

보안 설정 (Environment Variables)

본 프로젝트는 보안을 위해 DB 접속 정보 및 비밀키를 코드(application.yml)에 포함하지 않았습니다. 실행 전 반드시 환경 변수를 설정하거나, 프로젝트 루트에 .env 파일을 생성해야 정상 작동합니다.

필수 환경 변수 목록:

MYSQL_HOST (기본값: localhost)

MYSQL_PORT (기본값: 3306)

MYSQL_DATABASE (기본값: bookstore_db)

MYSQL_USERNAME (기본값: root)

MYSQL_PASSWORD (설정 필수)

JWT_SECRET (JWT 서명 키)

2. API 주소 (JCloud 배포 정보)
배포된 서버의 접속 정보입니다. 포트 리다이렉션 설정에 따라 외부 접속 포트를 사용합니다.

Swagger UI
https://app.swaggerhub.com/apis/jbnu-31b/book_store/1.0.0

API Root URL
http://localhost:8080/api/v1


http://<JCLOUD_IP>:<PORT>/api/v1

Health Check

http://<JCLOUD_IP>:<PORT>/api/v1/health


3. 코드 설치 및 실행 방법
이 프로젝트는 Gradle을 사용합니다. 터미널에서 아래 명령어를 순서대로 입력하여 빌드 및 실행할 수 있습니다.

사전 준비

Java 17 이상 설치

MySQL 실행 및 데이터베이스 생성 (CREATE DATABASE bookstore_db;)

루트 경로에 .env 파일 생성 (DB 정보 입력)

빌드 및 실행 (Mac / Linux)

Bash
# 1. 권한 부여 (필요 시)
chmod +x gradlew

# 2. 클린 빌드 (테스트 포함)
./gradlew clean build

# 3. JAR 파일 실행
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
빌드 및 실행 (Windows)

DOS
:: 1. 클린 빌드
gradlew.bat clean build

:: 2. JAR 파일 실행
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
4. Postman 컬렉션
https://jeonghyeonhwan-9288669.postman.co/workspace/%25EC%25A0%2595%25ED%2598%2584%25ED%2599%2598's-Workspace~11ecd958-a0df-4b55-855b-290e61610d00/overview

사용 방법:

Postman 실행

좌측 상단 Import 버튼 클릭

제출된 .json 파일 선택

컬렉션 내의 API 요청 테스트 (환경 변수 {{base_url}} 설정 권장)

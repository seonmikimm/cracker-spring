# Crackers(Spring Boot)
## 1. 프로젝트 소개
- 주제: 맛집 정보를 공유하고 의견을 나눌 수 있는 커뮤니티 서비스
- 개발 기간: 2022.06.27~2022.07.29
- 참여 인원: 5명
- 역할: 팀원
- http://crackers.life

## 2. 사용 기술
- Backend : Java 11 Spring Boot 2.6.9 Gradle 7.4.1 Thymeleaf Spring Data JPA Spring Security
- Frontend : HTML JavaScript CSS JQuery
- AWS : S3 EC2 Secret Manager CodeDeploy
- Database : AWS RDS MySQL 8.0.28
- CI/CD : Github Action

## 3. 맡은 기능
### 대표 기능 : 프로필 CRUD (프로필 페이지, 북마커 기능)
- Spring data JPA를 활용한 DB 업데이트 구현
- AWS S3 Service 를 통한 이미지 업로드 구현
- 파일 경로(이미지 URL) DB 저장하여 관리
- Dto를 활용하여 유지보수성 증가

## 4. 어려웠던 점
### 이미지 파일 전송 및 AWS S3 연동
- 회원이 변경하는 이미지 파일을 AWS에 연동하여 S3에 저장하기로 했습니다.
- 이미지 파일 저장 기능을 확인하기 위해 먼저 로컬에 이미지를 저장한 다음 나중에 AWS S3 연동으로 바꾸는 것을 고려해보았지만, 나중에 코드를 수정하는 것이 더 어려울 것 같아서 처음부터 S3에 연동하기로 했습니다.
- AWS에 연동하는 방법은 여러가지 구현 방법이 있었지만, 제가 알고있는 DTO 와 Spring 3계층을 사용할 수 있도록 필요한 코드만을 남기고 설계하였습니다.
- 결과적으로 아래와 같은 설계가 된다는 것을 이해하고 구현할 수 있었습니다.

## 5. 문제 해결 경험
### 이미지 전송 시 POST 500 에러
- 프론트엔드의 formdata 의 데이터를 Dto로 전달하는 과정에서 에러가 발생했습니다.
- 프론트엔드 콘솔 창을 확인했지만 POST 500 외에 별다른 메세지가 없어 서버를 수정하기로 했습니다.
- 먼저 Controller의 설정 consumes 설정에서 Json type을 제거, Controller 클래스에 @ResponseBody 어노테이션을 추가하고 매개변수 Dto에 @ModelAttribute를 추가하여 formdata를 자바 객체로 받을 수 있도록 설정했습니다.
- 프론트엔드 콘솔 창을 다시 확인했을 때에도 데이터 타입 오류가 아니라 Null 으로 들어가는 것을 보고 전체적인 코드를 다시 확인해보니 formdata 변수명이 Dto와 일치하지 않는 것을 발견했습니다. 이 에러를 통해 API 통신 시 변수명의 중요성을 알게 되었습니다.

### 이미지를 전송하지 않을 시 BeanPropertyBindingResult 에러
- 회원이 닉네임이나 상태메세지를 변경하지 않고 null 을 전송해도 에러가 발생하지 않았는데, 프로필 사진을 변경하지 않고 Null을 보내니 org.springframework.validation.BeanPropertyBindingResult 에러가 발생했습니다.
- 에러메세지를 검색해보니 파일에 잘못된 값을 보냈을 때 스프링의 유효성 검사 기능이 작동한 것이었고, 이 에러를 통해 개발자가 손을 대지 않아도 스프링 프레임워크가 유효성 검사를 하고 있다는 것을 알게 되었습니다.
- 스프링은 @ModelAttribute를 통해 formdata를 자바 객체에 바인딩할 때 서버에 잘못된 데이터가 저장되는 것을 방지하기 위해서 formdata의 유효성 검사를 합니다. 파일을 null 으로 보내면 에러가 발생하는데 이때 에러를 BindingResult 객체에 담아주어야 에러가 발생해도 작업을 완료할 수 있습니다.
- 그래서 formdata를 바인딩하는 매개변수 Dto에 @Validated를 추가하였고, 매개변수 BindingResult를 추가하였습니다. @Validated를 사용해 유효성 검사 결과를 BindingResult 매개변수에 담고 작업을 진행할수 있게 됩니다.

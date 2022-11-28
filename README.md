# Tradlinx_Back-End_API_TEST
1. 테이블 DDL
   - 계정 Table 
     - create table account (
         user_id varchar(255) not null,
         points integer not null,
         pw varchar(255),
         username varchar(255),
         primary key (user_id)
       )
   - 글 Table
     - create table article (
         article_id varchar(255) not null,
         article_contents varchar(255),
         article_title varchar(255),
         user_id varchar(255),
         primary key (article_id)
       )
   - 댓글 Table 
     - create table comment (
         comment_id varchar(255) not null,
         comment_contents varchar(255),
         account_id varchar(255),
         article_id varchar(255),
         primary key (comment_id)
       )
2. swagger API URL
   - https://app.swaggerhub.com/apis-docs/kangyonghyun/TradLinx_Kangyonghyun_Test/1.0.0
3. API 요구사항 및 구현
   - 회원가입 API (o)
     - 회원 id,pw,name 파라미터로 받아서 회원가입, 응답값으로 회원 id 리턴
   - 로그인 API (o)
     - 회원 id,pw 파라미터를 받아서 로그인, 응답값으로 jwt 리턴
     - 응답으로 header 에 jwt 전달
   - 내 정보 조회 API (o)
     - get 방식으로 회원 조회하고 회원 id, name 리턴
     - 파라미터로 header 에 jwt 전달
   - 내 Points 조회 API (o)
     - get 방식으로 회원 조회하고 회원 points 리턴
     - 파라미터로 header 에 jwt 전달
   - 글 쓰기 API (o)
     - 글제목, 글내용을 파라미터로 받아서 글작성, 응답값으로 글 id 리턴
     - 작성자 points 3 증가
   - 글 수정 API (o)
     - 글id, 글제목, 글내용을 파라미터로 받아서 글수정, 응답값으로 글 id 리턴
   - 글 조회 API (o)
     - 글id 를 get 파라미터로 받아서 글 조회, 글에 작성된 댓글 id 리턴
   - 글 삭제 API (o)
     - 글id 를 get 파라미터로 받아서 글 삭제, 해당하는 댓글 삭제, 1 리턴
     - 글 작성 후 받은 points 전부 제거
   - 댓글 쓰기 API (o)
     - 글 id 와 댓글 내용을 파라미터로 받아서 글에 댓글 작성, 댓글 id 리턴
     - 글 작성자 +1, 댓글 작성자 +2 증가
   - 댓글 삭제 API (o)
     - 댓글 id 를 파라미터로 받아서 해당하는 댓글 삭제, 댓글 id 리턴
     - 해당하는 points 제거
4. API 테스트
   - Junit5 와 Mockito 테스트 프레임워크를 활용하여 각 API 구현시 단위 테스트
   - 성공 사례 테스트 작성, 실패 사례 테스트 작성이 미흡(보안 필요)
5. GitHub URL
   - https://github.com/kangyonghyun/Tradlinx_Back-End
6. 서술형 URL
   - https://kangyonghyun.notion.site/TradLinx-1f14b544826641479357a5f93cf64cf0
   - 
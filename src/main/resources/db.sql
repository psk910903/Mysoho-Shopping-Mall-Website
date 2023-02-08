-- mysoho.sql
-- 모바일 쇼핑몰 DB 스키마(테이블) 설계

-- 1. member : 회원가입, 로그인, 비번찾기, 아이디찾기
--             적립금, 쿠폰
--   member_no INT(10) : PK
--   member_id : 아이디
--   member_pw : 비번
--   joindate  : 회원가입일
--   회원가입 화면 참조
--   point :
--   coupon :
-- 2. item : 상품목록 - 베스트상품, 최신상품
--
--
-- 3. cart : 장바구니 - 비회원, 회원
--    cart_no : PK
--    member_id
--    item_no  (상품테이블의 PK)
-- 4. order : 주문목록 (결제정보, 묶음주문 최대5개)
-- 주문상태 코드
-- 주문완료 -> 결제대기 -> 결제완료 -> 배송대기 -> 배송중 ->
-- 배송완료
-- 주문취소 (배송중일때는 안됨)
-- 5. qna : 묻고 답하기
-- 6. notice : 공지사항
-- 7. review : 상품후기

DROP DATABASE IF EXISTS mysoho;
CREATE DATABASE mysoho;
USE mysoho;

-- 회원가입
DROP TABLE if EXISTS member;
CREATE TABLE member (
  member_no INT PRIMARY KEY AUTO_INCREMENT, -- 고유키
	member_id VARCHAR(255) NOT NULL UNIQUE, -- 회원아이디
	member_pw VARCHAR(255) NOT NULL, -- 비밀번호
	member_name CHAR(255) NOT NULL, -- 회원이름
	member_email VARCHAR(255) NOT NULL, -- 이메일
	member_phone CHAR(255) NOT NULL, -- 전화번호(중간에 -는 제거)
	member_mileage INT DEFAULT 0, -- 마일리지(적립금)
	member_join_datetime DATETIME DEFAULT NOW(),-- 회원가입 날짜
  member_exit_datetime DATETIME DEFAULT NULL, -- 탈퇴 날짜
  member_exited TINYINT DEFAULT 0, -- 탈퇴 여부(0: 회원, 1: 탈퇴 회원)
);

INSERT INTO member VALUES( NULL, 'hong', '1234', '홍길동', 'hong@gmail.com',
  '01022223333', default, default, default, default);
SELECT * FROM member;

-- 쿠폰
DROP TABLE coupon;
CREATE TABLE coupon(
  coupon_no INT PRIMARY KEY AUTO_INCREMENT, -- 고유키
  coupon_code VARCHAR(255) NOT NULL UNIQUE, -- 쿠폰 코드(UUID포맷-32자리)
  coupon_price INT NOT NULL, -- 쿠폰 가격
  member_id VARCHAR(255) DEFAULT NULL UNIQUE -- 회원아이디
  -- FOREIGN KEY(member_id)
  -- REFERENCES member(member_id)
    -- ON UPDATE CASCADE
    -- ON DELETE CASCADE
);
INSERT INTO coupon VALUES(NULL, '123e4567-e89b-12d3-a456-556642440000', 10, 'hong');
SELECT * FROM coupon;

-- 상품정보
DROP TABLE if EXISTS item;
CREATE TABLE item(
  item_no INT PRIMARY KEY AUTO_INCREMENT, -- 고유키
  item_code VARCHAR(255) NOT NULL UNIQUE, -- 상품 코드(UUID포맷-32자리)
  item_name VARCHAR(255) NOT NULL, -- 상품이름
	item_option_color VARCHAR(255) NULL, -- 색상
	item_option_size VARCHAR(255) NULL, -- 사이즈
	item_price INT(255) NOT NULL, -- 가격
	item_image_url TEXT , -- 이미지 NOT NULL 임시로 주석처리
	item_info TEXT, -- 상품설명
	item_info_image_url_1 TEXT, -- 상세 이미지1
	item_info_image_url_2 TEXT, -- 상세 이미지2
	item_update_datetime DATETIME DEFAULT NOW() -- 작성/수정 시간
);
INSERT INTO item VALUES(null, '123e4567-e89b-12d3-a456-556642440000', '아크네데님 연청', 'blue', 'S,M,L', '35000', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png', NULL, 'https://img.pagekin.com/1/2013/201902/3f2f4d31d66c3c923be883b35d63f9c5.jpg', 'https://img.pagekin.com/1/2013/201902/8bd6d93cd4a589207d9b57e4ad18bd92.jpg', default);
SELECT * FROM item;

-- 장바구니
DROP TABLE if EXISTS cart;
-- 장바구니 담기(회원,비회원)
CREATE TABLE cart (
	cart_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
  cart_code VARCHAR(255) NOT NULL UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
	member_id VARCHAR(255) NULL, -- 아이디(회원)
  session_id VARCHAR(255) NULL, -- 세션아이디(비회원) 예)32자리 - 3CB361E0BE1A9A7DE7DB926DF0772BAE
  item_code VARCHAR(255) NOT NULL UNIQUE, -- 상품 코드(UUID포맷-32자리)
	item_name TEXT NOT NULL, -- 상품이름
	item_option_color VARCHAR(255) NULL, -- 색상
	item_option_size VARCHAR(255) NULL, -- 사이즈
	cart_item_amount INT(255) NOT NULL, -- 구매갯수
	cart_date DATETIME DEFAULT NOW(), -- 장바구니에 담긴 시간/날짜
  -- FOREIGN KEY(member_id)
   	-- REFERENCES member(member_id)
    -- ON UPDATE CASCADE
    -- ON DELETE CASCADE,
  -- FOREIGN KEY(item_code)
   	-- REFERENCES item(item_code)
    -- ON UPDATE CASCADE
    -- ON DELETE CASCADE
);

INSERT INTO cartList VALUES(NULL, 'hong', '3CB361E0BE1A9A7DE7DB926DF0772BAE', '123e4567-e89b-12d3-a456-556642440000', '퍼프블라우스', '화이트', 'FREE', 1, default);
SELECT * FROM cartList;

-- 구매경로 : 1. 장바구니에 넣고 결제하기 2. 바로 결제하기(1개 장바구니에 넣고 결제)
-- 주문정보
DROP TABLE if EXISTS order;
CREATE TABLE order (
	order_no INT AUTO_INCREMENT PRIMARY KEY, -- 고유키
  -- 구매상품 정보
	cart_code_1 VARCHAR(255) NOT NULL UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
  cart_code_2 VARCHAR(255) UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
  cart_code_3 VARCHAR(255) UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
  cart_code_4 VARCHAR(255) UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
  cart_code_5 VARCHAR(255) UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
  order_total_price INT NOT NULL, -- 주문 총금액
  order_total_count TINYINT NOT NULL, -- 주문 상품 개수
	-- 주문자/수령자 정보
	order_name CHAR(255) NOT NULL, -- 주문자 이름
	order_phone CHAR(255) NOT NULL, -- 주문자 연락처
	order_recipient_name CHAR(255) NOT NULL, -- 수령자 이름
	order_recipient_phone CHAR(255) NOT NULL, -- 수령자 연락처
	order_recipient_addr_number CHAR(255) NOT NULL, -- 수령자 우편번호
	order_recipient_addr_1 CHAR(255) NOT NULL, -- 수령자 기본주소
	order_recipient_addr_2 CHAR(255) NOT NULL, -- 수령자 나머지주소
	-- 적립금 사용
	member_mileage INT DEFAULT 0, -- 사용 마일리지(적립금)
  -- 쿠폰 사용
  member_coupon INT DEFAULT 0, -- 사용 쿠폰 금액
	-- 결제방법
	order_pay_type DEFAULT(255) NOT NULL, -- 휴대폰결제 or 무통장입금 선택
  -- 주문상태
  -- 미입금/주문완료 -> 배송대기 -> 배송중 -> 배송완료, 취소/반품/교환
	order_state VARCHAR(255) NOT NULL DEFAULT '미입금/주문완료', -- 주문상태
	order_datetime DATETIME DEFAULT NOW(), -- 결제시간
  -- FOREIGN KEY(cart_code_1)
  --  	REFERENCES cart(cart_code)
  --   ON UPDATE CASCADE
  --   ON DELETE CASCADE,
  -- FOREIGN KEY(cart_code_2)
  --  	REFERENCES cart(cart_code)
  --   ON UPDATE CASCADE
  --   ON DELETE CASCADE,
  -- FOREIGN KEY(cart_code_3)
  --  	REFERENCES cart(cart_code)
  --   ON UPDATE CASCADE
  --   ON DELETE CASCADE,
  -- FOREIGN KEY(cart_code_4)
  --  	REFERENCES cart(cart_code)
  --   ON UPDATE CASCADE
  --   ON DELETE CASCADE,
  -- FOREIGN KEY(cart_code_5)
  --  	REFERENCES cart(cart_code)
  --   ON UPDATE CASCADE
  --   ON DELETE CASCADE
);

INSERT INTO order
	VALUES (NULL, '123e4567-e89b-12d3-a456-556642440000', NULL, NULL, NULL, NULL, 35000, 1,
          '홍길동', '01022223333', '홍길동엄마', '01044445555', '12345', '서울시 마포구 갈매기동', '나머지주소',
          default, default, NULL, '무통장입금', '입금전', default);
SELECT * FROM order;

-- 리뷰
DROP TABLE if EXISTS review;
CREATE TABLE review (
   review_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
   member_id VARCHAR(255) NOT NULL, -- 아이디(회원) / 비회원은 후기를 달수없음
  item_name VARCHAR(255) NOT NULL UNIQUE, -- 상품 코드(UUID포맷-32자리)
   review_star TINYINT NOT NULL,  -- 별점
  review_content TEXT NULL,  -- 상품후기
   review_image_url TEXT NULL, -- 이미지
   review_datetime DATE DEFAULT NOW(), -- 작성시간
   review_exposure VARCHAR(255) DEFAULT '표시함' -- 노출여부
);
INSERT INTO review
   VALUES (NULL, 'hong', '린넨ops베이지', '5', '가성비 좋아요', NULL, DEFAULT,DEFAULT );
INSERT INTO review
   VALUES (NULL, 'tom', '도트sk오렌지', '4', '배송이 빨라요', NULL, DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, 'hana', '골지원피스', '4.5', '옷이 예뻐요', NULL, DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, 'hong', '린넨ops베이지2', '5', '가성비 좋아요', NULL, DEFAULT,DEFAULT );
SELECT * FROM review;

-- QNA 질문
DROP TABLE if EXISTS qna;
CREATE TABLE qna (
	qna_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
	member_id VARCHAR(255) NULL, -- 회원아이디(회원)
  session_id VARCHAR(255) NULL, -- 세션아이디(비회원)
	qna_type VARCHAR(255) NOT NULL, -- 문의 타입(상품문의)
	qna_name VARCHAR(255) NULL, -- 문의 닉네임
	qna_content TEXT NOT NULL,  -- 문의 내용
	qna_pw VARCHAR(255) NOT NULL, -- 문의 글 비밀번호
	qna_datetime DATETIME DEFAULT NOW() -- 문의 작성시간
  qna_answer_content TEXT DEFAULT NULL, -- 답글 내용
  qna_answer_id VARCHAR(255) DEFAULT NULL, -- 답글단 사람 아이디
  qna_answer_datetime DATETIME DEFAULT NULL -- 답글 작성시간
);
INSERT INTO qna
	VALUES (NULL, 'hong', '3CB361E0BE1A9A7DE7DB926DF0772BAE', '배송문의', '홍길동',
     '배송언제되나요', '1234', default, '안녕하세요 고객님 곧 배송됩니다.', 'admin', '2023-01-16 05:30:01');
SELECT * FROM qna;

-- 공지사항
DROP TABLE notice
CREATE TABLE notice (
	notice_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 게시글 번호
	notice_type VARCHAR(255) NOT NULL,  -- 공지사항 종류(이벤트, 배송지연안내 등등)
	notice_title VARCHAR(255) NOT NULL,  -- 공지사항 제목
	notice_content TEXT NULL,  -- 공지사항 내용
	notice_image_url TEXT NULL, -- 공지사항 첨부이미지 url
	notice_datetime DATETIME DEFAULT NOW() -- 작성시간
);
INSERT INTO notice
	VALUES (NULL, '공지사항', '[필독] 구매 전 꼭 확인해주세요:)', '공지사항 내용', 'https://img.makeshop.co.kr/4/40267/202208/2c64b5922083b21869bbd495352d78f8.jpg', default);
SELECT * FROM notice;
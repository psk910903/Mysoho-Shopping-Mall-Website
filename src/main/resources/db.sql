
-- qna 테이블 준하 --------------------------------------------------------------

DROP TABLE qna;
DROP TABLE qnacomment;

CREATE TABLE `qna`
(
	qna_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	qna_category VARCHAR(255) NOT NULL,
	qna_name VARCHAR(50) NOT NULL,
	qna_title VARCHAR(255) NOT NULL,
	qna_password VARCHAR(255) NOT NULL,
	qna_content TEXT NOT NULL,
	qna_hit INT DEFAULT 0 NOT NULL,
	qna_local_date_time  DATETIME DEFAULT NOW()
);

INSERT INTO qna VALUES (1,'환불문의' , '홍길동', '제목1', 1234 ,'내용1',DEFAULT, DEFAULT);

ALTER TABLE qna CONVERT TO CHARSET UTF8;

CREATE TABLE qnacomment
(
	comment_id       	BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	comment_writer   	VARCHAR(255) NOT NULL,
	comment_content  	VARCHAR(255) NOT NULL,
	comment_qna_id 	BIGINT NOT NULL,
	comment_date     	DATETIME DEFAULT NOW()
);
ALTER TABLE qnaComment CONVERT TO CHARSET UTF8;

INSERT INTO qnaComment VALUES (1,'홍길동','내일배송됩니다','1',DEFAULT);

SELECT * FROM qnacomment;
SELECT * FROM qna;


-- 회원테이블 경빈 --------------------------------------------------------------

DROP TABLE if EXISTS member;
CREATE TABLE member (
  member_no INT PRIMARY KEY AUTO_INCREMENT, -- 고유키
   member_id VARCHAR(255) NOT NULL UNIQUE, -- 회원아이디
   member_pw VARCHAR(255) NOT NULL, -- 비밀번호
   member_name CHAR(255) NOT NULL, -- 회원이름
   member_rating VARCHAR(255) NOT NULL,
   member_email VARCHAR(255) NOT NULL, -- 이메일
   member_phone CHAR(255) NOT NULL, -- 전화번호(중간에 -는 제거)
   member_mileage INT DEFAULT 0, -- 마일리지(적립금)
   member_addr_number CHAR(255) NOT NULL, -- 우편번호
   member_addr1 CHAR(255) NOT NULL, -- 기본주소
   member_addr2 CHAR(255) NOT NULL, -- 나머지 주소
   member_join_datetime DATETIME DEFAULT NOW(),-- 회원가입 날짜
  member_exit_datetime DATETIME DEFAULT NULL, -- 탈퇴 날짜
  member_exited TINYINT DEFAULT 0 -- 탈퇴 여부(0: 회원, 1: 탈퇴 회원)
);
INSERT INTO member VALUES( NULL, 'hong', '1234', '홍길동', 'VIP', 'hong@gmail.com',
  '01022223333', DEFAULT,'54321','파푸아뉴기니','앞바다',default ,default ,default );
INSERT INTO member VALUES( NULL, 'lee', '1234', '이거','일반회원', 'this@gmail.com',
  '01022223333', DEFAULT,'3210','대포동','미사일', default, default, default);
INSERT INTO member VALUES( NULL, 'right', '1234', '맞냐', '일반회원', 'IsThatRight@gmail.com',
  '01022223333', DEFAULT,'12345','금수산태양궁전','정문', default, default, DEFAULT);


  -- 리뷰테이블 은진 --------------------------------------------------------------

  USE mydb;

DROP TABLE if EXISTS review;
CREATE TABLE review (
   review_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
   member_id VARCHAR(255) NOT NULL, -- 아이디(회원) / 비회원은 후기를 달수없음
   item_no VARCHAR(255) NOT NULL UNIQUE, -- 상품 코드(UUID포맷-32자리)
   review_star TINYINT NOT NULL,  -- 별점
   review_content TEXT NULL,  -- 상품후기
   review_image_url TEXT NULL, -- 이미지
   review_datetime DATETIME DEFAULT NOW(), -- 작성시간
   review_exposure VARCHAR(255) DEFAULT '표시함' -- 노출여부
);
INSERT INTO review
   VALUES (NULL, 'titio', '00001', '5', '가성비 좋아요', 'https://img.makeshop.co.kr/1/1371/201902/5ea10187021f32483958eb8c91e943bb.jpg', DEFAULT,DEFAULT );
INSERT INTO review
   VALUES (NULL, 'top', '00002', '4', '배송이 빨라요', 'https://img.makeshop.co.kr/1/1371/201902/29116c10bf21223a5382cfac76b874ed.png', DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, 'adam', '00003', '4.5', '옷이 예뻐요', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, 'opt', '00004', '5', '가성비 좋아요', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png', DEFAULT,DEFAULT );
INSERT INTO review
   VALUES (NULL, 'titio', '00005', '5', '가성비 좋아요', 'https://img.makeshop.co.kr/1/1371/201902/5ea10187021f32483958eb8c91e943bb.jpg', DEFAULT,DEFAULT );
INSERT INTO review
   VALUES (NULL, '성춘향', '00006', '4', '배송이 빨라요', 'https://img.makeshop.co.kr/1/1371/201902/29116c10bf21223a5382cfac76b874ed.png', DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, 'tpt', '00007', '4.5', '옷이 예뻐요', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, '홍길동', '00008', '5', '가성비 좋아요', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png', DEFAULT,DEFAULT );
INSERT INTO review
   VALUES (NULL, 'tpt', '00009', '4.5', '옷이 예뻐요', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT,DEFAULT);
INSERT INTO review
   VALUES (NULL, '김진사', '00010', '5', '가성비 좋아요', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png', DEFAULT,DEFAULT );

SELECT * FROM review;



-- 상품정보 테이블 선교 --------------------------------------------------------------

DROP TABLE if EXISTS item;
CREATE TABLE item(
  item_no INT PRIMARY KEY AUTO_INCREMENT , -- 고유item키
  item_category VARCHAR(255) NOT NULL, -- 카테고리 TOP, BOTTOM, ONE-PIECE, ACC
  item_name VARCHAR(255) NOT NULL, -- 상품이름
	item_option_color VARCHAR(255) NULL, -- 색상
	item_option_size VARCHAR(255) NULL, -- 사이즈
	item_price INT(255) NOT NULL, -- 가격
	item_image_url TEXT , -- 이미지 NOT NULL 임시로 주석처리
	item_info TEXT DEFAULT '', -- 상품설명
	item_info_image_url_1 TEXT, -- 상세 이미지1
	item_info_image_url_2 TEXT, -- 상세 이미지2
	item_update_datetime DATETIME DEFAULT NOW() -- 작성/수정 시간
);
ALTER TABLE `item` AUTO_INCREMENT=20000;

INSERT INTO item VALUES(NULL, 'BOTTOM', '아크네데님 연청', '연청', 'S,M,L', '35000', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png',DEFAULT , 'https://img.pagekin.com/1/2013/201902/3f2f4d31d66c3c923be883b35d63f9c5.jpg', 'https://img.pagekin.com/1/2013/201902/8bd6d93cd4a589207d9b57e4ad18bd92.jpg', DEFAULT);
INSERT INTO item VALUES(NULL, 'TOP', '퍼프 블라우스 퍼플', '퍼플', 'FREE', '49000', 'https://img.makeshop.co.kr/1/1371/201902/115892be102b381a004ea0022421d6e7.png', DEFAULT, 'https://img.pagekin.com/1/2013/201902/3f2f4d31d66c3c923be883b35d63f9c5.jpg', 'https://img.pagekin.com/1/2013/201902/8bd6d93cd4a589207d9b57e4ad18bd92.jpg', DEFAULT);
INSERT INTO item VALUES(NULL, 'ONE-PIECE', '골지원피스', '블랙', 'FREE', '32000', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT, 'https://img.pagekin.com/1/2013/201902/3f2f4d31d66c3c923be883b35d63f9c5.jpg', 'https://img.pagekin.com/1/2013/201902/8bd6d93cd4a589207d9b57e4ad18bd92.jpg', DEFAULT);


-- 장바구니 테이블 선교 --------------------------------------------------------------

DROP TABLE if EXISTS cart;
-- 장바구니 담기(회원,비회원)
CREATE TABLE cart (
	cart_no BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
   cart_code VARCHAR(255) NOT NULL UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
	member_id VARCHAR(255) NULL, -- 아이디(회원)
   session_id VARCHAR(255) NULL, -- 세션아이디(비회원) 예)32자리 - 3CB361E0BE1A9A7DE7DB926DF0772BAE
   item_code VARCHAR(255) NOT NULL, -- UNIQUE  상품 코드(UUID포맷-32자리)
	item_name TEXT NOT NULL, -- 상품이름
	item_option_color VARCHAR(255) NULL, -- 색상
	item_option_size VARCHAR(255) NULL, -- 사이즈
	cart_item_amount BIGINT(255) DEFAULT 1, -- 구매갯수
	cart_date DATETIME DEFAULT NOW() -- 장바구니에 담긴 시간/날짜
  -- FOREIGN KEY(member_id)
   	-- REFERENCES member(member_id)
    -- ON UPDATE CASCADE
    -- ON DELETE CASCADE,
  -- FOREIGN KEY(item_code)
   	-- REFERENCES item(item_code)
    -- ON UPDATE CASCADE
    -- ON DELETE CASCADE
);
ALTER TABLE `cart` AUTO_INCREMENT=10000;

INSERT INTO `cart` VALUES(NULL, '22222', 'hong', NULL , '22222', '퍼프블라우스', '화이트', 'FREE', 1, DEFAULT);
SELECT * FROM `cart`;

-- 구매경로 : 1. 장바구니에 넣고 결제하기 2. 바로 결제하기(1개 장바구니에 넣고 결제)


-- 주문정보 테이블 선교 --------------------------------------------------------------

DROP TABLE if EXISTS `order`;
CREATE TABLE `order` (
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
	order_pay_type VARCHAR(255) DEFAULT '휴대폰결제' NOT NULL, -- 휴대폰결제 or 무통장입금 선택
  -- 주문상태
  -- 미입금/주문완료 -> 배송대기 -> 배송중 -> 배송완료, 취소/반품/교환
	order_state VARCHAR(255) NOT NULL DEFAULT '미입금/주문완료', -- 주문상태
	order_datetime DATETIME DEFAULT NOW() -- 결제시간
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
ALTER TABLE `order` AUTO_INCREMENT=10000;


INSERT INTO `order`
	VALUES (NULL, '22222', NULL, NULL, NULL, NULL, 35000, 1,
          '홍길동', '01022223333', '홍길동엄마', '01044445555', '12345', '서울시 마포구 갈매기동', '나머지주소',
          default, default, '무통장입금', '입금전', default);
SELECT * FROM `order`;


-- 상품문의 테이블 은진&희진 --------------------------------------------------------------

DROP TABLE inquiry;

create table inquiry(
   inquiry_no BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
   member_id VARCHAR(255) NOT NULL,
   item_no INT NOT NULL,
   inquiry_title VARCHAR(255) NOT NULL,
   inquiry_content VARCHAR(255) NOT NULL,
   inquiry_hit INT DEFAULT 0,
   inquiry_date DATETIME DEFAULT NOW()
);
INSERT INTO inquiry VALUE(NULL,'홍길동', '0001','배송언제 오나요?','배송빨리빨리',DEFAULT,DEFAULT);
INSERT INTO inquiry VALUE(NULL,'tom', '0002','제품사이즈문의요?','정사이즈인가요',DEFAULT,DEFAULT);
INSERT INTO inquiry VALUE(NULL,'성춘향', '0003','색깔문의요?','스크린이랑 같나요',DEFAULT,DEFAULT);
SELECT * FROM inquiry;

DROP TABLE inquiry_reply;

create table inquiry_reply(
   inquiry_reply_no BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
   inquiry_reply_content TEXT NOT NULL,
   inquiry_reply_date DATETIME DEFAULT NOW(),
   inquiry_no BIGINT NOT NULL
);

SELECT * FROM inquiry_reply;
INSERT INTO inquiry_reply VALUE(NULL,'노력중요',DEFAULT,1);
INSERT INTO inquiry_reply VALUE(NULL,'표준사이즈입니다',DEFAULT,2);
INSERT INTO inquiry_reply VALUE(NULL,'똑같아요',DEFAULT,3);

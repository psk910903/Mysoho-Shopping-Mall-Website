
-- qna 테이블 준하 --------------------------------------------------------------

DROP TABLE if EXISTS qna;
DROP TABLE if EXISTS qnacomment;

CREATE TABLE `qna`
(
	qna_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	member_id VARCHAR(255) NOT NULL,
	qna_category VARCHAR(255) NOT NULL,
	qna_name VARCHAR(50) NOT NULL,
	qna_title VARCHAR(255) NOT NULL,
	qna_password VARCHAR(255) NOT NULL,
	qna_content TEXT NOT NULL,
    qna_secret  VARCHAR(255) NOT NULL,
	qna_hit INT DEFAULT 0 NOT NULL,
	qna_local_date_time  DATETIME DEFAULT NOW()
);


INSERT INTO qna VALUES (1,'hong','환불문의' , '홍길동', '제목1', '1234' ,'내용1','비공개',DEFAULT, DEFAULT);

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
   member_rate VARCHAR(255) NOT NULL DEFAULT '일반', --회원등급
   member_email VARCHAR(255) NOT NULL, -- 이메일
   member_phone CHAR(255) NOT NULL, -- 전화번호(중간에 -는 제거)
   member_mileage INT DEFAULT 0, -- 마일리지(적립금)
   member_addr_number CHAR(255) NOT NULL, -- 우편번호
   member_addr1 CHAR(255) NOT NULL, -- 기본주소
   member_addr2 CHAR(255) NOT NULL, -- 나머지 주소
   member_join_datetime DATETIME DEFAULT NOW(),-- 회원가입 날짜
   member_exit_datetime DATETIME DEFAULT NULL, -- 탈퇴 날짜
  	member_role CHAR(10) DEFAULT '일반',
   member_exited VARCHAR(255) DEFAULT '회원' -- 탈퇴 여부(0: 회원, 1: 탈퇴 회원)
);
-- role을 테이블에 넣었을 때 데이터 입력값
INSERT INTO member VALUES( NULL, 'hong', '1234', '홍길동', 'VIP', 'hong@gmail.com',
  '01022223333', DEFAULT,'54321','파푸아뉴기니','앞바다',DEFAULT ,DEFAULT, DEFAULT ,DEFAULT );
INSERT INTO member VALUES( NULL, 'lee', '1234', '이거','일반', 'this@gmail.com',
  '01022223333', DEFAULT,'3210','대포동','미사일', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO member VALUES( NULL, 'right', '1234', '맞냐', '일반', 'IsThatRight@gmail.com',
  '01022223333', DEFAULT,'12345','금수산태양궁전','정문', DEFAULT, DEFAULT, DEFAULT , DEFAULT);
INSERT INTO member VALUES( NULL, 'ADMIN1', '1234', '관리자','VIP', 'ADMIN_EMAIL',
  'ADMIN_NUM', DEFAULT,'ADMIN_POST','ADMIN_ADDR1','ADMIN_ADDR2', default, DEFAULT,'관리자', default);

SELECT * FROM member;
-- 리뷰테이블 은진 --------------------------------------------------------------


DROP TABLE if EXISTS review;

CREATE TABLE review (
review_no INT AUTO_INCREMENT PRIMARY KEY, -- 고유키
member_id VARCHAR(255) NOT NULL, -- 아이디(회원) / 비회원은 후기를 달수없음
item_no VARCHAR(255) NOT NULL UNIQUE, -- 상품 코드(UUID포맷-32자리)
review_star TINYINT NOT NULL, -- 별점
review_content TEXT NULL, -- 상품후기
review_image_url TEXT NULL, -- 이미지
review_datetime DATETIME DEFAULT NOW(), -- 작성시간
review_exposure VARCHAR(255) DEFAULT '노출함' -- 노출여부
);

INSERT INTO review
VALUES (NULL, 'hong', '20005', '5', '가성비 좋아요', 'https://img.makeshop.co.kr/1/1371/201902/5ea10187021f32483958eb8c91e943bb.jpg', DEFAULT,DEFAULT );
INSERT INTO review
VALUES (NULL, 'lee', '20006', '4', '배송이 빨라요', 'https://img.makeshop.co.kr/1/1371/201902/29116c10bf21223a5382cfac76b874ed.png', DEFAULT,DEFAULT);
INSERT INTO review
VALUES (NULL, 'right', '20007', '4.5', '옷이 예뻐요', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT,DEFAULT);
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
	item_discount_rate INT(255) NOT NULL, -- 할인율
	item_image_url TEXT , -- 이미지 NOT NULL
	item_info TEXT DEFAULT '', -- 상품설명
	item_exposure VARCHAR(255) DEFAULT '노출함', -- 노출여부
	item_sold_out TEXT DEFAULT '판매중', -- 품절여부 soldout
	item_update_datetime DATETIME DEFAULT NOW() -- 작성/수정 시간
);
ALTER TABLE `item` AUTO_INCREMENT=20000;

INSERT INTO item VALUES(20001, 'TOP', '퍼프 블라우스 화이트', '화이트', 'FREE', '49000', '9', 'https://img.makeshop.co.kr/1/1371/201902/d6a6527987d660f2b72290920ce2a33c.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20002, 'TOP', '퍼프 블라우스 퍼플', '퍼플', 'FREE', '49000', '9', 'https://img.makeshop.co.kr/1/1371/201902/115892be102b381a004ea0022421d6e7.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20003, 'ACC', '브라운 사각 bag', '브라운', 'FREE', '32000', '0', 'https://img.makeshop.co.kr/1/1371/201902/0609ced86595538cd42aa85ee6f4c45f.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20004, 'ONE-PIECE', '린넨 ops 베이지', '베이지', 'S,M,L', '49000', '10', 'https://img.makeshop.co.kr/1/1371/201902/5ea10187021f32483958eb8c91e943bb.jpg', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20005, 'BOTTOM', '도트 sk', '베이지, 오렌지', 'FREE', '42000', '0', 'https://img.makeshop.co.kr/1/1371/201902/29116c10bf21223a5382cfac76b874ed.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20006, 'ONE-PIECE', '린넨 ops 베이지', '화이트', 'S,M,L', '49000', '10', 'https://img.makeshop.co.kr/1/1371/201902/242ced7785d1eeaec029be9457bab3e8.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20007, 'ONE-PIECE', '골지원피스', '블랙, 베이지', 'FREE', '32000', '0', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20008, 'BOTTOM', '아크네데님 연청', '연청', 'S,M,L', '35000', '0', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(20009, 'TOP', '골지 브이티', '블랙', 'FREE', '19900', '0', 'https://img.makeshop.co.kr/1/1371/201902/81f5662d13dcab1ce7ee6fd978bbb5f5.png', DEFAULT, DEFAULT, '품절', DEFAULT);

SELECT * FROM item

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

INSERT INTO `cart` VALUES(NULL, '22222', 'hong', NULL , '20001', '퍼프블라우스', '화이트', 'FREE', 1, DEFAULT);
SELECT * FROM `cart`;
INSERT INTO `cart` VALUES(NULL, '33333', 'lee', NULL , '20001', '퍼프블라우스', '화이트', 'FREE', 1, DEFAULT);
SELECT * FROM `cart`;
INSERT INTO `cart` VALUES(NULL, '44444', 'right', NULL , '20001', '퍼프블라우스', '화이트', 'FREE', 1, DEFAULT);
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
          INSERT INTO `order`
	VALUES (NULL, '33333', NULL, NULL, NULL, NULL, 35000, 1,
          '둘리', '01098765432', '둘리엄마', '01012345678', '54321', '제주 서귀포시 토평동', '산15-1',
          default, default, '휴대폰결제', '입금완료', default);

SELECT * FROM `order`;


-- 상품문의 테이블 은진&희진 --------------------------------------------------------------

DROP TABLE if EXISTS inquiry;

create table inquiry(
   inquiry_no BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
   member_id VARCHAR(255) NOT NULL,
   item_no INT NOT NULL,
   inquiry_title VARCHAR(255) NOT NULL,
   inquiry_content VARCHAR(255) NOT NULL,
   inquiry_secret  VARCHAR(255) NOT NULL,
   inquiry_hit INT DEFAULT 0,
   inquiry_date DATETIME DEFAULT NOW()
);
INSERT INTO inquiry VALUE(NULL,'hong', '20002','배송언제 오나요?','배송빨리빨리','비공개',DEFAULT,DEFAULT);
INSERT INTO inquiry VALUE(NULL,'lee', '20003','제품사이즈문의요?','정사이즈인가요','비공개',DEFAULT,DEFAULT);
INSERT INTO inquiry VALUE(NULL,'right', '20004','색깔문의요?','스크린이랑 같나요','공개',DEFAULT,DEFAULT);
SELECT * FROM inquiry;



-- 상품문의 답변 ---------------------------------------------
DROP TABLE if EXISTS inquiry_reply;
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


-- 공지사항 테이블 희진 --------------------------------------------------------------
DROP TABLE if EXISTS notice;

CREATE TABLE notice (
	notice_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 게시글 번호
	notice_type VARCHAR(255) NOT NULL,  -- 공지사항 종류(이벤트, 배송지연안내 등등)
	notice_title VARCHAR(255) NOT NULL,  -- 공지사항 제목
	notice_content TEXT NULL,  -- 공지사항 내용
	notice_datetime DATETIME DEFAULT NOW() -- 작성시간
);
INSERT INTO notice
	VALUES (NULL, '공지사항', '공지[필독] 구매 전 꼭 확인해주세요:)', '공지사항 내용', default);
SELECT * FROM notice;
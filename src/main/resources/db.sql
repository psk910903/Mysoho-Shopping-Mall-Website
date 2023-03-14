
use mydb;
-- qna 테이블 준하 --------------------------------------------------------------

DROP TABLE if EXISTS qna;
DROP TABLE if EXISTS qnacomment;

-- 0306 이준하 테이블 수정 (NOT NULL 부분)
CREATE TABLE `qna`
(
	qna_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	member_id VARCHAR(255) , -- NOT NULL
	qna_category VARCHAR(255) NOT NULL,
	qna_name VARCHAR(50) ,-- NOT NULL
	qna_password VARCHAR(255) NOT NULL,
	qna_content TEXT NOT NULL,
	qna_secret  VARCHAR(255) NOT NULL,
	qna_local_date_time  DATETIME DEFAULT NOW()
);


INSERT INTO qna VALUES (null,'jeong','상품문의' , '정희진', '1234' ,'안녕하세요.','공개', DEFAULT);
INSERT INTO qna VALUES (null,'jeong','상품문의' , '정희진', '1234' ,'안녕하세요.','비공개', DEFAULT);
INSERT INTO qna VALUES (null,null,'상품문의' , '닉네임', '1234' ,'안녕하세요.','공개', DEFAULT);
INSERT INTO qna VALUES (null,null,'상품문의' , '닉네임', '1234' ,'안녕하세요.','비공개', DEFAULT);

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

INSERT INTO qnaComment VALUES (NULL,'홍길동','내일배송됩니다','1',DEFAULT);

SELECT * FROM qnacomment;
SELECT * FROM qna;


-- 회원테이블 경빈 --------------------------------------------------------------
DROP TABLE if EXISTS member;
CREATE TABLE member (
  member_no INT PRIMARY KEY AUTO_INCREMENT, -- 고유키
   member_id VARCHAR(255) NOT NULL UNIQUE, -- 회원아이디
   member_pw VARCHAR(255) NOT NULL, -- 비밀번호
   member_name CHAR(255) NOT NULL, -- 회원이름
   member_rate VARCHAR(255) NOT NULL,
   member_email VARCHAR(255) NOT NULL UNIQUE, -- 이메일
   member_phone CHAR(255) NOT NULL UNIQUE, -- 전화번호(중간에 -는 제거)
   member_mileage INT DEFAULT 0, -- 마일리지(적립금)
   member_coupon INT DEFAULT 0, --  쿠폰
   member_addr_number CHAR(255) NOT NULL, -- 우편번호
   member_addr1 CHAR(255) NOT NULL, -- 기본주소
   member_addr2 CHAR(255) NOT NULL, -- 나머지 주소
   member_join_datetime DATETIME DEFAULT NOW(),-- 회원가입 날짜
   member_exit_datetime DATETIME DEFAULT NULL, -- 탈퇴 날짜
   member_role CHAR(10) DEFAULT 'ROLE_USER',
   member_exited VARCHAR(255) DEFAULT '회원' -- 탈퇴 여부(0: 회원, 1: 탈퇴 회원)
);
-- role을 테이블에 넣었을 때 데이터 입력값
INSERT INTO member VALUES( NULL, 'hong', '1234', '홍길동', 'VIP', 'hong@gmail.com',
  '01022223333', DEFAULT,DEFAULT,'54321','파푸아뉴기니','앞바다',DEFAULT ,DEFAULT, DEFAULT ,DEFAULT );
INSERT INTO member VALUES( NULL, 'lee', '1234', '이거','일반회원', 'this@gmail.com',
  '01022223334', DEFAULT,DEFAULT,'3210','대포동','미사일', DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO member VALUES( NULL, 'right', '1234', '맞냐', '일반회원','IsThatRight@gmail.com',
  '01022223335', DEFAULT,DEFAULT,'12345','금수산태양궁전','정문', DEFAULT, DEFAULT, DEFAULT , DEFAULT);
INSERT INTO member VALUES( NULL, 'ADMIN1', '1234', '관리자','VIP', 'ADMIN_EMAIL',
  'ADMIN_NUM', DEFAULT,DEFAULT,'ADMIN_POST','ADMIN_ADDR1','ADMIN_ADDR2', default, DEFAULT,'관리자', default);

SELECT * FROM member;
-- 리뷰테이블 은진 --------------------------------------------------------------


DROP TABLE if EXISTS review;

CREATE TABLE review (
review_no INT AUTO_INCREMENT PRIMARY KEY, -- 고유키
member_id VARCHAR(255) NOT NULL, -- 아이디(회원) / 비회원은 후기를 달수없음
item_no VARCHAR(255) NOT NULL, -- 상품 코드(UUID포맷-32자리)
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
VALUES (NULL, 'right', '20007', '4', '옷이 예뻐요', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT,DEFAULT);

INSERT INTO review
VALUES (NULL, 'hong', '20005', '5', '가성비 좋아요2', 'https://img.makeshop.co.kr/1/1371/201902/5ea10187021f32483958eb8c91e943bb.jpg', DEFAULT,DEFAULT );
INSERT INTO review
VALUES (NULL, 'right', '20005', '4', '코딩개어려워요', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', DEFAULT,DEFAULT);
INSERT INTO review
VALUES (NULL, 'right', '20005', '3', '사진이 없어요', null, DEFAULT,DEFAULT);
INSERT INTO review
VALUES (NULL, 'right', '20005', '3', '사진이 없어요2', null, DEFAULT,DEFAULT);
INSERT INTO review
VALUES (NULL, 'right', '20005', '3', '사진이 없어요3', null, DEFAULT,DEFAULT);


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

INSERT INTO item VALUES(NULL, 'TOP', '퍼프 블라우스 화이트', '화이트', 'FREE', '49000', '9', 'https://img.makeshop.co.kr/1/1371/201902/d6a6527987d660f2b72290920ce2a33c.png', '<h2>&nbsp;</h2><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p><strong>퍼프 블라우스 (바지는 개인 소장입니다)</strong></p><p><br>&nbsp;</p><p>아이보리,소라 / 44,500원</p><p>상세 사이즈 free</p><p>[어깨 37 소매 57.5 가슴 47 총기장 57]</p><p><br>&nbsp;</p><p>사랑스러운 레이스 블라우스를 소개합니다!</p><p>시스루 디테일과 플로럴 레이스 디테일로 보자마자</p><p>너무너무 예뻤던 블라우스에요!</p><p>치마, 바지 모두 자연스럽게 연출가능하구요,</p><p>요즘 결혼식이 굉장히 많은데 소라색은</p><p>하객룩으로도 좋아요!</p><p>개인적으로 소라색이 넘 예쁜,,</p><p><br>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/75304a73-3564-4e81-a7fb-473378ba0bcc.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/6173131b-3569-4a40-b1d4-83c4fb541895.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/6933eb07-2566-4dc0-9865-fb1e1c09a442.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/5485ac7f-f84f-48c9-962a-81542f2f983e.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/9ebaa38e-0bce-4ebe-baa5-59408e289f7e.png"></figure><p><br>&nbsp;</p><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/2cda7ed8-99f5-40a2-a17d-c223e3bcd458.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/7b04fc0c-0818-4c0a-9859-924fe3e50af8.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/d6539351-b0f2-43c5-813d-61da23745b11.png"></figure><p>&nbsp;</p><p>&nbsp;위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p><br>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'TOP', '퍼프 블라우스 퍼플', '퍼플', 'FREE', '49000', '9', 'https://img.makeshop.co.kr/1/1371/201902/115892be102b381a004ea0022421d6e7.png', '<h2>&nbsp;</h2><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p><strong>퍼프 블라우스 (바지는 개인 소장입니다)</strong></p><p><br>&nbsp;</p><p>아이보리,소라 / 44,500원</p><p>상세 사이즈 free</p><p>[어깨 37 소매 57.5 가슴 47 총기장 57]</p><p><br>&nbsp;</p><p>사랑스러운 레이스 블라우스를 소개합니다!</p><p>시스루 디테일과 플로럴 레이스 디테일로 보자마자</p><p>너무너무 예뻤던 블라우스에요!</p><p>치마, 바지 모두 자연스럽게 연출가능하구요,</p><p>요즘 결혼식이 굉장히 많은데 소라색은</p><p>하객룩으로도 좋아요!</p><p>개인적으로 소라색이 넘 예쁜,,</p><p><br>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/75304a73-3564-4e81-a7fb-473378ba0bcc.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/6173131b-3569-4a40-b1d4-83c4fb541895.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/6933eb07-2566-4dc0-9865-fb1e1c09a442.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/5485ac7f-f84f-48c9-962a-81542f2f983e.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/9ebaa38e-0bce-4ebe-baa5-59408e289f7e.png"></figure><p><br>&nbsp;</p><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/2cda7ed8-99f5-40a2-a17d-c223e3bcd458.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/7b04fc0c-0818-4c0a-9859-924fe3e50af8.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/d6539351-b0f2-43c5-813d-61da23745b11.png"></figure><p>&nbsp;</p><p>&nbsp;위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p><br>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'ACC', '브라운 사각 bag', '브라운', 'FREE', '32000', '0', 'https://img.makeshop.co.kr/1/1371/201902/0609ced86595538cd42aa85ee6f4c45f.png', '<p>&nbsp;</p><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/20519a2c-1599-4108-914d-abc69afbede9.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/8cf49d49-8189-4e6b-b1ad-1145409c7080.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/608829fd-8ee7-4b49-ab6a-485e8b21dbea.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/e1bf6166-4faf-4162-b23e-b3f70eed3d7c.jpg"></figure><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/0d469d25-7018-4867-a8b5-3a07e69805cd.png"></figure><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'ONE-PIECE', '린넨 ops 베이지', '베이지', 'S,M,L', '49000', '10', 'https://img.makeshop.co.kr/1/1371/201902/5ea10187021f32483958eb8c91e943bb.jpg', '<figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/83648d7d-ea0d-4446-af00-8860b48c9e4f.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/396b9f97-16e2-4eb2-b700-1c3ee9551322.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/554ae3b8-1720-4764-b26d-f55f58d4db25.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/465b2c6f-d501-4dcc-b212-82c5276e3874.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/9c6bb5f6-b35c-43a5-a383-1711753a43bc.png"></figure><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'BOTTOM', '도트 sk', '베이지,오렌지', 'FREE', '42000', '0', 'https://img.makeshop.co.kr/1/1371/201902/29116c10bf21223a5382cfac76b874ed.png', '<figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/4b176fdc-6a5d-4d77-871d-37ccc363421c.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/164f45c6-5905-4ea6-85d0-9b155691405c.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/cabb8661-5962-45df-877b-c28e068c7fb3.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/c9a84bbe-3a95-4ead-bfc8-4d7cda7a6aa9.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/f0619d89-6c44-4dfd-9c17-3e490a60e8f7.png"></figure><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'ONE-PIECE', '린넨 ops 화이트', '화이트', 'S,M,L', '49000', '10', 'https://img.makeshop.co.kr/1/1371/201902/242ced7785d1eeaec029be9457bab3e8.png', '<figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/c50bb076-12b7-4660-a769-98e2aec0662e.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/3041a6c4-cf93-4328-9095-7a2a2b922a46.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/e2310ed3-b705-4de9-87d1-c1f4b4ba4893.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/94c3fc80-9416-4b12-b203-1adb3292b846.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/72d168ee-8c4a-4a94-b20e-3c32412725e7.png"></figure><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'ONE-PIECE', '골지원피스', '블랙,베이지', 'FREE', '32000', '0', 'https://img.makeshop.co.kr/1/1371/201902/2aaac2b5fedefa92510d799e9a151db1.png', '<figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/07852db4-cee1-4942-ae46-6a25d971832e.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/4add3929-7c35-4757-aff6-b884c42627f2.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/c4a1589a-81fa-4558-bc89-99d967b494ae.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/1e610c50-2771-4922-9d86-2f2ff5c241a7.png"></figure><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/ab5b5276-3cab-49d6-bc31-763c29133f77.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/75709640-d5f5-468f-aaf6-3cd2db084b12.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/426d7ec5-2bf2-409a-bbf9-dddc6f23a8b3.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/29789ea0-9cd2-4317-a5d9-805017457df0.png"></figure><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'BOTTOM', '아크네데님 연청', '연청', 'S,M,L', '35000', '0', 'https://img.makeshop.co.kr/1/1371/201902/15dac27e8d767f405ecd84786413ad19.png', '<figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/626dade6-dd35-4c9f-a7ff-6bd44f50d6ae.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/8c2b8966-9acc-4b70-950f-78abb7148e62.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/291a27ee-b8cc-45b1-a75d-70514990bfb1.png"></figure><p>&nbsp;</p><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/06ef1bab-e6bf-45cb-ba9f-afd27060452f.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/2a3c2709-f68c-423b-9eba-0342867ec835.png"></figure><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO item VALUES(NULL, 'TOP', '골지 브이티', '블랙', 'FREE', '19900', '0', 'https://img.makeshop.co.kr/1/1371/201902/81f5662d13dcab1ce7ee6fd978bbb5f5.png', '<figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/b0168fb5-e9ca-49bf-80b5-0d4be8afae8a.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/4c3b86b0-66bf-4824-abd2-e72c5619dc70.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/78b59d18-6d82-4d1c-89fc-d4a909ba749e.png"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/303a1b2f-2e60-4556-824f-8bd9c9c273ba.jpg"></figure><p>&nbsp;</p><figure class="image"><img src="https://s3-doreen-bucket.s3.ap-northeast-2.amazonaws.com/97b78b4b-e5cb-4e45-8455-4d20f57c11eb.png"></figure><p>&nbsp;</p><p>위 컬러가 모두 뚜렷하게 구분이 되는지 확인하세요.</p><p>제품의 정확한 정보 표현을 위하여</p><p>모니터의 밝기 및 명암 감마 조정을 권장합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>', DEFAULT, '품절', DEFAULT);

SELECT * FROM item;

SELECT item_image_url FROM `item` WHERE item_no LIKE '20001' and item_exposure='노출함' order BY `item_name` DESC;

-- 장바구니 테이블 선교 --------------------------------------------------------------
DROP TABLE if EXISTS cart;
-- 장바구니 담기(회원,비회원)
CREATE TABLE cart (
   cart_no BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
   cart_code VARCHAR(255) NOT NULL UNIQUE, -- 장바구니 코드(UUID포맷-32자리)
   order_code BIGINT, -- 주문정보 PK
   member_id VARCHAR(255) NULL, -- 아이디(회원)
   session_id VARCHAR(255) NULL, -- 세션아이디(비회원) 예)32자리 - 3CB361E0BE1A9A7DE7DB926DF0772BAE
   item_code VARCHAR(255) NOT NULL, -- 상품 코드
   item_name TEXT NOT NULL, -- 상품이름
   item_option_color VARCHAR(255) NULL, -- 색상
   item_option_size VARCHAR(255) NULL, -- 사이즈
   cart_item_amount BIGINT(255) DEFAULT 1, -- 구매갯수
  cart_item_original_price BIGINT(255), -- 할인전가격(ex.49000원)
   cart_discount_price BIGINT(255), -- 할인율이 적용된 차감될 금액(ex.4900원)
   cart_item_price BIGINT(255), -- 상품가격(ex.44100원)
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

INSERT INTO `cart` VALUES(NULL, '22222', '10000','hong', NULL , '20000', '퍼프블라우스', '화이트', 'FREE', 1, '49000', '4500', '44500',DEFAULT);
INSERT INTO `cart` VALUES(NULL, '22223', '10000','hong', NULL , '20001', '퍼프블라우스', '퍼플', 'FREE', 1, '49000', '4500', '44500', DEFAULT);
INSERT INTO `cart` VALUES(NULL, '33333', '10001','lee', NULL , '20000', '퍼프블라우스', '화이트', 'FREE', 1, '49000', '4500', '44500', DEFAULT);
INSERT INTO `cart` VALUES(NULL, '44444', '10000','right', NULL , '20000', '퍼프블라우스', '화이트', 'FREE', 1, '49000', '4500', '44500', DEFAULT);
INSERT INTO `cart` VALUES(NULL, '11111', '10002', NULL, NULL , '20000', '퍼프블라우스', '화이트', 'FREE', 1, '49000', '4500', '44500',DEFAULT);
INSERT INTO `cart` VALUES(NULL, '11112', '10002', NULL, NULL , '20001', '퍼프블라우스', '퍼플', 'FREE', 1, '49000', '4500', '44500', DEFAULT);

INSERT INTO `cart` VALUES(NULL, '11113', '10003', NULL, NULL , '20002', '브라운 사각 bag', '브라운', 'FREE', 1, '32000', '0', '32000',DEFAULT);
INSERT INTO `cart` VALUES(NULL, '11114', '10003', NULL, NULL , '20003', '린넨 ops 베이지', '베이지', 'S', 1, '49000', '4900', '44100', DEFAULT);

INSERT INTO `cart` VALUES(NULL, '11121', '10004', 'psk910903', NULL , '20000', '퍼프블라우스', '화이트', 'FREE', 1, '49000', '4500', '44500',DEFAULT);
INSERT INTO `cart` VALUES(NULL, '11122', '10004', 'psk910903', NULL , '20001', '퍼프블라우스', '퍼플', 'FREE', 1, '49000', '4500', '44500', DEFAULT);

INSERT INTO `cart` VALUES(NULL, '11123', '10005', 'psk910903', NULL , '20002', '브라운 사각 bag', '브라운', 'FREE', 1, '32000', '0', '32000',DEFAULT);
INSERT INTO `cart` VALUES(NULL, '11124', '10005', 'psk910903', NULL , '20003', '린넨 ops 베이지', '베이지', 'S', 1, '49000', '4900', '44100', DEFAULT);

SELECT * FROM `cart`;

SELECT * FROM cart WHERE cart_code = 11124 AND NOT member_id is NULL;

SELECT * FROM cart WHERE member_id = 'psk910903';

SELECT * FROM cart WHERE cart_code = 11121 and member_id is NULL;


-- 구매경로 : 1. 장바구니에 넣고 결제하기 2. 바로 결제하기(1개 장바구니에 넣고 결제)


-- 주문정보 테이블 선교 --------------------------------------------------------------

DROP TABLE if EXISTS `order`;
CREATE TABLE `order` (
  order_no BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유키
  order_code BIGINT NOT NULL UNIQUE,
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
	-- 회원 아이디
	member_id VARCHAR(255) NULL,
	-- 적립금 사용
	member_mileage INT DEFAULT 0, -- 사용 마일리지(적립금)
  -- 쿠폰 사용
  member_coupon INT DEFAULT 0, -- 사용 쿠폰 금액
	-- 결제방법
	order_pay_type VARCHAR(255) DEFAULT '휴대폰결제' NOT NULL, -- 휴대폰결제 or 무통장입금 선택
  -- 주문상태
  -- 미입금/주문완료 -> 배송대기 -> 배송중 -> 배송완료, 취소/반품/교환, 구매확정
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
	VALUES (NULL, '10000','22222', '22223', NULL, NULL, NULL, 35000, 1,
          '홍길동', '01022223333', '홍길동엄마', '01044445555', '12345', '서울시 마포구 갈매기동', '나머지주소', 'hong',
          default, default, '무통장입금', '결제대기', default);
INSERT INTO `order`
	VALUES (NULL, '10001','33333', NULL, NULL, NULL, NULL, 35000, 1,
          '둘리', '01098765432', '둘리엄마', '01012345678', '54321', '제주 서귀포시 토평동', '산15-1', 'lee',
          default, default, '휴대폰결제', '배송대기', default);

INSERT INTO `order`
	VALUES (NULL, '10002','11111', '11112', NULL, NULL, NULL, 35000, 1,
          '박선교', '01040246575', '박선교', '01040246575', '12345', '서울시 마포구 갈매기동', '나머지주소', null,
          default, default, '무통장입금', '결제대기', default);
INSERT INTO `order`
	VALUES (NULL, '10003','11113', '11114', NULL, NULL, NULL, 35000, 1,
          '박선교', '01040246575', '박선교', '01040246575', '12345', '서울시 마포구 갈매기동', '나머지주소', null,
          default, default, '무통장입금', '결제대기', default);

INSERT INTO `order`
	VALUES (NULL, '10004','11121', '11122', NULL, NULL, NULL, 35000, 1,
          '박선교', '01040246575', '박선교', '01040246575', '12345', '서울시 마포구 갈매기동', '나머지주소',
          'psk910903', default, default, '무통장입금', '결제대기', default);


INSERT INTO `order`
	VALUES (NULL, '10005','11123', '11124', NULL, NULL, NULL, 35000, 1,
          '박선교', '01040246575', '박선교', '01040246575', '12345', '서울시 마포구 갈매기동', '나머지주소',
          'psk910903', default, default, '무통장입금', '결제대기', default);
SELECT * FROM `order`;

SELECT * FROM `order` WHERE order_name LIKE CONCAT('%','박선교','%') AND order_phone LIKE CONCAT('%','01040246575','%') and member_id is NULL order BY order_datetime DESC;


-- 상품문의 테이블 은진&희진 --------------------------------------------------------------

DROP TABLE if EXISTS inquiry;

create table inquiry(
   inquiry_no BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
   member_id VARCHAR(255),
   inquiry_nickname VARCHAR(255),
   inquiry_password VARCHAR(255),
   item_no INT NOT NULL,
   inquiry_content VARCHAR(255) NOT NULL,
   inquiry_secret  VARCHAR(255) NOT NULL,
   inquiry_date DATETIME DEFAULT NOW()
);
-- 회원일 때
INSERT INTO inquiry VALUE(NULL,'hong',NULL, NULL, '20002','배송빨리빨리','비공개',DEFAULT);
INSERT INTO inquiry VALUE(NULL,'lee',NULL, NULL, '20003','정사이즈인가요','비공개',DEFAULT);
INSERT INTO inquiry VALUE(NULL,'right',NULL, NULL, '20004','스크린이랑 같나요','공개',DEFAULT);

-- 비회원일 때
INSERT INTO inquiry VALUE(NULL,NULL,'hong', '1234', '20002','배송빨리빨리','비공개',DEFAULT);
INSERT INTO inquiry VALUE(NULL,NULL,'lee', '1234', '20003','정사이즈인가요','비공개',DEFAULT);
INSERT INTO inquiry VALUE(NULL,NULL,'right', '1234', '20004','스크린이랑 같나요','공개',DEFAULT);
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

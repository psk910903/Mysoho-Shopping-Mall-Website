// 공유 아이콘 팝업
function showShareList() {
  document.querySelector(".bg-share").className = "bg-share showShareList";
}
function closeOrderList() {
  document.querySelector(".bg-share").className = "bg-share";
}

// document
//   .querySelector(".show-share-list")
//   .addEventListener("click", showShareList);

function openPopupShareBtn(name) {
  popup = window.open(name);
}
function alertbtn() {
  alert("링크가 복사되었습니다. \n원하는 위치에 붙여넣기하세요. ");
}

// 공유 아이콘 팝업 홈
function showShareListHome() {
  document.querySelector(".bg-share").className = "bg-share showShareListHome";
}
function closeOrderList() {
  document.querySelector(".bg-share").className = "bg-share";
}

// document
//   .querySelector(".show-share-list-home")
//   .addEventListener("click", showShareListHome);

function openPopupShareBtn(name) {
  popup = window.open(name);
}
function alertbtn() {
  alert("링크가 복사되었습니다. \n원하는 위치에 붙여넣기하세요. ");
}

//문의작성 팝업
var popup;
function openPopupQnAWrite() {
  popup = window.open("/user/popup/qna-write");
}

//삼성페이 결제 지원 단말 확인
function openPopupMobile() {
  popup = window.open("https://www.samsung.com/sec/samsung-pay/home/");
}

function openPopUpTop1() {
  popup = window.open("/popup/pop-page-top1.html");
}

function openPopupWrite() {
  popup = window.open("/popup/qna-write");
}

function openPopup(name) {
  popup = window.open(name);
}

//지성 펑션
function openPopUpInquiryWrite(itemNo) {
  popup = window.open("/inquiry/productInquiryWriteForm/"+itemNo);
}

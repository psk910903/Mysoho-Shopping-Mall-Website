// 이용약관, 개인정보처리방침 팝업
function openPopup1() {
  let popup = window.open("/terms/terms/service");
}
function openPopup2() {
  let popup = window.open("/terms/terms/privacy");
}
function openPopup3() {
  popup = window.open("/terms/terms/service/order");
}
function openPopup4() {
  popup = window.open("/terms/terms/policy/order");
}

// mysoho링크 팝업
function openPopupsoho1() {
  popup = window.open("https://mysoho.com/");
}
function openPopupsoho2() {
  popup = window.open("https://sohomarket.kr/?shop=sohonara");
}

//쇼핑몰 사업자정보 버튼
function footerbtn() {
  //
  if (document.querySelector(".hidden").style.display === "block") {
    document.querySelector(".hidden").style.display = "none";
    document.querySelector(".footer-down").style.display = "block";
    document.querySelector(".footer-up").style.display = "none";
  } else {
    document.querySelector(".hidden").style.display = "block";
    document.querySelector(".footer-down").style.display = "none";
    document.querySelector(".footer-up").style.display = "block";
  }
}

//사업자 정보보기 버튼
function footerbtn2() {
  popup = window.open(
    "https://www.ftc.go.kr/bizCommPop.do?wrkr_no=2068121131&apv_perm_no="
  );
}

// 클릭하면 상단으로 이동하는 버튼
// Get the button
//let mybutton = document.getElementById("myBtnfooter");

// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function () {
  scrollFunction();
};

function scrollFunction() {
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
    //mybutton.style.display = "block";
    mybutton.style.opacity = "1";
  } else {
    //mybutton.style.display = "none";
    mybutton.style.opacity = "0";
  }
}

// When the user clicks on the button, scroll to the top of the document
function topFunction() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

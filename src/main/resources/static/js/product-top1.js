function changePage(n) {
  let titarr = document.querySelectorAll(".pagetit");
  for (let i = 0; i < 3; i++) {
    titarr[i].classList.remove("now");
    document.querySelector(".page" + (i + 1)).style.display = "none";
  }
  titarr[n].classList.add("now");
  document.querySelector(".page" + (n + 1)).style.display = "block";
}

function pageOpenClose() {
  //
  if (document.querySelector(".btn-bottom-open1").style.display !== "none") {
    document.querySelector(".btn-bottom-open1").style.display = "none";
    document.querySelector(".btn-bottom-open2").style.display = "none";
    document.querySelector(".btn-bottom-close1").style.display = "block";
    document.querySelector(".btn-bottom-close2").style.display = "block";
    document.querySelector("#overflow").classList.remove("overflow");
  } else {
    document.querySelector(".btn-bottom-open1").style.display = "block";
    document.querySelector(".btn-bottom-open2").style.display = "block";
    document.querySelector(".btn-bottom-close1").style.display = "none";
    document.querySelector(".btn-bottom-close2").style.display = "none";
    document.querySelector("#overflow").classList.add("overflow");
  }
}
function orderInfo(n) {
  if (document.querySelector(".order-hd-open-" + n).style.display !== "none") {
    document.querySelector(".span" + n).style.display = "none";
    document.querySelector(".order-hd-open-" + n).style.display = "none";
    document.querySelector(".order-hd-close-" + n).style.display = "block";
    document.querySelector(".order" + n).style.display = "block";
  } else {
    document.querySelector(".span" + n).style.display = "block";
    document.querySelector(".order-hd-open-" + n).style.display = "block";
    document.querySelector(".order-hd-close-" + n).style.display = "none";
    document.querySelector(".order" + n).style.display = "none";
  }
}

function onclickCbx() {
  if (document.querySelector("#userJoinFormDiv").style.display !== "block") {
    document.querySelector("#userJoinFormDiv").style.display = "block";
  } else {
    document.querySelector("#userJoinFormDiv").style.display = "none";
  }
}

function onclickAgree() {
  if (agreeAllBtn.checked === true) {
    for (let tag of agree) {
      tag.checked = true;
    }
  } else {
    for (let tag of agree) {
      tag.checked = false;
    }
  }
}

function sum() {
  totalPrice += price;
  // 가격 형변환 후 쉼표추가 함수
  let a = String(totalPrice);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector("#price").innerHTML = output + "원";
  //갯수
  amount++;
  document.querySelector(".MSH-sto-stock").value = amount;
}
function sub() {
  if (amount > 1) {
    totalPrice -= price;
    // 가격 형변환 후 쉼표추가 함수
    let a = String(totalPrice);
    var output = [a.slice(0, position), b, a.slice(position)].join("");
    document.querySelector("#price").innerHTML = output + "원";
    //갯수
    amount--;
    document.querySelector(".MSH-sto-stock").value = amount;
  } else {
    document.querySelector(".MSH-sto-stock").value = amount;
    document.querySelector("#price").innerHTML = str;
  }
}
function hiddenSizeTap() {
  document.querySelector(".hidden-option-tap").style.display = "none";
  document.querySelector(".option-total-price").innerHTML = 0;
  document.querySelector(".option-choice").style.display = "block";
  document.querySelector(".option-btn-bg").style.display = "block";
}

function buyBtn() {
  document.querySelector(".bg-option").className = "bg-option";
  document.querySelector(".order-frm").style.display = "block";
  let finalColorResult = document.querySelector(".final-color").innerHTML;
  let finalSizeResult = document.querySelector(".final-size").innerHTML;
  let productCount = document.querySelector(".MSH-sto-stock").value;
  document.querySelector(".final-size-result").innerHTML = finalSizeResult;
  document.querySelector(".final-color-result").innerHTML = finalColorResult;
  document.querySelector("#final-size-result").value = finalSizeResult;
  document.querySelector("#final-color-result").value = finalColorResult;
  document.querySelector("#product-count").innerHTML = productCount;
  document.querySelector("#hidden").style.display = "block";

  document.getElementById("order-first-input").focus();
}

function closeOptionList() {
  if (document.querySelector(".option-choice").style.display === "block") {
    document.querySelector("#hidden").style.display = "none";
  } else {
    let finalColorResult = document.querySelector(".final-color").innerHTML;
    let finalSizeResult = document.querySelector(".final-size").innerHTML;
    let productCount = document.querySelector(".MSH-sto-stock").value;
    document.querySelector(".final-size-result").innerHTML = finalSizeResult;
    document.querySelector(".final-color-result").innerHTML = finalColorResult;
    document.querySelector("#final-size-result").value = finalSizeResult;
    document.querySelector("#final-color-result").value = finalColorResult;
    document.querySelector("#product-count").innerHTML = productCount;
    document.querySelector("#hidden").style.display = "block";
  }
}

// 장바구니 쿠키생성
function basketNumProduct(n) {
  if (
    n == 2 &&
    document.querySelector("#hidden").style.display !== "block" &&
    document.querySelector(".hidden-option-tap").style.display !== "block"
  ) {
    alert("옵션을 선택해주세요");
  } else {
    let itemNo = document.querySelector("#itemNo").value;
    let finalColorResult;
    let finalSizeResult;
    if (n == 1) {
      finalColorResult = document.querySelector("#itemOptionColor").value;
      finalSizeResult = document.querySelector("#itemOptionSize").value;
    } else if (n == 2) {
      finalColorResult = document.querySelector("#final-color").innerHTML;
      finalSizeResult = document.querySelector("#final-size").innerHTML;
    }
    let productCount = document.querySelector(".MSH-sto-stock").value;

    let cookieName = itemNo;
    cookieName += "." + finalColorResult;
    cookieName += "." + finalSizeResult;

    let cookieValue = Number(productCount); //아이템 수량으로 쿠키벨류 초기화
    basketNum += Number(cookieValue);
    document.querySelector(".basket-num").innerHTML = basketNum; //오른 숫자 반영

    if (getCookie("item_idx." + cookieName) == null) {
      //해당 상품이 쿠키로 만들어진적이 없으면 새로 생성하라는 뜻

      setCookie("item_idx." + cookieName, cookieValue);
    } else {
      let getCookieValue = getCookie("item_idx." + cookieName); //  getCookie("item_idx:" + item_idx) => value이다 "2"
      setCookie("item_idx." + cookieName, Number(getCookieValue) + cookieValue); //만약 같은 idx가 있으면 수량만 +1해서 기존 쿠키에 덮어씌운다.
      if (
        confirm(
          "장바구니에 담긴 수량이 있어 수량이 추가되었습니다.\n \n 지금 확인하시겠습니까?"
        ) == true
      ) {
        location.href = "/order";
        //장바구니링크 추가 예정
      } else {
      }
    }
  }
}
var getCookie = function (cookieName) {
  let encodedCookieName = "";
  let splitedCookieName = cookieName.split(".");

  let encodedColor = encodeURIComponent(splitedCookieName[2]);
  for (let idx in splitedCookieName) {
    if (idx == 0) {
      encodedCookieName += splitedCookieName[idx];
    } else if (idx == 2) {
      encodedCookieName += "." + encodedColor;
    } else {
      encodedCookieName += "." + splitedCookieName[idx];
    }
  }

  //매개변수로 쿠키이름을 넣어주면 value로 반환
  var value = document.cookie.match(
    "(^|;) ?" + encodedCookieName + "=([^;]*)(;|$)"
  );
  return value ? value[2] : null; //3항 연산자 : 값이 있으면 가져오고 없으면 null로 반환
};

function sortSelect(count) {
  if (document.querySelector("#sort" + count).style.display === "block") {
    document.querySelector("#sort" + count).style.display = "none";
  } else {
    document.querySelector("#sort" + count).style.display = "block";
  }
}

function onCheckDiv(number) {
  if (document.querySelector("#CheckDiv" + number).style.display === "none") {
    document.querySelector("#CheckDiv" + number).style.display = "block";
  } else {
    document.querySelector("#CheckDiv" + number).style.display = "none";
  }
}

function onCheckDiv2(number) {
  if (document.querySelector("#CheckDiv2" + number).style.display === "none") {
    document.querySelector("#CheckDiv2" + number).style.display = "block";
  } else {
    document.querySelector("#CheckDiv2" + number).style.display = "none";
  }
}

function checkDelete() {
  let form = document.querySelector("#deleteBtn");
  if (confirm("해당 Q&A를 삭제하겠습니까? 삭제 후 복구가 불가능합니다.")) {
    form.submit();
  } else {
    return false;
  }
}

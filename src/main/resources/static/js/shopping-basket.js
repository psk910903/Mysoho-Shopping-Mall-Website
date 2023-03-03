function orderInfo(n) {
  // 결제방법
  if (n === 3) {
    if (
      document.querySelector(".order-hd-open-" + n).style.display !== "none"
    ) {
      document.querySelector(".order-hd-open-" + n).style.display = "none";
      document.querySelector(".order-hd-close-" + n).style.display = "block";
      document.querySelector(".order" + n).style.display = "block";
      document.querySelector(".pay-checked").style.display = "none";
    } else {
      document.querySelector(".order-hd-open-" + n).style.display = "block";
      document.querySelector(".order-hd-close-" + n).style.display = "none";
      document.querySelector(".order" + n).style.display = "none";
      document.querySelector(".pay-checked").style.display = "block";
    }
  } else {
    if (
      document.querySelector(".order-hd-open-" + n).style.display !== "none"
    ) {
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
function selecpay() {
  let pay = document.querySelector("#pay2");
  if (pay.checked === true) {
    document.querySelector("#pay1text").style.display = "none";
    document.querySelector("#pay1color").style.color = "rgb(136, 136, 136)";
    document.querySelector("#pay2color").style.color = "black";
    document.querySelector("#pay2text").style.display = "block";
    document.querySelector("#pay1display").className = "blue fs-14 span3";
    document.querySelector("#pay2display").className =
      "blue fs-14 span3 pay-checked";
  } else {
    document.querySelector("#pay1color").style.color = "black";
    document.querySelector("#pay1text").style.display = "block";
    document.querySelector("#pay2text").style.display = "none";
    document.querySelector("#pay2color").style.color = "rgb(136, 136, 136)";
    document.querySelector("#pay2display").className = "blue fs-14 span3 ";
    document.querySelector("#pay1display").className =
      "blue fs-14 span3 pay-checked";
  }
}

function checkTxt(type, val) {
  // 입력값이 공백일 때
  if (val === "") {
    document.querySelector(".check-txt-" + type).style.display = "block";
    document.querySelector(".check-txt-" + type + "2").style.display = "none";
    document.querySelector(".check-txt-" + type + "3").style.display = "none";
  } else {
    document.querySelector(".check-txt-" + type).style.display = "none";
  }
}
function checkPW(val) {
  // 비밀번호 확인
  if (val === document.querySelector("#check-txt-pw").value) {
    document.querySelector(".check-txt-pwcheck-same").style.display = "block";
    document.querySelector(".check-txt-pwcheck-not").style.display = "none";
  } else {
    document.querySelector(".check-txt-pwcheck-same").style.display = "none";
    document.querySelector(".check-txt-pwcheck-not").style.display = "block";
  }
}
function checkUserinfo(type, val) {
  let mode = "";

  var ckeckId = /^[a-zA-z0-9]{4,12}$/; //아이디 유효성 검사
  var ckeckPW = /(?=.*[0-9])(?=.*[a-z])(?=.*\W)(?=\S+$).{8,20}/; //비밀번호 유효성 검사
  var ckeckName = /^[가-힣]{2,5}$/; //이름 유효성 검사
  var ckeckEmail = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+$/; //이메일 유효성 검사
  var ckeckPhone = /^[0-9]{10,11}$/; //이메일 유효성 검사
  if (type === "id") {
    mode = ckeckId;
  } else if (type === "pw") {
    mode = ckeckPW;
  } else if (type === "name") {
    mode = ckeckName;
  } else if (type === "email") {
    mode = ckeckEmail;
  } else if (type === "phone") {
    mode = ckeckPhone;
  }

  if (!mode.test(val)) {
    document.querySelector(".check-txt-" + type).style.display = "none";
    document.querySelector(".check-txt-" + type + "2").style.display = "block";
    document.querySelector(".check-txt-" + type + "3").style.display = "none";
    document.querySelector(".input-" + type).focus();
  } else {
    document.querySelector(".check-txt-" + type).style.display = "none";
    document.querySelector(".check-txt-" + type + "2").style.display = "none";
    document.querySelector(".check-txt-" + type + "3").style.display = "block";
  }
}
function onclickCbxCopy() {
  if (document.querySelector("#cdxCopy").checked === true) {
    let name = document.querySelector("#order-first-input").value;
    let phoneNumber = document.querySelector("#order-sec-input").value;
    document.querySelector("#order-first-input-copy").value = name;
    document.querySelector("#order-sec-input-copy").value = phoneNumber;
  } else {
    document.querySelector("#order-first-input-copy").value = "";
    document.querySelector("#order-sec-input-copy").value = "";
  }
}
function sample6_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ""; // 주소 변수
      var extraAddr = ""; // 참고항목 변수

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === "R") {
        // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else {
        // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }

      // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
      if (data.userSelectedType === "R") {
        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
        if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
          extraAddr += data.bname;
        }
        // 건물명이 있고, 공동주택일 경우 추가한다.
        if (data.buildingName !== "" && data.apartment === "Y") {
          extraAddr +=
            extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
        }
        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
        if (extraAddr !== "") {
          extraAddr = " (" + extraAddr + ")";
        }
        // 조합된 참고항목을 해당 필드에 넣는다.
        document.getElementById("sample6_extraAddress").value = extraAddr;
      } else {
        document.getElementById("sample6_extraAddress").value = "";
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById("sample6_postcode").value = data.zonecode;
      document.getElementById("sample6_address").value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("sample6_detailAddress").focus();
    },
  }).open();
}

function trash() {
  alert("장바구니가 비었습니다. 관심있는 상품을 담아보세요");
  location.href = "/category/home.html";
}

function showoptionList() {
  document.querySelector(".bg-option").className = "bg-option showoptionList";
}

function closeOrderList() {
  document.querySelector(".bg-option").className = "bg-option";
}

// 공유 아이콘 팝업
function openPopupoptionBtn(name) {
  popup = window.open(name);
}
function alertbtn() {
  alert("링크가 복사되었습니다. \n원하는 위치에 붙여넣기하세요. ");
}

// 사이즈탭 열고 닫기
function sizeTap(type) {
  if (document.querySelector("." + type + "-wrap").style.display !== "none") {
    document.querySelector("." + type + "-wrap").style.display = "none";
    document.querySelector(".btn-up-" + type).style.display = "none";
    document.querySelector(".btn-down-" + type).style.display = "block";
  } else {
    document.querySelector("." + type + "-wrap").style.display = "block";
    document.querySelector(".btn-up-" + type).style.display = "block";
    document.querySelector(".btn-down-" + type).style.display = "none";
  }
}
// 사이즈 선택시
function finalSize(size) {
  document.querySelector(".size-wrap").style.display = "none";
  document.querySelector(".hidden-option-tap").style.display = "block";
  document.querySelector(".option-choice").style.display = "none";
  document.querySelector(".option-btn-bg").style.display = "none";
  document.querySelector(".final-size").innerHTML = size;

  let strPrice = document.querySelector("#option-price").innerHTML;

  let strPrice2 = strPrice.replace("원", "");
  let price = Number(strPrice2.replace(",", ""));
  let b = ",";
  var position = -3;
  // 매개변수로 받은 사이즈를 final-size 클래스의 값으로 넣어야함
  // 이 탭 디스플레이 블록으로
  let a = String(price);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector(".option-total-price").innerHTML = output;
}

function finalColor(color) {
  document.querySelector(".size-wrap").style.display = "none";
  document.querySelector(".hidden-option-tap").style.display = "block";
  document.querySelector(".option-choice").style.display = "none";
  document.querySelector(".option-btn-bg").style.display = "none";
  document.querySelector(".final-color").innerHTML = color;
  let strPrice = document.querySelector("#option-price").innerHTML;
  let strPrice2 = strPrice.slice(0, -1);
  let price = Number(strPrice2.replace(",", ""));
  let b = ",";
  var position = -3;
  // 매개변수로 받은 사이즈를 final-size 클래스의 값으로 넣어야함
  // 이 탭 디스플레이 블록으로
  let a = String(price);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector(".option-total-price").innerHTML = output;
}

function sum() {
  optionTotalPrice += optionPrice;
  // 가격 형변환 후 쉼표추가 함수
  let a = String(optionTotalPrice);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector("#option-price").innerHTML = output + "원";
  document.querySelector("#finalPrice").innerHTML = output + "원";
  document.querySelector(".option-total-price").innerHTML = output;
  finalPrice = output;
  //갯수
  optionAmount++;
  document.querySelector(".MSH-sto-stock").value = optionAmount;
}
function sub() {
  if (optionAmount > 1) {
    optionTotalPrice -= optionPrice;
    // 가격 형변환 후 쉼표추가 함수
    let a = String(optionTotalPrice);
    var output = [a.slice(0, position), b, a.slice(position)].join("");
    document.querySelector("#option-price").innerHTML = output + "원";
    document.querySelector("#finalPrice").innerHTML = output + "원";
    document.querySelector(".option-total-price").innerHTML = output;
    finalPrice = output;
    //갯수
    optionAmount--;
    document.querySelector(".MSH-sto-stock").value = optionAmount;
  } else {
    document.querySelector(".MSH-sto-stock").value = optionAmount;
    document.querySelector("#option-price").innerHTML = optionStr;
    document.querySelector("#finalPrice").innerHTML = optionStr;
  }
}
//선택완료 버튼
function finalChoice() {
  document.querySelector(".bg-option").className = "bg-option";
  document.querySelector("#product-count").innerHTML = optionAmount;
  document.querySelector("#final-product-price").innerHTML = finalPrice;
}
//공유하기 버튼
function showShareList() {
  document.querySelector(".bg-share").className = "bg-share showShareList";
}

function closeOrderList() {
  document.querySelector(".bg-share").className = "bg-share";
}

// 공유 아이콘 팝업
function openPopupShareBtn(name) {
  popup = window.open(name);
}
function alertbtn() {
  alert("링크가 복사되었습니다. \n원하는 위치에 붙여넣기하세요. ");
}
//상세페이지에서 수량 수정(색상&사이즈 옵션 각 1개뿐일 때)
function productSum() {
  totalPrice += price;
  // 가격 형변환 후 쉼표추가 함수
  let a = String(totalPrice);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector("#price").innerHTML = output + "원";
  document.querySelector("#finalPrice").innerHTML = output + "원";

  //갯수
  amount++;
  document.querySelector(".MSH-sto-stock").value = amount;

  //할인상품일 경우
  if (itemDiscountRate != 0) {
    totalPrice2 += price2;
    let discount = totalPrice2 - totalPrice;
    let a2 = String(totalPrice2);
    let a3 = String(discount);
    var output2 = [a2.slice(0, position), b, a2.slice(position)].join("");
    var output3 = [a3.slice(0, position), b, a3.slice(position)].join("");
    document.querySelector("#finalTotalPrice").innerHTML = output2 + "원";
    document.querySelector("#discountPrice").innerHTML = "- " + output3 + "원";
    document.querySelector("#discountPrice2").innerHTML = output3 + "원";
  } else {
    document.querySelector("#finalTotalPrice").innerHTML = output + "원";
  }
}
function productSub() {
  if (amount > 1) {
    totalPrice -= price;

    // 가격 형변환 후 쉼표추가 함수
    let a = String(totalPrice);

    var output = [a.slice(0, position), b, a.slice(position)].join("");

    document.querySelector("#price").innerHTML = output + "원";
    document.querySelector("#finalPrice").innerHTML = output + "원";


    //갯수
    amount--;
    document.querySelector(".MSH-sto-stock").value = amount;

    if (itemDiscountRate != 0) {
          totalPrice2 -= price2;
          let discount = totalPrice2 - totalPrice;
          let a2 = String(totalPrice2);
          let a3 = String(discount);
          var output2 = [a2.slice(0, position), b, a2.slice(position)].join("");
          var output3 = [a3.slice(0, position), b, a3.slice(position)].join("");
          document.querySelector("#finalTotalPrice").innerHTML = output2 + "원";
          document.querySelector("#discountPrice").innerHTML = "- " + output3 + "원";
          document.querySelector("#discountPrice2").innerHTML = output3 + "원";
        } else {
          document.querySelector("#finalTotalPrice").innerHTML = output + "원";
        }
  } else {
    document.querySelector(".MSH-sto-stock").value = amount;
    var output = [str.slice(0, position), b, str.slice(position)].join("");
    document.querySelector("#price").innerHTML = output + "원";
    document.querySelector("#finalPrice").innerHTML = output + "원";
    document.querySelector("#finalTotalPrice").innerHTML = output + "원";
  }
}

function optionSum() {
  optionTotalPrice += optionPrice;
  // 가격 형변환 후 쉼표추가 함수
  let a = String(optionTotalPrice);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector("#option-price").innerHTML = output + "원";
  document.querySelector("#finalPrice").innerHTML = output + "원";
  document.querySelector(".option-total-price").innerHTML = output;
  finalPrice = output;
  //갯수
  optionAmount++;
  document.querySelector(".MSH-sto-stock").value = optionAmount;

  if (itemDiscountRate != 0) {
      totalPrice2 += price2;
      let discount = totalPrice2 - optionTotalPrice;
      let a2 = String(totalPrice2);
      let a3 = String(discount);
      var output2 = [a2.slice(0, position), b, a2.slice(position)].join("");
      var output3 = [a3.slice(0, position), b, a3.slice(position)].join("");
      document.querySelector("#finalTotalPrice").innerHTML = output2 + "원";
      document.querySelector("#discountPrice").innerHTML = "- " + output3 + "원";
      document.querySelector("#discountPrice2").innerHTML = output3 + "원";
    } else {
      document.querySelector("#finalTotalPrice").innerHTML = output + "원";
    }

}
function optionSub() {
  if (optionAmount > 1) {
    optionTotalPrice -= optionPrice;
    // 가격 형변환 후 쉼표추가 함수
    let a = String(optionTotalPrice);
    var output = [a.slice(0, position), b, a.slice(position)].join("");
    document.querySelector("#option-price").innerHTML = output + "원";
    document.querySelector("#finalPrice").innerHTML = output + "원";
    document.querySelector(".option-total-price").innerHTML = output;
    finalPrice = output;
    //갯수
    optionAmount--;
    document.querySelector(".MSH-sto-stock").value = optionAmount;

    if (itemDiscountRate != 0) {
              totalPrice2 -= price2;
              let discount = totalPrice2 - totalPrice;
              let a2 = String(totalPrice2);
              let a3 = String(discount);
              var output2 = [a2.slice(0, position), b, a2.slice(position)].join("");
              var output3 = [a3.slice(0, position), b, a3.slice(position)].join("");
              document.querySelector("#finalTotalPrice").innerHTML = output2 + "원";
              document.querySelector("#discountPrice").innerHTML = "- " + output3 + "원";
              document.querySelector("#discountPrice2").innerHTML = output3 + "원";
            } else {
              document.querySelector("#finalTotalPrice").innerHTML = output + "원";
            }
  } else {
    document.querySelector(".MSH-sto-stock").value = optionAmount;
    var output = [optionStr.slice(0, position), b, optionStr.slice(position)].join("");
        document.querySelector("#price").innerHTML = output + "원";
        document.querySelector("#finalPrice").innerHTML = output + "원";
        document.querySelector("#option-price").innerHTML = optionStr + "원";
        document.querySelector("#finalTotalPrice").innerHTML = output + "원";
  }
}


function basketNumCount() {
  basketNum++;
  document.querySelector(".basket-num").innerHTML = basketNum;
}

function popupOptionList(cartNo) {
  document.querySelector("#bg-option" + cartNo).className =
    "bg-option showoptionList";
}

function closeOptionList(cartNo) {
  document.querySelector("#bg-option" + cartNo).className = "bg-option";
}

// 옵션탭 열고 닫기
function optionTap(type, index) {
  if (
    document.querySelector("#" + type + "-wrap" + index).style.display !==
    "none"
  ) {
    document.querySelector("#" + type + "-wrap" + index).style.display = "none";
    document.querySelector("#btn-up-" + type + index).style.display = "none";
    document.querySelector("#btn-down-" + type + index).style.display = "block";
  } else {
    document.querySelector("#" + type + "-wrap" + index).style.display =
      "block";
    document.querySelector("#btn-up-" + type + index).style.display = "block";
    document.querySelector("#btn-down-" + type + index).style.display = "none";
  }
}
// 옵션 선택시
function finalOption(type, option, index) {
  document.querySelector("#" + type + "-wrap" + index).style.display = "none";
  document.querySelector("#final-" + type + index).innerText = option;
  document.querySelector("#final-" + type + "-input" + index).value = option;
}

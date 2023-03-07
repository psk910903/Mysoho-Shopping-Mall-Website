function showoptionList() {
  document.querySelector(".bg-option").className = "bg-option showoptionList";
}

function closeoptionList() {
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
  document.querySelector("#final-size-result").value = size;

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


let optionStr = document.querySelector("#option-price").innerHTML;
console.log(optionStr);
// 가격 형변환 후 쉼표추가 함수

let optionStr2 = optionStr.slice(0, -1);
let optionPrice = Number(optionStr2.replace(",", ""));
let optionTemp = optionPrice;
//기존 가격으로 토탈 초기화
let optionTotalPrice = optionTemp;

//상품 갯수
let optionN = document.querySelector(".MSH-sto-stock").value;
console.log(optionN);
let optionTemp2 = optionN;
let optionoptionAmount = optionTemp2;

// 중복이라 삭제 예정
var b = ",";
var position = -3;
function sum() {
  optionTotalPrice += optionPrice;
  // 가격 형변환 후 쉼표추가 함수
  let a = String(optionTotalPrice);
  var output = [a.slice(0, position), b, a.slice(position)].join("");
  document.querySelector("#option-price").innerHTML = output + "원";
  document.querySelector(".option-total-price").innerHTML = output;
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
    document.querySelector(".option-total-price").innerHTML = output;
    //갯수
    optionAmount--;
    document.querySelector(".MSH-sto-stock").value = optionAmount;
  } else {
    document.querySelector(".MSH-sto-stock").value = optionAmount;
    document.querySelector("#option-price").innerHTML = optionStr;
  }
}
function hiddenSizeTap() {
  document.querySelector(".hidden-option-tap").style.display = "none";
  document.querySelector(".option-total-price").innerHTML = 0;
  document.querySelector(".option-choice").style.display = "block";
  document.querySelector(".option-btn-bg").style.display = "block";
}

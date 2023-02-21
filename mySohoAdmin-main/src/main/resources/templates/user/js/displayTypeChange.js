//카테고리별 상품 페이지에서 버튼 클릭시 보여지는 리스트가 바뀌는 함수
function displayTypeChange1() {
  document.getElementById("display-type-steps2").style.display = "block";
  document.getElementById("display-type-steps1").style.display = "none";
  document.getElementById("list-type1").style.display = "block";
  document.getElementById("list-type2").style.display = "none";
}
function displayTypeChange2() {
  document.getElementById("display-type-steps2").style.display = "none";
  document.getElementById("display-type-steps1").style.display = "block";
  document.getElementById("list-type1").style.display = "none";
  document.getElementById("list-type2").style.display = "block";
}

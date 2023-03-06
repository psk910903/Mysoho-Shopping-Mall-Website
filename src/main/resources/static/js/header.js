//사이드메뉴
function openNav() {
  document.getElementById("mySidenav").style.width = "80%";
}
function closeNav() {
  document.getElementById("mySidenav").style.width = "0";
}

//돋보기
function openSearch() {
  document.getElementById("myOverlay").style.display = "block";
}

function closeSearch() {
  document.getElementById("myOverlay").style.display = "none";
}

function checkTxt(e) {
  let val = document.querySelector("#keyword").value;

  // 입력값이 공백일 때
  if (val === "") {
    alert("검색어를 입력해주세요.");
    return false;
  }
  return true;
}

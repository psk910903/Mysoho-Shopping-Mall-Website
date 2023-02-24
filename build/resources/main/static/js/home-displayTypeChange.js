//   리스트 변경 버튼
function listChangeBtn() {
  if (document.querySelector(".listType0").style.display !== "none") {
    document.querySelector(".listType0").style.display = "none";
    document.querySelector(".listType1").style.display = "flex";
    document.querySelector(".listType2").style.display = "none";
    document.querySelector(".listType3").style.display = "none";
    document.querySelector(".ico-list-1").style.display = "none";
    document.querySelector(".ico-list-2").style.display = "flex";
    document.querySelector(".ico-list-3").style.display = "none";
    document.querySelector(".ico-list-4").style.display = "none";
  } else if (document.querySelector(".listType1").style.display === "flex") {
    document.querySelector(".listType0").style.display = "none";
    document.querySelector(".listType1").style.display = "none";
    document.querySelector(".listType2").style.display = "flex";
    document.querySelector(".listType3").style.display = "none";
    document.querySelector(".ico-list-1").style.display = "none";
    document.querySelector(".ico-list-2").style.display = "none";
    document.querySelector(".ico-list-3").style.display = "flex";
    document.querySelector(".ico-list-4").style.display = "none";
  } else if (document.querySelector(".listType2").style.display === "flex") {
    document.querySelector(".listType0").style.display = "none";
    document.querySelector(".listType1").style.display = "none";
    document.querySelector(".listType2").style.display = "none";
    document.querySelector(".listType3").style.display = "block";
    document.querySelector(".ico-list-1").style.display = "none";
    document.querySelector(".ico-list-2").style.display = "none";
    document.querySelector(".ico-list-3").style.display = "none";
    document.querySelector(".ico-list-4").style.display = "flex";
  } else if (document.querySelector(".listType3").style.display === "block") {
    document.querySelector(".listType0").style.display = "block";
    document.querySelector(".listType1").style.display = "none";
    document.querySelector(".listType2").style.display = "none";
    document.querySelector(".listType3").style.display = "none";
    document.querySelector(".ico-list-1").style.display = "flex";
    document.querySelector(".ico-list-2").style.display = "none";
    document.querySelector(".ico-list-3").style.display = "none";
    document.querySelector(".ico-list-4").style.display = "none";
  }
}

//캐러셀
$("#carouselExampleCaptions").css("maxwidth", 640);
$("#carouselExampleCaptions").css("margin", "0 auto");
var myCarousel = document.querySelector("#carouselExampleCaptions");
var carousel = new bootstrap.Carousel(myCarousel, {
  interval: 2000, //기본 5초
  keyboard: true, //키보드이벤트 설정
  pause: "hover", //마우스를 가져대면 자동 순환이 멈춘다.
  wrap: true, //순환설정 loop
  touch: true, //왼쪽 오른쪽 드래그 설정
  ride: true, //자동재생
});

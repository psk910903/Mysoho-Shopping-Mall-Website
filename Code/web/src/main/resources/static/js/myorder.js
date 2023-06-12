       function changeRed(n) {
        growArr = document.querySelectorAll(".grow ");
        for (let i = 0; i < 6; i++) {
          growArr[i].classList.remove("red");
          document.querySelector(".list-type" + i).style.display = "none";
        }
        growArr[n].classList.add("red");
        document.querySelector(".list-type" + n).style.display = "block";

        if (document.querySelector("#value" + n).innerHTML === "0") {
          alert("주문정보가 없습니다.");
        }
      }

      // 주문정보 팝업
      function showOrderList(idx) {
        document.querySelector("#bg" + idx).className = "bg showOrderList";
      }

      function closeOrderList(idx) {
        document.querySelector("#bg" + idx).className = "bg";
      }
function sortSelected(name) {
        document.querySelector("#select").innerHTML =
          name +
          ' <img src="https://image.makeshop.co.kr/mysoho/assets/shop/img/common/arr_bottom.png" alt="">';
        document.querySelector("#sort").style.display = "none";

        if (
          document.querySelector(".ico-list-1").style.display != "none" &&
          name == "최신순"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#recentListType0").style.display = "block";
        } else if (
          name == "최신순" &&
          document.querySelector(".ico-list-2").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#recentListType1").style.display = "flex";
        } else if (
          name == "최신순" &&
          document.querySelector(".ico-list-3").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#recentListType2").style.display = "flex";
        } else if (
          name == "최신순" &&
          document.querySelector(".ico-list-4").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#recentListType3").style.display = "block";
        } else if (
          name == "판매순" &&
          document.querySelector(".ico-list-1").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#sellCountListType0").style.display = "block";
        } else if (
          name == "판매순" &&
          document.querySelector(".ico-list-2").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#sellCountListType1").style.display = "flex";
        } else if (
          name == "판매순" &&
          document.querySelector(".ico-list-3").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#sellCountListType2").style.display = "flex";
        } else if (
          name == "판매순" &&
          document.querySelector(".ico-list-4").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#sellCountListType3").style.display = "block";
        } else if (
          name == "낮은가격순" &&
          document.querySelector(".ico-list-1").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#LowPriceListType0").style.display = "block";
        } else if (
          name == "낮은가격순" &&
          document.querySelector(".ico-list-2").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#LowPriceListType1").style.display = "flex";
        } else if (
          name == "낮은가격순" &&
          document.querySelector(".ico-list-3").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#LowPriceListType2").style.display = "flex";
        } else if (
          name == "낮은가격순" &&
          document.querySelector(".ico-list-4").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#LowPriceListType3").style.display = "block";
        } else if (
          name == "리뷰많은순" &&
          document.querySelector(".ico-list-1").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighReviewListType0").style.display =
            "block";
        } else if (
          name == "리뷰많은순" &&
          document.querySelector(".ico-list-2").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighReviewListType1").style.display = "flex";
        } else if (
          name == "리뷰많은순" &&
          document.querySelector(".ico-list-3").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighReviewListType2").style.display = "flex";
        } else if (
          name == "리뷰많은순" &&
          document.querySelector(".ico-list-4").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighReviewListType3").style.display =
            "block";
        } else if (
          name == "평점높은순" &&
          document.querySelector(".ico-list-1").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighGradeListType0").style.display = "block";
        } else if (
          name == "평점높은순" &&
          document.querySelector(".ico-list-2").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighGradeListType1").style.display = "flex";
        } else if (
          name == "평점높은순" &&
          document.querySelector(".ico-list-3").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighGradeListType2").style.display = "flex";
        } else if (
          name == "평점높은순" &&
          document.querySelector(".ico-list-4").style.display != "none"
        ) {
          for (let i = 0; i < 4; i++) {
            document.querySelector("#recentListType" + i).style.display =
              "none";
            document.querySelector("#sellCountListType" + i).style.display =
              "none";
            document.querySelector("#LowPriceListType" + i).style.display =
              "none";
            document.querySelector("#HighReviewListType" + i).style.display =
              "none";
            document.querySelector("#HighGradeListType" + i).style.display =
              "none";
          }
          document.querySelector("#HighGradeListType3").style.display = "block";
        }
      }


window.onload = function() {
  var swiper = new Swiper("#headslider-container", {
    slidesPerView: 3,
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev"
    },
    pagination: {
      el:  ".swiper-pagination"
    },
    centeredSlides: true,
    breakpoints: {
      0: {
        slidesPerView: 1
      },
      1200: {
        slidesPerView: 3
      }
    }
  });

}

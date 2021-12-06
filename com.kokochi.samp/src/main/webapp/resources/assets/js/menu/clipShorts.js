document.addEventListener("DOMContentLoaded", function(){
    let swiper = new Swiper("#clip-swiper", {
        slidesPerView: 1,
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev"
        },
        centeredSlides: true,
    });

    document.querySelector('#testBtn').addEventListener('click', ()=> {
        let html = `
            <div class="swiper-slide">
              <div class="clips">새슬라이드</div>
            </div>
        `;
        swiper.appendSlide(html);
    })
});

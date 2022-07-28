document.addEventListener("DOMContentLoaded", function(){
    let swiper = new Swiper("#clip-swiper", {
        slidesPerView: 1,
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev"
        },
        centeredSlides: true,
        on: {
            activeIndexChange: function () {
                let clipSlides = document.querySelectorAll('.clips')
                if(this.realIndex > 0) clipSlides[this.realIndex-1].innerHTML = '';
                if(this.realIndex < clipSlides.length-1) clipSlides[this.realIndex+1].innerHTML = '';
                if(this.realIndex >= lastIndex -3 ) {
                    lastIndex += 10;
                    for(let i = lastIndex-10; i < lastIndex; i++) {
                        let html = `
                            <div class="swiper-slide">
                              <div class="clips">
                              </div>
                            </div>
                        `;
                        this.appendSlide(html);
                    }
                }
                clipSlides[this.realIndex].innerHTML = `
                    <iframe class="clip-video"
                            src="${clipsShortsData[this.realIndex].embed_url}&parent=localhost&autoplay=true"
                            height="500"
                            width="1000"
                            allowfullscreen="true">
                    </iframe>`;
                // console.log(clipsShortsData[this.realIndex].id)
                ajaxAwait('/menu/request/clipShorts/ban', 'POST', clipsShortsData[this.realIndex].id, (res) => {})
            }
        }
    });

    let clipsShortsData = [];
    let lastIndex = -1;
    function getDataClips(body) {
        ajaxAwait('/menu/request/clipShorts/get', 'POST', body, (res) => {
            clipsShortsData = JSON.parse(res);
            // console.log(clipsShortsData)
            lastIndex = 10;
            for(let i = 0;i < lastIndex; i++) {
                let html = `
                    <div class="swiper-slide">
                      <div class="clips">
                      </div>
                    </div>
                `;
                if(i == 0) {
                    html = `
                    <div class="swiper-slide">
                      <div class="clips">
                            <iframe class="clip-video"
                                    src="${clipsShortsData[i].embed_url}&parent=localhost&autoplay=true"
                                    height="500"
                                    width="1000"
                                    allowfullscreen="true">
                            </iframe>
                      </div>
                    </div>
                    `;
                    ajaxAwait('/menu/request/clipShorts/ban', 'POST', clipsShortsData[0].id, (res) => {})
                }
                swiper.appendSlide(html);
            }
        })
    } // /detail/request/live POST - 라이브 데이터 가져오기
    getDataClips('none')
});

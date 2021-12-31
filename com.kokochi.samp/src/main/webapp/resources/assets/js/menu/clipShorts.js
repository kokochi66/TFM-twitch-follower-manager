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
                clipSlides[this.realIndex].innerHTML = `
                    <iframe class="clip-video"
                            src="${clipsShortsData[this.realIndex].embed_url}&parent=localhost&autoplay=true"
                            height="500"
                            width="1000"
                            allowfullscreen="true">
                    </iframe>`;
            }
        }


    });

    let clipsShortsData = [];
    let lastIndex = -1;
    function getDataClips(body) {
        ajaxAwait('/menu/request/clipShorts/get', 'POST', body, (res) => {
            clipsShortsData = JSON.parse(res);
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
                    `
                }
                swiper.appendSlide(html);
            }

            // if(res !== 'error') {
            //     live_box.innerHTML = `
            //         <div class="live-info">
            //             <div class="video-box">
            //                 <a href="https://www.twitch.tv/${result.user_login}" class="linkBox url" target="_blank">
            //                     <div class="thumbnail">
            //                         <img src="${result.thumbnail_url}" alt="" width="100%">
            //                     </div>
            //                     <div class="play_btn">
            //                         <img src="/resources/assets/img/play.png" alt="" width="100%">
            //                     </div>
            //                     <div class="text"></div>
            //                 </a>
            //             </div>
            //             <div class="info-box">
            //                 <div class="title">${result.title ? result.title : ''}</div>
            //                 <div class="viewer">${result.viewer_count ? result.viewer_count+'명' : ''}</div>
            //                 <div class="game">${result.game_name ? result.game_name : ''}</div>
            //                 <div class="start_at">${result.started_at ? result.started_at : ''}</div>
            //                 <div class="rank"></div>
            //                 <div class="tags">
            //                     <div class="tag">한국어</div>
            //                     <div class="tag">e스포츠</div>
            //                 </div>
            //             </div>
            //         </div>
            //         `
            // }
        })
    } // /detail/request/live POST - 라이브 데이터 가져오기
    getDataClips('none')
});

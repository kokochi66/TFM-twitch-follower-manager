document.addEventListener("DOMContentLoaded", function(){

    // 메인 헤드 슬라이더의 데이터 값 가져오기
    ajaxSubmit('/home/request/getLiveVideo', 'POST', '', (res) => {
        // console.log('라이브 비디오 가져오기 선언')
        if(res !== 'error') addLiveVideo(JSON.parse(res))
    })

    // 메인 헤드 슬라이더 초기화 함수
    function init_home_headSlider() {
        let swiper = new Swiper("#headslider-container", {
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

    // 메인 헤드 슬라이더 슬라이드 추가 함수
    function addLiveVideo(data) {
        let swiper_wrapper_box = document.querySelector('#headslider .swiper-wrapper')
        for(let i=0;i<data.length;i++) {
            let html = `
					<div class="swiper-slide">
					  <div class="imageBox">
						<a href="https://www.twitch.tv/${data[i].user_login}"
						  class="linkBox" target="_blank">
						  <div class="img">
							<img src="${data[i].thumbnail_url}" alt="방송이미지" width="100%">
						  </div>
						</a>
						<div class="info">
						  <div class="profile_img">
							<img src="${data[i].profile_image_url}" alt="" width="100%">
						  </div>
						  <div class="user_name">${data[i].user_name}(${data[i].user_login})</div>
						  <div class="stream_game">${data[i].game_name}</div>
						  <div class="viewer_count">시청자 ${data[i].viewer_count}명</div>
						  <div class="started_date">${data[i].started_at}</div>
						</div>
					  </div>
				  	</div>
				`;
            swiper_wrapper_box.insertAdjacentHTML('beforeend', html);
        }
        init_home_headSlider();
    }

});
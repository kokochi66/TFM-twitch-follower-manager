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
function addLiveVideo(data) {
  let swiper_wrapper_box = document.querySelector('#headslider .swiper-wrapper')
  for(let i=0;i<data.length;i++) {
    let swiper_slide_box = document.createElement('div')
    swiper_slide_box.className = 'swiper-slide'
    swiper_slide_box.innerHTML =     `
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
    `;
    swiper_wrapper_box.appendChild(swiper_slide_box)
  }
  init_home_headSlider();
}
function request_getLiveVideo() {
  let response = fetch('/home/request/getLiveVideo', { // 서버 자체에 POST 요청을 보냄.
      method: 'POST', 
      headers: {
          'Content-Type': 'application/json;charset=utf-8'
      }
  }).then(function(res){ 
      res.json().then(result => { // 결과값을 json 객체로 받아옴
          // console.log(result)
          if(result !== 'error') addLiveVideo(result)
      }).catch(resB => console.log(resB));
  }).catch(function(res){ 
      console.log('catch res :: ' , res)
      // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
  })
}

request_getLiveVideo();
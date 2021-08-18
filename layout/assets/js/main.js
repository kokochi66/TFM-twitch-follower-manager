window.onload = function() {
  let addMoreBtn = document.querySelector('#services .addMore');

  addMoreBtn.addEventListener('click', () => {
    service_container = document.querySelector('#services .container')
    service_container.appendChild(loading_box);

    let Service_iconSet = document.querySelectorAll('#services .icon-set');
    let Last_iconSet = Service_iconSet[Service_iconSet.length-1];
    let user_idSet = Last_iconSet.querySelectorAll('.user_id')
    let next_pageSet = Last_iconSet.querySelectorAll('.next_page')
    let Service_Map = []
    for(let i=0;i<user_idSet.length;i++) {
      Service_Map[i] = []
      Service_Map[i][0] = user_idSet[i].innerHTML
      Service_Map[i][1] = next_pageSet[i].innerHTML
    }
    sendService_Data(video_list , Service_Map);
  })
  let service = document.querySelector('#services')
  let service_container = document.querySelector('#services .container')
  let service_last = document.querySelector('#services .service_last_box');
  let AboutListBtns = document.querySelectorAll('#about .listBtn')
  let video_list = 'recent_video'
  let video_list_str = ['recent_video', 'recent_live', 'recent_clip'];
  let video_list_info = ['관리목록 다시보기', '관리목록 라이브', '관리목록 인기클립'];

  AboutListBtns.forEach((elem, idx) => {
    elem.addEventListener('click', () => {
      service_container = document.querySelector('#services .container')
      service.removeChild(service_container)

      addService_Container(idx)
      video_list = video_list_str[idx]

      let s_last_box = document.createElement('div')
      s_last_box.className = 'service_last_box'
      service_container.appendChild(s_last_box);
      service_container.appendChild(loading_box);
      service_last = document.querySelector('#services .service_last_box');

      sendService_Data(video_list, 'n');
    })
  })

  function sendService_Data(video, map) {
    let body_data = {
        "service_map": JSON.stringify(map),
        "service_target": video
    }
    let response = fetch('/home/request/getNextVideo', { // 서버 자체에 POST 요청을 보냄.
        method: 'POST', 
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(body_data)    // 요청 body에 id값을 넣음
    }).then(function(res){ 
        res.json().then(result => { // 결과값을 json 객체로 받아옴
            // console.log('then :: ' + result)
            if(result !== 'error') addLiveVideo(JSON.parse(result))
        }).catch(resB => console.log(resB));
    }).catch(function(res){ 
        console.log('catch res :: ' , res)
        // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
    })
  }
  function addService_IconSet(data) {
    let s_row = document.createElement('div');
    s_row.className = 'row icon-set';
  
    for(let i=0;i<data.length;i++) {
      let s_col_box = document.createElement('div');
      s_col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
      s_col_box.innerHTML = `
        <div class="icon-box" title="방송목록">
          <a href="${data[i].url}" class="linkBox" target="_blank">
            <img alt="" src="${data[i].thumbnail_url ? data[i].thumbnail_url : default_img}" width="100%">
          </a>
        </div>
        <div class="icon-info">
          <div class="profile">
            <img alt="" src="${data[i].profile_url ? data[i].profile_url : default_img}" width="100%" height="100%">
          </div>
          <div class="title text">${data[i].title}</div>
          <div class="name text">${data[i].user_name}</div>
          <div class="user_id displayNone">${data[i].user_id}</div>
          <div class="next_page displayNone">${data[i].nextPage}</div>
        </div>
      `;
      s_row.appendChild(s_col_box)
    }
    service_container.insertBefore(s_row, service_last);
    service_container.removeChild(loading_box);
  }
  function addService_Container(idx) {
    let s_container_box = document.createElement('div')
    s_container_box.className = 'container'
    s_container_box.innerHTML = `
      <div class="section-title">
        <h2>${video_list_info[idx]}</h2>
      </div>
    `;
    service.insertBefore(s_container_box, addMoreBtn);
    service_container = document.querySelector('#services .container')
  }
}
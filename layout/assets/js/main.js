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
    httpRequest = new XMLHttpRequest();
    httpRequest.open('POST', '/home/request/getNextVideo');
    httpRequest.setRequestHeader('service_map', JSON.stringify(map));
    httpRequest.setRequestHeader('service_target',video);
  
    httpRequest.onload = () => {
      if(httpRequest.status == 200) {
        let result = httpRequest.response;
        if(result) {
          addService_IconSet(JSON.parse(result))
        }
        service_container.removeChild(loading_box);
        return result;
      } else {
      }
    }
  
    httpRequest.send();
  }
  function addService_IconSet(data) {
    let s_row = document.createElement('div');
    s_row.className = 'row icon-set';
  
    for(let i=0;i<data.length;i++) {
      let s_col_box = document.createElement('div');
      s_col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
  
      let s_icon_box = document.createElement('div');
      s_icon_box.className = 'icon-box';
    
      let s_link_box = document.createElement('a');
      s_link_box.className = 'linkBox';
      s_link_box.target = '_blank';
      s_link_box.href = data[i].url;
    
      let s_img_box = document.createElement('img');
      s_img_box.src = data[i].thumbnail_url ? data[i].thumbnail_url : default_img;
      s_img_box.style.width = '100%';
  
      s_link_box.appendChild(s_img_box)
      s_icon_box.appendChild(s_link_box);
      s_col_box.appendChild(s_icon_box);
  
      let s_icon_info = document.createElement('div');
      s_icon_info.className = 'icon-info';
  
      let s_profile = document.createElement('div');
      s_profile.className = 'profile'
      let s_profile_img = document.createElement('img');
      s_profile_img.src = data[i].profile_url ? data[i].profile_url : default_img;
      s_profile_img.style.width = '100%';
      s_profile_img.style.height = '100%';
      s_profile.appendChild(s_profile_img);
      let s_title = document.createElement('div');
      s_title.className = 'title text';
      s_title.innerHTML = data[i].title;
      let s_name = document.createElement('div');
      s_name.className = 'name text'
      s_name.innerHTML = data[i].user_name;
      let s_user_id = document.createElement('div');
      s_user_id.className = 'user_id displayNone'
      s_user_id.innerHTML = data[i].user_id;
      let s_next_page = document.createElement('div');
      s_next_page.className = 'next_page displayNone'
      s_next_page.innerHTML = data[i].nextPage;
  
      s_icon_info.appendChild(s_profile)
      s_icon_info.appendChild(s_title)
      s_icon_info.appendChild(s_name)
      s_icon_info.appendChild(s_user_id)
      s_icon_info.appendChild(s_next_page)
      s_col_box.appendChild(s_icon_info);
  
      s_row.appendChild(s_col_box)
    }
    service_container.insertBefore(s_row, service_last);
  }
  function addService_Container(idx) {
    let s_container_box = document.createElement('div')
    s_container_box.className = 'container'
  
    let s_section_title_box = document.createElement('div')
    s_section_title_box.className = 'section-title'

    let s_section_title_box_h2 = document.createElement('h2')
    s_section_title_box_h2.innerHTML = video_list_info[idx]
    
    s_section_title_box.appendChild(s_section_title_box_h2)
    s_container_box.appendChild(s_section_title_box);
    service.insertBefore(s_container_box, addMoreBtn);
    service_container = document.querySelector('#services .container')
  }
}
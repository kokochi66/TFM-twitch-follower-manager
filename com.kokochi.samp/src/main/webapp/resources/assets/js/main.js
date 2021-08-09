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


let addMoreBtn = document.querySelector('#services .addMore');
let default_img = '/resources/assets/img/default_image.jpg';
let loading_box = document.createElement('div');
loading_box.className = 'loading m-auto mt-5'

addMoreBtn.addEventListener('click', () => {
  let parentMoreNode = addMoreBtn.parentNode;
  parentMoreNode.appendChild(loading_box);
  sendService_Data();
})


function sendService_Data() {
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

  httpRequest = new XMLHttpRequest();
  httpRequest.open('POST', '/home/request/getNextVideo');
  httpRequest.setRequestHeader('service_map', JSON.stringify(Service_Map));
  httpRequest.setRequestHeader('service_target','replay_video');

  httpRequest.onload = () => {
    if(httpRequest.status == 200) {
        let result = JSON.parse(httpRequest.response);
        console.log(result)
        addService_IconSet(result);
        return result;
    } else {
        alert('error')
    }
  }

  httpRequest.send();
}

function addService_IconSet(data) {
	let parentNode = addMoreBtn.parentNode;
	parentNode.removeChild(loading_box);
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
  parentNode.insertBefore(s_row, addMoreBtn);
}
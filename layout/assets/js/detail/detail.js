window.onload = () => {
    let tab_select_btn = document.querySelectorAll('#tab .menu-box')
    let service_boxes = document.querySelectorAll('#services .container > div')

    let selected_btn_idx = 0;

    // 0 - 라이브, 1 - 다시보기, 2 - 클립 , 3 - 연관 스트리머
    let user_id = document.querySelector('#about .profile .user_id').innerHTML;
    let live_box = {
        "thumbnail" : document.querySelector('.live-box .video-box .thumbnail img'),
        "url" : document.querySelector('.live-box .video-box .url'),
        "title" : document.querySelector('.live-box .info-box .title'),
        "viewer" : document.querySelector('.live-box .info-box .viewer'),
        "game" : document.querySelector('.live-box .info-box .game'),
        "rank" : document.querySelector('.live-box .info-box .rank'),
        "start_at" : document.querySelector('.live-box .info-box .start_at'),
        "tags" : document.querySelector('.live-box .info-box .tags'),
    }
    let replay_detail_box = document.querySelector('#services .replay-detail-box')
    let replay_detail_addMore = document.querySelector('#services .replay-detail-box .addMore')
    replay_detail_addMore.addEventListener('click', () => {
        let replay_detail_row = replay_detail_box.querySelectorAll('.row')
        let replay_detail_last_next = replay_detail_row[replay_detail_row.length-1].querySelector('.next_page').innerHTML
        setting_replay_req("after="+replay_detail_last_next+"&");
    })

    let clip_box = document.querySelector('#services .clip-box')
    let clip_box_addMore = document.querySelector('#services .clip-box .addMore')
    clip_box_addMore.addEventListener('click', () => {
        let clip_box_row = clip_box.querySelectorAll('.row')
        let clip_box_last_next = clip_box_row[clip_box_row.length-1].querySelector('.next_page').innerHTML
        setting_clip_req("after="+clip_box_last_next+"&");
    })

    let setting_live_req = function() {
        let xhr = new XMLHttpRequest();
        xhr.open('POST', '/detail/request/live');
        xhr.setRequestHeader('login', user_id);
      
        xhr.onload = () => {
          if(xhr.status == 200) {
            let res = xhr.response;
            if(res) {
                res_json = JSON.parse(res);
                live_box.thumbnail.src = res_json.thumbnail_url;
                live_box.url.href = 'https://www.twitch.tv/'+res_json.user_login;
                live_box.title.innerHTML = res_json.title ? res_json.title : '';
                live_box.viewer.innerHTML = res_json.viewer_count ? res_json.viewer_count+'명' : '';
                live_box.game.innerHTML = res_json.game_name ? res_json.game_name : '';
                live_box.start_at.innerHTML = res_json.started_at ? res_json.started_at : '';
            }
            return res;
          } else {
          }
        }
      
        xhr.send();
    }
    let setting_replay_req = function(next) {
        let xhr = new XMLHttpRequest();
        xhr.open('POST', '/detail/request/replay');
        xhr.setRequestHeader('login', user_id);
        xhr.setRequestHeader('next', next);
      
        xhr.onload = () => {
          if(xhr.status == 200) {
            let res = xhr.response;
            if(res) {
                let res_json = JSON.parse(res);
                // console.log(res_json)
                addVideo_iconBox(res_json)
            }
          } else {
          }
        }
      
        xhr.send();
    }
    let setting_clip_req = function(next) {
        let xhr = new XMLHttpRequest();
        xhr.open('POST', '/detail/request/clips');
        xhr.setRequestHeader('login', user_id);
        xhr.setRequestHeader('next', next);
      
        xhr.onload = () => {
          if(xhr.status == 200) {
            let res = xhr.response;
            if(res) {
                let res_json = JSON.parse(res);
                addClips_iconBox(res_json)
            }
          } else {
          }
        }
      
        xhr.send();
    }
    let setting_relative_req = function() {}
    setting_live_req();
    setting_replay_req('');
    setting_clip_req('');
    let req_arr = [setting_live_req, setting_replay_req, setting_clip_req, setting_relative_req];


    tab_select_btn.forEach((elem,idx) => {
        elem.addEventListener('click', () => {
            tab_select_btn[selected_btn_idx].classList.remove('active')
            service_boxes[selected_btn_idx].classList.add('displayNone')
            selected_btn_idx = idx;
            tab_select_btn[selected_btn_idx].classList.add('active')
            service_boxes[selected_btn_idx].classList.remove('displayNone')
        })
    })

    function addVideo_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';

        for(let i=0;i<data.length;i++) {
            let col_box = document.createElement('div')
            col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
    
            let icon_box = document.createElement('div')
            icon_box.className = 'icon-box'
    
            let link_box = document.createElement('a');
            link_box.href = data[i].url;
            link_box.className = 'linkBox'
            link_box.target = '_blank'
    
            let img_box = document.createElement('img')
            img_box.style.width = '100%';
            img_box.src = data[i].thumbnail_url
    
            link_box.appendChild(img_box)
            icon_box.appendChild(link_box)
    
            let icon_info_box = document.createElement('div')
            icon_info_box.className = 'icon-info'
    
            let title_box = document.createElement('div')
            title_box.className = 'title'
            title_box.innerHTML = data[i].title
            let view_count_box = document.createElement('div')
            view_count_box.className = 'view_count'
            view_count_box.innerHTML = data[i].view_count
            let created_at_box = document.createElement('div')
            created_at_box.className = 'created_at'
            created_at_box.innerHTML = data[i].created_at
            let next_page_box = document.createElement('div')
            next_page_box.className = 'next_page displayNone'
            next_page_box.innerHTML = data[i].nextPage
    
            icon_info_box.appendChild(title_box)
            icon_info_box.appendChild(view_count_box)
            icon_info_box.appendChild(created_at_box)
            icon_info_box.appendChild(next_page_box)
    
            col_box.appendChild(icon_box)
            col_box.appendChild(icon_info_box)
    
            row_box.appendChild(col_box)
        }
        replay_detail_box.insertBefore(row_box, replay_detail_addMore);
    }
    function addClips_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';

        for(let i=0;i<data.length;i++) {
            let col_box = document.createElement('div')
            col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
    
            let icon_box = document.createElement('div')
            icon_box.className = 'icon-box'
    
            let link_box = document.createElement('a');
            link_box.href = data[i].url;
            link_box.className = 'linkBox'
            link_box.target = '_blank'
    
            let img_box = document.createElement('img')
            img_box.style.width = '100%';
            img_box.src = data[i].thumbnail_url
    
            link_box.appendChild(img_box)
            icon_box.appendChild(link_box)
    
            let icon_info_box = document.createElement('div')
            icon_info_box.className = 'icon-info'
    
            let title_box = document.createElement('div')
            title_box.className = 'title'
            title_box.innerHTML = data[i].title
            let view_count_box = document.createElement('div')
            view_count_box.className = 'view_count'
            view_count_box.innerHTML = data[i].view_count
            let created_at_box = document.createElement('div')
            created_at_box.className = 'created_at'
            created_at_box.innerHTML = data[i].created_at
            let next_page_box = document.createElement('div')
            next_page_box.className = 'next_page displayNone'
            next_page_box.innerHTML = data[i].nextPage
    
            icon_info_box.appendChild(title_box)
            icon_info_box.appendChild(view_count_box)
            icon_info_box.appendChild(created_at_box)
            icon_info_box.appendChild(next_page_box)
    
            col_box.appendChild(icon_box)
            col_box.appendChild(icon_info_box)
    
            row_box.appendChild(col_box)
        }
        clip_box.insertBefore(row_box, clip_box_addMore);
    }
}



window.onload = () => {
    function addVideo_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';
    
        for(let i=0;i<data.length;i++) {
            let col_box = document.createElement('div')
            col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
            col_box.innerHTML = `
                <div class="icon-box">
                    <a href="${data[i].url}" class="linkBox" target="_blank">
                        <img src="${data[i].thumbnail_url ? data[i].thumbnail_url : default_img}" width="100%">
                    </a>
                </div>
                <div class="icon-info">
                    <div class="title">${data[i].title}</div>
                    <div class="view_count">${data[i].view_count}</div>
                    <div class="created_at">${data[i].created_at}</div>
                    <div class="next_page displayNone">${data[i].nextPage}</div>
                </div
            `;
            row_box.appendChild(col_box)
        }
        replay_detail_box.insertBefore(row_box, replay_detail_addMore);
        replay_detail_addMore.addEventListener('click', replay_addEvent)
    }
    function addClips_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';
    
        for(let i=0;i<data.length;i++) {
            let col_box = document.createElement('div')
            col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
            col_box.innerHTML = `
                <div class="icon-box">
                    <a href="${data[i].url}" class="linkBox" target="_blank">
                        <img src="${data[i].thumbnail_url ? data[i].thumbnail_url : default_img}" width="100%">
                    </a>
                </div>
                <div class="icon-info">
                    <div class="title">${data[i].title}</div>
                    <div class="view_count">${data[i].view_count}</div>
                    <div class="created_at">${data[i].created_at}</div>
                    <div class="next_page displayNone">${data[i].nextPage}</div>
                </div
            `;
            row_box.appendChild(col_box)
        }
        clip_box.insertBefore(row_box, clip_box_addMore);
        clip_box_addMore.addEventListener('click', clips_addEvent)
    }
    function addRelative_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';
    
        for(let i=0;i<data.length;i++) {
            let col_box = document.createElement('div')
            col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
            col_box.innerHTML = `
                <div class="follow-box">
                    <div class="profile">
                        <a href="https://www.twitch.tv/${data[i].login}" class="linkBox" target="_blank">
                            <img src="${data[i].profile_image_url}" width="100%"/>
                        </a>
                    </div>
                    <div class="info">
                        <div class="name">${data[i].display_name}</div>
                        <div class="desc">${data[i].description}</div>
                        <div class="user_id displayNone">${data[i].id}</div>
                        <div class="check">
                            <input type="checkbox" class="form-check-input checkInput"/>
                        </div>
                    </div>
                </div>
            `;
            row_box.appendChild(col_box)
        }
        relative_box.insertBefore(row_box, relative_box_addMore);
        relative_box.removeChild(loading_box)
        relative_file_num += 8;
        relative_box_addMore.addEventListener('click', relative_addEvent)
    }
    
    function replay_addEvent() {
        replay_detail_addMore.removeEventListener('click', replay_addEvent)
        let replay_detail_row = replay_detail_box.querySelectorAll('.row')
        let replay_detail_last_next = replay_detail_row[replay_detail_row.length-1].querySelector('.next_page').innerHTML
        setting_replay_req("after="+replay_detail_last_next+"&");
    }
    function clips_addEvent() {
        clip_box_addMore.removeEventListener('click', clips_addEvent)
        let clip_box_row = clip_box.querySelectorAll('.row')
        let clip_box_last_next = clip_box_row[clip_box_row.length-1].querySelector('.next_page').innerHTML
        setting_clip_req("after="+clip_box_last_next+"&");
    }
    function relative_addEvent() {
        relative_box_addMore.removeEventListener('click', relative_addEvent)
        relative_box.insertBefore(loading_box, relative_box_addMore)
        relative_format_twitchUserSet(relative_file.slice(
            relative_file_num, relative_file_num+8))
    }

    let setting_live_req = function() {
        let response = fetch('/detail/request/live', { // 서버 자체에 POST 요청을 보냄.
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: user_id    // 요청 body에 id값을 넣음
        }).then(function(res){ 
            res.json().then(result => { // 결과값을 json 객체로 받아옴
                // console.log(result)
                if(result !== 'error') {
                    live_box.innerHTML = `
                    <div class="live-info">
                        <div class="video-box">
                            <a href="https://www.twitch.tv/${result.user_login}" class="linkBox url" target="_blank">
                                <div class="thumbnail">
                                    <img src="${result.thumbnail_url}" alt="" width="100%">
                                </div>
                                <div class="play_btn">
                                    <img src="/resources/assets/img/play.png" alt="" width="100%">
                                </div>
                                <div class="text"></div>
                            </a>
                        </div>
                        <div class="info-box">
                            <div class="title">${result.title ? result.title : ''}</div>
                            <div class="viewer">${result.viewer_count ? result.viewer_count+'명' : ''}</div>
                            <div class="game">${result.game_name ? result.game_name : ''}</div>
                            <div class="start_at">${result.started_at ? result.started_at : ''}</div>
                            <div class="rank"></div>
                            <div class="tags">
                                <div class="tag">한국어</div>
                                <div class="tag">e스포츠</div>
                            </div>
                        </div>
                    </div>
                    `
                }
            }).catch(res => console.log('catch :: ' + res));
        }).catch(function(res){ 
            console.log('catch res :: ' , res)
            // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
        })
    }
    let setting_replay_req = function(next) {
        let body_data = {
            "login": user_id,
            "next": next
        }
        let response = fetch('/detail/request/replay', { // 서버 자체에 POST 요청을 보냄.
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(body_data)    // 요청 body에 id값을 넣음
        }).then(function(res){ 
            res.json().then(result => { // 결과값을 json 객체로 받아옴
                console.log('then :: ' + result)
                if(result !== 'error') addVideo_iconBox(result)
            }).catch(resB => console.log(resB));
        }).catch(function(res){ 
            console.log('catch res :: ' , res)
            // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
        })

    }
    let setting_clip_req = function(next) {
        let body_data = {
            "login": user_id,
            "next": next
        }
        let response = fetch('/detail/request/clips', { // 서버 자체에 POST 요청을 보냄.
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(body_data)    // 요청 body에 id값을 넣음
        }).then(function(res){ 
            res.json().then(result => { // 결과값을 json 객체로 받아옴
                // console.log(result)
                if(result !== 'error') addClips_iconBox(result)
            }).catch(res => console.log('catch :: ' + res));
        }).catch(function(res){ 
            console.log('catch res :: ' , res)
            // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
        })
    }
    let setting_relative_req = function(left, right) {
        relative_box.insertBefore(loading_box, relative_box_addMore)
        let response = fetch('/detail/request/relative', { // 서버 자체에 POST 요청을 보냄.
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: user_id    // 요청 body에 id값을 넣음
        }).then(function(res){ 
            res.json().then(result => { // 결과값을 json 객체로 받아옴
                // console.log(result)
                if(result !== 'error') {
                    relative_file = result;
                    relative_format_twitchUserSet(relative_file.slice(left,right))
                }

            }).catch(res => console.log('catch :: ' + res));
        }).catch(function(res){ 
            console.log('catch res :: ' , res)
            // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
        })
    }
    let relative_format_twitchUserSet = function(data) {
        let response = fetch('/detail/request/getTwitchUserSet', { // 서버 자체에 POST 요청을 보냄.
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(data)    // 요청 body에 id값을 넣음
        }).then(function(res){ 
            res.json().then(result => { // 결과값을 json 객체로 받아옴
                // console.log(result)
                if(result !== 'error') addRelative_iconBox(result)
            }).catch(res => console.log('catch :: ' + res));
        }).catch(function(res){ 
            console.log('catch res :: ' , res)
            // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
        })
    }

    let tab_select_btn = document.querySelectorAll('#tab .menu-box')
    let service_boxes = document.querySelectorAll('#services .container > div')
    let selected_btn_idx = 0;

    // 0 - 라이브, 1 - 다시보기, 2 - 클립 , 3 - 연관 스트리머
    let user_id = document.querySelector('#about .profile .user_id').innerHTML;
    let live_box = document.querySelector('#services .live-box')

    let replay_detail_box = document.querySelector('#services .replay-detail-box')
    let replay_detail_addMore = document.querySelector('#services .replay-detail-box .addMore')

    let clip_box = document.querySelector('#services .clip-box')
    let clip_box_addMore = document.querySelector('#services .clip-box .addMore')

    let relative_box = document.querySelector('#services .relative-box')
    let relative_box_addMore = document.querySelector('#services .relative-box .addMore')
    let relative_file = {}
    let relative_file_num = 0;

    setting_live_req();
    setting_replay_req('');
    setting_clip_req('');
    setting_relative_req(relative_file_num,relative_file_num + 8);
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
}



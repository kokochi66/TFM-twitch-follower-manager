document.addEventListener("DOMContentLoaded", function(){

    let user_id = document.querySelector('#about .profile .user_id').innerHTML;
    let live_box = document.querySelector('#services .live-box')

    let recent_video = document.querySelector('#services .recent_video')
    let recent_video_addMore = document.querySelector('#services .recent_video .addMore')
    let recent_video_addMore_flag = 1;

    let recent_clip = document.querySelector('#services .recent_clip')
    let recent_clip_addMore = document.querySelector('#services .recent_clip .addMore')
    let recent_clip_addMore_flag = 1;

    let refresh_btn = document.querySelector('#refresh_btn');

    // 변수 초기화

    function getDataLive(body) {
        ajaxAwait('/detail/request/live', 'POST', body, (res) => {
            // console.log(JSON.parse(res))
            let result = JSON.parse(res);
            if(res !== 'error') {
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
        })
    } // /detail/request/live POST - 라이브 데이터 가져오기
    function getDataReplay(body) {
        let bodyData = {
            "login" : user_id,
            "next" : body
        }
        ajaxAwait('/detail/request/replay', 'POST', JSON.stringify(bodyData), (res) => {
            let result = JSON.parse(res);
            if(res !== 'error') {
                addVideo_iconBox(result);
            }
            recent_video_addMore_flag = 0;
        })
    }   // /detail/request/replay POST - 다시보기 데이터 가져오기
    function getDataClip(body) {
        let bodyData = {
            "login" : user_id,
            "next" : body
        }
        ajaxAwait('/detail/request/clips', 'POST', JSON.stringify(bodyData), (res) => {
            let result = JSON.parse(res);
            if(res !== 'error') {
                addClips_iconBox(result);
            }
            recent_clip_addMore_flag = 0;
        })
    }   // /detail/request/clip POST - 다시보기 데이터 가져오기

    recent_video_addMore.addEventListener('click', (e) => {
        if(recent_video_addMore_flag === 1) {
            alert('조회중 입니다.')
            return false;
        }
        recent_video_addMore_flag = 1;
        getDataReplay(recent_video_addMore.getAttribute('next-id'));
    }) // 다시보기 더보기 버튼 이벤트
    recent_clip_addMore.addEventListener('click', (e) => {
        if(recent_clip_addMore_flag === 1) {
            alert('조회중 입니다.')
            return false;
        }
        recent_clip_addMore_flag = 1;
        getDataClip(recent_clip_addMore.getAttribute('next-id'));
    }) // 클립 더보기 버튼 이벤트

    function addVideo_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';

        for(let i=0;i<data.length-1;i++) {
            let col_box = document.createElement('div')
            col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
            col_box.innerHTML = `
                <div class="icon-box" title="방송목록">
                    <a href="${data[i].url}" class="linkBox" target="_blank">
                    <img alt="" src="${data[i].thumbnail_url ? data[i].thumbnail_url : default_img}" width="100%">
                    </a>
                    <div class="video-follow-box ${data[i].isManaged ? 'minus' : 'plus'}">
                    ${data[i].isManaged ? '<i class="icofont-minus"></i>' : '<i class="icofont-plus"></i>'}
                    </div>
                </div>
                <div class="icon-info">
                    <div class="title" title="${data[i].title}">${data[i].title}</div>
                    <div class="view_count">${data[i].view_count}회 재생</div>
                    <div class="created_at">${data[i].created_at}</div>
                    <div class="video_id displayNone">${data[i].id}</div>
                </div>
            `;
            row_box.appendChild(col_box)
        }
        recent_video_addMore.setAttribute('next-id', data[data.length-1].next);
        recent_video.insertBefore(row_box, recent_video_addMore);
        let recent_video_iconInfo = document.querySelectorAll('#services .recent_video .icon-info')
        let recent_video_manageBtn = document.querySelectorAll('#services .recent_video .video-follow-box')
        recent_video_manageBtn.forEach((elem, idx) => {
            elem.addEventListener('click', () => {
                if(elem.classList.contains('plus')) {
                    elem.className = 'video-follow-box minus';
                    elem.innerHTML = `<i class="icofont-minus"></i>`
                }
                else {
                    elem.className = 'video-follow-box plus';
                    elem.innerHTML = `<i class="icofont-plus"></i>`
                }
                manageVideoToggle(recent_video_iconInfo[idx].querySelector('.video_id').innerHTML)
            })
        })
    }   // 다시보기 HTML 추가하기
    function addClips_iconBox(data) {
        let row_box = document.createElement('div')
        row_box.className = 'row icon-set';

        for(let i=0;i<data.length-1;i++) {
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
        recent_clip_addMore.setAttribute('next-id', data[data.length-1].next);
        recent_clip.insertBefore(row_box, recent_clip_addMore);
    }   // 클립 HTML 추가하기

    refresh_btn.addEventListener('click', (e) => {
        ajaxAwait('/detail/request/refresh', 'POST', user_id, (res) => {
            console.log(res);
        })
    })  // 새로고침 버튼 이벤트

    getDataLive(user_id);
    getDataReplay('0');
    getDataClip('0');
});









function detailInit(){
    // 0 - 라이브, 1 - 다시보기, 2 - 클립 , 3 - 연관 스트리머
    let user_id = document.querySelector('#about .profile .user_id').innerHTML;
    let live_box = document.querySelector('#services .live-box')

    let recent_video = document.querySelector('#services .recent_video')
    let recent_video_addMore = document.querySelector('#services .recent_video .addMore')

    let recent_clip = document.querySelector('#services .recent_clip')
    let recent_clip_addMore = document.querySelector('#services .recent_clip .addMore')

    let relative_box = document.querySelector('#services .relative_box')
    let relative_box_addMore = document.querySelector('#services .relative_box .addMore')
    let relative_file = {}
    let relative_file_num = 0;


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
                            <input type="checkbox" class="form-check-input checkInput" ${data[i].isManaged ? 'checked' : ''}/>
                        </div>
                    </div>
                </div>
            `;
            row_box.appendChild(col_box)
        }
        relative_box.insertBefore(row_box, relative_box_addMore);
        relative_box.removeChild(relative_box.querySelector('.loading'))
        relative_file_num += 8;

        let relative_user_id_set = row_box.querySelectorAll('.user_id')
        let relative_checkInput = row_box.querySelectorAll('.check .checkInput')
        relative_checkInput.forEach((elem, idx) => {
            elem.addEventListener('change', () => {
                console.log(relative_user_id_set[idx].innerHTML)
                manageFollowToggle(relative_user_id_set[idx].innerHTML);
            })
        })

        relative_box_addMore.addEventListener('click', relative_addEvent)
    }   // 연관 스트리머 HTML 추가하기

    function replay_addEvent() {
        recent_video_addMore.removeEventListener('click', replay_addEvent)
        let recent_video_row = recent_video.querySelectorAll('.row')
        let recent_video_last_next = recent_video_row[recent_video_row.length-1].querySelector('.next_page').innerHTML
        setting_replay_req("after="+recent_video_last_next+"&");
    }   // 다시보기 추가 버튼 이벤트
    function clips_addEvent() {
        recent_clip_addMore.removeEventListener('click', clips_addEvent)
        let recent_clip_row = recent_clip.querySelectorAll('.row')
        let recent_clip_last_next = recent_clip_row[recent_clip_row.length-1].querySelector('.next_page').innerHTML
        setting_clip_req("after="+recent_clip_last_next+"&");
    }   // 클립 추가 버튼 이벤트
    function relative_addEvent() {
        relative_box_addMore.removeEventListener('click', relative_addEvent)
        // relative_box.insertBefore(createLoadingBox(), relative_box_addMore)
        relative_format_twitchUserSet(relative_file.slice(relative_file_num, relative_file_num+8))
    }   // 연관 스트리머 추가 버튼 이벤트

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
    }   // 클립 데이터 가져오기 요청
    let setting_relative_req = function(left, right) {
        // relative_box.insertBefore(createLoadingBox(), relative_box_addMore)
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
    }   // 연관 스트리머 데이터 가져오기 요청
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
    }   // twitchUser 포맷 요청

    setting_clip_req('');
    setting_relative_req(relative_file_num,relative_file_num + 8);
}
function detail_TabInit() {
    let tab_select_btn = document.querySelectorAll('#tab .menu-box')
    let service_boxes = document.querySelectorAll('#services .container > div')
    let selected_btn_idx = 0;
    tab_select_btn.forEach((elem,idx) => {
        elem.addEventListener('click', () => {
            tab_select_btn[selected_btn_idx].classList.remove('active')
            service_boxes[selected_btn_idx].classList.add('displayNone')
            selected_btn_idx = idx;
            tab_select_btn[selected_btn_idx].classList.add('active')
            service_boxes[selected_btn_idx].classList.remove('displayNone')
        })
    })  // 탭 버튼 클릭 이벤트
}
/*detailInit();*/
detail_TabInit();

document.addEventListener("DOMContentLoaded", function(){

    let user_id = document.querySelector('#about .profile .user_id').innerHTML;
    let live_box = document.querySelector('#services .live-box')

    let recent_video = document.querySelector('#services .recent_video')
    let recent_video_addMore = document.querySelector('#services .recent_video .addMore')
    let recent_video_addMore_flag = 1;

    let recent_clip = document.querySelector('#services .recent_clip')
    let recent_clip_addMore = document.querySelector('#services .recent_clip .addMore')
    let recent_clip_addMore_flag = 1;

    let relative_box = document.querySelector('#services .relative_box')
    let relative_box_addMore = document.querySelector('#services .relative_box .addMore')
    let relative_file = [];
    let relative_file_num = 0;

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
        recent_video.insertBefore(createLoadingBox(), recent_video_addMore);
        ajaxAwait('/detail/request/replay', 'POST', JSON.stringify(bodyData), (res) => {
            let result = JSON.parse(res);
            if(res !== 'error') {
                addVideo_iconBox(result);
            }
            recent_video_addMore_flag = 0;
        })
    }   // /detail/request/replay POST - 다시보기 데이터 가져오기
    function getDataClip(body) {
        recent_clip.insertBefore(createLoadingBox(), recent_clip_addMore);
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
    }   // /detail/request/clip POST - 클립 데이터 가져오기
    function getDataRelative(body) {
        relative_box.insertBefore(createLoadingBox(), relative_box_addMore);
        ajaxAwait('/detail/request/relative', 'POST', body, (res) => {
            let result = JSON.parse(res);
            console.log(result.slice(0,20))
            addRelative_iconBox(result.slice(0,20))
            // if(res !== 'error') {
            //     addVideo_iconBox(result);
            // }
            // recent_video_addMore_flag = 0;
        })
    }   // /detail/request/replay POST - 연관 스트리머 데이터 가져오기


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
        recent_video.removeChild(recent_video.querySelector('.loading'))
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
        recent_clip.removeChild(recent_clip.querySelector('.loading'))
    }   // 클립 HTML 추가하기
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

        // relative_box_addMore.addEventListener('click', relative_addEvent)
    }   // 연관 스트리머 HTML 추가하기

    refresh_btn.addEventListener('click', (e) => {
        ajaxAwait('/detail/request/refresh', 'POST', user_id, (res) => {
            alert('새로고침 되었습니다.')
            location.reload();
        })
    })  // 새로고침 버튼 이벤트

    getDataLive(user_id);
    getDataReplay('0');
    getDataClip('0');
    getDataRelative(user_id);
});

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
detail_TabInit();

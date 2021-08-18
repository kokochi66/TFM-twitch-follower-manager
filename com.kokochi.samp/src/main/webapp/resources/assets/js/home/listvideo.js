function listVideo() {
    let service_video_boxes = document.querySelectorAll('#services .container > div')
    let AboutListBtns = document.querySelectorAll('#about .listBtn')

    let recent_video_box = document.querySelector('#services .recent_video')
    let recent_video_addBtn = document.querySelector('#services .recent_video .addMore')
    let recent_video_data = [];

    let recent_live_box = document.querySelector('#services .recent_live')
    let recent_live_addBtn = document.querySelector('#services .recent_live .addMore')

    let recent_clip_box = document.querySelector('#services .recent_clip')
    let recent_clip_addBtn = document.querySelector('#services .recent_clip .addMore')
    let recent_clip_data = [];
    recent_video_box.insertBefore(createLoadingBox(), recent_video_addBtn)
    recent_live_box.insertBefore(createLoadingBox(), recent_live_addBtn)
    recent_clip_box.insertBefore(createLoadingBox(), recent_clip_addBtn)
  
    AboutListBtns.forEach((elem, idx) => {
      elem.addEventListener('click', () => {
        service_video_boxes.forEach(video_elem => {video_elem.classList.add('displayNone')})
        service_video_boxes[idx].classList.remove('displayNone')
      })
    })
    recent_video_addBtn.addEventListener('click', () => {
      recent_video_box.insertBefore(createLoadingBox(), recent_video_addBtn)
      let next_body = [],
        recent_video_rowBox = recent_video_box.querySelectorAll('.icon-set'),
        recent_video_rowBox_last = recent_video_rowBox[recent_video_rowBox.length-1];
        recent_video_rowBox_last_userId = recent_video_rowBox_last.querySelectorAll('.user_id')
        recent_video_rowBox_last_nextPage = recent_video_rowBox_last.querySelectorAll('.next_page')
      for(let i=0;i<recent_video_rowBox_last_userId.length;i++) {
        next_body[i] = [];
        next_body[i][0] = recent_video_rowBox_last_userId[i].innerHTML
        next_body[i][1] = recent_video_rowBox_last_nextPage[i].innerHTML
      }
      request_getMyRecentVideo(JSON.stringify(next_body))
    })
    recent_live_addBtn.addEventListener('click', () => {
      recent_live_box.insertBefore(createLoadingBox(), recent_live_addBtn)
      let next_body = [],
        recent_live_rowBox = recent_live_box.querySelectorAll('.icon-set'),
        recent_live_rowBox_last = recent_live_rowBox[recent_live_rowBox.length-1];
        recent_live_rowBox_last_userId = recent_live_rowBox_last.querySelectorAll('.user_id')
        recent_live_rowBox_last_nextPage = recent_live_rowBox_last.querySelectorAll('.next_page')
      for(let i=0;i<recent_live_rowBox_last_userId.length;i++) {
        next_body[i] = [];
        next_body[i][0] = recent_live_rowBox_last_userId[i].innerHTML
        next_body[i][1] = recent_live_rowBox_last_nextPage[i].innerHTML
      }
      request_getMyLiveVideo(JSON.stringify(next_body))
    })
    recent_clip_addBtn.addEventListener('click', () => {
      recent_clip_box.insertBefore(createLoadingBox(), recent_clip_addBtn)
      for(let i=0;i<8;i++) {
        if(recent_clip_data[i].nextPage) {
          let next_body = [recent_clip_data[i].user_id, recent_clip_data[i].nextPage]
          request_getMyClipVideoNext(JSON.stringify(next_body))
        }
      }
      addService_IconSet(recent_clip_data.slice(0, 8), recent_clip_box, recent_clip_addBtn)
      recent_clip_data = recent_clip_data.slice(8, recent_clip_data.length);
    })

    function request_getMyRecentVideo(body) {
      let response = fetch('/home/request/getMyRecentVideo', {
          method: 'POST', 
          headers: {
              'Content-Type': 'application/json;charset=utf-8'
          },
          body : body
      }).then(function(res){ 
          res.json()
          .then(result => { // 결과값을 json 객체로 받아옴
              console.log(result)
              for(let i=0;i<result.length;i++) {
                console.log(result[i].created_at)
              }
              result.sort((a,b) => {
                return Date.parse(a.created_at) < Date.parse(b.created_at) ? -1 : 1
              })
              if(result !== 'error') addService_IconSet(result, recent_video_box, recent_video_addBtn)
          })
          .catch(resB => {
            recent_video_box.removeChild(recent_video_box.querySelector('.loading'));
          })
      }).catch(function(res){ 
          console.log('catch res :: ' , res)
      })
    }
    function request_getMyRecentVideoNext(body) {
      let response = fetch('/home/request/getMyRecentVideo/next', {
          method: 'POST', 
          headers: {
              'Content-Type': 'application/json;charset=utf-8'
          },
          body : body
      }).then(function(res){ 
          res.json()
          .then(result => { // 결과값을 json 객체로 받아옴
              // console.log(result)
              if(result !== 'error') {
                addService_IconSet(result, recent_video_box, recent_video_addBtn)
                for(let i=0;i<result.length;i++) recent_video_data.push(result[i])
                recent_video_data.sort((a,b) => {
                  return Date.parse(a.created_at) < Date.parse(b.created_at) ? -1 : 1
                })
              }
          })
          .catch(resB => {
            recent_video_box.removeChild(recent_video_box.querySelector('.loading'));
          })
      }).catch(function(res){ 
          console.log('catch res :: ' , res)
      })
    }
    function request_getMyLiveVideo() {
      let response = fetch('/home/request/getMyLiveVideo', { // 서버 자체에 POST 요청을 보냄.
          method: 'POST', 
          headers: {
              'Content-Type': 'application/json;charset=utf-8'
          },
          body : 'none'
      }).then(function(res){ 
          res.json()
          .then(result => { // 결과값을 json 객체로 받아옴
              // console.log(result)
              if(result !== 'error') addService_IconSet(result, recent_live_box, recent_live_addBtn)
          })
          .catch(resB => {
            console.log('res json catch :: ' , resB)
            recent_live_box.removeChild(recent_live_box.querySelector('.loading'));
          });
      }).catch(function(res){ 
          console.log('catch res :: ' , res)
      })
    }
    function request_getMyClipVideoNext(body) {
      let response = fetch('/home/request/getMyClipVideo/next', { // 서버 자체에 POST 요청을 보냄.
          method: 'POST', 
          headers: {
              'Content-Type': 'application/json;charset=utf-8'
          },
          body : body
      }).then(function(res){ 
          res.json()
          .then(result => {
              // console.log(result)
              if(result !== 'error') {
                // 쿼리 결과 처리
                for(let i=0;i<result.length;i++) recent_clip_data.push(result[i])
                recent_clip_data.sort((a,b) => {
                  return b.view_count - a.view_count
                })
                console.log(recent_clip_data)
              }
          }).catch(resB => {
            console.log(resB)
            recent_clip_box.removeChild(recent_clip_box.querySelector('.loading'));
          })
      }).catch(function(res){ 
          console.log('catch res :: ' , res)
      })
    }
    function request_getMyClipVideo(body) {
      let response = fetch('/home/request/getMyClipVideo', { // 서버 자체에 POST 요청을 보냄.
          method: 'POST', 
          headers: {
              'Content-Type': 'application/json;charset=utf-8'
          },
          body : body
      }).then(function(res){ 
          res.json()
          .then(result => { // 결과값을 json 객체로 받아옴
              // console.log(result)
              if(result !== 'error') {
                recent_clip_data = result;
                addService_IconSet(recent_clip_data.slice(0, 8), recent_clip_box, recent_clip_addBtn)
                recent_clip_data = recent_clip_data.slice(8, recent_clip_data.length);
              }
          }).catch(resB => {
            console.log(resB)
            recent_clip_box.removeChild(recent_clip_box.querySelector('.loading'));
          })
      }).catch(function(res){ 
          console.log('catch res :: ' , res)
      })
    }
    function addService_IconSet(data, target, last) {
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

      target.insertBefore(s_row, last);
      target.removeChild(target.querySelector('.loading'));
    }
    request_getMyRecentVideo('none')
    request_getMyLiveVideo('none')
    request_getMyClipVideo('none')
  }
  listVideo();
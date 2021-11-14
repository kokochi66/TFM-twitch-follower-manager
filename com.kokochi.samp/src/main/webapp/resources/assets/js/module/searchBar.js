function init_SearchBar() {
    let searchBar_input_text = document.querySelector('#searchBar_text')
    let searchBar_input_preview = document.querySelector('#searchbar .input-preview')

    searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
    // searchBar_input_text.addEventListener('focusin', () => {
    //     searchBar_input_preview.classList.add('active');
    // })
    document.addEventListener('click', (e) => {
        // console.log(e.target , searchBar_input_preview)
        if(e.target !== searchBar_input_preview &&
            e.target !== searchBar_input_text) {
            searchBar_input_preview.classList.remove('active');
        }
    }) // 영역 이외의 부분을 클릭하면 검색 미리보기 지우기


    async function searchBar_input_searchEvent() {
        if(!searchBar_input_text.value) return; // 널값이 입력될 경우에는 쿼리를 진행하지 않음.
        searchBar_input_preview.classList.add('active');    // 창 표시하기
        searchBar_input_text.removeEventListener('input', searchBar_input_searchEvent)
        // 검색 쿼리가 진행되는 동안 쿼리가 중복 진행되지 않도록, 이벤트를 잠시동안 제거
        searchBar_input_preview.innerHTML = ''
        // 이전의 미리보기에 존재하던 값을 초기화해줌.
        
        let response = fetch('/query/request/searchStreams', { // 서버 자체에 POST 요청을 보냄.
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: searchBar_input_text.value    // 요청 body에 검색창 입력값을 넣음
        }).then(function(res){ 
            res.json().then(result => { // 결과값을 json 객체로 받아옴
                // console.log(result)
                searchBar_add_searchPreview(result) // 받아온 객체 배열로 뷰에 추가해줌.
            }).catch(resB => {console.log(resB);searchBar_input_text.addEventListener('input', searchBar_input_searchEvent);});
        }).catch(function(res){ 
            console.log('catch res :: ' , res)
            // 에러가 발생한 경우에는 아무런 값이 뷰에 추가되지 않음.
            searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
        })
    }

    function searchBar_add_searchPreview(data) {
        for(let i=0;i<data.length;i++) {
            let c_input_box = document.createElement('a')
            c_input_box.className = 'input-preview-cont linkBox'
            c_input_box.href = `/detail?streams=${data[i].id}`
            c_input_box.innerHTML = `
                <div class="profile_img">
                    <img src="${data[i].thumbnail_url}" />
                </div>
                <div class="user_nickname">${data[i].display_name}</div>
                <div class="user_login">${data[i].broadcaster_login}</div>
                <div class="user_id displayNone">${data[i].id}</div>
            `;
            searchBar_input_preview.appendChild(c_input_box)
        }
        searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
    }
}
init_SearchBar();
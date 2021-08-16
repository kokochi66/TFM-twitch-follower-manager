function init_SearchBar() {
    let searchBar_input_text = document.querySelector('#searchBar_text')
    let searchBar_input_preview = document.querySelector('#searchbar .input-preview')

    searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
    // searchBar_input_text.addEventListener('focusin', () => {
    //     searchBar_input_preview.classList.add('active');
    // })
    document.addEventListener('click', (e) => {
        console.log(e.target , searchBar_input_preview)
        if(e.target !== searchBar_input_preview &&
            e.target !== searchBar_input_text) {
            searchBar_input_preview.classList.remove('active');
        }
    }) // 영역 이외의 부분을 클릭하면 검색 미리보기 지우기


    async function searchBar_input_searchEvent() {
        searchBar_input_preview.classList.add('active');
        searchBar_input_text.removeEventListener('input', searchBar_input_searchEvent)
        if(!searchBar_input_text.value) {
            searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
            return;
        }
        searchBar_input_preview.innerHTML = ''
        
        const response = fetch('/query/request/searchStreams', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: searchBar_input_text.value
        }).then(function(res){ 
            res.json().then(result => {
                // console.log(result)
                searchBar_add_searchPreview(result)
            });
        }).catch(function(res){ 
            console.log('catch res :: ' , res) 
            searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
        })
    }
    function searchBar_add_searchPreview(data) {
        for(let i=0;i<data.length;i++) {
            let c_input_preview_cont = document.createElement('a')
            c_input_preview_cont.className = 'input-preview-cont linkBox'
            c_input_preview_cont.href = '/detail?streams='+data[i].id


            let c_input_preview_profile = document.createElement('div')
            c_input_preview_profile.className = 'profile_img'
            let c_input_preview_profile_img = document.createElement('img')
            c_input_preview_profile_img.src = data[i].thumbnail_url
            c_input_preview_profile.appendChild(c_input_preview_profile_img)

            let c_input_preview_userNickname = document.createElement('div')
            c_input_preview_userNickname.className = 'user_nickname'
            c_input_preview_userNickname.innerHTML = data[i].display_name
            let c_input_preview_user_login = document.createElement('div')
            c_input_preview_user_login.className = 'user_login'
            c_input_preview_user_login.innerHTML = data[i].broadcaster_login
            let c_input_preview_userId = document.createElement('div')
            c_input_preview_userId.className = 'user_id displayNone'
            c_input_preview_userId.innerHTML = data[i].id

            c_input_preview_cont.appendChild(c_input_preview_profile)
            c_input_preview_cont.appendChild(c_input_preview_userNickname)
            c_input_preview_cont.appendChild(c_input_preview_user_login)
            c_input_preview_cont.appendChild(c_input_preview_userId)
            searchBar_input_preview.appendChild(c_input_preview_cont)
        }
        searchBar_input_text.addEventListener('input', searchBar_input_searchEvent)
    }
}
init_SearchBar();
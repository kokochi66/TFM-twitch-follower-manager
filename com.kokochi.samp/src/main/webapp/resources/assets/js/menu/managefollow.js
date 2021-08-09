let followBoxes = document.querySelectorAll('#content .follow-box'),
    followBtn = document.querySelectorAll('#content .follow-box .checkInput')

followBtn.forEach((elem, idx) => {
    elem.addEventListener('change', () => {
        if(elem.checked) {
            followBoxes[idx].classList.add('mylist');
            httpRequest = new XMLHttpRequest();
            httpRequest.open('POST', '/menu/request/managedfollow/add');
            httpRequest.setRequestHeader('user_id', 'kokochi');
            httpRequest.setRequestHeader('to_user',
             followBoxes[idx].querySelector('.login').innerHTML.slice(1).slice(0,-1));

            let user_json = {
                'user':'kokochi',
                'pwd':'1234'
            }
            httpRequest.setRequestHeader('user_map', JSON.stringify(user_json));


            httpRequest.onload = () => {
                if(httpRequest.status == 200) {
                    let result = httpRequest.response;
                    console.log(result)
                    return result;
                } else {
                    alert('error')
                }
            }

            httpRequest.send();
        }
        else {
            followBoxes[idx].classList.remove('mylist');
            httpRequest = new XMLHttpRequest();
            httpRequest.open('POST', '/menu/request/managedfollow/remove');
            httpRequest.setRequestHeader('user_id', 'kokochi');
            httpRequest.setRequestHeader('to_user',
             followBoxes[idx].querySelector('.login').innerHTML.slice(1).slice(0,-1)
             );
            httpRequest.send();
        }
    })
})
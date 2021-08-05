let followBoxes = document.querySelectorAll('#content .follow-box'),
    followBtn = document.querySelectorAll('#content .follow-box .checkInput')

followBtn.forEach((elem, idx) => {
    elem.addEventListener('change', () => {
        if(elem.checked) {
            followBoxes[idx].classList.add('mylist');
            httpRequest = new XMLHttpRequest();
            httpRequest.open('POST', '/request/managedfollow/add');
            httpRequest.setRequestHeader('user_id', 'kokochi');
            httpRequest.setRequestHeader('to_user',
             followBoxes[idx].querySelector('.login').innerHTML.slice(1).slice(0,-1)
             );
            httpRequest.send();
        }
        else {
            followBoxes[idx].classList.remove('mylist');
            httpRequest = new XMLHttpRequest();
            httpRequest.open('POST', '/request/managedfollow/remove');
            httpRequest.setRequestHeader('user_id', 'kokochi');
            httpRequest.setRequestHeader('to_user',
             followBoxes[idx].querySelector('.login').innerHTML.slice(1).slice(0,-1)
             );
            httpRequest.send();
        }
    })
})
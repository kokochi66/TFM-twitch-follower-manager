let followBoxes = document.querySelectorAll('#content .follow-box'),
    followBtn = document.querySelectorAll('#content .follow-box .checkInput')

followBtn.forEach((elem, idx) => {
    elem.addEventListener('change', () => {
        if(elem.checked) followBoxes[idx].classList.add('mylist');
        else followBoxes[idx].classList.remove('mylist');
    })
})
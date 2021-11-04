let default_img = '/resources/assets/img/default_image.jpg';

//===== 공통 스크립트 =====//
// 로딩박스 생성
function createLoadingBox() {
    let loading_box = document.createElement('div');
    loading_box.className = 'loading m-auto mt-5 mb-5'
    return loading_box
}
// 동기 Ajax 통신
async function ajaxAwait(url, method, body, callback){
    let response = await fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body : body
    }).then(res => {
        res.text().then(text => {
            callback(text)
        })
    })
    .catch(res => {
        alert('ajaxAwait 에러가 발생하였습니다.');
        console.log('catch res :: ' , res)
    })
}
// 비동기 Ajax 통신
function ajaxSubmit(url, method, body, callback) {
    let response = fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body : body
    }).then(res => {
        res.text().then(text => {
            callback(text)
        })
    })
    .catch(res => {
        alert('ajaxAwait 에러가 발생하였습니다.');
        console.log('catch res :: ' , res)
    })
}
//=====//공통 스크립트//=====//
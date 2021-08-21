async function manageVideoToggle(data) {
    let response = await fetch('/manage/video/toggle', {
        method: 'POST', 
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body : data
    }).then(res => {
        res.text().then(text => {
            console.log(text)
        })
    })
    .catch(res => { 
        console.log('catch res :: ' , res)
    })
}   // 다시보기를 관리목록에 추가/삭제

async function manageFollowToggle(data) {
    let response = await fetch('/manage/follow/toggle', {
        method: 'POST', 
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body : data
    }).then(res => {
        res.text().then(text => {
            console.log(text)
        })
    })
    .catch(res => { 
        console.log('catch res :: ' , res)
    })
}   // 스트리머를 관리목록에 추가/삭제
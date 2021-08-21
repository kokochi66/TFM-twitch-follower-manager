async function manageVideoToggle(data) {
    let response = fetch('/manage/video/toggle', {
        method: 'POST', 
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body : data
    }).then(res => {
        res.text().then(text => {
            let response_text = text;
        })
    })
    .catch(res => { 
        console.log('catch res :: ' , res)
    })
    return response_text;
}
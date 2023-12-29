const submitBtn = document.querySelector("#submitBtn");

submitBtn.addEventListener("click", function(){
    const article = {
        articleWriter : document.querySelector("#writer").value,
        title : document.querySelector("#title").value,
        content : document.querySelector("#content").value
    }

    console.log(article)

    const url = "/article";
    fetch(url,{
        method: "POST",
        body : JSON.stringify(article),
        headers : {
            "Content-Type": "application/json"
        }
    }).then(response => {
        const msg = (response.ok) ? "글이 등록되었습니다" : "오류 발생"
        alert(msg);
        window.location.href ="/articles";
    })
})
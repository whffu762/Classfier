const submitBtn = document.querySelector("#submitBtn");

submitBtn.addEventListener("click", function(){
    const article = {
        articleId : document.querySelector("#articleId").value,
        articleWriter : document.querySelector("#writer").value,
        title : document.querySelector("#title").value,
        content : document.querySelector("#content").value
    }

    console.log(article)

    const url = "/article/edit/"+ article.articleId;
    console.log("url : " + url)
    fetch(url,{
        method: "PATCH",
        body : JSON.stringify(article),
        headers : {
            "Content-Type": "application/json"
        }
    }).then(response => {
        const msg = (response.ok) ? "글이 수정되었습니다" : "오류 발생"
        alert(msg);

        window.location.href ="/article/detail/"+ article.articleId;
    })
})
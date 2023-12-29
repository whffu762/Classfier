const submitBtn = document.querySelector("#submitBtn");
const articleId = document.querySelector("#articleId").value;
submitBtn.addEventListener("click", function(){
    const article = {
        articleId : articleId,
        articleWriter : document.querySelector("#writer").value,
        title : document.querySelector("#title").value,
        content : document.querySelector("#content").value
    }

    if(article.articleWriter.trim() === "" || article.content.trim() === ""){
        alert("공백이 있으면 안됩니다");
    }
    else{
        const url = "/article";
        fetch(url,{
            method: "PUT",
            body : JSON.stringify(article),
            headers : {
                "Content-Type": "application/json"
            }
        }).then(response => {
            const msg = (response.ok) ? "글이 수정되었습니다" : "오류 발생"
            alert(msg);
            window.location.href ="/article?no="+ articleId;
        })
    }
})

function getArticle(){
    window.location.href = "/article?no=" + articleId;
}
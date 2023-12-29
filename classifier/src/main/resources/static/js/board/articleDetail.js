const article_Id = document.querySelector("#hiddenId").value;
const comment_writer = document.querySelector("#commentWriter").value;

function deleteArticle() {
    const url = "/article?no=" + article_Id;
    const result = confirm("글을 삭제하겠습니까")
    if (result) {
        fetch(url, {
            method: "DELETE",
        }).then(response => {
            const msg = (response.ok) ? "글이 삭제되었습니다" : "오류 발생"
            alert(msg);
            window.location.href = "/articles";
        })
    }
}

function getUpdateArticleForm(){
    window.location.href = "/article/edit?no=" + article_Id;
}


const submitBtn = document.querySelector("#commentBtn");
submitBtn.addEventListener("click", function () {

    const url = "/comment?no="+article_Id;
    if (!comment_writer) {
        fetch(url).then(response => {
            window.location.href = response.url
        })
    } else {
        const comment = {
            articleId: article_Id,
            commentWriter: comment_writer,
            content: document.querySelector("#commentContent").value
        }

        if (comment.content.trim()) {
            fetch(url, {
                method: "POST",
                body: JSON.stringify(comment),
                headers: {
                    "Content-Type": "application/json"
                }
            }).then(response => {
                const msg = (response.ok) ? "댓글이 등록되었습니다" : "오류 발생"
                alert(msg);
                window.location.reload();
            })
        } else {
            alert("댓글을 입력하세요")
        }
    }
})

function commentSelectFunc(comment_id, writer, content) {

    $('#commentUpdateId').val(comment_id)
    $('#commentUpdateWriter').val(writer)
    $('#commentUpdateContent').val(content)
}

const commentUpdateBtn = document.querySelector("#commentUpdateBtn");
commentUpdateBtn.addEventListener("click", function () {
    const comment = {
        articleId: article_Id,
        commentId: document.querySelector("#commentUpdateId").value,
        commentWriter: document.querySelector("#commentUpdateWriter").value,
        content: document.querySelector("#commentUpdateContent").value
    }
    const url = "/comment"
    if (comment.content.trim()) {
        fetch(url, {
            method: "PUT",
            body: JSON.stringify(comment),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
            const msg = (response.ok) ? "댓글이 수정되었습니다" : "오류 발생"
            alert(msg);
            window.location.reload();
        })
    } else {
        alert("댓글을 입력하세요")
    }
})

function commentDeleteFunc(comment_id) {
    const comment = {
        articleId : article_Id,
        commentId : comment_id
    }
    const url = "/comment";
    const result = confirm("글을 삭제하겠습니까")
    if (result) {
        fetch(url, {
            method: "DELETE",
            body: JSON.stringify(comment),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
            const msg = (response.ok) ? "글이 삭제되었습니다" : "오류 발생"
            alert(msg);
            window.location.reload();
        })
    }
}

function needLoginLikeHate() {
    const url = "/article/like-hate?no=" + article_Id;
    fetch(url).then(response => {
        window.location.href = response.url
    })
    alert("로그인이 필요합니다")
}

const likeBtn = document.querySelector("#likeBtn");
likeBtn.addEventListener("change", function () {
    const likeBtn = document.querySelector("#likeBtn");
    const hateBtn = document.querySelector("#hateBtn");
    const likeLabel = document.querySelector("#likeNum");
    const hateLabel = document.querySelector("#hateNum");

    let url = "/article/like-hate";
    let likeCnt = Number(likeLabel.innerText);
    let hateCnt = Number(hateLabel.innerText);
    if (hateBtn.checked) {
        hateBtn.checked = false;
        hateLabel.innerText = --hateCnt;
        likeLabel.innerText = ++likeCnt;
    } else {
        if (likeBtn.checked) {
            likeLabel.innerText = ++likeCnt;
        } else {
            likeLabel.innerText = --likeCnt;
        }
    }
    makeLikeHateFetch(url, true);
});

const hateBtn = document.querySelector("#hateBtn");
hateBtn.addEventListener("change", function () {
    const likeBtn = document.querySelector("#likeBtn");
    const hateBtn = document.querySelector("#hateBtn");
    const likeLabel = document.querySelector("#likeNum");
    const hateLabel = document.querySelector("#hateNum");

    let url = "/article/like-hate";
    let hateCnt = Number(hateLabel.innerText);
    let likeCnt = Number(likeLabel.innerText);

    if (likeBtn.checked) {
        likeBtn.checked = false;
        likeLabel.innerText = --likeCnt;
        hateLabel.innerText = ++hateCnt;
    } else {
        if (hateBtn.checked) {
            hateLabel.innerText = ++hateCnt;
        } else {
            hateLabel.innerText = --hateCnt;
        }
    }
    makeLikeHateFetch(url, false);
})

function makeLikeHateFetch(url, check){
    const Dto = {
        articleId: article_Id,
        status : check
    }
    fetch(url, {
        method: "POST",
        body: JSON.stringify(Dto),
        headers: {
            "Content-Type": "application/json"
        }
    })
}
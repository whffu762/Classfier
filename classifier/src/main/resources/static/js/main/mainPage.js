window.onload = function () {
    $("#div_load_image").hide();
}

const submitForm = document.querySelector("#submitForm")
const submitButton = document.querySelector("#file-send-btn")
const uploadFiles  = document.querySelector("#files")

submitForm.addEventListener('submit', function(event) {
    event.preventDefault();

    const fLength = uploadFiles.files.length

    if(fLength === 5) {
        $(this).unbind('submit').submit();
        $("#div_load_image").show();
        $("#wrapper").css({ "opacity":"0.7"});
        submitButton.disabled = true;
    }
    else {
        alert("이미지는 5장을 선택해야 합니다.")
    }
})
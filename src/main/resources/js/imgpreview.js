$(document).ready(function () {
    // console.log("in the js0!");
    $("#imgFile").on('change', function () {
        //  console.log("in the js!");
        let countFiles = $(this)[0].files.length;
        let imgPath = $(this)[0].value;
        let extn = imgPath.substring(imgPath.lastIndexOf('.') + 1).toLowerCase();
        console.log(imgPath);
        let image_holder = $("#imgpreview");
        image_holder.empty();
        if (extn === "gif" || extn === "png" || extn === "jpg" || extn === "jpeg") {
            if (typeof (FileReader) != "undefined") {
                for (let i = 0; i < countFiles; i++) {
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        $("<img/>", {
                            "src": e.target.result,
                            "class": "small-image"
                        }).appendTo(image_holder);
                    }
                    image_holder.show();
                    reader.readAsDataURL($(this)[0].files[i]);
                }
            } else {
                console.log("not support FileReader.");
            }
        } else {
            console.log("Input error");
        }
    });
});
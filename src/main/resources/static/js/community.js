/**
 * 提交回复
 */


function commentPost() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(parentId, 1, content);
}


/**
 *
 */
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();

    comment2target(commentId, 2, content);
}

/**
 * 展开二级回复
 */

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    if (comments.hasClass("in")) {
        e.classList.remove("active");
        comments.removeClass("in");
    } else {
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length != 1){
            comments.addClass("in");
            e.classList.add("active");
        }else{
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"media-object img-rounded media-img",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>", {
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"comment-menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(Comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                comments.addClass("in");
                e.classList.add("active");

            });

        }



    }
}


/**
 * 提交回复复用函数
 */
function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 4001) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=2b0a53bc531a21c9f53b&redirect_uri=http://localhost:8888/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function showSelectTag() {
    $("#select-tag").show();
}


/**
 * 将选中的标签放入标签框
 * @param value
 */
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    
    if (previous.split(",").indexOf(value) == -1){
        if (previous){
            $("#tag").val(previous+","+value);
        }else{
            $("#tag").val(value);
        }
    }
}

function like(btn, entityType, entityId, entityUserId) {
    $.post(
        //post方法
        //url：CONTEXT_PATH + "/like"
        //传入参数：{"entityType":entityType,"entityId":entityId},
        CONTEXT_PATH + "/like",
        {"entityType":entityType,"entityId":entityId, "entityUserId":entityUserId},
        //data是返回的json字符串
        function(data) {
            //将json字符串转化为json对象
            data = $.parseJSON(data);
            if(data.code == 0) {
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':"赞");
            } else {
                alert(data.msg);
            }
        }
    );
}
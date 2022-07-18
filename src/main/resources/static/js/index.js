$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	/*点击发布之后需要隐藏发布框*/
	$("#publishModal").modal("hide");

	/*向服务器发送内容*/
	// 获取标题和内容
	/*jQuery 是一个js框架 $("#idName")是一个id选择器，选择器选取带有指定 id 的元素。
	id 引用 HTML 元素的 id 属性*/
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求(POST)
	/* $就是jQuery的别称
	* $.post() = jQuery.post()
	* */

	/*
	*$(selector).post(URL,data,function(data,status,xhr),dataType)
	* 其中只有URL是必须传入的
	* */
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		function(data) {
			data = $.parseJSON(data);
			// 在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2秒后,自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);
}
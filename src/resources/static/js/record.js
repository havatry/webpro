// 删除id对应的请求
function deleteRequest(id) {
    RequestPostX('http://localhost:8080/remove', {id: id}, function (data) {
        // data中有msg字段
        alert(data['msg']);
        flush(true);
    });
}

// 获取请求的参数
function getArguments(id) {
    RequestGetX('http://localhost:8080/arguments', {id: id}, function (data) {
        if (data['msg'] != null) {
            alert(data['msg']);
        } else {
            alert(JSON.stringify(data));
        }
    })
}

// 下载结果
function download(id) {
    var url = "http://localhost:8080/download";
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "id").attr("value", id));
    form.appendTo('body').submit().remove();
}

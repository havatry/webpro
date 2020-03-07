// 用于提交ajax的请求
function RequestPost(url, data, func_succ) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        contentType: false,
        processData: false,
        xhrFields: {withCredentials: true},
        success: func_succ,
        error: function(e){
            alert(JSON.stringify(e));
        }
    });
}

function RequestGet(url, data, func_succ) {
    $.ajax({
        type: "GET",
        url: url,
        data: data,
        dataType: "json",
        contentType: false,
        processData: false,
        xhrFields: {withCredentials: true},
        success: func_succ,
        error: function(e){
            alert(JSON.stringify(e));
        }
    });
}
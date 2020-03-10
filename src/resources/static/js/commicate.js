// 用于提交ajax的请求
function RequestPostVoid(url, data, func) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        contentType: false,
        processData: false,
        xhrFields: {withCredentials: true},
        error: func
    });
}

function RequestPost(url, data, func) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        // contentType: false,
        // processData: false,
        xhrFields: {withCredentials: true},
        success: func,
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function RequestGet(url, func_succ) {
    $.ajax({
        type: "GET",
        url: url,
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
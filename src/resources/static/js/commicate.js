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
        contentType: false,
        processData: false,
        xhrFields: {withCredentials: true},
        success: func,
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function RequestPostX(url, data, func) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        xhrFields: {withCredentials: true},
        success: func,
        error: function (data) {
            alert(JSON.stringify(data));
        }
    });
}

function RequestGetPage(dom, url, func_succ) {
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: false,
        processData: false,
        xhrFields: {withCredentials: true},
        success: func_succ,
        error: function(e){
            // alert(JSON.stringify(e));
            dom.innerHTML = '';
            alert('无法获取到请求数据，请检查网络连接或者联系管理员');
            // dom.innerHTML = '<h3 style="text-align:center; color:rgb(186, 85, 211); margin-top: 100px; margin-bottom: 100px">未能获取到请求数据</h3>'
        }
    });
}

function RequestGetPageWithDes(dom, url, func_succ) {
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        contentType: false,
        processData: false,
        xhrFields: {withCredentials: true},
        beforeSend: function () {
            dom.innerHTML = '<h3 style="text-align:center; color:rgb(64, 224, 208); margin-top: 100px; margin-bottom: 100px">请求数据中...</h3>';
        },
        success: func_succ,
        error: function(e){
            // alert(JSON.stringify(e));
            dom.innerHTML = '';
            alert('无法获取到请求数据，请检查网络连接或者联系管理员');
            // dom.innerHTML = '<h3 style="text-align:center; color:rgb(186, 85, 211); margin-top: 100px; margin-bottom: 100px">未能获取到请求数据</h3>'
        }
    });
}

function RequestGetX(url, data, func_succ) {
    $.ajax({
        type: "GET",
        url: url,
        data: data,
        dataType: "json",
        xhrFields: {withCredentials: true},
        success: func_succ,
        error: function(e){
            alert(JSON.stringify(e));
        }
    });
}
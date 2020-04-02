// 表单文件请求无返回值
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

// x-www请求无返回值
function RequestPostXVoid(url, data, func) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        xhrFields: {withCredentials: true},
        error: func
    });
}

// 表单文件请求有返回值
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
            alert(data['responseText']);
        }
    });
}

// x-www请求有返回值
function RequestPostX(url, data, func) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        xhrFields: {withCredentials: true},
        success: func,
        error: function (data) {
            alert(data['responseText']);
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
            $('#pagination').html("");
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
            alert(e['responseText']);
        }
    });
}
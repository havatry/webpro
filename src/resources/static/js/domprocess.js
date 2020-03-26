// 依据返回结果处理dom元素
function taskQueueUpdate(tableDom, data) {
    // alert(JSON.stringify(data))
    /**
     * {"data":[{"algorithm":"algorithm","date":"2020-03-07 23:52:01","id":74,"status":"在执行中","type":"手动分析"},{"algorithm":"algorithm","date":"2020-03-07 21:08:25","id":73,"status":"映射成功","type":"手动分析"},{"algorithm":"algorithm","date":"2020-03-07 21:07:57","id":72,"status":"映射成功","type":"手动分析"},{"algorithm":"algorithm","date":"2020-03-07 21:07:25","id":71,"status":"映射成功","type":"手动分析"},{"algorithm":"algorithm","date":"2020-03-07 20:54:58","id":70,"status":"映射成功","type":"手动分析"},{"algorithm":"algorithm","date":"2020-03-07 20:54:34","id":69,"status":"映射成功","type":"手动分析"}],"total":6}
     */
    var tab = '';
    var header = '<tr style="color: rgb(209, 73, 78);"><td>时间</td><td>类型</td>' +
        '<td>状态</td><td>结果</td><td>操作</td></tr>';
    // 填充数据
    var content = data['data'];
    if (content.length == 0) {
        tab = '<h3 style="text-align:center; color:rgb(186, 85, 211); margin-top: 100px; margin-bottom: 100px">暂时没有历史数据</h3>';
    } else {
        tab += header;
        for (var i = 0; i < content.length; i++) {
            var line = templateTr().format(content[i].date, content[i].type, statusLabel(content[i].status), content[i].status,
                (content[i].status == '在执行中' || content[i].status == '请求异常') ? 'btn btn-secondary disabled' : 'btn btn-primary',
                content[i].type == '手动分析' ? 'display:none' : '', content[i].id, (content[i].status == '在执行中' || content[i].status == '请求异常')
                    ? 'btn btn-secondary disabled' : 'btn btn-primary', content[i].id, content[i].id);
            tab += line;
        }
    }
    // 修改内容
    tableDom.innerHTML = tab;
}

// 分页插件
function renderPagination(jqueryObj, total, f) {
    jqueryObj.bootstrapPaginator({
        currentPage: 1,
        totalPages: total,
        size: 'normal',
        bootstrapMajorVersion: 3,
        align: 'right',
        numberOfPages: 10,
        itemTexts: function (type, page, current, f) {
            switch (type) {
                case 'first':
                    return '首页';
                case 'prev':
                    return '上页';
                case 'next':
                    return '下页';
                case 'last':
                    return '尾页';
                case 'page':
                    return page;
            }
        },
        onPageChanged: f
    })
}

function templateTr() {
    return '<tr><td>{0}</td><td>{1}</td><td><span class="{2}">{3}</span></td><td><button class="{4}" style="color: black;{5}">查看</button>' +
        '<button onclick="download({6})" class="{7}" style="color: black">下载</button></td><td><button class="btn btn-danger" onclick="deleteRequest({8})">删除</button>' +
        '<button class="btn btn-success" onclick="getArguments({9})">参数</button></td></tr>';
}

function statusLabel(status) {
    switch (status) {
        case '在执行中':
            return 'label label-default';
        case '执行完成':
            return 'label label-primary';
        case '映射成功':
            return 'label label-success';
        default:
            return 'label label-danger';
    }
}

String.prototype.format = function() {
    if (arguments.length == 0) return this;
    var param = arguments[0];
    var s = this;
    if (typeof(param) == 'object') {
        for (var key in param)
            s = s.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
        return s;
    } else {
        for (var i = 0; i < arguments.length; i++)
            s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
        return s;
    }
}
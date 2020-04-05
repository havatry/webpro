function search() {
    var c = $("#searchContent").val();
    if (c.trim() == "") {
        init();
    } else if (c.indexOf('local') >= 0) {
        // 本地指令, 设置页面高度
        var pair = c.split(":")[1].split("=");
        var h = parseInt(pair[1]);
        if (pair[0] == 'height') {
            $('.jumbotron').height(h);
            localStorage.setItem("content_height", h);
        } else if (pair[0] == 'margin') {
            $('table').css('margin-bottom', h);
            localStorage.setItem("table_margin", h);
        } else if (pair[0] == 'top') {
            $('.container').css('margin-top', h);
            localStorage.setItem("contain_margin", h);
        } else if (pair[0] == 'reset') {
            // 重置所以设置
            localStorage.clear();
            setProperty();
        }
    } else {
        RequestGetX("http://localhost:8080/search/record", {query: c, pageNo: 0}, function (data) {
            if (typeof(data['msg']) != 'undefined') {
                alert(data['msg']);
                return;
            }
            taskQueueUpdate(document.getElementById('tab'), data);
            var total = Math.max(Math.ceil(data['total'] / 10), 1);
            renderPagination($('#pagination'), total, function (event, oldPage, newPage) {
                RequestGetPage(tabDom, 'http://localhost:8080/search/record?pageNo='+(newPage - 1)+'&query='+c, function (data) {
                    taskQueueUpdate(document.getElementById('tab'), data);
                    flush(data['autoFlush']);
                })
            });
        });
    }
}
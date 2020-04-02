function search() {
    var c = $("#searchContent").val();
    if (c.trim() == "") {
        init();
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
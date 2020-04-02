// 手动分析
function submit_handle() {
    var doc = document.getElementById("handle_analyze");
    var formData = new FormData(doc);
    RequestPostVoid("http://localhost:8080/analyze/handle", formData, function () {
        flush(true);
    });
    // 擦除数据
    $('#handle').modal('hide');
    $('#handle').on('hidden.bs.modal', function () {
        doc.reset();
    })
}

// 一阶段实验
function submit_onestage() {
    var doc = document.getElementById("onestage_analyze");
    RequestPostXVoid("http://localhost:8080/handle/onestage", $("#onestage_analyze").serialize(), function () {
        flush(true);
    });
    // 擦除数据
    $('#experiment1').modal('hide');
    $('#experiment1').on('hidden.bs.modal', function () {
        doc.reset();
    })
}

// 二阶段协调实验
function submit_twostage() {
    var doc = document.getElementById("twostage_analyze");
    RequestPostXVoid("http://localhost:8080/handle/twostage", $("#twostage_analyze").serialize(), function () {
        flush(true);
    });
    // 擦除数据
    $('#experiment2').modal('hide');
    $('#experiment2').on('hidden.bs.modal', function () {
        doc.reset();
    })
}
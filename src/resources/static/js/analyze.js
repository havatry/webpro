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
    if (!check_oneStage()) {
        return;
    }
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
    if (!check_twoStage()) {
        return;
    }
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

function check_oneStage() {
    if ($("input[name='nodes']").val() == "") {
        $("input[name='nodes']").val("100-115-5");
    }
    if (parseInt($("input[name='nodes']").val().split("-")[1]) > 500) {
        alert('节点数最多不能超过500');
        return false;
    }
    if ($("input[name='nodeRatio']").val() == "") {
        $("input[name='nodeRatio']").val("0.1");
    }
    if ($("input[name='resourceRatio']").val() == "") {
        $("input[name='resourceRatio']").val("0.01");
    }
    return true;
}

function check_twoStage() {
    if ($("input[name='resource']").val() == "") {
        $("input[name='resource']").val("50-70");
    }
    if ($("input[name='alpha']").val() == "") {
        $("input[name='alpha']").val("0.5");
    }
    if ($("input[name='beta']").val() == "") {
        $("input[name='beta']").val("0.1");
    }
    if ($("input[name='arrive']").val() == "") {
        $("input[name='arrive']").val("1/10");
    }
    if ($("input[name='presever']").val() == "") {
        $("input[name='presever']").val("1/1000")
    }
    if ($("input[name='time']").val() == "") {
        $("input[name='time']").val("2000");
    }
    if (parseInt($("input[name='time']").val()) > 2000) {
        alert("仿真时间请不要超过2000时间单元，这可能花费较多时间，请重新设置一个小于或者等于2000的数值");
        return false;
    }
    return true;
}
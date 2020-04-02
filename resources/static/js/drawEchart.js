function look(id, type) {
    // 打开Echarts框
    // sessionStorage.setItem("type", type);
    $(".look-echarts").html(type);
    RequestGetX("http://localhost:8080/look", {id: id, type: type}, function (data) {
//            alert(JSON.stringify(data));
        $("#look").modal("show");
        var myChart = echarts.init(document.getElementById('graph'));
        legend = data["legend"].split(",");
        xAxis = [];
        setTimeout(function () {
            if (type == "一阶段实验") {
                ar = data["snodes"].split("-");
                for (var i = parseInt(ar[0]); i < parseInt(ar[1]); i += parseInt(ar[2])) {
                    xAxis.push(i.toString());
                }
                option = oneStageEchartsOptionTemplate(xAxis, legend, data["RE"], data["RA"], data["RR"],
                    data["SE"], data["SA"], data["SR"], data["GE"], data["GA"], data["GR"]);
            } else if (type == "二阶段协调实验") {
                for (var i = 50; i <= parseInt(data["total"]); i+=50) {
                    xAxis.push(i.toString());
                }
                option = twoStageEchartsOptionTemplate(xAxis, legend, data["AAE"], data["ABE"], data["SE"],
                    data["DE"], data["NE"], data["AAA"], data["ABA"], data["SA"], data["DA"], data["NA"],
                    data["AACR"], data["ABCR"], data["SCR"], data["DCR"], data["NCR"], data["AASD"], data["ABSD"],
                    data["SSD"], data["DSD"], data["NSD"]);
            }
            myChart.setOption(option, true);
        }, 100);
    })
}
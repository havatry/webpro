function oneStageEchartsOptionTemplate(xAxis, legend, RE, RA, RR, SE, SA, SR, GE, GA, GR) {
    option = {
        grid: [
            {x: '10%', y: '10%', width: '38%', height: '30%'},
            {x2: '3%', y: '27%', width: '40%', height: '50%'},
            {x: '10%', y2: '10%', width: '38%', height: '30%'},
        ],
        tooltip: {
            formatter: '{a}: ({c})'
        },
        xAxis: [
            {gridIndex: 0, boundaryGap: false, data: xAxis, type: 'category'},
            {gridIndex: 1, boundaryGap: false, data: xAxis, type: 'category'},
            {gridIndex: 2, boundaryGap: false, data: xAxis, type: 'category'},
        ],
        yAxis: [
            {gridIndex: 0, name: 'Acceptance Ratio'}, // y坐标系0-15
            {gridIndex: 1, name: 'Execution Time(ms)'},
            {gridIndex: 2, name: 'RevenueToCost Ratio'},
        ],
        legend: {
            left: 'right',
            data: legend
        },
        series: [
            {
                name: 'RCRGF',
                type: 'line',
                itemStyle : itemStyle_red,
                smooth: true,
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: RE
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                smooth: true,
                itemStyle : itemStyle_green,
                data: SE
            },
            {
                name: 'Greedy',
                type: 'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                smooth: true,
                itemStyle : itemStyle_blue,
                data: GE
            },
            {
                name: 'RCRGF',
                type: 'scatter',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbolSize: 5,
                itemStyle : itemStyle_red,
                data: RA
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'scatter',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbolSize: 5,
                itemStyle : itemStyle_green,
                data: SA
            },
            {
                name: 'Greedy',
                type: 'scatter',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbolSize: 5,
                itemStyle : itemStyle_blue,
                data: GA
            },
            {
                name: 'RCRGF',
                type: 'scatter',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbolSize: 5,
                itemStyle : itemStyle_red,
                data: RR
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'scatter',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbolSize: 5,
                itemStyle : itemStyle_green,
                data: SR
            },
            {
                name: 'Greedy',
                type: 'scatter',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbolSize: 5,
                itemStyle : itemStyle_blue,
                data: GR
            },
        ]
    };
    return option;
}

function twoStageEchartsOptionTemplate(xAxis, legend, AAE, ABE, SE, DE, NE, AAA, ABA, SA, DA, NA,
                                       AACR, ABCR, SCR, DCR, NCR, AASD, ABSD, SSD, DSD, NSD) {
    option = {
        grid: [
            {x: '10%', y: '15%', width: '38%', height: '30%'},
            {x2: '0%', y: '15%', width: '38%', height: '30%'},
            {x: '10%', y2: '7%', width: '38%', height: '30%'},
            {x2: '0%', y2: '7%', width: '38%', height: '30%'}
        ],
        tooltip: {
            formatter: 'Group {a}: ({c})'
        },
        xAxis: [
            {gridIndex: 0, boundaryGap: false, data: xAxis, type: 'category'},
            {gridIndex: 1, boundaryGap: false, data: xAxis, type: 'category'},
            {gridIndex: 2, boundaryGap: false, data: xAxis, type: 'category'},
            {gridIndex: 3, boundaryGap: false, data: xAxis, type: 'category'}
        ],
        yAxis: [
            {gridIndex: 0, name: "Acceptance Ratio"},
            {gridIndex: 1, name: "Execution Time"},
            {gridIndex: 2, name: "CostToRevenue Ratio"},
            {gridIndex: 3, name: "Standard Deviation"}
        ],
        legend: {
            left: "center",
            data: legend
        },
        series: [
            // 执行时间
            {
                name: 'AEF_Advance',
                type: 'line',
                itemStyle : itemStyle_red,
                symbol: 'none',
                smooth: true,
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: AAE
            },
            {
                name: 'AEF_Baseline',
                type: 'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_green,
                data: ABE
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_blue,
                data: SE
            },
            {
                name: 'D-ViNE_SP',
                type: 'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_cyan,
                data: DE
            },
            {
                name: 'NRM',
                type: 'line',
                xAxisIndex: 1,
                yAxisIndex: 1,
                symbol: 'none',
                smooth: true,
                itemStyle: itemStyle_violet,
                data: NE
            },
            // 接受率
            {
                name: 'AEF_Advance',
                type: 'line',
                itemStyle : itemStyle_red,
                symbol: 'none',
                smooth: true,
                xAxisIndex: 0,
                yAxisIndex: 0,
                data: AAA
            },
            {
                name: 'AEF_Baseline',
                type: 'line',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_green,
                data: ABA
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'line',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_blue,
                data: SA
            },
            {
                name: 'D-ViNE_SP',
                type: 'line',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_cyan,
                data: DA
            },
            {
                name: 'NRM',
                type: 'line',
                xAxisIndex: 0,
                yAxisIndex: 0,
                symbol: 'none',
                smooth: true,
                itemStyle: itemStyle_violet,
                data: NA
            },
            // 代价收益比
            {
                name: 'AEF_Advance',
                type: 'line',
                itemStyle : itemStyle_red,
                symbol: 'none',
                smooth: true,
                xAxisIndex: 2,
                yAxisIndex: 2,
                data: AACR
            },
            {
                name: 'AEF_Baseline',
                type: 'line',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_green,
                data: ABCR
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'line',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_blue,
                data: SCR
            },
            {
                name: 'D-ViNE_SP',
                type: 'line',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_cyan,
                data: DCR
            },
            {
                name: 'NRM',
                type: 'line',
                xAxisIndex: 2,
                yAxisIndex: 2,
                symbol: 'none',
                smooth: true,
                itemStyle: itemStyle_violet,
                data: NCR
            },
            // 标准差
            {
                name: 'AEF_Advance',
                type: 'line',
                itemStyle : itemStyle_red,
                symbol: 'none',
                smooth: true,
                xAxisIndex: 3,
                yAxisIndex: 3,
                data: AASD
            },
            {
                name: 'AEF_Baseline',
                type: 'line',
                xAxisIndex: 3,
                yAxisIndex: 3,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_green,
                data: ABSD
            },
            {
                name: 'SubgraphIsomorphism',
                type: 'line',
                xAxisIndex: 3,
                yAxisIndex: 3,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_blue,
                data: SSD
            },
            {
                name: 'D-ViNE_SP',
                type: 'line',
                xAxisIndex: 3,
                yAxisIndex: 3,
                symbol: 'none',
                smooth: true,
                itemStyle : itemStyle_cyan,
                data: DSD
            },
            {
                name: 'NRM',
                type: 'line',
                xAxisIndex: 3,
                yAxisIndex: 3,
                symbol: 'none',
                smooth: true,
                itemStyle: itemStyle_violet,
                data: NSD
            }
        ]
    };
    return option;
}
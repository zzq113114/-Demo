$package("phis.script")

$import("phis.script.SimpleModule", "phis.script.rmi.jsonRequest",
		"phis.script.rmi.miniJsonRequestSync",
		"phis.script.rmi.miniJsonRequestAsync", "phis.script.ux.echarts_min")

phis.script.echartsReport = function(cfg) {
	this.pageId = cfg.pageId; // 报表id

	phis.script.echartsReport.superclass.constructor.apply(this, [cfg]);
}
Ext.extend(phis.script.echartsReport, phis.script.SimpleModule, {
			initPanel : function() {
				if (this.panel)
					return this.panel;
				var panel = new Ext.Panel({
							html : '<div id="myecharts" style="width: 600px;height:400px;"></div>',
							border : false,
							bodyBorder : false
						});
				panel.on("afterrender", this.onReady, this);
				this.panel = panel;
				return panel;
			},
			onReady : function() {
				this.initEcharts();
			},
			initEcharts : function() {
				// 基于准备好的dom，初始化echarts实例
				var myChart = echarts
						.init(document.getElementById('myecharts'));

				// 指定图表的配置项和数据
				var option = {
					title : {
						text : 'ECharts 入门示例'
					},
					tooltip : {},
					legend : {
						data : ['销量']
					},
					xAxis : {
						data : ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
					},
					yAxis : {},
					series : [{
								name : '销量',
								type : 'bar',
								data : [5, 20, 36, 10, 10, 20]
							}]
				};

				// 使用刚指定的配置项和数据显示图表。
				myChart.setOption(option);
			}

		});

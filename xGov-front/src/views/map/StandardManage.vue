<template>
    <section class="map-standard-outer p-standard-container" :style="{height: ch > oldh ? oldh : ch}">
        <div class="container" :style="{ transform: 'scale(' + baseStyles.zoom + ')'}">
            <!--返回-->
            <div class="map-back" @click="backBase"></div>
            <!--头部模块-->
            <div class="main-header">
                标准管理区
            </div>
            <div class="main-body">
                 <div class="perspective">
                     <div id="warp">
                         <img @click="linkToCollect" class="model" src="../../assets/images/map/list/standard-link.png"/>
                         <img @click="linkToCollect" class="model" src="../../assets/images/map/list/standard-link.png"/>
                         <img @click="linkToCollect" class="model" src="../../assets/images/map/list/standard-link.png"/>
                     </div>
                  </div>
                <div class="main-body-l">
                    <div class="box-one">
                        <div class="box-title">
                            <img src="../../assets/images/map/icon/title.png"/>
                            标准概况
                            <span class="link"></span>
                        </div>
                        <ul class="box-one-content">
                            <li>
                                <span class="title">标准分类</span><br/>
                                <span class="num">{{generalInfo.dataSetNum}}</span><br/>
                            </li>
                            <li>
                                <span class="title">标准维护</span><br/>
                                <span class="num">{{generalInfo.dataElementNum}}</span><br/>
                            </li>
                            <li>
                                <span class="title">业务代码</span><br/>
                                <span class="num">{{generalInfo.codeSetNum}}</span><br/>
                            </li>
                            <li>
                                <span class="title">业务代码</span><br/>
                                <span class="num">{{generalInfo.codeNum}}</span><br/>
                            </li>
                            <li>
                                <span class="title">文档管理</span><br/>
                                <span class="num">{{generalInfo.documentNum}}</span><br/>
                            </li>
                        </ul>
                    </div>
                    <div class="box-two">
                        <div class="box-title">
                            <img src="../../assets/images/map/icon/title.png"/>
                            数据标准趋势分析
                            <span class="link"></span>
                        </div>
                        <div class="charts-content">
                            <div ref="chartBar" class="charts-inner-bar"></div>
                        </div>
                    </div>
                </div>
                <div class="main-body-c">
                    <div class="box-three">
                        <i class="box-three-img1"></i>
                        <i class="box-three-img2"></i>
                    </div>
                    <div class="box-four">
                        <div class="box-title">
                            <img src="../../assets/images/map/icon/title.png"/>
                            数据稽核率
                            <span class="link"></span>
                        </div>
                        <div class="charts-pie-inner" ref="chartPie">

                        </div>
                    </div>
                </div>
                <div class="main-body-r">
                    <div class="box-five">
                        <div class="box-title">
                            <img src="../../assets/images/map/icon/title.png"/>
                            常用数据标准
                            <span class="link"></span>
                        </div>
                        <div class="box-five-inner"  ref="chartWord">

                        </div>
                    </div>
                    <div class="box-six">
                        <div class="box-title">
                            <img src="../../assets/images/map/icon/title.png"/>
                            近期更新标准
                            <span class="link"></span>
                        </div>
                        <ul class="box-six-inner">
                            <li>
                                <span>标准名称</span>
                                <span>类型</span>
                                <span>状态</span>
                            </li>
                            <li :key="index" v-for="(item,index) in recentUpdate">
                                <span>{{item.name}}</span>
                                <span>{{item.type}}</span>
                                <span v-if="item.status==0">草稿</span>
                                <span v-if="item.status==1">待审核</span>
                                <span v-if="item.status==2">已审核</span>
                                <span v-if="item.status==3">变更中</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    import echarts from "echarts";
    import 'echarts-wordcloud/dist/echarts-wordcloud.min'
    export default {
        name: "",
        components: {},
        data() {
            return {
                tweenedNumber:0,
                oldw: document.body.clientWidth,
                oldh: document.body.clientHeight,
                ch: null,
                baseStyles: {
                    zoom:1

                },
                drawBarCharts:null,
                drawPieCharts:null,
                drawWordCharts:null,    //词云

                generalInfo:{},
                dataStandardsTrends:{},
                dateAuditRate:{
                    relCount: 95,
                    unRelCount: 5
                },
                commonElement:[],
                recentUpdate:[],
            }
        },
        methods: {
            setThree(){
                let oImgs=document.getElementsByClassName("model");
                let deg=360/(oImgs.length);
                for(let i=0;i<oImgs.length;i++){
                     oImgs[i].style.transform='rotateY('+i*deg+'deg) translateZ(400px)';
                }
            },
            getData(){
              this.$urlApi.mapData.standardData().then((res)=>{
                  this.generalInfo=res.data.generalInfo;
                  this.dataStandardsTrends=res.data.dataStandardsTrends;
                  this.dateAuditRate=res.data.dateAuditRate;
                  this.recentUpdate=res.data.recentUpdate.slice(0,4);
                  res.data.commonElement.forEach((item)=>{
                      item.name=item.dataElementName
                      item.value=item.count
                  });
                  this.commonElement=res.data.commonElement
                  this.drawBarChart();
                  this.drawPieChart();
                  this.drawWordChart()
              })
            },
            backBase(){
                this.$router.push('/dataMap')
            },
            linkToCollect(){
                this.$router.push('/mapCollect')
            },
            drawBarChart() {
                let that=this
                this.drawBarCharts = echarts.init(this.$refs.chartBar);
                this.drawBarCharts.setOption({
                    color: ['#349FFD', '#66DFF5', '#7687F2'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        itemWidth: 10,
                        itemHeight: 10,
                        itemGap: 20,
                        textStyle: {
                            color: '#c8cdd6'
                        },
                        data: ['标准维护', '业务代码', '文档管理']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            axisTick: {show: false},
                            axisLine: {show: false},
                            data: that.dataStandardsTrends.time,
                            axisLabel:{
                                color:'#93C0F6'
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            axisTick: {show: false},
                            axisLine: {show: false},
                            splitLine: {
                                show: false
                            },
                            axisLabel:{
                                color:'#93C0F6'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '标准维护',
                            type: 'bar',
                            barGap: 0,
                            data: that.dataStandardsTrends.element,
                            itemStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                        offset: 0,
                                        color: '#349FFD'
                                    }, {
                                        offset: 1,
                                        color: '#064175'
                                    }]),
                                }
                            },
                        },
                        {
                            name: '业务代码',
                            type: 'bar',
                            data: that.dataStandardsTrends.code,
                            itemStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                        offset: 0,
                                        color: '#66DFF5'
                                    }, {
                                        offset: 1,
                                        color: '#019DA9'
                                    }]),
                                }
                            },
                        },
                        {
                            name: '文档管理',
                            type: 'bar',
                            data: that.dataStandardsTrends.document,
                            itemStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                        offset: 0,
                                        color: '#818EFF'
                                    }, {
                                        offset: 1,
                                        color: '#2253D6'
                                    }]),
                                }
                            },
                        }
                    ],
                });
            },
            drawPieChart() {
                let that=this;
                this.drawPieCharts = echarts.init(this.$refs.chartPie);
                this.drawPieCharts.setOption({
                    tooltip: {
                    },
                    legend: {
                        bottom:0,
                        data:['合格','不合格'],
                        textStyle:{
                            color:'#5DCFE8',
                        },
                        itemWidth: 10,
                        itemHeight: 10,
                        itemGap: 20,
                    },
                    series: {
                        type: 'pie',
                        radius: ['50%', '70%'],
                        color:'#5DCFE8',
                        hoverOffset:3,
                        label: {
                            normal: {
                                position: 'center'
                            }
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b}: {c} ({d}%)"
                        },
                        data: [{
                            value: that.dateAuditRate.relCount,
                            name: '合格',
                            label: {
                                normal: {
                                    formatter: '{d} %',
                                    lineHeight: 50,
                                    textStyle: {
                                        fontSize: 18,
                                        color:'#5CCEE7'
                                    }
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                        offset: 0,
                                        color: '#66DFF5'
                                    }, {
                                        offset: 1,
                                        color: '#019DA9'
                                    }]),
                                }
                            },
                        }, {
                            value: that.dateAuditRate.unRelCount,
                            name: '不合格',
                            label: {
                                normal: {
                                    formatter: '\n合格率',
                                    lineHeight: 50,
                                    textStyle: {
                                        color: '#5CCEE7',
                                        fontSize: 18
                                    }
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                        offset: 0,
                                        color: '#818EFF'
                                    }, {
                                        offset: 1,
                                        color: '#2253D6'
                                    }]),
                                }
                            },
                        }]
                    }
                });
            },
            drawWordChart() {
                let that=this;
                this.drawWordCharts = echarts.init(this.$refs.chartWord);
                this.drawWordCharts.setOption({
                    tooltip: {},
                    series: [{
                        type: 'wordCloud',
                        gridSize: 20,
                        sizeRange: [12, 50],
                        rotationRange: [0, 0],
                        shape: 'circle',
                        textStyle: {
                            normal: {
                                color: function() {
                                    return 'rgb(' + [
                                        Math.round(Math.random() * 160),
                                        Math.round(Math.random() * 160),
                                        Math.round(Math.random() * 160)
                                    ].join(',') + ')';
                                }
                            },
                            emphasis: {
                                shadowBlur: 10,
                                shadowColor: '#333'
                            }
                        },
                        data: that.commonElement
                    }]
                });
            },
        },
        created:function () {
            this.baseStyles.zoom = this.oldw / 1920;
            this.oldh = 1080 *this.baseStyles.zoom;
            if (this.oldh < 600 || this.oldw < 1280) return;
        },
        mounted: function () {
            this.setThree()
            this.getData();

        }
    }
</script>

<style lang="scss" scoped>
    @import '../../assets/styles/mapStandard';
</style>

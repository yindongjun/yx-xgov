<template>
    <section class="map-standard-dataCollect-outer" :style="{height: ch > oldh ? oldh : ch}">
        <div class="container" :style="{ transform: 'scale(' + baseStyles.zoom + ')'}">
            <!--返回-->
            <div class="map-back" @click="backBase"></div>
            <!--头部模块-->
            <div class="main-header">
                标准分类
            </div>
            <div class="main-body">
                <div class="secrch-input">
                    <input class="secrch-input-inner" type="text" v-model="searchVal"/>
                    <i class="search-icon valign" @click="searchData">
                        <img src="../../assets/images/map/icon/search.png"/>
                    </i>
                </div>
                <div class="main-data">
                    <div class="main-data-inner" ref="dataRelation"></div>
                </div>

                <div class="dialog" v-show="showDialog" ref="popContent">
                    <div class="close-btn" @click="closeEnter"></div>
                    <div class="box-title">
                        <img src="../../assets/images/map/icon/title.png"/>
                        数据标准趋势分析
                        <span class="link"></span>
                    </div>
                    <ul class="dialog-t">
                        <li><span>标准分类名称:</span>{{testName}}</li>
                        <li><span>标准维护个数:</span> 293</li>
                        <li><span>标准分类名称:</span> 213</li>
                    </ul>
                    <ul class="dialog-b">
                        <li>
                            <span>关联字段</span>
                            <span>对应标准维护</span>
                            <span>数据类型</span>
                        </li>
                        <li :key="index" v-for="(item,index) in recentUpdate">
                            <span>{{item.name}}</span>
                            <span>{{item.meta}}</span>
                            <span>{{item.type}}</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    import echarts from "echarts";
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
                searchVal:'',
                chartGraphData:null,
                chartGraphOption:{},
                recentUpdate:[
                    {name:'kugu',type:'mysql',meta:'#65C4FA'},
                    {name:'kugu',type:'mysql',meta:'#65C4FA'},
                    {name:'kugu',type:'mysql',meta:'#65C4FA'},
                    {name:'kugu',type:'mysql',meta:'#65C4FA'},
                ],
                showDialog:false,
                testName:'',
            }
        },
        methods: {
            backBase(){
                this.$router.push('/standardManage')
            },
            searchData(){

            },
            //绘制关系图方法
            drawRelationGraph:  function  ()  {
                let  that  =  this;
                let nodes=[
                    { name:'n1',"category":0,type:0,},
                    {name:'n2',"category":0,type:0,symbolSize:30},
                    {name:'n3',"category":0,type:0,},
                    {name:'n4',"category":0,type:1},
                    {name:'n5',"category":0,type:1},
                    {name:'n6',"category":0,type:1,symbolSize:20},


                    {name:'n7',"category":1,type:0,},
                    {name:'n8',"category":1,type:0,},
                    {name:'n9',"category":1,type:1},
                    {name:'n10',"category":1,type:1,symbolSize:20},

                    {name:'n11',"category":2,type:1},
                    {name:'n12',"category":2,type:1},
                    {name:'n13',"category":2,type:1},
                    {name:'n14',"category":2,type:1,symbolSize:35},
                    {name:'n15',"category":2,type:1},
                    {name:'n16',"category":2,type:1},
                    {name:'n17',"category":2,type:1},
                    {name:'n18',"category":2,type:1,symbolSize:25},
                    {name:'n19',"category":2,type:1},
                    {name:'n20',"category":2,type:1},
                    {name:'n21',"category":2,type:1},
                    {name:'n22',"category":2,type:1},


                    {name:'n23',"category":3,type:1},
                    {name:'n24',"category":3,type:1},
                    {name:'n25',"category":3,type:1,symbolSize:40},
                    {name:'n26',"category":3,type:1},
                    {name:'n27',"category":3,type:1},
                    {name:'n28',"category":3,type:1,symbolSize:20},
                    {name:'n29',"category":3,type:1},
                    {name:'n30',"category":3,type:1},
                    {name:'n31',"category":3,type:1},
                    {name:'n32',"category":3,type:1},

                    {name:'n33',"category":4,type:1},
                    {name:'n34',"category":4,type:1},
                    {name:'n35',"category":4,type:1,symbolSize:28},
                    {name:'n36',"category":4,type:1},
                    {name:'n37',"category":4,type:1},
                    {name:'n38',"category":4,type:1,symbolSize:22},

                    {name:'n39',"category":5,type:1},
                    {name:'n40',"category":5,type:1},
                    {name:'n41',"category":5,type:1,symbolSize:28},
                    {name:'n42',"category":5,type:1},
                    {name:'n43',"category":5,type:1},
                    {name:'n44',"category":5,type:1,symbolSize:40},
                    {name:'n45',"category":5,type:1},
                    {name:'n46',"category":5,type:1,symbolSize:70},
                    {name:'n47',"category":5,type:1},
                    {name:'n48',"category":5,type:1},

                    {name:'n59',"category":6,type:1},
                    {name:'n50',"category":6,type:1},
                    {name:'n51',"category":6,type:1,symbolSize:28},
                    {name:'n52',"category":6,type:1},
                    {name:'n53',"category":6,type:1},
                    {name:'n54',"category":6,type:1,symbolSize:35},
                    {name:'n55',"category":6,type:1},
                    {name:'n56',"category":6,type:1},
                    {name:'n57',"category":6,type:1,symbolSize:60},
                    {name:'n58',"category":6,type:1},
                ];
                let links=[
                    {source: 'n1',target: 'n2'},
                    {source: 'n2',target: 'n3'},
                    {source: 'n3',target: 'n4'},
                    {source: 'n5',target: 'n6'},
                    {source: 'n5',target: 'n2'},

                    {source: 'n7',target: 'n10'},
                    {source: 'n8',target: 'n10'},
                    {source: 'n9',target: 'n10'},

                    {source: 'n11',target: 'n14'},
                    {source: 'n12',target: 'n14'},
                    {source: 'n13',target: 'n14'},
                    {source: 'n15',target: 'n14'},
                    {source: 'n16',target: 'n14'},
                    {source: 'n17',target: 'n18'},
                    {source: 'n19',target: 'n18'},
                    {source: 'n20',target: 'n18'},
                    {source: 'n21',target: 'n18'},
                    {source: 'n22',target: 'n18'},
                    {source: 'n14',target: 'n18'},
                    {source: 'n14',target: 'n18'},
                    {source: 'n14',target: 'n18'},
                    {source: 'n14',target: 'n18'},
                    {source: 'n14',target: 'n18'},
                    {source: 'n14',target: 'n18'},
                    {source: 'n14',target: 'n18'},


                ];
                let categories = [{"name":"标准分类0"},{"name":"标准分类1"},{"name":"标准分类2"},{"name":"标准分类3"},{"name":"标准分类4"},{"name":"标准分类5"},{"name":"标准分类6"}];

                that.chartGraphData = echarts.init(that.$refs.dataRelation);
                that.chartGraphOption = {
                    title: {
                        text: 'Les Miserables',
                        subtext: 'Default layout',
                        top: 'bottom',
                        left: 'right'
                    },
                    tooltip: {},
                    legend: [{
                        // selectedMode: 'single',
                        top: 20,
                        data: categories.map(function (a) {
                            return a.name;
                        }),
                        textStyle:{
                            color:'#5DCFE8',
                        },
                        itemWidth: 10,
                        itemHeight: 10,
                        itemGap: 20,
                    }],
                    animationDuration: 1500,
                    animationEasingUpdate: 'quinticInOut',
                    series : [
                        {
                            name: 'Les Miserables',
                            type: 'graph',
                            layout: 'force',
                            data: nodes,
                            links: links,
                            categories: categories,
                            roam: true,
                            focusNodeAdjacency: true,
                            itemStyle: {
                                normal: {
                                    borderColor: '#fff',
                                    borderWidth: 1,
                                    shadowBlur: 10,
                                    shadowColor: 'rgba(0, 0, 0, 0.3)'
                                }
                            },
                            force: {
                                repulsion: 140
                            },
                            label: {
                                position: 'right',
                                formatter: '{b}'
                            },
                            lineStyle: {
                                color: 'source',
                                curveness: 0.4
                            },
                            emphasis: {
                                lineStyle: {
                                    width: 10
                                }
                            }
                        }
                    ]
                };
                that.chartGraphData.setOption(that.chartGraphOption);
            },
            showModal(){
                let that=this;
                this.chartGraphData.on('click', function (params) {
                    if(params.dataType==='node'){
                        that.showDialog=true;
                        that.testName=params.name;
                        that.$refs.popContent.style.left=(params.event.offsetX+300)+'px';
                    }

                });
            },
            closeEnter(){
                this.showDialog=false;
            }
        },
        created:function () {
            this.baseStyles.zoom = this.oldw / 1920;
            this.oldh = 1080 *this.baseStyles.zoom;
            if (this.oldh < 600 || this.oldw < 1280) return;
        },
        mounted: function () {
            this.drawRelationGraph()
            this.showModal()
        }
    }
</script>

<style lang="scss" scoped>
    @import '../../assets/styles/mapStandardCollcet';
</style>

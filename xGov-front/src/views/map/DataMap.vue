<template>
    <section class="data-map-outer p-container" :style="{height: ch > oldh ? oldh : ch}">
        <div class="container" :style="{ transform: 'scale(' + baseStyles.zoom + ')'}">
            <div class="container-inner">
                <!--头部模块-->
                <div class="main-title">
                    <!-- <span class="title">xGov项目数据地图</span> -->
                    <span class="title">数据地图</span>
                </div>
                <!--顶部退出-->
                <div class="menu-wrap">
                    <div class="menu-hd" @click="quitMap"></div>
                </div>
                <div class="left-bg">
                </div>
                <div class="right-bg">
                </div>
                <!--内容模块-->
                <div class="wrapper">
                    <!--数据列表-->
                    <div class="index-menu">
                        <ul>
                            <li>
                                <p class="index-menu-title">累计数据量</p>
                                <div class="info">
                                    <span class="value" >{{animatedNumber0}}</span>
                                    <span class="unit">GB</span>
                                </div>
                            </li>
                            <li>
                                <p class="index-menu-title">当日数据增量</p>
                                <div class="info">
                                    <span class="value" >{{animatedNumber11}}</span>
                                    <span class="unit">KB</span>
                                </div>
                            </li>
                            <li>
                                <p class="index-menu-title">标准化数据量</p>
                                <div class="info">
                                    <span class="value" >{{animatedNumber22}}</span>
                                    <span class="unit">KB</span>
                                </div>
                            </li>
                            <li>
                                <p class="index-menu-title">数据合规率</p>
                                <div class="info">
                                    <span class="value" >{{animatedNumber33}}</span>
                                    <span class="unit">KB</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <!--中间地图-->
                    <div class="contentMap">
                        <i class="centerBg"></i>
                        <div class="modelOne">
                            <ul>
                                <li @click="showMore($event,'modelOne')"></li>
                                <li @click="showMore($event,'modelOne')"></li>
                                <li @click="showMore($event,'modelOne')"></li>
                                <li @click="showMore($event,'modelOne')"></li>
                            </ul>
                            <div class="modelOne-title">
                                <p class="cubeTitle">数据共享</p>
                            </div>
                        </div>
                        <div class="modelTwo">
                            <ul>
                                <li @click="showMore($event,'modelTwo')"></li>
                                <li @click="showMore($event,'modelTwo')"></li>
                                <li @click="showMore($event,'modelTwo')"></li>
                                <li @click="showMore($event,'modelTwo')"></li>
                                <li @click="showMore($event,'modelTwo')"></li>
                                <li @click="showMore($event,'modelTwo')"></li>
                            </ul>
                            <div class="modelTwo-title">
                                <p class="cubeTitle">数据挖掘</p>
                            </div>
                        </div>
                        <div class="modelThree">
                            <ul>
                                <li @click="showMore($event,'modelThree')"></li>
                                <li @click="showMore($event,'modelThree')"></li>
                                <li @click="showMore($event,'modelThree')"></li>
                                <li @click="showMore($event,'modelThree')"></li>
                                <li @click="showMore($event,'modelThree')"></li>
                                <li @click="showMore($event,'modelThree')"></li>
                            </ul>
                            <div class="modelThree-title">
                                <p class="cubeTitle">标准管理区</p>
                            </div>
                            <div class="modelThree-title-one title-common">
                                <p class="cubeTitle">标准分类</p>
                            </div>
                            <div class="modelThree-title-two title-common">
                                <p class="cubeTitle">业务代码</p>
                            </div>
                            <div class="modelThree-title-three title-common">
                                <p class="cubeTitle">文档管理</p>
                            </div>
                        </div>
                        <div class="modelFour">
                            <ul>
                                <li @click="showMore($event,'modelFour')"></li>
                                <li @click="showMore($event,'modelFour')"></li>
                                <li @click="showMore($event,'modelFour')"></li>
                                <li @click="showMore($event,'modelFour')"></li>
                                <li @click="showMore($event,'modelFour')"></li>
                                <li @click="showMore($event,'modelFour')"></li>
                            </ul>
                            <div class="modelFour-title">
                                <p class="cubeTitle">数据汇总区</p>
                            </div>
                        </div>
                        <div class="modelFive">
                            <ul>
                                <li @click="showMore($event,'modelFive')"></li>
                                <li @click="showMore($event,'modelFive')"></li>
                                <li @click="showMore($event,'modelFive')"></li>
                                <li @click="showMore($event,'modelFive')"></li>
                            </ul>
                            <div class="modelFive-title">
                                <p class="cubeTitle">标准数据区</p>
                            </div>
                        </div>
                        <div class="modelSix">
                            <ul>
                                <li @click="showMore($event,'modelSix')"></li>
                                <li @click="showMore($event,'modelSix')"></li>
                                <li @click="showMore($event,'modelSix')"></li>
                                <li @click="showMore($event,'modelSix')"></li>
                                <li @click="showMore($event,'modelSix')"></li>
                                <li @click="showMore($event,'modelSix')"></li>
                            </ul>
                            <div class="modelSix-title">
                                <p class="cubeTitle">脏数据区</p>
                            </div>
                        </div>
                        <div class="modelSeven">
                            <ul>
                                <li @click="enterMore($event,item)" :key="item.id" v-for="item in AccessArea"><p class="seven-title">{{item.datasourceName}}</p></li>
                            </ul>
                            <div class="modelSeven-title">
                                <p class="cubeTitle">数据接入区</p>
                            </div>
                        </div>
                        <div class="line" id="line"></div>
                    </div>
                </div>
            </div>
            <div class="popContent" ref="popContent" v-show="showModal">
                <img src="../../assets/images/map/Bulletbox/box.png" class="left-bk"/>
                <div class="popContent-inner">
                    <div class="close-btn" @click="closeModal"></div>
                    <div v-if="currType==='modelThree'">
                        <p>文档总数：{{standardArea.codeSetNum}}</p>
                        <p>业务代码总数：{{standardArea.dataSetNum}}</p>
                        <p>标准分类总数：{{standardArea.documentNum}}</p>
                    </div>
                    <div v-else-if="currType==='modelFour'">
                        <p>当日新增数据量：{{summaryArea.increment}}</p>
                        <p>数据总量：{{summaryArea.dataSize}}</p>
                        <p>数据源总数：{{summaryArea.sourceNum}}</p>
                    </div>
                    <div v-else-if="currType==='modelSix'">
                        <p>标准数据行数：{{dataArea.CorrectData}}</p>
                        <p>问题数据行数：{{dataArea.DirtyRead}}</p>
                    </div>
                    <div v-else>
                        <p>当日新增数据：0.75GB</p>
                        <p>待处理问题数据：12.5KB</p>
                        <p>累计数据存量：12.95GB</p>
                    </div>
                    <span class="showMore" @click="getMorePage">More ></span>
                </div>
            </div>

            <div class="enterContent" ref="enterContent" v-show="enterModal">
                <img src="../../assets/images/map/Bulletbox/box1.png" class="left-bk"/>
                <div class="enterContent-inner">
                    <div class="close-btn" @click="closeEnter"></div>
                    <p>数据库类型：{{currAccess.databaseType}}</p>
                    <p>数据源名称：{{currAccess.datasourceName}}</p>
                    <p>数据库名称：{{currAccess.dbname}}</p>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    export default {
        name: "",
        components: {},
        data() {
            return {
                clientHeight: '600px',
                tweenedNumber:0,
                animatedNumber1:0,
                animatedNumber2:0,
                animatedNumber3:0,
                oldw: document.body.clientWidth,
                oldh: document.body.clientHeight,
                ch: null,
                currType:'',
                baseStyles: {
                    zoom:1
                },
                showModal:false,
                enterModal:false,

                AccessArea:[
                    {
                        id: 1,
                        datasourceName: 'mysql'
                    },
                    {
                        id: 2,
                        datasourceName: 'oracle'
                    },
                    {
                        id: 3,
                        datasourceName: 'hive'
                    },
                    {
                        id: 4,
                        datasourceName: 'impala'
                    },
                    {
                        id: 5,
                        datasourceName: 'terdata'
                    },
                    {
                        id: 6,
                        datasourceName: 'sqlserver'
                    }
                ],
                currAccess:{},
                dataArea:{
                    CorrectData:'',
                    DirtyRead:''
                },
                standardArea:{
                    codeSetNum:'',
                    dataSetNum:'',
                    documentNum:'',
                },
                summaryArea:{
                    dataSize:'',
                    increment:'',
                    sourceNum:'',
                },
            }
        },
        computed: {
            animatedNumber0: function() {
                return this.tweenedNumber.toFixed(1);
            },
            animatedNumber11: function() {
                return this.animatedNumber1.toFixed(1);
            },
            animatedNumber22: function() {
                return this.animatedNumber2.toFixed(1);
            },
            animatedNumber33: function() {
                return this.animatedNumber3.toFixed(1);
            }
        },
        methods: {
            getmapData(){
                this.$urlApi.mapData.mapBaseData().then((res)=>{
                    this.dataArea=res.data.dataArea;
                    this.standardArea=res.data.standardArea;
                    this.summaryArea=res.data.summaryArea;
                    this.AccessArea=res.data.AccessArea;
                })
            },
            setMenuData(){   //设置菜单
                let newValue=2.26;
                let newValue1=859.6;
                let newValue2=205.3;
                let newValue3=450.56;
                TweenLite.to(this.$data, 1, {
                    ease: Power2.easeOut,
                    tweenedNumber: newValue
                });
                TweenLite.to(this.$data, 1, {
                    ease: Power2.easeOut,
                    animatedNumber1: newValue1
                });
                TweenLite.to(this.$data, 1, {
                    ease: Power2.easeOut,
                    animatedNumber2: newValue2
                });
                TweenLite.to(this.$data, 1, {
                    ease: Power2.easeOut,
                    animatedNumber3: newValue3
                });
            },
            setOtherPath() {
                // 右边图层 到cube 绘制线条的路径
                let cube0 = $('.modelSeven .modelSeven-title');
                let cube0Obj = {
                    x: cube0[0].offsetLeft + cube0.width() / 2,
                    y: cube0[0].offsetTop + cube0.height() / 2
                };
                let linArr0 = [
                    [cube0Obj.x - 75, cube0Obj.y-85],
                    [cube0Obj.x - 155, cube0Obj.y-130],
                ];
                this.setPath(linArr0);
                let cube1 = $('.modelFour .modelFour-title');
                let cube1Obj = {
                    x: cube1[0].offsetLeft + cube1.width() / 2,
                    y: cube1[0].offsetTop + cube1.height() / 2
                };
                let linArr1 = [
                    [cube1Obj.x - 30, cube1Obj.y+10],
                    [cube1Obj.x - 105, cube1Obj.y+55],
                ];
                this.setPath(linArr1);

                let cube2 = $('.modelThree .modelThree-title-three');
                let cube2Obj = {
                    x: cube2[0].offsetLeft + cube2.width() / 2,
                    y: cube2[0].offsetTop + cube2.height() / 2
                };
                let linArr2c1 = [
                    [cube2Obj.x +95, cube2Obj.y+17],
                    [cube2Obj.x + 153, cube2Obj.y+52],
                ];
                this.setPath(linArr2c1);

                let linArr2c2 = [
                    [cube2Obj.x +95, cube2Obj.y+17],
                    [cube2Obj.x + 125, cube2Obj.y+35],
                    [cube2Obj.x+20, cube2Obj.y+95],
                    [cube2Obj.x+94, cube2Obj.y+140],
                    [cube2Obj.x+25, cube2Obj.y+178],
                ];
                this.setPath(linArr2c2);

                let cube3 = $('.modelSix .modelSix-title');
                let cube3Obj = {
                    x: cube3[0].offsetLeft + cube3.width() / 2,
                    y: cube3[0].offsetTop + cube3.height() / 2
                };
                let linArr3 = [
                    [cube3Obj.x - 10, cube3Obj.y+20],
                    [cube3Obj.x - 110, cube3Obj.y+80],
                ];
                this.setPath(linArr3);

                let cube4 = $('.modelTwo .modelTwo-title');
                let cube4Obj = {
                    x: cube4[0].offsetLeft + cube4.width() / 2,
                    y: cube4[0].offsetTop + cube4.height() / 2
                };
                let linArr4c1 = [
                    [cube4Obj.x + 50, cube4Obj.y+82],
                    [cube4Obj.x -10 , cube4Obj.y+42],
                ];
                this.setPath(linArr4c1);

                let linArr4c2 = [
                    [cube4Obj.x + 50, cube4Obj.y+82],
                    [cube4Obj.x +20 , cube4Obj.y+62],
                    [cube4Obj.x -195 , cube4Obj.y+175],
                    [cube4Obj.x -225 , cube4Obj.y+152],
                ];
                this.setPath(linArr4c2);


            },
            setPath(arr, movePath, ballTarget, lineTarget, ballTotal, timer) {   //设置路径
                movePath = movePath ? movePath : [];
                if (movePath.length == 0) {
                    arr.forEach(function(item) {
                        // 处理为运动球的路径
                        movePath.push({
                            x: item[0],
                            y: item[1]
                        });
                    });
                }
                ballTarget = ballTarget ? ballTarget : $('.contentMap');
                lineTarget = lineTarget ? lineTarget : 'line';
                ballTotal = ballTotal ? ballTotal : 2;
                timer = timer ? timer : 1;
                let draw = SVG(lineTarget); // svg绘制
                draw.polyline(arr).attr({
                    fill: 'none',
                    stroke: 'rgba(167,223,255,1)',
                    'stroke-width': 2
                });
                // 默认连线多少个球球运动
                for (var i = 0; i < ballTotal; i++) {
                    this.$utils.moveBall(movePath, ballTarget, Math.random() * 1 + timer);
                }
            },
            quitMap(){
                this.$router.push('/property/default/maindata')
            },
            showMore(event,type){
                this.enterModal=false;
                this.showModal=true;
                this.currType=type;
                if(type==='modelOne'){
                    this.$refs.popContent.style.left='480px'; this.$refs.popContent.style.top='320px';
                }else if(type==='modelTwo'){
                    this.$refs.popContent.style.left='695px'; this.$refs.popContent.style.top='215px';
                }else if(type==='modelThree'){
                    this.$refs.popContent.style.left='880px'; this.$refs.popContent.style.top='110px';
                }else if(type==='modelFour'){
                    this.$refs.popContent.style.left='1166px'; this.$refs.popContent.style.top='45px';
                }else if(type==='modelFive'){
                    this.$refs.popContent.style.left='890px'; this.$refs.popContent.style.top='460px';
                }else if(type==='modelSix'){
                    this.$refs.popContent.style.left='1175px'; this.$refs.popContent.style.top='315px';
                }
            },
            getMorePage(){
                this.$router.push('/standardManage')
            },
            closeModal(){
                this.showModal=false;
            },
            enterMore(event,item){
                this.showModal=false;
                this.enterModal=true;
                this.currAccess=item;
                let x=event.x,y=event.y;
                this.$refs.enterContent.style.left=(x/this.baseStyles.zoom+5)+'px'; this.$refs.enterContent.style.top=(y/this.baseStyles.zoom-220)+'px';
            },
            closeEnter(){
                this.enterModal=false;
            }

        },
        mounted: function () {
            let that=this;
            that.baseStyles.zoom = that.oldw / 1920;
            that.oldh = 1080 *that.baseStyles.zoom;
            if (that.oldh < 600 || that.oldw < 1280) return;
            that.setOtherPath();
            that.setMenuData();//设置菜单
            that.getmapData();
        },
    }
</script>

<style lang="scss">
    @import '../../assets/styles/mapBase';

</style>

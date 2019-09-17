<template>
    <el-dialog :close-on-click-modal='false'
        :title="'编辑 ' + isEditCombin.name + ' 关联关系'"
        :visible.sync="SetCombinVisible"
        :before-close="closeDialog"
        class="set-combin"
        width="860px">
        <el-row class="set-combin-inner">
            <el-col :span="10" class="set-combin-l">
                <el-row style="line-height: 32px">
                    <el-col :span="7">数据源： </el-col>
                    <el-col :span="17">
                        <el-select
                            v-model="dataOrigin"
                            placeholder="请选择数据源"
                            style="width: 100%" size="small"
                            filterable
                            :disabled="!dataOriginList.length"
                            @change="changeDataOriginBase"
                        >
                            <el-option
                                v-for="item in dataOriginList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </el-col>
                </el-row>
                <el-row class="mt10">
                    <el-input placeholder="请输入表名查询" size="small" @keyup.enter.native="searchTable(searchTablebyValue)" v-model="tableName" class="input-with-select">
                        <el-select @change="changeHandle" v-model="searchTablebyValue" slot="prepend" style="width: 90px" placeholder="请选择">
                            <el-option label="表名" value="表名"></el-option>
                            <el-option label="字段名" value="字段名"></el-option>
                        </el-select>
                        <el-button size="small" slot="append" icon="el-icon-search" @click="searchTable(searchTablebyValue)"></el-button>
                    </el-input>
                </el-row>
                <el-row class="set-combin-l-b mt10">
                    <vue-scroll v-loading='loading'>
                        <el-tree
                            v-if="changeSelect && searchTablebyValue === '表名'"
                            :render-content="renderContent"
                            :props="defaultProps"
                            highlight-current
                            :expand-on-click-node='false'
                            :default-expanded-keys = 'expandKey' 
                            :default-checked-keys="expandCheck"
                            empty-text="暂无数据"
                            show-checkbox
                            @check="checkBox"
                            :load="loadNodeTree"
                            ref="tree"
                            node-key="id"
                            lazy
                        >
                        </el-tree>
                        <el-tree
                            v-if="isField"
                            :render-content="renderContent"
                            :data='fieldData'
                            :props="defaultProps1"
                            highlight-current
                            empty-text="暂无数据"
                            show-checkbox
                            @check="checkBox"
                            ref="tree"
                            node-key="id"
                        >
                        </el-tree>
                    </vue-scroll>
                </el-row>
            </el-col>
            <el-col :span="14" class="set-combin-r">
                <vue-scroll>
                    <el-table
                        :data="tableData"
                        style="width: 100%">
                        <template slot="empty">
                            <div class="show-empty">
                            暂无关联数据
                            </div>
                        </template>
                        <el-table-column
                            prop="sourceName"
                            :show-overflow-tooltip="true"
                            label="对应数据源">
                        </el-table-column>
                        <el-table-column
                            prop="tablename"
                            :show-overflow-tooltip="true"
                            label="表名">
                        </el-table-column>
                        <el-table-column
                            prop="fildname"
                            :show-overflow-tooltip="true"
                            label="字段名">
                        </el-table-column>
                        <el-table-column
                            label="操作"
                            width="80">
                            <template slot-scope="scope">
                                <i class="table-icon-menu" @click="deleteRow(scope.$index,scope)"><svg-icon iconClass="删除"></svg-icon></i>
                            </template>
                        </el-table-column>
                    </el-table>
                </vue-scroll>
                <div class="delete-tip"><i class="el-icon-warning"></i> 删除关联字段后，请保存使配置生效！</div>
            </el-col>
        </el-row>
        <span slot="footer" class="dialog-footer">
            <el-button type="info" size="small"  @click="resetForm">取 消</el-button>
            <el-button type="danger" size="small" :disabled="!originTableData.length && !tableData.length" @click="submitForm" :loading="buttonLoading">保 存</el-button>
        </span>
    </el-dialog>
</template>

<script type="text/jsx">
    export default {
        name: "",
        components: {},
        props:['SetCombinVisible','isEditCombin'],
        data() {
            return {
                expandKey:[],
                expandCheck: [],
                dataOriginList:[],
                dataOrigin:'',
                buttonLoading:false,
                tableData:[],
                treeData:[],
                defaultProps: {
                    children: 'children',
                    label: 'typename',
                    isLeaf: 'leaf',
                    id:'id',
                },
                defaultProps1: {
                    children: 'cols',
                    label: 'typename',
                    id:'id',
                },
                tmpResolvephoto:'',
                tableName:'',
                changeSelect:false,
                currBaseId:'',
                crrrTableName:"",
                originTableData:[],
                allCheckedData: {},
                searchTablebyValue: '表名',
                fieldData: [],
                isField: false,
                loading: false
            }
        },
        methods: {
            searchTable(searchTablebyValue){
                if (searchTablebyValue === '表名') {
                    this.changeSelect=false;
                    setTimeout(() => {
                        this.changeSelect=true;
                    }, 500);
                } else if (searchTablebyValue === '字段名') {
                    let param={id:this.dataOrigin, columnName:this.tableName};
                    this.loading = true;
                    this.$urlApi.dataStandard.searchByColumn(param).then((res)=>{
                        this.loading = false;
                        this.$nextTick(() => {
                            const a = [];
                            res.data.forEach(c => {
                                c.bind && (c.disabled = true);
                                c.cols && c.cols.forEach(k => {
                                    if (k.bind) {
                                        k.disabled = true;
                                        a.push(k.id)
                                    }
                                })
                            })
                            this.fieldData = res.data;
                            setTimeout(() => {
                                this.$refs.tree.setCheckedKeys([...a]);
                            }, 500)
                        })
                    }).catch(e => {
                        this.loading = false;
                    })
                }
            },
            getData(){
                this.$urlApi.dataStandard.getRelationShip({dataElementId:this.isEditCombin.id}).then((res)=>{
                    this.originTableData=[...res.data]
                    this.tableData=res.data
                })
            },
            loadNodeTree(node, resolve) {
                let param={};
                if(this.dataOrigin===''){
                    return
                }
                if(node.level===0){
                    param={id:this.currBaseId,pid:-2,searchName:this.tableName};
                }else{
                    param={
                        pid: node.data.pid,
                        id: node.data.id,
                        name: node.data.name
                    };
                }
                this.loading = true;
                this.$urlApi.dataStandard.getDataTreeById(param).then((res)=>{
                    this.loading = false;
                    if(res.code==='200'){
                        res.data.forEach(et => {
                            et.children=[];
                            if(et.isParent==='true'){
                                et.leaf=false;
                            }else{
                                et.leaf=true;
                            }
                            if (et.bind) {
                                 node.level!==0 && (et.disabled=true);
                            }
                        });
                        resolve(res.data);
                        if(node.level ===1){
                            node.childNodes.forEach(et => {
                                if(et.data.bind){
                                    et.checked=true
                                }else{
                                   et.checked=false 
                                }
                            })
                            if (node.childNodes.every(c => c.data.bind)) {
                                node.checked = true;
                            }
                         }else{
                            node.childNodes.forEach(et => {
                                if(et.data.bind){
                                    et.checked=true
                                }
                             })
                         }
                        if(node.checked){
                            node.expanded=true
                            node.childNodes.forEach(et => {
                                et.checked=true
                            })
                        }
                        this.tmpResolvephoto = resolve;
                    }else{
                        // this.$message({
                        //     "message":res.message,
                        //     'type':'warning'
                        // })
                        this.$toast('warning', res.message)
                    }
                }).catch(e => {
                    this.loading = false;
                })
            },
            getAllDataSource(){
                this.$urlApi.dataStandard.getDataTreeById({pid:-1}).then((res)=>{
                    this.dataOriginList=res.data
                })
            },
            changeDataOriginBase(cb){
                if (this.searchTablebyValue === '表名') {
                    this.isField=false;
                    this.currBaseId=cb;
                    this.changeSelect=false;
                    // !this.allCheckedData[cb] && (this.allCheckedData[cb] = []);
                    setTimeout(() => {
                        this.changeSelect=true;
                        // console.log(this.oldId, this.currentId)
                        // this.oldId === this.currentId && this.$nextTick(() => {
                        //     this.$refs.tree.setCheckedKeys(this.allCheckedData[cb].map(c => (c.id)));
                        // })
                    }, 500);
                } else if (this.searchTablebyValue === '字段名') {
                    this.fieldData = [];
                    this.isField=false;
                    this.searchTable(this.searchTablebyValue)
                    setTimeout(() => {
                        this.isField=true;
                    }, 500);
                }
            },
            changeHandle(val) {
                if (!this.dataOrigin) {
                    // this.$message.error('请先选择数据源！');
                    this.$toast('error', '请先选择数据源！')
                    return
                }
                this.fieldData = [];
                this.changeDataOriginBase(this.dataOrigin)
            },
            handleNodeClick(data,node){
                let check=true;
                if(!data.bind && node.level===2){
                    this.tableData.forEach((item)=>{
                        if(item.fildname===data.name){
                            if(item.tablename===data.pname){
                                if(item.sourceName===data.sourceName){
                                    check=false;
                                }
                            }
                        }
                    });
                    if(check){
                        this.tableData.push({
                            sourceId:data.sourceId,
                            sourceName:data.sourceName,
                            tablename:data.pname,
                            fildname:data.name,
                        })
                    }
                }
            },
            checkBox(data) {
                const aaaa = () => {
                    this.$nextTick(() => {
                        this.allCheckedData[this.currBaseId + 'check'] = this.$refs.tree.getCheckedNodes();
                        let tableData = [];
                        const allCheckedData = Object.assign({}, this.allCheckedData)
                        for (const key in allCheckedData) {
                            const itemArr = allCheckedData[key].filter(v => (v.sourceId && v.sourceName && !v.disabled)).map(c => ({
                                sourceId:c.sourceId,
                                sourceName:c.sourceName,
                                tablename:c.pname,
                                fildname:c.name,
                            }))
                            tableData = [...tableData, ...itemArr]
                        }
                        this.tableData = this.originTableData.concat(tableData)
                    })
                }
                if (!data.leaf && this.searchTablebyValue === '表名') {
                    let Node=this.$refs.tree.getNode(data);
                    Node.loadData();
                }
                setTimeout(() => aaaa(), 1500)
            },
            deleteRow(num,scope){
                const a = this.allCheckedData;
                this.$urlApi.dataStandard.checkDelete({elementId:this.isEditCombin.id,sourceId:scope.row.sourceId,tableName:scope.row.tablename,column:scope.row.fildname}).then((res)=>{
                    this.tableData.splice(num,1);
                    const b = this.allCheckedData[scope.row.sourceId + 'check'];
                    const index = b.findIndex(c => c.sourceName === scope.row.sourceName && c.pname === scope.row.tablename && c.name === scope.row.fildname)
                    if (index !== -1) {
                        this.allCheckedData[scope.row.sourceId + 'check'].splice(index, 1)
                    }
                    this.$nextTick(() => {
                        this.$refs.tree.setCheckedKeys([]);
                        this.$refs.tree.setCheckedKeys(this.allCheckedData[scope.row.sourceId + 'check'].map(c => (c.id)));
                    })
                })
            },
            renderContent(h, {node, data, store}) {
                let nodeItem =              //第一级
                    <span class="nodeItem">
                        <span>
                            <span class="node-label" title={ node.label }>{ node.label }</span>
                        </span>
                    </span>;
                let nodeItemContent=        //第二级
                    <span class="nodeItem">
                        <span>
                            <span class="node-label" title={ node.label }>{ node.label }</span>
                            <span class="nodeOptDetail" on-click={ () => this.removeRelation(data, node, h) }>
                                <el-tooltip class="item" effect="dark" content="解除绑定" placement="top">
                                    <el-button type="text" size="mini"><svg-icon iconClass="分享"></svg-icon></el-button>
                                </el-tooltip>
                            </span>
                        </span>
                    </span>;
                if(node.level===1){
                    return nodeItem;
                }else{
                    if(data.bind){
                        return nodeItemContent;
                    }else{
                        return nodeItem;
                    }
                }
            },
            submitForm(){
                this.buttonLoading =true;
                this.$urlApi.dataStandard.saveRelation({dataElementId:this.isEditCombin.id,relationJson:JSON.stringify(this.tableData)}).then((res)=>{
                    this.$emit("SetCombinBack",0);
                    this.buttonLoading = false;
                    this.tableData=[];
                    this.changeSelect=false;
                    this.dataOrigin='';
                    this.tableName='';
                    this.allCheckedData = {};
                    this.searchTablebyValue = '表名';
                    this.fieldData = [];
                }).catch(e => {
                    this.buttonLoading = false;
                })
            },
            removeRelation(data, node, h){
                let grandnode = this.$refs.tree.getNode(data.pid);
                let param={
                    dataSoureId:data.sourceId,
                    tableName:data.pname,
                    column:data.name
                };
                this.loading = true;
                this.$urlApi.dataStandard.checkDelete({elementId:this.isEditCombin.id,sourceId:data.sourceId,tableName:data.pname,column:data.name}).then((res)=>{
                    this.$urlApi.dataStandard.unbindRelation(param).then((res)=>{
                        if (this.searchTablebyValue === '表名') {
                            this.loadNodeTree(grandnode,this.tmpResolvephoto);
                        } else if (this.searchTablebyValue === '字段名') {
                            this.searchTable(this.searchTablebyValue)
                        }
                        const index = this.tableData.findIndex(c => {
                            return (c.sourceName === data.sourceName && c.tablename === data.pname && c.fildname === data.name)
                        });
                        const index1 = this.originTableData.findIndex(c => {
                            return (c.sourceName === data.sourceName && c.tablename === data.pname && c.fildname === data.name)
                        });
                        if (index1 != -1) {
                            this.originTableData.splice(index1, 1);
                        }
                        if (index != -1) {
                            this.tableData.splice(index, 1);
                        }
                        this.loading = false;
                        // this.$message({
                        //     message:'解除绑定成功',
                        //     type:'success'
                        // })
                        this.$toast('success', '解除绑定成功')
                    }).catch(e => {
                        this.loading = false;
                    })
                })
            },
            resetForm() {
                this.$emit("SetCombinBack",0);
                this.tableData=[];
                this.changeSelect=false;
                this.dataOrigin='';
                this.tableName='';
                this.allCheckedData = {};
                this.oldId = this.currentId;
                this.searchTablebyValue = '表名';
                this.fieldData = [];
            },
            closeDialog(){
                this.tableData=[];
                this.changeSelect=false;
                this.dataOrigin='';
                this.tableName='';
                this.$emit("SetCombinBack",0);
                this.allCheckedData = {};
                this.oldId = this.currentId;
                this.searchTablebyValue = '表名';
                this.fieldData = [];
            }
        },
        mounted: function () {

        },
        watch:{
            isEditCombin:{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    this.getAllDataSource();
                    this.getData()
                },
                deep:true
            }
        }
    }
</script>

<style lang="scss">
    .set-combin{
        .el-dialog__body{
            padding: 13px 20px;
            .set-combin-inner{
                height: 440px;
                .set-combin-l{
                    height: 100%;
                    border-right: 1px solid #eee;
                    padding: 10px;
                    .set-combin-l-b{
                        height:-moz-calc(100% - 88px);
                        height:-webkit-calc(100% - 88px);
                        height: calc(100% - 88px);
                        .nodeItem {
                            font-size: 12px;
                            width: 100%;
                            position: relative;
                            .node-label {
                                display: inline-block;
                                width: 160px;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                                .node-icon{
                                    margin-right: 5px;
                                    font-size: 14px;
                                }
                            }
                            .node-label:hover {
                                display: inline-block;
                            }
                            .nodeOpt {
                                display: none!important;
                                float: right;
                                position: relative;
                                top: -2px;
                                font-size: 14px;
                                font-weight: bold;
                            }
                            .nodeOptDetail {
                                display: none;
                                position: absolute;
                                right: 0;
                                top: -4px;
                                height: 26px;
                                line-height: 26px;
                                font-size: 14px;
                                background:rgba(51,55,67,.7);
                                border-radius: 2px;
                                cursor: default;
                                padding: 0 8px;
                                .el-button{
                                    color: #ffffff;
                                }
                            }
                        }
                        .nodeItem:hover .nodeOptDetail {
                            display: inline-block!important;
                            text-align: right;
                        }
                    }
                }
                .set-combin-r{
                    height: 100%;
                    padding: 10px;
                    padding-bottom: 20px;
                    .delete-tip {
                        width: 100%;
                        text-align: center;
                        color: #f78989;
                        font-size: 13px;
                        padding: 10px 0;
                    }
                    .el-table__empty-block {
                        align-items: baseline;
                        .show-empty {
                            margin-top: 20px;
                        }
                    }
                }
            }
        }

    }
</style>

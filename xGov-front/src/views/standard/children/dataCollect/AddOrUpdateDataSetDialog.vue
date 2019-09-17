<template>
    <section class="add-or-update-data-set-dialog-section">
        <el-dialog
            title=""
            :visible.sync="addOrUpdateDataSetDialogVisible"
            @closed="closeDialog"
            @open="openDialog"
            :before-close="closeDialog"
            :lock-scroll="false"
            width="760px"
            :close-on-click-modal="false"
            >
            <span slot="title" v-if="isEditMeta.id === ''">新建标准分类</span>
            <span slot="title" v-if="!(isEditMeta.id === '')&& !isEditMeta.isHistory">编辑标准分类</span>
            <span slot="title" v-if="isEditMeta.isHistory">标准分类详情</span>
            <el-form :model="addOrUpdateDataSetDialogForm"  :rules="rules" ref="addOrUpdateDataSetDialogForm" class="demo-form-inline" label-width="110px">
                <div class="content">
                    <el-col :span="24">
                        <el-form-item
                            label="标准分类名称："
                            prop="dataSetName"
                            >
                            <el-input
                                v-if="!isEditMeta.isHistory"
                                v-model="addOrUpdateDataSetDialogForm.dataSetName"
                                maxlength="20"
                                placeholder="最大长度为20个字符"
                                size="small"
                                style="width: 100%"
                                >
                            </el-input>
                            <label v-else style="font-size:12px;color:#606266;">{{addOrUpdateDataSetDialogForm.dataSetName}}</label>
                        </el-form-item>
                    </el-col>
                    <el-col v-if="!isEditMeta.isHistory" :span="8" class="pt10">
                        <div class="tree-block">
                            <div>待选标准维护</div>
                            <div>
                                <span class="type_name">数据源:</span>
                                <el-select
                                    v-model="addOrUpdateDataSetDialogForm.dataOrigin"
                                    placeholder="请选择"
                                    size="mini"
                                    :disabled="!dataOriginList.length || isEditMeta.isHistory"
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
                            </div>
                            <div class="mt5 mb5">
                                <span class="type_name">数据表:</span>
                                <el-select
                                    v-model="addOrUpdateDataSetDialogForm.dataTable"
                                    :placeholder="!dataTableList.length || isEditMeta.isHistory ? '' : '请选择'"
                                    size="mini"
                                    :disabled="!dataTableList.length || isEditMeta.isHistory"
                                    @change="changeDataOriginTable"
                                >
                                    <el-option
                                        v-for="item in dataTableList"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.name"
                                    >
                                    </el-option>
                                </el-select>
                            </div>
                            <div class="mt5 mb5">
                                <span class="type_name">数据类型:</span>
                                <el-select
                                    v-model="addOrUpdateDataSetDialogForm.dataType"
                                    placeholder="请选择"
                                    size="mini"
                                    :disabled="!dataTypeList.length || isEditMeta.isHistory"
                                    @change="changeDataType"
                                >
                                    <el-option
                                        v-for="item in dataTypeList"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </div>
                            <div class="mt5">
                                <el-input :placeholder="isEditMeta.isHistory ? '' : '输入标准维护名称'" @keyup.enter.native="searchMeta" size="mini" v-model="addOrUpdateDataSetDialogForm.dataElementName" style="width: 100%" :disabled="isEditMeta.isHistory">
                                    <el-button size="mini" slot="append" @click="searchMeta" icon="el-icon-search" :disabled="isEditMeta.isHistory"></el-button>
                                </el-input>
                            </div>
                            <div class="dataOriginList ac" v-loading="loading">
                                <vue-scroll>
                                    <el-tag :type="showCkeck(item)" size="small" :key="i" v-for="(item, i) in listAction"><span @click="addMeta(item,isEditMeta.isHistory)">{{item.dataElementName}}</span></el-tag>
                                    <div style="color:#909399;font-size: 13px;margin-top: 20px;" v-if="!listAction.length">暂无数据，请在<span class="redText">数据标准--标准维护</span>列表添加标准维护</div>
                                </vue-scroll>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="isEditMeta.isHistory ? 24 : 16" class="pt20">
                        <div class="tree-content">
                            <div v-if="!isEditMeta.isHistory">已选标准维护</div>
                            <div class="tree-content-table">
                                <vue-scroll>
                                    <el-table
                                        :data="tableData"
                                        style="width: 100%;"
                                    >
                                    <template slot="empty">
                                        <div class="show-empty">暂未选择标准维护，请选择右侧已创建标准维护</div>
                                    </template>
                                        <el-table-column
                                            prop="dataElementName"
                                            label="标准维护名称"
                                            show-overflow-tooltip
                                        >
                                        </el-table-column>
                                        <el-table-column
                                            prop="dataElementCode"
                                            label="CODE"
                                            show-overflow-tooltip
                                        >
                                        </el-table-column>
                                        <el-table-column
                                            prop="dataType"
                                            label="数据类型"
                                            show-overflow-tooltip
                                        >
                                        </el-table-column>
                                        <el-table-column
                                            prop="codesetName"
                                            label="值域"
                                            show-overflow-tooltip
                                        >
                                        </el-table-column>
                                        <el-table-column
                                            label="操作"
                                            width="70"
                                            v-if="!isEditMeta.isHistory">
                                            <template slot-scope="scope">
                                                <i class="table-icon-menu" @click="deleteRow(scope.$index)"><svg-icon iconClass="删除"></svg-icon></i>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </vue-scroll>
                            </div>
                        </div>
                    </el-col>
                    <div class="clear"></div>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer" v-if="!openStatus">
                <el-button type="info" @click="closeDialog" size="small">取 消</el-button>
                <el-button type="danger" size="small" @click="submitForm('addOrUpdateDataSetDialogForm')" :loading="buttonLoading">保 存</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
	export default {
		name: "add-or-update-data-set-dialog",
		components: {

        },
        props: ['isEditMeta','addOrUpdateDataSetDialogVisible', ],
		data() {
			return {
                buttonLoading: false,
                openStatus:false,
                isLoading: false,
                addOrUpdateDataSetDialogForm: {
                    dataSetName: '',
                    dataOrigin: '',
                    dataTable: '全部',
                    dataType: '',
                    dataElementName:''
                },
                dataOriginList:[
                    {id: '',name: '全部',}
                ],
                dataTableList: [],
                dataTypeList: [
                    {value:'',des:'',type:0,label:'全部'},
                    {value:'c',des:'输入小于4000的正整数',type:0,label:'c'},
                    {value:'c..',des:'输入小于4000的正整数',type:0,label:'c..'},
                    {value:'a',des:'输入小于4000的正整数',type:0,label:'a'},
                    {value:'a..',des:'输入小于4000的正整数',type:0,label:'a..'},
                    {value:'n',des:'输入小于4000的正整数',type:0,label:'n'},
                    {value:'n..',des:'输入小于4000的正整数',type:0,label:'n..'},
                    {value:'an',des:'输入小于4000的正整数',type:0,label:'an'},
                    {value:'an..',des:'输入小于4000的正整数',type:0,label:'an..'},
                    {value:'n..()',des:'输入两个正整数，并用英文逗号隔开',type:0,label:'n..()'},
                    {value:'d',des:'YYYYMMDD',type:'d',label:'d'},
                    {value:'t',des:'hhmmss',type:1,label:'t'},
                    {value:'dt',des:'YYYYMMDD hhmmss',type:1,label:'dt'}
                ],
                tableData: [],
                listAction:[],
                loading: false
            }
		},
		methods: {
            addMeta(obj,flag){
                let check=true;
                if(flag==false){
                    this.tableData.forEach((item)=>{
                        if(item.id===obj.id){
                            check=false;
                        }
                    });
                    if(check){
                        this.tableData.push(obj)
                    }
                }
            },
            getDataById(){
                this.$urlApi.dataStandard.editDataSet({id:this.isEditMeta.id}).then((res)=>{
                    this.tableData=res.element;
                    this.addOrUpdateDataSetDialogForm.dataSetName=res.set.name;
                })
            },
            deleteRow(num){
                this.tableData.splice(num,1)
            },
            getData(){
                let param={
                    dataSourceId:this.addOrUpdateDataSetDialogForm.dataOrigin,
                    tableName:this.addOrUpdateDataSetDialogForm.dataTable === '全部' ? '' : this.addOrUpdateDataSetDialogForm.dataTable,
                    dataElementType:this.addOrUpdateDataSetDialogForm.dataType,
                    dataElementName:this.addOrUpdateDataSetDialogForm.dataElementName,
                };
                this.loading = true;
                this.$urlApi.dataStandard.getAllElement(param).then((res)=>{
                    this.listAction=res.data
                    this.loading = false;
                }).catch(e => {
                    this.loading = true;
                })
            },
            getAllDataSource(){                 //获取所有数据源
                this.$urlApi.dataSource.getAllDataSource({pid:-1}).then((res)=>{
                    this.dataOriginList=this.dataOriginList.concat(res.data)
                })
            },
            changeDataOriginBase(val){
                this.addOrUpdateDataSetDialogForm.dataTable = '全部'
                this.$urlApi.dataSource.getDataTreeById({pid:-2,id:val}).then((res)=>{
                    if(res.data.length>0){
                        this.dataTableList=res.data;
                        this.dataTableList.unshift({id: '', name: '全部'})
                    }else{
                        this.dataTableList=[];
                    }
                });
                this.getData()
            },
            changeDataOriginTable(val){
                this.getData()
            },
            searchMeta(){
                this.getData()
            },
            changeDataType() {
                this.getData()
            },
            cleanData(){
                this.dataOriginList=[{id: '',name: '全部',}];
                this.dataTableList=[];
                this.tableData=[];
                this.listAction=[];
                this.addOrUpdateDataSetDialogForm.dataSetName='';
                this.addOrUpdateDataSetDialogForm.dataOrigin='';
                this.addOrUpdateDataSetDialogForm.dataType='';
                this.addOrUpdateDataSetDialogForm.dataElementName='';
                this.addOrUpdateDataSetDialogForm.dataTable='全部';
            },
            closeDialog() {
                this.$refs['addOrUpdateDataSetDialogForm'].resetFields();
                this.cleanData();
                this.$emit('addOrUpdateDataSetDialogVisibleEvent', this.addOrUpdateDataSetDialogVisible);
            },
            openDialog(){
                this.openStatus=this.isEditMeta.isHistory
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let idsArr=[];
                        this.tableData.forEach((item)=>{
                            idsArr.push(item.id)
                        });
                        let param={
                            id:this.isEditMeta.id,
                            pid:this.isEditMeta.pid,
                            name:this.addOrUpdateDataSetDialogForm.dataSetName,
                            ids:idsArr.join(','),
                        };
                        this.buttonLoading = true;
                        this.$urlApi.dataStandard.addDataSet(param).then((res)=>{
                            this.cleanData();
                            this.buttonLoading = false;
                            this.$emit('addOrUpdateDataSetDialogVisibleEvent', this.addOrUpdateDataSetDialogVisible);
                            this.$refs['addOrUpdateDataSetDialogForm'].resetFields();
                        }).catch(e => {
                            this.buttonLoading = false;
                        })
                    } else {
                        return false;
                    }
                });
            },
            showCkeck(item) {
                return this.tableData.find(c => c.id === item.id) ? 'primary' : 'info'
            }
        },
        computed: {
            rules() {
                if (!this.isEditMeta.isHistory) {
                    return {
                        dataSetName: [
                            { required: true, message: '请填写标准分类名称', trigger: 'blur' },
                        ],
                    }
                } else {
                    return {}
                }
            }
        },
        watch:{
            isEditMeta:{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    this.getData()
                    this.getAllDataSource();
                    if(val.id!==''){
                         this.getDataById();
                    }
                },
                deep:true
            }
        },
	}
</script>

<style lang="scss">
    .add-or-update-data-set-dialog-section {
        .el-dialog__wrapper {
            min-width: 600px!important;
            .el-dialog__body {
                padding: 13px 20px;
                .el-form .el-form-item{
                    margin-bottom: 15px;
                }
            }
        }
        .el-table--scrollable-x .el-table__body-wrapper {
            overflow-x: hidden;
        }
        .tree-block {
            border-right: 1px solid #ddd;
            .type_name {
                display: inline-block;
                width: 60px;
                text-align: right;
            }
            .dataOriginList {
                height: 150px;
               /* overflow-y: scroll;*/
                .el-tag {
                    display: block;
                    width: 184px;
                    margin: 4px auto;
                    text-align: center;
                    cursor: pointer;
                    span{
                        display: inline-block;
                        width: 100%;
                    }
                }
            }
        }
        .tree-content {
            padding: 0 10px;
            .tree-content-table{
                height: 275px;
            }
        }
        .el-input {
            width: 160px!important;
        }
        .el-input .el-input__inner {
            display: inline-block;
            width: 200px;
        }
        .el-select {
            display: inline-block;
        }
        .el-select .el-input__inner {
            width: 100% !important;
        }
        [class*=el-col-12] {
            float: left;
        }
        .el-input-group--append {
            width: 178px;
            .el-input__inner {
                width: 178px;
            }
        }
        .el-table td, .el-table .el-table th {
            padding: 6px 0;
        }
        .el-table__body {
            width: 100%!important;
        }
    }
</style>

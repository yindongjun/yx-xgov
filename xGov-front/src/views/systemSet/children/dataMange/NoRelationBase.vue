<template>
    <el-dialog
        :title="isEditNoRelation.id===''? '新建数据源':'编辑数据源'"
        :visible.sync="NoRelationBaseVisible"
        :before-close="closeDialog"
        :lock-scroll="false"
        :close-on-click-modal="false"
        class="no-relation-base"
        width="760px">
        <el-dialog :close-on-click-modal='false'
            width="500px"
            title="元数据库配置"
            :visible.sync="innerVisible"
            @open="openInner"
            append-to-body>
            <el-form :model="hiveInnerForm" ref="hiveInnerForm" :rules="rules" size="small"  label-width="100px" label-position="right">
                <el-form-item label="数据源类型">
              <!--      <el-select v-model="hiveInnerForm.hiveMetaDataDbType" style="width: 100%" size="small" placeholder="请选择数据源类型">
                        <el-option value="MySQL" label="MySQL"></el-option>
                    </el-select>-->
                    <el-input v-model="hiveInnerForm.hiveMetaDataDbType" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="IP" prop="hiveMetaDataIp">
                    <el-input v-model="hiveInnerForm.hiveMetaDataIp" :disabled="isEditNoRelation.id!==''"></el-input>
                </el-form-item>
                <el-form-item label="数据库端口" prop="hiveMetaDataPort">
                    <el-input v-model="hiveInnerForm.hiveMetaDataPort" :disabled="isEditNoRelation.id!==''"></el-input>
                </el-form-item>
                <el-form-item label="数据库名称" prop="hiveMetaDataDbName">
                    <el-input v-model="hiveInnerForm.hiveMetaDataDbName" :disabled="isEditNoRelation.id!==''"></el-input>
                </el-form-item>
                <el-form-item label="用户名" prop="hiveMetaDataUserName">
                    <el-input v-model="hiveInnerForm.hiveMetaDataUserName" :disabled="isEditNoRelation.id!==''"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="hiveMetaDataPassword">
                    <el-input type="password" v-model="hiveInnerForm.hiveMetaDataPassword"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="info" size="small" @click="innerVisible=false">取 消</el-button>
                <el-button type="danger" size="small" @click="submitMetaForm('hiveInnerForm')">确 定</el-button>
            </span>
        </el-dialog>
        <el-form :model="baseForm" ref="baseForm" :rules="rules" size="small"  label-width="110px" label-position="right">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="数据源名称"  prop="datasourceName">
                        <el-input v-model="baseForm.datasourceName" :disabled="isEditNoRelation.id!==''"  maxlength="20" placeholder="最大长度为20个字符"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据源类型" prop="databaseType">
                        <el-select :disabled="isEditNoRelation.id!==''" v-model="baseForm.databaseType" style="width: 100%" size="small" placeholder="请选择数据源类型">
                            <el-option value="HDFS" label="HDFS"></el-option>
                            <el-option value="Hive" label="Hive"></el-option>
                            <el-option value="Hbase" label="Hbase"></el-option>
                            <el-option value="Impala" label="Impala"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="IP" v-if="baseForm.databaseType!=='Hbase'" prop="ip">
                        <el-input v-model="baseForm.ip" :disabled="isEditNoRelation.id!==''"></el-input>
                    </el-form-item>
                    <el-form-item label="IP地址"  v-else prop="zkQuorum">
                        <el-input v-model="baseForm.zkQuorum" :disabled="isEditNoRelation.id!==''"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="端口号"  prop="port">
                        <el-input v-model="baseForm.port" :disabled="isEditNoRelation.id!==''"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <!-- <div v-if="baseForm.databaseType==='HDFS'">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="数据库用户名"  prop="userName">
                            <el-input v-model="baseForm.userName" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div> -->
            <div v-if="baseForm.databaseType==='Hbase'">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="表空间"  prop="dbname">
                            <el-input v-model="baseForm.dbname" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="rootdir" prop="rootdir">
                            <el-input v-model="baseForm.rootdir" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
            <div v-if="baseForm.databaseType==='Hive'">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="数据库" prop="dbname">
                            <el-input v-model="baseForm.dbname" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="">
                            <el-button type="text" @click="innerVisible=true">元数据库配置</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户名" prop="userName">
                            <el-input v-model="baseForm.userName" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码">
                            <el-input type="password" v-model="baseForm.password"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
            <div v-if="baseForm.databaseType==='Impala'">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="数据库" prop="dbname">
                            <el-input v-model="baseForm.dbname" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="">
                            <el-button type="text" @click="innerVisible=true">元数据库配置</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户名" prop="userName1">
                            <el-input v-model="baseForm.userName" :disabled="isEditNoRelation.id!==''"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="密码" prop="password1">
                            <el-input type="password" v-model="baseForm.password"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="所属部门">
                        <el-input v-model="baseForm.departmentId" :disabled="true"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据源分层" prop="sourceLayerId">
                        <el-cascader
                            style="width: 100%"
                            change-on-select
                            :options="layerOptions"
                            :props="layerOptionsProps"
                            v-model="baseForm.sourceLayerId"
                            @change="handleLayerChange">
                        </el-cascader>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button class="reset-btn" type="info" size="small"  @click="resetForm('baseForm')" v-if="isEditNoRelation.id==''">重 置</el-button>
            <el-button type="danger" size="small" @click="submitForm('baseForm')" :loading="buttonLoading">保 存</el-button>
        </span>
    </el-dialog>
</template>

<script>
    export default {
        name: "",
        components: {},
        props:['NoRelationBaseVisible','isEditNoRelation'],
        data() {
            return {
                buttonLoading:false,
                innerVisible:false,
                hiveInnerForm:{
                    hiveMetaDataDbType:'MySQL',
                    hiveMetaDataIp:'',
                    hiveMetaDataPort:'',
                    hiveMetaDataDbName:'',
                    hiveMetaDataUserName:'',
                    hiveMetaDataPassword:'',
                },
                baseForm:{
                    id:'',
                    datasourceName:'',
                    databaseType:'',
                    ip:'',
                    port:'',
                    dbname:'',
                    oracleSid:'',
                    schemasName:'',
                    userName:'',
                    password:'',
                    zkQuorum:'',
                    rootdir:'',
                    departmentId:'',
                    sourceLayerId:[],
                    hiveMetaDataDbType:'MySQL',
                    hiveMetaDataIp:'',
                    hiveMetaDataPort:'',
                    hiveMetaDataDbName:'',
                    hiveMetaDataUserName:'',
                    hiveMetaDataPassword:'',
                    hbaseId:'',
                    hiveId:'',
                    hdfsId:'',
                },
                rules: {
                    datasourceName: [
                        { required: true, message: '请填写数据源名称', trigger: 'blur' },
                    ],
                    databaseType:[
                        { required: true, message: '请选择数据源类型', trigger: 'change' }
                    ],
                    ip: [
                        { required: true, message: '请填写ip地址', trigger: 'blur' },
                    ],
                    port:[
                        { required: true, message: '请填写端口号', trigger: 'blur' },
                    ],
                    oracleSid:[
                        { required: true, message: '请填写数据库SID', trigger: 'blur' },
                    ],
                    dbname:[
                        { required: true, message: '请填写数据库名称', trigger: 'blur' }
                    ],
                    userName: [
                        // { required: true, message: '请填写数据库用户名', trigger: 'blur' },
                        { message: '请填写数据库用户名', trigger: 'blur' },
                    ],
                    password:[
                        // { required: true, message: '请填写密码', trigger: 'blur' }
                        { message: '请填写密码', trigger: 'blur' }
                    ],
                    userName1: [
                        // { required: true, message: '请填写数据库用户名', trigger: 'blur' },
                        { message: '请填写数据库用户名', trigger: 'blur' },
                    ],
                    password1:[
                        // { required: true, message: '请填写密码', trigger: 'blur' }
                        { message: '请填写密码', trigger: 'blur' }
                    ],
                    schemasName:[
                        { required: true, message: '请填写模式', trigger: 'blur' }
                    ],
                    zkQuorum:[
                        { required: true, message: '请填写zkQuorum', trigger: 'blur' }
                    ],
                    rootdir:[
                        { required: true, message: '请填写rootdir', trigger: 'blur' }
                    ],
                    sourceLayerId: [
                        { required: true, message: '请选择数据源分层', trigger: 'change' },
                    ],

                    hiveMetaDataDbType:[
                        { required: true, message: '请填写元数据类型', trigger: 'blur' }
                    ],
                    hiveMetaDataIp:[
                        { required: true, message: '请填写元数据IP', trigger: 'blur' }
                    ],
                    hiveMetaDataPort:[
                        { required: true, message: '请填写元数据端口号', trigger: 'blur' }
                    ],
                    hiveMetaDataDbName:[
                        { required: true, message: '请填写元数据库名称', trigger: 'blur' }
                    ],
                    hiveMetaDataUserName:[
                        { required: true, message: '请填写元数据库用户名', trigger: 'blur' }
                    ],
                    hiveMetaDataPassword:[
                        { required: true, message: '请填写元数据库密码', trigger: 'blur' }
                    ],
                },
                layerOptionsProps:{
                    value:'id',
                    label:'name'
                },
                layerOptions:[

                ]
            }
        },
        methods: {
            getLayerMenu(){
                this.$urlApi.dataSource.getLayerMenu().then((res)=>{
                    this.layerOptions=res.data
                })
            },
            getDataById(){
                this.$urlApi.dataSource.getDataById({'id':this.isEditNoRelation.id}).then((res)=>{
                    this.baseForm.datasourceName=res.data.datasourceName;
                    this.baseForm.databaseType=res.data.databaseType;
                    this.baseForm.ip=res.data.ip;
                    this.baseForm.port=res.data.port;
                    this.baseForm.dbname=res.data.dbname;
                    this.baseForm.oracleSid=res.data.oracleSid;
                    this.baseForm.schemasName=res.data.schemasName;
                    this.baseForm.userName=res.data.userName;
                    this.baseForm.password=res.data.password;
                    this.baseForm.password='';
                    this.baseForm.sourceLayerId=res.data.layerList.split(',').map(Number);
                    if(res.data.databaseType=='HDFS'){
                        this.baseForm.hdfsId=res.hdfsSource.id;
                    }
                    if(res.data.databaseType=='Hbase') {
                        this.baseForm.zkQuorum = res.hbaseSource.zkQuorum;
                        this.baseForm.rootdir = res.hbaseSource.rootdir;
                        this.baseForm.hbaseId = res.hbaseSource.id;
                    }
                    if(res.data.databaseType=='Hive'){
                        this.hiveInnerForm.hiveMetaDataDbType=res.hiveSource.hiveMetaDataDbType;
                        this.hiveInnerForm.hiveMetaDataIp=res.hiveSource.hiveMetaDataIp;
                        this.hiveInnerForm.hiveMetaDataPort=res.hiveSource.hiveMetaDataPort;
                        this.hiveInnerForm.hiveMetaDataDbName=res.hiveSource.hiveMetaDataDbName;
                        this.hiveInnerForm.hiveMetaDataUserName=res.hiveSource.hiveMetaDataUserName;
                        this.hiveInnerForm.hiveMetaDataPassword=res.hiveSource.hiveMetaDataPassword;
                        this.hiveInnerForm.hiveMetaDataPassword=res.hiveSource.hiveMetaDataPassword;
                        this.baseForm.hiveId=res.hiveSource.id;
                    }
                    if(res.data.databaseType=='Impala'){
                        this.hiveInnerForm.hiveMetaDataDbType=res.hiveSource.metadataDbType;
                        this.hiveInnerForm.hiveMetaDataIp=res.hiveSource.ip;
                        this.hiveInnerForm.hiveMetaDataPort=res.hiveSource.port;
                        this.hiveInnerForm.hiveMetaDataDbName=res.hiveSource.dbname;
                        this.hiveInnerForm.hiveMetaDataUserName=res.hiveSource.userName;
                        this.hiveInnerForm.hiveMetaDataPassword=res.hiveSource.password;
                        this.hiveInnerForm.hiveMetaDataPassword=res.hiveSource.password;
                        this.baseForm.hiveId=res.hiveSource.id;
                    }
                })
            },
            handleLayerChange(cb){   //数据分层改变
                console.log(cb)
            },
            submitMetaForm(formName){        //元数据modal
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.innerVisible=false;

                    } else {
                        return false;
                    }
                });
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.baseForm.hiveMetaDataDbType=this.hiveInnerForm.hiveMetaDataDbType;
                        this.baseForm.hiveMetaDataIp=this.hiveInnerForm.hiveMetaDataIp;
                        this.baseForm.hiveMetaDataPort=this.hiveInnerForm.hiveMetaDataPort;
                        this.baseForm.hiveMetaDataDbName=this.hiveInnerForm.hiveMetaDataDbName;
                        this.baseForm.hiveMetaDataUserName=this.hiveInnerForm.hiveMetaDataUserName;
                        this.baseForm.hiveMetaDataPassword=this.hiveInnerForm.hiveMetaDataPassword;

                        this.baseForm.layerId=this.baseForm.sourceLayerId[this.baseForm.sourceLayerId.length-1];
                        this.baseForm.layerList=this.baseForm.sourceLayerId.join(",");
                        this.buttonLoading=true;
                        this.$urlApi.dataSource.checkData(this.baseForm).then((res)=>{
                            this.$urlApi.dataSource.saveData(this.baseForm).then((res)=>{
                                this.buttonLoading=false;
                                this.$refs['baseForm'].resetFields();
                                this.clearHiveInner();
                                this.baseForm.sourceLayerId=[];
                                this.baseForm.ip='';
                                this.$emit("NoRelationBaseBack",1);
                            }).catch(e => {
                                this.buttonLoading = false;
                            })
                        }).catch(e => {
                            this.buttonLoading = false;
                        })
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.baseForm.sourceLayerId=[];
                this.baseForm.rootdir="";
                this.baseForm.userName="";
                this.baseForm.password="";
                this.baseForm.port="";
                this.baseForm.dbname="";

                this.$refs['hiveInnerForm'] && this.$refs['hiveInnerForm'].resetFields();
                this.clearHiveInner();
            },
            openInner(){
                //console.log(this.isEditNoRelation.id);
                /*if(this.isEditNoRelation.id===""){
                    this.clearHiveInner()
                }*/
            },
            clearHiveInner(){
                this.hiveInnerForm.hiveMetaDataDbType = 'MySQL';
                this.hiveInnerForm.hiveMetaDataIp = '';
                this.hiveInnerForm.hiveMetaDataPort = '';
                this.hiveInnerForm.hiveMetaDataDbName = '';
                this.hiveInnerForm.hiveMetaDataUserName = '';
                this.hiveInnerForm.hiveMetaDataPassword = '';
            },
            closeDialog(){
                this.buttonLoading=false;
                this.$refs['baseForm'].resetFields();
                this.baseForm.ip='';
                this.baseForm.userName="";
                this.baseForm.password="";
                this.baseForm.port="";
                this.baseForm.dbname="";
                this.baseForm.sourceLayerId=[];
                this.clearHiveInner();
                this.$emit("NoRelationBaseBack",0);
            },
        },
        mounted: function () {

        },
        watch:{
            isEditNoRelation:{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    this.baseForm.rootdir="";
                    this.baseForm.id="";
                    this.baseForm.userName="";
                    this.baseForm.password="";
                    this.baseForm.port="";
                    this.baseForm.dbname="";
                   // this.baseForm.sourceLayerId=[];
                    this.getLayerMenu();
                    //this.clearHiveInner()
                    if(val.id!==''){
                        this.getDataById();
                        this.baseForm.id=val.id
                    }
                },
                deep:true
            },
            baseForm: {
                handler(newVal, oldVal){
                    // this.checkStatus=false
                },
                deep: true
            }
        }
    }
</script>

<style lang="scss">
    .no-relation-base{
        .check-ok-status{
            padding-left: 110px;font-size: 12px;color: #67c23a;height: 25px;line-height: 25px;margin: 0;
        }
        .check-false-status{
            display: none;margin: 0;
        }
    }
</style>

<template>
    <el-dialog :close-on-click-modal='false'
        title=""
        :visible.sync="AddUpdateVisible"
        @closed="closeDialog"
        @open="openDialog"
        :before-close="closeDialog"
        class="add-update-meta"
        width="800px">
        <span slot="title" v-if="isEditMeta.id === ''">新建标准</span>
        <span slot="title" v-if="!(isEditMeta.id === '')&& !isEditMeta.isHistory">编辑标准</span>
        <span slot="title" v-if="isEditMeta.isHistory">标准详情</span>
        <el-form :model="baseForm" ref="baseForm"  :rules="rules" size="small"  label-width="110px" label-position="right">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="标准维护名称：" prop="dataElementName">
                        <span class="historyShow" v-if="isEditMeta.isHistory">{{baseForm.dataElementName}}</span>
                        <el-input v-else v-model="baseForm.dataElementName" maxlength="20" placeholder="最大长度为20个字符"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="英文名称：" prop="dataElementCode">
                        <span class="historyShow" v-if="isEditMeta.isHistory|| !(isEditMeta.id === '')">{{baseForm.dataElementCode}}</span>
                        <el-input v-else class="checkEnglish" v-model="baseForm.dataElementCode" maxlength="20" placeholder="输入字母数字和下划线"
                                  @blur="checkCode()"
                        ></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item label="标准类型：" prop="standardType">
                        <span class="historyShow" v-if="isEditMeta.isHistory">{{baseForm.standardType}}</span>
                        <el-select v-else v-model="baseForm.standardType" size="small">
                            <el-option value="技术标准" label="技术标准"></el-option>
                            <el-option value="指标标准" label="指标标准"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="标准别名：" prop="standardAlias">
                        <span class="historyShow" v-if="isEditMeta.isHistory">{{baseForm.standardAlias}}</span>
                        <el-input size="mini" v-else v-model="baseForm.standardAlias" placeholder="输入标准别名，不同别名用逗号隔开"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
         <!--   <el-form-item label="数据源标记">
                <el-input v-model="baseForm.ip"></el-input>
            </el-form-item>-->
            <el-row>
                <el-col :span="12">
                    <el-form-item label="数据类型：" prop="dataElementType">
                    <span class="historyShow" v-if="!(isEditMeta.id === '') || isEditMeta.isHistory">{{baseForm.dataElementType}}</span>
                    <el-select v-else v-model="baseForm.dataElementType" style="width: 100%" size="small" placeholder="请选择数据类型">
                        <el-option-group
                        v-for="group in dataTypeSource"
                        :key="group.value"
                        :label="group.value">
                        <!-- <el-tooltip class="item" :enterable='false' effect="light" :content="item.tip" v-for="item in group.options" :key="item.value" placement="right"> -->
                            <el-option
                                v-for="item in group.options" :key="item.value"
                                :disabled="isEditMeta.isHistory"
                                :title="item.tip"
                                :label="item.value"
                                :value="item.value">
                                    <span style="float: left">{{ item.value }}</span>
                                    <span style="float: right; color: #8492a6; font-size: 12px">{{ item.tip }}</span>
                            </el-option>
                        <!-- </el-tooltip> -->

                        </el-option-group>
                        <!-- <el-option :value="item.value" :label="item.value" :key="item.value" v-for="item in dataType" :disabled="isEditMeta.isHistory" ></el-option> -->
                    </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="!isEditMeta.isHistory" prop="dataElementAttr" label="校验长度：">
                        <template v-for="item in dataType" >
                            <el-input
                                v-model="baseForm.dataElementAttr"
                                :placeholder="isEditMeta.isHistory ? '' :item.des"
                                v-if="item.value===baseForm.dataElementType"
                                onkeyup="this.value=this.value.replace(/[^\d\,]/g,'')"
                                :key="item.value">
                            </el-input>
                        </template>
                    </el-form-item>
                    <el-form-item v-else label="校验长度：">
                        <label style="color: #333;font-size: 12px;">{{baseForm.dataElementAttr}}</label>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="值域：">
                <span class="historyShow" v-if="isEditMeta.isHistory">{{getMenu}}</span>
                <el-cascader
                    v-else
                    :placeholder="isEditMeta.isHistory ? '': '请选择'"
                    clearable
                    :options="layerOptions"
                    :props="layerOptionsProps"
                    v-model="baseForm.sourceLayerId"
                    @change="handleLayerChange">
                </el-cascader>
            </el-form-item>
           <!-- <el-form-item label="数据范围"  prop="dataRange">-->
            <el-row>
                <el-col :span="13">
                    <el-form-item
                        label="数据范围："
                        prop="dataRangeFront">
                        <span class="historyShow" v-if="isEditMeta.isHistory">{{baseForm.dataRangeFront}}&nbsp;&nbsp;~&nbsp;&nbsp;{{baseForm.dataRangeEnd}}</span>
                        <el-input v-else v-model.number="baseForm.dataRangeFront" :placeholder="isEditMeta.isHistory ? '' : '请输入较小数,不小于-4000000000000000'"></el-input>
                    </el-form-item>
                </el-col>
                <el-col v-if="!isEditMeta.isHistory" class="line ac" :span="1">~</el-col>
                <el-col v-if="!isEditMeta.isHistory" :span="10">
                    <el-form-item
                        label-width="0px"
                        prop="dataRangeEnd"
                    >
                        <el-input v-model.number="baseForm.dataRangeEnd" :placeholder="isEditMeta.isHistory ? '' : '请输入较大数,4000000000000000'" :disabled="isEditMeta.isHistory"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

           <!-- </el-form-item>-->
            <el-form-item label="正则表达式：">
                <span class="historyShow" v-if="isEditMeta.isHistory">{{getRegExp}}</span>
                <el-select v-else clearable v-model="baseForm.regularExpression"  size="small" :placeholder="isEditMeta.isHistory ? '' : '请选择正则表达式'">
                    <el-option v-for="c in regExpOptions" :key="c.value" :value="c.value" :label="c.label"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="描述：" prop="remark">
                <span class="historyShow" v-if="isEditMeta.isHistory">{{baseForm.remark}}</span>
                <el-input
                    v-else
                    type="textarea"
                    :rows="2"
                    :placeholder="isEditMeta.isHistory ? '' : '请输入内容'"
                    v-model="baseForm.remark">
                </el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer" v-if="!openStatus">
            <el-button class="reset-btn" type="info" size="small"  @click="resetForm('baseForm')">重 置</el-button>
            <el-button type="danger" size="small" @click="submitForm('baseForm')" :loading="buttonLoading">保 存</el-button>
        </span>
    </el-dialog>
</template>
<script>
    export default {
        name: "",
        components: {},
        props:['AddUpdateVisible','isEditMeta'],
        data() {
            return {
                openStatus:false,
                buttonLoading:false,
                dataTypeSource:[
                    {
                        value: 'c',
                        options: [
                            {value:'c', tip: '定长的字符'},
                            {value:'c..', tip: '最多xx位字符'}
                        ]
                    },
                    {
                        value: 'a',
                        options: [
                            {value:'a', tip: '定长字母字符'},
                            {value:'a..', tip: '最多xx位字母字符'}
                        ]
                    },
                    {
                        value: 'n',
                        options: [
                            {value:'n', tip: '定长数字字符'},
                            {value:'n..', tip: '最多xx位数字字符'},
                            {value:'n..()', tip: '浮点数'}
                        ]
                    },
                    {
                        value: 'an',
                        options: [
                            {value:'an', tip: '定长字母和数字字符'},
                            {value:'an..', tip: '最多xx位字母和数字字符'}
                        ]
                    },
                    {
                        value: 'd',
                        options: [
                            {value:'d', tip: 'YYYYMMDD日期'}
                        ]
                    },
                    {
                        value: 't',
                        options: [
                            {value:'t', tip: 'hhmmss时间'}
                        ]
                    },
                    {
                        value: 'dt',
                        options: [
                            {value:'dt', tip: 'YYYYMMDDhhmmss日期时间'}
                        ]
                    }
                ],
                dataType:[
                    {value:'c',des:'请输入校验字符长度',type:0},
                    {value:'c..',des:'请输入校验字符长度',type:0},
                    {value:'a',des:'请输入校验字符长度',type:0},
                    {value:'a..',des:'请输入校验字符长度',type:0},
                    {value:'n',des:'请输入校验字符长度',type:0},
                    {value:'n..',des:'请输入校验字符长度',type:0},
                    {value:'an',des:'请输入校验字符长度',type:0},
                    {value:'an..',des:'请输入校验字符长度',type:0},
                    {value:'n..()',des:'请输入校验长度和小数位数，用逗号隔开',type:0},
                    {value:'d',des:'YYYYMMDD',type:1},
                    {value:'t',des:'hhmmss',type:1},
                    {value:'dt',des:'YYYYMMDD hhmmss',type:1},
                    // {value:'b',des:'图形，图片等二进制类型',type:1},
                ],
                baseForm:{
                    id:0,
                    dataElementName:'',
                    dataElementCode:'',
                    standardType: '技术标准',
                    standardAlias: '',
                    dataElementType:'',
                    dataElementAttr:'',
                    dataRangeFront:'',
                    dataRangeEnd:'',
                    regularExpression:'',
                    remark:'',
                    codeset:'',
                    codeList:'',
                    sourceLayerId:[],
                },
                layerOptionsProps:{
                    value:'id',
                    label:'name'
                },
                layerOptions:[

                ],
                regExpOptions: [
                    {
                        label: '身份证号码验证',
                        value: '^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))$'
                    },
                    {
                        label: '手机号码验证',
                        value: '^(17[0-9]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$'
                    },
                    {
                        label: 'IP地址验证',
                        value: '((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))'
                    },
                    {
                        label: '邮箱地址验证',
                        value: '^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$'
                    }
                ]
            }
        },
        methods: {
            validateDataRangeFront(rule, value, callback){
                if (value && typeof value === "string") {
                    callback(new Error('请输入数字'));
                }
                if(value!==''&& this.baseForm.dataRangeEnd!==''){
                    if(value>this.baseForm.dataRangeEnd || !Number.isFinite(value) || value < -4000000000000000){
                        callback(new Error('请输入合法范围值'));
                    }else{
                        callback();
                    }
                }else if(value!==''&& this.baseForm.dataRangeEnd==''){
                    this.$refs.baseForm.validateField('dataRangeEnd');
                }else if(value==''&& this.baseForm.dataRangeEnd!==''){
                    callback(new Error('请输入合法范围值'));
                }else{
                    callback();
                }
                //this.$refs.baseForm.validateField('dataRangeEnd');
            },
            validateDataRangeEnd(rule, value, callback) {
                if (value && typeof value === "string") {
                    callback(new Error('请输入数字'));
                }
                if(value!==''&& this.baseForm.dataRangeFront!==''){
                    if(value<this.baseForm.dataRangeFront || !Number.isFinite(value) || value > 4000000000000000){
                        callback(new Error('请输入合法范围值'));
                    }else{
                        this.$refs.baseForm.validateField('dataRangeFront');
                        callback();
                    }
                }else if(value!==''&& this.baseForm.dataRangeFront==''){
                    this.$refs.baseForm.validateField('dataRangeFront');
                }else if(value==''&& this.baseForm.dataRangeFront!==''){
                    callback(new Error('请输入合法范围值'));
                }else{
                    callback();
                }
            },
            validateElementAttr(rule, value, callback){
                if(value!==''&&this.baseForm.dataElementType!=='n..()'){
                    if(value.indexOf(',')!=-1 || value < 1 || value > 4000){
                        callback(new Error('请输入1-4000的整数！'));
                    }else{
                        callback();
                    }
                }else if(value!==''&&this.baseForm.dataElementType=='n..()'){
                    if(value.indexOf(',')!=-1 && !value.endsWith(',')){
                        var m = value.split(",");
                        if(m.length!==2){
                            callback(new Error('请输入合法的校验长度和小数位数并以逗号分隔！'));
                        }
                        if(parseInt(m[0])<=parseInt(m[1]) || parseInt(m[0])< 1 || parseInt(m[0])>4000 || parseInt(m[1])<0){
                            callback(new Error('请输入合法的校验长度和小数位数并以逗号分隔！'));
                        }
                        callback();
                    }else{
                        callback(new Error('请输入合法的校验长度和小数位数并以逗号分隔！'));
                    }
                }else{
                    callback();
                }
            },
            checkCode(){
                let code=this.baseForm.dataElementCode.replace(/[^\w_]/g,'');
                this.baseForm.dataElementCode = code;
            },
            getLayerMenu(){
                this.$urlApi.dataStandard.getAuditedCodeSet().then((res)=>{
                    this.layerOptions=res.data.filter(c => !!c.id)
                })
            },
            getDataById(){
                this.$urlApi.dataStandard.editDataElement({'id':this.isEditMeta.id}).then((res)=>{
                    this.baseForm.dataElementName=res.data.dataElementName;
                    this.baseForm.dataElementCode=res.data.dataElementCode;
                    this.baseForm.standardType = res.data.standardType;
                    this.baseForm.standardAlias = res.data.standardAlias;
                    this.baseForm.dataElementType=res.data.dataElementType;
                    this.baseForm.dataElementAttr=res.data.dataElementAttr;
                    if(res.data.dataRangeFront!==''){
                        this.baseForm.dataRangeFront=Number(res.data.dataRangeFront);
                    }else{
                        this.baseForm.dataRangeFront=res.data.dataRangeFront;
                    }
                    if(res.data.dataRangeEnd!==''){
                        this.baseForm.dataRangeEnd=Number(res.data.dataRangeEnd);
                    }else{
                        this.baseForm.dataRangeEnd=res.data.dataRangeEnd;
                    }
                    this.baseForm.regularExpression=res.data.regularExpression;
                    this.baseForm.remark=res.data.remark;
                    if(res.data.codeList){
                        this.baseForm.sourceLayerId=res.data.codeList.split(',').map(Number)
                    }
                })
            },
            handleLayerChange(cb){   //数据分层改变
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.baseForm.sourceLayerId.length===0){
                            this.baseForm.codeset='';
                        }else{
                            this.baseForm.codeset=this.baseForm.sourceLayerId[this.baseForm.sourceLayerId.length-1];
                        }
                        this.baseForm.codeList=this.baseForm.sourceLayerId.join(",");
                        this.buttonLoading=true;
                        this.$urlApi.dataStandard.addDataElement(this.baseForm).then((res)=>{
                            this.buttonLoading=false;
                            this.$refs['baseForm'].resetFields();
                            this.closeData();
                            this.$emit("AddUpdateVisibleBack",1);
                            // this.$message({
                            //     type: 'success',
                            //     message: '操作成功'
                            // });
                            this.$toast('success', '操作成功')
                        }).catch(e => {
                            this.buttonLoading=false;
                        })
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                if(!(this.isEditMeta.id === '')&& !this.isEditMeta.isHistory){
                    this.baseForm = {
                        id:this.baseForm.id,
                        dataElementName:'',
                        dataElementCode:this.baseForm.dataElementCode,
                        dataElementType:this.baseForm.dataElementType,
                        standardType: '技术标准',
                        standardAlias: '',
                        dataElementAttr:'',
                        dataRangeFront:'',
                        dataRangeEnd:'',
                        regularExpression:'',
                        remark:'',
                        codeset:'',
                        codeList:'',
                        sourceLayerId:[],
                    }
                }else{
                    this.closeData()
                    this.$refs[formName].resetFields();
                }
            },
            closeData(){
                this.baseForm.id = 0;
                this.baseForm.dataElementName = '';
                this.baseForm.dataElementCode = '';
                this.baseForm.standardType = '技术标准';
                this.baseForm.standardAlias = '';
                this.baseForm.dataElementType = '';
                this.baseForm.dataElementAttr = '';
                this.baseForm.dataRangeFront = '';
                this.baseForm.dataRangeEnd = '';
                this.baseForm.regularExpression = '';
                this.baseForm.remark = '';
                this.baseForm.codeset = '';
                this.baseForm.codeList = '';
                this.baseForm.sourceLayerId = [];
            },
            closeDialog(){
                // this.$refs['baseForm'].resetFields();
                this.closeData();
                this.$emit("AddUpdateVisibleBack",0);
            },
            openDialog(){
                this.openStatus=this.isEditMeta.isHistory;
                this.buttonLoading=false;
                this.$refs['baseForm'] && this.$refs['baseForm'].resetFields();
                this.$nextTick(() => {
                    this.checkEnglish();
                })
            },
            checkEnglish() {
                let _this = this;
                let isOk = false;
                $('.checkEnglish input').on('compositionstart', function () {
                    isOk = false;
                }).on('compositionend', function () {
                    isOk = true;
                }).on("keyup", function () {
                    if (isOk) {
                        _this.baseForm.dataElementCode = $('.checkEnglish input').val().replace(/[^\w_]/g,'');
                    }
                });
            }
        },
        computed: {
            rules() {
                if (!this.isEditMeta.isHistory) {
                    return {
                        dataElementName: [
                            { required: true, message: '请填写标准维护名称', trigger: 'blur' },
                        ],
                        dataElementCode:[
                            { required: true, message: '请填写英文名称', trigger: 'blur' }
                        ],
                        dataElementType:[
                            { required: true, message: '请选择数据类型', trigger: 'change' }
                        ],
                        remark:[
                            { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur' }
                        ],
                        dataRangeFront:[
                        // { type: 'number', message: '必须为数字值',required:false},
                            { validator: this.validateDataRangeFront, type: 'number', trigger:[ 'blur','change'],required:false }
                        ],
                        dataRangeEnd:[
                            //{ type: 'number', message: '必须为数字值',required:false},
                            { validator: this.validateDataRangeEnd, type: 'number', trigger: [ 'blur','change'] }
                        ],
                        dataElementAttr:[
                            { required: (this.baseForm.dataElementType=='c..'||this.baseForm.dataElementType=='a..'||this.baseForm.dataElementType=='n..'||this.baseForm.dataElementType=='an..'||this.baseForm.dataElementType=='n..()'),
                                message: '请输入校验字符长度', trigger: 'blur' },
                            { validator: this.validateElementAttr, trigger: [ 'blur','change'] }
                        ]
                    }
                } else {
                    return {
                        remark:[
                            { min: 1, max: 255, message: '长度在 1 到 255 个字符', trigger: 'blur' }
                        ],
                        dataRangeFront:[
                            { validator: this.validateDataRangeFront, trigger:[ 'blur','change'],required:false }
                        ],
                        dataRangeEnd:[
                            { validator: this.validateDataRangeEnd, trigger: [ 'blur','change'] }
                        ]
                    }
                }
            },
            getRegExp() {
                const a = this.baseForm.regularExpression;
                const reg = this.regExpOptions.find(c => c.value == a);
                if (reg) {
                    return reg.label
                }
            },
            getMenu() {
                const func = (arr) => {
                    const b = [];
                    const a = (arr) => {
                        arr.forEach(c => {
                            b.push(c)
                            if (c.children) {
                                a(c.children)
                            }
                        })
                    }
                    a(arr);
                    return b
                }
                const d = func(this.layerOptions)
                return this.baseForm.sourceLayerId.map(k => {
                    const currentMenu = d.find(c => k == c.id);
                    return currentMenu ? currentMenu.name : '';
                }).join('/')
            }
        },
        mounted: function () {
            this.getLayerMenu();
        },
        watch:{
            isEditMeta:{//深度监听，可监听到对象、数组的变化
                handler(val, oldVal){
                    this.getLayerMenu();
                    if(val.id!==''){
                        this.getDataById();
                        this.baseForm.id=val.id
                    }
                },
                deep:true
            },
        }
    }
</script>

<style lang="scss">
    .add-update-meta{
        .historyShow {
            font-size: 12px;
            color: #333;
        }
    }
</style>

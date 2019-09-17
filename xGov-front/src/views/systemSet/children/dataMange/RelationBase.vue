<template>
  <el-dialog
    :title="isEditRelation.id === '' ? '新建数据源' : '编辑数据源'"
    :visible.sync="RelationBaseVisible"
    :before-close="closeDialog"
    :lock-scroll="false"
    :close-on-click-modal="false"
    class="relation-base"
    width="760px"
  >
    <el-form
      :model="baseForm"
      ref="baseForm"
      :rules="rules"
      size="small"
      label-width="100px"
      label-position="right"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="数据源名称" prop="datasourceName">
            <el-input
              v-model="baseForm.datasourceName"
              maxlength="20"
              placeholder="最大长度为20个字符"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据源类型" prop="databaseType">
            <el-select
              :disabled="isEditRelation.id !== ''"
              v-model="baseForm.databaseType"
              style="width: 100%"
              size="small"
              placeholder="请选择数据源类型"
            >
              <el-option value="MySQL" label="MySQL"></el-option>
              <el-option value="Oracle" label="Oracle"></el-option>
              <el-option value="SqlServer" label="SqlServer"></el-option>
              <el-option value="DB2" label="DB2"></el-option>
              <el-option value="PostgreSQL" label="PostgreSQL"></el-option>
              <el-option value="Sybase" label="Sybase"></el-option>
              <el-option value="Teradata" label="Teradata"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="IP" prop="ip">
            <el-input
              v-model="baseForm.ip"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="端口号" prop="port">
            <el-input
              v-model="baseForm.port"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            prop="dbname"
            v-if="baseForm.databaseType !== 'Oracle'"
            label="数据库名"
          >
            <el-input
              v-model="baseForm.dbname"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
          <el-form-item prop="oracleSid" v-else label="数据库SID">
            <el-input
              v-model="baseForm.oracleSid"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="模式"
            prop="schemasName"
            v-if="
              baseForm.databaseType === 'Oracle' ||
                baseForm.databaseType === 'SqlServer' ||
                baseForm.databaseType === 'DB2' ||
                baseForm.databaseType === 'PostgreSQL' ||
                baseForm.databaseType === 'Sybase'
            "
          >
            <el-input
              v-model="baseForm.schemasName"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户名" prop="userName">
            <el-input
              v-model="baseForm.userName"
              :disabled="isEditRelation.id !== ''"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="baseForm.password"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属部门" :disabled="true">
            <el-input
              v-model="baseForm.departmentId"
              :disabled="true"
            ></el-input>
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
              @change="handleLayerChange"
            >
            </el-cascader>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- <p :class="checkStatus ? 'check-ok-status' : 'check-false-status'">
      <i style="font-weight: 700" class="el-icon-check"></i
      ><span class="ml5">已连接，校验成功</span>
    </p> -->
    <span slot="footer" class="dialog-footer">
      <el-button
        type="info"
        size="small"
        @click="resetForm('baseForm')"
        v-if="isEditRelation.id == ''"
        >重 置</el-button
      >
      <el-button
        type="danger"
        size="small"
        @click="submitForm('baseForm')"
        :loading="buttonLoading"
        >保 存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  components: {},
  props: ["RelationBaseVisible", "isEditRelation"],
  data() {
    return {
      buttonLoading: false,
      baseForm: {
        id: "",
        datasourceName: "",
        databaseType: "",
        ip: "",
        port: "",
        dbname: "",
        oracleSid: "",
        schemasName: "",
        userName: "",
        password: "",
        departmentId: "",
        sourceLayerId: []
      },
      rules: {
        datasourceName: [
          { required: true, message: "请填写数据源名称", trigger: "blur" }
        ],
        databaseType: [
          { required: true, message: "请选择数据源类型", trigger: "change" }
        ],
        ip: [{ required: true, message: "请填写ip地址", trigger: "blur" }],
        port: [{ required: true, message: "请填写端口号", trigger: "blur" }],
        oracleSid: [
          { required: true, message: "请填写数据库SID", trigger: "blur" }
        ],
        dbname: [
          { required: true, message: "请填写数据库名称", trigger: "blur" }
        ],
        userName: [
          { required: true, message: "请填写数据库用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请填写密码", trigger: "blur" }],
        schemasName: [
          { required: true, message: "请填写模式", trigger: "blur" }
        ],
        sourceLayerId: [
          { required: true, message: "请选择数据源分层", trigger: "change" }
        ]
      },
      layerOptionsProps: {
        value: "id",
        label: "name"
      },
      layerOptions: []
    };
  },
  methods: {
    getLayerMenu() {
      this.$urlApi.dataSource.getLayers().then(res => {
        this.layerOptions = res.data;
      });
    },
    getDataById() {
      this.$urlApi.dataSource
        .getDataById({ id: this.isEditRelation.id })
        .then(res => {
          this.baseForm.datasourceName = res.data.datasourceName;
          this.baseForm.databaseType = res.data.databaseType;
          this.baseForm.ip = res.data.ip;
          this.baseForm.port = res.data.port;
          this.baseForm.dbname = res.data.dbname;
          this.baseForm.oracleSid = res.data.oracleSid;
          this.baseForm.schemasName = res.data.schemasName;
          this.baseForm.userName = res.data.userName;
          this.baseForm.password = res.data.password;
          this.baseForm.password = '';
          this.baseForm.sourceLayerId = res.data.layerList
            .split(",")
            .map(Number);
        });
    },
    handleLayerChange(cb) {},
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.baseForm.layerId = this.baseForm.sourceLayerId[
            this.baseForm.sourceLayerId.length - 1
          ];
          this.baseForm.layerList = this.baseForm.sourceLayerId.join(",");
          this.buttonLoading = true;
          this.$urlApi.dataSource.checkData(this.baseForm).then(res => {
            this.$urlApi.dataSource
              .saveData(this.baseForm)
              .then(res => {
                this.buttonLoading = false;
                this.baseForm.sourceLayerId = [];
                this.baseForm.schemasName = "";
                this.$refs["baseForm"].resetFields();
                this.$emit("RelationBaseBack", 1);
              })
              .catch(e => {
                this.buttonLoading = false;
              });
          }).catch(e => {
            this.buttonLoading = false;
          })
          }
      });
    },
    resetForm(formName) {
      this.baseForm.sourceLayerId = [];
      this.$refs[formName].resetFields();
    },
    closeDialog() {
      this.buttonLoading = false;
      this.$refs["baseForm"].resetFields();
      this.baseForm.sourceLayerId = [];
      this.baseForm.schemasName = "";
      this.$emit("RelationBaseBack", 0);
    }
  },
  mounted: function() {
    this.getLayerMenu();
  },
  watch: {
    isEditRelation: {
      handler(val, oldVal) {
        this.baseForm.schemasName = "";
        this.baseForm.id = "";
        this.getLayerMenu();
        if (val.id !== "") {
          this.getDataById();
          this.baseForm.id = val.id;
        }
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
.relation-base {
  .check-ok-status {
    padding-left: 110px;
    font-size: 12px;
    color: #67c23a;
    height: 25px;
    line-height: 25px;
    margin: 0;
  }
  .check-false-status {
    display: none;
    margin: 0;
  }
}
</style>

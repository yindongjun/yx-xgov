<template>
  <el-dialog
    title="获取表数据"
    :visible.sync="setTableInfoVisible"
    :before-close="closeDialog"
    class="set-table-info"
    :lock-scroll="false"
    width="760px"
    @open="openTableInfo"
    append-to-body
    :close-on-click-modal="false"
  >
    <el-form
      :model="baseForm"
      ref="baseForm"
      :rules="rules"
      size="small"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="步骤名称" prop="stepName">
        <el-input v-model="baseForm.stepName"></el-input>
      </el-form-item>
      <el-form-item label="表名">
        {{ baseForm.tableName }}
      </el-form-item>
      <el-form-item label="SQL语句" prop="desc">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请输入内容"
          v-model="baseForm.desc"
        >
        </el-input>
      </el-form-item>
      <el-row>
        <el-col :span="12">
          <el-form-item label="最大记录数" prop="maxNum">
            <el-input v-model="baseForm.maxNum"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主键" prop="primaryKey">
            <el-input v-model="baseForm.primaryKey"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  props: ["setTableInfoVisible", "isTableRelation"],
  components: {},
  data() {
    return {
      baseForm: {
        stepName: "",
        tableName: "",
        desc: "",
        maxNum: "",
        primaryKey: ""
      },
      rules: {
        stepName: [
          { required: true, message: "请填写步骤名称", trigger: "blur" }
        ],
        desc: [{ required: true, message: "请填写sql语句", trigger: "blur" }],
        maxNum: [
          { required: true, message: "请填写最大记录数", trigger: "blur" }
        ],
        primaryKey: [{ required: true, message: "请填写主键", trigger: "blur" }]
      }
    };
  },
  methods: {
    getBackData() {
      this.$urlApi.dataQuality
        .createTransByTable({ tableDesignId: this.isTableRelation.id })
        .then(res => {
          this.baseForm.stepName = res.data.name;
          this.baseForm.desc = JSON.parse(res.data.verifyDetail).sql;
          this.baseForm.maxNum = res.data.fetchSize;
          this.baseForm.primaryKey = res.data.pkColumn;
        });
    },
    closeDialog() {
      this.$refs["baseForm"].resetFields();
      this.$emit("setTableInfoBack");
    },
    openTableInfo() {
      this.getBackData();
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$emit("setTableInfoBack");
          this.$refs["baseForm"].resetFields();
        } else {
          return false;
        }
      });
    }
  },
  mounted: function() {},
  watch: {
    isTableRelation: {
      handler(val, oldVal) {
        this.baseForm.tableName = val.tableName;
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
</style>

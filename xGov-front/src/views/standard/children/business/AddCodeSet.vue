<template>
  <el-dialog
    :close-on-click-modal="false"
    width="500px"
    :title="!isEditCodeSet.isEdit ? '新建代码集' : '编辑代码集'"
    :visible.sync="addCodeSetVisible"
    :before-close="closeDialog"
    append-to-body
  >
    <el-form
      :model="addCodeSet"
      :rules="rules"
      ref="addCodeSet"
      size="small"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="代码集名称：" prop="name">
        <el-input
          v-model="addCodeSet.name"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
      </el-form-item>
      <el-form-item label="代码规范：" prop="coderule">
        <el-input
          placeholder="请输入内容"
          v-model="addCodeSet.coderule"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="制定依据：" prop="createPolicy">
        <el-input
          type="textarea"
          placeholder="请输入内容"
          v-model="addCodeSet.createPolicy"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        @click="submitCodeSet('addCodeSet')"
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
  props: ["addCodeSetVisible", "isEditCodeSet"],
  data() {
    return {
      buttonLoading: false,
      addCodeSet: {
        pid: "",
        id: "",
        name: "",
        code: "",
        createPolicy: "",
        iscodeset: "0",
        explanation: "",
        express: "",
        coderule: ""
      },
      rules: {
        name: [
          { required: true, message: "请填写代码集名称", trigger: "blur" }
        ],
        code: [
          { required: true, message: "请填写代码集编号", trigger: "blur" }
        ],
        coderule: [
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submitCodeSet(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.dataStandard
            .updateAndAddCodeSet(this.addCodeSet)
            .then(res => {
              this.buttonLoading = false;
              this.$toast("success", "操作成功");
              this.$emit("addCodeSetBack");
              this.$refs["addCodeSet"].resetFields();
              this.cleanData();
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    cleanData() {
      this.addCodeSet.explanation = "";
      this.addCodeSet.express = "";
      this.addCodeSet.coderule = "";
      this.addCodeSet.createPolicy = "";
      this.addCodeSet.id = "";
      this.addCodeSet.pid = "";
    },
    closeDialog() {
      this.$refs["addCodeSet"].resetFields();
      this.cleanData();
      this.$emit("addCodeSetBack");
    }
  },
  mounted: function() {},
  watch: {
    isEditCodeSet: {
      handler(val, oldVal) {
        if (val.isEdit) {
          this.addCodeSet.pid = val.pid;
          this.addCodeSet.id = val.id;
        } else {
          this.addCodeSet.pid = val.id;
        }
        this.addCodeSet.name = val.name;
        this.addCodeSet.coderule = val.rule;
        this.addCodeSet.createPolicy = val.createPolicy;
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
</style>

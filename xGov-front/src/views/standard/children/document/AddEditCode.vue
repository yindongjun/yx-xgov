<template>
  <el-dialog
    :close-on-click-modal="false"
    width="500px"
    title="新建分层"
    :visible.sync="AddEditCodeListVisible"
    :before-close="closeDialog"
    append-to-body
  >
    <el-form
      :model="addEditCodeForm"
      :rules="rules"
      ref="addEditCodeForm"
      size="small"
      label-width="10z0px"
      label-position="right"
    >
      <el-form-item label="代码" prop="name">
        <el-input v-model="addEditCodeForm.name"></el-input>
      </el-form-item>
      <el-form-item label="代码名称" prop="code">
        <el-input v-model="addEditCodeForm.code"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        @click="submitCodeList('addEditCodeForm')"
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
  props: ["AddEditCodeListVisible", "isEditCodeList", "currNodeId"],
  data() {
    return {
      buttonLoading: false,
      isOuter: false,
      addEditCodeForm: {
        pid: "-1",
        id: "",
        name: "",
        code: "",
        codesetId: ""
      },
      rules: {
        name: [{ required: true, message: "请填写代码", trigger: "blur" }],
        code: [{ required: true, message: "请填写代码名称", trigger: "blur" }]
      }
    };
  },
  methods: {
    submitCodeList(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addEditCodeForm.codesetId = this.currNodeId;
          this.buttonLoading = true;
          if (this.addEditCodeForm.id === undefined) {
            this.$urlApi.dataStandard
              .codeSetAddCode(this.addEditCodeForm)
              .then(res => {
                this.buttonLoading = false;
                this.$toast("success", "操作成功");
                this.$emit("editCodeListBack");
                this.$refs["addEditCodeForm"].resetFields();
              })
              .catch(e => {
                this.buttonLoading = false;
              });
          } else {
            this.buttonLoading = true;
            this.$urlApi.dataStandard
              .CodeSetUpDate(this.addEditCodeForm)
              .then(res => {
                this.buttonLoading = false;
                this.$toast("success", "操作成功");
                this.$emit("editCodeListBack");
                this.$refs["addEditCodeForm"].resetFields();
              })
              .catch(e => {
                this.buttonLoading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
    closeDialog() {
      this.$refs["addEditCodeForm"].resetFields();
      this.$emit("editCodeListBack");
    }
  },
  mounted: function() {},
  watch: {
    isEditCodeList: {
      handler(val, oldVal) {
        if (val.id === undefined) {
          this.addEditCodeForm.id = undefined;
        } else {
          this.addEditCodeForm.id = val.id;
          this.addEditCodeForm.name = val.name;
          this.addEditCodeForm.code = val.code;
        }
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
</style>

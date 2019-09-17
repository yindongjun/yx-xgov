<template>
  <el-dialog
    :close-on-click-modal="false"
    width="500px"
    :title="
      isEditCatalog.isEdit === 'add'
        ? '新建目录'
        : isEditCatalog.isEdit === 'addChild'
        ? '新建下级目录'
        : '编辑目录'
    "
    :visible.sync="addCatalogVisible"
    :before-close="closeDialog"
    append-to-body
  >
    <el-form
      :model="addCataLogForm"
      :rules="rules"
      ref="addCataLogForm"
      size="small"
      label-width="80px"
      label-position="right"
    >
      <el-form-item v-if="isEditCatalog.isEdit !== 'add'" label="上级目录">
        <label v-if="isEditCatalog.isEdit === 'addChild'" style="font-size: 12px">{{ isEditCatalog.name }}</label>
        <label v-if="isEditCatalog.isEdit === 'edit'" style="font-size: 12px">{{ isEditCatalog.pname }}</label>
      </el-form-item>
      <el-form-item label="目录名称" prop="name">
        <el-input
          v-model="addCataLogForm.name"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
      </el-form-item>
      <el-form-item label="目录描述" prop="explanation">
        <el-input
          v-model="addCataLogForm.explanation"
          type="textarea"
          :rows="3"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        @click="submitCatalog('addCataLogForm')"
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
  props: ["addCatalogVisible", "isEditCatalog"],
  data() {
    return {
      buttonLoading: false,
      isOuter: false,
      addCataLogForm: {
        pid: "",
        id: "",
        name: "",
        explanation: "",
        iscodeset: "1"
      },
      rules: {
        name: [{ required: true, message: "请填写目录名称", trigger: "blur" }],
        // explanation: [
        //   { required: true, message: "请填写目录描述", trigger: "blur" },
        //   { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
        // ]
      }
    };
  },
  methods: {
    submitCatalog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.dataStandard
            .updateAndAddCodeSet(this.addCataLogForm)
            .then(res => {
              this.buttonLoading = false;
              this.$toast("success", "操作成功");
              this.$emit("addCatalogBack");
              this.$refs["addCataLogForm"].resetFields();
              this.cleanAddCataLogForm();
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    cleanAddCataLogForm() {
      this.addCataLogForm.pid = "";
      this.addCataLogForm.id = "";
      this.addCataLogForm.name = "";
      this.addCataLogForm.explanation = "";
      this.addCataLogForm.iscodeset = "1";
    },
    closeDialog() {
      this.$refs["addCataLogForm"].resetFields();
      this.cleanAddCataLogForm();
      this.$emit("addCatalogBack");
    }
  },
  mounted: function() {},
  watch: {
    isEditCatalog: {
      handler(val, oldVal) {
        if (val.id === undefined) {
          this.addCataLogForm.pid = -1;
          this.addCataLogForm.id = "";
          this.addCataLogForm.isCodeset = "1";
        } else {
          if (val.isEdit === "edit") {
            this.addCataLogForm.pid = val.pid;
            this.addCataLogForm.id = val.id;
            this.addCataLogForm.name = val.name;
            this.addCataLogForm.explanation = val.explanation;
          } else if (val.isEdit === "add") {
            this.addCataLogForm.pid = -1;
            this.addCataLogForm.id = "";
            this.addCataLogForm.isCodeset = "1";
          } else if (val.isEdit === "addChild") {
            this.addCataLogForm.pid = val.id;
            this.addCataLogForm.id = "";
            this.addCataLogForm.name = "";
            this.addCataLogForm.explanation = "";
          }
        }
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
</style>

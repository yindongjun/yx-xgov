<template>
  <el-dialog
    :close-on-click-modal="false"
    width="500px"
    :title="isEditCatalog.isEdit === 'add' ? '新建目录' : '新建下级目录'"
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
      <el-form-item label="上级目录" v-if="isEditCatalog.isEdit === 'addChild'">
        <label style="font-size: 12px">{{ isEditCatalog.name }}</label>
      </el-form-item>
      <el-form-item label="目录名称" prop="name">
        <el-input
          v-model="addCataLogForm.name"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
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
        parentId: "",
        id: "",
        name: ""
      },
      rules: {
        name: [{ required: true, message: "请填写目录名称", trigger: "blur" }]
      }
    };
  },
  methods: {
    submitCatalog(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.dataStandard
            .addDocumentContent(this.addCataLogForm)
            .then(res => {
              this.buttonLoading = false;
              this.$toast("success", "操作成功");
              this.$emit("addCatalogBack");
              this.$refs["addCataLogForm"].resetFields();
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    closeDialog() {
      this.$refs["addCataLogForm"].resetFields();
      this.$emit("addCatalogBack");
    }
  },
  mounted: function() {},
  watch: {
    isEditCatalog: {
      handler(val, oldVal) {
        if (val.id === undefined) {
          this.addCataLogForm.parentId = -1;
          this.addCataLogForm.id = "";
        } else {
          if (val.isEdit === "addChild") {
            this.addCataLogForm.parentId = val.pid;
            this.addCataLogForm.id = val.id;
          } else {
            this.addCataLogForm.parentId = val.id;
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

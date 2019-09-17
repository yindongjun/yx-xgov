<template>
  <el-dialog
    :close-on-click-modal="false"
    width="500px"
    title=""
    :visible.sync="AddEditCodeListVisible"
    :before-close="closeDialog"
    @closed="closeDialog"
    @open="openDialog"
    append-to-body
  >
    <span
      slot="title"
      style="font-size: 14px;"
      v-if="isEditCodeList.id === null"
      >新建代码</span
    >
    <span
      slot="title"
      style="font-size: 14px;"
      v-if="!(isEditCodeList.id === null) && !isEditCodeList.isHistory"
      >编辑代码</span
    >
    <span slot="title" style="font-size: 14px;" v-if="isEditCodeList.isHistory"
      >代码详情</span
    >
    <el-form
      :model="addEditCodeForm"
      :rules="rules"
      ref="addEditCodeForm"
      size="small"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="代码：" prop="code">
        <el-input
          v-model="addEditCodeForm.code"
          v-if="!isEditCodeList.isHistory"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
        <label v-else style="font-size: 12px; color:#606266;">{{addEditCodeForm.code}}</label>
      </el-form-item>
      <el-form-item label="代码名称：" prop="name">
        <el-input
          v-model="addEditCodeForm.name"
          v-if="!isEditCodeList.isHistory"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
        <label v-else style="font-size: 12px; color:#606266;">{{addEditCodeForm.name}}</label>
      </el-form-item>
      <el-form-item v-if="isEditCodeList.isHistory" label="所属目录：" prop="name">
        <label style="font-size: 12px; color:#606266;">{{addEditCodeForm.contents}}</label>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer" v-if="!openStatus">
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
      openStatus: false,
      isOuter: false,
      addEditCodeForm: {
        pid: "-1",
        id: "",
        name: "",
        code: "",
        codesetId: "",
        contents: "/wahaha/kuang quangshui"
      }
    };
  },
  methods: {
    submitCodeList(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addEditCodeForm.codesetId = this.currNodeId;
          this.buttonLoading = true;
          if (!this.isEditCodeList.id) {
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
    getCodeById() {
      this.$urlApi.dataStandard
        .getCodeById({ id: this.addEditCodeForm.id })
        .then(res => {
          this.addEditCodeForm.name = res.data[0].name;
          this.addEditCodeForm.code = res.data[0].code;
          this.addEditCodeForm.contents = res.data[0].path;
        });
    },
    closeDialog() {
      this.$refs["addEditCodeForm"].resetFields();
      this.$emit("editCodeListBack");
    },
    openDialog() {
      this.openStatus = this.isEditCodeList.isHistory;
      if (this.addEditCodeForm.id) {
        this.getCodeById();
      }
    }
  },
  computed: {
    rules() {
      if (!this.isEditCodeList.isHistory) {
        return {
          name: [
            { required: true, message: "请填写代码名称", trigger: "blur" }
          ],
          code: [{ required: true, message: "请填写代码", trigger: "blur" }]
        };
      } else {
        return {};
      }
    }
  },
  mounted: function() {},
  watch: {
    isEditCodeList: {
      handler(val, oldVal) {
        if (val.id === undefined || val.id === null) {
          this.addEditCodeForm.id = undefined;
        } else {
          this.addEditCodeForm.id = val.id;
        }
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
</style>

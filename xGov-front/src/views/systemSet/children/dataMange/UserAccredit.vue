<template>
  <el-dialog
    :close-on-click-modal="false"
    title="用户授权"
    :visible.sync="UserAccreditVisible"
    class="user-accredit"
    :before-close="closeDialog"
    width="760px"
  >
    <el-form
      :model="baseForm"
      ref="ruleCatalogue"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="上级目录">
        <el-input
          v-model="baseForm.prevName"
          size="medium"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
      </el-form-item>
      <el-form-item label="目录名称">
        <el-input
          v-model="baseForm.currName"
          size="medium"
          maxlength="20"
          placeholder="最大长度为20个字符"
        ></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button type="danger" size="small" @click="submitForm('ruleForm')"
        >保 存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  components: {},
  props: ["UserAccreditVisible"],
  data() {
    return {
      baseForm: {
        prevName: "",
        currName: ""
      }
    };
  },
  methods: {
    submitForm() {
      this.$emit("UserAccreditBack");
    },
    closeDialog() {
      this.$emit("UserAccreditBack");
    }
  },
  mounted: function() {}
};
</script>

<style lang="scss">
</style>

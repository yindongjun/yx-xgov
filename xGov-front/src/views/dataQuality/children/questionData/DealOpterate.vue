<template>
  <el-dialog
    title="问题数据处理"
    :visible.sync="dealOperateVisible"
    :before-close="closeDialog"
    class="deal-operate"
    :lock-scroll="false"
    @open="openDialog"
    width="500px"
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
      <el-form-item label="处理人" prop="dealPeople">
        <el-input
          type="text"
          placeholder="请输入内容"
          maxlength="20"
          v-model="baseForm.dealPeople"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="处理方式" prop="dealComment">
        <el-input
          type="textarea"
          :rows="3"
          placeholder="请输入内容"
          v-model="baseForm.dealComment"
        >
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        @click="submitForm('baseForm')"
        :loading="buttonLoading"
        >保存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  props: ["dealOperateVisible", "isdealOperate"],
  components: {},
  data() {
    return {
      buttonLoading: false,
      baseForm: {
        ids: "",
        dealPeople: "",
        dealComment: ""
      },
      rules: {
        dealPeople: [
          { required: true, message: "请填写处理人", trigger: "blur" }
        ],
        dealComment: [
          { required: true, message: "请填写处理方式", trigger: "blur" },
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    closeDialog() {
      this.$refs["baseForm"].resetFields();
      this.$emit("dealOperateBack");
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.dataQuality
            .dealProblemData(this.baseForm)
            .then(res => {
              this.buttonLoading = false;
              this.$emit("dealOperateBack");
              this.$refs["baseForm"].resetFields();
              this.$toast("success", "操作成功");
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    openDialog() {
      if (this.isdealOperate.dealPeople) {
        this.baseForm.dealPeople = this.isdealOperate.dealPeople;
      } else {
        this.baseForm.dealPeople = JSON.parse(localStorage.getItem('user')).user;
      }
      this.baseForm.dealComment = this.isdealOperate.dealComment;
      this.baseForm.ids = this.isdealOperate.ids;
    }
  },
  mounted: function() {},
  watch: {
    isdealOperate: {
      handler(val, oldVal) {},
      deep: true
    }
  }
};
</script>

<style lang="scss">
</style>

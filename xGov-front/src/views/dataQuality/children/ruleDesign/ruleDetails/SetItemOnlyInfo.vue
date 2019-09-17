<template>
  <el-dialog
    :title="isItemRelation.name"
    :visible.sync="setItemOnlyInfoVisible"
    :before-close="closeDialog"
    class="set-Item-info"
    :lock-scroll="false"
    width="500px"
    @open="openItem"
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
      <el-form-item label="规则名称" prop="stepName">
        <el-input :disabled="true" v-model="baseForm.stepName"></el-input>
      </el-form-item>
      <el-form-item label="字段名称" prop="filedName">
        <el-select
          :disabled="currentStatus == 2"
          style="width: 100%"
          v-model="baseForm.filedName"
          filterable
          placeholder="请选择字段名称"
        >
          <el-option
            v-for="item in fieldList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button
        type="info"
        v-if="currentStatus == 2"
        size="small"
        @click="closeDialog"
        >关 闭</el-button
      >
      <el-button
        type="info"
        size="small"
        @click="closeDialog"
        >取 消</el-button
      >
      <el-button
        type="danger"
        v-if="currentStatus != 2"
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
  props: ["setItemOnlyInfoVisible", "isItemRelation", "status"],
  data() {
    return {
      buttonLoading: false,
      baseForm: {
        stepName: "",
        filedName: ""
      },
      rules: {
        filedName: [
          { required: true, message: "请选择字段名称", trigger: "blur" }
        ]
      },
      fieldList: [],
      currType: ""
    };
  },
  methods: {
    closeDialog() {
      this.$refs["baseForm"].resetFields();
      this.$emit("setItemOnlyInfoBack", this.isItemRelation.add ? 1 : 0);
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$emit("setItemOnlyInfoBack", { ...this.baseForm });
          setTimeout(() => {
            this.buttonLoading = false;
          }, 1000);
        } else {
          return false;
        }
      });
    },
    openItem() {
      this.currType = this.isItemRelation.type;
      this.baseForm.stepName = this.isItemRelation.name;
      this.baseForm.filedName = this.isItemRelation.isEdit
        ? this.isItemRelation.filedName
        : "";
      this.$urlApi.dataStandard
        .searchByColumn({
          id: this.isItemRelation.id,
          tableName: this.isItemRelation.tableName,
          enableFuzzy: false
        })
        .then(res => {
          this.fieldList = res.data[0]
            ? res.data[0].cols.map(c => ({
                label: c.name,
                value: c.name
              }))
            : [];
        });
    }
  },
  computed: {
    currentStatus() {
      return this.$props.status;
    }
  },
  mounted: function() {}
};
</script>

<style lang="scss">
</style>

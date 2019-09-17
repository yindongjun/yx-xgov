<template>
  <section class="add-or-update-warn-rule-dialog-section">
    <el-dialog
      :title="isEditRelation.id == -1 ? '新建报警规则' : '修改报警规则'"
      :visible.sync="addOrUpdateWarnRuleDialogVisible"
      :before-close="closeDialog"
      :lock-scroll="false"
      :close-on-click-modal="false"
      @open='open'
      width="500px"
    >
      <el-form
        :model="addOrUpdateWarnRuleDialogForm"
        :rules="rules"
        ref="addOrUpdateWarnRuleDialogForm"
        class="demo-form-inline"
        label-width="80px"
      >
        <el-form-item label="报警名称: " prop="alarmName">
          <el-input
            v-model="addOrUpdateWarnRuleDialogForm.alarmName"
            maxlength="128"
            placeholder="最大长度为128个字符"
            size="small"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="报警原因: " prop="alarmReasion">
          <el-radio-group v-model="addOrUpdateWarnRuleDialogForm.alarmReasion">
            <el-radio label="1" name="warnReason">完成</el-radio>
            <el-radio label="0" name="warnReason">失败</el-radio>
            <el-radio label="2" name="warnReason">完成/失败</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="报警方式: " prop="alarmMethod">
          <el-radio-group v-model="addOrUpdateWarnRuleDialogForm.alarmMethod">
            <el-radio label="1" name="warnMethod">邮件</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="接收人: " prop="receivePeople">
          <el-input v-model="addOrUpdateWarnRuleDialogForm.receivePeople" :disabled="true" size="small">
            <el-button slot="append" @click="selectUsers" icon="el-icon-more"></el-button>
          </el-input>
        </el-form-item>
      </el-form>
      <select-uers :visible='visibleSelectUsers' :type="isEditRelation.id == -1 ? 'add' : 'edit'" :ids='selectedUsers' @selectCallback='selectCallback'></select-uers>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="closeDialog" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitForm('addOrUpdateWarnRuleDialogForm')"
          :loading="buttonLoading"
          >保 存</el-button
        >
      </div>
    </el-dialog>
  </section>
</template>

<script>
import selectUers from './selectUers';

export default {
  name: "add-or-update-warn-rule-dialog",
  components: { selectUers },
  props: ["addOrUpdateWarnRuleDialogVisible", "isEditRelation"],
  data() {
    return {
      buttonLoading: false,
      addOrUpdateWarnRuleDialogForm: {
        alarmId: 0,
        alarmName: "",
        alarmReasion: "",
        alarmMethod: '1',
        receivePeople:''
      },
      rules: {
        alarmName: [
          {
            min: 1,
            max: 128,
            required: true,
            message: "最大长度为128个字符",
            trigger: "blur"
          }
        ],
        alarmReasion: [
          { required: true, message: "请至少选择一个", trigger: "change" }
        ],
        alarmMethod: [
          { required: true, message: "请至少选择一个", trigger: "change" }
        ],
        receivePeople: [
          { required: true, message: "请选择接收人" }
        ]
      },
      visibleSelectUsers: false,
      selectedUsers: [],
      allUser: []
    };
  },
  methods: {
    getDataById() {
      this.$urlApi.dispatchTask
        .getAlarmById({ id: this.isEditRelation.id })
        .then(res => {
          if (res.data) {
            this.addOrUpdateWarnRuleDialogForm = {
              alarmId: res.data.id,
              alarmName: res.data.alarmName,
              alarmReasion: res.data.alarmReasion + "",
              alarmMethod: res.data.alarmMethod + "",
              receivePeople: res.data.receivePeople.split(',').map(c => this.allUser.find(k => k.id == c).username).join(',')
            };
            this.selectedUsers = res.data.receivePeople.split(',').map(c => +c)
          }
        });
    },
    closeDialog() {
      this.$emit(
        "addOrUpdateWarnRuleDialogVisibleEvent",
        this.addOrUpdateWarnRuleDialogVisible
      );
      this.addOrUpdateWarnRuleDialogForm.alarmId = 0;
      this.$refs["addOrUpdateWarnRuleDialogForm"].resetFields();
      this.resetData();
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          const a = { ...this.addOrUpdateWarnRuleDialogForm }
          a.receivePeople = a.receivePeople.split(',').map(c => this.allUser.find(k => k.username === c).id).join(',')
          this.$urlApi.dispatchTask
            .saveUpdateAlarm(a)
            .then(res => {
              this.buttonLoading = false;
              this.$toast("success", "操作成功");
              this.$refs["addOrUpdateWarnRuleDialogForm"].resetFields();
              this.addOrUpdateWarnRuleDialogForm.alarmId = 0;
              this.$emit("addOrUpdateWarnRuleDialogVisibleEvent", 1);
              this.resetData()
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    selectUsers() {
      this.visibleSelectUsers = true;
    },
    selectCallback(cb) {
      if (cb) {
        this.addOrUpdateWarnRuleDialogForm.receivePeople = cb.map(c => c.username).join(',');
        this.selectedUsers = cb.map(c => c.id);
      }
      this.visibleSelectUsers = false;
    },
    open() {
      this.$urlApi.dispatchTask.getUserList({pageNumber: 1, pageSize: 10000000}).then(res => {
        this.allUser = res.data;
        this.isEditRelation.id != -1 && this.getDataById();
      });
    },
    resetData() {
      this.addOrUpdateWarnRuleDialogForm.alarmId = 0;
      this.addOrUpdateWarnRuleDialogForm.alarmName = "";
      this.addOrUpdateWarnRuleDialogForm.alarmReasion = "";
      this.addOrUpdateWarnRuleDialogForm.alarmMethod = '1';
      this.addOrUpdateWarnRuleDialogForm.receivePeople = '';
      this.visibleSelectUsers = false;
      this.selectedUsers = [];
      this.allUser = [];
    }
  }
};
</script>

<style lang="scss">
.add-or-update-warn-rule-dialog-section {
  .el-dialog__wrapper {
    .el-dialog__body {
      .el-form .el-form-item {
        margin-bottom: 0 !important;
        &:nth-child(1) {
          .el-form-item__error {
            top: 85%;
          }
        }
      }
    }
    .el-form-item__error {
      top: 85%;
    }
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #3ba3f8;
    background: #3ba3f8;
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #3ba3f8;
    font-size: 12px;
  }
  .el-radio__label {
    font-size: 12px;
  }
}
</style>

<template>
  <el-dialog
    :title="isItemRelation.name"
    :visible.sync="setItemInfoVisible"
    :before-close="closeDialog"
    class="set-Item-info"
    :lock-scroll="false"
    width="760px"
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
    </el-form>
      <div style="overflow: hidden">
        <span style="float: left">
          <el-button class="grey-btn" v-if="currentStatus != 2" size="small" @click="addAuditFields">新增</el-button>
          <el-button class="grey-btn" v-if="currentStatus != 2" :disabled='deleteDisable' size="small" @click="() => deleteAuditFields()">删除</el-button>
        </span>
        <label style="float: right; font-size: 12px"
          >批量操作开关
          <el-switch v-model="batchSwitch" @change="allBatchChange"> </el-switch>
        </label>
      </div>
      <el-table :data="tableList" height="300" style="width: 100%" @selection-change="changeFun">
        <el-table-column type="selection" width="55" :selectable='checkboxT'> </el-table-column>
        <el-table-column prop="columnName" label="字段名"> </el-table-column>
        <el-table-column label="是否为内置规则">
          <template slot-scope="scope">
            <span v-if="scope.row.isStandard === 'Y'">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>

        <el-table-column
          v-if="currType == 6"
          prop="regularValue"
          label="正则表达式"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column v-if="currType == 2" prop="formatType" label="类型">
        </el-table-column>
        <el-table-column v-if="currType == 2" prop="formatLength" label="长度">
        </el-table-column>

        <el-table-column v-if="currType == 4" prop="minValue" label="最小值">
        </el-table-column>
        <el-table-column v-if="currType == 4" prop="maxValue" label="最大值">
        </el-table-column>

        <el-table-column
          prop="codeSetName"
          label="值域配置"
          v-if="currType == 5"
        >
        </el-table-column>

        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.enable"> </el-switch>
          </template>
        </el-table-column>
      </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">取 消</el-button>
      <el-button
        type="danger"
        size="small"
        v-if="currentStatus != 2"
        @click="submitForm('baseForm')"
        :loading="buttonLoading"
        >保存</el-button
      >
    </span>
    <add-audit-fields
      :addAuditVisible='addAuditVisible'
      :currType='currType'
      :tableInfo='isItemRelation'
      :tableList='tableList'
      @addAuditFieldsBack='addAuditFieldsBack'
    ></add-audit-fields>
  </el-dialog>
</template>

<script>
import SvgIcon from "../../../../../components/SvgIcon.vue";
import addAuditFields from './addAuditFields.vue';

export default {
  name: "",
  props: ["setItemInfoVisible", "isItemRelation", 'status'],
  components: { SvgIcon },
  components: {addAuditFields},
  data() {
    return {
      buttonLoading: false,
      baseForm: {
        stepName: ""
      },
      rules: {
        stepName: [
          { required: true, message: "请填写步骤名称", trigger: "blur" }
        ]
      },
      tableList: [],
      currType: "",
      addAuditVisible: false,
      checkData: []
    };
  },
  computed: {
    batchSwitch() {
      if (this.tableList.some(c => !!c.enable)) {
        return true;
      } else {
        return false;
      }
    },
    currentStatus() {
      return this.$props.status
    },
    deleteDisable() {
      return this.checkData.some(c => c.isStandard === 'Y')
    }
  },
  methods: {
    changeFun(val) {
      this.checkData = val;
    },
    checkboxT(row, index) {
      if (row.isStandard === 'Y') {
        return false
      }else{
        return true
      }
    },
    deleteAuditFields() {
      if (!this.checkData.length) {
        return this.$toast('error', '请先选择非内置规则！')
      }
      const params = this.checkData.map(c => c.id).join(',')
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      }).then(() => {
        this.$urlApi.dataQuality
          .removeQualityById({qualityTaskDetailIds: params})
          .then(res => {
            if (res.code === "200") {
              this.$toast("success", "删除成功");
              this.$emit(
                "setItemInfoBack",
                2
              );                          
            }
          })
      })
    },
    closeDialog() {          
      this.$emit("setItemInfoBack", this.isItemRelation.add ? 1 : 0);
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$emit(
            "setItemInfoBack",
            this.tableList.map(c => ({ id: c.id, enable: c.enable ? 1 : 0 }))
          );                  
          setTimeout(() => {
            this.buttonLoading = false;
          }, 1000);
        } else {
          return false;
        }
      });
    },
    deleteItem(item) {},
    openItem() {
      this.currType = this.isItemRelation.type;
      this.baseForm.stepName = this.isItemRelation.name;
      let NewArr = [];
      this.isItemRelation.rule && this.isItemRelation.rule.forEach(item => {
        let zlist = JSON.parse(item.verifyDetail);
        NewArr.push({
          columnName: item.columnName,
          isStandard: zlist.isStandard,
          id: item.id,
          regularValue: zlist.regularValue,
          formatType: zlist.formatType,
          formatLength: zlist.formatLength,
          maxValue: zlist.maxValue,
          minValue: zlist.minValue,
          codeSetName: zlist.codeSetName,
          enable: item.enable === 1
        });
      });
      this.tableList = NewArr;
    },
    allBatchChange(val) {
      this.tableList = this.tableList.map(c => {
        c.enable = val;
        return c;
      });
    },
    addAuditFields() {
      this.addAuditVisible = true;
    },
    addAuditFieldsBack(cb) {
      this.addAuditVisible = false;
      if (cb) {
          this.$emit(
            "setItemInfoBack",
            2
          );
          this.$refs["baseForm"].resetFields();        
      }
    }
  },
  mounted: function() {},
  watch: {
    isItemRelation: {
      handler(val, oldVal) {},
      deep: true
    }
  }
};
</script>

<style lang="scss">
.set-Item-info {
  .el-table {
    .deleteIcon {
      font-size: 13px;
      cursor: pointer;
    }
  }
}
</style>

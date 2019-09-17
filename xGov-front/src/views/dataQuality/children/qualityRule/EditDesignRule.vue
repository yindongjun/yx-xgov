<template>
  <el-dialog
    :title="isDesignRule.isHistory == true ? '查看质量规则' : '编辑质量规则'"
    :visible.sync="editDesignRuleVisible"
    :before-close="closeDialog"
    @open="openDialog"
    class="edit-design-info"
    :lock-scroll="false"
    width="760px"
    :close-on-click-modal="false"
  >
    <el-row class="business-basic-b">
      <el-table :data="tableData" height="310" style="width: 100%">
        <el-table-column prop="columnName" label="字段" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="sfFieldFormatVerify.rulevalue"
          label="数据格式校验"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-checkbox
              @change="
                checked =>
                  changeBox(checked, scope.$index, 'formatVerifyStatus')
              "
              v-if="scope.row.sfFieldFormatVerify.rulevalue"
              v-model="scope.row.formatVerifyStatus === 'OPEN'"
              :disabled="isDesignRule.isHistory"
            />
            &nbsp;{{ scope.row.sfFieldFormatVerify.rulevalue }}
          </template>
        </el-table-column>
        <el-table-column
          prop="sfFieldEnum.ruleStatus"
          label="值域校验"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-checkbox
              @change="
                checked => changeBox(checked, scope.$index, 'enumStatus')
              "
              v-model="scope.row.enumStatus === 'OPEN'"
              v-if="scope.row.sfFieldEnum.rulevalue"
              :disabled="isDesignRule.isHistory"
            />&nbsp; {{ scope.row.sfFieldEnum.rulevalue }}
          </template>
        </el-table-column>
        <el-table-column
          prop="sfFieldDefect.ruleStatus"
          label="空值校验"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-checkbox
              @change="
                checked => changeBox(checked, scope.$index, 'defectStatus')
              "
              v-model="scope.row.defectStatus === 'OPEN'"
              :disabled="isDesignRule.isHistory"
            />
          </template>
        </el-table-column>
        <el-table-column label="数据范围校验" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-checkbox
              @change="
                checked => changeBox(checked, scope.$index, 'intervalStatus')
              "
              v-model="scope.row.intervalStatus === 'OPEN'"
              v-if="scope.row.sfFieldInterval.rulevalue"
              :disabled="isDesignRule.isHistory"
            />&nbsp; {{ scope.row.sfFieldInterval.rulevalue }}
          </template>
        </el-table-column>
        <el-table-column label="正则表达式校验" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-checkbox
              @change="
                checked => changeBox(checked, scope.$index, 'regularStatus')
              "
              v-model="scope.row.regularStatus === 'OPEN'"
              v-if="scope.row.sfFieldRegular.rulevalue"
              :disabled="isDesignRule.isHistory"
            />
            &nbsp;{{ scope.row.sfFieldRegular.rulevalue }}
          </template>
        </el-table-column>
        <el-table-column label="操作" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.fieldValidateSwitch === 'OPEN'"
              @change="
                checked =>
                  changeSwitch(checked, scope.$index, 'fieldValidateSwitch')
              "
              active-color="#13ce66"
              :disabled="isDesignRule.isHistory"
            >
            </el-switch>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="ar mt15"
        @current-change="handleCurrentChangeInner"
        :current-page="currentPage"
        :page-size="10"
        layout="total, prev, pager, next"
        :total="total"
      >
      </el-pagination>
    </el-row>
    <span slot="footer" class="dialog-footer" v-if="!openStatus">
      <el-button type="info" size="small" @click="closeDialog">关 闭</el-button>
      <el-button
        type="danger"
        size="small"
        @click="saveData"
        :loading="buttonLoading"
        >保存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  props: ["editDesignRuleVisible", "isDesignRule"],
  components: {},
  data() {
    return {
      buttonLoading: false,
      openStatus: false,
      tableData: [],
      total: 0,
      currentPage: 1,
      pageLength: 10
    };
  },
  methods: {
    getData() {
      let params = {
        ruleId: this.isDesignRule.id,
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength
      };
      this.$urlApi.dataQuality.getRuleDetailById(params).then(res => {
        this.tableData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    handleCurrentChangeInner(cb) {
      this.currentPage = cb;
      this.getData();
    },
    changeBox(a, b, c) {
      if (a) {
        this.tableData[b][c] = "OPEN";
      } else {
        this.tableData[b][c] = "CLOSE";
      }
    },
    changeSwitch(a, b, c) {
      if (a) {
        this.tableData[b][c] = "OPEN";
      } else {
        this.tableData[b][c] = "CLOSE";
      }
    },
    closeDialog() {
      this.$emit("editDesignRuleBack");
      this.tableData = [];
    },
    openDialog() {
      this.openStatus = this.isDesignRule.isHistory;
    },
    saveData() {
      this.buttonLoading = true;
      this.$urlApi.dataQuality
        .updateRuleDetail({ ruleDetailList: JSON.stringify(this.tableData) })
        .then(res => {
          this.buttonLoading = false;
          this.$toast("success", "修改成功");
          this.$emit("editDesignRuleBack", 1);
          this.tableData = [];
        })
        .catch(e => {
          this.buttonLoading = false;
        });
    }
  },
  mounted: function() {},
  watch: {
    isDesignRule: {
      handler(val, oldVal) {
        this.getData();
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
.edit-design-info {
  .el-dialog__body {
    height: 350px;
    .business-basic-b {
      height: 100%;
      .el-table {
        td,
        .el-table th {
          padding: 6px 0;
        }
        .el-checkbox__input.is-checked .el-checkbox__inner,
        .el-checkbox__input.is-indeterminate .el-checkbox__inner {
          background-color: rgb(19, 206, 102);
          border-color: rgb(19, 206, 102);
        }
      }
    }
  }
}
</style>

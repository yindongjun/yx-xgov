<template>
  <section class="design-details-dialog-section">
    <el-dialog
      title="新建质量规则"
      :visible.sync="designDetailsVisible"
      :before-close="closeDialog"
      :lock-scroll="false"
      width="760px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="designDetailForm"
        ref="designDetailForm"
        class="demo-form-inline"
        label-width="90px"
      >
        <div class="content">
          <el-col :span="8" class="pt10">
            <div class="tree-block">
              <span>待添加规则数据源</span>
              <el-select
                v-model="designDetailForm.dataOrigin"
                placeholder="请选择"
                size="mini"
                class="mt10 mb5"
                :disabled="!dataOriginList.length"
                @change="changeDataOriginBase"
              >
                <el-option
                  v-for="item in dataOriginList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
              <div class="mt5">
                <el-input
                  placeholder="输入表名称"
                  size="mini"
                  v-model="designDetailForm.dataElementName"
                  style="width: 100%"
                >
                  <el-button
                    size="mini"
                    slot="append"
                    @click="searchMeta"
                    icon="el-icon-search"
                  ></el-button>
                </el-input>
              </div>
              <div class="dataOriginList ac" v-loading="isLoading">
                <vue-scroll>
                  <span
                    v-if="listAction.length == 0"
                    style="font-size: 12px;line-height: 150px"
                    >表个数为空</span
                  >
                  <el-tag
                    type="info"
                    size="small"
                    :key="item"
                    v-for="(item, index) in Newitems"
                  >
                    <span @click="addMeta(item, index)">{{ item }}</span>
                    <i
                      :class="{
                        'el-icon-circle-check': true,
                        selected: currIndex == index
                      }"
                    ></i>
                  </el-tag>
                </vue-scroll>
              </div>
            </div>
          </el-col>
          <el-col :span="16" class="pt20">
            <div class="tree-content">
              <div class="tree-content-table">
                <el-table
                  :data="tableData"
                  style="width: 100%"
                  height="300"
                >
                  <el-table-column
                    prop="columnName"
                    label="字段"
                    width="70"
                    show-overflow-tooltip
                  >
                  </el-table-column>
                  <el-table-column
                    prop="sfFieldFormatVerify.rulevalue"
                    label="数据格式校验"
                    width="100"
                    show-overflow-tooltip
                  >
                  </el-table-column>
                  <el-table-column
                    prop="sfFieldEnum.ruleStatus"
                    width="80"
                    label="值域校验"
                    show-overflow-tooltip
                  >
                  </el-table-column>
                  <el-table-column
                    prop="sfFieldDefect.ruleStatus"
                    width="80"
                    label="空值校验"
                    show-overflow-tooltip
                  >
                  </el-table-column>
                  <el-table-column
                    prop="sfFieldInterval.rulevalue"
                    label="数据范围校验"
                    width="100"
                    show-overflow-tooltip
                  >
                  </el-table-column>
                  <el-table-column
                    prop="sfFieldRegular.rulevalue"
                    label="正则表达式校验"
                    width="110"
                    show-overflow-tooltip
                  >
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </el-col>
          <div class="clear"></div>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="closeDialog" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitForm"
          :loading="buttonLoading"
          :disabled="currTableName == '' || currBaseId == '' || tableData == ''"
          >保 存</el-button
        >
      </div>
    </el-dialog>
  </section>
</template>

<script>
export default {
  name: "add-or-update-data-set-dialog",
  components: {},
  props: ["isEditDetail", "designDetailsVisible"],
  data() {
    return {
      buttonLoading: false,
      isLoading: false,
      currBaseId: "",
      currTableName: "",
      designDetailForm: {
        dataOrigin: "",
        dataTable: "",
        dataType: "",
        dataElementName: ""
      },
      dataOriginList: [],
      tableData: [],
      listAction: [],
      currIndex: -1
    };
  },
  computed: {
    Newitems() {
      let _this = this;
      let Newitems = [];
      _this.listAction.map(function(item) {
        if (item.search(_this.designDetailForm.dataElementName) != -1) {
          Newitems.push(item);
        }
      });
      return Newitems;
    }
  },
  methods: {
    addMeta(name, index) {
      this.currIndex = index;
      this.tableData = [];
      this.$urlApi.dataQuality
        .createRuleDetail({ sourceId: this.currBaseId, tableName: name })
        .then(res => {
          this.currTableName = name;
          this.tableData = res.data;
        });
    },
    getAllDataSource() {
      this.$urlApi.dataSource.getAllDataSource({ pid: -1 }).then(res => {
        this.dataOriginList = res.data;
      });
    },
    changeDataOriginBase(val) {
      this.currBaseId = val;
      this.listAction = [];
      this.isLoading = true;
      this.currIndex = -1;
      this.tableData = [];
      this.$urlApi.dataQuality
        .getAvailableTable({ sourceId: val })
        .then(res => {
          this.isLoading = false;
          if (res.data.length > 0) {
            this.listAction = res.data;
          } else {
            this.listAction = [];
          }
        });
    },
    searchMeta() {},
    cleanData() {
      this.currTableName = "";
      this.currBaseId = "";
      this.dataOriginList = [];
      this.tableData = [];
      this.listAction = [];
      this.designDetailForm.dataOrigin = "";
      this.designDetailForm.dataType = "";
      this.designDetailForm.dataElementName = "";
    },
    closeDialog() {
      this.cleanData();
      this.$emit("designDetailBack");
    },
    submitForm() {
      let param = {
        tableName: this.currTableName,
        sourceId: this.currBaseId,
        ruleDetailList: JSON.stringify(this.tableData)
      };
      this.buttonLoading = true;
      this.$urlApi.dataQuality
        .addQualityRule(param)
        .then(res => {
          this.buttonLoading = false;
          this.cleanData();
          this.$toast("success", "新建成功");
          this.$emit("designDetailBack", 1);
        })
        .catch(e => {
          this.buttonLoading = false;
        });
    }
  },
  watch: {
    isEditDetail: {
      handler(val, oldVal) {
        this.getAllDataSource();
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
.design-details-dialog-section {
  .el-dialog__wrapper {
    min-width: 600px !important;
    .el-dialog__body {
      padding: 13px 20px;
      .el-form .el-form-item {
        margin-bottom: 15px;
      }
    }
  }
  .tree-block {
    border-right: 1px solid #ddd;
    .type_name {
      display: inline-block;
      width: 60px;
      text-align: right;
    }
    .dataOriginList {
      height: 200px;
      .el-tag {
        display: block;
        width: 184px;
        margin: 4px auto;
        text-align: center;
        cursor: pointer;
        position: relative;
        span {
          display: inline-block;
          width: 100%;
        }
        i {
          position: absolute;
          right: 5px;
          top: 5px;
          display: none;
        }
        .selected {
          position: absolute;
          right: 5px;
          top: 5px;
          color: #5daf34;
          display: block;
        }
      }
    }
  }
  .tree-content {
    padding: 0 10px;
    .tree-content-table {
      height: 275px;
    }
  }
  .el-input {
    width: 160px !important;
  }
  .el-input .el-input__inner {
    display: inline-block;
    width: 200px;
  }
  .el-select {
    display: inline-block;
  }
  .el-select .el-input__inner {
    width: 100% !important;
  }
  [class*="el-col-12"] {
    float: left;
  }
  .el-input-group--append {
    width: 178px;
    .el-input__inner {
      width: 178px;
    }
  }
  .el-table td,
  .el-table .el-table th {
    padding: 6px 0;
  }
}
</style>

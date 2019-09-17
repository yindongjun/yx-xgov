<template>
  <section class="add-or-update-data-set-dialog-section">
    <el-dialog
      :title="addOrUpdate === 'add' ? '新建标准分类' : '修改标准分类'"
      :visible.sync="addOrUpdateDataSetDialogVisible"
      :before-close="closeDialog"
      :lock-scroll="false"
      width="760px"
      :close-on-click-modal="false"
      :open="initDialogData()"
    >
      <el-form
        :model="addOrUpdateDataSetDialogForm"
        :rules="rules"
        ref="addOrUpdateDataSetDialogForm"
        class="demo-form-inline"
        label-width="90px"
      >
        <div class="content">
          <el-col :span="12">
            <el-form-item label="标准分类名称: " prop="dataSetName">
              <el-input
                v-model="addOrUpdateDataSetDialogForm.dataSetName"
                size="small"
                style="width: 100%"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标准分类编码: " prop="dataSetCode">
              <el-input
                v-model="addOrUpdateDataSetDialogForm.dataSetCode"
                size="small"
                style="width: 100%"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" class="pt10">
            <div class="tree-block">
              <div>待选标准维护</div>
              <div>
                <span class="type_name">数据源:</span>
                <el-select
                  v-model="addOrUpdateDataSetDialogForm.dataOrigin"
                  placeholder="请选择"
                  size="mini"
                  :disabled="!dataOriginList.length"
                >
                  <el-option
                    v-for="item in dataOriginList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </div>
              <div class="mt5 mb5">
                <span class="type_name">数据表:</span>
                <el-select
                  v-model="addOrUpdateDataSetDialogForm.dataTable"
                  placeholder="请选择"
                  size="mini"
                  :disabled="!dataTableList.length"
                >
                  <el-option
                    v-for="item in dataTableList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </div>
              <div class="mt5 mb5">
                <span class="type_name">数据类型:</span>
                <el-select
                  v-model="addOrUpdateDataSetDialogForm.dataType"
                  placeholder="请选择"
                  size="mini"
                  :disabled="!dataTypeList.length"
                >
                  <el-option
                    v-for="item in dataTypeList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </div>
              <div class="mt5">
                <el-input placeholder="输入标准维护名称" size="mini">
                  <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
              </div>
              <div class="dataOriginList">
                <el-tag type="info">标准维护a</el-tag>
                <el-tag type="info">标准维护b</el-tag>
                <el-tag type="info">标准维护c</el-tag>
                <el-tag type="info">标准维护e</el-tag>
                <el-tag type="info">标准维护e</el-tag>
              </div>
            </div>
          </el-col>
          <el-col :span="16" class="pt20">
            <div class="tree-content">
              <div>已选标准维护</div>
              <el-table :data="tableData" style="width: 100%;">
                <el-table-column
                  prop="dataOriginName"
                  label="标准维护名称"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column prop="code" label="CODE" show-overflow-tooltip>
                </el-table-column>
                <el-table-column
                  prop="dataType"
                  label="数据类型"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="valueRange"
                  label="值域"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column label="操作" width="70">
                  <template>
                    <el-button type="text" size="mini">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination
                class="mt15 ar"
                layout="prev, pager, next"
                :total="20"
              >
              </el-pagination>
            </div>
          </el-col>
          <div class="clear"></div>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer" align="center">
        <el-button type="danger" size="small">保 存</el-button>
        <el-button type="default" size="small">送 审</el-button>
        <el-button type="info" @click="closeDialog" size="small"
          >取 消</el-button
        >
      </div>
    </el-dialog>
  </section>
</template>

<script>
export default {
  name: "add-or-update-data-set-dialog",
  components: {},
  props: [
    "addOrUpdateDataSetDialogVisible",
    "addOrUpdate",
    "updateData",
    "updateStatus"
  ],
  data() {
    return {
      isLoading: false,
      addOrUpdateDataSetDialogForm: {
        dataSetName: "",
        dataSetCode: "",
        dataOrigin: "",
        dataTable: "",
        dataType: ""
      },
      dataOriginList: [
        {
          value: 1,
          label: "1"
        },
        {
          value: 2,
          label: "2"
        },
        {
          value: 3,
          label: "3"
        }
      ],
      dataTableList: [
        {
          value: 1,
          label: "1"
        },
        {
          value: 2,
          label: "2"
        },
        {
          value: 3,
          label: "3"
        }
      ],
      dataTypeList: [
        {
          value: 1,
          label: "1"
        },
        {
          value: 2,
          label: "2"
        },
        {
          value: 3,
          label: "3"
        }
      ],
      rules: {},
      tableData: [
        {
          dataOriginName: "标准维护a",
          code: "a",
          dataType: "n12"
        },
        {
          dataOriginName: "标准维护b",
          code: "b",
          dataType: "n13"
        },
        {
          dataOriginName: "标准维护c",
          code: "c",
          dataType: "n14"
        },
        {
          dataOriginName: "标准维护d",
          code: "d",
          dataType: "n15"
        },
        {
          dataOriginName: "标准维护e",
          code: "e",
          dataType: "n16"
        }
      ]
    };
  },
  methods: {
    closeDialog() {
      this.$emit(
        "addOrUpdateDataSetDialogVisibleEvent",
        this.addOrUpdateDataSetDialogVisible
      );
    },
    initDialogData: function() {}
  }
};
</script>

<style lang="scss">
.add-or-update-data-set-dialog-section {
  .el-dialog__wrapper {
    min-width: 600px !important;
    .el-dialog__body {
      padding: 13px 20px;
      .el-form .el-form-item {
        margin-bottom: 15px;
      }
    }
  }
  .el-table--scrollable-x .el-table__body-wrapper {
    overflow-x: hidden;
  }
  .tree-block {
    border-right: 1px solid #ddd;
    .type_name {
      display: inline-block;
      width: 60px;
      text-align: right;
    }
    .dataOriginList {
      height: 150px;
      overflow-y: scroll;
      .el-tag {
        display: block;
        width: 184px;
        margin: 4px 0;
        text-align: center;
        cursor: pointer;
      }
    }
  }
  .tree-content {
    padding: 0 10px;
  }
  .el-input {
    width: 120px !important;
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
    width: 130px !important;
    .el-input__inner {
      width: 130px;
    }
  }
  .el-table td,
  .el-table .el-table th {
    padding: 6px 0;
  }
  .el-table__body {
    width: 100% !important;
  }
}
</style>

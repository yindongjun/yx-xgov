<template>
  <section class="main-data-detail">
    <div class="main-data-left">
      <div class="tree-menu">
        <el-input
          placeholder="搜索"
          class="search-input mb10"
          size="small"
          v-model="treeName"
        >
          <i slot="suffix" class="el-input__icon el-icon-search searchBtn"></i>
        </el-input>
        <div class="tree-c mb10">
          <el-tooltip
            class="item"
            effect="dark"
            content="添加表"
            placement="top"
          >
            <span class="tree-t-set" @click="addFile"
              ><svg-icon iconClass="g-添加"></svg-icon
            ></span>
          </el-tooltip>
        </div>
        <div class="tree-b">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :render-content="renderContent"
            :filter-node-method="filterNode"
            ref="tree2"
          >
          </el-tree>
        </div>
      </div>
    </div>
    <div class="main-data-right">
      <!-- <el-row class="main-data-right-t">
        <el-input
          v-model="fieldName"
          placeholder="选择字段名查询"
          style="width: 197px"
          size="small"
        ></el-input>
        <el-input
          v-model="dataContent"
          placeholder="请输入数据内容进行查询"
          style="width: 330px"
          size="small"
        >
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
        <el-popover placement="bottom" width="400" trigger="click">
          <p style="margin: 0">
            <span class="fl" style="line-height: 38px;font-size: 13px"
              >添加搜索条件</span
            ><el-button
              class="fr"
              @click="addCondition"
              type="text"
              style="color: #F06273"
              >新增</el-button
            >
          </p>
          <el-table :data="tableData3" class="super-search-table">
            <el-table-column label="字段名称">
              <template slot-scope="scope">
                <el-select v-model="scope.row.fieldName" size="mini">
                  <el-option value="1" label="1"></el-option>
                  <el-option value="2" label="2"></el-option>
                  <el-option value="3" label="3"></el-option>
                  <el-option value="4" label="4"></el-option>
                  <el-option value="5" label="5"></el-option>
                  <el-option value="6" label="6"></el-option>
                  <el-option value="7" label="7"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="符号">
              <template slot-scope="scope">
                <el-select v-model="scope.row.symbol" size="mini">
                  <el-option value="1" label=">"></el-option>
                  <el-option value="2" label="<"></el-option>
                  <el-option value="3" label="="></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="条件">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.date"></el-input>
              </template>
            </el-table-column>
            <el-table-column width="50" label="操作">
              <template slot-scope="scope">
                <el-button
                  @click="deleteRow(scope.$index)"
                  style="color: #86939E"
                  type="text"
                  ><svg-icon iconClass="删除"></svg-icon
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-row class="ar mt10">
            <el-button type="info" size="mini">取消</el-button>
            <el-button type="danger" size="mini">保存</el-button>
          </el-row>
          <el-button type="text" slot="reference" class="superSearch"
            >高级搜索</el-button
          >
        </el-popover>

        <el-popover
          placement="top-start"
          width="200"
          trigger="hover"
          content="数据区域只展示部分样例数据，详细数据查询请使用高级搜索"
        >
          <i
            style="font-size: 14px;margin-left: 10px;cursor: pointer"
            slot="reference"
            ><svg-icon iconClass="help"></svg-icon
          ></i>
        </el-popover>
      </el-row> -->
      <el-row class="main-data-right-b">
        <el-table
          class="tb-edit"
          highlight-current-row
          :data="tableData2"
          height="100%"
          style="width: 100%"
        >
          <template v-for="(col, index) in cols">
            <el-table-column
              :key="index"
              :show-overflow-tooltip="true"
              :prop="col.prop"
              :label="col.label"
            ></el-table-column>
          </template>
        </el-table>
      </el-row>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      title="新增"
      :visible.sync="dialogPointVisible"
      class="add-table-dialog"
      @before-close="handleClose"
    >
      <div style="text-align: center">
        <el-transfer
          filterable
          filter-placeholder="请输入表名称"
          v-model="pointValue"
          :titles="['待选表', '已选表']"
          :data="pointData"
          @change="pointChange"
          style="text-align: left; display: inline-block"
        >
        </el-transfer>
      </div>
    </el-dialog>
  </section>
</template>

<script type="text/jsx">
import ElButton from "../../../../../node_modules/element-ui/packages/button/src/button.vue";
import ElRow from "element-ui/packages/row/src/row";
import SvgIcon from "../../../../components/SvgIcon.vue";

export default {
  name: "",
  components: {
    SvgIcon,
    ElRow,
    ElButton
  },
  data() {
    return {
      isSearch: false,
      currId: "",
      treeName: "",
      fieldName: "",
      dataContent: "",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "tableName"
      },
      tableData2: [],
      tableData3: [],
      filterDara: [],
      totalData: 0,
      cols: [],

      dialogPointVisible: false,
      pointValue: [],
      pointData: []
    };
  },
  methods: {
    addFile() {
      let that = this;
      this.dialogPointVisible = true;
      this.$urlApi.propertyManage
        .getAllTables({ mainDataId: this.currId })
        .then(res => {
          res.data.forEach(item => {
            item.label = item.name;
            item.key = item.name;
          });
          this.pointData = res.data;
        });
    },
    handleClose() {
      this.dialogPointVisible = false;
    },
    getTreeData() {
      this.$urlApi.propertyManage
        .getMainTables({ mainDataId: this.currId })
        .then(res => {
          this.treeData = res.data;
        });
    },
    handleNodeClick(data) {
      this.$urlApi.propertyManage
        .getFields({ id: data.id })
        .then(res => {
          let heads = [];
          for (let item in res.data) {
            heads.push({
              label: res.data[item].field_name,
              prop: res.data[item].field_name
            });
          }
          this.cols = heads;
        })
        .then(res => {
          this.$urlApi.propertyManage.getShowData({ id: data.id }).then(res => {
            this.tableData2 = res.data;
            this.filterDara = this.tableData2.filter(
              (u, index) => index < 10 * 1 && index >= 10 * (1 - 1)
            );
            this.totalData = res.rowCount;
          });
        });
    },
    handleCurrentChange(cb) {
      this.filterDara = this.tableData2.filter(
        (u, index) => index < 10 * cb && index >= 10 * (cb - 1)
      );
    },
    renderContent(h, { node, data, store }) {
      let nodeItem = (
        <span class="nodeItem">
          <span>
            <span class="node-label">
              <span class="node-icon">
                <svg-icon iconClass="文件夹" />
              </span>
              {node.label}
            </span>
            <span class="nodeOptDetail">
              <el-tooltip
                class="item"
                effect="dark"
                content="删除"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.deleteSource(data, node, h)}
                >
                  <svg-icon iconClass="删除" />
                </el-button>
              </el-tooltip>
            </span>
          </span>
        </span>
      );
      return nodeItem;
    },
    addCondition() {
      this.tableData3.push({
        date: "",
        name: "",
        address: "",
        fieldName: "",
        symbol: ""
      });
    },
    pointChange(a, b, c) {
      let params = {
        tableName: c.join(","),
        mainDataId: this.currId
      };
      if (b === "left") {
      } else if (b === "right") {
        this.$urlApi.propertyManage.addTables(params).then(res => {
          this.$toast("success", "新建表成功");
          this.getTreeData();
          this.dialogPointVisible = false;
          this.pointValue = [];
          this.pointData = [];
        });
      }
    },
    deleteSource(data, node, h) {
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.propertyManage
            .deleteTables({ ids: data.id })
            .then(res => {
              this.$toast("success", "删除成功");
              this.getTreeData();
            });
        })
        .catch(() => {});
    },
    deleteRow(num) {
      this.tableData3.splice(num, 1);
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.tableName.indexOf(value) !== -1;
    }
  },
  mounted: function() {
    this.currId = this.$route.query.id;
    this.getTreeData();
  },
  watch: {
    treeName(val) {
      this.$refs.tree2.filter(val);
    }
  }
};
</script>

<style lang="scss">
.main-data-detail {
  height: 100%;
  display: flex;
  min-width: 0;
  .add-table-dialog {
    .el-transfer {
      font-size: 12px;
      .el-checkbox {
        .el-checkbox__label {
          font-size: 12px;
        }
      }
      .el-transfer__button:first-child {
        display: none;
      }
    }
  }
  .main-data-left {
    width: 254px;
    padding-right: 15px;
    height: 100%;
    .tree-menu {
      height: 100%;
      background: #ffffff;
      padding: 14px 8px;
      box-sizing: border-box;
      .searchBtn {
        color: #797b7b;
        cursor: pointer;
      }
      .searchBtn:hover {
        color: #fb99a0;
      }
      .tree-c {
        display: flex;
        justify-content: flex-start;
        .tree-t-set {
          width: 28px;
          height: 26px;
          display: block;
          border-radius: 2px;
          border: 1px solid #e1e5ec;
          margin-right: 7px;
          cursor: pointer;
          background: #ffffff;
          font-size: 14px;
          line-height: 26px;
          text-align: center;
        }
      }
      .tree-b {
        height: -moz-calc(100% - 72px);
        height: -webkit-calc(100% - 72px);
        height: calc(100% - 72px);
        border-right: 1px solid rgba(151, 151, 151, 0.26);
        overflow: auto;
        .nodeItem {
          width: 100%;
          position: relative;
          .node-label {
            display: inline-block;
            width: 140px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            .node-icon {
              margin-right: 5px;
              font-size: 14px;
            }
          }
          .node-label:hover {
            display: inline-block;
          }
          .nodeOpt {
            display: none !important;
            float: right;
            position: relative;
            top: -2px;
            font-size: 14px;
            font-weight: bold;
          }
          .nodeOptDetail {
            display: none;
            position: absolute;
            right: 0;
            top: -4px;
            height: 26px;
            line-height: 26px;
            font-size: 14px;
            background: rgba(51, 55, 67, 0.7);
            border-radius: 2px;
            cursor: default;
            padding: 0 8px;
            .el-button {
              color: #ffffff;
            }
          }
        }
        .nodeItem:hover .nodeOptDetail {
          display: inline-block !important;
          text-align: right;
        }
      }
    }
  }
  .main-data-right {
    flex: 1;
    min-width: 0;
    .main-data-right-t {
      .superSearch {
        margin-left: 10px;
        color: #3e4456;
      }
    }
    .main-data-right-b {
      height: -moz-calc(100% - 40px);
      height: -webkit-calc(100% - 40px);
      height: calc(100% - 40px);
    }
  }
}
.super-search-table {
  td,
  th {
    padding: 6px 0;
  }
}
</style>

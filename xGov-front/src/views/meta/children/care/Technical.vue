<template>
  <section class="care-technical">
    <div class="care-technical-left">
      <div class="tree-menu">
        <el-input
          placeholder="请输入技术元数据名称查询"
          class="search-input mb10"
          size="small"
          @keyup.enter.native="searchTreeName"
          v-model="treeName"
        >
          <i
            slot="suffix"
            @click="searchTreeName"
            class="el-input__icon el-icon-search searchBtn"
          ></i>
        </el-input>
        <div class="tree-c mb10">
          <!-- <el-tooltip class="item" effect="dark" content="添加一级目录" placement="top">
                        <span class="tree-t-set" @click="catalogueFormVisible=true"><svg-icon iconClass="文件"></svg-icon></span>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="导入元数据（只能选择目录进行导入）" placement="top">
                        <span class="tree-t-set"><svg-icon iconClass="分享"></svg-icon></span>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="下载导入模板" placement="top">
                        <span class="tree-t-set" @click="downloadVisible=true"><svg-icon iconClass="下载"></svg-icon></span>
                    </el-tooltip> -->
        </div>
        <div class="tree-b" v-loading="searchLoading">
          <vue-scroll>
            <el-tree
              @node-click="handleNodeClick"
              :render-content="renderContent"
              :props="defaultProps"
              highlight-current
              :key="treeKey"
              :data="treeData"
              :expand-on-click-node="false"
              :load="loadNodeTree"
              :default-expand-all="isSearch"
              ref="tree"
              node-key="id"
              :lazy="!isSearch"
            >
            </el-tree>
          </vue-scroll>
        </div>
      </div>
    </div>
    <div class="care-technical-right">
      <div v-if="currNodeId === ''" class="no-data-pic-outer valign">
        <div class="no-data-box">
          <img
            class="no-data-pic"
            src="../../../../assets/images/noDataPic.png"
          />
          <p class="no-data-text">暂无技术元数据</p>
        </div>
      </div>
      <el-tabs
        v-model="activeName"
        @tab-click="handleClick"
        v-if="currNodeId !== ''"
      >
        <el-tab-pane label="基本信息" name="first">
          <basic :basicVisible="basicVisible" :currNodeId="currNodeId"></basic>
        </el-tab-pane>
        <el-tab-pane label="版本管理" name="second">
          <edition
            :EditionVisible="EditionVisible"
            :currNodeId="currNodeId"
          ></edition>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog
      :close-on-click-modal="false"
      title="新建目录"
      :visible.sync="catalogueFormVisible"
      :before-close="closeDialog"
      width="500px"
    >
      <el-form
        :model="catalogueForm"
        ref="ruleCatalogue"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="上级目录">
          <el-input v-model="catalogueForm.prevName" size="medium"></el-input>
        </el-form-item>
        <el-form-item label="目录名称">
          <el-input v-model="catalogueForm.currName" size="medium"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog" type="info" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitCataloguForm('ruleCatalogue')"
          :loading="buttonLoading1"
          >保 存</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      title="下载导入模板"
      :visible.sync="downloadVisible"
      width="500px"
    >
      <el-form
        :model="downloadForm"
        ref="ruleDownload"
        label-width="60px"
        label-position="right"
      >
        <el-form-item label="元模型">
          <el-cascader
            expand-trigger="hover"
            :options="options"
            v-model="downloadForm.name"
            style="width: 100%"
            size="medium"
            @change="DownloadChange"
          >
          </el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="downloadVisible = false" type="info" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitdownloadForm('ruleDownload')"
          :loading="buttonLoading2"
          >保 存</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      title="新增元数据"
      :visible.sync="addUnitVisible"
      width="500px"
    >
      <el-form
        :model="unitForm"
        ref="unitForm"
        size="medium"
        label-width="90px"
        label-position="right"
      >
        <el-form-item label="元模型">
          <el-select v-model="unitForm.model" style="width: 100%">
            <template v-for="item in unitFormOptions">
              <el-option
                :value="item.label"
                :label="item.label"
                :key="item.label"
                v-if="item.type === currUnitType"
              ></el-option>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="元数据名称">
          <el-input v-model="unitForm.name"></el-input>
        </el-form-item>
        <el-form-item label="元数据代码">
          <el-input v-model="unitForm.code"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addUnitVisible = false" type="info" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitUnitForm('unitForm')"
          :loading="buttonLoading3"
          >保 存</el-button
        >
      </span>
    </el-dialog>
  </section>
</template>

<script type="text/jsx">
import Basic from "./business/Basic.vue";
import Edition from "./business/Edition.vue";
import Blood from "./business/Blood.vue";
import Affect from "./business/Affect.vue";
export default {
  name: "",
  components: {
    Basic,
    Edition,
    Blood,
    Affect
  },
  data() {
    return {
      buttonLoading1: false,
      buttonLoading2: false,
      buttonLoading3: false,
      basicVisible: new Date().toTimeString(),
      EditionVisible: new Date().toTimeString(),
      isSearch: false,
      currNodeId: "",
      uniqueCode: "",
      activeName: "first",
      treeName: "",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name",
        id: "id",
        isLeaf: "leaf"
      },
      catalogueFormVisible: false,
      downloadVisible: false,
      addUnitVisible: false,
      currUnitType: null,
      downloadForm: {
        name: []
      },
      catalogueForm: {
        prevName: "",
        currName: ""
      },
      unitForm: {
        model: "",
        name: "",
        code: ""
      },
      unitFormOptions: [
        { label: "表", type: 2 },
        { label: "视图", type: 2 },
        { label: "字段", type: 3 },
        { label: "主键", type: 3 },
        { label: "外键", type: 3 },
        { label: "索引", type: 3 }
      ],
      options: [],
      treeKey: new Date().getTime(),
      searchLoading: false
    };
  },
  methods: {
    searchTreeData() {
      this.searchLoading = true;
      this.$urlApi.metaData
        .careMenuList({ menuType: 1, searchKey: this.treeName })
        .then(res => {
          this.searchLoading = false;
          this.treeData = res.data;
        });
    },
    loadNodeTree(node, resolve) {
      let param = {};
      if (node.level === 0) {
        param = { menuType: 1, searchKey: this.treeName };
        this.searchLoading = true;
        this.$urlApi.metaData.careMenuList(param).then(res => {
          this.searchLoading = false;
          res.data.forEach(et => {
            et.children = [];
          });
          resolve(res.data);
        });
      } else {
        param = { menuId: node.data.id };
        this.$urlApi.metaData.findByMenuId(param).then(res => {
          res.data.forEach(et => {
            et.children = [];
          });
          resolve(res.data);
        });
      }
    },
    closeDialog() {
      this.catalogueForm.currName = "";
      this.catalogueForm.prevName = "";
      this.catalogueFormVisible = false;
    },
    searchTreeName() {
      let that = this;
      setTimeout(() => {
        that.treeKey = new Date().getTime();
        if (that.treeName == "") {
          that.isSearch = false;
        } else {
          that.isSearch = true;
          this.searchTreeData();
        }
      }, 100);
    },
    handleNodeClick(data) {
      if (data.isMenu === "N") {
        this.currNodeId = data.id;
        this.uniqueCode = data.uniqueCode;
        this.basicVisible = new Date().toTimeString();
        this.activeName = "first";
      }
    },
    DownloadChange() {},
    renderContent(h, { node, data, store }) {
      let nodeItem = ( //第一级
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
              <span class="node-icon">
                <svg-icon iconClass="文件夹" />
              </span>
              {node.label}
            </span>
          </span>
        </span>
      );
      let nodeItemContent = ( //第二级
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
              <span class="node-icon">
                <svg-icon iconClass="g-层应用" />
              </span>
              {node.label}
            </span>
            <span class="nodeOptDetail">
              <el-tooltip
                class="item"
                effect="dark"
                content="添加元数据"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.addSource(data, node, h)}
                >
                  <svg-icon iconClass="添加数据源" />
                </el-button>
              </el-tooltip>
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
      let nodeTableContent = (
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
              <span class="node-icon">
                <svg-icon iconClass="表格" />
              </span>
              {node.label}
            </span>
            <span class="nodeOptDetail">
              <el-tooltip
                class="item"
                effect="dark"
                content="添加元数据"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.addSource(data, node, h)}
                >
                  <svg-icon iconClass="添加数据源" />
                </el-button>
              </el-tooltip>
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
      let nodeFieldItem = ( //第一级
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
              <span class="node-icon">
                <span v-show={data.metamodelId === 29}><svg-icon iconClass="metamod8"/></span>
                <span v-show={data.metamodelId === 30}><svg-icon iconClass="metamod9"/></span>
                <span v-show={data.metamodelId === 31}><svg-icon iconClass="metamod10"/></span>
                <span v-show={data.metamodelId === 32}><svg-icon iconClass="metamod11"/></span>
                <span v-show={data.metamodelId === 33}><svg-icon iconClass="metamod12"/></span>
              </span>
              {node.label}
            </span>
          </span>
        </span>
      );
      switch (node.level) {
        case 1:
          return nodeItem;
        case 2:
          return nodeItemContent;
        case 3:
          return nodeTableContent;
        default:
          return nodeFieldItem;
          break;
      }
    },
    addSource(data, node, h) {
      //添加源
      this.unitForm.model = "";
      this.unitForm.name = "";
      this.unitForm.code = "";
      this.currUnitType = node.level;
      this.addUnitVisible = true;
    },
    deleteSource(data, node, h) {
      //删除源
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          // this.$message({
          //     type: 'success',
          //     message: '删除成功'
          // });
          this.$toast("success", "删除成功");
        })
        .catch(() => {});
    },

    submitCataloguForm() {
      //保存
      this.catalogueFormVisible = false;
      this.buttonLoading1 = false;
    },
    submitdownloadForm() {
      this.downloadVisible = false;
      this.buttonLoading2 = false;
    },
    submitUnitForm() {
      this.addUnitVisible = false;
      this.buttonLoading3 = false;
    },
    handleClick: function(tab) {
      switch (tab.name) {
        case "first":
          let tempDate1 = new Date();
          this.basicVisible = tempDate1.toTimeString();
          break;
        case "second":
          let tempDate2 = new Date();
          this.EditionVisible = tempDate2.toTimeString();
          break;
        default:
          break;
      }
    }
  },
  mounted: function() {}
};
</script>

<style lang="scss">
.care-technical {
  height: 100%;
  display: flex;
  min-width: 0;
  .care-technical-left {
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
        color: #3ba3f8;
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
          background: rgba(240, 241, 244, 1);
          font-size: 14px;
          line-height: 26px;
          text-align: center;
        }
      }
      .tree-b {
        height: -moz-calc(100% - 72px);
        height: -webkit-calc(100% - 72px);
        height: calc(100% - 72px);
        overflow: auto;
        .nodeItem {
          width: 100%;
          position: relative;
          .node-label {
            display: inline-block;
            width: 100px;
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
  .care-technical-right {
    flex: 1;
    min-width: 0;
    .no-data-pic-outer {
      height: 100%;
      text-align: center;
      position: relative;
      .no-data-box {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        .no-data-text {
          color: #9da1a9;
          padding: 20px 0;
        }
      }
    }
    .el-tabs {
      height: 100%;
      .el-tabs__content {
        height: -moz-calc(100% - 55px);
        height: -webkit-calc(100% - 55px);
        height: calc(100% - 55px);
        .el-tab-pane {
          height: 100%;
        }
      }
    }
  }
}
</style>

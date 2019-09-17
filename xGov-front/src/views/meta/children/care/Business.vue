<template>
  <section class="care-business">
    <div class="care-business-left">
      <div class="tree-menu">
        <el-input
          placeholder="请输入业务元数据名称查询"
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
        <div class="tree-b" v-loading="searchLoading">
          <vue-scroll>
            <el-tree
              @node-click="handleNodeClick"
              :render-content="renderContent"
              :props="defaultProps"
              :data="treeData"
              :key="treeKey"
              highlight-current
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
    <div class="care-business-right">
      <div v-if="currNodeId === ''" class="no-data-pic-outer valign">
        <div class="no-data-box">
          <img
            class="no-data-pic"
            src="../../../../assets/images/noDataPic.png"
          />
          <p class="no-data-text">暂无业务元数据</p>
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
  </section>
</template>

<script  type="text/jsx">
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
      basicVisible: new Date().toTimeString(),
      EditionVisible: new Date().toTimeString(),
      isSearch: false,
      currNodeId: "",
      uniqueCode: "",
      activeName: "first",
      treeName: "",
      treeData: [],
      treeKey: new Date().getTime(),
      searchLoading: false,
      defaultProps: {
        children: "children",
        label: "name",
        id: "id",
        isLeaf: "leaf"
      }
    };
  },
  methods: {
    searchTreeData() {
      this.searchLoading = true;
      this.$urlApi.metaData
        .careMenuList({ menuType: 0, searchKey: this.treeName })
        .then(res => {
          this.searchLoading = false;
          this.treeData = res.data;
        });
    },
    loadNodeTree(node, resolve) {
      let param = {};
      if (node.level === 0) {
        param = { menuType: 0, searchKey: this.treeName };
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
          </span>
        </span>
      );
      let nodeFieldItem = ( //第一级
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
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
        default:
          return nodeFieldItem;
          break;
      }
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
.care-business {
  height: 100%;
  display: flex;
  min-width: 0;
  .care-business-left {
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
      .tree-b {
        height: -moz-calc(100% - 42px);
        height: -webkit-calc(100% - 42px);
        height: calc(100% - 42px);
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
          .nodeOpt {
            display: none !important;
            float: right;
            position: relative;
            top: -2px;
            font-size: 14px;
            font-weight: bold;
          }
        }
      }
    }
  }
  .care-business-right {
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

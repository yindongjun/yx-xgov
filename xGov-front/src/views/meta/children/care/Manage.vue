<template>
  <section class="care-manage">
    <div class="care-manage-left">
      <div class="tree-menu">
        <el-input
          placeholder="搜索..."
          class="search-input mb10"
          size="small"
          v-model="treeName"
        >
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
        <div class="tree-b">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :render-content="renderContent"
          >
          </el-tree>
        </div>
      </div>
    </div>
    <div class="care-manage-right">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="基本信息" name="first">
          <basic></basic>
        </el-tab-pane>
        <el-tab-pane label="版本管理" name="second">
          <edition></edition>
        </el-tab-pane>
        <el-tab-pane label="血统分析" name="third">
          <blood></blood>
        </el-tab-pane>
        <el-tab-pane label="影响分析" name="fourth">
          <affect></affect>
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
      activeName: "first",
      treeName: "",
      treeData: [
        {
          label: "关系型数据库",
          children: [
            {
              label: "二级 2-1-1",
              children: [
                {
                  label: "三级 1-1-1"
                }
              ]
            },
            {
              label: "二级 2-2-1",
              children: [
                {
                  label: "三级 1-1-2"
                }
              ]
            }
          ]
        },
        {
          label: "大数据",
          children: [
            {
              label: "二级 2-1",
              children: [
                {
                  label: "三级 2-1-1"
                }
              ]
            },
            {
              label: "二级 2-2",
              children: [
                {
                  label: "三级 2-2-1"
                }
              ]
            }
          ]
        }
      ],
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  methods: {
    handleNodeClick(data) {},
    renderContent(h, { node, data, store }) {
      let nodeItem = ( //第一级
        <span class="nodeItem">
          <span>
            <span class="node-label">
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
            <span class="node-label">
              <span class="node-icon">
                <svg-icon iconClass="g-层应用" />
              </span>
              {node.label}
            </span>
          </span>
        </span>
      );
      let nodeTableContent = (
        <span class="nodeItem">
          <span>
            <span class="node-label">
              <span class="node-icon">
                <svg-icon iconClass="表格" />
              </span>
              {node.label}
            </span>
          </span>
        </span>
      );
      let nodeFieldItem = ( //第一级
        <span class="nodeItem">
          <span>
            <span class="node-label">{node.label}</span>
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
    handleClick() {}
  },
  mounted: function() {}
};
</script>

<style lang="scss">
.care-manage {
  height: 100%;
  display: flex;
  min-width: 0;
  .care-manage-left {
    width: 215px;
    padding-right: 15px;
    height: 100%;
    .tree-menu {
      height: 100%;
      background: rgba(245, 247, 250, 1);
      padding: 15px 17px;
      box-sizing: border-box;
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
  .care-manage-right {
    flex: 1;
    min-width: 0;
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

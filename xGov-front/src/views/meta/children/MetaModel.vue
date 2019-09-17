<template>
  <section class="meta-model main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner meta-model-inner">
      <div class="tree-menu">
        <el-input
          placeholder="请输入元模型数据名称查询"
          class="search-input mb10"
          size="small"
          v-model="treeName"
        >
          <i slot="suffix" class="el-input__icon el-icon-search searchBtn"></i>
        </el-input>
        <div class="tree-b">
          <el-tree
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            default-expand-all
            highlight-current
            :filter-node-method="filterNode"
            node-key="id"
            ref="tree2"
          >
          </el-tree>
        </div>
      </div>
      <div class="model-content">
        <div class="model-content-inner">
          <div class="model-title">
            <span class="model-title-l">元模型</span>
          </div>
          <div v-if="modelItem.length === 0" class="no-data-pic-outer valign">
            <div class="no-data-box">
              <img
                class="no-data-pic"
                src="../../../assets/images/noDataPic.png"
              />
              <p class="no-data-text">暂无数据</p>
            </div>
          </div>
          <div v-if="modelItem.length !== 0">
            <ul class="model-content-t mt15">
              <li
                :key="item.id"
                :class="item.active ? 'active' : ''"
                @click="selectModel(item.id, index)"
                v-for="(item, index) in modelItem"
              >
                <i><svg-icon :iconClass="item.icoId"></svg-icon></i><br />
                <span>{{ item.name }}</span>
              </li>
              <div class="model-to-l model-to">
                <i class="el-icon-arrow-left"></i>
              </div>
              <div class="model-to-r model-to">
                <i class="el-icon-arrow-right"></i>
              </div>
            </ul>
            <div class="model-content-b">
              <el-tabs
                v-model="activeName"
                type="card"
                :before-leave="beforeLeave"
              >
                <el-tab-pane label="基本信息" name="first">
                  <basic-msg
                    :BasicMsgTab="BasicMsgTab"
                    :BasicMsgTabTime="BasicMsgTabTime"
                  ></basic-msg>
                </el-tab-pane>
                <el-tab-pane label="继承关系" name="second">
                  <inherit-relation></inherit-relation>
                </el-tab-pane>
                <el-tab-pane label="组合关系" name="third">
                  <group-relation
                    :BasicMsgTab="BasicMsgTab"
                    :GroupRelationTabTime="GroupRelationTabTime"
                  ></group-relation>
                </el-tab-pane>
                <el-tab-pane label="依赖关系" name="four">
                  <depend-relation
                    :BasicMsgTab="BasicMsgTab"
                    :DependRelationTabTime="DependRelationTabTime"
                  ></depend-relation>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </div>
      </div>
    </el-row>
  </section>
</template>

<script>
import BasicMsg from "./model/BasicMsg.vue";
import DependRelation from "./model/DependRelation.vue";
import GroupRelation from "./model/GroupRelation.vue";
import InheritRelation from "./model/InheritRelation.vue";
export default {
  name: "",
  components: { BasicMsg, DependRelation, GroupRelation, InheritRelation },
  data() {
    return {
      BasicMsgTab: "",
      BasicMsgTabTime: "",
      GroupRelationTabTime: "",
      DependRelationTabTime: "",
      treeName: "",
      activeName: "first",
      modelItem: [],
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      }
    };
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    selectModel(id, index) {
      this.modelItem.forEach((item, num) => {
        if (num === index) {
          item.active = true;
        } else {
          item.active = false;
        }
      });
      this.BasicMsgTab = this.modelItem[index];
      this.activeName = "first";
      this.BasicMsgTabTime = new Date().getTime();
    },
    getLayerMenu() {
      this.$urlApi.metaData.getMetamodelMenu().then(res => {
        this.treeData = res.data;
      });
    },
    beforeLeave(curr, oldVal) {
      let tempDate = new Date().getTime();
      if (curr === "first") {
        this.BasicMsgTabTime = tempDate;
      } else if (curr === "third") {
        this.GroupRelationTabTime = tempDate;
      } else if (curr === "four") {
        this.DependRelationTabTime = tempDate;
      }
    },
    handleNodeClick(data, obj, node) {
      if (data.children.length === 0) {
        this.$urlApi.metaData.getListMetaModel({ id: data.id }).then(res => {
          res.data.forEach((item, num) => {
            if (num === 0) {
              item.active = true;
            } else {
              item.active = false;
            }
          });
          this.modelItem = res.data;
          this.selectModel(this.modelItem[0], 0);
        });
      }
    }
  },
  mounted: function() {
    this.$urlApi.metaData.getMetamodelMenu().then(res => {
      this.treeData = res.data;
      const a = [];
      const clickOneNode = array => {
        array.map(c => {
          if (c.children.length > 0) {
            clickOneNode(c.children);
          } else {
            a.push(c);
          }
        });
        return a;
      };
      this.handleNodeClick(clickOneNode(res.data)[0]);
      this.$nextTick(() => {
        this.$refs.tree2.setCurrentKey(clickOneNode(res.data)[0].id);
      });
    });
  },
  watch: {
    treeName(val) {
      this.$refs.tree2.filter(val);
    }
  }
};
</script>

<style lang="scss">
.meta-model {
  min-height: 100%;
  .meta-model-inner {
    display: flex;
    min-width: 0;
    min-height: 100%;
    height: auto;
    .tree-menu {
      background: #ffffff;
      padding: 14px 8px;
      width: 254px;
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
      }
    }
    .model-content {
      flex: 1;
      min-width: 0;
      padding-left: 15px;
      box-sizing: border-box;
      .model-content-inner {
        height: 100%;
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
        .model-title {
          height: 36px;
          border-bottom: 2px solid #e1e5ec;
          .model-title-l {
            font-size: 13px;
            color: #3e4456;
            font-weight: 600;
            line-height: 36px;
          }
        }
        .model-content-t {
          display: flex;
          justify-content: flex-start;
          padding: 60px 30px;
          position: relative;
          .model-to {
            position: absolute;
            width: 22px;
            font-size: 22px;
            cursor: pointer;
            font-weight: 600;
          }
          .model-to-l {
            left: 0;
            top: 85px;
          }
          .model-to-r {
            right: 0;
            top: 85px;
          }
          li {
            height: 78px;
            width: 78px;
            border: 1px solid #ACB5C3;
            border-radius: 50%;
            text-align: center;
            padding-top: 15px;
            box-sizing: border-box;
            margin-right: 24px;
            cursor: pointer;
            i {
              font-size: 23px;
            }
            span {
              margin-top: 8px;
            }
            &:hover {
              background-color: #647094;
              border-color: #647094;
              color: #ffffff;
            }
          }
          .active {
            background-color: #647094;
            border-color: #647094;
            color: #ffffff;
          }
        }
      }
    }
  }
}
</style>


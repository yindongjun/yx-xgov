<template>
  <section class="data-manage main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner-allSize">
      <div class="main-data-left">
        <div class="tree-menu">
          <el-input
            placeholder="请输入目录名称"
            class="search-input mb10"
            size="mini"
            v-model="treeName"
          >
            <i
              slot="suffix"
              class="el-input__icon el-icon-search searchBtn"
            ></i>
          </el-input>
          <div class="tree-b">
            <el-button style="margin-bottom: 10px;" type="primary" size="mini" @click="addNewLayer(-1)">新建分层</el-button>
            <vue-scroll>
              <el-tree
                :data="treeData"
                :props="defaultProps"
                @node-click="handleNodeClick"
                :highlight-current="true"
                default-expand-all
                :expand-on-click-node="false"
                :render-content="renderContent"
                :filter-node-method="filterNode"
                ref="tree2"
                node-key="id"
              >
              </el-tree>
            </vue-scroll>
          </div>
        </div>
      </div>
      <div class="main-data-right">
        <!-- <el-row class="main-data-right-t">
          <el-select
            v-model="dataSourceType"
            size="mini"
            placeholder="请选择数据库类型"
            @change="dataSelect"
            style="margin-right: 5px"
          >
            <el-option value="" label="全部"></el-option>
            <el-option value="MySQL" label="MySQL"></el-option>
            <el-option value="Oracle" label="Oracle"></el-option>
            <el-option value="SqlServer" label="SqlServer"></el-option>
            <el-option value="DB2" label="DB2"></el-option>
            <el-option value="PostgreSQL" label="PostgreSQL"></el-option>
            <el-option value="Sybase" label="Sybase"></el-option>
            <el-option value="Teradata" label="Teradata"></el-option>
            <el-option value="HDFS" label="HDFS"></el-option>
            <el-option value="Hive" label="Hive"></el-option>
            <el-option value="Hbase" label="Hbase"></el-option>
            <el-option value="Impala" label="Impala"></el-option>
          </el-select>
          <el-input
            v-model="dataSourceName"
            placeholder="请输入数据源名称"
            @keyup.enter.native="saveList"
            style="width: 300px"
            size="mini"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
          <el-button type="danger" size="mini" @click="saveList"
            >查询</el-button
          >
        </el-row> -->
        <el-row class="mt20">
          <el-button type="danger" size="small" @click="relationDatasource">
            关联数据源
          </el-button>
        </el-row>
        <el-row class="main-data-right-b">
          <div v-if="listAction.length === 0" class="no-data-pic-outer valign">
            <div class="no-data-box">
              <img
                class="no-data-pic"
                src="../../../assets/images/noDataPic.png"
              />
              <p class="no-data-text">
                暂无数据，开始<span class="redText" @click="relationDatasource"> 关联数据源</span>
              </p>
            </div>
          </div>
          <vue-scroll>
            <ul class="analyze-b">
              <li :key="item.id" v-for="item in listAction">
                <p :title="item.databaseType" @click="showDatasource(item)" class="check-header">{{ item.datasourceName }}</p>
                <div class="check-body over" :title="item.databaseType">
                  数据源类型：{{ item.databaseType }}
                </div>
                <div class="check-body over" :title="item.sourceAddress">
                  服务地址：{{ item.sourceAddress }}
                </div>
                <div class="check-set">
                  <!-- <span @click="setAccredit"
                    ><svg-icon iconClass="nav6-4"></svg-icon
                  ></span>
                  <span @click="editData(item)"
                    ><svg-icon iconClass="编辑1"></svg-icon
                  ></span> -->
                  <span @click="deleteData(item.id)"
                    ><svg-icon iconClass="删除"></svg-icon
                  ></span>
                </div>
              </li>
            </ul>
          </vue-scroll>
          <el-pagination
            class="ar"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="12"
            layout="total, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </el-row>
      </div>
    </el-row>
    <el-dialog
      :close-on-click-modal="false"
      width="500px"
      :title="addLayerForm.id === '' ? '新建下级目录' : '编辑目录'"
      :visible.sync="addLayerVisible"
      :before-close="closeModal"
      append-to-body
    >
      <el-form
        :model="addLayerForm"
        ref="addLayerForm"
        :rules="rules"
        size="small"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="上级目录">
          <label style="font-size: 12px;">{{ parentContents }}</label>
        </el-form-item>
        <el-form-item label="目录名称" prop="name">
          <el-input
            v-model="addLayerForm.name"
            maxlength="20"
            placeholder="最大长度为20个字符"
          ></el-input>
        </el-form-item>
        <el-form-item label="目录描述" prop="remark">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入内容"
            v-model="addLayerForm.remark"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="closeModal"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitLayer('addLayerForm')"
          :loading="buttonLoading"
          >保 存</el-button
        >
      </span>
    </el-dialog>
    <user-accredit
      :UserAccreditVisible="UserAccreditVisible"
      @UserAccreditBack="UserAccreditBack"
    ></user-accredit>
    <relation-base
      :RelationBaseVisible="RelationBaseVisible"
      :isEditRelation="isEditRelation"
      @RelationBaseBack="RelationBaseBack"
    ></relation-base>
    <no-relation-base
      :NoRelationBaseVisible="NoRelationBaseVisible"
      @NoRelationBaseBack="NoRelationBaseBack"
      :isEditNoRelation="isEditNoRelation"
    ></no-relation-base>
    <RelationSource
      :dialog="dialog"
      @relationSource="relationSource"
      ref="relationSource"
    ></RelationSource>
    <el-dialog
      title="数据源详情"
      :visible.sync="dialog1.visible"
      :before-close="closeDialog"
      :lock-scroll="false"
      width="50rem"
      @open="openDialog"
      :close-on-click-modal="false">
      <ViewDataSource ref="viewDataSource"></ViewDataSource>
    </el-dialog>
  </section>
</template>

<script type="text/jsx">
import UserAccredit from "./dataMange/UserAccredit.vue";
import RelationBase from "./dataMange/RelationBase.vue";
import NoRelationBase from "./dataMange/NoRelationBase.vue";
import RelationSource from "./dataMange/RelationSource.vue";
import ViewDataSource from './../../dataQuality/children/components/ViewDataSource'
export default {
  name: "",
  components: { UserAccredit, RelationBase, NoRelationBase, RelationSource,
    ViewDataSource},
  data() {
    return {
      buttonLoading: false,
      total: 0,
      currentPage: 1,
      pageLength: 12,
      listAction: [],
      parentContents: "",
      isEditRelation: {
        currTime: new Date().getTime(),
        id: ""
      },
      isEditNoRelation: {
        currTime: new Date().getTime(),
        id: ""
      },
      addLayerVisible: false, //新建分层dialog
      RelationBaseVisible: false, //关系型dialog
      NoRelationBaseVisible: false, //非关系型dialog
      UserAccreditVisible: false, //用户授权dialog
      treeName: "",
      dataSourceName: "", //数据源名称
      dataSourceType: "", //数据源类型
      addLayerForm: {
        id: "",
        name: "",
        parentId: -1,
        remark: ""
      },
      rules: {
        name: [{ required: true, message: "请填写分层名称", trigger: "blur" }],
        remark: [
          // { required: true, message: "请填写描述", trigger: "blur" },
          // { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
        ]
      },
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      layerId: "",
      showTip: false,
      dialog: {
        visible: false,
        data: {}
      },
      currentLayer: '',
      dialog1: {
        visible: false
      }
    };
  },
  methods: {
    getList() {
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        layerId: this.layerId
      };
      this.$urlApi.dataSource.getSourceByLayerId(param).then(res => {
        this.listAction = Array.isArray(res.data) ? res.data : [];
        this.total = res.recordsFiltered;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    },
    getLayerMenu() {
      this.$urlApi.dataSource.getLayers().then(res => {
        this.treeData = res.data;
      });
    },
    dataSelect(cb) {
      this.dataSourceType = cb;
      this.getList();
    },
    saveList() {
      this.getList();
    },
    handleSizeChange(cb) {
      this.this.pageLength = cb;
      this.currentPage = 1;
      this.getList();
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getList();
    },
    editData(item) {
      let tempDate = new Date().getTime();
      if (
        item.databaseType != "HDFS" &&
        item.databaseType != "Hive" &&
        item.databaseType != "Hbase" &&
        item.databaseType != "Impala"
      ) {
        this.RelationBaseVisible = true;
        this.isEditRelation.id = item.id;
        this.isEditRelation.currTime = tempDate;
      } else {
        this.NoRelationBaseVisible = true;
        this.isEditNoRelation.id = item.id;
        this.isEditNoRelation.currTime = tempDate;
      }
    },
    deleteData(id) {
      this.$confirm("确定删除？", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          const params = {
            layerId: this.layerId,
            sourceId: id
          }
          this.$urlApi.dataSource.removeSourceFromLayer({ ...params }).then(res => {
            this.getList();
            this.$toast("success", "删除成功");
          });
        })
        .catch(() => {});
    },
    handleNodeClick(data) {
      this.currentLayer = data;
      this.layerId = data.id;
      this.getList();
    },
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
            <span class="nodeOptDetail">
              <el-tooltip
                class="item"
                effect="dark"
                content="新建下级目录"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.addNewLayer(data, node, h)}
                >
                  <svg-icon iconClass="g-添加" />
                </el-button>
              </el-tooltip>
              <el-tooltip
                class="item"
                effect="dark"
                content="编辑"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.editLayer(data, node, h)}
                >
                  <svg-icon iconClass="编辑1" />
                </el-button>
              </el-tooltip>
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
            <span class="nodeOptDetail">
              <el-tooltip
                className="item"
                effect="dark"
                content="新建下级目录"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.addNewLayer(data, node, h)}
                >
                  <svg-icon iconClass="g-添加" />
                </el-button>
              </el-tooltip>
              <el-tooltip
                class="item"
                effect="dark"
                content="编辑"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.editLayer(data, node, h)}
                >
                  <svg-icon iconClass="编辑1" />
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
                  on-click={() => this.deleteLayer(data, node, h)}
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
            <span class="node-label">{node.label}</span>
            <span class="nodeOptDetail">
              <el-tooltip
                className="item"
                effect="dark"
                content="编辑"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.editLayer(data, node, h)}
                >
                  <svg-icon iconClass="编辑1" />
                </el-button>
              </el-tooltip>
              <el-tooltip
                className="item"
                effect="dark"
                content="删除"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.deleteLayer(data, node, h)}
                >
                  <svg-icon iconClass="删除" />
                </el-button>
              </el-tooltip>
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
          return nodeItemContent;
        default:
          return nodeFieldItem;
          break;
      }
    },
    handleCommand(cb) {
      //选择数据源
      let tempDate = new Date().getTime();
      if (cb === "a") {
        this.RelationBaseVisible = true;
        this.isEditRelation.currTime = tempDate;
      } else {
        this.NoRelationBaseVisible = true;
        this.isEditNoRelation.currTime = tempDate;
      }
    },
    RelationBaseBack(cb) {
      //关系型回调
      if (cb === 1) {
        this.getList();
        !this.isEditRelation.id && (this.showTip = true);
        setTimeout(() => this.closeTip(), 3000);
      }
      this.RelationBaseVisible = false;
      this.isEditRelation.id = "";
    },
    NoRelationBaseBack(cb) {
      //非关系型回调
      if (cb === 1) {
        this.getList();
        !this.isEditNoRelation.id && (this.showTip = true);
        setTimeout(() => this.closeTip(), 3000);
      }
      this.NoRelationBaseVisible = false;
      this.isEditNoRelation.id = "";
    },
    setAccredit() {
      //设置权限
      this.UserAccreditVisible = true;
    },
    UserAccreditBack() {
      //权限回调
      this.UserAccreditVisible = false;
    },
    addNewLayer(a, b, c) {
      this.addLayerVisible = true;
      this.parentContents = a.name;
      if (a != -1) {
        this.addLayerForm.parentId = a.id;
      }
    },
    closeModal() {
      this.$refs["addLayerForm"].resetFields();
      this.addLayerForm.id = "";
      this.addLayerForm.name = "";
      this.addLayerForm.parentId = -1;
      this.addLayerForm.remark = "";
      this.addLayerVisible = false;
    },
    editLayer(data, node, h) {
      //添加源
      this.addLayerForm.name = data.name;
      this.addLayerForm.remark = data.remark;
      this.addLayerForm.parentId = data.parentId;
      this.addLayerForm.id = data.id;
      this.addLayerVisible = true;
      this.parentContents = node.parent ? node.parent.data.name : "";
    },
    submitLayer(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let params = {}
          if (this.addLayerForm.id) {
            params = { ...this.addLayerForm }
          } else {
            delete this.addLayerForm.id
            params = { ...this.addLayerForm }
          }
          this.buttonLoading = true;
          this.$urlApi.dataSource
            .saveOrUpdateLayer(params)
            .then(res => {
              this.buttonLoading = false;
              this.getLayerMenu();
              this.addLayerVisible = false;
              this.$toast("success", "操作成功");
              this.closeModal();
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    deleteLayer(data, node, h) {
      //删除源
      this.$confirm("会将分层和分层下关联的数据源删除，是否删除？", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataSource.deleteLayer({ layerId: data.id }).then(res => {
            this.$toast("success", "删除成功");
            this.getLayerMenu();
          });
        })
        .catch(() => {});
    },
    closeTip() {
      this.showTip = false;
    },
    createSource() {
      this.$refs.createSource.handleClick();
    },
    createDataCollect() {
      this.$router.push('/metaData/default/collcet')
    },
    createDataStandard() {
      this.$router.push({
        path: '/standard/default/meta',
        query: {type: 'add'}
      })
    },
    relationDatasource () {
      const _this = this
      this.dialog = {
        visible: true,
        data: {
          layerId: _this.layerId,
          currentLayer: ''
        }
      }
    },
    relationSource (cb) {
      this.dialog = {
        visible: false,
        data: {}
      }
      if (cb) {
        this.handleNodeClick(this.currentLayer)
      }
    },
    showDatasource (data) {
      this.dialog1.visible = true
      this.dialog1.data = data
    },
    openDialog () {
      this.$nextTick(() => {
        this.$refs.viewDataSource.init(this.dialog1.data)
      })
    },
    closeDialog () {
      this.dialog1.visible = false
      this.dialog1.data = ''
    }
  },
  mounted: function () {
    this.$urlApi.dataSource.getLayers().then(res => {
      this.treeData = res.data;
      this.$nextTick(() => {
        this.handleNodeClick(res.data[0])
        this.$refs.tree2.setCurrentKey(res.data[0].id)
      })
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
.data-manage {
  .main-inner-allSize {
    height: 100%;
    display: flex;
    min-width: 0;
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
          color: #3ba3f8;
        }
        .newCatalog {
          color: #3ba3f8;
          font-size: 13px;
          cursor: pointer;
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
          /*overflow: auto;*/
          border-right: 1px solid rgba(151, 151, 151, 0.26);
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
    .main-data-right {
      flex: 1;
      min-width: 0;
      .create-data-source-tip {
        padding: 8px 0;
        background-color: #f0f7ff;
        color: #5e6977;
        font-size: 12px;
        text-align: center;
        float: right;
        width: calc(100% - 150px);
      }
      .main-data-right-t {
        .superSearch {
          margin-left: 10px;
          color: #3e4456;
        }
      }
      .main-data-right-b {
        padding: 15px 0;
        height: -moz-calc(100% - 95px);
        height: -webkit-calc(100% - 95px);
        height: calc(100% - 95px);
        /* overflow: auto;*/
        .no-data-pic-outer {
          height: 100%;
          text-align: center;
          position: relative;
          width: 100%;
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
        .analyze-b {
          display: flex;
          justify-content: flex-start;
          flex-wrap: wrap;
          li {
            margin-right: 60px;
            overflow: hidden;
            margin-bottom: 30px;
            border: 1px solid #e1e5ec;
            text-align: center;
            width: 187px;
            box-shadow: 0px 1px 4px 0px rgba(215, 215, 215, 0.5);
            border-radius: 2px;
            position: relative;
            transition: linear 2s; /* Safari and Chrome */
            //height: 142px;
            .check-header {
              line-height: 32px;
              background: #acb5c3;
              color: #ffffff;
              margin: 0;
              cursor: pointer;
            }
            .check-body {
              background: rgba(249, 249, 250, 1);
              padding: 10px;
              padding-bottom: 5px;
              text-align: left;
              box-sizing: border-box;
              span {
                line-height: 28px;
                font-size: 16px;
                font-weight: bold;
              }
            }
            .check-footer {
              line-height: 32px;
              border-top: 1px solid #e1e5ec;
              height: 32px;
            }
            .check-set {
              position: absolute;
              width: 100%;
              height: 32px;
              background: rgba(51, 55, 67, 0.7);
              bottom: -32px;
              left: 0;
              cursor: pointer;
              box-sizing: border-box;
              transition: 0.5s;
              display: flex;
              justify-content: center;
              span {
                width: 45px;
                display: inline-block;
                height: 100%;
                font-size: 16px;
                line-height: 32px;
                text-align: center;
                color: #ffffff;
                &:hover {
                  background: rgba(255, 255, 255, 0.27);
                }
              }
            }
            &:hover {
              .check-set {
                bottom: 0;
              }
            }
          }
        }
      }
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

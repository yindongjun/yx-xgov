<template>
  <section class="data-collect-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner-allSize">
      <el-col class="pt10 care-technical-left">
        <div class="tree-menu">
          <div class="tree-c mb10">
            <el-tooltip
              class="item"
              effect="dark"
              content="新建"
              placement="top"
            >
              <span class="tree-t-set" @click="addNewLayer(-1)"
                ><svg-icon iconClass="增加"></svg-icon
              ></span>
            </el-tooltip>
          </div>
          <div class="tree-b">
            <vue-scroll>
              <el-tree
                @node-click="handleNodeClick"
                :render-content="renderContent"
                :props="defaultProps"
                highlight-current
                :indent="12"
                :key="treeKey"
                :load="loadNodeTree"
                empty-text=''
                :data="treeData"
                ref="tree"
                node-key="id"
                lazy
                :default-expanded-keys="treeExpandedKeys"
                :expand-on-click-node="false"
                @node-expand="treeExpand"
                @node-collapse="treeCollapse"
              >
              </el-tree>
              <el-popover v-if="!treeData.length" style="text-align: center;" placement="right-start" trigger="hover">
                <div class="tree-empty-shujuji">
                  <div class="title">标准分类帮助文档</div>
                  <div class="subtitle">创建标准分类只<span style="color: #3BA3F8">需2步</span></div>
                  <div class="yindao">
                    <div class="first-step step">
                      <div class="iicon">1</div>
                      <div class="yin-txt">新建目录</div>
                    </div>
                    <div class="second-step">------------------------------------------------> </div>
                    <div class="third-step step">
                      <div class="iicon">2</div>
                      <div class="yin-txt">新建标准分类</div>
                    </div>
                  </div>
                  <div class="yindao-img">
                    <div class="step1"><img src="./../../../assets/images/guide/数据标准-标准分类1.png" alt=""></div>
                    <div class="step2"><img src="./../../../assets/images/guide/数据标准-标准分类.png" alt=""></div>
                  </div>
                </div>
                <div style="width: 80px;margin: 0 auto" slot="reference">
                  暂无数据&nbsp;&nbsp;
                  <svg-icon iconClass="help-circle" />
                </div>
              </el-popover>
            </vue-scroll>
          </div>
        </div>
      </el-col>
      <el-col class="pt20 care-technical-right">
        <div class="box-operate pt0">
          <el-row class="box-operate-t">
            <div class="analyze-t">
              <el-row>
                <label style="margin-top: 0;font-size: 13px">标准分类名称：</label>
                <el-input
                  v-model="NodeMsg.name"
                  @keyup.enter.native="searchList"
                  size="mini"
                  style="width: 300px"
                ></el-input>
                <el-select
                  v-model="NodeMsg.status"
                  placeholder="请选择"
                  size="mini"
                  style="width: 180px"
                  :disabled="!statusList.length"
                  @change="SelectNodeList"
                >
                  <el-option
                    v-for="item in statusList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <el-button
                  type="danger"
                  size="mini"
                  class="ml10"
                  @click="searchList"
                  :disabled="NodeMsg.pid === ''"
                  >查 询</el-button
                >
              </el-row>
            </div>
            <el-col :span="24" align="left" class="mt15">
              <el-button
                class="grey-btn"
                @click="addDataSet"
                size="mini"
                :disabled="NodeMsg.pid === ''"
                >新建</el-button
              >
              <el-button
                class="grey-btn"
                size="mini"
                @click="sendAudit"
                :disabled="deleteSelection.length <= 0"
                >送审</el-button
              >
              <el-button
                class="grey-btn"
                size="mini"
                @click="deleteDataSet('s', 1)"
                :disabled="deleteSelection.length <= 0"
                >删除</el-button
              >
            </el-col>
          </el-row>
          <el-row class="mt20 box-operate-b">
            <el-table
              :data="tableData"
              @selection-change="changeFun"
              height="100%"
              style="width: 100%;"
            >
              <template slot="empty">
                <div class="show-empty">
                  <el-popover v-if="!tableData.length" style="text-align: center;" placement="bottom" trigger="hover">
                    <div class="tree-empty-shujuji">
                      <div class="title">标准分类帮助文档</div>
                      <div class="subtitle">创建标准分类只<span style="color: #3BA3F8">需2步</span></div>
                      <div class="yindao">
                        <div class="first-step step">
                          <div class="iicon">1</div>
                          <div class="yin-txt">新建目录</div>
                        </div>
                        <div class="second-step">------------------------------------------------> </div>
                        <div class="third-step step">
                          <div class="iicon">2</div>
                          <div class="yin-txt">新建标准分类</div>
                        </div>
                      </div>
                      <div class="yindao-img">
                        <div class="step1"><img src="./../../../assets/images/guide/数据标准-标准分类1.png" alt=""></div>
                        <div class="step2"><img src="./../../../assets/images/guide/数据标准-标准分类.png" alt=""></div>
                      </div>
                    </div>
                    <div slot="reference">
                      暂无数据&nbsp;&nbsp;
                      <svg-icon iconClass="help-circle" />
                    </div>
                  </el-popover>
                </div>
              </template>
              <el-table-column type="selection" width="55" :selectable="checkBox"> </el-table-column>
              <el-table-column prop="name" label="标准分类名称">
                <template slot-scope="scope">
                  <div
                    type="text"
                    style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"
                    class="dataElementName-class"
                    @click="showDataSource(scope.row)"
                  >
                    {{ scope.row.name }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="创建时间">
                <template slot-scope="scope">
                  <span>{{ scope.row.createTime | date("%Y-%m-%d %T") }}</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" :render-header="statusRenderHeader">
                <template slot-scope="scope">
                  <el-tag class="new-tag" v-if="scope.row.status === 0"
                    >草稿</el-tag
                  >
                  <el-tag class="going-tag" v-if="scope.row.status === 1"
                    >待审核</el-tag
                  >
                  <el-tag class="success-tag" v-if="scope.row.status === 2"
                    >已审核</el-tag
                  >
                  <el-tag class="error-tag" v-if="scope.row.status === 3"
                    >变更中</el-tag
                  >
                  <el-tag class="quit-tag" v-if="scope.row.status === 4"
                    >已退回</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column label="操作" width="170">
                <template slot-scope="scope">
                  <el-button
                    v-if="
                      scope.row.status === 0 ||
                        scope.row.status === 3 ||
                        scope.row.status === 4
                    "
                    class="operate-btn"
                    @click="editDataSet(scope.row.id)"
                    type="text"
                    >编辑</el-button
                  >
                  <el-button
                    v-if="
                      scope.row.status === 0 ||
                        scope.row.status === 3 ||
                        scope.row.status === 4
                    "
                    class="operate-btn"
                    @click="deleteDataSet(scope.row.id, 0)"
                    type="text"
                    >删除</el-button
                  >
                  <el-button
                    v-if="scope.row.status === 2"
                    class="operate-btn"
                    @click="changeStatus(scope.row.id)"
                    type="text"
                    >变更</el-button
                  >
                  <el-button
                    v-if="scope.row.status === 1"
                    class="operate-btn"
                    @click="recall(scope.row.id)"
                    type="text"
                    >撤回</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              v-if="NodeMsg.pid"
              class="ar mt15"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="10"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
            >
            </el-pagination>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      :close-on-click-modal="false"
      width="500px"
      title="变更"
      :visible.sync="addChangeVisible"
      :before-close="closeChangeModal"
      append-to-body
    >
      <el-form
        :model="changeForm"
        ref="addLayerForm"
        size="small"
        label-width="80px"
        label-position="right"
      >
        <el-form-item
          label="变更原因"
          prop="changeInfo"
          :rules="[
            { required: true, message: '请填写变更原因', trigger: 'blur' },
            {
              min: 1,
              max: 255,
              message: '长度在 1 到 255 个字符',
              trigger: 'blur'
            }
          ]"
        >
          <el-input
            type="textarea"
            :rows="4"
            placeholder="请输入内容"
            v-model="changeForm.changeInfo"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="closeChangeModal"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitChange('addLayerForm')"
          :loading="buttonLoading1"
          >保 存</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      width="500px"
      :title="contentsTitle"
      :visible.sync="addLayerVisible"
      :before-close="closeAddlayerModal"
      append-to-body
    >
      <el-form
        :model="addLayerForm"
        ref="addLayerForm"
        size="small"
        :rules="rules"
        label-width="80px"
        label-position="right"
      >
        <el-form-item v-if="contentsTitle !== '新建目录'" label="上级目录">
          <label style="font-size: 12px;">{{ parentContents }}</label>
        </el-form-item>
        <el-form-item label="目录名称" prop="name">
          <el-input
            v-model="addLayerForm.name"
            maxlength="20"
            placeholder="最大长度为20个字符"
          ></el-input>
        </el-form-item>
        <el-form-item label="目录描述" prop="desc">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入内容"
            v-model="addLayerForm.desc"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="closeAddlayerModal"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitLayer('addLayerForm')"
          :loading="buttonLoading2"
          >保 存</el-button
        >
      </span>
    </el-dialog>
    <add-or-update-data-set-dialog
      :isEditMeta="isEditMeta"
      :addOrUpdateDataSetDialogVisible="addOrUpdateDataSetDialogVisible"
      @addOrUpdateDataSetDialogVisibleEvent="updateDialogStatus"
    ></add-or-update-data-set-dialog>
  </section>
</template>

<script type="text/jsx">
import AddOrUpdateDataSetDialog from "./dataCollect/AddOrUpdateDataSetDialog";
export default {
  name: "meta-collect",
  components: {
    AddOrUpdateDataSetDialog
  },
  data() {
    return {
      buttonLoading1: false,
      buttonLoading2: false,
      total: 0,
      currentPage: 1,
      pageLength: 10,
      contentsTitle: "",
      parentContents: "",
      rootNode: [],
      addChangeVisible: false,
      changeForm: {
        id: "",
        changeInfo: ""
      },
      addLayerVisible: false,
      addLayerForm: {
        prevName: "",
        name: "",
        code: "",
        desc: "",
        id: "",
        pid: 0
      },
      rules: {
        name: [{ required: true, message: "请填写目录名称", trigger: "blur" }],
        desc: [
          {
            min: 1,
            max: 255,
            message: "长度在 1 到 255 个字符",
            trigger: "blur"
          }
        ]
      },

      treeData: [], //tree数据
      treeExpandedKeys: [], //展开的key
      treeKey: "", //强制刷新的key
      activeData: null, // 当前操作数据
      defaultProps: {
        id: "id",
        label: "name",
        children: "children",
        isLeaf: "leaf"
      },

      currNodeId: "",
      addOrUpdateDataSetDialogVisible: false,
      isEditMeta: {
        id: "",
        pid: "",
        currTime: new Date().getTime(),
        isHistory: false
      },
      NodeMsg: {
        pid: "",
        name: "",
        status: ""
      },
      statusList: [
        { value: "", label: "全部" },
        { value: 0, label: "草稿" },
        { value: 1, label: "待审核" },
        { value: 2, label: "已审核" },
        { value: 3, label: "变更中" },
        { value: 4, label: "已退回" }
      ],
      tableData: [],
      deleteSelection: []
    };
  },
  methods: {
    loadNodeTree(node, resolve) {
      let param = {};
      if (node.level === 0) {
        param = { id: 0 };
      } else {
        param = { id: node.data.id };
      }
      this.$urlApi.dataStandard.getMenuList(param).then(res => {
        res.data.forEach(et => {
          et.children = [];
          if (et.isMenu === "Y") {
            et.leaf = false;
          } else {
            et.leaf = true;
          }
        });
        if (node.level === 0) {
          resolve(res.data)
          this.treeData = res.data;
        } else {
          node.data.children = res.data;
          resolve(res.data);
        }
        if (node.level === 0) {
          this.$nextTick(() => {
            if (res.data.length) {
              this.$refs.tree && this.$refs.tree.setCurrentKey(res.data[0].id);
              this.handleNodeClick(res.data[0]);
            }
          });
        }
      });
    },
    handleNodeClick(data, node, h) {
      this.NodeMsg.pid = data.id;
      this.NodeMsg.name = "";
      this.NodeMsg.status = "";
      this.getNodeList();
    },
    renderContent(h, { node, data, store }) {
      let nodeItemContent = ( //第二级
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
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
              <el-tooltip
                class="item"
                effect="dark"
                content="删除"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.deleteLayer(node, data, h)}
                >
                  <svg-icon iconClass="删除" />
                </el-button>
              </el-tooltip>
            </span>
          </span>
        </span>
      );
      let nodeItemContentFour = ( //第二级
        <span class="nodeItem">
          <span>
            <span class="node-label" title={node.label}>
              {node.label}
            </span>
            <span class="nodeOptDetail">
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
                  on-click={() => this.deleteLayer(node, data, h)}
                >
                  <svg-icon iconClass="删除" />
                </el-button>
              </el-tooltip>
            </span>
          </span>
        </span>
      );
      return node.level < 4 ? nodeItemContent : nodeItemContentFour;
    },
    addNewLayer(a, b, c) {
      this.activeData = a;
      if (a === -1) {
        this.addLayerForm.pid = 0;
        this.currNodeId = "";
        this.contentsTitle = "新建目录";
      } else {
        this.currNodeId = a.id;
        this.addLayerForm.pid = a.id;
        this.addLayerForm.id = "";
        this.contentsTitle = "新建下级目录";
        this.parentContents = a.name;
      }
      this.addLayerVisible = true;
    },
    editLayer(data, node, h) {
      if (node.level === 1) {
        this.currNodeId = "";
      } else {
        this.currNodeId = data.parentId;
      }
      this.parentContents = node.parent ? node.parent.data.name : "";
      this.addLayerForm.prevName = data.pname;
      this.addLayerForm.name = data.name;
      this.addLayerForm.code = data.code;
      this.addLayerForm.desc = data.descrption;
      this.addLayerForm.id = data.id;
      this.addLayerForm.pid = data.parentId;
      this.contentsTitle = "编辑目录";
      this.addLayerVisible = true;
    },
    submitLayer(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading2 = true;
          this.$urlApi.dataStandard
            .addDataMetaMenu(this.addLayerForm)
            .then(res => {
              this.buttonLoading2 = false;
              this.renderTree();
              this.addLayerVisible = false;
              this.closeAddlayerModal();
            })
            .catch(e => {
              this.buttonLoading2 = false;
            });
        } else {
          return false;
        }
      });
    },
    // 刷新key值，重新渲染tree
    renderTree() {
      this.treeKey = +new Date();
    },
    closeAddlayerModal() {
      this.$refs["addLayerForm"].resetFields();
      this.addLayerForm.prevName = "";
      this.addLayerForm.name = "";
      this.addLayerForm.code = "";
      this.addLayerForm.desc = "";
      this.addLayerForm.id = "";
      this.addLayerForm.pid = 0;
      this.addLayerVisible = false;
      this.parentContents = "";
    },
    deleteLayer(node, data, h) {
      //删除源
      let that = this;
      if (node.level === 1) {
        this.currNodeId = "";
      } else {
        this.currNodeId = data.parentId;
      }
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataStandard
            .deleteDataMetaMenu({ id: data.id })
            .then(res => {
              this.renderTree();
            });
        })
        .catch(() => {});
    },
    // 当节点打开时，记录下打开节点的id
    treeExpand(data, node, self) {
      this.treeExpandedKeys.push(data.id);
    },
    // 当节点关闭时，移除节点的id
    treeCollapse(data) {
      const index = this.treeExpandedKeys.indexOf(data.id);
      if (index > -1) {
        this.treeExpandedKeys.splice(index, 1);
      }
    },
    updateDialogStatus: function() {
      this.addOrUpdateDataSetDialogVisible = false;
      this.isEditMeta.id = "";
      this.getNodeList();
      this.isEditMeta.isHistory = false;
    },
    addDataSet: function() {
      let tempDate = new Date().getTime();
      this.addOrUpdateDataSetDialogVisible = true;
      this.isEditMeta.currTime = tempDate;
      this.isEditMeta.pid = this.NodeMsg.pid;
    },
    editDataSet(id) {
      let tempDate = new Date().getTime();
      this.addOrUpdateDataSetDialogVisible = true;
      this.isEditMeta.currTime = tempDate;
      this.isEditMeta.pid = this.NodeMsg.pid;
      this.isEditMeta.id = id;
    },
    searchList() {
      this.getNodeList(1);
    },
    SelectNodeList(val) {
      this.NodeMsg.status = val;
    },
    getNodeList(page, type) {
      page && (this.currentPage = page);
      let param = {
        pid: this.NodeMsg.pid,
        name: this.NodeMsg.name,
        status: this.NodeMsg.status,
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength
      };
      this.$urlApi.dataStandard.getNodeListByNode(param).then(res => {
        if (type === "delete") {
          if (
            res.recordsFiltered % this.pageLength === 0 &&
            this.currentPage > 1
          ) {
            this.getNodeList(res.recordsFiltered / this.pageLength);
          }
        }
        this.tableData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.currentPage = 1;
      this.getNodeList();
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getNodeList();
    },
    changeFun(val) {
      this.deleteSelection = val;
    },
    sendAudit() {
      let statusArr = this.deleteSelection.map(item => item.status).join();
      if (
        statusArr.indexOf("1") !== -1 ||
        statusArr.indexOf("2") !== -1 ||
        statusArr.indexOf("4") !== -1
      ) {
        this.$toast("warning", "只有草稿和变更中的标准分类可以送审");
        return;
      }
      let ids = this.deleteSelection.map(item => item.id).join();
      this.$confirm("确认要送审吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataStandard.dataSetSendAudit({ ids: ids }).then(res => {
            this.getNodeList();
            this.$toast("success", "送审成功");
          });
        })
        .catch(() => {});
    },
    deleteDataSet(id, num) {
      let statusArr = this.deleteSelection.map(item => item.status).join();
      if (statusArr.indexOf("1") !== -1 || statusArr.indexOf("2") !== -1) {
        this.$toast("warning", "只有草稿、变更中及已退回的标准分类可以删除！");
        return;
      }
      let ids = "";
      if (num === 0) {
        ids = id;
      } else {
        ids = this.deleteSelection.map(item => item.id).join();
      }
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataStandard.deleteDataSet({ ids: ids }).then(res => {
            this.getNodeList(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .then(() => {
          setTimeout(() => {
            this.getNodeList();
          }, 500);
        });
    },
    closeChangeModal() {
      this.changeForm.changeInfo = "";
      this.addChangeVisible = false;
    },
    changeStatus(id) {
      this.addChangeVisible = true;
      this.changeForm.id = id;
    },
    submitChange(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading1 = true;
          this.$urlApi.dataStandard
            .changeDataSet(this.changeForm)
            .then(res => {
              this.buttonLoading1 = false;
              this.addChangeVisible = false;
              this.closeChangeModal();
              this.$toast("success", "操作成功");
              this.getNodeList();
            })
            .catch(e => {
              this.buttonLoading1 = false;
            });
        } else {
          return false;
        }
      });
    },
    showDataSource(item) {
      let tempDate = new Date().getTime();
      this.addOrUpdateDataSetDialogVisible = true;
      this.isEditMeta.currTime = tempDate;
      this.isEditMeta.id = item.id;
      this.isEditMeta.isHistory = true;
    },
    checkBox(row, index) {
      if (row.status === 1 || row.status === 2) {
        return false
      } else {
        return true
      }
    },
    statusRenderHeader(h, { column, $index }) {
      return (
        <el-popover placement="top" trigger="hover">
          <div style='font-size: 12px'>
            <div>草稿：新建未提交标准，可编辑/删除。</div>
            <div style="padding: 5px 0">待审核：待审批标准，不可编辑/删除，可撤回。</div>
            <div>已审核：可使用数据标准，不可删除，点击“变更”状态改为“变更中”。</div>
            <div style="padding: 5px 0">变更中：标准变更时的中间状态，可删除编辑。</div>
            <div>已退回：被退回标准，可编辑/删除。</div>
          </div>
          <div slot="reference" class="cell">
            状态&nbsp;&nbsp;
            <svg-icon iconClass="help-circle" />
          </div>
        </el-popover>
      );
    },
    recall(id) {
      this.$confirm("确认要撤回吗？", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      }).then(async () => {
        const res = await this.withDraw(id);
        res.code === '200' && this.$toast('success', '撤回成功！')
        this.getNodeList()
      })
    },
    withDraw(id) {
      return this.$urlApi.dataStandard.dataSetRecall({id: id})
    }
  },
  mounted: function() {
    let that = this;
  },
  beforeDestroy: function() {
    let that = this;
  }
};
</script>

<style lang="scss">
.data-collect-section {
  .main-inner-allSize {
    display: flex;
    min-width: 0;
    .care-technical-left {
      width: 254px;
      margin-right: 15px;
      height: 100%;
      .tree-menu {
        height: 100%;
        background: #ffffff;
        padding: 14px 8px;
        box-sizing: border-box;
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
          overflow: auto;
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
    .care-technical-right {
      flex: 1;
      min-width: 0;
      .box-operate {
        height: 100%;
        .box-operate-b {
          height: -moz-calc(100% - 135px);
          height: -webkit-calc(100% - 135px);
          height: calc(100% - 135px);
          .el-table__empty-block {
            align-items: baseline;
            .el-popover__reference {
              margin-top: 20px;
            }
          }
          // .el-table__empty-text {
          //   display: block !important;
          //   width: 250px !important;
          //   height: 200px !important;
          //   line-height: 455px !important;
          //   background: url(./../../../assets/images/noDataPic.png) no-repeat
          //     center !important;
          // }
        }
      }
    }
  }
  .dataElementName-class {
    cursor: pointer;
    color: #606266;
  }
  .dataElementName-class:hover {
    color: #3ba3f8;
  }
}
.tree-empty-shujuji {
  width: 850px;
  height:400px;
  padding: 0 20px;
  box-sizing: border-box;
  .title {
    font-size: 18px;
    color: #3E4456;
    margin: 25px 0;
    font-weight: 700;
  }
  .subtitle {
    font-size: 16px;
    color: #3E4456;
    margin-bottom: 50px;
  }
  .yindao {
    height: 60px;
    padding-left: 120px;
    user-select: none;
    .first-step, .second-step, .third-step {
      float: left;
    }
    .second-step {
      color: #3BA3F8;
      line-height: 40px;
      margin-right: 10px;
    }
    .iicon {
      color: #fff;
      background-color: #3BA3F8;
      height: 36px;
      width: 36px;
      line-height: 36px;
      text-align: center;
      font-size: 22px;
      border-radius: 50%;
    }
    .yin-txt {
      color: #3BA3F8;
      font-size: 12px;
      margin-top: 5px;
      margin-left: -7px;
    }
  }
  .yindao-img {
    padding-left: 30px;
    margin-top: 25px;
    .step1,.step2, .step3 {
      float: left;
      margin-right: 20px;
    }
  }
}
</style>

<template>
  <section class="business-glossary-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner-allSize">
      <el-col class="pt10 care-technical-left">
        <div class="tree-menu">
          <el-input
            placeholder="请输入目录或者代码集名称"
            class="search-input mb10"
            size="small"
            v-model="treeName"
          >
            <i
              slot="suffix"
              class="el-input__icon el-icon-search searchBtn"
            ></i>
          </el-input>
          <div class="tree-c mb10">
            <el-tooltip
              class="item"
              effect="dark"
              content="新建目录"
              placement="top"
            >
              <span class="tree-t-set" @click="addNewLayer(-1)"
                ><svg-icon iconClass="文件"></svg-icon
              ></span>
            </el-tooltip>
            <el-tooltip
              class="item"
              effect="dark"
              content="下载模板"
              placement="top"
            >
              <span class="tree-t-set" @click="downloadTemplate"
                ><svg-icon iconClass="下载"></svg-icon
              ></span>
            </el-tooltip>
          </div>
          <div class="tree-b">
            <el-scrollbar style="height:100%;">
              <el-tree
                :data="nodeList"
                default-expand-all
                :highlight-current="true"
                :expand-on-click-node="true"
                :props="defaultProps"
                :empty-text="!nodeList.length ? '' : '暂无数据'"
                :render-content="renderContent"
                @node-click="nodeClick"
                :filter-node-method="filterNode"
                nodeKey="id"
                :indent="9"
                ref="tree2"
              >
              </el-tree>
              <el-popover v-if="!nodeList.length" style="text-align: center;" placement="right-start" trigger="hover">
                <div class="tree-empty-shujuji">
                  <div class="title">业务代码帮助文档</div>
                  <div class="subtitle">创建业务代码只<span style="color: #3BA3F8">需3步</span></div>
                  <div class="yindao">
                    <div class="first-step step">
                      <div class="iicon">1</div>
                      <div class="yin-txt">新建目录</div>
                    </div>
                    <div class="second-step">-------------------------------> </div>
                    <div class="third-step step">
                      <div class="iicon">2</div>
                      <div class="yin-txt">新建代码集</div>
                    </div>
                    <div class="second-step">-------------------------------> </div>
                    <div class="third-step step">
                      <div class="iicon">3</div>
                      <div class="yin-txt">新建代码</div>
                    </div>
                  </div>
                  <div class="yindao-img">
                    <div class="step1"><img src="./../../../assets/images/guide/数据标准-业务代码1.png" alt=""></div>
                    <div class="step2"><img src="./../../../assets/images/guide/数据标准-业务代码2.png" alt=""></div>
                    <div class="step3"><img src="./../../../assets/images/guide/数据标准-业务代码3.png" alt=""></div>
                  </div>
                </div>
                <div style="width: 80px;margin: 0 auto" slot="reference">
                  暂无数据&nbsp;&nbsp;
                  <svg-icon iconClass="help-circle" />
                </div>
              </el-popover>
            </el-scrollbar>
          </div>
        </div>
      </el-col>
      <el-col class="pt20 care-technical-right">
        <div class="box-operate pt0">
          <el-form size="small" label-width="100px" label-position="right">
            <el-row>
              <el-col :span="8">
                <el-form-item class="bussinessCodeSet" label="代码集名称：">
                  <el-tooltip
                    class="item"
                    effect="dark"
                    :content="codeSetName"
                    placement="top"
                  >
                    <div class="bussinessCodeSetText">{{ codeSetName }}</div>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item class="bussinessCodeSet" label="代码规范：">
                  <el-tooltip
                    class="item"
                    effect="dark"
                    :content="codeSetRule"
                    placement="top"
                  >
                    <div :title="codeSetRule" class="bussinessCodeSetText">
                      {{ codeSetRule }}
                    </div>
                  </el-tooltip>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item class="bussinessCodeSet" label="代码集编号：">
                  <el-tooltip
                    class="item"
                    effect="dark"
                    :content="uniqueCode"
                    placement="bottom"
                  >
                    <div :title="uniqueCode" class="bussinessCodeSetText">
                      {{ uniqueCode }}
                    </div>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item class="bussinessCodeSet" label="代码状态：">
                  <el-select
                    v-model="status"
                    placeholder="请选择数据状态"
                    :disabled="!statusList.length"
                    @change="changeDataStatus"
                  >
                    <el-option
                      v-for="item in statusList"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-row>
            <el-button
              size="mini"
              @click="addNewMeta({ status: -1 })"
              :disabled="currNodeId === ''"
              class="grey-btn"
              >新建</el-button
            >
            <el-button
              size="mini"
              class="grey-btn"
              @click="sendAudit"
              :disabled="this.deleteSelection.length <= 0"
              >送审</el-button
            >
            <el-button
              size="mini"
              @click="deleteList"
              :disabled="this.deleteSelection.length <= 0"
              class="grey-btn"
              >删除</el-button
            >
          </el-row>
          <el-row class="box-operate-b">
            <el-table
              :data="tableData"
              height="100%"
              @selection-change="changeFun"
              style="width: 100%;"
            >
              <template slot="empty">
                <div class="show-empty">
                  <el-popover v-if="!tableData.length" style="text-align: center;" placement="bottom" trigger="hover">
                    <div class="tree-empty-shujuji">
                      <div class="title">业务代码帮助文档</div>
                      <div class="subtitle">创建业务代码只<span style="color: #3BA3F8">需3步</span></div>
                      <div class="yindao">
                        <div class="first-step step">
                          <div class="iicon">1</div>
                          <div class="yin-txt">新建目录</div>
                        </div>
                        <div class="second-step">-------------------------------> </div>
                        <div class="third-step step">
                          <div class="iicon">2</div>
                          <div class="yin-txt">新建代码集</div>
                        </div>
                        <div class="second-step">-------------------------------> </div>
                        <div class="third-step step">
                          <div class="iicon">3</div>
                          <div class="yin-txt">新建代码</div>
                        </div>
                      </div>
                      <div class="yindao-img">
                        <div class="step1"><img src="./../../../assets/images/guide/数据标准-业务代码1.png" alt=""></div>
                        <div class="step2"><img src="./../../../assets/images/guide/数据标准-业务代码2.png" alt=""></div>
                        <div class="step3"><img src="./../../../assets/images/guide/数据标准-业务代码3.png" alt=""></div>
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
              <el-table-column prop="code" label="代码" show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                prop="name"
                label="代码名称"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column label="状态" :render-header="statusRenderHeader" show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-tag class="new-tag" v-if="scope.row.status == 0"
                    >草稿</el-tag
                  >
                  <el-tag class="going-tag" v-if="scope.row.status == 1"
                    >待审核</el-tag
                  >
                  <el-tag class="success-tag" v-if="scope.row.status == 2"
                    >已审核</el-tag
                  >
                  <el-tag class="error-tag" v-if="scope.row.status == 3"
                    >变更中</el-tag
                  >
                  <el-tag class="quit-tag" v-if="scope.row.status == 4"
                    >已退回</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column label="操作" width="220">
                <template slot-scope="scope">
                  <el-button
                    v-if="
                      scope.row.status == 0 ||
                        scope.row.status == 3 ||
                        scope.row.status == 4
                    "
                    class="operate-btn"
                    @click="addNewMeta(scope.row)"
                    type="text"
                    >编辑</el-button
                  >
                  <el-button
                    v-if="
                      scope.row.status == 0 ||
                        scope.row.status == 3 ||
                        scope.row.status == 4
                    "
                    class="operate-btn"
                    @click="deleteList(scope.row.id, 0)"
                    type="text"
                    >删除</el-button
                  >
                  <el-button
                    v-if="scope.row.status == 2"
                    class="operate-btn"
                    @click="changeStatus(scope.row.id)"
                    type="text"
                    >变更</el-button
                  >
                  <el-button
                    v-if="scope.row.status == 1"
                    class="operate-btn"
                    @click="recall(scope.row.id)"
                    type="text"
                    >撤回</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              v-if="currNodeId"
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
        :rules="rules"
        ref="addLayerForm"
        size="small"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="变更原因" prop="changeInfo">
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
          :loading="buttonLoading"
          >保 存</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      width="30%"
      title="导入"
      :visible.sync="importVisible"
      :before-close="closeImportDialog"
      append-to-body
    >
      <el-upload
        class="upload-demo"
        ref="upload"
        :action="dataSetImport"
        :headers="headers"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
        accept="xlsx,xls"
        :file-list="fileList"
        :on-success="handleSuccess"
        :on-error="handleError"
        :auto-upload="false"
      >
        <el-button slot="trigger" size="small" type="danger"
          >选择文件</el-button
        >
        <el-button
          style="margin-left: 10px;"
          size="small"
          type="danger"
          @click="submitUpload"
          >上传到服务器</el-button
        >
        <div slot="tip" class="el-upload__tip">
          导入文件前请先下载模板，只能上传xlsx，xls格式文件！
        </div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="closeImportDialog"
          >关 闭</el-button
        >
      </span>
    </el-dialog>
    <add-edit-code
      :currNodeId="currNodeId"
      :AddEditCodeListVisible="AddEditCodeListVisible"
      :isEditCodeList="isEditCodeList"
      @editCodeListBack="editCodeListBack"
    ></add-edit-code>
    <add-catalog
      :addCatalogVisible="addCatalogVisible"
      :isEditCatalog="isEditCatalog"
      @addCatalogBack="addCatalogBack"
    ></add-catalog>
    <add-code-set
      :addCodeSetVisible="addCodeSetVisible"
      :isEditCodeSet="isEditCodeSet"
      @addCodeSetBack="addCodeSetBack"
    ></add-code-set>
  </section>
</template>

<script type="text/jsx">
import AddCatalog from "./business/AddCatalog.vue";
import AddCodeSet from "./business/AddCodeSet.vue";
import AddEditCode from "./business/AddEditCode.vue";
export default {
  name: "business-glossary",
  components: {
    AddCatalog,
    AddCodeSet,
    AddEditCode
  },
  data() {
    return {
      buttonLoading: false,
      rules: {
        changeInfo: [
          { required: true, message: "请填写变更原因", trigger: "blur" },
          {
            min: 1,
            max: 255,
            message: "长度在 1 到 255 个字符",
            trigger: "blur"
          }
        ]
      },
      changeForm: {
        id: "",
        changeInfo: ""
      },
      status: "",
      treeName: "",
      currNodeId: "",
      codeSetName: "",
      codeSetRule: "",
      code: null,
      uniqueCode: "",
      addCatalogVisible: false,
      addCodeSetVisible: false,
      AddEditCodeListVisible: false,
      addChangeVisible: false,
      importVisible: false,
      fileList: [],
      dataSetImport: "",
      isEditCodeList: {
        currTime: new Date().getTime(),
        id: "",
        codesetId: ""
      },
      isEditCatalog: {
        currTime: new Date().getTime(),
        id: "",
        pid: "",
        isCodeset: "1",
        isEdit: "",
        name: "",
        code: "",
        explanation: ""
      },
      isEditCodeSet: {
        currTime: new Date().getTime(),
        id: "",
        pid: "",
        isCodeset: "0",
        isEdit: false,
        name: "",
        code: "",
        rule: ""
      },
      total: 0,
      currentPage: 1,
      pageLength: 10,

      deleteSelection: [],
      tableData: [],
      nodeList: [],
      defaultProps: {
        id: "id",
        label: "name",
        children: "children",
        parent: "parentId"
      },
      statusList: [
        {
          value: "",
          label: "全部"
        },
        {
          value: 0,
          label: "草稿"
        },
        {
          value: 1,
          label: "待审核"
        },
        {
          value: 2,
          label: "已审核"
        },
        {
          value: 3,
          label: "变更中"
        },
        {
          value: 4,
          label: "已退回"
        }
      ],
      headers: {
        token: this.$auth.getToken()
      }
    };
  },
  methods: {
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
          this.buttonLoading = true;
          this.$urlApi.dataStandard
            .changeCode(this.changeForm)
            .then(res => {
              this.buttonLoading = false;
              this.addChangeVisible = false;
              this.closeChangeModal();
              this.$toast("success", "操作成功");
              this.getData();
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    changeDataStatus(val) {
      this.getData(1);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    },
    getLayerMenu() {
      this.$urlApi.dataStandard.getCodeSet().then(res => {
        this.nodeList = res.data;
      });
    },
    getData(page, type) {
      page && (this.currentPage = page);
      let param = {
        id: this.currNodeId,
        status: this.status,
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength
      };
      this.$urlApi.dataStandard.getCodesFromSet(param).then(res => {
        if (type === "delete") {
          if (
            res.recordsFiltered % this.pageLength === 0 &&
            this.currentPage > 1
          ) {
            this.getData(res.recordsFiltered / this.pageLength);
          }
        }
        this.tableData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    getCodeSetData() {
      this.$urlApi.dataStandard
        .getCodeSetById({ id: this.currNodeId })
        .then(res => {
          this.codeSetName = res.data.name;
          this.codeSetRule = res.data.codeRule;
          this.uniqueCode = res.data.uniqueCode;
        });
    },
    changeFun(val) {
      this.deleteSelection = val;
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.getData(1);
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getData();
    },
    addNewMeta(row) {
      if (row.status == "0" || row.status == "3" || row.status == "4") {
        this.isEditCodeList.id = row.id;
        this.isEditCodeList.name = row.name;
        this.isEditCodeList.code = row.code;
        this.isEditCodeList.currTime = new Date().getTime();
        this.AddEditCodeListVisible = true;
      } else if (row.status == -1) {
        this.AddEditCodeListVisible = true;
        this.isEditCodeList.id = null;
      } else {
        this.$toast("warning", "只有草稿、变更中、已退回状态才能修改");
      }
    },
    sendAudit() {
      let statusArr = this.deleteSelection.map(item => item.status).join();
      if (
        statusArr.indexOf("1") !== -1 ||
        statusArr.indexOf("2") !== -1 ||
        statusArr.indexOf("4") !== -1
      ) {
        this.$toast("warning", "只有草稿和变更中状态下的代码可以送审");
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
          this.$urlApi.dataStandard.codeSetsendAudit({ ids: ids }).then(res => {
            this.getData();
            this.$toast("success", "送审成功");
          });
        })
        .catch(() => {});
    },
    deleteList(id, num) {
      let statusArr = this.deleteSelection.map(item => item.status).join();
      if (statusArr.indexOf("1") !== -1 || statusArr.indexOf("2") !== -1) {
        this.$toast("warning", "只有草稿、变更中及已退回的代码可以删除！");
        return;
      }
      let ids;
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
          this.$urlApi.dataStandard.deleteCodes({ ids: ids }).then(res => {
            this.getData(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .catch(() => {});
    },
    renderContent(h, { node, data, store }) {
      let nodeItemContent = ( //第二级
        <span class="nodeItem" title={node.label}>
          <span>
            <span class="node-label">
              <span class="node-icon">
                <svg-icon iconClass="文件夹" />
              </span>
              {node.label}
            </span>
            <span style="float: right">
              <el-dropdown trigger="hover">
                <span class="el-dropdown-link">
                  <i on-click={e => e.stopPropagation()} class="el-icon-more" />
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <div on-click={() => this.addNewLayer(data, node, h)}>
                      <svg-icon iconClass="g-添加" /> 新建下级目录
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div
                      on-click={() => this.addNewCodeSet(data, node, h, "add")}
                    >
                      <svg-icon iconClass="g-新建" /> 新建代码集
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div on-click={() => this.editLayer(data, node, h)}>
                      <svg-icon iconClass="编辑1" /> 编辑
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div on-click={() => this.importTemplate(data, node, h)}>
                      <svg-icon iconClass="导入" /> 导入
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div
                      on-click={() => this.exportTemplate(data)}
                    >
                      <svg-icon iconClass="导出" /> 导出
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div on-click={() => this.deleteLayer(data, node, h)}>
                      <svg-icon iconClass="删除" /> 删除
                    </div>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
          </span>
        </span>
      );
      let nodeFieldItem = ( //第一级
        <span class="nodeItem" title={node.label}>
          <span>
            <span class="node-label">{node.label}</span>
            <span style="float: right">
              <el-dropdown trigger="hover">
                <span class="el-dropdown-link">
                  <i class="el-icon-more" />
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <span
                      on-click={() => this.editCodeSet(data, node, h, "edit")}
                    >
                      <svg-icon iconClass="编辑1" /> 编辑
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <span on-click={() => this.deleteLayer(data, node, h)}>
                      <svg-icon iconClass="删除" /> 删除
                    </span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
          </span>
        </span>
      );
      let nodeItem = ( //第4级
        <span class="nodeItem" title={node.label}>
          <span>
            <span class="node-label">
              <span class="node-icon">
                <svg-icon iconClass="文件夹" />
              </span>
              {node.label}
            </span>
            <span style="float: right">
              <el-dropdown trigger="hover">
                <span class="el-dropdown-link">
                  <i on-click={e => e.stopPropagation()} class="el-icon-more" />
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <div on-click={() => this.addNewCodeSet(data, node, h)}>
                      <svg-icon iconClass="g-新建" /> 新建代码集
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div on-click={() => this.editLayer(data, node, h)}>
                      <svg-icon iconClass="编辑1" /> 编辑
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div on-click={() => this.importTemplate(data, node, h)}>
                      <svg-icon iconClass="导入" /> 导入
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div
                      on-click={() => this.exportTemplate(data)}
                    >
                      <svg-icon iconClass="导出" /> 导出
                    </div>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <div on-click={() => this.deleteLayer(data, node, h)}>
                      <svg-icon iconClass="删除" /> 删除
                    </div>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
          </span>
        </span>
      );
      if (data.isCodeset === "0") {
        return nodeFieldItem;
      } else if (node.level === 4) {
        return nodeItem;
      } else {
        return nodeItemContent;
      }
    },
    nodeClick: function(nodeObj, node, obj) {
      if (nodeObj.isCodeset === "0") {
        this.currNodeId = nodeObj.id;
        this.getCodeSetData();
        this.getData(1);
      } else {
        this.$nextTick(() => {
          this.$refs.tree2.setCurrentKey(this.currNodeId);
        });
      }
    },
    addNewCodeSet(data, node, h, type) {
      this.addCodeSetVisible = true;
      this.isEditCodeSet.id = data.id;
      this.isEditCodeSet.currTime = new Date().getTime();
      this.isEditCodeSet.pid = data.parentId;
      this.isEditCodeSet.isCodeset = data.isCodeset;
      this.isEditCodeSet.isEdit = type === "edit";
    },
    addNewLayer(data, node, h) {
      this.addCatalogVisible = true;
      this.isEditCatalog.id = data.id;
      this.isEditCatalog.name = data.name;
      this.isEditCatalog.currTime = new Date().getTime();
      this.isEditCatalog.pid = data.parentId;
      this.isEditCatalog.isCodeset = data.isCodeset;
      if (data === -1) {
        this.isEditCatalog.isEdit = "add";
      } else {
        this.isEditCatalog.isEdit = "addChild";
      }
    },
    editLayer(data, node, h) {
      this.addCatalogVisible = true;
      this.isEditCatalog.id = data.id;
      this.isEditCatalog.name = data.name;
      this.isEditCatalog.code = data.code;
      this.isEditCatalog.currTime = new Date().getTime();
      this.isEditCatalog.pid = data.parentId;
      this.isEditCatalog.isCodeset = data.isCodeset;
      this.isEditCatalog.isEdit = "edit";
      this.isEditCatalog.explanation = data.explanation;
      this.isEditCatalog.pname = node.parent ? node.parent.data.name : "";
    },
    editCodeSet(data, node, h) {
      this.addCodeSetVisible = true;
      this.isEditCodeSet.id = data.id;
      this.isEditCodeSet.name = data.name;
      this.isEditCodeSet.rule = data.codeRule;
      this.isEditCodeSet.createPolicy = data.createPolicy;
      this.isEditCodeSet.currTime = new Date().getTime();
      this.isEditCodeSet.pid = data.parentId;
      this.isEditCodeSet.isCodeset = data.isCodeset;
      this.isEditCodeSet.isEdit = true;
    },
    deleteLayer(data, node, h) {
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
            .deleteCodeSets({ id: data.id })
            .then(res => {
              this.$toast("success", "删除成功");
              this.getLayerMenu();
              this.codeSetName = "";
              this.codeSetRule = "";
              this.uniqueCode = "";
              this.status = "";
              this.tableData = [];
            });
        })
        .catch(() => {});
    },
    addCatalogBack() {
      this.addCatalogVisible = false;
      this.getLayerMenu();
    },
    addCodeSetBack() {
      this.addCodeSetVisible = false;
      this.isEditCodeSet.id = "";
      this.isEditCodeSet.name = "";
      this.isEditCodeSet.rule = "";
      this.isEditCodeSet.pid = "";
      this.isEditCodeSet.isCodeset = "0";
      this.isEditCodeSet.isEdit = false;
      this.getLayerMenu();
      if (this.currNodeId !== "") {
        this.getCodeSetData();
      }
    },
    editCodeListBack() {
      this.AddEditCodeListVisible = false;
      this.getData();
    },
    importTemplate(data, node, h) {
      this.importVisible = true;
      this.dataSetImport = `${this.$urlApi.dataStandard.dataSetImport()}?parentId=${
        data.id
      }`;
    },
    exportTemplate(data) {
      if (!data.children || !data.children.length || !data.children.find(c => c.isCodeset === '0')) {
        return this.$toast('error', '目录下不存在代码集！')
      }
      this.download(data)
    },
    download(data) {
      const { name, id } = data
      return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        const url = `${this.$urlApi.dataStandard.exportCodeSet()}?parentId=${id}`;
        xhr.open("GET", url, true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.setRequestHeader(
          "token",
          this.$auth.getToken()
        );
        xhr.responseType = "blob";
        xhr.onload = function(event) {
          const res = xhr.response;
          const link = document.createElement("a");
          link.download = `${name}.xls`;
          link.style.display = "none";
          const blob = new Blob([res]);
          link.href = URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          resolve()
        };
        xhr.send();
      })
    },
    downloadTemplate() {
      const url = this.$urlApi.dataStandard.dataSetTemplateDownload();
      window.open(url);
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {},
    handlePreview(file) {},
    handleSuccess(res, file) {
      if (res.code === "200") {
        this.$toast("success", "导入成功");
        this.getLayerMenu();
      } else {
        this.$toast("error", res.message);
        this.fileList = [];
      }
    },
    handleError(err, file, fileList) {
      this.$toast("error", "导入失败：请检查文件内容格式！");
      this.fileList = [];
    },
    beforeUpload(file) {
      let testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "xlsx";
      const extension1 = testmsg === "xls";
      if (!extension && !extension1) {
        this.$toast("warning", "上传文件只能是xlsx,xls格式!");
        return false;
      } else {
        return true;
      }
    },
    closeImportDialog() {
      this.importVisible = false;
      this.fileList = [];
    },
    checkBox(row, index) {
      if (row.status == 1 || row.status == 2) {
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
        this.getData()
      })
    },
    withDraw(id) {
      return this.$urlApi.dataStandard.codeRecall({id: id})
    }
  },
  mounted: function() {
    this.$urlApi.dataStandard.getCodeSet().then(res => {
      this.nodeList = res.data;
      const a = [];
      const clickOneNode = array => {
        array.map(c => {
          if (c.children) {
            clickOneNode(c.children);
          } else {
            a.push(c);
          }
        });
        return a;
      };
      this.$nextTick(() => {
        if (res.data.length) {
          this.$refs.tree2 && this.$refs.tree2.setCurrentKey(clickOneNode(res.data)[0].id);
          this.nodeClick(clickOneNode(res.data)[0]);
        }
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
.business-glossary-section {
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
          .el-scrollbar__wrap {
            overflow-x: hidden;
          }
          .el-tree {
            .el-tree-node__content {
              position: relative;
              .nodeItem {
                width: 80%;
                /*     position: relative;*/
                .node-label {
                  display: inline-block;
                  width: 80%;
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
                  top: 0px;
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
      }
    }
    .care-technical-right {
      flex: 1;
      min-width: 0;
      .box-operate {
        height: 100%;
        .bussinessCodeSet {
          &.el-form-item {
            margin-bottom: 10px !important;
          }
          label {
            color: #2c3e50 !important;
          }
          .bussinessCodeSetText {
            color: #606266;
            font-size: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
        .box-operate-b {
          height: -moz-calc(100% - 155px);
          height: -webkit-calc(100% - 155px);
          height: calc(100% - 155px);
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
      margin-left: -5px;
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

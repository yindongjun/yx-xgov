<template>
  <section class="document-manage-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner-allSize">
      <el-col class="pt10 care-technical-left">
        <div class="tree-menu">
          <el-input
            placeholder="请输入目录名称"
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
          </div>
          <div class="tree-b">
            <el-tree
              :data="nodeList"
              default-expand-all
              :highlight-current="true"
              :expand-on-click-node="false"
              :props="defaultProps"
              :empty-text="!nodeList.length ? '' : '暂无数据'"
              :render-content="renderContent"
              @node-click="nodeClick"
              :filter-node-method="filterNode"
              nodeKey="id"
              ref="tree2"
            >
            </el-tree>
            <el-popover v-if="!nodeList.length" style="text-align: center;" placement="right-start" trigger="hover">
              <div class="tree-empty-shujuji">
                <div class="title">文档管理帮助文档</div>
                <div class="subtitle">创建文档管理只<span style="color: #3BA3F8">需2步</span></div>
                <div class="yindao">
                  <div class="first-step step">
                    <div class="iicon">1</div>
                    <div class="yin-txt">新建目录</div>
                  </div>
                  <div class="second-step">------------------------------------------------> </div>
                  <div class="third-step step">
                    <div class="iicon">2</div>
                    <div class="yin-txt">上传文件</div>
                  </div>
                </div>
                <div class="yindao-img">
                  <div class="step1"><img src="./../../../assets/images/guide/数据标准-文档管理1.png" alt=""></div>
                  <div class="step2"><img src="./../../../assets/images/guide/数据标准-文档管理2.png" alt=""></div>
                </div>
              </div>
              <div style="width: 80px;margin: 0 auto" slot="reference">
                暂无数据&nbsp;&nbsp;
                <svg-icon iconClass="help-circle" />
              </div>
            </el-popover>
          </div>
        </div>
      </el-col>
      <el-col class="pt20 care-technical-right">
        <div class="box-operate pt0">
          <el-row>
            <el-button
              size="mini"
              @click="upLoad({ id: currNodeId })"
              :disabled="currNodeId === ''"
              class="grey-btn"
              >上传文件</el-button
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
                      <div class="title">文档管理帮助文档</div>
                      <div class="subtitle">创建文档管理只<span style="color: #3BA3F8">需2步</span></div>
                      <div class="yindao">
                        <div class="first-step step">
                          <div class="iicon">1</div>
                          <div class="yin-txt">新建目录</div>
                        </div>
                        <div class="second-step">------------------------------------------------> </div>
                        <div class="third-step step">
                          <div class="iicon">2</div>
                          <div class="yin-txt">上传文件</div>
                        </div>
                      </div>
                      <div class="yindao-img">
                        <div class="step1"><img src="./../../../assets/images/guide/数据标准-文档管理1.png" alt=""></div>
                        <div class="step2"><img src="./../../../assets/images/guide/数据标准-文档管理2.png" alt=""></div>
                      </div>
                    </div>
                    <div slot="reference">
                      暂无数据&nbsp;&nbsp;
                      <svg-icon iconClass="help-circle" />
                    </div>
                  </el-popover>
                </div>
              </template>
              <el-table-column
                prop="name"
                label="文件名称"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                prop="type"
                label="文件类型"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column
                prop="filePath"
                label="文件路径"
                show-overflow-tooltip
              >
              </el-table-column>
              <el-table-column label="操作" width="220">
                <template slot-scope="scope">
                  <a
                    :href="downLoadFile + '?id=' + scope.row.id"
                    class="download-document"
                    ><el-button class="operate-btn" type="text"
                      >下载</el-button
                    ></a
                  >
                  <el-button
                    class="operate-btn"
                    @click="upDateFile(scope.row.id)"
                    type="text"
                    >更新</el-button
                  >
                  <el-button
                    class="operate-btn"
                    @click="deleteList(scope.row.id)"
                    type="text"
                    >删除</el-button
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
      title="上传文件"
      :visible.sync="upLoaddialogVisible"
      width="50%"
      @close="closeModal"
      :before-close="handleClose"
    >
      <el-upload
        class="upload-demo"
        ref="upload"
        :action="upLoadUrl"
        :headers="headers"
        :data="{ delimiter: delimiter, id: currId }"
        :before-upload="beforeUpload"
        :on-success="successUpLoad"
        multiple
        accept="doc,docx,pdf"
        :limit="3"
        :auto-upload="false"
        :on-exceed="overLimit"
        :file-list="fileList"
        :on-change="handleChange"
      >
        <el-button slot="trigger" size="small" type="danger"
          >选取文件</el-button
        >
        <el-button
          style="margin-left: 10px;"
          size="small"
          type="danger"
          @click="submitUpload"
          >上传到服务器</el-button
        >

        <div slot="tip" class="el-upload__tip">只能上传doc，docx，pdf文件</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="upLoaddialogVisible = false" type="info" size="small"
          >关 闭</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      :close-on-click-modal="false"
      title="更新文件"
      :visible.sync="upDatedialogVisible"
      width="50%"
      @close="closeModalUpdate"
      :before-close="handleCloseUpdate"
    >
      <el-upload
        class="upload-demo0"
        ref="upload0"
        :action="updateUrl"
        :headers="headers"
        :data="{ id: currListId }"
        :before-upload="beforeUpDateFile"
        :on-success="successUpLoadUpDateFile"
        multiple
        accept="doc,docx,pdf"
        :limit="1"
        :auto-upload="false"
        :on-exceed="overLimitUpDateFile"
        :file-list="fileListUpdate"
      >
        <el-button slot="trigger" size="small" type="danger"
          >选取文件</el-button
        >
        <el-button
          style="margin-left: 10px;"
          size="small"
          type="danger"
          @click="submitUpdate"
          >上传到服务器</el-button
        >
        <div slot="tip" class="el-upload__tip">只能上传doc，docx，pdf文件</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="upDatedialogVisible = false" type="info" size="small"
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
  </section>
</template>

<script type="text/jsx">
import AddCatalog from "./document/AddCatalog.vue";
import AddEditCode from "./document/AddEditCode.vue";
import ElRow from "element-ui/packages/row/src/row";
export default {
  name: "business-glossary",
  components: {
    ElRow,
    AddCatalog,
    AddEditCode
  },
  data() {
    return {
      upLoaddialogVisible: false,
      upDatedialogVisible: false,
      downLoadFile: "",
      upLoadUrl: "",
      fileList: [],
      upNum: 0,

      updateUrl: "",
      fileListUpdate: [],
      currListId: "",

      treeName: "",
      currNodeId: "",
      addCatalogVisible: false,
      AddEditCodeListVisible: false,
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
        code: ""
      },
      total: 0,
      currentPage: 1,
      pageLength: 10,

      currId: "",
      deleteSelection: [],
      tableData: [],
      nodeList: [],
      delimiter: ",",
      delimiterUpdate: ",",
      defaultProps: {
        id: "id",
        label: "name",
        children: "children",
        parent: "parentId"
      },
      headers: {
        token: this.$auth.getToken()
      }
    };
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
    },
    getLayerMenu() {
      this.$urlApi.dataStandard.getMenuDocumentContent().then(res => {
        this.nodeList = res.data;
      });
    },
    getData(page, type) {
      page && (this.currentPage = page);
      let param = {
        id: this.currNodeId,
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength
      };
      this.$urlApi.dataStandard.getDocumentFile(param).then(res => {
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
    deleteList(id) {
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataStandard.deleteDocumentFile({ id: id }).then(res => {
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
                content="上传"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.upLoad(data, node, h)}
                >
                  <svg-icon iconClass="上传" />
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
        <span class="nodeItem" title={node.label}>
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
                content="上传"
                placement="top"
              >
                <el-button
                  type="text"
                  size="mini"
                  on-click={() => this.upLoad(data, node, h)}
                >
                  <svg-icon iconClass="上传" />
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
      if (node.level > 3) {
        return nodeFieldItem;
      } else {
        return nodeItemContent;
      }
    },
    nodeClick: function(nodeObj, node, obj) {
      this.currNodeId = nodeObj.id;
      this.getData(1);
    },
    addNewLayer(data, node, h) {
      this.addCatalogVisible = true;
      this.isEditCatalog.id = data.id;
      this.isEditCatalog.currTime = new Date().getTime();
      this.isEditCatalog.pid = data.parentId;
      this.isEditCatalog.name = data.name;
      this.isEditCatalog.isCodeset = data.isCodeset;
      if (data === -1) {
        this.isEditCatalog.isEdit = "add";
      } else {
        this.isEditCatalog.isEdit = "addChild";
        this.isEditCatalog.pid = data.id;
        this.isEditCatalog.id = "";
      }
    },
    deleteLayer(data, node, h) {
      if (data.children && data.children.length) {
        return this.$toast('error', "存在子级目录，请先删除子级目录")
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
            .deleteDocumentFolder({ id: data.id })
            .then(res => {
              this.$toast("success", "删除成功");
              this.getLayerMenu();
              this.tableData = [];
            });
        })
        .catch(() => {});
    },
    addCatalogBack() {
      this.addCatalogVisible = false;
      this.getLayerMenu();
    },
    editCodeListBack() {
      this.AddEditCodeListVisible = false;
      this.getData();
    },
    upLoad(data) {
      this.currId = data.id;
      this.currNodeId = data.id;
      this.upLoaddialogVisible = true;
    },
    beforeUpload(file) {
      let testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "docx";
      const extension1 = testmsg === "doc";
      const extension2 = testmsg === "pdf";
      if (!extension && !extension1 && !extension2) {
        this.$toast("warning", "上传文件只能是doc,docx,pdf格式!");
        return false;
      } else {
        return true;
      }
    },
    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    overLimit(files, fileList) {
      this.$toast("warning", "最多上传3个文件");
    },
    successUpLoad(response, file, fileList) {
      this.upNum++;
      if (response.code === "506") {
        this.$refs.upload.clearFiles();
        this.$toast("error", response.message);
      } else {
        if (this.upNum === this.fileList.length) {
          this.$toast("success", "上传成功");
          this.upLoaddialogVisible = false;
        }
      }
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleClose() {
      this.upLoaddialogVisible = false;
      this.fileList = [];
    },
    closeModal() {
      this.fileList = [];
      this.upNum = 0;
      this.getData();
    },
    upDateFile(id) {
      this.upDatedialogVisible = true;
      this.currListId = id;
    },
    beforeUpDateFile(file) {
      let testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "docx";
      const extension1 = testmsg === "doc";
      const extension2 = testmsg === "pdf";
      if (!extension && !extension1 && !extension2) {
        this.$toast("warning", "上传文件只能是doc,docx,pdf格式!");
        return false;
      } else {
        return true;
      }
    },
    overLimitUpDateFile(files, fileList) {
      this.fileListUpdate = [];
      this.$toast("warning", "更新时，只能上传与文档名称相同的文档!");
    },
    successUpLoadUpDateFile(response, file, fileList) {
      if (response.code === "200") {
        this.fileListUpdate = fileList;
        this.$toast("success", "上传成功");
        this.upDatedialogVisible = false;
      } else {
        this.$refs["upload0"].clearFiles();
        this.$toast("error", response.message);
      }
    },
    submitUpdate() {
      this.$refs["upload0"].submit();
    },
    closeModalUpdate() {
      this.fileListUpdate = [];
      this.getData();
    },
    handleCloseUpdate() {
      this.upDatedialogVisible = false;
      this.fileListUpdate = [];
      this.getData();
    }
  },
  mounted: function() {
    this.upLoadUrl = this.$urlApi.dataStandard.uploadFile();
    this.updateUrl = this.$urlApi.dataStandard.updateFile();
    this.downLoadFile = this.$urlApi.dataStandard.downLoadFile();
    this.$urlApi.dataStandard.getMenuDocumentContent().then(res => {
      this.nodeList = res.data;
      this.$nextTick(() => {
        if (res.data.length) {
          this.$refs.tree2 && this.$refs.tree2.setCurrentKey(res.data[0].id);
          this.nodeClick(res.data[0]);
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
.document-manage-section {
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
          .el-tree {
            .el-tree-node__content {
              position: relative;
              .nodeItem {
                width: 80%;
                /*     position: relative;*/
                .node-label {
                  display: inline-block;
                  width: 100%;
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
        .box-operate-b {
          height: -moz-calc(100% - 70px);
          height: -webkit-calc(100% - 70px);
          height: calc(100% - 70px);
          .download-document {
            color: #606266;
            text-decoration: none;
            font-size: 12px;
            cursor: pointer;
          }
          .download-document:hover {
            color: #f06273;
          }
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

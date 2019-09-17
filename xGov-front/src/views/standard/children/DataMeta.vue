<template>
  <section class="data-meta-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner dataMeta">
      <el-row>
        <el-form
          :model="dataMetaForm"
          size="small"
          ref="dataMetaForm"
          class="demo-form-inline"
          label-width="80px"
        >
          <div class="content">
            <el-row :gutter="60">
              <el-col :span="8">
                <el-form-item label="数据源: ">
                  <el-select
                    v-model="dataMetaForm.dataOrigin"
                    placeholder="请选择数据源"
                    filterable
                    :disabled="!dataOriginList.length"
                    @change="changeDataOriginBase"
                  >
                    <el-option
                      v-for="item in dataOriginList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="数据表: ">
                  <el-select
                    v-model="dataMetaForm.dataTable"
                    :disabled="!dataTableList.length"
                    @change="changeDataOriginTable"
                  >
                    <el-option
                      v-for="item in dataTableList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="60">
              <el-col :span="8">
                <el-form-item label="状态: ">
                  <el-select
                    v-model="dataMetaForm.status"
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
              <el-col :span="8">
                <el-form-item label="数据类型: ">
                  <el-select
                    v-model="dataMetaForm.category"
                    @change="changeDatatype"
                    placeholder="请选择数据类型"
                  >
                    <el-option
                      v-for="item in dataType"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-col :span="24">
              <el-button class="grey-btn" size="mini" @click="addNewMeta"
                ><svg-icon iconClass="g-新建"></svg-icon> 新建</el-button
              >
              <el-button
                class="grey-btn"
                size="mini"
                @click="sendAudit"
                :disabled="this.deleteSelection.length <= 0"
                ><svg-icon iconClass="g-送审"></svg-icon> 送审</el-button
              >
              <el-button
                class="grey-btn"
                size="mini"
                @click="deleteList('s', 1)"
                :disabled="this.deleteSelection.length <= 0"
                ><svg-icon iconClass="删除"></svg-icon> 删除</el-button
              >
              <el-dropdown
                style="margin: 0 10px"
                @command="handleCommand"
                size="mini"
              >
                <el-button class="grey-btn" size="mini"
                  ><svg-icon iconClass="g-更多"></svg-icon> 更多</el-button
                >
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="downloadTemplate"
                    >下载模板</el-dropdown-item
                  >
                  <el-dropdown-item command="importTemplate"
                    >导入</el-dropdown-item
                  >
                  <el-dropdown-item
                    command="exportTemplate"
                    :style="
                      this.deleteSelection.length <= 0
                        ? 'cursor: not-allowed'
                        : ''
                    "
                    >导出</el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown>
              <el-input
                v-model="dataMetaForm.name"
                @keyup.enter.native="getData(1)"
                placeholder="请输入要查询的数据标准名称"
                style="width: 300px"
                size="mini"
              >
                <i
                  slot="suffix"
                  @click="getData(1)"
                  class="el-input__icon el-icon-search searchBtn"
                ></i>
              </el-input>
            </el-col>
          </div>
        </el-form>
      </el-row>
      <el-row class="mt15" style="height: calc(100% - 180px)">
        <el-table
          :data="listAction"
          style="width: 100%;"
          @selection-change="changeFun"
          height="100%"
        >
          <template slot="empty">
            <div class="show-empty">
              暂无数据，请先<span style="color: #3BA3F8;cursor:pointer" @click="addNewMeta">新建标准</span>
            </div>
          </template>
          <el-table-column type="selection" width="55" :selectable="checkBox"> </el-table-column>
          <el-table-column label="标准名称" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <div
                type="text"
                style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"
                class="dataElementName-class"
                @click="showDataSource(scope.row)"
              >
                {{ scope.row.dataElementName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="dataElementCode"
            label="英文名称"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="dataType"
            :render-header="dataTypeRenderHeader"
          >
          </el-table-column>
          <el-table-column prop="codesetName" label="值域"> </el-table-column>
          <el-table-column label="描述">
            <template slot-scope="scope">
              <div
                class="cell"
                style="text-overflow:ellipsis; overflow: hidden; white-space: nowrap"
              >
                {{ scope.row.remark }}
              </div>
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
          <el-table-column width="220" label="操作">
            <template slot-scope="scope">
              <el-button
                v-if="
                  scope.row.status === 0 ||
                    scope.row.status === 3 ||
                    scope.row.status === 4
                "
                class="operate-btn operate-btns"
                @click="editData(scope.row.id)"
                type="text"
                >编辑</el-button
              >
              <el-button
                v-if="
                  scope.row.status === 0 ||
                    scope.row.status === 3 ||
                    scope.row.status === 4
                "
                class="operate-btn operate-btns"
                @click="deleteList(scope.row.id, 0)"
                type="text"
                >删除</el-button
              >
              <el-button
                v-if="scope.row.status === 2"
                class="operate-btn operate-btns"
                @click="conbinRelation(scope.row)"
                type="text"
                >关联关系</el-button
              >
              <el-button
                v-if="
                  scope.row.status === 1 ||
                    scope.row.status === 2 ||
                    scope.row.status === 3
                "
                class="operate-btn operate-btns"
                @click="showHistory(scope.row.id)"
                type="text"
                >查看历史</el-button
              >
              <el-button
                v-if="scope.row.status === 2"
                class="operate-btn operate-btns"
                @click="changeStatus(scope.row.id)"
                type="text"
                >变更</el-button
              >
              <el-button
                v-if="scope.row.status === 1"
                class="operate-btn operate-btns"
                @click="recall(scope.row.id)"
                type="text"
                >撤回</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
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
        :action="dataElementImport"
        :headers="headers"
        :on-preview="handlePreview"
        :before-upload="beforeUpload"
        :on-remove="handleRemove"
        :file-list="fileList"
        accept="xlsx,xls"
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
          导入文件前请先下载模板，只能上传xlsx, xls格式文件！
        </div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="closeImportDialog"
          >关 闭</el-button
        >
      </span>
    </el-dialog>
    <add-update-meta
      :AddUpdateVisible="AddUpdateVisible"
      :isEditMeta="isEditMeta"
      @AddUpdateVisibleBack="AddUpdateVisibleBack"
    ></add-update-meta>
    <set-combin-relation
      :SetCombinVisible="SetCombinVisible"
      :isEditCombin="isEditCombin"
      @SetCombinBack="SetCombinBack"
    ></set-combin-relation>
    <history-detail
      :HistoryVisible="HistoryVisible"
      :historyList="historyList"
      @HistoryBack="HistoryBack"
    ></history-detail>
  </section>
</template>

<script>
import AddUpdateMeta from "./dataMeta/AddUpdateMeta.vue";
import SetCombinRelation from "./dataMeta/SetCombinRealation.vue";
import ElRow from "element-ui/packages/row/src/row";
import ElButton from "../../../../node_modules/element-ui/packages/button/src/button.vue";
import HistoryDetail from "./dataMeta/HistoryDetail.vue";
export default {
  name: "data-meta",
  components: {
    ElButton,
    ElRow,
    AddUpdateMeta,
    SetCombinRelation,
    HistoryDetail
  },
  data() {
    return {
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
      buttonLoading: false,
      total: 0,
      currentPage: 1,
      pageLength: 10,

      HistoryVisible: false,
      addChangeVisible: false,
      AddUpdateVisible: false,
      SetCombinVisible: false,
      importVisible: false,
      fileList: [],
      dataElementImport: this.$urlApi.dataStandard.dataElementImport(),
      changeForm: {
        id: "",
        changeInfo: ""
      },
      isEditMeta: {
        id: "",
        currTime: new Date().getTime(),
        isHistory: false
      },
      isEditCombin: {
        id: "",
        currTime: new Date().getTime()
      },
      dataMetaForm: {
        dataOrigin: "",
        dataTable: "全部",
        status: "",
        category: "",
        name: ""
      },
      dataOriginList: [{ id: "", name: "全部" }],
      dataTableList: [],
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
      dataType: [
        { value: "", label: "全部", type: 0 },
        { value: "c", des: "固定长度字符", type: 0 },
        { value: "c..", des: "最多为xx长度的字符", type: 0 },
        { value: "a", des: "固定长度字母", type: 0 },
        { value: "a..", des: "最多为xx长度的字母", type: 0 },
        { value: "n", des: "固定长度数字", type: 0 },
        { value: "n..", des: "最多为xx长度的数字", type: 0 },
        { value: "an", des: "固定长度字母数字组合", type: 0 },
        { value: "an..", des: "最多为xx长度的字母数字组合", type: 0 },
        { value: "n..()", des: "输入两个正整数，并用英文字母隔开", type: 0 },
        { value: "d", des: "按YYYYMMDD显示的日期", type: "d" },
        { value: "t", des: "按hhmmss显示的时间", type: 1 },
        { value: "dt", des: "按YYYYMMDD hhmmss显示的日期时间", type: 1 }
        // {value:'b',des:'表示图片，图形等二进制类型',type:1},
      ],
      listAction: [],
      deleteSelection: [],
      historyList: {
        id: ""
      },
      headers: {
        token: this.$auth.getToken()
      }
    };
  },
  methods: {
    getData(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        dataSourceId: this.dataMetaForm.dataOrigin,
        ...(this.dataMetaForm.dataTable !== "全部"
          ? { tableName: this.dataMetaForm.dataTable }
          : {}),
        status: this.dataMetaForm.status,
        dataElementType: this.dataMetaForm.category,
        dataElementName: this.dataMetaForm.name
      };
      this.$urlApi.dataStandard.dataElementList(param).then(res => {
        if (type === "delete") {
          if (
            res.recordsFiltered % this.pageLength === 0 &&
            this.currentPage > 1
          ) {
            this.getData(res.recordsFiltered / this.pageLength);
          }
        }
        this.listAction = res.data;
        this.total = res.recordsFiltered;
      });
    },
    getAllDataSource() {
      this.$urlApi.dataSource.getAllDataSource({ pid: -1 }).then(res => {
        this.dataOriginList = this.dataOriginList.concat(res.data);
      });
    },
    changeDataOriginBase(val) {
      this.dataMetaForm.dataTable = "全部";
      if (val === "") {
        this.dataTableList = [];
      } else {
        this.$urlApi.dataSource
          .getDataTreeById({ pid: -2, id: val })
          .then(res => {
            if (res.data.length > 0) {
              this.dataTableList = res.data;
              this.dataTableList.unshift({ id: "", name: "全部" });
            }
          });
      }
      this.getData(1);
    },
    changeDataOriginTable(val) {
      this.getData(1);
    },
    changeDataStatus(val) {
      this.getData(1);
    },
    changeDatatype(val) {
      this.getData(1);
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
    handleCommand(command) {
      switch (command) {
        case "downloadTemplate":
          const downloadUrl = this.$urlApi.dataStandard.dataElementTemplateDownload();
          window.open(downloadUrl);
          break;
        case "importTemplate":
          this.importVisible = true;
          break;
        case "exportTemplate":
          if (this.deleteSelection.length <= 0) {
            return false;
          }
          // const params = this.deleteSelection.map(c => c.id).join(',')
          // const exportUrl = `http://www.baidu.com${params}`
          // window.open(exportUrl)
          break;

        default:
          break;
      }
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {},
    handlePreview(file) {},
    handleSuccess(res, file) {
      if (res.code === "200") {
        this.$toast("success", "导入成功");
        this.getData();
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
    addNewMeta() {
      let tempDate = new Date().getTime();
      this.AddUpdateVisible = true;
      this.isEditMeta.currTime = tempDate;
    },
    sendAudit() {
      let statusArr = this.deleteSelection.map(item => item.status).join();
      if (
        statusArr.indexOf("1") !== -1 ||
        statusArr.indexOf("2") !== -1 ||
        statusArr.indexOf("4") !== -1
      ) {
        this.$toast("warning", "只有草稿和变更中的标准维护可以送审");
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
          this.$urlApi.dataStandard.sendAudit({ ids: ids }).then(res => {
            this.getData();
            this.$toast("success", "送审成功");
          });
        })
        .catch(() => {});
    },
    editData(id) {
      let tempDate = new Date().getTime();
      this.AddUpdateVisible = true;
      this.isEditMeta.currTime = tempDate;
      this.isEditMeta.id = id;
    },
    deleteList(id, num) {
      let statusArr = this.deleteSelection.map(item => item.status).join();
      if (statusArr.indexOf("1") !== -1 || statusArr.indexOf("2") !== -1) {
        this.$toast("warning", "只有草稿、变更中及已退回的标准维护可以删除！");
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
          this.$urlApi.dataStandard.deleteElement({ ids: ids }).then(res => {
            this.getData(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .then(() => {
          setTimeout(() => {
            this.getData();
          }, 500);
        });
    },
    conbinRelation(row) {
      if (row.status !== 2) {
        return;
      }
      let tempDate = new Date().getTime();
      this.SetCombinVisible = true;
      this.isEditCombin.currTime = tempDate;
      this.isEditCombin.id = row.id;
      this.isEditCombin.name = row.dataElementName;
    },
    AddUpdateVisibleBack(cb) {
      this.AddUpdateVisible = false;
      this.isEditMeta.id = "";
      this.isEditMeta.isHistory = false;
      this.getData();
    },
    SetCombinBack(cb) {
      this.SetCombinVisible = false;
    },
    closeChangeModal() {
      this.$refs.addLayerForm && this.$refs.addLayerForm.resetFields();
      this.changeForm.changeInfo = "";
      this.addChangeVisible = false;
    },
    showHistory(id) {
      this.HistoryVisible = true;
      this.historyList.id = id;
    },
    HistoryBack() {
      this.HistoryVisible = false;
      this.historyList.id = "";
    },
    changeStatus(id) {
      this.$confirm("该标准维护变更后，关联字段将被删除，是否继续？", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      }).then(() => {
        this.addChangeVisible = true;
        this.changeForm.id = id;
      })
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
      return this.$urlApi.dataStandard.dataElementRecall({id: id})
    },
    submitChange(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.dataStandard
            .changeDataElement(this.changeForm)
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
    showDataSource(item) {
      let tempDate = new Date().getTime();
      this.AddUpdateVisible = true;
      this.isEditMeta.currTime = tempDate;
      this.isEditMeta.isHistory = true;
      this.isEditMeta.id = item.id;
    },
    dataTypeRenderHeader(h, { column, $index }) {
      return (
        <el-popover placement="right-start" trigger="hover">
          <div class="dataSourceType-bg" />
          <div slot="reference" class="cell">
            数据类型&nbsp;&nbsp;
            <svg-icon iconClass="help-circle" />
          </div>
        </el-popover>
      );
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
    checkBox(row, index) {
      if (row.status === 1 || row.status === 2) {
        return false
      } else {
        return true
      }
    }
  },
  mounted: function() {
    this.getData();
    this.getAllDataSource();
    if (this.$route.query.type === 'add') {
        this.addNewMeta();
    }
  }
};
</script>

<style lang="scss">
.data-meta-section {
  padding: 108px 15px;
  .dataMeta {
    height: calc(100% - 35px);
  }
  .el-table__empty-block {
    align-items: baseline;
    .show-empty {
      margin-top: 20px;
    }
  }
  .content {
    .el-input .el-input__inner {
      width: 100% !important;
    }
    .el-select {
      display: block;
    }
    .el-select .el-input__inner {
      width: 100% !important;
    }
    [class*="el-col-12"] {
      float: left;
    }
    .searchBtn {
      color: #797b7b;
      cursor: pointer;
    }
    .searchBtn:hover {
      color: #3ba3f8;
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
.dataSourceType-bg {
  width: 800px;
  height: 520px;
  background: url(./../../../assets/images/dataSourceType.png) no-repeat center;
  background-size: contain;
}
@media screen and (max-width: 1400px) {
  .dataSourceType-bg {
    width: 600px;
    height: 400px;
  }
}
</style>

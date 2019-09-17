<template>
  <section class="storage-check">
    <el-row class="search-condition ar mb10">
      <el-input
        placeholder="请输入任务名称进行查询"
        style="width: 300px"
        size="small"
        @keyup.enter.native="searchList"
        v-model="dataSourceName"
      >
        <i
          slot="suffix"
          @click="searchList"
          style="cursor: pointer"
          class="el-input__icon el-icon-search searchBtn"
        ></i>
      </el-input>
      <!-- <el-button type="danger" size="mini" @click="searchList">查 询</el-button> -->
    </el-row>
    <ul class="storage-check-list">
      <div v-if="sourceList.length === 0" class="no-data-pic-outer valign">
        <div class="no-data-box">
          <img
            class="no-data-pic"
            src="../../../../assets/images/noDataPic.png"
          />
          <p class="no-data-text">暂无数据，请先<span class="redText">添加采集任务至待审核</span></p>
        </div>
      </div>
      <li :key="item.id" v-for="item in sourceList">
        <p class="check-header over" :title="item.taskName">
          {{ item.taskName }}
        </p>
        <div class="check-body">
          <span>{{ item.dataSourceName }}</span
          ><br />
        </div>
        <div class="check-set">
          <span @click="checkTask(item.datasourceId)"
            ><svg-icon iconClass="审核"></svg-icon
          ></span>
        </div>
      </li>
    </ul>
    <el-pagination
      v-if="sourceList.length !== 0"
      class="ar mt15"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="12"
      layout="total, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <el-dialog
      :close-on-click-modal="false"
      title="数据源入库审核"
      :visible.sync="dialogVisible"
      width="760px"
      @close="closeOuterModal"
      :before-close="handleClose"
    >
      <el-dialog
        :close-on-click-modal="false"
        width="500px"
        title="审核"
        :visible.sync="innerVisible"
        @closed="closeInnerForm"
        @open="openInner"
        append-to-body
      >
        <el-row style="font-size: 12px">
          <el-col :span="12">新增表 : {{ InnerForm.newMetadataNum }}</el-col>
          <el-col :span="12">删除表 ：{{ InnerForm.deleteMetadataNum }}</el-col>
        </el-row>
        <el-form
          :model="InnerForm"
          ref="InnerForm"
          size="small"
          :rules="rules"
          label-width="70px"
          label-position="right"
        >
          <el-form-item label="审核结果">
            <el-radio-group v-model="InnerForm.status" @change="changeRadio">
              <el-radio label="1">审核通过</el-radio>
              <el-radio label="2">驳回</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="描述" prop="desc">
            <el-input
              :rows="3"
              type="textarea"
              v-model="InnerForm.desc"
            ></el-input>
          </el-form-item>
          <p style="font-size: 12px;padding-left: 15px;">
            <i class="el-icon-warning"></i> {{ InnerForm.warning }}
          </p>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="info" size="small" @click="closeInnerForm"
            >取 消</el-button
          >
          <el-button
            type="danger"
            size="small"
            @click="submitMetaForm('InnerForm')"
            :loading="buttonLoading"
            >保 存</el-button
          >
        </span>
      </el-dialog>
      <el-table
        :data="gridData"
        v-loading="loadingTable"
        height="320"
        style="width: 100%"
      >
        <el-table-column
          prop="startTime"
          label="采集时间"
          :formatter="this.$utils.dateFormat"
        >
        </el-table-column>
        <el-table-column prop="spent" label="执行时长(s)"> </el-table-column>
        <el-table-column width="100" label="操作">
          <template slot-scope="scope">
            <el-button
              class="operate-btn"
              @click="checkMetaData(scope.row.id)"
              type="text"
              >审核</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="ar mt15"
        @current-change="handleCurrentChangeInner"
        :current-page="currentPageInner"
        :page-size="10"
        layout="total, prev, pager, next"
        :total="totalInner"
      >
      </el-pagination>
    </el-dialog>
  </section>
</template>

<script>
import SvgIcon from "../../../../components/SvgIcon.vue";
import ElRow from "element-ui/packages/row/src/row";
import ElCol from "element-ui/packages/col/src/col";

export default {
  name: "",
  components: {
    ElCol,
    ElRow,
    SvgIcon
  },
  data() {
    return {
      currentPageInner: 1,
      totalInner: 0,
      pageLengthInner: 10,

      innerVisible: false,
      dialogVisible: false,
      dataSourceName: "",
      total: 0,
      currentPage: 1,
      pageLength: 12,
      sourceList: [],
      InnerForm: {
        sourceId: "",
        collectHisId: "",
        deleteMetadataNum: "",
        newMetadataNum: "",
        desc: "已通过",
        status: "1",
        warning:
          "审核通过后，系统以本次采集结果更新元数据库，历史采集未入库记录将失效。"
      },
      currId: "",
      loadingTable: false,
      gridData: [],
      buttonLoading: false,
      rules: {
        desc: [
          { required: true, message: "请填写描述信息", trigger: "change" },
          {
            min: 1,
            max: 255,
            message: "长度在 1 到 255 个字符",
            trigger: "blur"
          }
        ]
      }
    };
  },
  props: ["StorageCheckTabTime"],
  methods: {
    changeRadio(cb) {
      if (cb == 1) {
        this.InnerForm.desc = "已通过";
        this.InnerForm.warning =
          "审核通过后，系统以本次采集结果更新元数据库，历史采集未入库记录将失效。";
      } else {
        this.InnerForm.desc = "已驳回";
        this.InnerForm.warning = "驳回后，系统将放弃更新本次元数据变更信息。";
      }
    },
    openInner() {
      let param = {
        sourceId: this.InnerForm.sourceId,
        collectHisId: this.InnerForm.collectHisId
      };
      this.InnerForm.desc = "已通过";
      this.InnerForm.status = "1";
      this.$urlApi.metaData.compareData(param).then(res => {
        this.InnerForm.newMetadataNum = res.data.newMetadataNum;
        this.InnerForm.deleteMetadataNum = res.data.deleteMetadataNum;
      });
    },
    getCheckList(page) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        taskName: this.dataSourceName
      };
      this.$urlApi.metaData.getcheckList(param).then(res => {
        this.sourceList = res.data;
        this.total = res.recordsFiltered;
      });
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.getCheckList(1);
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getCheckList();
    },
    searchList() {
      this.getCheckList(1);
    },
    checkTask(id) {
      this.dialogVisible = true;
      this.currId = id;
      this.InnerForm.sourceId = id;
      let params = {
        sourceId: this.currId,
        start: (this.currentPageInner - 1) * 10,
        length: this.pageLengthInner
      };
      this.loadingTable = true;
      this.$urlApi.metaData.unreleaseCollecthis(params).then(res => {
        this.loadingTable = false;
        this.gridData = res.data;
        this.totalInner = res.recordsFiltered;
      });
    },
    handleCurrentChangeInner(cb) {
      this.currentPageInner = cb;
      this.loadingTable = true;
      let params = {
        sourceId: this.currId,
        start: (this.currentPageInner - 1) * 10,
        length: this.pageLengthInner
      };
      this.$urlApi.metaData.unreleaseCollecthis(params).then(res => {
        this.loadingTable = false;
        this.gridData = res.data;
        this.totalInner = res.recordsFiltered;
      });
    },
    handleClose() {
      this.dialogVisible = false;
      this.innerVisible = false;
      this.gridData = [];
    },
    closeOuterModal() {
      this.getCheckList();
    },
    checkMetaData(collectHisId) {
      this.innerVisible = true;
      this.InnerForm.collectHisId = collectHisId;
    },
    submitMetaForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.metaData
            .submitCompareCheck(this.InnerForm)
            .then(res => {
              this.buttonLoading = false;
              this.InnerForm.status === "1" &&
                this.$toast("success", "审核通过，元数据库已更新");
              this.InnerForm.status === "2" &&
                this.$toast("success", "元数据入库已驳回");

              this.$refs["InnerForm"].resetFields();
              this.InnerForm.newMetadataNum = "";
              this.InnerForm.deleteMetadataNum = "";
              this.innerVisible = false;
            })
            .then(res => {
              let params = {
                sourceId: this.currId,
                start: (this.currentPageInner - 1) * 10,
                length: this.pageLengthInner
              };
              this.$urlApi.metaData.unreleaseCollecthis(params).then(res => {
                this.gridData = res.data;
                this.totalInner = res.recordsFiltered;
              });
            })
            .catch(e => {
              this.buttonLoading = false;
              this.$toast("error", "审核操作超时，请稍后重试");
            });
        } else {
          return false;
        }
      });
    },
    closeInnerForm() {
      this.$refs["InnerForm"].resetFields();
      this.InnerForm.newMetadataNum = "";
      this.InnerForm.deleteMetadataNum = "";
      this.innerVisible = false;
    }
  },
  mounted: function() {
    this.getCheckList();
  },
  watch: {
    StorageCheckTabTime(val, oldVal) {
      this.getCheckList();
    }
  }
};
</script>

<style lang="scss">
.storage-check {
  height: 100%;
  .el-dialog__body {
    padding-left: 20px;
    padding-right: 20px;
    padding-bottom: 40px;
  }
  .searchBtn {
    color: #797b7b;
    cursor: pointer;
  }
  .searchBtn:hover {
    color: #3ba3f8;
  }
  .storage-check-list {
    height: calc(100% - 87px);
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    overflow: auto;
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
      transition: linear 2s;
      height: 95px;
      .check-header {
        line-height: 32px;
        background: #acb5c3;
        color: #ffffff;
        margin: 0;
      }
      .check-body {
        background: rgba(249, 249, 250, 1);
        padding-top: 19px;
        box-sizing: border-box;
        padding-bottom: 16px;
        span {
          line-height: 28px;
          font-size: 16px;
          font-weight: bold;
        }
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
.el-radio__label {
  font-size: 12px !important;
}
</style>

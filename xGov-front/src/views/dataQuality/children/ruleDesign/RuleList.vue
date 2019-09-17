<template>
  <section class="rule-design-list main-outer">
    <el-row class="rule-design-b main-inner" v-loading="buttonLoading">
    <el-row>
      <el-input
        placeholder="请输入数据源名称查询"
        style="width: 300px"
        size="mini"
        @keyup.enter.native="searchList"
        v-model="sourceName"
      >
      </el-input>
      <el-button
        style="margin-left: 10px"
        size="mini"
        type="danger"
        @click="searchList"
        >查询</el-button
      >
    </el-row>
      <ul class="DBlist">
        <div v-if="tableData.length === 0" class="no-data-pic-outer valign">
          <div class="no-data-box">
            <img
              class="no-data-pic"
              src="../../../../assets/images/noDataPic.png"
            />
            <p class="no-data-text">暂无数据，现在<span class="redText" @click="addDataSource"> 关联数据源</span></p>
          </div>
        </div>
        <li
          :key="item.id"
          v-for="item in tableData"
        >
          <div class="check-header">
            <span style="cursor: pointer" @click='showDatasource(item)'>{{ item.datasourceName }}</span>
            <div v-if="item.loading" style="float: right;padding: 0 10px;font-size: 16px;">
              <i class="el-icon-loading"></i>
            </div>
            <div v-else @click="(e) => exportRuleDesign(item, e)" title="导出规则" style="float: right;padding: 0 10px;font-size: 16px;"><svg-icon iconClass='rule-export'></svg-icon></div>
          </div>
          <div @click="() => goDetils(item)" class="check-body" style="float: left">
            <span
              ><span style="color: #2c3e50;font-size: 30px;font-weight:blod">{{
                item.tableNumber || 0
              }}</span
              ><span style="color: #2c3e50;font-size: 13px;margin-right: 5px;">
                张</span
              ><span style="color:rgba(78,78,78,0.6)"
                >表已设置校验规则</span
              ></span
            >
          </div>
          <div class="check-body-server" style="float: left">
            <div>服务器地址：</div>
            <div>{{ item.sourceAddress }}</div>
          </div>
          <div class="check-set">
            <span @click="goDetils(item)">
              <el-tooltip
                class="item"
                effect="dark"
                content="规则设计"
                placement="top"
              >
                <i class="table-icon-menu"
                  ><svg-icon iconClass="编辑1"></svg-icon
                ></i>
              </el-tooltip>
            </span>
            <span @click="deleteList(item.id)">
              <svg-icon iconClass="删除"></svg-icon>
            </span>
          </div>
        </li>
      </ul>
      <el-pagination
        v-if="tableData.length !== 0"
        class="ar mt15 pagination"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="12"
        layout="total, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-row>
    <set-rule-design
      :setRuleDesignVisible="setRuleDesignVisible"
      :modalTime="modalTime"
      @setRuleDesignBack="setRuleDesignBack"
    ></set-rule-design>
    <rule-detail
      :ruleDetail="ruleDetail"
      :id="sourceInfoId"
      :name="dataSourceName"
      @setRuleDetailfoBack="setRuleDetailfoBack"
    ></rule-detail>
    <el-dialog
      title="数据源详情"
      :visible.sync="dialog.visible"
      :before-close="closeDialog"
      :lock-scroll="false"
      width="50rem"
      @open="openDialog"
      :close-on-click-modal="false">
      <ViewDataSource ref="viewDataSource"></ViewDataSource>
    </el-dialog>
  </section>
</template>

<script>
import RuleDetail from "./RuleDetail.vue";
import SetRuleDesign from "./SetRuleDesign.vue";
import ElRow from "element-ui/packages/row/src/row";
import ViewDataSource from './../components/ViewDataSource'
export default {
  name: "",
  components: {
    ElRow,
    SetRuleDesign,
    RuleDetail,
    ViewDataSource
  },
  data() {
    return {
      setRuleDesignVisible: false,
      modalTime: new Date().getTime(),
      ruleDetail: false,
      sourceName: "",
      total: 0,
      currentPage: 1,
      pageLength: 12,
      tableData: [],
      buttonLoading: false,
      sourceInfoId: null,
      dataSourceName: null,
      dialog: {
        visible: false
      }
    };
  },
  methods: {
    getList() {
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        sourceName: this.sourceName
      };
      this.$urlApi.dataQuality.listQualitySource(param).then(res => {
        this.tableData = res.data.map(c => {
          c.loading = false;
          return c
        });
        this.total = res.recordsFiltered;
        this.getNumber();
      });
    },
    getNumber() {
      this.$urlApi.dataQuality.countQualityTable().then(res => {
        this.tableData = this.tableData.map(c => {
          const tableNumber = res.data.find(k => k.datasourceId == c.id);
          c.tableNumber = tableNumber ? tableNumber.tableNum : 0;
          return c;
        });
      });
    },
    searchList() {
      this.getList();
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.currentPage = 1;
      this.getList();
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getList();
    },
    addNewData() {
      this.modalTime = new Date().getTime();
      this.setRuleDesignVisible = true;
    },
    goDetils: function(item) {
      this.sourceInfoId = item.id;
      this.dataSourceName = item.dataSourceName;
      this.ruleDetail = true;
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
          this.buttonLoading = true;
          this.$urlApi.dataQuality
            .deleteSourceDesign({ designSourceId: id })
            .then(res => {
              this.buttonLoading = false;
              this.getList();
              if (res.code == "407") {
                this.$toast("error", res.message);
              } else {
                this.$toast("success", "删除成功");
              }
            });
        })
        .catch(() => {});
    },
    setRuleDesignBack(cb) {
      this.setRuleDesignVisible = false;
      if (cb === 1) {
        this.getList();
      }
    },
    setRuleDetailfoBack() {
      this.ruleDetail = false;
      this.getList();
    },
    addDataSource() {
      this.$router.push('/systemSet/default/datasource')
    },
    exportRuleDesign(item, e) {
      const index = this.tableData.findIndex(c => c.id === item.id)
      const currentItem = this.tableData[index]
      currentItem.loading = true
      this.$urlApi.dataQuality.exportQualityTaskDetail({sourceId: item.id}).then((res) => {
        if (res.code === '200') {
          this.download(res.data).then(() => currentItem.loading = false).catch(e => currentItem.loading = false)
        }
      }).catch(e => {
        currentItem.loading = false
      })
      e.stopPropagation();
      e.preventDefault();
    },
    download(fileName) {
      return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        const url = this.$urlApi.dataQuality.downloadQualityTaskDetail();
        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.setRequestHeader(
          "token",
          this.$auth.getToken()
        );
        xhr.responseType = "blob";
        xhr.onload = function(event) {
          const res = xhr.response;
          const link = document.createElement("a");
          link.download = fileName;
          link.style.display = "none";
          const blob = new Blob([res]);
          link.href = URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          resolve()
        };
        xhr.send(`fileName=${fileName}`);
      })
    },
    showDatasource (data) {
      this.dialog.visible = true
      this.dialog.data = data
    },
    openDialog () {
      this.$nextTick(() => {
        this.$refs.viewDataSource.init(this.dialog.data)
      })
    },
    closeDialog () {
      this.dialog.visible = false
      this.dialog.data = ''
    }
  },
  mounted: function() {
    this.getList();
  }
};
</script>

<style lang="scss">
.rule-design-list {
  height: calc(100% - 35px);
  .rule-design-b {
    height: 100%;
    padding: 45px 29px 13px 29px!important;
    .DBlist {
      height: calc(100% - 85px);
      overflow: auto;
      padding-top: 5px;
      margin-top: 20px;
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
      li {
        float: left;
        margin-right: 60px;
        overflow: hidden;
        margin-bottom: 30px;
        text-align: center;
        width: 228px;
        box-shadow: 0px 1px 4px 0px rgba(125, 125, 125, 0.5);
        border-radius: 2px;
        position: relative;
        height: 142px;
        box-sizing: border-box;
        .check-header {
          line-height: 35px;
          font-size: 14px;
          text-align: left;
          margin: 0;
          font-weight: bold;
          border-left: 4px solid #54618a;
          padding-left: 20px;
        }
        .check-body {
          cursor: pointer;
          width: 100%;
          padding: 12px;
          padding-left: 20px;
          box-sizing: border-box;
          text-align: left;
          color: #5e6977;
          border-left: 4px solid #54618a;
          span {
            font-size: 12px;
          }
        }
        .check-body-server {
          width: 100%;
          text-align: left;
          padding: 8px;
          padding-left: 20px;
          border-left: 4px solid #babecb;
          border-top: 1px solid rgba(151, 151, 151, 0.16);
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
          padding-left: 48px;
          box-sizing: border-box;
          transition: 0.5s;
          span {
            width: 45px;
            float: left;
            height: 100%;
            font-size: 16px;
            line-height: 32px;
            text-align: center;
            color: #ffffff;
          }
        }
      }
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
    }
  }

  .el-form-item {
    margin: 0 !important;
  }
  .el-form--label-top .el-form-item__label {
    padding: 0 !important;
  }
}
</style>

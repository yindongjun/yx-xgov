<template>
  <section class="meta-analyze main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner-allSize">
      <div class="analyze-t">
        <p style="margin-top: 0;font-size: 13px">元数据名称</p>
        <el-row>
          <el-input
            v-model="searchName"
            @keyup.enter.native="searchList"
            size="mini"
            style="width: 300px"
          ></el-input>
          <el-button type="danger" size="mini" class="ml10" @click="searchList"
            >查 询</el-button
          >
          <el-button
            type="text"
            size="mini"
            class="superSearch"
            @click="dialogVisible = true"
            >高级搜索</el-button
          >
        </el-row>
      </div>
      <ul class="analyze-b">
        <div v-if="tableData3.length === 0" class="no-data-pic-outer valign">
          <div class="no-data-box">
            <img
              class="no-data-pic"
              src="../../../assets/images/noDataPic.png"
            />
            <p class="no-data-text">暂无数据元分析数据</p>
          </div>
        </div>
        <li
          v-else
          v-for="item in tableData3"
          :key="item.id"
          class="analyze-b-list"
        >
          <p class="check-header over" :title="item.name">{{ item.name }}</p>
          <div class="check-body over">
            <span :title="item.name">{{ item.name }}</span
            ><br />
            元数据名称
          </div>
          <div class="check-set">
            <span @click="checkTask(item.id)"
              ><i class="el-icon-search"></i
            ></span>
          </div>
        </li>
      </ul>
      <el-pagination
        v-if="tableData3.length !== 0"
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
    <el-dialog
      :close-on-click-modal="false"
      title="提示"
      :visible.sync="dialogVisible"
      class="searchDialog"
      width="760px"
    >
      <el-form
        :model="searchForm"
        ref="searchForm"
        size="medium"
        label-width="100px"
        label-position="right"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="元模型分类">
              <el-cascader
                expand-trigger="hover"
                :options="options"
                v-model="searchForm.Modeltype"
                :show-all-levels="false"
                style="width: 100%"
                size="medium"
              >
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="元模型">
              <el-select v-model="searchForm.model">
                <el-option value="1" label="质量任务"></el-option>
                <el-option value="2" label="脚本"></el-option>
                <el-option value="3" label="输入源"></el-option>
                <el-option value="4" label="输入源实例"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="元数据分类">
          <el-select v-model="searchForm.datatype">
            <el-option value="1" label="基本元数据"></el-option>
            <el-option value="2" label="技术元数据"></el-option>
            <el-option value="3" label="管理元数据"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="元数据名称">
          <el-input v-model="searchForm.name"></el-input>
        </el-form-item>
        <div class="addField">
          <el-button type="danger" size="mini" @click="addProperty"
            ><svg-icon iconClass="添加"></svg-icon> 新建</el-button
          >
          <el-button type="danger" size="mini" @click="removeProperty"
            ><svg-icon iconClass="删除"></svg-icon> 删除</el-button
          >
        </div>
        <el-table
          ref="multipleTable"
          :data="tableData3"
          @selection-change="handleSelectionChange"
          style="width: 100%"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column label="属性名称">
            <template slot-scope="scope">
              <el-select v-model="scope.row.name" size="mini">
                <el-option value="1" label="标准维护名称"></el-option>
                <el-option value="2" label="标准维护标记"></el-option>
                <el-option value="3" label="数据类型"></el-option>
                <el-option value="4" label="标准维护格式"></el-option>
                <el-option value="5" label="定义"></el-option>
                <el-option value="6" label="值域"></el-option>
                <el-option value="7" label="数据范围"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="属性值">
            <template slot-scope="scope">
              <el-input size="mini" v-model="scope.row.date"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" type="info" size="small"
          >取 消</el-button
        >
        <el-button type="danger" size="small" @click="submitForm('searchForm')"
          >搜 索</el-button
        >
      </span>
    </el-dialog>
    <detail-info
      :isEditRelation="isEditRelation"
      :infoVisible="infoVisible"
      @detailHandleback="detailHandleback"
    ></detail-info>
  </section>
</template>

<script>
import DetailInfo from "./analyze/DetailInfo.vue";
export default {
  name: "",
  components: { DetailInfo },
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 12,

      searchName: "",
      dialogVisible: false,
      infoVisible: false,
      searchForm: {
        Modeltype: [],
        model: "",
        datatype: "",
        name: ""
      },
      tableData3: [],
      multipleSelection: [],
      options: [],

      isEditRelation: {
        currTime: new Date().getTime(),
        id: ""
      }
    };
  },
  methods: {
    getData() {
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        name: this.searchName
      };
      this.$urlApi.metaData.analyzeMetalist(param).then(res => {
        this.tableData3 = res.data;
        this.total = res.recordsFiltered;
      });
    },
    searchList() {
      this.getData();
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.currentPage = 1;
      this.getData();
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getData();
    },
    checkTask(id) {
      let tempDate = new Date().getTime();
      this.infoVisible = true;
      this.isEditRelation.id = id;
      this.isEditRelation.currTime = tempDate;
    },
    detailHandleback(cb) {
      this.infoVisible = false;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    addProperty() {
      this.tableData3.push({
        date: "",
        name: ""
      });
    },
    removeProperty() {
      this.$toast("info", "删除");
    },
    submitForm() {
      this.dialogVisible = false;
    }
  },
  mounted: function() {
    this.getData();
  }
};
</script>

<style lang="scss">
.meta-analyze {
  height: 100%;
  .analyze-t {
    .superSearch {
      margin-left: 5px;
      color: #3e4456;
    }
  }
  .analyze-b {
    margin-top: 30px;
    height: -moz-calc(100% - 140px);
    height: -webkit-calc(100% - 140px);
    height: calc(100% - 140px);
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
    .analyze-b-list {
      float: left;
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
      height: 110px;
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
  .searchDialog {
    .el-table {
      td,
      .el-table th {
        padding: 6px 0;
      }
    }
  }
}
</style>


<template>
  <section class="collect-list">
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
      <el-button size="small" class="ml10" type="danger" @click="addCollect(-1)"
        >新建采集</el-button
      >
      <el-button
        size="small"
        type="danger"
        @click="deleteCollect('s', 1)"
        :disabled="this.deleteSelection.length <= 0"
        >删除</el-button
      >
    </el-row>
    <div v-if="sourceList.length === 0" class="no-data-pic-outer valign">
      <div class="no-data-box">
        <img
          class="no-data-pic"
          src="../../../../assets/images/noDataPic.png"
        />
        <p class="no-data-text">暂无数据，请先<span class="redText"  @click="addCollect(-1)"> 新建采集任务</span></p>
      </div>
    </div>
    <el-table
      :data="sourceList"
      style="width: 100%"
      @selection-change="changeFun"
      height="calc(100% - 87px)"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column
        prop="taskName"
        :show-overflow-tooltip="true"
        label="任务名称"
      >
      </el-table-column>
      <el-table-column label="数据源名称">
        <template slot-scope="scope">
          <div v-if="scope.row.deleteFlag === '2'" style="color: #e52100">
            {{ scope.row.dataSourceName }}(已删除)
          </div>
          <div v-if="scope.row.deleteFlag === '0'">
            {{ scope.row.dataSourceName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="sourceType" label="数据库类型"> </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        :formatter="this.$utils.dateFormat"
      >
      </el-table-column>
      <el-table-column label="操作" width="170">
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.deleteFlag === '2'"
            class="operate-btn"
            @click="addCollect(scope.row.id)"
            type="text"
            >编辑</el-button
          >
          <el-button
            class="operate-btn"
            @click="deleteCollect(scope.row.id)"
            type="text"
            >删除</el-button
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
    <add-update-collect
      :editCollectVisible="editCollectVisible"
      :isEditRelation="isEditRelation"
      @editCollectBack="editCollectBack"
    ></add-update-collect>
  </section>
</template>
<script>
import AddUpdateCollect from "./AddUpdateCollect.vue";
export default {
  name: "",
  props: ["CollectListTabTime"],
  components: { AddUpdateCollect },
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],
      isEdit: false,
      isEditRelation: {
        currTime: new Date().getTime(),
        id: ""
      },
      editCollectVisible: false,
      dataSourceName: "",
      sourceList: []
    };
  },
  methods: {
    getList(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        taskName: this.dataSourceName
      };
      this.$urlApi.metaData.getCollectList(param).then(res => {
        if (type === "delete") {
          if (
            res.recordsFiltered % this.pageLength === 0 &&
            this.currentPage > 1
          ) {
            this.getList(res.recordsFiltered / this.pageLength);
          }
        }
        this.sourceList = res.data;
        this.total = res.recordsFiltered;
      });
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.getList(1);
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getList();
    },
    changeFun(val) {
      this.deleteSelection = val;
    },
    testAlert(id) {
      this.$confirm("是否提交采集任务?", "提交任务", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        type: "warning",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.metaData.submitTask({ taskId: id }).then(res => {
            this.$toast("success", "提交成功!");
            this.getList();
          });
        })
        .catch(() => {});
    },
    addCollect(num) {
      let tempDate = new Date().getTime();
      this.editCollectVisible = true;
      this.isEditRelation.id = num;
      this.isEditRelation.currTime = tempDate;
    },
    deleteCollect(id) {
      let ids = this.deleteSelection.map(item => item.id).join();
      const data = id === "s" ? { ids: ids } : { ids: id };
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.metaData.deleteCollect(data).then(res => {
            this.getList(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .catch(() => {});
    },
    searchList() {
      this.getList(1);
    },
    editCollectBack(cb) {
      this.editCollectVisible = false;
      this.isEditRelation.id = -1;
      if (cb === 1) {
        this.getList();
      }
    }
  },
  mounted: function() {
    this.getList();
  },
  watch: {
    CollectListTabTime(val, oldVal) {
      this.getList();
    }
  }
};
</script>

<style lang="scss">
.collect-list {
  height: 100%;
  .searchBtn {
    color: #797b7b;
    cursor: pointer;
  }
  .searchBtn:hover {
    color: #3ba3f8;
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
</style>

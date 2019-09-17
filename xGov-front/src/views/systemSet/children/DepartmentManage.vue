<template>
  <section class="meta-collect main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner-allSize">
      <div class="DM-top">
        <el-row :gutter="20">
          <el-col :span="6">
            <div>
              <span>所属数据源</span><br />
              <el-input
                v-model="input"
                placeholder="请输入内容"
                size="small"
                class="mt5"
              ></el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <span>问题责任人</span><br />
              <el-input
                v-model="input"
                placeholder="请输入内容"
                size="small"
                class="mt5"
              ></el-input>
            </div>
          </el-col>
          <el-col :span="4">
            <div>
              <el-button type="danger" size="small" style="margin-top:20px"
                >查询</el-button
              >
            </div>
          </el-col>
        </el-row>
        <el-col class="mt15 mb5">
          <el-button icon="el-icon-document" size="small">新建</el-button>
          <el-button icon="el-icon-delete" size="small">删除</el-button>
        </el-col>
      </div>
      <div class="db-table">
        <el-table :data="tableData" height="80%" style="width: 100%">
          <el-table-column
            type="selection"
            width="55"
            :selectable="checkDisabled"
          >
          </el-table-column>
          <el-table-column prop="name" label="部门名称"> </el-table-column>
          <el-table-column prop="code" label="组织机构代码"> </el-table-column>
          <el-table-column prop="do" label="操作" width="220">
            <template>
              <el-button type="text" class="operate-btn">修改</el-button>
              <el-button type="text" class="operate-btn">添加子部门</el-button>
              <el-button type="text" class="operate-btn"
                >调整人员部门</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination class="mt15 ar" layout="prev, pager, next" :total="40">
        </el-pagination>
      </div>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  data() {
    return {
      input: "",
      tableData: [
        {
          name: "mysql",
          code: 1024
        },
        {
          name: "db",
          code: 1306
        }
      ]
    };
  },
  methods: {
    checkDisabled() {}
  },
  mounted: function() {}
};
</script>

<style lang="scss">
.DM-top {
  height: 100px;
}
.db-table {
  padding-top: 10px;
  height: -moz-calc(100% - 100px);
  height: -webkit-calc(100% - 100px);
  height: calc(100% - 100px);
  th {
    background: rgba(245, 247, 250, 1) !important;
  }
}
</style>

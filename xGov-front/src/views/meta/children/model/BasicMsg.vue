<template>
  <section class="basic-msg">
    <el-row class="basic-msg-t">
      <el-col :span="12">模型代码：{{ BasicMsgTab.code }}</el-col>
      <el-col :span="12">模型名称：{{ BasicMsgTab.name }}</el-col>
      <el-col :span="12">模型描述：{{ BasicMsgTab.discription }}</el-col>
      <el-col :span="12">作者：{{ BasicMsgTab.createUser }}</el-col>
      <el-col :span="12"
        >图标：<svg-icon :iconClass="BasicMsgTab.icoId"></svg-icon
      ></el-col>
    </el-row>
    <el-row class="basic-msg-b">
      <div class="model-title">
        <span class="model-title-l">属性列表</span>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column
          prop="name"
          :show-overflow-tooltip="true"
          label="属性名称"
        >
        </el-table-column>
        <el-table-column
          prop="code"
          :show-overflow-tooltip="true"
          label="属性代码"
        >
        </el-table-column>
        <el-table-column prop="type" label="数据类型"> </el-table-column>
        <el-table-column prop="length" label="长度"> </el-table-column>
        <el-table-column label="是否只读">
          <template slot-scope="scope">
            <span v-if="scope.row.isRead === 'Y'">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column label="是否必填">
          <template slot-scope="scope">
            <span v-if="scope.row.required === 'Y'">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column label="是否内置">
          <template slot-scope="scope">
            <span v-if="scope.row.buildin === 'Y'">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  props: ["BasicMsgTab", "BasicMsgTabTime"],
  data() {
    return {
      tableData: []
    };
  },
  methods: {
    getMetamodelAttr() {
      this.$urlApi.metaData
        .getMetamodelAttr({ modelId: this.BasicMsgTab.id })
        .then(res => {
          this.tableData = res.data;
        });
    }
  },
  mounted: function() {
    this.getMetamodelAttr();
  },
  watch: {
    BasicMsgTabTime(val, oldVal) {
      this.getMetamodelAttr();
    }
  }
};
</script>

<style lang="scss">
.basic-msg {
  .basic-msg-t {
    padding: 20px 14px 40px 20px;
    font-size: 13px;
    line-height: 30px;
  }
}
</style>

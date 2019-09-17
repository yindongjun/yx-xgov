<template>
  <section class="inherit-relation">
    <el-row class="inherit-relation-t">
      <div class="model-title">
        <span class="model-title-l">父类列表</span>
      </div>
      <el-table :data="tableDataParent" style="width: 100%">
        <el-table-column prop="datasourceName" label="父类代码">
        </el-table-column>
        <el-table-column prop="databaseType" label="父类名"></el-table-column>
        <el-table-column prop="dbname" label="是否挂在元数据">
        </el-table-column>
        <el-table-column prop="createTime" label="描述"></el-table-column>
      </el-table>
    </el-row>
    <el-row class="inherit-relation-b mt15">
      <div class="model-title">
        <span class="model-title-l">子类列表</span>
      </div>
      <el-table :data="tableDataChildren" style="width: 100%">
        <el-table-column prop="datasourceName" label="父类代码">
        </el-table-column>
        <el-table-column prop="databaseType" label="父类名"> </el-table-column>
        <el-table-column prop="dbname" label="是否挂在元数据">
        </el-table-column>
        <el-table-column prop="createTime" label="描述"> </el-table-column>
      </el-table>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  data() {
    return {
      tableDataParent: [],
      tableDataChildren: []
    };
  },
  methods: {},
  mounted: function() {}
};
</script>

<style lang="scss">
</style>

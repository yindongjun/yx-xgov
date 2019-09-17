<template>
  <section class="use_self">
    <div class="use_self_left pl10">
      <h4>部门列表</h4>
      <h4>数据管理</h4>
      <h4>运维</h4>
    </div>
    <div class="use_self_right pl10">
      <h4>用户列表</h4>
      <el-checkbox
        :indeterminate="isIndeterminate"
        v-model="checkAll"
        @change="handleCheckAllChange"
        >全选</el-checkbox
      >
      <div style="margin: 15px 0;"></div>
      <el-checkbox-group
        v-model="checkedCities"
        @change="handleCheckedCitiesChange"
      >
        <el-checkbox v-for="city in cities" :label="city" :key="city">{{
          city
        }}</el-checkbox>
      </el-checkbox-group>
    </div>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  data() {
    return {
      checkAll: false,
      cities: [1, 2, 3, 4],
      checkedCities: "",
      isIndeterminate: true
    };
  },
  methods: {
    handleCheckAllChange(val) {
      this.checkedCities = val ? [1, 2, 3, 4] : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange() {}
  },
  mounted: function() {}
};
</script>

<style lang="scss">
.use_self {
  height: 100%;
  display: flex;
  .use_self_left {
    width: 240px;
    height: 365px;
    background: rgba(255, 255, 255, 1);
    border: 1px solid #ccc;
    flex: 1;
  }
  .use_self_right {
    width: 240px;
    height: 365px;
    background: rgba(255, 255, 255, 1);
    border: 1px solid #ccc;
    flex: 1;
    margin-left: 5px;
    .el-checkbox + .el-checkbox {
      margin-left: 0px;
      display: block;
    }
    .el-checkbox {
      margin-right: 25px;
    }
  }
}
</style>


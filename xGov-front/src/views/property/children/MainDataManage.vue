<template>
  <section class="main-data-manage main-outer">
    <el-row class="common-condition ar mb15"> </el-row>
    <el-row
      style="height: calc(100% - 35px)"
      :class="routeName === '详情' ? 'main-inner-allSize' : 'main-inner'"
    >
      <router-view></router-view>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  data() {
    return {
      routeName: ""
    };
  },
  methods: {},
  mounted: function() {},
  watch: {
    $route(to, from) {
      this.routeName = to.name;
    }
  }
};
</script>

<style lang="scss">
</style>

<template>
  <section class="left-menu">
    <el-menu
      class="el-menu-vertical-demo"
      text-color="#fff"
      active-text-color="#fff"
      background-color="#333743"
      :default-active="$route.matched[2].path"
      :router="true"
    >
      <el-menu-item
        index="/property/default/maindata"
        key="/property/default/maindata"
      >
        <i class="icon"><svg-icon iconClass="nav5-1"></svg-icon></i>
        <span slot="title">主数据管理</span>
      </el-menu-item>
      <el-menu-item index="/dataMap" key="/dataMap">
        <i class="icon"><svg-icon iconClass="nav5-2"></svg-icon></i>
        <span slot="title">资产目录</span>
      </el-menu-item>
    </el-menu>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  data() {
    return {};
  },
  methods: {},
  mounted: function() {}
};
</script>

<style lang="scss">
</style>

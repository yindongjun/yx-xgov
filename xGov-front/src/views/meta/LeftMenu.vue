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
        index="/metaData/default/model"
        key="/metaData/default/model"
      >
        <i class="icon"><svg-icon iconClass="nav1-1"></svg-icon></i>
        <span slot="title">元模型</span>
      </el-menu-item>
      <el-menu-item
        index="/metaData/default/collcet"
        key="/metaData/default/collcet"
      >
        <i class="icon"><svg-icon iconClass="nav1-2"></svg-icon></i>
        <span slot="title">元数据采集</span>
      </el-menu-item>
      <el-menu-item index="/metaData/default/care" key="/metaData/default/care">
        <i class="icon"><svg-icon iconClass="nav1-3"></svg-icon></i>
        <span slot="title">元数据维护</span>
      </el-menu-item>
      <el-menu-item
        index="/metaData/default/analyze"
        key="/metaData/default/nalyze"
      >
        <i class="icon"><svg-icon iconClass="nav1-4"></svg-icon></i>
        <span slot="title">元数据分析</span>
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

<template>
  <section class="left-menu aside-container">
    <el-menu
      class="el-menu-vertical-demo"
      text-color="#fff"
      active-text-color="#fff"
      background-color="#333743"
      :default-active="$route.matched[2].path"
      :router="true"
    >
      <el-menu-item index="/standard/default/meta" key="/standard/default/meta">
        <i class="icon"><svg-icon iconClass="nav2-1"></svg-icon></i>
        <span slot="title">标准维护</span>
      </el-menu-item>
      <el-menu-item
        index="/standard/default/collect"
        key="/standard/default/collect"
      >
        <i class="icon"><svg-icon iconClass="nav2-2"></svg-icon></i>
        <span slot="title">标准分类</span>
      </el-menu-item>
      <el-menu-item
        index="/standard/default/businessGlossary"
        key="/standard/default/businessGlossary"
      >
        <i class="icon"><svg-icon iconClass="nav2-3"></svg-icon></i>
        <span slot="title">业务代码</span>
      </el-menu-item>
      <el-menu-item
        index="/standard/default/documentManage"
        key="/standard/default/documentManage"
      >
        <i class="icon"><svg-icon iconClass="nav2-4"></svg-icon></i>
        <span slot="title">文档管理</span>
      </el-menu-item>
      <el-menu-item
        index="/standard/default/standardCheck"
        key="/standard/default/standardCheck"
      >
        <i class="icon"><svg-icon iconClass="nav2-5"></svg-icon></i>
        <span slot="title">标准审核</span>
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

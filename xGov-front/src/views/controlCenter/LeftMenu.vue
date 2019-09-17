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
      <el-menu-item
        index="/controlCenter/default/taskManage"
        key="/controlCenter/default/taskManage"
      >
        <i class="icon"><svg-icon iconClass="nav4-1"></svg-icon></i>
        <span slot="title">任务管理</span>
      </el-menu-item>
      <el-menu-item
        index="/controlCenter/default/taskMonitor"
        key="/controlCenter/default/taskMonitor"
      >
        <i class="icon"><svg-icon iconClass="nav4-2"></svg-icon></i>
        <span slot="title">任务监控</span>
      </el-menu-item>
      <el-menu-item
        index="/controlCenter/default/warnSetting"
        key="/controlCenter/default/warnSetting"
      >
        <i class="icon"><svg-icon iconClass="nav4-3"></svg-icon></i>
        <span slot="title">告警设置</span>
      </el-menu-item>
    </el-menu>
  </section>
</template>

<script>
import LeftMenu from "./LeftMenu";
export default {
  name: "left-menu",
  components: {
    LeftMenu
  },
  data() {
    return {};
  },
  methods: {}
};
</script>

<style scoped>
</style>

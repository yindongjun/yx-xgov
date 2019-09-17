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
        index="/quality/default/design"
        key="/quality/default/design"
      >
        <i class="icon"><svg-icon iconClass="nav3-2"></svg-icon></i>
        <span slot="title">规则设计</span>
      </el-menu-item>
      <el-menu-item index="/quality/default/task" key="/quality/default/task">
        <i class="icon"><svg-icon iconClass="nav3-4"></svg-icon></i>
        <span slot="title">质量任务</span>
      </el-menu-item>
      <el-menu-item
        index="/quality/default/question"
        key="/quality/default/question"
      >
        <i class="icon"><svg-icon iconClass="nav3-3"></svg-icon></i>
        <span slot="title">问题数据</span>
      </el-menu-item>
      <el-menu-item
        index="/quality/default/report"
        key="/quality/default/report"
      >
        <i class="icon"><svg-icon iconClass="nav3-5"></svg-icon></i>
        <span slot="title">质量报告</span>
      </el-menu-item>
      <el-menu-item
        index="/quality/default/template"
        key="/quality/default/template"
      >
        <i class="icon"><svg-icon iconClass="nav3-6"></svg-icon></i>
        <span slot="title">报告模板</span>
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
.icon {
  font-size: 18px;
  margin-right: 5px;
}
</style>

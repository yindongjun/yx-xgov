<template>
  <el-container class="homeOuterContainer">
    <el-header class="homeHeader" style="background: #fff">
      <page-home-default></page-home-default>
    </el-header>
    <el-main class="homeMain" style="background: #F5F7FA">
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script>
import PageHomeDefault from "./PageHomeDefault.vue";

export default {
  name: "page-home",
  components: {
    PageHomeDefault
  },
  computed: {},
  data() {
    return {};
  },
  methods: {},
  mounted: function() {}
};
</script>

<style lang="scss">
</style>

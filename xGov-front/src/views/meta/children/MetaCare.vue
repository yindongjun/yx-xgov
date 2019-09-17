<template>
  <section class="meta-collect main-outer">
    <el-row class="common-condition ar mb15"> </el-row>
    <el-row class="main-inner-allSize">
      <el-tabs v-model="activeName2" type="card" :before-leave="beforeLeave">
        <el-tab-pane label="业务元数据" name="business"></el-tab-pane>
        <el-tab-pane label="技术元数据" name="technical"></el-tab-pane>
      </el-tabs>
      <div class="tab-contant">
        <router-view></router-view>
      </div>
    </el-row>
  </section>
</template>
<script>
export default {
  name: "",
  components: {},
  data() {
    return {
      activeName2: "business"
    };
  },
  methods: {
    beforeLeave(curr, oldVal) {
      if (curr === "business") {
        this.$router.push("/metaData/default/care/business");
      } else if (curr === "technical") {
        this.$router.push("/metaData/default/care/technical");
      } else {
        this.$router.push("/metaData/default/care/manage");
      }
    }
  },
  watch: {
    $route(val, oldVal) {
      const urlList = val.path.split("/");
      this.activeName2 = urlList[urlList.length - 1];
    }
  },
  mounted: function() {
    const urlList = this.$route.path.split("/");
    this.activeName2 = urlList[urlList.length - 1];
  }
};
</script>

<style lang="scss">
.meta-collect {
  .tab-contant {
    height: -moz-calc(100% - 56px);
    height: -webkit-calc(100% - 56px);
    height: calc(100% - 56px);
  }
}
</style>


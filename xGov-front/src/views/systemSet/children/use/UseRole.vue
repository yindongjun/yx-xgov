<template>
  <section class="use_self">
    <div class="use_self_left pl10"></div>
    <div class="use_self_right pl10"></div>
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
      isIndeterminate: true
    };
  },
  methods: {
    handleCheckAllChange(val) {
      this.checkedCities = val ? [1, 2, 3, 4] : [];
      this.isIndeterminate = false;
    }
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



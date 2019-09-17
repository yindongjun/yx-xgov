<template>
  <section class="meta-collect main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <div class="main-inner">
      <el-tabs v-model="activeName" type="card" :before-leave="beforeLeave">
        <el-tab-pane class="collect-child" label="元数据采集列表" name="first">
          <collect-list :CollectListTabTime="CollectListTabTime"></collect-list>
        </el-tab-pane>
        <el-tab-pane class="collect-child" label="元数据入库审核" name="second">
          <storage-check
            :StorageCheckTabTime="StorageCheckTabTime"
          ></storage-check>
        </el-tab-pane>
      </el-tabs>
    </div>
  </section>
</template>

<script>
import CollectList from "./collect/CollectList.vue";
import StorageCheck from "./collect/StorageCheck.vue";

export default {
  name: "",
  components: { CollectList, StorageCheck },
  data() {
    return {
      CollectListTabTime: new Date().getTime(),
      StorageCheckTabTime: new Date().getTime(),
      activeName: "first"
    };
  },
  methods: {
    beforeLeave(curr, oldVal) {
      let tempDate = new Date().getTime();
      if (curr === "first") {
        this.CollectListTabTime = tempDate;
      } else if (curr === "second") {
        this.StorageCheckTabTime = tempDate;
      }
    }
  },
  mounted: function() {}
};
</script>

<style lang="scss">
.main-inner {
  height: calc(100% - 75px);
  .el-tabs {
    height: 100%;
    .el-tabs__content {
      height: calc(100% - 55px);
      .collect-child {
        height: 100%;
      }
    }
  }
}
</style>

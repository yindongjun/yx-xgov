<template>
  <section class="standard-check-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner">
      <el-tabs
        v-model="tabType"
        type="border-card"
        :tab-position="tabPosition"
        @tab-click="tabClick"
      >
        <el-tab-pane
          class="standardCheck-child"
          label="待审核标准"
          :key="1"
          name="check-standard"
        >
          <CheckStandardTab
            :checkStandardVisible="checkStandardVisible"
          ></CheckStandardTab>
        </el-tab-pane>
        <el-tab-pane
          class="standardCheck-child"
          label="已审核标准"
          :key="2"
          name="checked-standard"
        >
          <CheckedStandardTab
            @backToCheck='backToCheck'
            :checkedStandardVisible="checkedStandardVisible"
          ></CheckedStandardTab>
        </el-tab-pane>
      </el-tabs>
    </el-row>
  </section>
</template>

<script>
import ElInput from "element-ui/packages/input/src/input";
import CheckStandardTab from "./CheckStandardTab";
import CheckedStandardTab from "./CheckedStandardTab";

export default {
  name: "standard-check",
  components: {
    CheckStandardTab,
    CheckedStandardTab
  },
  data() {
    return {
      tabType: "check-standard",
      tabPosition: "top",
      checkStandardVisible: "",
      checkedStandardVisible: ""
    };
  },
  methods: {
    tabClick: function(tab) {
      switch (tab.name) {
        case "check-standard":
          let tempDate1 = new Date();
          this.checkStandardVisible = tempDate1.toTimeString();
          break;
        case "checked-standard":
          let tempDate2 = new Date();
          this.checkedStandardVisible = tempDate2.toTimeString();
          break;
        default:
          break;
      }
    },
    backToCheck() {
      this.tabType = 'check-standard'
    }
  }
};
</script>

<style lang="scss">
.standard-check-section {
  .main-inner {
    height: calc(100% - 35px);
    .el-tabs {
      height: 100%;
      .el-tabs__content {
        height: calc(100% - 55px);
        .standardCheck-child {
          height: 100%;
        }
      }
    }
  }
  .el-tabs__item {
    height: 42px;
  }
  .el-tabs--border-card {
    border: 0;
    -webkit-box-shadow: none;
    -moz-box-shadow: none;
    -o-box-shadow: none;
    box-shadow: none;
  }
  .el-tabs--border-card > .el-tabs__header {
    background: transparent;
  }
  .el-tabs--border-card > .el-tabs__header .el-tabs__item {
    margin: 0 -1px -2px;
    background: #f9f9fa;
    border-width: 2px 1px 1px 1px;
    border-style: solid;
    border-color: #e1e5ec;
  }
  .el-tabs .el-tabs__header .el-tabs__nav .el-tabs__item.is-active {
    border-top: 2px solid #3ba3f8;
    background: #fff;
  }
}
</style>

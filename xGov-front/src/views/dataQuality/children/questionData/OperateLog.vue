<template>
  <el-dialog
    title="处理记录"
    :visible.sync="operateLogVisible"
    :before-close="closeDialog"
    class="operate-log"
    :lock-scroll="false"
    width="500px"
    @open="openDialog"
    :close-on-click-modal="false"
  >
    <el-row>
      <p>处理人：{{ dealPeople }}</p>
      <p>处理结论：{{ dealComment }}</p>
      <p>处理时间：{{ dealTime | date("%Y-%m-%d %T") }}</p>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import ElRow from "element-ui/packages/row/src/row";

export default {
  name: "",
  props: ["operateLogVisible", "isOperateLog"],
  components: { ElRow },
  data() {
    return {
      dealPeople: "",
      dealComment: "",
      dealTime: ""
    };
  },
  methods: {
    closeDialog() {
      this.$emit("operateLogBack");
    },
    openDialog() {
      this.dealPeople = this.isOperateLog.dealPeople;
      this.dealComment = this.isOperateLog.dealComment;
      this.dealTime = this.isOperateLog.dealTime;
    }
  },
  mounted: function() {}
};
</script>

<style lang="scss">
</style>

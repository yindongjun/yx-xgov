<template>
  <transition name="el-fade-in-linear">
    <div class="toast" ref="toast" :class="type" @mouseenter="clearTimer" @mouseleave="startTimer">
      <span style="float: left" :title="text">
        <i class="iconStyle" :class="'el-icon-' + type"></i>
        <span class="text">{{ text.length > 50 ? text.substr(0, 50) + '...' : text}}</span>
      </span>
      <span @click="close" class="closeIcon"
        ><i class="el-icon-close"></i
      ></span>
    </div>
  </transition>
</template>
<script>
export default {
  name: "Toast",
  data() {
    return {
      type: "info",
      text: "",
      closed: false,
      timer: null
    };
  },
  mounted() {
    this.startTimer();
  },
  methods: {
    destory() {
      this.$destroy(true);
      this.$el.parentNode.removeChild(this.$el);
    },
    clearTimer() {
      clearTimeout(this.timer)
    },
    startTimer() {
      this.timer = setTimeout(() => {
        if (!this.closed) {
          this.close();
        }
      }, 3000);
    },
    close() {
      this.closed = true;
      this.destory();
    },
  }
};
</script>
<style lang="scss" scoped>
.toast {
  position: fixed;
  top: 65px;
  right: 36px;
  min-width: 300px;
  height: 35px;
  line-height: 35px;
  padding: 0 15px;
  font-size: 12px;
  z-index: 99999999;
  transition: all 0.5s;
  &.warning {
    color: #e66f43;
    background: #fff2ee;
    border: 1px solid #e66f43;
  }
  &.success {
    color: #1bb899;
    background: #f2fffc;
    border: 1px solid #1bb899;
  }
  &.error {
    color: #f26565;
    background: #fff6f5;
    border: 1px solid #f26565;
  }
  &.info {
    color: #3ba3f8;
    background: #f3f8ff;
    border: 1px solid #3ba3f8;
  }
  .iconStyle {
    font-size: 14px;
    vertical-align: middle;
    margin-right: 10px;
  }
  .closeIcon {
    float: right;
    font-size: 14px;
    cursor: pointer;
    vertical-align: middle;
  }
}
</style>
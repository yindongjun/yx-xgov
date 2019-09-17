<template>
  <div class="notfound">
    <div class="left"></div>
    <div class="right">
      <h3>对不起，您暂无此模块使用权限</h3>
      <p>即将跳转首页倒计时 <span>&nbsp;&nbsp;{{restTime}}秒</span></p>
      <el-button class="backtoHome" size="large" type="danger" @click="backToHome">立即跳转</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      restTime: 5,
      timer: null
    }
  },
  mounted() {
    // not found 404页面
    this.timer = setInterval(() => {
      this.restTime--
      if (this.restTime <= 0) {
        this.backToHome()
      }
    }, 1000)
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    backToHome() {
      this.$router.push('/');
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang='scss'>
.notfound {
  width: 100%;
  height: 100%;
  padding-left: 15%;
  box-sizing: border-box;
  .left {
    width: 40%;
    float: left;
    height: 100%;
    background: url('./../../assets/images/404.png') center no-repeat;
    background-size: contain;
  }
  .right {
    width: 40%;
    float: left;
    margin-top: 20%;
    h3 {
      font-size: 22px;
      color: rgba(#253239, 0.9);
      font-weight: normal;
    }
    p {
      font-size: 20px;
      color: #5E6977;
      margin: 20px 0;
      span {
        font-size: 24px;
      }
    }
    .backtoHome {
      font-size: 14px!important;
    }
  }
}
</style>

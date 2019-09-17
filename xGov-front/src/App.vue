<template>
  <div id="app">
    <router-view v-wechat-title="$route.meta.title"></router-view>
  </div>
</template>

<script>
  export default {
    name: 'App',
    beforeRouteEnter (to, from, next) {},
    beforeRouteUpdate (to, from, next) {},
    beforeRouteLeave (to, from, next) {}
  }
</script>
<style lang="scss">
  @import './assets/styles/index.scss';
</style>

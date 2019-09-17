<template>
    <section class="system-set homeMainInner">
        <el-row class="system-set-inner inner-container">
            <el-col :span="5" class="pt10 aside-container">
                <asides-menu :menuType="menuType"></asides-menu>
            </el-col>
            <el-col :span="19" class="main-container">
                <div class="main-container-top">
                    <el-breadcrumb separator="/" style="padding: 15px 0 15px 35px;" class="breadcrumb">
                        <el-breadcrumb-item :to="{ path: $route.matched[1].path }">{{$route.matched[1].name}}</el-breadcrumb-item>
                        <el-breadcrumb-item v-if="$route.matched.length >= 3" :to="{ path: $route.matched[2].path }">{{$route.matched[2].name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <router-view></router-view>
            </el-col>
        </el-row>
    </section>
</template>
<script>
    import LeftMenu from './LeftMenu.vue'
    import AsidesMenu from '../../components/Asides.vue'
    export default {
        name: "",
        components: {AsidesMenu},
        data() {
            return {
                menuType:'数据标准'
            }
        },
        methods: {},
        mounted: function () {

        }
    }
</script>

<style lang="scss">

</style>

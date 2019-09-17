<template>
    <section class="left-menu aside-container">
        <el-menu
            class="el-menu-vertical-demo"
            text-color="#fff"
            active-text-color="#fff"
            background-color="#333743"
            :default-active="$route.matched[2].path"
            :router="true"
        >
            <el-menu-item index="/systemSet/default/datasource" key="/systemSet/default/datasource">
                <i class="icon"><svg-icon iconClass="nav6-1"></svg-icon></i>
                <span slot="title" >数据源管理</span>
            </el-menu-item>
            <el-menu-item index="/systemSet/default/department" key="/systemSet/default/department">
                <i class="icon"><svg-icon iconClass="nav6-2"></svg-icon></i>
                <span slot="title" >部门管理</span>
            </el-menu-item>
            <el-menu-item index="/systemSet/default/user" key="/systemSet/default/user">
                <i class="icon"><svg-icon iconClass="nav6-3"></svg-icon></i>
                <span slot="title" >用户管理</span>
            </el-menu-item>
            <el-menu-item index="/systemSet/default/role" key="/systemSet/default/role">
                <i class="icon"><svg-icon iconClass="nav6-4"></svg-icon></i>
                <span slot="title" >角色权限</span>
            </el-menu-item>
        </el-menu>
    </section>
</template>

<script>
    export default {
        name: "",
        components: {},
        data() {
            return {}
        },
        methods: {
        },
        mounted: function () {

        }
    }
</script>

<style lang="scss">
.icon {
    font-size: 18px;
    margin-right: 5px;
}
</style>

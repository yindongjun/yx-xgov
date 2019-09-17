<template>
    <section class="meta-collect main-outer">
        <el-row class="search-condition ar mb15">
            <el-input
                placeholder="搜索..."
                style="width: 180px"
                class="search-input"
                size="small"
                v-model="dataSourceName">
                <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <el-button size="small" class="ml10" type="danger">新增采集</el-button>
            <el-button size="small" type="info">删除</el-button>
        </el-row>
        <el-row class="main-inner">
            <el-table
                :data="sourceList"
                stripe
                style="width: 100%"
            >
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    prop="datasourceName"
                    label="数据源名称">
                </el-table-column>
                <el-table-column
                    prop="databaseType"
                    label="数据库类型">
                </el-table-column>
                <el-table-column
                    prop="dbname"
                    label="数据源库名">
                </el-table-column>
                <el-table-column
                    prop="createTime"
                    label="创建时间"
                    :formatter="this.$utils.dateFormat"
                >
                </el-table-column>
                <el-table-column
                    prop="lastModifyTime"
                    label="修改时间"
                    :formatter="this.$utils.dateFormat"
                >
                </el-table-column>
                <el-table-column
                    label="操作"
                    width="170">
                    <template slot-scope="scope">
                        <el-tooltip :popper-class="toolTipClass" class="item" effect="dark" content="执行" placement="top">
                            <i class="table-icon-menu"><svg-icon iconClass="执行"></svg-icon></i>
                        </el-tooltip>
                        <el-tooltip class="item" effect="dark" content="编辑" placement="top">
                            <i class="table-icon-menu"><svg-icon iconClass="编辑"></svg-icon></i>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                class="mt15 ar"
                layout="prev, pager, next"
                :total="40">
            </el-pagination>
        </el-row>
    </section>
</template>

<script>
    import SvgIcon from "../../../components/SvgIcon.vue";

    export default {
        name: "",
        components: {SvgIcon},
        data() {
            return {
                dataSourceName:'',
                sourceList:[
                    {
                        createTime: 1541580072000,
                        databaseType: "SqlServer",
                        datasourceName: "SQLserver-auto",
                        dbname: "datatools",
                        driver: "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                        id: 596,
                        ip: "10.221.121.1",
                        lastModifyTime: 1541580072000,
                        userName: "sa"
                    },
                    {
                        createTime: 1541580072000,
                        databaseType: "SqlServer",
                        datasourceName: "SQLserver-auto",
                        dbname: "datatools",
                        driver: "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                        id: 596,
                        ip: "10.221.121.1",
                        lastModifyTime: 1541580072000,
                        userName: "sa"
                    },
                    {
                        createTime: 1541580072000,
                        databaseType: "SqlServer",
                        datasourceName: "SQLserver-auto",
                        dbname: "datatools",
                        driver: "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                        id: 596,
                        ip: "10.221.121.1",
                        lastModifyTime: 1541580072000,
                        userName: "sa"
                    },
                    {
                        createTime: 1541580072000,
                        databaseType: "SqlServer",
                        datasourceName: "SQLserver-auto",
                        dbname: "datatools",
                        driver: "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                        id: 596,
                        ip: "10.221.121.1",
                        lastModifyTime: 1541580072000,
                        userName: "sa"
                    },
                ]
            }
        },
        methods: {
        },
        mounted: function () {

        }
    }
</script>

<style lang="scss">

</style>

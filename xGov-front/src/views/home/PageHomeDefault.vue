<template>
  <div class="header-top">
    <div class="header-top-logo">
      <!-- <img src="../../assets/images/dataRun.png"/> -->
      <!-- <svg-icon iconClass="govlogo"></svg-icon> XGov -->
      <span style="font-size: 20px;margin-right: 10px">
        <svg-icon iconClass='logo1'></svg-icon>
      </span>
      <span>数据治理</span>
    </div>
    <el-menu
      :default-active="activeRoute"
      :router="true"
      class="el-menu-demo el-menu-top"
      mode="horizontal"
      text-color="#fff"
      active-text-color="#fff"
      background-color="#333743"
    >
      <!-- <el-menu-item index="/metaData/default" key="/metaData/default">元数据管理</el-menu-item>
            <el-menu-item index="/standard/default" key="/standard/default">数据标准</el-menu-item>
            <el-menu-item index="/quality/default" key="/quality/default">数据质量</el-menu-item>
            <el-menu-item index="/controlCenter/default" key="/controlCenter/default">调度中心</el-menu-item>
            <el-menu-item index="/property/default" key="/property/default">资产管理</el-menu-item>
            <el-menu-item index="/systemSet/default" key="/systemSet/default">系统设置</el-menu-item> -->

      <el-menu-item
        v-for="(router, index) in routers"
        :key="index"
        :index="router.permission"
      >
        {{ router.pName }}
      </el-menu-item>
    </el-menu>
    <div class="header-top-right">
      <el-tooltip
        class="item"
        effect="dark"
        content="数据地图"
        placement="bottom"
      >
        <span class="dataMap" @click="jumpMap"
          ><svg-icon iconClass="map"></svg-icon
        ></span>
      </el-tooltip>
      <el-dropdown @command="handleCommand" trigger="click" :show-timeout="0">
        <!--<span class="el-dropdown-link">
                admin<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
              </span>-->
        <span class="el-dropdown-link">
          <img style="vertical-align: middle" src="./../../assets/images/user.png" width="28px" alt="user">
          {{ userName }}&nbsp;&nbsp;<i
            class="el-icon-arrow-down el-icon&#45;&#45;right"
          ></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-if="!config.isSplit" command="c">返回主界面</el-dropdown-item>
          <el-dropdown-item command="d">退出系统</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
        <!-- <span v-if="!config.isSplit" title="返回" class="backToPublic" @click="backToPublic">
          <svg-icon iconClass="退出"></svg-icon>
          <span class="back">返回</span>
        </span> -->
    </div>
  </div>
</template>

<script>
import SvgIcon from "../../components/SvgIcon.vue";
import config from '@/config.js';

export default {
  name: "",
  components: { SvgIcon },
  data() {
    return {
      activeIndex: 1,
      userName: "",
      menuList: [],
      config: config,
      activeRoute: ''
    };
  },
  computed: {
    routers() {
      return this.menuList;
    }
  },
  methods: {
    jumpMap() {
      this.$router.push("/dataMap");
    },
    handleCommand(command) {
      let that = this;
      switch (command) {
        case "a":
          this.$alert("新手指引", "信息提示", {
            confirmButtonText: "确定",
            callback: action => {}
          });
          break;
        case "c":
          this.backToPublic()
          break;
        case "d":
          this.$confirm("确定要退出系统?", "信息提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            closeOnClickModal: false
          })
            .then(() => {
              if (config.isSplit) {
                this.$auth.removeToken();
                localStorage.removeItem("user");
                localStorage.removeItem("menuList");
                this.$router.push('/login');
              } else {
                this.$urlApi.userPrivate.logout({ sessionId: this.$auth.getToken() })
                  .then(res => {
                      this.$auth.removeToken();
                      localStorage.removeItem("user");
                      localStorage.removeItem("menuList");
                      window.location = location.origin + "/public/#/";
                  });
              }
            })
            .catch(() => {});
          break;
        default:
          break;
      }
    },
    getMenulist() {
      let dataList = [
        {
          child: [
            {
              del: "0",
              icon: '{"name":"元模型","icon":"nav1-1"}',
              id: 157,
              indexs: 0,
              name: "元模型",
              os: "xgov",
              pName: "元数据管理",
              permission: "/metaData/default/model",
              pid: 156,
              resourceType: "menu",
              urlList: ["gov/meta_model/**"]
            },
            {
              del: "0",
              icon: '{"name":"元数据采集","icon":"nav1-2"}',
              id: 158,
              indexs: 0,
              name: "元数据采集",
              os: "xgov",
              pName: "元数据管理",
              permission: "/metaData/default/collcet",
              pid: 156,
              resourceType: "menu",
              urlList: [
                "gov/collectTask/**",
                "gov/data_source/**",
                "gov/meta_data/**"
              ]
            },
            {
              del: "0",
              icon: '{"name":"元数据维护","icon":"nav1-3"}',
              id: 159,
              indexs: 0,
              name: "元数据维护",
              os: "xgov",
              pName: "元数据管理",
              permission: "/metaData/default/care/business",
              pid: 156,
              resourceType: "menu",
              urlList: ["gov/meta_data/**"]
            },
            {
              del: "0",
              icon: '{"name":"源数据分析","icon":"nav1-4"}',
              id: 160,
              indexs: 0,
              name: "元数据分析",
              os: "xgov",
              pName: "元数据管理",
              permission: "/metaData/default/analyze",
              pid: 156,
              resourceType: "menu",
              urlList: ["gov/meta_data/search_meta"]
            }
          ],
          del: "0",
          icon: '{"name":"源数据分析","icon":"nav1-4"}',
          id: 156,
          indexs: 0,
          name: "元数据管理",
          os: "xgov",
          pName: "xgov",
          permission: "/metaData/default",
          pid: 155,
          resourceType: "menu",
          urlList: []
        },
        {
          child: [
            {
              del: "0",
              icon: '{"name":"标准维护","icon":"nav2-1"}',
              id: 162,
              indexs: 0,
              name: "标准维护",
              os: "xgov",
              pName: "数据标准",
              permission: "/standard/default/meta",
              pid: 161,
              resourceType: "menu",
              urlList: [
                "gov/code/**",
                "gov/data_element/**",
                "gov/data_source/**"
              ]
            },
            {
              del: "0",
              icon: '{"name":"标准分类","icon":"nav2-2"}',
              id: 163,
              indexs: 0,
              name: "标准分类",
              os: "xgov",
              pName: "数据标准",
              permission: "/standard/default/collect",
              pid: 161,
              resourceType: "menu",
              urlList: [
                "gov/data_set/**",
                "gov/data_element/**",
                "gov/data_source/**"
              ]
            },
            {
              del: "0",
              icon: '{"name":"业务数据","icon":"nav2-3"}',
              id: 164,
              indexs: 0,
              name: "业务代码",
              os: "xgov",
              pName: "数据标准",
              permission: "/standard/default/businessGlossary",
              pid: 161,
              resourceType: "menu",
              urlList: ["gov/code/**"]
            },
            {
              del: "0",
              icon: '{"name":"文档管理","icon":"nav2-4"}',
              id: 165,
              indexs: 0,
              name: "文档管理",
              os: "xgov",
              pName: "数据标准",
              permission: "/standard/default/documentManage",
              pid: 161,
              resourceType: "menu",
              urlList: ["gov/documentcontent/**"]
            },
            {
              del: "0",
              icon: '{"name":"标准审核","icon":"nav2-5"}',
              id: 166,
              indexs: 0,
              name: "标准审核",
              os: "xgov",
              pName: "数据标准",
              permission: "/standard/default/standardCheck",
              pid: 161,
              resourceType: "menu",
              urlList: ["gov/code/**", "gov/standard_audit/**"]
            }
          ],
          del: "0",
          icon: '{"name":"标准审核","icon":"nav2-5"}',
          id: 161,
          indexs: 0,
          name: "数据标准",
          os: "xgov",
          pName: "xgov",
          permission: "/standard/default",
          pid: 155,
          resourceType: "menu",
          urlList: []
        },
        {
          child: [
            {
              del: "0",
              icon: '{"name":"规则设计","icon":"nav3-1"}',
              id: 168,
              indexs: 0,
              name: "规则设计",
              os: "xgov",
              pName: "数据质量",
              permission: "/quality/default/design/list",
              pid: 167,
              resourceType: "menu",
              urlList: ["gov/qualityDesignTable/**", "gov/data_source/**"]
            },
            {
              del: "0",
              icon: '{"name":"质量任务","icon":"nav3-2"}',
              id: 169,
              indexs: 0,
              name: "质量任务",
              os: "xgov",
              pName: "数据质量",
              permission: "/quality/default/task",
              pid: 167,
              resourceType: "menu",
              urlList: [
                "gov/qualityTask/**",
                "gov/qualityDesignTable/**",
                "gov/qualityTask/**"
              ]
            },
            {
              del: "0",
              icon: '{"name":"问题数据","icon":"nav3-3"}',
              id: 170,
              indexs: 0,
              name: "问题数据",
              os: "xgov",
              pName: "数据质量",
              permission: "/quality/default/question",
              pid: 167,
              resourceType: "menu",
              urlList: ["gov/qualityDesignTable/**"]
            },
            {
              del: "0",
              icon: '{"name":"数据源管理","icon":"nav6-1"}',
              id: 193,
              indexs: 0,
              name: "质量报告",
              os: "xgov",
              pName: "数据质量",
              permission: "/quality/default/report",
              pid: 167,
              resourceType: "menu",
              urlList: ["gov/quality-report/**"]
            },
            {
              del: "0",
              icon: '{"name":"主数据管理","icon":"nav5-1"}',
              id: 194,
              indexs: 0,
              name: "报告模板",
              os: "xgov",
              pName: "数据质量",
              permission: "/quality/default/template",
              pid: 167,
              resourceType: "menu",
              urlList: [
                "gov/quality-report/showReportTemplte",
                "gov/quality-report/addReportTemplte"
              ]
            }
          ],
          del: "0",
          icon: '{"name":"质量任务","icon":"nav3-2"}',
          id: 167,
          indexs: 0,
          name: "数据质量",
          os: "xgov",
          pName: "xgov",
          permission: "/quality/default",
          pid: 155,
          resourceType: "menu",
          urlList: []
        },
        {
          child: [
            {
              del: "0",
              icon: '{"name":"任务管理","icon":"nav4-1"}',
              id: 175,
              indexs: 0,
              name: "任务管理",
              os: "xgov",
              pName: "调度中心",
              permission: "/controlCenter/default/taskManage",
              pid: 174,
              resourceType: "menu",
              urlList: ["gov/dispatchCenter/**", "gov/dispatch_alarm/**"]
            },
            {
              del: "0",
              icon: '{"name":"任务监控","icon":"nav4-2"}',
              id: 176,
              indexs: 0,
              name: "任务监控",
              os: "xgov",
              pName: "调度中心",
              permission: "/controlCenter/default/taskMonitor",
              pid: 174,
              resourceType: "menu",
              urlList: ["gov/dispatchCenter/monitor/**"]
            },
            {
              del: "0",
              icon: '{"name":"告警设置","icon":"nav4-3"}',
              id: 177,
              indexs: 0,
              name: "告警设置",
              os: "xgov",
              pName: "调度中心",
              permission: "/controlCenter/default/warnSetting",
              pid: 174,
              resourceType: "menu",
              urlList: ["gov/dispatch_alarm/**"]
            }
          ],
          del: "0",
          icon: "{}",
          id: 174,
          indexs: 0,
          name: "调度中心",
          os: "xgov",
          pName: "xgov",
          permission: "/controlCenter/default",
          pid: 155,
          resourceType: "menu",
          urlList: []
        },
        {
          child: [
            {
              del: "0",
              icon: '{"name":"主数据管理","icon":"nav5-1"}',
              id: 179,
              indexs: 0,
              name: "主数据管理",
              os: "xgov",
              pName: "资产管理",
              permission: "/property/default/maindata/list",
              pid: 178,
              resourceType: "menu",
              urlList: ["gov/main_data/**"]
            },
            {
              del: "0",
              icon: '{"name":"资产目录","icon":"nav5-2"}',
              id: 180,
              indexs: 0,
              name: "资产目录",
              os: "xgov",
              pName: "资产管理",
              permission: "/dataMap",
              pid: 178,
              resourceType: "menu",
              urlList: ["gov/dataMap/**"]
            }
          ],
          del: "0",
          icon: "{}",
          id: 178,
          indexs: 0,
          name: "资产管理",
          os: "xgov",
          pName: "xgov",
          permission: "/property/default",
          pid: 155,
          resourceType: "menu",
          urlList: []
        },
        {
          child: [
            {
              del: "0",
              icon: '{"name":"数据源管理","icon":"nav6-1"}',
              id: 182,
              indexs: 0,
              name: "数据源管理",
              os: "xgov",
              pName: "系统设置",
              permission: "/systemSet/default/datasource",
              pid: 181,
              resourceType: "menu",
              urlList: ["gov/data_source/**"]
            }
          ],
          del: "0",
          icon: "{}",
          id: 181,
          indexs: 0,
          name: "系统设置",
          os: "xgov",
          pName: "xgov",
          permission: "/systemSet/default",
          pid: 155,
          resourceType: "menu",
          urlList: []
        }
      ];
      let menuList1 = JSON.parse(localStorage.getItem('menuList')).find(c => c.os === 'xgov').child
      this.menuList = [...menuList1].map(c => c.child[0])
    },
    backToPublic() {
      window.location = location.origin + '/public/#/'
    }
  },
  mounted: function() {
    this.userName = JSON.parse(localStorage.getItem("user")).user;
    this.getMenulist();
    this.$nextTick(() => {
      this.activeRoute = this.menuList.find(c => c.permission.includes(this.$route.matched[0].path)).permission
    })
  },
  watch: {
    $route(to, from) {
      const route = this.menuList.find(c => c.permission.includes(to.matched[0].path)).permission
      this.activeRoute = route
    }
  }
};
</script>

<style lang="scss">
.header-top {
  height: 100%;
  width: 100%;
  background-color: #333743;
  display: flex;
  // .header-top-logo{
  //     width: 200px;
  //     box-shadow:-4px 0px 5px 0px rgba(0,0,0,0.5);
  //     box-sizing: border-box;
  //     height: 60px;
  //     text-align: center;
  //     line-height: 60px;
  //     img{
  //         width: 130px;
  //     }
  // }
  .header-top-logo {
    width: 200px;
    color: #DEEBFA;
    text-align: center;
    line-height: 60px;
    font-size: 18px;
    box-shadow: -4px 0px 5px 0px rgba(0, 0, 0, 0.5);
    box-sizing: border-box;
    height: 60px;
    // padding-left: 25px;
  }
  .el-menu-top {
    flex: 1;
    border-bottom: none;
    .el-menu-item {
      border-bottom: none;
      font-size: 13px;
    }
    .is-active {
      border: none;
      background: #3e4456 !important;
    }
  }
  .header-top-right {
    line-height: 60px;
    .dataMap {
      font-size: 20px;
      margin-right: 20px;
      cursor: pointer;
      height: 100%;
      display: inline-block;
    }
    .backToPublic {
      font-size: 16px;
      margin-right: 10px;
      cursor: pointer;
      height: 100%;
      color: #fff;
      display: inline-block;
      .back {
        font-size: 14px;
      }
    }
    .el-dropdown {
      color: #ffffff;
      cursor: pointer;
      height: 100%;
      margin-right: 40px;
    }
  }
}
</style>

export default [
    {
        path: '/login',
        name: '登录',
        component: resolve => require (['views/login/Login.vue'], resolve),
	    meta: {
		    title: '登录页面'
	    }
    },
    // {
    //     path: '/',
    //     redirect: '/metaData'
    // },
    {    //元数据管理
        path: '/metaData',
        name: 'metaData',
        component: resolve => require (['views/home/PageHome.vue'], resolve),
        meta: {
          title: '首页'
        },
        redirect: '/metaData/default',
        children:[
            {
                path: 'default',
                name: '元数据管理',
                component: resolve => require (['views/meta/Index.vue'], resolve),
                meta: {
                    title: '元数据管理-首页'
                },
                redirect: '/metaData/default/model',
                children:[
                    {
                        path: 'model',
                        name: '元模型',
                        component: resolve => require (['views/meta/children/MetaModel.vue'], resolve),
                        meta: {
                            title: '元数据管理-元模型'
                        },
                    },
                    {
                        path: 'collcet',
                        name: '元数据采集',
                        component: resolve => require (['views/meta/children/MetaCollect.vue'], resolve),
                        meta: {
                            title: '元数据管理-采集'
                        },
                    },
                    {
                        path: 'care',
                        name: '元数据维护',
                        component: resolve => require (['views/meta/children/MetaCare.vue'], resolve),
                        meta: {
                            title: '元数据管理-维护'
                        },
                        redirect: '/metaData/default/care/business',
                        children:[
                            {
                                path: 'business',
                                name: '业务元数据',
                                component: resolve => require (['views/meta/children/care/Business.vue'], resolve),
                                meta: {
                                    title: '元数据管理-业务元数据'
                                },
                            },
                            {
                                path: 'technical',
                                name: '技术元数据',
                                component: resolve => require (['views/meta/children/care/Technical.vue'], resolve),
                                meta: {
                                    title: '元数据管理-技术元数据'
                                },
                            },
                            {
                                path: 'manage',
                                name: '管理元数据',
                                component: resolve => require (['views/meta/children/care/Manage.vue'], resolve),
                                meta: {
                                    title: '元数据管理-管理元数据'
                                },
                            },
                        ]
                    },
                    {
                        path: 'analyze',
                        name: '元数据分析',
                        component: resolve => require (['views/meta/children/MetaAnalyze.vue'], resolve),
                        meta: {
                            title: '元数据管理-分析'
                        },
                    },
                ]
            },
        ]
    },
    {   //数据标准
        path: '/standard',
        component: resolve => require (['views/home/PageHome.vue'], resolve),
        meta: {
            title: '数据标准'
        },
        redirect: '/standard/default',
        children:[
            {
                path: 'default',
                name: '数据标准',
                component: resolve => require (['views/standard/Index.vue'], resolve),
                meta: {
                    title: '数据标准-首页'
                },
                redirect: '/standard/default/meta',
                children:[
                    {
                        path: 'meta',
                        name: '标准维护',
                        component: resolve => require (['views/standard/children/DataMeta.vue'], resolve),
                        meta: {
                            title: '数据标准-标准维护'
                        },
                    },
                    {
                        path: 'collect',
                        name: '标准分类',
                        component: resolve => require (['views/standard/children/DataCollect.vue'], resolve),
                        meta: {
                            title: '数据标准-标准分类'
                        },
                    },
                    {
                        path: 'businessGlossary',
                        name: '业务代码',
                        component: resolve => require (['views/standard/children/BusinessGlossary.vue'], resolve),
                        meta: {
                            title: '数据标准-业务代码'
                        },
                    },
                    {
                        path: 'documentManage',
                        name: '文档管理',
                        component: resolve => require (['views/standard/children/DocumentManage.vue'], resolve),
                        meta: {
                            title: '数据标准-文档管理'
                        },
                    },
                    {
                        path: 'standardCheck',
                        name: '标准审核',
                        component: resolve => require (['views/standard/children/StandardCheck.vue'], resolve),
                        meta: {
                            title: '数据标准-标准审核'
                        },
                    }
                ]
            },
        ]
    },
    {   //调度中心
        path: '/controlCenter',
        component: resolve => require(['views/home/PageHome.vue'], resolve),
        meta: {
            title: '调度中心'
        },
        redirect: '/controlCenter/default',
        children: [
            {
                path: 'default',
                name: '调度中心',
                component: resolve => require(['views/controlCenter/Index.vue'], resolve),
                meta: {
                    title: '调度中心-首页'
                },
                redirect: '/controlCenter/default/taskManage',
                children: [
                    {
                        path: 'taskManage',
                        name: '任务管理',
                        component: resolve => require(['views/controlCenter/children/TaskManage.vue'], resolve),
                        meta: {
                            title: '调度中心-任务管理'
                        }
                    },
                    {
                        path: 'taskMonitor',
                        name: '任务监控',
                        component: resolve => require(['views/controlCenter/children/TaskMonitor.vue'], resolve),
                        meta: {
                            title: '调度中心-任务监控'
                        }
                    },
                    {
                        path: 'warnSetting',
                        name: '告警设置',
                        component: resolve => require(['views/controlCenter/children/WarnSetting.vue'], resolve),
                        meta: {
                            title: '调度中心-告警设置'
                        }
                    }
                ]
            }
        ]
    },
    {   //数据质量
        path: '/quality',
        component: resolve => require (['views/home/PageHome.vue'], resolve),
        meta: {
            title: '数据质量'
        },
        redirect: '/quality/default',
        children:[
            {
                path: 'default',
                name: '数据质量',
                component: resolve => require (['views/dataQuality/Index.vue'], resolve),
                meta: {
                    title: '数据质量-质量规则'
                },
                redirect: '/quality/default/design',
                children:[
                    {
                        path: 'rule',
                        name: '质量规则',
                        component: resolve => require (['views/dataQuality/children/QualityRule.vue'], resolve),
                        meta: {
                            title: '数据质量-质量规则'
                        },
                    },
                    {
                        path: 'design',
                        name: '规则设计',
                        component: resolve => require (['views/dataQuality/children/RuleDesign/RuleList.vue'], resolve),
                        meta: {
                            title: '数据质量-规则设计'
                        },
                        // redirect: '/quality/default/design/list',
                        // children: [
                        //     {
                        //         path: 'list',
                        //         name: '规则列表',
                        //         component: resolve => require (['views/dataQuality/children/ruleDesign/RuleList.vue'], resolve),
                        //         meta: {
                        //             title: '数据质量-规则设计'
                        //         },
                        //     },
                        //     {
                        //         path: 'detail',
                        //         name: '质量设计',
                        //         component: resolve => require (['views/dataQuality/children/ruleDesign/RuleDetail.vue'], resolve),
                        //         meta: {
                        //             title: '数据质量-质量设计'
                        //         },
                        //     }
                        // ]
                    },
                    {
                        path: 'question',
                        name: '问题数据',
                        component: resolve => require (['views/dataQuality/children/QuestionData.vue'], resolve),
                        meta: {
                            title: '数据质量-问题数据'
                        },
                    },
                    {
                        path: 'task',
                        name: '质量任务',
                        component: resolve => require (['views/dataQuality/children/QualityTask.vue'], resolve),
                        meta: {
                            title: '数据质量-质量任务'
                        },
                    },
                    {
                        path: 'report',
                        name: '质量报告',
                        component: resolve => require (['views/dataQuality/children/QualityReport.vue'], resolve),
                        meta: {
                            title: '数据质量-质量报告'
                        },
                    },
                    {
                        path: 'template',
                        name: '报告模板',
                        component: resolve => require (['views/dataQuality/children/ReportTemplate.vue'], resolve),
                        meta: {
                            title: '数据质量-报告模板'
                        },
                    }
                ]
            },

        ]
    },
    {   //系统配置
        path: '/systemSet',
        component: resolve => require (['views/home/PageHome.vue'], resolve),
        meta: {
            title: '系统配置'
        },
        redirect: '/systemSet/default',
        children:[
            {
                path: 'default',
                name: '系统配置',
                component: resolve => require (['views/systemSet/Index.vue'], resolve),
                meta: {
                    title: '系统配置-首页'
                },
                redirect: '/systemSet/default/datasource',
                children:[
                    {
                        path: 'datasource',
                        name: '数据源管理',
                        component: resolve => require (['views/systemSet/children/DataManage.vue'], resolve),
                        meta: {
                            title: '系统配置-数据源管理'
                        },
                    },
                    {
                        path: 'department',
                        name: '部门管理',
                        component: resolve => require (['views/systemSet/children/DepartmentManage.vue'], resolve),
                        meta: {
                            title: '系统配置-数据源管理'
                        },
                    },
                    {
                        path: 'user',
                        name: '用户管理',
                        component: resolve => require (['views/systemSet/children/UserManage.vue'], resolve),
                        meta: {
                            title: '系统配置-用户管理'
                        },
                    },
                    {
                        path: 'role',
                        name: '角色权限',
                        component: resolve => require (['views/systemSet/children/RoleManage.vue'], resolve),
                        meta: {
                            title: '系统配置-角色权限'
                        },
                    },
                    {
                        path: 'log',
                        name: '系统日志',
                        component: resolve => require (['views/systemSet/children/LogManage.vue'], resolve),
                        meta: {
                            title: '系统配置-系统日志'
                        },
                    },
                ]
            },
        ]
    },
    {   //资产管理
        path: '/property',
        component: resolve => require (['views/home/PageHome.vue'], resolve),
        meta: {
            title: '资产管理'
        },
        redirect: '/property/default',
        children:[
            {
                path: 'default',
                name: '资产管理',
                component: resolve => require (['views/property/Index.vue'], resolve),
                meta: {
                    title: '资产管理-首页'
                },
                redirect: '/property/default/maindata',
                children:[
                    {
                        path: 'maindata',
                        name: '主数据',
                        component: resolve => require (['views/property/children/MainDataManage.vue'], resolve),
                        meta: {
                            title: '资产管理-主数据'
                        },
                        redirect: '/property/default/maindata/list',
                        children:[
                            {
                                path: 'list',
                                name: '主数据-列表',
                                component: resolve => require (['views/property/children/mainData/List.vue'], resolve),
                                meta: {
                                    title: '主数据-列表'
                                },
                            },
                            {
                                path: 'detail',
                                name: '详情',
                                component: resolve => require (['views/property/children/mainData/Detail.vue'], resolve),
                                meta: {
                                    title: '主数据-详情'
                                },
                            },
                        ]
                    },
                    {
                        path: 'catalog',
                        name: '资源目录',
                        component: resolve => require (['views/property/children/ResourceCatalog.vue'], resolve),
                        meta: {
                            title: '资产管理-资源目录'
                        },
                    },
                ]
            },
        ]
    },
    {
        path: '/dataMap',
        name: '数据地图',
        component: resolve => require (['views/map/DataMap.vue'], resolve),
        meta: {
            title: '数据地图'
        }
    },
    {
        path: '/standardManage',
        name: '标准管理区',
        component: resolve => require (['views/map/StandardManage.vue'], resolve),
        meta: {
            title: '数据地图-标准管理区'
        }
    },
    {
        path: '/mapCollect',
        name: '标准分类-mapCollect',
        component: resolve => require (['views/map/StandardDataCollect.vue'], resolve),
        meta: {
            title: '数据地图-标准分类'
        }
    },
    {
        path: '/404',
        name: '页面不存在',
        component: resolve => require (['views/notFound/index.vue'], resolve),
        meta: {
            title: '页面不存在'
        }
    },
    // {
    //     path: '*',
    //     redirect: '/404'
    // },
]

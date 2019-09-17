import axios from 'axios';
import $http from './http'
let BASE_URL = '/xgov/gov';
let BASE_URL_LOGIN='/auth/api' //权限
let BASE_URL_METADATA='/metadata/api' //权限
const userPrivate={
    login: params => {return $http.post(`${BASE_URL_LOGIN}/login`,params)},  //权限登陆页面
    logout: params => {return $http.post(`${BASE_URL_LOGIN}/logout`,params)},  //权限登陆页面
    loginIn: params => {return $http.post(`${BASE_URL}/loginV1.do`,params)},  //登录
    loginOut: params => {return $http.get(`${BASE_URL}/logout.do`)},  //登录
};

//数据源模块
const dataSource={
    // getList:params => { return $http.post(`${BASE_URL}/data_source/list_action`,params) },                  //得到数据源列表
    getSourceByLayerId:params => { return $http.post(`${BASE_URL}/source_layer/getSourceByLayerId`,params) },                  //得到数据源列表
    // getLayerMenu:params => { return $http.get(`${BASE_URL}/data_source/layerMenu`,params) },                //获取所有菜单层级
    getLayers:params => { return $http.post(`${BASE_URL}/source_layer/getLayers`,params) },                //获取所有菜单层级
    checkData: params => { return $http.post(`${BASE_URL}/data_source/check_datasource`, params)},          //检查提交参数
    saveData:params => { return $http.post(`${BASE_URL}/data_source/update_add_action`, params)},           //保存参数
    getDataById:params => { return $http.get(`${BASE_URL}/data_source/get_source_id`, params)},             //获取当前数据源
    // deleteDataById:params => { return $http.get(`${BASE_URL}/data_source/deletes_action`, params)},         //删除数据
    removeSourceFromLayer:params => { return $http.post(`${BASE_URL}/source_layer/removeSourceFromLayer`, params)},         //删除数据
    // updateAddLayer:params => { return $http.post(`${BASE_URL}/data_source/updateAddLayer`, params)},        //新增层级
    saveOrUpdateLayer:params => { return $http.post(`${BASE_URL}/source_layer/saveOrUpdateLayer`, params)},        //新增层级
    // deleteLayer:params => { return $http.get(`${BASE_URL}/data_source/deleteLayer`, params)},               //新增层级
    deleteLayer:params => { return $http.post(`${BASE_URL}/source_layer/deleteLayer`, params)},               //新增层级
    getAllDataSource:params => { return $http.post(`${BASE_URL}/data_source/get_datatree`, params)},        //获取所有的数据源
    getDataTreeById:params => { return $http.post(`${BASE_URL}/data_source/get_datatree`, params)},         //根据父节点获取数据源表
    getUnrelationSource:params => { return $http.post(`${BASE_URL}/source_layer/getSources`, params)},         //根据父节点获取数据源表
    addSourceOnLayer:params => { return $http.post(`${BASE_URL}/source_layer/addSourceOnLayer`, params)},         //根据父节点获取数据源表
    getLayerPath:params => { return $http.post(`${BASE_URL}/source_layer/getLayerPath`, params)},         //根据父节点获取数据源表
};

//元数据模块
const metaData={
    getMetamodelMenu:params => { return $http.get(`${BASE_URL}/meta_model/getMetamodelMenu`, params)},      //新增层级
    getListMetaModel:params => { return $http.get(`${BASE_URL}/meta_model/itemListMetamodel`, params)},     //获得元模型
    getMetamodelAttr:params => { return $http.get(`${BASE_URL}/meta_model/getMetamodelAttr`, params)},      //获得元模型属性列表
    getMetamodelGroup:params => { return $http.get(`${BASE_URL}/meta_model/getMetamodelComb`, params)},     //获得元模型组合列表
    getMetamodelDepend:params => { return $http.get(`${BASE_URL}/meta_model/getMetamodelDep`, params)},     //获得元模型依赖列表
    getMetaDataDirs:params => { return $http.get(`${BASE_URL}/collectTask/metadata-dirs`, params)},         //获取挂载目录
    getDataCollectDs:params => { return $http.get(`${BASE_URL}/data_source/uncollect-ds`, params)},         //获取数据源
    AddCollectTask:params => { return $http.post(`${BASE_URL}/collectTask/addTask`, params)},               //保存数据采集
    getCollectList:params => { return $http.post(`${BASE_URL}/collectTask/collections`, params)},            //获取采集列表
    deleteCollect:params => { return $http.get(`${BASE_URL}/collectTask/collection`, params)},              //删除采集任务
    submitTask:params => { return $http.get(`${BASE_URL}/collectTask/submitTask`, params)},                 //提交采集任务
    getCollectDataById:params => { return $http.get(`${BASE_URL}/collectTask/collection-id`, params)},      //通过id获取采集数据
    getcheckList:params => { return $http.post(`${BASE_URL}/collectTask/exed-collections`, params)},         //获取审核列表
    getCompareList:params => { return $http.get(`${BASE_URL}/meta_data/compare`, params)},                  //版本比较列表
    submitCompareCheck:params => { return $http.post(`${BASE_URL}/meta_data/review`, params)},               //提交审核

    unreleaseCollecthis:params => { return $http.get(`${BASE_URL}/meta_data/unrelease-collecthis`, params)}, //查看列表
    compareData:params => { return $http.get(`${BASE_URL}/meta_data/compare`, params)},                      //查看列表



    careMenuList:params => { return $http.post(`${BASE_URL}/meta_data/menu_list`, params)},                  //数据维护list
    findByMenuId:params => { return $http.get(`${BASE_URL}/meta_data/findByMenuId`, params)},               //下拉树
    basicInfoMsg:params => { return $http.get(`${BASE_URL}/meta_data/basic_info`, params)},                 //获取基本信息
    getMetaVersion:params => { return $http.get(`${BASE_URL}/meta_data/getVersion`, params)},               //版本管理列表
    analyzeMetalist:params => { return $http.get(`${BASE_URL}/meta_data/search_meta`, params)},             //元数据分析列表

};
//数据标准
const dataStandard={
    dataElementImport: ()=> {return `${BASE_URL}/data_element/importFromExcel`},                            // 标准维护导入
    dataElementTemplateDownload: ()=> {return `${BASE_URL}/data_element/downloadTemplate`},                 // 标准维护下载
    dataSetImport: ()=> {return `${BASE_URL}/code/importFromExcel`},                                        // 标准分类导入
    dataSetTemplateDownload: ()=> {return `${BASE_URL}/code/downloadTemplate`},                             // 标准分类下载
    qualityDesignTableExport: ()=> {return `${BASE_URL}/qualityDesignTable/exportToExcel`},                 // 问题数据导出
    addDataElement:params => { return $http.post(`${BASE_URL}/data_element/addDataElement`, params)},        //新建及编辑标准维护
    editDataElement:params => { return $http.get(`${BASE_URL}/data_element/editElement`, params)},          //新建及编辑标准维护
    getCodeSet:params => { return $http.get(`${BASE_URL}/code/getCodeSet`, params)},                        //获取业务术语
    getAuditedCodeSet:params => { return $http.get(`${BASE_URL}/code/getAuditedCodeSet`, params)},                        //获取业务代码
    dataElementList:params => { return $http.post(`${BASE_URL}/data_element/dataElement_list`, params)},     //获取标准维护列表
    deleteElement:params => { return $http.get(`${BASE_URL}/data_element/deleteElement`, params)},          //删除list
    sendAudit:params => { return $http.get(`${BASE_URL}/data_element/sendAudit`, params)},                  //标准维护送审
    getDataTreeById:params => { return $http.post(`${BASE_URL}/data_source/get_datatree`, params)},         //
    saveRelation:params => { return $http.post(`${BASE_URL}/data_element/saveRelation`, params)},            //保存关联关系
    unbindRelation:params => { return $http.get(`${BASE_URL}/data_element/unbindRelation`, params)},        //接除关联关系
    getRelationShip:params => { return $http.get(`${BASE_URL}/data_element/relation_ship`, params)},        //得到关联关系
    changeDataElement:params => { return $http.post(`${BASE_URL}/data_element/change_element`, params)},     //标准维护变更
    getHistory:params => { return $http.get(`${BASE_URL}/data_element/history`, params)},     //标准维护变更
    checkDelete:params => { return $http.get(`${BASE_URL}/data_element/check_relation`, params)},     //标准维护变更
    dataElementRecall:params => { return $http.post(`${BASE_URL}/data_element/back`, params)},     //标准维护待审核撤回
    searchByColumn:params => { return $http.post(`${BASE_URL}/data_source/searchByColumn`, params)},         //根据父节点获取数据源表

    getMenuList:params => { return $http.get(`${BASE_URL}/data_set/menu_list`, params)},                    //标准分类展示tree
    addDataMetaMenu:params => { return $http.post(`${BASE_URL}/data_set/add_menu`, params)},                 //添加标准分类菜单
    deleteDataMetaMenu:params => { return $http.get(`${BASE_URL}/data_set/delMenu`, params)},               //删除菜单
    getAllElement:params => { return $http.get(`${BASE_URL}/data_element/get_all_element`, params)},        //获取所有标准维护
    getNodeListByNode:params => { return $http.post(`${BASE_URL}/data_set/serch_set`, params)},              //获取标准分类列表通过节点id
    addDataSet:params => { return $http.post(`${BASE_URL}/data_set/add_set`, params)},                       //新建标准分类
    dataSetSendAudit:params => { return $http.get(`${BASE_URL}/data_set/sendAudit`, params)},               //标准分类送审
    deleteDataSet:params => { return $http.get(`${BASE_URL}/data_set/del_set`, params)},                    //删除标准分类
    editDataSet:params => { return $http.get(`${BASE_URL}/data_set/edit_set`, params)},                     //编辑数据源
    changeDataSet:params => { return $http.get(`${BASE_URL}/data_set/change_set`, params)},                 //标准分类变更
    dataSetRecall:params => { return $http.post(`${BASE_URL}/data_set/back`, params)},     //标准分类待审核撤回
    updateAndAddCodeSet:params => { return $http.post(`${BASE_URL}/code/updateAndAddCodeSet`, params)},     //添加修改代码集
    deleteCodeSets:params => { return $http.get(`${BASE_URL}/code/deleteCodeSets`, params)},                //删除代码集
    getCodesFromSet:params => { return $http.get(`${BASE_URL}/code/getCodesFromSet`, params)},              //获取列表
    deleteCodes:params => { return $http.get(`${BASE_URL}/code/deleteCodes`, params)},                      //s删除list
    codeSetsendAudit:params => { return $http.get(`${BASE_URL}/code/sendAudit`, params)},                   //送审code
    codeSetAddCode:params => { return $http.post(`${BASE_URL}/code/addCode`, params)},                      //保存
    CodeSetUpDate:params => { return $http.post(`${BASE_URL}/code/updateCode`, params)},
    getCodeById:params => { return $http.get(`${BASE_URL}/code/getCodeById`, params)},                     //根据id获取code信息
    getCodeSetById:params => { return $http.get(`${BASE_URL}/code/getCodeSetById`, params)},                     //根据id获取code信息
    changeCode:params => { return $http.get(`${BASE_URL}/code/change_code`, params)},                     //根据id获取code信息
    codeRecall:params => { return $http.post(`${BASE_URL}/code/back`, params)},     //标准分类待审核撤回


    standardAuditList:params => { return $http.post(`${BASE_URL}/standard_audit/listAll`, params)},         //获取审核list
    updataAuditStatus:params => { return $http.post(`${BASE_URL}/standard_audit/updataAuditStatus`, params)}, //审核
    listAuditDetail:params => { return $http.get(`${BASE_URL}/standard_audit/listAuditDetail`, params)},    //详情


    getMenuDocumentContent:params => { return $http.get(`${BASE_URL}/documentcontent/getMenuDocumentContent`, params)},  //获取文档目录菜单
    addDocumentContent:params => { return $http.post(`${BASE_URL}/documentcontent/addDocumentContent`, params)},          //保存新建文件目录
    deleteDocumentFolder:params => { return $http.get(`${BASE_URL}/documentcontent/deleteFolder`, params)},              //删除目录

    uploadFile:_ => { return `${BASE_URL}/documentcontent/upload`},                             //上传文件
    updateFile:_ => { return `${BASE_URL}/documentcontent/updateFile`},                         //更新文件
    downLoadFile:_ => { return `${BASE_URL}/documentcontent/downLoadFile`},                     //下载文件
    getDocumentFile:params => { return $http.get(`${BASE_URL}/documentcontent/getDocumentFile`, params)},   //获取列表
    deleteDocumentFile:params => { return $http.get(`${BASE_URL}/documentcontent/deleteFile`, params)},       //删除文件

    changeDetail:params => { return $http.post(`${BASE_URL}/standard_audit/changeDetail`, params)},       //审核查看变更原因
    exportCodeSet:params => { return `${BASE_URL}/code/exportToExcel`},       //导出业务代码


};
//调度中心
const dispatchTask={
    getDispatchTaskList:params => { return $http.post(`${BASE_URL}/dispatchCenter/manager/tasks`, params)},   //获得调度列表
    getDispatchTaskById:params => { return $http.get(`${BASE_URL}/dispatchCenter/manager/getTaskById`, params)},   //根据id获取调度任务
    dispatchTaskConfig:params => { return $http.post(`${BASE_URL}/dispatchCenter/manager/config`, params)},   //调度配置
    dispatchExcuteTask:params => { return $http.get(`${BASE_URL}/dispatchCenter/excuteTask`, params)},       //立即执行
    dispatchPause:params => { return $http.post(`${BASE_URL}/dispatchCenter/manager/pause`, params)},        //暂停任务
    dispatchResume:params => { return $http.post(`${BASE_URL}/dispatchCenter/manager/resume`, params)},      //重启任务
    monitorList:params => { return $http.post(`${BASE_URL}/dispatchCenter/monitor/histories/lastest`, params)},         //监控列表

    queryAlarmList:params => { return $http.post(`${BASE_URL}/dispatch_alarm/queryAlarm`, params)},           //报警配置列表
    saveUpdateAlarm:params => { return $http.post(`${BASE_URL}/dispatch_alarm/updateAlarm`, params)},         //保存
    getAlarmById:params => { return $http.get(`${BASE_URL}/dispatch_alarm/getAlarmById`, params)},           //获取报警信息
    deleteAlarm:params => { return $http.get(`${BASE_URL}/dispatch_alarm/deleteAlarm`, params)},             //删除报警信息
    openAlarm:params => { return $http.get(`${BASE_URL}/dispatch_alarm/open`, params)},                      //打开/关闭警报
    checkIsOpen:params => { return $http.get(`${BASE_URL}/dispatch_alarm/verifyOpen`, params)},              //检查警报信息

    getLogsByTable:params => { return $http.post(`${BASE_URL}/qualityTaskLog/getLogsByTable`, params)},              //执行历史
    getMonitorHistories:params => { return $http.post(`${BASE_URL}/dispatchCenter/monitor/histories`, params)},              //执行历史

    getDepartment: params => {return $http.get(`${BASE_URL_LOGIN}/department/select`,params)},
    getDepartMentPeople: params => {return $http.get(`${BASE_URL_LOGIN}/department/getDepartMentPeople`,params)},
    getUserList: params => {return $http.post(`${BASE_URL_LOGIN}/user/select`,params)},
    getUserById: params => {return $http.get(`${BASE_URL_LOGIN}/user/getById`,params)}
};

const dataQuality={
    getRuleList:params => { return $http.get(`${BASE_URL}/qualityRule/getRuleList`, params)},                //获取质量规则
    deletesAction:params => { return $http.post(`${BASE_URL}/qualityRule/deletes_action`, params)},          //删除多个规则
    getAvailableTable:params => { return $http.post(`${BASE_URL}/qualityRule/getaAailableTable`, params)},   //根据源获取表
    createRuleDetail:params => { return $http.get(`${BASE_URL}/qualityRule/createRuleDetail`, params)},      //根据选择的表自动创建规则
    addQualityRule:params => { return $http.post(`${BASE_URL}/qualityRule/addQualityRule`, params)},         //保存规则
    getRuleDetailById:params => { return $http.get(`${BASE_URL}/qualityRuleDetail/getRuleDetail`, params)}, //获取表的校核对象
    updateRuleDetail:params => { return $http.post(`${BASE_URL}/qualityRuleDetail/updateRuleDetail`, params)}, //保存校核对象
    updateQualityRule:params => { return $http.get(`${BASE_URL}/qualityRule/refreshRule`, params)}, //重新生成质量规则
    addUserDefineQuality:params => { return $http.post(`${BASE_URL}/qualityDesignTable/addUserDefineQuality`, params)}, //新增唯一性校验

    downloadQualityTaskDetail:() => `${BASE_URL}/qualityTask/downloadQualityTaskDetail`, //数据质量导出
    exportQualityTaskDetail:(params) => $http.get(`${BASE_URL}/qualityTask/exportQualityTaskDetail`, params), //数据质量导出判断
    

    // getDesignList:params =>{ return $http.get(`${BASE_URL}/qualityDesignSource/getDesignList`, params)},     //设计规则列表
    // getDesignList:params =>{ return $http.post(`${BASE_URL}/data_source/list_action`, params)},     //设计规则列表
    listQualitySource:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/listQualitySource`, params)},     //设计规则规则列表
    countQualityTable:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/countQualityTable`, params)},     //设计规则规则列表个数
    updateEnable:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/updateEnable`, params)},     //设计规则校验开关
    toDraft:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/toDraft`, params)},     //设计规则校验开关
    debugAndSave:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/debugAndSave`, params)},     //设计规则批量调试
    getAvailableSource:params =>{ return $http.get(`${BASE_URL}/qualityDesignSource/getAvailableSource`, params)}, //新增设计列表
    deleteSourceDesign:params =>{ return $http.get(`${BASE_URL}/qualityDesignSource/deleteSourceDesign`, params)}, //保存
    createDesignBySourceId:params =>{ return $http.get(`${BASE_URL}/qualityDesignSource/createDesignBySourceId`, params)},

    refreshTableDesignList:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/refreshTableDesignList`, params)},   //获取详情列表
    createTransByTable:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/createTransByTable`, params)},           //回显表名数据
    buildTransByTable:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/buildTransByTable`, params)},           //回显表名数据

    getTablePreviewData:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/getTablePreviewData`, params)},        //得到表详情

    saveQualityTask:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/saveQualityTask`, params)},                 //保存
    getVerifyDetail:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/getVerifyDetail`, params)},                 //得到详情

    executeTask:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/execute`, params)},   //调试
    add2task:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/add2task`, params)},     //添加任务
    saveDesignTasks:params =>{ return $http.post(`${BASE_URL}/qualityTask/saveDesignTasks`, params)},     //添加任务

    sourceTask:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/source-task`, params)},     //添加任务
    findtables:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/findtables`, params)},      //
    findTaskTables:params =>{ return $http.post(`${BASE_URL}/qualityTask/findTaskTables`, params)},      //
    qualitytasksList:params =>{ return $http.post(`${BASE_URL}/qualityTask/qualitytasks`, params)},  //list
    qualitytaskdetail:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/qualitytaskdetail`, params)},  //获取详情
    deleteQualityTask:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/deleteQualityTask`, params)},   //删除list

    //问题数据
    queryProblemData:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/queryProblemData`, params)},   //获取问题数据列表
    getAllDBList:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/getAllDBList`, params)},           //获取所有数据源列表

    dealProblemData:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/dealProblemData`, params)},     //问题处理
    problemDetail:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/problemDetail`, params)},         //问题详情
    removeQualityOnTable:params =>{ return $http.get(`${BASE_URL}/qualityDesignTable/removeQualityOnTable`, params)},

    removeQualityOnTableByType:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/removeQualityOnTableByType`, params)},

    // 质量报告 模板获取
    showReportTemplte:params =>{ return $http.post(`${BASE_URL}/quality-report/showReportTemplte`, params)},
    // 质量报告 添加模板
    addReportTemplte:params =>{ return $http.post(`${BASE_URL}/quality-report/addReportTemplte`, params)},
    // 质量报告 获取数据表
    listTables:params =>{ return $http.post(`${BASE_URL}/quality-report/listTables`, params)},
    // 创建报告
    createReport:params =>{ return $http.post(`${BASE_URL}/quality-report/createReport`, params)},
    // 获取报告列表
    listReports:params =>{ return $http.post(`${BASE_URL}/quality-report/listReports`, params)},
    // 预览报告
    showReport:params =>{ return $http.get(`${BASE_URL}/quality-report/showReport`, params)},
    // 删除报告
    deleteReport:params =>{ return $http.post(`${BASE_URL}/quality-report/deleteReport`, params)},

    // 质量任务详情
    getTaskTables:params =>{ return $http.post(`${BASE_URL}/qualityTask/getTaskTables`, params)},
    getTaskTablesDetail:params =>{ return $http.post(`${BASE_URL}/qualityTask/getTaskTablesDetail`, params)},
    removeQualityById:params =>{ return $http.post(`${BASE_URL}/qualityDesignTable/removeQualityById`, params)},
};

const propertyManage={
    queryMainData:params =>{ return $http.get(`${BASE_URL}/main_data/searchMainData`, params)},               //获取list
    getDatasource:params =>{ return $http.get(`${BASE_URL}/main_data/getDatasource`, params)},                //获取数据源
    addMainData:params =>{ return $http.get(`${BASE_URL}/main_data/addMainData`, params)},                    //保存
    deleteDataBase:params =>{ return $http.get(`${BASE_URL}/main_data/deleteDataBase`, params)},              //删除list
    getMainTables:params =>{ return $http.get(`${BASE_URL}/main_data_detail/getMainTables`, params)},               //获取tree列表
    deleteTables:params =>{ return $http.get(`${BASE_URL}/main_data_detail/deleteTables`, params)},             //删除
    getShowData:params =>{ return $http.get(`${BASE_URL}/main_data_detail/getShowData`, params)},               //查询
    getFields:params =>{ return $http.get(`${BASE_URL}/main_data_detail/getFields`, params)},

    getAllTables:params =>{ return $http.get(`${BASE_URL}/main_data_detail/getAllTables`, params)},
    addTables:params =>{ return $http.get(`${BASE_URL}/main_data_detail/addTables`, params)},
};

const mapData={
    mapBaseData:params =>{ return $http.get(`${BASE_URL}/dataMap/index0`, params)},
    standardData:params =>{ return $http.get(`${BASE_URL}/dataMap/index1`, params)},
};
const datasourceManage = {
    getSourceById: params => $http.get(`${BASE_URL_METADATA}/data_source/get_source_id`, params, true)
}

export default {
    userPrivate,
    dataSource,
    metaData,
    dataStandard,
    dispatchTask,
    dataQuality,
    propertyManage,
    mapData,
    datasourceManage
}

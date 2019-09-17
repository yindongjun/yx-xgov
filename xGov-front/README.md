# x-gov

> 项目说明

npm install             安装依赖

npm run dev             运行项目

npm run build           打包

1.文件结构
    src>>
        assets>>
            icons        字体图标
            styles       公共样式
            images       图片
        common>>
            directives   通用指令
            api.js       项目所有接口
            https.js     封装请求
            utils.js     静态文件
            common.js    入口
        components       公共组件
        router           路由
        store            状态管理

        views>>          项目视图
            主模块>>
                子模块

2.编写说明：
    1）文件目录小写，Vue文件采用驼峰英文单词命名;
    2）css采用scss编译，所有公共样式定义在index.scss中,100%页面布局使用.main-inner-Allsize,自适应高度使用.main-inner;
    3）已编写当前项目通用模板，dialog,form,table等，在mock文件直接复用，可增加模板;
    4）项目views视图层由内向里模块化编写，便于维护




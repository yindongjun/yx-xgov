<template>
    <section class="login-outer-container">
        <!--<img class="img" src="../../assets/images/loginBk.png"/>-->
        <div class="logo-outer">
            <div class="logo mb15">
                <i style="font-size: 24px;margin-right: 5px"><svg-icon iconClass="govlogo-home"></svg-icon></i><span>xGov</span>
                <!-- <img src="../../assets/images/dataRun.png"/><span>数据治理</span> -->
            </div>
        </div>
        <div class="login-container-outer">
            <div class="login-container-l">
                <!-- <h3>企业数据治理支撑平台</h3> -->
                <h3>xGov企业数据治理支撑平台</h3>
                <p>企业数据治理支撑平台，集合元数据管理、数据质量监控、数据资产管理等功能，帮助企业提高内部数据质量，挖掘数据价值，提高市场竞争力。</p>
            </div>
            <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-position="top" class="demo-ruleForm login-container">
                <!-- <h3 class="title">用户登录</h3>-->
                <el-form-item prop="account" label="用户名称">
                    <el-input type="text" @keyup.enter.native="login('loginForm')" v-model="loginForm.account" auto-complete="off" placeholder="请输入账号">
                    </el-input>
                </el-form-item>
                <el-form-item prop="checkPass" label="用户密码">
                    <el-input type="password" @keyup.enter.native="login('loginForm')" v-model="loginForm.checkPass" auto-complete="off" placeholder="请输入密码">
                    </el-input>
                </el-form-item>
                <el-form-item style="width:100%;margin-top: 15px">
                    <el-button type="danger" size="small" style="width:100%;height: 34px;" @click="login('loginForm')" @keyup.enter.native="login" :loading="isLogin">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </section>
</template>

<script>
    import auth from '../../common/auth';
	export default {
		name: "login",
		components: {

        },
        data() {
            let checkUser = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('用户名不能为空'));
                } else {
                    const reg  = /^[a-zA-Z0-9_-]{3,16}$/;
                    if (reg.test(value)) {
                        callback();
                    } else {
                        return callback(new Error('请输入3到16位（字母，数字，下划线，减号）'));
                    }
                }
            };
            let checkpwd = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('密码不能为空'));
                } else {
                    const reg  = /^[a-zA-Z0-9_-]{3,16}$/;
                    if (reg.test(value)) {
                        callback();
                    } else {
                        return callback(new Error('请输入3到16位密码（字母，数字，下划线，减号）'));
                    }
                }
            };
            return {
                isLogin: false,
                loginForm: {
                    account: '',
                    checkPass: ''
                },
                loginRules: {
                    account: [
                        {validator: checkUser, trigger: 'blur'},
                    ],
                    checkPass: [
                        {validator: checkpwd, trigger: 'blur'},
                    ]
                },
            }
        },
        methods: {
            // login(formName) {
            //     let that = this;
            //     this.$refs[formName].validate((valid) => {
            //         if (valid) {
            //             that.isLogin = true;
            //             this.$urlApi.userPrivate.loginIn({"loginName":that.loginForm.account,"loginPwd":this.$md5(that.loginForm.checkPass)}).then(
            //                 (res) => {
            //                     that.isLogin = false;
            //                     if(res.code==='200'){
            //                         this.$auth.set('userName',that.loginForm.account);
            //                         this.$router.push({ path: '/metaData/default' });
            //                     }else{
            //                         this.$toast('error', res.message)
            //                     }
            //                 }
            //             ).catch(e => {
            //                 that.isLogin = false;
            //             })
            //         }
            //     })
            // },
            login(formName) {   //权限版本
                let that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        that.isLogin = true;
                        let formData = this.loginForm;
                        let param={
                            loginName: formData.account,
                            loginPwd: formData.checkPass
                        }
                        this.$urlApi.userPrivate.loginIn(param).then((res) => {
                            that.isLogin = false;
                            if(res.code==='200'){
                                const a = res.data.menuList.find(c => c.os === 'xgov').child
                                if (!a) {
                                    return this.$toast('error', '当前用户还未分配角色，请联系管理员')
                                }

                                this.$auth.setToken(res.data.sessionId);
                                localStorage.setItem(
                                    "user",
                                    JSON.stringify({user: formData.account})
                                );
                                localStorage.setItem(
                                    "menuList",
                                    JSON.stringify(res.data.menuList)
                                );
                                const b = this.getWhiteList(a)
                                setTimeout(() => this.$router.push({ path: b[0] }), 500)
                            }else{
                                this.$toast('error', res.message)
                            }
                        }).catch(e => {
                            that.isLogin = false;
                        })
                    }
                })
            },
            getWhiteList(data){
                const whiteList = [];
                let dealPermission = (permission) => {
                    permission.map(k => {
                         k.permission && whiteList.push(k.permission)
                        if (k.child) {
                            dealPermission(k.child);
                        }
                    })
           
                    return whiteList;
                }
                dealPermission(data)
                return whiteList
            }
        }
	}
</script>

<style lang="scss">
    @import '../../assets/styles/login.scss';
    .login-outer-container{
        background-size: 100% 100%;
        background-image: url("../../assets/images/loginBk.png");
        background-repeat: no-repeat;
    }
</style>

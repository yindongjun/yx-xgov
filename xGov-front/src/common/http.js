'use strict';
import axios from 'axios';
import qs from 'qs';
import auth from '@/common/auth.js';
import { app } from './../main';

let instance = axios.create({
    timeout: 20000,
    headers: {
        'X-Requested-With': 'XMLHttpRequest'
    },
    withCredentials: true
});

//请求时的拦截器
instance.interceptors.request.use(config => {
    store.commit('loading');
    return config;
}, error => {
    store.commit('unloading');
    return Promise.reject(error);
});
//请求后响应拦截器
instance.interceptors.response.use(res => {
    store.commit('unloading');
    return res
}, error => {
    store.commit('unloading');
    return Promise.resolve(error.res);
});

//return res返回的是一个对象
//网络或者服务器的错误
/*
function checkStatus(res){
    //这里可以加一些动作, 比如来个进度条结束动作
    //如果http状态码正常，则直接返回数据
    console.log(JSON.stringify(res))
    if (res && (res.code === "200")) {
        return res;
        //如果不需要除了data之外的数据，可以直接 return res.data
    }
    //异常状态下，把错误信息返回去
    return {
        code: "-404",
        message: '网络异常'
    };
}
*/

//程序端的错误
function checkCode(res){
    //如果code异常(这里已经包括网络错误，服务器错误，后端抛出的错误)，可以弹出一个错误提示，告诉用户
    if(res.data.code === '506') {
        app.$toast('error', res.data.message)
        throw new Error(res.data.message);
    }
    if(res.data.code === '400') {
        app.$toast('error', res.data.message)
        throw new Error(res.data.message);
    }
    if (res.data.code === '477') {
        app.$toast('error', '登录超时');
        setTimeout(() => {
            auth.removeToken();
            localStorage.removeItem('user');
            localStorage.removeItem('menuList');
            window.location = location.origin + '/public/#/'
        }, 1000)
    }
    //访问数据的权限
    if (res.code === '90004') {
        app.$message.error({
            message: res.message
        });
        app.$router.push({name: '数据查询'});
        throw new Error(res.message);
    }
    return res.data;
}

export default {
    post(url, data) {
        return axios({
            headers: {token: auth.getToken()},
            method: 'post', //请求协议
            url: url, //请求的地址
            data: qs.stringify(data), //post请求的数据
            timeout: 100000 //超时时间,单位毫秒
        }).then(
            (res) => {
                return checkCode(res);
            }
        )
    },
    get(url, params) {
        return axios({
            headers: {token: auth.getToken()},
            method: 'get',
            url: url,
            params, //get请求时带的参数
            timeout: 100000
        }).then(
            (res) => {
                return checkCode(res);
            }
        )
    }
}

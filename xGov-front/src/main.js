import Vue from 'vue';
import Vuex from 'vuex';
import App from './App';
import router from './router';
import './router/filter';
import ElementUI from 'element-ui';
import common from './common/common';
import store from './store/AppStore';
import VueWechatTitle from 'vue-wechat-title';
import 'element-ui/lib/theme-chalk/index.css';
import vueFilter from 'vue-filter';
import './assets/icons/index.js';
import QuillEditor from 'vue-quill-editor';
import vuescroll from 'vuescroll/dist/vuescroll-native';
import 'vuescroll/dist/vuescroll.css';
import md5 from 'js-md5';
import { TweenLite, TimelineLite, CSSPlugin, Ease } from 'gsap/TweenMax';
import SVG from 'svg.js';
import $ from 'jquery';
import Toast from '@/components/notice/notice.js';
import htmlToPDF from '@/common/htmlToPDF.js';
import auth from '@/common/auth.js';
import ErrorPlugin from './common/errorPlugin.js'

Vue.prototype.$md5 = md5;
Vue.prototype.$toast = Toast;
Vue.prototype.$auth = auth;
Vue.use(vuescroll);
Vue.use(VueWechatTitle);
Vue.use(ElementUI);
Vue.use(QuillEditor);
Vue.use(common);
Vue.use(vueFilter);
Vue.use(htmlToPDF);
Vue.use(ErrorPlugin);

Vue.config.productionTip = false;

export const app = new Vue({
    el: '#app',
    router,
    store,
    data() {
        return {
            loading: false
        };
    },
    render: h => h(App)
});

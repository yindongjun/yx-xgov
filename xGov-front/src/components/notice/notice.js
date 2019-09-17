import Vue from 'vue';
import Main from './notice.vue';

const ToastConstructor = Vue.extend(Main);

export default (type, text) => {
    const toastDom = new ToastConstructor({
        el: document.createElement('div'),
        data() {
            return {
                type,
                text
            };
        }
    });
    document.body.appendChild(toastDom.$el);
};

import Vue from 'vue';
import Resource from 'vue-resource';
import Router from 'vue-router';
import routes from './routes';
import ElementUI from 'element-ui';
import App from './App.vue';

Vue.use(ElementUI);

Vue.use(Router);

Vue.use(Resource);

const router = new Router({
    routes: routes
});

new Vue({
    router,
    render: h => h(App)
}).$mount("#app");
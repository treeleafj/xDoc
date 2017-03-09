import Index from './views/Index.vue';
import Login from './views/Login.vue';
import ChangePwd from './views/ChangePwd.vue';
import Account from './views/Account.vue';
import Role from './views/Role.vue';
import Log from './views/Log.vue';
import Manager from './views/Manager.vue';
import SEO from './views/SEO.vue';
import Banner from './views/Banner.vue';
import MessageBoard from './views/MessageBoard.vue';
import SystemConfig from './views/SystemConfig.vue';

const routes = [
    { path: '/index', component: Index },
    { path: '/account', component: Account },
    { path: '/role', component: Role },
    { path: '/log', component: Log },
    { path: '/systemConfig', component: SystemConfig },
    { path: '/login', component: Login, name: 'login', meta: { full: true } },
    { path: '/changePwd', component: ChangePwd},
    { path: '/manager', component: Manager},
    { path: '/seo', component: SEO},
    { path: '/banner', component: Banner},
    { path: '/messageBoard', component: MessageBoard},
    { path: '*', redirect: '/login' }
];

export default routes;
<!--suppress ALL -->
<template>
    <div class="el-menu-vertical">
        <div class="app-menu left-menu" id="left-menu">
            <!--<el-tree ref="aside" default-expand-all accordion highlight-current :data="menus" node-key="url" :props="defaultProps" @current-change="checkChangeHandle"></el-tree>-->

            <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
                <el-submenu v-for="menu in menus" :index="menu.label"  class="menu-user-box">
                    <template slot="title"><i :class="menu.icon"></i>{{menu.label}}</template>

                    <router-link v-for="child in menu.children" tag="li" class="el-menu-item" :to="{path:child.url}" active-class="is-active">
                        <i class="el-icon-menu"></i>{{child.label}}
                    </router-link>
                </el-submenu>
            </el-menu>
        </div>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                defaultProps: {
                  children: 'children',
                  label: 'label'
                },
                username: 'admin',
                isAdmin: false,
                menus: [
                    {
                        label: '系统管理',
                        icon: 'el-icon-setting',
                        id: 2,
                        children: [
                            { label: '系统用户', url: '/account' },
                            { label: '角色管理', url: '/role' },
                            { label: '操作日志', url: '/log' },
                            { label: '首页横幅', url: '/Banner' },
                            { label: 'SEO', url: '/SEO' },
                            { label: '管理员', url: '/Manager' },
                            { label: '消息', url: '/MessageBoard' },
                            { label: '基础数据', url: '/systemConfig' }
                        ]
                    },
                    {
                        label: '商户管理',
                        icon: 'el-icon-menu',
                        id: 1,
                        children: [
                            { label: '商户列表', url: '/aaa' }
                        ]
                    }
                ]
            }
        },

        mounted() {
        },
        
        methods : {

            //节点选中
            checkChangeHandle(data, checked, indeterminate) {
                if (data.url && this.$router.currentRoute.path != data.url) {
                    this.$router.push(data.url);
                }
            }
        }
    }


</script>

<style>

.left-menu{
    width: 200px;
    border-right: solid 2px #EAEDF1;
    display: block;
    position: fixed;
    top: 60px;
    bottom: 0px;
    z-index: 102;
    overflow-x: hidden;
}

.el-tree {
    border : 0px solid #d1dbe5
}

.el-menu {
    background-color : #3b8dbd;
}

.el-submenu__title {
    color : #FFF;
}

.menu-user-box > div:hover {
    background-color: #4898c7;
}

.el-submenu .el-menu {
    background-color : #FFF;
}

</style>
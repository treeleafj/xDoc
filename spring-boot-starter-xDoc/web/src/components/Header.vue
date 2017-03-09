<template>
    <div id="header">
        <el-menu theme="dark" default-active="1" class="el-menu" mode="horizontal" @select="selectHandler">
            <h2 class="logo-text">LOGO</h2>
            <el-submenu index="1" class="head-user-box">
                <template slot="title">{{reverseUsername}}</template>
                <el-menu-item index="1-1">修改密码</el-menu-item>
                <el-menu-item index="1-2">退出登录</el-menu-item>
            </el-submenu>
        </el-menu>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                username: ''
            }
        },
        methods: {
            selectHandler(key, keyPath) {
                if (key == '1-2') {
                    this.$http({
                        url: '/api/operater/logout',
                        method: 'post'
                    }).then((resp) => {
                        sessionStorage.clear();
                        this.$router.replace('login');
                    }, (resp) => {
                        alert('系统错误');
                        console.log(resp)
                    });
                } else if (key == '1-1') {
                    this.$router.push('changePwd');
                }
            },
            expandMenu() {
                if (document.getElementsByClassName('left-menu')[0].style.width == '180px') {
                    document.getElementsByClassName('left-menu')[0].style.width = '51px';
                    document.getElementsByClassName('right-menu')[0].style.left = '53px';
                    document.getElementById('main').style.left = '244px';
                } else {
                    document.getElementsByClassName('left-menu')[0].style.width = '180px';
                    document.getElementsByClassName('right-menu')[0].style.left = '180px';
                    document.getElementById('main').style.left = '360px';
                }

            }
        },
        computed: {
            reverseUsername() {
                let username = sessionStorage.getItem('username');
                return username || '未登录';
            }
        }
    }

</script>
<style>

    #header {
        position: fixed;
        width: 100%;
        height: 60px;
        background: #373d41;
        z-index: 101;
        border-bottom: solid 1px rgba(67, 164, 222, 0.64)
    }

    #header ul {
        background: #3b8dbd;
    }

    #header .btn-menu-control {
        border: 0 none;
        width: 38px;
        height: 30px;
        background-color: rgba(71, 148, 208, 0.56);
        color: transparent;
    }

    #header .head-user-box > div {
        color: #fff
    }

    #header .head-user-box > div:hover {
        border-bottom: 5px solid #4898c7;
        background-color: #4898c7;
    }

    #header .el-menu {
        border-radius : 0px;
    }

    .logo-text {
        float: left;
        color: #EEE;
        height: 60px;
        line-height: 60px;
        padding-left: 32px;
    }

    .menu-ext-btn {
        height: 60px;
        line-height: 60px;
        float: left;
        padding-left: 87px;
    }

    .el-menu--horizontal .el-submenu {
        float: right;
        margin-right: 4px;
    }

    .el-menu--horizontal .el-menu-item {
        float: right;
    }

    .el-menu-item, .el-submenu {
        /*padding: 0 25px;*/
        text-align: left;
    }


</style>
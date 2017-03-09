<template>
    <div class="login-div">
        <h2 class="login-title">系统登录</h2>
        <el-form ref="loginForm" label-width="0" :model="loginUser" :rules="rules">
            <el-form-item prop="username">
                <el-input placeholder="用户名" v-model="loginUser.username" @keyup.native.enter="login()"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input placeholder="密码" type="password" v-model="loginUser.password" @keyup.native.enter="login()"></el-input>
            </el-form-item>
            <el-button style="width:100%" type="primary" :loading="processing" @click="login()">登录</el-button>
        </el-form>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loginUser: {
                username: '',
                password: '',
                code: ''
            },
            codeBtnTitle: "获取短信验证码",
            intervalId: 0,
            codeTimeLong: 60,
            countTemp: 0,
            processing : false,
            codeBtnDisabled: false,
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
                ],
                code: [
                    { required: true, message: '请输入验证码', trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        login() {
            let _this = this;
            _this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    _this.processing = true;
                    
					this.$http.post('/api/operater/login', _this.loginUser, {emulateJSON:true}).then((response) => {
                        return response.json();
                    }, (resp) => {
                        this.processing = false;
                        this.$message.error('系统错误');
                    }).then((data) => {
                        _this.processing = false;
                        if(data.retCode == '000000') {
                            sessionStorage.setItem('username', data.username);
                            sessionStorage.setItem('id', data.id);
                            this.$router.replace('index');
                        } else {
                            this.$message.error('登录失败:' + data.msg);
                        }
                    });
                }
            });
        },
        getCode(){
            let _this = this;
            if(_this.loginUser.username == ""){
                _this.$refs.loginForm.validateField("username");
            }else{
                _this.$http.get('api/valid/send', {params: {target: _this.loginUser.username, type: 1}}).then(response => {
                    return response.json();
                }, response => {
                    this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode != "000000"){
                        this.$message.error('获取验证码失败:' + data.msg);
                    }
                });
                let codeTimeLong = _this.codeTimeLong;
                if(_this.intervalId){
                    clearInterval(_this.intervalId);
                }
                _this.intervalId = setInterval(()=>{
                    if(_this.countTemp<codeTimeLong){
                        _this.codeBtnDisabled = true;
                        _this.countTemp++;
                        _this.codeBtnTitle = _this.countTemp + "秒";
                    }else{
                        clearInterval(_this.intervalId);
                        _this.codeBtnTitle = "重新获取";
                        _this.countTemp = 0;
                        _this.codeBtnDisabled = false;
                    }
                }, 1000);
            }
        }
    }
};
</script>

<style scoped>
.login-div{
    width : 300px;
    height : 200px;
    text-align : center;
    margin: 150px auto;
}
.login-title{
    height : 50px;
}
.login-btn {
    width : 60px;
}
</style>
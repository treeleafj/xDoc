<template>
    <div class="changePwd-div">
        <h2 class="changePwd-title">修改密码</h2>
        <el-form ref="chnagePwdForm" label-width="0" :model="user" :rules="rules">
            <el-form-item prop="oldPassword">
                <el-input placeholder="旧密码" type="password" v-model="user.oldPassword" @keyup.native.enter="changePwd()"></el-input>
            </el-form-item>
            <el-form-item prop="newPassword">
                <el-input placeholder="新密码" type="password" v-model="user.newPassword"
                          @keyup.native.enter="changePwd()"></el-input>
            </el-form-item>
            <el-form-item prop="newPassword2">
                <el-input placeholder="再次输入新密码" type="password" v-model="user.newPassword2"
                          @keyup.native.enter="changePwd()"></el-input>
            </el-form-item>
            <el-button style="width:100%" type="primary" :loading="processing" @click="changePwd()">确认修改</el-button>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                user: {
                    oldPassword: '',
                    newPassword: '',
                    newPassword2: ''
                },
                processing: false,
                rules: {
                    oldPassword: [
                        {required: true, message: '请输入旧密码', trigger: 'blur'},
                        {min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}
                    ],
                    newPassword: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                        {min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}
                    ],
                    newPassword2: [
                        {required: true, message: '请再次输入新密码', trigger: 'blur'},
                        {min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}
                    ]
                }
            };
        },
        methods: {
            changePwd() {

                this.$refs.chnagePwdForm.validate((valid) => {

                    if (valid) {

                        if (this.user.newPassword != this.user.newPassword2) {
                            this.$message.error('两次新密码不一致');
                            return;
                        }

                        this.processing = true;

                        this.$http({
                            url: '/api/operater/changePwd',
                            method: 'post',
                            params: this.user
                        }).then((resp) => {
                            var data = JSON.parse(resp.data);
                            this.processing = false;
                            if (resp.ok && resp.data && data.retCode == '000000') {
                                this.$message.info('密码修改成功');
                                this.$router.replace('index');
                            } else {
                                this.$message.error('密码修改失败:' + data.msg);
                            }
                        }, (resp) => {
                            this.processing = false;
                            this.$message.error('系统错误');
                        });
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .changePwd-div {
        width: 300px;
        height: 200px;
        text-align: center;
        margin: 150px auto;
    }

    .changePwd-title {
        height: 50px;
    }

    .login-btn {
        width: 60px;
    }
</style>
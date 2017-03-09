<template>
    <div class="change-div">
        <el-form ref="seoForm" :model="config" label-width="180px">
            <el-collapse v-model="activeName" accordion>
                <el-collapse-item title="PC端首页TDK" name="1">
                    <el-form-item prop="pcindexTitle" label="页面标题">
                        <el-input placeholder="页面标题" v-model="config.pcindexTitle"></el-input>
                    </el-form-item>
                    <el-form-item prop="pcindexDesc" label="页面描述">
                        <el-input placeholder="页面描述" v-model="config.pcindexDesc"></el-input>
                    </el-form-item>
                    <el-form-item prop="pcindexKey" label="关键字">
                        <el-input placeholder="关键字" v-model="config.pcindexKey"></el-input>
                    </el-form-item>
                </el-collapse-item>

                <el-collapse-item title="PC端和创优势TDK" name="2">
                    <el-form-item prop="pcadvantageTitle" label="页面标题">
                        <el-input placeholder="页面标题" v-model="config.pcadvantageTitle"></el-input>
                    </el-form-item>
                    <el-form-item prop="pcadvantageDesc" label="页面描述">
                        <el-input placeholder="页面描述" v-model="config.pcadvantageDesc"></el-input>
                    </el-form-item>
                    <el-form-item prop="pcadvantageKey" label="关键字">
                        <el-input placeholder="关键字" v-model="config.pcadvantageKey"></el-input>
                    </el-form-item>
                </el-collapse-item>

                <el-collapse-item title="PC端服务案例TDK" name="3">
                    <el-form-item prop="pccaseTitle" label="页面标题">
                        <el-input placeholder="页面标题" v-model="config.pccaseTitle"></el-input>
                    </el-form-item>
                    <el-form-item prop="pccaseDesc" label="页面描述">
                        <el-input placeholder="页面描述" v-model="config.pccaseDesc"></el-input>
                    </el-form-item>
                    <el-form-item prop="pccaseKey" label="关键字">
                        <el-input placeholder="关键字" v-model="config.pccaseKey"></el-input>
                    </el-form-item>
                </el-collapse-item>

                <el-collapse-item title="移动端首页TDK" name="4">
                    <el-form-item prop="mindexTitle" label="页面标题">
                        <el-input placeholder="页面标题" v-model="config.mindexTitle"></el-input>
                    </el-form-item>
                    <el-form-item prop="mindexDesc" label="页面描述">
                        <el-input placeholder="页面描述" v-model="config.mindexDesc"></el-input>
                    </el-form-item>
                    <el-form-item prop="mindexKey" label="关键字">
                        <el-input placeholder="关键字" v-model="config.mindexKey"></el-input>
                    </el-form-item>
                </el-collapse-item>

                <el-collapse-item title="移动端关于我们TDK" name="5">
                    <el-form-item prop="maboutTitle" label="页面标题">
                        <el-input placeholder="页面标题" v-model="config.maboutTitle"></el-input>
                    </el-form-item>
                    <el-form-item prop="maboutDesc" label="页面描述">
                        <el-input placeholder="页面描述" v-model="config.maboutDesc"></el-input>
                    </el-form-item>
                    <el-form-item prop="maboutKey" label="关键字">
                        <el-input placeholder="关键字" v-model="config.maboutKey"></el-input>
                    </el-form-item>
                </el-collapse-item>

                <el-collapse-item title="第三方统计代码" name="6">
                    <el-form-item prop="pclink" label="PC段第三方统计代码">
                        <el-input placeholder="PC段第三方统计代码" v-model="config.pclink"></el-input>
                    </el-form-item>
                    <el-form-item prop="mlink" label="移动端第三方统计代码">
                        <el-input placeholder="移动端第三方统计代码" v-model="config.mlink"></el-input>
                    </el-form-item>
                </el-collapse-item>
            </el-collapse>

            <div style="width:100%; text-align:center;margin-top:50px;">
                <el-button type="primary" class="change-btn" :loading="processing" @click="chnage()">修改</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                activeName : '1',
                config: {},
                processing: false
            };
        },
        created() {
            this.$http({
                url: '/api/config/list',
                method: 'post'
            }).then((resp) => {
                var data = JSON.parse(resp.data);
                this.processing = false;
                if (resp.ok && resp.data && data.retCode == '000000') {
                    console.log(data)
                    var config = {};
                    for (var i = 0; i < data.array.length; i++) {
                        config[data.array[i].key] = data.array[i].value;
                    }
                    this.config = config;
                } else {
                    this.$message.error('查询失败:' + data.msg);
                }
            }, (resp) => {
                this.processing = false;
                this.$message.error('系统错误');
            });
        },
        methods: {
            chnage() {

                this.processing = true;

                this.$http({
                    url: '/api/config/update',
                    method: 'post',
                    params: this.config,
                }).then((resp) => {
                    var data = JSON.parse(resp.data);
                    this.processing = false;
                    if (resp.ok && resp.data && data.retCode == '000000') {
                        console.log(data)
                        this.$message.info('修改成功');
                    } else {
                        this.$message.error('修改失败:' + data.msg);
                    }
                }, (resp) => {
                    this.processing = false;
                    this.$message.error('系统错误');
                });
            }
        }
    }

</script>

<style scoped>
    .change-div {
        text-align : left;
        margin: 50px 50px;
    }

    .title {
        height: 50px;
    }

    .change-btn {
        width: 160px;
    }

</style>
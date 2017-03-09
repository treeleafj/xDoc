<template>
    <div class="change-div">
        <el-form ref="changeForm" label-width="100px" :model="about">
            <el-collapse v-model="activeName" accordion>
                <el-collapse-item title="PC端关于我们内容" name="1">
                    <el-form-item prop="content" label="关于我们">
                        <el-input type="textarea" :rows="10" v-model="about.pccontent" class="about-textare"></el-input>
                    </el-form-item>
                </el-collapse-item>
                <el-collapse-item title="移动端端关于我们内容" name="2">
                    <el-form-item prop="content" label="关于我们">
                        <el-input type="textarea" :rows="10" v-model="about.mcontent" class="about-textare"></el-input>
                    </el-form-item>
                </el-collapse-item>
            </el-collapse>
            <div style="margin-top:50px;text-align:center;">
                <el-button class="btn" type="primary" :loading="processing" @click="change()">修改</el-button>
            </div>
        </el-form>
    </div>
</template>

<script>
export default {
    data() {
        return {
            about: {
                pccontent: '',
                mcontent: ''
            },
            activeName : '1',
            processing : false
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
                for (var i = 0; i < data.array.length; i++) {
                    if (data.array[i].key == 'pccontent') {
                        this.about.pccontent = data.array[i].value;
                    }
                    if (data.array[i].key == 'mcontent') {
                        this.about.mcontent = data.array[i].value;
                    }
                }
            } else {
                this.$message.error('查询失败:' + data.msg);
            }
        }, (resp) => {
            this.processing = false;
            this.$message.error('系统错误');
        });
    },

    methods: {
        change() {
            this.processing = true;
            console.log(this.about.pccontent, this.about.mcontent)
            this.$http({
                url: '/api/config/update',
                method: 'post',
                params: {
                    pccontent : this.about.pccontent,
                    mcontent : this.about.mcontent
                },
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
    .btn {
        width : 160px;
    }
</style>
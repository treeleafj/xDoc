<template>
        <el-row>
            <el-col :span="24">
                <ul class="breadcrumb">
                    <li>操作日志管理</li>
                </ul>
            </el-col>
            <el-col :span="24">
                <div class="smart-widget">
                    <div class="smart-widget-inner">
                        <div class="smart-widget-body">
                            <el-form :inline="true" >
                                <el-form-item>
                                    <el-input v-model="searchParam.search" placeholder="档案名"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" size="small" @click="search">搜索</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="24">
                <el-table
                        :data="systemConfigData.list"
                        border
                        style="width: 100%">
                    <el-table-column
                            label="序号"
                            type="index">
                    </el-table-column>
                    <el-table-column
                            prop="id"
                            label="配置名称">
                    </el-table-column>
                    <el-table-column
                            prop="value"
                            label="配置内容">
                    </el-table-column>
                    <el-table-column
                            prop="desc"
                            label="描述">
                    </el-table-column>
                    <el-table-column
                            label="操作">
                        <template scope="scope">
                            <el-button type="text" size="small" @click="editSystemConfig(scope.row)">编辑</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
            <el-col :span="24">
                <el-pagination
                        @current-change="nextPage"
                        :current-page="page.pageNo"
                        :page-size="page.pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="page.total" >
                </el-pagination>
            </el-col>
            <el-col :span="24">
                <el-dialog title="编辑数据" v-model="editSystemConfigModal" size="tiny">
                    <el-form :model="editSystemConfigForm" ref="editSystemConfigForm" label-width="100px" class="add-account-form" :rules="rules">
                        <el-form-item label="值" prop="value">
                            <el-input v-model="editSystemConfigForm.value"></el-input>
                        </el-form-item>
                        <el-form-item label="描述" prop="desc">
                            <el-input v-model="editSystemConfigForm.desc"></el-input>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="editSystemConfigModal = false">取 消</el-button>
                        <el-button type="primary" @click="editSystemConfigPost">确 定</el-button>
                    </div>
                </el-dialog>
            </el-col>
        </el-row>
</template>
<script>
    export default{
        data(){
            return{
                searchParam: {
                    search: ""
                },
                page: {
                    pageNo: 1,
                    pageSize: 10,
                    total: 0
                },
                nextPage(val) {
                    this.page.pageNo = val;
                    this.fetchSystemConfigList();
                },
                systemConfigData: {},
                editSystemConfigModal: false,
                editSystemConfigForm: {
                    id: "",
                    value: "",
                    desc: ""
                },
                rules: {
                    value: [
                        { required: true, message: '请输入值', trigger: 'blur' }
                    ],
                    desc: [
                        { required: true, message: '请输入描述', trigger: 'blur' }
                    ]
                }
            }
        },
        components:{
        },
        methods: {
            search(){
                this.fetchSystemConfigList();
            },
            fetchSystemConfigList(){
                let _this = this;
                Object.assign(_this.searchParam, _this.page);
                _this.$http.get('api/systemConfig/list', {params: _this.searchParam}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.systemConfigData = data;
                        _this.page.pageNo = data.pageNo;
                        _this.page.total = data.total;
                    }else{
                        _this.$message.error('获取数据表失败:' + data.msg);
                    }
                });
            },
            editSystemConfig(systemConfig){
                this.editSystemConfigModal = true;
                this.editSystemConfigForm = {
                    id: systemConfig.id||"",
                    value: systemConfig.value||"",
                    desc: systemConfig.desc||""
                };
            },
            editSystemConfigPost(){
                let _this = this;
                _this.$refs.editSystemConfigForm.validate((valid) => {
                    if (valid) {
                        _this.$http.get('api/systemConfig/update',{params: _this.editSystemConfigForm} ).then(response => {
                            return response.json();
                        }, response => {
                            _this.$message.error('系统错误');
                        }).then(data => {
                            if(data.retCode == "000000"){
                                _this.editSystemConfigModal = false;
                                this.fetchSystemConfigList();
                            }else{
                                _this.$message.error('修改数据失败:' + data.msg);
                            }
                        });
                    }
                });
            }
        },
        watch: {
            '$route': 'fetchSystemConfigList'
        },
        created(){
            this.fetchSystemConfigList();
        }
    }
</script>
<style>
    .add-account-form{
        width: 360px;
    }
</style>

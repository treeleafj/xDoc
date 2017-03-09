<template>
        <el-row>
            <el-col :span="24">
                <ul class="breadcrumb">
                    <li>操作日志管理</li>
                </ul>
            </el-col>
            <el-tabs @tab-click="tabClick">
                <el-tab-pane label="应用操作日志" name="first">
                    <el-table
                            :data="operateLogData.list"
                            border
                            style="width: 100%">
                        <el-table-column
                                label="序号"
                                type="index">
                        </el-table-column>
                        <el-table-column
                                prop="ip"
                                label="ip">
                        </el-table-column>
                        <el-table-column
                                prop="operaterName"
                                label="操作者">
                        </el-table-column>
                        <el-table-column
                                prop="operaterType"
                                label="业务">
                        </el-table-column>
                        <el-table-column
                                prop="result"
                                label="结果">
                        </el-table-column>
                        <el-table-column
                                prop="createdtime"
                                label="时间">
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="商户操作日志" name="second">
                    <el-row>
                        <el-col :span="24" style="text-align: right;padding: 0 15px 15px 0;">
                            <el-select v-model="logParam.merchantId" @change="merchantChange">
                                <el-option
                                        v-for="merchant in merchants"
                                        :label="merchant.name"
                                        :value="merchant.id">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-row>
                    <el-table
                            :data="operateLogData.list"
                            border
                            style="width: 100%">
                        <el-table-column
                                label="序号"
                                type="index">
                        </el-table-column>
                        <el-table-column
                                prop="ip"
                                label="ip">
                        </el-table-column>
                        <el-table-column
                                prop="merchantId"
                                label="商户号">
                        </el-table-column>
                        <el-table-column
                                prop="operaterName"
                                label="操作者">
                        </el-table-column>
                        <el-table-column
                                prop="operaterType"
                                label="业务">
                        </el-table-column>
                        <el-table-column
                                prop="result"
                                label="结果">
                        </el-table-column>
                        <el-table-column
                                prop="createdtime"
                                label="时间">
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
            <el-col :span="24">
                <el-pagination
                        @current-change="nextpage"
                        :current-page="page.pageNo"
                        :page-size="page.pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="page.total" >
                </el-pagination>
            </el-col>
        </el-row>
</template>
<script>
    export default{
        data(){
            return{
                logParam: {
                    type: 1,
                    merchantId: "",
                    operateId: ""
                },
                page: {
                    pageNo: 1,
                    pageSize: 10,
                    total: 0
                },
                merchants: [],
                operateLogData: []
            }
        },
        components:{
        },
        methods: {
            tabClick(tab, event) {
                if(tab.name == "second"){
                    this.logParam.type = 2;
                    this.fetchOperateLogList();
                }else{
                   this.logParam = {
                        type: 1,
                        merchantId: "",
                        operateId: ""
                    };
                    this.fetchOperateLogList();
                }
            },
            fetchOperateLogList(){
                let _this = this;
                Object.assign(_this.logParam, _this.page);
                _this.$http.get('api/operateLog/list', {params: _this.logParam}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.operateLogData = data;
                        _this.page.pageNo = data.pageNo;
                        _this.page.total = data.total;
                    }else{
                        _this.$message.error('获取操作日志列表失败:' + data.msg);
                    }
                });
            },
            nextpage(val) {
                this.page.pageNo = val;
                this.fetchOperateLogList();
            },
            fetchMerchantList(){
                let _this = this;
                _this.$http.get('api/merchant/list', {params: {pageNo: 1, pageSize: 9999}}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.merchants = data.list;
                    }else{
                        _this.$message.error('获取商户列表失败:' + data.msg);
                    }
                });
            },
            merchantChange(val){
                this.logParam.type = 2;
                this.logParam.merchantId = val;
                this.fetchOperateLogList();
            }
        },
        watch: {
            '$route': 'fetchOperateLogList, fetchMerchantList'
        },
        created(){
            this.fetchOperateLogList();
            this.fetchMerchantList();
        }
    }
</script>
<style>
</style>

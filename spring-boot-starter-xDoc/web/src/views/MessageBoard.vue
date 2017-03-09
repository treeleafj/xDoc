<template>
    <div v-loading="processing">
        <div class="topbar">
            <el-row type="flex" justify="end">
                <el-col :span="4" :offset="13">
                    类型:
                    <el-select v-model="type" clearable placeholder="全部" @change="handleSearch()">
                        <el-option v-for="item in typeOptions" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                </el-col>
                <el-col :span="6">
                    <el-input placeholder="请输入姓名或联系号码" v-model="search" @keyup.native.enter="handleSearch()">
                        <el-button slot="append" icon="search" @click="handleSearch()"></el-button>
                    </el-input>
                </el-col>
            </el-row>
        </div>
        <el-table :data="list" border style="width: 100%;">
            <el-table-column label="留言时间" width="200">
                <template scope="scope">
                    <el-icon name="time"></el-icon>
                    <span style="margin-left: 10px">{{ scope.row.createdtime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="咨询类型" width="100">
                <template scope="scope">
                    {{scope.row.type == '1' ? '企业名称' : '商标名'}}
                </template>
            </el-table-column>
            <el-table-column prop="content" label="内容"></el-table-column>
            <el-table-column prop="phone" label="联系号码" width="100"></el-table-column>
            <el-table-column prop="name" label="联系人" width="100"></el-table-column>
            <el-table-column label="状态" width="100">
                <template scope="scope">
                    {{scope.row.status == '1' ? '未联系' : '已联系'}}
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="联系结果" width="300"></el-table-column>
            <el-table-column prop="status" label="操作" width="150">
                <template scope="scope">
                    <el-button v-if="scope.row.status == '1'" @click="handleEdit(scope.$index, scope.row)">
                        备注为已联系
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="block page">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page="pageNo"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="pageSize"
                    layout="total, prev, pager, next, jumper"
                    :total="total"
                    :loading="processing">
            </el-pagination>
        </div>

        <el-dialog title="备注联系结果" v-model="dialogFormVisible">
            <el-form :model="form">
                <el-form-item prop="remark" label="联系结果:">
                    <el-input type="textarea" :rows="10" v-model="form.remark"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleLink()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                list: [],
                pageNo: 1,
                pageSize: 10,
                total: 0,
                search: '',
                type: '',
                processing: false,

                typeOptions: [
                    {label: '企业名称', value: '1'},
                    {label: '商标名', value: '2'}
                ],

                dialogFormVisible: false,
                form: {
                    id: null,
                    remark: null
                }
            };
        },
        methods: {
            handleSearch() {
                this.fetchData();
            },
            handleEdit(rowIndex, data) {
                console.log(rowIndex, data);
                this.form.remark = '';
                this.form.id = data.id;
                this.dialogFormVisible = true;//显示弹出框
            },
            handleLink() {
                this.$http({
                    url: '/api/messageboard/link',
                    method: 'post',
                    params: {
                        id: this.form.id,
                        remark: this.form.remark
                    }
                }).then((resp) => {
                    this.dialogFormVisible = false;//隐藏弹出框
                    this.processing = false;
                    var data = JSON.parse(resp.data);
                    if (resp.ok && resp.data && data.retCode == '000000') {
                        console.log(data)
                        this.fetchData();
                        this.$message.info('修改成功');
                    } else {
                        this.$message.error('修改失败:' + data.msg);
                    }
                }, (resp) => {
                    this.processing = false;
                    this.$message.error('系统错误');
                });
            },
            handleCurrentChange(val) {
                this.pageNo = val;
                console.log(`当前页: ${val} ${this.pageNo}`);
                this.fetchData();
            },
            fetchData() {
                this.processing = true;
                this.$http({
                    url: '/api/messageboard/list',
                    method: 'post',
                    params: {
                        pageNo: this.pageNo,
                        pageSize: this.pageSize,
                        search: this.search,
                        type: this.type
                    }
                }).then((resp) => {
                    this.processing = false;
                    var data = JSON.parse(resp.data);
                    if (resp.ok && resp.data && data.retCode == '000000') {
                        console.log(data)
                        this.list = data.list;
                        this.pageNo = data.pageNo;
                        this.pageSize = data.pageSize;
                        this.total = data.total;
                    } else {
                        this.$message.error('查询失败:' + data.msg);
                    }
                }, (resp) => {
                    this.processing = false;
                    this.$message.error('系统错误');
                });
            }
        },
        created() {
            this.fetchData();
        }
    }
</script>

<style scoped>
    .page {
        margin: 20px auto;
    }

    .topbar {
        margin: 15px auto 15px;
    }
</style>
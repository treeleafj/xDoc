<template>
    <div v-loading="processing">
        <div class="topbar">
            <el-row type="flex" justify="end">
                <el-col :span="2" :offset="22">
                    <el-button @click="handleShowAddForm()">
                        添加
                    </el-button>
                </el-col>
            </el-row>
        </div>
        <el-table :data="list" border style="width: 100%;">
            <el-table-column prop="id" label="ID"></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column label="状态">
                <template scope="scope">
                    <label v-if="scope.row.status == '1'" style="color:green">已启用</label>
                    <label v-if="scope.row.status == '2'" style="color:red">已禁用</label>
                </template>
            </el-table-column>
            <el-table-column label="创建时间">
                <template scope="scope">
                    <el-icon name="time"></el-icon>
                    <span style="margin-left: 10px">{{ scope.row.createdtime }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="操作" width="200">
                <template scope="scope">
                    <el-button v-if="scope.row.status == '1'" size="small" @click="handleStatus(scope.$index, scope.row)">
                        禁用
                    </el-button>
                    <el-button v-if="scope.row.status == '2'" size="small" @click="handleStatus(scope.$index, scope.row)">
                        启用
                    </el-button>
                    <el-button  size="small" @click="handlePassword(scope.$index, scope.row)">
                        重置密码
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
		
        <el-dialog title="添加管理员账户" v-model="dialogFormVisible" size="tiny" class="dialogForm">
            <el-form :model="form">
                <el-form-item prop="username" label="用户名:">
                    <el-input type="text" v-model="form.username"></el-input>
                </el-form-item>
                <el-form-item prop="password" label="密码:">
                    <el-input type="password" v-model="form.password"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSave()">确 定</el-button>
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
                dialogFormVisible: false,
                form: {}
            };
        },
        methods: {
            handleShowAddForm() {
                this.form = {};
                this.dialogFormVisible = true;
            },
            handlePassword(rowIndex, data) {
                this.$confirm('是否确认要重置密码?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    this.$http({
                        url: '/api/manager/resetpassword',
                        method: 'post',
                        params: {
                            id: data.id
                        }
                    }).then((resp) => {
                        this.processing = false;
                        var data = JSON.parse(resp.data);
                        if (resp.ok && resp.data && data.retCode == '000000') {
                            console.log(data)

                            this.$alert('新密码为:' + data.password, '重置成功', {
                                confirmButtonText: '确定',
                                callback: action => {
                                }
                            });
                        } else {
                            this.$message.error('重置失败:' + data.msg);
                        }
                    }, (resp) => {
                        this.processing = false;
                        this.$message.error('系统错误');
                    });
                }).catch(() => {
                });;
            },
            handleStatus(rowIndex, data) {
                console.log(rowIndex, data);

                this.$confirm('是否确认' + (data.status == '1' ? '禁用' : '启用') + '该用户?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    this.$http({
                        url: '/api/manager/status',
                        method: 'post',
                        params: {
                            id: data.id,
                            status: data.status == '1' ? '2' : '1'
                        }
                    }).then((resp) => {
                        this.processing = false;
                        var data = JSON.parse(resp.data);
                        if (resp.ok && resp.data && data.retCode == '000000') {
                            console.log(data)
                            this.$message.info('修改成功');
                            this.fetchData();
                        } else {
                            this.$message.error('修改失败:' + data.msg);
                        }
                    }, (resp) => {
                        this.processing = false;
                        this.$message.error('系统错误');
                    });
                }).catch(() => {
                });;
            },

            handleCurrentChange(val) {
                this.pageNo = val;
                console.log(`当前页: ${val} ${this.pageNo}`);
                this.fetchData();
            },

            handleSave() {
                this.$http({
                    url: '/api/manager/save',
                    method: 'post',
                    params: this.form
                }).then((resp) => {
                    this.processing = false;
                    var data = JSON.parse(resp.data);
                    if (resp.ok && resp.data && data.retCode == '000000') {
                        this.dialogFormVisible = false;
                        console.log(data)
                        this.$message.info('添加成功');
                        this.fetchData();
                    } else {
                        this.$message.error('添加失败:' + data.msg);
                    }
                }, (resp) => {
                    this.processing = false;
                    this.$message.error('系统错误');
                });
            },

            fetchData() {
                this.processing = true;
                this.$http({
                    url: '/api/manager/list',
                    method: 'post',
                    params: {
                        pageNo: this.pageNo,
                        pageSize: this.pageSize
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
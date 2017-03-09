<template>
        <el-row>
            <el-col :span="24">
                <ul class="breadcrumb">
                    <li>系统用户管理</li>
                </ul>
            </el-col>
            <el-col :span="24">
                <div class="smart-widget">
                    <div class="smart-widget-inner">
                        <div class="smart-widget-body">
                            <el-button type="primary" size="small" @click="addAccountModal=true">新增账户</el-button>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="24">
                <el-table
                        :data="accountData.list"
                        border
                        style="width: 100%">
                    <el-table-column
                            label="序号"
                            type="index">
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="姓名">
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="账号/邮箱">
                    </el-table-column>
                    <el-table-column
                            prop="mobile"
                            label="手机号">
                    </el-table-column>
                    <el-table-column
                            prop="roles"
                            label="角色"
                            :formatter="formatterRoles">
                    </el-table-column>
                    <el-table-column
                            prop="status"
                            label="账户状态"
                            :formatter="formatterStatus">
                    </el-table-column>
                    <el-table-column
                            label="操作">
                        <template scope="scope">
                            <el-button type="text" size="small" @click="editAccount(scope.row)">编辑</el-button>
                            <el-button type="text" size="small"  @click="delAccount(scope.row.id, scope.row.name)">删除</el-button>
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
                <el-dialog title="新增用户" v-model="addAccountModal" size="tiny">
                    <el-form :model="addAccountForm" ref="addAccountForm" label-width="100px" class="add-account-form" :rules="rules">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="addAccountForm.name"></el-input>
                        </el-form-item>
                        <el-form-item label="帐号" prop="username">
                            <el-input v-model="addAccountForm.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input type="password" v-model="addAccountForm.password"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="addAccountForm.email"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号" prop="mobile">
                            <el-input v-model="addAccountForm.mobile" ></el-input>
                        </el-form-item>
                        <el-form-item label="角色权限" >
                            <el-radio-group v-model="addAccountForm.radio">
                                <el-radio :label="1">按角色</el-radio>
                                <el-radio :label="2">按角色组</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="" >
                            <el-select v-model="addAccountForm.roleIds" multiple v-if="addAccountForm.radio==1">
                                <el-option
                                        v-for="role in roleList"
                                        :label="role.name"
                                        :value="role.id">
                                </el-option>
                            </el-select>
                            <el-select v-model="addAccountForm.roleGroupIds" multiple v-if="addAccountForm.radio==2">
                                <el-option
                                        v-for="roleGroup in roleGroupList"
                                        :label="roleGroup.name"
                                        :value="roleGroup.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="addAccountModal = false">取 消</el-button>
                        <el-button type="primary" @click="addAccount">确 定</el-button>
                    </div>
                </el-dialog>
                <el-dialog title="编辑用户" v-model="editAccountModal" size="tiny">
                    <el-form :model="editAccountForm" ref="editAccountForm" label-width="100px" class="add-account-form" :rules="rules">
                        <el-form-item label="姓名" prop="name">
                            <el-input v-model="editAccountForm.name"></el-input>
                        </el-form-item>
                        <el-form-item label="帐号" prop="username">
                            <el-input v-model="editAccountForm.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input type="password" v-model="editAccountForm.password"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="editAccountForm.email"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号" prop="mobile">
                            <el-input v-model="editAccountForm.mobile" ></el-input>
                        </el-form-item>
                        <el-form-item label="角色权限" >
                            <el-radio-group v-model="editAccountForm.radio">
                                <el-radio :label="1">按角色</el-radio>
                                <el-radio :label="2">按角色组</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="" >
                            <el-select v-model="editAccountForm.roleIds" multiple v-if="editAccountForm.radio==1">
                                <el-option
                                        v-for="role in roleList"
                                        :label="role.name"
                                        :value="role.id">
                                </el-option>
                            </el-select>
                            <el-select v-model="editAccountForm.roleGroupIds" multiple v-if="editAccountForm.radio==2">
                                <el-option
                                        v-for="roleGroup in roleGroupList"
                                        :label="roleGroup.name"
                                        :value="roleGroup.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="editAccountModal = false">取 消</el-button>
                        <el-button type="primary" @click="editAccountPost">确 定</el-button>
                    </div>
                </el-dialog>
            </el-col>
        </el-row>
</template>
<script>
    export default{
        data(){
            let validateMobile = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入手机号'));
                } else if (!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(value))) {
                    callback(new Error('手机号格式错误'));
                } else {
                    callback();
                }
            };
            let validateEmail = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入邮箱'));
                } else if (!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(value))) {
                    callback(new Error('邮箱格式错误'));
                } else {
                    callback();
                }
            }
            return{
                page: {
                    pageNo: 1,
                    pageSize: 10,
                    total: 0
                },
                addAccountModal: false,
                editAccountModal: false,
                accountData: {},
                roleList: [],
                roleGroupList: [],
                addAccountForm: {
                    radio: 1,
                    name: "",
                    username: "",
                    password: "",
                    email: "",
                    mobile: "",
                    roleIds: [],
                    roleGroupIds: []
                },
                editAccountForm: {
                    radio: 1,
                    name: "",
                    username: "",
                    password: "",
                    email: "",
                    mobile: "",
                    roleIds: [],
                    roleGroupIds: []
                },
                rules: {
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
                    ],
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
                    ],
                    mobile: [
                        { validator: validateMobile, trigger: 'blur', required: true }
                    ],
                    email: [
                        { validator: validateEmail, trigger: 'blur', required: true }
                    ]
                }
            }
        },
        components:{
        },
        methods: {
            nextPage(val) {
                this.page.pageNo = val;
                this.fetchOperaterList();
            },
            fetchOperaterList(){
                let _this = this;
                _this.$http.get('api/operater/operaterList', {params: {pageNo: _this.page.pageNo, pageSize: _this.page.pageSize}}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.accountData = data;
                        _this.page.pageNo = data.pageNo;
                        _this.page.total = data.total;
                    }else{
                        _this.$message.error('获取用户列表失败:' + data.msg);
                    }
                });
            },
            fetchRoleList(){
                let _this = this;
                _this.$http.get('api/role/roleList', {params: {}}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.roleList = data.list;
                    }else{
                        _this.$message.error('获取角色列表失败:' + data.msg);
                    }
                });
            },
            fetchRoleGroupList(){
                let _this = this;
                _this.$http.get('api/roleGroup/roleGroupList', {params: {pageNo: 1, pageSize: 999}}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.roleGroupList = data.list;
                    }else{
                        _this.$message.error('获取角色组列表失败:' + data.msg);
                    }
                });
            },
            formatterStatus(row, column){
                let statusStr = ['已禁用', '正常'];
                return statusStr[row.status];
            },
            formatterRoles(row, column){
                let roleNames = [];
                let roles = row.roles;
                roles.forEach((e,i) => {
                    roleNames.push(e.name);
                });
                return roleNames.join("、");
            },
            addAccount(){
                let _this = this;
                _this.$refs.addAccountForm.validate((valid) => {
                    if (valid) {
                        _this.$http.get('api/operater/addOperater',{params: _this.addAccountForm} ).then(response => {
                            return response.json();
                        }, response => {
                            _this.$message.error('系统错误');
                        }).then(data => {
                            if(data.retCode == "000000"){
                                _this.addAccountModal = false;
                                this.fetchOperaterList();
                            }else{
                                _this.$message.error('添加用户失败:' + data.msg);
                            }
                        });
                    }
                });
            },
            editAccount(account){
                let _this = this;
                _this.editAccountModal = true;
                _this.editAccountForm = {
                    radio: 1,
                    name: account.name || "",
                    username: account.username || "",
                    password: account.password || "",
                    email: account.email || "",
                    mobile: account.mobile || "",
                    roleIds: [],
                    roleGroupIds: []
                };
                if(account.roles.length){
                    _this.editAccountForm.radio = 1;
                    account.roles.forEach((e,i) => {
                        _this.editAccountForm.roleIds.push(e.id);
                    });
                }else if(account.roleGroups.length){
                    _this.editAccountForm.radio = 2;
                    account.roleGroups.forEach((e,i) => {
                        _this.editAccountForm.roleGroupIds.push(e.id);
                    });
                }
            },
            editAccountPost(){
                let _this = this;
                _this.$refs.editAccountForm.validate((valid) => {
                    if (valid) {
                        _this.$http.get('api/operater/updateOperater',{params: _this.editAccountForm} ).then(response => {
                            return response.json();
                        }, response => {
                            _this.$message.error('系统错误');
                        }).then(data => {
                            if(data.retCode == "000000"){
                                _this.editAccountModal = false;
                                this.fetchOperaterList();
                            }else{
                                _this.$message.error('修改用户失败:' + data.msg);
                            }
                        });
                    }
                });
            },
            delAccount(id, name){
                let _this = this;
                _this.$confirm('此操作将永久删除 '+ name +' , 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.$http.get('api/operater/delOperater', {params: {id: id}}).then(response => {
                        return response.json();
                    }, response => {
                        _this.$message.error('系统错误');
                    }).then(data => {
                        if(data.retCode == "000000"){
                            _this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            _this.fetchOperaterList();
                        }else{
                            _this.$message.error('删除用户失败:' + data.msg);
                        }
                    });
                }).catch(() => {
                    _this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },
        watch: {
            '$route': 'fetchOperaterList,fetchRoleList,fetchRoleGroupList'
        },
        created(){
            this.fetchOperaterList();
            this.fetchRoleList();
            this.fetchRoleGroupList();
        }
    }
</script>
<style>
    .add-account-form{
        width: 360px;
    }
    .add-account-form .el-select{
        width: 100%;
    }
</style>

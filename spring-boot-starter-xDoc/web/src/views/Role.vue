<template>
        <el-row>
            <el-col :span="24">
                <ul class="breadcrumb">
                    <li>系统角色管理</li>
                </ul>
            </el-col>
            <el-tabs @tab-click="tabClick">
                <el-tab-pane label="角色管理" name="first">
                    <el-row>
                        <el-col :span="2" style="padding-bottom: 15px;">
                            <el-input
                                    placeholder="搜索"
                                    icon="search"
                                    v-model="searchRoleParam.name"
                                    :on-icon-click="searchRole">
                            </el-input>
                        </el-col>
                        <el-col :span="2" style="padding: 5px 0 0 15px">
                            <el-button type="primary" size="small" @click="addRoleModal=true">新增角色</el-button>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-dialog title="新增角色" v-model="addRoleModal" size="tiny">
                                <el-form :model="addRoleForm" ref="addRoleForm" label-width="100px" class="add-account-form" :rules="rules">
                                    <el-form-item label="角色名称" prop="name">
                                        <el-input v-model="addRoleForm.name"></el-input>
                                    </el-form-item>
                                    <el-form-item label="角色描述" prop="remark">
                                        <el-input v-model="addRoleForm.remark"></el-input>
                                    </el-form-item>
                                    <el-form-item label="角色组" >
                                        <el-select v-model="addRoleForm.roleGroupIds" multiple>
                                            <el-option
                                                    v-for="roleGroup in roleGroupList"
                                                    :label="roleGroup.name"
                                                    :value="roleGroup.id">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-form>
                                <div slot="footer" class="dialog-footer">
                                    <el-button @click="addRoleModal = false">取 消</el-button>
                                    <el-button type="primary" @click="addRole">确 定</el-button>
                                </div>
                            </el-dialog>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="11">
                            <el-table
                                :data="roleListData.list"
                                border
                                highlight-current-row
                                @current-change="selectRole"
                                style="width: 100%">
                                <el-table-column
                                        prop="name"
                                        label="角色名称">
                                </el-table-column>
                                <el-table-column
                                        prop="roleGroup"
                                        label="角色组"
                                        :formatter="formatterRoleGroups">
                                </el-table-column>
                                <el-table-column
                                        label="操作">
                                    <template scope="scope">
                                        <el-button type="text" size="small"  @click="delRole(scope.row)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                        <el-col :span="1"></el-col>
                        <el-col :span="12">
                            <el-tree
                                    :data="resourceListData.data"
                                    show-checkbox
                                    node-key="id"
                                    ref="tree"
                                    highlight-current
                                    default-expand-all
                                    auto-expand-parent
                                    :props="treeProps">
                            </el-tree>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="角色组管理" name="second">角色组管理</el-tab-pane>
            </el-tabs>
        </el-row>
</template>
<script>
    export default{
        data(){
            return{
                searchRoleParam: {
                    name: ""
                },
                roleListData: [],
                resourceListData: [],
                treeProps: {
                    children: 'childInfo',
                    label: 'name'
                },
                addRoleModal: false,
                roleGroupList: [],
                addRoleForm: {
                    name: "",
                    remark: "",
                    roleGroupIds: ""
                },
                rules: {
                    name: [
                        { required: true, message: '请输入角色名', trigger: 'blur' },
                        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
                    ]
                }
            }
        },
        components:{
        },
        methods: {
            tabClick(tab, event) {
                if(tab.name == "second"){

                }else{

                }
            },
            fetchRoleListList(){
                let _this = this;
                _this.$http.get('api/role/roleList', {params: _this.searchRoleParam}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.roleListData = data;
                    }else{
                        _this.$message.error('获取角色列表失败:' + data.msg);
                    }
                });
            },
            fetchResourceByRoleId(id){
                let _this = this;
                _this.$http.get('api/resource/resourceByRoleId', {params: {roleId: id}}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        let resourceIds = [];
                        data.list.forEach((e,i) => {
                            resourceIds.push(e.id);
                        });
                        _this.$refs.tree.setCheckedKeys(resourceIds);
                    }else{
                        _this.$message.error('获取资源列表失败:' + data.msg);
                    }
                });
            },
            fetchResourceAll(){
                let _this = this;
                _this.$http.get('api/resource/resourceAll', {params: {}}).then(response => {
                    return response.json();
                }, response => {
                    _this.$message.error('系统错误');
                }).then(data => {
                    if(data.retCode == "000000"){
                        _this.resourceListData = data;
                    }else{
                        _this.$message.error('获取资源列表失败:' + data.msg);
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
            formatterRoleGroups(row, column){
                let roleGroupNames = [];
                let roleGroups = row.roleGroup;
                roleGroups.forEach((e,i) => {
                    roleGroupNames.push(e.name);
                });
                return roleGroupNames.join("、");
            },
            searchRole(){
                this.fetchRoleListList();
            },
            selectRole(currentRow, oldCurrentRow){
                this.fetchResourceByRoleId(currentRow.id);
            },
            delRole(row){
                let _this = this;
                _this.$confirm('此操作将永久删除 '+ row.name +' , 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.$http.get('api/role/delRole', {params: {id: row.id}}).then(response => {
                        return response.json();
                    }, response => {
                        _this.$message.error('系统错误');
                    }).then(data => {
                        if(data.retCode == "000000"){
                            _this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            _this.fetchRoleListList();
                        }else{
                            _this.$message.error('删除角色失败:' + data.msg);
                        }
                    });
                }).catch(() => {
                    _this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            addRole(){
                let _this = this;
                _this.$refs.addRoleForm.validate((valid) => {
                    if (valid) {
                        _this.$http.get('api/role/addRole',{params: _this.addRoleForm} ).then(response => {
                            return response.json();
                        }, response => {
                            _this.$message.error('系统错误');
                        }).then(data => {
                            if(data.retCode == "000000"){
                                _this.addRoleModal = false;
                                _this.fetchRoleListList();
                            }else{
                                _this.$message.error('添加角色失败:' + data.msg);
                            }
                        });
                    }
                });
            }
        },
        watch: {
            '$route': 'fetchRoleListList,fetchResourceAll,fetchRoleGroupList'
        },
        created(){
            this.fetchRoleListList();
            this.fetchResourceAll();
            this.fetchRoleGroupList();
        }
    }
</script>
<style>
</style>

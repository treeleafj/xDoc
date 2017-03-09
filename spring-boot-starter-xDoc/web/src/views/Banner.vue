<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div v-loading="processing">
        <el-row>
            <el-col :span="24">
                <ul class="breadcrumb">
                    <li>首页横幅</li>
                </ul>
            </el-col>
            <el-col :span="24">
                <div class="smart-widget">
                    <div class="smart-widget-inner">
                        <div class="smart-widget-body">
                            <el-button type="primary" size="small" @click="handleAddSlide()">添加幻灯片</el-button>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>

        <el-table :data="list" border style="width: 100%;">
            <el-table-column label="序号" width="80">
                <template scope="scope">
                    {{scope.$index + 1}}
                </template>
            </el-table-column>
            <el-table-column prop="name" label="幻灯片名称" width="200"></el-table-column>
            <el-table-column prop="imageId" label="幻灯片图片" width="200">
                <template scope="scope">
                    <img v-bind:src="'/api/slide/file?id=' + scope.row.imageId" width="120"/>
                </template>
            </el-table-column>
            <el-table-column prop="url" label="幻灯片地址"></el-table-column>
            <el-table-column prop="remark" label="备注" width="300"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
                <template scope="scope">
                    <label v-if="scope.row.status == '1'" style="color:green">已启用</label>
                    <label v-if="scope.row.status == '2'" style="color:red">已禁用</label>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template scope="scope">
                    <el-button v-if="scope.row.status == '2'" size="small"
                               @click="handleStatus(scope.$index, scope.row)">
                        启用
                    </el-button>
                    <el-button v-if="scope.row.status == '1'" size="small" type="danger"
                               @click="handleStatus(scope.$index, scope.row)">
                        禁用
                    </el-button>

                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
                        修改
                    </el-button>
                    <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">
                        删除
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

        <el-dialog title="添加幻灯片" v-model="dialogFormVisible" size="tiny">
            <el-form :model="form" ref="saveForm">
                <el-form-item prop="name" label="标题" :label-width="formLabelWidth">
                    <el-input v-model="form.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="图片" :label-width="formLabelWidth">
                    <el-upload
                            action="/api/slide/upload"
                            type="drag"
                            :multiple="false"
                            name="img"
                            :thumbnail-mode="true"
                            :on-preview="handlePreview"
                            :on-remove="handleRemove"
                            :on-success="handleUploaded"
                            :default-file-list="imageId"
                    >
                        <i class="el-icon-upload"></i>
                        <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                    </el-upload>
                </el-form-item>
                <el-form-item prop="url" label="地址" :label-width="formLabelWidth">
                    <el-input v-model="form.url" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item prop="remark" label="备注" :label-width="formLabelWidth">
                    <el-input v-model="form.remark" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSave()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="修改幻灯片" v-model="dialogEditFormVisible" size="tiny">
            <el-form :model="form" ref="updateForm">
                <el-form-item prop="name" label="标题：" :label-width="formLabelWidth">
                    <el-input v-model="form.name" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="图片：" :label-width="formLabelWidth">
                    <el-upload
                            action="/api/slide/upload"
                            type="drag"
                            :multiple="false"
                            name="img"
                            :thumbnail-mode="true"
                            :on-preview="handlePreview"
                            :on-remove="handleRemove"
                            :on-success="handleUploaded"
                            :default-file-list="imageId"
                    >
                        <i class="el-icon-upload"></i>
                        <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                    </el-upload>
                </el-form-item>
                <el-form-item prop="url" label="地址：" :label-width="formLabelWidth">
                    <el-input v-model="form.url" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item prop="remark" label="备注：" :label-width="formLabelWidth">
                    <el-input v-model="form.remark" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogEditFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleUpdate()">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                list: [ ],
                imageId: [],
                pageNo: 1,
                pageSize: 10,
                total: 0,
                dialogTitle:"添加幻灯片",
                formLabelWidth: '80px',
                processing: false,
                dialogFormVisible: false,
                dialogEditFormVisible:false,
                form: {
                    id: null,
                    name:null,
                    remark: null,
                    img : null,
                    status:"1"
                }
            };
        },
        methods: {
            handleRemove(file, fileList) {
                console.log('开始删除',file, fileList);
            },
            handlePreview(file) {
                console.log('开始上传', file);
            },
            handleUploaded(resp, file, fileList) {
                console.log('上传成功', resp, file, fileList);
                if (resp.retCode == '000000') {
                    this.form.imageId = resp.id;
                } else {
                    this.$message.error('上传失败:' + resp.msg);
                }
            },

            handleStatus(rowIndex, data) {
                console.log(rowIndex, data);

                this.$confirm('是否确认' + (data.status == '1' ? '禁用' : '启用') + '该幻灯片?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    this.$http({
                        url: '/api/slide/status',
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

            handleAddSlide() {
                this.form = {};
                this.imageId = [];
                this.dialogFormVisible = true;//显示弹出框
            },

            handleEdit(rowIndex, data) {
                console.log(rowIndex, data);
                this.form.id = data.id,
                this.form.name = data.name,
                this.form.imageId = data.imageId;
                this.form.remark = data.remark;
                this.form.status = data.status;
                this.form.url = data.url;
                this.dialogEditFormVisible = true;//显示弹出框
            },

            handleDelete(id) {
                this.$confirm('此操作将永久删除该幻灯片, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.processing = true;
                    this.$http({
                        url: '/api/slide/remove',
                        method: 'post',
                        params: {
                            id : id
                        }
                    }).then((resp) => {
                        this.processing = false;
                        var data = JSON.parse(resp.data);
                        if (resp.ok && resp.data && data.retCode == '000000') {
                            console.log(data)
                            this.fetchData();
                            this.$message.info('删除成功');
                        } else {
                            this.$message.error('删除失败:' + data.msg);
                        }
                    }, (resp) => {
                        this.processing = false;
                        this.$message.error('系统错误');
                    });
                }).catch(() => {
                    console.log("取消");
                });
            },

            handleSave() {
                this.$http({
                    url: '/api/slide/save',
                    method: 'post',
                    params: this.form
                }).then((resp) => {
                    this.dialogFormVisible = false;//隐藏弹出框
                    this.processing = false;
                    var data = JSON.parse(resp.data);
                    if (resp.ok && resp.data && data.retCode == '000000') {
                        console.log(data)
                        this.fetchData();
                        this.$message.info('添加成功');
                    } else {
                        this.$message.error('添加失败:' + data.msg);
                    }
                }, (resp) => {
                    this.processing = false;
                    this.$message.error('系统错误');
                });
            },

            handleUpdate() {
                this.$http({
                    url: '/api/slide/update',
                    method: 'post',
                    params: this.form
                }).then((resp) => {
                    this.dialogEditFormVisible = false;//隐藏弹出框
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
                    url: '/api/slide/list',
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
            // this.fetchData();
        }
    }



</script>

<style scoped>
    .page {
        margin: 20px auto;
    }
    .el-input{
        width: 363px;
    }


</style>
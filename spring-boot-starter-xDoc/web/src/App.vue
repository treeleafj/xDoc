<template>
    <div id="app">

        <div>
            <el-menu id="header" mode="horizontal">
                <h2 class="logo-text">XDoc 接口文档</h2>
                <el-submenu index="1" class="head-user-box">
                    <template slot="title">版本</template>
                    <el-menu-item index="1-1">V1</el-menu-item>
                    <el-menu-item index="1-2">V1.1</el-menu-item>
                    <el-menu-item index="1-3">V1.2</el-menu-item>
                </el-submenu>
                <el-menu-item index="2" class="head-user-box">
                    <el-input placeholder="请输入搜索内容" icon="search" v-model="search"></el-input>
                </el-menu-item>
            </el-menu>
        </div>

		
        <div class="app-menu left-menu" id="left-menu">
            <el-tree ref="apiTree" highlight-current default-expand-all :data="reverseMenus" :props="defaultProps"
                     @current-change="checkChangeHandle" :filter-node-method="filterNode"></el-tree>
        </div>
        <div id="main" v-if="currentApiAction != null">
            <h2>{{currentApiAction.title}}</h2>
            <p class="comment">{{currentApiAction.comment}}</p>
            <p class="uri" v-if="currentApiAction.methods.length > 0">只支持:
                <span v-for="m in currentApiAction.methods">{{m}} </span>
            </p>
            <p class="uri" v-for="uri in currentApiAction.uris">接口地址: {{currentApiModule.uris[0] + '/' + uri}}</p>
            <p class="return">接口返回: {{currentApiAction.returnDesc}}</p>

            <div>
                <h2>入参</h2>
                <br/>
                <el-table :data="reverseParam" style="width: 60%">
                    <el-table-column prop="paramName" label="参数名" width="180"></el-table-column>
                    <el-table-column prop="paramDesc" label="描述" width="180"></el-table-column>
                    <el-table-column prop="require" label="是否必填">
                        <template scope="scope">{{scope.row.require ? '是' : '否'}}</template>
                    </el-table-column>
                </el-table>
            </div>

            <br/>
            <div>
                <h2>出参</h2>
                <br/>
                <el-table :data="reverseResponse" style="width: 60%">
                    <el-table-column prop="paramName" label="参数名" width="180"></el-table-column>
                    <el-table-column prop="paramDesc" label="描述" width="180"></el-table-column>
                    <el-table-column prop="type" label="类型"></el-table-column>
                    <el-table-column prop="require" label="是否必填">
                        <template scope="scope">{{scope.row.require ? '是' : '否'}}</template>
                    </el-table-column>
                </el-table>
            </div>

            <br/>
            <div v-if="currentApiAction.respbody != null">
                <h2>例子</h2>
                <br/>
                <pre>{{reverseRespbody}}</pre>
            </div>
        </div>

        <div id="test" v-if="currentApiAction != null">
            <h2>测试</h2>
            <br/>
            <el-form ref="form" :model="testForm" label-width="120px" class="test-form">
                <el-form-item label="请求方式">
                    <el-radio-group v-model="request.type">
                        <el-radio v-for="m in currentApiAction.methods" :label="m">{{m}}</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item v-for="param in reverseParam" :label="param.paramDesc">
                    <el-input v-model="testForm[param.paramName]"></el-input>
                </el-form-item>
                <div class="btn-div">
                    <el-button type="primary" @click="onTest">测试</el-button>
                </div>
            </el-form>
            
            <h3>返回内容</h3>
            <br/>
            <el-input ref="respbpdy" v-model="testRespbody" type="textarea" :rows="10" placeholder="返回内容..."></el-input>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'app',
        data() {
            return {
                search: '',
                radio2: '1',
                testRespbody: '',
                apiModules: [],
                testForm : {},
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                request : {
                    type : 'get'
                },
                currentApiModule: null,
                currentApiAction: null
            }
        },

        mounted() {
            this.$http.get('apis').then(response => {
                return response.json();
            }, response => {
                this.$message.error('系统错误');
            }).then(data => {
                this.apiModules = data;
                if (this.apiModules.length > 0 && this.apiModules[0].apiActions.length > 0) {
                    this.currentApiModule = this.apiModules[0];
                    this.currentApiAction = this.apiModules[0].apiActions[0];
                }
            });
        },

        computed: {
            reverseMenus() {
                console.log(this.apiModules[i])
                var menu = [];
                var id = 1;
                for (var i = 0; i < this.apiModules.length; i++) {
                    var apiModule = this.apiModules[i];

                    var children = [];

                    for (var j = 0; j < apiModule.apiActions.length; j++) {
                        var apiAction = apiModule.apiActions[j];
                        apiAction.id = id++;//赋值一个ID
                        children.push({
                            label: apiAction.title,
                            id: apiAction.id
                        });
                    }

                    var item = {
                        label: apiModule.comment,
                        children: children,
                        data: apiAction
                    };
                    menu.push(item);
                }

                return menu;
            },

            reverseParam() {
                if (this.currentApiAction) {
                    var data = [];

                    for (var i = 0; i < this.currentApiAction.param.length; i++) {
                        var par = this.currentApiAction.param[i];
                        data.push(par);
                    }
                    return data;
                }
            },

            reverseResponse() {

                if (this.currentApiAction && this.currentApiAction.respParam && this.currentApiAction.respParam.length > 0) {
                    var data = [];

                    for (var i = 0; i < this.currentApiAction.respParam.length; i++) {
                        var par = this.currentApiAction.respParam[i];
                        data.push({
                            paramName: par.paramName,
                            paramDesc: par.paramDesc,
                            type: par.type,
                            require: par.require
                        });
                    }
                    return data;
                }

                if (this.currentApiAction && this.currentApiAction.returnObj) {
                    var data = [];

                    for (var i = 0; i < this.currentApiAction.returnObj.fieldInfos.length; i++) {
                        var par = this.currentApiAction.returnObj.fieldInfos[i];
                        data.push({
                            paramName: par.name,
                            paramDesc: par.comment,
                            type: par.simpleTypeName
                        });
                        console.log(data, par);
                    }
                    return data;
                }
            },

            reverseRespbody() {
                if (this.currentApiAction && this.currentApiAction.respbody) {
                    return this.currentApiAction.respbody;
                }
            }
        },

        methods: {
            checkChangeHandle(data, checked, indeterminate) {
                var array = this.findModuleAndActionById(data.id);
                if (array) {
                    this.currentApiModule = array[0];
                    this.currentApiAction = array[1];
                }
            },

            findModuleAndActionById(id) {
                for (var i = 0; i < this.apiModules.length; i++) {
                    var apiModule = this.apiModules[i];

                    for (var j = 0; j < apiModule.apiActions.length; j++) {
                        if (apiModule.apiActions[j].id == id) {
                            return [apiModule, apiModule.apiActions[j]]
                        }
                    }
                }
            },

            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },

            onTest() {
                console.log(this.testForm);
                
                var uri = '';
                if (this.currentApiModule.uris.length > 0) {
                    uri += '/' + this.currentApiModule.uris[0];
                }

                if (this.currentApiAction.uris.length > 0) {
                    uri += '/' + this.currentApiAction.uris[0];
                }
                this.$http[this.request.type.toLocaleLowerCase()](uri, this.testForm).then(response => {
                    return response.text();
                }, response => {
                    this.$message.error('系统错误');
                }).then(response => {
                    this.testRespbody = response;
                });
            }
        },

        watch : {
            search(val) {
                this.$refs.apiTree.filter(val);
            },
        }
    }

</script>

<style>
    * {
        padding: 0;
        margin: 0;
    }

    .logo-text {
        float: left;
        color: rgba(102, 102, 102, 0.7);
        height: 60px;
        line-height: 60px;
        padding-left: 32px;
    }

    .left-menu {
        width: 400px;
        border-right: solid 2px #EAEDF1;
        display: block;
        position: fixed;
        bottom: 0px;
        z-index: 102;
        top: 60px;
        overflow-x: hidden;
    }

    #header {
        height: 60px;
        z-index: 200;
    }

    #header .head-user-box {
        float: right;
    }

    .el-tree {
        border: 0px solid #d1dbe5
    }

    #main {
        left: 403px;
        padding: 20px 20px;
        width: auto;
        position: absolute;
        top: 60px;
        bottom: 0px;
        right: 0px;
        overflow: hidden;
    }

    #main .comment, .uri, .return {
        padding: 20px;
    }

    #test {
        right: 0px;
        padding: 20px 20px;
        position: absolute;
        width : 20px;
        top: 60px;
        bottom: 0px;
        overflow: hidden;
        background-color:#FFF;
        border-left : 2px solid #eef1f6;
        transition:all .2s ease;
        -webkit-transition:all .2s ease;
        z-index : 1;
    }

    #test:hover {
        width : 800px;
    }

    .test-form {
        width : 80%;
    }

    .btn-div {
        width : 700px;
        text-align : center;
    }
</style>

<template>
    <div id="app">
        <div>
            <el-menu id="header" mode="horizontal">
                <h2 class="logo-text">{{title}}</h2>
                <el-submenu index="1" class="head-user-box">
                    <template slot="title">版本</template>
                    <el-menu-item index="1-1">V1</el-menu-item>
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
        <div id="main" v-if="currentApiAction != null" @click="hideTest">
            <h2>{{currentApiAction.title}}</h2>
            <p class="comment">{{currentApiAction.comment}}</p>
            <p class="uri" v-if="currentApiAction.methods.length > 0">只支持:
                <span v-for="m in currentApiAction.methods">{{m}} </span>
            </p>
            <p class="uri" v-for="uri in currentApiAction.uris">接口地址: {{(currentApiModule.uris[0] ? currentApiModule.uris[0] + '/' : '') + uri}}</p>
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

        <div id="test" :class="testClassName" v-if="currentApiAction != null && isOnline" @click="showTest">
            <h2>测试</h2>
            <br/>
            <div class="test-main">
                <el-form ref="testForm" :model="testForm" label-width="120px" class="test-form">

                    <el-form-item v-if="currentApiAction.methods.length > 0" label="请求方式">
                        <el-radio-group v-model="request.type">
                            <el-radio v-for="m in currentApiAction.methods" :label="m">{{m}}</el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item v-for="param in reverseParam" :label="param.paramName" :prop="param.paramName">
                        <el-input v-model="testForm[param.paramName]"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="onTest">测试</el-button>
                        <el-button @click="resetTestForm">重置</el-button>
                    </el-form-item>
                </el-form>
                
                <h3>返回内容</h3>
                <br/>
                <div class="test-respbody">
                    <el-input ref="respbpdy" v-model="testRespbody" type="textarea" :rows="10" placeholder="返回内容..."></el-input>
                </div>
            </div>
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
                isOnline : true,
                testClassName : 'test-off',
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                request : {
                    type : 'GET'
                },
                title : 'XDoc 接口文档',
                currentApiModule: null,
                currentApiAction: null
            }
        },

        mounted() {
            if (!window.apis) {
                this.$http.get('apis').then(response => {
                    return response.json();
                }, response => {
                    this.$message.error('系统错误,请求api列表 接口失败');
                }).then(data => {
                    if (typeof(data) == 'string') {//兼容后端返回数据出问题
                        data = JSON.parse(data);
                    }
                    this.fullData(data);
                });
            } else {
                this.isOnline = false;
                this.fullData(window.apis);
            }
        },

        computed: {
            reverseMenus() {
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
                    this.testForm = {};
                    for (var i = 0; i < this.currentApiAction.param.length; i++) {
                        var par = this.currentApiAction.param[i];
                        data.push(par);
                        this.$set(this.testForm, par.paramName, '');//动态绑定监控
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
                            type: par.simpleTypeName,
                            require : par.require
                        });
                    }
                    return data;
                }
            },

            reverseRespbody() {
                if (this.currentApiAction && this.currentApiAction.respbody) {
                    try {
                        var obj = JSON.parse(this.currentApiAction.respbody);
                        return JSON.stringify(obj, null, 4);
                    } catch(e) {

                    }
                    return this.currentApiAction.respbody;
                }
            }
        },

        methods: {

            fullData(data) {
                this.title = data.title;
                this.apiModules = data.apiModules;
                if (this.apiModules.length > 0 && this.apiModules[0].apiActions.length > 0) {
                    this.currentApiModule = this.apiModules[0];
                    this.currentApiAction = this.apiModules[0].apiActions[0];
                    if (this.currentApiAction.methods.length > 0) {
                        this.request.type = this.currentApiAction.methods[0];
                    }
                }
            },

            checkChangeHandle(data, checked, indeterminate) {
                var array = this.findModuleAndActionById(data.id);
                if (array) {
                    this.currentApiModule = array[0];
                    this.currentApiAction = array[1];
                    if (this.currentApiAction.methods.length > 0) {
                        this.request.type = this.currentApiAction.methods[0];
                    } else {
                        this.request.type = 'get';
                    }
                    this.resetTestForm();
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

            showTest() {
                this.testClassName = 'test-on';
            },

            hideTest() {
                 this.testClassName = 'test-off';
            },

            resetTestForm() {
                if (this.$refs.testForm) {
                    this.$refs.testForm.resetFields();
                }
            },

            onTest() {
                
                var uri = '';
                if (this.currentApiModule.uris.length > 0) {
                    uri += '/' + this.currentApiModule.uris[0];
                }

                if (this.currentApiAction.uris.length > 0) {
                    uri += '/' + this.currentApiAction.uris[0];
                }
                var requestType = this.request.type.toLocaleLowerCase();

                console.log('请求方式:', requestType, '请求数据:', this.testForm);

                var http;
                if ('get' == requestType) {
                    http = this.$http.get(uri, {params : this.testForm});
                } else {
                    http = this.$http[requestType](uri, this.testForm, {emulateJSON:true})
                }

                http.then((resp) => {
                    return resp.text();
                }, (resp) => {
                    return resp.text();
                }).then((resp) => {
                    this.testRespbody = resp;
                }, (resp) => {
                    console.log(resp);
                    window.resp = resp;
                    this.$message.error('系统错误:' + resp);
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
        overflow: auto;
    }

    #main .comment, .uri, .return {
        padding: 20px;
    }

    #test {
        right: 0px;
        padding: 20px 20px;
        position: absolute;
        top: 60px;
        bottom: 0px;
        overflow: auto;
        background-color:#FFF;
        border-left : 2px solid #eef1f6;
        transition:all .2s ease;
        -webkit-transition:all .2s ease;
        z-index : 1;
    }

    .test-off {
        width : 20px;
        cursor: pointer;
    }

    .test-on {
        width : 800px;
        cursor: auto;
    }

    .test-off .test-main {
        display : none;
    }

    .test-on .test-main {
        display : auto;
    }

    .test-form {
        width : 80%;
    }

    .test-respbody {
        padding-left : 50px;
        width : 700px;
    }
</style>

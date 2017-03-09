<template>
    <div id="app">
        <div class="app-menu left-menu" id="left-menu">
            <el-tree default-expand-all :data="reverseMenus" :props="defaultProps" @current-change="checkChangeHandle"></el-tree>
        </div>
        <div id="main" v-if="currentApiAction != null">
            <h2>{{currentApiAction.title}}</h2>
            <p class="comment">{{currentApiAction.comment}}</p>
            <p class="uri" v-for="uri in currentApiAction.uris">接口地址:{{uri}}</p>

            <h2>入参</h2>
            <template>
                <el-table :data="tableData" style="width: 60%">
                    <el-table-column prop="date" label="参数名" width="180"></el-table-column>
                    <el-table-column prop="name" label="描述" width="180"></el-table-column>
                    <el-table-column prop="address" label="是否必填"></el-table-column>
                </el-table>
            </template>

        </div>
    </div>
</template>

<script>
    export default {
        name: 'app',
        data() {
            return {
                apiModules : [{"apiActions":[{"comment":"查询当前登录用户的基本信息","docTags":{"list":[{"name":"@param","paramDesc":"当前登录用户","paramName":"user","require":false,"values":"user 当前登录用户"},{"name":"@return","values":"当前登录用户的基本信息"},{"name":"@see","values":{"comment":"用户","fieldInfos":[{"comment":"密码","name":"password","simpleTypeName":"String","type":"java.lang.String"},{"comment":"用户ID","name":"id","simpleTypeName":"String","type":"java.lang.String"},{"comment":"用户名","name":"username","simpleTypeName":"String","type":"java.lang.String"}],"type":"org.treeleafj.xdoc.test.vo.User"}}]},"json":true,"methods":[],"name":"info","param":[{"$ref":"$[0].apiActions[0].docTags.list[0]"}],"returnDesc":"当前登录用户的基本信息","returnObj":{"$ref":"$[0].apiActions[0].docTags.list[2].values"},"title":"查询当前登录用户的基本信息","uris":["info"]},{"comment":"用户注册","docTags":{"list":[{"name":"@param","paramDesc":"用户名","paramName":"username","require":true,"values":"username 用户名"},{"name":"@param","paramDesc":"密码","paramName":"password","require":true,"values":"password 密码"},{"name":"@return","values":"当前登录用户的基本信息"},{"name":"@title","values":"用户注册"},{"name":"@respbody","values":"{\"id\":\"123\",\"password\":\"123456\",\"username\":\"admin\"}"},{"name":"@see","values":{"comment":"用户","fieldInfos":[{"comment":"密码","name":"password","simpleTypeName":"String","type":"java.lang.String"},{"comment":"用户ID","name":"id","simpleTypeName":"String","type":"java.lang.String"},{"comment":"用户名","name":"username","simpleTypeName":"String","type":"java.lang.String"}],"type":"org.treeleafj.xdoc.test.vo.User"}}]},"json":true,"methods":["POST"],"name":"register","param":[{"$ref":"$[0].apiActions[1].docTags.list[0]"},{"$ref":"$[0].apiActions[1].docTags.list[1]"}],"respbody":"{\n\t\"id\":\"123\",\n\t\"password\":\"123456\",\n\t\"username\":\"admin\"\n}","returnDesc":"当前登录用户的基本信息","returnObj":{"$ref":"$[0].apiActions[1].docTags.list[5].values"},"title":"用户注册","uris":["register"]}],"comment":"用户模块","json":false,"methods":[],"uris":["user"]}],
                defaultProps :{
                    children: 'children',
                    label: 'label'
                },
                currentApiAction : null,
            }
        },

        mounted() {
            if (this.apiModules.length > 0 && this.apiModules[0].apiActions.length > 0) {
                this.currentApiAction = this.apiModules[0].apiActions[0];
            }
        },

        computed: {
            reverseMenus() {
                console.log(this.apiModules[i])
                var menu = [];
                for (var i = 0; i < this.apiModules.length; i++) {
                    var apiModule = this.apiModules[i];

                    var children = [];

                    for (var j = 0; j < apiModule.apiActions.length; j++) {
                        var apiAction = apiModule.apiActions[j];
                        console.log(apiAction);
                        children.push({
                            label : apiAction.title,
                            url : apiAction.uris[0]
                        });
                    }

                    var item = {
                        label : apiModule.comment,
                        children : children,
                        data : apiAction
                    };
                    menu.push(item);
                }

                return menu;
            }
        },

        methods : {
            checkChangeHandle(data, checked, indeterminate) {
                console.log(data);
                //this.currentApiAction
            }
        }
    }

</script>

<style>
    * {
        padding: 0;
        margin: 0;
    }

    .left-menu{
        width: 400px;
        border-right: solid 2px #EAEDF1;
        display: block;
        position: fixed;
        bottom: 0px;
        z-index: 102;
        top : 0;
        overflow-x: hidden;
    }

    .el-tree {
        border : 0px solid #d1dbe5
    }

    .el-menu {
        background-color : #3b8dbd;
    }

    .el-submenu__title {
        color : #FFF;
    }

    .menu-user-box > div:hover {
        background-color: #4898c7;
    }

    .el-submenu .el-menu {
        background-color : #FFF;
    }

    #main {
        left: 403px;
        padding : 20px 20px;
        width: auto;
        position: absolute;
        top: 0px;
        bottom: 0px;
        right: 0px;
        overflow: hidden;
    }

    #main .comment, .uri {
        padding : 20px;
    }
</style>

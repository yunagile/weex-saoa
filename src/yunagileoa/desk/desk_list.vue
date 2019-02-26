<template>
    <scroller class="wrapper">
        <div class="content">
            <div class="content_c">
                <div class="classify_title" >
                    <text class="title-font">常用服务</text>
                </div>
                <x-cell @cellClick="openView('dayReport')" class="tool" title="日报" desc="记录每天的工作成果和心得经验" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#1fb5f5">
                        <text class="iconfont cell-icon">&#xe602;</text>
                    </div>                 
                </x-cell>
                <x-cell @cellClick="openView('weekReport')" class="tool" title="周报" desc="总结每周的工作情况和下周计划" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#fea158">
                        <text class="iconfont cell-icon">&#xe655;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('cwa')" class="tool" title="考勤" desc="工作考勤" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#14cabb">
                        <text class="iconfont cell-icon">&#xe65e;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('day')" class="tool" title="日程" desc="建立和查看个人工作安排" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#f05e4d">
                        <text class="iconfont cell-icon">&#xe666;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('flow')" class="tool" title="流程审批" desc="各种流程的移动审批" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#14cabb">
                        <text class="iconfont cell-icon">&#xe65a;</text>
                    </div>                 
                </x-cell>
                <x-cell @cellClick="openView('notice')" class="tool" title="通知公告" desc="最新的动态和要求" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#F17679">
                        <text class="iconfont cell-icon">&#xe654;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('email')" class="tool" title="电子邮件" desc="" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#00a7ff">
                        <text class="iconfont cell-icon">&#xe6d9;</text>
                    </div>                 
                </x-cell>
                <x-cell @cellClick="openView('message')" class="tool" title="内部短信" desc="公司人员的快捷交流" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#01cfa1">
                        <text class="iconfont cell-icon">&#xe689;</text>
                    </div>                 
                </x-cell>    
            </div>
            <div class="content_c">
                <div class="classify_title"> 
                    <text class="title-font">其他服务</text>
                </div>
                <x-cell @cellClick="openView('news')" class="tool" title="新闻中心" desc="" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#fea158">
                        <text class="iconfont cell-icon" >&#xe652;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('vote')" class="tool" title="投票中心" desc="发起和参与投票" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#01cfa1">
                        <text class="iconfont cell-icon">&#xe67a;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('taskCenter')" class="tool" title="任务中心" desc="任务内容和任务完成情况的查看" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#1fb5f5">
                        <text class="iconfont cell-icon">&#xe659;</text>
                    </div>                 
                </x-cell>               
                <x-cell @cellClick="openView('doc')" class="tool" title="文档中心" desc="统一管理和共享工作文件和工作经验" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#00a7ff">
                        <text class="iconfont cell-icon">&#xe667;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('ht')" class="tool" title="合同" desc="记录合同的详细信息" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#F17679">
                        <text class="iconfont cell-icon" >&#xe65c;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('hy')" class="tool" title="会议" desc="查看会议安排" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#43a1ff">
                        <text class="iconfont cell-icon" >&#xe657;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('kh')" class="tool" title="客户关系" desc="客户信息管理" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#f05e4d">
                        <text class="iconfont cell-icon" >&#xe88f;</text>
                    </div>                 
                </x-cell>
                <x-cell @click="openView('yunagile')" class="tool" title="软捷平台" desc="" bottomBorder="true" :hasArrow="true">
                    <div slot="label" class="tool_icon" style="background-color:#1fb5f5">
                        <text class="iconfont cell-icon" >&#xe689;</text>
                    </div>                 
                </x-cell>    
            </div>            
        </div>       
    </scroller>
</template>

<script>
import asCore from "../../common/js/core";
const navigator = weex.requireModule("navigator");
export default {
    props: {
      data: {
        type: Object,
        default: {}
      }
    },
    components:{
        "x-cell" : require("../../common/component/cell.vue"),
    },
    methods:{
        openView(event){ 
            var appData = this.data;
            if(appData && appData[event]){
                var ldata = appData[event];
                if(ldata.isok){
                    if(ldata.type=="jsnative"){
                        /* 打开js地址 */
                        navigator.push(ldata.link);
                    }else if(ldata.type=="web"){
                        /* 用内部的webview打开 */
                        navigator.pushParam(asCore.localpath + "common/component/webview.js",{
                            title:ldata.appname,
                            url:ldata.link
                        });
                    }
                }else{
                    asCore.toast("当前应用未启用");
                }
            }else{
                asCore.toast("当前应用未启用");
            }
        }
    }
}
</script>

<style>
    .iconfont{
        font-family: iconfont;
    }

    .classify_title{
        background-color: #ffffff;
        height:80px;
        border-bottom-width: 1px;
        border-bottom-color: #e2e2e2;
        justify-content: center;
        padding-left:30px;
        opacity: 1;
        position: sticky;
    }
    .is-op {
        opacity: 0;
    }

    .title-font {
        color:#666666;
        font-size:30px;
    }

    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }
    .tool {
        padding-top:15px;
        padding-bottom:15px;

    }
    .tool_icon{
        height: 95px;
        width: 95px;
        border-radius: 10px;
        margin-right:20px;
        align-items: center;
        justify-content: center;
    }

    .cell-icon{
        font-size:55px;
        color:#fff;
    }


    
</style>

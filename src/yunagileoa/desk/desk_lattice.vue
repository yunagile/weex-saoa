<template>
    <list class="wrapper">
        <refresh class="refresh" @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
        </refresh>
        <cell class="content">           
            <div class="classify_title2">
                <text class="title-font">常用服务</text>
            </div>
            <div class="tool_div">
                <div class="tool tool_border_right" @click="openView('dayReport')">
                    <div class="tool_icon" style="background-color:#1fb5f5">
                        <text class="iconfont tool_icon_t">&#xe602;</text>
                    </div>
                    <text class="tool_title">日报</text>
                </div>
                
                <div class="tool tool_border_right" @click="openView('weekReport')">
                    <div class="tool_icon" style="background-color:#fea158">
                        <text class="iconfont tool_icon_t">&#xe655;</text>
                    </div>
                    <text class="tool_title">周报</text>
                    
                </div>

                 <div class="tool tool_border_right" @click="openView('cwa')">
                    <div class="tool_icon" style="background-color:#01cfa1">
                        <text class="iconfont tool_icon_t">&#xe65e;</text>
                    </div>
                    <text class="tool_title">考勤</text>
                    
                </div>

                <div class="tool" @click="openView('day')">
                    <div class="tool_icon" style="background-color:#f05e4d">
                        <text class="iconfont tool_icon_t">&#xe666;</text>
                    </div>
                    <text class="tool_title">日程</text>
                    
                </div>
                
            </div>
            <div class="tool_div">
                <div class="tool tool_border_right" @click="openView('flow')">
                    <div class="tool_icon" style="background-color:#14cabb">
                        <text class="iconfont tool_icon_t">&#xe65a;</text>
                    </div>
                    <text class="tool_title">流程审批</text>
                    
                </div>
                
                <div class="tool tool_border_right" @click="openView('notice')">
                    <div class="tool_icon" style="background-color:#F17679">
                        <text class="iconfont tool_icon_t">&#xe654;</text>
                    </div>
                    <text class="tool_title">通知公告</text>
                    
                </div>

                 <div class="tool tool_border_right" @click="openView('email')">
                    <div class="tool_icon" style="background-color:#00a7ff">
                        <text class="iconfont tool_icon_t">&#xe6d9;</text>
                    </div>
                    <text class="tool_title">电子邮件</text>
  
                </div>
                <div class="tool" @click="openView('message')">
                    <div class="tool_icon" style="background-color:#01cfa1">
                        <text class="iconfont tool_icon_t">&#xe689;</text>
                    </div>
                    <text class="tool_title">内部短信</text>
                </div>          
            </div>
            <div class="classify_title2">
                <text class="title-font">其他服务</text>
            </div>
            <div class="tool_div">
                <div class="tool tool_border_right" @click="openView('news')" >
                    <div class="tool_icon" style="background-color:#fea158">
                        <text class="iconfont tool_icon_t">&#xe652;</text>
                    </div>
                    <text class="tool_title">新闻中心</text>
                </div>
                <div class="tool tool_border_right" @click="openView('vote')">
                    <div class="tool_icon" style="background-color:#01cfa1">
                        <text class="iconfont tool_icon_t">&#xe67a;</text>
                    </div>
                    <text class="tool_title">投票中心</text>
                </div>
                 <div class="tool tool_border_right" @click="openView('taskCenter')">
                    <div class="tool_icon" style="background-color:#1fb5f5">
                        <text class="iconfont tool_icon_t">&#xe659;</text>
                    </div>
                    <text class="tool_title">任务中心</text>
                </div>
                <div class="tool" @click="openView('doc')">
                    <div class="tool_icon" style="background-color:#00a7ff;">
                        <text class="iconfont tool_icon_t">&#xe667;</text>
                    </div>
                    <text class="tool_title">文档中心</text>
                </div>
            </div>
            <div class="tool_div">
                <div class="tool tool_border_right" @click="openView('ht')">
                    <div class="tool_icon" style="background-color:#F17679">
                        <text class="iconfont tool_icon_t">&#xe65c;</text>
                    </div>
                    <text class="tool_title">合同</text>
                </div>
                <div class="tool tool_border_right" @click="openView('hy')">
                    <div class="tool_icon" style="background-color:#43a1ff">
                        <text class="iconfont tool_icon_t">&#xe657;</text>
                    </div>
                    <text class="tool_title">会议</text>
                </div>
                 <div class="tool tool_border_right" @click="openView('kh')">
                    <div class="tool_icon" style="background-color:#f05e4d">
                        <text class="iconfont tool_icon_t">&#xe88f;</text>
                    </div>
                    <text class="tool_title">客户关系</text>
                    
                </div>
                <div class="tool" @click="openView('yunagile')">
                    <div class="tool_icon" style="background-color:#1fb5f5">
                        <text class="iconfont tool_icon_t">&#xe653;</text>
                    </div>
                    <text class="tool_title">软捷平台</text>
                    
                </div>
                
            </div>
        </cell>
    </list>
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
    data(){
        return {
            refreshing:false
        }
    },
    methods:{
        onrefresh(){
            var s = this;
            s.refreshing = true
            setTimeout(() => {
                s.refreshing = false;
            }, 1000);
        },
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
            // if(event == 'dayReport'){
            //     navigator.push(asCore.localpath+"yunagileoa/app/dayReport/dayList.js");
            // }else if(event == 'weekReport'){
            //     navigator.push(asCore.localpath+"yunagileoa/app/weekReport/weekReportList.js");
            // }else if(event == 'notice'){
            //     navigator.push(asCore.localpath+"yunagileoa/app/notice/noticeActivity.js");
            // }else if(event == 'doc'){
            //     navigator.pushParam(asCore.localpath + "common/component/webview.js",{
            //         title:"文档中心",
            //         url:"http://demo.kodcloud.com/"
            //     });
            // }else if(event == 'news') {
            //     navigator.push(asCore.localpath+"yunagileoa/app/news/news.js");
            // }else if(event == 'message') {
            //     navigator.push(asCore.localpath+"yunagileoa/app/message/message.js");
            // }
        }
    }
}
</script>
<style scoped>
    .refresh {
        width: 750;
        display: -ms-flex;
        display: -webkit-flex;
        display: flex;
        -ms-flex-align: center;
        -webkit-align-items: center;
        -webkit-box-align: center;
        align-items: center;
    }
    .indicator {
        margin-top: 30px;
        margin-bottom: 20px;
        height: 70px;
        width: 70px;
        color: #ce2020;
    }
    .iconfont{
        font-family: iconfont;
    }

    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }

    .classify_title2{
        background-color: #ffffff;
        height:80px;
        border-bottom-width: 1px;
        border-bottom-color: #e2e2e2;
        justify-content: center;
        padding-left:30px;
        margin-top:40px;

    }
    .title-font {
        color:#666666;
        font-size:30px;
    }
    .tool_div{
        width:750px;
        flex-direction: row;
        justify-content: flex-start;
        background-color:#fff;
    }

    .tool{
        width:187.5px;
        height:160px;           
        padding-top:25px;
        align-items: center;
        border-bottom-width: 1px;
        border-bottom-color: #e2e2e2;
    }

    .tool:active{
         background-color: #eee;
    }

    .tool_border_right{
        border-right-width:1px;
        border-right-color:#e2e2e2;
    }


    .tool_icon{
        width:70px;
        height:70px;
        border-radius: 15px;
        background-color:blanchedalmond;
        padding-top:11px;
        margin-bottom:10px;

        
    }

    .tool_icon_t{
        text-align: center;
        color:#fff;
        font-size: 39px;
        line-height:46px;
        font-weight: 700px;

    }

    .tool_title{
        font-size:27px;
        color:#666666;
        font-weight:600px;

    }

</style>

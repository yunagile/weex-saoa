<template>
    <div class="wrapper" style="background-color:#f5f5f5;">
        <minbar title="通知详细" :isPadding="true"> 
             <text v-if="self"  slot="right" class="iconfont title-icon" @click="openYd(noticeId)">&#xe663;</text>
        </minbar>  
        <list>
            <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            <cell>
                <div class="title_content">
                    <div style="border-bottom-width:1px;border-bottom-color:#ddd;padding-bottom:10px ">
                        <div><text class="title_text">{{title}}</text></div>
                        <div><text class="title_bottom_text">{{createPsnName}} 发布于 {{createTime}}</text></div>
                    </div> 
                    <div style="margin-top:20px;" class="content_d">
                        <vhtml :html="content"  class="content_c" @pageinit="htmlPageInit"></vhtml>
                    </div>
                </div> 
            </cell>  
        </list>
        <x-load :show="loadinging2" :type="'default'" :interval="0" ></x-load>
    </div>  
</template>
<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core"; 
const navigator = weex.requireModule("navigator");
const animation = weex.requireModule('animation');
const paramBroad = new BroadcastChannel('notice');
export default {
    components:{
        "minbar":require("../../../common/component/minbar.vue"), 
        "x-load":require("../../../common/component/loading.vue") 
    },
    data(){
        return {
             noticeId:"",
             statusCode:"",
             title:"",
             index:0,
             createTime:"",
             createPsnName:"",
             content:"",
             self:false,
             createpsnid:"",
             htmlheight:0,
             refreshing:false,
             loadinging2:false,
             param:{
                 id:function(){
                     return "";
                 }
             }
        }
    },
    
     created(){ 
        var s = this; 
        var globalEvent = weex.requireModule('globalEvent') ;
        globalEvent.addEventListener("onPageInit", function (e) {
            var param = navigator.param();
            s.noticeId = param['noticeId'];  
            s.statusCode = param['statusCode'];  
            s.index = param['index']; 
            s.loadinging2 = true; 
            s.getData();    
        }); 
    },
    methods:{
        onrefresh(){
            var s = this;
            /* 显示默认的刷新 */
            s.refreshing = true;
            s.getData(() => {
                s.refreshing = false;
            });
        } ,
         htmlPageInit(data) {
             var count = data['count'];
             if(count>0){
                 var height = count * 50;
                 this.htmlheight = data['height'];
                 //asCore.alert(data);
             }
         },
         getData(callback){
            var s=this;
            s.param.id=s.noticeId;
            asCore.getContext(context =>{
                asCore.post('/notice/noticeBarQueryByIdAction',s.param,(data,flag)=>{
                    if(data['status'] == "SUCCESS"){
                        var pData = JSON.parse(data["data"]["data"]);
                        var rows = pData.rows;   
                        if(rows.length>0){ 
                            s.title=rows[0].title;   
                            var createTime=rows[0].createtime;
                            s.createTime = Utils.formatDate(new Date(createTime),'yyyy-MM-dd hh:mm:ss');
                            s.createPsnName=rows[0].createpsnname;
                            s.content=rows[0].content; 
                            s.createpsnid=rows[0].createpsnid; 
                            if(s.createpsnid==context.currentPersonID){ 
                                s.self=true;
                            }  
                            if(s.statusCode=="0"){
						       s.updateReadStatus();
					        }
                        }else{
                            asCore.toast('获取通知公告失败！');
                        } 
                    }else{
                        asCore.toast('获取通知公告失败！');
                    } 
                    s.loadinging2 = false; 
                    if(callback) callback();
                });
            }) 
         },
         updateReadStatus(){ 
            var s = this;
            asCore.post("/notice/updateNoticeStatExamineAction",{id:this.noticeId},function(data,flag,msg){
                /* 执行回调 */
                paramBroad.postMessage({index:s.index});
		    }); 
         },
         openYd(noticeId){
            if(noticeId){ 
                navigator.pushParam(asCore.localpath + "yunagileoa/app/notice/noticeHaveRead.js",{
                    noticeId:noticeId 
                });
            }
         }
    } 
}
</script>
<style scoped>       
.title_content{
    padding:30px;
    background-color:white
}
.title_text{
    font-size: 35px;
    line-height: 40px;

}
.title_bottom_text{
    line-height: 40px;
    color:#999999;
	font-size:26px;
}
.iconfont {
  font-family: iconfont;
}
.title-icon {
    font-size:35px;
    color:#fff;
    font-weight: 300;
}
.content_d {
    height: 1500px;
}
.content_c {
   height: 1500px;
}
    /*刷新加载更多类的样式*/
    .refresh {
        width: 750;
        display: flex;
        align-items: center;
    }
    .indicator {
        margin-top: 30px;
        margin-bottom: 20px;
        height: 70px;
        width: 70px;
        color: #ce2020;
    }
</style>
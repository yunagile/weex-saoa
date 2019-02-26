    <template>
            <div class="wrapper" style="background-color:#f5f5f5">
                <minbar title="已阅的人" :isPadding="true"></minbar>   
                <list> 
                    <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                        <loading-indicator class="indicator"></loading-indicator>
                    </refresh>
                    <cell  v-for="(c,index) in datas" :key="index">      
                        <div class="content_c">
                            <div class="user_div">
                                <image class="head" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+c.createPsnCode"/>
                                <div>
                                    <div style=" flex-direction: row;justify-content:flex-start;width:600px  ">
                                        <text  class="content_text">{{c.name}}</text>
                                        <text class="content_right_text">{{c.status}}</text>
                                    </div>
                                    <text  class="content_bottom_text">{{c.examineStateTime}}</text> 
                                </div>
                            </div>
                        </div>
                    </cell>    
                </list>  
                <x-load :show="loadinging2" :type="'default'" :interval="0" ></x-load>
            </div>  
        </template>
    <script>
    import minbar from "../../../common/component/minbar.vue" 
    import asCore from "../../../common/js/core";   
    import Utils from "../../../common/js/utils";
    import load from "../../../common/component/loading.vue";
    const navigator = weex.requireModule("navigator");
    export default {
    data(){
        return {
            noticeId:"",
            bsessionid:"",
            refreshing:false,
            loadinging2:false,
            param:{
                noticeId:""
            },
            rootPath:asCore.rootPath,
            datas:[]
        }
    },
    components:{
        "minbar":minbar,  
        "x-load": load
    },
    created(){ 
        var s = this; 
        var globalEvent = weex.requireModule('globalEvent') ;
        globalEvent.addEventListener("onPageInit", function (e) {
            var param = navigator.param(); 
            s.noticeId = param['noticeId'];  
            s.getPath();     
        }); 
    },
    methods:{  
         getPath() { 
            var s = this; 
            asCore.getBsessionid(bid => {
                if (bid) {
                    s.bsessionid = bid;
                    s.loadinging2 = true;
                    s.getData();
                }
            });
          },
          getData(callback){
              var s=this;
              s.param.noticeId= s.noticeId;
              asCore.post('/notice/noticeStatQueryAction',s.param,(data,flag)=>{
                    if(data['status'] == "SUCCESS"){ 
                        var pData = JSON.parse(data["data"]["data"]);
                        var rows = pData.rows;   
                        var arrs=[] 
                        if(rows.length>0){ 
                            for(var i=0;i<rows.length;i++){ 
                                var id=rows[i].id;  
                                var name=rows[i].receivePersonName;
                                var status=rows[i].examineStateInfo;   
                                var createPsnCode=rows[i].receivePersonCode;  
                                var examineStateTime=rows[i].examineStateTime; 
                                examineStateTime = Utils.formatDate(new Date(examineStateTime),'yyyy-MM-dd')
                                arrs.push({"id":id,"name":name,"status":status,"createPsnCode":createPsnCode,"examineStateTime":examineStateTime});  
                            } 
                            s.datas = arrs;
                        }else{
                            s.datas = [];
                        }
                    }else{
                        asCore.toast('获取已阅人员失败！');
                    } 
                s.loadinging2 = false;
                if(callback) callback();
            });
          },
          /* 数据刷新时做的事情 */
        onrefresh(e) {
            var s = this;
            /* 显示默认的刷新 */
            s.refreshing = true;
            s.getData(() => {
                s.refreshing = false;
            });
        }
          
        } 
    }
    </script>
    <style scoped>      
     .content_c{ 
        padding:20px; 
        background-color: white;  
        border-bottom-width: 1px;
        border-bottom-color: #e2e2e2;
    }  
    .content_c:active{
        background-color:#e2e2e2;
    }  
    .user_div{
        width:710px;
        height:80px;
        flex-direction: row;
        justify-content:flex-start;  
    }
    .head{
        width:80px;
        height:80px;
        border-radius: 40px;
        margin-right:15px;  
        float: right;
    } 
    .content_text{
        line-height: 40px;
        font-size:26px;
    } 
    .content_right_text{
        line-height: 40px;
        font-size:26px;
        color: red; 
        position: absolute;
        right: 10px;;
    }
    .content_bottom_text{
        line-height: 40px;
        color:#999999;
	    font-size:22px;
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
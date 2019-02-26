    <template>
            <div class="wrapper" style="background-color:#edeeee">
                <minbar title="待我点评的周报" :isPadding="true"> 
                </minbar>  
                    <div class="btn_bar" >
                    <div @click="onclick(0)" class="btn">
                        <text  :class="['btn_text',isClick?'btn_text2':'' ]">未点评</text>
                    </div>
                    <div class="btn" @click="onclick(1)">
                        <text :class="['btn_text',isClick2?'btn_text2':'' ]" >已点评</text>
                    </div>
                </div>
                <div style="width:750px;height:5px;position:relative">
                    <div style="width:750px;height:1px;background-color:#e2e2e2;z-index:1;position:absolute;top:0"></div>
                    <div ref="line" :style="{'width':lineWidth+'px','margin-left':lineLeft+'px','background-color':theme}" style="height: 5;z-index:2;position:absolute;top:0"></div>
                </div>
                <text v-if="noRows" class="no_text">没有任何记录！</text>
                <list  loadmoreoffset="1" @loadmore="onloading"> 
                    <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                        <loading-indicator class="indicator"></loading-indicator>
                    </refresh>
                    <cell  v-for="(c,index) in weekReports" :key="index">      
                        <div class="content_c" @click="openDetail(c.id)">
                            <div class="user_div">
                                <image  class="head" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+c.createPsnCode"/>
                                <div>
                                    <text  class="content_text">{{c.content}}</text>
                                    <text  class="content_bottom_text">提交于 {{c.createTime}}</text> 
                                </div>
                            </div>
                        </div>
                    </cell>    
                    <div class="loading" v-if="loadinging">
                            <text class="indicator-text">{{psText}}</text>
                    </div>    
                </list>  
                <wx-popup :show="rightMenu" pos="right"  width="600"   @wxcPopupOverlayClicked="closeRightMenu">
                     <div slot="popup_content" >
                         <div style="border-bottom-width:1px;border-bottom-color:#ddd; "><text class="rightMenu_title">快速过滤</text></div>
                         <div style="padding:10px">
                            <text style="color:#666666;font-size:26px">通知类型</text>
                            <div style="flex-direction: row;justify-content:space-between;padding-right:20px;margin-top:10px">
                                 <button  class="x-fl-button">全部</button>     
                            </div>
                             
                         </div>
                     </div> 
                </wx-popup>    
                <wx-load :show="loadinging2" :type="'default'" :loading-text="'加载中'"  :interval="0" ></wx-load>
            </div>  
        </template>
    <script>
    import minbar from "../../../common/component/minbar.vue"
    import Utils from "../../../common/js/utils";
    import asCore from "../../../common/js/core";
    import popup from "../../../common/component/popup.vue"  
    const animation = weex.requireModule('animation');
    const storage = weex.requireModule('storage'); 
    const navigator = weex.requireModule("navigator");
    export default {
    data(){
        return {
            bsessionid:"",
            rootPath:"",
            isClick:true,
            isClick2:false,
            lineWidth:375,
            lineLeft:0,
            fromindex:0,
            theme:'#2e3238',
            loadinging:false,
            loadinging2:false,
            status:"false",
            weekReports:[], 
            psText:'正在加载更多...',  
            param:{
                "rows":10,
                "personId":'', 
            },
            firstL:true,
            refreshing:false,
            noRows:false, 
            all:false,
            rightMenu:false
        }
    },
    components:{
        "minbar":minbar, 
        "wx-popup": popup,
    },
    created () {  
        var s = this; 
        Utils.getThemeColor((color,theme) => {
            if(theme=="theme1"){
                s.theme = "#F15A49";
            }else{
                s.theme = color;
            }
        }); 
        this.getPath();
        this.onrefresh('loadinging2');   
    },
    methods:{  
         getPath() { 
            var self = this; 
            asCore.getBsessionid(bid => {
                if (bid) {
                    self.bsessionid = bid;
                } else {
                    
                }
            });
            self.rootPath = asCore.rootPath; 
        },
         /*数据刷新和加载*/
        onloading () {
            var self = this;
            self.loadinging = true;
            self.firstL = false;
            this.getData(() =>{
                self.loadinging = false;
                self.refreshing = false; 
            });
        }, 
        onclick(event){
            this.weekReports=[]
            this.noRows=false, 
            this.animateLine(event);
            this.getData(() =>{
                self.loadinging = false;
                self.refreshing = false; 
            });
        }, 
        onchange(event){
            this.page=event.index;
            this.animateLine(event.index); 
        },
        animateLine(toindex){
            if (this.fromindex == toindex) {
                return;
            } 
            if(toindex == 0){
                this.status ="false"; 
                this.isClick = true;
                this.isClick2 = false;
            }else{
                this.status ="true"; 
                this.isClick = false;
                this.isClick2 = true;
            } 
            var c = this.$refs.line; 
            var to = 375;
            if(toindex == 0){
                to = 0;
            } 
            animation.transition(c, {
                styles: {
                    transform: 'translate('+to+'px,0px)',
                },
                duration: 300, //ms
                timingFunction: 'ease',
                delay: 0 //ms
                }, function() {
                
                })
                this.fromindex = toindex; 
            },
            onrefresh(e){
                var self = this;
                self.noRows = false;
                self.firstL = true;
                self.param.rows = 10;
                self.refreshing = true;
                self.all = false;
                if(e == 'loadinging2'){ 
                    self.loadinging2 = true;
                } 
                this.getData(()=>{ 
                    self.loadinging2 = false;
                    self.refreshing = false; 
                }); 
            }, 
            openDetail(id){
                if(id){ 
                    navigator.pushParam(asCore.localpath + "yunagileoa/app/weekReport/weekReview.js",{
                        id:id 
                    });
                }
            },
            pushData(arrs,nId){ 
                var _this=this; 
                asCore.post("/oa/queryIsCommentedAction", {
                    weekReportId :nId
                    }, function(data, flag, msg) { 
                        if (data.data.re == _this.status) {   
                             _this.weekReports=arrs
                        } 
                    }
                ); 
            },
            getData(callback){     
                var _this=this; 
                if(!this.firstL){
                    this.param.rows = this.param.rows+10;
                }else{
                    this.weekReports=[]
                }     
                asCore.getContext(context =>{
                    this.param.personId= context.currentPersonCode;   
                    asCore.post('/oa/mainSendQueryAction',this.param,(data,flag)=>{
                        if(data['status'] == "SUCCESS"){
                            var pData = JSON.parse(data["data"]["data"]); 
                            var rows = pData.rows;  
                            if(rows.length>0){  
                                var arrs=[]; 
                                for(var i=0;i<rows.length;i++){ 
                                    var id=rows[i].id; 
                                    var year=rows[i].year;
                                    var month=rows[i].month;
                                    var week=rows[i].week; 
                                    var createPsn=rows[i].createPsn;
                                    var createPsnCode=rows[i].createPsnId;
                                    var createTime=rows[i].createTime;
                                    createTime = Utils.formatDate(new Date(createTime),'yyyy-MM-dd hh:mm:ss')
                                    arrs.push({"id":id,"year":year,"content":createPsn+year+"年"+month+"月第"+week+"周工作周报","createTime":createTime,"createPsnCode":createPsnCode});                                       
                                    this.pushData(arrs,id)
                                }    
                                if(!this.all){
                                    if(rows.length%10 != 0||rows.length == this.rows){
                                        this.all = true;
                                        asCore.toast('已经加载全部数据');
                                    }   
                                }
                            }else{
                                this.weekReports=[]
                                this.noRows = true; 
                            }   
                        }else{
                            asCore.toast('获取待点评周报失败！');
                        }
                        if(callback) callback();
                    });
                }); 
            }
        } 
    }
    </script>
    <style scoped lang="scss">      
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
    .indicator-text {
        color: #888888;
        font-size: 30px;
        text-align: center;
    }
    .no_text{
        font-size: 35px;
        text-align: center;
        line-height: 300px;
        color:#888 
    }
    .btn_bar{
        flex-direction: row;
        align-items: center;
        justify-content:center;
        width:750px;
        height:95px;
        z-index: 9999;
    }

    .btn{
        flex: 1;
        height:95px;
        justify-content:center;
        background-color: #FFFFFF;
    }

    .btn:active{
        background-color: #e1e1e1;
    }

    .btn_text{
        font-size:34px;
        color:#888;
        font-weight: 500;
        line-height: 40px;
        text-align: center;
    }

    .btn_text2{
        font-size:34px;
        color:#2e3238;
        font-weight: 500;
        line-height: 40px;
        text-align: center;
    } 
    .content_c{ 
        padding:10px 20px; 
        background-color: white;  
        margin-bottom: 8px;
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
        font-size:30px;
    } 
    .content_bottom_text{
        line-height: 40px;
        color:#999999;
	    font-size:22px;
    }
    .iconfont{
        font-family: iconfont;
    } 
    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    } 
    .rightMenu_title{
        color: #666666;
        line-height: 100px; 
        width:600px;
        text-align: center;
        font-size: 26px;
    }
    .x-fl-button {
        border:none;
        background-color: #f4f4f4;
        border-radius:5px;
        margin-right:5px;
        width:300px;
        height:70px;
        font-size:12px;
        color:#333333;
        text-align: center;
        justify-content:center;
        line-height: 70px;
    }
    </style>
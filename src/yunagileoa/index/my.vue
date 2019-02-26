<template>
    <div class="wrapper">
        <minibar title="我的" :theme="bartheme" :leftButton="false" :isPadding="true" :useDefaultReturn="false" >
        </minibar>
        <div class="panel" @click="openPage('personinfo')">
                <div class="panel-content">
                    <image class="content-img" :src="headData.img"></image>
                    <div class="content-div">
                        <text :class="['content-title',isIos()?'ios-top':'']">{{headData.name}}</text>
                        <text class="content-text">{{headData.label}}</text>
                    </div>
                    <div class="panel-right">
                        <text class="iconfont panel-right-icon" >&#xe632;</text>
                        <text class="iconfont panel-right-icon2" >&#xe619;</text>
                    </div>
                </div>
        </div>
        <list class="scroller" >
            <refresh class="refresh" @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            
            <cell>
                <x-cell title="关注的事项" :topBorder="true" :hasArrow="true" class="list-margin">
                    <div slot="label" class="list-icon" style="background-color:#FF5660;">
                        <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe73d;</text>
                    </div>
                </x-cell>
                <x-cell title="未完成事项" :hasArrow="true">
                    <div slot="label" class="list-icon" style="background-color:#31B3F5;">
                        <text class="iconfont cell-icon" :style="{fontSize:50}">&#xe68b;</text>
                    </div>
                </x-cell>
                <x-cell title="消息提醒设置" :bottomBorder="true" :hasArrow="true" @cellClick="openPage('messageSeting')">
                    <div slot="label" class="list-icon" style="background-color:#4D90ED;">
                        <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe631;</text>
                    </div>
                </x-cell>

                <x-cell title="帮助中心" :topBorder="true" :hasArrow="true" class="list-margin" @cellClick="openPage('helpCenter')">
                    <div slot="label" class="list-icon" style="background-color:#46CA88;">
                        <text class="iconfont cell-icon" :style="{fontSize:43}">&#xe60d;</text>
                    </div>
                </x-cell>
                <x-cell title="分享给朋友" :bottomBorder="true" :hasArrow="true" @cellClick="openPage('share')">
                    <div slot="label" class="list-icon" style="background-color:#FFA317;">
                        <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe634;</text>
                    </div>
                </x-cell>

                <x-cell title="设置" @cellClick="openPage('setting')" 
                    :topBorder="true" :hasArrow="true" :bottomBorder="true" class="list-margin">
                    <div slot="label" class="list-icon" style="background-color:#777D85;">
                        <text class="iconfont cell-icon" :style="{fontSize:38}">&#xe67c;</text>
                    </div>
                </x-cell>
            </cell>
        </list>
        <!-- 分享组件 -->
        <share v-model="showShare" :items="shareItems" @btnClick="shareBtnClick">
        </share>
    </div>
</template>
<script>
  import asCore from "../../common/js/core";
  const navigator = weex.requireModule('navigator');
  const modal = weex.requireModule('modal');
  const updateImg = new BroadcastChannel("updateImg");
  export default {
    components: { 
        "minibar" :require("../../common/component/minbar.vue"),
        "x-cell" : require("../../common/component/cell.vue"),
        "share" : require("../../common/component/share.vue")
    },
    data (){
        return {
            showShare:false,
            shareItems:[
                {
                   type:"image",
                    icon:"",
                    text:"分享至QQ",
                    style:{
                        width:"75",
                        height:"75"
                    },
                    imgurl:asCore.localpath + "img/qqicon.png"
                },
                {
                    type:"image",
                    icon:"",
                    text:"分享至微信",
                    style:{
                        width:"78",
                        height:"78"
                    },
                    imgurl:asCore.localpath + "img/weixin.png"
                },
                {
                    type:"image",
                    icon:"",
                    text:"分享至空间",
                    style:{
                        width:"73",
                        height:"73"
                    },
                    imgurl:asCore.localpath + "img/qqkj.png"
                },
                {
                    type:"image",
                    icon:"",
                    text:"分享至微博",
                    style:{
                        width:"75",
                        height:"75"
                    },
                    imgurl:asCore.localpath + "img/weibo.png"
                }
            ],
            refreshing:false,
            headData :{
                name:"未登录",
                label:"未登录",
                img :"https://m.yunagile.com:442/examples/oa/img/duser.png"
            }
        }
    },
    props: {
      bartheme:{
        type: String,
        default: ""
      }
    },
    created() {
        updateImg.onmessage = this.updateImg;
    },
    methods: {
        /* 判断是否登录 */
        isLogin : function(){
            var s = this;
            asCore.getBsessionid(bid=>{
                if(bid) {
                    asCore.getContext(context=>{
                        s.headData.name = context.currentUserName;
                        s.headData.label = context.currentUserFullName;
                        s.loadHeadImg(bid);
                    });
                }
            });
            
        },
        /* 判断是否是ios */
        isIos:function(){
            const { platform } = weex.config.env;
            return platform.toLowerCase() === 'ios';
        },
        /* 加载头像 */
        loadHeadImg(bid){
            var s = this;
            asCore.getTime(time=>{
                s.headData.img = asCore.rootPath + "/system/user/userPhotoDownloadAction?bsessionid=" + bid + "&t="+time;
            });
        },
        onrefresh (event) {
            var s = this;
            s.refreshing = true
            setTimeout(() => {
                s.refreshing = false; 
            }, 1000);
        },
        openPage (type){
            if(type==="setting"){
                navigator.push(asCore.localpath + 'yunagileoa/my/setting.js');
            }else if(type==="personinfo"){
                navigator.push(asCore.localpath + 'yunagileoa/my/personinfo.js');
            }else if(type === 'messageSeting'){
                navigator.push(asCore.localpath + 'yunagileoa/my/messageSeting.js');
            }else if(type === 'helpCenter'){
                navigator.push(asCore.localpath + 'yunagileoa/my/helpCenter/helpCenter.js');
            }else if(type === 'share'){
                this.showShare = true;
            }
        },
        setTheme:function(name){
            this.bartheme = name;
        },
        /* 更新头像 */
        updateImg(event) {
            this.isLogin();
        },
        shareBtnClick(){
            this.showShare = false;
        }
    }
  };
</script>
<style scoped>
.wrapper {
    position: absolute;
    top: 0px;
    bottom: 0px;
    left: 0px;
    right: 0px;
  }
.iconfont {
    font-family:iconfont;
}
.title-icon {
    font-size:35px;
    color:#fff;
    font-weight: 300;
}
.title-icon2 {
    font-size:45px;
    color:#fff;
    font-weight: 300;
}
.minibar {
    z-index:101;
}
.scroller {
    z-index: 102;
}
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
.panel {
    background-color:#fff; 
    padding-left:15px;
    padding-right:30px;
    border-bottom-width: 1px;
    border-bottom-color: #d9d9d9;
}
.panel:active {
    background-color:#eee; 
}
.panel-content {
    flex-direction: row;
    margin-left:20px;
    padding-top:25px;
    padding-bottom:25px;
}
.content-img {
    width: 140px;
    height:140px;
    border-radius: 10px;
}
.content-div {
    flex: 1;
    padding-left:30px;
}
.content-title {
    margin-top:25px;
    color:#333;
    text-overflow:ellipsis;
    lines:1;
    font-size:35px;
}
.ios-top {
    margin-top:20px;
}
.content-text {
    color:#888;
    text-overflow:ellipsis;
    lines:1;
    margin-top:18px;
    font-size:28px;
}
.list-icon {
    height: 65px;
    width: 65px;
    border-radius: 10px;
    margin-right:20px;
    align-items: center;
    justify-content: center;
}
.list-margin {
    margin-top:30px;
}
.cell-icon {
    color:#ffffff;
}
.panel-right {
    flex-direction: row;
    align-items: center;
    justify-content: center;
    width:84px;
    height: 150px;
}
.panel-right-icon {
    font-size:50px;
    color:#c1c1c1;
}
.panel-right-icon2 {
    font-size:50px;
    color:#c1c1c1;
}

</style>
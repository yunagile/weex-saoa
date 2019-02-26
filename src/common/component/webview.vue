<template>
    <div class="wrapper" @viewdisappear="closePage">
        <!-- 标题栏 -->
        <div class="x-minibar bar-padding" append="tree" :style="{backgroundColor:barback}">
            <div :class="['left',isIos()?'ios-left':'']">
                <div class="x-back" @click="closeOrBack">
                    <text class="iconfont buttn-icon">&#xe9cf;</text>
                </div>
                <div class="x-close" v-if="isShowClose" @click="trueClose">
                    <text class="iconfont buttn-icon">&#xe6a9;</text>
                </div>
            </div>
            <text class="middle-title">{{title}}</text>
            <div :class="['right',isIos()?'ios-right':'']" @click="openShare">
                <text class="iconfont buttn-icon">&#xe6a7;</text>
            </div>
        </div>
        <web class="content" 
            ref="webview" 
            :src="url" 
            :showLoading="false" 
            @pagestart="pagestart" 
            @receivedtitle="getTitle" 
            @pagefinish="pagefinish"  
            @error="pageferror">
            
        </web>
        <x-load :show="loadding" :type="'default'" :interval="0" ></x-load>
        <!--自定义上拉菜单-->
        <share v-model="showShare" :items="shareItems" @itemClick="itemClick"
            @btnClick="shareBtnClick">
        </share>
    </div>
</template>

<script>
import asCore from "../js/core";
const navigator = weex.requireModule('navigator') ;
const wobj = weex.requireModule('webview');
export default {
    components:{
        'minbar' : require("./minbar.vue"),
        'x-load':require("./loading.vue"),
        'share' :require("./share.vue")
    },
    data(){
        return {
            barback:"#f5f5f5",
            refreshing:false,
            title:"",
            url:"",
            isFirst:0,
            isShowClose:false,
            loadding:false,
            showShare:false,
            shareItems:[
                {
                    type:"icon",
                    icon:"\ue6b0",
                    text:"复制链接",
                    style:{
                        fontSize:"65"
                    },
                    imgurl:""
                },
                {
                    type:"icon",
                    icon:"\ue6b8",
                    text:"刷新",
                    style:{
                        fontSize:"65"
                    },
                    imgurl:""
                },
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
                    text:"浏览器打开",
                    style:{
                        width:"68",
                        height:"68"
                    },
                    imgurl:asCore.localpath + "img/llq.png"
                }
            ],
            currentUrl:""
        }
    },
    created:function(){
        var s = this;
        var globalEvent = weex.requireModule('globalEvent') ;
        globalEvent.addEventListener("onPageInit", function (e) {
            /* 设置状态栏的颜色 */
            const nav = weex.requireModule("navbar");
            nav.setStatusBarStyle("black");
            /* 接收参数 */
            var param = navigator.param();
            s.title = param['title'];
            s.url = param['url'];
        });

    },
    methods:{ 
        onrefresh(){
            var s = this;
            s.refreshing = true
            setTimeout(() => {
                s.refreshing = false;
            }, 1000);
        },
        pagestart(event){
            this.loadding = true;
        },
        /* web页加载完成时事件 */
        pagefinish(event){
            var s = this;
            s.isFirst++;
            if(s.isFirst>=2){
                s.isShowClose = true;
            }
            if(event.title) {
                s.title = event.title;
            }
            s.loadding = false;
            s.currentUrl = event.url;
        },
        pageferror(){
            //asCore.alert("打开出错");
            /* 页面刷新出错时的逻辑 */
            s.loadding = false;
        },
        closePage(){
            const nav = weex.requireModule("navbar");
            nav.setStatusBarStyle("white");
        },
        isIos:function(){
            const { platform } = weex.config.env;
            return platform.toLowerCase() === 'ios';
        },
        closeOrBack(){
            var s = this;
            if(s.isFirst==1){
                navigator.back();
            }else{
                var web = this.$refs.webview;
                wobj.goBack(web);
            }
            
        },
        trueClose (){
            navigator.back();
        },
        /* 设置标题栏 */
        getTitle(event){
            this.title = event.title;
        },
        /* 打开分享面板 */
        openShare() {
            this.showShare = true;
        },
        /* 点击遮罩层时隐藏 */
        shareBtnClick() {
            this.showShare = false;
        },
        /* 分享按钮item点击事件 */
        itemClick(index,item){
            if(index==0){
                /* 复制浏览器链接 */
                const clipboard = weex.requireModule('clipboard');
                clipboard.setString(this.currentUrl)
                asCore.toast("复制成功");
            }else if(index==1){
                /* 刷新 */
                var web = this.$refs.webview;
                wobj.reload(web);
            }else if(index==3){
                /* 在浏览器中打开 */
                const webutil = weex.requireModule('webutil');
                webutil.openUrl(this.currentUrl);
            }
        }
    }
}
</script>

<style scoped>
    .wrapper{
        background-color: #f5f5f5;
    }

    .iconfont{
        font-family: iconfont;
    }

    .content{
        position:fixed;
        top: 130;
        left: 0;
        right:0;
        bottom: 0;
    }

    .bar-padding {
        padding-top: 30px;
    }
    .x-minibar {
        position: fixed;
        display: flex;
        top: 0;
        left: 0;
        right: 0;
        width: 750px;
        height: 130px;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        background-color: transparent;
        flex-wrap: nowrap;
        z-index: 101;
        border-bottom-color: #d1d1d1;
        border-bottom-width: 1px;
    }
    .left {
        width: 180px;
        flex-direction: row;
    }
    .x-back {
        padding-left: 25px;
        padding-right:15px;
    }
    .x-close {
        padding-left:15px;
        padding-right:10px;
    }
    .middle-title {
        font-weight: bold;
        font-size: 35px;
        font-weight: 300;
        color: #666666;
        height: 36px;
        line-height: 36px;
        width: 300px;
        text-align: center;
        text-overflow :ellipsis;
        lines:1;
    }
    .cell-icon {
        color:#666666;
    }
    .right {
        width: 180px;
        padding-right: 30px;
        align-items: flex-end;
        padding-top:8px;
    }
    .buttn-icon {
        font-size: 40px;
        color: #666666;
    }
    .ios-left {
        padding-top:10px;
    }
    .ios-right {
        padding-top:12px;
    }
</style>

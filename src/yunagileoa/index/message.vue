<template>  
    <div class="wrapper" bubble="true">
        <minibar :theme="bartheme" title="消息" :leftButton="false" :isPadding="true" :useDefaultReturn="false" 
            @leftButtonClick="leftButtonClick" @rightButtonClick="opentopM" :rightButton="true">
            <text slot="left" class="iconfont title-icon">&#xe633;</text>
            <text slot="right" class="iconfont title-icon2" >&#xe889;</text>
        </minibar>
        
        <list class="scroller" >
            <refresh class="refresh" @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            <cell>
                <searchbar class="searchbar"  ref="searchbar" disabled=true @inputDisabledClicked="openSearch"></searchbar>
            </cell>
            <div class="list-top-border" v-if="msgData.length > 0"></div>
            <cell v-for="(msg,index) in msgData" :key="index" ref="mainList" > 
                <swipecell @cellclick="clickmenu(msg)" @actionClick="actionClick(msg)">
                  <div slot="content" class="panel" >
                    <div class="panel-content">
                        <div :class="['content-icon',isIos()?'ios-icon-top':'']" :style="{ backgroundColor: msg.backcolor}" v-if="msg.icon && !msg.img">
                            <text class="content-font iconfont" :style="{ fontSize: msg.fontsize + 'px'}">{{msg.icon}}</text>
                        </div>
                        <image class="content-img" :src="msg.img" v-if="msg.img && !msg.icon"></image>
                        <div class="msg-tag" v-if="msg.count > 0">
                            <text class="count-text">{{msg.count}}</text>
                        </div>
                        <div class="content-div">
                            <text :class="['content-title',isIos()?'':'ios-icon-top3']">{{msg.title}}</text>
                            <text :class="['content-text',isIos()?'':'ios-icon-top2']">{{msg.label}}</text>
                        </div>
                    </div>
                  </div>
                </swipecell>
            </cell> 
        </list>
        
           
     <div class="topMenu" @click="closetopM" v-if="topMShow">  
          <div slot="content" :class="['content',theme]">
                  <div :class="['topBtn',theme+'_border']" @click="openQr">           
                      <text :class="['iconfont', 'topBtn_icon',theme+'_font']" >&#xe672;</text>
                      <text :class="['topBtn_text',theme+'_font']">扫描二维码</text>
                  </div>
                  <div :class="['topBtn',theme+'_border']" @click="onrefresh">
                      <text :class="['iconfont', 'topBtn_icon',theme+'_font']" >&#xe8ae;</text>
                      <text :class="['topBtn_text',theme+'_font']">刷新</text>
                  </div>
          </div>          
     </div>            
    </div>
</template>

<script>
    import msgconfig from "../../configs/mesconfig";
    import asCore from "../../common/js/core";
    const modal = weex.requireModule('modal');
    const navigator = weex.requireModule('navigator');
    const storage = weex.requireModule('storage');
    export default {
        components: { 
            "minibar" :require("../../common/component/minbar.vue"),
            "searchbar" :require("../../common/component/searchbar.vue"),
            "swipecell" : require("../../common/component/swipecell.vue")
        },
        props: {
            qrColor: {
                type: String,
                default: "#1B82D2"
            },
            bartheme:{
                type: String,
                default: ""
            }
        },
        data () {
            return {
                refreshing: false,
                msgData : msgconfig.msgconfig,
                topMShow:false,
                theme : "topM_theme1"
            }
        },
        methods: {
            clickmenu(data){
                if(data.rowid  == "news"){
                    navigator.push(asCore.localpath + "yunagileoa/app/news/news.js");
                }else if(data.rowid  == "dayReport"){
                    navigator.push(asCore.localpath + "yunagileoa/app/dayReport/dayList.js");
                }else if(data.rowid  == "notice"){
                    navigator.push(asCore.localpath + "yunagileoa/app/notice/noticeActivity.js");
                }else if(data.rowid  == "email"){

                }else{
                    navigator.pushParam(asCore.localpath + "yunagileoa/app/message/message.js",{
                        title: data.title,
                        url: data.img
                    }
                );
                }
            },
            actionClick(){
                asCore.toast("删除按钮被单击");
            },
            leftButtonClick () {
                navigator.push(asCore.localpath + "saui/list.js");
            },
            openMessage (data) {
               
            },
            onrefresh (event) {
                this.refreshing = true;
                this.topMShow = false;
                setTimeout(() => {
                    this.refreshing = false;
                    modal.toast({
                        message:"刷新成功"
                    });
                }, 1000)
            },
            opentopM(){
                this.topMShow = true;
            },
            closetopM:function(e){
                this.topMShow = false;
                e.stopPropagation();
            },
            openQr:function(e){
                var qr=weex.requireModule('qr');
                var p={};
                p.color='#ffffff'
                p.bgcolor=this.qrColor;
                this.topMShow = false;
                qr.open(p,(res)=>{
                    var url=res.url;
                })        
            },
            openSearch() {
                navigator.push(asCore.localpath + "yunagileoa/index/searchP.js");
            },
            setColor(color){
                this.qrColor = color;
            }, 
            setTheme(tname){
                this.bartheme = tname;
                storage.getItem('theme',event=>{
                    if(event.data){
                        if(event.data == 'theme1'){
                            this.theme = 'topM_'+event.data;
                        }else{
                            this.theme = 'topM_theme2'
                        }
                    }         
                }); 
            },
            isIos () {
                const { platform } = weex.config.env;
                return platform.toLowerCase() === 'ios';
            }
        }
    };
</script>
<style scoped src="./css/message.css"></style>
<style scoped lang="scss">
    @import './css/theme.scss'; 
</style>

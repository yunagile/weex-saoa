<template>
    <div class="wrappere">
        <minbar title="帮助中心" :isPadding="true" :rightButton="true" @rightButtonClick="openShare">
            <text slot="right" class="iconfont title-icon2">&#xe640;</text>
        </minbar>
        <div class="content">
            <div class="content_list" @click="openPage('help')">
                <div class="content_icon" style="background-color:#00d28b">
                    <text class="iconfont icon">&#xe775;</text>
                </div>
                <div class="content_list_div">
                    <text class="list_title">常见问题</text>
                    <text class="list_lable">常见问题解答</text>
                </div>
            </div>

            <div class="content_list" @click="openPage('anli')">
                <div class="content_icon" style="background-color:#ff5a65">
                    <text class="iconfont icon">&#xe674;</text>
                </div>
                <div class="content_list_div">
                    <text class="list_title">行业案列</text>
                    <text class="list_lable">经典行业案列</text>
                </div>
            </div>

            <div class="content_list" @click="openPage('contactWay')">
                <div class="content_icon" style="background-color:#22b8fd;">
                    <text class="iconfont icon">&#xe646;</text>
                </div>
                <div class="content_list_div">
                    <text class="list_title">联系我们</text>
                    <text class="list_lable">有任何疑问可直接电话沟通</text>
                </div>
            </div>
        </div> 
        <share v-model="showShare" :items="shareItems" @itemClick="itemClick"
            @btnClick="shareBtnClick">
        </share>
    </div>
</template>

<script>
import asCore from "../../../common/js/core";
const navigator = weex.requireModule('navigator');
export default {
    components:{
        "minbar" : require("../../../common/component/minbar.vue"),
        "share" : require("../../../common/component/share.vue")
    },
    data () { 
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
            ]
        }
    },
    methods:{
        openPage(event){
            if(event === 'contactWay'){
                navigator.push(asCore.localpath+'yunagileoa/my/helpCenter/contactWay.js');
            }else if(event === 'help'){
                navigator.pushParam(asCore.localpath + "common/component/webview.js",{
                    title:"常见问题",
                    url:"http://kf.qq.com/touch/product/mobileqq_app.html"
                });
            }else if(event === 'anli'){
                navigator.pushParam(asCore.localpath + "common/component/webview.js",{
                    title:"公司案例",
                    url:"http://www.yunagile.com/a/anli/"
                });
            } 
        },
        openShare(){
            this.showShare = true;
        },
        shareBtnClick() {
            this.showShare = false;
        },
        itemClick(index,item){
            
        }
    }

}
</script>
<style scoped>
    .wrapper{
        
    }
    .iconfont{
        font-family: iconfont;
    }

    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }
    .title-icon2 {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }

    .content{
        width:750px;
        height:1500px;
        background-color: #f5f5f5;
        padding-top:30px;
        
    }

    .content_list{
        flex-direction: row;
        width: 710px;
        padding-top: 20px;
        padding-bottom: 25px;
        background-color: #fff;
        border-top-color: #dfe3eb;
        border-top-width: 1px;
        border-top-style: solid;
        border-bottom-color: #dfe3eb;
        border-bottom-width: 1px;
        border-bottom-style: solid;
        border-left-color: #dfe3eb;
        border-left-width: 1px;
        border-left-style: solid;
        border-right-color: #dfe3eb;
        border-right-width: 1px;
        border-right-style: solid;
        border-radius: 3px;
        margin-left:20px;
        margin-right:20px;
        padding-left:10px;
        margin-bottom:15px;
    }

    .content_list:active{
       background-color: #eee;
    }

    

    .content_list_div{
        flex-direction: column;
        
    }

    .content_icon{
        width:100px;
        height:100px;
        margin-right:25px;
        margin-left :20px;
        border-radius:50px;
        align-items: center;
        justify-content: center;
    }

    .icon{
        font-size: 50px;
        color:#fff;
        text-align: center;
    }


    .list_title{
        color:#333;
        font-size:30px;
        line-height: 45;
        text-align: left;
    }
    .list_lable{
        color:#888;
        padding-top:10px;
        font-size:25px;
        line-height: 40;
        text-align: left;
    }
    


</style>

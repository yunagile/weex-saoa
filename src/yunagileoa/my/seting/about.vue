<template>
    <div class="wrappre">
        <minbar title="关于" :isPadding="true">
        </minbar>
        <div class="content" >
            <div class="content_h">
                <image class="logo" :src="logourl"></image>
                <div style="width:150px; height:20px;" ></div>
                <div class="content_h_div">
                    <text class="content_h_text1">软捷OA</text>
                    <text class="content_h_text2">v2.2.0</text>
                </div>                
            </div>
            <div class="content_c">
                <x-cell title="新功能介绍" :topBorder="true" :hasArrow="true"></x-cell>
                <x-cell title="常见问题"  :hasArrow="true"></x-cell>
                <x-cell title="公司介绍"  :hasArrow="true"></x-cell>
                <x-cell title="联系我们"  :bottomBorder="true" :hasArrow="true" @cellClick="openPage('contactWay')"></x-cell>
            </div>           
        </div>
        <div class="content_b">
                <div class="content_b_top">
                    <a class="text1_l">
                        <text class="content_b_text1 ">官网</text>
                    </a>
                    <a calss="text1_c">
                        <text class="content_b_text1 ">  |   </text>
                    </a>
                    <a class="text1_r">
                        <text class="content_b_text1 ">服务协议</text>
                    </a>  
                </div>
                <text class="content_b_text2 ">Copyright © 2013-2018 AgileSoft all Reserved</text>
        </div>
    </div>
</template>

<script>
import asCore from "../../../common/js/core";
const navigator = weex.requireModule('navigator');
export default {
    components:{
        "x-cell" : require("../../../common/component/cell.vue"),
        "minbar" : require("../../../common/component/minbar.vue")
    },
    data(){
        return {
            logourl:""
        }
    },
    created () { 
       this.logourl = asCore.localpath + "img/icon.png";
    },
    methods:{
        openPage(event){
            if(event === 'contactWay'){
                navigator.push(asCore.localpath+'yunagileoa/my/helpCenter/contactWay.js');
            }
        }
    }
}
</script>
<style>
    .wrapper{
        position: absolute;
        top:0;
        left: 0;
        right: 0;       
    }
    .iconfont{
        font-family: iconfont;
    }
    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }
    .content{
        height:1500px;
        background-color: #f5f5f5;
    }
    .content_h{
        flex-direction: column;
        justify-content: space-between;
        width:750px;
        padding-top:100px;
        padding-bottom:60px;
        padding-left:300px;
        background-color: #f5f5f5;
    }
    .logo{
        width:150px;
        height:150px;
        border-radius: 15px;
    }
    .content_h_div{
        width:150px;
        
    }
    .content_h_text1{
        font-size: 30px;
        font-weight: 600px;
        text-align: center;
        color:#333333;
    }
    .content_h_text2{
        font-size: 20px;
        color:#c2c2c2;
        text-align: center;
    }
    .content_b{   
        position:absolute;
        bottom:0px;
        height:120px;
        width:750px;
        background-color:#f7f7f7;
        
    }
    .content_b_top{
        width:400px;
        flex-direction: row;
        justify-content:center;
        margin-left:175px;
        margin-bottom:20px;
        margin-top:15px;
    }
    .content_b_text1{
        font-size:25px;
        color:#33a0d6;
    }
    .text1_l{
        width:65px;
    }
    .text1_c{
        width:35px;
    }
    .text1_r{
        width:130px;
    }
    .content_b_text2{
        font-size:20px;
        color:#a0a0a0;
        text-align: center;
    }
</style>

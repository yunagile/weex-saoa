<template>
    <div class="wrapper">
        <minbar title="密码修改" :isPadding="true">   
        </minbar>
        <div class="content">
            <div class="title_div" style="height:80px;padding-top:20px;">
                <text class="title_text">原密码</text>
            </div>
            <div class="input_div">
                <input type="password" class="input" placeholder ='请输入原密码'  :value="oldPassword" @input="oldPassinput">
            </div>
            <div class="title_div" >
                <text class="title_text">新密码</text>
            </div>
            <div class="input_div">
                <input type="password" class="input" placeholder ='6-16个字符，区分大小写' :value="newPassword"  @input="newPlassinput">
            </div>
            <div class="title_div" >
                <text class="title_text">新密码确认</text>
            </div>
            <div class="input_div">
                <input type="password" class="input" placeholder ='再次输入新密码' :value="newPassword2" @input="newPassinput2">
            </div>
            
            <div class="button">
                <x-btn type="normal" text="确认修改" :btnStyle="{width:700}" :textStyle="getColor" @buttonClick="modify"></x-btn>
            </div>
            
        </div>
    </div>
</template>

<script>
import asCore from "../../common/js/core";
import Utils from "../../common/js/utils";
import md5 from "../../common/js/md5";
var stream = weex.requireModule("stream");
const modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
export default {
    data(){
        return {
            currentPersonCode:"",
            oldPassword:"",
            newPassword:"",
            newPassword2:"",
            theme:"#555555"
        }        
    },
    computed :{
        getColor(){
            return {
                color:this.theme
            }
        }
    },
    created() {
        var s = this;
        Utils.getThemeColor((color,theme) => {
            if(theme=="theme1"){
                s.theme = "#F15A49";
            }else{
                s.theme = color;
            }
        });
    },
    components:{
        "minbar" : require("../../common/component/minbar.vue"),
        "x-btn" : require("../../common/component/button.vue")
    },
    methods:{
        modify:function(){
            var s = this;
            if(!s.oldPassword){
                modal.toast({message:'请输入原密码'});
                return false;
            }
            if(!s.newPassword){
                modal.toast({message:'请输入新密码'});
                return false;
            } 
            if(s.newPassword != s.newPassword2){
                modal.toast({message:'两次密码输入不相同,请重新输入'});
                return false;
            }
            asCore.getContext(context =>{
                s.currentPersonCode = context.currentPersonCode;
            });
            var oldps = md5.hex_md5(s.oldPassword);
            var newps = md5.hex_md5(s.newPassword);
            var param={
                username:s.currentPersonCode,
			    password:oldps,
			    new_password:newps
            }
            asCore.post("/newPortal/opm/person/changePassword",param,(data,flag)=>{
                if(data['status']=="SUCCESS"){
                    asCore.toast("修改密码成功");
                    navigator.back();
                }else{
                    asCore.toast(data.msg);
                }
            });
        },
        oldPassinput: function(event) {
            this.oldPassword = event.value;
        },
        newPlassinput: function(event){
            this.newPassword = event.value;
        },
        newPassinput2: function(event){
            this.newPassword2 = event.value;
        }

    }
}
</script>

<style>
    .wrapper{
        background-color:#f5f5f5;
    }
    .button {
        align-items: center;
        justify-content: center;
        height:200px;
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
        width:750px;
    }

    .title_div{
        width:750px;
        height:70px;
        padding-left:20px;
        padding-top:10px;
    }


    .title_text{
        font-size:30px;
        line-height: 60px;
        font-weight: 600px;
        color:#666666;
    }

    .input_div{
        width:750px;
        padding-left:20px;
        height:85px;
        background-color:#fff;
        border-bottom-color: #e2e2e2;
        border-bottom-width: 1px;
        border-top-color: #e2e2e2;
        border-top-width: 1px;
    }

    .input{
        font-size:30px;
        height: 83px;
        background-color:#fff;
    }

    .alterBtn{
        width:650px;
        height:80px;
        background-color:#33a0d6;
        border-radius:8px;
        padding-top:20px;
        margin-left:50px;
        margin-top:50px;
    }

    .alterBtn:active{
        background-color:#7D7D7D;
    }

    .btn_text{
        font-size: 32px;
        line-height: 40px;
        color:#fff;
        text-align: center;
    }
    

</style>

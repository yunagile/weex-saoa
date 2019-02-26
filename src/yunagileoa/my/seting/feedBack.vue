<template>
    <div class="wrapper">
        <minbar title="意见与反馈" :isPadding="true">
        </minbar>
        <div class="content">
            <div class="content_div border-top-bottom">
                <div class="text_div">
                    <textarea class="feedContent" placeholder="请输入遇到的问题或建议" rows=4></textarea>
                    <div class="upload_div">
                        <div class="upload_div_l">
                            <text class="iconfont upload_icon">&#xe623;</text>
                        </div>
                        <div class="upload_div_r">
                            <input  placeholder="添加图片说明"/>
                        </div>
                        
                    </div>
                </div>
            </div>
            <div class="content_div2 border-top-bottom">
                <text class="content_div2_text">联系方式</text>
                <div class="input_div">
                    <input class="upload_div_input" placeholder="选填，便于我们联系您"/>
                </div>               
            </div>

            <div class="button">
                <x-btn type="normal" text="提 交" :btnStyle="{width:680}" :textStyle="getColor" @buttonClick="modify"></x-btn>
            </div>           

        </div>
    </div>
    
</template>

<script>
import Utils from "../../../common/js/utils";
export default {
    components:{
        "minbar" : require("../../../common/component/minbar.vue"),
        "x-btn" : require("../../../common/component/button.vue")
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
    data () {
        return {
            theme:"#555555"
        }
    }
}
</script>

<style>
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
        padding-top:30px;
        background-color: #f5f5f5;
        padding-bottom:40px;
    }
    .content_div{
        width:750px;
        background-color:white;
        padding-left:25px;
        padding-right:25px;
        padding-bottom:30px;
    }
    .border-top-bottom {
        border-top-color: #e2e2e2;
        border-top-width: 1px;
        border-bottom-color: #e2e2e2;
        border-bottom-width: 1px;
    }
    .text_div{
        width:700;
    }
    .feedContent{
        padding-top:30px;
        font-size:30px;
    }
    .upload_div{
        flex-direction: row;
        justify-content: flex-start;
        width:750px;
        margin-top:15px;
        margin-left:5px;
    }
    .upload_div_l{
        width:120px;
        height:120px;
        border-bottom-width:1px;
        border-bottom-color:#c5c5c5;
        border-top-width:1px;
        border-top-color:#c5c5c5;
        border-left-width:1px;
        border-left-color:#c5c5c5;
        border-right-width:1px;
        border-right-color:#c5c5c5;
        margin-right:40px;
        padding-top:34px;
    }
    .upload_icon{
        font-size: 50px;
        line-height: 50px;
        text-align: center;
        color:#c5c5c5;

    }
    .upload_div_r{
        width:540px;
        height:120px;
        padding-top:43px;
    }
    .button {
        align-items: center;
        justify-content: center;
        height:200px;
    }
    .content_div2{
        flex-direction: row;
        justify-content: center;
        align-items: center;
        width:750px;
        height:100px;
        margin-top:40px;
        background-color:white;   
        padding-left:20px;
        padding-top:30px;
        padding-bottom:30px;
    }

    .content_div2_text{
        color:#666666;
        font-size: 30px;
        width: 150px;
        padding-left:5px;
    }

    .input_div{
        flex: 1;
        
    }

    .upload_div_input{
        font-size: 30px;
        height: 100px;
    }
    .submitBtn{
        width:650px;
        height:80px;
        background-color:#33a0d6;
        border-radius:8px;
        padding-top:20px;
        margin-left:50px;
    }

    .submitBtn:active{
        background-color:#7D7D7D;
    }

    .btn_text{
        font-size: 32px;
        line-height: 40px;
        color:#fff;
        text-align: center;
    }



</style>


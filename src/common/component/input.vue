<template>
    <div class="wrappere">
        <minbar :title="title" :isPadding="true" :rightButton="true" @rightButtonClick="saveInput">
            <text slot="right" class="title-text">保存</text>
        </minbar>
        <div class="content">
            <textarea class="textarea" rows=50 placeholder="请输入内容" :value="textValue" @input="textInput"></textarea>
        </div>
    </div>
</template>

<script>
import minbar from "./minbar.vue"
import asCore from "../../common/js/core";
const navigator = weex.requireModule('navigator') ;
const lBroad = new BroadcastChannel("inputBroad");
export default {
    components:{
        "minbar" : minbar
    },
    data(){
        return {
            title:"",
            textValue:"",
            type:""
        }
    },
    created:function(){
        var s = this;
        var globalEvent = weex.requireModule('globalEvent') ;
        globalEvent.addEventListener("onPageInit", function (e) {
            var param = navigator.param();
            s.title = param['title'];
            s.textValue = param['value'];
            s.type = param['type'];
        });
    },
    methods:{ 
        saveInput : function(){
            var s = this;
            navigator.back();
            lBroad.postMessage({
                type:s.type,
                value:s.textValue
            });
        },
        textInput : function(event){
            this.textValue = event.value;
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

    .content{
        width:750px;
        height:1500px; 
        background-color: #f7f7f7;
        padding-top:30px;
    }

    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }
    .title-text {
        color:#fff;
        font-size:30px;
    }
    .name_text{
        color:#888;
        font-size:28px;
    }
    .textarea {
        position: absolute;
        width: 750px;
        top:0;
        left: 0;
        right: 0;
        bottom: 0;
        padding:20px;
    }

    
</style>

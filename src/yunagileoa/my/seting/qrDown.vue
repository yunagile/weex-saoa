<template>
    <div class="wrappre">
        <minbar title="二维码下载" :isPadding="true"></minbar>
        <div class="btn_bar" >
            <div @click="onclick(0)" class="btn">
                <text  :class="['btn_text',isClick?'btn_text2':'' ]">Android</text>
            </div>
            <div class="btn" @click="onclick(1)">
                <text :class="['btn_text',isClick2?'btn_text2':'' ]" >IOS</text>
            </div>
        </div>
        <div style="width:750px;height:5px;position:relative">
            <div style="width:750px;height:1px;background-color:#e2e2e2;z-index:1;position:absolute;top:0"></div>
            <div ref="line" :style="{'width':lineWidth+'px','margin-left':lineLeft+'px','background-color':theme}" style="height: 5;z-index:2;position:absolute;top:0"></div>
        </div>
        <div class="content" >
            <div class="qr_div">
                <slider class="img_div" :index="page" @change="onchange" infinite="false">
                    <div class="img_div2" v-for="img in imageList">
                        <image style="width:590px;height:590px;" :src="img.src"/>
                    </div>
                </slider>
                <text class="text">扫描上方的二维码下载app</text>
            </div>
            
        </div>
    </div>
</template>

<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core";
var modal = weex.requireModule('modal');
const animation = weex.requireModule('animation');
const storage = weex.requireModule('storage'); 
export default {
    components:{
        "minbar" : require("../../../common/component/minbar.vue")
    },
    data(){
        return {
            page:0,
            isClick:true,
            isClick2:false,
            lineWidth:375,
            lineLeft:0,
            fromindex:0,
            imageList: [
                { src: ''},
                { src: ''},
            ],
            theme:'#2e3238'
        }
    },
    
    created () { 
        var s = this;
        s.imageList[0]['src'] = asCore.localpath + "img/android.png";
        s.imageList[1]['src'] = asCore.localpath + "img/ios.png";
        Utils.getThemeColor((color,theme) => {
            if(theme=="theme1"){
                s.theme = "#F15A49";
            }else{
                s.theme = color;
            }
       });
    },
    methods:{
        onclick(event){
            this.page=event;        
            this.animateLine(event);
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
                this.isClick = true;
                this.isClick2 = false;
            }else{
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
            
        }
    }
}
</script>

<style scoped>
    .wrappre{ 
        background-color:#F5F5F5;
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
        position: absolute;
        align-items: center;
        justify-content: center;
        top:230;
        left: 0;
        right: 0;
        bottom:0;
    }

    .btn_bar{
        flex-direction: row;
        align-items: center;
        justify-content: center;
        width: 750px;
        height: 85px;
        z-index: 9999;
    }

    .btn{
        flex: 1;
        height: 85px;
        justify-content: center;
        background-color: #ffffff;
    }

    .btn:active{
        background-color: #e1e1e1;
    }

    .btn_text{
        font-size: 28px;
        color: #888;
        line-height: 40px;
        text-align: center;
    }

    .btn_text2{
        font-size: 28px;
        color: #555555;
        line-height: 40px;
        text-align: center;
    }

    .qr_div{
        align-items: center;
        justify-content: center;
        background-color:#ffffff;
        box-shadow: 5px 5px 3px #c1c1c1;
        margin-top:-100px;
    }

    .img_div{
        width:620px;
        height:620px;
    }
    .img_div2 {
        padding:20px;
    }

    .text{
        padding-top:10px;
        padding-bottom:25px;
        text-align: center;
        color:#888;
        font-size:28px;
    }


    
</style>

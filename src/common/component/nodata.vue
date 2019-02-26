<!-- by:Jie. 2018/5/8
    @param type  [文本型] 默认值为 icon , 代表是要显示字体图标还是图片
    @param imgsrc [文本型] 如果type为 img 类型 , 需要设置imgsrc的图片地址
    @param iconStyle [对象] 设置icon的字体图标样式或者图片的宽度高度 如：
        iconStyle:{
            code :"\ue716",   //code为必须参数,设置字体图标的编码,如果设置的为图片可不填写
            fontSize: 50,
            color: "#333333"
        }
    @param textStyle [对象] 设置文本的样式,方式如上
    @param title [文本型] 设置图标下方的文本,默认为 "暂无数据~"
    @param top [文本型] 设置top,默认为 210
-->
<template>
    <div class="x-nodata" :style="{top:top}">
        <slot name="content">
            <text v-if="type=='icon'" class="iconfont" :style="[iconStyle,{marginTop:-top}]">{{iconStyle.code}}</text>
            <image v-else :src="imgsrc" :style="iconStyle"></image>
            <text class="font-text" :style="textStyle">{{title}}</text>
        </slot>
    </div>
</template>
<script>
    module.exports = {
        props:{
            type:{
                type: String,
                default: "icon"
            },
            imgsrc: {
                type: String,
                default: false
            },
            iconStyle:{
                type: Object,
                default: {}
            },
            textStyle:{
                type: Object,
                default: {}
            },
            title:{
                type: String,
                default: "暂无数据~"
            },
            top :{
                type: String,
                default: "230"
            }
        }
    }
</script>
<style scoped>
    .iconfont {
        font-family: iconfont;
    }
    .font-text {
        color:#A6A9AF;
        font-size:30px;
        margin-top:10px;
    }
    .x-nodata {
        position: absolute;
        bottom:0;
        left:0;
        right:0;
        align-items: center;
        justify-content: center;
    }
</style>
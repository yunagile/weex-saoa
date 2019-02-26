<template>
    <div class="wrapper">
        <minbar title="消息提醒设置" :isPadding="true">
        </minbar>
        <scroller>
        <div class="content">
            <div class="content_list">
                <div class="content_title">
                    <text class="content_title_t">会议提醒</text>
                </div>
                <x-cell title="开会前15分钟提醒我" :topBorder="true"  :bottomBorder="true" :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
            </div>
            <div class="content_list">
                <div class="content_title">
                    <text class="content_title_t">考勤</text>
                </div>
                <x-cell title="上班前15分钟提醒我"  :topBorder="true"  :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
                <x-cell title="下班前15分钟提醒我"  :bottomBorder="true" :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
            </div>
            <div class="content_list">
                <div class="content_title">
                    <text class="content_title_t">日报</text>
                </div>
                <x-cell title="日报提醒" :topBorder="true" :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
                <x-cell title="时间"  :hasArrow="true">
                    <div soft="value"><text class="value_text">19:00</text></div>
                </x-cell>
                <x-cell title="提醒" :bottomBorder="true" :hasArrow="true">
                    <div soft="value"><text class="value_text">周一，周二，周三，周四，周五</text></div>
                </x-cell>
            </div>
            <div class="content_list">
                <div class="content_title">
                    <text class="content_title_t">消息提醒</text>
                </div>
                <x-cell title="锁屏消息提醒" :topBorder="true" :bottomBorder="true" :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
            </div>
            <div class="content_list">      
                <div class="content_title">
                    <text class="content_title_t">本机提醒</text>
                </div>
                <x-cell title="已设置提醒" :topBorder="true" :bottomBorder="true" :hasArrow="true">
                    <div soft="value"><text class="value_text">0</text></div>
                </x-cell>
            </div>
            <div class="content_list">
                <div class="content_title">
                    <text class="content_title_t">推送提示开关</text>
                </div>
                <x-cell title="评论" :topBorder="true" :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
                <x-cell title="日程"  :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
                <x-cell title="报告" :bottomBorder="true" :hasArrow="false">
                    <div soft="value"><switch></switch></div>
                </x-cell>
            </div>
        </div>
        </scroller>
    </div>
</template>
<script>
export default {
    components:{
        "minbar" : require("../../common/component/minbar.vue"),
        "x-cell" : require("../../common/component/cell.vue")
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
    font-size:45px;
    color:#fff;
    font-weight: 300;
}
.content_title{
    width:750px;
    height:85px;
    background-color:#f5f5f5;
    padding-left: 25px;
    position: sticky;
}
.content_title_t{
    font-size: 30px;
    line-height: 85px;
	color:#666666;
    
}
.value_text{
    color:#a0a0a0;
    font-size:28px;
}
</style>

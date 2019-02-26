<template>
    <div class="wrappere">
        <minbar title="联系我们" :isPadding="true">
            
        </minbar>
        <div class="content">
            <x-cell title="联系方式" :topBorder="true" :hasArrow="false"></x-cell>
            <x-cell title="联系电话" lable=""  :hasArrow="false">
                <div soft="name">
                    <text class="name_text">0871-68322567</text>
                </div>
            </x-cell>
            <x-cell title="传真号码"   :hasArrow="false">
                <div soft="name">
                    <text class="name_text">0871-68265261</text>
                </div>
            </x-cell>
            <x-cell title="邮箱地址"   :hasArrow="false">
                <div soft="name">
                    <text class="name_text">yunagile@qq.com</text>
                </div>
            </x-cell>
            <x-cell title="公司地址"   :hasArrow="false" :bottomBorder="true">
                <div soft="name">
                    <text class="name_text">昆明市高新区滇缅大道活力空间307室</text>
                </div>
            </x-cell>
        </div>

    </div>
</template>

<script>
export default {
    components:{
        "x-cell" : require("../../../common/component/cell.vue"),
        "minbar" : require("../../../common/component/minbar.vue")
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
        background-color: #f5f5f5;
        padding-top:30px;
    }
    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }
    .name_text{
        color:#888;
        font-size:28px;
    }
    
</style>

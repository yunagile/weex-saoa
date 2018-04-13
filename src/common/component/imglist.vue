<template>
    <div class="wrapper">
        <list class="list" >
            <cell class="cell" v-for="(list,index) in listData" 
                :key="index"
                :index="index" 
                @click="oncellclick(list.rowid)">
                <div class="panel" >
                    <div class="panel-content">
                        <image class="content-img" :src="list.src"></image>
                        <div class="content-div">
                            <text class="content-title">{{list.title}}</text>
                            <text class="content-text">{{list.content}}</text>
                        </div>
                    </div>
                </div>
            </cell>
        </list>
    </div>
</template>

<style scoped>
    .panel {
        background-color:#fff; 
        padding-left:30px;
        padding-right:30px;
    }
    .panel:active {
        background-color:#eee; 
    }
    .panel-content {
        flex-direction: row;
        margin-left:10px;
        padding-top:25px;
        padding-bottom:25px;
        border-bottom-width: 1px;
        border-bottom-color: #d9d9d9;
    }
    .content-img {
        width: 200px;
        height:150px;
        border-radius: 10px;
    }
    .content-div {
        flex: 1;
        padding-left:25px;
    }
    .content-title {
        padding-top:3px;
        color:#333;
        text-overflow:ellipsis;
        lines:1;
        font-size:30px;
    }
    .content-text {
        color:#888;
        text-overflow:ellipsis;
        lines:2;
        padding-top:18px;
        font-size:30px;
    }
    .wrapper {


    }
</style>
<script>
    var modal = weex.requireModule('modal');
    module.exports = {
        props:["listData"],
        data () {
            return {
                
            }
        },
        methods: {
            oncellclick(id) {
                const self = this;
                self.$emit('rowclick', {rowid:id});
            }
        }
        
    }
</script>
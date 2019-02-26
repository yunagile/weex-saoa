<template>
    <div class="wrapper">
        <minbar title="工作台" :theme="bartheme" :leftButton="false" :rightButton="true" :isPadding="true" @rightButtonClick="switchT()">
            <text slot="right" class="iconfont title-icon" v-if="theme == 'list'" >&#xe62a;</text>
            <text slot="right" class="iconfont title-icon" v-if="theme == 'lattice'" >&#xe680;</text>
        </minbar>
        <lattice v-if="theme == 'lattice'" :data="appData" ref="lattice"></lattice>   
        <list1 v-if="theme == 'list'" :data="appData" ref="list1"></list1>
    </div>   
</template>

<script>
var pref = weex.requireModule("storage");
const stream = weex.requireModule("stream");
const modal = weex.requireModule("modal");
export default {
    data () {
        return {
            appData:{}
        }
    },
    props: {
      theme: {
        type: String,
        default: 'list'
      },
      bartheme:{
        type: String,
        default: ""
      }
    },
    components:{
        "minbar" : require("../../common/component/minbar.vue"),
        "list1" : require("../desk/desk_list.vue"),
        "lattice" : require("../desk/desk_lattice.vue")
    }, 
    methods:{
        /* 获取应用列表 */
        getApp(callback){
            var s = this;
            stream.fetch({
                method: "get",
                url: "http://120.27.26.19:8068/bsys/public/res/app.json",
                type:"json"
            },
            function(ret) {
                if (!ret.ok) {
                    asCore.toast("网络连接失败,请检查网络设置");
                    callback && (callback({}));
                } else {
                    var data = ret.data; 
                    s.appData = data;
                    callback && (callback(data));
                }
            });
        },
        switchT(){
            if(this.theme == 'list'){
                this.theme = "lattice";
                pref.setItem('appcenter', "lattice");
            }else{
                this.theme = "list";
                pref.setItem('appcenter', "list");
            }
        },
        setTheme:function(name){
            this.bartheme = name;
        }
    }
}
</script>


<style scoped>
    
    .wrapper{
        position:absolute;
        top: 0px;
        bottom: 0px;
        left: 0px;
        right: 0px;
    }
    .iconfont{
        font-family: iconfont;
    }
    .title-icon {
        font-size:35px;
        color:#fff;
        font-weight: 300;
    }
</style>
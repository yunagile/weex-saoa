<template>
    <div class="warpper">
        <minbar :title="title+'项目'" :isPadding="true" @titleButtonClick="openMenu">
            <text slot='other-title' :class="['iconfont','title-icon2',isIos()?'iostop':'']" @click="openMenu">&#xee4e;</text>
            <text slot="right" class=" title-icon" @click="addItem">添加({{selectP.length}})</text>
        </minbar>
        <div class="backbai">
            <searchbar ref="searchbar"  @wxcSearchbarInputOnInput="onInput"></searchbar>
        </div>
        <!-- 暂无记录 -->
        <x-nodata v-if="noRows" 
          type="icon" 
          title="暂无项目数据~" 
          :iconStyle="{code:'\ue6c4',fontSize:150,color:'#DBDCE0'}">
        </x-nodata>
        <list v-else class="scroller" loadmoreoffset="10" @loadmore="onloading" ref="mainlist">
            <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            <cell class="content backbai">
                <div v-for="(p,index) in projects" :key="index" class="item" @click="clickBox(index)">
                    <div class="item_title" >
                        <text class="title_text">{{p.name}}</text>
                        <text class="lable_text">{{p.typeName}}</text>
                    </div>
                    <div class="item_click" v-if="p.click">
                        <text :class="['iconfont','click_icon',theme]">&#xe671;</text>
                    </div>
                </div>

            </cell>
            <!-- 加载更多数据,目前最佳效果则采用 cell的方式进行加载 , 当数据加载完成时直接隐藏 -->
            <cell class="loading" v-if="loadinging">
                <image class="loadimage" :src="loadmoreSrc"></image>
                <text class="indicator-text">正在加载更多...</text>
            </cell>  
        </list>
        <x-popup :show="showMenu" pos="top"  height="363 "   @popupClicked="closeMenu">
            <div slot="popup_content" class="popup_content">
                <div class="menu_div" @click="changeClick('all')">
                    <text :class="['menu_title',param.typeCode==''?theme:'']">全部</text>
                    <text v-if="param.typeCode == ''" :class="['menu_click_icon', 'iconfont', param.typeCode==''?theme:'']">&#xe671;</text>
                </div>
                <div class="bottom_border"></div>
                <div class="menu_div" @click="changeClick('3')">
                    <text :class="['menu_title',param.typeCode=='3'?theme:'']">在行</text>
                    <text v-if="param.typeCode == '3'" :class="['menu_click_icon', 'iconfont', param.typeCode=='3'?theme:'']">&#xe671;</text>
                </div>
                <div class="bottom_border"></div>
                <div class="menu_div"  @click="changeClick('1')">
                    <text :class="['menu_title',param.typeCode=='1'?theme:'']">售前</text>
                    <text v-if="param.typeCode == '1'" :class="['menu_click_icon', 'iconfont', param.typeCode=='1'?theme:'']">&#xe671;</text>
                </div>
                <div class="bottom_border"></div>
                <div class="menu_div" @click="changeClick('2')">
                     <text :class="['menu_title',param.typeCode=='2'?theme:'']">售后</text>
                    <text v-if="param.typeCode == '2'" :class="['menu_click_icon', 'iconfont', param.typeCode=='2'?theme:'']">&#xe671;</text>
                </div>
            </div>
        </x-popup>
        <x-load :show="loadinging2" :type="'default'" :interval="0" ></x-load>
    </div>
</template>

<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core";
const storage = weex.requireModule('storage'); 
const navigator = weex.requireModule("navigator");
const dom = weex.requireModule('dom');
export default {
    components:{
        "minbar":require("../../../common/component/minbar.vue"),
        "searchbar": require("../../../common/component/searchbar.vue"),
        "x-popup": require("../../../common/component/popup.vue"),
        'x-load':require("../../../common/component/loading.vue"),
        "x-nodata":require("../../../common/component/nodata.vue")
    },
    data(){
        return{
            arr:{},
            projects:[],
            param:{
                page:1,
                rows:20,
                keywords : '',
                state:'',
                typeCode:''
            },
            theme:'theme1',
            showMenu:false,
            title:'全部',
            search:'',
            loadinging:false,
            loadinging2:false,
            refreshing:false,
            loadmoreSrc: asCore.localpath + "img/juhua.gif",
            selectP:[],
            all:false,
            noRows:false
        }
    },
    
    beforeCreate(){
       storage.getItem("theme", event => {
        if (event.data) {
            this.theme = event.data;
            if (event.data == "theme1") {
                this.theme = "redtheme";
            }
        }
      });
    },
    created(){
        this.refreshData();
    },
    methods:{
        refreshData(){
            var s = this;
            s.all = false;
            s.param.page = 1;
            s.loadinging2 = true;
            s.getProject("refresh");
        },
        onrefresh(){
            var s = this;
            s.all = false;
            /* 初始化页 */
            s.param.page = 1;
            s.refreshing = true;
            /* 重置加载更多 */
            s.$refs.mainlist.resetLoadmore();
            s.getProject("refresh",()=>{
                s.refreshing = false;
            });
        },
        onloading(){
            var s = this;
            if(s.all) return;
            /* 初始化页 */
            s.param.page += 1;
            s.getProject("loadmore",()=>{});
        },
        getProject(retype,callback){
            var s = this;
            asCore.post('/oa/weekItemQueryAction',s.param,(data,flag)=>{
                if(data['status'] == "SUCCESS"){
                    var pData = JSON.parse(data["data"]["data"]);
                    var rows = pData.rows;
                    var parrs = []; 
                    if(rows.length>0){
                        s.noRows = false;
                        s.loadinging2 = false;
                        for(var i = 0;i<rows.length;i++){
                            let click = false; 
                           /**判断是否被选中 */
                            if(s.selectP.length>0){
                                 click = s.isClick(rows[i].id);   
                            }
                            parrs.push({
                                'id':rows[i].id,
                                'code':rows[i].code,
                                'createTime':rows[i].createTime,
                                'name':rows[i].name,
                                'pmCode':rows[i].pmCode,
                                'pmName':rows[i].pmName,
                                'state':rows[i].state,
                                'typeCode':rows[i].typeCode,
                                'typeName':rows[i].typeName,
                                'click':click
                            });
                        }
                        if(retype=="refresh"){
                            s.projects = parrs;
                        }else{
                            s.projects.push(...parrs);
                        }
                        
                        /* 判断是否已加载全部数据 */
                        if (rows.length % 20 != 0 && s.param.page >= 1) {
                            s.all = true;
                            /* 隐藏加载更多 */
                            s.loadinging = false;
                        } else {
                            s.loadinging = true;
                            /* 重置加载更多 */
                            s.$refs.mainlist.resetLoadmore();
                        }
                    }else{
                        /* 无数据 */
                        s.loadinging2 = false;
                        s.loadinging = false;
                        s.projects = [];
                        s.noRows = true;
                        /* 重置加载更多 */
                        //s.$refs.mainlist.resetLoadmore();
                    }
                    
                    callback && (callback());
                }else{
                    s.loadinging2 = false;
                    if (s.param.page > 1) {
                        s.all = true;
                        /* 隐藏加载更多 */
                        s.loadinging = false;
                    }
                    asCore.toast('获取项目信息失败！');
                    callback && (callback());
                }
                
            });
        },

        isClick(id){
            var s = this;
            var click = false;
            for(var y = 0;y < s.selectP.length;y++){
                 if(id == s.selectP[y].id){
                     click = true;
                     break;
                 }
            }
            return click;
        },

        clickBox(e){
            var s = this;
            if(!s.projects[e].click){
               s.projects[e].click=true;
               s.selectP.push(s.projects[e]);
            }else{
                s.projects[e].click = false;
                for(var i = 0;i<s.selectP.length;i++){
                    if(s.selectP[i].id == s.projects[e].id){
                        s.selectP.splice(i,1);
                        break;
                    }
                }
            }
            
        },
        addItem(){
            var s = this;
            if(s.selectP.length>0){
                const addItem = new BroadcastChannel('addItem');
                addItem.postMessage(s.selectP);
                navigator.back();
            }else{
                asCore.toast("请选择项目");
            }
        },
        openMenu(){
            if(this.showMenu){
                this.showMenu = false;
            }else{
                this.showMenu = true;
            }
        },
        closeMenu(){
            this.showMenu = false;
        },
        changeClick(e){
            var s = this;
            if(e == 'all'){
                s.param.typeCode='';
                s.title = '全部';
            }else{
                s.param.typeCode=e;
                if(e == 1){
                    s.title = '售前';
                }
                if(e == 2){
                    s.title = '售后';
                }
                if(e == 3){
                    s.title = '在行';
                }
            }
            s.refreshData();
            s.showMenu = false;
        },
        onInput(event){
            var s = this;
            s.param.keywords = event.value;
            s.refreshData();
        },
        isIos () {
            const { platform } = weex.config.env;
            return platform.toLowerCase() === 'ios';
        }
    }
  
}
</script>

<style scoped lang="scss">
@import './css/theme.scss';
.warpper {
    background-color: #f5f5f5;
}
/*刷新加载更多类的样式*/
.refresh {
  width: 750;
  display: flex;
  align-items: center;
}
.loading {
  height: 100px;
  padding-top: 30px;
  padding-bottom: 30px;
  background-color: #ffffff;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}
.indicator {
  margin-top: 30px;
  margin-bottom: 20px;
  height: 70px;
  width: 70px;
  color: #ce2020;
}
.indicator-text {
  color: #888888;
  font-size: 30px;
  text-align: center;
}
.loadimage {
  height: 40px;
  width: 40px;
}
.backbai {
    background-color: #ffffff;
}
.iconfont {
  font-family: iconfont;
}
.title-icon {
    font-size:30px;
    color:#fff;
    font-weight: 300;
}
.title-icon2{
    color: #fff;
    font-weight: 300;
    font-size: 30px;
    margin-top:25px;
    padding-bottom:20px;
    margin-left:5px;
}
.iostop {
    margin-top:32px;
}
.scroller{
    margin-top:20px;
    border-top-color:#e2e2e2;
    border-top-width: 1px;
}
.item{
    width:750px;
    height:120px;
    padding-left:20px;
    padding-right:20px;
    border-bottom-color:#e2e2e2;
    border-bottom-width:1px;
    flex-direction: row;
    justify-content: space-between;

}
.item:active {
    background-color: #eeeeee;
}
.item_title{
    width:650px;
    height:120px;
    padding-top:15px;
    padding-bottom:15px;
    flex-direction: column;
    justify-content: space-around
}
.title_text{
    font-size:30px;
    color: #333333;
}
.lable_text{
    font-size:22px;
    color:#999999;
}
.item_click{
    width:60px;   
    height:120px;
    justify-content:space-around;
    align-items: center
}
.click_icon{
    font-size:35px;
}
.popup_content{
      border-top-color: #e2e2e2;
      border-top-width:2px;
      border-bottom-color: #e2e2e2;
      border-bottom-width:1px;
}
.menu_div{
    width:750px;
    height:90px;
    padding-left:40px;
    padding-right:40px;
    flex-direction: row;
    justify-content: space-between;
    
}
.menu_div:active{
    background-color:#eee;
}
.menu_title{
    font-size: 30px;
    line-height: 90px;
    color: #888;
}
.menu_click_icon{
    font-size: 30px;
    line-height: 90px;   
    align-items: flex-end;
}
.theme{
    color: #329C92;
}
.bottom_border{
    margin-left:40px;
    border-bottom-color: #e2e2e2;
    border-bottom-width:1px; 
}
.redtheme {
  color: #f15a49;
}
</style>


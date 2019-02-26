<!--
    1.后期需更新weex-sdk之后才能实现下方标题栏随着键盘顶到上方
 -->
<template>
    <div class="wrapper">
        <minbar is-padding="true" :title="dayTime+'的日报'" @titleButtonClick="selectDate" :rightButton="true" @rightButtonClick="saveBtn">
            <text slot = 'other-title' :class="['iconfont','title-icon2',isIos()?'iostop':'']" ref="arrow" >&#xe690;</text>
            <text slot="right" class="save_btn">保存</text>
        </minbar>
        <list class="con_list">
            <cell>
                <textarea class="day_content" placeholder="记录一下今天的成果吧~" rows=50 :value="content" @input="editContent"> </textarea>
            </cell>
        </list>
        <div class="bottom">
            <div class="projects">
                <div v-if="items.length>0" class="scroller" >
                    <div v-for="(it,index) in items" :key="index" class="project_content"> 
                        <text class="p_name p_text">{{it.name}}</text>
                        <text class="iconfont ratio p_text" @click="changeProportion(index,it.id)">{{('\ue690 '+it.scale+"%")}}</text>
                        <text class="p_delete iconfont" @click="showDialog(index,it.id)">&#xe677;</text>
                    </div>
                </div>
            </div>
             <div class="bottom_menu">
                <div class="menu-left">
                    <text class="iconfont b_menu_icon">&#xe682;</text>
                    <text class="iconfont b_menu_icon">&#xe7ed;</text> 
                </div>
                <div class="menu-right">
                    <div  class="project_content2" @click="selectItem">  
                        <text style="color:#666666;font-size:28px;">添加项目</text>
                    </div>  
                </div>     
            </div>
        </div>
         <!-- 对话框组件 -->
        <x-dialog title="温馨提示"
                content="是否确定删除次项目?"
                confirm-text="确定"
                cancel-text="取消"
                overlay-can-close="false"
                :show="dialogShow"
                :single="false"
                top=550
                @cancelBtnClick="cancelBtnClick"
                @okBtnClick="deleteItem">
        </x-dialog>
        <x-load :show="loadinging" :type="'default'" :interval="0" ></x-load>
    </div>

</template>

<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core";
import SimpleData from "../../../common/js/SimpleData";
const navigator = weex.requireModule("navigator");
const storage = weex.requireModule('storage'); 
const picker = weex.requireModule('picker');
const addItem = new BroadcastChannel("addItem");
export default {
    components:{
        "minbar":require("../../../common/component/minbar.vue"),
        "x-dialog": require("../../../common/component/dialog.vue"),
        "x-load": require("../../../common/component/loading.vue"),
    },
    data(){
        return{
            mainId:'',
            dayTime:'',
            items:[],
            addItems:[],
            delItems:[],
            dialogShow: false,
            delItemId:'',
            itemsIndex:'',
            sDate:'',
            dayContent:null,
            itemContent:null,
            content:'',
            editType:'',
            loadinging:false,
            dayDataConfig:{
                queryAction:{
                    url:"/oa/queryDailyReportAction",
                    param:{
                        'dayTime':''
                    }
                },
                saveAction:{
                    url:"/oa/dailyReportSaveAction"
                },
                newAction:{
                    url:"/oa/dailyReportNewAction",
                    param:{
                        'dayTime':''
                    }
                }
            },
            itemDataConfig:{
                newAction: {
			        url: "/oa/dailyReportDetailNewAction"
		        },
                saveAction: {
                    url: "/oa/dailyReportDetailSaveAction"
                },
                queryAction: {
                    url: "/oa/dailyReportDetailQueryAction",
                    param: {
                        'masterId':"1"
                    }
                },
            },
            saveBoo:false
        }
    },
    
    created(){
        this.dayContent = SimpleData.creat('dayContent',this.dayDataConfig);
        this.itemContent = SimpleData.creat('itemContent',this.itemDataConfig);
        var s = this;
        /**获取选中的项目 */
        addItem.onmessage = function(event){
            var data = event.data; 
            if(s.items.length == 0){
                s.addItems = data;
            }else{
                for(var i =0 ;i<data.length;i++){
                    let  name = data[i].name;
                    let  boo = true;
                    for(var y = 0;y<s.items.length;y++){    
                        if(name == s.items[y].name){
                            boo = false;
                            break;
                        }
                    }
                    if(boo){
                         s.addItems.push(data[i]);   
                    }
                } 
            }
            if(s.addItems.length>0){
                s.newItem();
            }
        };
        var globalEvent = weex.requireModule('globalEvent') ;
        globalEvent.addEventListener("onPageInit", function (e) {
            var param = navigator.param();
            s.editType = param['editType'];
            s.dayTime = param['dayTime'];
            
            s.queryDay();
        });
    },
    methods:{
        queryDay(){
            var s = this;
            var dayData = s.dayContent;
            s.dayDataConfig.queryAction.param.dayTime = s.dayTime;
            s.dayDataConfig.newAction.param.dayTime = s.dayTime;
            dayData.clear();
            s.loadinging = true;
            dayData.refreshData(function(data,flag,msg){
                if(flag){
                    if(data.length >0){
                        dayData.selectRow(data[0]['id']);
                        s.content = dayData.getValue("content");
                        s.mainId = dayData.getValue("id");
                        s.queryItem(dayData.getValue("id"));
                    }else{
                        s.content = "";
                        dayData.newData(function(rowId,flag){   
                           if(flag){
                               dayData.selectRow(rowId);
                               s.mainId = dayData.getValue("id");
                               s.queryItem(dayData.getValue("id"));
                           }else{
                               s.loadinging = false;
                               asCore.toast("新建失败");
                           }
                        });
                    }
                }else{
                    s.loadinging = false;
                    asCore.toast('获取日报信息失败');
                }
            });

        },
        queryItem(mainId){
            var s = this;  
            var itemData = s.itemContent;
            s.itemDataConfig.queryAction.param.masterId = mainId;
            itemData.clear();
            itemData.refreshData(function(data,flag,msg){
                if(flag){
                    s.items = data;
                }else{
                    asCore.toast("获取项目信息失败");
                }
                s.loadinging = false;
            });
        },

        newItem(){
            var s = this;
            var itemData = s.itemContent;
            var dayData = s.dayContent;
            if(s.addItems.length>0){
                itemData.newData(function(rowId,flag){
                    if(!Array.isArray(rowId)){
                        itemData.setValue("name", s.addItems[0]["name"], rowId);
						itemData.setValue("code", s.addItems[0]["code"], rowId);
						itemData.setValue("pmName", s.addItems[0]["pmName"], rowId);
						itemData.setValue("pmCode", s.addItems[0]["pmCode"], rowId);
						itemData.setValue("scale", "100", rowId);
                        itemData.setValue("masterId", dayData.getCurrentID(), rowId);
                        s.items.push(itemData.getData(rowId));
                    }else{
                        for(var i =0;i<rowId.length;i++){
                            let rowid = rowId[i];
                            itemData.setValue("name", s.addItems[i]["name"], rowid);
                            itemData.setValue("code", s.addItems[i]["code"], rowid);
                            itemData.setValue("pmName", s.addItems[i]["pmName"], rowid);
                            itemData.setValue("pmCode", s.addItems[i]["pmCode"], rowid);
                            itemData.setValue("scale", "100", rowid);
                            itemData.setValue("masterId", dayData.getCurrentID(), rowid);
                            s.items.push(itemData.getData(rowid));
                        }
                    }
                    s.addItems = [];
                },s.addItems.length);
            }
        },
       
        selectItem(){
            navigator.push(asCore.localpath +"yunagileoa/app/dayReport/selectItem.js");
        },
        deleteItem(){
            var s = this;
            var itemData = s.itemContent;
            s.items.splice(s.itemsIndex,1);
            s.dialogShow = false;
            itemData.deleteByRowID(s.delItemId);
        },

        cancelBtnClick(){
            this.dialogShow = false;
        },
        showDialog(index,id){
            var s = this;
            s.itemsIndex = index;
            s.delItemId = id;
            s.dialogShow = true;
        },
        selectDate(){
            var s = this;
            picker.pickDate({
                value:s.dayTime
                }, event => {
                if (event.result === 'success') {
                    if(Utils.formatDate(new Date(event.data),'yyyy-MM-dd')<=Utils.formatDate(new Date(),'yyyy-MM-dd')){
                        s.sDate = event.data;
                        s.dayTime = Utils.formatDate(new Date(event.data),'yyyy-MM-dd');
                        s.$nextTick(()=>{
                            s.queryDay();
                        });
                        
                    }else{
                        asCore.toast("明天的日报还不能写哦");
                    }
                }
            })
        },

        editContent(e){
            this.content = e.value;
        },
        itemSave(){
            
            var s = this;
            var itemData = s.itemContent;
            itemData.save(function(flag){    
                if(flag){
                    s.saveBoo = true;
                    asCore.toast("保存成功");
                }else{
                    s.saveBoo = false;
                    asCore.toast("保存失败，项目数据保存错误");
                }
            });
            if(s.saveBoo){
                asCore.toast("保存成功");
                s.saveBoo = false;
            }
        },

        saveBtn(){
            var s = this;
            var dayData = s.dayContent;
            if(!s.content){
                asCore.toast("请填写日报内容!");
                return;
            }
            if(s.items.length<1){
                asCore.toast("请选择项目");
                return;
            }
            var scale = 0;
            for(var i=0;i<s.items.length;i++){
                scale += s.items[i]['scale'] * 1;
            }
            if(scale!=100){
                asCore.toast("项目占比总比例必须为100%");
                return;
            }
            dayData.setValue("content",s.content);
            dayData.setValue("remark",s.content);
            dayData.save(function(flag){
                if(flag){ 
                   s.saveBoo = true;
                   s.itemSave();
                }else{
                    s.saveBoo = false;
                    asCore.toast("保存失败,请稍后再试");
                }
            });
        },
        changeProportion(index,id) {
            var s = this;
            var itemData = s.itemContent;
            var modal = weex.requireModule('modal');
            modal.prompt({
                message: '修改项目占比',
                okTitle:"修改",
                cancelTitle:"取消"
            }, function (value) {
                var dataValue = value.data * 1;
                if(value.result!="取消"){
                    if(isNaN(dataValue)){
                        asCore.alert("请输入正确的数字");
                        return;
                    }
                    s.items[index]["scale"] = dataValue;
                    itemData.setValue("scale",dataValue,id);
                }
            });
        },
        isIos () {
            const { platform } = weex.config.env;
            return platform.toLowerCase() === 'ios';
        }
    }
  
}
</script>

<style scoped>
.wrapper {
    justify-content: space-between;
}
.iconfont {
  font-family: iconfont;
}
.title-icon {
    font-size:35px;
    color:#fff;
    font-weight: 300;
}
.save_btn{
    font-size:32px;
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
.con_list {
    flex: 1;
}
.day_content{
    width:750px;
    height: 1000px;
    padding-left:20px;
    padding-right:20px; 
    padding-top:20px;
    font-size:34px;
    color:#555555;
}
.bottom{
    flex: 0;
    justify-content: flex-end;
}
.bottom_menu{
    width:750px;
    height:90px;
    border-top-color: #ddd;
    border-top-width:1px;
    flex-direction: row;
    padding-left:20px;
    align-items: center;
}
.menu-left {
    flex-direction: row;
    flex: 1;
}
.menu-right {
    flex-direction: row;
    justify-content: flex-end;
    flex: 1;
}
.b_menu_icon{
    font-size:40px;
    margin-right:30px;
    color:#7A7A7A
}
.projects{
    width:750px;
    padding-left:20px;
    padding-right:20px;
    padding-bottom:16px;
}
.project_content{
    width:710px;
    height:65px;
    background-color:#f7f7f7;
    border-top-color:#e2e2e2;
    border-top-width:1px;
    border-bottom-color:#e2e2e2;
    border-bottom-width:1px;
    border-left-color:#e2e2e2;
    border-left-width:1px;
    border-right-color:#e2e2e2;
    border-right-width:1px;
    border-radius: 5px;
    flex-direction: row;
    justify-content:center;
    align-items: center;
    padding-left:10px;
    padding-right:20px;
    margin-top:10px;
}
.project_content2{
    height:89px;
    background-color:#ffffff;
    justify-content: center;
    align-items: flex-end;
    padding-left:40px;
    padding-right:40px;
    border-left-width: 1px;
    border-left-color: #ddd;
}
.project_content2:active{
    background-color:#e2e2e2;
}
.p_text{
    font-size:26px;
    color:#47A9F1;
}
.ratio{
    width:120px;
    height:65px;
    line-height: 65px;
    align-items: center;
    justify-content: center;
    padding-left: 10px;
    padding-right:10px;
}
.ratio:active {
    background-color: #eeeeee;
}
.p_name{
    width:510px;
}
.p_delete{
    width: 50px;
    font-size: 35px;
    color:#999;
    text-align: right;
}
.iostop {
    margin-top:32px;
}
.scroller {
    width: 750px;
    flex-direction: column-reverse;
}
</style>




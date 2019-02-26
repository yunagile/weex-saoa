<template>
    <div class="wrapper" style="background-color:#edeeee; overflow: auto;">
        <minbar title="周报发布" :isPadding="true"> 
            <text slot="right" class="iconfont title-icon" @click="selectDate">&#xe6e2;</text>
        </minbar>  
        <list style="padding-bottom:20px"> 
            <cell>
        <div style="background-color: white"> 
            <div class="x-row" style="margin-top:30px;justify-content:center;width:750px">   
                <div class="btn_left" @click="frsitWeek()">
                    <text class="btn-text">上一周</text>
                </div>
                <div class="btn_center" @click="week()">
                    <text class="btn-text">本周</text>
                </div>
                <div class="btn_right" @click="nextWeek">
                    <text class="btn-text">下一周</text>
                </div>
            </div>
            <div class="content_c">
                <div class="user_div">
                    <image  class="head" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+createPsnCode" />
                    <div>
                        <text  class="content_text">{{title}}</text>
                        <div style="flex-direction:row;width:600px">
                            <text class="content_bottom_text">[{{weekStart}}~{{weekEnd}}]</text> 
                            <!--<text class="content_bottom_right_text">本周日报</text> -->
                        </div>
                    </div>
                </div>
            </div> 
        </div>
        <div style="margin-top:20px;background-color: white;padding:20px">
            <div  class="x-row" style="padding:10px;width:750px;" >
                <div slot="label" class="list-icon" style="background-color:#ff5a65;">
                     <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe693;</text>
                </div>
                <text  style="line-height:50px">本周工作成效</text> 
                <text v-if="edit" @click="selectItem(1)"  style="color:#666666;font-size:24px;line-height:50px;position: absolute;right:40px;">添加项目</text>
            </div>
            <div  class="x-row">
                <list>  
                    <cell  v-for="(c,index) in workCaseRows" :key="index">       
                        <div style="border-top-width: 1px;border-color:#ddd;width:750px;padding:10px">
                            <div class="x-row">
                                <text  class="content_text" style="flex:1;">{{c.name}}</text>
                                <text v-if="edit"  class="content_text" style="color:red;width:100px" @click="deleteWorkCaseData(c.id)">删除</text>
                            </div> 
                            <text v-if="edit==false" class="content_bottom_text">{{c.content}}</text> 
                            <textarea  v-if="edit" class="day_content content_bottom_text" rows=50 :value="c.content" @input="workCaseInput($event,c.id)"></textarea>  
                        </div>  
                    </cell>     
                </list>
            </div>
        </div>
        <div style="margin-top:20px;background-color: white;padding:20px">
            <div  class="x-row" style="padding:10px;width:750px;border-bottom-width: 1px;border-color:#ddd " >
                <div slot="label" class="list-icon" style="background-color:#00d28b;">
                     <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe6ab;</text>
                </div>
                <text  style="line-height:50px">总结心得</text>  
            </div> 
            <div  class="x-row">
                <div style="padding:10px">
                    <text v-if="edit==false" style="line-height:50px;line-height:100px">{{conclusion}}</text> 
                    <textarea  v-if="edit" class="day_content" rows=50 :value="conclusion?conclusion:''" @input="editConclusion($event)"></textarea>
                </div>    
            </div>
            
        </div>
        <div style="margin-top:20px;background-color: white;padding:20px">
            <div  class="x-row" style="padding:10px;width:750px;" >
                <div slot="label" class="list-icon" style="background-color:#22b8fd;">
                     <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe653;</text>
                </div>
                <text  style="line-height:50px">下周工作计划</text> 
                <text v-if="edit" @click="selectItem(2)"  style="color:#666666;font-size:24px;line-height:50px;position: absolute;right:40px;">添加项目</text>
            </div>
            <div  class="x-row">
                <list>
                    <cell v-for="(c,index) in workPlanRows" :key="index">       
                        <div style="border-top-width: 1px;border-color:#ddd;width:750px;padding:10px">
                            <div class="x-row">
                                <text  class="content_text" style="flex:1;">{{c.name}}</text>
                                <text v-if="edit"  class="content_text" style="color:red;width:100px" @click="deleteWorkPlanData(c.id)">删除</text>
                            </div> 
                            <text v-if="edit==false" class="content_bottom_text">{{c.content}}</text> 
                            <textarea  v-if="edit" class="day_content content_bottom_text" rows=50 :value="c.content" @input="workPlanInput($event,c.id)"></textarea> 
                        </div>  
                    </cell>     
                </list> 
            </div>
        </div>
        <div style="margin-top:20px;background-color: white;padding:10px">
            <div  class="x-row" style="width:750px;padding:20px;border-bottom-width: 1px;border-color:#ddd" >
                 <text style="color:#666666;" class="content_text">点评人 {{handlePerson}}</text>
                 <text v-if="edit" @click="addPerson(3)"  style="color:#666666;font-size:24px;line-height:50px;position: absolute;right:40px;">添加点评人</text>
            </div> 
             <div  class="x-row" style="width:750px;padding:20px" >
                <text style="color:#666666;" class="content_text">抄送人 {{copyPerson}}</text>
                <text v-if="edit" @click="addPerson(4)"  style="color:#666666;font-size:24px;line-height:50px;position: absolute;right:40px;">添加抄送人</text>
            </div> 
        </div>
        <div v-if="edit" class="btnDiv">
            <div class="saveBtn" @click="dataSave()">
                <text class="btn_text">保存</text>    
            </div>  
            <div class="submitBtn" @click="sumbit()">
                <text class="btn_text">提交</text>    
            </div>   
        </div>  
            </cell>
        </list>
        <!-- 右侧的弹出菜单 -->
        <x-popup :show="rightMenu" pos="right"  width="600"   @wxcPopupOverlayClicked="closeRightMenu">
            <div slot="popup_content" >
                <div style="border-bottom-width:1px;border-bottom-color:#ddd; "><text class="rightMenu_title">时间设置</text></div>
                <div style="padding:10px">
                    <text style="color:#666666;font-size:26px">时间设置</text>
                    <div style="flex-direction: row;justify-content:space-between;padding-right:20px;margin-top:10px">
                        <button  class="x-fl-button">年份</button>     
                    </div>
                             
                </div>
            </div> 
        </x-popup>
        <wx-load :show="loadinging"
                 :type="'default'"
                 :loading-text="'加载中'"
                 :interval="0" ></wx-load>  
    </div>   
</template>
<script>
import minbar from "../../../common/component/minbar.vue" 
import cell from "../../../common/component/cell.vue" 
import asCore from "../../../common/js/core"; 
import Utils from "../../../common/js/utils";
import SimpleData from "../../../common/js/SimpleData";
import popup from "../../../common/component/popup.vue";
import load from "../../../common/component/loading.vue";
const navigator = weex.requireModule("navigator");
const addItem = new BroadcastChannel("addItem");
export default {
    data(){
        return {
             createPsnCode:"",
             bsessionid:"",
             currentWeekInfo:"",
             factWeekInfo:"",
             formData:"",
             workCaseData:"",
             workCaseRows:[],
             workPlanData:"",
             workPlanRows:[],
             rootPath:"",
             title:"" , 
             weekStart:"",
             weekEnd:"",
             conclusion:"",
             handlePerson:"",
             copyPerson:"",
             edit:true,
             selectType:"1",
             rightMenu: false,
             loadinging:false,
             config:{   
                 newAction : {
                        url : "/oa/weekReportNewAction",
                        param : {
                            year : function() {
                                return "";
                            },
                            month : function() {
                                return "";
                            },
                            week : function() {
                                return "";
                            }
                        }
                    },
                    saveAction : {
                        url : "/oa/weekReportSaveAction"
                    },
                    queryAction : {
                        url : "/oa/weekReportQueryAction",
                        param : {
                            year : function() {
                                return "";
                            },
                            month : function() {
                                return "";
                            },
                            week : function() {
                                return "";
                            }
                        }
                    }, 
             },
             workCaseCofig:{
                newAction : {
                    url : "/oa/workCaseNewAction"
                },
                saveAction : {
                    url : "/oa/workCaseSaveAction"
                },
                queryAction : {
                    url : "/oa/workCaseQueryAction",
                    param : {
                        masterId : function() {
                            return "";
                        }
                    }
                } 
             },
             workPlanCofig:{
                newAction : {
				    url : "/oa/workPlanNewAction"
			    },
			    saveAction : {
				    url : "/oa/workPlanSaveAction"
			    },
			    queryAction : {
				    url : "/oa/workPlanQueryAction",
				    param : {
					    masterId : function() {
						    return "";
					    }
				    }
			    },
             }
        }
    },
    components:{
        "minbar":minbar,
        "wx-cell" : cell,
        "x-popup": popup,
        'wx-load':load
    }, 
    created () {    
        var _this=this;
        addItem.onmessage = function(event){
            var data = event.data;     
            if(_this.selectType==1){  
                var tWorkCaseRows= _this.workCaseRows; 
                var tI=[];
                for(var i in data){   
                    var t=true; 
                    for(var j in tWorkCaseRows)  {
                        if(data[i].code==tWorkCaseRows[j].code){
                            t=false;
                        }
                    }
                    if(t){ 
                        tI.push(i);
                    }  
                }   
                if(tI.length==0){
                    return;
                }
                _this.workCaseData.newData(function(rowId,flag){   
                    if(flag){
                        var rowid; 
                        var i=tI[0];
                        if(rowId instanceof Array==false){  
                            rowid = rowId; 
                            _this.workCaseData.setValue("name", data[i].name, rowId);
                            _this.workCaseData.setValue("code", data[i].code, rowId);
                            _this.workCaseData.setValue("pmName", data[i].pmName, rowId);
                            _this.workCaseData.setValue("pmCode", data[i].pmCode, rowId);
                            _this.workCaseData.setValue("createTime", data[i].createTime, rowId);
                            _this.workCaseData.setValue("masterId", _this.workCaseCofig.queryAction.param.masterId, rowId);
                            _this.workCaseRows.push({id:rowId,"name":data[i].name,code:data[i].code,pmName:data[i].pmName,pmCode:data[i].pmCode,createTime:data[i].createTime,masterId:data[i].masterId});
                        }else{ 
                            for (var i in rowId) {  
                                rowid = rowId[i];  
                                _this.workCaseData.setValue("name", data[tI[i]].name, rowid);
                                _this.workCaseData.setValue("code", data[tI[i]].code, rowid);
                                _this.workCaseData.setValue("pmName", data[tI[i]].pmName, rowid);
                                _this.workCaseData.setValue("pmCode", data[tI[i]].pmCode, rowid);
                                _this.workCaseData.setValue("createTime", data[tI[i]].createTime, rowid);
                                _this.workCaseData.setValue("content", data[tI[i]].content, rowid);
                                _this.workCaseData.setValue("masterId", _this.workCaseCofig.queryAction.param.masterId, rowid);  
                                _this.workCaseRows.push({id:rowid,code:data[tI[i]].code,"name":data[tI[i]].name,content:data[tI[i]].content}); 
                            } 
                        }  
                    }else{
                        asCore.toast("新建失败");
                    }
                },tI.length); 
            }else if(_this.selectType==2){
                var tWorkPlanRows= _this.workPlanRows; 
                var tI=[];
                for(var i in data){   
                    var t=true; 
                    for(var j in tWorkPlanRows)  {
                        if(data[i].code==tWorkPlanRows[j].code){
                            t=false;
                        }
                    }
                    if(t){ 
                        tI.push(i);
                    }  
                }   
                if(tI.length==0){
                    return;
                } 
                _this.workPlanData.newData(function(rowId,flag){   
                    if(flag){
                        var rowid; 
                        var i=tI[0];
                        if(rowId instanceof Array==false){
                            rowid = rowId; 
                            _this.workPlanData.setValue("name", data[i].name, rowId);
                            _this.workPlanData.setValue("code", data[i].code, rowId);
                            _this.workPlanData.setValue("pmName", data[i].pmName, rowId);
                            _this.workPlanData.setValue("pmCode", data[i].pmCode, rowId);
                            _this.workPlanData.setValue("createTime", data[i].createTime, rowId);
                            _this.workPlanData.setValue("masterId", _this.workCaseCofig.queryAction.param.masterId, rowId);
                            _this.workPlanRows.push({id:rowId,"name":data[i].name,code:data[i].code,pmName:data[i].pmName,pmCode:data[i].pmCode,createTime:data[i].createTime,masterId:data[i].masterId});
                        }else{
                            for (var i in rowId) {  
                                rowid = rowId[i];  
                                _this.workPlanData.setValue("name", data[tI[i]].name, rowid);
                                _this.workPlanData.setValue("code", data[tI[i]].code, rowid);
                                _this.workPlanData.setValue("pmName", data[tI[i]].pmName, rowid);
                                _this.workPlanData.setValue("pmCode", data[tI[i]].pmCode, rowid);
                                _this.workPlanData.setValue("createTime", data[tI[i]].createTime, rowid);
                                _this.workPlanData.setValue("content", data[tI[i]].content, rowid);
                                _this.workPlanData.setValue("masterId", _this.workCaseCofig.queryAction.param.masterId, rowid); 
                                _this.workPlanRows.push({id:rowid,code:data[tI[i]].code,"name":data[tI[i]].name,content:data[tI[i]].content}); 
                            } 
                        } 
                    }else{
                        asCore.toast("新建失败");
                    }
                },tI.length); 
            }else if(_this.selectType==3){
                if (data.length > 1) {
                    asCore.toast("只能选择一位点评人");
                    return;
                } 
                var weekData=_this.formData;   
                weekData.setValue("handlePersonId", data[0].code);
                weekData.setValue("handlePerson", data[0].name);  
                _this.handlePerson=data[0].name;
            }else if(_this.selectType==4){ 
                var copyPIds=""; 
                var copyPNames="";
                for(var i in data){   
                    if(i==0){
                        copyPIds=data[i].code;
                        copyPNames=data[i].name;
                    }else{
                        copyPIds+=","+data[i].code;
                        copyPNames+=","+data[i].name;
                    } 
                }
                var weekData=_this.formData;   
                weekData.setValue("copyPersonId", copyPIds);
                weekData.setValue("copyPerson", copyPNames);  
                _this.copyPerson=copyPNames;     
            }
        } 
        _this.formData = SimpleData.creat("center", _this.config); 
        _this.workCaseData = SimpleData.creat("workCase", _this.workCaseCofig); 
		_this.workPlanData = SimpleData.creat("workPlan", _this.workPlanCofig);
        _this.getPath();
        _this.getCurrentWeek();   
    },
    methods:{ 
        workPlanInput(event,id){ 
            this.workPlanData.setValue("content",event.value,id);
        },
        workCaseInput(event,id){ 
            this.workCaseData.setValue("content",event.value,id);
        }, 
        editConclusion(event){
            this.conclusion=event.value;
        },
        loadData(){    
            var _this=this;
            _this.loadinging = true;
            var weekData=_this.formData; 
            this.config.queryAction.param.week = this.config.queryAction.param.week;    
            _this.workCaseRows=[];
            _this.workPlanRows=[];
            weekData.clear();
            _this.workCaseData.clear();
            _this.workPlanData.clear();
		    weekData.refreshData(function(data, flag, mes) {   
			        if (flag === true) { 
                         if(data.length >0){     
                            var rowId=data[0]['id']; 
                            weekData.selectRow(rowId);  
                            _this.title=weekData.getValue("title");  
                            _this.weekStart=Utils.formatDate(new Date(weekData.getValue("weekStart")),'MM-dd');  
                            _this.weekEnd=Utils.formatDate(new Date(weekData.getValue("weekEnd")),'MM-dd');  
                            _this.createPsnCode=weekData.getValue("createPsnId");   
                            _this.conclusion=weekData.getValue("conclusion");  
                            _this.handlePerson=weekData.getValue("handlePerson");  
                            _this.copyPerson=weekData.getValue("copyPerson");  
                            _this.workCaseCofig.queryAction.param.masterId=rowId;  
                            if(weekData.getValue("stateCode")=="1"){
                                _this.edit=false;
                            }else{
                                _this.edit=true;
                            }
                            _this.workCaseData.refreshData(function(data, flag, mes) {   
                                 if (flag === true) {  
                                      if(data.length >0){     
                                           var rows=data; 
                                           for(var i=0;i<rows.length;i++){  
                                            _this.workCaseRows.push({id:rows[i].id,"name":rows[i].name,code:rows[i].code,content:rows[i].content,pmName:rows[i].pmName,pmCode:rows[i].pmCode,createTime:rows[i].createTime,masterId:rows[i].masterId}); 
                                          }     
                                      }
                                    }  
                                }
                            );
                            _this.workPlanCofig.queryAction.param.masterId=rowId;
                            _this.workPlanData.refreshData(function(data, flag, mes) {   
                                 if (flag === true) { 
                                      if(data.length >0){     
                                          var rows=data;
                                           for(var i=0;i<rows.length;i++){ 
                                                _this.workPlanRows.push({id:rows[i].id,"name":rows[i].name,code:rows[i].code,content:rows[i].content,pmName:rows[i].pmName,pmCode:rows[i].pmCode,createTime:rows[i].createTime,masterId:rows[i].masterId});  
                                           }     
                                       }
                                    }  
                                }
                            ); 
                            _this.loadinging=false;
                        }else{
                            _this.config.newAction.param.year = _this.config.queryAction.param.year;
                            _this.config.newAction.param.month = _this.config.queryAction.param.month;
                            _this.config.newAction.param.week = _this.config.queryAction.param.week;
                            weekData.newData(function(rowid, flag, mes) { 
                                    if (flag === true) {
                                        _this.edit=true;
                                        weekData.selectRow(rowid);   
                                        var weekId=rowid;
                                        asCore.post("/oa/lastWeekPlansQueryAction",  _this.config.queryAction.param, function(data, status) { 
                                            if (status == true) {
                                                var arr = eval("(" + data.data.data + ")");
                                                var rows = arr.rows; 
                                                if(rows.length>0){ 
                                                    _this.workCaseData.newData(function(rowId,flag){   
                                                        if(flag){
                                                            var rowid; 
                                                            if(!rowid instanceof Array){
                                                                    rowid = rowId;
                                                                    _this.workCaseData.setValue("name", rows[i].name, rowid);
                                                                    _this.workCaseData.setValue("code", rows[i].code, rowid);
                                                                    _this.workCaseData.setValue("pmName", rows[i].pmName, rowid);
                                                                    _this.workCaseData.setValue("pmCode", rows[i].pmCode, rowid);
                                                                    _this.workCaseData.setValue("createTime", rows[i].createTime, rowid);
                                                                    _this.workCaseData.setValue("content", rows[i].content, rowid);
                                                                    _this.workCaseData.setValue("masterId",weekId, rowid); 
                                                                    _this.workCaseRows.push({id:rowid,code:rows[i].code,"name":rows[i].name,content:rows[i].content});
                                                            }else{
                                                                for (var i in rowId) {
                                                                    rowid = rowId[i];
                                                                    _this.workCaseData.setValue("name", rows[i].name, rowid);
                                                                    _this.workCaseData.setValue("code", rows[i].code, rowid);
                                                                    _this.workCaseData.setValue("pmName", rows[i].pmName, rowid);
                                                                    _this.workCaseData.setValue("pmCode", rows[i].pmCode, rowid);
                                                                    _this.workCaseData.setValue("createTime", rows[i].createTime, rowid);
                                                                    _this.workCaseData.setValue("content", rows[i].content, rowid);
                                                                    _this.workCaseData.setValue("masterId",weekId, rowid); 
                                                                    _this.workCaseRows.push({id:rowid,code:rows[i].code,"name":rows[i].name,content:rows[i].content}); 
                                                                } 
                                                            }
                                                            
                                                        }else{
                                                            asCore.toast("新建失败");
                                                        }
                                                    }, rows.length);  
                                                }
                                            }
                                        });
                                        _this.title=weekData.getValue("title");  
                                        _this.weekStart=Utils.formatDate(new Date(weekData.getValue("weekStart")),'MM-dd');  
                                        _this.weekEnd=Utils.formatDate(new Date(weekData.getValue("weekEnd")),'MM-dd');  
                                        _this.createPsnCode=weekData.getValue("createPsnId");   
                                        _this.conclusion=weekData.getValue("conclusion");     
                                        _this.loadinging=false;
                                    } else { 
                                        asCore.toast(mes);
                                        _this.week();
                                        _this.loadinging=false;
                                    }
                                }
                            );
                        }  
                    }
                }
            );
        },
        getCurrentWeek(){  
            var _this=this;
            asCore.post("/oa/queryCurrentWeeksAction", null, function(data, flag, mes) {  
                if (flag == true) { 
                        //当前选择的时间数据
                        _this.currentWeekInfo = data; 
                        //实际当前周的时间数据
                        _this.factWeekInfo = data;     
                        var year = {
                            cYear : _this.currentWeekInfo.data.cYear,
				    		years : []
                        };
                        for (var i = 0; i < 5; i++) {
                            year.years.push({
                                val : _this.currentWeekInfo.data.cYear - i
                            });
				        }
				        // 加载年份按钮
                        _this.setYear(year); 
                        _this.setMonth();
                        // 加载周别按钮
                        _this.setWeek();  
                        _this.loadData();
                    } else { 
                        asCore.toast(mes);
                    }
                }
            );
        },
        frsitWeek(){
            var _this=this;
            var year=_this.config.queryAction.param.year;
            var month=_this.config.queryAction.param.month;  
            var week=_this.config.queryAction.param.week;  
            var monthData=_this.currentWeekInfo.data.timeline;
            if(week==1){
                month=month-1; 
                for(var i in monthData){
                    if(month==monthData[i].num){
                         week=monthData[i].ws[monthData[i].ws.length-1].num;
                    }
                }
            }else{
                week=week-1;
            }
            _this.config.queryAction.param.month=month;
            _this.config.queryAction.param.week=week;
            _this.loadData();
        },
        week(){  
            this.getCurrentWeek();   
        },
        nextWeek(){
            var _this=this;
            var year=_this.config.queryAction.param.year;
            var month=_this.config.queryAction.param.month;  
            var week=_this.config.queryAction.param.week;  
            var monthData=_this.currentWeekInfo.data.timeline;
            var tWeek="";
            for(var i in monthData){
                if(month==monthData[i].num){
                    tWeek=monthData[i].ws[monthData[i].ws.length-1].num;
                }
            } 
            if(week==tWeek){
                month=month+1; 
                for(var i in monthData){
                    if(month==monthData[i].num){
                         week=1;
                    }
                }
            }else{
                week=week+1;
            }
            _this.config.queryAction.param.month=month;
            _this.config.queryAction.param.week=week;
            _this.loadData();
        },
        setYear(year){ 
            this.config.queryAction.param.year =year.cYear; 
        },
        setMonth(){
            var cw = this.currentWeekInfo.data.cw; 
            var months = this.currentWeekInfo.data.timeline;  
		    if (cw) {
                this.config.queryAction.param.month = cw.mnum;   
		    } 
        },
        setWeek(){ 
            var cw = this.currentWeekInfo.data.cw; 
            var cW = "";  
            if (cw) {
                if (cW) { 
                    this.config.queryAction.param.week = cW; 
                } else { 
                    this.config.queryAction.param.week = cw.wnum;  
                }
            }
        },
        getPath() { 
            var self = this; 
            asCore.getBsessionid(bid => {
                if (bid) {
                    self.bsessionid = bid;
                } else {
                    
                    }
            });
            self.rootPath = asCore.rootPath; 
        },
        selectItem(type){
            this.selectType=type;
            navigator.push(asCore.localpath +"/yunagileoa/app/dayReport/selectItem.js");
        }, 
        addPerson(type){
            this.selectType=type; 
            navigator.pushParam(asCore.localpath + "yunagileoa/app/weekReport/selectPerson.js",{
                type:type 
            });
        },
        dataSave(){
            var _this=this;
            var weekData=_this.formData;    
            var caseNum = _this.workCaseData.getCount();
            if(caseNum==0){
                asCore.toast("请添加本周工作成效");
                return;
            }    
            if(!_this.conclusion){
                asCore.toast("请填写总结心得"); 
			    return;
            }
            weekData.setValue("conclusion",_this.conclusion)
            var workPlanNum = _this.workPlanData.getCount();
            if(workPlanNum==0){
                asCore.toast("请添加下周工作计划项目");
                return;
            } 
           
            weekData.save(function(flag){    
                if(flag){ 
                   asCore.toast("保存成功");
                }else{ 
                    asCore.toast("周报信息保存失败");
                }
            });
            _this.workCaseData.save(function(flag){    
                    if(flag){  
                        asCore.toast("保存成功");
                    }else{ 
                        asCore.toast("保存失败");
                    }
                }
            );  
            
            _this.workPlanData.save(function(flag){    
                    if(flag){  
                        asCore.toast("保存成功");
                    }else{ 
                        asCore.toast("保存失败");
                    }
                }
            ); 
        },
        sumbit(){
            var _this=this;
            var weekData=_this.formData;     
            var caseNum = _this.workCaseData.getCount();
            if(caseNum==0){
                asCore.toast("请添加本周工作项目");
                return;
            }    
            if(!_this.conclusion){
                asCore.toast("请填写总结心得"); 
			    return;
            }
            var workPlanNum = _this.workPlanData.getCount();
            if(workPlanNum==0){
                asCore.toast("请添加下周工作计划项目");
                return;
            } 
            weekData.setValue("stateCode", "1");
		    weekData.setValue("stateName", "已提交"); 
            weekData.save(function(flag){    
                if(flag){ 
                   asCore.toast("提交成功");
                }else{ 
                    asCore.toast("周报信息提交失败");
                }
            });
        },
        deleteWorkCaseData(id){
            var _this=this;
            _this.workCaseData.deleteByRowID(id); 
            var arr=_this.workCaseRows;
            var i=arr.length; 
            while(i--){
                if(arr[i].id==id){
                    arr.splice(i,1)
                }
            } 
        },
        deleteWorkPlanData(id){
            var _this=this;
            _this.workPlanData.deleteByRowID(id); 
            var arr=_this.workPlanRows;
            var i=arr.length; 
            while(i--){
                if(arr[i].id==id){
                    arr.splice(i,1)
                }
            } 
        },
        selectDate() { 
            this.rightMenu = true;
        },
        closeRightMenu() {
            this.rightMenu = false;
        },
    } 
}
</script>
<style scoped lang="scss">  
.day_content{
    width:750px;
    background-color:#fff;  
    height:100px;
    font-size:26px;
}
.scroller {
    z-index: 102;
   
 }
 .list-icon {
    height: 65px;
    width: 65px;
    border-radius: 10px;
    margin-right:20px;
    align-items: center;
    justify-content: center; 
}
.rightMenu_title {
  color: #666666;
  line-height: 100px;
  width: 600px;
  text-align: center;
  font-size: 26px;
}
.x-row{ 
    flex-direction: row; 
    width:750px;
}     
.btn_left {
  align-items: center;
  justify-content: center;
  background-color: white;   
  border-style: solid;
  border-color: #33a0d6;
  border-top-width: 1px;
  border-left-width: 1px;
  border-bottom-width: 1px;  
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  padding: 0px 10px;
  height: 70px;
  width: 140px;
}
.btn_center {
  align-items: center;
  justify-content: center;
  background-color: white;   
  border-style: solid;
  border-color: #33a0d6;
  border-width: 1px; 
  padding: 0px 10px;
  height: 70px;
  width: 200px;
} 
.btn_right {
  align-items: center;
  justify-content: center;
  background-color: white;   
  border-style: solid;
  border-color: #33a0d6; 
  border-top-width: 1px;
  border-right-width: 1px;
  border-bottom-width: 1px;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  padding: 0px 10px;
  height: 70px;
  width: 140px;
} 
.btn_left:active {
	background-color: #33a0d6;
	color: #ffffff;
	text-decoration: none;
}
.btn_center:active {
	background-color: #33a0d6;
	color: #ffffff;
	text-decoration: none;
}
.btn_right:active {
	background-color: #33a0d6;
	color: #ffffff;
	text-decoration: none;
}
.btn-text {
  color: #33a0d6;
  font-size: 30px;
}
.user_div{ 
    height:80px;
    flex-direction: row;
    justify-content:flex-start;  
    margin: 30px;
    border: 1px solid #ddd;
}
.head{
    width:80px;
    height:80px;
    border-radius: 40px;
    margin-right:15px;  
    float: right;
} 
.content_text{
    line-height: 40px;
    font-size:26px;
}
.content_bottom_text{
    line-height: 40px;
    color:#999999;
    font-size:22px;
}
.content_bottom_right_text{
    line-height: 40px;
    color:#33a0d6;
    font-size:22px; 
    position:absolute;
    right:10px;   
}
.iconfont {
    font-family:iconfont;
}
.title-icon {
  font-size: 35px;
  color: #fff;
  font-weight: 300;
}
.cell-icon {
    color:#ffffff;
}
 .list-icon {
    height: 60px;
    width: 60px;
    border-radius: 50%;
    margin-right:20px;
    align-items: center;
    justify-content: center;
} 

.saveBtn{ 
    height:80px;
    background-color:#7D7D7D;
    border-radius:8px;  
    line-height:80px;
    flex: 1;
}

.btnDiv{
    flex-direction: row;
    padding: 20px;
}
.submitBtn{ 
    height:80px;
    background-color:#33a0d6;
    border-radius:8px;  
    line-height:80px;
    flex: 1;
    margin-left: 10px;
}

.submitBtn:active{
    background-color:#7D7D7D; 
}

.btn_text{
    font-size: 32px;
    line-height: 80px;
    color:#fff;
    text-align: center;
}
</style>
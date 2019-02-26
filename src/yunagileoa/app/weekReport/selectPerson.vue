    <template>
            <div class="wrapper" style="background-color:#edeeee">
                <minbar :title="title" :isPadding="true"> 
                    <text slot="right" class=" title-icon" @click="addItem">添加({{selectP.length}})</text>
                </minbar>  
                <div>
                    <searchbar ref="searchbar"  @wxcSearchbarInputOnInput="onInput"></searchbar>
                </div>
                <scroller class="scroller" loadmoreoffset="1" @loadmore="getData('load')">
                    <div class="content">
                        <div v-for="(p,index) in projects" :key="index" class="item" @click="clickBox(index)">
                            <div class="item_title" >
                                <text class="title_text">{{p.name}}</text>
                                <text class="lable_text">{{p.fullName}}</text>
                            </div>
                            <div class="item_click" v-if="p.click">
                                <text class="iconfont click_icon">&#xe6a3;</text>
                            </div>
                        </div> 
                    </div>
                </scroller>  
                <wx-load :show="loadinging"
                 :type="'default'"
                 :loading-text="'加载中'"
                 :interval="0" ></wx-load>  
            </div>  
        </template>
    <script>
    import minbar from "../../../common/component/minbar.vue"
    import searchbar from "../../../common/component/searchbar.vue";
    import Utils from "../../../common/js/utils";
    import asCore from "../../../common/js/core";
    import popup from "../../../common/component/popup.vue";
    import load from "../../../common/component/loading.vue";
    const storage = weex.requireModule('storage'); 
    const navigator = weex.requireModule("navigator");
    export default {
    data(){
        return {
            arr:{},
            projects:[], 
            aProjects:[],
            param:{
                keywords : '',
                psm : 1,
                isInformation:'1',
                code:"",
                page:1,
                rows: 8,
            },
            theme:'theme1',
            showMenu:false,
            title:'选择点评人',
            search:'',
            loadinging:false,
            selectP:[]
        }
    },
    components:{
        "minbar":minbar,
        "searchbar": searchbar,
        "wx-popup": popup,
        'wx-load':load
    },
    beforeCreate(){
        var s = this;
        var globalEvent = weex.requireModule('globalEvent') ;
        globalEvent.addEventListener("onPageInit", function (e) {
            var param = navigator.param();
            var type = param['type']; 
            if(type=="3"){
                s.title="选择点评人"
            }else{
                s.title="选择抄送人"
            }
        });
    },
    created () {  
         this.getData();
    },
    methods:{   
          getData(type){
            var self = this;
            self.loadinging = true;
            if(type == 'load'){
                self.param.rows = self.param.rows+8; 
                self.param.page += 1;
            }     
            asCore.post('/org/loadOrgAndChildAction1',self.param,(data,flag)=>{ 
                if(flag==true){ 
                    var rows = data.data; 
                    var parrs = []; 
                    var arrs = {};  
                    if(rows.length>0){   
                        for(var i = 0;i<rows.length;i++){
                            if(rows[i].kind=="psm"){
                                let click = false; 
                                /**判断是否被选中 */
                                if(self.selectP.length>0){
                                    click = this.isClick(rows[i].id);   
                                } 
                                parrs.push({
                                    'id':rows[i].id,
                                    'code':rows[i].code, 
                                    'name':rows[i].name,
                                    'fullName':rows[i].fullName,
                                    'fullCode':rows[i].fullCode, 
                                    'click':click 
                                });
                                arrs[i] = false; 
                            }
                            
                        }
                    }
                    self.projects = parrs;
                    self.aProjects=parrs;
                    if(type != 'load'){
                        self.arr = arrs;
                    } 
                    self.loadinging = false;
                }else{
                    asCore.toast('获取项目信息失败！');
                }
            });
          },
         isClick(id){
            var self = this;
            var click = false;
            for(var y = 0;y < self.selectP.length;y++){
                if(id == self.selectP[y].id){
                    
                    click = true;
                    break;
                }
            }
            return click;
        },

        clickBox(e){
            var self = this;
            if(!self.projects[e].click){
               self.projects[e].click=true;
               self.selectP.push(self.projects[e]);

            }else{
                self.projects[e].click = false;
                for(var i = 0;i<self.selectP.length;i++){
                    if(self.selectP[i].id == self.projects[e].id){
                        self.selectP.splice(i,1);
                        break;
                    }
                }
            }
            
        },
        addItem(){
            var self = this;

        
            if(self.selectP.length>0){
                const addItem = new BroadcastChannel('addItem');
                addItem.postMessage(self.selectP);
                navigator.back();
            }else{
                asCore.toast("请选择项目");
            }
        },
        onInput(event){
            var self = this;
            var val=event.value; 
            self.projects=self.aProjects.filter(function (x) {     
                var name=x.name; 
                if(name.indexOf(val)>-1){ 
                    return x;
                }  
            });
        }

        } 
    }
    </script>
    <style scoped lang="scss">    
    .iconfont {
  font-family: iconfont;
}
.title-icon {
    font-size:30px;
    color:#fff;
    font-weight: 300;
}

.title-icon2{
    color:#fff;
    font-weight: 300;
    font-size:35px;
    line-height: 34px;
    margin-left:-80px;

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
}

.lable_text{
    font-size:22px;
    color:#888888;
}

.item_click{
    width:60px;   
    height:120px;
    justify-content:space-around;
    align-items: center
}

.click_icon{
    font-size:40px;
    color:red;
    
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
    </style>
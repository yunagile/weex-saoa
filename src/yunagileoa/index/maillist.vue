<template>
    <div class="wrapper" >
        <minibar append="tree" :theme="bartheme"  title="通讯录" :leftButton="false" :isPadding="true" :useDefaultReturn="false" >
        </minibar>
            <scroller class="scroller" >
                  <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                    <loading-indicator class="indicator"></loading-indicator> 
                  </refresh>
                    <div  class="list_content"  ref="searchK">
                      <searchbar class="searchbar" @inputDisabledClicked="openSearch" ref="searchbar" disabled=true></searchbar>
                    </div>
                    <div  class="list_content"  ref="otherK">
                        <x-cell  bottomBorder="true" title="部门" >
                            <div slot="label" class="icon" style="margin-right:20px" >
                                <div class="icon_div" style="background-color:#fb5c67">
                                    <text class="iconfont icon_text">&#xe665;</text>
                                </div>                                    
                            </div>
                        </x-cell>
                        <x-cell  bottomBorder="true" title="聊天群组">
                            <div slot="label" class="icon" style="margin-right:20px" >
                                <div class="icon_div" style="background-color:#1fb9ec">
                                    <text class="iconfont icon_text">&#xe669;</text>
                                </div>                
                            </div>
                        </x-cell>
                        <x-cell  bottomBorder="true" title="关注的同事">
                            <div slot="label" class="icon" style="margin-right:20px" >
                                <div class="icon_div" style="background-color:#ffba32">
                                    <text class="iconfont icon_text">&#xe641;</text>
                                </div>                                    
                            </div>
                        </x-cell>
                        <x-cell  bottomBorder="true" title="客户联系人">
                            <div slot="label" class="icon" style="margin-right:20px" >
                                <div class="icon_div" style="background-color:#389cff">
                                    <text class="iconfont icon_text">&#xe66b;</text>
                                </div>                                    
                            </div>
                        </x-cell>
                    </div>
                   <div v-for="(psn,index) in persons" :key="index" class="list_content"  :ref="'item'+psn.py" >
                       <div class="list_title">
                           <text class="title_text">{{psn.py}}</text>
                       </div>
                       <x-cell v-for="(psnc,index) in psn.psns" :key="index" :title="psnc.name" bottomBorder="true" @cellClick="openPerson(psnc)" >
                           <div slot="label" class="tool_icon" style="margin-right:20px" > 
                                <image :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+psnc.code" style="width:80px;height:80px;border-radius: 10px;"></image>
                            </div>
                       </x-cell>
                   </div>   
               </scroller>
                <div :class="['index-list-nav',showZheZhao?'index-list-back':'']" @panstart="touchstart"  @panmove ="touchmove" @panend="touchend" ref="indexList" append="tree">
                    <text class="iconfont list-nav-key icon-padding" @click="indexClick('search')" ref="search">&#xe673;</text>
                    <text v-for="(ts,index) in titles" :key="index" class="list-nav-key" @click="indexClick(ts)" ref="key">{{ts}}</text>
                    <text class="iconfont list-nav-key icon-padding-bottom" @click="indexClick('#')" ref='other'>#</text>
                </div>
               <div class="x-letter" v-if="isShowTer" ref="letter">
                 <text class="iconfont x-letter-text">{{terText}}</text>
               </div>

       
    </div>
</template>

<script>
import asCore from "../../common/js/core";
const dom = weex.requireModule("dom");
const modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
const storage = weex.requireModule("storage");
const animation = weex.requireModule('animation')
export default {
  components: {
    "minibar": require("../../common/component/minbar.vue"),
    "searchbar": require("../../common/component/searchbar.vue"),
    "x-cell": require("../../common/component/cell.vue")
  },
  data() {
    return {
      titles: [
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
       
      ],
      dpys: [],
      persons: [],
      bsessionid: "",
      rootPath: "",
      tpys: [],
      refreshing: false,
      showZheZhao:false,
      keysT:[],
      first:true,
      keyTop:0,
      nowIndex:null,
      offset:-120,
      isShowTer:false,
      terText:"",
      moveOffset:0
    };
  },
  props: {
    bartheme:{
      type: String,
      default: ""
    }
  },
  created() {
    this.getPath();
    if(this.isIos()){
      this.offset = 0;
      this.moveOffset = -250;
    }else{
      this.offset = -120;
      this.moveOffset = 0;
    }
  },

  methods: {
    gotolist(event) {
      
      var s = this;
      var pys = s.dpys;
      var lastValue = event;
      for (var i = 0; i < pys.length; i++) {
        if(event == 'search'){
          const el = this.$refs.searchK;
          lastValue = "\ue673";
          dom.scrollToElement(el, { offset: s.offset});
        }
        if( event == '#'){
          lastValue = "#";
          const el = this.$refs.otherK;
          dom.scrollToElement(el, { offset: s.offset });
        }
        if (event == pys[i]) {
          const el = this.$refs["item" + event][0];
          dom.scrollToElement(el, { offset: s.offset });
          lastValue = event;
        }
      }
      //asCore.toast(event);
      this.isShowTer = true;
      this.terText = lastValue;
    },
    indexClick(value){
      this.gotolist(value);
      this.$nextTick(function(){
        setTimeout(()=>{
          this.hideTer();
        },500);
      });
    },
    getPath() {
      var s = this;
      s.rootPath = asCore.rootPath;
      asCore.getBsessionid(bid => {
        if (bid) {
          s.bsessionid = bid;
          s.getData();
        }
      });
    },

    getData() {
      var s = this;
      storage.getItem("dpys", e => {
        if(e.result=="success"){
          var arr = e.data.split(",");
          arr.splice(arr.length - 1, 1);
          s.dpys = arr;
          storage.getItem("persons", e => {
            if(e.result=="success"){
              s.persons = JSON.parse(e.data);
            }else{
              this.getNewData();
              return;
            }
          });
        }else{
          this.getNewData();
          return;
        }
      });

      
    },
    isIos:function(){
        const { platform } = weex.config.env;
        return platform.toLowerCase() === 'ios';
    },
    getNewData(callback) {
      var s = this;
      asCore.post("/org/personQueryInOrgAction", null, (data, flag) => {
        if (data["status"] == "SUCCESS") {
          var pData = JSON.parse(data["data"]["data"]);
          var rows = pData.rows;
          var pys = [];
          var pysStr = "";
          for (var i = 0; i < rows.length; i++) {
            var py = rows[i].headName;
            var boo = true;
            if (pys.length > 1) {
              for (var y = 0; y < pys.length; y++) {
                if (pys[y] == py) {
                  boo = false;
                  break;
                }
              }
            }
            if (boo) {
              pysStr = pysStr + py + ",";
              pys.push(py);
            }
          }
          storage.setItem("dpys", pysStr);
          s.dpys = pys;
          var ps = [];
          for (var j = 0; j < pys.length; j++) {
            var p = { py: pys[j], psns: [] };
            var nameArray = [];
            for (var x = 0; x < rows.length; x++) {
              if (rows[x].headName == pys[j]) {
                p.psns.push({
                  id: rows[x].personID,
                  name: rows[x].personName,
                  code: rows[x].personCode
                });
              }
            }
            ps.push(p);
          }
          s.persons = ps;
          storage.setItem("persons", JSON.stringify(ps));
          if(callback) callback();
        } else {
          asCore.toast(data.msg);
          if(callback) callback();
        }
      });
    },

    openSearch() {
      navigator.push(asCore.localpath + "yunagileoa/index/searchP.js");
    },

    onrefresh(event) {
      var s = this;
      s.refreshing = true;
      this.getNewData(()=>{
        modal.toast({
          message: "刷新成功"
        });
        s.refreshing = false;
      });
    },
    touchstart(e){
      this.showZheZhao = true;
      var s = this;
        if(s.first){
            dom.getComponentRect(this.$refs.indexList,option=>{
                s.keyTop = option.size.top;
                
            });
            dom.getComponentRect(this.$refs.search,option=>{
              s.keysT.push(option.size.top);
            });
            const keys = this.$refs['key'];
            for(var i=0;i<keys.length;i++){
              dom.getComponentRect(keys[i],option=>{
                s.keysT.push(option.size.top);
              })
            }
            dom.getComponentRect(this.$refs.other,option=>{
                s.keysT.push(option.size.top);
            });
            s.first = false;
        }
       
    },
    touchend(e){
      this.showZheZhao = false;
      this.hideTer();
    },
    hideTer:function(){
       var s = this;
       var el = this.$refs.letter;
        animation.transition(el, {
          styles: {
            opacity: '0'
          },
          duration: 500, //ms
          timingFunction: 'ease',
          delay: 0 //ms
        }, function () {
          s.isShowTer = false;
        });
    },
    touchmove(e){
        var s = this;
        var y =  e.changedTouches[0].pageY + s.moveOffset;
        var index = Math.floor(y/35);
        if(index >=0){
          if(s.nowIndex != index){
              if(index == 0){
                this.gotolist('search')
              }else if(index == 27){
                this.gotolist('#')
              }else{
                this.gotolist(s.titles[index-1]);
              }
              s.nowIndex = index;
            } 
        }

        
    },
    openPerson(psnData){
      navigator.pushParam(asCore.localpath + "yunagileoa/my/personother.js",{
          pid:psnData['id'],
          pname:psnData['name'],
          pcode:psnData['code'],
          type:"maillist"
      });
    },
    setTheme:function(name){
      this.bartheme = name;
    }
  }
};
</script>


<style scoped>

.wrapper {
  position: absolute;
  top: 0px;
  bottom: 0px;
  left: 0px;
  right: 0px;
}
.iconfont {
  font-family: iconfont;
}
.title-icon {
  font-size: 35px;
  color: #fff;
  font-weight: 300;
}
.title-icon2 {
  font-size: 45px;
  color: #fff;
  font-weight: 300;
}
.minibar {
  z-index: 101;
}

.icon_text {
  font-size: 50px;
  color: #fff;
  text-align: center;
  line-height: 80px;
}

.searchbar {
  margin-bottom: 20px;
}

.icon_div {
  width: 80px;
  height: 80px;
  border-radius: 10px;
}

.list_title {
  width: 750px;
  height: 60px;
  border-bottom-color: #eeeeee;
  border-bottom-width: 1px;
  background-color: #f7f7f7;
  position: sticky;
}

.title_text {
  font-size: 28px;
  line-height: 60px;
  margin-left: 20px;
  color: #636463;
}

.index-list-nav {
  position: absolute;
  top: 250;
  right: 0;
  width: 70px;
  background-color: transparent;
  opacity: 1;
  z-index: 999;
}
.icon-padding {
  padding-top:10px;
}
.icon-padding-bottom {
  padding-bottom:10px;
}

.index-list-back {
  background-color: rgba(	211,211,211,.5);
  
}
.list-nav-key {
  text-align: center;
  font-size: 24px;
  color: #666666;
  opacity: 1;
  padding-top:2px;
  padding-bottom:2px;
}
.refresh {
  width: 750;
  display: -ms-flex;
  display: -webkit-flex;
  display: flex;
  -ms-flex-align: center;
  -webkit-align-items: center;
  -webkit-box-align: center;
  align-items: center;
}
.indicator {
  margin-top: 30px;
  margin-bottom: 20px;
  height: 70px;
  width: 70px;
  color: #ce2020;
}

.index-list-pop {
    position: fixed;
    top: 550px;
    left: 316px;
    width: 120px;
    height: 120px;
    text-align: center;
    justify-content: center;
    background-color: rgba(32, 35, 37, .6);
    border-bottom-left-radius: 60px;
    border-bottom-right-radius: 60px;
    border-top-left-radius: 60px;
    border-top-right-radius: 60px;
    padding-left: 0;
    padding-right: 0;
    padding-top: 35px;
    padding-bottom: 35px;
    color: #ffffff;
  }
  .list-pop-text {
    font-size: 40px;
    text-align: center;
    color: #ffffff;
  }
  .x-letter{
    position: absolute;
    top:0;
    bottom:0;
    left:0;
    right:70;
    justify-content: center;
    align-items: center;
    margin-top:100px;
    padding-left:70px;
    z-index: 666;
  }
  .x-letter-text {
    font-size:80px;
    color:#555555;
    text-align: center;
    border-radius: 10px;
    height:150px;
    line-height: 150px;
    width:150px;
    background-color: rgba(145,145,145,0.6);
  }
</style>

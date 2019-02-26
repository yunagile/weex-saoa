<template>
    <div class="wrapper" @viewdisappear="closePage" style="background-color:#f5f5f5">
        <searchbar ref="searchbar" @wxcSearchbarInputOnInput="onInput"  @wxcSearchbarCancelClicked="cenceBtn" alwaysShowCancel="true" @wxcSearchbarCloseClicked="sclose" ></searchbar>
        <div class="searchType" v-if="showType">
          <div class="searchType_top"><text class="searchType_top_text">搜索指定内容</text></div>
          <div class="searchType_content">
              <div class="type">
                 <text class="type_text">通讯录</text>
              </div>
              <div class="type">
                 <text class="type_text">任务</text>
              </div>
              <div class="type">
                 <text class="type_text">工作台</text>
              </div>

          </div>
        </div>
        <scroller class="scroller" >
            <x-cell v-for="(s,index) in search" :key="index" bottomBorder="true"  :title="s.name" @cellClick="openPerson(s)">
                <div slot="label" class="tool_icon" style="margin-right:20px" > 
                    <image :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+s.code" style="width:80px;height:80px;border-radius: 15px;"></image>
                </div>
            </x-cell> 
        </scroller>
    </div>
</template>

<script>
import asCore from "../../common/js/core";
const navigator = weex.requireModule("navigator");
export default {
  components: {
    "searchbar": require("../../common/component/searchbar.vue"),
    "x-cell": require("../../common/component/cell.vue")
  },
  data() {
    return {
      search: [],
      allRows: [],
      bid: "",
      rootPath: "",
      showType:true
    };
  },
  created() {
    this.getData();
    var globalEvent = weex.requireModule("globalEvent");
    globalEvent.addEventListener("onPageInit", function(e) {
      const nav = weex.requireModule("navbar");
      nav.setStatusBarStyle("black");
    });
  },
  methods: {
    getData() {
      var s = this;
      asCore.getBsessionid(bid => {
        if (bid) {
          s.bsessionid = bid;
        } else {
          asCore.toast("获取bsessionid失败，无法加载头像");
        }
      });
      s.rootPath = asCore.rootPath;
      asCore.post("/org/personQueryInOrgAction", null, (data, flag) => {
        if (data["status"] == "SUCCESS") {
          var pData = JSON.parse(data["data"]["data"]);
          var rows = pData.rows;
          s.allRows = rows;
        } else {
          asCore.toast(data.msg);
        }
      });
    },
    onInput(e) {
      var str = e.value;
      var s = this;
      this.getSearch(str);
      if(str){
          s.showType = false;  
          s.getSearch(str);
      }else{
        s.showType = true;
        s.search = [];
      }
    },
    getSearch(event) {
      var s = this;
      var rows = s.allRows;
      var searchs = [];
      for (var i = 0; i < rows.length; i++) {
          var personName = rows[i].personName;
          var personID = rows[i].personID;
          var personNameIndex = rows[i].personNameIndex;
          var personNamePinyin = rows[i].personNamePinyin;
          
          if(personNameIndex && personNameIndex.search(event)!=-1){
              searchs.push({id:personID,name:personName,code:rows[i].personCode});
              continue;
          }
          if(personName && personName.search(event)!=-1){
              searchs.push({id:personID,name:personName,code:rows[i].personCode});
              continue;
          }
          if(personNamePinyin && personNamePinyin.search(event)!=-1){
              searchs.push({id:personID,name:personName,code:rows[i].personCode});
              continue;
          }
      }
        //asCore.toast("搜索出"+searchs.length+'条结果');
        s.search = searchs;
    },
    cenceBtn(){
        const nav = weex.requireModule("navbar");
        nav.setStatusBarStyle("white");
        navigator.back();
    },
    closePage(){
      const nav = weex.requireModule("navbar");
      nav.setStatusBarStyle("white");
    },
    sclose(){
      this.$refs.searchbar.setValue(" ");
    },
    openPerson(psnData){
      navigator.pushParam(asCore.localpath + "yunagileoa/my/personother.js",{
          pid:psnData['id'],
          pname:psnData['name'],
          pcode:psnData['code'],
          type:"search"
      });
    }
  }
};
</script>

<style scoped>
.wrapper{
    padding-top:30px;
}
.scroller {
  margin-top: 20px;
}
.searchType{
  width:550px;
  padding-top:10px;
  padding-left:10px;
  padding-right: 10px;
  margin-top:60px;
  margin-left:100px;
  margin-right:100px;
}

.searchType_top{
  width:530px;
}

.searchType_top_text{
  font-size:26px;
  line-height: 60px;
  text-align: center;
  color:#888;
  margin-bottom:30px;
}

.searchType_content{
  width:530px;
  flex-direction: row;
  justify-content: space-between;
}

.type{
  width:176px;
  height:50px;

}

.type_text{
  font-size:26px;
  line-height: 50px;
  color:green;
  text-align: center;
}
</style>

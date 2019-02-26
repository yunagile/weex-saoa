<template>
    <div class="wrap" @viewdisappear="closePage">
       <!-- 标题栏 -->
        <div class="x-minibar bar-padding" append="tree" :style="{backgroundColor:'rgba('+head.rgb+','+head.bgAlpha+')',opacity : .98}">
            <div class="left" @click="closePerson">
                <text class="iconfont buttn-icon">&#xe9cf;</text>
            </div>
            <text class="middle-title">TA的资料</text>
            <div class="right">
                <text class="iconfont buttn-icon">&#xe647;</text>
            </div>
        </div>
        <!-- 头像部分 -->
        <scroller class="main-list" @scroll="scroll">
          <image class="back-img" resize="cover" :src="imgBack"></image>
          <div class="back-div">
              <image class="back-img2" :src="imgurl"></image>
              <div class="pinfo-name">
                  <text class="pinfo-name-font">{{infoData.username}}</text>
                  <div v-if="boyShow" class="pinfo-boy"><text class="iconfont pinfo-boy-icon" >&#xe64c;</text></div>
                  <div v-if="grilShow" class="pinfo-girl"><text class="iconfont pinfo-girl-icon" >&#xe668;</text></div>
              </div>
              <div class="pinfo-st"></div>
              <div class="pinfo-sb">
                <div class="pinfo-st-div ">
                  <text class="iconfont pinfo-st-text">&#xe694;发任务</text>
                </div>
                <div class="pinfo-st-rb"></div>
                <div class="pinfo-st-div">
                  <text :class="['iconfont','pinfo-pass-icon',isIos()?'iostop':'']">&#xe746;</text>
                  <text class="iconfont pinfo-st-text">发消息</text>
                </div>
              </div>
          </div>
          <!-- 个人信息 -->
          <x-cell title="所属机构" :topBorder="true" :hasArrow="true" class="list-margin">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:38,color:'#31BBFF'}">&#xe6a2;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.orgName}}</text>
            </div>
          </x-cell>
          <x-cell title="所属部门" :hasArrow="true">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:43,color:'#30C462'}">&#xe69d;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.deptName}}</text>
            </div>
          </x-cell>
          <x-cell title="当前岗位" :hasArrow="true">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:43,color:'#FAA727'}">&#xe742;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.positionName}}</text>
            </div>
          </x-cell>
          <x-cell title="手机号码" :hasArrow="true" >
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:43,color:'#31BBFF'}">&#xe6f4;</text>
            </div>
            <div soft="name" v-if="infoData.phone">
              <text class="name_text">{{infoData.phone}}</text>
            </div>
          </x-cell>
          <x-cell title="电子邮箱" :hasArrow="true">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:35,color:'#F15A49'}">&#xe69b;</text>
            </div>
            <div soft="name">
                <text class="name_text">{{infoData.email}}</text>
            </div>
          </x-cell>
          <x-cell title="QQ" :hasArrow="true">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:40,color:'#31BBFF'}">&#xe69e;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.qq}}</text>
            </div>
          </x-cell>
          <x-cell title="居住地址" :bottomBorder="true" :hasArrow="true" style="margin-bottom:500px;">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:38,color:'#888'}">&#xe697;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.address}}</text>
            </div>
          </x-cell>
        
      </scroller>
    </div>
</template>
<script>
import asCore from "../../common/js/core";
import SimpleData from "../../common/js/SimpleData";
import Utils from "../../common/js/utils";
const modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
const lBroad = new BroadcastChannel("inputBroad");
export default {
  components: {
    "minibar": require("../../common/component/minbar.vue"),
    "x-cell": require("../../common/component/cell.vue")
  },
  created() {
    /* 获取主题 */
    Utils.getThemeColor((color,theme,rgb) => {
        this.head.rgb = rgb;
    });
    var s = this;
    var globalEvent = weex.requireModule('globalEvent') ;
    /* 接收参数 */
    globalEvent.addEventListener("onPageInit", function (e) {
      var param = navigator.param();
      s.currentPersonID = param['pid'];
      s.currentPersonName = param['pname'];
      s.currentPersonCode = param['pcode'];
      s.retype = param['type'];
      s.getAsInfo();
    });
  },
  data() {
    return {
      imgurl: "",
      imgBack:"",
      currentPersonID:"",
      currentPersonName:"",
      currentPersonCode:"",
      grilShow:false,
      boyShow:false,
      retype:false,
      infoData:{
        username:"",
        orgName:"",
        deptName:"",
        positionName:"",
        phone:"",
        qq:"",
        email:"",
        address:""
      },
      head: {
        bgAlpha: 0,
        rgb:"27,130,210"
      }
    };
  },
  methods: {
    getAsInfo: function() {
      var s = this;
      asCore.getBsessionid(bid => {
        if (bid) {
            s.loadHeadImg(bid,s.currentPersonCode);
            s.infoData['username'] = s.currentPersonName;
            s.getBasePsnInfo();
        }
      });
    },
    loadHeadImg (bid,code){
      var s = this;
      asCore.getTime(time=>{
        var url = "",backUrl = "";
        /* ios暂时不支持filter样式 */
        /* 获取时间戳来判断是否刷新图片,因为图片进行了缓存 */
        if(!s.isIos()){
          url = asCore.rootPath + "/system/user/userPhotoDownloadAction?bsessionid=" + bid + "&code=" + code + "&t="+time;
          backUrl = url;
        }else{
          backUrl = asCore.localpath + "img/lanse.png";
          url = asCore.rootPath + "/system/user/userPhotoDownloadAction?bsessionid=" + bid + "&code=" + code + "&t="+time;
        }
        s.imgurl = url; 
        s.imgBack = backUrl;
      });
    },
    /* 查询个人信息 */
    getBasePsnInfo: function(){
      var s = this;
      asCore.post("/queryPsnFullInfoAction", {personID:s.currentPersonID}, function(data, flag) {
        if (data["status"] == "SUCCESS") {
          
          var sData = data.data;
				  var orgData = sData.orgs;
          var psnData = sData.psn;
          var sex = psnData['sex'];
          if(sex=="男"){
            s.boyShow = true;
            s.grilShow = false;
          }else if(sex=="女"){
            s.grilShow = true;
            s.boyShow = false;
          }else {
            s.grilShow = false;
            s.boyShow = false;
          }
          s.setOrgInfo(orgData[0]);
          s.setBaseInfo(psnData);
        }else{
          asCore.toast("个人数据加载失败");
        }
		  });

    },
    setBaseInfo: function (rows){
      var s = this;
      /* 设置手机号码 */
      s.infoData['phone'] = rows['mobilePhoneNum']?rows['mobilePhoneNum']:"未填写";
      s.infoData['qq'] = rows['sqq']?rows['sqq']:"未填写";
      /* 设置QQ */
      s.infoData['email'] = rows['mail']?rows['mail']:"未填写";
      s.infoData['address'] = rows['sfamilyaddress']?rows['sfamilyaddress']:"未填写";
    },
    setOrgInfo:function(orgData){
     
      var s = this;
      try{
        var fullName = orgData['fullName'];
        var names = fullName.split("/");
        var kind = orgData['kind'];
        s.infoData['orgName'] = names[1];
        if(kind=='pos'){
          s.infoData['positionName'] = names[names.length-1];
          if(names[names.length-2]==names[1]){
            s.infoData['deptName'] = "未设置所属部门";
          }else{
            s.infoData['deptName'] = names[names.length-2];
          }
        }else if(kind=='dept'){
          s.infoData['positionName'] = "未设置当前岗位";
          s.infoData['deptName'] = names[names.length-1];
        }else if(kind=='org'){
          s.infoData['positionName'] = "未设置当前岗位";
          s.infoData['deptName'] = "未设置所属部门";
        }
      }catch(e){
        s.infoData['orgName'] = "未设置所属机构";
        s.infoData['positionName'] = "未设置当前岗位";
        s.infoData['deptName'] = "未设置所属部门";
      }
    },
    isIos:function(){
      const { platform } = weex.config.env;
      return platform.toLowerCase() === 'ios';
    },
    closePerson:function(){
      navigator.back();
    },
    scroll:function(e){ 
      var scrollTop = Math.abs(e.contentOffset.y);
      if(scrollTop< 200){
          this.head.bgAlpha = 0;
      }else{
          this.head.bgAlpha =1;
      }
    },
    closePage(){
      const nav = weex.requireModule("navbar");
      if(this.retype=="search"){
        nav.setStatusBarStyle("black");
      }
    }
  }
};
</script>
<style scoped>
.wrap {
  background-color: #f5f5f5;
}
.list-margin {
  margin-top: 0px;
}
.name_text{
  color:#888;
  font-size:28px;
  text-overflow:ellipsis;
  width:400px;
  text-align: right;
  lines:1;
}
.back-img {
  width: 750px;
  height: 600px;
  filter: blur(100px);
  background-position: center;
}
.back-div {
  position: absolute;
  top: 0px;
  left: 0;
  right: 0;
  height: 600px;
  align-items: center;
  justify-content: center;
}
.bar-padding {
  padding-top: 30px;
}
.x-minibar {
    position: fixed;
    display: flex;
    top: 0;
    left: 0;
    right: 0;
    width: 750px;
    height: 130px;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    background-color: transparent;
    flex-wrap: nowrap;
    z-index: 101;
}
.left {
  height:80px;
  width: 180px;
  justify-content: center;
  padding-left: 25px;
}
.middle-title {
  font-size: 35px;
  font-weight: 300;
  color: #ffffff;
  height: 36px;
  line-height: 36px;
}
.cell-icon {
    color:#ffffff;
}
.right {
  width: 180px;
  padding-right: 25px;
  align-items: flex-end;
}
.back-img2 {
  height: 230px;
  width: 230px;
  border-radius: 100%;
  border-width: 5px;
  border-color: #fff;
}
.iconfont {
  font-family: iconfont; 
} 
.buttn-icon {
  font-size: 40px;
  color: #fff;
}
.pinfo-name {
    padding-top:20px;
    flex-direction: row;
    align-items: center;
}
.pinfo-name-font {
    font-size:35px;
    font-weight: 300px;
    color: #fff;
}
.pinfo-boy {
    background-color: #1DA1F2;
    height:30px;
    width:30px;
    border-radius: 30px;
    margin-left:5px;
    align-items: center;
    justify-content: center;
    margin-top:3px;
}
.pinfo-boy-icon{
    color:#fff;
    font-size:22px;
}
.pinfo-girl {
    background-color: #FF3D8A;
    height:30px;
    width:30px;
    border-radius: 30px;
    margin-left:5px;
    align-items: center;
    justify-content: center;
    margin-top:3px;
}
.pinfo-girl-icon {
    color:#fff;
    font-size:22px;
}
.pinfo-st { 
    background-color: #000000;
    opacity: 0.2;
    height:90px;
    position: absolute;
    top:510px;
    left:0;
    right: 0;
    bottom:0;
}
.pinfo-sb { 
    height:90px;
    position: absolute;
    top:510px;
    left:0;
    right: 0;
    bottom:0;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
.pinfo-st-div {
    background-color:transparent;
    opacity: 1;
    flex: 1;
    height:90px;
    align-items: center;
    justify-content: center;
    flex-direction: row;
}
.pinfo-st-rb {
    border-right-color: #ddd;
    border-right-width: 2px;
    opacity: 0.1;
    height:60px;
}
.pinfo-st-div:active {
    background-color: #000000;
    opacity: 0.1;
}
.pinfo-st-text {
    color:#ffffff;
    font-size:30px;
}
.pinfo-pass-icon{
    color:#ffffff;
    font-size:28px;
    margin-top:2px;
}
.list-icon {
    height: 65px;
    width: 60px;
    border-radius: 10px;
    margin-right:5px;
    align-items: center;
    justify-content: center;
}
.iostop {
  margin-top:9px;
}
</style>
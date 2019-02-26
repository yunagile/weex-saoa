<template>
    <div class="wrap">
       <!-- 标题栏 -->
        <div class="x-minibar bar-padding" append="tree" :style="{backgroundColor:'rgba('+head.rgb+','+head.bgAlpha+')',opacity : .98}">
            <div class="left" @click="closePerson">
                <text class="iconfont buttn-icon">&#xe9cf;</text>
            </div>
            <text class="middle-title">个人资料</text>
            <div class="right">
                <text class="iconfont buttn-icon">&#xe647;</text>
            </div>
        </div>
        <!-- 头像部分 -->
        <scroller class="main-list" @scroll="scroll">
          <image class="back-img" resize="cover" :src="imgBack"></image>
          <div class="back-div">
              <image class="back-img2" :src="imgurl" @click="openSheet"></image>
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
                <div class="pinfo-st-div" @click="openUpdatePass">
                  <text :class="['iconfont','pinfo-pass-icon',isIos()?'iostop':'']">&#xe692;</text>
                  <text class="iconfont pinfo-st-text">密码修改</text>
                </div>
              </div>
          </div>
          <!-- 个人信息 -->
          <!-- 后期可以list方式展示 -->
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
          <x-cell title="手机号码" :hasArrow="true" @cellClick="navPush('phone','修改手机号码')">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:43,color:'#31BBFF'}">&#xe6f4;</text>
            </div>
            <div soft="name" v-if="infoData.phone">
              <text class="name_text">{{infoData.phone}}</text>
            </div>
          </x-cell>
          <x-cell title="电子邮箱" :hasArrow="true" @cellClick="navPush('email','修改地址邮箱')">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:35,color:'#F15A49'}">&#xe69b;</text>
            </div>
            <div soft="name">
                <text class="name_text">{{infoData.email}}</text>
            </div>
          </x-cell>
          <x-cell title="QQ" :hasArrow="true" @cellClick="navPush('qq','修改qq号码')">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:40,color:'#31BBFF'}">&#xe69e;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.qq}}</text>
            </div>
          </x-cell>
          <x-cell title="居住地址" :bottomBorder="true" :hasArrow="true" @cellClick="navPush('address','修改地址')" style="margin-bottom:500px;">
            <div slot="label" class="list-icon" >
                <text class="iconfont cell-icon" :style="{fontSize:38,color:'#888'}">&#xe697;</text>
            </div>
            <div soft="name">
              <text class="name_text">{{infoData.address}}</text>
            </div>
          </x-cell>
        
      </scroller>
      <!--自定义上拉菜单-->
        <actionsheet
                :items="actionsheetItems"
                v-model="showAction"
                @itemClick="actionsheetItemClick"
                @btnClick="actionsheetBtnClick">
        </actionsheet>
    </div>
</template>
<script>
import asCore from "../../common/js/core";
import SimpleData from "../../common/js/SimpleData";
import Utils from "../../common/js/utils";
const modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
const lBroad = new BroadcastChannel("inputBroad");
const photo = weex.requireModule('photo');
export default {
  components: {
    "minibar": require("../../common/component/minbar.vue"),
    "x-cell": require("../../common/component/cell.vue"),
    "actionsheet" :require("../../common/component/actionsheet.vue")
  },
  created() {
    /* 获取主题 */
    Utils.getThemeColor((color,theme,rgb) => {
        this.head.rgb = rgb;
        this.phoneColor = color;
    });
    /* 创建personData */
    this.personData = SimpleData.creat("personData",this.baseInfoConfig);
    /* 得到个人信息 */
    this.getAsInfo();
    lBroad.onmessage = this.inputCallBack;
  },
  data() {
    return {
      imgurl: "",
      imgBack:"",
      currentPersonID:"",
      /* 控制显示性别 */
      grilShow:false,
      boyShow:false,
      /* 目前信息的data也只能这样来保存 */
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
      /* 控制导航条透明度 */
      head: {
        bgAlpha: 0,
        rgb:"27,130,210"
      },
      personData:null,
      /* simpleData的配置 */
      baseInfoConfig: {
		    queryAction:{
			    url:"/org/queryPersonByIdAction",
          param:{
            id:""   /* 一开始配置时参数不能有值只能动态的设置 */
          }
		    },
        saveAction:{
          url:"/org/savePersonDataAction"
        }
      },
      showAction: false,
      /* 配置弹出菜单 */
      actionsheetItems: ['同步QQ头像', '拍照','从相册选择'],
      phoneColor:"#000000",
      bsessionid:""
    };
  },
  methods: {
    getAsInfo: function() {
      var s = this;
      /* 获取bid 目前只能异步获取 */
      asCore.getBsessionid(bid => {
        if (bid) {
          s.bsessionid = bid;
          asCore.getContext(context => {
            s.loadHeadImg(bid);
            /* 设置基本信息 */
            s.currentPersonID = context.currentPersonID;
            s.infoData['username'] = context.currentUserName;
            s.infoData['orgName'] = context.currentOgnName;
            s.infoData['deptName'] = context.currentDeptName?context.currentDeptName:"未设置所属部门";
            s.infoData['positionName'] = context.currentPositionName;
            s.getBasePsnInfo();
          });
        }
      });
    },
    loadHeadImg (bid){
      var s = this;
      asCore.getTime(time=>{
        var url = "",backUrl = "";
        /* ios暂时不支持filter样式 */
        /* 获取时间戳来判断是否刷新图片,因为图片进行了缓存 */
        if(!s.isIos()){
          url = asCore.rootPath + "/system/user/userPhotoDownloadAction?bsessionid=" + bid + "&t="+time;
          backUrl = url;
        }else{
          backUrl = asCore.localpath + "img/lanse.png";
          url = asCore.rootPath + "/system/user/userPhotoDownloadAction?bsessionid=" + bid + "&t="+time;
        }
        s.imgurl = url; 
        s.imgBack = backUrl;
      });
    },
    /* 查询个人信息 */
    getBasePsnInfo: function(){
      var s = this;
      var pData = this.personData;
      /* 在使用simpleData时,再动态设置对应的值即可 */
      this.baseInfoConfig.queryAction.param.id = s.currentPersonID;
      pData.refreshData(function(data,flag,msg){
        if(flag==true){
          pData.selectRow(data[0]['id']);
          var sex = pData.getValue("sex");
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
          s.setBaseInfo(data[0]);
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
    /* 统一的修改简单值的弹出框 */
    navPush:function(filed,title){
      var  s = this;
      var value = s.infoData[filed];
      navigator.pushParam(asCore.localpath + "common/component/input.js",{
          type:filed,
          value:value,
          title:title
      });
    },
    openUpdatePass:function(){
      navigator.push(asCore.localpath + 'yunagileoa/my/alterPassword.js');
    },
    inputCallBack : function(event){
      var s = this;
      var data = event.data;
      var value = data['value'];
      if(data['type']=="phone"){
        s.setValueAndSave("mobilePhoneNum","phone",value);
      }else if (data['type']=="qq"){
        s.setValueAndSave("sqq","qq",value);
      }else if(data['type']=="email"){
        s.setValueAndSave("mail","email",value);
      }else if(data['type']=="address"){
        s.setValueAndSave("sfamilyaddress","address",value);
      }
    },
    /* 设置并保存 */
    setValueAndSave:function(filed,sData,value){
      var s = this;
      var pData = s.personData;
      pData.setValue(filed,value);
      pData.save(function(flag){
        if(flag){ 
            s.infoData[sData] = value;
            asCore.toast("修改成功");
          }else{
            asCore.toast("修改失败,请稍后再试");
          }
      });
    },
    /* 判断是否是ios */
    isIos:function(){
      const { platform } = weex.config.env;
      return platform.toLowerCase() === 'ios';
    },
    closePerson:function(){
      navigator.back();
    },
    /* 监听滚动事件来设置标题栏的透明度 */
    scroll:function(e){ 
      var scrollTop = Math.abs(e.contentOffset.y);
      if(scrollTop< 200){
          this.head.bgAlpha = 0;
      }else{
          this.head.bgAlpha =1;
      }
    },
    openSheet() {
      this.showAction = true;
    },
    actionsheetItemClick(item,index) {
      this.showAction = false;
      if(index==1){
        this.openCamera();
      }else if(index==2){
        this.openPhoto();
      }
    },
    actionsheetBtnClick() {
      this.showAction = false;
    },
    /* 打开照相机 */
    openCamera(){
      var s=this;
      photo.openCamera(500,500,this.phoneColor,function(e){
        s.uploadHead(e['path']);
      });
    },
    /* 打开相册选择 */
    openPhoto(){
      var s = this;
      photo.openPhoto(500,500,this.phoneColor,'#ffffff','#ffffff',function(e){
        s.uploadHead(e['path']);
      });
    },
    /* 上传头像 */
    uploadHead(datastr){ 
      var s = this;
      asCore.rendUrl("/system/user/photoUploadAction",uploadUrl => {
        if(uploadUrl){
          /**
           * @param uploadUrl 上传图片的url
           * @param datastr 图片路径
           * @param callback 回调参数 [flag]
           */
		  photo.uploadByWeb(uploadUrl,datastr,data => {
			//asCore.alert("上传地址："+uploadUrl);
            if(data['flag']=='SUCCESS'){
              var time = new Date().getTime();
              var url = asCore.rootPath + "/system/user/userPhotoDownloadAction?bsessionid=" + s.bsessionid + "&t=" + time;
              asCore.setTime(time + "");
              s.imgurl = url;
              s.imgBack = url;
              /* 发送消息告诉其他页面更新 */
              asCore.toast("上传成功");
              const updateImg = new BroadcastChannel("updateImg");
              updateImg.postMessage("update");
            }else{
              asCore.alert("头像上传失败！");
            }
          }); 
        }
      });
    }
  }
};
</script>
<style scoped>
.wrap {
  background-color: #f4f4f4;
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
  width: 180px;
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
    margin-top:5px;
}
.iostop {
  margin-top:9px;
}
.list-icon {
    height: 65px;
    width: 60px;
    border-radius: 10px;
    margin-right:5px;
    align-items: center;
    justify-content: center;
}
</style>
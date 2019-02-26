<template>
  <div class="wrapper">
    <!-- logo层 -->
    <div class="img-div">
      <image class="logo-img" :src="logourl"></image>
    </div> 
    <!-- 用户密码层 -->
    <div class="content-div">
      <div class="cli-div">
        <text class="cli-icon iconfont">&#xe66a;</text>
        <input type="text"  ref="usname" class="x-input" placeholder="请输入用户名" v-model="username" @input="userinput" @focus="changeClean" />
        <text v-if="showClean" class="cli-icon iconfont cli-icon2" @click="cleanUsername">&#xe677;</text>
      </div>
      <div class="cli-div margin-top-20">
        <text class="cli-icon iconfont">&#xe649;</text>
        <input v-if="(showPass)" type="text" class="x-input" placeholder="请输入密码" :value="userpass"/>
        <input v-if="(showPass2)" type="password" class="x-input" placeholder="请输入密码" :value="userpass"/>
        <text v-if="showPass" class="cli-icon iconfont" @click="hPass">&#xe62d;</text>
        <text v-else class="cli-icon iconfont" @click="sPass">&#xe62e;</text>
      </div>
    </div>
    <!-- 自动登录层 -->
    <div class="content-div">
      <div class="xli-div">
        <div class="xli-left">
          <checkbox ref="lcheckbox" class="checkbox-item" :value="checkData" @input="v=>checkData=v" val="1" @change="setCheckData">记住密码</checkbox>
        </div>
        <div class="xli-right">
          <text class="remeber-pass">忘记密码?</text>
        </div>
      </div>
    </div>
    <!-- 按钮层 -->
    <div class="content-div">
      <div class="bli-div">
        <loginbutton :text="loginBtnText" @buttonClick="login" 
        :showLoadding="showLoginLoadding" 
        :disabled="btnDisabled"></loginbutton>
      </div>
    </div>
    <!-- 第三方登录 -->
    <div class="content-div margin-top-50 ">
      <div class="sli-div no-padding ">
        <div class="sli-line"></div>
        <text class="content-div sli-title">第三方登录</text>
        <div class="sli-line"></div>
      </div>
      <div class="sli-div">
        <!-- QQ -->
        <div class="flex1">
          <div class="x-sli-icon" :style="{backgroundColor:'#5ab9ee'}">
            <text class="x-sli-font iconfont">&#xe63d;</text>
          </div>
        </div>
        <!-- 微信 -->
        <div class="flex1">
          <div class="x-sli-icon" :style="{backgroundColor:'#72bd63'}">
            <text class="x-sli-font iconfont" :style="{fontSize:65}">&#xe644;</text>
          </div>
        </div>
        <!-- 微博 -->
        <div class="flex1">
          <div class="x-sli-icon" :style="{backgroundColor:'#ff6c55'}">
            <text class="x-sli-font iconfont">&#xe642;</text>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Utils from "./common/js/utils";
import encrypt from "./common/js/encrypt";
import asCore from "./common/js/core";
var modal = weex.requireModule("modal");
var stream = weex.requireModule("stream");
var pref = weex.requireModule("storage");
const navigator = weex.requireModule('navigator');
const loginBroad = new BroadcastChannel('login');
export default {
  components: {
    "checkbox": require("./common/component/checkbox.vue"),
    "loginbutton": require("./common/component/button.vue")
  },
  beforeCreate() {
    
  },
  created() {
    this.logourl = asCore.localpath + "img/logo.png";
    var globalEvent = weex.requireModule("globalEvent");
    globalEvent.addEventListener("onPageInit", function(e) {
      const nav = weex.requireModule("navbar");
      nav.setStatusBarStyle("black");
      const page = weex.requireModule("page");
      page.enableBackKey(false); 
      page.setBackKeyCallback(()=>{ 
        page.enableBackKey(true); 
        page.exit();
      });
    });
    /* 去设置账户密码 */
    this.goToSetPass();
  },
  data() {
    return {
      showLoginLoadding: false,
      btnDisabled: false,
      loginBtnText: "登录",
      logourl: "",
      username: "",
      userpass: "",
      checkData: ['1'],
      isremember: true,
      showPass:false,
      showPass2:true,
      showClean:false
    
    };
  },
  methods: {
    userinput: function(event) {
      var self = this;
      this.changeClean()
    },
    passinput: function(event) {
      this.userpass = event.value;
    },
    setCheckData: function(data, ischeck) {
      if (ischeck == false) {
        this.isremember = true;
      } else {
        this.isremember = false;
      }
    },
    loginCheck: function() {
      if (!this.username) {
        this.hintShow("请输入用户名");
        return false;
      }
      if (!this.userpass) {
        this.hintShow("请输入密码");
        return false;
      }
      return true;
    },
    login: function() {
      var s = this;
      if (!this.loginCheck()) return;
      this.setBtnDisabled(true);
      encrypt.setMaxDigits(130);
      var key = new encrypt.RSAKeyPair(
        "10001",
        "",
        "818e85269508bd1b747a0fa10a85e832ce461ccc2195f944430611c7ac28b0da2eb7814a57c194a4fd396d6ec802aa74353fa4f6981bdc726d79400920304e6d60780f5b55fc312831618d512c32df94133cefddedd733843cd419b9c2e6c7bb593b134018d84c6a14e1e2931ddc0d9c9342fef8c95dd3cc29552f1056c822b1"
      );
      var ps = encrypt.encryptedString(key, encodeURIComponent(s.userpass));
      stream.fetch({
          method: "POST",
          url: asCore.rootPath + "/loginAsAction",
          type: "json",
          body: asCore.toParams({
            username: s.username,
            password: ps
          })
        },
        function(ret) {
          if (!ret.ok) {
            s.hintShow("登录失败,请检查网络连接!");
            s.setBtnDisabled(false);
          } else {
            var re = ret.data;
            if (re.status == "SUCCESS") {
              pref.setItem("as_username", s.username);
              if(s.isremember==true){
                pref.setItem("as_password", s.userpass);
                pref.setItem("encryptedPass",ps)
                pref.setItem("as_password_checkbox", '1');
              }else{
                pref.setItem("as_password", "nopass");
                pref.setItem("encryptedPass","nopass")
                pref.setItem("as_password_checkbox", '0');
              }
              asCore.setBsessionid(re.data.bsessionid);
              s.setBtnInfo(false,"登录成功",true);
              loginBroad.postMessage({success:1});
              navigator.back();
            } else {
              var index = re.msg.indexOf("登录错误次数过多");
              if (index > -1) {
                var r = re.msg.split(":");
                var t = r[1];
                var m = r[0];
                var interval = setInterval(function() {
                  t = t - 1000;
                  if (t < 0) {
                    s.setBtnInfo(false,"登录",false);
                    clearInterval(interval);
                  } else {
                    s.setBtnInfo(false,"登录错误次数过多(" + Math.ceil(t / 1000) + ")",true);
                  }
                }, 1000);
              } else {
                s.setBtnDisabled(false);
                s.hintShow(re.msg);
              }
            }
          }
        }
      );
    },
    hintShow: function(msg) {
      modal.toast({ message: msg });
    },
    setBtnDisabled: function(value) {
      if (value) {
        this.showLoginLoadding = true;
        this.loginBtnText = "登录中...";
        this.btnDisabled = true;
      } else {
        this.showLoginLoadding = false;
        this.loginBtnText = "登录";
        this.btnDisabled = false;
      }
    },
    setBtnInfo : function(loadding,label,disabled){
      this.showLoginLoadding = loadding;
      this.loginBtnText = label;
      this.btnDisabled = disabled;
    },
    goToSetPass : function(){
      var s = this;
      pref.getItem("as_username",event => {
        if(event.result=="success"){
          s.username = event.data;
          s.showClean = true;
        }
      });
      var checkPass = ''; 
      pref.getItem("as_password_checkbox",event => {
          checkPass = event.data;
          if(checkPass=='1'){
            pref.getItem("as_password",event => {
              s.userpass = event.data;
            });
            s.isremember = true;
          }else{
            s.checkData = [];
            s.isremember = false;
            s.userpass = "";
          }
      });
    },
    cleanUsername(){
      var s = this;
      s.username = '';
      s.$nextTick(()=>{
        s.showClean = false;
      });
    },
    sPass(){
      this.showPass2 = false;
      this.$nextTick(()=>{
        this.showPass = true;
      });
      
    },
    hPass(){
      this.showPass = false;
      this.$nextTick(()=>{
        this.showPass2 = true;
      });
    },
    changeClean(){
      var self = this;
      var e = this.$refs.usname.attr.value;
      if(e!=''){
        self.showClean = true;
      }else{
         self.showClean = false;
      }
    }
  }
};
</script>
<style scoped src="./login.css"></style>

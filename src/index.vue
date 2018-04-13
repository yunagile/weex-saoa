<template>
  <div class="wrapper">
    <minibar title="WEEX-SAOA" theme="theme2" :leftButton="false" :isPadding="true" :useDefaultReturn="false" ></minibar>
    <div class="hello">
      <text class="hello-text">hello weex-saoa</text>
      <x-button class="button" text="打开新页面" type="normal" @buttonClick="openPage" :showLoadding="false" ></x-button>
    </div>

  </div>
  
</template>

<script>
import minibar from "./common/component/minbar.vue";
import button from "./common/component/button.vue";
import Utils from "./common/js/utils";
import asCore from "./common/js/core";
/*主页重要的页面*/
var modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
const loginBroad = new BroadcastChannel("login");
const storage = weex.requireModule('storage'); 
export default {
  components: {
    "minibar":minibar,
    "x-button":button
  },
  computed: {
    
  },
  beforeCreate() {
    var s = this;
    var globalEvent = weex.requireModule("globalEvent");
    globalEvent.addEventListener("onPageInit", function(e) {
      const nav = weex.requireModule("navbar");
      nav.setStatusBarStyle("white");
      navigator.addBackGestureSelfControl();
    });
    /* 判断是否登录过和是否超时(超时将自动从登) */
    asCore.getBsessionid(sessionid =>{
      if (!sessionid) {
        navigator.push(asCore.localpath + "login.js");
      } else {
        /* 所有操作请在appinital 之后操作 */
        asCore.appInitial(() =>{
          /* 加载context */
          asCore.toast("登录成功"); 
        });
      }
    });
    /* 双击退出程序 */
    const page  = weex.requireModule("page");
    page.doubleBack();
    /* 登录之间进行通信的隧道 */
    loginBroad.onmessage = function(event) {
      var data = event.data;
      if(data.success == 1){
        page.reload();
      }
    };
  },
  created() {
    Utils.initIconFont();
  },
  data() {
    return {
      
    };
  },
  methods: {
    openPage(){
      navigator.push(asCore.localpath + "saui/list.js");
    }
  }
};
</script>
<style scoped>
.wrapper {
 background-color: #f5f5f5;
}
.hello {
  align-items: center;
  justify-content: center;
  position: absolute;
  top: 100;
  bottom: 0;
  left: 0;
  right: 0;
  
}
.hello-text {
  color:red;
  font-size:40px;
}
.button {
  width:250px;
  margin-top:20px;
}
</style>

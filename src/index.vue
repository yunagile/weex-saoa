<template>
  <div class="wrapper">
  </div>
  
</template>

<script>
import Utils from "./common/js/utils";
import asCore from "./common/js/core";
/*主页重要的页面*/
var modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
const loginBroad = new BroadcastChannel("login");
const storage = weex.requireModule('storage'); 
export default {
  components: {
    
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
  }
};
</script>
<style scoped>
.wrapper {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
</style>

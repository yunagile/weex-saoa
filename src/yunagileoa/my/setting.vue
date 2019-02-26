<template>
    <div class="wrap">
        <minibar append="tree" title="设置" :isPadding="true" ref="mbar" :theme="bartheme"></minibar>
        <x-cell title="免打扰设置" :topBorder="true" :hasArrow="true" class="list-margin"></x-cell>
        <x-cell title="消息记录设置" :bottomBorder="true" :hasArrow="true"></x-cell>
        <x-cell title="二维码下载" :topBorder="true" :hasArrow="true" class="list-margin" @cellClick="openPage('qrDown')"></x-cell>
        <x-cell title="意见与反馈" :hasArrow="true" @cellClick="openPage('feedBack')"></x-cell>
        <x-cell title="清除缓存"  :hasArrow="true"></x-cell>
        <x-cell title="主题设置" :bottomBorder="true" :hasArrow="true" @cellClick="selectTheme"></x-cell>
        <x-cell title="新版本检查" :topBorder="true" :hasArrow="true" class="list-margin"></x-cell>
        <x-cell title="使用指导" :hasArrow="true"></x-cell>
        <x-cell title="关于" :bottomBorder="true" :hasArrow="true" @cellClick="openPage('about')"></x-cell>
        <x-cell :topBorder="true" :bottomBorder="true" class="list-margin" @cellClick="logout">
            <div slot="title" class="logout">
                <text class="logout-text">退出当前账号</text>
            </div>
        </x-cell>
        <!-- 对话框组件 -->
        <x-dialog title="温馨提示"
                content="是否确定退出当前账号?"
                confirm-text="确定"
                cancel-text="取消"
                overlay-can-close="false"
                :show="dialogShow"
                :single="false"
                top=550
                @cancelBtnClick="cancelBtnClick"
                @okBtnClick="okBtnClick">
        </x-dialog>

        <x-mask 
              height="440"
              width="602"
              border-radius="5"
              :overlayCanClose="false"
              mask-bg-color="#FFFFFF"
              :has-overlay="true"
              :show="maskShow">

            <div class="mask_content">
                <div class="mask_content_t">
                    <text class="title_text">主题选择</text>
                </div>
                <div class="hr_div"></div>
                <div class="mask_content_c">
                    <div class="color_theme" style="background-color:#2e3238" @click="clickTheme('theme1')"><text v-if="theme == 'theme1'" class="iconfont now_theme" >&#xe6a3;</text></div>
                    <div class="color_theme" style="background-color:#1B82D2" @click="clickTheme('theme2')"><text v-if="theme == 'theme2'" class="iconfont now_theme" >&#xe6a3;</text></div>
                    <div class="color_theme" style="background-color:#DF6766" @click="clickTheme('theme3')"><text v-if="theme == 'theme3'" class="iconfont now_theme" >&#xe6a3;</text></div>
                    <div class="color_theme" style="background-color:#DE7243" @click="clickTheme('theme4')"><text v-if="theme == 'theme4'" class="iconfont now_theme" >&#xe6a3;</text></div>
                    <div class="color_theme" style="background-color:#329C92;margin-right:0px;" @click="clickTheme('theme5')"><text v-if="theme == 'theme5'" class="iconfont now_theme" >&#xe6a3;</text></div>
                </div>
                <div class="mask_content_c">
                    <div class="color_theme" style="background-color:#66538B" @click="clickTheme('theme6')"><text v-if="theme == 'theme6'" class="iconfont now_theme" >&#xe6a3;</text></div>
                    <div class="color_theme" style="background-color:#A80000" @click="clickTheme('theme7')"><text v-if="theme == 'theme7'" class="iconfont now_theme" >&#xe6a3;</text></div>
                </div>
                <div class="mask_content_b">
                    <text class="btn_text" style="font-weight:500px" @click="confirmTheme">确定</text>
                    <text class="btn_text" @click="closeTheme">取消</text>
                </div>
            </div>

        </x-mask>
    </div>
    
</template>
<script>
import asCore from "../../common/js/core";
const modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
const storage = weex.requireModule('storage');

export default {
  components: {
    "minibar": require("../../common/component/minbar.vue"),
    "x-cell": require("../../common/component/cell.vue"),
    "x-dialog": require("../../common/component/dialog.vue"),
    "x-mask": require("../../common/component/mask.vue")
  },
  data() {
    return {
      dialogShow: false,
      maskShow: false,
      theme:'theme2',
      bartheme:"theme2"
    };
  },
  created() {
    const loginBroad = new BroadcastChannel("login");
    loginBroad.onmessage = function(event) {
      var data = event.data["success"];
      if (data == 1) {
        navigator.back();
      }
    };
    storage.getItem('theme',event=>{
      if (event.result == "success") {
        this.theme = event.data;
        this.bartheme = event.data; 
      }
    });
  },
  methods: {
    logout: function() {
      this.dialogShow = true;
    },
    cancelBtnClick: function() {
      this.dialogShow = false;
    },
    okBtnClick: function() {
      this.dialogShow = false;
      asCore.logout(function(edata) {
        navigator.push(asCore.localpath + "login.js");
      });
    },
    openPage(event) {
      if (event === "qrDown") {
        navigator.push(asCore.localpath + "yunagileoa/my/seting/qrDown.js");
      }
      if (event === "feedBack") {
        navigator.push(asCore.localpath + "yunagileoa/my/seting/feedBack.js");
      }

      if (event === "about") {
        navigator.push(asCore.localpath + "yunagileoa/my/seting/about.js");
      }
    },
    selectTheme() {
      this.maskShow = true;
    },
    clickTheme(event){
        this.theme = event;
    },
    closeTheme() {
        this.maskShow = false;
    },
    confirmTheme(){
        const theme1 = new BroadcastChannel('theme'); 
        const page  = weex.requireModule("page");  
        storage.setItem('theme',this.theme);
        this.bartheme = this.theme;
        this.closeTheme();
        theme1.postMessage({msg:'ok'});
    }
  }
};
</script>
<style scoped>
.list-margin {
  margin-top: 30px;
}
.wrap {
  background-color: #f5f5f5;
}

.iconfont {
  font-family: iconfont;
}
.logout {
  align-items: center;
  justify-content: center;
}
.logout-text {
  color: #ff3e3e;
  font-size: 30px;
}
.mask_content_t {
  width: 602px;
  height: 70px;
  margin-top: 30px;
  padding-left: 51px;
}
.title_text {
  font-size: 40px;
  line-height: 70px;
}

.hr_div {
  width: 500px;
  height: 1px;
  border-bottom-color: #eeeeee;
  border-bottom-width: 2px;
  margin-left: 51px;
}

.mask_content_c {
  width: 602px;
  padding-top: 20px;
  padding-left: 51px;
  padding-right: 51px;
  flex-direction: row;
}

.color_theme {
  width: 80px;
  height: 80;
  border-radius: 40px;
  margin-right: 25px;
}

.mask_content_b {
  width: 602px;
  height: 100px;
  margin-bottom: 0px;
  padding-right: 51px;
  flex-direction: row;
  align-items: flex-end;
  justify-content: flex-end;
}

.btn_text {
  font-size: 35px;
  margin-left: 50px;
}

.now_theme{
    font-size:42px;
    line-height: 80px;
    text-align: center;
    color:#fff
}
</style>
<template>
    <div class="wrapper">
        <minbar title="新闻资讯" :isPadding="true"></minbar>  
        <!-- list列表 -->
        <list loadmoreoffset="0" ref="mainlist"> 
            <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            <cell  v-for="(newItem,index) in news" :key="index">      
              <div class="content">
                <div class="news_time_div">
                  <text class="news_time">{{newItem.time.zh}}</text>
                </div>
                <div class="news_content">
                  <image class="news_image" 
                         resize="cover" 
                         :src="newItem.poster" 
                         @click="openNews(newItem.title,newItem.link)">
                  </image>
                  <div class="news_title_div" @click="openNews(newItem.title,newItem.link)">
                    <!-- 标题 -->
                    <div :class="['news_padding',newItem.summary?'news_nobottom':'news_noto']" >
                      <text class="news_title_text">{{newItem.title}}</text>
                    </div>
                    <!-- 说明 -->
                    <div v-if="newItem.summary" class="news_padding news_notop">
                      <text class="news_title_smary">{{newItem.summary}}</text>
                    </div>
                  </div>
                  <!-- 其他标题 -->
                  <div class="news_other" 
                       v-if="newItem.related" 
                       v-for="(reItem,i) in newItem.related" 
                       :key="i" 
                       @click="openNews(reItem.title,reItem.link)">
                    <text class="news_other_text">{{reItem.title}}</text>
                    <image v-if="reItem.poster" :src="reItem.poster" class="news_other_image"></image>
                  </div>
                </div>
              </div>
            </cell> 
        </list>  
        <!-- 加载中图标,目前只是第一次进入时显示以及 切换tab 页时显示 -->    
        <x-load :show="xload" :type="'default'" :interval="0" ></x-load> 
    </div>  
 </template>
<script>
import asCore from "../../../common/js/core";
const stream = weex.requireModule("stream");
const navigator = weex.requireModule("navigator");
export default {
  components: {
    "minbar": require("../../../common/component/minbar.vue"),
    "x-load": require("../../../common/component/loading.vue")
  },
  data() {
    return {
      xload: false,
      refreshing: false,
      news: [],
    };
  },
  created() {
    var s = this;
    s.refreshData();
  },
  methods: {
    refreshData() {
      var s = this;
      s.xload = true;
      s.getNewsData();
    },
    /* 数据刷新时做的事情 */
    onrefresh(e) {
      var s = this;
      s.refreshing = true;
      s.getNewsData(()=>{
        s.refreshing = false;
      });
    },
    getNewsData(callback) {
      var s = this;
      stream.fetch(
        {
          method: "post",
          url: "http://dotwe.org/query/weex-playground-app",
          headers: {"Content-Type": "application/x-www-form-urlencoded"},
          type:"json",
          body: "name=news"
        },
        function(ret) {
          if (!ret.ok) {
              asCore.toast("网络连接失败,请检查网络设置");
              s.xload = false;
          } else {
            var data = ret.data; 
            if(data['success']){
              s.news = data.news;
            }
            s.xload = false;
          }
          callback && (callback());
        }
      );
    },
    openNews: function (title,url) {
      navigator.pushParam(asCore.localpath + "common/component/webview.js",{
        title:title,
        url:url
      });
    }
  }
};
</script>
<style scoped>
.wrapper {
  background-color:#f1f1f1
} 
/*刷新加载更多类的样式*/
.refresh {
  width: 750;
  display: flex;
  align-items: center;
}
.indicator {
  margin-top: 30px;
  margin-bottom: 20px;
  height: 70px;
  width: 70px;
  color: #ce2020;
}
.content {
  align-items: center;
}
.news_time_div {
  padding-top:25px;
  padding-bottom:25px;
}
.news_content {
  background-color: #ffffff;
  width: 680px;
  border-radius: 15px;
  border-width: 1px;
  border-color: #deddde;
  margin-bottom: 30px;
}
.news_padding {
  padding:30px;
}
.news_nobottom {
  padding-bottom: 10px;
}
.news_notop {
  padding-top: 0px;
}
.news_title_text {
  font-size:40px;
  color: #333333;
}
.news_other_text {
  flex: 1;
  font-size:35px;
  color: #333333;
}
.news_other_image {
  width: 50px;
  height: 50px;
}
.news_title_smary {
  color: #929292;
  font-size: 30px;
}
.news_image {
  width: 678px;
  height: 330px;
  background-color: #deddde;
  border-top-left-radius: 15px;
  border-top-right-radius: 15px;
}
.news_title_div:active {
  background-color: #eeeeee;
}
.news_other:active {
  background-color: #eeeeee;
}
.news_other {
  padding:30px;
  border-top-width: 1px;
  border-top-color: #deddde;
  flex-direction: row;
}
.news_time {
  color: #ffffff;
  font-size: 25px;
  background-color: #d9d9d9;
  border-radius: 10px;
  padding-left:15px;
  padding-right:15px;
  padding-top:10px;
  padding-bottom:10px;
}
</style>

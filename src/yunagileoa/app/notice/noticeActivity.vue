<template>
    <div class="wrapper" style="background-color:#f5f5f5">
        <minbar title="通知公告" :isPadding="true" :rightButton="true" @rightButtonClick="selectType"> 
            <text slot="right" class="iconfont title-icon">&#xe6e2;</text>
        </minbar>  
        <!-- tab切换页 -->
        <div class="btn_bar" >
            <div @click="onclick(0)" class="btn">
                <text  :class="['btn_text',isClick?'btn_text2':'' ]">未读</text>
            </div>
            <div class="btn" @click="onclick(1)">
                <text :class="['btn_text',isClick2?'btn_text2':'' ]" >已读</text>
            </div>
        </div>
        <!-- tab切换页的线条 -->
        <div style="width:750px;height:5px;position:relative">
            <div style="width:750px;height:1px;background-color:#e2e2e2;z-index:1;position:absolute;top:0"></div>
            <div ref="line" :style="{'width':lineWidth+'px','margin-left':lineLeft+'px','background-color':theme}" style="height: 5;z-index:2;position:absolute;top:0"></div>
        </div>
        <!-- 暂无记录 -->
        <x-nodata v-if="noRows" 
          type="icon" 
          title="暂无通知公告数据~" 
          :iconStyle="{code:'\ue716',fontSize:210,color:'#DBDCE0'}">
        </x-nodata>
        <!-- list列表 -->
        <list v-else @loadmore="onloading" loadmoreoffset="0" ref="mainlist"> 
            <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            <cell  v-for="(c,index) in notices" :key="index">      
                <div class="content_c" @click="openDetail(c.id,index)">
                    <div class="user_div"> 
                        <div>
                            <text  class="content_text content-padding">{{c.title}}</text>
                            <div class="content_div">
                                <text  class="content_bottom_text content-padding">{{c.createPsnName}}  {{c.createTime}}</text>
                            </div>
                            <div>
                                <text class="content_center_text content-padding">{{c.content}}</text>
                            </div> 
                        </div>
                    </div>
                </div>
            </cell> 
            <!-- 加载更多数据,目前最佳效果则采用 cell的方式进行加载 , 当数据加载完成时直接隐藏 -->
            <cell class="loading" v-if="loadinging">
                <image class="loadimage" :src="loadmoreSrc"></image>
                <text class="indicator-text">{{psText}}</text>
            </cell>  
        </list>  
        <!-- 右侧的弹出菜单 -->
        <x-popup :show="rightMenu" pos="right"  width="600"   @popupClicked="closeRightMenu">
            <div slot="popup_content" >
                <div style="border-bottom-width:1px;border-bottom-color:#ddd; "><text class="rightMenu_title">快速过滤</text></div>
                <div style="padding:10px">
                    <text style="color:#666666;font-size:26px">通知类型</text>
                    <div style="flex-direction: row;justify-content:space-between;padding-right:20px;margin-top:10px">
                        <button class="x-fl-button">全部</button>     
                    </div>
                             
                </div>
            </div> 
        </x-popup>
        <!-- 加载中图标,目前只是第一次进入时显示以及 切换tab 页时显示 -->    
        <x-load :show="loadinging2" :type="'default'" :interval="0" ></x-load>
    </div>  
 </template>
<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core";
const animation = weex.requireModule("animation");
const storage = weex.requireModule("storage");
const navigator = weex.requireModule("navigator");
const paramBroad = new BroadcastChannel('notice');
export default {
  components: {
    "minbar": require("../../../common/component/minbar.vue"),
    "x-popup": require("../../../common/component/popup.vue"),
    "x-load": require("../../../common/component/loading.vue"),
    "x-nodata":require("../../../common/component/nodata.vue")
  },
  data() {
    return {
      bsessionid: "",
      rootPath: "",
      isClick: true,
      isClick2: false,
      lineWidth: 375,
      lineLeft: 0,
      fromindex: 0,
      theme: "#2e3238",
      loadinging: false,
      newloading:false,
      loadinging2: false,
      notices: [],
      loadmoreSrc:asCore.localpath + "img/juhua.gif",
      psText: "正在加载更多...",
      param: {
        page:1,
        rows: 8,
        noticeTypeId: "",
        examineStateCode: "0"
      },
      firstL: true,
      refreshing: false,
      noRows: false,
      all: false,
      rightMenu: false
    };
  },
  created() {
    var s = this;
    Utils.getThemeColor((color, theme) => {
      if (theme == "theme1") {
        s.theme = "#F15A49";
      } else {
        s.theme = color;
      }
    });
    paramBroad.onmessage = s.noticecallback;
    this.getPath();
  },
  methods: {
    getPath() {
      var s = this;
      asCore.getBsessionid(bid => {
        if (bid) {
          s.bsessionid = bid;
          s.rootPath = asCore.rootPath;
          /* 去刷新数据 */
          s.refreshData();
        }
      });
    },
    /*加载更多数据*/
    onloading() {
        var s = this;
        if(s.all) return;
        s.param.page += 1;
        s.getData("loadmore",() => {
            s.loadinging2 = false;
            s.refreshing = false;
        });
    },
    refreshData(){
        var s = this;
        /* 自定义的刷新 */
        s.loadinging2 = true;
        s.onrefresh();
    },
    /* 数据刷新时做的事情 */
    onrefresh(e) {
      var s = this;
      s.noRows = false;
      s.all = false;
      /* 初始化页 */
      s.param.page = 1;
      /* 显示默认的刷新 */
      if(!s.firstL){
        s.refreshing = true;
      }
      s.firstL = false;
      /* 重置加载更多 */
      s.$refs.mainlist.resetLoadmore();
      /* 区分refresh 和loadmore */
      this.getData("refresh",() => {
        s.loadinging2 = false;
        s.refreshing = false;
      });
    },
    onclick(event) {
        var s = this;
        s.noRows = false;
        /* 设置参数 */
        s.param.examineStateCode = event;
        s.param.page = 1;
        /* 线条移动 */
        s.animateLine(event);
        /* 显示默认的刷新 */
        s.refreshing = true;
        s.loadinging = false;
        /* 自定义的刷新 */
        s.loadinging2 = true;
        s.getData("refresh",() => {
            s.loadinging2 = false;
            s.refreshing = false;
        });
    },
    selectType() {
      if (!this.rightMenu) {
        this.rightMenu = true;
      } else {
        this.rightMenu = false;
      }
    },
    closeRightMenu() {
      this.rightMenu = false;
    },
    /**@method animateLine
     * @description tab页线条移动的动画
     * @param toindex 要移动到的索引
     * @author zj
     */
    animateLine(toindex) {
      if (this.fromindex == toindex) {
        return;
      }
      if (toindex == 0) {
        this.isClick = true;
        this.isClick2 = false;
      } else {
        this.isClick = false;
        this.isClick2 = true;
      }
      var c = this.$refs.line;
      var to = 375;
      if (toindex == 0) {
        to = 0;
      }
      animation.transition(
        c, {
          styles: {
            transform: "translate(" + to + "px,0px)"
          },
          duration: 300, //ms
          timingFunction: "ease",
          delay: 0 //ms
        },
        function() {}
      );
      this.fromindex = toindex;
    },
    
    /**@method openDetail
     * @description 打开通知公告详情
     * @param id 通知公告id
     * @param index 数组索引，用于回调时删除数组
     * @author zj
     */
    openDetail(id,index) {
      var statusCode = this.param.examineStateCode;
      if (id) {
        navigator.pushParam(
          asCore.localpath + "yunagileoa/app/notice/noticeDetailActivity.js",{
            noticeId: id,
            statusCode: statusCode,
            index:index
          }
        );
      }
    },
    /**@method noticecallback
     * @description 将通知公告设置为已读之后的callback
     * @param event 传回来的参数
     * @author zj
     */
    noticecallback(event){
      this.notices.splice(event.data.index,1);
      if(this.notices.length<=0){
        this.noRows = true;
      }
    },
    /**@method getData
     * @description 刷新和加载更多的通知公告数据
     * @param retype 刷新数据的类型 [refresh,loadmore] 刷新和加载更多
     * @param callback 刷新成功回调
     * @author zj
     */
    getData(retype,callback) {
      var s = this;
      asCore.post("/notice/queryCompanyNoticeAction", s.param, (data, flag) => {
        if (data["status"] == "SUCCESS") {
          var pData = JSON.parse(data["data"]["data"]);
          var rows = pData.rows;
          var arrs = [];
          if (rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
              var id = rows[i].id;
              var title = rows[i].title;
              var createPsnName = rows[i].createPsnName;
              var createTime = rows[i].createTime;
              var content = s.getHtmlText(rows[i]["content"]);
              createTime = Utils.formatDate(new Date(createTime), "yyyy-MM-dd");
              arrs.push({
                id: id,
                title: title,
                createTime: createTime,
                createPsnName: createPsnName,
                content: content
              });
            }
            if(retype==="refresh"){
                s.notices = arrs;
            }else{
                s.notices.push(...arrs);
            }
            /* 判断是否已加载全部数据 */
            if (rows.length % 8 != 0 && s.param.page >= 1) {
                s.all = true;
                /* 隐藏加载更多 */
                s.loadinging = false;
                //asCore.toast("已经加载全部数据");
            }else{
                s.loadinging = true;
                /* 重置加载更多 */
                s.$refs.mainlist.resetLoadmore();
            }
            callback && (callback());
          } else {
            if(s.param.page > 1){
              s.all = true;
              /* 隐藏加载更多 */
              s.loadinging = false;
            }else{
              s.notices = [];
              s.noRows = true;
            }
            callback && (callback());
          }
        } else {
          asCore.toast("获取通知公告失败！");
          callback && (callback());
        }
        
      });
    },
    /**@method getHtmlText
     * @description 处理html代码
     * @param html 要处理的html内容
     * @returns String类型 返回处理之后的内容
     * @author zj
     */
    getHtmlText(html) {
      //asCore.alert(html)
      html += "";
      if (html && html != "null" && html != null) {
        var content = html
          .replace(/<\/?.+?>/g, "")
          .replace(/[ ]|[&nbsp;]/g, "");
        return content;
      } else {
        return "无内容";
      }
    }
  }
};
</script>
<style scoped>
/*字体图标类*/
.iconfont {
  font-family: iconfont;
}
/*刷新加载更多类的样式*/
.refresh {
  width: 750;
  display: flex;
  align-items: center;
}
.loading{
    height:100px;
    padding-top:30px;
    padding-bottom:30px;
    background-color:#ffffff;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
.indicator {
  margin-top: 30px;
  margin-bottom: 20px;
  height: 70px;
  width: 70px;
  color: #ce2020;
}
.indicator-text {
  color: #888888;
  font-size: 30px;
  text-align: center;
}
.loadimage {
  height:40px;
  width:40px;
}

.no_text {
  font-size: 35px;
  text-align: center;
  line-height: 300px;
  color: #888;
}

/*tab切换页的样式*/
.btn_bar {
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 750px;
  height: 85px;
  z-index: 9999;
}

.btn {
  flex: 1;
  height: 85px;
  justify-content: center;
  background-color: #ffffff;
}

.btn:active {
  background-color: #e1e1e1;
}

.btn_text {
  font-size: 28px;
  color: #888;
  line-height: 40px;
  text-align: center;
}

.btn_text2 {
  font-size: 28px;
  color: #555555;
  line-height: 40px;
  text-align: center;
}

.content_c {
  background-color: white;
  margin-top: 25px;
}
.content_c:active {
  background-color: #e2e2e2;
}
.user_div {
  padding-top: 30px;
  padding-bottom: 30px;
  flex-direction: row;
  justify-content: flex-start;
}
.head {
  width: 80px;
  height: 80px;
  border-radius: 40px;
  margin-right: 15px;
  float: right;
}
.content_text {
  line-height: 40px;
  font-size: 33px;
}
.content-padding {
  padding-left: 30px;
  padding-right: 30px;
}
.content_center_text {
  font-size: 30px;
  width: 750px;
  lines: 4;
  text-overflow: ellipsis;
  color: #666666;
  padding-top: 25px;
}
.content_bottom_text {
  flex: 1;
  line-height: 43px;
  color: #999999;
  font-size: 26px;
  border-bottom-width: 1px;
  border-bottom-color: #e2e2e2;
  padding-bottom: 20px;
}

.title-icon {
  font-size: 35px;
  color: #fff;
  font-weight: 300;
}
.rightMenu_title {
  color: #666666;
  line-height: 100px;
  width: 600px;
  text-align: center;
  font-size: 26px;
}
.x-fl-button {
  border: none;
  background-color: #f4f4f4;
  border-radius: 5px;
  margin-right: 5px;
  width: 300px;
  height: 70px;
  font-size: 28px;
  color: #333333;
  text-align: center;
  justify-content: center;
  padding-left:20px;
  line-height: 70px;
}
.content_div {
  flex-direction: row;
  width: 750px;
}
.content_bottom_text2 {
  flex: 1;
  line-height: 40px;
  color: #999999;
  font-size: 26px;
  border-bottom-width: 1px;
  border-bottom-color: #e2e2e2;
  padding-bottom: 20px;
  text-align: right;
  padding-right: 30px;
}
</style>

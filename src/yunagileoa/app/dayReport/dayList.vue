<!-- 
  1.日报标题栏上的箭头加上动画特效
  2.日报内容加上过期填写标签
  3.加上返回顶部功能
  4.记得处理手机上html的渲染问题
  5.将我的日报和他人的日报进行分离,尽量模块化
 -->
<template>
    <div class="wrapper">
        <minbar :title="title" :isPadding="true" :rightButton="true" @titleButtonClick="openMenu" @rightButtonClick="selectDate2">
            <text slot='other-title' :class="['iconfont','title-icon2',isIos()?'iostop':'']">&#xee4e;</text>
            <text slot="right" class="iconfont title-icon">&#xe6e2;</text>
        </minbar>
        <!-- 暂无记录 -->
        <x-nodata v-if="noRows" 
          type="icon" 
          title="暂无工作日报~" 
          :iconStyle="{code:'\ue6c2',fontSize:180,color:'#DBDCE0'}">
        </x-nodata>
        <list v-else loadmoreoffset="0" @loadmore="onloading" ref="mainlist">
            <refresh class="refresh"  @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
                <loading-indicator class="indicator"></loading-indicator>
            </refresh>
            <cell class="day_content" v-for="(c,index) in dayReports" :key="index">
                <div v-if="type == 'my'">
                    <div class="day_content_t">
                        <text class="report_date">{{c.time}}</text>
                        <text v-if="type == 'my'&& c.operation == 'add'" class="iconfont icon_edit" @click="openEdit({'type':'add','dayTime':c.time2})">&#xe623;</text>
                        <text v-if="type == 'my'&& c.operation == 'edit'" class="iconfont icon_edit" @click="openEdit({'type':'edit','dayTime':c.time2})">&#xe690;</text>
                    </div>   
                    <div class="day_content_c" @click="openDetail(c.id)">
                        <div class="user_div">
                            <image v-if="type == 'other'" class="head" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+c.psnCode"></image>
                            <div :class="[type =='my'?'name_ctime2':'name_ctime',]">
                                <text  :class="[type =='my'?'name_text2':'name_text',]">{{c.name}}</text>
                                <text  :class="[type =='my'?'ctime_text2':'ctime_text',]">{{c.ctime}}</text>
                            </div> 
                        </div>
                        <text class="content_text">{{c.content}}</text>
                    </div>
                    <div class="day_content_b">
                        <div class="b_btn">
                            <div class="div_btn" >
                                <text class="iconfont btn_icon">&#xe618;</text>
                                <text class="count_text">(0)</text>
                            </div>
                            <div class="div_btn">
                                <text class="iconfont btn_icon">&#xe678;</text>
                                <text class="count_text">(0)</text>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-if="type=='other'">
                    <!-- <div class="day_content_t day_content_t2">
                        <text class="report_date">{{c.time}}</text>
                    </div> -->
                        <div class="day_content_c" @click="openDetail(c.id)">
                            <div class="user_div">
                                <image v-if="type == 'other'" class="head" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+c.psnCode"></image>
                                <div class="name_ctime">
                                    <text  class="name_text">{{c.name}}</text>
                                    <text  class="ctime_text">{{c.ctime}}</text>
                                </div>
                            </div>
                            <text class="content_text">{{c.content}}</text>
                        </div>
                        <div class="day_content_b">
                            <div class="other_time">
                              <text class="other_date">{{c.time2}}</text>
                            </div>
                            <div class="b_btn">
                                <div class="div_btn" >
                                    <text class="iconfont btn_icon">&#xe618;</text>
                                    <text class="count_text">(0)</text>
                                </div>
                                <div class="div_btn">
                                    <text class="iconfont btn_icon">&#xe678;</text>
                                    <text class="count_text">(0)</text>
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
        <x-popup :show="showMenu" pos="top"  height="363"   @popupClicked="closeMenu">
            <div slot="popup_content" class="popup_content">
                <div class="menu_div" @click="changeClick('my')">
                    <text :class="['menu_title',type=='my'?theme:'']">我的工作日报</text>
                    <text v-if="type == 'my'" :class="['menu_click_icon', 'iconfont', type=='my'?theme:'']">&#xe671;</text>
                </div>
                <div class="bottom_border"></div>
                <div  class="menu_div" @click="changeClick('other')">
                    <text :class="['menu_title',type=='other'?theme:'']">他人的工作日报</text>
                    <text v-if="type == 'other'" :class="['menu_click_icon', 'iconfont', type=='other'?theme:'']">&#xe671;</text>
                </div>
                <div class="bottom_border"></div>
                <div class="menu_div" >
                    <text class="menu_title">评论我的</text>
                    <text v-if="type == 'comment'" class="menu_click_icon iconfont theme">&#xe671;</text>
                </div>
                <div class="bottom_border"></div>
                <div class="menu_div" >
                    <text class="menu_title">回复我的</text>
                    <text v-if="type == 'feedback'" class="menu_click_icon iconfont theme">&#xe671;</text>
                </div>
            </div>
        </x-popup>
            <x-popup :show="rightMenu" pos="right"  width="600"   @popupClicked="closeRightMenu">
                <div slot="popup_content" class="right_popup_content">
                    <div class="search_div" >
                        <text class="r_m_title">搜索</text>
                        <div class="search_bar">
                            <input class="search_input" placeholder="搜索" v-model="param.keywords"/>
                            <div class = 'search_btn'>
                                <text class="btn_text" @click="clickSearch">查询</text>
                            </div>
                        </div> 
                    </div>
                    <div class="search_div" style="margin-top:30px;">
                        <text class="r_m_title">按时间过滤</text>
                        <div style="flex-direction: row;justify-content:space-between;padding-left:20px;padding-right:20px;">
                            <div class="selectDate_div">
                                <input ref="startT" class="selectDate_input" placeholder ="开始时间" @focus="selectDate(1)" :value="param.startTime"/>
                                <image class="rilidiv" :src="riliSrc"></image>
                            </div> 
                            <div class="selectDate_div">
                                <input ref = "endT" class="selectDate_input" placeholder ="结束时间 " @focus="selectDate(2)" :value="param.endTime"/>
                                <image class="rilidiv" :src="riliSrc"></image>
                            </div>
                        </div>
                        <div class="clean_time">
                            <text :class="['clean_time_text',theme]" @click="cleanTime(1)">清除开始时间</text>
                            <text :class="['clean_time_text',theme]" @click="cleanTime(2)">清除结束时间</text>
                        </div>

                    </div>
                </div>
            </x-popup>
        <x-load :show="loadinging2" :type="'default'" :interval="0" ></x-load>
    </div>

</template>

<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core";
const animation = weex.requireModule("animation");
const storage = weex.requireModule("storage");
const picker = weex.requireModule("picker");
const navigator = weex.requireModule("navigator");
export default {
  components: {
    "minbar": require("../../../common/component/minbar.vue"),
    "x-popup": require("../../../common/component/popup.vue"),
    "x-load": require("../../../common/component/loading.vue"),
    "x-nodata" :require("../../../common/component/nodata.vue")
  },
  data() {
    return {
      bsessionid: "",
      rootPath: "",
      title: "我的工作日报",
      type: "my",
      psnCode: "",
      param: {
        page: 1,
        rows: 10,
        psnCode: "",
        endTime: "",
        keywords: "",
        startTime: ""
      },
      dayReports: [],
      loadinging: false,
      loadmoreSrc: asCore.localpath + "img/juhua.gif",
      riliSrc: asCore.localpath + "img/rili.png",
      loadinging2: false,
      firstL: true,
      refreshing: false,
      showMenu: false,
      theme: "theme1",
      showContent: true,
      noRows: false,
      sDate: "",
      psText: "正在加载更多...",
      rightMenu: false,
      sValue: "",
      all: false
    };
  },

  beforeCreate() {
    storage.getItem("theme", event => {
      if (event.data) {
        this.theme = event.data;
        if (event.data == "theme1") {
          this.theme = "redtheme";
        }
      }
    });
  },
  created() {
    this.getPath();
  },
  methods: {
    getPath() {
      var s = this;
      asCore.getBsessionid(bid => {
        if (bid) {
          s.bsessionid = bid;
          s.rootPath = asCore.rootPath;
          /* 一开始设置一个默认值 */
          s.type = 'my';
          s.refreshData();
        }
      });
      
    },
    /* 刷新数据 */
    refreshData() {
      var s = this;
      /* 自定义的刷新 */
      s.loadinging2 = true;
      s.loadinging = false;
      s.onrefresh();
    },
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
      /* 首先要先获取到 context 之前的写法是不对的 */
      asCore.getContext(context => {
        s.psnCode = context.currentPersonCode;
        s.param.psnCode = context.currentPersonCode;
        /* 在这里判断是要加载 自己的数据还是其他人的数据 */
        if(s.type === "my"){
          s.getData("refresh", () => {
            s.loadinging2 = false;
            s.refreshing = false;
          });
        }else{
          s.param.psnCode = "";
          s.getOtherData("refresh", () => {
            s.loadinging2 = false;
            s.refreshing = false;
          });
        }
      });
    },
    /*数据刷新和加载*/
    onloading() {
      var s = this;
      if (s.all) return;
      s.param.page += 1;
      
      if(s.type === "my"){
        s.getData("loadmore", () => {
          s.loadinging2 = false;
          s.refreshing = false;
        });
      }else{
        s.getOtherData("loadmore", () => {
          s.loadinging2 = false;
          s.refreshing = false;
        });
      } 
    },
    /* 获取其他人的数据 */
    getOtherData(retype,callback) {
      var s = this;
      asCore.post("/oa/dailyReportQueryAction", s.param, (data, flag) => {
          if (data["status"] != "SUCCESS") return;
          var drData = JSON.parse(data["data"]["data"]);
          var rows = drData.rows;
          if (rows.length > 0) {
            var arrs = [];
            var operation = "other";
            for(var i=0;i<rows.length;i++){
              var dayTime = s.getFormatTime(rows[i].dayTime);
              var ctime = s.getFormatTime(rows[i].createTime, "123");
              var dayTime2 = Utils.formatDate(
                new Date(rows[i].dayTime),
                "yyyy-MM-dd"
              );
              
              arrs.push({
                  name: rows[i].createPsnName,
                  psnCode: rows[i].createPsnCode,
                  content: rows[i].remark,
                  time: dayTime,
                  ctime: ctime,
                  id: rows[i].id,
                  operation: operation,
                  time2: dayTime2
              });
            }
            
            if (retype === "refresh") {
              s.dayReports = arrs;
            } else {
              s.dayReports.push(...arrs);
            }
            /* 判断是否已加载全部数据 */
            if (rows.length % 10 != 0 && s.param.page >= 1) {
              s.all = true;
              /* 隐藏加载更多 */
              s.loadinging = false;
              //asCore.toast("已经加载全部数据");
            } else {
              s.$refs.mainlist.resetLoadmore();
              s.loadinging = true;
            }
            callback && (callback());
          }else{
            if (s.param.page > 1) {
              s.all = true;
              /* 隐藏加载更多 */
              s.loadinging = false;
            } else {
              s.showContent = false;
              s.dayReports = [];
              s.noRows = true;
            }
            callback && (callback());
          }
          callback && (callback());
      });
    },
    /*获取数据*/
    getData(retype, callback) {
      var s = this;
      asCore.post("/oa/dailyReportQueryAction", s.param, (data, flag) => {
        if (data["status"] != "SUCCESS") return;
        var drData = JSON.parse(data["data"]["data"]);
        var rows = drData.rows;
        if (rows.length > 0) {
          var arrs = [];
          var psnName, psnCode, content, dayTime = "";
          var ctime, type, id, operation, dayTime2 = "";
          var today = Utils.formatDate(new Date(), "yyyy-MM-dd");
          for (var i = 0; i < rows.length; i++) {
            psnName = rows[i].createPsnName;
            psnCode = rows[i].createPsnCode;
            content = rows[i].remark;
            dayTime = s.getFormatTime(rows[i].dayTime);
            ctime = s.getFormatTime(rows[i].createTime, "123");
            dayTime2 = Utils.formatDate(
              new Date(rows[i].dayTime),
              "yyyy-MM-dd"
            );
            /* 只有刷新数据时才需要判断是否有当天的日报数据,否则直接从 */
            /*判断是否有当天的日报*/
            if (
              i == 0 &&
              retype == "refresh" &&
              !s.param.endTime &&
              !s.param.startTime &&
              !s.param.keywords
            ) {
              /*判断是否需要插入未填写日报*/
              var cday =  (new Date().getTime() - new Date(rows[i].dayTime).getTime()) /  (1000 * 60 * 60 * 24);
              if (cday > 1) {
                arrs.push({
                  name: psnName,
                  psnCode: psnCode,
                  content: "无日报内容",
                  time: "今天",
                  operation: "add",
                  time2: today
                });
                if (cday >= 2) {
                  for (var z = 1; z < cday - 1; z++) {
                    var cdate = new Date(
                      new Date().setDate(new Date().getDate() - z)
                    );
                    var dayTime3 = Utils.formatDate(cdate, "yyyy-MM-dd");
                    operation = "add";
                    arrs.push({
                      name: psnName,
                      psnCode: psnCode,
                      content: "无日报内容",
                      time: s.getFormatTime(cdate),
                      operation: operation,
                      time2: dayTime3
                    });
                  }
                }
              }
            } else {
              /* 这里添加的是加载更多时添加的数据 */
              /*添加无内容记录 */
              if (!s.param.keywords) {
                /* 先得到在数据中最后一天的时间 */
                var lastTime = null;
                if(s.param.startTime||s.param.endTime){
                  lastTime = new Date(rows[i].dayTime);
                }else if(arrs.length == 0){
                  lastTime = new Date(s.dayReports[s.dayReports.length - 1]['time2']);
                }else {
                  lastTime = new Date(arrs[arrs.length - 1]['time2']);
                }
                
                //asCore.alert(rows[i].dayTime);
                var cday = (lastTime.getTime() - new Date(rows[i].dayTime).getTime()) / (1000 * 60 * 60 * 24);
                //asCore.alert(cday);
                cday = parseInt(cday);

                if (cday > 2) {
                  for (var z = 1; z < cday; z++) {
                    var nDate = new Date(lastTime);
                    var cdate = new Date(nDate.setDate(nDate.getDate() - z));
                    dayTime2 = Utils.formatDate(cdate, "yyyy-MM-dd");
                    operation = "add";
                    arrs.push({
                      name: psnName,
                      psnCode: psnCode,
                      content: "无日报内容",
                      time: s.getFormatTime(cdate),
                      operation: operation,
                      time2: dayTime2
                    });
                  }
                }
              }
            }
            id = rows[i].id;
            operation = "edit";
            arrs.push({
                name: psnName,
                psnCode: psnCode,
                content: content,
                time: dayTime,
                ctime: ctime,
                id: id,
                operation: operation,
                time2: dayTime2
            });
          }
           if (retype === "refresh") {
             s.dayReports = arrs;
           } else {
             s.dayReports.push(...arrs);
           }
          /* 判断是否已加载全部数据 */
          if (rows.length % 10 != 0 && s.param.page >= 1) {
            s.all = true;
            /* 隐藏加载更多 */
            s.loadinging = false;
            //asCore.toast("已经加载全部数据");
          } else {
            s.loadinging = true;
            s.$refs.mainlist.resetLoadmore();
          }
          callback && (callback());
        } else {
          if (s.param.page > 1) {
            s.all = true;
            /* 隐藏加载更多 */
            s.loadinging = false;
          } else {
            s.showContent = false;
            s.dayReports = [];
            s.noRows = true;
          }
          callback && (callback());
        }
      });
    },

    /*时间格式*/
    getFormatTime(time, type) {
      var nTime = new Date();
      var nTimeDate = new Date(Utils.formatDate(nTime, "yyyy-MM-dd"));
      var cTime = new Date(time);
      var cTimeDate = new Date(Utils.formatDate(cTime, "yyyy-MM-dd"));
      //相差多少天 hh:mm:ss
      var dayValue = Utils.formatDate(cTime, "yyyy-MM-dd");
      var timeValue = Utils.formatDate(cTime, "hh:mm:ss");
      var cday =
        (nTimeDate.getTime() - cTimeDate.getTime()) / (1000 * 60 * 60 * 24);

      if (cday <= 0) {
        if (type) {
          return "今天  " + timeValue;
        } else {
          return "今天 ";
        }
      } else if (cday == 1) {
        if (type) {
          return "昨天  " + timeValue;
        } else {
          return "昨天 ";
        }
      } else if (cday == 2) {
        if (type) {
          return "前天  " + timeValue;
        } else {
          return "前天  ";
        }
      } else {
        var weekDay = new Array(7);
        weekDay[0] = "周日";
        weekDay[1] = "周一";
        weekDay[2] = "周二";
        weekDay[3] = "周三";
        weekDay[4] = "周四";
        weekDay[5] = "周五";
        weekDay[6] = "周六";
        if (type) {
          return Utils.formatDate(cTime, "yyyy-MM-dd   hh:mm:ss");
        } else {
          return (
            weekDay[cTime.getDay()] +
            " " +
            Utils.formatDate(cTime, "yyyy-MM-dd")
          );
        }
      }
    },

    /*顶部菜单*/
    openMenu() {
      if (!this.showMenu) {
        this.showMenu = true;
      } else {
        this.showMenu = false;
      }
    },
    closeMenu() {
      this.showMenu = false;
    },

    changeClick(e) {
      var s = this;
      
      if (e == "other") {
        s.param.psnCode = "";
        s.title = "他人的工作日报";
        s.type = "other";
      } else {
        s.type = "my";
        s.title = "我的工作日报";
      }
      s.dayReports = [];
      s.closeMenu();
      s.refreshData();
    },

    /**选择日期 */
    selectDate(e) {
      var s = this;
      var sDate = "";
      if (e == 1) {
        sDate = s.param.startTime;
        this.$refs.startT.blur();
      } else {
        sDate = s.param.endTime;
        this.$refs.endT.blur();
      }

      picker.pickDate(
        {
          value: sDate
        },
        event => {
          if (event.result === "success") {
            if (e == 1) {
              s.param.startTime = event.data;
            }
            if (e == 2) {
              s.param.endTime = event.data;
            }
            s.refreshData();
          }
        }
      );
    },
    /* 查看详细 */
    openDetail(id) {
      if (id) {
        navigator.pushParam(
          asCore.localpath + "yunagileoa/app/dayReport/dayDetail.js",
          {
            dayDetailId: id
          }
        );
      }
    },
    /**打开sendDay页面 */
    openEdit(event) {
      navigator.pushParam(
        asCore.localpath + "yunagileoa/app/dayReport/sendDay.js",
        {
          dayTime: event.dayTime,
          editType: event.type
        }
      );
    },
    /* 打开右侧过滤查询菜单 */
    selectDate2() {
      if (!this.rightMenu) {
        this.rightMenu = true;
      } else {
        this.rightMenu = false;
      }
    },
    /* 关闭右侧弹出的菜单 */
    closeRightMenu() {
      this.rightMenu = false;
    },
    /* 搜索框的赋值 */
    searchInput(e) {
      this.sValue = e.value;
      this.param.keywords = this.sValue;
      this.refreshData();
      
    },
    /* 点击搜索按钮 */
    clickSearch() {
      this.refreshData();
    },
    /* 清除时间 */
    cleanTime(e) {
      if (e == 1) {
        this.param.startTime = "";
      }
      if (e == 2) {
        this.param.endTime = "";
      }
      this.refreshData();
    },
    isIos () {
      const { platform } = weex.config.env;
      return platform.toLowerCase() === 'ios';
    }
  }
};
</script>

<style scoped lang="scss">
@import "./css/theme.scss";
.wrapper {
  background-color: #f5f5f5;
}
.iconfont {
  font-family: iconfont;
}
/*刷新加载更多类的样式*/
.refresh {
  width: 750;
  display: flex;
  align-items: center;
}
.loading {
  height: 100px;
  padding-top: 30px;
  padding-bottom: 30px;
  background-color: #ffffff;
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
  height: 40px;
  width: 40px;
}

.no_text {
  font-size: 35px;
  text-align: center;
  line-height: 300px;
  color: #888;
}

.title-icon {
  font-size: 35px;
  color: #fff;
  font-weight: 300;
}
.title-icon2 {
  color: #fff;
  font-weight: 300;
  font-size: 30px;
  margin-top:25px;
  padding-bottom:20px;
  margin-left:5px;
}

.day_content {
  width: 750px;
  background-color: #fff;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}
.day_content_t {
  width: 750px;
  height: 80px;
  flex-direction: row;
  padding-left: 20px;
  padding-right: 20px;
  justify-content: space-between;
  align-items: center;
}

.day_content_t2 {
  background-color: #f7f7f7;
}

.report_date {
  font-size: 30px;
  font-weight: 700px;
  color: #1AA6DE;
}

.icon_edit {
  width:60px;
  height:50px;
  text-align: right;
  color: #c1c1c1;
  font-size: 40px;
  align-items: flex-end;
}

.day_content_c {
  width: 750px;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 20px;
  flex-direction: column;
}

.day_content_c:active {
  background-color: #e2e2e2;
}

.user_div {
  width: 710px;
  height: 80px;
  flex-direction: row;
  justify-content: flex-start;
  margin-bottom: 17px;
}

.head {
  width: 80px;
  height: 80px;
  border-radius: 40px;
  margin-right: 15px;
}

.name_ctime {
  height: 80px;
  flex-direction: column;
  justify-content: space-between;
}

.name_ctime2 {
  height: 60px;
  flex-direction: row;
  justify-content: space-between;
}

.ctime_text {
  font-size: 20px;
  line-height: 30px;
  color: #9B9B9B;
}

.ctime_text2 {
  font-size: 20px;
  line-height: 60px;
  padding-top: 5px;
  padding-left: 20px;
  color: #9B9B9B;
}

.name_text {
  font-size: 30px;
  line-height: 50px;
  color: #1AA6DE;
}

.name_text2 {
  font-size: 30px;
  line-height: 60px;
  color: #303030;
}

.day_content_b {
  width: 750px;
  height: 60px;
  flex-direction: row;
}

.other_time {
  flex: 1;
  padding-left:20px;
}
.b_btn {
  flex: 1;
  padding-right: 20px;
  flex-direction: row;
  justify-content: flex-end;
}

.div_btn {
  padding-left: 5px;
  padding-right: 5px;
  flex-direction: row;
  justify-content: space-between;
}

.btn_icon {
  font-size: 35px;
  line-height: 60px;
  color: #8992a3;
}

.count_text {
  font-size: 26px;
  line-height: 60px;
  color: #8992a3;
}

.content_text {
  font-size: 30px;
  padding-bottom: 20px;
  color: #4d4d4d;
  line-height: 40px;
}

.popup_content {
  border-top-color: #e2e2e2;
  border-top-width: 2px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.menu_div {
  width: 750px;
  height: 90px;
  padding-left: 40px;
  padding-right: 40px;
  flex-direction: row;
  justify-content: space-between;
}

.menu_div:active {
  background-color: #eee;
}

.menu_title {
  font-size: 30px;
  line-height: 90px;
  color: #888;
}

.menu_click_icon {
  font-size: 30px;
  line-height: 90px;
  align-items: flex-end;
}

.theme {
  color: #329c92;
}

.bottom_border {
  margin-left: 40px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.right_popup_content {
  padding-top: 11px;
}

.search_div {
  width: 600px;
  padding-top: 15px;
  padding-bottom: 10px;
}

.r_m_title {
  font-size: 26px;
  color: #5e5e5e;
  margin-left: 20px;
  margin-bottom: 20px;
}

.search_bar {
  width: 600px;
  flex-direction: row;
  justify-content: space-around;
  padding-left: 20px;
  padding-right: 20px;
}

.search_input {
  width: 410px;
  height: 70px;
  line-height: 70px;
  background-color: #eeeeee;
  border-radius: 6px;
  text-align: center;
  font-size: 30px;
}

.search_btn {
  height: 70px;
  width: 130px;
  background-color: #fff;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
  border-top-color: #e2e2e2;
  border-top-width: 1px;
  border-left-color: #e2e2e2;
  border-left-width: 1px;
  border-right-color: #e2e2e2;
  border-right-width: 1px;
  border-radius: 5px;
  align-items: center;
  justify-content: space-around;
}

.search_btn:active {
  background-color: #eeeeee;
}

.btn_text {
  font-size: 25px;
  color:#555555;
}

.selectDate_div {
  width: 265px;
  height: 70px;
  background-color: #fff;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
  border-top-color: #e2e2e2;
  border-top-width: 1px;
  border-left-color: #e2e2e2;
  border-left-width: 1px;
  border-right-color: #e2e2e2;
  border-right-width: 1px;
  border-radius: 5px;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  padding-left: 10px;
  padding-right: 10px;
}

.selectDate_input {
  width: 200px;
  height: 70px;
  font-size: 26px;
  padding-left: 10;
}

.selectDate_div_text {
  font-size: 30px;
  line-height: 70px;
  color: #5e5e5e;
}
.redtheme {
  color: #f15a49;
}

.clean_time {
  width: 600px;
  height: 60px;
  flex-direction: row;
  justify-content: space-around;
  padding-left: 20px;
  padding-right: 20px;
  margin-top: 40px;
}

.clean_time_text {
  color: #245eb9;
  font-size: 25px;
}
.rilidiv {
  height:30px;
  width:30px;
}
.other_date {
  padding-top:15px;
  color:#666666;
  font-size:28px;
}
.iostop {
    margin-top:32px;
}
</style>
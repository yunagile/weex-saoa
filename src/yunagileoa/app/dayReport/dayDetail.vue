<!-- 
  1.加上进度加载提示
  2.优化评论获取方式(当前评论与回复是分开加载的模式)
  3.查看是否能修复切换tab页时 评论内容出不来的问题
 -->
<template>
    <div class="wrapper">
        <minbar :title="title" :isPadding="true">
        </minbar>
        <list class="scroller">
                <cell class="day_content">
                    <div class="day_content_c">
                        <div class="user_div">
                            <image  class="head" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+dayDetail.code"></image>
                            <div class="name_ctime">
                                <text  class="name_text">{{dayDetail.name}}</text>
                                <text  class="ctime_text">{{dayDetail.ctime}}</text>
                            </div>
                        </div>
                        <text class="content_text">{{dayDetail.content}}</text>
                    </div>
                    <div class="day_content_b">
                        <text class="viewNum">浏览{{viewNum}}次</text>
                    </div> 
                </cell>
                <cell class="moreContent">
                    <div class="menu">
                      <!-- :style="getLineStyle('commonts')" -->
                        <div class="menu_btn" :style="getLineStyle1" @click="gotoChild('commonts')"><text class="menu_btn_title">评论</text></div>
                        <div class="menu_btn" :style="getLineStyle2" @click="gotoChild('projects')"><text class="menu_btn_title">项目详情</text></div>
                        <div class="menu_btn" :style="getLineStyle3" @click="gotoChild('viewPsn')"><text class="menu_btn_title">最近浏览的人</text></div>
                    </div>
                        <div class="more_content">
                                <div class="more_content_c comment" v-if="type == 'commonts'">
                                    <div v-if="noPldata" class="noViewPsn">
                                        <text class="noViewPsn_text">该日志暂无评论~</text>
                                    </div>
                                    <div v-else v-for="(c,index) in comments" :key="index">
                                        <div class="commont_line1"  @click="showSendC('hf',c.id,c.psnName,c.psnCode)">
                                                <text class="commont_text content_text_color" >{{c.psnName}}</text>
                                                <text class="commont_text">:</text>
                                                <text class="commont_text">{{c.line1}}</text>
                                        </div>  
                                        <div  class="commont_line2">
                                            <text class="commont_text">{{c.line2}}</text>
                                        </div>
                                        <div v-for="(r,index1) in c.reply" :key="index1">
                                            <div class="commont_line1"  @click="showSendC('hf',r.parentID,r.psnName,r.psnCode)">
                                                    <text class="commont_text content_text_color" >{{r.psnName}}</text>
                                                    <text class="commont_text"> 回复 </text>
                                                    <text class="commont_text content_text_color" >{{r.hfName}}</text>
                                                    <text class="commont_text">:   </text>
                                                    <text class="commont_text">{{r.line1}}</text>
                                            </div>  
                                            <div  class="commont_line2">
                                                <text class="commont_text">{{r.line2}}</text>
                                            </div>
                                        </div>
                                       
                                   </div>
                                   <div class="line"></div>
                                   <div class="input_div">
                                       <div  class="send1" @click="showSendC('pl')">
                                           <text class="send1_text">评论</text>
                                       </div>
                                   </div>
                                   
                                </div>
                            
                                <div class="more_content_c" v-else-if="type == 'projects'">
                                    <div v-for="(p,index) in projects" :key="index" class="project_title_div">
                                        <text class="p_text">{{p.pname}}</text>
                                        <text class="ratio p_text">{{p.ratio}}%</text>
                                    </div>
                                </div>
                            
                           
                                <div class="more_content_c" v-else>
                                    <div v-if="noViewPsn" class="noViewPsn">
                                        <text class="noViewPsn_text">该日志暂无浏览的人~</text>
                                    </div>
                                    <div v-for="(vp,index) in viewPsn" :key="index" class="user_div2" >
                                        <image  class="head2" :src="rootPath+'/system/user/userPhotoDownloadAction?bsessionid='+bsessionid+'&code='+vp.code"></image>
                                        <div class="name_ctime">
                                            <text  class="name_text">{{vp.name}}</text>
                                            <text  class="ctime_text">{{vp.ltime}}</text>
                                        </div>
                                    </div>
                                </div>
                        </div>
                </cell>    
        </list>
        <div class="sendDiv" v-if="showSend" @click="closeSend">
            <div class="send_div" @click="sdf">
                <div class="send_more">
                    <text class="iconfont send_more_icon">&#xe682;</text>
                    <text class="iconfont send_more_icon">&#xe7ed;</text>    
                </div>
                <div class="send2_div">
                    <input class="send2" placeholder = "评论" autofocus ="true" ref="send" @input="sendInput" />
                    <div class="send_btn">
                        <text class="btn_text" @click="sendClick">发表</text>
                    </div>
                </div>
            </div>
        </div>
        
    </div>
</template>

<script>
import Utils from "../../../common/js/utils";
import asCore from "../../../common/js/core";
const storage = weex.requireModule("storage");
const dom = weex.requireModule("dom");
const navigator = weex.requireModule("navigator");
export default {
  components: {
    "minbar": require("../../../common/component/minbar.vue")
  },
  computed :{
    /* 动态计算 */
    getLineStyle1 (){
      return this.getLineStyle("commonts");
    },
    getLineStyle2 (){
      return this.getLineStyle("projects");
    },
    getLineStyle3 (){
      return this.getLineStyle("viewPsn");
    }
  },
  data() {
    return {
      dayDetailId: "",
      dayDetail: {},
      bsessionid: "",
      rootPath: "",
      projects: [],
      viewNum: 0,
      viewPsn: [],
      noViewPsn: false,
      title: "日报详情",
      comments: [],
      temp:[],
      type: "commonts",
      showSend: false,
      sendType: "pl",
      user: {
        psnCode: "",
        psnName: ""
      },
      parentId: "",
      sendContent: "",
      hanName: "",
      hanCode: "",
      theme:"",
      noPldata:false
    };
  },
  created() {
    var s = this;
    /* 获取主题 */
    Utils.getThemeColor((color,theme) => {
        if(theme=="theme1"){
          s.theme = "#F15A49";
        }else{
          s.theme = color;
        }
    });
    
    var globalEvent = weex.requireModule("globalEvent");
    globalEvent.addEventListener("onPageInit", function(e) {
        var param = navigator.param();
        s.dayDetailId = param["dayDetailId"],
        s.getPath();
        
    });
  },
  methods: {
    /* 动态计算并获取线条主题的方法体 */
    getLineStyle (retype){
      if(this.type==retype){
        return {
          borderBottomColor:this.theme,
          borderBottomWidth:4
        }
      }else{
        return {
          borderBottomColor:"#e2e2e2",
          borderBottomWidth:1
        }
      }
    },
    getPath() {
      var s = this;
      asCore.getBsessionid(bid => {
        if (bid) {
          s.bsessionid = bid;
          s.rootPath = asCore.rootPath;
          s.getDetailData();
          s.getViewNum();
          s.getComment();
          s.getProjectData();
          s.getViewPsn();
        } 
      });
    },
    /**获取日报主数据*/
    getDetailData() {
      var s = this;
      asCore.post("/oa/queryDailyByIdAction",{ id: s.dayDetailId },(data, flag) => {
          if (data["status"] == "SUCCESS") {
            var drData = JSON.parse(data["data"]["data"]);
            s.dayDetail = {
              code: drData.rows[0].createPsnCode,
              name: drData.rows[0].createPsnName,
              content: drData.rows[0].remark,
              ctime: s.getFormatTime(drData.rows[0].createTime, "ctiem"),
              daytime: s.getFormatTime(drData.rows[0].dayTime),
              viewNum: drData.rows[0].viewNum
            };

            s.title = drData.rows[0].createPsnName + " " + Utils.formatDate(new Date(drData.rows[0].dayTime), "MM-dd") + "的日报";
          } else {
            asCore.toast("获取日报内容失败");
          }
        }
      );
    },

    /**获取项目数据*/
    getProjectData() {
      var s = this;
      asCore.post("/oa/dailyReportDetailQueryAction",{ masterId: s.dayDetailId },(data, flag) => {
          if (data["status"] == "SUCCESS") {
            var pData = JSON.parse(data["data"]["data"]);
            var rows = pData.rows;
            var arr = [];
            if (rows.length > 0) {
              for (var i = 0; i < rows.length; i++) {
                arr.push({ pname: rows[i].name, ratio: rows[i].scale });
              }
            }
            s.projects = arr;
          } else {
            asCore.toast("获取项目内容失败");
          }
        }
      );
    },

    /**获取评论*/
    getComment() {
      var s = this;
      asCore.post("/oa/dayCommentQueryAction",{ masterId: s.dayDetailId },(data, flag) => {
          if (data["status"] == "SUCCESS") {
            var cData = JSON.parse(data["data"]["data"]);
            var rows = cData.rows;
            let arrc = [];
            if (rows.length > 0) {
              s.noPldata = false;
              for (var i = 0; i < rows.length; i++) {
                var cd = s.getByteLen(
                  rows[i].createPersonName + ":   " + rows[i].content
                );
                var line1 = "";
                var line2 = "";
                if (cd.boo) {
                  line1 = rows[i].content;
                } else {
                  var str = rows[i].createPersonName + ":   ";
                  line1 = rows[i].content.substring(0, cd.num - 1 - str.length);
                  line2 = rows[i].content.substring(
                    cd.num,
                    rows[i].content.length
                  );
                }

                let jarr = {
                  id: rows[i].id,
                  psnName: rows[i].createPersonName,
                  psnCode: rows[i].createPersonCode,
                  line1: line1,
                  line2: line2,
                  reply: []
                };

                s.getReply(rows[i].id, sData => {
                  jarr.reply = sData;
                  arrc.push(jarr);
                });
              }
            } else {
              //显示暂无评论
              s.noPldata = true;

            }
            s.comments = arrc;
            s.temp = s.comments;
          } else {
            asCore.toast("获取评论失败");
          }
        }
      );
    },

    /**获取回复*/
    getReply(mainId, callbak) {
      var s = this;
      asCore.post("/oa/dayReplyQueryAction",{ masterId: mainId },(data, flag) => {
          if (data["status"] == "SUCCESS") {
            var rData = JSON.parse(data["data"]["data"]);
            var rows = rData.rows;
            let arr = [];
            if (rows.length > 0) {
              for (var i = 0; i < rows.length; i++) {
                var cd = s.getByteLen(
                  rows[i].createPersonName +
                    " 回复 " +
                    rows[i].createPersonCode +
                    ":   " +
                    rows[i].content
                );
                var line1 = "";
                var line2 = "";
                if (cd.boo) {
                  line1 = rows[i].content;
                } else {
                  var str =
                    rows[i].createPersonName +
                    " 回复 " +
                    rows[i].createPersonCode +
                    ":   ";
                  line1 = rows[i].content.substring(0, cd.num - 1 - str.length);
                  line2 = rows[i].content.substring(
                    cd.num,
                    rows[i].content.length
                  );
                }
                arr.push({
                  psnName: rows[i].createPersonName,
                  psnCode: rows[i].createPersonCode,
                  hfName: rows[i].hanPersonName,
                  hfCode: rows[i].hanPersonCode,
                  line1: line1,
                  line2: line2,
                  hfID: rows[i].id,
                  parentID: rows[i].masterId
                });
              }
            }
            callbak && (callbak(arr))
          }
        }
      );
    },

    /**获取浏览次数*/
    getViewNum() {
      var s = this;
      asCore.post(
        "/oa/dayLookUpQueryAction",
        { masterId: s.dayDetailId, rows: 1 },
        (data, flag) => {
          if (data["status"] == "SUCCESS") {
            var vData = JSON.parse(data["data"]["data"]);
            if (vData.total || vData.total == 0) {
              s.viewNum = vData.total;
            }
          } else {
            asCore.toast("获取浏览次数失败");
          }
        }
      );
    },

    /**获取最近浏览的人*/
    getViewPsn() {
      var s = this;
      asCore.post("/oa/dayLookUpQueryAction", { masterId: s.dayDetailId, rows: 1 },(data, flag) => {
          if (data["status"] == "SUCCESS") {
            var vpData = JSON.parse(data["data"]["data"]);
            var rows = vpData.rows;
            var arr = [];
            if (rows.length > 0) {
              for (var i = 0; i < rows.length; i++) {
                arr.push({
                  name: rows[i].createPersonName,
                  code: rows[i].createPersonCode,
                  ltime: this.getFormatTime(rows[i].lookTime, "ctime")
                });
              }

              s.viewPsn = arr;
            } else {
              s.noViewPsn = true;
            }
          } else {
            asCore.toast("获取最近浏览的人失败");
          }
        }
      );
    },

    /**功能菜单切换*/
    gotoChild(e) {
      var s = this;
      s.type = e;
      this.$nextTick(() => {
        if(e!="commonts") {
          s.comments = [];
        }else{
          s.comments = s.temp;
          //s.getComment();
        }
      });
      
      //asCore.alert(this.comments);
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

    /*获取评论长度*/
    getByteLen(val) {
      var len = 0;
      var num = 0;
      var boo = true;
      for (var i = 0; i < val.length; i++) {
        var a = val.charAt(i);
        if (a.match(/[^\x00-\xff]/gi) != null) {
          len += 2;
        } else {
          len += 1;
        }
        if (len > 59) {
          boo = false;
          num = i;
          break;
        }
      }
      return { boo: boo, num: num };
    },

    /*弹出评论框*/
    showSendC(e, pid, pName, pCode) {
      this.sendType = e;
      this.showSend = true;
      if (pid) {
        this.parentId = pid;
        this.hanName = pName;
        this.hanCode = pCode;
      }
    },

    /*关闭评论框*/
    closeSend() {
      this.$refs.send.blur();
      this.sendContent = "";
      this.parentId = "";
      this.showSend = false;
    },

    sendInput(event) {
      var self = this;
      self.sendContent = event.value;
    },

    /*评论*/
    sendClick() {
      var self = this;
      asCore.getContext(context => {
        self.user.psnCode = context.currentPersonCode;
      });

      if (!self.sendContent) {
        asCore.toast("请输入要发送的内容");
        return;
      }
      if (self.sendType == "pl") {
        asCore.post(
          "/oa/dayCommentCreateAction",
          { masterId: self.dayDetailId, content: self.sendContent },
          (data, flag) => {
            if (data["status"] == "SUCCESS") {
              asCore.toast("评论成功");
              this.closeSend();
              this.getComment();
            } else {
              asCore.toast("评论失败");
            }
          }
        );
      }
      if (self.sendType == "hf") {
        var param = {
          masterId: self.parentId,
          content: self.sendContent,
          hanPsnName: self.hanName,
          hanPsnCode: self.hanCode
        };

        asCore.post("/oa/dayReplyCreateAction", param, (data, flag) => {
          asCore.toast(data);
          if (data["status"] == "SUCCESS") {
            asCore.toast("回复成功");
            this.closeSend();
            this.getComment();
          } else {
            asCore.toast("回复失败");
          }
        });
      }
    }
  }
};
</script>

<style scopde>
.wrapper {
  justify-content: space-between;
  background-color: #f5f5f5;
}
.scroller {
  flex: 1;
}
.iconfont {
  font-family: iconfont;
}
.title-icon {
  font-size: 35px;
  color: #fff;
  font-weight: 300;
}

.day_content {
  width: 750px;
  background-color: #fff;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.day_content_c {
  width: 750px;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 20px;
  flex-direction: column;
}

.user_div {
  width: 710px;
  height: 80px;
  flex-direction: row;
  justify-content: flex-start;
  margin-bottom: 20px;
}

.user_div2 {
  width: 750px;
  padding-top: 10px;
  padding-bottom: 10px;
  padding-left: 20px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
  flex-direction: row;
  justify-content: flex-start;
}

.head {
  width: 80px;
  height: 80px;
  border-radius: 40px;
  margin-right: 15px;
}

.head2 {
  width: 70px;
  height: 70px;
  border-radius: 40px;
  margin-right: 20px;
  margin-top: 5px;
}

.name_ctime {
  height: 80px;
  flex-direction: column;
  justify-content: space-between;
}
.ctime_text {
  font-size: 20px;
  line-height: 30px;
  color: #8992a3;
}
.name_text {
  font-size: 30px;
  line-height: 50px;
  color: #1AA6DE;
}
.day_content_b {
  width: 750px;
  margin-top: 25px;
}

.viewNum {
  font-size: 26px;
  color: #8992a3;
  line-height: 60px;
  padding-left: 20px;
}

.content_text {
  font-size: 30px;
  color:#555555;
}

.more_content_c {
  width: 750px;
  background-color: #fff;
}

.title_div {
  width: 750px;
  height: 90px;
  padding-left: 20px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.title_text {
  font-size: 26px;
  line-height: 90px;
  color: #888888;
  font-weight: 550px;
}

.project_title_div {
  width: 750px;
  height: 90px;
  flex-direction: row;
  justify-content: space-between;
  padding-left: 20px;
  padding-right: 20px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.p_text {
  font-size: 28xp;
  color: #888888;
  line-height: 90px;
}
.ratio {
  align-items: flex-end;
}

.menu {
  width: 750px;
  flex-direction: row;
  background-color: #fff;
  margin-top: 20px;
  border-top-color: #e2e2e2;
  border-top-width: 1px;
  
}
.menu_btn {
  width: 250px;
  height: 80px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.menu-active {
  border-bottom-color: red;
  border-bottom-width: 4px;
}
.menu_btn:active {
  background-color: #eee;
}

.menu_btn_title {
  font-size: 32px;
  line-height: 80px;
  text-align: center;
  color: #555555;
}

.more_content {
  flex-direction: row;
}
.comment {
  padding-top:15px;
}

.commont {
  width: 750px;
}

.commont_line1 {
  width: 750px;
  height: 45px;
  flex-direction: row;
  justify-content: flex-start;
  padding-left: 20px;
  padding-right: 20px;
}

.commont_line2 {
  width: 750px;
  padding-left: 20px;
  padding-right: 20px;
}

.commont_text {
  font-size: 28px;
  line-height: 45px;
  color:#666666;
}
.content_text_color {
  color:#1AA6DE;
}

.input_div {
  width: 750px;
  height: 85px;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 15px;
  padding-bottom: 20px;
  justify-content: space-around;
  align-items: center;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;

}

.send1 {
  width: 700px;
  border-top-color: #e2e2e2;
  border-top-width: 2px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 2px;
  border-left-color: #e2e2e2;
  border-left-width: 2px;
  border-right-color: #e2e2e2;
  border-right-width: 2px;
  border-radius: 4px;
  font-size: 26px;
  padding-left: 10px;
  padding-right: 10px;
  height: 60px;
  justify-content: space-around;
}

.send1_text {
  font-size: 26px;
  color: #c4c4c4;
}

.line {
  width: 710px;
  height: 1px;
  background-color: #e2e2e2;
  margin-top: 15px;
  margin-left: 20px;
  margin-right: 20px;
}

.sendDiv {
  width: 750px;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  right: 0px;
}

.send_div {
  position: fixed;
  bottom: 0;
  width: 750px;
  height: 150px;
  background-color: #F5F5F5;
  border-top-color: #e2e2e2;
  border-top-width: 1px;
}

.send_more {
  width: 750px;
  padding-left: 20px;
  padding-top: 15px;
  height: 70px;
  flex-direction: row;
  justify-content: flex-start;
}

.send_more_icon {
  font-size: 40px;
  color: #999999;
  margin-right: 20px;
  justify-content: space-around;
}

.send2_div {
  width: 750px;
  margin-left: 20px;
  flex-direction: row;
  justify-content: flex-start;
}

.send2 {
  border-top-color: #e2e2e2;
  border-top-width: 1px;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
  border-left-color: #e2e2e2;
  border-left-width: 1px;
  border-right-color: #e2e2e2;
  border-right-width: 1px;
  border-radius: 4px;
  width: 600px;
  font-size: 26px;
  padding-left: 10px;
  padding-right: 10px;
  height: 62px;
  background-color: #fff;
}

.send_btn {
  margin-left: 10px;
  width: 100px;
  height: 62px;
  background-color: #1baceb;
  border-radius: 4px;
  justify-content: space-around;
  align-items: center;
}

.btn_text {
  font-size: 28px;
  color: #fff;
}

.noViewPsn {
  padding-top: 40px;
  padding-bottom: 40px;
  justify-content: space-around;
  align-items: center;
  border-bottom-color: #e2e2e2;
  border-bottom-width: 1px;
}

.noViewPsn_text {
  font-size: 28px;
  color:#888888;
}
</style>



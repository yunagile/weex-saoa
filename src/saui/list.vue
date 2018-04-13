<template>
    <div class="wrap" >
        <minbar title="左滑删除demo" :isPadding="true"></minbar>
        <div class="content">
            
            <div v-for="(msg,index) in msgData" :key="index" ref="mainList" > 
                <swipecell @cellclick="clickmenu">
                  <div slot="content" class="panel" >
                    <div class="panel-content">
                        <div class="content-icon" :style="{ backgroundColor: msg.backcolor}" v-if="msg.icon && !msg.img">
                            <text class="content-font iconfont" :style="{ fontSize: msg.fontsize + 'px'}">{{msg.icon}}</text>
                        </div>
                        <image class="content-img" :src="msg.img" v-if="msg.img && !msg.icon"></image>
                        <div class="msg-tag" v-if="msg.count > 0">
                            <text class="count-text">{{msg.count}}</text>
                        </div>
                        <div class="content-div">
                            <text class="content-title">{{msg.title}}</text>
                            <text class="content-text">{{msg.label}}</text>
                        </div>
                    </div>
                  </div>
                </swipecell>
                
            </div> 
        </div>
    </div>
</template>

<script>
import minbar from "../common/component/minbar.vue";
import swipecell from "../common/component/swipecell.vue";
import msgconfig from "../configs/mesconfig";
import asCore from "../common/js/core";
export default {
  components: {
    minbar: minbar,
    swipecell:swipecell
  },
  data() {
    return {
      msgData: msgconfig.msgconfig,
      btnAry1: [{
        'title': '取消',
        'bgcolor' : '#c6c7c8'
      },{
        'title': '标记',
        'bgcolor' : '#3399ff'
      }]
    };
  },
  mounted : function() {
    /* 去绑定一次事件 */
     //this.listShow();
  },
  methods: {
    clickmenu(){
       //asCore.alert(1);
    },
    ontouchstart(event) {
      
    },
    bindTiming(refname) {
      
    }
  }
};
</script>

<style scoped>
.wrap {
  background-color: #f3f3f3;
}
.iconfont {
  font-family: iconfont;
}
.title-icon {
  font-size: 35px;
  color: #fff;
  font-weight: 300;
}
.panel {
  width: 750px;
  background-color: #fff;
}
.panel-content {
  width: 750px;
  flex-direction: row;
  padding-left: 25px;
  padding-top: 18px;
  padding-right: 30px;
  padding-bottom: 18px;
}
.panel-pos {
  position: relative;
}
.content-icon {
  width: 100px;
  height: 100px;
  border-radius: 10px;
  align-items: center;
  justify-content: center;
}
.content-font {
  color: #ffffff;
}
.content-img {
  width: 100px;
  height: 100px;
  border-radius: 10px;
}
.content-div {
  flex: 1;
  padding-left: 25px;
}
.content-title {
  margin-top:3px;
  color: #333;
  text-overflow: ellipsis;
  lines: 1;
  font-size: 32px;
}
.content-text {
  color: #888;
  text-overflow: ellipsis;
  lines: 1;
  padding-top:11px;
  font-size: 28px;
}
.msg-tag {
  position: absolute;
  top: 10px;
  left: 90px;
  border-radius: 40px;
  background-color: #ff3e3e;
  height: 40px;
  width: 40px;
  align-items: center;
  justify-content: center;
}
.count-text {
  font-size: 24px;
  color: #ffffff;
}
.list-top-border {
  border-bottom-width: 1px;
  border-bottom-color: #d9d9d9;
}
</style>

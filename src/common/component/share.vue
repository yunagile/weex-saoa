<!-- by:Jie. 2018/4/18
    @param items  [数组类型]按钮的数据,格式为:
        [{
        type:"icon",     //类型分为[icon],[image]两种类型
        icon:"\ue6b0",   //[可选]字体图标代码,如果type为image类型icon可为空
        text:"复制链接",  //[必须]按钮下方的内容
        style:{          //[必须]style样式可随意填写
            fontSize:"65"
        },
        imgurl:""        //[可选]图片地址,type为image时必填
        }] 
    @param button   [文本类型]默认值为'取消'
    @param itemClick  [方法]item的点击事件,参数为 index,item 
-->
<template>
    <div v-if="value">
        <smask ref="mask" @click="_maskClick" :style="{'opacity': '0'}" rgba="0.5"></smask>
        <div class="share-box" :style="{'bottom': '-'+ bottom +'px'}" ref="shareBox">
            <div class="share-top">
                <div class="items" v-for="(item,i) in items" :key="i" >
                    <div class="items-icon" @click="itemClick(i,item)">
                        <text :class="['iconfont','items-text',isIos()?'ios-text-padding':'']" 
                        :style="item.style" 
                        v-if="item.type=='icon'">{{item.icon}}</text>
                        <image :src="item.imgurl" :style="item.style" v-else></image>
                    </div>
                    <div>
                        <text class="items-txt">{{item.text}}</text>
                    </div>
                </div>
            </div>
            <div class="share-bottom">
                <text class="share-btn" @click="_btnClick">{{button}}</text>
            </div>
        </div>
    </div>

</template>

<script>
var animation = weex.requireModule("animation");
module.exports = {
  components: {
    "smask": require("./smask.vue")
  },
  props: {
    items: {
      type: Array
    },
    button: {
      default: "取消"
    },
    value: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    value(val) {
      this.$emit("input", val);
      setTimeout(() => {
        if (val) {
          this._open();
        }
      }, 50);
    }
  },
  computed: {
    bottom() {
      return 4 * 100 + 80;
    }
  },
  methods: {
    show() {},
    itemClick (i,item){
        this._maskClick();
        this.$emit("itemClick", i,item);
    },
    _maskanimation: function(translate, fn) {
      var el = this.$refs.mask;
      animation.transition(
        el,
        {
          styles: {
            opacity: translate
          },
          duration: 200,
          delay: 0
        },
        () => {
          fn && fn();
        }
      );
    },
    _animationFn: function(translate, fn) {
      var el = this.$refs.shareBox;
      animation.transition(
        el,
        {
          styles: {
            transform: translate
          },
          duration: 200,
          delay: 0
        },
        () => {
          fn && fn();
        }
      );
    },
    _open() {
      var translate = "translate(0px, -" + this.bottom + "px, 0px)";
      this._maskanimation("1");
      this._animationFn(translate);
    },
    _maskClick() {
      var translate = "translate(0px, " + this.bottom + "px, 0px)";
      this._animationFn(translate, () => {
        this.$emit("input", false);
        this.$emit("maskClick");
      });
      this._maskanimation("0");
    },
    _btnClick() {
      var translate = "translate(0px, " + this.bottom + "px, 0px)";
      this._animationFn(translate, () => {
        this.$emit("input", false);
        this.$emit("cancel");
      });
      this._maskanimation("0");
    },
    isIos() {
      const { platform } = weex.config.env;
      return platform.toLowerCase() === "ios";
    }
  }
};
</script>

<style >
.iconfont {
  font-family: iconfont;
}
.share-box {
  position: fixed;
  left: 0px;
  right: 0px;
  margin-top: 0px;
  flex-direction: column;
  overflow: hidden;
}

.share-top {
  justify-content: flex-start;
  align-items: center;
  flex-direction: row;
  height: 250px;
  background-color: rgba(233, 233, 233, 0.95);
}
.items {
  justify-content: center;
  align-items: center;
}
.items-icon {
  height: 130px;
  width: 130px;
  margin-left: 40px;
  border-radius: 30px;
  background-color: #f9f9f9;
  justify-content: center;
  align-items: center;
}
.items-icon:active {
  background-color: #d1d1d1;
}
.items-text {
  color: #4d4d4d;
  font-size: 65px;
}
.ios-text-padding {
  padding-top: 13px;
}
.items-txt {
  width: 130px;
  margin-left: 40px;
  margin-top: 15px;
  align-items: center;
  text-align: center;
  justify-content: center;
  color: #5b5b5b;
  font-size: 24px;
}

.share-btn {
  font-size: 36px;
  color: #333333;
  background-color: #f5f5f5;
  padding: 30px;
  text-align: center;
}

.share-btn:active {
  background-color: #d1d1d1;
}
</style>
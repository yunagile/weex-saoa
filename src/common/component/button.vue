<template>
  <div class="x-btn"
       :style="mrBtnStyle"
       @click="onClicked"
       :accessible="true"
       :aria-label="text">
    <text v-if="showLoadding" ref="loaddingText" class="x-loadding">&#xe639;</text>
    <text class="btn-text" :style="mrTextStyle">{{text}}</text>
  </div>
</template>

<script>
const animation = weex.requireModule('animation')
import { STYLE_MAP, TEXT_STYLE_MAP } from './js/buttontype';
export default {
  props: {
    text: {
      type: String,
      default: '确认'
    },
    type: {
      type: String,
      default: 'default'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    showLoadding :{
      type: Boolean,
      default: false
    },
    btnStyle: Object,
    textStyle: Object
  },
  data () {
    return {
      current_rotate:0
    }
  },
  computed: {
    mrBtnStyle() {
      const { type, disabled, btnStyle } = this;
      const mrBtnStyle = {
        ...STYLE_MAP[type],
        ...btnStyle
      };
      return disabled
        ? {
            ...mrBtnStyle,
            backgroundColor: 'rgba(0, 0, 0, 0.1)',
            borderWidth: 0
          }
        : mrBtnStyle;
    },
    mrTextStyle() {
      const { type, disabled, textStyle } = this;
      const mrTextStyle = { ...TEXT_STYLE_MAP[type], ...textStyle };
      return disabled ? { ...mrTextStyle, color: '#FFFFFF' } : mrTextStyle;
    }
  },
  methods: {
    onClicked(e) {
      const { type, disabled } = this;
      let that = this;
      if(disabled) return;
      setTimeout(() => {
        that.rotate()
      },100 );
      this.$emit('buttonClick', { e, type, disabled });
    },
    rotate () {
      let self = this;
      let el = this.$refs.loaddingText;
      this.current_rotate+=8000;
      animation.transition(el, {
        styles: {
            color: '#fff',
            transform: 'rotate(' + self.current_rotate + 'deg)',
            transformOrigin: 'center center'
        },
        duration: 30000, //ms
        timingFunction: 'linear',
        delay: 0 //ms
      },function () {

      })
    }
  }
};
</script>

<style scoped>
.x-btn {
  width: 550px;
  height: 80px;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  flex-direction :row;
}
.btn-text {
  text-overflow: ellipsis;
  lines: 1;
  font-size: 32px;
  color: #ffffff;
}
.x-loadding {
  font-size:35px;
  color:#fff;
  font-family:iconfont;
  margin-right:10px;
}
</style>
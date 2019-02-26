<template>
  <div :class="['x-minibar', isPadding?'bar-padding':'',theme]"  v-if="show">
    <div :class="['left',isIos()?'ios-left':'']" @click="leftButtonClicked" aria-label="返回" :accessible="true">
      <slot name="left">
        <text v-if="leftButton && !leftText"
               class="iconfont buttn-icon">&#xe9cf;</text>
        <text v-if="leftText"
              class="icon-text"
              :style="{ color: textColor }">{{leftText}}</text>
      </slot>
    </div>
    <slot name="middle">
      <div class="middle-div"  @click="titleButtonClick">
        <text class="middle-title" :style="{ color: textColor }">{{title}}</text>
        <slot name="other-title"></slot>
      </div>
    </slot>
    <div :class="['right',isIos()?'ios-left':'']" @click="rightButtonClicked">
      <slot name="right"></slot>
    </div>
  </div>
</template>
<script>
  const Navigator = weex.requireModule('navigator');
  const storage = weex.requireModule('storage'); 
  export default {
    data(){
      return{
      }
    },
    props: {
      theme: {
        type: String,
        default: ''
      },
      leftButton: {
        type: Boolean,
        default: true
      },
      textColor: {
        type: String,
        default: '#FFFFFF'
      },
      rightButton: {
        type: Boolean,
        default: false
      },
      isPadding:{
        type: Boolean,
        default: false
      },
      title: {
        type: String,
        default: '标题'
      },
      leftText: {
        type: String,
        default: ''
      },
      useDefaultReturn: {
        type: Boolean,
        default: true
      },
      show: {
        type: Boolean,
        default: true
      }
    },
    beforeCreate(){
       storage.getItem('theme',event=>{
          if(event.result=="success"){
            this.theme = event.data;
          }else{
            this.theme = "theme2";
          }
        });  
    },
    methods: {
      leftButtonClicked () {
        const self = this;
        if (self.useDefaultReturn) {
          Navigator.back();
        }
        self.$emit('leftButtonClick', {});
      },
      isIos:function(){
        const { platform } = weex.config.env;
        return platform.toLowerCase() === 'ios';
      },
      rightButtonClicked () {
        const self = this;
        if (self.rightText || self.rightButton) {
          self.$emit('rightButtonClick', {});
        }
      },
      titleButtonClick(){
        const self = this;
        self.$emit('titleButtonClick', {});
      },
      getTheme(){
        storage.getItem('theme',event=>{
          if(event.data){
            this.theme = event.data;
          }         
        });        
      }
    }
  };
</script>
<style lang="scss" scoped >
  @import './css/theme.scss';
  .iconfont {
    font-family:iconfont;
  }
  .buttn-icon {
    font-size:40px;
    color:#fff;
  }
  .x-minibar {
    width: 750px;
    height: 130px;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    
  }
  .bar-padding {
    padding-top:30px;
  }
  .left {
    height:80px;
    width: 180px;
    justify-content: center;
    padding-left: 25px;
  }
  .ios-left {
    padding-top:10px;
  }
  .middle-div {
    flex-direction: row;
  }
  .middle-title {
    font-size: 35px;
    font-weight: 300;
    color: #ffffff;
    padding-top:20px;
    padding-bottom:20px;
  }

  .right {
    width: 180px;
    height:80px;
    padding-right: 25px;
    align-items: flex-end;
    justify-content: center;
  }

  .left-button {
    width: 21px;
    height: 36px;
  }

  .right-button {
    width: 32px;
    height: 32px;
  }

  .icon-text {
    font-size: 28px;
    color: #ffffff;
  }
</style>
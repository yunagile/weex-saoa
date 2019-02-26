<!-- by:Jie. 2018/4/18
    @param show           [Boolean]  显示隐藏对话框,默认值为false
    @param title          [String]   对话框标题
    @param content        [String]   对话框显示内容
    @param top            [String]   对话框距离顶部的位置,默认值为400
    @param confirmText    [String]   左边按钮文字,默认值为[确定]
    @param cancelText     [String]   右边按钮文字,默认值为[取消]
    @param mainBtnColor   [String]   主按钮颜色,默认值为'#1B82D2'
    @param secondBtnColor [String]   副按钮颜色,默认值为'#666666'
    @param showNoPrompt   [Boolean]  是否显示提示文字,默认值为false
    @param noPromptText   [String]   提示文字,默认值为[不再提示]
    @param isChecked      [Boolean]  是否选择提示文字
    @param okBtnClick     [function] 确定按钮被单击事件,参数为:type
    @param cancelBtnClick [function] 取消按钮被单击事件,参数为:type
    @param noPromptClick  [function] 提示文字被单击
-->
<template>
  <div class="container">
    <x-overlay v-if="show" :show="true" :hasAnimation="false"></x-overlay>
    <div class="dialog-box" v-if="show" :style="{top:top+'px'}">
      <div class="dialog-content">
        <slot name="title">
          <text class="content-title">{{title}}</text>
        </slot>
        <slot name="content">
          <text class="content-subtext">{{content}}</text>
        </slot>
        <div class="no-prompt"
             v-if="showNoPrompt"
             @click="noPromptClicked">
          <image :src="noPromptIcon" class="no-prompt-icon"></image>
          <text class="no-prompt-text">{{noPromptText}}</text>
        </div>
      </div>
      <div class="dialog-footer">
        <div class="footer-btn cancel"
             v-if="!single"
             @click="secondaryClicked">
          <text class="btn-text" :style="{ color: secondBtnColor }">{{cancelText}}</text>
        </div>
        <div class="footer-btn confirm" @click="primaryClicked">
          <text class="btn-text" :style="{ color: mainBtnColor }">{{confirmText}}</text>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { CHECKED, UN_CHECKED } from './js/dialog';
  export default {
    components: { 
      "x-overlay":require('./overlay.vue')
    },
    props: {
      show: {
        type: Boolean,
        default: false
      },
      single: {
        type: Boolean,
        default: false
      },
      title: {
        type: String,
        default: ''
      },
      content: {
        type: String,
        default: ''
      },
      top: {
        type: Number,
        default: 400
      },
      cancelText: {
        type: String,
        default: '取消'
      },
      confirmText: {
        type: String,
        default: '确定'
      },
      mainBtnColor: {
        type: String,
        default: '#1B82D2'
      },
      secondBtnColor: {
        type: String,
        default: '#666666'
      },
      showNoPrompt: {
        type: Boolean,
        default: false
      },
      noPromptText: {
        type: String,
        default: '不再提示'
      },
      isChecked: {
        type: Boolean,
        default: false
      }
    },
    data: () => ({
      noPromptIcon: UN_CHECKED,
      pageHeight: 1334
    }),
    created () {
      const { env: { deviceHeight, deviceWidth } } = weex.config;
      this.pageHeight = deviceHeight / deviceWidth * 750;
    },
    methods: {
      secondaryClicked () {
        this.$emit('cancelBtnClick', {
          type: 'cancel'
        });
      },
      primaryClicked (e) {
        this.$emit('okBtnClick', {
          type: 'confirm'
        });
      },
      noPromptClicked (e) {
        const isChecked = !this.isChecked;
        this.noPromptIcon = isChecked ? CHECKED : UN_CHECKED;
        this.$emit('noPromptClick', { isChecked });
      }
    }
  };
</script>
<style scoped>
  .container {
    position: fixed;
    width: 750px;
    /*兼容H5异常*/
    z-index: 99998;
  }
  .dialog-box {
    position: fixed;
    left: 96px;
    width: 558px;
    background-color: #FFFFFF;
  }
  .dialog-content {
    padding-top: 36px;
    padding-bottom: 36px;
    padding-left: 36px;
    padding-right: 36px;
  }
  .content-title {
    color: #333333;
    font-size: 36px;
    text-align: center;
    margin-bottom: 24px;
  }
  .content-subtext {
    color: #666666;
    font-size: 28px;
    line-height: 36px;
    text-align: center;
  }
  .dialog-footer {
    flex-direction: row;
    align-items: center;
    border-top-color: #F3F3F3;
    border-top-width: 1px;
  }
  .footer-btn {
    flex-direction: row;
    align-items: center;
    justify-content: center;
    flex: 1;
    height: 90px;
  }
  .cancel {
    border-right-color: #F3F3F3;
    border-right-width: 1px;
  }
  .btn-text {
    font-size: 36px;
    color: #666666;
  }
  .no-prompt {
    width: 486px;
    align-items: center;
    justify-content: center;
    flex-direction: row;
    margin-top: 24px;
  }
  .no-prompt-icon {
    width: 24px;
    height: 24px;
    margin-right: 12px;
  }
  .no-prompt-text {
    font-size: 24px;
    color: #A5A5A5;
  }
</style>

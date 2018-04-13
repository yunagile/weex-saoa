<template>
<div :class="['wxc-cello',topBorder && 'cell-top-border',bottomBorder && 'cell-bottom-border',!disabeld && 'cell-disabeld']" @click="cellClicked">
  <div :class="['wxc-cell', hasTopBorder && 'cell-top-border', hasBottomBorder && !bottomBorder && 'cell-bottom-border', hasVerticalIndent && 'cell-indent', desc && 'has-desc']"
    :style="cellStyle"
    :accessible="autoAccessible"
    :aria-label="`${label},${title},${desc}`" >
    <slot name="label">
      <div v-if="label">
        <text class="cell-label-text">{{label}}</text>
      </div>
    </slot>
    <div class="cell-title">
      <slot name="title">
        <text class="cell-content">{{title}}</text>
        <text class="cell-desc-text"
              v-if="desc">{{desc}}</text>
      </slot>
    </div>
    <slot name="value"></slot>
    <slot></slot>
    <text class="iconfont cell-arrow-icon" v-if="hasArrow">&#xe619;</text>
  </div>
</div>
</template>

<style scoped>
  .iconfont {
    font-family:iconfont;
   }
  .wxc-cello {
    padding-left: 30px;
    background-color: #ffffff;
  }
  .wxc-cell {
    height: 100px;
    flex-direction: row;
    align-items: center;
    padding-right: 20px;
  }
  .cell-disabeld {
    background-color: #ffffff;
  }
  .cell-disabeld:active {
      background-color: #eee;
  }
  .cell-title {
    flex: 1;
  }

  .cell-indent {
    padding-bottom: 30px;
    padding-top: 30px;
  }

  .has-desc {
    padding-bottom: 18px;
    padding-top: 18px;
  }

  .cell-top-border {
    border-top-color: #e2e2e2;
    border-top-width: 1px;
  }

  .cell-bottom-border {
    border-bottom-color: #e2e2e2;
    border-bottom-width: 1px;
  }

  .cell-label-text {
    font-size: 30px;
    color: #666666;
    width: 188px;
    margin-right: 10px;
  }

  .cell-arrow-icon {
    color:#c1c1c1;
    font-size:50px;
  }

  .cell-content {
    color: #333333;
    font-size: 30px;
    line-height: 40px;
  }

  .cell-desc-text {
    color: #999999;
    font-size: 24px;
    line-height: 30px;
    margin-top: 4px;
  }
</style>

<script>

  export default {
    props: {
      label: {
        type: String,
        default: ''
      },
      title: {
        type: String,
        default: ''
      },
      desc: {
        type: String,
        default: ''
      },
      topBorder :{
        type: Boolean,
        default: false
      },
      bottomBorder :{
        type: Boolean,
        default: false
      },
      disabeld : {
        type: Boolean,
        default: false
      },
      hasTopBorder: {
        type: Boolean,
        default: false
      },
      hasBottomBorder: {
        type: Boolean,
        default: true
      },
      hasArrow: {
        type: Boolean,
        default: false
      },
      hasVerticalIndent: {
        type: Boolean,
        default: true
      },
      cellStyle: {
        type: Object,
        default: () => ({})
      },
      autoAccessible: {
        type: Boolean,
        default: true
      }
    },
    methods: {
      cellClicked (e) {
        this.$emit('cellClick', { e });
      }
    }
  };
</script>
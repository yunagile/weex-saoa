<!-- by:Jie. 2018/4/18
    @param value      [Aarray]  checkbox组
    @param val        [String]  默认选中值
    @param disabled   [Boolean] 禁用选择框
-->
<template>
  <div class="checkbox" @click="change">
    <text class="box" :class="[checked ? 'box-checked' : '', disabled ? 'box-disabled' : '']"></text>
    <text v-if="checked" class="icon" :class="[disabled ? 'icon-disabled' : '']"></text>
    <text class="text" :class="[disabled ? 'text-disabled' : '']"><slot></slot></text>
  </div>
</template>
<script>
export default {
  props: {
    value: [Array],
    val: [String, Number, Boolean, Object, Array],
    disabled: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    checked () {
      return this.value.indexOf(this.val) > -1
    }
  },
  methods: {
    change () {
      if (!this.disabled) {
        const copyed = this.value.slice()
        if (this.checked) {
          copyed.splice(this.value.indexOf(this.val), 1)
        } else {
          copyed.push(this.val)
        }
        this.$emit('input', copyed)
        this.$emit('change', copyed,this.checked)
      }
    }
  }
}
</script>
<style scoped>
    .checkbox{
        position:relative;
        align-items:center;
        flex-direction:row;
    }
    
    .box{
        width:36px;
        height:36px;
        border-style:solid;
        border-width:2px;
        border-color:#d1d1d1;
        border-radius:8px;
        background-color: transparent;
    }
    
    .box-checked{
        border-color:#33a0d6;
        background-color: #33a0d6;
    }
    
    .box-disabled{
        border-color:#e1e1e1;
    }
    .icon {
        position:absolute;
        left:9px;
        top:10px;
        width:20px;
        height:12px;
        border-left-style:solid;
        border-left-width:3px;
        border-left-color:#ffffff;
        border-bottom-style:solid;
        border-bottom-width:3px;
        border-bottom-color:#ffffff;
        border-radius:2px;
        transform:rotate(-45deg);
    }
    
    .icon-disabled{
        border-left-color:#33a0d6;
        border-bottom-color:#33a0d6;
    }
    
    .text{
        margin-left:15px;
        color:#888888;
        font-size:28px;
        margin-top:3px;
    }
    .text-disabled{
        color:#ffffff;
    }
</style>
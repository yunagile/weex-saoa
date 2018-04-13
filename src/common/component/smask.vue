<!-- by:zj 2018/4/9 -->
<template>
    <div class="mask" @click="_click" :style="{'background-color':'rgba(0,0,0,'+ rgba +')'}"></div>
</template>
<script>
    module.exports = {
        props: {
            rgba: {
                type: [String, Number],
                default: '0.2'
            }
        },
        computed: {
            newRgba(){
                return this.rgba + '';
            }
        },
        methods: {
            _click() {
                this.$emit("click");
            }
        }
    }
</script>

<style>
    .mask{
        position: fixed;
        left: 0px;
        right: 0px;
        top: 0px;
        bottom: 0px;
        background-color:rgba(0,0,0,0.2);
        z-index:999;
    }
</style>
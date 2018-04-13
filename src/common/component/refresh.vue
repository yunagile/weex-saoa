<template>
    <refresh class="refresh" @refresh="onrefresh" :display="refreshing ? 'show' : 'hide'">
        <loading-indicator class="indicator"></loading-indicator>
    </refresh>
</template>
<style scoped>
    .refresh {
        width: 750;
        display: -ms-flex;
        display: -webkit-flex;
        display: flex;
        -ms-flex-align: center;
        -webkit-align-items: center;
        -webkit-box-align: center;
        align-items: center;
    }
    .indicator {
        margin-top: 50px;
        margin-bottom:20px;
        height: 70px;
        width: 70px;
        color: red;
    }

</style>
<script>
    var modal = weex.requireModule('modal');
    module.exports = {
        props:[],
        data () {
            return {
                refreshing: false
            }
        },
        methods: {
            onrefresh (event) {
                this.refreshing = true
                setTimeout(() => {
                    modal.alert({
                        message:"刷新完成"
                    });
                    this.refreshing = false
                }, 2000)
            }
        }
    }
</script>
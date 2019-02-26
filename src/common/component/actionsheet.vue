<!-- by:Jie. 2018/4/18
    @param items  [数组类型]按钮的数据,格式为: ['拍照','从相册选择'] 
    @param title  [文本类型]标题,默认为'请选择'
    @param button [文本类型]默认值为'取消'
    @param itemClick  [方法]item的点击事件,参数为 item,index
-->
<template>
    <div v-if="value">
        <smask ref="mask" @click="_maskClick" :style="{'opacity': '0'}" rgba="0.5"></smask>
        <div class="actionsheet-box" :style="{'bottom': '-'+ bottom +'px'}" ref="actionsheetBox">
            <div class="actionsheet-top">
                <text class="actionsheet-title" v-if="title">{{title}}</text>
                <div class="actionsheet-content">
                    <text class="actionsheet-list" v-for="(item,index) in items" @click="_itemClick(item,index)" :key="index">{{item}}</text>
                </div>
            </div>
            <div class="actionsheet-bottom">
                <text class="actionsheet-btn" @click="_btnClick">{{button}}</text>
            </div>
        </div>
    </div>

</template>

<script>
    var animation = weex.requireModule('animation');
    module.exports = {
        components:{
            "smask" : require("./smask.vue")
        },
        props: {
            title: {
                default: "请选择"
            },
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
                this.$emit('input', val);
                setTimeout(()=>{
                    if(val){
                        this._open();
                    }
                },50);
            }
        },
        computed: {
            bottom () {
                return (this.items.length + 1) * 100 + 80;
            }
        },
        methods: {
            show(){ },
            _maskanimation:function(translate, fn){
                var el = this.$refs.mask;
                animation.transition(el, {
                    styles: {
                        opacity: translate,
                    },
                    duration: 200,
                    delay: 0
                }, () => {
                    fn && fn();
                })
            },
            _animationFn: function (translate, fn) {
                var el = this.$refs.actionsheetBox;
                animation.transition(el, {
                    styles: {
                        transform: translate
                    },
                    duration: 200,
                    delay: 0
                }, () => {
                    fn && fn();
                })
            },
            _open() {
                var translate = 'translate(0px, -' + (this.bottom + 20) + 'px, 0px)';
                this._maskanimation("1");
                this._animationFn(translate)
            },
            _maskClick () {
                var translate = 'translate(0px, ' + (this.bottom + 20) + 'px, 0px)';
                this._animationFn(translate, () => {
                    this.$emit('input', false);
                    this.$emit("maskClick");
                });
                this._maskanimation("0");
            },
            _itemClick(item,index) {
                var translate = 'translate(0px, ' + (this.bottom + 20) + 'px, 0px)';
                this._animationFn(translate, () => {
                    this.$emit('input', false);
                    this.$emit('itemClick', item,index);
                });
                this._maskanimation("0");
            },
            _btnClick() {
                var translate = 'translate(0px, ' + (this.bottom + 20) + 'px, 0px)';
                this._animationFn(translate, () => {
                    this.$emit('input', false);
                    this.$emit('cancel');
                });
                this._maskanimation("0");
            }
        }
    }
</script>

<style >
.actionsheet-box {
  position: fixed;
  left: 0px;
  right: 0px;
  margin: 20px;
  margin-top: 0px;
  flex-direction: column;
  overflow: hidden;
}

.actionsheet-top {
  border-radius: 20px;
  overflow: hidden;
  background-color: #ffffff;
}

.actionsheet-bottom {
  margin-top: 15px;
}

.actionsheet-title {
  padding: 30px;
  text-align: center;
  font-size: 28px;
  color: #9ea7b4;
}

.actionsheet-content {
  flex-direction: column;
  flex: 1;
}

.actionsheet-list {
  border-top-width: 1px;
  border-top-color: #d7dde4;
  padding: 30px;
  text-align: center;
  font-size: 34px;
  color: #037AFE;
}

.actionsheet-list:active {
  background-color: #f5f5f5;
}

.actionsheet-btn {
  font-size: 36px;
  color: #037AFE;
  font-weight: bold;
  background-color: #ffffff;
  padding: 30px;
  text-align: center;
  border-radius: 20px;
}

.actionsheet-btn:active {
  background-color: #f5f5f5;
}

</style>
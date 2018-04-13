<template>
    <div class="list-swipe-menu">
        <div class="cell-swipe-menu" >
            <div class="list-swipe">
                <div class="list-swipe-btn" :style="{'background-color': item.bgcolor}" @click="_actionClick(index)" v-for="(item, index) in items">
                    <text class="list-swipe-btn-text">{{item.title}}</text>
                </div>
            </div>
            <div @click="_click" @panstart="ontouchstart" @touchend="ontouchend" class="list-main list-swipe-main" ref="swipedom">
                <div class="list-main-left">
                    <slot name="content" >
                        <text class="list-title" v-if="title">{{title}}</text>
                    </slot>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    const animation = weex.requireModule('animation');
    const bindx = weex.requireModule("bindingx");
    let defaultAction=[
        {
            'title': '删除',
            'bgcolor' : '#eb4e3d'
        }
    ];

    module.exports = {
        data(){
            return {
                x:0,
                y:0,
                isInAnimation:false,
                gesToken:0,
                isOpen:false,
                background:"#ffffff"
            }
        },
        props: {
            items: {
                type: Array,
                default:defaultAction
            },
            height: {
                type: String,
                default: '100px'
            },
            title: {
                type: String
            }
        },
        mounted : function() {
            /* 这是为了适配ios */
            var refname = this.$refs.swipedom.ref;
            this.ontouchstart({target:{ref:refname}});
            this.ontouchend();
        },
        methods: {
            //操作点击事件
            _actionClick (index){
                this.close(()=>{
                    this.$emit('actionClick', index);
                });
            },
            ontouchend(){
                this.background = "#ffffff";
            },
            ontouchstart(e){
                this.background = "#eeeeee";
                var s = this;
                if(this.gesToken != 0) {
                    bindx.unbind({
                        eventType:'pan',
                        token:s.gesToken
                    })
                    this.gesToken = 0;
                }
                if(this.isOpen  === true) {
                    this.close();
                    return
                }
                var my = e.target.ref; 
                var x_origin = "x<=-240?-240:x>=0?0:x";
                var x_transformed = '{"type":"+","children":[{"type":"Identifier","value":"x"},{"type":"NumericLiteral","value":0}]}';
                var gesTokenObj = bindx.bind({
                    anchor:my,
                    eventType:'pan',
                    props: [{
                            element:my, 
                            property:'transform.translateX',
                            expression:{
                            transformed:x_transformed,
                            origin:x_origin
                          }
                        }] 
                    }, function(e) {
                        
                    if(e.state === 'end') { 
                        s.x += e.deltaX;
                        s.bindTiming();
                        
                    }
                });
                s.gesToken = gesTokenObj.token;
            },
            bindTiming(e){
                var s = this;
                if(s.x>=-100) {
                    this.close();
                }else if(s.x<=-100){
                    this.open(function(){
                        s.isOpen = true;
                    });
                }
                
            },
            _click(event){
                if(this.isOpen  === true) {
                    
                    this.close();
                }else{
                    this.$emit('cellclick');
                }
            },
            close(fn){
                let translate = 'translate(0px, 0px)';
                let el = this.$refs.swipedom;
                var s = this;
                this._animationFn(el, 1, translate, 'ease-in',()=> {
                    s.isOpen = false;
                    s.x = 0;
                    fn && fn();
                });
            },
            open(fn){
                let len = this.items.length;
                let translate = 'translate(-'+240*len+'px, 0px)';
                let el = this.$refs.swipedom;
                var s = this;
                this._animationFn(el, 1, translate, 'ease-in',()=>{
                    fn && fn();
                });
            },
            _animationFn (el, opacity, translate, timing, fn){
                animation.transition(el, {
                    styles: {
                        opacity: opacity,
                        transform: translate,
                        transformOrigin: 'center center'
                    },
                    duration: 100,
                    timingFunction: timing,
                    delay: 0
                }, () => {
                    fn && fn();
                });
            }
        }
    }
</script>
<style scoped>
.cell-swipe-menu {
  flex-direction: row;
  border-bottom-width: 1px;
  border-bottom-color: #d9d9d9;
  
}

.list-left {
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.list-main {
  flex: 1;
  justify-content: center;
  
}

.list-swipe {
  position: absolute;
  left: 0px;
  right: 0px;
  top: 0px;
  bottom: 0px;
  flex-direction: row;
  height: auto;
  justify-content: flex-end; }

.list-swipe-btn {
  flex-direction: row;
  width: 240px;
  height: 136px;
  align-items: center;
  justify-content: center;
  text-align: center;
  background-color: #c6c7c8; 
}

.list-swipe-btn-text {
  font-size: 30px;
  color: #ffffff; }

.list-swipe-main {
  flex-direction: row;
  justify-content: flex-start; }

.list-main-left {
  justify-content: center;
  flex: 1; }


</style>


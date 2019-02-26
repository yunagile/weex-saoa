<template>
  <div class="container" >

    <div class="border">
      <div :ref="'my'" class="box" @touchstart="ontouchstart">
        <div class="head">
          <div class="avatar"></div>
          <text class="username">HACKER</text>
        </div>
        <div class="content">
          <text class="desc">Google announced a new version of Nearby Connections for fully offline.high bandwidth peer to peer device communications.</text>
        </div>
        <div class="footer">
          <text class="action">SHARE</text>
          <text class="action" style="color:#7C4DFF">EXPLORE</text>
        </div>
      </div>
    </div>
  
  <div style="width:750;align-items:center">
    
     <text style="font-size:40">
      Google Now-Style Card
    </text>
  </div>
   
  </div>
</template>

<style scoped>
  .container {
    flex: 1;
    background-color:#eeeeee;
    
  }
  .border{
    height:1000px;
    padding-left:35px;
    padding-right:35px;
    padding-top:100px;
  }
  .box {
    width: 680px;
    height: 450px;
    background-color:#651FFF;
  }
  .head {
    background-color:#651FFF;
    width:680px;
    height:120px;
    flex-direction:row;
    align-items:center;
  }
  .content{
    width:680px;
    height:240px;
    background-color:#651FFF;
    padding-left:24px;
    padding-top:24px;
    padding-right:24px;
  }
  .footer {
    width:680px;
    height:90px;
    background-color: #fff;
    align-items:center;
    justify-content:flex-end;
    padding-right:25px;
    flex-direction:row
  }
  .action {
    font-size:35;
    padding-right:20px;
  }
  
  .desc {
    font-size:32;
    color:#fff;
    padding-left:24px;
  }
  
  
  .avatar {
    width:96px;
    height:96px;
    border-radius:48px;
    background-color:#CDDC39;
    margin-left:36px;
    margin-right:48px;
  }
  
  .username {
    color:#fff;
    font-size:32;
  }
  
</style>

<script>
  const eb = weex.requireModule('bindingx')
  export default {
    data () {
      return {
        x: 0,
        y: 0,
        isInAnimation: false,
        opacity:1,
        gesToken:0
      }
    },
    methods: {
      
      ontouchstart (event) {
        var self = this; 
        if(this.isInAnimation  === true) {
          if(this.gesToken != 0) {
            eb.unbind({
              eventType:'pan',
              token:self.gesToken
            })
            this.gesToken = 0;
          }
          return
        }
        
        var my = this.$refs.my.ref;
        var translate_x_origin = "x+0";
        var translate_x_transformed = "{\"type\":\"+\",\"children\":[{\"type\":\"Identifier\",\"value\":\"x\"},{\"type\":\"NumericLiteral\",\"value\":0}]}";
      
        // 拖拽
       var gesTokenObj = eb.bind({
          anchor:my,
          eventType:'pan',
          props: [
              {element:my, property:'transform.translateX',expression:{
                transformed:translate_x_transformed,
                origin:translate_x_origin
              }}
            ]
        }, function(e) {
          if(e.state === 'end') {
             self.x += e.deltaX;
             self.y += e.deltaY;
             self.bindTiming();
          }
        })
        
        self.gesToken = gesTokenObj.token;
        
      },
      bindTiming() {
      
       this.isInAnimation = true;
      
	     var my = this.$refs.my.ref;
	     var self = this;
	     
	     var exit_origin = "t>300";
	     var exit_transformed = "{\"type\":\">\",\"children\":[{\"type\":\"Identifier\",\"value\":\"t\"},{\"type\":\"NumericLiteral\",\"value\":300}]}";
	     
      var changed_x;
      var final_x;
        
       var translate_x_origin;
       var translate_x_transformed;
        var shouldDismiss = false;
        shouldDismiss = false;
        final_x = 0;
        changed_x = 0-self.x;
        translate_x_origin = "easeOutSine(t,"+self.x+","+changed_x+",300)"
        translate_x_transformed = "{\"type\":\"CallExpression\",\"children\":[{\"type\":\"Identifier\",\"value\":\"easeOutSine\"},{\"type\":\"Arguments\",\"children\":[{\"type\":\"Identifier\",\"value\":\"t\"},{\"type\":\"NumericLiteral\",\"value\":"+self.x+"},{\"type\":\"NumericLiteral\",\"value\":"+changed_x+"},{\"type\":\"NumericLiteral\",\"value\":300}]}]}"
 
	     var result = eb.bind({
          eventType:'timing',
          exitExpression: {
            transformed: exit_transformed,
            origin: exit_origin
          },
          props: [
              {element:my, property:'transform.translateX',expression:{
                transformed:translate_x_transformed,
                origin:translate_x_origin
              }}
            ]
          
        },function(e){
            if(e.state === 'end' || e.state === 'exit') {
              self.x = final_x;
              self.isInAnimation = false;
            }
        })
	     
      }

   
    }
  }
</script>
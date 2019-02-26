<template>
  <div class="wrapper">
      <minbar title="电子邮件" append="tree">
      </minbar>
      <div class="content">
            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">收件箱</text>
                </div>
                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>
                              
            </div>

            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">星标邮件</text>
                </div>

                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>               
            </div>

      </div>


      <div class="content">
            <div class="content_title">
                <text class="content_title_text">系统文件夹</text>
            </div>
            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">草稿箱</text>
                </div>

                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>               
            </div>

            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">已发送</text>
                </div> 

                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>              
            </div>

            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">已收藏</text>
                </div> 

                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>              
            </div>

            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">垃圾箱</text>
                </div>

                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>               
            </div>

            <div class="content_list">
                <div class="content_list_l">
                    <image class="content_img" src="https://m.yunagile.com:442/examples/oa/img/myhead.gif"></image>
                </div>
                
                <div class="content_list_c">
                    <text class="content_list_title">其他文件</text>
                </div> 

                <div class="content_list_r">
                    <text class="content_list_icon iconfont">&#xe619;</text> 
                </div>              
            </div>

      </div>
      
  </div>
</template>

<script>
import minbar from "../../../common/component/minbar.vue";
import Utils from "../../../common/js/utils";
export default {
    created () { 
      Utils.initIconFont();
    },
    components:{
        minbar:minbar,
    }

  
}
</script>

<style>
    .wrapper{
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
    }

    .iconfont{
        font-family: iconfont;
    }

    .title-icon{
       font-size: 28px;
       color: #ffffff;
    }

    .content{
        width:750px;       
    }

    .content_list{
        width:750px;
        height:120px;
        flex-direction:row;
        justify-content: space-between;
        padding-left:20px;   
        padding-right:20px;
        border-bottom-color: #FFFAFA;
        border-bottom-width: 1px;
    }

    .content_list_l{
        width:100px;
        height:120px;
    }

    .content_img{
        width:100px;
        height:100px;    
    }

    .content_list_c{
        height:120px;
        width:500px;
        padding-top:40px;

    }

    .content_title_text{
        font-size:30px;
        font-weight: 700px;
        line-height:40px;  
    }

    .content_list_title{
        font-size:30px;
        font-weight: 600px;
        line-height:40px;
        text-align: left;
    }

    .content_title{
        width:750px;
        height:110px;
        background-color:#FFFAFA;
        padding-top:40px;
        padding-left:20px;
    }


    .content_list_r{
       height:120px;
       width:50px;
       padding-top:40px;
    }

    .content_list_icon{
        font-size: 34px;
        line-height: 40px;
        color: #C1C1C1;
    }

    

    
</style>

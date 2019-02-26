<template>
    <div class="wrapper" style="background-color:#edeeee">
        <minbar title="周报" :isPadding="true"> 
        </minbar>  
          <wx-cell title="周报填写" :topBorder="true" :hasArrow="true" class="list-margin" style="margin-top:30px"  @cellClick="openView('sendWeek')">
            <div slot="label" class="list-icon" style="background-color:#00d28b;">
                <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe6ab;</text>
             </div>
         </wx-cell> 
         <wx-cell title="待我点评的周报" :topBorder="true" :hasArrow="true" class="list-margin"  @cellClick="openView('reviewList')">
             <div slot="label" class="list-icon" style="background-color:#ff5a65;">
                 <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe693;</text>
            </div>
         </wx-cell> 
         <wx-cell title="抄送给我的周报" :topBorder="true" :hasArrow="true" class="list-margin"  @cellClick="openView('copyList')">
             <div slot="label" class="list-icon" style="background-color:#22b8fd;">
                <text class="iconfont cell-icon" :style="{fontSize:35}">&#xe653;</text>
            </div>
        </wx-cell> 
    </div>  
</template>
<script>
import minbar from "../../../common/component/minbar.vue" 
import cell from "../../../common/component/cell.vue"
import asCore from "../../../common/js/core"; 
const navigator = weex.requireModule("navigator");
export default {
    data(){
        return {
             
        }
    },
    components:{
        "minbar":minbar,
        "wx-cell" : cell,   
    },
    methods:{ 
        openView(event){ 
            if(event == 'sendWeek'){
                navigator.push(asCore.localpath+"yunagileoa/app/weekReport/sendWeek.js");
            }else  if(event == 'reviewList'){
                navigator.push(asCore.localpath+"yunagileoa/app/weekReport/reviewList.js");
            }else if(event == 'copyList'){
                navigator.push(asCore.localpath+"yunagileoa/app/weekReport/copyList.js");
            }else{

            }
        },     
    } 
}
</script>
<style scoped lang="scss">   
.scroller {
    z-index: 102;
 }
 .list-icon {
    height: 65px;
    width: 65px;
    border-radius: 10px;
    margin-right:20px;
    align-items: center;
    justify-content: center;
} 
.iconfont {
    font-family:iconfont;
}

.cell-icon {
    color:#ffffff;
}
</style>
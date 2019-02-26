/**
  *By Jie. 2018/04/08
  *核心js库
  **/
const localpath = "http://192.168.1.88:8080/";
//const localpath = "http://120.27.26.19:9527/";
//const rootPath = "http://192.168.1.88:8089/bsys";
const rootPath = "http://oa.yunagile.com:8068/bsys";
const docPath = "https://owa-box.vips100.com";
const otherRootPath = "http://oa.yunagile.com:8068/bsys";
var basePath = rootPath;
var pref = weex.requireModule("storage");
var stream = weex.requireModule("stream");
var modal = weex.requireModule("modal");
const navigator = weex.requireModule("navigator");
var bsessionid = pref.getItem('bsessionid');
var asCore = {
    localpath: localpath,
    rootPath: basePath,
    docPath: docPath,
    otherRootPath: otherRootPath,
    bsessionid: bsessionid,
    /* 获取context */
    appInitial: function (callback) { 
        var s = this;
        this.post("/initPortalInfo", null, function (data, flag, msg) {
            try {
                if (flag == true) {
                    s.setContext(data.data);
                    if (callback) callback();
                } else {
                    if (data == "timeout") {
                        /* 调用自动登录方法 */
                        s.autoLogin(function () {
                            if (callback) callback();
                        });
                    }
                }
            } catch (e) {

            }
        });
    },
    /* 自动登录代码 */
    autoLogin: function (callback) {
        const progress = weex.requireModule('progress');
        var s = this;
        /* 判断当前是否记住了密码，没有记住密码那只能跳出登录界面自行登录 */
        pref.getItem("as_username", event => {    
            var username = event.data;
            var checkPass = '';
            pref.getItem("as_password_checkbox", event => {
                checkPass = event.data;
                if (checkPass == '1') {
                    pref.getItem("encryptedPass", event => {
                        var ps = event.data;
                        //progress.showFull("自动登录中...");
                        /* 自动登录 */
                        stream.fetch({
                            method: "POST",
                            url: s.rootPath + "/loginAsAction",
                            type: "json",
                            body: s.toParams({
                                username: username,
                                password: ps
                            })
                        },
                            ret=> {
                                if (!ret.ok) {
                                    s.toast("自动登录失败,请手动登录");
                                    //progress.dismiss();
                                    navigator.push(s.localpath + "login.js");
                                } else {
                                    var re = ret.data;
                                    if (re.status == "SUCCESS") {
                                        //progress.dismiss();
                                        s.setBsessionid(re.data.bsessionid);
                                        if (callback) callback();
                                    } else {
                                        var index = re.msg.indexOf("信息有误");
                                        if (index > -1) {
                                            s.toast("用户账号信息有误,请手动登录");
                                           // progress.dismiss();
                                            navigator.push(s.localpath + "login.js");
                                        } else {
                                            s.toast(re.msg);
                                            //progress.dismiss();
                                            navigator.push(s.localpath + "login.js");
                                        }
                                    }
                                }
                            });
                    });
                } else {
                    /* 手动登录 */
                    s.toast('登录超时,请重新登录');
                    navigator.push(s.localpath + "login.js");
                }
            });
        });
    },
    /* 获取上下文 */
    getContext: function (callback) {
        pref.getItem('context', event => {
            var value = event.data;
            try {
                value = JSON.parse(value);
            } catch (error) {
                value = {};
            }
            if (callback) callback(value);
        });
    },
    setContext: function (value) {
        value = value ? JSON.stringify(value) : '';
        pref.setItem('context', value);
    },
    setBsessionid: function (value) {
        pref.setItem('bsessionid', value);
    },
    getBsessionid: function (callback) {
        pref.getItem('bsessionid', event => {
            var value = event.data;
            if (event.result != "success") {
                value = "";
            }
            if (callback) callback(value);
        });
    },
    getTime:function(callback){
        pref.getItem('imgtime', event => {
            if (event.result == "success") {
                var value = event.data;
                if (callback) callback(value);
            }else{
                if (callback) callback("10000");
            }
        });
    },
    setTime:function(time){
        pref.setItem('imgtime',time);
    },
    /* 与web端的ajax用法一致 */
    post: function (action, param, callBack) {
        var s = this;
        s.getBsessionid(bs => {
            var bid = bs;
            if (action.indexOf("bsessionid=") == -1) {
                if (action.indexOf("?") > -1) {
                    action += "&as_call_type=ajax&bsessionid=" + bid;
                } else {
                    action += "?as_call_type=ajax&bsessionid=" + bid;
                }
            }
            stream.fetch({
                method: "POST",
                url: basePath + action,
                type: "json",
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'},
                body: s.toParams(param)
            },
                function (ret) {
                    if (!ret.ok) {
                        if (ret.status == 550) {
                            if (callBack) callBack("timeout", false);
                        } else {
                            if (ret.status == -1) {
                                s.toast('网络连接失败,请检查网络设置');
                            }
                            if (callBack) callBack(ret.statusText, ret.ok);
                        }
                    } else {
                        var data = ret.data;
                        try {
                            if (callBack) callBack(data, ret.ok);
                        } catch (e) {

                        }
                    }
                });
        });
    },
    /* 解析URL */
    rendUrl :function(url,callback){
        var s = this;
        s.getBsessionid(bs => {
            if(url.indexOf("bsessionid=")==-1){  
                if(url.indexOf("?")>-1){
                    url+="&as_call_type=ajax&bsessionid="+bs;
                }else{
                    url+="?as_call_type=ajax&bsessionid="+bs;
                }
            }
            if(callback) callback(basePath + url);
        });
    },
    logout: function (callback) {
        this.post("/as/logout", {}, function (data) {
            pref.removeItem("bsessionid");
            if (callback) callback(data);
        });
    },
    toParams: function (obj) {
        var param = ""
        for (const name in obj) {
            if (typeof obj[name] != 'function') {
                param += "&" + name + "=" + encodeURI(obj[name])
            }
        }
        return param.substring(1)
    },
    toast: function (msg) {
        modal.toast({
            message: msg
        });
    },
    alert: function (msg) {
        modal.alert({
            message: msg
        });
    }

};
export default asCore;
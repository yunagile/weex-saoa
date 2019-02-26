<template>
  <div class="wrapper">
    <minbar :title="title" :isPadding="true">
    </minbar>
    <list class="chat-list">
      <cell style="height:40px"></cell>
      <cell class="chat-cell" v-for="(message, m) in chatMessages" :key="m">
        <div :class="['message', `message-${message.from}`]" >
          <image class="chat-icon" resize="cover" v-if="message.from === 'bot'" :src="headUrl"></image>
          <div :class="['message-box', `message-box-${message.from}`]" ref="messages">
            <text class="message-text">{{message.content || message.title}}</text>
          </div>
        </div>
        <div class="chat-spliter" v-if="message.reset"></div>
      </cell>
      <cell style="height:50px"></cell>
    </list>
    <div class="operations">
      <scroller class="chat-options" scrollDirection="horizontal">
        <div class="btn-option" v-for="(option, i) in feedbackOptions" :key="i" @click="fastReply(i)">
          <text class="btn-option-text">{{option.title}}</text>
        </div>
      </scroller>
      <div class="input-bar">
        <input class="input" type="text" @focus="scrollToBottom" placeholder="请输入消息内容" v-model="userInput" />
        <div class="btn-send" @click="handleUserInput">
          <text class="btn-send-text" :style="{color:theme}">发送</text>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
  const QADB = [
    {
      qid: 100001,
      content: `Hi, I'm weex-saoa Bot. What can I do for you?`,
      options: [
        { nextQid: 200001, title: 'Report Bug', content: `I found a bug of Weex!` },
        { nextQid: 300001, title: 'Ask Questions', content: `I want to ask some questions about Weex.` },
        { nextQid: 400001, title: 'Just Chat' },
      ]
    }, {
      qid: 200001,
      content: `What kind of bug did you find?`,
      options: [
        { nextQid: 210001, title: 'Styles', content: 'Something wrong with the style.' },
        { nextQid: 201001, title: 'Component' },
        { nextQid: 202001, title: 'Module' },
      ]
    }, {
      qid: 201001,
      content: `Which component?`,
      defaultNextQid: 210001,
      options: [
        { title: 'loading', content: '<loading>' },
        { title: 'refresh', content: '<refresh>' },
        { title: 'video', content: '<video>' },
        { title: 'web', content: '<web>' },
        { title: 'cell', content: '<cell>' },
        { title: 'scroller', content: '<scroller>' },
        { title: 'slider', content: '<slider>' },
        { title: 'indicator', content: '<indicator>' },
        { title: 'input', content: '<input>' },
        { title: 'list', content: '<list>' },
        { title: 'a', content: '<a>' },
        { title: 'textarea', content: '<textarea>' },
        { title: 'image', content: '<image>' },
        { title: 'text', content: '<text>' },
        { title: 'div', content: '<div>' },
      ]
    }, {
      qid: 202001,
      content: `Which module?`,
      defaultNextQid: 210001,
      options: [
        { title: 'dom' },
        { title: 'animation' },
        { title: 'WebSocket' },
        { title: 'webview' },
        { title: 'navigator' },
        { title: 'picker' },
        { title: 'stream' },
        { title: 'storage' },
        { title: 'clipboard' },
        { title: 'globalEvent' },
        { title: 'modal' },
        { title: 'meta' },
      ]
    }, {
      qid: 210001,
      content: `Which platform occurs the bug?`,
      options: [
        { nextQid: 220001, title: 'Android' },
        { nextQid: 220001, title: 'iOS' },
        { nextQid: 220001, title: 'Web' },
        { nextQid: 220001, title: 'Both Android and iOS' },
        { nextQid: 220001, title: 'Every platform' }
      ]
    }, {
      qid: 220001,
      content: `Do you know the version of the Weex SDK? You can type it to me.`,
      options: [
        { nextQid: 230001, title: 'No', content: 'No, how do I get it?' }
      ]
    }, {
      qid: 230001,
      content: `Scan the QR code can get the env message of your phone.`,
      options: [
        { nextQid: 240001, title: 'OK' },
      ]
    }, {
      qid: 240001,
      content: `Can you offer an example to reproduce that bug?`,
      options: [
        { nextQid: 900000, title: 'OK' },
      ]
    }, {
      qid: 300001,
      content: `What do you want to know?`,
      options: [
        { nextQid: 900002, title: 'About Weex', content: 'I want to know something about Weex.' },
        { nextQid: 900000, title: 'How to write CSS?' },
        { nextQid: 900000, title: 'How to learn JS?' },
        { nextQid: 900000, title: 'How to learn English?' },
      ]
    }, {
      qid: 400001,
      content: `→_→`,
      options: [
        { reset: true,  nextQid: 100001, title: 'You win...' },
        { nextQid: 400001, title: '←_←' },
        { nextQid: 400002, title: '(ー_ー)!!', content: '(ー_ー)!! Could you stop ...' },
      ]
    }, {
      qid: 400002,
      content: `(◔◡◔) What do you think? I am an offline robot.`,
      options: [
        { reset: true,  nextQid: 100001, title: 'You win...' },
        { reset: true,  nextQid: 100001, title: '╮(￣▽￣")╭ ' },
      ]
    }, {
      qid: 900000,
      content: `Sorry, I don't know.`,
      options: [
        { reset: true, nextQid: 100001, title: 'All right' }
      ]
    }, {
      qid: 900001,
      doNotRecord: true,
      content: `Sorry, I don't understand Chinese. Please talk to me in English.`,
      options: []
    }, {
      qid: 900002,
      content: `I don't tell you.`,
      options: [
        { nextQid: 400001, title: '←_←' }
      ]
    }, {
      qid: 999999,
      content: `Something wrong.`,
      options: [
        { reset: true, nextQid: 100001, title: 'Reset' }
      ]
    }
  ]

  function select (qid) {
    return new Promise((resolve, reject) => {
      const qa = QADB.find(item => item.qid === qid)
      qa ? resolve(qa) : reject()
    })
    return QADB.find(item => item.qid === qid)
  }

  const dba = { QADB, select }
  function containChinese (string) {
    return !!/.*[\u4e00-\u9fa5]+.*$/.test(string)
  }
  
  function matchAnswer (question, answer) {
    const candidates = answer.options
    let chosen = 900000
    console.log(question)
    if (containChinese(question.title)) {
      return 900001
    }
    candidates.forEach(option => {
      // TODO: smarter
      if (question.title == option.title) {
        chosen = option.nextQid || answer.defaultNextQid
        return
      }
    })
  
    // TODO: fallback
  
    return chosen
  }
  
  function selectAndAnswer (qid, callback) {
    return dba.select(qid)
      .then(callback)
      .catch(() => {
        selectAndAnswer(999999)
          .then(callback)
          .catch(() => {
            setTimeout(() => {
              selectAndAnswer(999999)
            }, 2000)
          })
      })
  }
  
  let lastAnswer = null
  function judge (chatHistory) {
    const lastQuestion = Object.assign({}, chatHistory[chatHistory.length - 1])
    let qid = 100001
    return new Promise((resolve, reject) => {
      if (lastAnswer) {
        qid = matchAnswer(lastQuestion, lastAnswer)
      }
      selectAndAnswer(qid, answer => {
        // console.log(answer)
        const realAnswer = Object.assign(answer)
        if (answer.doNotRecord) {
          realAnswer.options = lastAnswer.options
        } else {
          lastAnswer = realAnswer
        }
        resolve(realAnswer)
      })
    })
  }
  import Utils from "../../../common/js/utils";
  const dom = weex.requireModule('dom');
  const modal = weex.requireModule('modal');
  const navigator = weex.requireModule("navigator");
  export default {
    data () {
      return {
        headUrl:"http://120.27.26.19:9527/img/head.jpg",
        title:"内部消息",
        theme:"#555555",
        userInput: '',
        chatMessages: [],
        feedbackOptions: []
      }
    },
    components: {
        "minbar": require("../../../common/component/minbar.vue")
    },
    created () {
      var s = this;
      Utils.getThemeColor((color,theme) => {
        if(theme=="theme1"){
            s.theme = "#F15A49";
        }else{
            s.theme = color;
        }
      });
      judge([]).then(result => {
        this.reply(result)
      });
      var globalEvent = weex.requireModule("globalEvent");
      globalEvent.addEventListener("onPageInit", function(e) {
        var param = navigator.param();
        if(!param) return;
        s.title = "与" + param["title"] + "聊天中",
        s.headUrl = param["url"];
      });
    },
    methods: {
      reply (feedback) {
        const { content, options } = feedback
        if (content) {
          this.chatMessages.push({ from: 'bot', content })
          this.feedbackOptions = options || []
          this.scrollToBottom()
        }
      },
      handleUserInput () {
        if (this.userInput) {
          this.sendUserMessage({
            title: this.userInput,
          })
          this.userInput = ''
        }
      },
      scrollToBottom () {
        if (!this.$refs.messages) {
          return
        }
        const lastMessage = this.$refs.messages[this.$refs.messages.length - 1]
        setTimeout(() => {
          dom.scrollToElement(lastMessage, {
            offset: 0,
            animated: true
          })
        }, 0)
      },
      sendUserMessage (message) {
        if (message) {
          this.chatMessages.push(Object.assign({
            from: 'user'
          }, message))
          this.scrollToBottom()
          judge(this.chatMessages)
            .then(result => {
              this.reply(result)
            })
        }
      },
      fastReply (index) {
        const message = this.feedbackOptions[index]
        if (message) {
          this.sendUserMessage(message)
        }
      }
    }
  }
</script>
<style scoped>
  .wrapper {
    justify-content: space-between;
  }
  .chat-list {
    flex: 1;
    background-color: #F3F3F3;
  }
  .chat-spliter {
    height: 1px;
    margin: 80px;
    background-color: #CCC;
  }
  .message {
    flex-direction: row;
    align-items: flex-start;
  }
  .message-bot {
    justify-content: flex-start;
  }
  .message-user {
    justify-content: flex-end;
  }
  .chat-icon {
    width: 80px;
    height: 80px;
    margin-top: 18px;
    margin-left: 15px;
    background-color: #DDD;
    border-radius: 5px;
  }
  .message-box {
    width: auto;
    /* width: 600px; */
    margin-top: 15px;
    margin-bottom: 15px;
    padding-top: 22px;
    padding-bottom: 22px;
    padding-left: 25px;
    padding-right: 25px;
    border-radius: 12px;
    border-color: #DDD;
    border-width: 1px;
  }
  .message-box-bot {
    margin-left: 15px;
    background-color: #FFF;
  }
  .message-box-bot:active {
    background-color: #EEE;
  }
  .message-box-user {
    margin-right: 15px;
    background-color: #99E152;
  }
  .message-box-user:active {
    background-color: #71CA2D;
  }
  .message-text {
    max-width: 510px;
    font-size: 32px;
    color: #454545;
  }
  .operations {
    height: 200px;
    flex: 0;
    border-top-width: 1px;
    border-top-style: solid;
    border-top-color: #E0E0E0;
    justify-content: flex-end;
    background-color: #F5F5F5;
  }
  .chat-options {
    flex-direction: row;
    align-items: center;
    height: 100px;
    padding-left: 10px;
    padding-right: 10px;
  }
  .btn-option {
    /* width: auto; */
    height: 75px;
    justify-content: center;
    border-width: 1px;
    border-color: #C5C5C5;
    background-color: #FFFFFF;
    border-radius: 10px;
    padding-left: 25px;
    padding-right: 25px;
    margin-left: 8px;
    margin-right: 8px;
  }
  .btn-option:active {
    background-color: #EEEEEE;
  }
  .btn-option-text {
    color: #808080;
    font-size: 28px;
  }
  .input-bar {
    height: 100px;
    border-top-width: 1px;
    border-top-color: #DDD;
    background-color: #FFFFFF;
    flex-direction: row;
    justify-content: space-between;
  }
  .input {
    width: 610px;
    height: 98px;
    padding-left: 30px;
    color: #606060;
    background-color: #FFFFFF;
    font-size: 32px;
  }
  .btn-send {
    width: 140px;
    justify-content: center;
    border-left-width: 1px;
    border-left-color: #ddd;
    background-color: #F5F5F5;
  }
  .btn-send:active {
    background-color: #E0E0E0;
  }
  .btn-send-text {
    text-align: center;
    color: #606060;
    font-size: 32px;
  }
</style>
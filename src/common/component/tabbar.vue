<template>
  <div class="wxc-tab-page"
       :style="{backgroundColor:wrapBgColor }">
    <div class="tab-page-wrap" ref="tab-page-wrap">
      <div ref="tab-container"
           class="tab-container">
        <slot></slot>
      </div>
    </div>
    <div class="tab-title-list"
         :style="{ backgroundColor: tabStyles.bgColor, height: (tabStyles.height + (isIPhoneX ? 78 : 0))+'px',paddingBottom:isIPhoneX?'78px':'0'}">
      <div class="title-item"
           v-for="(v,index) in tabTitles"
           :key="index"
           :ref="'wxc-tab-title-'+index"
           @click="setPage(index,v.url)"
           :style="{ width: tabStyles.width +'px', height: tabStyles.height +'px', backgroundColor: currentPage == index ? tabStyles.activeBgColor : tabStyles.bgColor }"
           :accessible="true"
           :aria-label="`${v.title?v.title:'标签'+index}`">

        <image :src="currentPage == index ? v.activeIcon : v.icon"
               v-if="titleType === 'icon' && !titleUseSlot"
               :style="{ width: tabStyles.iconWidth + 'px', height:tabStyles.iconHeight+'px'}"></image>

        <text :class="['icon-font',isIphone?'no-margin':'']"
              v-if="titleType === 'iconFont' && v.codePoint && !titleUseSlot"
              :style="{fontFamily: 'iconfont',fontSize: tabStyles.iconFontSize+'px', color: currentPage == index ? tabStyles.activeIconFontColor : tabStyles.iconFontColor}">{{v.codePoint}}</text>
        <text
          v-if="!titleUseSlot"
          :style="{ fontSize: tabStyles.fontSize+'px', fontWeight: (currentPage == index && tabStyles.isActiveTitleBold)? 'bold' : 'normal', color: currentPage == index ? tabStyles.activeTitleColor : tabStyles.titleColor, paddingLeft:tabStyles.textPaddingLeft+'px', paddingRight:tabStyles.textPaddingRight+'px'}"
          class="tab-text">{{v.title}}</text>
        <div class="desc-tag" v-if="v.badge && !titleUseSlot">
          <text class="desc-text">{{v.badge}}</text>
        </div>
        <div v-if="v.dot && !v.badge && !titleUseSlot" class="dot"></div>
        <slot :name="`tab-title-${index}`" v-if="titleUseSlot"></slot>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .wxc-tab-page {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .tab-title-list {
    flex-direction: row;
    justify-content: space-around;
    border-top-color: #e1e1e1;
    border-top-width: 1px;
  }

  .title-item {
    justify-content: center;
    align-items: center;
    border-bottom-style: solid;
  }

  .tab-page-wrap {
    width: 750px;
    flex: 1;
    overflow: hidden;
  }

  .tab-container {
    flex: 1;
    flex-direction: row;
    position: absolute;
  }

  .tab-text {
    lines: 1;
    text-overflow: ellipsis;
  }

  .desc-tag {
    position: absolute;
    top: 10px;
    right: 22px;
    border-radius: 30px;
    background-color: #FF3E3E;
    height: 30px;
    width: 30px;
    align-items: center;
    justify-content: center;
    padding-left: 6px;
    padding-right: 6px;
  }

  .dot {
    width: 14px;
    height: 14px;
    border-bottom-right-radius: 14px;
    border-bottom-left-radius: 14px;
    border-top-left-radius: 14px;
    border-top-right-radius: 14px;
    position: absolute;
    top: 13px;
    right: 43px;
    background-color: #FF3E3E;
  }

  .desc-text {
    font-size: 20px;
    color: #ffffff;
  }

  .icon-font {
    margin-bottom: 8px;
  }
  .no-margin {
    margin-bottom: 0px;
  }
</style>

<script>
  const dom = weex.requireModule('dom');
  const animation = weex.requireModule('animation');
  import Utils from '../js/utils'

  export default {
    props: {
      tabTitles: {
        type: Array,
        default: () => ([])
      },
      tabStyles: {
        type: Object,
        default: () => ({
          bgColor: '#FFFFFF',
          titleColor: '#666666',
          activeTitleColor: '#3D3D3D',
          activeBgColor: '#FFFFFF',
          isActiveTitleBold: true,
          iconWidth: 70,
          iconHeight: 70,
          width: 160,
          height: 120,
          fontSize: 24,
          activeBottomColor: '#FFC900',
          activeBottomWidth: 120,
          activeBottomHeight: 6,
          textPaddingLeft: 10,
          textPaddingRight: 10
        })
      },
      titleType: {
        type: String,
        default: 'icon'
      },
      titleUseSlot: {
        type: Boolean,
        default: false
      },
      isTabView: {
        type: Boolean,
        default: true
      },
      duration: {
        type: [Number, String],
        default: 300
      },
      timingFunction: {
        type: String,
        default: 'cubic-bezier(0.25, 0.46, 0.45, 0.94)'
      },
      wrapBgColor: {
        type: String,
        default: '#f2f3f4'
      }
    },
    data: () => ({
      currentPage: 0,
      translateX: 0
    }),
    created () {
      const { titleType, tabStyles } = this;
      if (titleType === 'iconFont' && tabStyles.iconFontUrl) {
        // var font = weex.requireModule("font");
        // font.addFont('iconfont','http://at.alicdn.com/t/font_557063_l3ad49ffyz2utyb9.ttf')
      }
      this.isIPhoneX = Utils.env.isIPhoneX();
      this.isIphone = Utils.env.isIOS();
      if(this.isIphone==true){


      }
    },
    methods: {
      next () {
        let page = this.currentPage;
        if (page < this.tabTitles.length - 1) {
          page++;
        }
        this.setPage(page);
      },
      prev () {
        let page = this.currentPage;
        if (page > 0) {
          page--;
        }
        this.setPage(page);
      },
      setPage (page, url = null, animated = true) {
        if (!this.isTabView) {
          this.jumpOut(url);
          return;
        }
        const previousPage = this.currentPage;
        const currentTabEl = this.$refs[`wxc-tab-title-${page}`][0];
        const { width } = this.tabStyles;
        const appearNum = parseInt(750 / width);
        const tabsNum = this.tabTitles.length;
        const offset = page > appearNum ? -(750 - width) / 2 : -width * 2;

        if (appearNum < tabsNum) {
          (previousPage > appearNum || page > 1) && dom.scrollToElement(currentTabEl, {
            offset, animated
          });

          page <= 1 && previousPage > page && dom.scrollToElement(currentTabEl, {
            offset: -width * page,
            animated
          });
        }

        this.currentPage = page;
        this._animateTransformX(page, animated);
        this.$emit('wxcTabBarCurrentTabSelected', { page });
      },
      jumpOut (url) {
        url && Utils.goToH5Page(url)
      },
      _animateTransformX (page, animated) {
        const { duration, timingFunction } = this;
        const computedDur = animated ? duration : 0.00001;
        const containerEl = this.$refs[`tab-container`];
        const dist = page * 750;
        containerEl.s
        animation.transition(containerEl, {
          styles: {
            transform: `translateX(${-dist}px)`
          },
          duration: 0,
          timingFunction,
          delay: 0
        }, () => {
        });
      }
    }
  };
</script>

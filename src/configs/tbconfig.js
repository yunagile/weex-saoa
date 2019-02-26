export default {
    // 正常模式的tab title配置
    tabTitles: [
      {
        title: '消息',
        icon: 'https://gw.alicdn.com/tfs/TB1MWXdSpXXXXcmXXXXXXXXXXXX-72-72.png',
        activeIcon: 'https://gw.alicdn.com/tfs/TB1kCk2SXXXXXXFXFXXXXXXXXXX-72-72.png'
      },
      {
        title: '工作台',
        icon: 'https://gw.alicdn.com/tfs/TB1ARoKSXXXXXc9XVXXXXXXXXXX-72-72.png',
        activeIcon: 'https://gw.alicdn.com/tfs/TB19Z72SXXXXXamXFXXXXXXXXXX-72-72.png'
      },
      {
        title: '通讯录',
        icon: 'https://gw.alicdn.com/tfs/TB1VKMISXXXXXbyaXXXXXXXXXXX-72-72.png',
        activeIcon: 'https://gw.alicdn.com/tfs/TB1aTgZSXXXXXazXFXXXXXXXXXX-72-72.png',
        badge: 5
      },
      {
        title: '我的',
        icon: 'https://gw.alicdn.com/tfs/TB1Do3tSXXXXXXMaFXXXXXXXXXX-72-72.png',
        activeIcon: 'https://gw.alicdn.com/tfs/TB1LiNhSpXXXXaWXXXXXXXXXXXX-72-72.png',
        dot: true
      }
    ],
    tabStyles: {
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
      textPaddingLeft: 10,
      textPaddingRight: 10
    },
  
    // 使用 iconFont 模式的tab title配置
    tabIconFontTitles: [
      {
        title: '消息',
        codePoint: '\ue658',
        badge: 5
      },
      {
        title: '工作台',
        codePoint: '\ue761'
      },
      {
        title: '通讯录',
        codePoint: '\ue61d'
        
      },
      {
        title: '我的',
        codePoint: '\ue614',
        dot: true
      }
    ],
    tabIconFontStyles: {
      bgColor: '#FAFBFE',
      titleColor: '#828282',
      activeTitleColor: '#66538B',
      activeBgColor: '#FAFBFE',
      isActiveTitleBold: false,
      width: 160,
      height: 120,
      fontSize: 24,
      textPaddingLeft: 10,
      textPaddingRight: 10,
      iconFontSize: 50,
      iconFontColor: '#6A6A6A',
      activeIconFontColor: '#1B82D2',
      iconFontUrl: '//at.alicdn.com/t/font_557063_d1mc4j7gv509t3xr.ttf'
    }
  }
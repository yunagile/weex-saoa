//
//  WXWebUtilModule.m
//  AFNetworking
//
//  Created by  张杰 on 2018/5/2.
//

#import "WXWebUtilModule.h"
#import "farwolf.h"

@implementation WXWebUtilModule
WX_EXPORT_METHOD(@selector(openUrl:))
/*用浏览器打开网址*/
-(void)openUrl:(NSString*)url
{
    NSString *textUrl = url;
    NSURL *lastUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@",textUrl]];
    [[UIApplication sharedApplication] openURL:lastUrl];
}
@end

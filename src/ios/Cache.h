#import <Cordova/CDV.h>
#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import "AppDelegate.h"

@interface Cache : CDVPlugin
{
    // Member variables go here.
}
- (void)getCacheSize:(CDVInvokedUrlCommand *)command;
- (void)clearCache:(CDVInvokedUrlCommand *)command;

// retain command for async repsonses
@property (nonatomic, strong) CDVInvokedUrlCommand* command;

@end

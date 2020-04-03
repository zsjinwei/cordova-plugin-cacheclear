#import <Cordova/CDV.h>
#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>
#import "AppDelegate.h"

@interface cacheclear : CDVPlugin
{
    // Member variables go here.
}
- (void)getCacheSize:(CDVInvokedUrlCommand *)command;
- (void)clearCache:(CDVInvokedUrlCommand *)command;

@end

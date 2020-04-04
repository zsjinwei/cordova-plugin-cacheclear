/*
  Copyright 2014 Modern Alchemists OG

  Licensed under MIT.

  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
  documentation files (the "Software"), to deal in the Software without restriction, including without limitation
  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
  to permit persons to whom the Software is furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all copies or substantial portions of
  the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
  THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
*/

package com.stardrv.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;

@TargetApi(19)
public class Cache extends CordovaPlugin {

  private static final String LOG_TAG = "CacheClear";
  private CallbackContext callbackContext;

  /**
   * Constructor.
   */
  public Cache() {}

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("clearCache")) {
      this.clearCache(callbackContext);
      return true;
    } else if (action.equals("getCacheSize")) {
      this.getCacheSize(callbackContext);
      return true;
    }
    return false;
  }

  /**
   * 缓存清理
   *
   * @param callbackContext
   * @return
   */
  private void clearCache(CallbackContext callbackContext) {
    try {
      Log.v(LOG_TAG,"Cordova Android clearCache() called.");
      this.callbackContext = callbackContext;
      final Cache self = this;

      cordova.getActivity().runOnUiThread( new Runnable() {
        public void run() {
          try {
            // clear the cache
            self.webView.clearCache(true);

            // clear the data
            self.clearApplicationData();

            // send success result to cordova
            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.setKeepCallback(false);
            self.callbackContext.sendPluginResult(result);
          }
          catch( Exception e ) {
            String msg = "Error while clearing webview cache.";
            Log.e(LOG_TAG, msg);
            // return error answer to cordova
            PluginResult result = new PluginResult(PluginResult.Status.ERROR, msg);
            result.setKeepCallback(false);
            self.callbackContext.sendPluginResult(result);
          }
        }
      });
    } catch (Exception e) {
      e.getCause();
      PluginResult result = new PluginResult(PluginResult.Status.ERROR, "clearCache failed");
      result.setKeepCallback(false);
      callbackContext.sendPluginResult(result);
    }
  }

  private void clearApplicationData() {
    File appDir = this.cordova.getActivity().getCacheDir();
    // File appDir = new File(cache.getParent());
    Log.i(LOG_TAG, "Absolute path: " + appDir.getAbsolutePath());
    if (appDir.exists()) {
      String[] children = appDir.list();
      for (String s : children) {
        if (!s.equals("lib")) {
          deleteDir(new File(appDir, s));
          Log.i(LOG_TAG, "File /data/data/APP_PACKAGE/" + s + " DELETED");
        }
      }
    }
  }

  private static boolean deleteDir(File dir) {
    Log.i(LOG_TAG, "Deleting: " + dir.getAbsolutePath());
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return dir.delete();
  }

  /**
   * @param file
   * @return
   * @throws Exception
   */
  public void getCacheSize(CallbackContext callbackContext) {
    File appDir = cordova.getActivity().getCacheDir();
    // File appDir = new File(cache.getParent());
    try {
      String fileSize = getFormatSize(getFolderSize(appDir));
      callbackContext.success(fileSize);
    } catch (Exception e) {
      callbackContext.error("获取缓存长度失败");
    }
  }

  /**
   * @param file
   * @return
   * @throws Exception
   */
  public long getFolderSize(File file) {
    long size = 0;
    File[] fileList = file.listFiles();
    for (int i = 0; i < fileList.length; i++) {
      if (fileList[i].isDirectory()) {
        size = size + getFolderSize(fileList[i]);
      } else {
        size = size + fileList[i].length();
      }
    }
    return size;
  }

  /**
   * @param size
   * @return
   */
  public static String getFormatSize(double size) {
    double kiloByte = size / 1024;
    if (kiloByte < 1) {
      return "0KB";
    }

    double megaByte = kiloByte / 1024;
    if (megaByte < 1) {
      BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
      return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
        .toPlainString() + "KB";
    }

    double gigaByte = megaByte / 1024;
    if (gigaByte < 1) {
      BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
      return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
        .toPlainString() + "MB";
    }

    double teraBytes = gigaByte / 1024;
    if (teraBytes < 1) {
      BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
      return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
        .toPlainString() + "GB";
    }
    BigDecimal result4 = new BigDecimal(teraBytes);
    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
      + "TB";
  }
}

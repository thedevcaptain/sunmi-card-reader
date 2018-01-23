/**
 */
package it.piratafrancis;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import java.util.Date;

public class SunmiCardReader extends CordovaPlugin {

	private int i = 0;
	private static final String TAG = "SunmiCardReader";

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}

	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if ("echo".equals(action)) {
			this.i++;
			callbackContext.success(i);
		}
		return false;  // Returning false results in a "MethodNotFound" error.
	}
}

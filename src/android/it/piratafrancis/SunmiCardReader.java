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
import android.os.RemoteException;
import android.content.Context;

import java.util.Date;

import com.sunmi.pay.hardware.aidl.AidlConstants;
import com.sunmi.pay.hardware.aidl.bean.CardInfo;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardCallback;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardOpt;
import com.sunmi.pay.hardware.aidl.system.BasicOpt;

import sunmi.paylib.SunmiPayKernel;


public class SunmiCardReader extends CordovaPlugin {

	private static ReadCardOpt opt;
	private static BasicOpt bOpt;

	private static final String TAG = "SunmiCardReader";

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		initItAll();
	}

	private void initItAll(){
		Context applicationContext = this.cordova.getActivity().getApplicationContext();
		SunmiPayKernel.getInstance().connectPayService(applicationContext, new SunmiPayKernel.ConnCallback() {
			@Override
			public void onServiceConnected() {
				opt = SunmiPayKernel.getInstance().mReadCardOpt;
				bOpt = SunmiPayKernel.getInstance().mBasicOpt;
			}

			@Override
			public void onServiceDisconnected() {

			}
		});
	}

	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if(opt == null || bOpt == null){
			initItAll();
		}
		if(action.equals("startReadCard")){
			try{
				int o = 0;
				for(int i =0;i<args.length();i++){
					o += args.getInt(i);
				}
				opt.checkCard(
						o
						,new ReadCardCallback.Stub() {
							@Override
							public void onCardDetected(CardInfo cardInfo) throws RemoteException {
								Card c = new Card();
								c.type = cardInfo.cardType;
								if(cardInfo.cardType == AidlConstants.CardType.MAGNETIC.getValue()){
									c.t1 = cardInfo.track1;
									c.t2 = cardInfo.track2;
									c.t3 = cardInfo.track3;

									OptRes result = new OptRes();
									result.setRes(0);
									result.setValue(c);
									try{
										callbackContext.success(result.toJSONObject());
									}catch(JSONException e){
										callbackContext.error(e.getMessage());
									}
								}else if(cardInfo.cardType == AidlConstants.CardType.MIFARE.getValue()){
									OptRes result = new OptRes();
									result.setRes(0);
									result.setValue(c);
									try{
										callbackContext.success(result.toJSONObject());
									}catch(JSONException e){
										callbackContext.error(e.getMessage());
									}
								}
							}

							@Override
							public void onError(int i, String s) throws RemoteException {
								callbackContext.error(s);
							}

							@Override
							public void onStartCheckCard() throws RemoteException {

							}
						},250);
				return true;
			}catch(RemoteException e){
				return false;
			}catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}else if(action.equals("authMifare")){
			//ARGS 
			/*
			[
				blockKey,
				keyA = 0 | keyB = 1
				blockToRead
			]
			*/
			try{
				byte[] key = hexStringToByteArray(args.getString(0));
				int res = opt.mifareAuth(args.getInt(1),args.getInt(2),key);
				OptRes result = new OptRes();
				result.setRes(res);
				callbackContext.success(result.toJSONObject());
			}catch(RemoteException e){
				callbackContext.error("Other:"+e.getMessage());
			}catch (JSONException e) {
				callbackContext.error("Json:"+e.getMessage());
			}
		}else if(action.equals("readBlockMifare")){
			//ARGS 
			/*
			[
				blockToRead
			]
			*/
			try{
				byte[] blockData = new byte[260];
				int res = opt.mifareReadBlock(args.getInt(0),blockData);
				if(res == 0){
					Card c = new Card();
					c.type = AidlConstants.CardType.MIFARE.getValue();
					c.setMiFareData(getReadableBlock(blockData));
					OptRes result = new OptRes();
					result.setRes(res);
					result.setValue(c);
					callbackContext.success(result.toJSONObject());
				}else{
					OptRes result = new OptRes();
					result.setRes(res);
					callbackContext.error(result.toJSONObject());
				}
				opt.cancelCheckCard();
			}catch(RemoteException e){
				callbackContext.error(e.getMessage()+"Remote");
			}catch (JSONException e) {
				e.printStackTrace();
				callbackContext.error(e.getMessage()+"Json");
			}
		}else if(action.equals("writeBlockMifare")){
			//ARGS
			/*
			[
				blockToWrite,
				stringToWrite
			]
			*/
			try{
				byte[] blockData = hexStringToByteArray(args.getString(1));
				int res = opt.mifareWriteBlock(args.getInt(0),blockData);
				if(res == 0){
					Card c = new Card();
					c.type = AidlConstants.CardType.MIFARE.getValue();
					c.setMiFareData(args.getString(1));
					OptRes result = new OptRes();
					result.setRes(res);
					result.setValue(c);
					callbackContext.success(result.toJSONObject());
				}else{
					OptRes result = new OptRes();
					result.setRes(res);
					callbackContext.error(result.toJSONObject());
				}
				opt.cancelCheckCard();
			}catch(RemoteException e){
				callbackContext.error(e.getMessage()+"Remote");
			}catch (JSONException e) {
				e.printStackTrace();
				callbackContext.error(e.getMessage()+"Json");
			}
		}else if(action.equals("setPinStatus")){
			//ARGS
			/*
			[
				pinToEdit,
				statusResolved
			]
			*/
			try{

				int res = bOpt.ledStatusOnDevice(args.getInt(0),args.getInt(1));
				if(res == 0){
					OptRes result = new OptRes();
					result.setRes(res);
					callbackContext.success(result.toJSONObject());
				}else{
					OptRes result = new OptRes();
					result.setRes(res);
					callbackContext.error(result.toJSONObject());
				}
			}catch(RemoteException e){
				callbackContext.error(e.getMessage()+"Remote");
			}catch (JSONException e) {
				e.printStackTrace();
				callbackContext.error(e.getMessage()+"Json");
			}
		}else if(action.equals("cancelCheckCard")){
			try{

				opt.cancelCheckCard();

				OptRes result = new OptRes();

				callbackContext.success(result.toJSONObject());
			}catch(RemoteException e){
				callbackContext.error(e.getMessage()+"Remote");
			}catch (JSONException e) {
				e.printStackTrace();
				callbackContext.error(e.getMessage()+"Json");
			}
		}
		return false;  // Returning false results in a "MethodNotFound" error.
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private String getReadableBlock(byte[] data){
		String res = bytesToHex(data);
		return res.substring(4,36);
	}

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}

	class LocalString  implements LocalSerializable{

		String bol;

		public LocalString(String b) {
			this.bol = b;
		}

		@Override
		public Object toJSONObject() throws JSONException {
			return this.bol;
		}
	}

	class OptRes implements LocalSerializable{
		private Integer res;
		private LocalSerializable value;
		public OptRes(){
			this.value = new LocalSerializable() {
				@Override
				public Object toJSONObject() throws JSONException {
					return false;
				}
			};
		}
		public void setRes(Integer r){
			this.res = r;
		}

		public Integer getRes(){
			return this.res;
		}

		public void setValue(LocalSerializable r){
			this.value = r;
		}

		public LocalSerializable getValue(){
			return this.value;
		}

		public JSONObject toJSONObject() throws JSONException{
			JSONObject o = new JSONObject();
			o.put("res",res);
			o.put("value",value.toJSONObject());
			return o;
		}

	}

	interface LocalSerializable{
		public Object toJSONObject() throws JSONException;
	}

	class Card implements LocalSerializable{
		private String t1;
		private String t2;
		private String t3;
		private String miFareData;
		private Integer type;

		public String getT1() {
			return t1;
		}

		public void setT1(String t1) {
			this.t1 = t1;
		}

		public String getT2() {
			return t2;
		}

		public void setT2(String t2) {
			this.t2 = t2;
		}

		public String getT3() {
			return t3;
		}

		public void setT3(String t3) {
			this.t3 = t3;
		}

		public String getMiFareData() {
			return miFareData;
		}

		public void setMiFareData(String miFareData) {
			this.miFareData = miFareData;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}
		public JSONObject toJSONObject() throws JSONException{
			JSONObject o = new JSONObject();
			o.put("type",type);
			o.put("t1",t1);
			o.put("t2",t2);
			o.put("t3",t3);
			o.put("miFareData",miFareData);
			return o;
		}
	}
}

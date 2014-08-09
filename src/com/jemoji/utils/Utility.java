
package com.jemoji.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class Utility {
	public static String getScreenParams(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return "&screen="
				+ (dm.heightPixels > dm.widthPixels ? dm.widthPixels + "*" + dm.heightPixels
						: dm.heightPixels + "*" + dm.widthPixels);
	}

	public static String generateUrlParams(Map params) {
		StringBuilder result = new StringBuilder();

		Iterator it = params.keySet().iterator();
		Object key;
		while (it.hasNext()) {
			key = it.next();
			result.append(String.format("%s=%s&", key, params.get(key)));
		}
		if (result.length() > 0) result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	public static void Assert(boolean cond) {
		if (!cond) {
			throw new AssertionError();
		}
	}

	/**
	 * 获取当前方法的函数名称
	 * 
	 * @return
	 */
	public static String getCurrentFunctionName() {
		return getCurrentFunctionName(1);
	}

	/**
	 * 获取当前方法堆栈中第index位置的函数名称
	 * 
	 * @param index 例如 1表示当前方法
	 * @return
	 */
	public static String getCurrentFunctionName(int index) {
		StackTraceElement traceElement = Thread.currentThread().getStackTrace()[index + 3];
		return traceElement.getMethodName();
	}

	public static void printlnList(String tag, @SuppressWarnings("rawtypes")
	Iterator it) {
		Object o = null;
		while (it.hasNext()) {
			o = it.next();
			System.out.println(String.format("<%s>%s", tag, o));
		}
	}

	public static class Densitys {

		/**
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
		 */
		public static float dip2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return dpValue * scale + 0.5f;
		}

		/**
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
		 */
		public static float dip2px(float scale, float dpValue) {
			return dpValue * scale + 0.5f;
		}

		/**
		 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
		 */
		public static float px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return pxValue / scale + 0.5f;
		}
	}

	public static class Format {
		/**
		 * 将单位装换为K，M的计量
		 * 
		 * @param size
		 * @return
		 */
		public static String sizeUnit(int size) {
			DecimalFormat df = new DecimalFormat("###.##");
			float f;
			if (size < 1024 * 1024) {
				f = (float)((float)size / (float)1024);
				return (df.format(new Float(f).doubleValue()) + "K");
			} else {
				f = (float)((float)size / (float)(1024 * 1024));
				return (df.format(new Float(f).doubleValue()) + "M");
			}
		}
	}

	public static class Dialog {
		public static void confirmAction(Context context, String title, String message,
				final android.content.DialogInterface.OnClickListener listener) {
			new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setTitle(
					title).setMessage(message).setPositiveButton(android.R.string.ok, listener)
					.setNegativeButton(android.R.string.cancel, listener).create().show();
		}

		public static void confirmAction(Context context, String title, String message,
				final Runnable action) {
			OnClickListener listener = new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							if (action != null) action.run();
					}
				}
			};
			confirmAction(context, title, message, listener);
		}

		public static void confirmAction(Context context, int title, int message,
				final Runnable action) {
			String ts = context.getResources().getString(title);
			String ms = context.getResources().getString(message);
			confirmAction(context, ts, ms, action);
		}

		public static void confirmListAction(Context context, String title, String[] items,
				OnClickListener listener) {
			confirmListAction(context, title, items, listener, false);
		}

		public static void confirmListAction(Context context, String title, String[] items,
				OnClickListener listener, boolean cancelable) {
			AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setItems(items,
					listener).create();
			dialog.setCanceledOnTouchOutside(cancelable);
			dialog.show();
		}
	}

	public static class Widget {
		public static void setBackIcon(android.app.ActionBar actionbar, Drawable backIcon) {
	        try {
	            Method method = Class.forName("android.app.ActionBar").getMethod(
	                    "setBackButtonDrawable", new Class[] { Drawable.class });
	            try {
	                method.invoke(actionbar, backIcon); 
	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            } catch (NotFoundException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            } catch (InvocationTargetException e) {
	                e.printStackTrace();
	            }
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static class Url {
		public static final int TAOBAO_IMAGE_URL_SIZE_SMALL = 0;
		public static final int TAOBAO_IMAGE_URL_SIZE_MIDDLE = 1;
		public static final int TAOBAO_IMAGE_URL_SIZE_LARGE = 2;
		public static final int TAOBAO_IMAGE_URL_SIZE_SOURCE = 3;// 原图
		// 110, 60, 80, 90, 100, 30, 120, 160, 180, 310, 460, 640
		private static final Pattern TAOBAO_URL_IMAGE_SIZE_PATTERN = Pattern
				.compile("_\\d+x\\d+.jpg");

		public static String generateTaobaoImageUrl(String url, int type) {
			if (url == null) return null;
			int size = 310;
			switch (type) {
				case TAOBAO_IMAGE_URL_SIZE_SMALL:
					size = 240;
					break;
				case TAOBAO_IMAGE_URL_SIZE_MIDDLE:
					size = 310;
					break;
				case TAOBAO_IMAGE_URL_SIZE_LARGE:
					size = 640;
					break;
			}

			String result;
			Matcher m = TAOBAO_URL_IMAGE_SIZE_PATTERN.matcher(url);
			String end = String.format("_%sx%s.jpg", size, size);
			
			if(m.find()) result = m.replaceFirst(end);
			else result = url + end;
			
			if (TAOBAO_IMAGE_URL_SIZE_SOURCE == type) {
				result = m.replaceFirst("");
			}
			return result;
		}

		public static String generateTaobaoImageSourceUrl(String url) {
			return generateTaobaoImageUrl(url, TAOBAO_IMAGE_URL_SIZE_SOURCE);
		}
	}

	public static class File {
		public static boolean saveBitmap(java.io.File file, android.graphics.Bitmap bitmap)
				throws FileNotFoundException {
			OutputStream outStream = new FileOutputStream(file);
			return bitmap.compress(CompressFormat.PNG, 75, outStream);
		}
		
		public static String unit(int size) {
			DecimalFormat df = new DecimalFormat("###.##");
			float f;
			if (size < 1024 * 1024) {
				f = (float) ((float) size / (float) 1024);
				return (df.format(new Float(f).doubleValue()) + "K");
			} else {
				f = (float) ((float) size / (float) (1024 * 1024));
				return (df.format(new Float(f).doubleValue()) + "M");
			}
		}
		
		/**
		 * 压缩图片
		 * @param bmp
		 * @param maxSize 单位KB哦
		 * @return
		 */
		public  static Bitmap zoomImage(Bitmap bmp, double maxSize) {
			Bitmap bitMap = bmp;
			double mid = bmpToByteArray(bitMap, false).length / 1024;
			while (mid > maxSize) {
				double i = mid / maxSize;
				bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight()
						/ Math.sqrt(i));
				mid = bmpToByteArray(bitMap, false).length / 1024;
			}
			return bitMap;
		}

		public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
			float width = bgimage.getWidth();
			float height = bgimage.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float)newWidth) / width;
			float scaleHeight = ((float)newHeight) / height;
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int)width, (int)height, matrix,
					true);
			return bitmap;
		}

		public static byte[] bmpToByteArray(final android.graphics.Bitmap bmp,
				final boolean needRecycle) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.PNG, 75, output);
			if (needRecycle) {
				bmp.recycle();
			}

			byte[] result = output.toByteArray();
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		public static byte[] readFile(java.io.File file) {
			int size = (int)file.length();
			byte[] bytes = new byte[size];
			try {
				BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
				buf.read(bytes, 0, bytes.length);
				buf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bytes;
		}
	}

	public static class Information {
		public final static int DEVICES_ID = 0;
		public final static int VERSION_NAME = 1;
		public final static int CHANNEL_NAME = 2;
		public final static int BRAND = 3;
		public final static int VERSION_CODE = 4;
		public final static int PREVIEW = 5;
		private final static HashMap<Integer, String> values = new LinkedHashMap<Integer, String>();
		
		public final static String PREVIEW_FROM_BANNER_FORMAT = "BANNER";
		public final static String PREVIEW_FROM_SEARCH_FORMAT = "SEARCH_%s";
		public final static String PREVIEW_FROM_ENTITY_FORMAT = "ENTITY_%s";
		public final static String PREVIEW_FROM_NOVUS_FORMAT = "NOVUS";//新品点击，实际上现在已经改成了热门
		public final static String PREVIEW_FROM_SELECTION_FORMAT = "SELECTION";
		public final static String PREVIEW_FROM_POPULAR_FORMAT = "POPULAR_%s";//[ DAY | WEEK ]
		public final static String PREVIEW_FROM_CATEGORY_FORMAT = "CATEGORY_%s_%s";//CATEGORY_id_[entity | note | liked]
		public final static String PREVIEW_FROM_FEED_FORMAT = "FEED_%s";//[ FRIEND | SOCIAL ]
		public final static String PREVIEW_FROM_MESSAGE_FORMAT = "MESSAGE";
		public final static String PREVIEW_FROM_USER_FORMAT = "USER_%s_%s";//USER_id_[liked | note | tag]
		public final static String PREVIEW_FROM_TAG_FORMAT = "TAG_%s";
		public final static String PREVIEW_FROM_NOTE_FORMAT = "NOTE_%s";
		public final static String PREVIEW_FROM_DISCOVER_FORMAT = "DISCOVER_%s";//DISCOVER ( _[ GROUP_$id | RANDOM ] ) 
		public final static String PREVIEW_FROM_POPULAR_CATEGORY_FORMAT = "POPULAR_CATEGORY";
		public final static String PREVIEW_FROM_EXTERNAL_FORMAT = "EXTERNAL_wx";

		public static void init(Context context) {
			values.put(VERSION_NAME, getVersionName(context));
			values.put(VERSION_CODE, getVersionCode(context) + "");
			values.put(DEVICES_ID, getDevicesId(context));
			values.put(CHANNEL_NAME, getMetaData(context, "UMENG_CHANNEL"));
			values.put(BRAND, getDevicesBrand());
		}

		public static String getValue(int key) {
			String value = values.get(key);
			if (values.isEmpty()) throw new IllegalArgumentException(String.format(
					"Value of %s not initlized ", key));
			else return value;
		}
		
		public static void setPreview(String pre){
			values.put(PREVIEW, pre);
		}
		
		public static String pokePreview(){
			String value = values.get(PREVIEW); 
			values.put(PREVIEW, null);
			return value;
		}
		
		private static String getMetaData(Context context, String key){
			ApplicationInfo info;
			String result = null;
	        try {
	            info = context.getPackageManager().getApplicationInfo(context. getPackageName(), PackageManager.GET_META_DATA);
	            result = info.metaData.get(key).toString();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return result;
		}
		
		public static boolean hasSmartBar() {
			try {
				Method method = Class.forName("android.os.Build").getMethod(
						"hasSmartBar");
				return ((Boolean) method.invoke(null)).booleanValue();
			} catch (Exception e) {
			}
	 
			if (Build.VERSION.SDK_INT >= 14 && Utility.Information.isMeizu()) {
				return true;
			} else return false;
		}

		public static String getVersionName(Context context) {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = null;
			try {
				packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			String version = (packInfo == null ? "" : packInfo.versionName);
			return version;
		}
		
		public static int getVersionCode(Context context) {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = null;
			try {
				packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			int version = (packInfo == null ? 0 : packInfo.versionCode);
			return version;
		}

		public static String getDevicesId(Context context) {
			TelephonyManager tm = (TelephonyManager)context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = tm.getDeviceId();
			return deviceId;
		}
		
		public static String getDevicesBrand(){
			return Build.BRAND;
		}
		
		public static boolean isMeizu(){
			return "meizu".equalsIgnoreCase(getDevicesBrand());
		}
	}
	
	public static class Strings{
		public static boolean isEmptyString(String str) {
			if(str == null) return true;
			return "".equals(str.trim());
		}
	}
}
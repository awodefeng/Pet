/**
 * 
 */
package com.xxun.watch.xunpet.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;


public class ToastUtil {


	public static Toast toastStr;
	public synchronized static void show(final Context context, final String info) {

		if (toastStr == null) {
			toastStr = Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_LONG);
			LinearLayout layout = (LinearLayout) toastStr.getView();
			TextView tv = (TextView) layout.getChildAt(0);
			tv.setTextSize(42);
			toastStr.setGravity(Gravity.TOP, 0, 0);
		} else {
			toastStr.setText(info);
		}
		toastStr.show();
	}


	public static Toast toastInt;
	public synchronized static void show(Context context, int info) {
		if (toastInt == null) {
			toastInt = Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_SHORT);
		} else {
			toastInt.setText(info);
		}
		toastInt.show();
	}
}

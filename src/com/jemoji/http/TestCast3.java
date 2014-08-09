
package com.jemoji.http;

import java.util.concurrent.CountDownLatch;

import android.test.AndroidTestCase;

import com.jemoji.image.RequestManager;
import com.jemoji.utils.LOG;

public class TestCast3 extends AndroidTestCase {
	// //////////////////////////////////////////////////////////////////////////////////////////

	private Throwable wrong;

	private void checkWrong() throws Throwable {
		if (wrong != null) {
			throw wrong;
		}
	}

	CountDownLatch latch;

	private void preRequest() {
		this.latch = new CountDownLatch(1);
	}

	private void checkResponse(int code, Object json, Throwable error) {
		switch (code) {
			case 200:
				System.out.println(String.format("%s", json));
				break;

			default:
				LOG.e(LOG.TAG_API, error);
				wrong = error;
				break;
		}
		latch.countDown();
	}

	private void finishResponse() throws Throwable {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		checkWrong();
	}

	public void testCase() throws Throwable {
		RequestManager.init(getContext());
		URLs.changeEnvoriment(URLs.ENVORIMENT_FORMAL);
		
		String ur = URLs.getAbsoluteUrl(" http://www.baidu.com");
		System.out.println(String.format(" =========== %s   %s", ur, ur.trim().indexOf("http")));

		/*String IMG = "/1407549723664.amr";
		preRequest();
		GKHttpInterface.genFile(IMG, "amr", new GKJsonResponseHandler() {
			@Override
			public void onResponse(int code, Object json, Throwable error) {
				checkResponse(code, json, error);
			}
		});
		finishResponse();*/
		
		
		preRequest();
		GKHttpInterface.pushMessage("18511557126", "00000000000000000000", new GKJsonResponseHandler() {
			@Override
			public void onResponse(int code, Object json, Throwable error) {
				checkResponse(code, json, error);
			}
		});
		finishResponse();
	}

}

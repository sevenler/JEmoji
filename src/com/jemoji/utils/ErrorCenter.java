
package com.jemoji.utils;

import java.util.LinkedList;
import java.util.List;

public class ErrorCenter {
	private static ErrorCenter intsance;

	public static ErrorCenter instance() {
		if (intsance == null) intsance = new ErrorCenter();
		return intsance;
	}

	private ErrorCenter() {
	}
	
	private List<ErrorDelegate> delegates = new LinkedList<ErrorDelegate>();
	
	public void regesterErrorDelegate(ErrorDelegate delegate){
		delegates.add(delegate);
	}
	public void unregesterErrorDelegate(ErrorDelegate delegate){
		delegates.remove(delegate);
	}
	
	public void onError(Exception ex){
		for (ErrorDelegate delegate : delegates) {
			delegate.onError(ex);
		}
	}
	
	public static interface ErrorDelegate{
		public void onError(Exception ex);
	}
}

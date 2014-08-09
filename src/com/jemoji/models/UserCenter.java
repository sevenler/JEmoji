package com.jemoji.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.jemoji.R;

public class UserCenter {
	private  static UserCenter intsance;
	public static UserCenter instance(){
		if(intsance == null) intsance = new UserCenter();
		return intsance;
	}
	private UserCenter(){
		users = new LinkedHashMap<String, User>();
		
		users.put("18511557127", new User("18511557127", R.drawable.header1));
		users.put("18511557126", new User("18511557126", R.drawable.header2));
	}
	
	private HashMap<String, User> users;
	
	public User get(String name){
		return users.get(name);
	}
}

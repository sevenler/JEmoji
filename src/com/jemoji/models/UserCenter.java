package com.jemoji.models;

import java.util.Collection;
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
		
		users.put("18511557127", new User("18511557127", "林超", R.drawable.header1));
		users.put("18511557126", new User("18511557126", "宋尉", R.drawable.header2));
	}
	
	private HashMap<String, User> users;
	private User me;
	
	public User get(String name){
		return users.get(name);
	}
	
	public Collection<User> getAll(){
		return users.values();
	}
	
	public User getMe(){
		return me;
	}
	
	public void setMe(User me){
		this.me = me;
	}
}

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
		
		users.put("18688903201", new User("18688903201", "林超", R.drawable.header1));
		users.put("18511557126", new User("18511557126", "宋尉", R.drawable.header2));
		users.put("18600753802", new User("18600753802", "窝窝", R.drawable.wow));
		users.put("15160686608", new User("15160686608", "lishili", R.drawable.lishili));
		users.put("18666096095", new User("18666096095", "cindyc", R.drawable.cindyc));
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

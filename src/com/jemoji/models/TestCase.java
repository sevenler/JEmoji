
package com.jemoji.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jemoji.utils.PreferManager;

class HUser {
	private Long id;
	
	private String name;
	
	private User next;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getNext() {
		return next;
	}

	public void setNext(User next) {
		this.next = next;
	}
}



public class TestCase extends AndroidTestCase{
	public void testObj() {
//		HUser user1 = new HUser();
//		  user1.setId(1001L);
//		  user1.setName("zhengling");
//		  HUser user2 = new HUser();
//		  user2.setId(1002L);
//		  user2.setName("yangyang");
//		  Map<String, HUser> userMap = new HashMap<String,HUser>();
//		  userMap.put("user1", user1);
//		  userMap.put("user2", user2);
		  
		Map<String, Emoji> map = new HashMap<String, Emoji>();
		map.put("18511557126",
				new Emoji("/storage/emulated/0/emojis_download/1408101207553.gif", "",
						"http://emoji.b0.upaiyun.com/test/1408101207310.amr")
						.setImageUrl(
								"http://emoji.b0.upaiyun.com/test/640a10dfa9ec8a13611191fff503918fa0ecc048.jpg.gif")
						.setBackground(-1));
//		map.put("002", new Emoji("imge1", "voice1", "url1").setImageUrl("imageurl1"));
		  
		  GsonBuilder builder = new GsonBuilder();
		  Gson gson = builder.create();
		  String sUserMap = gson.toJson(map, new TypeToken< Map<String, Emoji>>(){}.getType());
		  System.out.println(sUserMap);
		  
		   String KEY_SAVED_MESSAGE = "KEY_SAVED_MESSAGE";
			String saved = PreferManager.instance().getStringFromPrefs(getContext(), KEY_SAVED_MESSAGE, "");
		  Map<String,List<Emoji>> userMap2 = gson.fromJson(saved, new TypeToken<  Map<String, List<Emoji>>>() {}.getType());
		  System.out.println(userMap2);
	}

}

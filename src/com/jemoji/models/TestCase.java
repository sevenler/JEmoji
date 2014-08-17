
package com.jemoji.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jemoji.utils.PreferManager;

public class TestCase extends AndroidTestCase{
	public void testDB(){
		DataBaseWrapper db_wrapper = new DataBaseWrapper(getContext());
		
		db_wrapper.insertEmoji(new Emoji(0, "is right", null, null, null, -1, Emoji.EMOJI_TYPE_COLLECT));
		db_wrapper.insertEmoji(new Emoji(0, "is wrong", null, null, null, -1, Emoji.EMOJI_TYPE_OFFICAL));
		
		db_wrapper.insertMessage(new Message(0, "18511557126", "18511557126", new Emoji(0, "this is a meeesgae emoji", null, null, null, -1, Emoji.EMOJI_TYPE_COLLECT)));
		db_wrapper.insertMessage(new Message(0, "18511557126", "18511557127", new Emoji(0, "this is a meeesgae emoji", null, null, null, -1, Emoji.EMOJI_TYPE_COLLECT)));
		db_wrapper.insertMessage(new Message(0, "18511557126", "18511557128", new Emoji(0, "this is a meeesgae emoji", null, null, null, -1, Emoji.EMOJI_TYPE_COLLECT)));
		
		List<String> list = db_wrapper.getAllFromUser();
		for(String me : list){
			System.out.println(String.format("%s", me));
		}
		
		db_wrapper.dropTable();
	}
	
	public void htestObj() {
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


package com.jemoji.models;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;

public class EmojiSelector {
	private static EmojiSelector instance;

	public static EmojiSelector instance() {
		if (instance == null) instance = new EmojiSelector();
		return instance;
	}
	
	private static String getFullUrl(String name) {
		return String.format("%s/%s", "http://emoji.b0.upaiyun.com/test", name);
	}
	
	private static String getFullPath(String name) {
		return String.format("%s/%s", "/sdcard/Emoji_Image", name);
	}

	public List<Emoji> officialEmojis = new LinkedList<Emoji>();//官方的表情
	public List<Emoji> collectEmojis = new LinkedList<Emoji>();//收集的表情
	
	public static final int EMOJI_TYPE_OFFICAL  = 0;
	public static final int EMOJI_TYPE_COLLECT  = 1;

	public EmojiSelector() {
		String[] urls = {"640a10dfa9ec8a13611191fff503918fa0ecc048.jpg.gif",
				"8f175d6034a85edfc2ded2994b540923dc5475db.jpg.gif",
				"e27ecdbf6c81800abc476efab03533fa838b474e.jpg.gif",
				"452f4a36acaf2eddf22e8dc98f1001e9380193dd.jpg.gif",
				"f0e8c2fdfc039245cf23ab7c8594a4c27c1e25d0.jpg.gif",
				"f57a11385343fbf2e95647d7b17eca8064388ff6.jpg.gif",
				"banjo_3.gif",
				"e9924bed2e738bd4bcd449c5a08b87d6267ff977.jpg.gif",
				"93ca09fa513d269769dbf3a954fbb2fb4216d8e4.jpg.gif",
				"65689358d109b3dec4edc8d2cebf6c81810a4cdb.jpg.gif",
				"b1245bafa40f4bfb58a0b1c0014f78f0f63618d3.jpg.gif",
				"395d03087bf40ad1120c8d90552c11dfa8eccefb.jpg.gif",
				"b4b2a5c27d1ed21ba57de8faaf6eddc451da3f1b.jpg.gif",
				"66ddb3fb43166d22c6aa5b5d472309f79252d2c4.jpg.gif",
				"the-end.gif",
				"cf259345d688d43f5d4064287f1ed21b0ff43b21.jpg.gif",
				"upside_cat.gif",
				"8a34c9fcc3cec3fdebb8ed06d488d43f869427e0.jpg.gif",
				"3dd21ad5ad6eddc4fe5a72e43bdbb6fd52663312.jpg.gif",
				"15b158ee3d6d55fb86fc8ed26c224f4a20a4dd36.jpg.gif",
				"5845f7246b600c337b70f3ed184c510fd9f9a1b4.jpg.gif",
				"9e48dcc451da81cbffb868e95066d0160924315a.jpg.gif",
				"catpurr.gif",
				"738b4710b912c8fcb97a8e7bfe039245d78821fe.jpg.gif",
				"sleep.gif",
				"d439b6003af33a87e72178acc45c10385343b533.jpg.gif",
				"b32c18d8bc3eb13553959b01a41ea8d3fc1f44db.jpg.gif",
				"c0f8b58f8c5494ee9ce4c9062ff5e0fe98257e95.jpg.gif",
				"0a27203fb80e7bec287ff9152e2eb9389a506b47.jpg.gif",
				"ac1bf9dcd100baa1ddba0dc84510b912c9fc2ed0.jpg.gif",
				"3973b219ebc4b7450b990265cdfc1e178a82155d.jpg.gif",
				"a035b17eca8065386b885ce796dda144ac34828e.jpg.gif",
				"055cdbb44aed2e73ea43c8e58601a18b86d6fa73.jpg.gif",
				"e2eea686c9177f3ece88172972cf3bc79f3d5678.jpg.gif",
				"a6ecd0c8a786c917739a1cf0c83d70cf3ac75707.jpg.gif",
				"3b292df5e0fe9925f68411e636a85edf8cb171ff.jpg.gif",
				"80f89d82d158ccbf37ee99801bd8bc3eb03541db.jpg.gif",
				"walkingcat.gif",
				"2afebd3eb13533fa298d76daa9d3fd1f41345b35.jpg.gif",
				"803feac4b74543a9796c00381f178a82b80114f1.jpg.gif",
				"8377f9198618367a215d97f92c738bd4b21ce5e1.jpg.gif",
				"9e6dd01373f08202b4ba69cb4afbfbedaa641bc4.jpg.gif",
				"5fa7810a19d8bc3eee5a2a70808ba61ea9d345db.jpg.gif",
				"7ce4d4628535e5dd60ea5d2b77c6a7efcf1b6222.jpg.gif",
				"6f4635a85edf8db19b7c50de0b23dd54574e74db.jpg.gif",
				"14599f2f07082838758d92ddb999a9014d08f119.jpg.gif",
				"43e93ac79f3df8dc6e1c7565cc11728b461028c3.jpg.gif",
				"c8cbaa64034f78f0b0a1d43a78310a55b2191c72.jpg.gif",
				"70306c224f4a20a47eefc73391529822730ed073.jpg.gif",
				"3c0243a7d933c895ea2dd7c5d31373f0830200e6.jpg.gif",
				"e27ecdbf6c81800a44b7a5b4b33533fa838b47db.jpg.gif",
				"9a513812b31bb051a38efde1377adab44bede073.jpg.gif",
				"be97ca13495409238e3b4fc49058d109b2de49db.jpg.gif",
				"aa03bd315c6034a810b0943bc9134954082376db.jpg.gif",
				"9538a9d3fd1f4134f05d285b271f95cad1c85e56.jpg.gif",
				"8d2f6a63f6246b604b194a80eaf81a4c500fa2d1.jpg.gif",
				"paper.gif",
				"f83548540923dd5402218bd2d309b3de9d8248db.jpg.gif",
				"b462eaf81a4c510fe4da0af76059252dd52aa5e3.jpg.gif",
				"6aa4b3b7d0a20cf441e7062b77094b36adaf99f1.jpg.gif",
				"09a1e850352ac65c4b478287faf2b21192138aae.jpg.gif",
				"8bbfa8014c086e06010dc5bf03087bf40bd1cb47.jpg.gif",
				"3bdc503d269759ee1d068befb0fb43166d22df62.jpg.gif",
				"a6ecd0c8a786c91710ea701ccb3d70cf3ac757cb.jpg.gif",
				"calmingcatsmall.gif",
				"983e962bd40735fa29b4a59d9c510fb30f240870.jpg.gif",
				"078e5fdf8db1cb13183b10a9df54564e93584bdb.jpg.gif",
				"ce394034970a304e86704bded3c8a786c9175c6e.jpg.gif",
				"vsoto_hg_02.png",
				"129.jpg",
				"10774b5549f5588b6e34f943efc476cd.png",
				"despicable-me-2-Minion-icon-5.png",
				"08e19e3df8dcd100405b5e13718b4710b8122fe7.jpg",
				"c410aec379310a55b7262a2eb54543a98326108b.jpg.png",
				"201404301398846027.jpg",
				"8191d1a20cf431ad0d5001a34936acaf2fdd9854.jpg",
				"9842024f78f0f736ae98b8db0855b319eac413af.jpg.png",
				"c425908fa0ec08fa6a57e67d5bee3d6d54fbdabf.jpg.png",
				"IMG_0279.JPG",
				"IMG_0258.JPG",
				"eee250da81cb39db663d8776d2160924ab183033.jpg",
				"cedcae51f3deb48f6a8ee344f21f3a292cf5786f.jpg",
				"119.jpg",
				"140.png",
				"u=1534308294,1174985864&fm=23&gp=0.jpg",
				"00dc838ba61ea8d3d3d01070970a304e241f58fd.jpg",
				"a163d788d43f87948ef90eabd21b0ef41ad53ad2.jpg",
				"0a34b21bb051f8199023f690d8b44aed2f73e788.jpg.png",
				"IMG_0274.JPG",
				"94a0c8177f3e6709de06037a3bc79f3df9dc55d2.jpg",
				"1c2ebb2d67e336e0b8aaca0f8d874deb.png",
				"126.jpg",
				"120.jpg",
				"IMG_0266.JPG",
				"d5eb7a899e510fb3ff1d834ddb33c895d0430c8e.jpg.png",
				"16435BT5-20.png",
				"119.png",
				"20130820220642_MBfXH.thumb.600_0.png.jpeg",
				"d8de1b4c510fd9f98f74a1b3272dd42a2934a4ab.jpg.png",
				"296a500fd9f9d72afe8d5bf6d42a2834359bbb12.jpg.png",
				"104.jpg",
				"IMG_0298.PNG",
				"106.jpg",
				"c8cbaa64034f78f0c7143e3b7b310a55b2191c84.jpg",
				"1b12359b033b5bb5a9bfd42434d3d539b700bca2.jpg.png",
				"20140404154537-1159652299.jpg",
				"201404301398846031.jpg",
				"8bbfa8014c086e06e1ace59d03087bf40bd1cb83.jpg",
				"kitty.jpg",
				"48d20bd162d9f2d3fe28593fabec8a136227cccf.jpg.png",
				"43e93ac79f3df8dcdfbbc5e2cf11728b461028e0.jpg.gif",
				"133.jpg",
				"16-032807_842.jpg",
				"7c6c21a4462309f7d78a58c8700e0cf3d6cad62d.jpg.png",
				"c3393b292df5e0fe75a1cb9b5e6034a85fdf7291.jpg",
				"28f3ac6eddc451daa282b998b4fd5266d0163276.jpg.gif",
				"IMG_0292.PNG",
				"131.jpg",
				"b1245bafa40f4bfbc546d427014f78f0f63618d0.jpg.gif",
				"340c92420f088ecc9089140e1678add6.png",
				"1ed3e1fe9925bc31bab44beb5cdf8db1ca1370fa.jpg.gif",
				"51f6f603918fa0ec329091d7249759ee3c6ddbbf.jpg.png",
				"be3600e93901213f3847b3d756e736d12f2e956f.jpg",
				"117.jpg",
				"14599f2f070828384c1278dcba99a9014d08f185.jpg",
				"IMG_0262.JPG",
				"127.jpg",
				"IMG_0275.JPG",
				"20140406005732_zM3C3.thumb.600_0.jpeg",
				"IMG_0259.JPG",
				"shot_1291720201.png",
				"32cf3801213fb80eafcb213834d12f2eb83894e9.jpg",
				"001.png",
				"20130820223924_f8iru.thumb.600_0.png.jpeg",
				"9e125882b2b7d0a276bef047c9ef76094a369a9f.jpg.png",
				"48d20bd162d9f2d3c3714c15abec8a136227cc8a.jpg.png",
				"a40dd50735fae6cda4d850530cb30f2442a70fbe.jpg",
				"92ad86d6277f9e2f2632e9d21d30e924b999f38f.jpg.png",
				"92951388114577.jpg",
				"IMG_0272.JPG",
				"igel.png",
				"e22fb2de9c82d1587e86ad6b820a19d8bd3e428e.jpg.png",
				"9538a9d3fd1f41342b5cf25e261f95cad1c85e49.jpg",
				"20130502110449_KNkzs.png",
				"IMG_0268.JPG",
				"IMG_0271.JPG",
				"e23572f082025aaff6ca23f4f8edab64034f1a3e.jpg",
				"114.jpg",
				"0e4b54fbb2fb43162f488fa022a4462308f7d3bf.jpg.png",
				"B06E57BC419C342DBA685461DD67F03D.jpeg",
				"8b287aec54e736d1c084e4f299504fc2d462695f.jpg.png",
				"201472811258849.jpg",
				"62fc80cb39dbb6fd1ba18ddc0b24ab18962b3773.jpg.png",
				"a40dd50735fae6cd96b05fbb0db30f2443a70f8e.jpg.png",
				"IMG_0282.JPG",
				"312e7af40ad162d9c26d97c613dfa9ec8b13cd0b.jpg.png",
				"105.jpg",
				"IMG_0285.JPG",
				"306979f0f736afc335f87dffb119ebc4b645125d.jpg",
				"201404301398846250.jpg",
				"113.png",
				"20300542485961139746715595211.jpg",
				"IMG_0264.JPG",
				"IMG_0293.PNG",
				"201404301398846043.jpg",
				"cb3f8718367adab422c55dc68bd4b31c8601e4c0.jpg",
				"6ac83c6d55fbb2fb02a8adc84d4a20a44723dc0b.jpg.png",
				"b9a4b8014a90f603642711673a12b31bb051ed25.jpg",
				"004.png",
				"20130914234625_YSrnk.thumb.600_0.jpeg",
				"108.jpg",
				"128.jpg",
				"IMG_0283.JPG",
				"c139bf096b63f624e99b9ccb8544ebf8184ca3de.jpg",
				"00dc838ba61ea8d3d3c21070970a304e241f5883.jpg",
				"7c6c21a4462309f728b3c966720e0cf3d6cad688.jpg",
				"48d20bd162d9f2d36716d035abec8a136227cc8f.jpg.png",
				"130.jpg",
				"06dce7cd7b899e5130887c9142a7d933c9950dd2.jpg",
				"IMG_0278.JPG",
				"no.jpg",
				"20130820223600_GhCnM.thumb.600_0.png.jpeg",
				"6065faf2b211931351b09bc465380cd790238dfd.jpg",
				"192.png",
				"b613e4dde71190ef8ea6fa25cc1b9d16fcfa6095.jpg.png",
				"images.jpeg",
				"IMG_0273.JPG",
				"c3393b292df5e0fe43e634335f6034a85edf7228.jpg",
				"b8f2b21c8701a18ba17a9f6f9d2f07082838fe27.jpg",
				"48d20bd162d9f2d36692d135abec8a136227cc0b.jpg.png",
				"121.jpg",
				"IMG_0269.JPG",
				"matouren-jms2.png",
				"IMG_0256.PNG",
				"IMG_0291.JPG",
				"136.png",
				"846342a98226cffc15af4a68bb014a90f703ea0d.jpg.png",
				"IMG_0281.JPG",
				"028bcaef76094b360055bddfa0cc7cd98c109de7.jpg",
				"b1ada71ea8d3fd1fff4d57e0324e251f94ca5f4e.jpg.png",
				"IMG_0267.JPG",
				"20130820224352_eauUz.thumb.600_0.png.jpeg",
				"b9a4b8014a90f6032ca5dac23912b31bb151edc0.jpg",
				"IMG_0295.PNG",
				"IMG_0270.JPG",
				"191.png",
				"9e3df8dcd100baa1fe6be2404610b912c9fc2e81.jpg.png",
				"109.png",
				"002.png",
				"IMG_0263.JPG",
				"406.jpg",
				"1ed3e1fe9925bc3117d02e0c5cdf8db1ca1370be.jpg",
				"708f8326cffc1e17b18628f94890f603728de9e7.jpg",
				"1ed3e1fe9925bc31543a69625cdf8db1ca13708e.jpg.png",
				"123.jpg",
				"4c186609c93d70cf22fcec88f8dcd100bba12bd2.jpg",
				"28f3ac6eddc451da6394f831b4fd5266d11632b4.jpg.png",
				"IMG_0257.PNG",
				"20130820221909_XE3eQ.thumb.600_0.png.jpeg",
				"da02b999a9014c08465edc7f087b02087af4f4ad.jpg.png",
				"IMG_0284.JPG",
				"IMG_0288.JPG",
				"11289403100a943977o.jpg",
				"1892081_962232.jpg",
				"6f4635a85edf8db133293ae10923dd54574e74d2.jpg",
				"116.jpg",
				"7181d833c895d1435b9211f971f082025baf072e.jpg.png",
				"107.png",
				"a4fba044ad345982cb6410480ef431adcaef8420.jpg.png",
				"118.jpg",
				"112.jpg",
				"IMG_0286.JPG",
				"542fc83d70cf3bc754f13836d300baa1cc112a19.jpg.png",
				"124.png",
				"b100cefc1e178a82b4b98a7af403738da877e80d.jpg.png",
				"111.png",
				"IMG_0297.PNG",
				"51fff3d3572c11df6fc2a442622762d0f703c262.jpg",
				"62fc80cb39dbb6fdb45e10fc0b24ab18962b378d.jpg.png",
				"103.jpg",
				"9c77f2deb48f8c540a9930f538292df5e1fe7f4c.jpg.png",
				"IMG_0289.JPG",
				"e23572f082025aafad03f932f9edab64034f1a3f.jpg",
				"IMG_0277.JPG",
				"2d318b82b9014a90a8380038a9773912b21beed3.jpg",
				"e9924bed2e738bd4f42f1303a28b87d6277ff998.jpg",
				"163994cad1c8a786e14d22f46509c93d71cf508e.jpg.png"};
		
		for (String url : urls){
			officialEmojis.add(new Emoji(getFullPath(url), getFullUrl(url), Color.parseColor("#ffffff")));
		}
		
		String[] collect = {"e23572f082025aafad03f932f9edab64034f1a3f.jpg",
		"IMG_0277.JPG",
		"2d318b82b9014a90a8380038a9773912b21beed3.jpg",
		"e9924bed2e738bd4f42f1303a28b87d6277ff998.jpg",
		"163994cad1c8a786e14d22f46509c93d71cf508e.jpg.png"};
		
		for (String url : collect){
			collectEmojis.add(new Emoji(getFullPath(url), getFullUrl(url), Color.parseColor("#ffffff")));
		}
	}

	public Emoji getOfficial(int index){
		return officialEmojis.get(index);
	}

	public int officialSize() {
		return officialEmojis.size();
	}
	
	public Emoji getCollect(int index){
		return collectEmojis.get(index);
	}

	public int collectSize() {
		return collectEmojis.size();
	}
	
	public boolean addCollect(Emoji emoji){
		return collectEmojis.add(emoji);
	}
	
	public List<Emoji> getEmojiData(int type) {
		List<Emoji> list = new LinkedList<Emoji>();

		switch (type) {
			case EMOJI_TYPE_COLLECT:
				for (int i = 0; i < collectSize(); i++) {
					list.add(getCollect(i));
				}
				break;

			default:
				for (int i = 0; i < officialSize(); i++) {
					list.add(getOfficial(i));
				}
				break;
		}
		return list;
	}
}

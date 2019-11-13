package com.e_commerce.miscroservice.sdx_proj.utils;

import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * 通过ISBN获取书本信息
 *
 * @Author: FangyiXu
 * @Date: 2019-11-12 10:29
 */
public class IsbnHelper {
	private final static String url = "https://book.douban.com/isbn/";

	public static DoubanBookInfo infos(String IsbnCode) throws IOException {
		String connectUrl = url + IsbnCode;
		Document document = Jsoup.connect(connectUrl).get();
		return dealWithInfo(document);
	}

	public static void main(String[] args) {
		try {
			infos("9787513927277");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static DoubanBookInfo dealWithInfo(Document doc) {
		if(doc == null) return new DoubanBookInfo();
		//包含关键信息的div -> id = info
		Elements info = doc.select("div#info");
		//包含书名的div -> id = wrapper
		Elements wrapper = doc.select("div#wrapper");
		final List<String> namesAll = Arrays.asList("作者", "出版社", "译者", "出版年", "页数", "定价", "装帧", "丛书", "ISBN");
		Map<String, String> valNameMap = new HashMap<>();
		Map<String, List<String>> keyValMap = new HashMap<>();
		//初始化
		for (String name : namesAll) {
			keyValMap.put(name, null);
		}

		for (Element theEle : info) {
			List<TextNode> textNodes = theEle.textNodes();
			for (TextNode textNode : textNodes) {
				String text = textNode.text();
				if (StringUtil.isEmpty(text.trim()) || "<br>".equals(text)) {
					continue;
				}
				Node node = textNode.previousSibling();
//				System.out.println(node);
				if (node != null) {
					String name = getInnerHtml(node.toString());
					name = getRidOfColon(name);
//					System.out.println("name: " + name);
					List<String> strings = keyValMap.get(name);
					if (strings == null) strings = new ArrayList<>();
					strings.add(getRidOfSpace(text));
					keyValMap.put(name, strings);
				}
			}
			Elements a = info.select("a");

			for (Element theA : a) {
				String aSVal = theA.text();
				Element element = theA.previousElementSibling();
				String previousElementVal = element.text();
				String theVal = valNameMap.get(previousElementVal);
				String name = theVal != null ? theVal : previousElementVal;
				//eg. （司马迁,作者）
				valNameMap.put(aSVal, previousElementVal);
				name = getRidOfColon(name);    //处理多余的标点符号
				List<String> vals = getNotNullValue(keyValMap, name);
				vals.add(aSVal);
				keyValMap.put(name, vals);
			}

			//书名、图片、评分
			String name = wrapper.select("h1").select("span").get(0).text();    //书名

			String src = doc.select("div#mainpic").select("a").select("img").attr("src");    //图片

			String rate = doc.select("div[class=rating_self clearfix]").select("strong[class=ll rating_num ]").text();    //评分

			//简介

			Elements intro = doc.select("div[class=indent]").select("span[class=all hidden]").select("div[class=intro]").select("p");
			StringBuilder paragraphAppender = new StringBuilder();
			for(Element single:intro) {
				paragraphAppender.append(single.text());
				paragraphAppender.append("\n");
			}
			String introduction = paragraphAppender.toString();	//简介
			System.out.println(introduction);
			if(introduction.endsWith("\n")) {
				introduction = introduction.substring(0, introduction.length() - "\n".length());
			}
			//TODO 目录、分类


			//装载遗漏
			keyValMap.put(DoubanBookInfoEnum.NAME, Arrays.asList(name));
			keyValMap.put(DoubanBookInfoEnum.COVER_PIC, Arrays.asList(src));
			keyValMap.put(DoubanBookInfoEnum.RATING, Arrays.asList(rate));
			keyValMap.put(DoubanBookInfoEnum.INTRODUCTION, Arrays.asList(introduction));

			//打印最终的map
			System.out.println(keyValMap);

		}
		DoubanBookInfo doubanBookInfo = mapToDoubanBookInfo(keyValMap);
		System.out.println(doubanBookInfo);
		return doubanBookInfo;
	}

	private static String getRidOfSpace(String text) {
		return text.contains(" ") ? text.replace(" ", "") : text;
	}

	private static String getRidOfColon(String name) {
		return name.contains(":") ? name.replace(":", "") : name;
	}

	private static String getInnerHtml(String srcStr) {
		int start = srcStr.indexOf(">");
		int end = srcStr.lastIndexOf("<");
		return srcStr.substring(start + 1, end);
	}

	private static DoubanBookInfo mapToDoubanBookInfo(Map<String, List<String>> keyValMap) {
		Map<String, String> map = listMapToStringMap(keyValMap);
		String price = map.get(DoubanBookInfoEnum.PRICE);
		price = price.endsWith("元")? price.substring(0, price.length() - 1) : price;
		return DoubanBookInfo.builder()
			.author(map.get(DoubanBookInfoEnum.AUTHOR))
			.publisher(map.get(DoubanBookInfoEnum.PUBLISHER))
			.translator(map.get(DoubanBookInfoEnum.TRANSLATOR))
			.publishTime(map.get(DoubanBookInfoEnum.PUBLISH_TIME))
			.totalPageNum(map.get(DoubanBookInfoEnum.TOTAL_PAGES))
			.price(Double.valueOf(price))
			.bindingLayout(map.get(DoubanBookInfoEnum.BINDING_LAYOUT))
			.series(map.get(DoubanBookInfoEnum.SERIES))
			.isbnCode(map.get(DoubanBookInfoEnum.ISBN))
			.name(map.get(DoubanBookInfoEnum.NAME))
			.coverPic(map.get(DoubanBookInfoEnum.COVER_PIC))
			.rating(map.get(DoubanBookInfoEnum.RATING))
			.build();
	}

	private static Map<String, String> listMapToStringMap(Map<String, List<String>> keyValMap) {
		Map<String, String> resultMap = new HashMap<>();
		keyValMap.forEach((k, v) -> {
			if(v == null) v = new ArrayList<>();
			StringBuilder builder = new StringBuilder();
			for (String tVal : v) {
				builder.append(tVal).append(",");
			}
			String val = builder.toString();
			val = val.endsWith(",") ? val.substring(0, val.length() - 1) : val;
			resultMap.put(k, val);
		});
		return resultMap;
	}

	private static List<String> getNotNullValue(Map<String, List<String>> keyValMap, String name) {
		List<String> vals = keyValMap.get(name);
		if (vals == null) {
			vals = new ArrayList<>();
		}
		return vals;
	}
}

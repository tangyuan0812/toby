package com.nmys.story.webmagic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * @Description: 调用接口工具类
 * @author: huangbaiping
 * @date: 2017/10/11
 */
public class DataUtils {
	
	public static String getSign(JSONObject jsonObject) {
		
		try {
			
			Map<String, Object> treeMap = new TreeMap<String, Object>();
			for(Entry<String, Object> entry : jsonObject.entrySet()){
				
				treeMap.put(entry.getKey(), entry.getValue());
			}
			
			StringBuffer paramValueSb = new StringBuffer();
	        for (Object o : treeMap.values()) {
	            paramValueSb.append(o);
	        }
	        
	        return DigestUtils.md5Hex(paramValueSb.toString());
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	// 首字母变大写
	public static String upperCase(String str) {  
	    char[] ch = str.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	}  
	
	// 把str的[{},{}]转换成array
	@SuppressWarnings("rawtypes")
	public static List<Map> strToArray(String json){
		try {
			if(json!= null && json!= ""){
				List<Map> list = JSON.parseArray(json, Map.class);
				return list;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map>();
	}
	
	// 把str的[[],[]]转换成array
	@SuppressWarnings("rawtypes")
	public static List<List> strToArrayInArray(String json){
		try {
			if(json!=null && json!=""){
				List<List> list = JSON.parseArray(json, List.class);
				return list;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<List>();
	}
	
	// 把str的[1,2]转换成array
	@SuppressWarnings("rawtypes")
	public static List<String> strToArrayInString(String json){
		try {
			if(json!=null && json!=""){
				List<String> list = JSON.parseArray(json, String.class);
				return list;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	
	// 把str的{}转换成map
	public static Map<String, Object> strToMap(String json){
		try {
			if(json!=null && json!=""){
				Map<String, Object> map = JSON.parseObject(json);
			
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, Object>();
	}
	
	// 去除空格
	public static String replaceBlank(String json){
		if(json != null){
			json = json.replace("\\s*|\t|\r|\n", "");
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
			Matcher m = p.matcher(json);  
			json = m.replaceAll("");  
			return json;
		}
		return "";
	}
	
	// 将字符串转成hash值  
    public static int toHash(String key) {  
        int arraySize = 11113; // 数组大小一般取质数  
        int hashCode = 0;  
        for (int i = 0; i < key.length(); i++) { // 从字符串的左边开始计算  
            int letterValue = key.charAt(i) - 96;// 将获取到的字符串转换成数字，比如a的码值是97，则97-96=1  
                                                    // 就代表a的值，同理b=2；  
            hashCode = ((hashCode << 5) + letterValue) % arraySize;// 防止编码溢出，对每步结果都进行取模运算  
        }  
        return hashCode;  
    } 
    
    public static long getKeyByKline(String interval, String exchangeName, String currencyPairName, String currencyName, long timestamp){
    	return Long.valueOf(DataUtils.toHash(interval +  "/" + exchangeName + "/" + currencyPairName + "/" + currencyName) + "" + (timestamp/1000L));
    }
    
	
	// obj转long
	public static Long objToLong(Object obj){
		if(obj == null){
			return null;
		}
		return Long.valueOf(obj.toString()).longValue();
	}
	
	// obj转字符串
	public static String objToStr(Object obj){
		if(obj == null){
			return "";
		}
		return obj.toString();
	}
	
	// obj转BigDecimal
	public static BigDecimal objToBigDecimal(Object obj){
		if(obj == null){
			return null;
		}
		return new BigDecimal(obj.toString());
	}
	
	// 判断集合不为空
	public static Boolean listIsNotEmpty(List list){
		
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}
	
	// 返回不为null的list
	public static <T> List<T> listNotNull(List<T> list){
		
		if(list == null){
			return new ArrayList<>();
		}
		
		return list;
	}
	
	// 判断page里面的list是否为空
	public static <T> Boolean pageIsNotEmpty(Page<T> page){
		
		if(page != null && page.getContent() != null && page.getContent().size() > 0){
			return true;
		}
		
		return false;
	}
	
	// 获取page里的list的第一个实体
	public static <T> T getFirstEntityFromPage(Page<T> page){
		if(DataUtils.pageIsNotEmpty(page)){
			
			return page.getContent().get(0);
		}
    	return null;
	}
	
	
	public static Long chagetmie(String string) {
        try {
			
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date dt=sdf.parse(string);
			return dt.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	public static long getId()
	{
		long tmpID = 0;

		boolean tmpIDlocked = false;
		long ltime = 0;
		while (true)
		{
			if(tmpIDlocked == false)
			{
				tmpIDlocked = true;
				//当前：（年、月、日、时、分、秒、毫秒）*10000
				ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS").format(new Date()).toString()) * 10000;
				if(tmpID < ltime)
				{
					tmpID = ltime;
				}
				else
				{
					tmpID = tmpID + 1;
					ltime = tmpID;
				}
				tmpIDlocked = false;
				return ltime;
			}
		}
	}
	
	public static void klineToNull(List klines){
		
		if(DataUtils.listIsNotEmpty(klines)){
			for(Object kline : klines){
				kline = null;
			}
		}
	}
	
	//当前时间24H之前数据
	public static Long getOneDayBefore(Long timestamp){
		Date dateEnd=new Date(timestamp);
	    Calendar date = Calendar.getInstance();
	    date.setTime(dateEnd);
	    date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
	    return date.getTime().getTime();
	}
	
	
	/**
	 * 首先进行入参检查防止出现空指针异常
	 * 如果两个参数都为空，则返回true
	 * 如果有一项为空，则返回false
	 * 接着对第一个list进行遍历，如果某一项第二个list里面没有，则返回false
	 * 还要再将两个list反过来比较，因为可能一个list是两一个list的子集
	 * 如果成功遍历结束，返回true
	 * @param l0
	 * @param l1
	 * @return
	 */
	public static boolean isListEqual(List l0, List l1){
		if (l0 == l1)
			return true;
		if (l0 == null && l1 == null)
			return true;
		if (l0 == null || l1 == null)
			return false;
		if (l0.size() != l1.size())
			return false;
		for (Object o : l0) {
			if (!l1.contains(o))
				return false;
		}
		for (Object o : l1) {
			if (!l0.contains(o))
				return false;
		}
		return true;
	}

	public static String getDate(Long long1) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(long1));
		
	}

}

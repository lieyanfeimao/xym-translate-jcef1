package com.xuanyimao.translate.core.translateplatform;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.xuanyimao.translate.anno.TranslatePlatformClass;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.entity.translate.TranslateResult;
import com.xuanyimao.translate.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 翻译平台 - 百度
 * 高级版每月100万字符免费额度，额度可能随时会调整，请去官网获取最新的额度
 * @author liuming
 *
 */
@TranslatePlatformClass(name="baidu",desc="百度翻译",order=1)
public class BaiduTranslatePlatform implements TranslatePlatform {


    public static MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
    /**URL地址**/
    public static String URL="https://fanyi-api.baidu.com/api/trans/vip/translate";

    //百度翻译申请地址。https://fanyi-api.baidu.com/product/112

    @Override
    public boolean validate() {
        if(StringUtils.isBlank(ApplicationData.appConfig.getBdAppId())
                || StringUtils.isBlank(ApplicationData.appConfig.getBdSecretKey())){
            return false;
        }
        TranslateResult translateResult = getTranslateData("test");
        return translateResult.isOk();
    }

    public TranslateResult getTranslateData(String translateData, String from, String to) {
        try {
            OkHttpClient okhttp=new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS)
                    .writeTimeout(5L, TimeUnit.SECONDS).readTimeout(5L,TimeUnit.SECONDS)
                    .connectionPool(ApplicationData.connectionPool).build();

            FormBody.Builder formBuilder = new FormBody.Builder();

            formBuilder.add("q",translateData);
            formBuilder.add("from",from);
            formBuilder.add("to",to);
            formBuilder.add("appid",ApplicationData.appConfig.getBdAppId());
            String salt=getSalt();
            formBuilder.add("salt",salt);
            formBuilder.add("sign", getSign(translateData, salt));

            Request request=new Request.Builder()
                    .url(URL)
                    .post(formBuilder.build())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
//           System.out.println("翻译的数据："+translateData);

            Response response = okhttp.newCall(request).execute();
            if(response!=null && response.isSuccessful()){
                String data=response.body().string();
                response.close();
                return dealTranslateData(data);
            }else {
                int code=1;
                if(response!=null) {
                    code=response.code();
                    response.close();
                }
                return TranslateResult.error("翻译失败："+code);
            }

        }catch(Exception e) {
            e.printStackTrace();
            return TranslateResult.error(e.getMessage());
        }
    }

    public TranslateResult getTranslateData(String translateData) {
        return getTranslateData(translateData,"en","zh");
    }
    
    public static TranslateResult dealTranslateData(String data) {
        if(StringUtils.isBlank(data)) {
            return TranslateResult.error("翻译结果为空");
        }
        try {
        	JsonObject jobj=JsonParser.parseString(data).getAsJsonObject();
            JsonArray jarry=jobj.get("trans_result").getAsJsonArray();
            jobj=jarry.get(0).getAsJsonObject();
            return TranslateResult.success(jobj.get("dst").getAsString());
        }catch(Exception e) {
            e.printStackTrace();
            return TranslateResult.error(e.getMessage());
        }
    }
    
    /**
     * 获取签名
     * @author:liuming
     * @since 2022年5月10日
     * @param queryStr
     * @param salt
     * @return
     */
    private static String getSign(String queryStr,String salt) {
    	return MD5Util.MD5Encode(ApplicationData.appConfig.getBdAppId()+queryStr+salt+ApplicationData.appConfig.getBdSecretKey(), "UTF-8").toLowerCase();
    }
    
    /**
     * 获取随机数
     * @author:liuming
     * @since 2022年5月10日
     * @return
     */
    private static String getSalt() {
    	return System.currentTimeMillis()+"";
    }

    @Override
    public Map<String, String> getTranslateLanguage() {
        Map<String,String> map=new LinkedHashMap<String,String>();
        map.put("zh","中文");
        map.put("en","英文");
        return map;
    }

    public static void main(String[] args) {
//		System.out.println(MD5Util.MD5Encode("2015063000000001apple143566028812345678", "UTF-8").toLowerCase());
    	TranslateResult tr=new BaiduTranslatePlatform().getTranslateData("This table maps artifacts into the jar file name. \"version-yyyymmdd\" is the POI version stamp. You can see what the latest stamp is on the downloads page.");
    	System.out.println(tr.isOk());
    	System.out.println(tr.getData());
	}
}

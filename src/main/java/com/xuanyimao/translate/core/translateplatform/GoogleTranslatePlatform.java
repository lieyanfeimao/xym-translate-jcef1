//package com.xuanyimao.translate.core.translateplatform;
//
//import java.util.concurrent.TimeUnit;
//import org.apache.commons.lang3.StringUtils;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.xuanyimao.xstjcef.anno.TranslatePlatformClass;
//import com.xuanyimao.xstjcef.common.ApplicationData;
//import com.xuanyimao.xstjcef.entity.translate.TranslateResult;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///**
// * 翻译平台 - 谷歌
// * 不推荐 
// * @author liuming
// *
// */
//@TranslatePlatformClass(name="谷歌翻译",order=2)
//public class GoogleTranslatePlatform implements TranslatePlatform{
//	
//	public static MediaType mediaType = MediaType.parse("application/json");
//    
//	/**请勿上传到git仓库等互联网公共区域，泄漏后请立即到谷歌开发者平台更换**/
//    public static String URL="https://translation.googleapis.com/language/translate/v2?key=key值";
//    
//    
//    public TranslateResult getTranslateData(String translateData) {
//        try {
//            OkHttpClient okhttp=new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS)
//                    .writeTimeout(5L, TimeUnit.SECONDS).readTimeout(5L,TimeUnit.SECONDS)
//                    .connectionPool(ApplicationData.getOkhttpConnectionPool()).build();
//            
//            translateData=translateData.replace("\"", "\\\"");
//            
//            RequestBody body = RequestBody.create(mediaType, "{" + 
//                    "  \"q\": \""+translateData+"\"," + 
//                    "  \"target\": \"zh\"" + 
//                    "}");
//            Request  request=new Request.Builder()
//                    .url(URL)
//                    .post(body)
//                    .addHeader("Content-Type", "application/json; charset=utf-8")
//                    .build();
////           System.out.println("翻译的数据："+translateData);
//           
//            Response response = okhttp.newCall(request).execute();
//            if(response!=null && response.isSuccessful()){
//            	String data=response.body().string();
//            	response.close();
//            	return dealTranslateData(data);
//            }else {
//            	int code=1;
//            	if(response!=null) {
//            		code=response.code();
//            		response.close();
//            	}
//            	return TranslateResult.error("翻译失败："+code);
//            }
//            
//        }catch(Exception e) {
//            e.printStackTrace();
//            return TranslateResult.error(e.getMessage());
//        }
//    }
//    
//    public static TranslateResult dealTranslateData(String data) {
//    	if(StringUtils.isBlank(data)) {
//            return TranslateResult.error("翻译结果为空");
//        }
//        try {
//            JsonObject jobj=JsonParser.parseString(data).getAsJsonObject();
//            jobj=jobj.get("data").getAsJsonObject();
//            JsonArray jarry=jobj.get("translations").getAsJsonArray();
//            jobj=jarry.get(0).getAsJsonObject();
//            return TranslateResult.success(jobj.get("translatedText").getAsString());
//        }catch(Exception e) {
//            e.printStackTrace();
//            return TranslateResult.error(e.getMessage());
//        }
//    }
//}

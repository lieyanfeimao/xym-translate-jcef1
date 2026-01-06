package com.xuanyimao.translate.core.translateplatform;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
import com.xuanyimao.translate.anno.TranslatePlatformClass;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.entity.translate.TranslateResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 腾讯云翻译平台
 * 每月500万字符免费额度，额度可能随时会调整，请去官网获取最新的额度
 * @author liuming
 *
 */
@TranslatePlatformClass(name="tencent",desc="腾讯翻译",order=0)
public class TencentTranslatePlatform implements TranslatePlatform{
    /** 区域 */
    private final static String REGION="ap-guangzhou";
	//腾讯云api调试地址
	//https://console.cloud.tencent.com/api/explorer?Product=tmt&Version=2018-03-21&Action=TextTranslate

    @Override
    public boolean validate() {
        if(StringUtils.isBlank(ApplicationData.appConfig.getTxSecretId())
                || StringUtils.isBlank(ApplicationData.appConfig.getTxSecretKey())){
            return false;
        }
        TranslateResult translateResult = getTranslateData("test");
        return translateResult.isOk();
    }

    public TranslateResult getTranslateData(String translateData, String from, String to){
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(ApplicationData.appConfig.getTxSecretId(), ApplicationData.appConfig.getTxSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            // ap-guangzhou 表示使用的是广州区域
            TmtClient client = new TmtClient(cred,REGION, clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            TextTranslateRequest req = new TextTranslateRequest();
            req.setSourceText(translateData);
            req.setSource(from);
            req.setTarget(to);
            req.setProjectId(0L);

            // 返回的resp是一个TextTranslateResponse的实例，与请求对象对应
            TextTranslateResponse resp = client.TextTranslate(req);
            // 输出json格式的字符串回包
//            System.out.println(TextTranslateResponse.toJsonString(resp));

            try {
                //休眠
                Thread.sleep(220L);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return TranslateResult.success(resp.getTargetText());
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return TranslateResult.error(e.getMessage());
        }
    }

	@Override
	public TranslateResult getTranslateData(String translateData) {
        return getTranslateData(translateData,"en","zh");
	}

    @Override
    public Map<String, String> getTranslateLanguage() {
        Map<String,String> map=new LinkedHashMap<String,String>();
        map.put("zh","中文");
        map.put("en","英文");
        return map;
    }
	public static void main(String[] args) {
		TranslateResult result=new TencentTranslatePlatform().getTranslateData("Retrieves the width of the track.");
		System.out.println(result.getData());
	}
}

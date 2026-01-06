package com.xuanyimao.translate.api.controller.common;

import com.google.gson.Gson;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.anno.JsFunction;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.core.XstManager;
import com.xuanyimao.translate.entity.AppConfig;
import com.xuanyimao.translate.entity.Message;
import com.xuanyimao.translate.util.LogUtil;
import com.xuanyimao.translate.util.TranslateUtil;
import com.xuanyimao.translate.util.VersionUtil;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefQueryCallback;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.handler.CefDialogHandler.FileDialogMode;

import java.io.IOException;
import java.util.Vector;


/**
 * 系统配置
 * @author liuming
 *
 */
@JsClass(prefix = "config")
public class ConfigController {

	/**
	 * 保存配置
	 * @param appConfig
	 * @return
	 */
	@JsFunction
	public Message saveConfig(AppConfig appConfig) {
		try {
			System.out.println(new Gson().toJson(appConfig));

			ApplicationData.saveAppConfig(appConfig);
			return Message.success();
		} catch (Exception e) {
			LogUtil.getLogger().error("保存配置失败",e);
			return Message.error(e.getMessage());
		}
	}

	/**
	 * 保存配置
	 * @return
	 */
	@JsFunction
	public Message queryConfig() {
		try {
			return Message.success("操作成功",ApplicationData.appConfig);
		} catch (Exception e) {
			LogUtil.getLogger().error("保存配置失败",e);
			return Message.error(e.getMessage());
		}
	}
}

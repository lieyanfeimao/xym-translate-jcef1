package com.xuanyimao.translate.api.controller.common;

import java.io.IOException;
import java.util.Vector;

import com.xuanyimao.translate.entity.Message;
import com.xuanyimao.translate.util.LogUtil;
import com.xuanyimao.translate.util.TranslateUtil;
import com.xuanyimao.translate.util.VersionUtil;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefQueryCallback;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.handler.CefDialogHandler.FileDialogMode;

import com.google.gson.Gson;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.anno.JsFunction;
import com.xuanyimao.translate.core.XstManager;


/**
 * 公用java方法
 * @author liuming
 *
 */
@JsClass(prefix = "common")
public class CommonController {

	/**
	 * 打开系统文件选择对话框
	 * @author:liuming
	 * @param mode 对话框的模式：FILE_DIALOG_OPEN 单个文件 ;FILE_DIALOG_OPEN_MULTIPLE  多个文件 ;FILE_DIALOG_SAVE  保存;FILE_DIALOG_OPEN_FOLDER 打开目录
	 * @param title 左上角标题
	 * @param filePath 默认路径
	 * @param filters  文件类型过滤器
	 * @param selectedFilter 默认选择的过滤器索引
	 * @param callback 回调类。自动注入，不是从js传进来的
	 * 
	 * @return 
	 */
	@JsFunction(name="fileDialog")
	public void fileDialog(String mode,String title,String filePath,Vector<String> filters,Integer selectedFilter,CefQueryCallback callback) {
		CefBrowser browser=XstManager.getInstance().getSelectTabBrowser().getBrowser();
		FileDialogMode fileDialogMode=FileDialogMode.valueOf(mode==null?"FILE_DIALOG_OPEN":mode);
		title=title==null?"选择文件":title;
//		System.out.println(filePath);
//		filePath="C:\\bg.jpg";
		System.out.println(mode+"  "+title+"  "+filePath);
		
		browser.runFileDialog(fileDialogMode, title, filePath, filters, selectedFilter==null?0:selectedFilter, new CefRunFileDialogCallback() {
			
			@Override
			public void onFileDialogDismissed(Vector<String> filePaths) {
				// TODO Auto-generated method stub
//				System.out.println(filePaths);
//				System.out.println(new Gson().toJson(filePaths));
				//如果没选择文件，是一个空数组：[]
				callback.success(new Gson().toJson(filePaths));
			}
		});
		
	}
	
	
	
	/**
	 * 打开文件资源管理器
	 * @author:liuming
	 * @param path 指定路径
	 */
	@JsFunction(name="openExplorer")
	public void openExplorer(String path) {
		try {
			if(!path.endsWith("\\")) {
				int i1=path.lastIndexOf("\\");
				if(i1!=-1) {
					path=path.substring(0,i1);
				}
			}
//			System.out.println("打开目录:"+path);
			Runtime.getRuntime().exec("explorer " + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 查询翻译程序列表
	 * @return
	 */
	@JsFunction(name = "queryTranslateProgramList")
	public Message queryTranslateProgramList(){
		return Message.success("查询成功", TranslateUtil.queryTranslateProgram());
	}

	/**
	 * 查询翻译平台列表
	 * @return
	 */
	@JsFunction(name="queryTranslatePlatformList")
	public Message queryTranslatePlatformList(){
		try{
			return Message.success("查询成功", TranslateUtil.queryTranslatePlatform());
		}catch (Exception e){
			LogUtil.getLogger().error("查询翻译平台失败:{}",e.getMessage(),e);
		}
		return Message.error();
	}

	/**
	 * 查询翻译语言列表
	 * @param platformName 翻译平台名
	 * @return
	 */
	@JsFunction(name="queryTranslateLanguageList")
	public Message queryTranslateLanguageList(String platformName){
		try{
			return Message.success("查询成功", TranslateUtil.queryTranslateLanguage(platformName));
		}catch (Exception e){
			LogUtil.getLogger().error("查询翻译语言失败:{}",e.getMessage(),e);
		}
		return Message.error();
	}

	/**
	 * 查询项目版本的树列表
	 * @return
	 */
	@JsFunction(name="queryVersionTree")
	public Message queryVersionTree(){
		try{
			return Message.success("查询成功", TranslateUtil.queryVersionTree());
		}catch (Exception e){
			LogUtil.getLogger().error("查询版本树失败:{}",e.getMessage(),e);
		}
		return Message.error();
	}


	/**
	 * 获取版本工作目录
	 * @return
	 */
	@JsFunction
	public Message queryVersionFolder(Long projectId,Long versionId){
		try{
			return Message.success("查询成功", VersionUtil.getVersionFolder(projectId,versionId));
		}catch (Exception e){
			LogUtil.getLogger().error("查询版本树失败:{}",e.getMessage(),e);
		}
		return Message.error();
	}

	/**
	 * 获取工作目录
	 * @return
	 */
	@JsFunction
	public Message queryWorkFolder(){
		try{
			return Message.success("查询成功", System.getProperty("user.dir"));
		}catch (Exception e){
			LogUtil.getLogger().error("查询版本树失败:{}",e.getMessage(),e);
		}
		return Message.error();
	}
}

package com.xuanyimao.translate.util;

import com.google.gson.Gson;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.entity.project.Project;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目 - 工具类
 */
public class ProjectUtil {
	
	/**
	 * 获取项目配置文件路径
	 * @return
	 */
	public static String getProjectConfigFilePath() {
		return ApplicationData.appPath+File.separator+ Constants.TRANSLATE_DATA_FOLDER_NAME+File.separator+Constants.TRANSLATE_DATA_CONFIG_FILE;
	}

	/**
	 * 读取项目配置文件
	 * @return 如果没有数据，返回值不会为null，会返回一个空的集合
	 */
	public static void readProjectConfigFile() {
		String path = getProjectConfigFilePath();
		String data=FileUtil.readFile(path);

		try{
			if(StringUtils.isNotBlank(data)) {
				ApplicationData.projectList=GsonUtil.jsonToList(data,Project.class);
				return;
			}
		} catch (Exception e) {
			LogUtil.getLogger().error("解析数据失败:{}",e.getMessage(),e);
		}
		ApplicationData.projectList=new ArrayList<Project>();
	}

	/**
	 * 保存配置文件
	 */
	public static void saveProjectConfigFile() {
		String path = getProjectConfigFilePath();
		String data = new Gson().toJson(ApplicationData.projectList);
		FileUtil.saveFile(path,data);
	}

}

package com.xuanyimao.translate.util;

import com.google.gson.Gson;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.entity.project.Version;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 版本 -工具类
 */
public class VersionUtil {

    /**
     * 获取项目下版本配置文件路径
     * @param projectId 项目id
     * @return
     */
    public static String getVersionConfigFilePath(Long projectId) {
        return ApplicationData.appPath+ File.separator+ Constants.TRANSLATE_DATA_VERSION_FOLDER_NAME+File.separator+projectId;
    }


    /**
     * 获取当前版本的工作目录路径。路径末尾带 \
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @return
     */
    public static String getVersionFolder(Long projectId,Long versionId){
        Version version=getVersion(projectId,versionId);
        if(version==null){
            return null;
        }
        return ApplicationData.appPath+ File.separator+ Constants.TRANSLATE_DATA_FOLDER_NAME+File.separator+version.getUuid()+File.separator;
    }

    /**
     * 根据项目id获取所有版本数据。如果没有值，返回一个空的集合，不会为null
     * @param projectId
     * @return
     */
    public static List<Version> queryVersionListByProjectId(Long projectId){
        String path = getVersionConfigFilePath(projectId);
        String data=FileUtil.readFile(path);
        try {
            if(StringUtils.isNotBlank(data)){
                List<Version> list=GsonUtil.jsonToList(data,Version.class);
                updateProgramDesc(list);
                return list;
            }
        } catch (Exception e) {
            LogUtil.getLogger().error("解析数据失败:{}",e.getMessage(),e);
        }
        return new ArrayList<Version>();
    }

    /**
     * 查询所有的版本数据
     * @return
     */
    public static List<Version> queryAllVersion(){
        //版本数据目录
        String folder = ApplicationData.appPath+ File.separator+ Constants.TRANSLATE_DATA_VERSION_FOLDER_NAME+File.separator;
        File file=new File(folder);
        if(!file.exists() && !file.isDirectory()){
            return new ArrayList<>();
        }
        List<Version> result=new ArrayList<>();
        for(File file1: file.listFiles()){
            if(!file1.isDirectory()){
                try {
                    String data=FileUtil.readFile(file1);
                    if(StringUtils.isNotBlank(data)){
                        result.addAll(GsonUtil.jsonToList(data,Version.class));
                    }
                } catch (Exception e) {
                    LogUtil.getLogger().error("解析数据失败:{}",e.getMessage(),e);
                }
            }
        }
        updateProgramDesc(result);
        return result;
    }

    /**
     * 保存配置文件
     * @param projectId 项目ID
     * @param versionList 版本列表
     */
    public static void saveVersionConfigFile(Long projectId, List<Version> versionList) {
        String path = getVersionConfigFilePath(projectId);
        String data = new Gson().toJson(versionList);
        FileUtil.saveFile(path,data);
    }

    /**
     * 更新程序描述
     * @param versionList
     */
    public static void updateProgramDesc(List<Version> versionList) {
        if(versionList==null||versionList.isEmpty()){
            return;
        }
        for(Version version:versionList){
            version.setProgramDesc(TranslateUtil.getProgramDesc(version.getProgramName()));
        }
    }

    /**
     * 获取项目版本对象
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @return
     */
    public static Version getVersion(Long projectId,Long versionId){
        List<Version> versionList= queryVersionListByProjectId(projectId);
        for(Version version:versionList){
            if(version.getId().equals(versionId)){
                return version;
            }
        }
        return null;
    }
}

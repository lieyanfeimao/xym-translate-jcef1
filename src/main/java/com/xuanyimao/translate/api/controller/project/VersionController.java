package com.xuanyimao.translate.api.controller.project;

import com.google.gson.Gson;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.anno.JsFunction;
import com.xuanyimao.translate.anno.JsObject;
import com.xuanyimao.translate.api.service.project.VersionService;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.Message;
import com.xuanyimao.translate.entity.project.Version;
import com.xuanyimao.translate.entity.translate.TranslateData;
import com.xuanyimao.translate.util.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版本管理
 */
@JsClass(prefix = "version")
public class VersionController {

    @JsObject
    VersionService versionService;

    /**
     * 查询列表
     * @param projectId 项目ID
     * @return
     */
    @JsFunction(name = "queryList")
    public Message queryList(Long projectId) {
        if(projectId == null){
            return Message.error("请选择项目");
        }
        return Message.success("查询成功", VersionUtil.queryVersionListByProjectId(projectId));
    }

    /**
     * 保存/更新
     * @param projectId 项目ID
     * @param version 版本对象
     * @return
     */
    @JsFunction(name = "save")
    public Message save(Long projectId,Version version) {
        try {
            if(projectId == null){
                return Message.error("请选择项目");
            }
            if(version == null){
                return Message.error("参数错误");
            }
            if(StringUtils.isBlank(version.getName())){
                return Message.error("版本名不能为空");
            }

            return versionService.save(projectId,version)?Message.success("操作成功"):Message.error("操作失败");
        }catch (Exception e) {
            LogUtil.getLogger().error("保存失败：{}",e.getMessage(),e);
            return Message.error("保存失败："+e.getMessage());
        }
    }

    /**
     * 根据ID查询实体
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @return
     */
    @JsFunction(name="queryById")
    public Message queryById(Long projectId,Long versionId){
        try {
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }
            Version version=versionService.findVersion(projectId,versionId);
            if(version==null){
                return Message.error("数据不存在或已被删除");
            }

            return Message.success("查询成功",version);
        }catch (Exception e) {
            LogUtil.getLogger().error("查询失败：{}",e.getMessage(),e);
            return Message.error("查询失败："+e.getMessage());
        }
    }

    /**
     * 删除
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @return
     */
    @JsFunction(name = "delete")
    public Message delete(Long projectId,Long versionId) {
        try{
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }

            versionService.delete(projectId,versionId);

            return Message.success("删除成功");
        }catch (Exception e) {
            LogUtil.getLogger().error("删除失败：{}",e.getMessage(),e);
            return Message.error("删除失败："+e.getMessage());
        }
    }

    /**
     * 查询翻译程序列表
     * @return
     */
    @JsFunction(name = "queryProgramList")
    public Message queryProgramList(){
        return Message.success("查询成功", TranslateUtil.queryTranslateProgram());
    }

    /**
     * 启动翻译 - 翻译启动页面url
     * @param projectId
     * @param versionId
     * @return
     */
    @JsFunction(name = "startTranslate")
    public Message startTranslate(Long projectId,Long versionId){
        try{
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }
            Version version=versionService.findVersion(projectId,versionId);
            if(version==null){
                return Message.error("版本不存在或已被删除");
            }
            //获取翻译程序对象
            TranslateProgram translateProgram=TranslateUtil.getTranslateProgram(version.getProgramName());
            if(translateProgram==null){
                return Message.error("程序不存在或已被删除");
            }

            return Message.success("操作成功",translateProgram.startTranslatePage());
        }catch (Exception e) {
            LogUtil.getLogger().error("获取启动页面失败：{}",e.getMessage(),e);
            return Message.error("获取启动页面失败："+e.getMessage());
        }
    }


    /**
     * 翻译修正 - 翻译修正首页的url
     * @param projectId
     * @param versionId
     * @return
     */
    @JsFunction(name = "updateTranslate")
    public Message updateTranslate(Long projectId,Long versionId){
        try{
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }
            Version version=versionService.findVersion(projectId,versionId);
            if(version==null){
                return Message.error("版本不存在或已被删除");
            }
            //获取翻译程序对象
            TranslateProgram translateProgram=TranslateUtil.getTranslateProgram(version.getProgramName());
            if(translateProgram==null){
                return Message.error("程序不存在或已被删除");
            }

            return Message.success("操作成功",translateProgram.updateTranslatePage());
        }catch (Exception e) {
            LogUtil.getLogger().error("获取修正页面失败：{}",e.getMessage(),e);
            return Message.error("获取修正页面失败："+e.getMessage());
        }
    }

    /**
     * 获取生成离线文件页面失败
     * @param projectId
     * @param versionId
     * @return
     */
    @JsFunction(name = "createLatestPage")
    public Message createLatestPage(Long projectId,Long versionId){
        try{
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }
            Version version=versionService.findVersion(projectId,versionId);
            if(version==null){
                return Message.error("版本不存在或已被删除");
            }
            //获取翻译程序对象
            TranslateProgram translateProgram=TranslateUtil.getTranslateProgram(version.getProgramName());
            if(translateProgram==null){
                return Message.error("程序不存在或已被删除");
            }

            return Message.success("操作成功",translateProgram.createLatestPage());
        }catch (Exception e) {
            LogUtil.getLogger().error("获取生成离线文件页面失败：{}",e.getMessage(),e);
            return Message.error("获取生成离线文件页面失败："+e.getMessage());
        }
    }

    /**
     * 获取阅览离线文件 首页
     * @param projectId
     * @param versionId
     * @return
     */
    @JsFunction(name = "viewLatestPage")
    public Message viewLatestPage(Long projectId,Long versionId){
        try{
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }
            Version version=versionService.findVersion(projectId,versionId);
            if(version==null){
                return Message.error("版本不存在或已被删除");
            }
            //获取翻译程序对象
            TranslateProgram translateProgram=TranslateUtil.getTranslateProgram(version.getProgramName());
            if(translateProgram==null){
                return Message.error("程序不存在或已被删除");
            }

            return Message.success("操作成功",translateProgram.viewLatestPage());
        }catch (Exception e) {
            LogUtil.getLogger().error("获取阅览离线文件页面失败：{}",e.getMessage(),e);
            return Message.error("获取阅览离线文件页面失败："+e.getMessage());
        }
    }

    /**
     * 创建词库文件
     * @param path 文件保存路径
     * @param projectId 项目id
     * @param versionId 版本id
     * @return
     */
    @JsFunction(name = "createDictStore")
    public Message createDictStore(String path,Long projectId,Long versionId){
        try{
            if( projectId==null || versionId==null ){
                return Message.error("参数错误");
            }
            if(StringUtils.isBlank(path)) {
                return Message.error("文件存储路径不能为空");
            }
            String folder=VersionUtil.getVersionFolder(projectId,versionId)+ Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME;

            File dir=new File(folder);
            if(!dir.exists() && !dir.isDirectory()){
                return Message.error("请先进行翻译再使用本功能");
            }
            File[] files=dir.listFiles();

            Map<String,String> result=new HashMap<>();
            for(File file:files){
                String data= FileUtil.readFile(file.getAbsolutePath());
                if(StringUtils.isNotBlank(data)){
                    List<TranslateData> list= GsonUtil.jsonToList(data,TranslateData.class);
                    for (TranslateData translateData:list) {
                        result.put(translateData.getOldText(),translateData.getTlText());
                    }
                }
            }

            FileUtil.saveFile(path,new Gson().toJson(result));

            File file=new File(path);
            Runtime.getRuntime().exec("explorer " + file.getParent());

            return Message.success("导出词库文件成功");
        }catch (Exception e) {
            LogUtil.getLogger().error("创建词库文件失败：{}",e.getMessage(),e);
            return Message.error("创建词库文件失败："+e.getMessage());
        }
    }
}

package com.xuanyimao.translate.api.service.project;

import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.entity.project.Version;
import com.xuanyimao.translate.util.LogUtil;
import com.xuanyimao.translate.util.VersionUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 版本管理 - service
 */
@JsClass
public class VersionService {

    /**
     * 保存/更新数据
     * @param projectId 项目ID
     * @param version 版本数据
     * @return
     */
    public boolean save(Long projectId,Version version) {
        //获取项目下的所有版本数据
        List<Version> versionList= VersionUtil.queryVersionListByProjectId(projectId);

        if(version.getId()==null){//新增
            Long maxId=findMaxId(versionList,0L);

            version.setUuid(generateUuid());
            version.setId(maxId+1);
            versionList.add(version);
        }else{//修改
            Version findVersion=findVersionById(versionList,version.getId());
            if(findVersion==null){
                return false;
            }
            findVersion.setName(version.getName());
            findVersion.setOrder(version.getOrder());
            findVersion.setRemark(version.getRemark());
            findVersion.setProgramName(version.getProgramName());
        }

        Collections.sort(versionList);

        VersionUtil.saveVersionConfigFile(projectId,versionList);
        return true;
    }


    /**
     * 查找最大ID
     * @param versionList 版本列表
     * @param id 默认值
     * @return
     */
    public Long findMaxId(List<Version> versionList,Long id) {
        if(versionList==null || versionList.isEmpty()){
            return 0L;
        }
        for(Version version:versionList){
            if(version.getId()>id){
                id=version.getId();
            }
        }
        return id;
    }

    /**
     * 生成uuid
     * @return
     */
    public String generateUuid(){
        //获取所有的版本数据
        List<Version> versionList=VersionUtil.queryAllVersion();
        List<String> uuidList=new ArrayList<String>();
        for(Version version:versionList){
            uuidList.add(version.getUuid());
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        while(uuidList.contains(uuid)){
            uuid=UUID.randomUUID().toString().replace("-", "");
        }

        return uuid;
    }

    /**
     * 删除
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @return
     */
    public boolean delete(Long projectId,Long versionId) {
        List<Version> versionList= VersionUtil.queryVersionListByProjectId(projectId);
        for(Version version:versionList){
            if(version.getId()==versionId){
                versionList.remove(version);

                //同时删除目录
                String folder= ApplicationData.appPath+ File.separator+ Constants.TRANSLATE_DATA_FOLDER_NAME+File.separator+version.getUuid()+File.separator;

                File file=new File(folder);
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    LogUtil.getLogger().error("删除版本文件目录失败:{}",e.getMessage(),e);
                }


                VersionUtil.saveVersionConfigFile(projectId,versionList);
                return true;
            }
        }
        return false;
    }

    /**
     * 根据ID查询版本数据对象
     * @param versionList 版本数据列表
     * @param id 数据ID
     * @return
     */
    public Version findVersionById(List<Version> versionList, Long id) {
        if(versionList==null || versionList.isEmpty() || id==null) return null;

        for (Version version : versionList) {
            if(version.getId().equals(id)){
                return version;
            }
        }
        return null;
    }

    /**
     * 根据 项目ID 和 版本ID 查找版本文件
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @return
     */
    public Version findVersion(Long projectId,Long versionId){
        List<Version> versionList= VersionUtil.queryVersionListByProjectId(projectId);
        for (Version version : versionList) {
            if(version.getId().equals(versionId)){
                return version;
            }
        }
        return null;
    }
}

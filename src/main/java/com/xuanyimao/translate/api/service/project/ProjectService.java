package com.xuanyimao.translate.api.service.project;

import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.entity.project.Project;
import com.xuanyimao.translate.entity.project.Version;
import com.xuanyimao.translate.util.LogUtil;
import com.xuanyimao.translate.util.ProjectUtil;
import com.xuanyimao.translate.util.VersionUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@JsClass
public class ProjectService {

    /**
     * 保存/修改 项目数据
     * @param project
     * @return
     */
    public boolean save(Project project) {
        if(project.getId()==null){//新增
            Long maxId=findMaxId(ApplicationData.projectList,0L);
            project.setId(maxId+1);
            ApplicationData.projectList.add(project);

        }else{//修改
            Project findProject=findProjectById(ApplicationData.projectList,project.getId());
            if(findProject==null){
                return false;
            }

            findProject.setOrder(project.getOrder());
            findProject.setText(project.getText());
            findProject.setParentId(project.getParentId());
        }

        ProjectUtil.saveProjectConfigFile();
        return true;
    }

    /**
     * 删除项目
     * @param project
     * @return
     */
    public boolean delete(Project project) {
        //获取所有子项目
        List<Project> allProject=new ArrayList<Project>();
        allProject.add(project);
        allProject.addAll(findChild(project));

        for (Project p : allProject) {
            deleteVersionByProject(p);
            ApplicationData.projectList.remove(p);
        }

        ProjectUtil.saveProjectConfigFile();
        return true;
    }

    /**
     * 查找子元素
     * @param project
     * @return
     */
    public List<Project> findChild(Project project) {
        List<Project> projectList=new ArrayList<>();
        for (Project p : ApplicationData.projectList) {
            if(p.getParentId()==project.getId()){
                projectList.add(p);
                projectList.addAll(findChild(p));
            }
        }
        return projectList;
    }

    /**
     * 删除项目下所有版本数据
     * @param project
     */
    public void deleteVersionByProject(Project project){
        List<Version> versionList= VersionUtil.queryVersionListByProjectId(project.getId());
        for(Version version:versionList){
            //同时删除目录
            String folder= ApplicationData.appPath+ File.separator+ Constants.TRANSLATE_DATA_FOLDER_NAME+File.separator+version.getUuid()+File.separator;

            File file=new File(folder);
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                LogUtil.getLogger().error("删除版本文件目录失败:{}",e.getMessage(),e);
            }
        }
        //删除配置文件
        String config=VersionUtil.getVersionConfigFilePath(project.getId());
        File file=new File(config);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 根据id查找项目数据
     * @param projectList
     * @param id 要查找的数据ID
     * @return
     */
    public Project findProjectById(List<Project> projectList,Long id) {
        if(projectList==null || projectList.isEmpty() || id==null) return null;

        for (Project project : projectList) {
            if(project.getId().equals(id)){
                return project;
            }
        }
        return null;
    }

    /**
     * 查找最大的id值
     * @param projectList
     * @param id 默认值
     * @return
     */
    public Long findMaxId(List<Project> projectList,Long id) {
        if(projectList==null || projectList.isEmpty()){
            return 0L;
        }
        for (Project project : projectList) {
            if(project.getId()>id){
                id=project.getId();
            }
        }
        return id;
    }


}

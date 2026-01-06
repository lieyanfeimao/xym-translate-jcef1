var versionApi={
    //查询列表
    queryList:async function(projectId){
        return await execJava("version.queryList",{projectId:projectId});
    },
    //保存数据
    save:function(params){
        return execJava("version.save",params);
    },
    //删除数据
    delete:function(projectId,versionId){
        return execJava("version.delete", {projectId:projectId,versionId:versionId});
    },
    //根据ID查询项目数据
    queryById:function(projectId,versionId){
        return execJava("version.queryById", {projectId:projectId,versionId:versionId});
    },
    //根据ID查询项目数据
    queryProgramList:function(){
        return execJava("version.queryProgramList", {});
    },
    //获取翻译启动页路径
    startTranslate:function(projectId,versionId){
        return execJava("version.startTranslate", {projectId:projectId,versionId:versionId});
    },
    //获取翻译修正启动页路径
    updateTranslate:function(projectId,versionId){
        return execJava("version.updateTranslate", {projectId:projectId,versionId:versionId});
    },
    /**
     * 创建离线版本
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    createLatestPage:function(projectId,versionId){
        return execJava("version.createLatestPage", {projectId:projectId,versionId:versionId});
    },
    /**
     * 阅览离线版本
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    viewLatestPage:function(projectId,versionId){
        return execJava("version.viewLatestPage", {projectId:projectId,versionId:versionId});
    },
    /**
     * 创建并导出词库
     * @param path 文件存储路径
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    createDictStore:function(path,projectId,versionId){
        return execJava("version.createDictStore", {path:path,projectId:projectId,versionId:versionId});
    },
    /**
     * 删除数据
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    delete:function(projectId,versionId){
        return execJava("version.delete", {projectId:projectId,versionId:versionId});
    }
}





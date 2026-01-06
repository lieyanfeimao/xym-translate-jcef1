var baseHtmlApi={
    /**
     * 查询配置
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @returns {Promise<unknown>}
     */
    config:function(projectId,versionId){
        return execJava("baseHtml.config", {projectId:projectId,versionId:versionId});
    },
    /**
     * 初始化
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @param folderPath 待翻译的文件目录
     * @returns {Promise<unknown>}
     */
    initData:function(projectId,versionId,folderPath){
        return execJava("baseHtml.initData", {projectId:projectId,versionId:versionId,folderPath:folderPath});
    },
    /**
     * 查询文件树
     * @param projectId 项目ID
     * @param versionId 版本ID
     * @returns {Promise | Promise<unknown> | *}
     */
    queryFileTree:function(projectId,versionId){
        return execJava("baseHtml.queryFileTree", {projectId:projectId,versionId:versionId});
    },
    /**
     * 保存配置
     * @param params 参数对象
     */
    saveConfig:function (params){
        return execJava("baseHtml.saveConfig", params);
    },
    /**
     * 启动翻译
     * @param params
     * @returns {Promise | Promise<unknown> | *}
     */
    startTranslate:function (params){
        return execJava("baseHtml.startTranslate", params);
    },
    /**
     * 停止翻译
     * @returns {Promise | Promise<unknown> | *}
     */
    stopTranslate:function (params){
        return execJava("baseHtml.stopTranslate", params);
    },
    queueInfo:function (params){
        return execJava("baseHtml.queueInfo", params);
    },

    /********* 离线 ********/
    /**
     * 查询所有可生成离线文件的文件
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    queryLatestFiles:function(projectId,versionId){
        return execJava("baseHtml.queryLatestFiles", {projectId:projectId,versionId:versionId});
    },
    /**
     * 生成文件
     * @param projectId
     * @param versionId
     * @param ids  选中的要生成的文件ID列表
     * @returns {Promise | Promise<unknown> | *}
     */
    createFiles:function(projectId,versionId,ids){
        return execJava("baseHtml.createFiles", {projectId:projectId,versionId:versionId,ids:ids});
    },
    /**
     * 得到预览页面
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    viewLatestPage:function(projectId,versionId){
        return execJava("baseHtml.viewLatestPage", {projectId:projectId,versionId:versionId});
    },
    /**
     * 导出离线压缩包
     * @param path 文件存储路径
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    createLatestPkg:function(path,projectId,versionId){
        return execJava("baseHtml.createLatestPkg", {path:path,projectId:projectId,versionId:versionId});
    }

}






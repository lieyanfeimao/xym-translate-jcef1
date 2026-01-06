var apiDocApi={
    /**
     * 创建搜索框
     * @param projectId
     * @param versionId
     * @returns {Promise | Promise<unknown> | *}
     */
    createSearch:function(projectId,versionId){
        return execJava("apiDoc.createSearch", {projectId:projectId,versionId:versionId});
    }

}






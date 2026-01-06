var configApi={
    /**
     * 保存配置
     * @returns {Promise<unknown>}
     */
    saveConfig:function(params){
        return execJava("config.saveConfig",params);
    },
    /**
     * 查询配置
     * @returns {*}
     */
    queryConfig:function(){
        return execJava("config.queryConfig");
    }
}





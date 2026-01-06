var projectApi={
    //查询列表
    queryList:function(){
        return execJava("project.queryList",{});
    },
    //保存数据
    save:function(params){
        return execJava("project.save",params);
    },
    //删除数据
    delete:function(id){
        return execJava("project.delete", {id:id});
    },
    //根据ID查询项目数据
    queryById:function(id){
        return execJava("project.queryById", {id:id});
    }
}





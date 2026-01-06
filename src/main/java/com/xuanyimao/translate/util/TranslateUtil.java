package com.xuanyimao.translate.util;

import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.core.translateplatform.TranslatePlatform;
import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.ComboxValue;
import com.xuanyimao.translate.entity.anno.TranslatePlatformClassAO;
import com.xuanyimao.translate.entity.anno.TranslateProgramClassAO;
import com.xuanyimao.translate.entity.project.IdRef;
import com.xuanyimao.translate.entity.project.Project;
import com.xuanyimao.translate.entity.project.Version;
import kotlin.jvm.internal.Ref;
import org.cef.misc.IntRef;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻译工具类
 */
public class TranslateUtil {

    /**
     * 获取翻译程序列表 - 下拉选择框
     * @return
     */
    public static List<ComboxValue> queryTranslateProgram(){
        List<ComboxValue> comboxValueList=new ArrayList<ComboxValue>();

        for (TranslateProgramClassAO ao: ApplicationData.translateProgramClassAOList) {
            ComboxValue cv=new ComboxValue(ao.getName(),ao.getDesc());
            comboxValueList.add(cv);
        }

        return comboxValueList;
    }

    /**
     * 获取翻译程序列表 - 下拉选择框
     * @return
     */
    public static List<ComboxValue> queryTranslatePlatform(){
        List<ComboxValue> comboxValueList=new ArrayList<ComboxValue>();
        for (TranslatePlatformClassAO ao: ApplicationData.translatePlatformClassAOList) {
            ComboxValue cv=new ComboxValue(ao.getName(),ao.getDesc());
            comboxValueList.add(cv);
        }
        return comboxValueList;
    }

    /**
     * 查询平台的翻译语言
     * @param platformName 平台名
     * @return
     */
    public static List<ComboxValue> queryTranslateLanguage(String platformName){
        List<ComboxValue> comboxValueList=new ArrayList<ComboxValue>();
        for(TranslatePlatformClassAO ao: ApplicationData.translatePlatformClassAOList){
//            System.out.println(ao.getName());
            if(ao.getName().equals(platformName)){
//                System.out.println("===========================");
//                System.out.println(ao.getPlatform().getTranslateLanguage());
                ao.getPlatform().getTranslateLanguage().forEach((k,v)->{
                    comboxValueList.add(new ComboxValue(k,v));
                });
                return comboxValueList;
            }
        }

        return comboxValueList;
    }

    /**
     * 根据程序名获取程序描述
     * @param name
     * @return
     */
    public static String getProgramDesc(String name){
        for (TranslateProgramClassAO ao: ApplicationData.translateProgramClassAOList) {
            if(ao.getName().equals(name)){
                return ao.getDesc();
            }
        }
        return "";
    }

    /**
     * 根据程序名获取程序对象
     * @param name
     * @return
     */
    public static TranslateProgram getTranslateProgram(String name){
        for (TranslateProgramClassAO ao: ApplicationData.translateProgramClassAOList) {
            if(ao.getName().equals(name)){
                return ao.getTranslateProgram();
            }
        }
        return null;
    }

    /**
     * 创建一个翻译程序
     * @param name 翻译程序名
     * @return
     * @throws Exception
     */
    public static TranslateProgram createTranslateProgram(String name) throws Exception {
        for (TranslateProgramClassAO ao: ApplicationData.translateProgramClassAOList) {
            if(ao.getName().equals(name)){
                return (TranslateProgram)ao.getClazz().getDeclaredConstructor().newInstance();
            }
        }
        return null;
    }

    /**
     * 获取翻译平台
     * @param name 平台名
     * @return
     */
    public static TranslatePlatform getTranslatePlatform(String name){
        for (TranslatePlatformClassAO ao: ApplicationData.translatePlatformClassAOList) {
            if(ao.getName().equals(name)){
                return ao.getPlatform();
            }
        }
        return null;
    }

    /**
     * 查询版本树 - zTree
     * @return
     */
    public static List<Map<String,Object>> queryVersionTree(){
        List < Map < String, Object > > treeList=new ArrayList<Map < String, Object >>();
        //主节点
        Map < String, Object > data=createNodeMap(0L,"项目版本列表",null);
        data.put("open", true);
        data.put("order", 0);

        data.put("attributes", createAttributeMap(0,"") );

        treeList.add(data);

        for(Project p: ApplicationData.projectList){
            data=createNodeMap(p.getId(),p.getText(),p.getParentId()==null?0L:p.getParentId());
            data.put("order", p.getOrder());

            data.put("attributes", createAttributeMap(0,p.getText()) );

            treeList.add(data);

            treeList.addAll(findVersion(p.getId()));
        }


        return treeList;
    }

    /**
     * 查找版本 - zTree
     * @param projectId
     * @return
     */
    public static List<Map<String,Object>> findVersion(Long projectId){
        List < Map < String, Object > > treeList=new ArrayList<Map < String, Object >>();
        List<Version> versionList=VersionUtil.queryVersionListByProjectId(projectId);
        for (Version v: versionList) {
            Map < String, Object > data=createNodeMap(0L,v.getName(),projectId);
            //重新设置了id
            data.put("id", projectId+"-"+v.getId());
            data.put("order", v.getOrder());

            data.put("attributes", createAttributeMap(1,v.getName()) );

            treeList.add(data);
        }

        return treeList;
    }

    /**
     * 查找文件并返回树列表数据 - zTree
     * @param file
     * @return
     */
    public static List<Map<String,Object>> queryFileTree(File file){
        IdRef idRef=new IdRef();

        List < Map < String, Object > > treeList=new ArrayList<Map < String, Object >>();

        //主节点
        Map < String, Object > data=createNodeMap(idRef.getValue(),"文件列表",null);
        data.put("open", true);
        data.put("order", 0);

        data.put("attributes", createAttributeMap(1,file.getAbsolutePath()) );

        treeList.add(data);

        treeList.addAll(findChildren(file,idRef));

        return treeList;
    }

    /**
     * 查找所有子文件
     * @param file 文件
     * @param idRef id引用
     * @return
     */
    public static List<Map<String,Object>> findChildren(File file,IdRef idRef){
        long parentId=idRef.getValue();
        List < Map < String, Object > > treeList=new ArrayList<Map < String, Object >>();

        File[] files=file.listFiles();
        for(File f:files) {
            idRef.increment();
            Map<String,Object> data=createNodeMap(idRef.getValue(),f.getName(),parentId);
            data.put("order", 1);

            data.put("attributes", createAttributeMap(f.isDirectory()?1:0,f.getAbsolutePath()) );

            treeList.add(data);
            if(f.isDirectory()){
                data.put("order", 0);
                treeList.addAll( findChildren(f,idRef) );
            }
        }

        return treeList;
    }

    /**
     * 创建节点map
     * @param id 节点id
     * @param name 名称
     * @param pId  父节点id
     * @return
     */
    private static Map < String, Object > createNodeMap(Long id,String name,Long pId){
        Map < String,Object > node=new HashMap < String, Object >();
        node.put("id",id);
        node.put("name", name);
        node.put("pId", pId);
        return node;
    }

    /**
     * 创建属性map
     * @param type 类型
     * @param path 文件路径
     * @return
     */
    private static Map< String,Object > createAttributeMap(int type,String path){
        Map < String,Object > attributes=new HashMap < String, Object >();
        attributes.put("type", type);
        attributes.put("path", path);
        return attributes;
    }

    /**
     * 获取字符在字符串中出现的次数
     * @author liuming
     * @since 2023年9月25日
     * @param c
     * @return
     */
    public static int getCharCount(String data,char c) {
        int count=0;
        for(int i=0;i<data.length();i++) {
            char c1=data.charAt(i);
            if(c==c1) {
                count++;
            }
        }
        return count;
    }
}

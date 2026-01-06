package com.xuanyimao.translate.api.service.program;


import com.xuanyimao.translate.anno.JsClass;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Api文档
 * @author liuming
 */
@JsClass
public class ApiDocService {

    //需要添加搜索框的文件名
    private final static String APIDOC_SEARCH_HTMLFILE="allclasses-frame.html";
    //需要添加搜索框的文件名
    private final static String APIDOC_SEARCH_HTMLFILE1="package-frame.html";

    private final static String APIDOC_SEARCH_JQUERY_JS="js/jquery.min.js";
    private final static String APIDOC_SEARCH_SEARCH_JS="js/xst-search.js";

    /**
     * 创建文件搜索框
     * @param folder 目录
     * @param index  索引。用于html中添加搜索框的脚本
     * @param oldCode 原文件编码
     * @param outCode  输出文件编码
     */
    public void createSearch(File folder, int index, String oldCode, String outCode) {
        String pathPre="";
        for(int i=0;i<index;i++) {
            pathPre+="../";
        }

        File[] fileList = folder.listFiles();
        if(fileList!=null) {
            for(File f:fileList) {
                if(!f.isDirectory()) {
                    if(f.getName().endsWith(APIDOC_SEARCH_HTMLFILE)
                            || f.getName().endsWith(APIDOC_SEARCH_HTMLFILE1)) {
                        createSearchFile(f, pathPre, oldCode, outCode);
                    }
                }else {
                    createSearch(f, index+1, oldCode, outCode);
                }
            }
        }
    }


    public void createSearchFile(File f,String pathPre,String oldCode,String outCode) {
        try {
            //创建搜索框
            Element jqScript=new Element("script").attr("src",pathPre+APIDOC_SEARCH_JQUERY_JS);
            Element shScript=new Element("script").attr("src",pathPre+APIDOC_SEARCH_SEARCH_JS);

            Element meta=new Element("meta").attr("charset", "UTF-8");

            FileInputStream fis = new FileInputStream(f);
            String data= IOUtils.toString(fis,oldCode);
            IOUtils.closeQuietly(fis);

            Document doc= Jsoup.parse(data,oldCode);
            doc.selectFirst("head").appendChild(meta).appendChild(jqScript).appendChild(shScript);
            Element h1=doc.selectFirst("h1.bar");
            h1.before(new Element("div").attr("class", "bar").append(h1.html()+"<input type=\"text\" id=\"search\" placeholder=\"搜索\" autocomplete=\"off\" />"));
//            h1.before(new Element("div").attr("style", "height:2em;"));
            h1.remove();

            FileOutputStream fos=new FileOutputStream(f);
            IOUtils.write(doc.outerHtml(), fos,outCode);
            IOUtils.closeQuietly(fos);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}

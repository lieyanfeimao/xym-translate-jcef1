package com.xuanyimao.translate.core.translateprogram.component;

import com.google.gson.Gson;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.core.XstManager;
import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.htmldoc.DocElement;
import com.xuanyimao.translate.entity.translate.TranslateData;
import com.xuanyimao.translate.entity.translate.TranslateResult;
import com.xuanyimao.translate.util.FileUtil;
import com.xuanyimao.translate.util.XStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

/**
 * html文档 处理程序
 * @author liuming
 * @date 2025年12月17日 09:04
 */
public abstract class HtmlDocTranslateProgram extends TranslateProgram {

    /**文档对象**/
    protected Document doc;

    /**文档元素**/
    protected List<DocElement> elemList;

    /**当前翻译的元素索引**/
    protected int index=0;

    /** 页面文件数据 **/
    protected List<TranslateData> tdList=new ArrayList<TranslateData>();

    /** jquery文件路径 */
    protected String jqueryJsFile="js/jquery.min.js";

    /*********  修正用 ***********/
    /** layui-js文件路径 */
    protected String layuiJsFile="layui/layui.js";
    /** layui-css文件路径 */
    protected String layuiCssFile="layui/css/layui.css";
    /** 修正用的脚本 */
    protected String updateJsFile="js/xst-update.js";


    /***** 模板用 *****/
    protected String modelCssFile="css/xst-ts.css";
    protected String modelJsFile="js/xst-lg.js";

    @Override
    public void run() {
        if(this.stop) return;

        Map<String, Object> notifyMap=new HashMap<String, Object>();
        notifyMap.put("id", fileInfo.getId());
        notifyMap.put("file", fileInfo.getPath());
        try {

            //读取文件
            String data=readFile(versionFolder+ Constants.TRANSLATE_DATA_FOLDER_OLD+ File.separator+fileInfo.getPath());
            if(data==null) {
                notifyMap.put("status", 0);
                notifyMap.put("errorInfo", "文件读取失败!");
                XstManager.getInstance().notifyTranslateJs(browserId, new Gson().toJson(notifyMap));
                return;
            }

            //解析文件
            parseData(data);

            //数据文件对象
            File dataFile=new File(dataDir+fileInfo.getId());

            //逐句翻译
            for(DocElement de:elemList) {
                try {
                    if(this.stop) return;
                    //数据id
                    String dataId="t_"+ UUID.randomUUID().toString().replace("-", "");
                    //获取需要翻译的句子
                    String content=de.getText();
                    String tlData=caches.get(content);

                    //缓存中没有数据并且待翻译的内容不为空
                    if(tlData==null && !StringUtils.isBlank(content)) {
//			        	System.out.println(">>> "+content);
                        TranslateResult tr=this.translatePlatform.getTranslateData(content,configData.getSrcLanguage(),configData.getDestLanguage());
                        if(tr.isOk()) {//翻译成功
                            tlData=tr.getData();
                            caches.put(content, tlData);
                        }else {
                            //通知前端页面此条翻译失败
                            notifyMap.put("status", 0);
                            notifyMap.put("errorInfo", tr.getData());
                            XstManager.getInstance().notifyTranslateJs(browserId, new Gson().toJson(notifyMap));
                        }
                    }


                    updateDocElement(dataId,de,tlData);
                    tdList.add(new TranslateData(dataId, content, tlData));
                    //保存数据文件
                    FileUtil.saveFile(dataFile, new Gson().toJson(tdList));


                    notifyMap.put("status", 1);
                    notifyMap.put("total", elemList.size());

                    notifyMap.put("dealCount", this.index+1);

                    //通知前端页面更新数据条数
                    XstManager.getInstance().notifyTranslateJs(browserId, new Gson().toJson(notifyMap));

                    this.index++;
                }catch(Exception e) {
                    e.printStackTrace();
                    //通知前端页面此条翻译失败
                    notifyMap.put("status", 0);
                    notifyMap.put("errorInfo", e.getMessage());
                    XstManager.getInstance().notifyTranslateJs(browserId, new Gson().toJson(notifyMap));
                }

            }

            saveFile();

            //文件翻译完成
            notifyMap.put("status", 2);
            XstManager.getInstance().notifyTranslateJs(browserId, new Gson().toJson(notifyMap));

        }catch(Exception e) {
            e.printStackTrace();

            notifyMap.put("status", 0);
            notifyMap.put("errorInfo", e.getMessage());
            XstManager.getInstance().notifyTranslateJs(browserId, new Gson().toJson(notifyMap));
        }
    }

    /**
     * 保存文件
     * @author liuming
     * @since 2023年9月25日
     */
    public void saveFile() {
        int count= XStringUtil.getCharCount(fileInfo.getPath(),File.separatorChar);

        String pathPre="";
        for(int i=0;i<count;i++) {
            pathPre+="../";
        }
        //生成修正用的文件
        Element layuiScript=new Element("script").attr("src",pathPre+layuiJsFile);
        Element layuiCss=new Element("link").attr("href",pathPre+layuiCssFile).attr("rel", "stylesheet");

        Element jqScript=new Element("script").attr("src",pathPre+jqueryJsFile);
        Element xstUpdateScript=new Element("script").attr("src",pathPre+updateJsFile);

        Element codeScript=new Element("script").attr("type", "text/javascript");
        codeScript.appendChild(new DataNode(UPDATE_SCRIPT.replace("{projectId}", projectId+"").replace("{versionId}", versionId+"").replace("{fileId}", fileInfo.getId())));

        doc.selectFirst("head").appendChild(META).appendChild(layuiCss).appendChild(jqScript).appendChild(layuiScript);
        doc.selectFirst("body").appendChild(codeScript).appendChild(xstUpdateScript);

        dealTlElem();

        //创建目录
        String savePath=versionFolder+File.separator+Constants.TRANSLATE_DATA_FOLDER_UPDATE+File.separator+fileInfo.getPath();
        int i1=savePath.lastIndexOf(File.separator);
        String saveFolder=savePath.substring(0, i1);
        File file=new File(saveFolder);
        if(!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        //保存文件
        FileUtil.saveFile(savePath, doc.outerHtml(),configData.getFileEncode());

        //模板文件处理

        layuiCss.remove();
        layuiScript.remove();
        codeScript.remove();
        xstUpdateScript.remove();
        for(DocElement de:elemList) {
            de.getTlElem().remove();
        }

        //生成模板文件
        dealModelElem();

        Element tlCss=new Element("link").attr("href",pathPre+modelCssFile).attr("rel", "stylesheet");

        Element xstLgScript=new Element("script").attr("src",pathPre+modelJsFile);

        doc.selectFirst("head").appendChild(tlCss).appendChild(xstLgScript);

        savePath=versionFolder+File.separator+Constants.TRANSLATE_DATA_FOLDER_MODEL+File.separator+fileInfo.getPath();
        i1=savePath.lastIndexOf(File.separator);
        saveFolder=savePath.substring(0, i1);
        file=new File(saveFolder);
        if(!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        String data=doc.outerHtml();
        //模板使用freemarker处理，预处理$符号
        data=data.replace("$", "${'$'}");
        data=data.replace("XYM_TS{", "${");

        FileUtil.saveFile(savePath, data,configData.getFileEncode());
    }

    /**
     * 处理翻译元素
     */
    public void dealTlElem(){
        for(DocElement de:elemList) {
            de.getElem().appendChild(de.getTlElem());
        }
    }

    /**
     * 处理模板元素
     */
    public void dealModelElem(){
        for(DocElement de:elemList) {
            String html=de.getElem().html();
            de.getElem().html("<span lg_en=\""+de.getId()+"\">"+html+"</span>");
            de.getElem().appendChild(de.getModelElem());
        }
    }

    @Override
    public abstract void parseData(String data);

    /**
     * 更新文档元素 - 此方法在每条数据翻译后执行一次，用于设置 离线文件模板 和 修正用的模板
     * @param id   数据id
     * @param de   html元素
     * @param result  翻译后的文本内容
     */
    public void updateDocElement(String id,DocElement de,String result) {
        de.setTlText(result);
        de.setId(id);

        //翻译修正用的元素
        //包含：dataId(数据id),dataTpl(模板索引[0：通用模板，1：poi代码块模板]),dataTxtColor(翻译文本的颜色[如：rgb(255,255,255)])
        de.setTlElem(
                new Element("div").attr("name","tsUpdate").attr("data-id", id).attr("data-tpl", "0").attr("data-txt-color", "green")
        );

        de.setModelElem(
                new Element("span").attr("class", "tscolor").attr("lg_zh",id).append(MD_TRANS.replace("{dataId}", id))
        );
    }

    /**
     * 通用html元素处理方法
     * @param elems 元素组
     */
    public void commonDealElement(Elements elems) {
        commonDealElement(elems,0);
    }

    /**
     * 通用html元素处理方法
     * @param elems 元素组
     * @param flag  标识
     */
    public void commonDealElement(Elements elems,int flag) {
        if(elems!=null) {
            for(Element em:elems) {
                if(StringUtils.isBlank(em.text())) {
                    continue;
                }
//        		System.out.println(em.text());

                elemList.add(createDocElement(em,em.text(),flag));
            }
        }
    }

    /**
     * 创建文档元素对象
     * @param elem 元素
     * @param text 文本内容
     * @return
     */
    public DocElement createDocElement(Element elem, String text) {
        return createDocElement(elem,text,0);
    }

    /**
     * 创建文档元素对象
     * @param elem 元素
     * @param text 文本内容
     * @param flag 标记
     * @return
     */
    public DocElement createDocElement(Element elem, String text,int flag) {
        DocElement de=new DocElement();
        de.setElem(elem);
        de.setText(text);
        de.setFlag(flag);
        return de;
    }


    /** head 中的meta标签 */
    public final static Element META=new Element("meta").attr("charset", "UTF-8");

    /******* 修正文件使用 *********/
    /**添加到修正页面的 按钮 **/
//    public final static String TL_TRANS="<span id=\"{idStr}_input\" style=\"display:none;\">\r\n" +
//            "      <textarea id=\"{idStr}\" style=\"width:100%;height:40px;\"></textarea> \r\n" +
//            "     <button class=\"layui-btn layui-btn-xs layui-btn-normal\" style=\"width:30%;margin-left:15%;\" onclick=\"updateTl('{idStr}')\">保存</button>\r\n" +
//            "	 <button class=\"layui-btn layui-btn-xs\" style=\"width:30%;\" onclick=\"hideInput('{idStr}')\">隐藏修正框</button>\r\n" +
//            "	  </span>\r\n" +
//            "	  \r\n" +
//            "	  <span id=\"{idStr}_text\">\r\n" +
//            "	  <span style=\"color:green;\" id=\"{idStr}_span\"></span>\r\n" +
//            "	  <button class=\"layui-btn layui-btn-xs layui-btn-danger\" onclick=\"gotoUpdateData('{idStr}');\">修正</button>\r\n" +
//            "	  &nbsp;&nbsp;<button class=\"layui-btn layui-btn-xs layui-btn-normal\" onclick=\"gotoReplaceData('{idStr}');\">替换其他位置相同译文</button>" +
//            "	  </span><span id=\"vs_{idStr}\" style=\"font-size:10pt;color:red;\"></span><a name=\"{idStr}_a\"></a>";
//    /**添加到修正页面的 按钮模板，包含：数据id,模板索引[0：通用模板，1：poi代码块模板],翻译文本的颜色[如：rgb(255,255,255)] **/
//    public final static String TL_TRANS="<div name=\"tsUpdate\" dataId=\"{dataId}\" dataTpl=\"{dataTpl}\" dataTxtColor=\"{dataTxtColor}\"></div>";

    /** 添加到修正页面的javascript脚本 **/
    public final static String UPDATE_SCRIPT="var projectId='{projectId}';var versionId='{versionId}';var fileId='{fileId}';";

    /*******模板文件使用*******/
    public final static String MD_TRANS="<span lg_kh='1'>(</span>XYM_TS{{dataId}}<span lg_kh='1'>)</span>";


}

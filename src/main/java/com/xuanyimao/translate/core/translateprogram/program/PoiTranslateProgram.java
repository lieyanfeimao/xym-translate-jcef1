package com.xuanyimao.translate.core.translateprogram.program;

import com.xuanyimao.translate.anno.TranslateProgramClass;
import com.xuanyimao.translate.core.translateprogram.component.HtmlDocTranslateProgram;
import com.xuanyimao.translate.entity.htmldoc.DocElement;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * poi官方文档
 * @author liuming
 */
@TranslateProgramClass(name="poiHelp",desc="POI开发文档翻译程序",order=1)
public class PoiTranslateProgram extends HtmlDocTranslateProgram {

    @Override
    public void parseData(String data) {
        elemList=new ArrayList<DocElement>();
        doc= Jsoup.parse(data,configData.getFileEncode());

        Element el=doc.selectFirst("body");
        if(el!=null) el.removeAttr("onload");

        el=doc.selectFirst("div#top");
        if(el!=null) el.remove();

        el=doc.selectFirst("div#publishedStrip");
        if(el!=null) el.remove();

        el=doc.selectFirst("div.breadtrail");
        if(el!=null) el.remove();

        el=doc.selectFirst("div#menu");
        if(el!=null) el.remove();

        el=doc.selectFirst("div#content");

        Elements topElems=doc.select("h1");
        commonDealElement(topElems);

        topElems=doc.select("h2");
        commonDealElement(topElems,1);

        topElems=doc.select("h3");
        commonDealElement(topElems,1);

        topElems=doc.select("h4");
        commonDealElement(topElems);

        topElems=doc.select("p");
        commonDealElement(topElems);

        topElems=doc.select("li");
        commonDealElement(topElems);

        topElems=doc.select("th");
        commonDealElement(topElems);

        topElems=doc.select("td");
        commonDealElement(topElems);

        topElems=doc.select("div.content");
        commonDealElement(topElems);

        topElems=doc.select("div.label");
        commonDealElement(topElems,1);

        //代码块注释
        boolean flag=false;
        boolean flag1=false;
        String str="";
        String str1="";

        topElems=doc.select("div.codeline");
        for(Element telem:topElems) {
            Element e1=telem.selectFirst("span.codebody");

            if(e1!=null) {
                if(e1.text().indexOf("//")==0) {
                    flag = true;
                    str += e1.text() + "\r\n";
                    //选择中文时隐藏
                    e1.parent().attr("lg_en", "1");
                }else if(e1.text().indexOf("/**")==0) {
                    flag1 = true;
                    str1+=e1.text() + "\r\n";
                    //选择中文时隐藏
                    e1.parent().attr("lg_en", "1");
                }else {
                    if(flag) {
                        DocElement de=new DocElement();
                        de.setElem(telem);
                        de.setText(str);
                        de.setFlag(2);
                        elemList.add(de);
                        str="";
                        flag=false;
                    }

                    if(flag1) {
                        str1+=e1.text() + "\r\n";
                        //选择中文时隐藏
                        e1.parent().attr("lg_en", "1");
                    }

                    if(e1.text().indexOf("//")!=-1){
                        int i1=e1.text().indexOf("//");
                        String prev=e1.text().substring(0,i1+2);
                        String txt=e1.text().substring(i1+2);
                        e1.html(prev);
                        Element e2=new Element("span").html(txt);
                        e1.appendChild(e2);

                        DocElement de=new DocElement();
                        de.setElem(e2);
                        de.setText(e2.text());
                        de.setFlag(3);
                        elemList.add(de);
                    }else if(e1.text().trim().equals("*/")) {
                        flag1=false;
                        DocElement de=new DocElement();
                        de.setElem(telem);
                        de.setText(str1);
                        de.setFlag(4);
                        str1="";
                        elemList.add(de);
                    }
                }
            }

        }
    }

    public void updateDocElement(String id,DocElement de,String result) {
        de.setTlText(result);
        de.setId(id);

        Element tlElem=new Element("div").attr("name","tsUpdate");
        //修正使用的模板
        String tpl="0";
        //修正页面翻译文本的颜色
        String txtColor="green";

        if(de.getFlag()==0 || de.getFlag()==3) {

            de.setModelElem(
                    new Element("span").attr("class", "tscolor").attr("lg_zh",id).append(MD_TRANS.replace("{dataId}", id))
            );
        }else if(de.getFlag()==1) {
            //标题，用红色文本
            txtColor="orange";

            de.setModelElem(
                    new Element("span").attr("class", "tscolor").attr("lg_zh",id).append(MD_TRANS.replace("{dataId}", id))
            );
        }else if(de.getFlag()==2) {
            // 双斜杠的注释
            tlElem.attr("class", "codeline");
            tpl="1";

            de.setModelElem(
                    new Element("div").attr("class", "codeline").attr("lg_zh",id).append(MD_TRANS_1.replace("{dataId}", id))
            );
//        }else if(de.getFlag()==3) {
//            //跟在代码后面的双斜杠注释
//            tlElem.attr("class", "codeline");
//            tpl="1";
//
//            de.setModelElem(
//                    new Element("i").attr("class", "tscolor").attr("lg_zh",id).append(MD_TRANS.replace("{dataId}", id))
//            );
        }else if(de.getFlag()==4) {
            // /***/形式的注释
            tlElem.attr("class", "codeline");
            tpl="1";

            de.setModelElem(
                    new Element("div").attr("class", "codeline").attr("lg_zh",id).append(MD_TRANS_1.replace("{dataId}", id))
            );
        }

        //包含：dataId(数据id),dataTpl(模板索引[0：通用模板，1：poi代码块模板]),dataTxtColor(翻译文本的颜色[如：rgb(255,255,255)])
        tlElem.attr("data-id", id).attr("data-tpl", tpl).attr("data-txt-color", txtColor);
        de.setTlElem(tlElem);

    }


    public void dealTlElem(){
        for(DocElement de:elemList) {
            if(de.getFlag()==4) {
                de.getElem().after(de.getTlElem());
            }else if(de.getFlag()==2) {
                de.getElem().before(de.getTlElem());
//            }else if(de.getFlag()==3) {
//                de.getElem().parent().after(de.getTlElem());
            }else {
                de.getElem().appendChild(de.getTlElem());
            }
        }
    }

    public void dealModelElem(){
        for(DocElement de:elemList) {
            if(de.getFlag()==4) {
                de.getElem().after(de.getModelElem());
            }else if(de.getFlag()==2) {
                de.getElem().before(de.getModelElem());
//            }else if(de.getFlag()==3) {
//                de.getElem().appendChild(de.getModelElem());
            }else {
                String html=de.getElem().html();
                de.getElem().html("<span lg_en=\""+de.getId()+"\">"+html+"</span>");
                de.getElem().appendChild(de.getModelElem());
            }
        }
    }

    /** 生成离线文件的模板 **/
    private final static String MD_TRANS_1="       <span class=\"lineno\"></span>\r\n" +
            "	   <span class=\"codebody\" style=\"color:green;\">XYM_TS{{dataId}}</span>";

    @Override
    public String startTranslatePage() {
        return "program/common/baseHtmlTranslate.html";
    }

    @Override
    public String updateTranslatePage() {
        return "../files/data/{uuid}/update/index.html";
    }

    @Override
    public String createLatestPage() {
        return "program/common/baseHtmlLatest.html";
    }

    @Override
    public String viewLatestPage(){
        return "../files/data/{uuid}/latest/index.html";
    }

    public static void main(String[] args) {

    }
}

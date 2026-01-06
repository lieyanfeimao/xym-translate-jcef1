package com.xuanyimao.translate.core.translateprogram.program;


import com.xuanyimao.translate.anno.TranslateProgramClass;
import com.xuanyimao.translate.core.translateprogram.component.HtmlDocTranslateProgram;
import com.xuanyimao.translate.entity.htmldoc.DocElement;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * API文档文件解析
 * @author liuming
 *
 */
@TranslateProgramClass(name="apiDoc",desc="API文档通用翻译程序",order=0)
public class ApiDocTranslateProgram extends HtmlDocTranslateProgram {

	//jquery文件路径，新版已包含，所以设置为空
//    public String jqueryJsFile="";
	
	public void parseData(String data) {
		elemList=new ArrayList<DocElement>();
        doc=Jsoup.parse(data,configData.getFileEncode());
		//一些文档略有差别
		//旧版
		parseDataOld();
		//新版
		parseDataNew();

	}

	private void parseDataNew(){
		Element elem1 = doc.selectFirst("section.class-description > div.block");
		//类说明
		if(elem1!=null) {
			//将<code>和textNode组合为一个<p>标签
			List<Node> childNodes = elem1.childNodes();
			StringBuffer sbuf=new StringBuffer("");
			boolean flag=false;
			for(Node n:childNodes) {
				if("code".equals(n.nodeName()) || "a".equals(n.nodeName()) || ("#text".equals(n.nodeName()) && StringUtils.isNotBlank(n.toString())) ) {
					sbuf.append(n.toString());
					flag=false;
					n.remove();
				}else {
					if(!flag) {
						n.before("<p>"+sbuf.toString()+"</p>");
						sbuf.delete(0,sbuf.length());
						flag=true;
					}
				}
			}
			if(!flag) {
				elem1.append("<p>"+sbuf.toString()+"</p>");
			}
			sbuf=null;

			Elements elems = elem1.select("p");
			commonDealElement(elems);

			elems = elem1.select("ul > li");
			commonDealElement(elems);

		}

		elem1 = doc.selectFirst("section.summary");
		//属性，方法等简要描述
		if(elem1!=null) {
			Elements elems = elem1.select("div.col-last");
			if(elems!=null) {
				for(Element em:elems) {
					if(em.hasClass("table-header")) {
						continue;
					}
					elemList.add(createDocElement(em,em.text()));
				}
			}
		}

		//详细描述
		elem1 = doc.selectFirst("section.details");
		//属性，方法等简要描述
		if(elem1!=null) {
			Elements elems = elem1.select("div.block");
			commonDealElement(elems);

			elems = elem1.select("dl.notes > dd");
			if(elems!=null) {
				for(Element em:elems) {
					if(em.selectFirst("ul")==null && StringUtils.isNotBlank(em.text()) ) {
//        				System.out.println(em.text());
						elemList.add(createDocElement(em,em.text()));
					}
				}
			}
		}
	}


	/**
	 * jcef 稍旧的文档
	 */
	private void parseDataOld(){
		Element elem1 = doc.selectFirst("div.description > ul.blockList > li.blockList > div.block");
		//类说明
		if(elem1!=null) {
			String str=elem1.text();
			DocElement de=new DocElement();
			de.setElem(elem1);
			de.setText(str);
			elemList.add(de);
		}
		//所有方法
		Elements elems = doc.select("div.summary > ul.blockList > li.blockList > ul.blockList > li.blockList > table > tbody > tr");
		if(elems!=null) {
			for(Element em:elems) {
				Element em1=em.selectFirst("td > div.block");
				if(em1!=null) {
					String str=em1.text();
					if(StringUtils.isNotBlank(str) && str.indexOf("返回")==-1 && str.indexOf("，")==-1 && str.indexOf("。")==-1) {
						DocElement de=new DocElement();
						de.setElem(em1);
						de.setText(str);
						elemList.add(de);
					}
				}
			}

		}
		//方法详解
		elems = doc.select("div.details > ul.blockList > li.blockList > ul.blockList > li.blockList > ul");
		if(elems!=null) {
			for(Element em:elems) {
				//获取解释
				Element em1=em.select("li.blockList > div.block").last();
				if(em1!=null) {
					String str=em1.text();
					if(StringUtils.isNotBlank(str) && str.indexOf("返回")==-1 && str.indexOf("，")==-1 && str.indexOf("。")==-1) {
						DocElement de=new DocElement();
						de.setElem(em1);
						de.setText(str);
						elemList.add(de);
					}
				}


				em1=em.selectFirst("li.blockList > dl");
				if(em1!=null) {
					boolean flag=false;
					for(Element pem:em1.children()) {
						if("dt".equals(pem.tagName())) {
							if(pem.text().startsWith("Parameters")
//									|| pem.text().startsWith("Specified")
									|| pem.text().startsWith("API")
									|| pem.text().startsWith("Returns")
									|| pem.text().startsWith("Throws")
									|| pem.text().startsWith("参数")
									|| pem.text().startsWith("返回") && pem.text().indexOf("，")==-1 && pem.text().indexOf("。")==-1) {
								flag=true;
							}else {
								flag=false;
							}
						}else {
							if(flag) {
								String str=pem.text();
								if(StringUtils.isNotBlank(str) && str.indexOf("返回")==-1 && str.indexOf("，")==-1 && str.indexOf("。")==-1) {
									DocElement de=new DocElement();
									de.setElem(pem);
									de.setText(str);
									elemList.add(de);
								}
							}
						}
					}
				}
			}
		}
	}
	

	
//	public static void main(String[] args) throws Exception {
//		File file=new File("C:\\test\\ffmpeg.html");
//		FileInputStream fis=new FileInputStream(file);
//		String data=IOUtils.toString(fis,"utf-8");
//		IOUtils.closeQuietly(fis);
//		new ApiDocTranslateProgram().parseData(data);
//
//	}

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
		return "program/apiDoc/latest.html";
	}

	@Override
	public String viewLatestPage(){
		return "../files/data/{uuid}/latest/index.html";
	}

}

package com.xuanyimao.translate.api.service.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.xuanyimao.translate.entity.htmldoc.FileInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.util.FileUtil;
import com.xuanyimao.translate.entity.translate.TranslateData;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 离线文件
 */
@JsClass
public class LatestService {

	/**
	 * 创建文件
	 * @param versionFolder 版本文件所在目录
	 * @param fileInfo  文件信息
	 * @param charset   字符编码
	 * @throws Exception
	 */
	public void createFile(String versionFolder,FileInfo fileInfo,String charset) throws Exception {
		//模板文件路径
		String modelFile=versionFolder+Constants.TRANSLATE_DATA_FOLDER_MODEL+File.separator+fileInfo.getPath();
		
		//目标文件路径
		String latestFile=versionFolder+Constants.TRANSLATE_DATA_FOLDER_LATEST+File.separator+fileInfo.getPath();
		
		//读取数据
		String dataFile=versionFolder+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+fileInfo.getId();
		List<TranslateData> list=new Gson().fromJson(FileUtil.readFile(dataFile), new TypeToken<List<TranslateData>>(){}.getType());
		Map<String, Object> datas=new HashMap<String, Object>();
		if(list!=null && !list.isEmpty()) {
			for(TranslateData td:list) {
				datas.put(td.getId(), td.getTlText()==null?"":td.getTlText());
			}
		}
		
		int i1=modelFile.lastIndexOf(File.separator);
		String modelFolder=modelFile.substring(0, i1);
		String fileName=modelFile.substring(i1+1);
		
//		System.out.println(modelFolder);
//		System.out.println(fileName);
		
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
		cfg.setDirectoryForTemplateLoading(new File(modelFolder));
		cfg.setDefaultEncoding(charset);
		
		Template template = cfg.getTemplate(fileName);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(latestFile), charset));
		template.process(datas, out);		
	}

	/**
	 * 获取已处理的离线文件数据
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryLatestFiles(List<FileInfo> fileList) throws Exception{
		List < Map < String, Object > > treeList=new ArrayList<Map < String, Object >>();

		Map < String,Object > node=createNodeMap("0","文件列表",null,0);
		node.put("open",true);
		node.put("attributes", createAttributesMap("") );

		treeList.add(node);

		treeList.addAll(dealFileList(fileList));
		return treeList;
	}


	/**
	 * 处理文件列表
	 * @param fileList
	 * @return
	 */
	public List<Map<String, Object>> dealFileList(List<FileInfo> fileList){
		Map<String, Object> maps=new HashMap<String, Object>();
		
		for(FileInfo tf:fileList) {
			String[] paths=tf.getPath().split("\\"+File.separator);
			dealPaths(paths, 0, tf.getId(), maps);
		}
//		System.out.println(maps);
		//将map转换成list<map>
		return mapToList(maps,"0");
	}

	private final static String TYPE_KEY_NAME = "xym_data_type";
	private final static String FID_KEY_NAME = "xym_data_fid";

	/**
	 * 根据路径生成Map形式文件树列表
	 * @param paths
	 * @param index
	 * @param id
	 * @param maps
	 */
	public void dealPaths(String[] paths,int index,String id,Map<String, Object> maps){
		Map<String, Object> map=(Map<String, Object>)maps.get(paths[index]);
		if(map==null) {
			map=new TreeMap<String, Object>();
			if(index==paths.length-1) {//文件
				map.put(TYPE_KEY_NAME, 1);
				map.put(FID_KEY_NAME, id);
			}else {//目录
				map.put(TYPE_KEY_NAME, 0);
				map.put(FID_KEY_NAME, "");
			}
			maps.put(paths[index], map);
		}
		
		if(index<paths.length-1) {
			dealPaths(paths, index+1, id, map);
		}
	}

	/**
	 * 将map转换成ztree组件的数据格式
	 * @param data
	 * @return
	 */
	public List<Map<String, Object>> mapToList(Map<String, Object> data,String pid) {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		for(Map.Entry<String, Object> et:data.entrySet()) {
			if(TYPE_KEY_NAME.equals(et.getKey()) || FID_KEY_NAME.equals(et.getKey())) {
				continue;
			}
			Map<String, Object> map=(Map<String, Object>)et.getValue();
			Integer type=(Integer)map.get(TYPE_KEY_NAME);
			if(type.intValue()==0) {//目录
				Map<String, Object> folderMap=createTree("", et.getKey(),pid,0);
				list.add(folderMap);

				list.addAll(mapToList(map,folderMap.get("id").toString()));
			}else {//文件
				list.add(createTree((String)map.get(FID_KEY_NAME), et.getKey(),pid,1));
			}
		}
		return list;
	}

	/**
	 * 创建树节点数据
	 * @param fid 文件id
	 * @param name  节点名
	 * @param pid   节点父id
	 * @param order  排序序号
	 * @return
	 */
	public Map < String, Object > createTree(String fid,String name,String pid,Integer order) {
		Map < String, Object > node=createNodeMap(UUID.randomUUID().toString().replace("-", ""),name,pid,order);
		node.put("attributes", createAttributesMap(fid) );
		return node;
  }


	/**
	 * 创建节点map
	 * @param id 数据id
	 * @param name 名称
	 * @param pId  父节点id
	 * @param order  序号
	 * @return
	 */
	private Map<String,Object> createNodeMap(String id,String name,String pId,Integer order){
		Map < String,Object > node=new HashMap < String, Object >();
		node.put("id",id);
		node.put("name", name);
		node.put("pId", pId);
		node.put("order", order);
		return node;
	}

	/**
	 * 创建属性map
	 * @param fid
	 * @return
	 */
	private Map<String,Object> createAttributesMap(String fid){
		Map < String,Object > data=new HashMap < String, Object >();
		data.put("fid",fid);
		return data;
	}

}

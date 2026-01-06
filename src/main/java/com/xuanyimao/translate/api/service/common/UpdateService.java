package com.xuanyimao.translate.api.service.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.common.Constants;
import com.xuanyimao.translate.entity.htmldoc.FileInfo;
import com.xuanyimao.translate.entity.htmldoc.TranslateReplaceDataFile;
import com.xuanyimao.translate.entity.htmldoc.TranslateReplaceDataItem;
import com.xuanyimao.translate.entity.translate.TranslateData;
import com.xuanyimao.translate.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@JsClass
public class UpdateService {

	public void queryReplaceData(String versionFolder, FileInfo tf, String dataId, String content, List<TranslateReplaceDataFile> trdfList) {

		//查找相同的数据
		String filePath= versionFolder+Constants.TRANSLATE_DATA_DATAS_FOLDER_NAME+File.separator+tf.getId();
		File file=new File(filePath);
		if(!file.exists()) {
			return;
		}
		List<TranslateData> tdList=new Gson().fromJson(FileUtil.readFile(file), new TypeToken<List<TranslateData>>(){}.getType());
		
		TranslateReplaceDataFile trdf=null;
		List<TranslateReplaceDataItem> trdiList=null;
		if(tdList!=null) {
			for(TranslateData td:tdList) {
				if(content.equals(td.getOldText())) {
					if(dataId.equals(td.getId())) {
						continue;
					}
					if(trdf==null) {
						trdf=new TranslateReplaceDataFile();
						trdf.setFileId(tf.getId());
						trdf.setFilePath(tf.getPath());
						trdfList.add(trdf);
						
						trdiList=new ArrayList<TranslateReplaceDataItem>();
						trdf.setTrdiList(trdiList);
					}
					
					TranslateReplaceDataItem trdi=new TranslateReplaceDataItem();
					trdi.setFileId(tf.getId());
					trdi.setFilePath(tf.getPath());
					trdi.setDataId(td.getId());
					trdi.setTlText(td.getTlText());
					trdiList.add(trdi);
				}
			}
		}
			
	}
	
}

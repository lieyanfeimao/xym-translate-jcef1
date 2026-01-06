package com.xuanyimao.translate.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.xuanyimao.translate.anno.*;
import com.xuanyimao.translate.core.translateplatform.TranslatePlatform;
import com.xuanyimao.translate.core.translateprogram.TranslateProgram;
import com.xuanyimao.translate.entity.AppConfig;
import com.xuanyimao.translate.entity.ComboxValue;
import com.xuanyimao.translate.entity.anno.*;
import com.xuanyimao.translate.entity.project.Project;
//import com.xuanyimao.translate.entity.translate.TranslateProject;
import com.xuanyimao.translate.util.FileUtil;
import com.xuanyimao.translate.util.LogUtil;
import com.xuanyimao.translate.util.ProjectUtil;
import com.xuanyimao.translate.util.RsaUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import okhttp3.ConnectionPool;
import org.reflections.Reflections;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

/***
 * 应用数据，单例。<br>
 * 此对象中的数据长存于内存中
 * @author liuming
 *
 */
public class ApplicationData {


	/** okhttp 连接池 **/
	public static ConnectionPool connectionPool;

	/** 项目列表 */
	public static List<Project> projectList;

	/**应用程序所在目录*/
	public static String appPath;

	/** 翻译平台注解对象列表 */
	public static List<TranslatePlatformClassAO> translatePlatformClassAOList;

	/** 翻译程序注解对象列表 */
	public static List<TranslateProgramClassAO> translateProgramClassAOList;

	/** 注解数据对象 **/
	public static AnnoData annoData;

	/** 应用配置 **/
	public static AppConfig appConfig;

	/**
	 * 初始化数据
	 */
	public static void initData(){
		appPath=System.getProperty("user.dir");

		connectionPool=new ConnectionPool(2000, 10, TimeUnit.SECONDS);
		//读取项目列表
		ProjectUtil.readProjectConfigFile();
		//扫描程序列表
		scannerAnno();
		//加载app配置
		loadAppConfig(appPath);
	}

	/**
	 * 加载应用配置
	 * @author liuming
	 * @since 2023年8月18日
	 * @param appPath 应用路径
	 * @return
	 * @throws IOException 
	 */
	public static AppConfig loadAppConfig(String appPath) {
		try{
			//读取config文件，如果不存在，创建新的config文件，并赋予默认值
			String configFilePath=appPath+File.separator+Constants.CONFIG_FILENAME;
			File file=new File(configFilePath);
			if(!file.exists()) {
				createDefaultAppConfig(configFilePath);
			}else {//读取配置文件
				FileInputStream fis=new FileInputStream(file);
				String content= IOUtils.toString(fis,"UTF-8");
				IOUtils.closeQuietly(fis);
				if(StringUtils.isBlank(content)) {
					createDefaultAppConfig(configFilePath);
				}else {
					appConfig=new Gson().fromJson(content, AppConfig.class);
					if(appConfig.getBdAppId()!=null) {
						appConfig.setBdAppId(RsaUtil.decrypt(appConfig.getBdAppId()));
					}
					if(appConfig.getBdSecretKey()!=null) {
						appConfig.setBdSecretKey(RsaUtil.decrypt(appConfig.getBdSecretKey()));
					}
					if (appConfig.getTxSecretId()!=null) {
						appConfig.setTxSecretId(RsaUtil.decrypt(appConfig.getTxSecretId()));
					}
					if(appConfig.getTxSecretKey()!=null) {
						appConfig.setTxSecretKey(RsaUtil.decrypt(appConfig.getTxSecretKey()));
					}

					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					System.out.println(new Gson().toJson(appConfig));
				}
			}
		}catch (Exception e){
			LogUtil.getLogger().error("加载配置文件失败",e);
		}
		return appConfig;
	}

	/**
	 * 保存配置
	 * @param config
	 */
	public static void saveAppConfig(AppConfig config) throws Exception {
		if(config.getBdAppId()!=null) {
			appConfig.setBdAppId(config.getBdAppId());
		}
		if(config.getBdSecretKey()!=null) {
			appConfig.setBdSecretKey(config.getBdSecretKey());
		}
		if(config.getTxSecretId()!=null) {
			appConfig.setTxSecretId(config.getTxSecretId());
		}
		if(config.getTxSecretKey()!=null) {
			appConfig.setTxSecretKey(config.getTxSecretKey());
		}

		saveAppConfig();
	}

	public static void saveAppConfig() throws Exception {
		AppConfig config=appConfig.clone();

		if(config.getBdAppId()!=null) {
			config.setBdAppId(RsaUtil.encrypt(config.getBdAppId()));
		}
		if(config.getBdSecretKey()!=null) {
			config.setBdSecretKey(RsaUtil.encrypt(config.getBdSecretKey()));
		}

		if(config.getTxSecretId()!=null) {
			config.setTxSecretId(RsaUtil.encrypt(config.getTxSecretId()));
		}
		if(config.getTxSecretKey()!=null) {
			config.setTxSecretKey(RsaUtil.encrypt(config.getTxSecretKey()));
		}

		FileOutputStream fos=new FileOutputStream(new File(appPath+File.separator+Constants.CONFIG_FILENAME));
		IOUtils.write(new Gson().toJson(config), fos,"UTF-8");
		IOUtils.closeQuietly(fos);
	}
	
	/**
	 * 创建默认的应用程序配置文件
	 * @author liuming
	 * @since 2023年8月18日
	 * @param configFilePath 配置文件路径
	 * @throws IOException
	 */
	private static void createDefaultAppConfig(String configFilePath) throws IOException {
		appConfig=new AppConfig();
		
		FileOutputStream fos=new FileOutputStream(new File(configFilePath));
		IOUtils.write(new Gson().toJson(appConfig), fos,"UTF-8");
		IOUtils.closeQuietly(fos);
	}
	

	
	/**
	 * logo图片路径
	 * @author liuming
	 * @since 2023年8月18日
	 * @return
	 */
	public static String logoImgPath() {
		return appPath+File.separator+Constants.VIEW_FOLDER+File.separator+Constants.LOGO_FILE_NAME;
	}
	
	/**
	 * 首页文件路径
	 * @author liuming
	 * @since 2023年8月18日
	 * @return
	 */
	public static String indexPath() {
		return appPath+File.separator+Constants.VIEW_FOLDER+File.separator+Constants.UI_INDEX_PAGE;
	}

	/**
	 * 扫描注解
	 */
	public static void scannerAnno(){
		Reflections ref = new Reflections("com.xuanyimao.translate");

		//翻译程序 ，如 API文档翻译程序
		translateProgramClassAOList=new ArrayList<TranslateProgramClassAO>();
		Set<Class<?>> set = ref.getTypesAnnotatedWith(TranslateProgramClass.class);
		for (Class<?> c : set) {
			try {
				TranslateProgramClass a = c.getAnnotation(TranslateProgramClass.class);
				if (a != null) {
					LogUtil.getLogger().info("扫描到的翻译程序：{}",c);
					TranslateProgram translateProgram=(TranslateProgram)c.getDeclaredConstructor().newInstance();
					TranslateProgramClassAO ao = new TranslateProgramClassAO(a.name(), a.desc(), a.order(), c,translateProgram);

					translateProgramClassAOList.add(ao);
				}
			}catch (Exception e){
				LogUtil.getLogger().error("翻译程序加载失败:{} -> {}",c,e.getMessage(),e);
			}
		}
		Collections.sort(translateProgramClassAOList);

		//翻译平台，如 百度，腾讯
		translatePlatformClassAOList=new ArrayList<TranslatePlatformClassAO>();
		set = ref.getTypesAnnotatedWith(TranslatePlatformClass.class);
		for (Class<?> c : set) {
			try {
				TranslatePlatformClass a = c.getAnnotation(TranslatePlatformClass.class);
				if (a != null) {
					LogUtil.getLogger().info("扫描到的翻译平台：{}",c);

					TranslatePlatform translatePlatform = (TranslatePlatform) c.getDeclaredConstructor().newInstance();
					TranslatePlatformClassAO ao = new TranslatePlatformClassAO(a.name(), a.desc(), a.order(), translatePlatform);
					translatePlatformClassAOList.add(ao);
				}
			}catch (Exception e){
				LogUtil.getLogger().error("翻译平台加载失败:{} -> {}",c,e.getMessage(),e);
			}
		}
		Collections.sort(translatePlatformClassAOList);


		//JS类
		annoData=new AnnoData();
		List<JsClassAO> jsClassAOList=new ArrayList<JsClassAO>();
		set = ref.getTypesAnnotatedWith(JsClass.class);
		for (Class<?> c : set) {
			try{
				JsClass jsClass=c.getAnnotation(JsClass.class);
				String className=jsClass.name();
				if(StringUtils.isBlank(className)) {
					className=c.getSimpleName();
					className=className.substring(0,1).toLowerCase()+className.substring(1);
				}
				String prefix=jsClass.prefix();
				if(StringUtils.isBlank(prefix)) {
					prefix=className;
				}

				JsClassAO jsClassAO=new JsClassAO(c, c.getDeclaredConstructor().newInstance(), className,c.getName(),prefix);
				jsClassAOList.add(jsClassAO);
			}catch (Exception e){
				LogUtil.getLogger().error("解析 {} 的 @JsClass 注解失败",c,e.getMessage(),e);
			}
		}
		annoData.setAnnoClassList(jsClassAOList);

		Map<String, JsFunctionAO> methodMap=new HashMap<String, JsFunctionAO>();
		//注入对象和解析方法
		for (JsClassAO jsClassAO : jsClassAOList) {
			Class c=jsClassAO.getCls();

			System.out.println("处理的类："+c);

			List<Field> fieldList=new ArrayList<>();
			fieldList.addAll(Arrays.asList(c.getDeclaredFields()));
			//获取继承的父类字段
			fieldList.addAll(findParentFields(c.getSuperclass()));

			if(!fieldList.isEmpty()) {
				for(Field field:fieldList) {
					System.out.println("处理的字段："+field);

					field.setAccessible(true);
					try{
						JsObject jsObject=field.getAnnotation(JsObject.class);
						if(jsObject!=null) {
//							System.out.println(field.getGenericType().getTypeName());
							//为属性赋值，以后根据需要做优化
							for(JsClassAO ac: jsClassAOList) {
								if(field.getGenericType().getTypeName().equals(ac.getClsName())) {//如果与列表的类名一致
//									System.out.println("对象注入："+ac.getClsName());

									field.set(jsClassAO.getObj(), ac.getObj());
									break;
								}
							}
						}
					}catch (Exception e){
						LogUtil.getLogger().error("解析 {} 的字段 {} 的 @JsObject 注解失败",c,field.getName(),e.getMessage(),e);
					}
				}
			}

			Method[] methods=c.getMethods();
			if(methods.length>0) {
				for(Method method:methods) {
					method.setAccessible(true);
					try{
						JsFunction jsFunction=method.getAnnotation(JsFunction.class);
						if(jsFunction!=null) {//方法含有jsFunction注解
							JsFunctionAO jsFunctionAO =new JsFunctionAO(method, jsClassAO);
							//获取方法的所有参数
							Class<?>[] paramClass=method.getParameterTypes();
							if(paramClass.length>0) {//存在参数
								List<JsFunctionParam> paramList=new ArrayList<JsFunctionParam>();
								//使用spring LocalVariableTableParameterNameDiscoverer获取参数名
								ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
								String[] pn=parameterNameDiscoverer.getParameterNames(method);
								for(int j=0;j<paramClass.length;j++) {
//									System.out.println(paramClass[j]+"...."+pn[j]);
									paramList.add(new JsFunctionParam(paramClass[j], pn[j]));
								}
								jsFunctionAO.setMethodParam(paramList);
							}

							String methodName=jsFunction.name();
							if(StringUtils.isBlank(methodName)) {
								methodName=method.getName();
							}
							String funcName=(StringUtils.isNotBlank(jsClassAO.getPrefix())? jsClassAO.getPrefix()+".":"") + methodName;
//							System.out.println("扫描到的JS函数："+funcName);
							jsFunctionAO.setDesc(jsFunction.desc());
							methodMap.put(funcName, jsFunctionAO);

						}
					} catch (Exception e) {
						LogUtil.getLogger().error("解析 {} 的方法 {} 的 @JsFunction 注解失败",c,method.getName(),e.getMessage(),e);
					}
				}
			}
			annoData.setMethodMap(methodMap);

		}


	}

	/**
	 * 查找父类的所有字段
	 * @param c
	 * @return
	 */
	private static List<Field> findParentFields(Class c) {
		List<Field> fieldList=new ArrayList<>();
		while (c!=null) {
			fieldList.addAll(Arrays.asList(c.getDeclaredFields()));
			c=c.getSuperclass();
		}
		return fieldList;
	}

	/**
	 * 获取@JsClass注解的实体对象
	 * @param name
	 * @return
	 */
	public static Object getJsClassInstance(String name) {
		List<JsClassAO> jsClassAOList = annoData.getAnnoClassList();
		if(jsClassAOList !=null && !jsClassAOList.isEmpty()) {
			for(JsClassAO ac: jsClassAOList) {
				if(name.equals(ac.getName())) {
					return ac.getObj();
				}
			}
		}
		return null;
	}
}

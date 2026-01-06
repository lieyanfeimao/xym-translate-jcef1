//定时器是否启动
var timeFlag=false;


/**
 * 选择词典文件
 */
function chooseDict(){
	commonApi.fileDialog(1,"选择词典文件").then(function (r){
		if(r.length>0){
			let str="";
			for(let i=0;i<r.length;i++){
				str+=r[i]+";";
			}
			$("#dictPath").val(str);
		}
	});

}

/**
 * 更新选择的文件
 */
function updateCheckFile(){
	let nodes=zTreeFile.getCheckedNodes(true);

	let len=0;
	let rgx=$("#rgx").val();
	let str='';
	for(let i=0;i<nodes.length;i++){
		if(rgx!=""){
			if(!nodes[i].name.match(rgx)){
				continue;
			}
		}
		
		let att=nodes[i].attributes;
		if(att.type==0){
			if(rgx!=""){
				if(!att.path.match(rgx)){
					continue;
				}
			}
			str+='<div class="select-file" title="'+att.path+'">'+att.path+'</div>';
			len++;
		}
	}

	$("#selFileList").html(str);
	$("#fileInfo").html("当前选择了 <span style='color:green;'>"+len+"</span> 个文件");
}

/**
 * 日志信息，由翻译程序调用
 * @param info
 */
function changeLog(info){
	info=decodeURIComponent(info);
	let data=JSON.parse(info);
	if(data.status==0){
		$("#errorLog").append("<div>"+data.file+":"+data.errorInfo+"</div>");
	}else if(data.status==1){//
		let info=data.file+":共需翻译<span style='color:red;'>"+data.total+"</span>条,已翻译<span style='color:red;'>"+data.dealCount+"</span>条,剩余<span style='color:red;'>"+(data.total-data.dealCount)+"</span>条";
		if($("#"+data.id).length>0){
			$("#"+data.id).html(info);
		}else{
			$("#logInfo").append("<div id='"+data.id+"'>"+info+"</div>");
		}
	}else if(data.status==2){//已完成
		$("#"+data.id).html(data.file+":翻译完成");
		setTimeout(function(){
			$("#"+data.id).remove();
		},1500);
	}
	
}
//队列信息
async function queueInfo(){
	if(!timeFlag){
		$("#queueInfo").html("");
		return;
	}

	let r=await baseHtmlApi.queueInfo({browserId:browserId});
	if(r.ok){
		let data=r.data;
		if(data!=null){

			if(data.total>data.completedCount){
				$("#queueInfo").html("共需翻译<span style='color:red;'>"+data.total+"</span>个文件,已完成<span style='color:red;'>"+data.completedCount+"</span>个,剩余<span style='color:red;'>"+(data.total-data.completedCount)+"</span>个");
				setTimeout("queueInfo()",1000);
			}else{
				$("#queueInfo").html("翻译已完成，共翻译了<span style='color:red;'>"+data.total+"</span>个文件");
				$("#startButton").show();
				$("#stopButton").hide();
			}
		}
	}else{
		$("#queueInfo").html(r.msg);
	}

}

//选择一个版本
function chooseVersion(){
	layIndex=layer.open({
		type: 1,
		title:"选择现有版本做词库",
		skin: 'layui-layer-rim', //加上边框
		area: ['700px', '600px'], //宽高
		content:$("#tplVersionList"),
		success: function(lay, index){
			layform.render();
		}
	});
}

/**
 * 更新选中的版本列表
 */
function updateCheckVersionList(){
	checkVersionIds=new Array();
	let nodes=zTreeVersion.getCheckedNodes(true);
	let str="";
	for(let i=0;i<nodes.length;i++){
		let att=nodes[i].attributes;
		if(att.type==1){
			checkVersionIds[checkVersionIds.length]=nodes[i].id;

			//获取完整的路径
			let parent=nodes[i].getParentNode();
			let path=nodes[i].name;
			while (parent!=null){
				if(parent.id!=0){
					path=parent.name+"-"+path;
				}
				parent=parent.getParentNode();
			}

			str+=path+" ; ";
		}
	}

	$("#versionList").html(str);
}

/**
 * 获取配置用的参数
 * @returns {{projectId: string, versionId: string, platformName: (*|jQuery), rgx: (jQuery|*), dictPath: (jQuery|*), srcLanguage: (*|jQuery), destLanguage: (*|jQuery), fileEncode: (jQuery|*), translateCount: (jQuery|*), checkVersionIds}}
 */
function getConfigParams(){
	return {
		projectId:projectId,
		versionId:versionId,
		platformName:$("#platformName").val(),
		rgx:$("#rgx").val(),
		dictPath: $("#dictPath").val(),
		srcLanguage: $("#srcLanguage").val(),
		destLanguage: $("#destLanguage").val(),
		fileEncode: $("#fileEncode").val(),
		translateCount: $("#translateCount").val(),
		checkVersionIds:checkVersionIds
	};
}

/**
 * 保存配置
 */
function saveConfig(){
	let params=getConfigParams();
	baseHtmlApi.saveConfig(params)
		.then(function (r){
			tipsMsg(r.msg);
		});
}


//启动翻译
async function startTranslate(){
	let nodes=zTreeFile.getCheckedNodes(true);
	let files=new Array();
	let rgx=$("#rgx").val();

	for(let i=0;i<nodes.length;i++){
		if(rgx!=""){
			if(!nodes[i].name.match(rgx)){
				continue;
			}
		}

		let att=nodes[i].attributes;
		if(att.type==0){
			if(rgx!=""){
				if(!att.path.match(rgx)){
					continue;
				}
			}
			files[files.length]=att.path;
		}
	}
	$("#errorLog").html("");
	$("#logInfo").html("");
	$("#queueInfo").html("");

	let params=getConfigParams();
	params["files"]=files;

	let r=await baseHtmlApi.saveConfig(params);
	if(!r.ok){
		tipsMsg(r.msg);
		return;
	}

	params["browserId"]=browserId;

	r=await baseHtmlApi.startTranslate(params);
	if(r.ok){
		timeFlag=true;
		queueInfo();

		$("#startButton").hide();
		$("#stopButton").show();
	}else{
		infoMsg(r.msg);
	}
}
//停止翻译
async function stopTranslate(){
	let params={browserId:browserId};
	let r=await baseHtmlApi.stopTranslate(params);
	if(r.ok){
		timeFlag=false;

		$("#logInfo").html("翻译已手动停止!");
		$("#startButton").show();
		$("#stopButton").hide();
	}else{
		infoMsg(r.msg);
	}
}



/**
 * 初始化视图和控件
 * @returns {Promise<void>}
 */
async function initView(){
	let r=await commonApi.queryTranslatePlatformList();
	if(r.ok){
		createSelectView("#platformName",r.data);
	}
	//获取配置信息
	r=await baseHtmlApi.config(projectId,versionId);
	if(r.ok){
		versionFolder=r.data.versionFolder;

		fs=r.data.fs;
		$("[name='versionFolder']").html(versionFolder);
		if(r.data.config==null){//初始化
			openInitProgramDialog();
		}else{
			await initControlView(r.data.config);
		}
	}else{
		tipsMsg(r.msg,1);
	}

}

/**
 * 初始化控件视图
 * @param data
 */
async function initControlView(data){
	console.log(data);

	for (let key in data){
		$("#"+key).val(data[key]);
		if(key!="platformName"){
			$("#"+key).html(data[key]);
		}
	}


	if(data.platformName!="") $("#platformName").val(data.platformName);

	//初始化语言列表
	let platformName=$("#platformName").val();

	await initLanguage(platformName);

	if(data.srcLanguage!="") $("#srcLanguage").val(data.srcLanguage);
	if(data.destLanguage!="") $("#destLanguage").val(data.destLanguage);

	layform.render('select');

	//初始化树列表
	r=await baseHtmlApi.queryFileTree(projectId,versionId);
	if(r.ok){
		if(zTreeFile) zTreeFile.destroy();

		r.data.sort(function(a, b) {
			return a.order - b.order;
		});
		zTreeFile=$.fn.zTree.init($("#fileTree"),zTreeFileSetting, r.data );
	}

	//查询版本树列表
	r=await commonApi.queryVersionTree();
	if(r.ok){
		if(zTreeVersion) zTreeVersion.destroy();
		r.data.sort(function(a, b) {
			return a.order - b.order;
		});
		console.log(r.data);
		zTreeVersion=$.fn.zTree.init($("#versionTree"),zTreeVersionSetting, r.data );

		//初始化选中的版本树列表
		if(typeof data.checkVersionIds != "undefined" && data.checkVersionIds!=null){
			for(let i=0;i<data.checkVersionIds.length;i++){
				let node=zTreeVersion.getNodeByParam("id", data.checkVersionIds[i], null);
				if(node!=null) zTreeVersion.checkNode(node,true,true);
			}
			updateCheckVersionList();
		}
	}

}

/**
 * 初始化语言选择框
 * @param platformName
 * @returns {Promise<void>}
 */
async function initLanguage(platformName){
	let r=await commonApi.queryTranslateLanguageList(platformName);
	if(r.ok){
		createSelectView("#srcLanguage",r.data);
		createSelectView("#destLanguage",r.data);
	}
}

/**
 * 打开初始化程序对话框
 */
function openInitProgramDialog(){
	layIndex=layer.open({
		type: 1,
		title:"初始化",
		skin: 'layui-layer-rim', //加上边框
		area: ['800px', '500px'], //宽高
		content:$("#tplInitProgram"),
		success: function(lay, index){
			layform.render();
		}
	});
}

/**
 * 初始化程序
 */
function initProgram(){
	let folderPath=$("#folderPath").val();
	showLoading();
	baseHtmlApi.initData(projectId,versionId,folderPath)
		.then(function (r){
			tipsMsg(r.msg);
			if(r.ok){
				initControlView(r.data);
				closeDialog();
			}
		});
}


/**
 * 打开项目版本 工作目录
 */
function openVersionFolder(){
	commonApi.openExplorer(versionFolder);
}

/**
 * 选择文件目录
 */
function chooseFileFolder(){
	commonApi.fileDialog(3,"选择需要翻译的文件根目录")
		.then(function (r){
			if(r.length>0){
				$("#folderPath").val(r[0]);
			}
		});
}

/**
 * 创建select 视图
 * @param elem 元素
 * @param data 数据
 * @param selectValue 选中的值
 */
function createSelectView(elem,data,selectValue){
	let str='';
	for (let obj of data){
		str+='<option value="'+obj.value+'" ';
		if(obj.value==selectValue){
			str+=' selected';
		}
		str+='>'+obj.text+'</option>';
	}

	$(elem).html(str);
	layform.render();
}
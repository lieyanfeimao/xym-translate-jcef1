var lgTpl=new Array();
//通用
lgTpl[0]=[
	'<span id="{{d.id}}_input" style="display:none;">',
	'<textarea id="{{d.id}}" style="width:100%;height:40px;"></textarea>',
	'<button class="layui-btn layui-btn-xs layui-btn-normal" style="width:30%;margin-left:15%;" onclick="updateTl(\'{{d.id}}\')">保存</button>',
	'<button class="layui-btn layui-btn-xs" style="width:30%;" onclick="hideInput(\'{{d.id}}\')">隐藏修正框</button>',
	'</span>',
	'<span id="{{d.id}}_text">',
	'<span style="color:{{d.color}};" id="{{d.id}}_span"></span>',
	'<button class="layui-btn layui-btn-xs layui-btn-danger" onclick="gotoUpdateData(\'{{d.id}}\');">修正</button> &nbsp;&nbsp;',
	'<button class="layui-btn layui-btn-xs layui-btn-normal" onclick="gotoReplaceData(\'{{d.id}}\');">替换其他位置相同译文</button>',
	'</span>',
	'&nbsp;&nbsp;<span id="vs_{{d.id}}" style="font-size:10pt;color:red;"></span>',
	'<a name="{{d.id}}_a"></a>'
].join("");

//poi 代码
lgTpl[1]=[
	'<span class="lineno"></span>',
	'<span class="codebody" style="color:{{d.color}};white-space:normal;">',
	'<span id="{{d.id}}_input" style="display:none;">',
	'<textarea id="{{d.id}}" style="width:100%;height:40px;"></textarea>',
	'<button class="layui-btn layui-btn-xs layui-btn-normal" style="width:30%;margin-left:15%;" onclick="updateTl(\'{{d.id}}\')">保存</button>',
	'<button class="layui-btn layui-btn-xs" style="width:30%;" onclick="hideInput(\'{{d.id}}\')">隐藏修正框</button>',
	'</span>',
	'<span id="{{d.id}}_text">',
	'<i style="color:{{d.color}};" id="{{d.id}}_span"></i>',
	'<button class="layui-btn layui-btn-xs layui-btn-danger" onclick="gotoUpdateData(\'{{d.id}}\');">修正</button> &nbsp;&nbsp;',
	'<button class="layui-btn layui-btn-xs layui-btn-normal" onclick="gotoReplaceData(\'{{d.id}}\');">替换其他位置相同译文</button>',
	'</span>',
	'&nbsp;&nbsp;<span id="vs_{{d.id}}" style="font-size:10pt;color:red;"></span>',
	'<a name="{{d.id}}_a"></a>',
	'</span>'
].join("");



var layIndex;
var dataId="";
$(function(){
	
	layui.use(['table','layer','laytpl'], function(){
		var laytpl=layui.laytpl;
		$("[name='tsUpdate']").each(function(){
			let id=$(this).attr("data-id");
			let tpl=0;
			try{
				tpl=parseInt($(this).attr("data-tpl"));
			}catch(e){}
			let color=$(this).attr("data-txt-color");
			if(color=="") color="green";
			//console.log(id+"   "+tpl+"  "+color);

			let $dom=$(this);
			let params={id:id,color:color};
			laytpl(lgTpl[tpl]).render(params,function (html){
				$dom.append(html);
			});

		});

		loadTlData();
	});
});

function loadTlData(){
	layIndex= layer.load(1,{
	  shade: [0.4,'#fff']
	});
	baseHtmlApi.queryDataByFileId(projectId,versionId,fileId)
	.then(function(r){
		layer.close(layIndex);
		if(r.ok){
			let data=JSON.parse(r.data);
			for(var i=0;i<data.length;i++){
				$("#"+data[i].id).val(data[i].tlText);
				$("#"+data[i].id+"_span").html(data[i].tlText);
				$("#vs_"+data[i].id).html("[版本:"+data[i].version+"]");
			}
		}else{
			layer.msg(r.msg);
		}
	});
}

function updateTl(id){
	layIndex= layer.load(1,{
	  shade: [0.4,'#fff']
	});
	baseHtmlApi.updateTranslateData(projectId,versionId,fileId,id,$("#"+id).val())
	.then(function(r){
		layer.close(layIndex);
		if(r.ok){
			let data=r.data;
			$("#vs_"+id).html("版本号:"+data.version);
			$("#"+id).val(data.tlText);
			$("#"+id+"_span").html(data.tlText);
			layer.msg("保存成功");
		}else{
			layer.msg(r.msg);
		}
	});
}

function gotoUpdateData(id){
	$("#"+id+"_text").hide();
	$("#"+id+"_input").show();
}
function hideInput(id){
	$("#"+id+"_text").show();
	$("#"+id+"_input").hide();
}

function gotoReplaceData(id){
	//查询其他位置内容一模一样的翻译内容
	layIndex= layer.load(1,{
	  shade: [0.4,'#fff']
	});

	baseHtmlApi.queryReplaceDataList(projectId,versionId,fileId,id)
	.then(function(r){
		layer.close(layIndex);
		console.log(JSON.stringify(r));
		if(r.ok){
			dataId=id;
			let data=r.data;
			let $str='';
			if(data.trdFileList==null || data.trdFileList.length==0){
				$str='没有查找到与原文相同的内容';
			}else{
				var d=data.trdFileList;
				for(var i=0;i<d.length;i++){
					$str+='<fieldset class="layui-elem-field" style="font-size:14px;padding:5px;"><legend style="font-size:14px;font-weight:bold;">'+d[i].filePath+'</legend>';
					let d1=d[i].trdiList;
					$str+='<div>';
					for(var j=0;j<d1.length;j++){
						$str+='&nbsp;&nbsp;<input type="checkbox" name="dataId" value="'+d1[j].fileId+'|'+d1[j].dataId+'" />&nbsp;<a target="_blank" href="'+data.pathPre+d1[j].filePath+'#'+d1[j].dataId+'_a">'+d1[j].tlText+'</a>';
					}
					$str+='</fieldset>';
				}
			}
			
			layer.open({
				type: 1,
				title:'替换其他位置相同译文',
				area: ['80%', '80%'],
				btn:['替换','关闭'],
				yes:function(index){
					
					replaceData(index);
					
				},
				btn2:function(index){
				},
				content:'<div style="padding:10px;">'+ 
						'<fieldset class="layui-elem-field" style="font-size:14px;padding:5px;"><legend style="font-size:14px;font-weight:bold;">原文</legend><div>'+data.oldText+'</div></fieldset>'+
						'<fieldset class="layui-elem-field" style="font-size:14px;padding:5px;"><legend style="font-size:14px;font-weight:bold;">译文</legend><div>'+data.tlText+'</div></fieldset>'+
						'<div style="font-size:14px;font-weight:bold;text-align:center;">-&nbsp;&nbsp;搜索到的相同的译文&nbsp;&nbsp;-</div>'+
						'<div style="font-size:14px;font-weight:bold;"><input type="checkbox" onclick="selectAll(this)"/>全选</div>'+
						'<div>'+
						$str+
						'</div>'+
						'</div>'
			});
			
		}else{
			layer.msg(r.msg);
		}
	});
}

//选择/取消选择
function selectAll(elem){
	$("input[name='dataId']").prop("checked",$(elem).is(":checked"));
}

//替换数据
function replaceData(index){
	var datas=new Array();
	$('input[name="dataId"]:checked').each(function(){    
		datas.push($(this).val());    
	});

	layIndex= layer.load(1,{
	  shade: [0.4,'#fff']
	});

	baseHtmlApi.replaceData(projectId,versionId,fileId,dataId,datas)
	.then(function(r){
		layer.close(layIndex);
		if(r.ok){
			layer.msg(r.msg);
			layer.close(index);
			window.location.reload();
		}
		layer.msg(r.msg);
	});
}

/**
 * API交互对象
 * @type {{config: (function(*, *): Promise<unknown>)}}
 */
var baseHtmlApi={
	/**
	 * 根据文件ID获取翻译数据
	 * @param projectId 项目id
	 * @param versionId 版本id
	 * @param fileId 文件id
	 * @returns {Promise<unknown>}
	 */
	queryDataByFileId:function(projectId,versionId,fileId){
		return execJava("baseHtml.queryDataByFileId", {projectId:projectId,versionId:versionId,fileId:fileId});
	},
	/**
	 * 更新翻译文本
	 * @param projectId 项目id
	 * @param versionId 版本id
	 * @param fileId    文件id
	 * @param dataId    数据id
	 * @param tlText    翻译文本
	 * @returns {Promise<unknown>}
	 */
	updateTranslateData:function (projectId,versionId,fileId,dataId,tlText){
		return execJava("baseHtml.updateTranslateData", {projectId:projectId,versionId:versionId,fileId:fileId,dataId:dataId,tlText:tlText});
	},
	/**
	 * 查询所有需要替换的译文列表
	 * @param projectId 项目id
	 * @param versionId 版本id
	 * @param fileId    文件id
	 * @param dataId    数据id
	 * @returns {Promise<unknown>}
	 */
	queryReplaceDataList:function (projectId,versionId,fileId,dataId){
		return execJava("baseHtml.queryReplaceDataList", {projectId:projectId,versionId:versionId,fileId:fileId,dataId:dataId});
	},
	replaceData:function (projectId,versionId,fileId,dataId,datas){
		return execJava("baseHtml.replaceData", {projectId:projectId,versionId:versionId,fileId:fileId,dataId:dataId,datas:datas});
	}
}


/**
 * 公共执行Java方法的函数。此方法返回一个js对象 {"ok":true,"msg":"操作描述","data":JAVA方法返回的数据对象}
 * name:JAVA方法名
 * params:传递的参数。传入json字符串或对象，其他无效，无参数则不传
 *
 */
function execJava(name,params){
	return new Promise(function(resolve, reject) {
		if(name==null || typeof name!=="string"){
			layer.msg("调用的Java方法名必须是有效的字符串!");
			throw new Error("调用的Java方法名必须是有效的字符串!");
		}

		let p={method:name,params:""};
		//传递的参数
		if(params && (typeof params === "object" || typeof params === "string" )){
			if(typeof params === "object") params=JSON.stringify(params);
			p.params=params;
		}

		window.java({
			request:JSON.stringify(p)
			,persistent:false
			,onSuccess:function(response){
				// console.log(response);
				resolve(JSON.parse(response));
			}
			,onFailure:function(code,response){

				if(code==-1){//系统发送过来的错误信息
					let data=JSON.parse(response);
					layer.msg(data.msg.replace(/(\r\n)|(\n)/g,'<br/>'));
				}
				layer.close(layIndex);

				throw new Error("系统错误!"+code);
			}
		});
	});
}

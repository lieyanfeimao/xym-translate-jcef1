/************* 项目相关 ************/
/**
 * 添加项目
 */
function addProject(e){
	let data={id:"",text:"",order:0,parentId:projectTreeSelectId};
	openEditProjectDialog('添加项目',data);

	let even = window.event || arguments.callee.caller.arguments[0];
	even.preventDefault();
	even.stopPropagation();
}

/**
 * 编辑项目
 */
function editProject(){
	projectApi.queryById(projectTreeSelectId).then(function (r){
		if (!r.ok){
			tipsMsg(r.msg);
			return;
		}
		openEditProjectDialog('编辑项目',r.data);
	});

	let even = window.event || arguments.callee.caller.arguments[0];
	even.preventDefault();
	even.stopPropagation();
}

/**
 * 打开编辑项目的对话框
 */
function openEditProjectDialog(title,data){
	if(zTreeParent) zTreeParent.destroy();

	projectApi.queryList().then(function (r){

		zTreeParent=$.fn.zTree.init($("#parentTree"), {
			data: {
				simpleData: {
					enable: true
				}
			},callback: {
				onClick: function (event,treeId, treeNode) {
					if(parentTreeSelectId==treeNode.id){
						parentTreeSelectId=-1;
						zTreeParent.cancelSelectedNode(treeNode);
					}else{
						parentTreeSelectId=treeNode.id;
					}
				}
			}
		}, dataToZtree(r.data));


		if(data.parentId!=null && data.parentId!=-1){
			let node = zTreeParent.getNodeByParam("id", data.parentId, null); // 获取特定ID的节点
			if (node) {
				zTreeParent.selectNode(node); // 选中节点
			}
		}
	});

	layIndex=layer.open({
		type: 1,
		title:title,
		skin: 'layui-layer-rim', //加上边框
		area: ['60%', '70%'], //宽高
		content:$("#tplEditProject").html(),
		success: function(lay, index){
			layform.render();
			layform.val('projectForm',data);

			layform.on('submit(editProject)', function(data){
				showLoading();
				let params=layform.val('projectForm');
				if(params.order!==""){
					params.order=parseInt(params.order);
				}else{
					params.order=0;
				}
				if(params.id=="") params.id=null;

				params["parentId"]=null;

				let node=zTreeParent.getSelectedNodes();
				if(node.length>0){
					params["parentId"]=node[0].id;
				}

				projectApi.save(params)
						.then(function (r){
							layer.msg(r.msg);
							if(r.ok){
								refreshProjectTree();
								layer.close(layIndex);
							}
						});
				return false;
			});
		}
	});
}

/**
 * 删除项目
 * @param id
 */
function deleteProject(event){
	layer.confirm('此操作会删除项目下的所有数据(子项目，版本信息，翻译数据)，且不可恢复，你真的要删除这条记录吗?', {
		icon: 3,//询问图标
		btn: ['确定','取消'] //按钮
	}, function(index){
		layer.close(index);//关闭弹层
		showLoading();
		projectApi.delete(projectTreeSelectId).then(function (r){
			tipsMsg(r.msg);
			if(r.ok) {
				refreshProjectTree();
			}
		});

	}, function(index){
		layer.close(index);
	});

	let even = window.event || arguments.callee.caller.arguments[0];
	even.preventDefault();
	even.stopPropagation();
}

/**
 * 刷新项目树视图
 */
function refreshProjectTree(){
	if(zTree) zTree.destroy();

	projectApi.queryList()
		.then(function (r){

			zTree=$.fn.zTree.init($("#projectTree"), zTreeSetting, dataToZtree(r.data));

			projectTreeSelectId=-1;
			projectTreeFullName="";

			versionTable.reloadData({
				data:[]
			});
			$("[name='projectTreeFullName']").html(projectTreeFullName);

		});
}

/**
 * 数据转换。因为最开始是用的easyui的tree组件，所以属性值需要做下处理
 * @param data
 */
function dataToZtree(data){
	if(data){
		for(let i=0;i<data.length;i++){
			data[i]["pId"]=data[i].parentId;
			if(data[i].parentId==null){
				data[i]["pId"]=0;
			}
			data[i]["name"]=data[i].text;
			data[i]["open"]=true;
		}
	}
	data.sort(function(a, b) {
		return a.order - b.order;
	});
	return data;
}


// /**
//  * 将list转换为tree
//  * @param data
//  * @returns {*[]}
//  */
// function listToTree(data){
// 	return listToTree1(data,null);
// }
//
// /**
//  * 将list转换为tree，递归遍历
//  * @param data
//  * @param id
//  * @returns {*[]}
//  */
// function listToTree1(data,id){
// 	let arr=[];
// 	for(let i=0;i<data.length;i++){
// 		if(data[i].parentId==id){
// 			arr.push(data[i]);
// 			data[i]["children"]=listToTree1(data,data[i].id);
// 		}
// 	}
// 	arr.sort(function(a, b) {
// 		return a.order - b.order;
// 	});
// 	return arr;
// }
/*********************** 版本相关 *********************/
/**
 * 添加版本
 */
function addVersion(){
	if(projectTreeSelectId==-1){
		tipsMsg("请先选择项目",1);
		return;
	}

	let data={id:"",name:"",remark:"",order:0,program: ""};
	// initVersionForm(data);

	openEditVersionDialog('新建版本',data);
}

/**
 * 编辑版本
 * @param id
 */
function editVersion(id){
	versionApi.queryById(projectTreeSelectId,id).then(function (r){
		if (!r.ok){
			tipsMsg(r.msg,1);
			return;
		}

		openEditVersionDialog('编辑版本',r.data);
	});
}
/**
 * 打开版本编辑的对话框
 */
function openEditVersionDialog(title,data){
	layIndex=layer.open({
		type: 1,
		title:title,
		skin: 'layui-layer-rim', //加上边框
		area: ['60%', '70%'], //宽高
		content:$("#tplEditVersion").html(),
		success: function(lay, index){
			$("[name='projectTreeFullName']").html(projectTreeFullName);

			versionApi.queryProgramList()
				.then(function (r){
					if(r.ok){
						let str='';
						let d=r.data;
						for(let i=0;i<d.length;i++){
							str+='<option value="'+d[i].value+'">'+d[i].text+'</option>';
						}
						$("#programName").html(str);
					}

					layform.render();
					layform.val('versionForm',data);
				});

			layform.on('submit(editVersion)', function(data){
				showLoading();
				let params=layform.val('versionForm');
				if(params.order!==""){
					params.order=parseInt(params.order);
				}else{
					params.order=0;
				}
				if(params.id=="") params.id=null;
				params["projectId"]=projectTreeSelectId;
				// console.log(params);

				versionApi.save(params)
					.then(function (r){
						layer.msg(r.msg);
						if(r.ok){
							initVersionTableData();
							layer.close(layIndex);
						}
					});
				return false;
			});

		}
	});
}

/**
 * 初始化版本的表格数据
 */
function initVersionTableData(){
	versionApi.queryList(projectTreeSelectId)
		.then(function (r){
			// versionTable.datagrid({
			// 	data:r.data
			// });
			versionTable.reloadData({
				data:r.data
			});
		});
}

/**
 * 删除版本数据
 * @param versionId
 */
function deleteVersion(versionId){
	layer.confirm('此操作会删除版本目录包含的所有数据，且不可恢复。您真的要删除这条记录吗?', {
		icon: 3,//询问图标
		btn: ['确定','取消'] //按钮
	}, function(index){
		layer.close(index);//关闭弹层
		showLoading();
		versionApi.delete(projectTreeSelectId,versionId).then(function (r){
			tipsMsg(r.msg);
			if(r.ok) {
				initVersionTableData();
			}
		});

	}, function(index){
		layer.close(index);
	});
}


/**
 * 点击翻译按钮，获取翻译页面并打开
 * @param versionId 版本id
 * @param versionName 版本名
 * @param uuid uuid，也是版本工作目录名
 */
function startTranslate(versionId,versionName,uuid){
	versionApi.startTranslate(projectTreeSelectId,versionId)
		.then(function (r){
			if(r.ok){
				let url=createUrl(r.data,projectTreeSelectId,versionId,versionName,uuid);
				window.open(url);
			}else{
				tipsMsg(r.msg,1);
			}
		});
}

/**
 * 点击修正按钮，获取修正页面的首页
 * @param versionId 版本id
 * @param versionName 版本名
 * @param uuid uuid，也是版本工作目录名
 */
function updateTranslate(versionId,versionName,uuid){
	versionApi.updateTranslate(projectTreeSelectId,versionId)
		.then(function (r){
			if(r.ok){
				let url=createUrl(r.data,projectTreeSelectId,versionId,versionName,uuid);
				window.open(url);
			}else{
				tipsMsg(r.msg,1);
			}
		});
}

/**
 * 打开创建离线文件页面
 * @param versionId 版本id
 * @param versionName 版本名
 * @param uuid uuid，也是版本工作目录名
 */
function createLatestPage(versionId,versionName,uuid){
	versionApi.createLatestPage(projectTreeSelectId,versionId)
		.then(function (r){
			if(r.ok){
				let url=createUrl(r.data,projectTreeSelectId,versionId,versionName,uuid);
				window.open(url);
			}else{
				tipsMsg(r.msg,1);
			}
		});
}

/**
 * 阅览离线版本
 * @param versionId 版本id
 * @param versionName 版本名
 * @param uuid uuid，也是版本工作目录名
 */
function viewLatestPage(versionId,versionName,uuid){
	versionApi.viewLatestPage(projectTreeSelectId,versionId)
		.then(function (r){
			if(r.ok){
				let url=createUrl(r.data,projectTreeSelectId,versionId,versionName,uuid);
				window.open(url);
			}else{
				tipsMsg(r.msg,1);
			}
		});
}

/**
 * 导出词库
 * @param versionId 版本id
 * @param versionName 版本名
 */
async function exportDictStore(versionId,versionName){
	//弹出另存为对话框
	let data=await commonApi.fileDialog(2,"导出词库 >> "+projectTreeFullName+"-"+versionName,projectTreeFullName+"-"+versionName+".json");
	if(data.length>0){
		showLoading();
		data=await versionApi.createDictStore(data[0],projectTreeSelectId,versionId);
		hideLoading();
		tipsMsg(data.msg);
	}

}

/**
 * 创建url
 * @param url
 * @param projectId
 * @param versionId
 * @param versionName
 * @param uuid
 */
function createUrl(url,projectId,versionId,versionName,uuid){
	url=url.replaceAll("{uuid}",uuid);
	url=url+"?projectId="+projectId+"&versionId="+versionId+"&projectName="+encodeURIComponent(projectTreeFullName)+"&versionName="+encodeURIComponent(versionName);
	return url;
}



/**
 * 打开系统配置页面
 */
function openConfigView(){
	layIndex=layer.open({
		type: 2,
		title:'系统配置',
		skin: 'layui-layer-rim', //加上边框
		area: ['50%', '50%'], //宽高
		content:'config.html'
	});
}
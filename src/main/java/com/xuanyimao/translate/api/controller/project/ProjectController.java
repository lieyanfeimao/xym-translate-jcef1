package com.xuanyimao.translate.api.controller.project;

import com.xuanyimao.translate.anno.JsClass;
import com.xuanyimao.translate.anno.JsFunction;
import com.xuanyimao.translate.anno.JsObject;
import com.xuanyimao.translate.common.ApplicationData;
import com.xuanyimao.translate.entity.Message;
import com.xuanyimao.translate.entity.project.Project;
import com.xuanyimao.translate.api.service.project.ProjectService;
import com.xuanyimao.translate.util.LogUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目
 */
@JsClass(prefix="project")
public class ProjectController {

	@JsObject
	ProjectService projectService;
	
	/**
	 * 查询项目列表
	 * @return
	 */
	@JsFunction(name="queryList")
	public Message queryList() {
		return Message.success("查询成功", ApplicationData.projectList);
	}
	
	/**
	 * 保存/修改 项目
	 * @return
	 */
	@JsFunction(name="save")
	public Message save(Project project) {
		try{
			if(project==null){
				return Message.error("参数错误");
			}
			if(StringUtils.isBlank(project.getText())){
				return Message.error("项目名不能为空!");
			}

			if(project.getId()!=null){
				if(project.getId()==project.getParentId()) {
					return Message.error("不能将自身做为父节点!");
				}
				List<Project> projectList=projectService.findChild(project);
				if(!projectList.isEmpty()){
					for(Project p:projectList){
						if(p.getId()==project.getParentId()){
							return Message.error("不能将子节点做为父节点!");
						}
					}
				}
			}

			return projectService.save(project)?Message.success("操作成功"):Message.error("操作失败");
		}catch (Exception e) {
			LogUtil.getLogger().error("保存失败：{}",e.getMessage(),e);
			return Message.error("保存失败："+e.getMessage());
		}
	}


	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	@JsFunction(name="queryById")
	public Message queryById(Long id){
		try {
			if(id==null){
				return Message.error("参数错误");
			}
			Project findProject=projectService.findProjectById(ApplicationData.projectList,id);
			if(findProject==null){
				return Message.error("数据不存在或已被删除");
			}

			return Message.success("查询成功",findProject);
		}catch (Exception e) {
			LogUtil.getLogger().error("查询失败：{}",e.getMessage(),e);
			return Message.error("查询失败："+e.getMessage());
		}
	}

	/**
	 * 删除项目
	 * @param id 数据ID
	 * @return
	 */
	@JsFunction(name="delete")
	public Message delete(Long id) {
		try {
			Project findProject=projectService.findProjectById(ApplicationData.projectList,id);
			if(findProject==null){
				return Message.error("删除的数据不存在");
			}
			projectService.delete(findProject);
			return Message.success("删除成功");
		}catch (Exception e) {
			LogUtil.getLogger().error("删除失败：{}",e.getMessage(),e);
			return Message.error("删除失败："+e.getMessage());
		}
	}

}

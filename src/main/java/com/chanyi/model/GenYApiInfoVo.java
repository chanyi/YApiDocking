package com.chanyi.model;import java.io.Serializable;import java.util.List;/** * 接口表 *  * @author lilei * */public class GenYApiInfoVo implements Serializable {	private static final long serialVersionUID = -2168745114216576591L;	private int project_id;// 项目id,	private String path;// 路径,	private String title;// 接口名,	private String method;// 请求方式,	private List<YApiRequestBodyForm> yApiRequestBodyForms;// 接口的具体参数	private List<YApiRequestQuery> yApiReqQuerys;// 预览时的query参数	public int getProject_id() {		return project_id;	}	public void setProject_id(int project_id) {		this.project_id = project_id;	}	public String getPath() {		return path;	}	public void setPath(String path) {		this.path = path;	}	public String getTitle() {		return title;	}	public void setTitle(String title) {		this.title = title;	}	public String getMethod() {		return method;	}	public void setMethod(String method) {		this.method = method;	}	public List<YApiRequestBodyForm> getyApiRequestBodyForms() {		return yApiRequestBodyForms;	}	public void setyApiRequestBodyForms(List<YApiRequestBodyForm> yApiRequestBodyForms) {		this.yApiRequestBodyForms = yApiRequestBodyForms;	}	public List<YApiRequestQuery> getyApiReqQuerys() {		return yApiReqQuerys;	}	public void setyApiReqQuerys(List<YApiRequestQuery> yApiReqQuerys) {		this.yApiReqQuerys = yApiReqQuerys;	}	@Override	public String toString() {		return "GenYApiInfoVo [project_id=" + project_id + ", path=" + path + ", title=" + title + ", method=" + method + ", yApiRequestBodyForms=" + yApiRequestBodyForms + ", yApiReqQuerys="				+ yApiReqQuerys + "]";	}}
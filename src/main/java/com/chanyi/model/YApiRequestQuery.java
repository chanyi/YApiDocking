package com.chanyi.model;import java.io.Serializable;/** * 接口表 *  * @author lilei * */public class YApiRequestQuery implements Serializable {	private static final long serialVersionUID = -1681983395894810422L;	private String required;// ": "1",	private String name;// ": "s",	private String desc;// ": """"	// "req_query": [{	// "required": "1",	// "_id": "5ca57cc7b819cd13dede4e8a",	// "name": "s",	// "desc": ""	// }],	public String getRequired() {		return required;	}	public void setRequired(String required) {		this.required = required;	}	public String getName() {		return name;	}	public void setName(String name) {		this.name = name;	}	public String getDesc() {		return desc;	}	public void setDesc(String desc) {		this.desc = desc;	}	public static long getSerialversionuid() {		return serialVersionUID;	}	@Override	public String toString() {		return "YApiRequestQuery [required=" + required + ", name=" + name + ", desc=" + desc + "]";	}}
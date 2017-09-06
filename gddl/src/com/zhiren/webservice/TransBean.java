package com.zhiren.webservice;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.axis.utils.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class TransBean implements Serializable {
	
	private String shiwbh="";//事务编号
	private List listCommon;	//任务集合
	private String xml=""; 
	public List getListCommon() {
		return listCommon;
	}
	public void setListCommon(List listCommon) {
		this.listCommon = listCommon;
	}
	public String getShiwbh() {
		return shiwbh;
	}
	public void setShiwbh(String shiwbh) {
		this.shiwbh = shiwbh;
	}
	public String CreateXml(String rizPath){
		Element root = new Element("事务");
		Document document = new Document(root);
		
//		root.setAttribute(new Attribute("vin", "123fhg5869705iop90"));
		root.addContent(new Element("事务编号").addContent(shiwbh));
		try {  
			for(int i=0;i<listCommon.size();i++){//命令元素
				Element elMingl = new Element("命令"+(i+1));
				root.addContent(elMingl);
				elMingl.addContent(new Element("数据协议").addContent(((CommonBean)listCommon.get(i)).getShujxy()));
				elMingl.addContent(new Element("操作").addContent(((CommonBean)listCommon.get(i)).getCaoz()));
				elMingl.addContent(new Element("主键").addContent(((CommonBean)listCommon.get(i)).getZhuj()));
				elMingl.addContent(new Element("任务日期").addContent(((CommonBean)listCommon.get(i)).getRenwsj()));
				elMingl.addContent(new Element("电厂id").addContent(((CommonBean)listCommon.get(i)).getDiancxxb_id()));
				for(int j=0;j<((CommonBean)listCommon.get(i)).getShujjl().size();j++){//遍历命令的数据集合一般一条，删除无
					Element elShujjl = new Element("数据记录");
					elMingl.addContent(elShujjl);
					for(int k=0;k<((List)((CommonBean)listCommon.get(i)).getShujjl().get(j)).size();k++){//遍历数据字段
						elShujjl.addContent(new Element((((String[])((List)((CommonBean)listCommon.get(i)).getShujjl().get(j)).get(k)))[0]).
								addContent((((String[])((List)((CommonBean)listCommon.get(i)).getShujjl().get(j)).get(k)))[1]));
					}
				}
			}
			File file=new File(rizPath+"/t"+shiwbh+".xml") ;//(new SimpleDateFormat("yyyyMMddHH:mm:ss").format(new Date()))
			FileWriter writer = new FileWriter(file);
			XMLOutputter outputter = new XMLOutputter();   
			Format format=Format.getPrettyFormat(); 
			format.setEncoding("gb2312"); 
//			format.setOmitDeclaration(true);
			outputter.setFormat(format);
			outputter.output(document, writer);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			outputter.output(document, bo);
			xml=bo.toString();
			//xmlAray=bo.toByteArray();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
}
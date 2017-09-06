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
	
	private String shiwbh="";//������
	private List listCommon;	//���񼯺�
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
		Element root = new Element("����");
		Document document = new Document(root);
		
//		root.setAttribute(new Attribute("vin", "123fhg5869705iop90"));
		root.addContent(new Element("������").addContent(shiwbh));
		try {  
			for(int i=0;i<listCommon.size();i++){//����Ԫ��
				Element elMingl = new Element("����"+(i+1));
				root.addContent(elMingl);
				elMingl.addContent(new Element("����Э��").addContent(((CommonBean)listCommon.get(i)).getShujxy()));
				elMingl.addContent(new Element("����").addContent(((CommonBean)listCommon.get(i)).getCaoz()));
				elMingl.addContent(new Element("����").addContent(((CommonBean)listCommon.get(i)).getZhuj()));
				elMingl.addContent(new Element("��������").addContent(((CommonBean)listCommon.get(i)).getRenwsj()));
				elMingl.addContent(new Element("�糧id").addContent(((CommonBean)listCommon.get(i)).getDiancxxb_id()));
				for(int j=0;j<((CommonBean)listCommon.get(i)).getShujjl().size();j++){//������������ݼ���һ��һ����ɾ����
					Element elShujjl = new Element("���ݼ�¼");
					elMingl.addContent(elShujjl);
					for(int k=0;k<((List)((CommonBean)listCommon.get(i)).getShujjl().get(j)).size();k++){//���������ֶ�
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
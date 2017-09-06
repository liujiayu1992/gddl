package com.zhiren.common;

import java.io.File;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom.CDATA;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class DB2Xml {
	private DocumentBuilderFactory dbf = null;
	private DocumentBuilder db = null;
	private Document document = null;
	private Element root = null;
	private JDBCcon con = null;
	private DOMSource doms = null;
	public DB2Xml(){
		init();
	}
	private void init(){
		con = new JDBCcon();
		dbf = DocumentBuilderFactory.newInstance();
		try {
			db = dbf.newDocumentBuilder();
			document = db.newDocument();
			root = document.createElement("DataBassRoot");
			document.appendChild(root);
		} catch (ParserConfigurationException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	public Document getDoct(){
		if(document == null){
			init();
		}
		return document;
	}
	public JDBCcon getCon(){
		if(con == null){
			init();
		}
		return con;
	}
	public void addTable(String tName){
		setElementForSQL(tName,"select * from " + tName);
	}
	public void setElementForSQL(String eName,String sql){
		ResultSetList rsl = con.getResultSetList(sql);
		while(rsl.next()){
			
			Element eTable = getDoct().createElement(eName);
			root.appendChild(eTable);
			String[] cns = rsl.getColumnNames();
			for(int m = 0; m<cns.length ; m++){
				String nName = cns[m];
				String nType = rsl.getColumnTypes()[m];
				String nValue = null;
				Element eColumn = getDoct().createElement(nName);
				eColumn.setAttribute("dataType", nType);
				if("DATE".equals(nType)){
					nValue = rsl.getDateTimeString(nName);
				}else if("NUMBER".equals(nType)){
					nValue = (rsl.getString(nName)==null || "".equals(rsl.getString(nName)))?"0":rsl.getString(nName);
				}else{
					nValue = rsl.getString(nName);
				}
				//Text t = getDoct().createTextNode("<![CDATA[" + nValue + "]]>");
				eColumn.appendChild(getDoct().createCDATASection(nValue));
				eTable.appendChild(eColumn);
			}
		}
		rsl.close();
	}
	public DOMSource getDoms(){
		doms = new DOMSource(getDoct());
		return doms;
	}
	
	public void WriteFile(File xmlFile) throws TransformerException{
		StreamResult result = new StreamResult(xmlFile);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		Properties properties = transformer.getOutputProperties();
		properties.setProperty(OutputKeys.ENCODING, "GB2312");
		properties.setProperty(OutputKeys.METHOD, "xml");
		properties.setProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperties(properties);
		transformer.transform(getDoms(), result);
	}
}

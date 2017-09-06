package com.zhiren.common;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import com.zhiren.common.DB2Xml;

public class BaiscTableExport {

	/**
	 * @param args
	 */
	private static String[] tableNames = {
			"chebb" , //车别
			"chengsb" , //城市表 
			"chezxxb" , //主要默认数据为汽、皮带秤
			"danwb" , //单位与指标的关系 
			"diancxxb" , //电厂信息 
			"dianclbb" , //统计口径
			"feiylbb" , //费用类别 
			"gongsb" , //公式表
			"hetjjfsb" , //合同计价方式
			"hetjsfsb" , //合同结算方式表
			"hetjsxsb" , //合同结算形式表
			"jiekfspzb" , //接口发送配置表
			"jiekbmzhpzb" , //接口编码转换配置表
			"jiekjspzb" , //接口接收配置表
			"jiekzhb" , //接口帐户
			"jihkjb" , //计划口径表
			"leibztb" , //类别状态(流程) 
			"lujxxb" , //路局信息表 
			"nianfb" , //年份表
			//"renyxxb" , //人员信息表
			//"renyzqxb" , //人员组权限表
			"renwmcb" , //任务名称表
			"shengfb" , //省份表
			"shengfdqb" , //省份地区表
			"tiaojb" , //条件表
			"xitxxb" , //系统信息表
			"yuefb" , //月份表
			"yunsfsb" , //运输方式表
			"zhibb" , //指标表
			"ziyxxb" , //资源信息表
			//"zuqxb" , //组权限表
			//"zuxxb"  //组信息表	
			};
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		System.out.println("导出开始");
		try {
			DB2Xml dx = new DB2Xml();
			for(int i = 0 ; i < tableNames.length; i++){
				dx.addTable(tableNames[i]);
			}
			File file = new File("D:/关键表.xml");
			if(!file.exists()){
				file.createNewFile();
			}
			dx.WriteFile(file);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			System.out.println("导出结束");
			System.exit(0);
		}
			
	}

}

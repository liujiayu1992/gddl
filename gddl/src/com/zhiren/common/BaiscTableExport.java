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
			"chebb" , //����
			"chengsb" , //���б� 
			"chezxxb" , //��ҪĬ������Ϊ����Ƥ����
			"danwb" , //��λ��ָ��Ĺ�ϵ 
			"diancxxb" , //�糧��Ϣ 
			"dianclbb" , //ͳ�ƿھ�
			"feiylbb" , //������� 
			"gongsb" , //��ʽ��
			"hetjjfsb" , //��ͬ�Ƽ۷�ʽ
			"hetjsfsb" , //��ͬ���㷽ʽ��
			"hetjsxsb" , //��ͬ������ʽ��
			"jiekfspzb" , //�ӿڷ������ñ�
			"jiekbmzhpzb" , //�ӿڱ���ת�����ñ�
			"jiekjspzb" , //�ӿڽ������ñ�
			"jiekzhb" , //�ӿ��ʻ�
			"jihkjb" , //�ƻ��ھ���
			"leibztb" , //���״̬(����) 
			"lujxxb" , //·����Ϣ�� 
			"nianfb" , //��ݱ�
			//"renyxxb" , //��Ա��Ϣ��
			//"renyzqxb" , //��Ա��Ȩ�ޱ�
			"renwmcb" , //�������Ʊ�
			"shengfb" , //ʡ�ݱ�
			"shengfdqb" , //ʡ�ݵ�����
			"tiaojb" , //������
			"xitxxb" , //ϵͳ��Ϣ��
			"yuefb" , //�·ݱ�
			"yunsfsb" , //���䷽ʽ��
			"zhibb" , //ָ���
			"ziyxxb" , //��Դ��Ϣ��
			//"zuqxb" , //��Ȩ�ޱ�
			//"zuxxb"  //����Ϣ��	
			};
	public static void main(String[] args) {
		// TODO �Զ����ɷ������
		System.out.println("������ʼ");
		try {
			DB2Xml dx = new DB2Xml();
			for(int i = 0 ; i < tableNames.length; i++){
				dx.addTable(tableNames[i]);
			}
			File file = new File("D:/�ؼ���.xml");
			if(!file.exists()){
				file.createNewFile();
			}
			dx.WriteFile(file);
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			System.out.println("��������");
			System.exit(0);
		}
			
	}

}

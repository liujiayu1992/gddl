package com.zhiren.jt.jiesgl.changfhs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import bsh.EvalError;
import bsh.Interpreter;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.FileNameFilter;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Jiesdyz{
//	���ݵ糧�ϴ��Ľ������ݽ��г������ܵ�����֤
//	2008��08��25�� �汾V1.0
//		ʵ�ֹ��ܣ����ݵ糧�����������ݣ����¼���ú��ۡ���˰�ϼ�
//		˼·:�糧�����������ͨ���ӿ��ϴ���jiesyzsjy���У�
//			����Ǽ�Ȩƽ������jiesyzsjy����дһ�����ݣ�����ǵ����ξ�Ҫд��������
//			Ҫ����н�����֤�м���ǰ��������
//			1�����Ƚ��㵥Ҫ�ҵ���ͬ
//			2��Ҫ�����ҵ���Ӧ��fahb��¼
//			3��Ҫ�õ���Ӧ��xml���㹫ʽ
//			4��Ҫ�õ�jiesyzsjy����Ϣ
	
	Jiesdyzbean yzb=new Jiesdyzbean(); 
	
//	1�����ݳ����õ�����������Ϣ
	private String getYanz(long Jiesb_id,long Diancxxb_id){
		
		String sql="";
		
		JDBCcon con=new JDBCcon();
		try{
//			�ȵõ���ͬ��Ϣ���õ�hetb_id��jiessl
			sql="select jiessl,jiesb.hetb_id,jiesb.shuil,round_new(yingd-kuid,2) as yingk from jiesb,hetb 	\n" +
					" where jiesb.hetb_id=hetb.id  		   						\n" +
					" and jiesb.id="+Jiesb_id+" and jiesb.diancxxb_id="+Diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.next()){
				
				yzb.setHetb_id(rsl.getLong("hetb_id"));
				yzb.setJiessl(rsl.getDouble("jiessl"));
				yzb.setMeiksl(rsl.getDouble("shuil"));
			}
			
			if(yzb.getHetb_id()==0){//δ�ҵ���ͬ
				
				yzb.setErro_Msg("û���ҵ���Ӧ�ĺ�ͬ");
				return "";
			}else{//���ҵ���ͬ
				
				if(setOtherJcxx(Jiesb_id,Diancxxb_id)){//�ӷ�������ȡ��������Ϣ
					
					if(getGongsInfo(Diancxxb_id)){
						
						yzb.setSelIds(getJiesyzsjy_SelId(Jiesb_id,Diancxxb_id));
						
						if(!yzb.getSelIds().equals("")){//�õ���������ԴId�Ĵ�
							
							if(Count(yzb.getRanlpzb_id(),yzb.getYunsfsb_id(),yzb.getFaz_id(),yzb.getDaoz_id(),
									Diancxxb_id,yzb.getHetb_id(),yzb.getMinfahrq(),yzb.getGongysb_id())){
								
								yzb.setErro_Msg("����۸�ʱ����");
								return "";
							}
						}else{
							
							yzb.setErro_Msg("û�еõ���֤����Դ��Ϣ");
							return "";
						}
						
					}else{
						
						yzb.setErro_Msg("û�еõ���ʽ��Ϣ");
						return "";
					}
				}else{
					
					yzb.setErro_Msg("û�еõ���������");
					return "";
				}
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return yzb.getErro_Msg();
	}

	private boolean setOtherJcxx(long lngJiesb_id,long diancxxb_id){
		// TODO �Զ����ɷ������
//		��Щ��Ϣ��jiesb���޷��õ�������д�η���
		JDBCcon con=new JDBCcon();
		String sql="";
		boolean Flag=false;
		try{
			
			sql="select max(gongysb_id) as gongysb_id,max(pinzb_id) as ranlpzb_id,max(yunsfsb_id) as yunsfsb_id,max(faz_id) as faz_id, \n" +
					" max(daoz_id) as daoz_id,to_char(min(fahrq),'yyyy-mm-dd') as minfahrq	\n"+
					" from fahb where jiesb_id="+lngJiesb_id+" and diancxxb_id="+diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.getRows()>0){
				
				if(rsl.next()){
					
					yzb.setGongysb_id(rsl.getLong("gongysb_id"));
					yzb.setRanlpzb_id(rsl.getLong("ranlpzb_id"));
					yzb.setYunsfsb_id(rsl.getLong("yunsfsb_id"));
					yzb.setFaz_id(rsl.getLong("faz_id"));
					yzb.setDaoz_id(rsl.getLong("daoz_id"));
					yzb.setMinfahrq(rsl.getDate("minfahrq"));
					Flag=true;
				}
			}
			
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Flag;
	}

	private boolean Count(long Ranlpzb_id,long Yunsfsb_id,long Faz_id,long Daoz_id,long Diancxxb_id,
			long Hetb_id,Date Minfahrq,long Gongysb_id) {
		// TODO �Զ����ɷ������
		

		//�õ���ͬ��Ϣ�е��˼�
			JDBCcon con =new JDBCcon();
			String sql="";
			Interpreter bsh=new Interpreter();
			Jiesdcz Jscz=new Jiesdcz();
			try{
				
//				����(��ͬ������Ϊ��λ��ÿ�´�һ�����ݶ���ȡһ����)
				sql="select htsl.hetl,htb.gongfkhyh,htb.gongfzh \n"
					+ " from hetb htb, hetslb htsl	\n"
					+ " where htb.id=htsl.hetb_id	\n"      
					+ " and htsl.pinzb_id="+Ranlpzb_id+" and yunsfsb_id="+Yunsfsb_id+" and faz_id="+Faz_id+" and daoz_id="+Daoz_id+"	\n"
					+ " and htsl.diancxxb_id="+Diancxxb_id+" and htsl.riq<=to_date('"+Jiesdcz.FormatDate(Minfahrq)+"','yyyy-MM-dd')	\n"              
					+ " and htb.id="+Hetb_id+" order by riq desc ";
				
				ResultSetList rsl = con.getResultSetList(sql);
				if (rsl.next()){
					
					yzb.setHetml(rsl.getDouble("hetl"));
					yzb.setKaihyh(rsl.getString("gongfkhyh"));
					yzb.setZhangh(rsl.getString("gongfzh"));
				}
				
//				����(��ͬ��һ����ͬ�Ŷ�Ӧ���������¼)
				sql="select zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw	\n"
					+ " from hetb htb, hetzlb htzl,zhibb zbb,tiaojb tjb,danwb dwb	\n"
					+ " where htb.id=htzl.hetb_id and htzl.zhibb_id=zbb.id and htzl.tiaojb_id=tjb.id	\n" 
					+ " and htzl.danwb_id=dwb.id	\n"
					+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1	\n"          
					+ " and htb.id="+Hetb_id+" ";
				
//				�۸񣨺�ͬ��һ����ͬ��Ӧ��������۸�	
				sql="select htjg.id as hetjgb_id,zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jij,				\n"
					+ " jijdw.bianm as jijdw,jsfs.mingc as  jiesfs,jsxs.bianm as jiesxs,yunj,					\n"
					+ " yjdw.bianm as yunjdw,yingdkf,ysfs.mingc as yunsfs,zuigmj,htjfsb.bianm as hejfs			\n"
					+ " from hetb htb, hetjgb htjg,zhibb zbb,tiaojb tjb,danwb dwb,danwb jijdw,hetjsfsb jsfs,	\n"
					+ " hetjsxsb jsxs,danwb yjdw,yunsfsb ysfs,hetjjfsb htjfsb									\n"
					+ " where htb.id=htjg.hetb_id and htjg.zhibb_id=zbb.id and htjg.tiaojb_id=tjb.id			\n" 
					+ " and htjg.danwb_id=dwb.id and htjg.jijdwid=jijdw.id and htjg.hetjsfsb_id=jsfs.id			\n"
					+ " and htjg.hetjsxsb_id=jsxs.id and htjg.yunjdw_id=yjdw.id(+)								\n" 
					+ " and htjg.yunsfsb_id=ysfs.id																\n"
					+ " and htjg.hetjjfsb_id=htjfsb.id															\n"
					+ " and tjb.leib=1 and zbb.leib=1 and tjb.leib=1											\n"          
					+ " and htb.id="+Hetb_id+"";
				
				rsl=con.getResultSetList(sql);
				
				if(rsl.next()){
					
					yzb.setHetmdj(rsl.getDouble("jij"));			//����ú����
					yzb.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
					yzb.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
					yzb.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
					yzb.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
					yzb.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
					yzb.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
					yzb.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
				
					bsh.set("������ʽ", yzb.getJiesxs());
					bsh.set("�Ƽ۷�ʽ", yzb.getHetjjfs());
					bsh.set("�۸�λ", yzb.getHetmdjdw());	
					bsh.set("��ͬ�۸�", yzb.getHetmdj());
					bsh.set("���ú��", yzb.getZuigmj());
					
//					��ͬ����ָ��,ȡ�����������ĺ�ͬ����
					if(rsl.getRows()==1){
						
//						��һ����ͬ
						if(yzb.getHetjjfs().equals(Locale.mulj_hetjjfs)){	//								Ŀ¼�۽���
							
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){//								�����ν���
								
								String[] test=null;
								
								if(yzb.getSelIds().indexOf(",")>-1){
									
									test[0]=yzb.getSelIds();
								}else{
									
									test=yzb.getSelIds().split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									��ý�������������
									getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
									
//									ΪĿ¼�۸�ֵ
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
//									������ۿ�
									getZengkk(Hetb_id,bsh);
									
//									�û��Զ��幫ʽ
									bsh.set(Locale.user_custom_mlj_jiesgs,yzb.getUser_custom_mlj_jiesgs());			
									
//									ִ�й�ʽ
									bsh.eval(yzb.getGongs());
									
//									�õ�������ָ��
									setJieszb(bsh);
									
//									����ú����
									computData_Dpc();
								}
								
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//						��Ȩƽ��
								
//								��ý�������������
								getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs, yzb.getUser_custom_mlj_jiesgs());
								
//								ִ�й�ʽ
								bsh.eval(yzb.getGongs());
								
//								�õ�������ָ��
								setJieszb(bsh);
								
//								����ú����
								computData();
							}
							
						}else{		
							
//							��Ŀ¼��
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){//							�����ν���
								
								String[] test=null;
								
								if(yzb.getSelIds().indexOf(",")>-1){
									
									test[0]=yzb.getSelIds();
								}else{
									
									test=yzb.getSelIds().split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
//									��ý�������������
									getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
									
									double Dbltmp=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
									
									Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
									
									bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
									bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));								//ָ�굥λ
									
									yzb.setYifzzb(rsl.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
									
									bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
									bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
									
									
//									������ۿ�
									getZengkk(Hetb_id,bsh);
									
//									�û��Զ��幫ʽ
									bsh.set(Locale.user_custom_fmlj_jiesgs,yzb.getUser_custom_fmlj_jiesgs());
									
//									ִ�й�ʽ
									bsh.eval(yzb.getGongs());
									
//									�õ�������ָ��
									setJieszb(bsh);
									
//									����ú����
									computData_Dpc();
								}
								
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
								
//								��ý�������������
								getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
								
								double Dbltmp=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
								Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
								
								bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
								bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));
								
								yzb.setYifzzb(rsl.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
								
								bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
								bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_fmlj_jiesgs, yzb.getUser_custom_fmlj_jiesgs());
								
//								ִ�й�ʽ
								bsh.eval(yzb.getGongs());
								
//								�õ�������ָ��
								setJieszb(bsh);
								
//								����ú����
								computData();
							}
						}
						
						yzb.setHetjgpp_Flag(true);
						yzb.setHetjgb_id(rsl.getLong("hetjgb_id"));
					}else{
//						�ж����ͬ
						
						if(yzb.getHetjjfs().equals(Locale.mulj_hetjjfs)){			//					Ŀ¼�۽���
						
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){		//					�����ν���
							
								String[] test=null;
								
								if(yzb.getSelIds().indexOf(",")>-1){
									
									test[0]=yzb.getSelIds();
								}else{
									
									test=yzb.getSelIds().split(",");
								}
								
								for(int i=0;i<test.length;i++){
									
	//								��ý�������������
									getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
									
	//								ΪĿ¼�۸�ֵ
									computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
									
	//								������ۿ�
									getZengkk(Hetb_id,bsh);
									
//									�û��Զ��幫ʽ
									bsh.set(Locale.user_custom_mlj_jiesgs, yzb.getUser_custom_mlj_jiesgs());
									
	//								ִ�й�ʽ
									bsh.eval(yzb.getGongs());
									
	//								�õ�������ָ��
									setJieszb(bsh);
									
	//								����ú����
									computData_Dpc();
								}
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��

								
//								��ý�������������
								getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
								
//								ΪĿ¼�۸�ֵ
								computMlj(bsh, rsl, Jscz, Diancxxb_id, Gongysb_id, Hetb_id);
								
//								������ۿ�
								getZengkk(Hetb_id,bsh);
								
//								�û��Զ��幫ʽ
								bsh.set(Locale.user_custom_mlj_jiesgs, yzb.getUser_custom_mlj_jiesgs());
								
//								ִ�й�ʽ
								bsh.eval(yzb.getGongs());
								
//								�õ�������ָ��
								setJieszb(bsh);
								
//								����ú����
								computData();
							}
						}else{
							
//							�����ͬ
//							��Ŀ¼��
							double	Dbljijzb=0;
							
							if(yzb.getJiesxs().equals(Locale.danpc_jiesxs)){	//�����ν���
								
									String[] test=null;
									
									if(yzb.getSelIds().indexOf(",")>-1){
										
										test[0]=yzb.getSelIds();
									}else{
										
										test=yzb.getSelIds().split(",");
									}
							
									for(int i=0;i<test.length;i++){
										
										ResultSetList rsltmp=rsl;
										
//										��ý�������������
										getJiesszl(test[i],Diancxxb_id,Gongysb_id,Hetb_id);
										
										do{
											
											Dbljijzb=Jiesdcz.getYanZZb_info(yzb, rsltmp.getString("zhib"));
											Dbljijzb=Jiesdcz.getUnit_transform(rsltmp.getString("zhib"),rsltmp.getString("danw"),Dbljijzb);
										
											if(Dbljijzb>=rsltmp.getDouble("xiax")&&Dbljijzb<=(rsltmp.getDouble("shangx")==0?9999:rsltmp.getDouble("shangx"))){
											
												yzb.setHetmdj(rsltmp.getDouble("jij"));			//����ú����
												yzb.setZuigmj(rsltmp.getDouble("zuigmj"));			//���ú��
												yzb.setHetmdjdw(rsltmp.getString("jijdw"));		//��ͬú���۵�λ
												yzb.setJiesfs(rsltmp.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
												yzb.setJiesxs(rsltmp.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
												yzb.setHetyj(rsltmp.getDouble("yunj"));			//��ͬ�˼۵���
												yzb.setHetyjdw(rsltmp.getString("yunjdw"));		//��ͬ�˼۵�λ
												yzb.setHetjjfs(rsltmp.getString("hejfs"));		//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
											
												bsh.set("������ʽ", yzb.getJiesxs());
												bsh.set("�Ƽ۷�ʽ", yzb.getHetjjfs());
												bsh.set("�۸�λ", yzb.getHetmdjdw());	
												bsh.set("��ͬ�۸�", yzb.getHetmdj());
												bsh.set("���ú��", yzb.getZuigmj());
												
												bsh.set(rsltmp.getString("zhib")+Jiesdcz.getZhibbdw(rsltmp.getString("zhib"),rsltmp.getString("danw")),Dbljijzb);	//����ֵ
												bsh.set(rsltmp.getString("zhib")+"������λ", 	rsltmp.getString("danw"));
												
												yzb.setYifzzb(rsltmp.getString("zhib"));	//Ĭ�ϵ��Ѹ�ֵָ��
												
												bsh.set(rsltmp.getString("zhib")+"����", 		rsltmp.getDouble("shangx"));		//ָ������
												bsh.set(rsltmp.getString("zhib")+"����", 		rsltmp.getDouble("xiax"));			//ָ������
												
	//											������ۿ�
												getZengkk(Hetb_id,bsh);
												
	//											�û��Զ��幫ʽ
												bsh.set(Locale.user_custom_fmlj_jiesgs, yzb.getUser_custom_fmlj_jiesgs());
												
	//											ִ�й�ʽ
												bsh.eval(yzb.getGongs());
												
	//											�õ�������ָ��
												setJieszb(bsh);
												
	//											����ú����
												computData_Dpc();
												
												yzb.setHetjgpp_Flag(true);
											}	
										}while(rsltmp.next());
									}		
									
										
							}else if(yzb.getJiesxs().equals(Locale.jiaqpj_jiesxs)){//					��Ȩƽ��
								
//									��ý�������������
									getJiesszl(yzb.getSelIds(),Diancxxb_id,Gongysb_id,Hetb_id);
									
									do{
										
										Dbljijzb=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
										Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
										
										if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
											
											yzb.setHetmdj(rsl.getDouble("jij"));			//����ú����
											yzb.setZuigmj(rsl.getDouble("zuigmj"));			//���ú��
											yzb.setHetmdjdw(rsl.getString("jijdw"));		//��ͬú���۵�λ
											yzb.setJiesfs(rsl.getString("jiesfs"));			//���㷽ʽ�������ۡ�����ۣ�
											yzb.setJiesxs(rsl.getString("jiesxs"));			//������ʽ�������Ρ���Ȩƽ����
											yzb.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
											yzb.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
											yzb.setHetjjfs(rsl.getString("hejfs"));			//��ͬ�Ƽ۷�ʽ��Ŀ¼�ۡ���ֵ����(��)����ֵ����(��)�������۸��ۣ�
										
											bsh.set("������ʽ", yzb.getJiesxs());
											bsh.set("�Ƽ۷�ʽ", yzb.getHetjjfs());
											bsh.set("�۸�λ", yzb.getHetmdjdw());	
											bsh.set("��ͬ�۸�", yzb.getHetmdj());
											bsh.set("���ú��", yzb.getZuigmj());
											
											yzb.setHetjgb_id(rsl.getLong("hetjgb_id"));		//��ͬ�۸��id
											
											bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbljijzb);	//����ֵ
											bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));
											
											yzb.setYifzzb(rsl.getString("zhib"));			//Ĭ�ϵ��Ѹ�ֵָ��
											
											bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
											bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
											
//												������ۿ�
											getZengkk(Hetb_id,bsh);
											
//												�û��Զ��幫ʽ
											bsh.set(Locale.user_custom_fmlj_jiesgs, yzb.getUser_custom_fmlj_jiesgs());
											
//												ִ�й�ʽ
											bsh.eval(yzb.getGongs());
											
//												�õ�������ָ��
											setJieszb(bsh);
											
//												����ú����
											computData();
											
											yzb.setHetjgpp_Flag(true);
										}
										
									}while(rsl.next());
							}
						}
					}
				}
				
				if(!yzb.getHetjgpp_Flag()){
					
					yzb.setErro_Msg("û�к�ͬ�۸����������ƥ�䣡");
					return false;
				}
					
		    } catch (Exception e) {
		        e.printStackTrace();
		        return false;
		    } finally {
		    	con.Close();
		    }
		    return true;
	}
	
	private boolean getGongsInfo(long Diancxxb_id){
	    
		yzb.setGongs(Jiesdcz.GetJiesgs(Diancxxb_id,SysConstant.Gs_JS_HeadName_Mk));
		
		if(!yzb.getGongs().equals("")){
			
			return true;
		}
		return false;
	}

	private void SetJicxx(ResultSetList rsl) {
		// TODO �Զ����ɷ������
		
		yzb.setJiessl(rsl.getDouble("jiessl"));
		yzb.setQnetar(rsl.getDouble("Qnetar"));
		yzb.setStd(rsl.getDouble("std"));
		yzb.setAd(rsl.getDouble("ad"));
		yzb.setVdaf(rsl.getDouble("vdaf"));
		yzb.setMt(rsl.getDouble("mt"));
		yzb.setQgrad(rsl.getDouble("Qgrad"));
		yzb.setQbad(rsl.getDouble("Qbad"));
		yzb.setHad(rsl.getDouble("Had"));
		yzb.setStad(rsl.getDouble("stad"));
		yzb.setMad(rsl.getDouble("mad"));
		yzb.setAar(rsl.getDouble("aar"));
		yzb.setAad(rsl.getDouble("aad"));
		yzb.setVad(rsl.getDouble("Vad"));
		yzb.setT2(rsl.getDouble("T2"));
	}
	
	private String getJiesyzsjy_SelId(long Jiesb_id,long Diancxxb_id){
		String sql="";
		StringBuffer SelId=new StringBuffer("");
		JDBCcon con=new JDBCcon();
		try{
			
			sql="select id from jiesyzsjy where jiesb_id="+Jiesb_id+" and diancxxb_id="+Diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				SelId.append(rsl.getString("id")).append(",");
			}
			rsl.close();
			SelId.deleteCharAt(SelId.length()-1);
			
		}catch(Exception e){
		
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return SelId.toString();
	}
	
	private void getJiesszl(String SelIds,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
//		������������
		JDBCcon con=new JDBCcon();
		String sql="";
		try{
			
			sql="select * from jiesyzsjy where id in ("+SelIds+") and diancxxb_id="+Diancxxb_id;
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
				
				SetJicxx(rsl);
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	private void computMlj(Interpreter bsh,ResultSetList rsl,Jiesdcz Jscz,long Diancxxb_id,long Gongysb_id,long Hetb_id){
		
//		Ϊ����Ŀ¼�ۼ۸�ֵ
		try {
			double	Dbljijzb=0;
			
			bsh.set("��ֵ����_"+Locale.Qnetar_zhibb, 0);
			bsh.set(Locale.Qnetar_zhibb+"_����", 0);
			bsh.set(Locale.Qnetar_zhibb+"_����", 0);
			bsh.set("�ӷ��ݱȼ�_"+Locale.Vdaf_zhibb, 0);
			bsh.set(Locale.Vdaf_zhibb+"_����", 0);
			bsh.set(Locale.Vdaf_zhibb+"_����", 0);
			bsh.set("��ֱȼ�_"+Locale.Std_zhibb, 0);
			bsh.set(Locale.Std_zhibb+"_����", 0);
			bsh.set(Locale.Std_zhibb+"_����", 0);
			bsh.set("��ֱȼ�_"+Locale.Stad_zhibb, 0);
			bsh.set(Locale.Stad_zhibb+"_����", 0);
			bsh.set(Locale.Stad_zhibb+"_����", 0);
			bsh.set("�ҷֱȼ�_"+Locale.Aar_zhibb, 0);
			bsh.set(Locale.Aar_zhibb+"_����", 0);
			bsh.set(Locale.Aar_zhibb+"_����", 0);
			bsh.set("�ҷֱȼ�_"+Locale.Aad_zhibb, 0);
			bsh.set(Locale.Aad_zhibb+"_����", 0);
			bsh.set(Locale.Aad_zhibb+"_����", 0);
			
			do {
				
				Dbljijzb=Jiesdcz.getYanZZb_info(yzb,rsl.getString("zhib"));
				Dbljijzb=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbljijzb);
				
				if(Dbljijzb>=rsl.getDouble("xiax")&&Dbljijzb<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					if(Jiesdcz.CheckMljRz(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))));
						
						bsh.set(rsl.getString("zhib")+"_����", Jiesdcz.getUnit_transform(rsl.getString("zhib"), Locale.qiankmqk_danw, rsl.getDouble("xiax")));
						
						bsh.set("��ֵ����_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}	
					if(Jiesdcz.CheckMljHff(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("�ӷ��ݱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Jiesdcz.CheckMljLiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("��ֱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					if(Jiesdcz.CheckMljHiuf(rsl.getString("zhib"))){
						
						bsh.set(rsl.getString("zhib")+"_����", (rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx")));
						
						bsh.set(rsl.getString("zhib")+"_����", rsl.getDouble("xiax"));
						
						bsh.set("�ҷֱȼ�_"+rsl.getString("zhib"), rsl.getDouble("jij"));
					}
					
					yzb.setHetyj(rsl.getDouble("yunj"));			//��ͬ�˼۵���
					yzb.setHetyjdw(rsl.getString("yunjdw"));		//��ͬ�˼۵�λ
					yzb.setHetjgpp_Flag(true);						//��ͬ�۸�ƥ�䣨�ж��Ƿ�ȡ�����õĺ�ͬ�۸�
				}
				
			}while(rsl.next());
			
			bsh.set("Ʒ�ֱȼ�", Jscz.getMljPzbj(yzb.getRanlpzb_id()));
			
			//	�����ԼӼ�
			bsh.set(Locale.zhengcxjj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id,Hetb_id,Locale.zhengcxjj_jies, "0")));
					
			//	�Ӽ�
			bsh.set(Locale.jiaj_jies, Double.parseDouble(Jiesdcz.getJiessz_item(Diancxxb_id, Gongysb_id, Hetb_id,Locale.jiaj_jies, "0")));
			
		}catch(EvalError e){
			
			e.printStackTrace();
		}	
	}
	
	private boolean getZengkk(long Hetb_id,Interpreter bsh){
//		���ۿ�
		JDBCcon con=new JDBCcon();
		try{
			ResultSetList rsl=null;
			
			String sql="select distinct zbb.bianm as zhib,tjb.bianm as tiaoj,shangx,xiax,dwb.bianm as danw,jis,	\n"
				+ " jisdw.bianm as jisdw,kouj,kjdw.bianm as koujdw,zengfj,zfjdw.bianm as zengfjdw,	\n"
				+ " xiaoscl,jizzkj,jizzb,czxm.mingc as canzxm,czxmdw.bianm as canzxmdw,canzsx,canzxx	\n"  
				+ " from hetb htb, hetzkkb htzkk,zhibb zbb,tiaojb tjb,danwb dwb,danwb jisdw,danwb kjdw,	\n"
				+ " danwb zfjdw,zhibb czxm,danwb czxmdw	\n"
				+ " where htb.id=htzkk.hetb_id and htzkk.zhibb_id=zbb.id and htzkk.tiaojb_id=tjb.id	\n" 
				+ " and htzkk.danwb_id=dwb.id(+) and htzkk.jisdwid=jisdw.id(+)	\n" 
				+ " and htzkk.koujdw=kjdw.id(+)		\n" 
				+ " and htzkk.zengfjdw=zfjdw.id(+)	\n" 
				+ " and htzkk.canzxm=czxm.id(+)		\n" 
				+ " and htzkk.canzxmdw=czxmdw.id(+)	\n"
				+ " and tjb.leib=1 and zbb.leib=1	\n"          
				+ " and htb.id="+Hetb_id;
			
			rsl=con.getResultSetList(sql);
			double Dbltmp=0; 		//��¼ָ�����ֵ
			String  Strtmp="";		//��¼�趨��ָ��
			
			while(rsl.next()){
				
				Dbltmp=Jiesdcz.getYanZZb_info(yzb, rsl.getString("zhib"));
//				ָ��Ľ���ָ��
				Dbltmp=Jiesdcz.getUnit_transform(rsl.getString("zhib"),rsl.getString("danw"),Dbltmp);
				if(Dbltmp>=rsl.getDouble("xiax")&&Dbltmp<=(rsl.getDouble("shangx")==0?9999:rsl.getDouble("shangx"))){
					
					//ָ��������ͨ��zhibb�ı����ֶν������ã�ָ�굥λ��ͨ��danwb�ı����ֶν�������,ֻ�������������ɷ��ص�λ
					
					bsh.set(rsl.getString("zhib")+Jiesdcz.getZhibbdw(rsl.getString("zhib"),rsl.getString("danw")),Dbltmp);	//����ֵ
					bsh.set(rsl.getString("zhib")+"������λ", 	rsl.getString("danw"));			//ָ�굥λ
					bsh.set(rsl.getString("zhib")+"���ۿ�����", 	rsl.getString("tiaoj"));		//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
					bsh.set(rsl.getString("zhib")+"��������",	rsl.getDouble("zengfj"));		//������
					bsh.set(rsl.getString("zhib")+"�۸�����",	rsl.getDouble("kouj"));			//�ۼ�
					bsh.set(rsl.getString("zhib")+"�����۵�λ", 	rsl.getString("zengfjdw")==null?"":rsl.getString("zengfjdw"));	//���۵�λ
					bsh.set(rsl.getString("zhib")+"�۸��۵�λ", 	rsl.getString("koujdw")==null?"":rsl.getString("koujdw"));	//�ۼ۵�λ
					bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("shangx"));		//ָ������
					bsh.set(rsl.getString("zhib")+"����", 		rsl.getDouble("xiax"));			//ָ������
					bsh.set(rsl.getString("zhib")+"���ۿ����",	rsl.getDouble("jis"));			//������ÿ����xx�򽵵�xx��
					bsh.set(rsl.getString("zhib")+"С������",	Jiesdcz.getTransform_Xiaoscl(rsl.getInt("xiaoscl")));		//С������ÿ����xx�򽵵�xx��
					
					Strtmp+="'"+rsl.getString("zhib")+"',";					//��¼�û����õ�Ӱ����㵥�۵�ָ��
				}
			}
//			if(Strtmp.equals("")){
//				
//				if(!bsv.getHetjjfs().equals(Locale.mulj_hetjjfs)){
//					
//					bsv.setErroInfo("ϵͳ��û�з���Ҫ������ۿ��");
//					return false;
//				}
//				
//			}
				
//			ȡ��zhibb��û����hetzkkb�����ֵ���Ŀ��Ŀ����ҲҪ�ŵ���ʽ��ȥ����
			
			if(!Strtmp.equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ("+Strtmp.substring(0,Strtmp.lastIndexOf(","))+") and leib=1 ";
			}else if(!yzb.getYifzzb().equals("")){
				
				sql="select distinct bianm as zhib from zhibb where bianm not in ('"+yzb.getYifzzb()+"') and leib=1 ";
			}else{
				
				sql="select distinct bianm as zhib from zhibb where leib=1 ";
			}
			
			rsl=con.getResultSetList(sql);
			
			String Strtmpdw="";
			
			while(rsl.next()){
				
				Strtmpdw=Jiesdcz.getZhibbdw(rsl.getString("zhib"),"");
				
				bsh.set(rsl.getString("zhib")+Strtmpdw,		0);						//����ֵ
				bsh.set(rsl.getString("zhib")+"������λ", 	Strtmpdw);				//ָ�굥λ
				bsh.set(rsl.getString("zhib")+"���ۿ�����", 	"����");					//���ڵ��ڡ����ڡ�С�ڡ�С�ڵ��ڡ�	���䡢����
				bsh.set(rsl.getString("zhib")+"��������",		0);					//������
				bsh.set(rsl.getString("zhib")+"�۸�����",		0);					//�ۼ�
				bsh.set(rsl.getString("zhib")+"�����۵�λ", 	"");					//���۵�λ
				bsh.set(rsl.getString("zhib")+"�۸��۵�λ", 	"");					//�ۼ۵�λ
				bsh.set(rsl.getString("zhib")+"����", 		0);						//ָ������
				bsh.set(rsl.getString("zhib")+"����", 		0);						//ָ������
				bsh.set(rsl.getString("zhib")+"���ۿ����",	0);						//������ÿ����xx�򽵵�xx��
				bsh.set(rsl.getString("zhib")+"С������",	"");					//С������ÿ����xx�򽵵�xx��
			}
			rsl.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return true;
	}
	
	private void setJieszb(Interpreter bsh){
		
		try {
			
//			�������ۿ�ȡֵ
			yzb.setShul_ht(bsh.get("��ͬ��׼_��������").toString());
			yzb.setShul_yk(Double.parseDouble(bsh.get("ӯ��_��������").toString()));
			yzb.setShul_zdj(Double.parseDouble(bsh.get("�۵���_��������").toString()));
			
//			Qnetar
			yzb.setQnetar_ht(bsh.get("��ͬ��׼_Qnetar").toString());
			yzb.setQnetar_yk(Double.parseDouble(bsh.get("ӯ��_Qnetar").toString()));
			yzb.setQnetar_zdj(Double.parseDouble(bsh.get("�۵���_Qnetar").toString()));
			
//			Std
			yzb.setStd_ht(bsh.get("��ͬ��׼_Std").toString());
			yzb.setStd_yk(Double.parseDouble(bsh.get("ӯ��_Std").toString()));
			yzb.setStd_zdj(Double.parseDouble(bsh.get("�۵���_Std").toString()));
			
//			Ad
			yzb.setAd_ht(bsh.get("��ͬ��׼_Ad").toString());
			yzb.setAd_yk(Double.parseDouble(bsh.get("ӯ��_Ad").toString()));
			yzb.setAd_zdj(Double.parseDouble(bsh.get("�۵���_Ad").toString()));
			
//			Vdaf
			yzb.setVdaf_ht(bsh.get("��ͬ��׼_Vdaf").toString());
			yzb.setVdaf_yk(Double.parseDouble(bsh.get("ӯ��_Vdaf").toString()));
			yzb.setVdaf_zdj(Double.parseDouble(bsh.get("�۵���_Vdaf").toString()));
			
//			Mt
			yzb.setMt_ht(bsh.get("��ͬ��׼_Mt").toString());
			yzb.setMt_yk(Double.parseDouble(bsh.get("ӯ��_Mt").toString()));
			yzb.setMt_zdj(Double.parseDouble(bsh.get("�۵���_Mt").toString()));
			
//			Qgrad
			yzb.setQgrad_ht(bsh.get("��ͬ��׼_Qgrad").toString());
			yzb.setQgrad_yk(Double.parseDouble(bsh.get("ӯ��_Qgrad").toString()));
			yzb.setQgrad_zdj(Double.parseDouble(bsh.get("�۵���_Qgrad").toString()));
			
//			Qbad
			yzb.setQbad_ht(bsh.get("��ͬ��׼_Qbad").toString());
			yzb.setQbad_yk(Double.parseDouble(bsh.get("ӯ��_Qbad").toString()));
			yzb.setQbad_zdj(Double.parseDouble(bsh.get("�۵���_Qbad").toString()));
			
//			Had
			yzb.setHad_ht(bsh.get("��ͬ��׼_Had").toString());
			yzb.setHad_yk(Double.parseDouble(bsh.get("ӯ��_Had").toString()));
			yzb.setHad_zdj(Double.parseDouble(bsh.get("�۵���_Had").toString()));
			
//			Stad
			yzb.setStad_ht(bsh.get("��ͬ��׼_Stad").toString());
			yzb.setStad_yk(Double.parseDouble(bsh.get("ӯ��_Stad").toString()));
			yzb.setStad_zdj(Double.parseDouble(bsh.get("�۵���_Stad").toString()));
			
//			Mad
			yzb.setMad_ht(bsh.get("��ͬ��׼_Mad").toString());
			yzb.setMad_yk(Double.parseDouble(bsh.get("ӯ��_Mad").toString()));
			yzb.setMad_zdj(Double.parseDouble(bsh.get("�۵���_Mad").toString()));
			
//			Aar
			yzb.setAar_ht(bsh.get("��ͬ��׼_Aar").toString());
			yzb.setAar_yk(Double.parseDouble(bsh.get("ӯ��_Aar").toString()));
			yzb.setAar_zdj(Double.parseDouble(bsh.get("�۵���_Aar").toString()));
			
//			Aad
			yzb.setAad_ht(bsh.get("��ͬ��׼_Aad").toString());
			yzb.setAad_yk(Double.parseDouble(bsh.get("ӯ��_Aad").toString()));
			yzb.setAad_zdj(Double.parseDouble(bsh.get("�۵���_Aad").toString()));
			
//			Vad
			yzb.setVad_ht(bsh.get("��ͬ��׼_Vad").toString());
			yzb.setVad_yk(Double.parseDouble(bsh.get("ӯ��_Vad").toString()));
			yzb.setVad_zdj(Double.parseDouble(bsh.get("�۵���_Vad").toString()));
			
//			St
			yzb.setT2_ht(bsh.get("��ͬ��׼_T2").toString());
			yzb.setT2_yk(Double.parseDouble(bsh.get("ӯ��_T2").toString()));
			yzb.setT2_zdj(Double.parseDouble(bsh.get("�۵���_T2").toString()));
			
			//���㵥��
			yzb.setHansmj(Double.parseDouble(bsh.get("����۸�").toString()));
			
		} catch (EvalError e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}
	
//	������õ�����
	private void computData_Dpc(){
		//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
		//ú��
		double _Hansmj=yzb.getHansmj();
		double _Jiessl=yzb.getJiessl();
		double _Meiksl=yzb.getMeiksl();
		yzb.setJiessl_sum(yzb.getJiessl_sum()+_Jiessl);
		
		//ָ���۵���
		double _Qnetar=yzb.getQnetar_zdj();
		double _Std=yzb.getStd_zdj();
		double _Ad=yzb.getAd_zdj();
		double _Vdaf=yzb.getVdaf_zdj();
		double _Mt=yzb.getMt_zdj();
		double _Qgrad=yzb.getQgrad_zdj();
		double _Qbad=yzb.getQbad_zdj();
		double _Had=yzb.getHad_zdj();
		double _Stad=yzb.getStad_zdj();
		double _Mad=yzb.getMad_zdj();
		double _Aar=yzb.getAar_zdj();
		double _Aad=yzb.getAad_zdj();
		double _Vad=yzb.getVad_zdj();
		double _T2=yzb.getT2_zdj();
		double _Shul=yzb.getShul_zdj();
		
		//ָ��ӯ��
		double _Shulyk=yzb.getShul_yk();								//ִ�к�ͬ�еĳ��ֽ�����
		
		double _Jiashj=0;
		double _Jiakhj=0;
		double _Jiaksk=0;
		double _Jine=0;
		double _Buhsmj=0;
		double _Shulzjbz=0;
		double _Hej=0;
		
		//ָ���۽��
		double _Qnetarzje=0;
		double _Stdzje=0;
		double _Adzje=0;
		double _Vdafzje=0;
		double _Mtzje=0;
		double _Qgradzje=0;
		double _Qbadzje=0;
		double _Hadzje=0;
		double _Stadzje=0;
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		
		//�۸������
		_Jiashj=(double)Math.round(_Hansmj*_Jiessl*100)/100;												//��˰�ϼ�
		_Jiakhj=(double) Math.round ((_Jiashj)/(1+_Meiksl)*100)/100;										//�ۿ�ϼ�
		_Jiaksk=(double)Math.round((_Jiashj-_Jiakhj)*100)/100;												//�ۿ�˰��
		_Jine=_Jiakhj;																						//���
		_Buhsmj=(double)Math.round(_Jiakhj/_Jiessl*10000000)/10000000;										//����˰����
		_Shulzjbz=_Hansmj;
		
		//�ϼ�
		_Hej=(double)Math.round((_Jiashj)*100)/100;
		
		//����ӯ�����ۼ۽��
		_Qnetarzje=(double)Math.round(_Qnetar*_Jiessl*100)/100;
		_Stdzje=(double)Math.round(_Std*_Jiessl*100)/100;
		_Adzje=(double)Math.round(_Ad*_Jiessl*100)/100;
		_Vdafzje=(double)Math.round(_Vdaf*_Jiessl*100)/100;
		_Mtzje=(double)Math.round(_Mt*_Jiessl*100)/100;
		_Qgradzje=(double)Math.round(_Qgrad*_Jiessl*100)/100;
		_Qbadzje=(double)Math.round(_Qbad*_Jiessl*100)/100;
		_Hadzje=(double)Math.round(_Had*_Jiessl*100)/100;
		_Stadzje=(double)Math.round(_Stad*_Jiessl*100)/100;
		_Madzje=(double)Math.round(_Mad*_Jiessl*100)/100;
		_Aarzje=(double)Math.round(_Aar*_Jiessl*100)/100;
		_Aadzje=(double)Math.round(_Aad*_Jiessl*100)/100;
		_Vadzje=(double)Math.round(_Vad*_Jiessl*100)/100;
		_T2zje=(double)Math.round(_T2*_Jiessl*100)/100;
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		_Shulzje=(double)Math.round(_Shulzjbz*yzb.getYingksl()*100)/100;	//����������ӯ��
		
		//���㵥��ʾʱָ���۽����
		yzb.setJiashj(yzb.getJiashj()+_Jiashj);
		yzb.setJiakhj(yzb.getJiakhj()+_Jiakhj);
		yzb.setJiaksk(yzb.getJiaksk()+_Jiaksk);
		yzb.setJine(yzb.getJine()+_Jine);
		yzb.setHej(yzb.getHej()+_Hej);
		
		yzb.setQnetar_zje(yzb.getQnetar_zje()+_Qnetarzje);
		yzb.setStd_zje(yzb.getStd_zje()+_Stdzje);
		yzb.setAd_zje(yzb.getAd_zje()+_Adzje);
		yzb.setVdaf_zje(yzb.getVdaf_zje()+_Vdafzje);
		yzb.setMt_zje(yzb.getMt_zje()+_Mtzje);
		yzb.setQgrad_zje(yzb.getQgrad_zje()+_Qgradzje);
		yzb.setQbad_zje(yzb.getQbad_zje()+_Qbadzje);
		yzb.setHad_zje(yzb.getHad_zje()+_Hadzje);
		yzb.setStad_zje(yzb.getStad_zje()+_Stadzje);
		yzb.setMad_zje(yzb.getMad_zje()+_Madzje);
		yzb.setAar_zje(yzb.getAar_zje()+_Aarzje);
		yzb.setAad_zje(yzb.getAad_zje()+_Aadzje);
		yzb.setVad_zje(yzb.getVad_zje()+_Vadzje);
		yzb.setT2_zje(yzb.getT2_zje()+_T2);
		yzb.setShul_zje(yzb.getShul_zje()+_Shulzje);
		
		
		_Qnetar=(double)Math.round(yzb.getQnetar_zje()/yzb.getJiessl_sum()*100)/100;
		_Std=(double)Math.round(yzb.getStd_zje()/yzb.getJiessl_sum()*100)/100;
		_Ad=(double)Math.round(yzb.getAd_zje()/yzb.getJiessl_sum()*100)/100;
		_Vdaf=(double)Math.round(yzb.getVdaf_zje()/yzb.getJiessl_sum()*100)/100;
		_Mt=(double)Math.round(yzb.getMt_zje()/yzb.getJiessl_sum()*100)/100;
		_Qgrad=(double)Math.round(yzb.getQgrad_zje()/yzb.getJiessl_sum()*100)/100;
		_Qbad=(double)Math.round(yzb.getQbad_zje()/yzb.getJiessl_sum()*100)/100;
		_Had=(double)Math.round(yzb.getHad_zje()/yzb.getJiessl_sum()*100)/100;
		_Stad=(double)Math.round(yzb.getStad_zje()/yzb.getJiessl_sum()*100)/100;
		_Mad=(double)Math.round(yzb.getMad_zje()/yzb.getJiessl_sum()*100)/100;
		_Aar=(double)Math.round(yzb.getAar_zje()/yzb.getJiessl_sum()*100)/100;
		_Aad=(double)Math.round(yzb.getAad_zje()/yzb.getJiessl_sum()*100)/100;
		_Vad=(double)Math.round(yzb.getVad_zje()/yzb.getJiessl_sum()*100)/100;
		_T2=(double)Math.round(yzb.getT2_zje()/yzb.getJiessl_sum()*100)/100;
		_Shulzjbz=(double)Math.round(yzb.getShul_zje()/yzb.getJiessl_sum()*100)/100;
		_Buhsmj=(double)Math.round(yzb.getJiakhj()/yzb.getJiessl_sum()*100)/100;
		
		yzb.setQnetar_zdj(_Qnetar);
		yzb.setStd_zdj(_Std);
		yzb.setAd_zdj(_Ad);
		yzb.setVdaf_zdj(_Vdaf);
		yzb.setMt_zdj(_Mt);
		yzb.setQgrad_zdj(_Qgrad);
		yzb.setQbad_zdj(_Qbad);
		yzb.setHad_zdj(_Had);
		yzb.setStad_zdj(_Stad);
		yzb.setMad_zdj(_Mad);
		yzb.setAar_zdj(_Aar);
		yzb.setAad_zdj(_Aad);
		yzb.setVad_zdj(_Vad);
		yzb.setT2_zdj(_T2);
		yzb.setShulzjbz(_Shulzjbz);
		yzb.setBuhsmj(_Buhsmj);
	}
	
//	������ü�Ȩ
	private void computData(){
		//����ú��,�����ۼ�,���ۼ�,���۵��ۼ�
		//ú��
		double _Hansmj=yzb.getHansmj();
		double _Jiessl=yzb.getJiessl();
		double _Meiksl=yzb.getMeiksl();
		
		//ָ���۵���
		double _Qnetar=yzb.getQnetar_zdj();
		double _Std=yzb.getStd_zdj();
		double _Ad=yzb.getAd_zdj();
		double _Vdaf=yzb.getVdaf_zdj();
		double _Mt=yzb.getMt_zdj();
		double _Qgrad=yzb.getQgrad_zdj();
		double _Qbad=yzb.getQbad_zdj();
		double _Had=yzb.getHad_zdj();
		double _Stad=yzb.getStad_zdj();
		double _Mad=yzb.getMad_zdj();
		double _Aar=yzb.getAar_zdj();
		double _Aad=yzb.getAad_zdj();
		double _Vad=yzb.getVad_zdj();
		double _T2=yzb.getT2_zdj();
		double _Shul=yzb.getShul_zdj();
		
		//ָ��ӯ��
		double _Shulyk=yzb.getShul_yk();		//ִ�к�ͬ�еĳ��ֽ�����
		
		double _Jiashj=0;
		double _Jiakhj=0;
		double _Jiaksk=0;
		double _Jine=0;
		double _Buhsmj=0;
		double _Shulzjbz=0;
//		double _Yunfsk=0;
//		double _Yunzfhj=0;
//		double _Buhsyf=0;
		double _Hej=0;
		
		//ָ���۽��
		double _Qnetarzje=0;
		double _Stdzje=0;
		double _Adzje=0;
		double _Vdafzje=0;
		double _Mtzje=0;
		double _Qgradzje=0;
		double _Qbadzje=0;
		double _Hadzje=0;
		double _Stadzje=0;
		double _Madzje=0;
		double _Aarzje=0;
		double _Aadzje=0;
		double _Vadzje=0;
		double _T2zje=0;
		double _Shulzje=0;
		
		//�۸������
		_Jiashj=(double)Math.round(_Hansmj*_Jiessl*100)/100;												//��˰�ϼ�
		_Jiakhj=(double) Math.round ((_Jiashj)/(1+_Meiksl)*100)/100;										//�ۿ�ϼ�
		_Jiaksk=(double)Math.round((_Jiashj-_Jiakhj)*100)/100;												//�ۿ�˰��
		_Jine=_Jiakhj;																						//���
		_Buhsmj=(double)Math.round(_Jiakhj/_Jiessl*10000000)/10000000;										//����˰����
		_Shulzjbz=_Hansmj;
		
		//�����˷���
//		_Yunzfhj=Math.round((_Tielyf+_Tielzf+_Kuangqyf+_Kuangqzf)*100)/100;									//���ӷѺϼ�
//		_Yunfsk=(double)Math.round(((double)Math.round(_Tielyf*_Yunfsl*100)/100+_Kuangqsk)*100)/100;		//�˷�˰��		
//		_Buhsyf=(double)Math.round(((double)Math.round((_Yunzfhj-_Yunfsk)*100)/100+_Kuangqjk)*100)/100;	//����˰�˷�
		
		//�ϼ�
		_Hej=(double)Math.round((_Jiashj)*100)/100;
		
		//����ӯ�����ۼ۽��
		_Qnetarzje=(double)Math.round(_Qnetar*_Jiessl*100)/100;
		_Stdzje=(double)Math.round(_Std*_Jiessl*100)/100;
		_Adzje=(double)Math.round(_Ad*_Jiessl*100)/100;
		_Vdafzje=(double)Math.round(_Vdaf*_Jiessl*100)/100;
		_Mtzje=(double)Math.round(_Mt*_Jiessl*100)/100;
		_Qgradzje=(double)Math.round(_Qgrad*_Jiessl*100)/100;
		_Qbadzje=(double)Math.round(_Qbad*_Jiessl*100)/100;
		_Hadzje=(double)Math.round(_Had*_Jiessl*100)/100;
		_Stadzje=(double)Math.round(_Stad*_Jiessl*100)/100;
		_Madzje=(double)Math.round(_Mad*_Jiessl*100)/100;
		_Aarzje=(double)Math.round(_Aar*_Jiessl*100)/100;
		_Aadzje=(double)Math.round(_Aad*_Jiessl*100)/100;
		_Vadzje=(double)Math.round(_Vad*_Jiessl*100)/100;
		_T2zje=(double)Math.round(_T2*_Jiessl*100)/100;
//		_Shulzje=(double)Math.round(_Shul*_Jiessl*100/100);		��¼������ͬ��׼�İ��ֽ������㷨
		_Shulzje=(double)Math.round(_Shulzjbz*yzb.getYingksl()*100)/100;	//����������ӯ��
		
		//���㵥��ʾʱָ���۽����
		yzb.setJiashj(_Jiashj);
		yzb.setJiakhj(_Jiakhj);
		yzb.setJiaksk(_Jiaksk);
		yzb.setJine(_Jine);
		yzb.setBuhsmj(_Buhsmj);
		yzb.setShulzjbz(_Shulzjbz);
//		yzb.setYunfsk(_Yunfsk);
//		yzb.setBuhsyf(_Buhsyf);
//		yzb.setYunzfhj(_Yunzfhj);
//		yzb.setYunfsk(_Yunfsk);
//		yzb.setBuhsyf(_Buhsyf);
		yzb.setHej(_Hej);
		yzb.setQnetar_zje(_Qnetarzje);
		yzb.setStd_zje(_Stdzje);
		yzb.setAd_zje(_Adzje);
		yzb.setVdaf_zje(_Vdafzje);
		yzb.setMt_zje(_Mtzje);
		yzb.setQgrad_zje(_Qgradzje);
		yzb.setQbad_zje(_Qbadzje);
		yzb.setHad_zje(_Hadzje);
		yzb.setStad_zje(_Stadzje);
		yzb.setMad_zje(_Madzje);
		yzb.setAar_zje(_Aarzje);
		yzb.setAad_zje(_Aadzje);
		yzb.setVad_zje(_Vadzje);
		yzb.setT2_zje(_T2);
		yzb.setShul_zje(_Shulzje);
	}
}
package com.zhiren.dc.huophd;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import bsh.Interpreter;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;

/**
 * @author wl TODO 货票单
 */

public class Huopd extends BasePage {
	
	// ---------- 报错信息 -------------//
	private String _msg;
	
	public String getMsg() {
		return _msg;
	}
	
	public void setMsg(String ms) {
		_msg = MainGlobal.getExtMessageBox(ms,false);
	}
	
	protected void initialize() {
		_msg = "";
	}
	
	// -------------------- 货票表格 ----------------------//
	
	private Huopdxgbean _EditValue;
	
	
	
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}
	
	
	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}
	
	
	public Huopdxgbean getEditValue() {
		return _EditValue;
	}
	
	
	public void setEditValue(Huopdxgbean EditValue) {
		_EditValue = EditValue;
	}
	
//	1、有标准货票 
//	2、用户设定了矿和费用项目的关系
//	3、他很懒什么都没留下
	public void getSelectData(String Fahrq,long Diancxxb_id,long Faz_id,long Daoz_id,long Feiylbb_id,
			String Meikxxb_id,String Biaoz,int Chebb_id,String Feiymx,String SelectChepbId,String Lianpdp,
			String Yansbh) {
//		初始化货票费用界面
		JDBCcon con = new JDBCcon();
		List _editvalues = new ArrayList();
		try{
			
			if(getEditValues()!=null){
				
				getEditValues().clear();
			}
			String mstr_fahrq = Fahrq;
			String mstr_faz = "";
			String mstr_daoz = "";
			String mstr_chephs = "";
			String mstr_cheb=MainGlobal.getCheb(Chebb_id);
			String mstr_biaozs =Biaoz;
			
			String mstr_lic = "";
			double mdb_baojf=0;
			String mstr_fahdw="";
			
			String Meikxx[]=Meikxxb_id.split(",");
			for(int i=0;i<Meikxx.length;i++){
				
				mstr_fahdw+=MainGlobal.getTableCol("meikxxb", "mingc", "id", Meikxx[i])+",";
			}
			mstr_fahdw=mstr_fahdw.substring(0,mstr_fahdw.lastIndexOf(","));
			 
			String mstr_diancmc = MainGlobal.getTableCol("diancxxb", "mingc", "id", String.valueOf(Diancxxb_id));
			
			double mdb_Hej = 0.0;
			String Feiymc[] = new String[19];
			double Jine[] = new double[19];
			long  FeiyxmId[]=new long[19];
			int Shuib[]= new int[19];
			String sql="";
			
			ResultSetList rs=null;
//			情况1：有标准货票
			sql="select distinct fyxm.id,fymc.mingc,bzfy.zhi,bzfy.shuib \n"
		       + " from biaozhpb bzhp,biaozfyb bzfy,feiyxmb fyxm,feiymcb fymc	\n"
		       + "     where bzhp.id=bzfy.biaozhpb_id	\n" 
		       + "           and bzfy.feiyxmb_id=fyxm.id and fyxm.feiymcb_id=fymc.id \n"
		       + "	         and (bzhp.diancxxb_id="+Diancxxb_id+" or bzhp.diancxxb_id in \n"
		       + "				(select id from diancxxb where jib=3 and id in (select fuid from diancxxb where id = "
		       +Diancxxb_id+"))) and bzhp.faz_id="+Daoz_id+" and bzhp.feiylbb_id="+Feiylbb_id+" \n"
		       + "			 and bzhp.diancxxb_id=fyxm.diancxxb_id \n"
		       + "           and bzhp.daoz_id="+Daoz_id+" and bzhp.meikxxb_id in ("+Meikxxb_id+") and bzhp.biaoz="+Biaoz+" and bzhp.cheb="+Chebb_id+" \n"
		       + "		order by fymc.mingc	";
			
			rs=con.getResultSetList(sql);
			
			if(rs.getRows()>0){
				
				int i=0;
				double m_hej=0;
				while(rs.next()){
					
					FeiyxmId[i]=rs.getLong("id");
					Feiymc[i]=rs.getString("mingc");
					Jine[i]=rs.getDouble("zhi");
					Shuib[i]=rs.getInt("shuib");
					m_hej+=Jine[i];
					i++;
				}
				
				mstr_faz=getFeiymxjx(Feiymx,"发站:");
				mstr_daoz=getFeiymxjx(Feiymx,"到站:");
				mstr_lic=getFeiymxjx(Feiymx,"里程:");
				mdb_Hej=m_hej;
				((Visit) getPage().getVisit()).setInt3(i);
			
			}else{
//				情况2：用户设定了矿和费用项目的关系
				sql="select distinct fyxm.id,fymc.mingc,fyxm.gongs,fyxm.shuib \n "
			       + " from feiyxmb fyxm,feiymcb fymc,feiyglb glb,feiyxmmkglb fyxmglb	\n"
			       + "     where fyxm.feiymcb_id=fymc.id and fyxmglb.feiyxmb_id=fyxm.id \n" 
			       + "           and fyxmglb.feiyglb_id=glb.id and glb.feiylbb_id="+Feiylbb_id+" \n" 
			       + "			 and fyxm.diancxxb_id=glb.diancxxb_id \n"
			       + "           and (glb.diancxxb_id="+Diancxxb_id+" or glb.diancxxb_id in \n"
			       + "		(select id from diancxxb where jib=3 and id in (select fuid from diancxxb where id = "+Diancxxb_id+")))" 
			       +" and glb.meikxxb_id in ("+Meikxxb_id+") and fyxmglb.shifsy=1 order by fymc.mingc";
				
				rs=con.getResultSetList(sql);
				
				if(rs.getRows()>0){
					
					int i=0;
					double m_hej=0;
					Interpreter bsh=new Interpreter();
					Jiesdcz jscz=new Jiesdcz();
					String Huophdgs="";
					Huophdgs=getHuophdgs(Diancxxb_id)+"\n";	//得到货票核对公式
					
					//设置公式
					bsh=jscz.getYunfgsjx(Diancxxb_id,Faz_id,Daoz_id,Biaoz,mstr_cheb,SelectChepbId,Lianpdp);
					while(rs.next()){
						
						FeiyxmId[i]=rs.getLong("id");
						Feiymc[i]=rs.getString("mingc");
						((Visit) getPage().getVisit()).setString9(rs.getString("mingc"));
						if(rs.getString("gongs").indexOf(rs.getString("mingc")+"=")>-1){
							
							bsh.eval(Huophdgs+rs.getString("gongs"));
						}else{
							
							bsh.eval(Huophdgs+rs.getString("mingc")+"="+rs.getString("gongs")+";");
						}
						
						Jine[i]=Double.valueOf(bsh.get(rs.getString("mingc")).toString()).doubleValue();
						Shuib[i]=rs.getInt("shuib");
						m_hej+=Jine[i];
						i++;
					}
					
					mstr_faz=MainGlobal.getTableCol("chezxxb", "mingc", "id", String.valueOf(Faz_id));
					mstr_daoz=MainGlobal.getTableCol("chezxxb", "mingc", "id", String.valueOf(Daoz_id));
					mstr_lic=getLic(Diancxxb_id,Faz_id,Daoz_id);
					mdb_Hej=m_hej;
					((Visit) getPage().getVisit()).setInt3(i);
				
				}else{
					
//					情况3：用户没有配置矿的费用信息
					mstr_faz=MainGlobal.getTableCol("chezxxb", "mingc", "id", String.valueOf(Faz_id));
					mstr_daoz=MainGlobal.getTableCol("chezxxb", "mingc", "id", String.valueOf(Daoz_id));
					mstr_lic=getLic(Diancxxb_id,Faz_id,Daoz_id);
					((Visit) getPage().getVisit()).setInt3(0);
					this.setMsg("没有为煤矿："+mstr_fahdw+"设置费用，请选择\"新增费用\"按钮！");
				}
			}
			
			String tmp[][]=this.getChepb_info("", 1, "cheph", SelectChepbId);
			
			if(tmp!=null){
				
				for(int i=0;i<tmp.length;i++){
					
					for(int j=0;j<tmp[i].length;j++){
						
						mstr_chephs+=tmp[i][j]+",";
					}
				}
				
				mstr_chephs=mstr_chephs.substring(0,mstr_chephs.lastIndexOf(","));
			}
			
//			如果是单票就显示一个车皮
			if(Lianpdp.equals("dp")){
				
				if(mstr_chephs.indexOf(",")>-1){
					
					mstr_chephs=mstr_chephs.substring(0,mstr_chephs.indexOf(","));
				}
			}
			
			_editvalues.add(new Huopdxgbean(Faz_id, mstr_faz, Daoz_id, mstr_daoz,
					mstr_cheb, mstr_biaozs,mstr_fahrq, 
					mstr_lic,mdb_baojf,mstr_fahdw, mstr_diancmc,
					mstr_chephs,"",Yansbh,mdb_Hej,
					Feiymc[0],Jine[0],Feiymc[1],Jine[1],
					Feiymc[2], Jine[2],Feiymc[3], Jine[3], 
					Feiymc[4], Jine[4], Feiymc[5], Jine[5], 
					Feiymc[6], Jine[6], Feiymc[7], Jine[7], 
					Feiymc[8], Jine[8], Feiymc[9], Jine[9],
					Feiymc[10], Jine[10], Feiymc[11], Jine[11], 
					Feiymc[12], Jine[12],	Feiymc[13], Jine[13], 
					Feiymc[14], Jine[14], Feiymc[15], Jine[15],
					Feiymc[16], Jine[16], Feiymc[17], Jine[17], 
					Feiymc[18], Jine[18],	
					FeiyxmId[0],FeiyxmId[1],FeiyxmId[2],FeiyxmId[3],
					FeiyxmId[4],FeiyxmId[5],FeiyxmId[6],FeiyxmId[7],
					FeiyxmId[8],FeiyxmId[9],FeiyxmId[10],FeiyxmId[11],
					FeiyxmId[12],FeiyxmId[13],FeiyxmId[14],FeiyxmId[15],
					FeiyxmId[16],FeiyxmId[17],FeiyxmId[18],
					Shuib[0], Shuib[1], Shuib[2], Shuib[3], 
					Shuib[4], Shuib[5], Shuib[6], Shuib[7], 
					Shuib[8], Shuib[9], Shuib[10], Shuib[11], 
					Shuib[12], Shuib[13], Shuib[14], Shuib[15], 
					Shuib[16], Shuib[17], Shuib[18]));
			
			setEditValues(_editvalues);
			
		}catch(Exception e){
			
			
			this.setMsg(((Visit) getPage().getVisit()).getString9()+"费用设置有问题，请检查！");
			_editvalues.add(new Huopdxgbean());
			setEditValues(_editvalues);
//			e.printStackTrace();
			
			
		}finally{
			
			con.Close();
		}
	}

	private String getHuophdgs(long _Diancxxb_id) throws SQLException, IOException{
//		得到货票的计算公式
//		2008-12-23 新增Round方法，用于四舍五入（四舍六入五看双）等同于（Round_new）
		JDBCcon con =new JDBCcon();
		String Gongs="";
		   
		   //煤款结算公式
		ResultSet rs= con.getResultSet("select id from gongsb where mingc='货票核对' and leix='结算' and zhuangt=1 \n"
									+ " and (diancxxb_id="+_Diancxxb_id+" or diancxxb_id in (select id from diancxxb \n"
									+ " where jib=3 and id in (select fuid from diancxxb where id="+_Diancxxb_id+")))");
		if (rs.next()) {
	   	
			DataBassUtil clob=new DataBassUtil();
			Gongs=clob.getClob("gongsb", "gongs", rs.getLong(1));
	   }
	   rs.close();
	   con.Close();
	   return Gongs;
	}

	private String getLic(long Diancxxb_id, long Faz_id, long Daoz_id) {
		// TODO 自动生成方法存根
		
		JDBCcon con=new JDBCcon();
		String lic="";
		try{
			
			String sql="select zhi from licb where faz_id="+Faz_id+" and daoz_id="+Daoz_id+" and \n"
				+ " (diancxxb_id="+Diancxxb_id+" or diancxxb_id in (select id from diancxxb where jib=3 and id in \n"
				+ " (select fuid from diancxxb where id="+Diancxxb_id+")))";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				lic=rs.getString("zhi");
			}
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return lic;
	}

	private String[][] getChepb_info(String CondiTion,int ReturnColCon,String ReturncCol,String SelectChepbId) {
		// TODO 自动生成方法存根
//		返回车皮表中的信息
//		ReturncCol字段中的字符用“^~”分割   
		JDBCcon con=new JDBCcon();
		int i=0;
		try{
			
			String Coltmp[]=new String[ReturnColCon];
			Coltmp=ReturncCol.split("^~");
			String FinalCol=ReturncCol.replaceAll("^~", ",");
				
			String sql="select "+CondiTion+" "+FinalCol+" from chepb where id in("+SelectChepbId+")";
			ResultSet rs=con.getResultSet(sql);
			String Rettmp[][] =new String[JDBCcon.getRow(rs)][ReturnColCon];
			
			while(rs.next()){
				
				for(int j=0;j<Rettmp[i].length;j++){
					
					Rettmp[i][j]=rs.getString(Coltmp[j]);
				}
				i++;
			}	
				
			rs.close();
			return 	Rettmp;
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return null;
	}
	
	


	private String getFeiymxjx(String feiymx,String string) {
		// TODO 自动生成方法存根
		if(!feiymx.equals("")){
			
			String strtmp=feiymx.substring(feiymx.indexOf(string));
			return strtmp.substring(strtmp.indexOf(":"),strtmp.indexOf(" "));
		}else{
			
			return "";
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		// //////////////////////////////////////////////getDefaultItems();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setList1(null);//getEditValues()
			visit.setActivePageName(getPageName().toString());
//			visit.getLong1();	电厂信息表id
//			visit.getLong2();	费用类别表id
//			visit.getLong3();	煤矿信息id(已作废)
//			visit.getLong4();	发站id
//			visit.getLong5();	到站id
//			visit.getInt2();	车别
//			visit.getString2(); 单票或联票
//			visit.getString3() 	标准货票中费用明细信息，单车核对模块中左下角内容
//			visit.getString7()	发货日期
//			visit.getString4();	选中车号的id串
//			visit.getString5();	票重(单票为单车票重、联票为所选车皮所有票重)
//			visit.getString8();	验收编号
//			visit.getString12();煤矿信息id
			visit.setInt3(0);	//初始化feiyxm数量
			visit.setString9("");	//保存货票错误信息
			visit.setPagePreferences("");	//清空该页面像Feixm传参参数
		}
		
		getSelectData(visit.getString7(),visit.getLong1(),visit.getLong4(),visit.getLong5(),
				visit.getLong2(),visit.getString12(),visit.getString5(),visit.getInt2(),
				visit.getString3(),visit.getString4(),visit.getString2(),visit.getString8()); 
	}
	
	
	
	// --------------- 给出变量取值
	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.tapestry.AbstractPage#detach()
	 */
	public void detach() {
		// TODO 自动生成方法存根
		super.detach();
	}
	
	
	private String getFeiyxm_info(int Index,String Type) {
//		取回页面费用类型:费用名称，费用金额，费用项目表id，费用税别
//		Type:Shuib、Feiymc、Feiyje、FeiyxmId
		String Strtmp="";
		if(Type.equals("Feiymc")){
			
			switch(Index){
			
				case 0:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc0();
					break;
					
				case 1:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc1();
					break;
				
				case 2:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc2();
					break;	
				
				case 3:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc3();
					break;	
					
				case 4:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc4();
					break;	
					
				case 5:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc5();
					break;	
					
				case 6:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc6();
					break;	
					
				case 7:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc7();
					break;	
					
				case 8:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc8();
					break;	
				
				case 9:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc9();
					break;	
					
				case 10:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc10();
					break;		
			
				case 11:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc11();
					break;	
					
				case 12:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc12();
					break;		
					
				case 13:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc13();
					break;		
					
				case 14:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc14();
					break;	
					
				case 15:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc15();
					break;	
					
				case 16:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc16();
					break;	
					
				case 17:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc17();
					break;	
					
				case 18:
					Strtmp=((Huopdxgbean) getEditValues().get(0)).getFeiymc18();
					break;		
			}
			
			
		}else if(Type.equals("Feiyje")){
			
			switch(Index){
			
				case 0:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine0());
					break;
					
				case 1:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine1());
					break;
				
				case 2:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine2());
					break;	
				
				case 3:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine3());
					break;	
					
				case 4:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine4());
					break;	
					
				case 5:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine5());
					break;	
					
				case 6:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine6());
					break;	
					
				case 7:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine7());
					break;	
					
				case 8:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine8());
					break;	
				
				case 9:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine9());
					break;	
					
				case 10:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine10());
					break;		
			
				case 11:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine11());
					break;	
					
				case 12:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine12());
					break;		
					
				case 13:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine13());
					break;		
					
				case 14:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine14());
					break;	
					
				case 15:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine15());
					break;	
					
				case 16:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine16());
					break;	
					
				case 17:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine17());
					break;	
					
				case 18:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getJine18());
					break;
			}	
		
		}else if(Type.equals("FeiyxmId")){
			
			switch(Index){
			
				case 0:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID0());
					break;
					
				case 1:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID1());
					break;
				
				case 2:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID2());
					break;	
				
				case 3:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID3());
					break;	
					
				case 4:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID4());
					break;	
					
				case 5:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID5());
					break;	
					
				case 6:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID6());
					break;	
					
				case 7:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID7());
					break;	
					
				case 8:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID8());
					break;	
				
				case 9:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID9());
					break;	
					
				case 10:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID10());
					break;		
			
				case 11:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID11());
					break;	
					
				case 12:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID12());
					break;		
					
				case 13:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID13());
					break;		
					
				case 14:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID14());
					break;	
					
				case 15:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID15());
					break;	
					
				case 16:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID16());
					break;	
					
				case 17:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID17());
					break;	
					
				case 18:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getHPFEIYID18());
					break;
			}
			
		}else if(Type.equals("Shuib")){
			
			
				switch(Index){
				
				case 0:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib0());
					break;
					
				case 1:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib1());
					break;
				
				case 2:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib2());
					break;	
				
				case 3:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib3());
					break;	
					
				case 4:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib4());
					break;	
					
				case 5:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib5());
					break;	
					
				case 6:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib6());
					break;	
					
				case 7:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib7());
					break;	
					
				case 8:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib8());
					break;	
				
				case 9:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib9());
					break;	
					
				case 10:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib10());
					break;		
			
				case 11:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib11());
					break;	
					
				case 12:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib12());
					break;		
					
				case 13:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib13());
					break;		
					
				case 14:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib14());
					break;	
					
				case 15:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib15());
					break;	
					
				case 16:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib16());
					break;	
					
				case 17:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib17());
					break;	
					
				case 18:
					Strtmp=String.valueOf(((Huopdxgbean) getEditValues().get(0)).getShuib18());
					break;
			}
		}
		
		
		return Strtmp;
	}
	
	
	protected String getpageLink(String Pagename) {
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		IEngine engine = cycle.getEngine();
		IEngineService pageService = engine.getService(Tapestry.PAGE_SERVICE);
		ILink link = pageService
				.getLink(cycle, this, new String[] { Pagename });
		return link.getURL();
	}
	
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private boolean _SaveChick = false;
	
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _FeiylxAddChick = false;
	
	public void FeiylxAddButton(IRequestCycle cycle) {
		_FeiylxAddChick = true;
	}
	
	
//	private boolean _FeiylxDelChick = false;
	
	
//	
//	public void FeiylxDelButton(IRequestCycle cycle) {
//		_FeiylxDelChick = true;
//	}
	
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			
//			long Diancxxb_id,String ChepbId,
//			String Biaoz,long Feiylbb_id,
//			long Meikxxb_id,long Faz_id,
//			long Daoz_id,int Chebb_id,
//			String Yansbh,String Type
			
			Save_Hp(((Visit) getPage().getVisit()).getLong1(),((Visit) getPage().getVisit()).getString4(),
				((Visit) getPage().getVisit()).getString5(),((Visit) getPage().getVisit()).getLong2(),
				((Visit) getPage().getVisit()).getString12(),((Visit) getPage().getVisit()).getLong4(),
				((Visit) getPage().getVisit()).getLong5(),((Visit) getPage().getVisit()).getInt2(),
				((Visit) getPage().getVisit()).getString8(),((Visit) getPage().getVisit()).getString2(),
				cycle,((Visit) getPage().getVisit()).getString11());
		}
			
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
		if (_FeiylxAddChick) {
			_FeiylxAddChick = false;
			FeiylxAdd(cycle);
		}
//		if (_FeiylxDelChick) {
//			_FeiylxDelChick = false;
////			FeiylxDel(cycle);
//		}
	}
	
	
	private void FeiylxAdd(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		
		((Visit) getPage().getVisit()).setPagePreferences("Huopd");
		cycle.activate("Feiyxm");
	}

	private void Save_Hp(long Diancxxb_id,String ChepbId,String Biaoz,long Feiylbb_id,String Meikxxb_id,
				long Faz_id,long Daoz_id,int Chebb_id,String Yansbh,String Type,IRequestCycle cycle,String LpBiaoz) {
//		单票保存
		JDBCcon con = new JDBCcon();
		int flag=0;
		boolean success=true;
		try {

			String ChepId[]=null;
			if(ChepbId!=null&&!ChepbId.equals("")){
				
				ChepId=ChepbId.split(",");
			}	
			
			String Lpbiaoz[]=null;
			if(LpBiaoz!=null&&!LpBiaoz.equals("")){
				
				Lpbiaoz=LpBiaoz.split(",");
			}
				
			double mdb_Tmp[]=null;
			long Yansbhb_id=MainGlobal.getTableId("yansbhb", "bianm", Yansbh);
//			操作feiyb
//				mdb_tmp[0]=保存标识位
//				mdb_tmp[1]=总金额
//				mdb_tmp[2]=费用组ID
			mdb_Tmp=SaveFeiyb(Diancxxb_id);
				
//			操作yunfdjb,danjcpb
			if(mdb_Tmp[0]>=0){
//				mdb_tmp[0]=保存标识位
				mdb_Tmp=SaveYunfdjb(ChepId,Diancxxb_id,(long)mdb_Tmp[2],Biaoz,mdb_Tmp[1],Feiylbb_id,Yansbhb_id,Type,Lpbiaoz);
				
				if(mdb_Tmp[0]>=0){
					
					flag=UpdateBiaozhpb(Diancxxb_id,Feiylbb_id,Meikxxb_id,Faz_id,Daoz_id,Biaoz,Chebb_id);
					
						if(flag>=0){
							
							success=true;
						}else{
							
							success=false;
						}
				}else{
					
					success=false;
				}
			}else{
				
				success=false;
			}
			

			if(success){
				
				setMsg("操作成功！");
				cycle.activate("Danchd");
			}else{
				
				setMsg("操作失败！");
			}
			
		} catch (Exception e) {
			setMsg("操作失败！");
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	
	
	private int UpdateBiaozhpb(long Diancxxb_id, long Feiylbb_id, String Meikxxb_id, long Faz_id, 
				long Daoz_id, String Biaoz, int Chebb_id) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		int flag=0;
		try{
			long BiaozhpbId=0;
			
			String sql="select id from BIAOZHPB bzhp \n"
			       + " where bzhp.diancxxb_id="+Diancxxb_id+" and bzhp.feiylbb_id="+Feiylbb_id+"	\n" 
			       + " and meikxxb_id in ("+Meikxxb_id+") and faz_id="+Faz_id+" and daoz_id="+Daoz_id+"	\n"
			       + " and biaoz="+Biaoz+" and cheb="+Chebb_id;
			
			ResultSet rs=con.getResultSet(sql);
			if(JDBCcon.getRow(rs)>0){
//				已存在标准货票
				StringBuffer sbsql=new StringBuffer("begin	\n");
				while(rs.next()){
					
					BiaozhpbId=rs.getLong("id");
					sbsql.append(" delete from BIAOZFYB where BIAOZHPB_ID=").append(BiaozhpbId).append(";	\n");
					
					for(int j=0;j<((Visit) getPage().getVisit()).getInt3();j++){
						
						sbsql.append(" insert into biaozfyb (id, biaozhpb_id, feiyxmb_id, shuib, zhi)	\n"
									+ "  values (getnewid("+Diancxxb_id+"), "+BiaozhpbId+", "+this.getFeiyxm_info(j,"FeiyxmId")+", "+this.getFeiyxm_info(j,"Shuib")+", "+this.getFeiyxm_info(j,"Feiyje")+"); \n");
					}
				}
				
//				sql=" delete from BIAOZFYB where BIAOZHPB_ID="+BiaozhpbId;
				sbsql.append("end;");
				if(sbsql.length()>13){
					
					flag=con.getDelete(sbsql.toString());
				}
				
			}else{
//				未存在标准货票
				
				String Meikxx[]=Meikxxb_id.split(",");
				StringBuffer sbsql=new StringBuffer("begin	\n");
				for(int i=0;i<Meikxx.length;i++){
					
					BiaozhpbId=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
					sbsql.append("insert into biaozhpb (id, diancxxb_id, feiylbb_id, meikxxb_id, faz_id, daoz_id, biaoz, cheb) \n"
						+ " values ("+BiaozhpbId+", "+Diancxxb_id+", "+Feiylbb_id+", "+Meikxx[i]+", " +
						""+Faz_id+", "+Daoz_id+", "+Biaoz+", "+Chebb_id+");\n");
				
					for(int j=0;j<((Visit) getPage().getVisit()).getInt3();j++){
						
						sbsql.append("insert into biaozfyb (id, biaozhpb_id, feiyxmb_id, shuib, zhi)	\n"
									+ " values (getnewid("+Diancxxb_id+"), "+BiaozhpbId+", "+this.getFeiyxm_info(j,"FeiyxmId")+", "+this.getFeiyxm_info(j,"Shuib")+", "+this.getFeiyxm_info(j,"Feiyje")+"); \n");
					}
				}
				sbsql.append("end;");
				
				if(sbsql.length()>13){
					
					flag=con.getInsert(sbsql.toString());
				}
			}
			rs.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		
		return flag;
	}


	private double[] SaveYunfdjb(String[] ChepId, long Diancxxb_id, long FeiyfzId, String Biaoz, 
			double Zongje, long Feiylbb_id, long Yansbhb_id,String Type,String[] LpBiaoz) {
		// TODO 自动生成方法存根
		
		int flag=0;
		long Yunfdjb_id=0;
		double Yunfdjb[]=new double[1];
		JDBCcon con=new JDBCcon();
		StringBuffer SBsql = new StringBuffer("begin \n");
		
		if(Type.equals("dp")){
			
			for(int i=0;i<ChepId.length;i++){
				
				Yunfdjb_id=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
				SBsql.append("insert into yunfdjb (id, danjbh, feiyb_id, ches, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id)	\n"
							+ " values ("+Yunfdjb_id+",'"+((Huopdxgbean) getEditValues().get(0)).getPiaojbh()+"',"+FeiyfzId+",1,"+Biaoz+", "+Zongje+", " +
								"'"+((Visit) getPage().getVisit()).getRenymc()+"', sysdate, " +
										"'"+((Huopdxgbean) getEditValues().get(0)).getBeiz()+"', "+Feiylbb_id+"); \n");
				
				SBsql.append("insert into danjcpb (id, yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl)	\n"
							+ " values (getnewid("+Diancxxb_id+"), "+Yunfdjb_id+", "+ChepId[i]+", 0, "+Yansbhb_id+", "+Biaoz+"); \n");
				
				SBsql.append("update chepb set hedbz=4 where id="+ChepId[i]+"; \n");
			}
		}else if(Type.equals("lp")){
			
			Yunfdjb_id=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
			SBsql.append("insert into yunfdjb (id, danjbh, feiyb_id, ches, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id)	\n"
						+ " values ("+Yunfdjb_id+",'"+((Huopdxgbean) getEditValues().get(0)).getPiaojbh()+"',"+FeiyfzId+","+ChepId.length+", "+Biaoz+", "+Zongje+", " +
							"'"+((Visit) getPage().getVisit()).getRenymc()+"', sysdate, " +
									"'"+((Huopdxgbean) getEditValues().get(0)).getBeiz()+"', "+Feiylbb_id+"); \n");
			
			for(int i=0;i<ChepId.length;i++){
				
				SBsql.append("insert into danjcpb (id, yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl)	\n"
						+ " values (getnewid("+Diancxxb_id+"), "+Yunfdjb_id+", "+ChepId[i]+", 0, "+Yansbhb_id+", "+LpBiaoz[i]+"); \n");
				
				SBsql.append("update chepb set hedbz=4 where id="+ChepId[i]+"; \n");
			}
		}
		
		SBsql.append(" end;");
		flag=con.getInsert(SBsql.toString());
		con.Close();
		
		Yunfdjb[0]=flag;
		return Yunfdjb;
	}


	private double[] SaveFeiyb(long Diancxxb_id) {
		// TODO 自动生成方法存根
//		保持Feiyb表
		JDBCcon con=new JDBCcon();
		double mdb_Jine=0;
		int flag=0;
		double Feiyb[]=new double[3]; 
		long FeiyfzId=Long.parseLong(MainGlobal.getNewID(Diancxxb_id)),Id=0;
		StringBuffer SBsql = new StringBuffer("begin \n");
		for(int j=0;j<((Visit) getPage().getVisit()).getInt3();j++){
			if(j==0){
				
				SBsql.append("insert into feiyb (id, feiyb_id, feiyxmb_id, shuib, zhi) \n"
							+ " \tvalues ("+FeiyfzId+", "+FeiyfzId+", "+this.getFeiyxm_info(j,"FeiyxmId")+", "+this.getFeiyxm_info(j,"Shuib")+", "+this.getFeiyxm_info(j,"Feiyje")+"); \n");
			}else{
				
				Id=Long.parseLong(MainGlobal.getNewID(Diancxxb_id));
				SBsql.append(" insert into feiyb (id, feiyb_id, feiyxmb_id, shuib, zhi)	\n"
                   			+ " \tvalues ("+Id+", "+FeiyfzId+", "+this.getFeiyxm_info(j,"FeiyxmId")+", "+this.getFeiyxm_info(j,"Shuib")+", "+this.getFeiyxm_info(j,"Feiyje")+"); \n");
			}
			
			mdb_Jine+=Double.parseDouble(this.getFeiyxm_info(j,"Feiyje"));
		}
		SBsql.append("end;");
		flag=con.getInsert(SBsql.toString());
		con.Close();
		
		Feiyb[0]=flag;
		Feiyb[1]=mdb_Jine;
		Feiyb[2]=FeiyfzId;
		return Feiyb;
	}

	private void Return(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		cycle.activate("Danchd");
	}

	public IPropertySelectionModel getIChebModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "路车"));
		list.add(new IDropDownBean(2, "自备车"));
		list.add(new IDropDownBean(3, "汽"));
		list.add(new IDropDownBean(4, "船"));
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
		
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
}

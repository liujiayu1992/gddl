package com.zhiren.dc.gdxw.jianj;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.gdxw.jianj.gdxw_qcjj_hp;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;


/*����:wzb
 *����:2010-7-5 14:31:10
 *����:����޸Ĺ��ܵ���
 * 
 */

/*����:wzb
 *����:2010-6-12 9:58:38
 *����:��������ÿһ����Ƥ��ʱ�����ɲ�������ͷ���ʱ��Ͱ��,�����Ƥ�Ժ���ú������,û���޸Ľ���,�޸�ú���ͬʱ
 *�����ź�����idͬʱҪ�޸�
 * 
 */

public class Jianj_xiug extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // ��������
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
    
	
//	ú��λ������_��ʼ
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = "select mk.id, mk.mingc from meikxxb mk order by mk.mingc";
		setMeikdwModel(new IDropDownModel(sql, "��ѡ��"));
	}
//	ú��λ������_����
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			
		}
		this.getSelectData();
	}
	
	public void save() {

		/*����˼·:
		 * һ:�޸Ŀ۶�
		 *    ˼·:ֱ���޸�chepbtmp���еĿ۶��ֶ�,�޸���ɺ����¼���Ʊ���ֶ�(Ʊ��=ë��-Ƥ��-�۶�),
		 *         Ȼ������޸��е�zhilb_id����gdxw_cy��������Ӧ��zhilb_id
		 *         ���޸�gdxw_cy�л��ܵĿ۶��ֶ�,ͬʱҲҪ�޸�gdxw_cy�ľ����ֶ�,
		 *    Ǳ������:���޸Ŀ۶�ʱ,���ǡ����ɸ�ú������������׼(500��һ����),����Ҳ���ı�gdxw_cy
		 *            ���zhuangt�ֶ�,��Ϊ���ǲ�֪���޸Ŀ۶���������֮ǰ�޸Ļ���������֮���޸�.
		 * ��:�޸ĳ���
		 *    ˼·:ֱ���޸�chepbtmp���еĳ����ֶ�,�Է���û��Ӱ��.
		 *    
		 * ��:�޸�ú������
		 *    ˼·:1.���ҵ��޸�ǰ��ú������Ӧ��gdxw_cy���е�����,��gdxw_cy����ë���ۼ�,Ƥ���ۼ�,�۶��ۼ�
		 *           �����ۼ��ֶ��Ͽ۵���ú���ë��,Ƥ��,�۶ֺ;���,(˵��:��Ϊ�޸�ú��������ɸ�ú������
		 *           500�ֵ�������׼ʱ,�����ı��ú����״̬�ֶ�.)
		 *         2.�ٸ����޸ĺ��ú�����ƺ͸ó��ĳ���,ȥgdxw_cy���в��ҵ����Ƿ��и�ú��zhuang=0�Ĳ�����
		 *           ����������zhilb_id,�ҵ������chepbtmp���иó���Ӧ��zhilb_id��fahbtmp_id,��ͬʱ����
		 *           gdxw_cy���е�ë��,Ƥ��,�۶�,�����ֶ�,
		 *         3.�����gdxw_cy����δ�ҵ���ú��,�ó���,����zhuangt=0������,��ô����gdxw_cy���в��Ҹ�
		 *           ú��,�ó���,zhuangt=1��,�����ۼƾ�����С��zhilb_id,Ȼ�����chepbtmp���иó���Ӧ��
		 *           zhilb_id��fahbtmp_id,��ͬʱ����,Ҳ����gdxw_cy���е�ë��,Ƥ��,�۶�,�����ֶ�,
		 *         4.�����gdxw_cy�����Ҳ�����ú��,�ó�������Ӧ������,��ôȥ����������,ֻ���Ҹ�ú������
		 *           gdxw_cy�����Ƿ�������,���������,��ô����gdxw_cy���е�zhilb_id,����chepbtmp���е�
		 *           zhilb_id��fahbtmp_id,ͬʱ����gdxw_cy���е��ۼ�
		 *         5.�����ú�����û��ú,��gdxw_cy���оͲ��Ҳ���һ������,��ô��������Ͱ��,�����ŵ���Ϣ
		 * 
		 * 
		 */
		
		
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long zhilb_id=0;
		int flag = 0;
		StringBuffer sbsql = new StringBuffer("begin\n");
		String riq =this.getRiq();
		String find_caiyb="";
		long StrZhilb_id=0;
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if(mdrsl.next()) {
				
				
//				�жϳ���
				String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='����500�ֲ����Ƿ����ִ�С��' and zhuangt=1";
				ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
				double Dx_jingz=0.0;
				if(Daxcrs.next()){
					 Dx_jingz=Daxcrs.getDouble("beiz");
				}
				Daxcrs.close();
				String chex="";
				double jingz=mdrsl.getDouble("maoz")-mdrsl.getDouble("piz")-mdrsl.getDouble("koud");
				if(jingz>=Dx_jingz){
					chex=" and cy.chex=0";
				}else{
					chex=" and cy.chex=1";
				}
				
				
			
				String meikdwmc="";
				double maoz1=0.0;
				double piz1=0.0;
				double koud1=0.0;
				double jingz1=0.0;
				String sql5="select zhilb_id,meikdwmc,maoz,piz,koud from chepbtmp where id="+mdrsl.getLong("id");
				ResultSetList rs1=con.getResultSetList(sql5);
				if(rs1.next()){
					 zhilb_id=rs1.getLong(0);
					 meikdwmc=rs1.getString(1);
					 maoz1=rs1.getDouble(2);
					 piz1=rs1.getDouble(3);
					 koud1=rs1.getDouble(4);
					 jingz1=maoz1-piz1-koud1;
					
					
				}
			   
				//if(!mdrsl.getString("meikdwmc").equals(meikdwmc)){
					//�޸�ԭú������Ӧ��gdxw_cy���е��ۼ�ֵ
					sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz-"+maoz1+",cy.piz=" +
							"cy.piz-"+piz1+",cy.koud=cy.koud-"+koud1+",cy.jingz=" +
							"cy.jingz-"+jingz1+" where cy.zhilb_id="+zhilb_id+";\n");
					
					
					find_caiyb="select cy.zhilb_id from gdxw_cy cy where cy.meikdwmc='"+mdrsl.getString("meikdwmc")+"'" +
							""+chex+" and cy.zhuangt=0 and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"'";
				
					 ResultSetList rs2=con.getResultSetList(find_caiyb);
					if(rs2.next()){//����:ú������,����,zhuangt=0
						StrZhilb_id=rs2.getLong("zhilb_id");
						//����chepbtmp
						sbsql.append("update chepbtmp c set c.gongysmc='"+mdrsl.getString("meikdwmc")+"', c.meikdwmc='"+mdrsl.getString("meikdwmc")+"', c.cheph='" +
								""+mdrsl.getString("cheph")+"',c.meicb_id=(select max(id) from meicb where mingc='"+mdrsl.getString("meicb_id")+"'),c.meigy='"+mdrsl.getString("meigy")+"'," +
										"c.koud="+mdrsl.getDouble("koud")+",c.zhilb_id="+StrZhilb_id+"," +
										"c.fahbtmp_id="+StrZhilb_id+" where c.id="+mdrsl.getLong("id")+";\n");
						//����gdxw_cy
						sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz+"+mdrsl.getDouble("maoz")+",cy.piz=" +
								"cy.piz+"+mdrsl.getDouble("piz")+",cy.koud=cy.koud+"+mdrsl.getDouble("koud")+",cy.jingz=" +
								"cy.jingz+"+mdrsl.getDouble("jingz")+" where cy.zhilb_id="+StrZhilb_id+";\n");
						
					
						
						rs2.close();
					}else{
						find_caiyb="select zhilb_id from gdxw_cy cy where cy.meikdwmc='"+mdrsl.getString("meikdwmc")+"'" +
						""+chex+" and cy.zhuangt=1 and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"' order by cy.jingz";
						rs2.close();
					    rs2=con.getResultSetList(find_caiyb);
						if(rs2.next()){//����:ú��,����,zhuangt=1�����Ҿ�����Сһ��
							StrZhilb_id=rs2.getLong("zhilb_id");
							//����chepbtmp
							sbsql.append("update chepbtmp c set c.gongysmc='"+mdrsl.getString("meikdwmc")+"',c.meikdwmc='"+mdrsl.getString("meikdwmc")+"', c.cheph='" +
									""+mdrsl.getString("cheph")+"',c.meicb_id=(select max(id) from meicb where mingc='"+mdrsl.getString("meicb_id")+"'),c.meigy='"+mdrsl.getString("meigy")+"'," +
											"c.koud="+mdrsl.getDouble("koud")+",c.zhilb_id="+StrZhilb_id+"," +
											"c.fahbtmp_id="+StrZhilb_id+" where c.id="+mdrsl.getLong("id")+";\n");
							//����gdxw_cy
							sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz+"+mdrsl.getDouble("maoz")+",cy.piz=" +
									"cy.piz+"+mdrsl.getDouble("piz")+",cy.koud=cy.koud+"+mdrsl.getDouble("koud")+",cy.jingz=" +
									"cy.jingz+"+mdrsl.getDouble("jingz")+" where cy.zhilb_id="+StrZhilb_id+";\n");
							
							rs2.close();
						}else{
							
							find_caiyb="select zhilb_id from gdxw_cy cy where cy.meikdwmc='"+mdrsl.getString("meikdwmc")+"'  " +
							" and cy.zhuangt=1 and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"' order by cy.jingz";
							
							rs2.close();
						    rs2=con.getResultSetList(find_caiyb);
							if(rs2.next()){//����:ú��,zhuang=1
								
								StrZhilb_id=rs2.getLong("zhilb_id");
								//����chepbtmp
								sbsql.append("update chepbtmp c set c.gongysmc='"+mdrsl.getString("meikdwmc")+"', c.meikdwmc='"+mdrsl.getString("meikdwmc")+"', c.cheph='" +
										""+mdrsl.getString("cheph")+"',c.meicb_id=(select max(id) from meicb where mingc='"+mdrsl.getString("meicb_id")+"'),c.meigy='"+mdrsl.getString("meigy")+"'," +
												"c.koud="+mdrsl.getDouble("koud")+",c.zhilb_id="+StrZhilb_id+"," +
												"c.fahbtmp_id="+StrZhilb_id+" where c.id="+mdrsl.getLong("id")+";\n");
								//����gdxw_cy
								sbsql.append("update gdxw_cy cy set cy.maoz=cy.maoz+"+mdrsl.getDouble("maoz")+",cy.piz=" +
										"cy.piz+"+mdrsl.getDouble("piz")+",cy.koud=cy.koud+"+mdrsl.getDouble("koud")+",cy.jingz=" +
										"cy.jingz+"+mdrsl.getDouble("jingz")+" where cy.zhilb_id="+StrZhilb_id+";\n");
								rs2.close();
								
							}else{
								
								//���Ҫ�޸ĺ��ú����û����ú,��������Ͱ��,ת���,������ʱ��,�������
								flag = countCaiy(con, 259,mdrsl.getString("meikdwmc"), mdrsl.getDouble("maoz"), mdrsl.getDouble("piz"), mdrsl.getDouble("koud"),mdrsl.getString("id"));
								
							}
							
						}
						
						
						
						
					}
					
					
				}
				
			
				
				
				
				
				
			//}
		
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		
		
		
//		������ԭú���ۼ�ֵ���龻��,�������С��5��,ɾ��gdxw_cy�������
		find_caiyb="select jingz from gdxw_cy where zhilb_id="+zhilb_id;
		ResultSetList rs3=con.getResultSetList(find_caiyb);
		if(rs3.next()){
			double  zhilb_jingz=rs3.getDouble("jingz");
			if(zhilb_jingz<5){
				find_caiyb="delete gdxw_cy where zhilb_id="+zhilb_id;
				
				flag=con.getUpdate(find_caiyb);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
							"\nSQL:" + find_caiyb +"ɾ��gdxw_cy�е�zhilb_idʧ��!");
					setMsg(this.getClass().getName() + ":���¸���gdxw_cy�е�zhilb_idʧ��!");
					
				}
			}
		}
		
		
		rs3.close();
		// �жϸ��º��Ƿ�ﵽ�˰��������������, ����ﵽ��������,����gdxw_cy���zhuangt�ֶ�
		 find_caiyb = "select zhi,danw from xitxxb where  mingc ='��������������' and zhuangt=1 and leib= '����'";
		rs3=con.getResultSetList(find_caiyb);
		double Fenzl = 0;
		if(rs3.next()){
			Fenzl =rs3.getDouble("ZHI");
		}
		find_caiyb="select jingz from gdxw_cy where zhilb_id="+StrZhilb_id;
		rs3=con.getResultSetList(find_caiyb);
		if(rs3.next()){
			if(rs3.getDouble("jingz") > Fenzl){
				find_caiyb= "update gdxw_cy set zhuangt = 1 where zhilb_id ="+ StrZhilb_id;
				flag=con.getUpdate(find_caiyb);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
							"\nSQL:" + find_caiyb +"ɾ��gdxw_cy�е�zhilb_idʧ��!");
					setMsg(this.getClass().getName() + ":���¸���gdxw_cy�е�zhilb_idʧ��!");
					
				}
				
			}
		}
		
		
		mdrsl.close();
		con.Close();
		this.setMsg("�޸ĳɹ�!");
	
	}
	
	public void getSelectData() {
		

		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		String meikmc=this.getMeikdwValue().getValue();
		//System.out.println(meikmc);
		if(meikmc.equals("��ѡ��")){
			tiaoj="";
		}else{
			tiaoj= "and c.meikdwmc='"+meikmc+"'";
		}
			
		JDBCcon con = new JDBCcon();
		String sql = "select c.id,c.zhilb_id,c.fahbtmp_id,c.meikdwmc,c.cheph,c.maoz,c.piz,c.koud," +
		"(c.maoz-c.piz-c.koud) as jingz,c.biaoz,m.mingc as meicb_id,c.meigy,to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj," +
		"to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') as qingcsj,c.zhongcjjy,c.qingcjjy\n" +
		" from chepbtmp c,meicb m\n" + 
		"where c.daohrq=to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
		"and c.fahb_id=0 and c.meicb_id=m.id(+) \n" + 
		""+tiaoj+"\n"+
		"order by c.meikdwmc,c.zhongcsj";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		 ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid�����з�ҳ
		egu.addPaging(0);
		
		
		egu.getColumn("id").setEditor(null);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("fahbtmp_id").setEditor(null);
		egu.getColumn("fahbtmp_id").setHidden(true);
		
		
		egu.getColumn("meikdwmc").setHeader("ú��λ");
		
		
		ComboBox cmk= new ComboBox();
		egu.getColumn("meikdwmc").setEditor(cmk);
		cmk.setEditable(true);
		String mkSql="select id,piny || '-' ||mingc from meikxxb order by xuh";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId, new
		IDropDownModel(mkSql));
		
		egu.getColumn("cheph").setHeader("����");
		egu.getColumn("cheph").setWidth(80);
		
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setWidth(60);
		
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setWidth(60);
		
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(60);
		
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("biaoz").setHeader("Ʊ��");
		//egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setWidth(60);
		
		egu.getColumn("meicb_id").setHeader("жú�ص�");
		egu.getColumn("meicb_id").setWidth(100);
		
		egu.getColumn("meigy").setHeader("ú��Ա");
		egu.getColumn("meigy").setWidth(60);
		
		egu.getColumn("zhongcsj").setHeader("�غ�ʱ��");
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").setWidth(120);
		
		egu.getColumn("qingcsj").setHeader("�ᳵʱ��");
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").setWidth(120);
		
		egu.getColumn("zhongcjjy").setHeader("�غ���Ա");
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongcjjy").setWidth(80);
		
		egu.getColumn("qingcjjy").setHeader("�����Ա");
		egu.getColumn("qingcjjy").setEditor(null);
		egu.getColumn("qingcjjy").setWidth(80);
		
		
//		����ú��������
		ComboBox cmc= new ComboBox();
		egu.getColumn("meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,mingc from meicb order by xuh";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel(mcSql));
//      ��úԱ��Ӧ��ú��������
		ComboBox ymz= new ComboBox();
		egu.getColumn("meigy").setEditor(ymz);
		ymz.setEditable(true);
		String ymy = "select id ,zhiw from renyxxb r where r.bum='��úԱ' order by r.zhiw";
		egu.getColumn("meigy").setComboEditor(egu.gridId,new IDropDownModel(ymy));

		
		
		egu.addTbarText("�������ڣ�");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("ú��λ��");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setListWidth(150);
		comb.setTransform("Meikdw");
		comb.setId("Meikdw");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meikdw.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		

	
	
		setExtGrid(egu);
		rsl.close();
		con.Close();
	
	}
	
	/**
	 * �����ҳ����ȡ����ֵΪNull���ǿմ�����ô�����ݿⱣ���ֶε�Ĭ��ֵ
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
	}
	
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel3(null); // ú��λ������
			visit.setDropDownBean3(null);
			
			
		}
		getSelectData();
	}
	
	
	
	
	//��������
	private int countCaiy(JDBCcon con, long diancxxb_id,String meikdwmc,
			double maoz, double piz, double koud,String chepbtmp_id){
		Visit visit = (Visit) this.getPage().getVisit();
		String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='����500�ֲ����Ƿ����ִ�С��' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id()+"";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		String sql="";
		boolean Bool_Qufdxc=false;//�Ƿ����ִ�С������
		boolean Bool_dac=false;  //�Ƿ��Ǵ�
		if(Daxcrs.next()){
			String Dx_zhi=Daxcrs.getString("zhi");
			double Dx_jingz=Daxcrs.getDouble("beiz");
			//500�ַ����Ƿ����ִ�С��
			if(Dx_zhi.equals("��")){
				Bool_Qufdxc=true;
				String chex="";
				//���maoz-piz-koud>=Dx_jingz ,�Ǵ�,������С��,(Dx_jingz��45��),��chex=0,С��chex=1
				if(maoz-piz-koud>=Dx_jingz){
					Bool_dac=true;
					chex=" and chex=0";
				}else{
					chex=" and chex=1";
				}
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and shengcrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and shengcrq<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1  "+chex+""; 
			}else{
//				�жϲ���������û��δ�����Ĳ�����Ϣ(zhuangt = 0)
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and shengcrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and shengcrq<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 ";
			}
		}
		
		
		
 
		ResultSetList rs = con.getResultSetList(sql); 
		String id = "";
		String sql1="";
		int flag = 0;
		if(rs.next()){
			
			//ֱ�������µĲ�����Ϣ,���ø�����ǰ�Ĳ�����Ϣ
			
		}else{
//			���Ϊ��������
			id = MainGlobal.getNewID(diancxxb_id);
//			����������������
			long zhilb_id = Jilcz.getZhilbid(con, null, DateUtil.getDate(this.getRiq()), visit.getDiancxxb_id());
//			���ɲ�����������������
			flag = Caiycl.CreatBianh(con, zhilb_id, visit.getDiancxxb_id());
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\n�������ʧ��!");
				setMsg(this.getClass().getName() + ":�������ʧ��!");
				return flag;
			}
			
			
//			����δʹ�õĴ���λ��
			
			sql = 
			
			"select * from cunywzb where id in\n" +
			"(select id from cunywzb where zhuangt = 1\n" + 
			"minus\n" + 
			"select cunywzb_id from caiyb where zhilb_id in\n" + 
			"(select zhilb_id from gdxw_cy where  \n" +
			" shengcrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd') and shengcrq<=to_date('"+this.getRiq()+"','yyyy-mm-dd')+1))\n" + 
			"and rownum = 1";
			rs = con.getResultSetList(sql);
			if(rs.next()){
//				���²������д���λ��
				sql = "update caiyb set cunywzb_id = " + rs.getString("id") + 
				" where zhilb_id = " + zhilb_id;
//             ����chepbtmp�е�zhilb_id
				 sql1="update chepbtmp set gongysmc='"+meikdwmc+"',meikdwmc='"+meikdwmc+"', zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
				con.getUpdate(sql);
				con.getUpdate(sql1);
			}else{
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n����λ������,����Ӵ���λ��!");
				setMsg(this.getClass().getName() + ":����λ������,����Ӵ���λ��!");
				return -1;
			}
//			long 
			double jingz=maoz-piz-koud;
			//�Ƿ����ִ�С������
			if(Bool_Qufdxc){
				//����Ǵ�,chex=0,���������С��chex=1
				if(Bool_dac){
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",to_date('"+getRiq()+"','yyyy-mm-dd'),0,to_date('"+getRiq()+"','yyyy-mm-dd'))";
				}else{
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",to_date('"+getRiq()+"','yyyy-mm-dd'),1,to_date('"+getRiq()+"','yyyy-mm-dd'))";
				}
				
			}else{
				sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
				"values("+id+"," + diancxxb_id +
				",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",to_date('"+getRiq()+"','yyyy-mm-dd'),0,to_date('"+getRiq()+"','yyyy-mm-dd'))";
			}
			
			flag = con.getInsert(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n����gdxw_cy��Ϣʧ��!");
				setMsg(this.getClass().getName() + ":����gdxw_cy��Ϣʧ��!");
				return flag;
			}
		}
		rs.close();
		return flag;
	}
}
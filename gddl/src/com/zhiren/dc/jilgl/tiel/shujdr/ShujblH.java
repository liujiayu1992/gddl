package com.zhiren.dc.jilgl.tiel.shujdr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Button;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ��:2009-10-31
 * �޸�����:ǰһ�� ��������ʵ  �� ���� �����滻�Ĺ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-28
 * �������޸ı����糧�����ݵ���д��������Ƥ���س�ʱ�䡢�ᳵʱ���ֶ�
 */
/*
 * ����:tzf
 * ʱ��:2009-10-16
 * �޸�����:�������ǵ����ʱ��  ���ļ�����д�뵽���������cheplsb����ס
 */
/*
 * ����:tzf
 * ʱ��:2009-10-16
 * �޸�����:�������ݻ����д�����ֵ�༭  �ܺ�ֵҲ��Ӧ�ı�
 */
/*
 * ����:tzf
 * ʱ��:2009-07-03
 * �޸�����:���Ӿ��� �ֶ�  ����������ֶ�ֵ�ĸı� ��ֵ Ҳ�Զ�����.
 */
/*
 * 2009-05-13
 * ����
 * ����ϵͳ�������ÿ���ë���Զ����㲢��Ƥ��ȡĬ��ֵ,
 * �Իس����Զ���ת��������
 */
/*
 * ����:tzf
 * ʱ��:2009-5-4
 * �޸�����: �� diancxxb_id �ֶ� ʹ�õ��� �ӽڵ� �糧�����糧��ʹ�õ� ��ȫ���ĵ糧 ����
 *          �� ���߱���ʱ�����ڵ糧id�� �ܳ��ģ������diancxxb_id�޷��õ���Ӧ�ĵ糧id������.
 */
/**
 * 
 * @author Rock
 * 
 */
/*
 * 2009-05-06 
 * ����
 * �޸�Ĭ�Ϸ������ڡ��������ڡ���������ȡѡ��Ĺ�������
 * �ְ汾: 1.1.2.28
 */
/*
 * 2009-02-17 ���� ���ӹ�Ӧ��ѡ����������ж����ʽ�ֶ�
 */
/*
 * 2009-02-25 ���� ���ӹ����ID��Ϊ-1 ʹ�����μ�������ʱ���Խ���chepb�ķ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-16 10��39
 * ���������Ӳ�¼���ݱ�ʶ���� ��������ݿ� 
 *  alter table chepb add bulsj number(1) default 0 null;
	comment on column chepb.bulsj
	  is '��¼���ݱ�ʶ 1����¼ 0���ǲ�¼'; 
	alter table cheplsb add bulsj number(1) default 0 null;
	comment on column cheplsb.bulsj
	  is '��¼���ݱ�ʶ 1����¼ 0���ǲ�¼'; 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-02 11��11
 * ���������ӻ𳵺����ݲ�¼ ����ʾ���ݵ��밴ťʱ�������chh-013 ��������� 
 * ������Ϊ(��һ�����뵼�����ݰ�ť����)
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-02 13��36
 * �������������밴ť��������ʱ�ķ�ҳ����
 */
public class ShujblH extends BasePage implements PageValidateListener {
	//�������ݻ�������
	private List daordata=null;
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "ShujblH.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		String guohb_id="-1";
		String guohb_sql=" ";//�������������sql
		String guohsj = "";
		if(visit.getboolean6()){
			if(getWenjmSelectValue()== null||this.getWenjmSelectValue().getId()==0){//û����Ч�ļ�����
				
			}else{//��Ч�ļ�����
				String wenjm = this.getWenjmSelectValue().getValue();
				guohsj = wenjm.substring(0,4) + "-" + wenjm.substring(4,6) + "-" + wenjm.substring(6, 8) + " " +
				wenjm.substring(8, 10) + ":" + wenjm.substring(10, 12) + ":" + wenjm.substring(12, 14);
				guohsj = DateUtil.FormatOracleDateTime(guohsj);
				guohb_id=MainGlobal.getNewID(visit.getDiancxxb_id());
				guohb_sql=" insert into guohb(id,guohsj,wenjm,beiz) values("+guohb_id+",sysdate,'"+this.getWenjmSelectValue().getValue()+"','xxx'); ";
			}
			
		}
		
		
		StringBuffer sb = new StringBuffer();
		// ���복Ƥ��ʱ��
		sb.append("begin\n");
		while (rsl.next()) {
//			String diancxxb_id = getExtGrid().getColumn("diancxxb_id")
//			.combo.getBeanStrId(rsl.getString("diancxxb_id"));
			

			String diancxxb_id="";
			TreeNode tn=this.getTree().getRootNode();
			if(tn.getChildNodes()==null || tn.getChildNodes().size()==0 || tn.getText().equals(rsl.getString("diancxxb_id"))){
				diancxxb_id=this.getTreeid();
			}else{
				for(int i=0;i<tn.getChildNodes().size();i++){
					TreeNode node=(TreeNode)tn.getChildNodes().get(i);
					if(node.getText().equals(rsl.getString("diancxxb_id"))){
						diancxxb_id=node.getId();
					}
				}
			}
			
			if ("".equals(diancxxb_id)) {
				this.setMsg("�ֶ� �糧���� ����Ϊ�գ�");
				return;
			}
			
			if(!visit.getboolean6()){
				guohsj = DateUtil.FormatOracleDateTime(rsl.getString("guohsj"));
			}
			sb.append("insert into cheplsb\n");
			sb
					.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, caiybh, yunsfsb_id, chec,cheph, maoz, piz, biaoz, koud, kous, kouz, sanfsl, jianjfs, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, zhongcjjy, daozch, lury, beiz,qingcsj,zhongcsj,yunsdwb_id,caiyrq,xiecfsb_id,guohb_id,bulsj,zhongchh,ches)\n");
			sb.append("values (getnewid(").append(diancxxb_id)
					.append("),").append(diancxxb_id);
			sb.append(",").append(
					((IDropDownModel) getGongysModel()).getBeanId(rsl
							.getString("gongysb_id")));
			sb.append(",").append(
					((IDropDownModel) getMeikModel()).getBeanId(rsl
							.getString("meikxxb_id")));
			sb.append(",").append(
					(getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl
							.getString("pinzb_id")));
			sb.append(",").append(
					((IDropDownModel) getChezModel()).getBeanId(rsl
							.getString("faz_id")));
			sb.append(",").append(
					(getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl
							.getString("daoz_id")));
			sb.append(",").append(
					(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl
							.getString("jihkjb_id")));
			sb.append(",").append(
					DateUtil.FormatOracleDate(rsl.getString("fahrq")));
			sb.append(",").append(
					DateUtil.FormatOracleDate(rsl.getString("daohrq")));
			sb.append(",'").append(rsl.getString("bianm")).append("'");
			sb.append(",").append(SysConstant.YUNSFS_HUOY).append(",'").append(
					rsl.getString("chec"));
			sb.append("','").append(rsl.getString("cheph"));
			sb.append("',").append(rsl.getDouble("maoz"));
			sb.append(",").append(rsl.getDouble("piz"));
			sb.append(",").append(rsl.getDouble("biaoz"));
			sb.append(",").append(rsl.getDouble("koud"));
			sb.append(",").append(rsl.getDouble("kous"));
			sb.append(",").append(rsl.getDouble("kouz"));
			sb.append(",").append(rsl.getDouble("sanfsl"));
			sb.append(",'").append(rsl.getString("jianjfs"));
			sb.append("',").append(
					(getExtGrid().getColumn("chebb_id").combo).getBeanId(rsl
							.getString("chebb_id")));
			sb.append(",").append(
					(getExtGrid().getColumn("yuandz_id").combo).getBeanId(rsl
							.getString("yuandz_id")));
			sb.append(",").append(
					(getExtGrid().getColumn("yuanshdwb_id").combo)
							.getBeanId(rsl.getString("yuanshdwb_id")));
			sb.append(",'").append(rsl.getString("yuanmkdw"));
			sb.append("','").append(rsl.getString("zhongcjjy"));
			sb.append("','").append(rsl.getString("daozch"));
			sb.append("','").append(visit.getRenymc());
			sb.append("','").append(rsl.getString("beiz")).append("'");
			sb.append(",").append(guohsj);
			sb.append(",").append(guohsj);
			sb.append(",").append(
					(visit.getExtGrid1().getColumn("yunsdwb_id").combo)
							.getBeanId(rsl.getString("yunsdwb_id")));
			sb.append(",").append(
					DateUtil.FormatOracleDate(rsl.getString("caiyrq")));
			sb.append(",").append(
					(getExtGrid().getColumn("xiecfsb_id").combo).getBeanId(rsl
							.getString("xiecfsb_id")));
			sb.append(","+guohb_id+",1,'").append(rsl.getString("zhongchh")).append("',")
			      .append(rsl.getInt("ches"));
			sb.append(");\n");
		}
		sb.append(guohb_sql);//��������
		sb.append("end;");
 
		JDBCcon con=new JDBCcon();
		if(Jilcz.SaveJilData(con,sb.toString(), visit.getDiancxxb_id(),
			SysConstant.YUNSFS_HUOY, SysConstant.HEDBZ_YJJ, null, this
					.getClass().getName(), Jilcz.SaveMode_BL,null,!visit.getboolean6()).equals(ErrorMessage.SaveSuccessMessage)){
	                //�ƶ��ļ�,����ļ��������
	    		boolean isDaordata = con.getHasIt("select * from xitxxb where mingc = '��¼��ʾ���밴ť' and danw = '��·' and zhuangt =1 and zhi = '��' and diancxxb_id =" + visit.getDiancxxb_id());
	               if(isDaordata && !this.datacopy(this.getWenjmSelectValue().getValue())){
		               con.rollBack();
		               WriteLog.writeErrorLog(ErrorMessage.Chehhd013);
		               setMsg(ErrorMessage.Chehhd013);
	               }else {
	            	   con.commit();
	            	   this.setMsg("����ɹ�!");
	               }       
	    }else{
	    	
	    	setMsg("�����������");
	    }
	    con.Close();
	    //ˢ���ļ��������˵�
	    visit.setboolean6(false);
	    this.setWenjmSelectModel(null);
		this.setWenjmSelectValue(null);  
	}

	private void Daor(){

		Visit visit = (Visit) this.getPage().getVisit();
		visit.setboolean6(true);//����ʱ���ж��Ƿ����� ���� ��ť �Ӷ������Ƿ�Ҫд������
		File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
		if(getWenjmSelectValue()== null||this.getWenjmSelectValue().getId()==0){
			this.daordata=null;
			return;
		}
		File file = new File(filepath,this.getWenjmSelectValue().getValue());

		FileReader fr;
		BufferedReader br;
		BigInteger bi;
		BigInteger keygen;
		String bufferStr;
		String strarr[];
		StringBuffer sb;
		char c;
		long asclong;
		List list = new ArrayList();
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg004);
			setMsg(ErrorMessage.Hengdxg004);
			this.daordata=null;
			return;
		}
		br = new BufferedReader(fr);
		try {
			keygen = BigInteger.valueOf(Long.parseLong(file.getName()
					.substring(0, 8)));
			while ((bufferStr = br.readLine()) != null) {
				if("".equals(bufferStr)) {
					continue;
				}
				sb = new StringBuffer();
				strarr = bufferStr.split("  ");
				for (int k = 0; k < strarr.length; k++) {
					bi = new BigInteger(strarr[k].trim());
					asclong = bi.xor(keygen).longValue();
					c = (char) asclong;
					sb.append(c);
				}
				String strGuohxx[] = sb.toString().trim().split(",", 4);
				String maoz = "".equals(strGuohxx[0])?"":String.valueOf(CustomMaths.div(Double.parseDouble(strGuohxx[0]),
						1000.0));
				String piz = "".equals(strGuohxx[1])?"":String.valueOf(CustomMaths.div(Double.parseDouble(strGuohxx[1]),
						1000.0));
				String sud = strGuohxx[2];
				String cheph = strGuohxx[3];
				list.add(new Guohxx(maoz, piz, sud, cheph));
			}
		} catch (NumberFormatException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg005);
			setMsg(ErrorMessage.Hengdxg005);
			this.daordata=null;
			return;
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Hengdxg006);
			setMsg(ErrorMessage.Hengdxg006);
			this.daordata=null;
			return;
		}finally {
			try {
				br.close();
				fr.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		this.daordata=list;
		
	
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
    private boolean _DaorClick=false;
    public void DaorButton(IRequestCycle cycle){
    	_DaorClick=true;
    }
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if(_DaorClick){
			_DaorClick=false;
			Daor();
		}
		getSelectData();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		�Ƿ��Զ�����ë��
		boolean isAutoCountMaoz = con.getHasIt("select * from xitxxb where mingc = '��¼ë���Զ�����' and danw = '��·' and zhuangt =1 and zhi = '��' and diancxxb_id =" + visit.getDiancxxb_id());
//		boolean isAutoCountMaoz=true;
//		Ĭ��Ƥ��
		double defaultPiz = 0.0;
		String sql = "select * from xitxxb where mingc = '��¼Ĭ��Ƥ��' and danw ='��·' and zhuangt=1 and diancxxb_id=" +visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			defaultPiz = rsl.getDouble("zhi");
		}
		rsl.close();
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
		ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		StringBuffer sb = new StringBuffer();
		sb
				.append("select  c.diancxxb_id,c.cheph,c.maoz,c.piz,c.biaoz,c.koud,c.kous,c.kouz, round_new(c.maoz-c.piz-c.koud-c.kouz-c.kous,2) as jingz,c.sanfsl,c.chec,c.id,'' as gongysb_id,'' as meikxxb_id,'' as faz_id, \n");
		sb
				.append("'' as jihkjb_id,'' as pinzb_id,'' xiecfsb_id,'' as yunsdwb_id, c.fahrq as fahrq,c.daohrq as daohrq,c.caiyrq,\n");
		sb
				.append("c.jianjfs, '' as chebb_id,'' as guohsj,'' as bianm,c.zhongcjjy,\n");
		sb.append("'' as daoz_id,'' as yuandz_id,'' as yuanshdwb_id,\n");
		sb.append("c.yuanmkdw,c.daozch,c.beiz,c.ches,nvl(c.bulsj,1) bulsj,zhongchh,zhongcsj from cheplsb c ");
		/**
		 * huochaoyuan 2009-03-04����������������д���
		 */
		rsl = con.getResultSetList(sb.toString());
		//�����ļ�����
		if(this.daordata!=null){
		String wenjm=	this.getWenjmSelectValue().getValue();
		String shij=wenjm.trim().substring(0, 14);
		Timestamp btime=new Timestamp(Integer.parseInt(shij.substring(0, 4)),
				Integer.parseInt(shij.substring(4, 6)), 
						Integer.parseInt( shij.substring(6, 8)),
								Integer.parseInt(    shij.substring(8, 10)),
										Integer.parseInt(    shij.substring(10, 12)),
												Integer.parseInt(   shij.substring(12, 14)),
				                       0
		);
			for(int i=0;i<daordata.size();i++){
				String data[]=new String[rsl.getColumnNames().length];
			    Guohxx gx=	(Guohxx)daordata.get(i);
			    
			    data[0]=""+visit.getDiancmc();
			    data[1]=gx._cheph;
			    data[2]=gx._maoz;
			    data[3]=gx._piz;
			    data[4]="";
			    data[5]="";
			    data[6] = "";
				data[7] = "";
				
				//��һ������  �ֶ�
				float maoz=0;
				if(data[2]!=null && !data[2].equals("")){
					maoz=Float.parseFloat(data[2]);
				}
				float piz=0;
				if(data[3]!=null && !data[3].equals("")){
					piz=Float.parseFloat(data[3]);
				}
				
				data[8] = (maoz-piz)+"";
				
				int j=8;
				data[++j] = "";
				data[++j] = "";
				data[++j] = "0";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
				data[++j] = "";
			    data[++j]=gx._sud;
			    data[++j]="0";
			    data[++j]=wenjm.trim().substring(14);
			    data[++j]="";
 rsl.getResultSetlist().add(data);
			}
			this.daordata=null;
		}
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.addPaging(0);
		ComboBox dc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		dc.setEditable(true);
		sql = 
			"select id, mingc from (\n" +
			"select id, mingc, level,CONNECT_BY_ISLEAF leaf\n" + 
			"  from diancxxb\n" + 
			" start with id = "+getTreeid()+"\n" + 
			"connect by fuid = prior id)\n" + 
			"where leaf = 1";
		IDropDownModel dcmd = new IDropDownModel(sql);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dcmd);
		if(dcmd.getOptionCount()>1){
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else{
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
			//egu.getColumn("diancxxb_id").setDefaultValue(dcmd.getBeanValue(getTreeid()));
			
			String dc_value="";
			TreeNode tn=this.getTree().getRootNode();
			if(tn.getChildNodes()==null || tn.getChildNodes().size()==0 || tn.getId().equals(this.getTreeid())){
				dc_value=tn.getText();
			}else{
				for(int i=0;i<tn.getChildNodes().size();i++){
					TreeNode node=(TreeNode)tn.getChildNodes().get(i);
					if((node.getId()+"").equals(this.getTreeid())){
						dc_value=node.getText();
					}
				}
			}
			
			egu.getColumn("diancxxb_id").setDefaultValue(dc_value);
		}
		
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setWidth(65);
		egu.getColumn("jingz").update=false;
		NumberField nf=new NumberField();
		nf.setDecimalPrecision(3);
		egu.getColumn("jingz").editor=nf;
		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("caiyrq").setHeader(Locale.caiyrq_caiyb);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("guohsj").setHidden(true);
		egu.getColumn("guohsj").editor = null;
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(75);
		egu.getColumn("xiecfsb_id").setHeader(Locale.xiecfs_chepb);
		egu.getColumn("xiecfsb_id").setWidth(75);
		
        egu.getColumn("ches").setHidden(true);
        egu.getColumn("ches").editor=null;
        egu.getColumn("zhongchh").setHidden(true);
        egu.getColumn("zhongchh").editor=null;
        egu.getColumn("zhongcsj").setHidden(true);
        egu.getColumn("zhongcsj").editor=null;
//        egu.getColumn("bulsj").setHidden(true);
        egu.getColumn("bulsj").editor=null;
        egu.getColumn("bulsj").setDefaultValue("1");
        egu.getColumn("bulsj").setHidden(true);
//        
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		if(isAutoCountMaoz){
			egu.getColumn("cheph").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 5); }");
		}else{
			egu.getColumn("cheph").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 3); }");
			egu.getColumn("maoz").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 4); }");
			egu.getColumn("piz").editor
			.setListeners("specialkey:function(own,e){if(gridDiv_grid.getStore().getAt(row).get('BULSJ')!=0) gridDiv_grid.startEditing(row , 5); }");
		}
		
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
		
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		
		egu.getColumn("piz").setDefaultValue(String.valueOf(defaultPiz));
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		StringBuffer lins = new StringBuffer();
		lins
				.append("specialkey:function(own,e){ \n")
				.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
				.append("Ext.MessageBox.alert('��ʾ��Ϣ','�ѵ�������ĩβ��');return; \n")
				.append("} \n")
				.append("row = row+1; \n")
				.append(
						"last = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
				.append(
						"cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("copylastrec(last,cur); \n")
				.append("if(gridDiv_grid.getStore().getAt(row).get('BULSJ')==0){\n")
				.append("   gridDiv_grid.startEditing(row , 5);\n")
				.append("	   return;\n")
				.append("	}\n") 
				.append(
						"gridDiv_grid.startEditing(row , 2); }");
		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setDefaultValue("0");// ����Ĭ��ֵ
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(65);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.addTbarText("����ʱ��:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("ʱ:");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("HOUR");
		comb1.setId("HOUR");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("��:");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("MIN");
		comb2.setId("MIN");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());

		egu.addTbarText("��:");
		ComboBox comb3 = new ComboBox();
		comb3.setWidth(50);
		comb3.setTransform("SEC");
		comb3.setId("SEC");// ���Զ�ˢ�°�
		comb3.setLazyRender(true);// ��̬��
		comb3.setEditable(true);
		egu.addToolbarItem(comb3.getScript());
		// ж����ʽ��������
		ComboBox c8 = new ComboBox();
		egu.getColumn("xiecfsb_id").setEditor(c8);
		c8.setEditable(true);
		egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_xiecfs));

		// ���÷������ں͵������ڵ�Ĭ��ֵ
		egu.getColumn("fahrq").setDefaultValue(this.getRiq());
		egu.getColumn("daohrq").setDefaultValue(this.getRiq());
		egu.getColumn("caiyrq").setDefaultValue(this.getRiq());
		// ���õ�վ������
		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz_id").setEditor(c1);
		c1.setEditable(true);
		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		egu.getColumn("daoz_id").setDefaultValue(visit.getDaoz()==null?"":visit.getDaoz());
		// ����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c2);
		c2.setEditable(true);
		String pinzSql = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		// ���ÿھ�������
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c3);
		c3.setEditable(true);
		String jihkjSql = SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjSql));
		// ���ü�﷽ʽ������
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "����"));
		l.add(new IDropDownBean(1, "���"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("jianjfs").setDefaultValue("����");
		// ���ó���������
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "·��"));
		ls.add(new IDropDownBean(2, "�Ա���"));
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ls));
		egu.getColumn("chebb_id").setDefaultValue("·��");
		// ����ԭ��վ������
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz_id").setEditor(c6);
		c6.setEditable(true);
		String YuandzSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel(YuandzSql));
		egu.getColumn("yuandz_id").setDefaultValue(visit.getDaoz());
		// ����ԭ�ջ���λ������
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c7);
		c7.setEditable(true);// ���ÿ�����
		String Sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(
				"" + ((Visit) getPage().getVisit()).getDiancmc());
		// �������䵥λ������
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(comb);
		comb.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwSql));
		egu.getColumn("yunsdwb_id").setDefaultValue("��ѡ��");

		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
//				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
//						.getVisit()).getDiancxxb_id(), getTreeid());
//		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���

		// ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// ����ÿҳ��ʾ����
//		egu.addPaging(25);
//		egu.addToolbarButton(GridButton.ButtonType_Inserts, null);

		//���ݻ�����js�Ƿ���뵽�����¼�after����
		String after_zjr=" ";
		String before_zjr=" ";
		String gongys_zjr=" ";
		boolean insert_boo=false;//���ƽű�
		boolean save_boo=false;
		if(con.getResultSetList(" select * from xitxxb where mingc = '���ݲ�¼���ӻ�����' and danw = '��·' and leib='����' and zhuangt =1 and zhi = '��' ").next()){
			egu.addOtherScript(" \n zongjRow(gridDiv_ds,gridDiv_plant,diancTree_treePanel ); \n");
			egu.addOtherScript(" gridDiv_ds.addListener('remove',function(store,record,index){ \n " +
					" if(record.get('ID')=='-999'){return;}\n "+
					"  zongjRow(gridDiv_ds,gridDiv_plant,diancTree_treePanel ); \n" +
					"} );\n");
			after_zjr=" \n  zongjCol(gridDiv_ds,e.field ); \n";
			before_zjr=" \n if(bob.record.get('ID')=='-999'){ bob.cancel=true; } \n";
			gongys_zjr=" \n  if(rec.get('ID')=='-999'){ gongysTree_window.hide(); return; } \n ";
			insert_boo=true;
			save_boo=true;
		}
		
		
		GridButton gbt = new GridButton("���",getInsertScript(egu,insert_boo));
		gbt.icon=SysConstant.Btn_Icon_Insert;
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gbs = new GridButton("����",getSaveScript(egu,save_boo));
		gbs.icon=SysConstant.Btn_Icon_Save;
		egu.addTbarBtn(gbs);
		
		
//		��ϵͳ��Ϣ����ڴ˹��ܵ��Ƿ���ʾ
		ComboBox wenjm =null;
		boolean isDaordata = con.getHasIt("select * from xitxxb where mingc = '��¼��ʾ���밴ť' and danw = '��·' and zhuangt =1 and zhi = '��' and diancxxb_id =" + visit.getDiancxxb_id());
		if(isDaordata){
		//�����ļ��������˵��͵��밴ť
		wenjm = new ComboBox();
		wenjm.setEditable(true);
		wenjm.setTransform("WenjmSelect");
		egu.addToolbarItem(wenjm.getScript());
		
		//�������ݰ�ť
		GridButton DaorButton = new GridButton("��������","function (){document.getElementById('DaorButton').click()}");
		egu.addToolbarItem("{"+DaorButton.getScript()+"}");
		}
		
		//���Ӷ����滻���ܸ�ѡ��
		boolean isDuohth=con.getHasIt(" select * from xitxxb where mingc='��·���ݲ�¼��ʾ�����滻' and leib='����' and danw='��·' and zhuangt=1 and zhi='��' and diancxxb_id="+visit.getDiancxxb_id());
		String replace_str="";
		String replacetree_str="";
		if(isDuohth){
			replace_str=" replaceRec(gridDiv_ds,e.field,e.value,e.row); \n";
			replacetree_str=" replaceRecTree(gridDiv_ds,cks); \n";
			Checkbox cbselectlike=new Checkbox();
			
			cbselectlike.setId("SelectLike");
			egu.addToolbarItem(cbselectlike.getScript());
		}
		
		egu
				.addOtherScript("gridDiv_ds.on('add',function(own,rec,i){for(i=0;i<rec.length;i++){rec[i].set('GUOHSJ',guohrq.getValue().dateFormat('Y-m-d') + ' '+ HOUR.getRawValue()+':'+MIN.getRawValue()+':00');" +
						"rec[i].set('FAHRQ',guohrq.getValue().dateFormat('Y-m-d'));" +
						"rec[i].set('DAOHRQ',guohrq.getValue().dateFormat('Y-m-d'));" +
						"rec[i].set('CAIYRQ',guohrq.getValue().dateFormat('Y-m-d'));}});\n");
		if(isAutoCountMaoz){
			egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(e.record.get('BULSJ')==0){return;}if(e.field=='BIAOZ'){maoz = eval(e.record.get('BIAOZ')||0)+eval(e.record.get('PIZ')||0);if(maoz>0){e.record.set('MAOZ',maoz);}else{Ext.MessageBox.alert('��ʾ��Ϣ','ë�ص�ֵ�������!');return;}} " +
					"  if( e.field=='BIAOZ' || e.field=='MAOZ' || e.field=='PIZ' || e.field=='KOUD'  || e.field=='KOUS' || e.field=='KOUZ'){ var jingz_va=eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0)-eval(e.record.get('KOUD')||0)-eval(e.record.get('KOUS')||0)-eval(e.record.get('KOUZ')||0);e.record.set('JINGZ',jingz_va);  } " +
					"  "+replace_str+
					"  "+after_zjr+
					"     });\n");
		}else{
			egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){ if(e.field=='MAOZ' || e.field=='PIZ' || e.field=='KOUD'  || e.field=='KOUS' || e.field=='KOUZ'){ var jingz_va=eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0)-eval(e.record.get('KOUD')||0)-eval(e.record.get('KOUS')||0)-eval(e.record.get('KOUZ')||0);e.record.set('JINGZ',jingz_va);  }  " +
					"  "+replace_str+
					"" +after_zjr+
					" } );");
		}
		egu
				.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "row = irow; \n"
						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});\n");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(bob){if(bob.record.get('BULSJ')==0&&bob.column==3){bob.cancel=true;}" +
				" " +before_zjr+
				"});\n");
		

		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append(
						"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append(
						"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append(" "+ gongys_zjr)
				.append("if(cks.getDepth() == 4){ \n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.parentNode.text);\n")
				.append(
						"rec.set('MEIKXXB_ID', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
				.append(
						"rec.set('FAZ_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 3){\n")
				.append(
						"rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
				.append("rec.set('YUANMKDW', cks.parentNode.text);\n").append(
						"rec.set('FAZ_ID', cks.text);\n").append(
						"}else if(cks.getDepth() == 2){\n").append(
						"rec.set('GONGYSB_ID', cks.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.text);\n").append(
						"rec.set('YUANMKDW', cks.text);\n").append(
						"}else if(cks.getDepth() == 1){\n").append(
						"rec.set('GONGYSB_ID', cks.text); }\n").append(replacetree_str+
						"gongysTree_window.hide();\n").append("return;")
				.append("}");
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		con.Close();
	}
	
	private String getSaveScript(ExtGridUtil egu,boolean t){

		int saveMode=SysConstant.SaveMode_Upsubmit;
		String parentId=egu.gridId;
		 List columns=egu.getGridColumns(); 
		 
		StringBuffer record = new StringBuffer();
		record.append("function(){\n var "+parentId+"save_history = '';");
		if(saveMode == SysConstant.SaveMode_Allsubmit )
			record.append("var Mrcd = ").append(parentId).append("_ds.getRange();\n");
		else 
			if(saveMode == SysConstant.SaveMode_Upsubmit )
				record.append("var Mrcd = ").append(parentId).append("_ds.getModifiedRecords();\n");
			else
				if(saveMode == SysConstant.SaveMode_Selsubmit)
					record.append("var Mrcd = ").append(parentId).append("_grid.getSelectionModel().getSelections();\n");
		record.append("for(i = 0; i< Mrcd.length; i++){\n");
		if(t){record.append(" if(Mrcd[i].get('ID')=='-999') continue;");}
		record.append("if(typeof(").append(parentId).append("_save)=='function'){ var revalue = ").append(parentId).append("_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n");
		StringBuffer sb = new StringBuffer();
		sb.append(parentId).append("save_history += '<result>' ")
		.append("+ '<sign>U</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.editor != null && !gc.editor.allowBlank) {
					if(GridColumn.DataType_Float.equals(gc.datatype)) {
						String min = gc.editor.getMinValue();
						min = "".equals(min)?"-100000000":min;
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("') < ").append(min).append("){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�").append(gc.header).append(" ������Сֵ "+min+"');return;\n}");
						String max = gc.editor.getMaxValue();
						max = "".equals(max)?"100000000000":max;
						record.append("if(").append(" Mrcd[i].get('").append(gc.dataIndex).append("') >").append(max).append("){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ").append(gc.header).append(" �������ֵ "+max+"');return;\n}");
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("')!=0 && ").append("Mrcd[i].get('").append(gc.dataIndex).append("') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ").append(gc.header).append(" ����Ϊ��');return;\n}");
					}else {
						record.append("if(").append("Mrcd[i].get('").append(gc.dataIndex).append("') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ").append(gc.header).append(" ����Ϊ��');return;\n}");
					}
				}
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'))?").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("'):").append("Mrcd[i].get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("Mrcd[i].get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		record.append(sb);
		record.append(" + '</result>' ; }\n"); 
		record.append("if(").append(parentId).append("_history=='' && ").append(parentId).append("save_history==''){ \n");
		if(saveMode == SysConstant.SaveMode_Selsubmit){
			
			record.append("Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ��������Ϣ');\n");
		}else{
			
			record.append("Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n");
		}
		record.append("}else{\n").append("var Cobj = document.getElementById('CHANGE');");
		record.append("Cobj.value = ").append("'<result>'+").append(parentId).append("_history").append("+").append(parentId).append("save_history+'</result>';");
		record.append("document.getElementById('").append("SaveButton").append("').click();")
//		���ӱ������ʾ
//		.append("Ext.MessageBox.alert('��ʾ��Ϣ','���ڴ�������...');")
		.append(MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200))
		.append("\n}\n}");
		return record.toString();
	
	}
	
	private String getInsertScript(ExtGridUtil egu,boolean t){
		
		String parentId=egu.gridId;
	    String 	handler = "function() {\n" +
		"Ext.MessageBox.prompt('��ʾ', '��������Ӽ�¼��', function(btn,text){" +
		"if(btn=='ok'){if(text>0){" +
		" var line="+parentId+"_ds.getCount();\n";
		if(t) handler+=" if(gridDiv_ds.getCount()<=0){null; }else{ line="+parentId+"_ds.getCount()-1;} \n  ";
		handler+=" for(i=0;i<text;i++){" +
		getRecordScript(0,egu) +parentId+"_ds.insert("
		+"line"+",plant);} ";
		if(t) handler+=" zongjRow(gridDiv_ds,gridDiv_plant,diancTree_treePanel ) ; ";
		handler+="     }}});}\n";
		
		return handler;
	}
	
     private String getRecordScript(int type,ExtGridUtil egu) {
    	String parentId=egu.gridId;
        List columns=egu.getGridColumns(); 
		StringBuffer record = new StringBuffer();
		record.append("var plant = new ").append(parentId).append("_plant({");
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(type == 0) {
					record.append(gc.dataIndex).append(": '").append(gc.defaultvalue).append("',");
				}else 
				if(type ==1 ){
					record.append(gc.dataIndex).append(": rec.get('").append(gc.dataIndex).append("'),");
				}
			}
		}
		record.deleteCharAt(record.length()-1);
		record.append("});\n");
		return record.toString();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript1() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// ����Сʱ������
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	// ���÷���������
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}

	// ������������
	public IDropDownBean getSecValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getSecModel().getOptionCount(); i++) {
				Object obj = getSecModel().getOption(i);
				if (DateUtil.getSeconds(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setSecValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setSecValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getSecModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getSecModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setSecModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getSecModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setSecModel(new IDropDownModel(listMin));
	}
//�����ļ���
	public IDropDownBean getWenjmSelectValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean7() == null) {
				Object obj = getMinModel().getOption(0);
			    setWenjmSelectValue((IDropDownBean) obj);
				
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean7();
	}

	public void setWenjmSelectValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getWenjmSelectModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel7() == null) {
			getWenjmSelectModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel7();
	}

	public void setWenjmSelectModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel7(_value);
	}

	public void getWenjmSelectModels() {
	
		setWenjmSelectModel(new IDropDownModel(this.getWenjm()));
	}
//	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb where leix=1 order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel5() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel5();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel5(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel6() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel6();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel6(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
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
	//�õ������µ������ļ���
private synchronized List getWenjm(){
	Visit visit = (Visit) this.getPage().getVisit();
	File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwj");
	String filenames[];
	if(filepath.isDirectory()){
	 filenames=	filepath.list();
     ArrayList filenamesList=new ArrayList();

     if(filenames.length<=0){
         ArrayList filenamesListerror=new ArrayList();
		  filenamesListerror.add(new IDropDownBean(0,"����û������"));
		  return filenamesListerror;
	 }
	  for(int i=0;i<filenames.length;i++){
		IDropDownBean filenamebean=new IDropDownBean(i+1,filenames[i]);
		filenamesList.add(filenamebean);
	  }
	  return filenamesList;
	}else{
		WriteLog.writeErrorLog(ErrorMessage.Hengdxg006);
		setMsg("�ļ������������");
		  ArrayList filenamesListerror=new ArrayList();
		  filenamesListerror.add(new IDropDownBean(0,"����û������,�򱣴�·������"));
		  return filenamesListerror;
	}
//	File oldfilename = new File(filepath+"\\"+getHengdSelectValue().getValue()) ;
//	boolean success = oldfilename.delete();
}
//���ݲ���
private boolean datacopy(String wenjm){
	Visit visit=(Visit)this.getPage().getVisit();
	try{
	File file = new File(visit.getXitwjjwz() + "/shul/jianjwj/"+wenjm);
	File filepath = new File(visit.getXitwjjwz() + "/shul/jianjwjbak/"+wenjm.substring(0, 4)+"/"+wenjm.substring(4, 6));
	File bakfile = new File(filepath+"/",wenjm);
	if(bakfile.exists()) {
		bakfile.delete();
	}
	if (!filepath.exists()) {
		filepath.mkdirs();
	}
	if(!file.renameTo(bakfile)) {
		
		return false;
	}
	}catch(Exception e){
		return false;
	}
	return true;
}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean6(false);
			setRiq(DateUtil.FormatDate(new Date()));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			setSecValue(null);
			setSecModel(null);
			this.setWenjmSelectModel(null);
			this.setWenjmSelectValue(null);
			visit.setDefaultTree(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			getSelectData();
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
	}
	private class Guohxx {

		
		public String _maoz;

		public String _piz;

		public String _sud;

		public String _cheph;

		public Guohxx( String maoz, String piz, String sud, String cheph) {

			_maoz = maoz;
			_piz = piz;
			_sud = sud;
			_cheph = cheph;
		}
	}
}

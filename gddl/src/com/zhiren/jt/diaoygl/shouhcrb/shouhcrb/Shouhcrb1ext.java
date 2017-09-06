package com.zhiren.jt.diaoygl.shouhcrb.shouhcrb;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.tools.FtpCreatTxt;
import com.zhiren.common.tools.FtpUpload;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*����:���ܱ�
 * ����:2009-8-5 10:48:12
 * �޸�ԭ��:�պĴ�ҳ�����С��ϱ����ܡ���ť���뱨��ģ�����ϱ����ܹ����ظ����ڱ�ģ����ȡ����ֻ����ˢ�ºͱ��湦�ܡ�(�Ź����)
 */

/*����:���ܱ�
 * ʱ��:2009-8-5 13:51:54
 * ˵��:�������û�����ʱ,����������ϴ���,��糧���Ʊ���ɫ��ǳ��ɫ,������ֹ����,�糧����ɫ����.Ŀǰʹ�õ�DIANCSCWJM�ֶ����ж��Ƿ����ϴ���
 * �ϴ��ĵ糧DIANCSCWJM�ֶ�Ϊ��,�ֹ���ĵ糧DIANCSCWJM�ֶε�ֵΪ"�ֹ���д"
 */

/*����:sy
 * ʱ��:2009-8-11 
 * ˵��:�ֹ������sqlʱ��DIANCSCWJMѭ���ظ����飬�޸�i<rsl.getColumnCount()-1
 */

/*����:ll
 * ʱ��:2009-8-13 
 * ˵��:�޸�ҳ����ʾ���ݵ�����˳��
 */
public  class Shouhcrb1ext extends BasePage implements PageValidateListener {
	
	private String ZhongNbm="000007";
	private String ZhongNyh="000007";
	private String ZhongNmm="000007";
	private String ZhongNip="210.77.187.2";
	private String Baohys;
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
	private void Save() {
		if(!this.isZuorkc()){
		
			return;
		}
		int flag=0;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		while (rsl.next()) {
			if(rsl.getString("DIANCXXB_ID").equals("�ϼ�")){

			}else{
				StringBuffer sql2 = new StringBuffer();
				sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
				if ("0".equals(rsl.getString("ID"))) {
					
					
					long kuc=rsl.getLong("KUC");
					if(kuc!=0){//�жϵ����Ϊ0 ��ʱ��������
						sql.append("insert into ").append("shouhcrbb").append("(id");
						for (int i = 1; i < rsl.getColumnCount()-1; i++) {
							sql.append(",").append(rsl.getColumnNames()[i]);
							sql2.append(",").append(
									visit.getExtGrid1().getValueSql(
											visit.getExtGrid1().getColumn(
													rsl.getColumnNames()[i]),
											rsl.getString(i)));
						}
						sql.append(",DIANCSCWJM) values(").append(sql2).append(",'�ֹ���д');\n");
						
					
					}
					
				} else {
					sql.append("update ").append("shouhcrbb").append(" set ");
					for (int i = 1; i < rsl.getColumnCount(); i++) {
						sql.append(rsl.getColumnNames()[i]).append(" = ");
						sql.append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i))).append(",");
							}
					sql.deleteCharAt(sql.length() - 1);
					sql.append(" where id =").append(rsl.getString("ID")).append(";\n");
					}
				}
			}
		
		sql.append("end;");
	//	System.out.println(sql);
		flag=con.getUpdate(sql.toString());
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.commit();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _ShangbChick = false;

	public void ShangbButton(IRequestCycle cycle) {
		_ShangbChick = true;
	}
	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_ShangbChick){
			_ShangbChick=false;
			ShangbTXTFile();
			returnMsg=true;
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	private void getSettings(){
		JDBCcon cn = new JDBCcon();
		try {
			ResultSet rs = cn.getResultSet("select zhi from xitxxb where mingc='���ܱ���'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNbm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp�û�'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNyh=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp����'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNmm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='����Ftp������ip'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNip=rs.getString("zhi");
				};
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.closeRs();
			cn.Close();
		}
	}
	
	public void getSelectData() {
		
		if(!this.isZuorkc()){
			this.setMsg("��������û����д,������д��������!");
			returnMsg=true;
		}else{
			this.setMsg("");
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		String chaxun="";
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		
		
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=" and dc.id= " +this.getTreeid();
		} 
		
		//***************�жϵ糧�Ƿ�ʹ��ʱ�������ϸ**********************//
		long falg=0;
		
		String sql_ts =" select zhi from xitxxb where mingc = '�糧�Ƿ�ʹ��ʱ�������ϸ' and diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();//�ж�ϵͳ��Ϣ���С��Ƿ��л��ܿ�����������
		
		ResultSet rs1=con.getResultSet(sql_ts);
		try {
			if(rs1.next()){
				 falg=rs1.getLong("zhi");
			}
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String chaxun1="";//�ɷ���
		String chaxun2="";//���ɷ���
		
		chaxun1 = "select sum(nvl(dt.id,0)) as id,\n"
			+ "       decode( d.riq,null,'�ϼ�',to_char(nvl( d.riq,to_date('"+riqTiaoj+"','yyyy-mm-dd')),'yyyy-mm-dd')) as riq,\n"
			+ "       decode(d.mingc,null,'�ϼ�',d.mingc) as diancxxb_id,\n"+
			/*+ "		  sum(nvl(dt.fadl,0)) as fadl,"
			+ "       sum(nvl(zt.kuc,0)) as SHANGYKC,\n"
			+ "       sum(nvl(dt.dangrgm,0)) as dangrgm,\n"
			+ "       sum(nvl(lj.leijgm,0)) as leijgm,\n"
			+ "		  sum(nvl(dt.fady,0)) as fady,\n"
			+ " 	  sum(nvl(dt.gongry,0)) as gongry,\n"
			+ "       sum(nvl(dt.qity,0)) as qity,\n"
			+ "       sum(nvl(dt.cuns,0)) as cuns,\n"
			+ "       sum(nvl(dt.yuns,0)) as yuns,\n"
			+ "       sum(nvl(dt.shuifctz,0)) as shuifctz,\n"
			+ "       sum(nvl(dt.panyk,0)) as panyk,\n"
			+ "       sum(nvl(dt.haoyqkdr,0)) as haoyqkdr,\n"
			+ "       sum(nvl(lj.leijhm,0)) as leijhm,\n"
			+ "       sum(nvl(dt.tiaozl,0)) as tiaozl,\n"
			+ "		  sum(nvl(dt.kuc,0)) as kuc, \n"
			+ "		  sum(nvl(dt.shangbkc,0)) as shangbkc,\n"
		    + "		  sum(nvl(dt.kuc,0)-nvl(dt.shangbkc,0)) as chal, \n"
		    + "		  sum(nvl(dt.quemtjts,0)) as quemtjts, \n"
		    + "		  sum(nvl(dt.quemtjrl,0)) as quemtjrl, \n"
		    + "		  max(dt.DIANCSCWJM) as diancscwjm \n"*/
//		    -----------------------�޸ĺ�---------------------------------------------------
//			"	   sum(nvl(zt.kuc,0)) as SHANGYKC,\n" +
			"      decode(d.riq,null,' ',sum(zt.kuc)) as SHANGYKC,\n"+
		    "      sum(nvl(dt.dangrgm,0)) as dangrgm,\n" + 
		    "      sum(nvl(lj.leijgm,0)) as leijgm,\n" + 
		    "      sum(nvl(dt.haoyqkdr,0)) as haoyqkdr,\n" + 
		    "      sum(nvl(lj.leijhm,0)) as leijhm,\n" + 
		    "      sum(nvl(dt.tiaozl,0)) as tiaozl,\n" + 
		    "      decode(d.riq,null,' ',sum(nvl(dt.kuc, 0))) as kuc,\n"+
//		    "      sum(nvl(dt.kuc,0)) as kuc,\n" + 
		    "      sum(nvl(dt.quemtjts,0)) as quemtjts,\n" + 
		    "      sum(nvl(dt.quemtjrl,0)) as quemtjrl,\n" + 
		    "      sum(nvl(dt.fadl,0)) as fadl,\n" + 
		    "       sum(nvl(dt.fady,0)) as fady,\n" + 
		    "       sum(nvl(dt.gongry,0)) as gongry,\n" + 
		    "      sum(nvl(dt.qity,0)) as qity,\n" + 
		    "      sum(nvl(dt.cuns,0)) as cuns,\n" + 
		    "      sum(nvl(dt.yuns,0)) as yuns,\n" + 
		    "      sum(nvl(dt.shuifctz,0)) as shuifctz,\n" + 
		    "      sum(nvl(dt.panyk,0)) as panyk,\n" + 
		    "       sum(nvl(dt.shangbkc,0)) as shangbkc,\n" + 
		    "       sum(nvl(dt.kuc,0)-nvl(dt.shangbkc,0)) as chal,\n" + 
		    "       max(dt.DIANCSCWJM) as diancscwjm"

		    
			+ "  from (select h.*\n"
			+ "          from shouhcrbb h,diancxxb dc \n"
			+ "         where h.diancxxb_id = dc.id "+strdiancTreeID+" and h.riq >= first_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd')) \n"
			+ "           and h.riq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')) dt,\n"
			+ "       (select h.diancxxb_id,h.riq+1  as riq, h.kuc\n"
			+ "          from shouhcrbb h, diancxxb dc\n"
			+ "         where h.diancxxb_id = dc.id "+strdiancTreeID+"\n"
			+ "           and riq >=first_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd') - 1)"
			+ "           and riq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd') - 1) zt,\n"
			+ "       (select dc.id, dc.mingc, px.xuh,rq.riq\n"
			+ "          from diancxxb dc, dianckjpxb px,\n"
			+ " (select first_day(to_date('"+riqTiaoj+"','yyyy-mm-dd'))+rownum-1 as riq from all_objects \n"
			+ "		where rownum<=(to_date('"+riqTiaoj+"','yyyy-mm-dd')-first_day(to_date('"+riqTiaoj+"','yyyy-mm-dd'))+1)) rq\n "
			+ "         where dc.id = px.diancxxb_id(+)\n"
			+ "           "+strdiancTreeID+"\n"
			+ "           and (px.kouj = '����ȼ���ձ�' or px.kouj = '�պĴ��ձ�')) d,\n"
			+ "       (select rb.diancxxb_id,rb.riq,\n"
			+ "               get_ljsl(rb.diancxxb_id,rb.riq,'dangrgm') as leijgm,\n"
			+ "               get_ljsl(rb.diancxxb_id,rb.riq,'haoyqkdr') as leijhm\n"
			+ "          from shouhcrbb rb,diancxxb dc\n"
			+ "         where rb.diancxxb_id = dc.id "+strdiancTreeID+" \n"
			+ "           and rb.riq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n "
			+ "           and rb.riq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "         group by rb.diancxxb_id,rb.riq) lj,\n"
			+ "		  (select sum(jingz) as laimsl, fh.diancxxb_id,fh.daohrq as riq\n"
			+ "          from fahb fh, diancxxb dc\n"
			+ "         where daohrq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd')) and daohrq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and fh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by fh.diancxxb_id,fh.daohrq) fah,\n"
			+ "       (select fh.diancxxb_id,fh.daohrq as riq,get_fhlaimsl_lj(fh.diancxxb_id,fh.daohrq) as laimsl\n"
			+ "          from fahb fh, diancxxb dc\n"
			+ "         where daohrq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and daohrq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n"
			+ "           and fh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by fh.diancxxb_id,fh.daohrq) ljfah,\n"
			+ "       (select mh.diancxxb_id,mh.rulrq as riq,\n"
			+ "               sum(fadhy) as fadhy,\n"
			+ "               sum(gongrhy) as gongrhy,\n"
			+ "               sum(qity) as qity\n"
			+ "          from meihyb mh, diancxxb dc\n"
			+ "         where mh.rulrq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd')) and mh.rulrq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and mh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by mh.diancxxb_id,mh.rulrq) mhy,\n"
			+ "       (select mh.diancxxb_id,mh.rulrq as riq,\n"
			+ "               get_mhshul_lj(mh.diancxxb_id,mh.rulrq,'fadhy') as fadhy,\n"
			+ "               get_mhshul_lj(mh.diancxxb_id,mh.rulrq,'gongrhy') as gongrhy,\n"
			+ "               get_mhshul_lj(mh.diancxxb_id,mh.rulrq,'qity') as qity\n"
			+ "          from meihyb mh, diancxxb dc\n"
			+ "         where mh.rulrq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and mh.rulrq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n"
			+ "           and mh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by mh.diancxxb_id,mh.rulrq) ljmhy\n"
			+ " where dt.diancxxb_id(+) = d.id and dt.riq(+) = d.riq\n"
			+ "   and zt.diancxxb_id(+) = d.id and zt.riq(+) = d.riq\n"
			+ "   and lj.diancxxb_id(+) = d.id and lj.riq(+) = d.riq\n"
			+ "   and fah.diancxxb_id(+) = d.id and fah.riq(+) = d.riq\n"
			+ "   and ljfah.diancxxb_id(+) = d.id and ljfah.riq(+) = d.riq\n"
			+ "   and mhy.diancxxb_id(+) = d.id and mhy.riq(+) = d.riq\n"
			+ "   and ljmhy.diancxxb_id(+) = d.id and ljmhy.riq(+) = d.riq\n"
			+ "   group by rollup (d.mingc,d.riq)\n" + "  having not grouping(d.mingc)=1 \n order by max(d.xuh) ,d.mingc,d.riq";
		
		chaxun2 = "select sum(nvl(dt.id,0)) as id,\n"
			+ "       max(nvl( dt.riq,to_date('"+this.getRiqi()+"','yyyy-mm-dd'))) as riq,\n"
			+ "       decode(d.mingc,null,'�ϼ�',d.mingc) as diancxxb_id,\n"
			/*+ "		  sum(nvl(dt.fadl,0)) as fadl,"
			+ "       sum(nvl(zt.kuc,0)) as SHANGYKC,\n"
			+ "       sum(nvl(dt.dangrgm,0)) as dangrgm,\n"
			+ "       sum(nvl(lj.leijgm,0)) as leijgm,\n"
			+ "		  sum(nvl(dt.fady,0)) as fady,\n"
			+ " 	  sum(nvl(dt.gongry,0)) as gongry,\n"
			+ "       sum(nvl(dt.qity,0)) as qity,\n"
			+ "       sum(nvl(dt.cuns,0)) as cuns,\n"
			+ "       sum(nvl(dt.yuns,0)) as yuns,\n"
			+ "       sum(nvl(dt.shuifctz,0)) as shuifctz,\n"
			+ "       sum(nvl(dt.panyk,0)) as panyk,\n"
			+ "       sum(nvl(dt.haoyqkdr,0)) as haoyqkdr,\n"
			+ "       sum(nvl(lj.leijhm,0)) as leijhm,\n"
			+ "       sum(nvl(dt.tiaozl,0)) as tiaozl,\n"
			+ "		  sum(nvl(dt.kuc,0)) as kuc, \n"
			+ "		  sum(nvl(dt.shangbkc,0)) as shangbkc,\n"
		    + "		  sum(nvl(dt.kuc,0)-nvl(dt.shangbkc,0)) as chal, \n"
		    + "		  sum(nvl(dt.quemtjts,0)) as quemtjts, \n"
		    + "		  sum(nvl(dt.quemtjrl,0)) as quemtjrl, \n"
		    + "		  max(dt.DIANCSCWJM) as diancscwjm \n"*/
//		    -----------------------�޸ĺ�---------------------------------------------------
			+"	   sum(nvl(zt.kuc,0)) as SHANGYKC,\n" +
		    "      sum(nvl(dt.dangrgm,0)) as dangrgm,\n" + 
		    "      sum(nvl(lj.leijgm,0)) as leijgm,\n" + 
		    "      sum(nvl(dt.haoyqkdr,0)) as haoyqkdr,\n" + 
		    "      sum(nvl(lj.leijhm,0)) as leijhm,\n" + 
		    "      sum(nvl(dt.tiaozl,0)) as tiaozl,\n" + 
		    "      sum(nvl(dt.kuc,0)) as kuc,\n" + 
		    "      sum(nvl(dt.quemtjts,0)) as quemtjts,\n" + 
		    "      sum(nvl(dt.quemtjrl,0)) as quemtjrl,\n" + 
		    "      sum(nvl(dt.fadl,0)) as fadl,\n" + 
		    "       sum(nvl(dt.fady,0)) as fady,\n" + 
		    "       sum(nvl(dt.gongry,0)) as gongry,\n" + 
		    "      sum(nvl(dt.qity,0)) as qity,\n" + 
		    "      sum(nvl(dt.cuns,0)) as cuns,\n" + 
		    "      sum(nvl(dt.yuns,0)) as yuns,\n" + 
		    "      sum(nvl(dt.shuifctz,0)) as shuifctz,\n" + 
		    "      sum(nvl(dt.panyk,0)) as panyk,\n" + 
		    "       sum(nvl(dt.shangbkc,0)) as shangbkc,\n" + 
		    "       sum(nvl(dt.kuc,0)-nvl(dt.shangbkc,0)) as chal,\n" + 
		    "       max(dt.DIANCSCWJM) as diancscwjm"
		    
			+ "  from (select *\n"
			+ "          from shouhcrbb h\n"
			+ "         where h.riq = to_date('"+riqTiaoj+"', 'yyyy-mm-dd')) dt,\n"
			+ "       (select h.diancxxb_id, h.kuc\n"
			+ "          from shouhcrbb h, diancxxb dc\n"
			+ "         where h.diancxxb_id = dc.id\n"
			+ "           and riq = to_date('"+riqTiaoj+"', 'yyyy-mm-dd') - 1) zt,\n"
			+ "       (select dc.id, dc.mingc, px.xuh\n"
			+ "          from diancxxb dc, dianckjpxb px\n"
			+ "         where dc.id = px.diancxxb_id(+)\n"
			+ "           "+strdiancTreeID+"\n"
			+ "           and (px.kouj = '����ȼ���ձ�' or px.kouj = '�պĴ��ձ�')) d,\n"
			+ "       (select rb.diancxxb_id,\n"
			+ "               sum(rb.dangrgm) as leijgm,\n"
			+ "               sum(rb.haoyqkdr) as leijhm\n"
			+ "          from shouhcrbb rb\n"
			+ "         where rb.riq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n"
			+ "           and rb.riq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "         group by rb.diancxxb_id) lj,\n"
			+ "		  (select sum(jingz) as laimsl, fh.diancxxb_id\n"
			+ "          from fahb fh, diancxxb dc\n"
			+ "         where daohrq = to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and fh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by fh.diancxxb_id) fah,\n"
			+ "       (select sum(jingz) as laimsl, fh.diancxxb_id\n"
			+ "          from fahb fh, diancxxb dc\n"
			+ "         where daohrq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and daohrq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n"
			+ "           and fh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by fh.diancxxb_id) ljfah,\n"
			+ "       (select mh.diancxxb_id,\n"
			+ "               sum(fadhy) as fadhy,\n"
			+ "               sum(gongrhy) as gongrhy,\n"
			+ "               sum(qity) as qity\n"
			+ "          from meihyb mh, diancxxb dc\n"
			+ "         where mh.rulrq = to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and mh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by mh.diancxxb_id) mhy,\n"
			+ "       (select mh.diancxxb_id,\n"
			+ "               sum(fadhy) as fadhy,\n"
			+ "               sum(gongrhy) as gongrhy,\n"
			+ "               sum(qity) as qity\n"
			+ "          from meihyb mh, diancxxb dc\n"
			+ "         where mh.rulrq <= to_date('"+riqTiaoj+"', 'yyyy-mm-dd')\n"
			+ "           and mh.rulrq >= First_day(to_date('"+riqTiaoj+"', 'yyyy-mm-dd'))\n"
			+ "           and mh.diancxxb_id = dc.id\n"
			+ "            "+strdiancTreeID+"\n"
			+ "         group by mh.diancxxb_id) ljmhy\n"
			+ " where dt.diancxxb_id(+) = d.id\n"
			+ "   and zt.diancxxb_id(+) = d.id\n"
			+ "   and lj.diancxxb_id(+) = d.id\n"
			+ "   and fah.diancxxb_id(+) = d.id\n"
			+ "   and ljfah.diancxxb_id(+) = d.id\n"
			+ "   and mhy.diancxxb_id(+) = d.id\n"
			+ "   and ljmhy.diancxxb_id(+) = d.id\n"
			+ "   group by rollup (d.mingc)\n" + " order by max(d.xuh) ,d.mingc";
		
		
		if (jib==3){//���û�Ϊ�糧ʱ����ʾ����ǰ�µĵ�һ�쵽ѡ��������ֹ���������ݡ�
			if (falg==1){//�ɷ���
				chaxun=chaxun1;
			}else{//���ɷ���
				chaxun=chaxun2;
			}
		}else{
			chaxun=chaxun2;
		}
		
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcrbb");
		egu.setWidth("bodyWidth");
		egu.getColumn("riq").setHeader("����");
		if (jib==3){//�糧�û�
			if (falg==1){
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("riq").setHeader("����");
				egu.getColumn("riq").setEditor(null);
			}else{
				egu.getColumn("diancxxb_id").setHeader("��λ");
				egu.getColumn("riq").setHidden(true);
				egu.getColumn("riq").setEditor(null);
			}
		}else{
			egu.getColumn("diancxxb_id").setHeader("��λ");
			egu.getColumn("riq").setHidden(true);
			egu.getColumn("riq").setEditor(null);
		}
		/*egu.getColumn("fadl").setHeader("������(��kWh)");
		egu.getColumn("dangrgm").setHeader("���չ�ú(t)");
		egu.getColumn("leijgm").setHeader("�ۼƹ�ú(t)");
		egu.getColumn("fady").setHeader("�������(t)");
		egu.getColumn("gongry").setHeader("���Ⱥ���(t)");
		egu.getColumn("qity").setHeader("��������(t)");
		egu.getColumn("cuns").setHeader("����(t)");
		egu.getColumn("yuns").setHeader("����(t)");	
		egu.getColumn("shuifctz").setHeader("ˮ�ֲ����(t)");
		egu.getColumn("panyk").setHeader("��ӯ��(t)");
		egu.getColumn("haoyqkdr").setHeader("���պ���(t)");
		egu.getColumn("haoyqkdr").setEditor(null);
		egu.getColumn("leijhm").setHeader("�ۼƺ���(t)");
		egu.getColumn("leijhm").setUpdate(false);
		egu.getColumn("leijhm").setEditor(null);
		egu.getColumn("leijgm").setEditor(null);
		egu.getColumn("kuc").setEditor(null);
		egu.getColumn("SHANGYKC").setEditor(null);		
		egu.getColumn("leijgm").setUpdate(false);		
		egu.getColumn("SHANGYKC").setHeader("���տ��(t)");
		egu.getColumn("SHANGYKC").setUpdate(false);
		egu.getColumn("kuc").setHeader("���(t)");
		egu.getColumn("tiaozl").setHeader("������(t)");
		egu.getColumn("shangbkc").setHeader("�ϱ����(t)");
		egu.getColumn("shangbkc").setHidden(true);
		egu.getColumn("chal").setHeader("������(t)");
		egu.getColumn("chal").setHidden(true);
		egu.getColumn("shangbkc").setEditor(null);
		egu.getColumn("chal").setEditor(null);
		egu.getColumn("chal").setUpdate(false);
		egu.getColumn("quemtjts").setHeader("ȱúͣ��̨��(̨)");
		egu.getColumn("quemtjrl").setHeader("ȱúͣ������(��ǧ��)");
		egu.getColumn("diancscwjm").setHeader("�Ƿ��ϴ�");
		egu.getColumn("diancscwjm").setEditor(null);
		egu.getColumn("diancscwjm").setHidden(true);*/


//				---------------�޸ĺ�-------------------------------------
				egu.getColumn("SHANGYKC").setHeader("���տ��(t)");
				egu.getColumn("SHANGYKC").setUpdate(false);		
				egu.getColumn("dangrgm").setHeader("���չ�ú(t)");
				egu.getColumn("leijgm").setHeader("�ۼƹ�ú(t)");
				egu.getColumn("leijgm").setUpdate(false);
				egu.getColumn("leijgm").setEditor(null);
				egu.getColumn("haoyqkdr").setHeader("���պ���(t)");
				egu.getColumn("haoyqkdr").setEditor(null);
				egu.getColumn("leijhm").setHeader("�ۼƺ���(t)");
				egu.getColumn("leijhm").setUpdate(false);
				egu.getColumn("leijhm").setEditor(null);
				egu.getColumn("tiaozl").setHeader("������(t)");
				egu.getColumn("kuc").setHeader("���(t)");
				egu.getColumn("kuc").setEditor(null);
				egu.getColumn("quemtjts").setHeader("ȱúͣ��̨��(̨)");
				egu.getColumn("quemtjrl").setHeader("ȱúͣ������(��ǧ��)");
				egu.getColumn("fadl").setHeader("������(��kWh)");		
				egu.getColumn("fady").setHeader("�������(t)");
				egu.getColumn("gongry").setHeader("���Ⱥ���(t)");
				egu.getColumn("qity").setHeader("��������(t)");		
				egu.getColumn("yuns").setHeader("����(t)");
				egu.getColumn("cuns").setHeader("����(t)");
				egu.getColumn("shuifctz").setHeader("ˮ�ֲ����(t)");
				egu.getColumn("panyk").setHeader("��ӯ��(t)");
				egu.getColumn("SHANGYKC").setEditor(null);		
				egu.getColumn("shangbkc").setHeader("�ϱ����(t)");
				egu.getColumn("shangbkc").setHidden(true);
				egu.getColumn("shangbkc").setEditor(null);
				egu.getColumn("chal").setHeader("������(t)");
				egu.getColumn("chal").setHidden(true);		
				egu.getColumn("chal").setEditor(null);
				egu.getColumn("chal").setUpdate(false);
				egu.getColumn("diancscwjm").setHeader("�Ƿ��ϴ�");
				egu.getColumn("diancscwjm").setEditor(null);
				egu.getColumn("diancscwjm").setHidden(true);
		//�趨�еĳ�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(110);
		egu.getColumn("fadl").setWidth(90);
		egu.getColumn("shangykc").setWidth(70);
		egu.getColumn("dangrgm").setWidth(70);
		egu.getColumn("leijgm").setWidth(80);
		egu.getColumn("fady").setWidth(70);
		egu.getColumn("gongry").setWidth(70);
		egu.getColumn("qity").setWidth(70);
		egu.getColumn("cuns").setWidth(70);
		egu.getColumn("yuns").setWidth(70);
		egu.getColumn("shuifctz").setWidth(80);
		egu.getColumn("panyk").setWidth(70);
		egu.getColumn("haoyqkdr").setWidth(70);
		egu.getColumn("leijhm").setWidth(80);
		egu.getColumn("tiaozl").setWidth(60);
		egu.getColumn("kuc").setWidth(70);
		egu.getColumn("shangbkc").setWidth(70);
		egu.getColumn("chal").setWidth(70);
		egu.getColumn("quemtjts").setWidth(100);
		egu.getColumn("quemtjrl").setWidth(120);
		
		
		//�趨���ɱ༭�е���ɫ
		egu.getColumn("SHANGYKC").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leijgm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("haoyqkdr").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leijhm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("shangbkc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("chal").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		
		/*����:���ܱ�
		 * ʱ��:2009-8-5 13:51:54
		 * ˵��:�������û�����ʱ,����������ϴ���,��糧���Ʊ���ɫ��ǳ��ɫ,������ֹ����,�糧����ɫ����.Ŀǰʹ�õ�DIANCSCWJM�ֶ����ж��Ƿ����ϴ���
		 * �ϴ��ĵ糧DIANCSCWJM�ֶ�Ϊ��,�ֹ���ĵ糧DIANCSCWJM�ֶε�ֵΪ"�ֹ���д"
		 */
		if(jib==1||jib==2){
			egu.getColumn("diancxxb_id").setRenderer("function(value,metadata,rec,rowIndex){if(rec.get('DIANCSCWJM')==''){metadata.css='tdTextextGreen';} return value;}");
			
		}
		
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(1000);
		egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		//System.out.println(this.getRiqi());
		
		
		
		//*************************������*****************************************88
		//�糧������
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		
		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		// �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		
		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
	
		//*****************************************�ʺϵ糧��ʱ������JS���㷽ʽ*****************************************//
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){ \n");
		sb.append("if(e.field == 'DANGRGM'){ \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var rec = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("rec.set('LEIJGM',parseFloat(e.record.get('DANGRGM')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("rec1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("rec.set('LEIJGM',parseFloat(rec1.get('LEIJGM')) + parseFloat(rec.get('DANGRGM'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("e.record.set('HAOYQKDR',parseFloat(e.record.get('FADY')==''?0:e.record.get('FADY'))+ \n");
		sb.append("parseFloat(e.record.get('GONGRY')==''?0:e.record.get('GONGRY'))+ \n");
		sb.append("parseFloat(e.record.get('QITY')==''?0:e.record.get('QITY'))+ \n");
		sb.append("parseFloat(e.record.get('CUNS')==''?0:e.record.get('CUNS'))+ \n");
		sb.append("parseFloat(e.record.get('YUNS')==''?0:e.record.get('YUNS'))+ \n");
		sb.append("parseFloat(e.record.get('PANYK')==''?0:e.record.get('PANYK'))+ \n");
		sb.append("parseFloat(e.record.get('SHUIFCTZ')==''?0:e.record.get('SHUIFCTZ'))); \n");
		sb.append("if(e.field == 'FADY'){//���� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var FADY = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("FADY.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("FADY1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("FADY.set('LEIJHM',parseFloat(FADY1.get('LEIJHM')) + parseFloat(FADY.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("FADY.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("FADY.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("if(e.field == 'GONGRY'){//���� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var GONGRY = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("GONGRY.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("GONGRY1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("GONGRY.set('LEIJHM',parseFloat(GONGRY1.get('LEIJHM')) + parseFloat(GONGRY.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("GONGRY.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("GONGRY.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("if(e.field == 'QITY'){//���� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var QITY = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("QITY.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("QITY1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("QITY.set('LEIJHM',parseFloat(QITY1.get('LEIJHM')) + parseFloat(QITY.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("QITY.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("QITY.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("if(e.field == 'CUNS'){//���� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var CUNS = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("CUNS.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("CUNS1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("CUNS.set('LEIJHM',parseFloat(CUNS1.get('LEIJHM')) + parseFloat(CUNS.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("CUNS.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("CUNS.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("if(e.field == 'YUNS'){//���� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var YUNS = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("YUNS.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("YUNS1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("YUNS.set('LEIJHM',parseFloat(YUNS1.get('LEIJHM')) + parseFloat(YUNS.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("YUNS.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("YUNS.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("if(e.field == 'SHUIFCTZ'){//ˮ�ݵ����� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var SHUIFCTZ = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("SHUIFCTZ.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("SHUIFCTZ1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("SHUIFCTZ.set('LEIJHM',parseFloat(SHUIFCTZ1.get('LEIJHM')) + parseFloat(SHUIFCTZ.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("SHUIFCTZ.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("SHUIFCTZ.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("if(e.field == 'PANYK'){//��ӯ�� \n");
		sb.append("var size = gridDiv_ds.getCount();//������ \n");
		sb.append("var i=gridDiv_ds.indexOf(gridDiv_sm.getSelected());//�õ���ǰѡ�е��к� \n");
		sb.append("for (i;i<size;i++){//ѭ����ֵ \n");
		sb.append("var PANYK = gridDiv_ds.getAt(i);//�õ���ǰ�С� \n");
		sb.append("if (i==0){//��һ�� \n");
		sb.append("PANYK.set('LEIJHM',parseFloat(e.record.get('HAOYQKDR')));//����һ��ʱ���չ�ú=�ۼƹ�ú \n");
		sb.append("}else{ \n");
		sb.append("PANYK1 = gridDiv_ds.getAt(i-1);//�õ���һ���С� \n");
		sb.append("PANYK.set('LEIJHM',parseFloat(PANYK1.get('LEIJHM')) + parseFloat(PANYK.get('HAOYQKDR'))); \n");
		sb.append("} \n");
		sb.append("PANYK.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("PANYK.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("} \n");
		sb.append("} \n");
		sb.append("/*e.record.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+ \n");
		sb.append("parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL'))); \n");
		sb.append("e.record.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC'))); \n");
		sb.append("*/ \n");
		sb.append("rec = gridDiv_ds.getAt(gridDiv_ds.getCount()-1); \n");
		sb.append("/*if(e.field=='DANGRGM'){ \n");
		sb.append("rec.set('DANGRGM', eval(rec.get('DANGRGM')||0) + eval(e.value||0) - eval(e.originalValue||0)); \n");
		sb.append("rec.set('LEIJGM', eval(rec.get('LEIJGM')||0) + eval(e.value||0) - eval(e.originalValue||0)); \n");
		sb.append("rec.set('KUC', eval(rec.get('KUC')||0) + eval(e.value||0) - eval(e.originalValue||0));} \n");
		sb.append("if(e.field=='HAOYQKDR'){ \n");
		sb.append("rec.set('HAOYQKDR', eval(rec.get('HAOYQKDR')||0) + eval(e.value||0) - eval(e.originalValue||0)); \n");
		sb.append("rec.set('LEIJHM', eval(rec.get('LEIJHM')||0) + eval(e.value||0) - eval(e.originalValue||0)); \n");
		sb.append("rec.set('KUC', eval(rec.get('KUC')||0) - eval(e.value||0) + eval(e.originalValue||0));} \n");
		sb.append("if(e.field=='TIAOZL'){ \n");
		sb.append("rec.set('TIAOZL', eval(rec.get('TIAOZL')||0) + eval(e.value||0) - eval(e.originalValue||0)); \n");
		sb.append("rec.set('KUC', eval(rec.get('KUC')||0) + eval(e.value||0) - eval(e.originalValue||0));} \n");
		sb.append("rec.set('CHAL',rec.get('KUC'||0)-rec.get('SHANGBKC'||0)); \n");
		sb.append("*/ \n");
		sb.append("}); \n");
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�糧�в�����༭
		sb.append("});");
		
		
       //�趨�ϼ��в�����
		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return 'continue';}");
		
		
		//*****************************************���ʺϵ糧��ʱ������JS���㷽ʽ*****************************************//
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append("gridDiv_grid.on('afteredit',function(e){");
			//�����ۼƹ�ú
			sb2.append("if(e.field == 'DANGRGM'){e.record.set('LEIJGM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJGM')));}");
			//���㵱�պ���
			sb2.append("e.record.set('HAOYQKDR',parseFloat(e.record.get('FADY')==''?0:e.record.get('FADY'))+parseFloat(e.record.get('GONGRY')==''?0:e.record.get('GONGRY'))+parseFloat(e.record.get('QITY')==''?0:e.record.get('QITY'))+parseFloat(e.record.get('CUNS')==''?0:e.record.get('CUNS'))+parseFloat(e.record.get('YUNS')==''?0:e.record.get('YUNS'))+parseFloat(e.record.get('PANYK')==''?0:e.record.get('PANYK'))+parseFloat(e.record.get('SHUIFCTZ')==''?0:e.record.get('SHUIFCTZ')));");
			//�����ۼƺ�ú
			sb2.append("if(e.field == 'FADY'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb2.append("if(e.field == 'GONGRY'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb2.append("if(e.field == 'QITY'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb2.append("if(e.field == 'CUNS'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb2.append("if(e.field == 'YUNS'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb2.append("if(e.field == 'SHUIFCTZ'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb2.append("if(e.field == 'PANYK'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			//������ ;���=���չ�ú-���պ���+���տ��+������
			sb2.append("e.record.set('KUC',parseFloat(e.record.get('DANGRGM')==''?0:e.record.get('DANGRGM'))-parseFloat(e.record.get('HAOYQKDR')==''?0:e.record.get('HAOYQKDR'))+parseFloat(e.record.get('SHANGYKC')==''?0:e.record.get('SHANGYKC'))+parseFloat(e.record.get('TIAOZL')==''?0:e.record.get('TIAOZL')));");
			
			sb2.append("e.record.set('CHAL',parseFloat(e.record.get('KUC')==''?0:e.record.get('KUC'))-parseFloat(e.record.get('SHANGBKC')==''?0:e.record.get('SHANGBKC')));");
			
			//���չ�ú�����仯ʱ,�ϼ��е�(���չ�ú,�ۼƹ�ú,���)��������
			sb2.append("rec = gridDiv_ds.getAt(gridDiv_ds.getCount()-1);").append("if(e.field=='DANGRGM'){").append("rec.set('DANGRGM', eval(rec.get('DANGRGM')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('LEIJGM', eval(rec.get('LEIJGM')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('KUC', eval(rec.get('KUC')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
			
			//���պ��÷����仯ʱ,�ϼ��е�(���պ���,�ۼƺ���,���)��������
			sb2.append("if(e.field=='HAOYQKDR'){").append("rec.set('HAOYQKDR', eval(rec.get('HAOYQKDR')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('LEIJHM', eval(rec.get('LEIJHM')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('KUC', eval(rec.get('KUC')||0) - eval(e.value||0) + eval(e.originalValue||0));").append("}");
			
			//�����������仯ʱ,�ϼ��е�(������,���)�����仯
			sb2.append("if(e.field=='TIAOZL'){").append("rec.set('TIAOZL', eval(rec.get('TIAOZL')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("rec.set('KUC', eval(rec.get('KUC')||0) + eval(e.value||0) - eval(e.originalValue||0));").append("}");
			
			sb2.append("rec.set('CHAL',rec.get('KUC'||0)-rec.get('SHANGBKC'||0));");
			
		sb2.append("});");
		
		
		sb2.append("gridDiv_grid.on('beforeedit',function(e){");
		sb2.append("if(e.record.get('DIANCXXB_ID')=='�ϼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
		sb2.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�糧�в�����༭
		sb2.append("});");
		
		
       //�趨�ϼ��в�����
		sb2.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='�ϼ�') return 'continue';}");
		
		
		
		if (jib==3){//�糧
			if (falg==1){//�ɷ���
				egu.addOtherScript(sb.toString());
			}else{//���ɷ���
				egu.addOtherScript(sb2.toString());
			}
		}else{
			egu.addOtherScript(sb2.toString());
		}
		 
		//---------------ҳ��js�������--------------------------
		
		egu.addTbarText("-");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		if(!this.isShangbzn()){//�ж��Ƿ��ϱ�����,����Ѿ��ϱ�����,����ʾ���水ť
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
		}
		
		/*����:���ܱ�
		 * ����:2009-8-5 10:48:12
		 * �޸�ԭ��:�պĴ�ҳ�����С��ϱ����ܡ���ť���뱨��ģ�����ϱ����ܹ����ظ����ڱ�ģ����ȡ����ֻ����ˢ�ºͱ��湦�ܡ�(�Ź����)
		 */
		
		/*if (jib==1){
			egu.addTbarText("-");
			egu.addToolbarItem("{"+new GridButton("�ϱ�����","function(){document.getElementById('ShangbButton').click();}").getScript()+"}");
		}*/
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			this.setRiqi(null);
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
//	 �õ��Ƿ��������ϵͳ���ò���
	private String getBaohys() {
		String baohys = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql= "select zhi from xitxxb where mingc='�Ƿ��������' and diancxxb_id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				baohys = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return baohys;

	}
	
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	private void getUploadFtp(String tableName){
        if(tableName == ""){
            setMsg("û�����ݿ��ϱ���");
        }else{
            try{
            	getSettings();
                FtpUpload fu = new FtpUpload();
                String ip =ZhongNip;
                String username = ZhongNyh;
                String password = ZhongNmm;
                String filepath = "";
                fu.connectServer(ip, username, password, filepath);
                String filename = "C://Ribsc//" + tableName + ".txt";
                if(fu.upload(filename, tableName + ".txt" + "\n").equals("�ϴ��ɹ�")){
                	this.ShangbznUpdate();//�ϱ������պĴ�״̬����
                	setMsg("�ϱ��ɹ�!");
                }else{
                	setMsg("�ϴ�ʧ��!");
                }
                fu.closeConnect();
            }catch(Exception e){
            	setMsg("�ϴ�ʧ��!");
                e.printStackTrace();
            }
            
        }
    }

    private void ShangbTXTFile(){
    	//�ϴ��ӿ�Ԥ��,��ʱû��������
        String UploadFilename;
        if(!(new File("c://Ribsc")).isDirectory()){
            (new File("c://Ribsc")).mkdir();
        }
        JDBCcon con = new JDBCcon();
        FtpCreatTxt ct = new FtpCreatTxt();
        UploadFilename = "";
        StringBuffer fileline = new StringBuffer();
        String filedata = "";
        String date = "";
        String diancbm = "";
        double hetjh = 0;
        double dangrgm = 0;
        double leijgm = 0;
        double dangr = 0;
        double leij = 0;
        double dangrhy = 0;
        double leijhy = 0;
        double kuc = 0;
        try{
	        String gsbm =ZhongNbm;
	        String FileName = "HC" + gsbm.substring(0, 1) + gsbm.substring(3) + getRiqi().substring(8, 10);
	        ct.CreatTxt("c://Ribsc/" + FileName + ".txt");
                        
           StringBuffer sbsql = new StringBuffer();
           sbsql.append("select  to_char(to_date('" + getRiqi() + "','yyyy-mm-dd'),'yyyymmdd') as riq,dc.bianm as diancbm,0 as hetjh, \n");
           sbsql.append("      sum(nvl(dr.dangrgm,0)) as dangrgm,sum(nvl(lj.dangrgm,0)) as leijgm, \n");
           sbsql.append("      sum(nvl(dr.dangrgm,0)) as dangr,sum(nvl(lj.dangrgm,0)) as leij, \n");
           sbsql.append("      sum(nvl(dr.haoyqkdr,0)) as dangrhy,sum(nvl(lj.haoyqkdr,0)) as leijhy,sum(dr.kuc) as kuc \n");
           sbsql.append("from (select h.diancxxb_id,h.dangrgm as dangrgm,h.haoyqkdr as haoyqkdr,h.kuc  \n");
           sbsql.append("      from shouhcrbb h \n");
           sbsql.append("      where h.riq =to_date('" + getRiqi() + "','yyyy-mm-dd')) dr, \n");
           sbsql.append("     (select h.diancxxb_id,sum(h.dangrgm) as dangrgm,sum(h.haoyqkdr) as haoyqkdr \n");
           sbsql.append("       from shouhcrbb h \n");
           sbsql.append("       where h.riq >= First_day(to_date('" + getRiqi() + "','yyyy-mm-dd')) \n");
           sbsql.append("         and h.riq <= to_date('" + getRiqi() + "','yyyy-mm-dd') \n");
           sbsql.append("         group by h.diancxxb_id) lj,diancxxb dc,(select * from dianckjpxb kj where kouj='�պĴ��ձ�' and shujsbzt=1) kj \n");
           sbsql.append("  where dc.id=kj.diancxxb_id \n");
           sbsql.append("        and dc.id = lj.diancxxb_id(+) \n");
           sbsql.append("        and dc.id = dr.diancxxb_id(+) \n");
           sbsql.append("        group by (dc.bianm) \n");
           sbsql.append("        order by grouping(dc.bianm) desc \n");
           
            ResultSet rsdata = con.getResultSet(sbsql.toString());
            
            while(rsdata.next()){
                filedata = "";
                fileline.setLength(0);
                date = rsdata.getString("riq");
                diancbm = rsdata.getString("diancbm");
                hetjh = rsdata.getDouble("hetjh");
                dangrgm = rsdata.getDouble("dangrgm");
                leijgm = rsdata.getDouble("leijgm");
                dangr = rsdata.getDouble("dangr");
                leij = rsdata.getDouble("leij");
                dangrhy = rsdata.getDouble("dangrhy");
                leijhy = rsdata.getDouble("leijhy");
                kuc = rsdata.getDouble("kuc");
                fileline.append(getStr(8, date));
                fileline.append(getStr(6, diancbm));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(hetjh))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrgm))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leijgm))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangr))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leij))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(dangrhy))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(leijhy))));
                fileline.append(getNum(8, 0, String.valueOf(Math.round(kuc))));
                filedata = fileline.toString();
				ct.aLine(filedata);//д��������
            }
            rsdata.close();
            UploadFilename = FileName;
            ct.finish();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            con.Close();
        }
        getUploadFtp(UploadFilename);
    }
	
    private String getStr(int weis,String str){
		StringBuffer Str_zf = new StringBuffer();
		if(str==null || str.equals("")){
			for (int i=0;i<weis;i++){
				Str_zf.append(" ");
			}
		}else{
			char[] Str=str.toCharArray();
			int Str_lenght=Str.length;
			
			for (int j=0;j<Str_lenght;j++){
				String Strs=""+Str[j];
				Str_zf.append(Strs);
			}
			int cha=0;
			if (Str_lenght!=weis){
				cha=weis-Str_lenght;
				for (int i=0;i<cha;i++){
					Str_zf.append(" ");
				}
			}
		}
		return Str_zf.toString();
	}
	
	private String getNum(int weis,int xiaos,String Number){//�õ�λ����������
		StringBuffer Str_zf = new StringBuffer();
		String str="";
		str=Number;
		if(str.equals("") ){
			for (int j=0;j<weis-xiaos-2;j++){
				String Strs="";
				Str_zf.append(Strs);
			}
			Str_zf.append(0.);
			for (int j=0;j<xiaos;j++){
				Str_zf.append(0);
			}
		}else{
			int zhengsw=0; 
			if(xiaos!=0){//��С��λ��
				String[] c=str.split("\\.");
				String strs1=c[0];//����λ
				char[] Str1=strs1.toCharArray();//����λ
				String Strs2=c[1];//С��λ
				char[] Str2=Strs2.toCharArray();//С��λ
				//¼������λ
				zhengsw=weis-xiaos-1;
				if (Str1.length!=zhengsw){
					int cha=zhengsw-Str1.length;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str1.length;j++){
					String Strs=""+Str1[j];
					Str_zf.append(Strs);
				}
				//¼��С��λ
				Str_zf.append(".");
				if(Str2.length!=xiaos){
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
					for (int j=0;j<xiaos-Str2.length;j++){
						Str_zf.append(0);
					}
				}else{
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
				}
			}else{//����С��λ
				char[] Str=str.toCharArray();
				int Str_lenght=Str.length;
				int cha=0;
				if (Str_lenght!=weis){//���ո�
					cha=weis-Str_lenght;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str_lenght;j++){//¼������
					String Strs=""+Str[j];
					Str_zf.append(Strs);
				}
			}
		}
		return Str_zf.toString();
	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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

	//���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			
			con.Close();
		}

		return jib;
	}
	public boolean isZuorkc(){//�ж����տ���Ƿ�������
		boolean isZuorkc=false;
		int treejib = this.getDiancTreeJib();
		
		if (treejib == 3) {
			JDBCcon con = new JDBCcon();
			String riqTiaoj=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiqi()), -1, DateUtil.AddType_intDay));
			
			String sqlJib = "select s.kuc from shouhcrbb s where s.riq=to_date('"+riqTiaoj +"','yyyy-mm-dd') and s.diancxxb_id="+this.getTreeid()+"";
			ResultSet rs = con.getResultSet(sqlJib.toString());

			try {
				while (rs.next()) {
					isZuorkc = true;
				}
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				con.Close();
			}

		}else{
			isZuorkc=true;
		}
		
		return isZuorkc;
	}
	
//	�ж��Ƿ��Ѿ��ϱ�����,���״̬Ϊtrue,���水ť����,���ݲ������޸�,���Ϊfalse,�����޸�
	public boolean isShangbzn(){
		boolean isShangbzn=false;
		int treejib=this.getDiancTreeJib();
		if(treejib==3){
			JDBCcon con = new JDBCcon();
			Visit visit = (Visit) getPage().getVisit();
			int zhuangt=0;
			String sql="select s.zhuangt from shouhcrbb s where s.riq=to_date('"+getRiqi()+"','yyyy-mm-dd') and s.diancxxb_id="+this.getTreeid()+"";
			ResultSetList rs =con.getResultSetList(sql);
			try {
				if (rs.next()) {
					zhuangt=rs.getInt(zhuangt);
					if(zhuangt==1){
						isShangbzn=true;
					}else{
						isShangbzn=false;
					}
				}else{
					isShangbzn=false;
				}
				rs.close();
			} finally {
				con.Close();
			}
		}
		
		
		return isShangbzn;
	}
	
	public void ShangbznUpdate(){//�ϱ������Ժ�ı�shouhcrbb ��״̬Ϊ1,������糧�޸�
		  JDBCcon con = new JDBCcon();
		  String sql="update shouhcrbb s set s.zhuangt=1\n" +
			  "where s.riq=to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
			  "and s.diancxxb_id in (select  kj.diancxxb_id from dianckjpxb kj where kj.kouj='�պĴ��ձ�' and kj.shujsbzt=1)";
		
		  int isUpdateTrue = con.getUpdate(sql);
		  if(isUpdateTrue==0){
			  System.out.println("�ϴ������ձ������պĴ��ձ����״̬û�гɹ�!");
		  }
		  con.Close();
	}
	
}

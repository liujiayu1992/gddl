	package com.zhiren.dtrlgs.fkgl;
	
	 
	import java.sql.ResultSet;
	import java.util.ArrayList;
	import java.util.List;
	import org.apache.tapestry.IMarkupWriter;
	import org.apache.tapestry.IRequestCycle;
	import org.apache.tapestry.form.IPropertySelectionModel;
	import org.apache.tapestry.html.BasePage;
	import com.zhiren.common.IDropDownBean;
	import com.zhiren.common.IDropDownModel;
	import com.zhiren.common.JDBCcon;
//	import com.zhiren.liuc.Liuc;
	import  com.zhiren.dtrlgs.pubclass.BalanceLiuc;
	import com.zhiren.common.ResultSetList;
	import com.zhiren.common.ext.ExtGridUtil;
	import com.zhiren.common.ext.ExtTreeUtil;
	import com.zhiren.common.ext.GridButton;
	import com.zhiren.common.ext.GridColumn;
	import com.zhiren.common.ext.form.ComboBox;
//	import com.zhiren.liuc.Yijbean;
import com.zhiren.main.Visit;

	public class Yufktzdsh extends BasePage {
		private String msg = "";

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		// 页面变化记录
		private String Change;

		public String getChange() {
			return Change;
		}

		public void setChange(String change) {
			Change = change;
		}

		private void Save() {
			Visit visit = (Visit) this.getPage().getVisit();
			visit.getExtGrid1().Save(getChange(), visit);
		}

		private boolean _SbChick = false;

		public void SbButton(IRequestCycle cycle) {
			_SbChick = true;
		}
		
		private boolean _RbChick = false;

		public void RbButton(IRequestCycle cycle) {
			_RbChick = true;
		}

		private boolean _DeleteChick = false;

		public void DeleteButton(IRequestCycle cycle) {
			_DeleteChick = true;
		}
		
		public void submit(IRequestCycle cycle) {
			if (_SbChick) {
				_SbChick = false;
				Tij();
				getSelectData();
			}
			if (_RbChick) {
				_RbChick = false;
				Huit();
				getSelectData();
			}
			if(_DeleteChick){
				_DeleteChick = false;
				getDelete();
			}
		}
		
		private void Huit() {
			// TODO 自动生成方法存根
			long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
			
			if(!(this.getChange().equals("")||this.getChange()==null)){
				
				String change[]=this.getChange().split(";");
				
				for(int i=0;i<change.length;i++){
					
					if(change[i] == null || "".equals(change[i])) {
						
						continue;
					}
					String record[] = change[i].split(",");
					
					if(record[6].equals("开始")){
						
						getDelete();
						return;
					}else{
						BalanceLiuc.huit("fuktzb", Long.parseLong(record[0]), renyxxb_id, "");	
					}
				}
				
			}
		}
		
		public void Tij(){
			
			long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
			
			if(!(this.getChange().equals("")||this.getChange()==null)){
				
				String change[]=this.getChange().split(";");
				
				for(int i=0;i<change.length;i++){
					
					if(change[i] == null || "".equals(change[i])) {
						
						continue;
					}
					String record[] = change[i].split(",");		
					try {
						if(BalanceLiuc.isLastLeader(renyxxb_id, "预付款审核")){
							BalanceLiuc.tij("fuktzb", Long.parseLong(record[0]), renyxxb_id,true, "");
						}else{
						BalanceLiuc.tij("fuktzb", Long.parseLong(record[0]), renyxxb_id,false, "");
						}
					} catch (Exception e) {
						// TODO 自动生成 catch 块
						this.setMsg(e.getMessage());
					}
				}
			}
		}

		public void getDelete(){
			JDBCcon con = new JDBCcon();
			ResultSet rs;
			con.setAutoCommit(false);
			int result = -1;
			int fukd = 0;
			String sql = "";
			String strfktzbids = "";
			String strfktzbidsyfk = "";
			String strjsbids = "";
			StringBuffer delfkd = new StringBuffer();
			StringBuffer upjsb = new StringBuffer();
			StringBuffer upyfk = new StringBuffer();
			String dellsb = "";
			try{
				//删除付款通知单SQL
				
				if(!(this.getChange().equals("")||this.getChange()==null)){
					delfkd.append("begin \n");
					String change[]=this.getChange().split(";");
					for(int i=0;i<change.length;i++){
						if(change[i] == null || "".equals(change[i])) {
							continue;
						}
						String record[] = change[i].split(",");
						if(record[2].equals("实际付款")){
							if(strfktzbids.equals("")){
								strfktzbids = Long.parseLong(record[0])+"";
							}else{
								strfktzbids = strfktzbids +","+ Long.parseLong(record[0]);
							}
						}else{
							if(strfktzbidsyfk.equals("")){
								strfktzbidsyfk = Long.parseLong(record[0])+"";
							}else{
								strfktzbidsyfk = strfktzbidsyfk +","+ Long.parseLong(record[0]);
							}
						}
						delfkd.append("delete from fuktzb where id="+Long.parseLong(record[0])+"; \n");
						fukd++;
					}
					delfkd.append(" end; ");
				}
				
				
				if(strfktzbids.length()>0){//更新kuangfjsmkb和kuangfjsyf表中的fuktzb_id字段SQL
					
					sql = "select * from (select * from (select yf.id,yf.fuktzb_id from kuangfjsyf yf,fuktzb fk where yf.fuktzb_id=fk.id and fk.id in ("+strfktzbids+")"
						+ " union " 
						+ "select mk.id,mk.fuktzb_id from kuangfjsmkb mk,fuktzb fk where mk.fuktzb_id=fk.id and fk.id in ("+strfktzbids+")))";
					ResultSet res = con.getResultSet(sql);
					while(res.next()){
						if(strjsbids.equals("")){
							strjsbids = res.getLong("id")+"";
						}else{
							strjsbids = strjsbids+","+res.getLong("id");
						}
					}
					upjsb.append("begin \n");
					upjsb.append("update kuangfjsmkb mk set mk.fuktzb_id=0 where mk.fuktzb_id in ("+strfktzbids+"); \n");
					upjsb.append("update kuangfjsyf yf set yf.fuktzb_id=0 where yf.fuktzb_id in ("+strfktzbids+"); \n");
					upjsb.append(" end; \n");
				}
				
				if(strjsbids.length()>0){//更新预付款表中的余额SQL
					int i = 0;
					sql = "select ye.id,round(sy.jine+ye.yue,2) as yue from "
						+ "(select ls.yufkb_id as id,sum(ls.jine) as jine from yufklsb ls where ls.jiesb_id in ("+strjsbids+") group by ls.yufkb_id ) sy, "
						+ "(select fk.id,fk.yue from yufkb fk where fk.id in (select yufkb_id from yufklsb ls where ls.jiesb_id in ("+strjsbids+") )) ye  "
						+ "where sy.id=ye.id ";
					
					rs = con.getResultSet(sql);
					upyfk.append("begin \n");
					while(rs.next()){
						i++;
						upyfk.append("update yufkb yf set yf.yue="+rs.getDouble("yue")+" where yf.id="+rs.getLong("id")+"; \n ");
					}
					upyfk.append(" end; \n");
					if(i>0){//第一步：执行更新预付款余额
						result = con.getUpdate(upyfk.toString());
						if(result<0){
							con.rollBack();
							System.out.println("删除失败");
							return;
						}
					}
				}
				if(strfktzbidsyfk.length()>0){//更新预付款表中的fuktzb_id字段
					String updt = "update yufkb set fuktzb_id=0 where fuktzb_id in ("+strfktzbidsyfk+")";
					int rlt = con.getUpdate(updt);
					if(rlt<0){
						con.rollBack();
						System.out.println("删除失败");
						return;
					}
				}
				if(strjsbids.length()>0){//删除yufklsb数据SQL
					
					dellsb = "delete from yufklsb where jiesb_id in ("+strjsbids+")";
					int rlt = con.getDelete(dellsb);
					if(rlt<0){
						con.rollBack();
						System.out.println("删除失败");
						return;
					}
				}
					
				//第二步：执行更新kuangfjsmkb和kuangfjsyf表中的fuktzb_id
				if(upjsb.length()>0){
					result = con.getUpdate(upjsb.toString());
					if(result<0){
						con.rollBack();
						System.out.println("删除失败");
						return;
					}
				}
				
				if(fukd>0){//第三步：执行删除付款通知单
					result = con.getDelete(delfkd.toString());
					if(result<0){
						con.rollBack();
						System.out.println("删除失败");
						return;
					}
				}
					
				System.out.println("删除成功");
				con.commit();
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				con.Close();
			}
		}
		
		public void getSelectData() {

			String sql = "";
			JDBCcon con = new JDBCcon();
			String leib = "预付款";
			long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
			long liucztb_id=-1;
			String ids="";
			
//			/*if (getWeizSelectValue().getId() ==1) {// 自己的任务
//				String strWodrwIds = BalanceLiuc.getWodrws("fuktzb", renyxxb_id, leib);
//				sql="select * from ("
//					+ " select fk.id,fk.fukdbh,decode(dc.mingc,null,'预付款',dc.mingc) as dianmc,fk.fukdlx,fk.riq,gy.quanc,gy.bianm,to_char(fk.fapje,'FM9,999,999,990.09') as fapje,to_char(fk.shijfk,'FM9,999,999,990.09') as shijfk,to_char(fk.hexyfk,'FM9,999,999,990.09') as hexyfk,to_char(fk.kouyf,'FM9,999,999,990.09') as kouyf,to_char(fk.kouhkf,'FM9,999,999,990.09') as kouhkf, "
//					+"			decode(1,1,'付款单查看','') as chakfkd,decode(1,1,'结算单查看','') as chakjsd,lz.mingc zhuangt,zt.liucb_id,zt.id as liucztb_id "
//					+"  from fuktzb fk,liucztb zt,gongysb gy,leibztb lz,liuclbb ll, "
//					
//					+ "		(select distinct js.fuktzb_id,dc.mingc from kuangfjsmkb js,diancxxb dc where dc.id=js.diancxxb_id \n"
//					+ "         and js.fuktzb_id in ("+strWodrwIds+") \n"
//					+ "      /* union \n"
//					+ "      select distinct yf.fuktzb_id,dc.mingc from kuangfjsyf yf,diancxxb dc where dc.id=yf.diancxxb_id \n"
//					+ "         and yf.fuktzb_id in ("+strWodrwIds+")*/) dc \n"
//					
//					+" where fk.liucztb_id=zt.id and fk.gongysb_id=gy.id " 
//					+"   and zt.leibztb_id=lz.id and lz.liuclbb_id=ll.id "
//					+"   and fk.id=dc.fuktzb_id(+) and fk.id in (" +strWodrwIds+ ")order by fukdbh ) ";
//			}else {
//				String strLiuczIds = BalanceLiuc.getLiuczs("fuktzb", renyxxb_id, leib);
//				sql="select * from (("
//					+ " select fk.id,fk.fukdbh,decode(dc.mingc,null,'预付款',dc.mingc) as dianmc,fk.fukdlx,fk.riq,gy.quanc,gy.bianm,to_char(fk.fapje,'FM9,999,999,990.09') as fapje,to_char(fk.shijfk,'FM9,999,999,990.09') as shijfk,to_char(fk.hexyfk,'FM9,999,999,990.09') as hexyfk,to_char(fk.kouyf,'FM9,999,999,990.09') as kouyf,to_char(fk.kouhkf,'FM9,999,999,990.09') as kouhkf, "
//					+"			decode(1,1,'查看','') as chakfkd,decode(1,1,'查看','') as chakjsd,lz.mingc zhuangt,zt.liucb_id,zt.id as liucztb_id "
//					+"  from fuktzb fk,liucztb zt,gongysb gy,leibztb lz,liuclbb ll, "
//
//					+ "		(select distinct js.fuktzb_id,dc.mingc from kuangfjsmkb js,diancxxb dc where dc.id=js.diancxxb_id \n"
//					+ "         and js.fuktzb_id in ("+strLiuczIds+") \n"
//					+ "     /*  union \n"
//					+ "      select distinct yf.fuktzb_id,dc.mingc from kuangfjsyf yf,diancxxb dc where dc.id=yf.diancxxb_id \n"
//					+ "         and yf.fuktzb_id in ("+strLiuczIds+")*/) dc \n"
//					
//					+" where fk.liucztb_id=zt.id and fk.gongysb_id=gy.id " 
//					+"   and zt.leibztb_id=lz.id and lz.liuclbb_id=ll.id "
//					+"   and fk.id=dc.fuktzb_id(+) and fk.id in (" +strLiuczIds+ ")) order by fukdbh )  ";
//			}*/
			if (getWeizSelectValue().getId() ==1) {// 自己的任务
				try {
					liucztb_id=BalanceLiuc.getcaozsjzt(renyxxb_id, "预付款审核");
				} catch (Exception e) {
					this.setMsg(e.getMessage());
					e.printStackTrace();
				}
				sql="   select * from ( \n"+
				   "select fk.id,fk.fukdbh,decode(fk.tianzdw,null,'大唐燃料公司',fk.tianzdw) as dianmc,fk.fukdlx,fk.riq,gy.quanc,gy.bianm,\n"+
				   "to_char(fk.fapje,'FM9,999,999,990.09') as fapje,to_char(fk.shijfk,'FM9,999,999,990.09') as shijfk,\n"+
				   "to_char(fk.hexyfk,'FM9,999,999,990.09') as hexyfk,to_char(fk.kouyf,'FM9,999,999,990.09') as kouyf,to_char(fk.kouhkf,'FM9,999,999,990.09')\n"+
				   " as kouhkf, 			decode(1,1,'付款单查看','') as chakfkd/*,decode(1,1,'结算单查看','') as chakjsd*/,lz.mingc zhuangt,\n"+
				   " zt.liucb_id,zt.id as liucztb_id   from fuktzb fk,liucztb zt,gongysb gy,leibztb lz,liuclbb ll\n"+
				 "where fk.liucztb_id=zt.id and fk.gongysb_id=gy.id    and zt.leibztb_id=lz.id and lz.liuclbb_id=ll.id and fk.fukdlx='预付款'    and fk.liucztb_id in ("+liucztb_id+")order by fukdbh )\n"; 

				}else{
					ids=BalanceLiuc.getLiuczs("fuktzb", renyxxb_id, leib);
			 	sql="   select * from ( \n"+
				   "select fk.id,fk.fukdbh,decode(fk.tianzdw,null,'大唐燃料公司',fk.tianzdw) as dianmc,fk.fukdlx,fk.riq,gy.quanc,gy.bianm,\n"+
				   "to_char(fk.fapje,'FM9,999,999,990.09') as fapje,to_char(fk.shijfk,'FM9,999,999,990.09') as shijfk,\n"+
				   "to_char(fk.hexyfk,'FM9,999,999,990.09') as hexyfk,to_char(fk.kouyf,'FM9,999,999,990.09') as kouyf,to_char(fk.kouhkf,'FM9,999,999,990.09')\n"+
				   " as kouhkf, 			decode(1,1,'付款单查看','') as chakfkd/*,decode(1,1,'结算单查看','') as chakjsd*/,lz.mingc zhuangt,\n"+
				   " zt.liucb_id,zt.id as liucztb_id   from fuktzb fk,liucztb zt,gongysb gy,leibztb lz,liuclbb ll\n"+
				 "where fk.liucztb_id=zt.id and fk.gongysb_id=gy.id    and zt.leibztb_id=lz.id and lz.liuclbb_id=ll.id and fk.fukdlx='预付款'    and fk.id in ("+ids+")order by fukdbh )\n"; 

				}
            
            
			ResultSetList rsl=con.getResultSetList(sql);
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);//设定记录集对应的表单
//			egu.setTableName("yunslb");//要保存的表名
			egu.getColumn("id").setHidden(true);
			egu.getColumn("fukdbh").setHeader("付款编码");
			egu.getColumn("dianmc").setHeader("填制单位名称");
			egu.getColumn("fukdlx").setHeader("类型");
			egu.getColumn("fukdlx").setHidden(true);
			egu.getColumn("riq").setHeader("日期");
			egu.getColumn("quanc").setHeader("收货单位");
			egu.getColumn("bianm").setHeader("编码");
			
			egu.getColumn("fapje").setHeader("发票金额");
			egu.getColumn("shijfk").setHeader("实际付款");
			egu.getColumn("hexyfk").setHeader("核销预付款");
			egu.getColumn("kouyf").setHeader("扣运费");
			egu.getColumn("kouhkf").setHeader("扣回空费");
			
			egu.getColumn("chakfkd").setHeader("付款单");
			String str1="  var url = 'http://'+document.location.host+document.location.pathname;" +
			"var end = url.indexOf(';');" +
			"url = url.substring(0,end);" +
			"url = url + '?service=page/' + 'Fuktzdcx_dtrl&fukdbh='+record.data['FUKDBH'];";

			egu.getColumn("chakfkd").setRenderer(
			"function(value,p,record){" +str1+
			"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>查看</a>\"}");
			
		/*	egu.getColumn("chakjsd").setHeader("结算单");
			String str2="  var url = 'http://'+document.location.host+document.location.pathname;" +
			"var end = url.indexOf(';');" +
			"url = url.substring(0,end);" +
			"url = url + '?service=page/' + 'Showfkjsd&jiesdbh='+record.data['FUKDBH'];";

			egu.getColumn("chakjsd").setRenderer(
			"function(value,p,record){" +str2+
			"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>查看</a>\"}");*/
			
			egu.getColumn("zhuangt").setHeader("状态");
			egu.getColumn("zhuangt").setHidden(true);
			egu.getColumn("liucztb_id").setHidden(true);
			egu.getColumn("liucb_id").setHidden(true);
			
			egu.getColumn("id").setHidden(true);
			egu.getColumn("bianm").setHidden(true);
			egu.getColumn("liucztb_id").setHidden(true);
			egu.getColumn("liucb_id").setHidden(true);
//			egu.getColumn("jiesdbm").setHidden(true);
			
			egu.getColumn("fukdbh").setWidth(70);
			egu.getColumn("dianmc").setWidth(60);
			egu.getColumn("riq").setWidth(65);
			egu.getColumn("quanc").setWidth(180);
			egu.getColumn("bianm").setWidth(65);
			egu.getColumn("fapje").setWidth(85);
			egu.getColumn("shijfk").setWidth(85);
			egu.getColumn("hexyfk").setWidth(85);
			egu.getColumn("kouyf").setWidth(65);
			egu.getColumn("kouhkf").setWidth(65);
			egu.getColumn("chakfkd").setWidth(45);
//			egu.getColumn("chakjsd").setWidth(45);
			egu.getColumn("zhuangt").setWidth(65);
			
			egu.setWidth(990);
//			List tmp= new ArrayList();

//			for(int i=0;i<rsl.getRows();i++){
//				String strtmp="";
//				tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,0)));
//				for(int j=0;j<tmp.size();j++){
//					strtmp+=((Yijbean)tmp.get(j)).getXitts()+"\\n  "+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij())+"\\n ";
//				}
//				egu.setDataValue(i, 13, "付款编号 "+egu.getDataValue(i,1)+":\\n "+strtmp);
//			}
		
			egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
			egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
			egu.addPaging(25);
			egu.addTbarText("流程状态");
			ComboBox WeizSelect = new ComboBox();
			WeizSelect.setId("Weizx");
			WeizSelect.setWidth(80);
			WeizSelect.setLazyRender(true);
			WeizSelect.setTransform("WeizSelectx");
			egu.addToolbarItem(WeizSelect.getScript());
			if(((Visit) getPage().getVisit()).getboolean4()){
				
				egu.addToolbarItem("{"
							+ new GridButton(
									"回退",
									"function(){ if(gridDiv_sm.hasSelection()){ "
										+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
										+ " 	for(var i=0;i<rec.length;i++){ "
										+ " 		if(i==0){"
										+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('FUKDLX')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
										+ " 		}else{ "
										+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('FUKDLX')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
										+ " document.getElementById('RbButton').click();} else{	alert('请选择一张付款单!');}}").getScript()+"}");
				
				egu.addTbarText("-");
				egu.addToolbarItem("{"
							+ new GridButton(
									"审核",
									"function(){ if(gridDiv_sm.hasSelection()){"
										+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
										+ " 	for(var i=0;i<rec.length;i++){ "
										+ " 		if(i==0){"
										+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('FUKDLX')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
										+ " 		}else{ "
										+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('FUKDLX')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('ZHUANGT')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
										+ " document.getElementById('SbButton').click();}else{	alert('请选择一张付款单!');}}").getScript()+"}");
				
			}
			
/*
			if(((Visit) getPage().getVisit()).getboolean4()){
				
				egu.addToolbarItem("{"
							+ new GridButton(
									"审核",
									"function(){ if(gridDiv_sm.hasSelection()){"
										+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
										+ " 	for(var i=0;i<rec.length;i++){ "
										+ " 		if(i==0){"
										+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('FAPJE')+','+rec[i].get('SHIJFK')+','+rec[i].get('HEXYFK')+','+rec[i].get('KOUYF')+','+rec[i].get('KOUHKF')+','+rec[i].get('ZHUANGT')+','+rec[i].get('CHAK')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
										+ " 		}else{ "
										+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('FAPJE')+','+rec[i].get('SHIJFK')+','+rec[i].get('HEXYFK')+','+rec[i].get('KOUYF')+','+rec[i].get('KOUHKF')+','+rec[i].get('ZHUANGT')+','+rec[i].get('CHAK')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
										+ " document.getElementById('SbButton').click();}else{	alert('请选择一张付款单!');}}").getScript()+"}");
				egu.addTbarText("-");
				egu.addToolbarItem("{"
							+ new GridButton(
									"回退",
									"function(){ if(gridDiv_sm.hasSelection()){ "
										+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
										+ " 	for(var i=0;i<rec.length;i++){ "
										+ " 		if(i==0){"
										+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('FAPJE')+','+rec[i].get('SHIJFK')+','+rec[i].get('HEXYFK')+','+rec[i].get('KOUYF')+','+rec[i].get('KOUHKF')+','+rec[i].get('ZHUANGT')+','+rec[i].get('CHAK')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';"
										+ " 		}else{ "
										+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('FUKDBH')+','+rec[i].get('RIQ')+','+rec[i].get('QUANC')+','+rec[i].get('BIANM')+','+rec[i].get('FAPJE')+','+rec[i].get('SHIJFK')+','+rec[i].get('HEXYFK')+','+rec[i].get('KOUYF')+','+rec[i].get('KOUHKF')+','+rec[i].get('ZHUANGT')+','+rec[i].get('CHAK')+','+rec[i].get('LIUCB_ID')+','+rec[i].get('LIUCZTB_ID')+';';}}"
										+ " document.getElementById('RbButton').click();} else{	alert('请选择一张付款单!');}}").getScript()+"}");
				
			}
*/			
//			egu.addTbarText("-");
//			egu.addToolbarItem("{"
//						+ new GridButton("审核意见",
//								"function(){ "
//										+ "if(gridDiv_sm.hasSelection()){ "
//										+ " if(Weizx.getRawValue()=='流程中'){ "
//										+ " document.getElementById('DivMy_opinion').className = 'x-hidden';}"
//										+ "	window_panel.show(); "
//										+ "  rec = gridDiv_grid.getSelectionModel().getSelections(); "
//										+ " document.getElementById('My_opinion').value='';"
//										+ " document.getElementById('Histry_opinion').value='';"
//										+ " var strmyp=''; "
//										+ " for(var i=0;i<rec.length;i++){ "
//										+ " if(strmyp.substring(rec[i].get('YIJ'))>-1){ "
//										+ " if(strmyp==''){ strmyp=rec[i].get('YIJ');}else{ strmyp+=','+rec[i].get('YIJ');}}"
//										+ " var strtmp=rec[i].get('HISTRYYJ');"
//										+ " document.getElementById('Histry_opinion').value+=strtmp+'\\n';} document.getElementById('My_opinion').value=strmyp;"
//										+ " }else{ "
//										+ " 	alert('请选择一张付款单!');} " + "}")
//								.getScript() + "}");
			egu.addTbarText("-");
//			//往Toolbar绑定combobox
//			egu.addTbarText("审批类型:");
//			ComboBox JieslxDropDown = new ComboBox();
//			JieslxDropDown.setId("JieslxDrop");
//			JieslxDropDown.setWidth(100);
//			JieslxDropDown.setLazyRender(true);
//			JieslxDropDown.setTransform("JieslxDropDown");
//			egu.addToolbarItem(JieslxDropDown.getScript());
//			//树		
//			egu.addTbarText("-");
//			if(getJieslxValue().getId()!=3){
//			egu.addTbarText("单位名称:");
//			ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)getPage().getVisit()).getDiancxxbId(),getTreeid());
//			
//			setTree(etu);
//			egu.addTbarTreeBtn("diancTree");
//			}
//			
//			egu.addTbarText("-");
			
			egu.addOtherScript("Weizx.on('select',function(){ document.forms[0].submit();});");
//			egu.addOtherScript("JieslxDrop.on('select',function(){ document.forms[0].submit();});Weizx.on('select',function(){ document.forms[0].submit();});");
			egu.addOtherScript("gridDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
			//绑定combobox完成
			
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
		
		// 位置
		public IDropDownBean getWeizSelectValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean2((IDropDownBean) getWeizSelectModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean2();
		}

		public void setWeizSelectValue(IDropDownBean Value) {
			
			if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
				
				((Visit) getPage().getVisit()).setboolean1(true);
				((Visit) getPage().getVisit()).setDropDownBean2(Value);
			}
		}

		public void setWeizSelectModel(IPropertySelectionModel value) {
			((Visit) getPage().getVisit()).setProSelectionModel2(value);
		}

		public IPropertySelectionModel getWeizSelectModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
				getWeizSelectModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel2();
		}

		public void getWeizSelectModels() {
			List list = new ArrayList();
			list.add(new IDropDownBean(1, "我的任务"));
			list.add(new IDropDownBean(2, "流程中"));
			((Visit) getPage().getVisit())
					.setProSelectionModel2(new IDropDownModel(list));
		}
		
		//付款类型
		/*public IDropDownBean getJieslxValue() {

			if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean3((IDropDownBean) getJieslxModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean3();
		}

		public void setJieslxValue(IDropDownBean Value) {
			
			if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
				
				((Visit) getPage().getVisit()).setboolean2(true);
				((Visit) getPage().getVisit()).setDropDownBean3(Value);
			}
		}

		public IPropertySelectionModel getJieslxModel() {

			if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

				getJieslxModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel3();
		}

		public void setJieslxModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel3(value);
		}

		public void getJieslxModels() {

			List list = new ArrayList();
			list.add(new IDropDownBean(0, "全部"));
			list.add(new IDropDownBean(1, "厂方付款单"));
			list.add(new IDropDownBean(2, "矿方付款单"));
			list.add(new IDropDownBean(3, "付款指标审批"));
			((Visit) getPage().getVisit())
					.setProSelectionModel3(new IDropDownModel(list));
		}*/
		
		public boolean isQuanxkz() {
			return ((Visit) getPage().getVisit()).getboolean4();
		}

		public void setQuanxkz(boolean value) {
			((Visit) getPage().getVisit()).setboolean4(value);
		}

		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
			Visit visit = (Visit) getPage().getVisit();
			if (!visit.getActivePageName().toString().equals(
					this.getPageName().toString())) {
				// 在此添加，在页面第一次加载时需要置为空的变量或方法
				visit.setActivePageName(getPageName().toString());
				visit.setList1(null);
				visit.setInt1(-1);

				//3
//				setJieslxModel(null);
//				setJieslxValue(null);
//				getJieslxModels();
				//2
				setWeizSelectValue(null);
				setWeizSelectModel(null);
				getWeizSelectModel();
				visit.setboolean4(true);//我的任务、流程中
				visit.setboolean1(false);//位置
				visit.setboolean2(false);//付款类型
				visit.setboolean3(false);//单位
				getSelectData();
			}

			
			if (((Visit) getPage().getVisit()).getboolean1()
					|| ((Visit) getPage().getVisit()).getboolean2()
					|| ((Visit) getPage().getVisit()).getboolean3()) {// 如果合同位置改变
				// 1, 位置2, 付款类型3, 单位
				if (((Visit) getPage().getVisit()).getboolean1() == true) {
					if (getWeizSelectValue().getId() == 1) {
						visit.setboolean4(true);
					} else {
						visit.setboolean4(false);
					}
				}
				visit.setboolean1(false);
				visit.setboolean2(false);
				visit.setboolean3(false);
				getSelectData();
			}
		}
		
		private String treeid="";
		public String getTreeid() {
			
			if(treeid.equals("")){
				
				treeid=String.valueOf(((Visit)getPage().getVisit()).getDiancxxb_id());
			}
			return treeid;
		}
		public void setTreeid(String treeid) {
			
			if(!this.treeid.equals(treeid)){
				
				((Visit)getPage().getVisit()).setboolean3(true);
				this.treeid = treeid;
			}
		}
		
		public int getEditTableRow() {
			return ((Visit) this.getPage().getVisit()).getInt1();
		}
		
		public void setEditTableRow(int value){
			
			((Visit) this.getPage().getVisit()).setInt1(value);
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
		
		public String getWunScript(){
			
			return "for(var i=0;i<rec.length;i++){"
	    				
	                +"    rec[i].set('YIJ',document.getElementById('My_opinion').value);" 	
	    			+" }";
		}
	}

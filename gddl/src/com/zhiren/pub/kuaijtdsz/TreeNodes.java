package com.zhiren.pub.kuaijtdsz;

import java.sql.ResultSet;

import com.zhiren.common.JDBCcon;

public class TreeNodes {
	//��������ı�����id�š���ʾ�ֶΡ�������ʾ���ͣ�������Ĭ�ϵ�id��fuid���������ڵ�
	//�������Ͱ�����0,id���������Ӵ�1,id�������ϼ�2,0��1�ĺϲ�
	private String tableName;
	private String displayField;
	private long id;
	private int leix;
	private boolean isCheck;
	private String renyid;
	private StringBuffer bufferResult=null;
	private String sql0="";
	private String sql1="";
	private String sql2="";
	public TreeNodes(String tableName,long id, String displayField, int leix,boolean isCheck) {
		super();
		this.tableName = tableName;
		this.displayField = displayField;
		this.id = id;
		this.leix = leix;
		this.isCheck=isCheck;
		bufferResult=new StringBuffer();
		sql0="select "+tableName+".id, "+displayField+",level,CONNECT_BY_ISLEAF from "+tableName+"\n" +
		"start with id=" +id+ 
		"\n" + 
		"connect by fuid=prior id\n" + 
		"order SIBLINGS by  xuh";
		sql1=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			" )\n" + 
			" start with fuid=-1\n" + 
			" connect by fuid=prior id";

		sql2=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			"union\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with fuid=" +id+  
			" connect by fuid=prior id\n" + 
			" )\n" + 
			"  start with fuid=-1\n" + 
			" connect by fuid=prior id";

	}
	
	public TreeNodes(String tableName,long id, String displayField, int leix,boolean isCheck,String renyid) {
		super();
		this.tableName = tableName;
		this.displayField = displayField;
		this.id = id;
		this.leix = leix;
		this.renyid = renyid;
		this.isCheck=isCheck;
		bufferResult=new StringBuffer();
		sql0="select "+tableName+".id, "+displayField+",level,CONNECT_BY_ISLEAF from "+tableName+"\n" +
		"start with id=" +id+ 
		"\n" + 
		"connect by fuid=prior id\n" + 
		"order SIBLINGS by  xuh";
		sql1=

			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			" select id,"+displayField+",fuid\n" + 
			" from "+tableName+"\n" + 
			" start with id=" +id+  
			" connect by prior fuid= id\n" + 
			" )\n" + 
			" start with fuid=-1\n" + 
			" connect by fuid=prior id";

		sql2=
			"select id,"+displayField+",level,CONNECT_BY_ISLEAF\n" +
			"from(\n" + 
			"select zy.id,mingc,fuid\n" + 
			"from "+tableName+" zy\n" + 
			"where zy.leib = (select nvl(max(zy.leib),1) leib from zuxxb z,ziyxxb zy,zuqxb zu where zu.zuxxb_id = z.id and z.mingc =\n" + 
			"(select getrenyfz(id) zu from renyxxb where id = "+renyid+"))\n" + 
			"start with id =0 connect by fuid= id\n" + 
			"union\n" + 
			"select zy.id,"+displayField+",fuid\n" + 
			"from "+tableName+" zy\n" + 
			"where zy.leib = (select nvl(max(zy.leib),1) leib from zuxxb z,ziyxxb zy,zuqxb zu where zu.zuxxb_id = z.id and z.mingc =\n" + 
			"(select getrenyfz(id) zu from renyxxb where id = "+renyid+"))\n" + 
			"start with fuid =0 connect by fuid= prior id\n" + 
			" )\n" + 
			"  start with fuid=0\n" + 
			" connect by fuid=prior id";



	}
	/*
	 *   var node0 = new Ext.tree.TreeNode({id:'0',text:'��ԴȨ��'});
		var node4 = new Ext.tree.TreeNode({id:'4',text:'��ͬ����(�糧)',checked:true});
		node0.appendChild([node4]);
	 */
	public String getScript(){//�ж������¼Ҫô���ϸ���¼���ֵ�Ҫô������ҪôΪ��������֮����һ���Ҹ��׵Ĺ���
		int p=-1;//�ϸ���¼�ĸ��ױ�ʶ
		int shangj=-1;//�ϸ���¼�ļ���
		JDBCcon con=new JDBCcon();
		bufferResult.setLength(0);
		ResultSet rs=null;
		int[] zhangbl=new int[10]; //��ǰ�ڵ�ĳ�����,��Ȳ�����10
		int j=0;//����������
		int i=0;//�ڵ��ʶ
		try{
			switch(leix){
			case 2:
				rs=con.getResultSet(sql2);
				break;
			case 1:
				rs=con.getResultSet(sql1);
				break;
			default :
				rs=con.getResultSet(sql0);
			}
				while(rs.next()){//չ��
					if(i!=0){//������ϸ���¼�����и��׽ڵ�//���Ǹ��ڵ�
						
						if(shangj==rs.getInt("level")){// ����ϸ���¼�ļ��𲻵��������¼�ļ������ֵ�
							bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({");
							bufferResult.append("id:'"+rs.getString("id")+"',");
							bufferResult.append( "text:'"+rs.getString(displayField)+"',");
							if(isCheck){//�����������
								bufferResult.append(  "checked:false," );
							}
							
							bufferResult.append(  "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
							bufferResult.append(  "});\n");//����ڵ��ֵ
							bufferResult.append(" node"+(p)+".appendChild([node"+i+"]);\n");//���븸�׽ڵ�(��һ���ĸ���)
//							zhangbl[j][0]=i;//ƽ���ĳ�������һ��
//							zhangbl[j++][1]=rs.getInt("level");
							
							
						}else if(shangj<rs.getInt("level")){// ��������¼�ļ�������ϼ�����
							bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({" );
							bufferResult.append("id:'"+rs.getString("id")+"'," );
							bufferResult.append( "text:'"+rs.getString(displayField)+"'," );
//							bufferResult.append(  "checked:"+isCheck+"," );
							if(isCheck){//�����������
								bufferResult.append(  "checked:false," );
							}
							bufferResult.append( "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
							bufferResult.append(  "});\n");//����ڵ��ֵ
							bufferResult.append(" node"+(i-1)+".appendChild([node"+i+"]);\n");//���븸�׽ڵ㣨��һ����
							p=i-1;
							zhangbl[(rs.getInt("level")-2)]=i-1;//ƽ���ĳ�������һ��
							
						}else{// ��������¼�ļ���С���ϼ�����
							bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({" );
							bufferResult.append("id:'"+rs.getString("id")+"'," );
							bufferResult.append( "text:'"+rs.getString(displayField)+"'," );
//							bufferResult.append( "checked:"+isCheck+"," );
							if(isCheck){//�����������
								bufferResult.append(  "checked:false," );
							}
							bufferResult.append( "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
							bufferResult.append( "});\n");//����ڵ��ֵ
							//�ϸ��ڵ�ĳ��������ҵ����ף���Ϊ���б��д˽ڵ㸸��
							bufferResult.append(" node"+zhangbl[(rs.getInt("level")-2)]+".appendChild([node"+i+"]);\n");//���븸�׽ڵ㣨���ڵ�0��
							p=zhangbl[(rs.getInt("level")-2)];
						}
					}else{
						bufferResult.append(" var node"+i+" = new Ext.tree.TreeNode({" );
						bufferResult.append("id:'"+rs.getString("id")+"'," );
						bufferResult.append( "text:'"+rs.getString(displayField)+"',");
//						bufferResult.append( "checked:"+isCheck+"," );
						if(isCheck){//�����������
							bufferResult.append(  "checked:false," );
						}
						bufferResult.append(  "leaf :"+(rs.getInt("CONNECT_BY_ISLEAF")==1?"true":"false"));
						bufferResult.append(  "});\n");//����ڵ�iֵ
					}
					shangj=rs.getInt("level");//�ϸ���¼�ļ���
					i++;
				}
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return bufferResult.toString();
	}
	


}

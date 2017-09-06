var CommPort = "1";
var BaudRate = "4800";
var DataBits = "8";
var StopBits = "1";
var ParitySetting = "n";
var Jilhh = "A";
var DataMode = 1;
var isTest = 0;

//var CurrFile ;

function ResetMsCom(){
	if(isTest==0){
//	alert(CommPort + ","+ BaudRate + ","+ DataBits +"," + ParitySetting +"," + StopBits + "," + Jilhh + "," + DataMode);
		MSComm1.CommPort= CommPort;
		MSComm1.Settings=""+ BaudRate + ","+ ParitySetting +"," + DataBits +"," + StopBits + "";
		MSComm1.InputLen=0   ;   
		if(!MSComm1.PortOpen){
			MSComm1.PortOpen=true;
			document.getElementById("DTfont").style.color="yellow";
		}
		setTimeout('getData();',500);
	}
}
function setParam4Set(){
	var filePath = "D:/zhiren/huocfcjse/cans/huocfcj_cans";
	setParam(filePath);
}
function setParam(filePath){
	var fso, fd, fc, f;
	fso = new ActiveXObject("Scripting.FileSystemObject");
	/*if(fso.FolderExists(filePath)){
		fd = fso.GetFolder(filePath);		
		fc = new Enumerator(fd.files);
		for (; !fc.atEnd(); fc.moveNext()){
			if(fso.FileExists(fc.item())){
				if(!isNaN(curfile)){
					if(fc.item() == curfile){
						continue;
					}
				}
				curfile = fc.item();
				f = fso.OpenTextFile(curfile, 1, false);	
			}	   					
		}
	}*/
	if(fso.FileExists(filePath)){
		f = fso.OpenTextFile(filePath, 1, false);
		var line = 0;
		while (!f.AtEndOfStream){
			switch (line++){
				case 0:CommPort = f.ReadLine().split("=")[1];break;
				case 1:BaudRate = f.ReadLine().split("=")[1];break;
				case 2:DataBits = f.ReadLine().split("=")[1];break;
				case 3:StopBits = f.ReadLine().split("=")[1];break;
				case 4:ParitySetting = f.ReadLine().split("=")[1];break;
				case 5:Jilhh = f.ReadLine().split("=")[1];break;
				case 6:DataMode = f.ReadLine().split("=")[1];break;
				case 7:isTest = f.ReadLine().split("=")[1];break;
				default : break;
			}
			if(line > 10)break;
		}
		f.Close();
	}
}

body.onload =function(){
	
	drbl=false;//禁用导入按钮
	
	var filename=document.all.File_name.value;
	var folderpath="D:/zhiren/huocfcjse/temp/huocfcj_temp";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	
	if(filename=='true'){//数据保存成功
		if(fso.FileExists(folderpath)){
			var f=fso.GetFile(folderpath);
			f.Delete();
		}
	}
	
	if(filename=='false'){
		drbl=true;
//		InitGridData();
	}
	
	if(filename=='cancel'){
		
		if(fso.FileExists(folderpath)){
	/*	Ext.Msg.confirm('提示信息','存在文件数据，要导入吗?',function(btn){
						if(btn=='yes'){
							InitGridData();
							return;
						}
						if(btn=='no'){
							return;
						}
					});*/
		drbl=true;
		Ext.Msg.alert('提示信息','存在文件数据，请先导入!');
		}
		
	}
	
	
	
	document.getElementById("dongtzl").value=0.0;
	document.getElementById("DTfont").style.color="red";
	document.getElementById("caijzl").value=0.0;
	setParam4Set();
	ResetMsCom();
}

function getData(){
	if(MSComm1.InBufferCount>7){
		var Instring = MSComm1.Input;
		var str = Instring;
		var Stemp="";
		var temp1="";
		var temp2="";
		var temp4="";
		var blen = -1;
		var zhongl = 0;
		switch(DataMode){
			case "1": str = Instring.substring(1, 7);
				zhongl = parseInt(str, 10);
				break;
			case "70": str = Instring.substring(0, 22);
				for(var i=0;i<=22;i++){
					if(str.substr(i,1)=='+'){
						temp2=str.substring(i+1,i+6);
						break;
					}
				}
				zhongl = parseInt(temp2,10);
				zhongl=eval(zhongl*10);
				break;
			case "10": str = Instring.substring(0, 32);
				for(var i=0;i<=32;i++){
					if(str.substr(i,1)==' '){
						temp2=str.substring(i+1,i+10);
						break;
					}
				}
				zhongl = parseInt(temp2,10);
				zhongl=eval(zhongl/1000);
				break;   		
			case "20": str = Instring.substring(1, 9);
				for(var i = 8;i>= 0;i--){
					Stemp = Stemp + str.substr(i,1);
				}
				zhongl=Stemp*1000;
				break;
			case "30": str = Instring.substring(3, 14);
				zhongl= parseInt(str ,10);
				break;
			case "40": str = Instring.substring(4, 10);
        		zhongl = parseInt(str,10);
        		break;
        	case "50": str = Instring.substring(4, 9);
        		zhongl = parseInt(str,10);
        		break;
			case "60": str = Instring.substring(0, 16);
				for(var i=0;i<=7;i++){
					if(str.substr(i,1)=='='){
						temp1=str.substring(i+1,i+6);
					}
				}
				for(var i =(temp1.length-1);i>= 0;i--){
					if(temp1.substr(i,1)==' '||temp1.substr(i,1)=='='){
						Stemp=Stemp+'0';
					}else 
						Stemp = Stemp + temp1.substr(i,1);
				}
				zhongl=parseInt(Stemp,10);
				zhongl=eval(zhongl);
				break;
//邯郸热电 使用中，勿删
			case "170": 
			str = Instring.substring(0, 32);
			//alert(str);
			for(var i=0;i<=16;i++){
			//alert(str);
			if(str.substr(i,1)==""){
				//alert(str.substr(i,1));
				temp4 = str.substring(i+4,i+10);
				
				//alert(temp4);
				break;
				}
			}		
        		zhongl = parseInt(temp4,10);
        		break;
			case "190": Stemp = Instring.substring(0, 12);
//				alert(Stemp);
				blen = Stemp.indexOf("+");
				if(blen!=-1){
					str = Stemp.substring(blen, blen+7);
					zhongl = parseInt(str, 10);
//					alert(zhongl);
				}
				break;

			case "200" :str = Instring.substring(0, 8);
				if(str.indexOf(".")==0 && str.indexOf("=")==7){
				for(var i = 6;i>= 1;i--){
					Stemp = Stemp + str.substr(i,1);
				}
				zhongl=Stemp;
				}else{
					zhongl = 0;
				}
				break;
			default: break;
		}
		if(isNaN(zhongl)){
			zhongl = 0.0;
		}
		var zl = eval(zhongl/1000);
		document.getElementById("dongtzl").value = zl;
		
        var Lastzl = eval(document.all.item("Lastzl").value);
        if(zl > 0){
        	if(Lastzl == zl){
        		document.getElementById("DTfont").style.color = '#00FF00';
				document.all.item("CaijButton").disabled = false;
        	}else{
        		document.all.item("CaijButton").disabled = true;
				document.getElementById("DTfont").style.color = 'yellow';
				document.all.item("Lastzl").value = zl;
        	}
        }else{
    		document.all.item("CaijButton").disabled = false;
        	document.getElementById("DTfont").style.color = 'yellow';
        }
	}
	setTimeout('getData();',500);
}
function caij1(){
	if(isTest==2){
		document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+20;
		isTest = 1;
	}else
		if(isTest==1){
			document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+60;
			isTest = 2;
		}else
			document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	
	if(Ext.isReady){
		var rec;
		if(DataIndex=="MAOZ"){
			if(Mode == "sel")
				rec = grid.getSelectionModel().getSelected();
			else
				rec = grid.getStore().getAt(0);
			rec.set("ZHONGCHH",Jilhh);
		}else
		if(DataIndex=="PIZ"){
			if(Mode == "sel")
				rec = gridp.getSelectionModel().getSelected();
			else
				rec = gridp.getStore().getAt(0);
			rec.set("QINGCHH",Jilhh);
		}
		rec.set(DataIndex,eval(document.all.item("caijzl").value));
	}
	return true;
}


function jiscount(){
	
	var res=grid.getStore().getRange();
	var maoz=0;
	var piz=0;
	var recCount;
				
	for(var  i=0;i<res.length;i++){
		var rec=res[i];
		if(rec.get('ID')=='-1'){ recCount=res[i];continue;}
		maoz+=parseFloat(rec.get('MAOZ'));
		piz+=parseFloat(rec.get('PIZ'));
	}
				
	recCount.set('MAOZ',maoz);
	recCount.set('PIZ',piz);
	recCount.set('JINGZ',(maoz-piz));
}

function caij(Mode, DataIndex){
	
	
	if(Ext.isReady){
		var rec;
		
		if(Mode == "sel"){
			rec = grid.getSelectionModel().getSelected();
			if(rec == null){
				Ext.MessageBox.alert('提示信息','请选择一条记录进行起始操作!');
				return;
			}
		}
		
		
	if(isTest==2){
		document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+20;
		isTest = 1;
	}else
		if(isTest==1){
			document.all.item("caijzl").value = eval(Math.round(Math.random()*1000))/100+60;
			isTest = 2;
		}else
			document.all.item("caijzl").value = eval(document.all.item("dongtzl").value);
	
//			alert(ljz);
		
		
			var lianjz=ljz;
			var new_zl=eval(document.all.item("caijzl").value);
			if(new_zl>lianjz){//  maoz
				if(rec.get('MAOZ')!=null && rec.get('MAOZ')>0 ){
					
					Ext.Msg.show({title:'提示信息',msg:'您没有选择新纪录，需要覆盖当前记录吗?',buttons:Ext.Msg.YESNOCANCEL,fn: function(btn){
						if(btn=='yes'){
							rec.set('MAOZ',new_zl);
							
							rec.set('ZHONGCSJ',getGhsj());
							
							rec.set('JINGZ',parseFloat(rec.get('MAOZ')-rec.get('PIZ')));
							jiscount();
					
							createTempFile(grid.getStore());
						}
						if(btn=='no'){
							
							if(row_line==grid.getStore().getCount()-1){Ext.Msg.alert('提示信息','已达数据末尾，无法添加!');return;}
							
							grid.getSelectionModel().selectRow(row_line+1);
							
							rec = grid.getSelectionModel().getSelected();
							rec.set('MAOZ',new_zl);
							rec.set('ZHONGCSJ',getGhsj());
							
							
							rec.set('JINGZ',parseFloat(rec.get('MAOZ')-rec.get('PIZ')));
							jiscount();
							
							
							
							createTempFile(grid.getStore());
						}
						
						if(btn=='cancel'){
							
						}
						
						
					}
					
					});
					
				}else{
					rec.set('MAOZ',new_zl);
					rec.set('ZHONGCSJ',getGhsj());
						
							
							rec.set('JINGZ',parseFloat(rec.get('MAOZ')-rec.get('PIZ')));
							jiscount();
						
					
					
					createTempFile(grid.getStore());
				}
			}else{//piz
				
					if(rec.get('PIZ')!=null && rec.get('PIZ')>0 ){
				Ext.Msg.show({title:'提示信息',msg:'您没有选择新纪录，需要覆盖当前记录吗?',buttons:Ext.Msg.YESNOCANCEL,fn: function(btn){
						if(btn=='yes'){
							rec.set('PIZ',new_zl);
							
							rec.set('QINGCSJ',getGhsj());
							
							rec.set('JINGZ',parseFloat(rec.get('MAOZ')-rec.get('PIZ')));
							jiscount();
							
							
							createTempFile(grid.getStore());
						}
						if(btn=='no'){
							
							if(row_line==grid.getStore().getCount()-1){Ext.Msg.alert('提示信息','已达数据末尾，无法添加!');return;}
							
							grid.getSelectionModel().selectRow(row_line+1);
							
							rec = grid.getSelectionModel().getSelected();
							rec.set('PIZ',new_zl);
							
							rec.set('QINGCSJ',getGhsj());
							
							rec.set('JINGZ',parseFloat(rec.get('MAOZ')-rec.get('PIZ')));
							jiscount();
							
							createTempFile(grid.getStore());
						}
				}
				
				});
				
				}else{
					rec.set('PIZ',new_zl);
					
					rec.set('QINGCSJ',getGhsj());
					
					rec.set('JINGZ',parseFloat(rec.get('MAOZ')-rec.get('PIZ')));
					jiscount();
						
					
					createTempFile(grid.getStore());
				}
			}
			
	//		createTempFile(grid.getStore());
	//	rec.set(DataIndex,eval(document.all.item("caijzl").value));
		//if(DataIndex=="MAOZ"){
	//		rec.set("ZHONGCHH",Jilhh);
	//	}else
	//	if(DataIndex=="PIZ"){
	//		rec.set("QINGCHH",Jilhh);
	//	}
	
	
	}
	return true;
}


function getTempData(filepath){

var s=" var data=[";
var fso = new ActiveXObject("Scripting.FileSystemObject"); 

if(fso.FileExists(filepath)){

var f=fso.openTextFile(filepath);

while(!f.AtEndOfStream){
var tem_s=f.ReadLine().split(',');
var jingz=parseFloat(tem_s[2])-parseFloat(tem_s[3]);
var cp=tem_s[1];
if(tem_s[1]=='0') cp='';
if(tem_s[6]==' ') mk='';
if(tem_s[7]==' ') pz='';
s+="['"+tem_s[0]+"','"+cp+"','"+tem_s[2]+"','"+tem_s[3]+"','"+jingz+"','"+tem_s[4]+"','"+tem_s[5]+"','"+tem_s[6]+"','"+tem_s[7]+"','"+tem_s[8]+"','"+tem_s[9]+"'],";
}
f.Close();
s=s.substring(0,s.lastIndexOf(","));
}
s+='];';
//alert(s);
eval(s);

grid.getStore().loadData(data,false);
}


function InitGridData(){

 	isdr=true;
	var filename=document.all.File_name.value;
//	alert(filename);

	var folderpath="D:/zhiren/huocfcjse/temp/huocfcj_temp";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	
	if(!fso.FileExists(folderpath)){
		Ext.Msg.alert('提示信息','没有数据可导入!');
		return;
	}
	

var filepath="D:/zhiren/huocfcjse/temp/huocfcj_temp";
getTempData(filepath);
}


function Daords(){
	if(drbl==false){
		Ext.Msg.alert('提示信息','没有文件数据!');
		return;
	}
	
	if(isdr){
		Ext.Msg.alert('提示信息','数据已经导入!');
		return;
	}
	InitGridData();
}

function createTempFile(gridDiv_ds)
{	
	if(gridDiv_ds.getCount()<=0){
		return ;
	}
	  
	var filepath="D:/zhiren/huocfcjse/temp/huocfcj_temp";
	var folderpath="D:/zhiren/huocfcjse/temp";
	var fso=new ActiveXObject("Scripting.FileSystemObject");
	
	if(!fso.FolderExists(folderpath)){
		fso.CreateFolder("D:/zhiren/huocfcjse/temp");
	}
	var f=fso.CreateTextFile(filepath,true);
	 	 var rec=gridDiv_ds.getRange();
           for(var j=0;j<rec.length;j++){
           	
           	var s0=rec[j].get('ID');
           	var s1=rec[j].get('CHEPH');
			var s2=rec[j].get('MAOZ');
			var s3=rec[j].get('PIZ');
			var s4=rec[j].get('ZHONGCSJ');
			var s5=rec[j].get('QINGCSJ');
			var s6=rec[j].get('MK');
			var s7=rec[j].get('PZ');
			var s8=rec[j].get('BEIZ');
			var s9=rec[j].get('RIQ');
			
			if(s1==null || s1=='') {s1='0';}
			if(s2==null || s2=='') {s2='0';}
			if(s3==null || s3=='') {s3='0';}
			if(s4==null || s4=='') {s4='0';}
			if(s5==null || s5=='') {s5='0';}
			if(s6==null || s6=='') {s6=' ';}
			if(s7==null || s7=='') {s7=' ';}
			if(s8==null || s8=='') {s8=' ';}
			if(s9==null || s9=='') {s9='0';}
		
           	var tem_s=s0+','+s1+","+s2+","+s3+","+s4+","+s5+","+s6+","+s7+","+s8+","+s9;
           	f.WriteLine(tem_s);
           }
	 	
	
	f.Close();
}


function grid_save(gridDiv_ds){
	
	var gridDivsave_history = '';
	var Mrcd = gridDiv_ds.getRange();
	
	for(i = 0; i< Mrcd.length; i++){
		
		var s1=Mrcd[i].get('ID');
		
		if(s1=='-1'){continue;}
		
		
		var s2=Mrcd[i].get('CHEPH');
		var s3=Mrcd[i].get('MAOZ');
		var s4=Mrcd[i].get('PIZ');
		var s5=Mrcd[i].get('ZHONGCSJ');
		var s6=Mrcd[i].get('QINGCSJ');
		var s7=Mrcd[i].get('BEIZ');
		var s8=Mrcd[i].get('RIQ');
		
		var s9=Mrcd[i].get('MK');
		var s10=Mrcd[i].get('PZ');
		if(s1==null || s1=='') {s1='0';}
		if(s2==null || s2=='') {Ext.Msg.alert("提示信息","车皮号不可为空!");return;}
		if(s3==null || s3=='') {s3='0';}
		if(s4==null || s4=='') {s3='0';}
		if(s5==null || s5=='') {s5='0';}
		if(s6==null || s6=='') {s6='0';}
		if(s7==null || s7=='') {s7=' ';}
		if(s8==null || s8=='') {s8='0';}
		
		if(s9==null || s9=='') {s9='0';}
		
		if(s10==null || s10=='') {s10='0';}
	
	gridDivsave_history +=s1+','+s2+','+s3+','+s4+","+s5+","+s6+","+s7+","+s8+","+s9+","+s10;
	 
	 if(i!=Mrcd.length-1){
	 	gridDivsave_history+=";";
	 }
	
 
	}
	
	if(gridDivsave_history=='' && document.all.DELCHANGE.value==''){
		Ext.Msg.alert('提示信息','没有数据，无法保存!');return;
	}
	
	var Cobj = document.getElementById('CHANGE');
    Cobj.value = gridDivsave_history;
	document.getElementById('SaveButton').click();
    Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});
    
	
}


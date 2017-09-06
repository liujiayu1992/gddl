var findindex=0;


function MatchNum(obj1,str){
	var Numr, Numre;
	var Nums = obj1.value;

	if(Nums.substring(0,1)=='.'){			//将首字符为'.'的数值替换为'0.'
		Nums= 0 + Nums;	
	}
	Numre = str;  							// 创建正则表达式对象。
	Numr = Nums.match(Numre);               // 在字符串 s 中查找匹配。
	if(Nums!= Numr){						// 如果不等
		alert("数值超出合理范围！");
		if (Editrows!=-1){
			var UpperID;
			var Index;
			UpperID = (obj1.id).toUpperCase( ); 
			Index = (UpperID.indexOf("_EDIT"));
			if(Index > 0 ){
				obj1.value=document.all.item(((obj1.id).substring(0,Index)),Editrows).value;
			}else{
				Index = (UpperID.indexOf("EDIT"));
				obj1.value=document.all.item(((obj1.id).substring(0,Index)),Editrows).value;
			}
		}
		obj1.focus();						// 输入框取得焦点
		return false;
	}else{									// 否则判断是否超过上下限						
		obj1.value = Nums;						// 如果没超过上下限，将改正的数值返回到原输入框
		return true;
	}
}
function MatchRegExp(obj,exp,Msg){
	var result;
	var Nums = obj.value;
	if(Nums.substring(0,1)=='.'){			//将首字符为'.'的数值替换为'0.'
		Nums= 0 + Nums;	
	}
	var defaulMsg = "数值超出合理范围！";
	if(Msg!=""){
		defaulMsg = Msg;
	}
	result = Nums.match(exp);               // 在字符串 s 中查找匹配。
	if(result == null){						// 如果不等
		alert(defaulMsg);
		return false;
	}else{									// 否则判断是否超过上下限						
		obj.value = Nums;						// 如果没超过上下限，将改正的数值返回到原输入框
	}
	return true;
}
function MatchStr(obj,len){
	var Strs = obj.value;
	var s = Strs.replace(/[^\x00-\xff]/g,"aa");		// 将汉字和全角字符替换为aa
	if(s.length > len){							// 判断字符串是否超过指定长度
		alert("输入字符串过长，请重新输入！");
		obj.value="";
		obj.focus();
		return false;	
	}		
	return true;	
}

function MatchStr(obj,len){
	var Strs = obj.value;
	var s = Strs.replace(/[^\x00-\xff]/g,"aa");		// 将汉字和全角字符替换为aa
	if(s.length > len){							// 判断字符串是否超过指定长度<
		alert("输入字符串过长，请重新输入！");
//				obj.value="";
		if (Editrows!=-1){
			var UpperID;
			var Index;
			UpperID = (obj.id).toUpperCase( ); 
			Index = (UpperID.indexOf("_EDIT"));
			if(Index > 0 ){
				obj.value=document.all.item(((obj.id).substring(0,Index)),Editrows).value;
			}else{
				Index = (UpperID.indexOf("EDIT"));
				obj.value=document.all.item(((obj.id).substring(0,Index)),Editrows).value;
			}
			//obj.value=document.all.item(((obj.id).substring(0,(obj.id).indexOf("edit"))),Editrows).value;
		}
		obj.focus();
		return false;
	}		
	return true;
}
function Trim(obj){								// 消除首尾空格
	var str = obj.value;
	var s = str.replace(/(^\s*)|(\s*$)/g, "");
	obj.value = s;
}	
function  IsNotNull(str,strcn){
	var row = oData.rows.length;
	var strname = str.split(",");
	var strnamecn = strcn.split(",");
	var jrow = strname.length;
	for(i=0;i < row;i++){      
		for(j=0;j < jrow;j++){  
			name = strname[j];
			//alert(name);
			if(document.all.item(name,i).value == "" || document.all.item(name,i).value == "请选择"){
				break;
			}
		}
		if(j!=jrow){
			var m= i+1;
			alert("第<"+m+">行<"+strnamecn[j]+">没有填写！");
			break;
		}	
	}
	if(i!=row){
		return false;
	}else{
		return true;
	}		
}
function  SingleRowIsNotNull(str,strcn){
//	var row = oData.rows.length;
	var strname = str.split(",");
	var strnamecn = strcn.split(",");
	var jrow = strname.length;
	for(j=0;j < jrow;j++){  
		name = strname[j];
		if(document.all.item(name).value == "" || document.all.item(name).value == "请选择"){
			alert(strnamecn[j]+"没有填写！");
			break;
		}
	}
	if(j!=jrow){
		return false;
	}else{
		return true;
	}		
}
function SetDisabled(tName,enable){
	var obj;
	obj = document.getElementById(tName);
	var aReturn;
	var l ;
	var i ;
	aReturn =obj.getElementsByTagName("INPUT");
	
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;
		v = aReturn[i].disabled = enable;
	}
     
	aReturn =obj.getElementsByTagName("SELECT");
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;
		v = aReturn[i].disabled = enable;
	}

	aReturn =obj.getElementsByTagName("TEXTAREA");
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;
		v = aReturn[i].disabled = enable;
	}
}

function SetDisabled_WithOut(tName,enable,strinput){

	var obj;
	obj = document.getElementById(tName);
	var aReturn;
	var l ;
	var i,j ;
	var  ainput=new Array();
	ainput=strinput.split(",");	
	aReturn =obj.getElementsByTagName("INPUT");
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;
						
		v = aReturn[i].disabled = enable;
		
	}
     
	aReturn =obj.getElementsByTagName("SELECT");
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;
						
		v = aReturn[i].disabled = enable;
	}

	aReturn =obj.getElementsByTagName("TEXTAREA");
	l=aReturn.length;
	for (i=0;i<l;i++){
		var v;	
		v = aReturn[i].disabled = enable;
	}
	
	
	if(enable){
		
		for(j=0;j<ainput.length;j++){
			
			document.all.item(ainput[j]).disabled=false;
		}
	}else{
		
		for(j=0;j<ainput.length;j++){
			
			document.all.item(ainput[j]).disabled=true;
		}

	}
}


function SelectRowEdit(_row){ 
    SetDisabled('editSelectData',false);
    Editrows =eval(_row);
	document.all.item("EditTableRow").value = Editrows;
	//oData.rows(oldClickrow).className
   	oData.rows(eval(_row)).className="edittableTrClick";
	document.all.item("SaveButton").disabled = false;
	document.all.item("DeleteButton").disabled = false;
}
function setRow(){
	if(oData.rows.length>0){
		if(document.all.item("EditTableRow").value==-1){
			SetDisabled('editSelectData',true);
		}else{
			if(oData.rows.length>document.all.item("EditTableRow").value || document.all.item("EditTableRow").value ==0 )
				SelectRowEdit(document.all.item("EditTableRow").value);
			}
		}
	else{
		SetDisabled('editSelectData',true);
	}
}


function findSM(RORC,valueColName,findColName){
	var Findvalue;
	var Obj;
	var Str;
	var Len;
	Obj = document.all.item(findColName+"$avail");
	Len = Obj.options.length;
	if(RORC==1){
		findindex = 0;
	}
	if(RORC==2){
		for(allindex=0;allindex<Len;allindex++){
			Obj.options(allindex).selected = true;
		}
		return true;
	}
	Findvalue = document.all.item(valueColName).value;
	if(findindex>=Len){
		findindex = 0;
	}
	for(;findindex<Len;findindex++){
		Str = Obj.options(findindex).text;
		if(Str.indexOf(Findvalue)!=-1){
			Obj.selectedIndex = findindex;
			findindex = findindex+1;
			break;
		}
	}
	if(findindex<=Len){
		return true;
	}else{
		return false;
	}
}

function onEnterdown(br,strid){
	if(event.keyCode==13){
		if (objselectCol !=null){
			var n;
			var row;
			var str = objselectCol.name;
			n=str.indexOf("$");
			if(n!=-1){
				row =eval(str.substring(n+1))+1;
			}else{
				row =0;
			}
			if(oldSelectobj !=null){
				oldSelectobj.fireEvent('onchange');
			}
			if(row+1 == oData.rows.length && objselectCol.id==strid){
				if(br==1){
					document.all.item("InsertButton").click();
				}else
					if(br==2){
						alert("已到达数据末尾！");
					}
			}else{
				if(objselectCol.id==strid){
					CopyLastRow(row);
				}
				objselectCol.select();
				//objselectCol.focus();
			}
		}
		event.keyCode=9;
	}
}

function CopyLastRow(lastrow){

}


function RealDel(){
	return confirm("是否确认删除?");
} 

function ParentChangeChild(arrobj,arrindex,EditValue){
	ArrAllLen = arrobj.length;						//取得总数组长度
	var ParentId = arrobj[arrindex][0];						//取得数组ID
	var ParentObj = document.getElementById(ParentId);  //取得父对象;
	var ParentIndex = ParentObj.selectedIndex;
	if(ParentIndex == -1){
		ParentIndex = 0;
	}
	arrindex = arrindex+1;
	var ParentValue = ParentObj.options(ParentIndex).value;  //取得父对象当前值
	var ChildId = arrobj[arrindex][0];						//取得子对象id
	var ChildObj = document.getElementById(ChildId);   //取得子对象
	ChildObj.length = 0;										//将子对象置空
	ChildArrObj = arrobj[arrindex][1];						//取得子数组对象
	if(ChildArrObj == null){
		return;
	}
	ChildArrLen = ChildArrObj.length;						//取得子数组长度
	var ChildIndex = 0;											//子下拉框索引置零
	for(i=0;i < ChildArrLen ;i++){
		if(ParentValue == ChildArrObj[i][0]){
			ChildObj.options[ChildIndex++] = new Option(ChildArrObj[i][2],ChildArrObj[i][1]);
		}
	}
	if(ChildObj.length == 0 ){
		return;
	}
	if(arrindex+1 == ArrAllLen){
		droponchange(EditValue,ChildObj,0);
		//UpdataDropDownEdit(EditValue,ChildObj,);
		return;
	}
	ParentChangeChild(arrobj,arrindex,EditValue);				// 递归调用
}

function ChildChangeParent(arrobj,arrindex){
	if(arrindex == -1){
		return;
	}
	var ChildId = arrobj[arrindex][0];					
	var ChildObj = document.getElementById(ChildId);
	var ChildSelectIndex = ChildObj.selectedIndex;
	if(ChildSelectIndex == -1){
		return;
	}
	var ChildValue = ChildObj.options(ChildSelectIndex).value;
	var ChildArrObj = arrobj[arrindex][1];
	var ChildArrLen = ChildArrObj.length;
	var ParentValue;
	for(ChildArrIndex=0;ChildArrIndex < ChildArrLen;ChildArrIndex++){
		if(ChildValue == ChildArrObj[ChildArrIndex][1]){
			ParentValue = ChildArrObj[ChildArrIndex][0];
			break;
		}
	}
	ChildObj.length = 0;
	var ChildIndex = 0;	
	for(ChildArrIndex=0;ChildArrIndex<ChildArrLen ;ChildArrIndex++){
		if(ParentValue == ChildArrObj[ChildArrIndex][0]){
			ChildObj.options[ChildIndex++] = new Option(ChildArrObj[ChildArrIndex][2],ChildArrObj[ChildArrIndex][1]);
		}
	}
	var	ChildLen = ChildObj.length;
	for(ChildIndex =0;ChildIndex < ChildLen ; ChildIndex++){
		if(ChildValue == ChildObj.options(ChildIndex).value){
			ChildObj.selectedIndex = ChildIndex;
			break;
		}
	}
	
	if(arrindex == 0){
		return;
	}else{
		arrindex = arrindex-1;
		var ParentId = arrobj[arrindex][0];
		var ParentObj = document.getElementById(ParentId);
		var	ParentLen = ParentObj.length;
		var ParentIndex =0;
		for(;ParentIndex < ParentLen ; ParentIndex++){
			if(ParentValue == ParentObj.options(ParentIndex).value){
				ParentObj.selectedIndex = ParentIndex;
				break;
			}
		}
		if(ParentIndex == ParentLen){
			ParentObj.length = 0;
			var ParentArrObj = arrobj[arrindex][1];
			var ParentArrLen = ParentArrObj.length;
			for(ParentArrIndex=0;ParentArrIndex<ParentArrLen ;ParentArrIndex++){
				if(ParentValue == ParentArrObj[ParentArrIndex][1]){
					ParentObj.options[0] = new Option(ParentArrObj[ParentArrIndex][2],ParentArrObj[ParentArrIndex][1]);
					ParentObj.selectedIndex = 0;
					break;
				}
			}
		}

	}
	ChildChangeParent(arrobj,arrindex);
}

function SelectDropDown(arrobj,arrindex,InputId,CurrentRow){
	var InputObj = document.all.item(InputId,CurrentRow);//document.getElementsByName(InputId+"$"+CurrentRow);
	var InputValue = InputObj.value;
	if(InputValue=="" || InputValue=="请选择"){
		for(i=arrindex;i>0;i--){
			var ChildId = arrobj[i][0];
			var ChildObj = document.getElementById(ChildId);
			ChildObj.length =0;
		}
		var ChildId = arrobj[0][0];
		var ChildObj = document.getElementById(ChildId);
		ChildObj.selectedIndex = 0;
	}
	var ChildId = arrobj[arrindex][0];
	var ChildObj = document.getElementById(ChildId);
	var	ChildLen = ChildObj.length;
	var ChildIndex =0;
	for(;ChildIndex < ChildLen ; ChildIndex++){
		if(InputValue == ChildObj.options(ChildIndex).value){
			ChildObj.selectedIndex = ChildIndex;
			break;
		}
	}
	if(ChildIndex == ChildLen){
		ChildObj.length = 0;
		var ChildArrObj = arrobj[arrindex][1];
		var ChildArrLen = ChildArrObj.length;
		for(ChildArrIndex=0;ChildArrIndex<ChildArrLen ;ChildArrIndex++){
			if(InputValue == ChildArrObj[ChildArrIndex][2]){
				ChildObj.options[0] = new Option(ChildArrObj[ChildArrIndex][2],ChildArrObj[ChildArrIndex][1]);
				ChildObj.selectedIndex = 0;
				break;
			}
		}
		ChildChangeParent(arrobj,arrindex);
	}
}

function name2obj(srcElementName){
	var n;
	var row =0;
	var str = srcElementName;
	strn =str;
	n=str.indexOf("$");
	if(n!=-1){
		strn =str.substring(0,n);
		row =eval(str.substring(n+1))+1;
	}
	return document.all.item(strn,row);
}
//2006-10-25
//王刚补充，化验审核使用
function RealSubmit(){
	return confirm("是否确认全部审核?");
}

function Round_New(value, _bit) {
	// 四舍六入五成双的法则修约，
	// 即：
	// 1.拟舍弃数字的第一位大于5则进1，如24.236--->24.24,小于5则舍弃，如23.234--->23.23.
	// 2.拟舍弃数字的第一位等于5，且5后面的数字并非全为0时则进1，如23.2251--->23.23
	// 3.拟舍弃数字的第一位等于5，且5后面的数字全部为0时，若5前面一位为奇数，则进1成双，如23.235--->23.24;
	// 若5前面为偶数，则舍去，如23.225--->23.22
	var value1;// 拟舍弃数字的第一位等于5，且5前面的数字
	value1 = Math.floor(value * Math.pow(10, _bit))
			- Math.floor(value * Math.pow(10, _bit - 1)) * 10;
	//		alert(value1);
	var dbla = 0;
	dbla =  Math.round(value * Math.pow(10, _bit) * 10000000) / 10000000;
	if ((dbla - Math.floor(value * Math.pow(10, _bit))) >= 0.5
			&& (dbla - Math.floor(value * Math.pow(10, _bit))) < 0.6) {
		if ((dbla - Math.floor(value * Math.pow(10, _bit))) == 0.5) {
			if (value1 == 0 || value1 == 2 || value1 == 4 || value1 == 6
					|| value1 == 8) {
				return Math.floor(value * Math.pow(10, _bit))
						/ Math.pow(10, _bit);
			} else {
				return (Math.floor(value * Math.pow(10, _bit)) + 1)
						/ Math.pow(10, _bit);
			}
		} else {
			return Math.round(value * Math.pow(10, _bit))
					/ Math.pow(10, _bit);
		}
	} else {
		return Math.round(value * Math.pow(10, _bit)) / Math.pow(10, _bit);
	}
}

function Round(Num , scale){
	var floorNum = Math.floor(Num * Math.pow(10,scale ));
	var floorNum1 = Math.floor(Num * Math.pow(10,scale -1))*10;
	var floorNum2 = Math.floor(Num * Math.pow(10,scale +1));
	var BitNum = floorNum - floorNum1;
	var scaleNum = floorNum2 - floorNum*10;
	return Math.round(Num * Math.pow(10,scale))/Math.pow(10,scale);
} 

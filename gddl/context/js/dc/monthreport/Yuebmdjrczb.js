var varRecord;
var v_Gongys = 0;
var v_Jihkj = 1;
var v_Yunsfs = 2;
var v_Meij = 3;
var v_Yunf = 4;
var v_Yunzf = 5;
var v_Shicjgyx = 6;
//var v_Suopl = 7;
var v_Rez = 7;

//function copyData() {
//	varRecord = new Array();
//	for (i=0; i<=gridDiv_ds.getCount()-1; i++) {
//		if (gridDiv_ds.getAt(i).get('FENX')=='ÀÛ¼Æ') {
//			varOneRecord = new Array();
//			varOneRecord[v_Shul] = gridDiv_ds.getAt(i).get('SHUL');
//			varOneRecord[v_Meij] = gridDiv_ds.getAt(i).get('MEIJ');
//			varOneRecord[v_Yunf] = gridDiv_ds.getAt(i).get('YUNF');
//			varOneRecord[v_Yunzf] = gridDiv_ds.getAt(i).get('YUNZF');
//			varOneRecord[v_Shicjgyx] = gridDiv_ds.getAt(i).get('SHICJGYX');
//			varOneRecord[v_Suopl] = gridDiv_ds.getAt(i).get('SUOPL');
//			varOneRecord[v_Rez] = gridDiv_ds.getAt(i).get('REZ');
//			varRecord[i] = varOneRecord;
//		}
//	}
//}

function getItem(field) {
	switch(field) {
		case 'SHUL':
			return v_Shul;
		case 'MEIJ':
			return v_Meij;
		case 'YUNF':
			return v_Yunf;
		case 'YUNZF':
			return v_Yunzf;
		case 'SHICJGYX':
			return v_Shicjgyx;
//		case 'SUOPL':
//			return v_Suopl;
		case 'REZ':
			return v_Rez;
	}
}

function getOriginalValue(record, field) {
	for (var i=0; i<varRecord.length; i++) {
		if (varRecord[i][v_Gongys]==record.get('GONGYSB_ID')
			&& varRecord[i][v_Jihkj]==record.get('JIHKJB_ID')
			&& varRecord[i][v_Yunsfs]==record.get('YUNSFSB_ID')
			) {
			
			return varRecord[i][getItem(field)];
		}
	}
}

function computeEveLj(gridDiv_ds, row, field) {
	var _bit = 2;
	if (field=='REZ') {
		_bit = 0;
	} else if (field=='MEIJ') {
		_bit = 3;
	}
	var SHUL = eval(gridDiv_ds.getAt(row).get('SHUL')||0);
	var SHUL_LJ = eval(gridDiv_ds.getAt(row+1).get('SHUL')||0);
	var varData = eval(gridDiv_ds.getAt(row).get(field)||0);
	var orgiData = eval(getOriginalValue(gridDiv_ds.getAt(row+1), field)||0);
	if (SHUL != SHUL_LJ) {
		varData = (varData*SHUL + orgiData*(SHUL_LJ-SHUL))/SHUL_LJ;
	}
	gridDiv_ds.getAt(row+1).set(field, Round_new(varData, _bit));
}

function afteredit(gridDiv_ds, e) {
	if (e.record.get('FENX') != 'ÀÛ¼Æ') {
		if (e.field=='SUOPL') {
			var oldValue = eval(gridDiv_ds.getAt(e.row+1).get(e.field)||0);
			var changeValue = eval(e.value||0)-eval(e.originalValue||0);
			gridDiv_ds.getAt(e.row+1).set(e.field, eval(oldValue + changeValue));
		}else {
			computeEveLj(gridDiv_ds, e.row, e.field)
		}
		computeBiaomdj(gridDiv_ds, e.row);
		computeBiaomdj(gridDiv_ds, e.row+1);
		
	} else {
		computeBiaomdj(gridDiv_ds, e.row);
	}
}

function beforeedit(gridDiv_ds, e, SelectLike) {
	if(e.record.get('FENX')=='ÀÛ¼Æ') {
		if(!SelectLike.checked) {
			e.cancel=true;
		}		
	} else {
		if (e.field=='MEIJ'||e.field=='YUNF'||e.field=='YUNZF'||e.field=='SHICJGYX'||e.field=='SUOPL'||e.field=='REZ') ;
		else e.cancel = true;
	}
}

function computeBiaomdj(gridDiv_ds, row) {	
	var record = gridDiv_ds.getAt(row);
	var MEIJ = eval(record.get('MEIJ')||0);
	var YUNF = eval(record.get('YUNF')||0);
	var YUNZF = eval(record.get('YUNZF')||0);
	var REZ = eval(record.get('REZ')||0);
	var BIAOMDJ = 0;
	if (REZ > 0) {
		BIAOMDJ = Round_new((MEIJ+YUNF+YUNZF)*29271 / REZ, 2) ;
		gridDiv_ds.getAt(row).set('BIAOMDJ', BIAOMDJ);
	}
}
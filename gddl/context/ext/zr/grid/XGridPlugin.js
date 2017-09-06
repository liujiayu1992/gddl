Ext.namespace("Ext.ux.plugins");

Ext.ux.plugins.XGrid = function(config) {
	Ext.apply(this, config);
};

Ext.extend(Ext.ux.plugins.XGrid, Ext.util.Observable, {
    init: function(grid){
		this.grid = grid;
		this.view = this.grid.getView();
        this.view.layout = this.layout.createDelegate(this);
        this.grid.render = this.render.createDelegate(this);
        var cm = this.grid.getColumnModel();
        if(cm.headers) {
            this.initTemplates();
            this.initMethodDelegate();
            this.grid.on("columnresize", this.onColumnresize, this);
        }
 	},
    render : function(container, position){
        if(!this.grid.rendered && this.grid.fireEvent("beforerender", this) !== false){
            if((!container && this.grid.el) || typeof container == "string"){
                var elId = container ? container : this.grid.el;
                var c = document.getElementById(elId);
                if (!c.style.height) {
                    c.style.height = '100%';
                }
                this.grid.originWidth = c.offsetWidth;
                this.grid.originHeight = c.offsetHeight;
                this.grid.el = Ext.get(elId);
                container = this.grid.el.dom.parentNode;
                this.grid.allowDomMove = false;
            }
            this.grid.container = Ext.get(container);
            if(this.grid.ctCls){
                this.grid.container.addClass(this.grid.ctCls);
            }
            this.grid.rendered = true;
            if(position !== undefined){
                if(typeof position == 'number'){
                    position = this.grid.container.dom.childNodes[position];
                }else{
                    position = Ext.getDom(position);
                }
            }
            this.grid.onRender(this.grid.container, position || null);
            if(this.grid.autoShow){
                this.grid.el.removeClass(['x-hidden','x-hide-' + this.grid.hideMode]);
            }
            if(this.grid.cls){
                this.grid.el.addClass(this.grid.cls);
                delete this.grid.cls;
            }
            if(this.grid.style){
                this.grid.el.applyStyles(this.grid.style);
                delete this.grid.style;
            }
            this.grid.fireEvent("render", this);
            this.grid.afterRender(this.grid.container);
            if(this.grid.hidden){
                this.grid.hide();
            }
            if(this.grid.disabled){
                this.grid.disable();
            }

            this.grid.initStateEvents();
        }
        return this;
    },

    layout : function(){
        if(!this.view.mainBody){
            return; // not rendered
        }
        var g = this.view.grid;
        var c = g.getGridEl(), cm = this.view.cm,
                expandCol = g.autoExpandColumn,
                gv = this;
        if( Ext.isIE) {
            c.setHeight(this.grid.originHeight);
        }
        var csize = c.getSize(true);
        var vw = csize.width;

        if(vw < 20 || csize.height < 20){ // display: none?
            return;
        }

        if(g.autoHeight){
            this.view.scroller.dom.style.overflow = 'visible';
        }else{
            if( Ext.isIE) {
                this.view.el.setSize('100%', csize.height);
    
                var hdHeight = this.view.mainHd.getHeight();
                var vh = csize.height - (hdHeight);

    
                this.view.scroller.setSize('100%', vh);
                if(this.view.innerHd){
                    this.view.innerHd.style.width = '100%';
                }
            }    
            else {
                this.view.el.setSize(csize.width, csize.height);
    
                var hdHeight = this.view.mainHd.getHeight();
                var vh = csize.height - (hdHeight);
    
                this.view.scroller.setSize(vw, vh);
                if(this.view.innerHd){
                    this.view.innerHd.style.width = (vw)+'px';
                }
            }
        }
        if(this.view.forceFit){
            if(this.view.lastViewWidth != vw){
                this.view.fitColumns(false, false);
                this.view.lastViewWidth = vw;
            }
        }else {
            this.view.autoExpand();
        }
        this.view.onLayout(vw, vh);
    },
    initTemplates: function(){
        this.view.initTemplates.call(this);
        this.view.templates = {
            header : new Ext.Template(
                    '<table border="0" cellspacing="0" cellpadding="0" style="{tstyle};TABLE-LAYOUT: auto">',
                    '<thead>',
                    '{headers}',
                    '</thead>',
                    "</table>"
                    ),
            hcell : new Ext.Template(
                    '<td ',
                    '{rowspan}',
                    ' {colspan} ',
                    'class="x-grid3-hd x-grid3-cell x-grid3-td-{id}" ',
                    'style="{style}"><div {tooltip} {attr} ',
                    'class="x-grid3-hd-inner x-grid3-hd-{id}" ',
                    'unselectable="on" style="{istyle}">', 
                    this.grid.enableHdMenu ? '<a class="x-grid3-hd-btn" href="#"></a>' : '',
                    '{value}<img class="x-grid3-sort-icon" src="', Ext.BLANK_IMAGE_URL, '" />',
                    "</div></td>"
                    ),
            ghcell : new Ext.Template(
                    '<td ',
                    '{rowspan}',
                    ' {colspan} ',
                    'class="x-grid3-hd" ',
                    'style="border-bottom:1px solid #D0D0D0;{style}"><div {tooltip} {attr} ',
                    'class="x-grid3-hd-inner" ',
                    'unselectable="on" style="{istyle}">', 
                    this.grid.enableHdMenu ? '<a class="x-grid3-hd-btn" href="#"></a>' : '',
                    '{value}<img class="x-grid3-sort-icon" src="', Ext.BLANK_IMAGE_URL, '" />',
                    "</div></td>"
                    )

        };
    },
    
    initMethodDelegate: function() {
        var view = this.grid.getView();
        view.renderHeaders = this.renderHeaders.createDelegate(this);
        view.getColumnStyle = this.getColumnStyle.createDelegate(this);
        view.getHeaderCell = this.getHeaderCell.createDelegate(this);
        view.updateSortIcon = this.updateSortIcon.createDelegate(this);
        var cm = this.grid.getColumnModel();
        cm.moveColumn = this.moveColumn.createDelegate(this);
        /*
        view.getColumnWidth = this.getColumnWidth.createDelegate(this);
        view.getTotalWidth = this.getTotalWidth.createDelegate(this);
        */
    },

    moveColumn : function(oldIndex, newIndex){
        var cm = this.grid.getColumnModel();
        var c = cm.config[oldIndex];
        if (this.realCM) {
            var rm1 = this.realCM[oldIndex];
            var rm2 = this.realCM[newIndex];
            if ( (!rm1.rowspan && !rm2.rowspan) || (rm1.rowspan == rm2.rowspan)) {
                cm.config.splice(oldIndex, 1);
                cm.config.splice(newIndex, 0, c);
                cm.dataMap = null;
                this.columnMoved = true;
                cm.fireEvent("columnmoved", cm, oldIndex, newIndex);
                this.columnMoved = false;
            }
        } else {
            cm.config.splice(oldIndex, 1);
            cm.config.splice(newIndex, 0, c);
            cm.dataMap = null;
            this.columnMoved = true;
            cm.fireEvent("columnmoved", cm, oldIndex, newIndex);
            this.columnMoved = false;
        }
    },
    
    renderHeaders : function(){
        var cm = this.grid.getColumnModel();
        var hm = cm.headers;
        var ts = this.view.templates;
        var ct = ts.hcell;
        var tHeaderRow = new Ext.Template(
            '<tr class="x-grid3-hd-row">{cells}</tr>'
        );
        this.realCM = this.processRealColumnModel();
        /*
        for(var i = 0, len = this.realCM.length; i < len; i++){
            var config = cm.config[i];
            rm = this.realCM[i];
            //rm.style = this.getColumnStyle(i, true, rm);
            //rm.id = config ? config.id : rm.id;
            if (config) {
                rm.header = config.header;
            }
        }
        */

        var  rs = [];
        
        var colIndex = 0;
        for(var k = 0; k < hm.length; k++) {
            var header = hm[k];
            var cb = [];
            for(var i = 0, len = header.length; i < len; i++){
                var p = {};
                var rm = header[i];
                p.id = rm.id ? rm.id : i;
                p.value = rm.header || "";
                //p.value = this.findHeaderFromConfig(rm.id, rm.header) || "";
                p.style = rm.style || ""; // this.view.getColumnStyle(i, true, rm);
                //p.style = rm.style || ;
                p.tooltip = cm.getColumnTooltip(i);
                if (rm.rowspan) {
                    p.rowspan = 'rowspan=' + rm.rowspan;
                }
                if (rm.colspan && rm.colspan > 1) {
                    var colspan = rm.colspan;
                    var w = 0;
                    for (var c = 0; c < colspan; c++) {
                        var subCM = cm.config[colIndex+c];
                        if (subCM) {
                            w += cm.getColumnWidth(colIndex+c);
                        }
                    }
                    var style = '';
                    style += 'width:'+w+'px;';
                    if(cm.isHidden(i)){
                        style += 'display:none;';
                    }
                    var align = rm.align;
                    if(align){
                        style += 'text-align:'+align+';';
                    } else {
                        style += 'text-align:center;';
                    }
                    p.style = style;
                    p.colspan = 'colspan=' + rm.colspan;
                }
                
                if (rm.align == 'right'){
                    //p.istyle = 'padding-right:16px';
                } else {
                    delete p.istyle;
                }
                if (typeof(rm.id) != typeof(undef)) {
                    cb[cb.length] = ct.apply(p);
                    colIndex++ ;
                } else {
                    cb[cb.length] = ts.ghcell.apply(p);
                }
            }
            rs[rs.length] = tHeaderRow.apply({cells: cb.join("")});
        }

        //var data = ts.header.apply({headers:rs.join("")});
        var data = ts.header.apply({headers:rs.join(""), tstyle:'width:'+this.view.getTotalWidth()+';'});
        
        //document.getElementById("testdata").value=data;
        return data;
    },
    
    processRealColumnModel : function() {
        var cm = this.grid.getColumnModel();
        if (!cm.headers) {
            return cm.config;
        }
        var hm = cm.headers;
        var rows = hm.length;
        var realModels = [];
        var tmp = [];
        for (var i = 0; i < rows; i++) {
            tmp.push([]);
        }
        for (var k = 0; k < rows; k++) {
            var header = hm[k];
            for(var i = 0, len = header.length; i < len; i++){
                var p = header[i];
                
                var position = this.pushInto(tmp[k], p);
                
                if (p.rowspan && p.rowspan > 0) {
                    for (var r = 1; r < p.rowspan; r++) {
                        tmp[k + r][position] = p;
                    }
                }
                
                if (p.colspan && p.colspan > 0) {
                    for (var c = 0; c < p.colspan - 1; c++) {
                        position = this.pushInto(tmp[k], p);
                        if (p.rowspan && p.rowspan > 0) {
                            for (var r = 1; r < p.rowspan; r++) {
                                tmp[k + r][position] = p;
                            }
                        }
                    }
                }
            }
        }
        
        for (var i = 0; i < tmp[tmp.length-1].length; i++) {
            realModels.push(tmp[tmp.length-1][i]);
        }

        delete tmp;
        
        for(var i = 0, len = realModels.length; i < len; i++){
            var config = cm.config[i];
            rm = realModels[i];
            rm.style = this.getColumnStyle(i, true, rm);
            rm.id = config ? config.id : rm.id;
            if (config) {
                if (this.columnMoved) {
                    rm.header = config.header;
                } else {
                    config.header = rm.header;
                }

            }
        }
        return realModels;
    },
    
    pushInto : function(arrayObj, obj) {
        for (var i = 0; i < arrayObj.length; i++) {
            if (!arrayObj[i]) {
                arrayObj[i] = obj;
                return i;
            }
        }
        arrayObj.push(obj);
        return arrayObj.length - 1;
    },

    getColumnStyle : function(col, isHeader, realModel){
        var cm = this.grid.getColumnModel();
        var view = this.grid.getView();
        var style = !isHeader ? (cm.config[col].css || '') : '';
        if (Ext.isIE) {
            var w = cm.getColumnWidth(col);
            //w = isHeader ? w : w + 2;
            style += 'width:'+w+'px;';
        } else {
            style += 'width:'+this.view.getColumnWidth(col)+';';
        }
        if(cm.isHidden(col)){
            style += 'display:none;';
        }
        var align = cm.config[col].align;
        if (realModel && realModel.align) {
            align = realModel.align;
        }
        if (align) {
            style += 'text-align:'+align+';';
        } else {
            style += 'text-align:center;';
        }
        return style;
    },
    
    getHeaderCell : function(index){
        var view = this.grid.getView();
        var hds = view.mainHd.select('.x-grid3-cell');
        return (hds.item(index).dom);
    },
    
    updateSortIcon : function(col, dir){
        var sc = this.view.sortClasses;
        var hds = this.view.mainHd.select('.x-grid3-cell').removeClass(sc);
        hds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
    },
    
    
    onColumnresize : function(col, width){
        var cm = this.grid.getColumnModel();
        this.grid.reconfigure(this.grid.store,cm);
    }
});
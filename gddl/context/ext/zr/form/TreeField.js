Ext.form.TreeField = Ext.extend(Ext.form.TriggerField,  {
	readOnly : true	,
	defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},
	displayField : 'text',
	valueField : undefined,
	hiddenName : undefined,
	listWidth : undefined,
	minListWidth : 50,
	layerHeight : undefined,
	minLayerHeight : 60,
	dataUrl : undefined,
	tree : undefined,
	value : undefined,
	baseParams : {},
	treeRootConfig : {
		id : ' ',
		text : 'please select...',
		draggable:false
		},
	initComponent : function(){
		Ext.form.TreeField.superclass.initComponent.call(this);
		this.addEvents(
				'select',
				'expand',
				'collapse',
				'beforeselect'	   
		);
		
		if(this.transform){
            this.allowDomMove = false;
            var s = Ext.getDom(this.transform);
            if(!this.hiddenName){
                this.hiddenName = s.name;
            }
            
            s.name = Ext.id(); 
            if(!this.lazyRender){
                this.target = true;
                this.el = Ext.DomHelper.insertBefore(s, this.autoCreate || this.defaultAutoCreate);
                Ext.removeNode(s); 
                this.render(this.el.parentNode);
            }else{
                Ext.removeNode(s); 
            }

        }
	},
	onRender : function(ct, position){
        Ext.form.TreeField.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName, id: (this.hiddenId||this.hiddenName)},
                    'before', true);
            this.hiddenField.value =
                this.hiddenValue !== undefined ? this.hiddenValue :
                this.value !== undefined ? this.value : '';
            this.el.dom.removeAttribute('name');
        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }

        this.initList();
    },
	initList : function(){
        if(!this.list){
            var cls = 'x-treefield-list';

            this.list = new Ext.Layer({
                shadow: this.shadow, cls: [cls, this.listClass].join(' '), constrain:false
            });

            var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);
            this.list.setWidth(lw);
            this.list.swallowEvent('mousewheel');
			
			this.innerList = this.list.createChild({cls:cls+'-inner'});
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));
			this.innerList.setHeight(this.layerHeight || this.minLayerHeight);
			if(!this.tree){
				this.tree = this.createTree(this.innerList);	
			}
			var treeField = this;
        	this.tree.on('click',function(node,e){
				treeField.onSelect(node);
			});
			this.tree.render();
        }
    },
	onSelect:function(node){
		if(this.fireEvent('beforeselect', node, this)!= false){
			this.setValue(node);
	        this.collapse();
	        this.fireEvent('select', this, node);
		}
	},
	createTree:function(el){
		var Tree = Ext.tree;
    
		var tree = new Tree.TreePanel({
			el:el,
			autoScroll:true,
			animate:true,
			containerScroll: true, 
			loader: new Tree.TreeLoader({
				dataUrl : this.dataUrl,
				baseParams : this.baseParams
			})
		});
	
		var root = new Tree.AsyncTreeNode(this.treeRootConfig);
		tree.setRootNode(root);
		return tree;
	},
	getValue : function(){
        if(this.valueField){
            return typeof this.value != 'undefined' ? this.value : '';
        }else{
            return Ext.form.TreeField.superclass.getValue.call(this);
        }
    },
	setValue : function(node){
		var text = node[this.displayField];
		var value = node[this.valueField || this.displayField];
		if(this.hiddenField){
            this.hiddenField.value = value;
        }
		Ext.form.TreeField.superclass.setValue.call(this, text);
		this.value = value;
    },
	onDestroy : function(){
        if(this.list){
            this.list.destroy();
        }
        Ext.form.TreeField.superclass.onDestroy.call(this);
    },
	collapseIf : function(e){
        if(!e.within(this.wrap) && !e.within(this.list)){
            this.collapse();
        }
    },
	expand : function(){
        if(this.isExpanded() || !this.hasFocus){
            return;
        }
        this.list.alignTo(this.wrap, this.listAlign);
        this.list.show();
        Ext.getDoc().on('mousewheel', this.collapseIf, this);
        Ext.getDoc().on('mousedown', this.collapseIf, this);
        this.fireEvent('expand', this);
    },
	collapse : function(){
        if(!this.isExpanded()){
            return;
        }
        this.list.hide();
        Ext.getDoc().un('mousewheel', this.collapseIf, this);
        Ext.getDoc().un('mousedown', this.collapseIf, this);
        this.fireEvent('collapse', this);
    },
	isExpanded : function(){
        return this.list && this.list.isVisible();
    },
	onTriggerClick : function(){
        if(this.disabled){
            return;
        }
        if(this.isExpanded()){
            this.collapse();
        }else {
            this.onFocus({});
            this.expand();
        }
		this.el.focus();
    }
								
});
Ext.reg('treefield', Ext.form.TreeField);
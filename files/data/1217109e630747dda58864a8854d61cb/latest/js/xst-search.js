var clist=new Array();
$(function(){
	
	$("ul").each(function(index,e){
		var ind=index;
		$(this).find("li").each(function(){
			clist[clist.length]={'con':$(this).html(),title:$(this).text(),ind:ind};
		});
	});
	
     $("#search").bind('input propertychange',function(event) {  
         findClass($(this).val());
     }); 
});

function findClass(val){
	var nlist=new Array();
	if(val===""){
		createView(clist);
	}else{
		val=val.toLowerCase();
		for(let s of clist){
			if(s.title.toLowerCase().indexOf(val)!=-1){
				nlist.push(s);
			}
		}
		createView(nlist);
	}
}

function createView(list){
	var strs=new Array();
	for(let s of list){
		if(!strs[s.ind]){
			strs[s.ind]='';
		}
		strs[s.ind]+='<li>'+s.con+'</li>';
	}
	$("ul").each(function(index){
		$(this).html(strs[index]?strs[index]:"");
	});
}
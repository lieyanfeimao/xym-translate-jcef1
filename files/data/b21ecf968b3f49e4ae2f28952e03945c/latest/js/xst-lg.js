$(function(){
	$("body").append('<div class="lgbox">'+
	'<span class="lglist" id="lglist1" onclick="changeSel(\'1\')">中文</span><br> '+
   '<span class="lglist" id="lglist2" onclick="changeSel(\'2\')">英文</span><br>'+
   '<span class="lglist lgsel" id="lglist0" onclick="changeSel(\'0\')">中英文</span><br> '+
  '</div>'+
  '<div class="container helpdocdesc" >'+
   '本翻译文档由<a href="https://51helpdoc.com" target="_blank">https://51helpdoc.com</a>整理,仅做学习交流,勿作他用'+
  '</div>');
//	var val=Cookies.get('lgname1',{path:'/'});
//	changeSel(val);
	
	
	const val = localStorage.getItem('lgname1');
	changeSel(val);
});

function changeSel(val){
	if(val){
		localStorage.setItem('lgname1', val);
		//Cookies.set('lgname1', val, { expires: 360, path: '/'});
		$(".lglist").removeClass("lgsel");
		$("#lglist"+val).addClass("lgsel");
		if(val==='1'){
			$('[lg_zh]').show();
			$('[lg_zh]').removeClass("tscolor");
			$('[lg_kh]').hide();
			$('[lg_en]').hide();
		}else if(val==='2'){
			$('[lg_zh]').hide();
			
			$('[lg_en]').show();
		}else{
			$('[lg_zh]').addClass("tscolor");
			$('[lg_zh]').show();
			$('[lg_kh]').show();
			$('[lg_en]').show();
		}
	}
}
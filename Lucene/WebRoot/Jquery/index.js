window.Index = {
	fileList: null,
	ManageFile : null,
	OptionSelect: null,
	ManageIndex : null
};

Index.SearchManagement = function() {
	var indexSearch = function() {
		$('body').on('keydown', function(e) {
			if(e.which == 13) {
				e.preventDefault();
				initSearch();
			}
		});
		
		$('#submit').on('click', function(e) {
			initSearch();
		});
	};
	
	var initSearch = function() {
		var searchContent = Index.OptionSelect.optionSelect();
		if( searchContent.content != '') {
			var file = {};
			if(searchContent.option == 'contents') {
				file.contents = searchContent.content;
			} else if(searchContent.option == 'name') {
				file.name = searchContent.content;
			} else if(searchContent.option == 'type') {
				file.type = searchContent.content;
			} else if(searchContent.option == 'author') {
				file.author = searchContent.content;
			} else if(searchContent.option == 'date') {
				file.date = searchContent.content;
			} else if(searchContent.option == 'version') {
				file.version = searchContent.content;
			} else if(searchContent.option == 'complexity') {
				
			}
			$.getJSON('SearchServlet/'+ searchContent.option +'Search', file, function(files) {
				Index.ManageFile.loadFiles(files);
			});
		};
	};
	
	indexSearch();
};

Index.OptionManagement = function() {
	var optionSetting = function() {
		$('.fullTextSearch').addClass('choice');
		var indexContent = $('#indexContent');
		indexContent.val('').attr('placeholder', "请输入要搜索的内容");
		$('#option').on('click', 'span', function() {
			var option = $(this);
			if(!option.hasClass('choice')) {
				$('.choice').removeClass('choice');
				option.addClass('choice');
				if(option.attr('id') == 'contetnts') indexContent.val('').attr('placeholder', "请输入要搜索的内容");
				else if(option.attr('id') == 'name') indexContent.attr('placeholder', "请输入文件名称");	
				else if(option.attr('id') == 'type') indexContent.attr('placeholder', "请输入文件类型");	
				else if(option.attr('id') == 'author') indexContent.attr('placeholder', "请输入文件作者");	
				else if(option.attr('id') == 'date') indexContent.attr('placeholder', "请输入文件提交时间（年-月-日）");	
				else if(option.attr('id') == 'version') indexContent.attr('placeholder', "请输入文件版本");
				else if(option.attr('id') == 'complexity') indexContent.attr('placeholder', "请输入复合条件（文件名，文件类型，文件作者......）");	
			}
		});
	};
	
	this.optionSelect = function() {
		var searchContent = {
			content : '',
			option : ''
		};
		if($('#indexContent').val() != '') searchContent.content = $('#indexContent').val();
		searchContent.option = $('.choice').attr('id');
		return searchContent;
	};
	
	optionSetting();
};

Index.MenuManagement = function() {
	var operateMenu = function() {
		$('.menu').menu({
			onClick:function(item){
				if(item.id == 'monitor') {
					$('#state').text('即时索引');
					Index.ManageIndex.monitorIndex();
				} else if(item.id == 'cycle') {
					$('#state').text('周期索引');
					Index.ManageIndex.cycleIndex();
				} else if(item.id == 'manual') {
					$('#state').text('手动索引');
					Index.ManageIndex.manualIndex();
				} else if(item.id == 'openFile') {
					Index.ManageFile.openFile(Index.ManageFile.getFile());
				}else if(item.id == 'openLog') {
					Index.ManageFile.openLog(Index.ManageFile.getFile());
				} else if(item.id == 'download') {
					Index.ManageFile.downloadFile();
				} else if(item.id =='difference') {
					Index.ManageFile.compareDifference(Index.ManageFile.getFile());
				}
			}
		});
		
	};
	
	var initMenu = function() {
		$('<div id="fileMenu"><div id="openFile" data-options="iconCls:\'icon-search\'">openFile</div><div id="openLog" data-options="iconCls:\'icon-search\'">openLog</div><div id="download" data-options="iconCls:\'icon-down\'">download</div><div id="rsizeFile" data-options="iconCls:\'icon-edit\'">compare</div></div>').appendTo('#content').menu();
		$('#manage').splitbutton({
			iconCls: 'icon-setting',
			menu: '#indexMenu'
		});
	};
	
	this.message = function(message,fn) {
		$.messager.confirm('Message', message, fn);
	};
	
	$(document).ajaxError(function(exception) {
		Index.MenuManagement.message('Internal system has error , please contact with the administrator');
	});
	
	initMenu();
	operateMenu();
};

Index.SettingManagement = function() {
	
	var initSetting = function() {
		$('#indexContent').focus();
		$('#viewThumbs').attr('checked', 'checked');
		$('#showName').attr('checked', 'checked');
		$('#showAuthor').attr('checked', 'checked');
		$('#showDate').attr('checked', 'checked');
		$('#showSize').attr('checked', 'checked');
		$('#sortName').attr('checked', 'checked');
	};
	
	var settings = function() {
		$('#toolbar').on('click', 'a', function() {
			if($(this).is('a[id="setting"]')) {
				$('#settings').toggle();
			} else if($(this).is('a[id="refresh"]')) {
				
			} 
		});
	};

	initSetting();
	settings();
};

Index.IndexManagement = function() {
	
	var indexState = function() {
		if(!localStorage.getItem('indexState')) {
			$.getJSON('IndexServlet/manualIndex', function() {
			});
			localStorage.setItem('indexState','手动索引');
		} 
		$('#state').text(localStorage.getItem('indexState'));
	};
	
	this.monitorIndex = function() {
		$.getJSON('IndexServlet/monitorIndex', function() {
		});
		localStorage.setItem('indexState','即时索引');
	};
	
	this.cycleIndex = function() {
		$.getJSON('IndexServlet/cycleIndex', function() {
		});
		localStorage.setItem('indexState','周期索引');
	};
	
	this.manualIndex = function() {
		$.getJSON('IndexServlet/manualIndex', function() {
		});
		localStorage.setItem('indexState','手动索引');
	};
	
	indexState();
};

Index.FileManagement = function() {
	
	this.loadFiles = function(files) {
		Index.fileList = files;
		showFiles(files.sort(sortFiles));
	};
	
	this.openFile = function(file) {
		$.getJSON('FileServlet/openFile', file);
	};
	
	this.openLog = function(file) {
		$.getJSON('FileServlet/openLog', file, function(file) {
			$('<div id="log"><tr><td>提交作者：'+file.author+'</td></tr><br><tr><td>文件版本：'+file.version+'</td></tr><br><tr><td>提交日期：'+file.date+'</td></tr><br><tr><td>提交信息：'+file.message+'</td></tr></div>').insertAfter('#files').dialog({
				title: 'File Log',
				buttons:[{
					text:'Close',
					handler:function(){
						$('#log').dialog('close').remove();
					}
				 }],
				 width: 400,
				 height: 200,
				 closed: false,
				 cache: false,
				 modal: true
			});
		});
	};
	
	this.compareDifference = function() {
		$.getJSON('FileServlet/compareDifference', {path:""}, function() {
			
		});
	};
	
	this.downloadFile = function() {   
		$('<iframe name="downloadFile"  style="display:none"></iframe>').appendTo('body');
		$('<form target="downloadFile" method="post" action="FileServlet/downloadFile"></form>').appendTo('body');
		$.each(Index.ManageFile.getFilePath(),function(index, value) {
			$('<input name="pathArray[]" type="hidden" value="'+value+'"/>').appendTo('form');
		});
		$.each(Index.ManageFile.getFileVersion(),function(index, value) {
			$('<input name="versionArray[]" type="hidden" value="'+value+'"/>').appendTo('form');
		});
		$('form').submit();
		return false;
	};
	
	var operateFiles = function() {
		$('.layout').on('click',function(e){
			if($(e.target).closest('.file').length == 0) $('.file.selected').each(function(){$(this).removeClass('selected');});
		});
		$('#files').on('mousedown', '.file', function(e) {
			if(e.which == 1) {
				if(e.ctrlKey || e.shiftKey) {
					if(!$('div[contentEditable="true"]').is(':focus') && !$('td[contentEditable="true"]').is(':focus')) $(this).toggleClass('selected');
					$('#fileinfo').text(Index.ManageFile.getFiles().length > 1 ? Index.ManageFile.getFiles().length + ' files' : Index.ManageFile.getFiles().length + ' file');
				}else {
					if($('.file.selected').each(function() {$(this);}) != null) {
						$('.file.selected').each(function(){
							$(this).removeClass('selected');
						});
					}
					if(!$('div[contentEditable="true"]').is(':focus') && !$('td[contentEditable="true"]').is(':focus')) $(this).toggleClass('selected');
					$('#fileinfo').text($(this).children('.name').text() + ' ' + '(' + $(this).children('.size').text() + ',' + $(this).children('.date').text() +')');
				}
			}else {
				if(e.ctrlKey || e.shiftKey) {
					$(this).addClass('selected');
					$('#fileinfo').text(Index.ManageFile.getFiles().length > 1 ? Index.ManageFile.getFiles().length + ' files' : Index.ManageFile.getFiles().length + ' file');
				} else {
					if(!$(this).hasClass('selected')) {
						if($('.file.selected').each(function() {$(this);}) != null) {
							$('.file.selected').each(function(){
								$(this).removeClass('selected');
							});
						}
					}
					$(this).addClass('selected');
					$('#fileinfo').text($(this).children('.name').text() + ' ' + '(' + $(this).children('.size').text() + ',' + $(this).children('.date').text() +')');
				}
			}
			$('.file').on('contextmenu',function(e){		
				 e.preventDefault();
				 $('#fileMenu').menu('show',{
					 left: e.pageX,
					 top: e.pageY
				 });
			});
		});
	};
	
	var orderFiles = function() {
		$('#settings').on('click', 'input' ,function() {
			if($('#files').text() != '') {
				if($(this).is('input[id="viewList"]')) showFiles(Index.fileList.sort(Index.sortFiles));
				else if($(this).is('input[id="viewThumbs"]')) showFiles(Index.fileList.sort(Index.sortFiles));
				else if($(this).is('input[id="showName"]')) $('.name').toggle();
				else if($(this).is('input[id="showAuthor"]')) $('.author').toggle();
				else if($(this).is('input[id="showSize"]')) $('.size').toggle();
				else if($(this).is('input[id="showDate"]')) $('.date').toggle();
				else if($(this).is('input[id="sortName"]')) showFiles(Index.fileList.sort(sortFiles));
				else if($(this).is('input[id="sortType"]')) showFiles(Index.fileList.sort(sortFiles));
				else if($(this).is('input[id="sortSize"]')) showFiles(Index.fileList.sort(sortFiles));
				else if($(this).is('input[id="sortDate"]')) showFiles(Index.fileList.sort(sortFiles));
				else if($(this).is('input[id="sortAuthor"]')) showFiles(Index.fileList.sort(sortFiles));
				else if($(this).is('input[id="descending"]')) showFiles(Index.fileList.sort(sortFiles)); 
			}
		});
	};
	
	var sortFiles = function(a,b) {
		var previous,next;
		if($('input[id="descending"]').is(':checked')) previous = b,next = a;
		else previous = a,next = b;
		if($('input[id="sortName"]').is(':checked')) return previous.name.localeCompare(next.name);
		else if($('input[id="sortType"]').is(':checked')) return previous.type.localeCompare(next.type);
		else if($('input[id="sortSize"]').is(':checked')) return previous.size - next.size;
		else if($('input[id="sortDate"]').is(':checked')) return previous.date.localeCompare(next.date);
		else if($('input[id="sortAuthor"]').is(':checked')) return previous.author.localeCompare(next.author);
	};

	var showFiles = function(files) {
		if($('input[id="viewList"]').is(':checked')) {
			$('#files').text('').append('<table><tbody"></tbody></table>');
			$.each(files, function(i, file) {
				$('<tr class="file"><td class="icon"  style="background-image: url(Theme/small/'+file.type+'.png)"></td><td class="name">'+file.name+'</td><td class="size">'+filesSize(file.size)+'</td><td class="author">'+file.author+'</td><td class="date">'+file.date+'</td><td class="path" style="display: none">'+file.path+'</td><td class="version" style="display: none">'+file.version+'</td></tr>').appendTo('#files > table');
			});
			$('input[id="showName"]').attr('disabled','disabled');
		} else {
			$('#files').text('');
			$.each(files, function(i, file) {
				$('<div class="file"><div class="icon" style="background-image: url(Theme/big/'+file.type+'.png)"></div><div class="name">'+file.name+'</div><div class="size">'+filesSize(file.size)+'</div><div class="author">'+file.author+'</div><div class="date">'+file.date+'</div><div class="path" style="display: none">'+file.path+'</div><div class="version" style="display: none">'+file.version+'</div></div>').appendTo('#files');
			});
			$('input[id="showName"]').removeAttr('disabled','disabled');
		}
		$('#fileinfo').text(files.length > 1 ? files.length + ' files' : files.length + ' file');
	};
	
	var filesSize = function(size) {
		if (size < 1024) {
			size = size.toString() + ' B';
		} else if (size < 1048576) {
			size /= 1024;
			size = parseInt(size).toString() + ' KB';
		} else if (size < 1073741824) {
			size /= 1048576;
			size = parseInt(size).toString() + ' MB';
		} else if (size < 1099511627776) {
			size /= 1073741824;
			size = parseInt(size).toString() + ' GB';
		} else {
			size /= 1099511627776;
			size = parseInt(size).toString() + ' TB';
		}
		return size;
	};
	
	this.getFile = function() {
		var file = {};
		file.path = $('.file.selected > .path').text();
		file.version = $('.file.selected > .version').text();
		return file;
	};
	
	this.getFiles = function() {
		var fileArray = new Array();
		$('.file.selected > .name').each(function() { 
			fileArray.push($(this).text());
		});
		return fileArray;
	};
	
	this.getFilePath = function() {
		var pathArray = new Array();
		$('.file.selected > .path').each(function() { 
			pathArray.push($(this).text());
		});
		return pathArray;
	};
	
	this.getFileVersion = function() {
		var versionArray = new Array();
		$('.file.selected > .version').each(function() { 
			versionArray.push($(this).text());
		});
		return versionArray;
	};
	              
	operateFiles();
	orderFiles();
};

Index.Init = function() {
	this.ManageIndex = new this.IndexManagement();
	this.ManageFile = new this.FileManagement();
	this.MenuManagement = new this.MenuManagement();
	this.SearchManagement();
	this.OptionSelect = new this.OptionManagement();
	this.SettingManagement();
};

$(document).ready(function() {
	Index.Init();
});
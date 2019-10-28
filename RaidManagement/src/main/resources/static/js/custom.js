function jqid( idToLookup ) {
	return "#" + idToLookup.replace( /(:|\.|\[|\]\,|=|@)/g, "\\1$");
}

function partialSubmit(urlForSubmit, formId, divId, successfulSubmit) {
	
	urlForSubmit = rootContext + urlForSubmit;
	
	// update the DIV when a good response is returned
	function onSuccess(response){
		
		$(jqid(divId)).html(response);
		
		if (successfulSubmit){
			successfulSubmit();
		}
	};
	
	// add an onFailure?
	function onFailure(response){
		// TODO add something here
		console.log('Failure:' + response);
	};

	// do the form submit
	postUrl(urlForSubmit, $(jqid(formId)).serialize(), onSuccess, onFailure);
}

function partialGet(urlForGet, divId) {
	
	urlForGet = rootContext + urlForGet;
	
	//console.log('Partial get for ' + urlForGet);
	
	// update the DIV when a good response is returned
	function onSuccess(response){
		$(jqid(divId)).html(response);
	};
	
	// add an onFailure?
	function onFailure(response){
		// TODO add something here
		console.log('Failure:' + response);
	};

	// do the form submit
	getUrl(urlForGet, onSuccess, onFailure);
}

function postUrl(urlForSubmit, data, successMethod, failureMethod){
	
	// do a fancy AJAXy call
	$.ajax({
		method: 'POST',
		url: urlForSubmit,
		data: data,
		success: function(response){
			successMethod(response);
		},
		failure: function(response){
			failureMethod(response);
		},
		timeout: 0 // no timeout.  increase this to something?
	});
}

function getUrl(urlForGet, successMethod, failureMethod){
//	// do a fancy AJAXy call
	$.ajax({
		method: 'GET',
		url: urlForGet,
		success: function(response){
			successMethod(response);
		},
		failure: function(response){
			failureMethod(response);
		},
		timeout: 0 // no timeout.  increase this to something?
	});
}

/*********** Fancy Form Stuff *******/
function addBookToFeed(bookKeyId, addLink, removeLink){
	$.ajax({
		  method: "POST",
		  url: shard.config.rootContext + '/rs/user/preference/addBook',
		  data: { bookKey: bookKeyId + '' }
		});
	if (removeLink!==null){
		$("#" + removeLink).show();
	} else {
		$("#removeBookToFeedLink").show();
	}
	if (addLink!=null){
		$("#" + addLink).hide();
	} else {
		$("#addBookToFeedLink").hide();
	}
}
function removeBookToFeed(bookKeyId, removeLink, addLink){
	$.ajax({
		  method: "POST",
		  url: shard.config.rootContext + '/rs/user/preference/removeBook',
		  data: { bookKey: bookKeyId + '' }
		});
	if (removeLink!==null){
		$("#" + removeLink).hide();
	} else {
		$("#removeBookToFeedLink").hide();
	}
	if (addLink!=null){
		$("#" + addLink).show();
	} else {
		$("#addBookToFeedLink").show();
	}
}
function removeAllBooksFromFeed(){
	$.ajax({
		  method: "POST",
		  url: shard.config.rootContext + '/rs/user/preference/remove',
		  data: { preferenceName: 'BOOK_FEED' }
		});
	
}




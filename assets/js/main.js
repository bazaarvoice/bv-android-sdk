
$(function(){

	// Number all tabs and tab content areas so that we can switch all code areas between swift and objective-c
	var index = 0;
	$('a[href*="swift"]').each(function (e) {
		// number the href
	  $(this).attr('href', $(this).attr('href') + index);
	  // also number the id of the content element
	  var content = $(this).parent().parent().next().find('#swift');
	  content.attr('id', content.attr('id') + index);

	  index += 1
	});

	// Do the same for objective-c
	index = 0;
	$('a[href*="objc"]').each(function (e) {
		// number the href
	  $(this).attr('href', $(this).attr('href') + index);
	  // also number the id of the content element
	  var content = $(this).parent().parent().next().find('#objc');
	  content.attr('id', content.attr('id') + index);

	  index += 1
	});

	// when swift/objective-c tab clicked, swap tabs on each element on screen
	$('a[href*="objc"]').click(function (event) {
	  event.preventDefault();
	  $('a[href*="objc"]').each(function(e){
		$(this).tab('show');
	  });
	});

	$('a[href*="swift"]').click(function (event) {
	  event.preventDefault();
	  $('a[href*="swift"]').each(function(e){
		$(this).tab('show');
	  });
	});

	// Listen for collapsing elements, to animate their arrow
	function listenForCollapseAndRotate(collapsable_element, arrow_element, closed_start, closed_end, open_start, open_end) {
		collapsable_element.on('hide.bs.collapse', function () {

			arrow_element.rotate({
			    angle:open_start,
			    animateTo:open_end,
			    duration:500,
			    easing: $.easing.easeInOutExpo
			  });

		});
		collapsable_element.on('show.bs.collapse', function () {

			arrow_element.rotate({
			    angle:closed_start,
			    animateTo:closed_end,
			    duration:500,
			    easing: $.easing.easeInOutExpo
			  });

		});
	}

	// installation / configuration on all pages
	listenForCollapseAndRotate($('#installationCollapse'), $('#installationArrow'), 0, 90, 90, 360);
	listenForCollapseAndRotate($('#configurationCollapse'), $('#configurationArrow'), 0, 90, 90, 360);

	// sidebar
	listenForCollapseAndRotate($('#curationsDropdown'), $('#curationsDropdownArrow'), 270, 0, 0, 270);
	listenForCollapseAndRotate($('#conversationsDropdown'), $('#conversationsDropdownArrow'), 270, 0, 0, 270);
	listenForCollapseAndRotate($('#bvPixelDropdown'), $('#bvPixelDropdownArrow'), 270, 0, 0, 270);

	// installation page
	listenForCollapseAndRotate($('#installationCocoaPods'), $('#installationCocoaPodsArrow'), 270, 0, 0, 270);
	listenForCollapseAndRotate($('#installationHeaderFiles'), $('#installationHeaderFilesArrow'), 270, 0, 0, 270);
	listenForCollapseAndRotate($('#installationBVSDKManager'), $('#installationBVSDKManagerArrow'), 270, 0, 0, 270);
	listenForCollapseAndRotate($('#manualInstallation'), $('#manualInstallationArrow'), 270, 0, 0, 270);


});


// NOTE: This doesn't currently work on a page reload. e.g. http://foo.com#target, will not load the target.
// It only handles the user click interactions.
function navigateTab(obj) {

  // Handle switching tabs on an anchor and setting the window location to selected tab
  target = obj.getAttribute("href")
  if (target.match('#')) {

    $('.nav-tabs a[href="#' + target.split('#')[1] + '"]').tab('show');
    window.location = target;
  } else {
  	console.log('no match')
  }
}


function setInstallationText(module) {

	var text = "pod 'BVSDK'\n";
	switch(module) {
		case "recommendations":
			text += "pod 'BVSDK/BVRecommendations"
			break;
		case "curations":
			text += "pod 'BVSDK/BVCurations'\n"
			text += "pod 'SDWebImage' # Used to load images asynchronously"
			break;
		case 'conversations':
			break;
		case 'bv_pixel':
			break;
	}
	$('#installation_placeholder').text(text);

	$('pre code').each(function(i, block) {
		hljs.highlightBlock(block);
	});
}

/*
Author: mg12
Feature: MenuList
Update: 2009/12/13
Tutorial URL: http://www.neoease.com/wordpress-menubar-6/
*/

jQuery(document).ready(function(){
	jQuery('#menus > li').each(function(){
		jQuery(this).hover(

			function(){
				jQuery(this).find('ul:eq(0)').show();
			},

			function(){
				jQuery(this).find('ul:eq(0)').hide();
			}

		);
	});
});
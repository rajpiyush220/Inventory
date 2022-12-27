/*!
    * Start Bootstrap - SB Admin v6.0.2 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2020 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    (function($) {
    "use strict";

    // Add active state to sidbar nav links
    var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
        $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
            if (this.href === path) {
                $(this).addClass("active");
            }
        });

    // Toggle the side navigation
    $("#sidebarToggle").on("click", function(e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });
})(jQuery);

function getBasePath(){
        var context = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
        return window.location.protocol+"//"+ window.location.host +context;
}

function build_sort_url(endpoint,size,number,order,column,key,type){
    var url = getBasePath().concat("/").concat(endpoint);
    if(size > 0 || number > 0 || order.length > 0 || column.length > 0 || key.length > 0 || type.length > 0){
        url = url.concat("?");
    }
    if(size > 0){
        url = url.concat("&size=").concat(size);
    }
    if(number > 0){
        url = url.concat("&page=").concat(number);
    }
    if(order.length > 0){
        url = url.concat("&sortOrder=").concat(order);
    }
    if(column.length > 0){
        url = url.concat("&sortColumn=").concat(column);
    }
    if(key.length > 0){
        url = url.concat("&searchKey=").concat(key);
    }
    if(type.length > 0){
        url = url.concat("&searchType=").concat(type);
    }
    return url;
}

function logoutUser() {
    $('#logoutForm').submit();
}


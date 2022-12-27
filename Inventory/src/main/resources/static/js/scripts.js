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

function build_sort_url(endpoint,size,number,order,column){
    return getBasePath() +"/"+endpoint+"?size="+size+"&page="+number+"&sortOrder="+order+"&sortColumn="+column;
}


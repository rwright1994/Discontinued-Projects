$(document).ready(function(){

    $('.js--mobile-navi-icon').click(function(){
        var nav = $('.js--main-navi');
        var icon = $('.js--mobile-navi-icon i');

        
        console.log('clicked');
        if(nav.is(':visible')){
            nav.css("display","")
            console.log('Now hidden');
        }else{
            nav.slideToggle();
            console.log('Now visible');
        }
        icon.removeClass('.ion-navicon-round');
        icon.addClass('.ion-close-round');
    });
});
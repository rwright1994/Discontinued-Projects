
$(document).ready(function() {

 var sideMenu = false;
  document.getElementById 
  $(".tab").click(function(e) { 
  
  if (!sideMenu) {
  $(this).closest("#tab").animate({left: "80px"});
   sideMenu = true; 
   }else{ 
   $(this).closest("#tab").animate({left: "0px"});
    sideMenu = false; }
    }); 
    }); 

  function popTabOut(evt, tabName){

        var i,tabContent,tab;

        tabContent = document.getElementsByClassName("tabContent");
        for(i = 0; i < tabContent.length; i++){
          tabContent[i].style.display = "none";
        }

        tab = document.getElementsByClassName("tab");
        for(i=0; i < tab.length; i++){
          tab[i].className = tab[i].className.replace(" active", "");
        }
        document.getElementById(tabName).style.display = "block";
        evt.currentTarget.className +=" active";
    }
       // document.getElementById("defaultcontent").click();
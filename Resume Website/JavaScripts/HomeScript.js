$(document).ready(function(){
	$("#myCarousel").on('slide.bs.carousel', function () {
   		$("#carousel-content").text("Thanks boys")
	});
});






/*Have text fade in/out one carousel slide change*/
$(document).ready(function(){
	
	var currentSlide = $('div.active').index()+1;
	

		$('#myCarousel').bind('slid.bs.carousel', function(){

			currentSlide = $('div.active').index() +1;
			console.log(currentSlide);

			if(currentSlide === 1){
				$(".carousel-desc").fadeOut(function(){
					$(this).text("Aspiring to be a sucessful business man and software developer with experience in HTML,CSS, JavaScript and other programming languages.");
					
				}).fadeIn();
				$("#carousel-link").fadeOut(function(){
					$(this).text("About");
				}).fadeIn();
				$('#carousel-link').attr('href', "About.html");
				
			}else if(currentSlide === 2){
				$(".carousel-desc").fadeOut(function(){
					$(this).text("Studied Computer Science and Business Administration at the University Of New Brunswick.");
				}).fadeIn();
				$("#carousel-link").fadeOut(function(){
					$(this).text("Resume");
				}).fadeIn();
				$('#carousel-link').attr('href', "Resume.html");
			}else{
				$(".carousel-desc").fadeOut(function(){
					$(this).text("Created serveral sample programs to showcase various concepts such as, Object-Orintated Desing, Model View Controller and other design patterns.")
				}).fadeIn();
					$("#carousel-link").fadeOut(function(){
					$(this).text("Programs");
				}).fadeIn();
				$('#carousel-link').attr('href', "Programs.html");
			}

		});
	
});

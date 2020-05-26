
function startGame(){
  var canvas = document.getElementById("gameBoard");
  document.getElementById("gameBoard").addEventListener('click', reveal);

  canvas.width = 600;
  canvas.height= 600;
  var ctx = canvas.getContext("2d");
  var blockWidth = (canvas.width - 2) / 8;
  var blockHeight = (canvas.height - 2 )/ 8;

  var i;
  var j;
  for( i = 0; i < 8; i++){
    for(j=0; j < 8; j++){
      ctx.fillRect(blockWidth * i +1, blockHeight * j +1, 74,74);
      ctx.stroke();

    }
  }
}

function reveal(){
  console.log("Clicked canvas.");
}

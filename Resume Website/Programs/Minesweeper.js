var board = [];
var difficulty = "easy";
var gameOver = false;

var blockWidth;
var blockHeight

var mineImg;



function startGame(){

  var canvas = document.getElementById("gameBoard");
  document.getElementById("gameBoard").addEventListener('click', reveal);

  canvas.width = 600;
  canvas.height= 600;

  gameOver = false;

  let spaces;
  let minesToPlace;

  if(difficulty == "easy"){
    spaces = 8;
    minesToPlace = 10;
    initBoard(spaces,minesToPlace);
  }

  let ctx = canvas.getContext("2d");
  blockWidth = canvas.width / spaces;
  blockHeight = canvas.height/ spaces;


  let i;
  let j;
  for( i = 0; i < 8; i++){
    for(j=0; j < 8; j++){
      ctx.fillRect(blockWidth * i +1, blockHeight * j +1, blockWidth-1,blockHeight-1);
      ctx.stroke();
    }
  }
}

function reset(){

  let canvas = document.getElementById("gameBoard");
  let ctx = canvas.getContext("2d");


  ctx.clearRect(0, 0, canvas.width, canvas.height);

  clearBoard();
  startGame();
}

function initBoard(spaces, minesToPlace){

  let x;
  let y;

  for(x = 0; x < spaces; x++){
    board.push([0]);
    for(y = 0; y < spaces-1; y++){
      board[x].push(0);
    }
  }

  placeMines(minesToPlace);
  initResources();
}

function initResources(){

  mineImg = new Image();
  mineImg.src = "Resources/Mine.png"

}

function reveal(e){

  let canvas = document.getElementById("gameBoard");
  let ctx = canvas.getContext("2d");

  xMousePos = Math.floor(e.offsetX/74);
  yMousePos = Math.floor(e.offsetY/74);

  console.log("x = " + xMousePos + " & " + "y = " + yMousePos);

  if(xMousePos >= 0  && yMousePos >= 0 && gameOver == false){
    if(board[xMousePos][yMousePos] == 9){
      gameOver = true;
      ctx.fillStyle = "#FF0000";
      ctx.drawImage(mineImg, blockWidth * xMousePos + 1, blockHeight * yMousePos + 1, blockWidth - 1, blockHeight - 1);


      console.log(board[xMousePos][yMousePos]);
      console.log("Gameover");
    }else{
      console.log(board[xMousePos][yMousePos]);
      uncoverSpace();
    }
  }

}

function uncoverSpace(xMousePos, yMousePos){

}

function clearBoard(){

  let x;
  let y;

  for(x = 0; x < board.length; x++){
    for(y = 0; y < board.length; y++){
      board[x][y] = 0;
    }
  }
}

function placeMines(minesToPlace){

  let x;
  for(x = 0; x < minesToPlace; x++){
    let xPos = Math.floor(Math.random() * 8);
    let yPos = Math.floor(Math.random() * 8);
    if(board[xPos][yPos] != 9){
      board[xPos][yPos] = 9;
      console.log("Mine placed @ xPos: " + xPos + " yPos: " + yPos);
    }else{
      x--;
    }
  }

}

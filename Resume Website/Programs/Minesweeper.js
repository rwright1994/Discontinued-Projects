//game settings
var board = [];
var difficulty = "easy";
var gameOver = false;

//game information.
var flagCounterDisplay;
var flags;

var timerDisplay;
var timer;
var seconds = 0;


//block dimensions.
var blockWidth;
var blockHeight

//block img resoures.
var mineImg;
var flagImg;
var numberImgs = [];


function startGame(){

  let canvas = document.getElementById("gameBoard");
  document.getElementById("gameBoard").addEventListener('click', reveal);
  document.getElementById("gameBoard").addEventListener('contextmenu', placeFlag);
  flagCounterDisplay = document.getElementById("flag-counter");
  timerDisplay = document.getElementById("timer");


  canvas.width = 600;
  canvas.height= 600;

  gameOver = false;

  let spaces;
  let minesToPlace;

  if(difficulty == "easy"){
    spaces = 8;
    minesToPlace = 10;
    flags = 10;
    initBoard(spaces,minesToPlace);
    flagCounterDisplay.innerHTML = "Flags: " + flags;
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
  timerDisplay.innerHTML = "Timer: 0";
  seconds = 0;
  timer = setInterval(incrementTimer, 1000);

}

//Clears game board and calls startGame.
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
  flagImg = new Image();
  flagImg.src = "Resources/Flag.png"

  let i;

  for( i = 0; i < 8; i++){
    numberImgs.push(new Image());
    numberImgs[i].src = "Resources/" + (i+1) + "-large.png";
    console.log(numberImgs[i].src);
  }
}

function incrementTimer(){
  seconds += 1;
  timerDisplay.innerHTML = "Time: " + seconds;
}

function reveal(e){

  let canvas = document.getElementById("gameBoard");
  let ctx = canvas.getContext("2d");

  xPos = Math.floor(e.offsetX/74);
  yPos = Math.floor(e.offsetY/74);

  if(xPos >= 0  && yPos >= 0 && gameOver == false){
    if(board[xPos][yPos] == 9){
      gameOver = true;

      ctx.drawImage(mineImg, blockWidth * xPos + 1, blockHeight * yPos + 1, blockWidth - 1, blockHeight - 1);
    }else{
      uncoverSpace(xPos,yPos);
    }
  }

}

function uncoverSpace(xPos, yPos){
  if(board[xPos][yPos] == 0){

  }
}

function countMines(xPos, yPos){
    let count = 0;

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
    }else{
      x--;
    }
  }
}

function placeFlag(e){

  let canvas = document.getElementById("gameBoard");
  let ctx = canvas.getContext("2d");

  xPos = Math.floor(e.offsetX/74);
  yPos = Math.floor(e.offsetY/74);



  if(board[xPos][yPos] >= 0 && board[xPos][yPos] <= 8 && flags > 0 && gameOver == false){
    board[xPos][yPos] += 10;
    ctx.drawImage(flagImg, blockWidth * xPos + 1, blockHeight * yPos + 1, blockWidth - 1, blockHeight - 1)
    flags--;
  }else if(board[xPos][yPos] == 9 && flags > 0 && gameOver == false){
    board[xPos][yPos] += 10;
    ctx.drawImage(flagImg, blockWidth * xPos + 1, blockHeight * yPos + 1, blockWidth - 1, blockHeight - 1)
    flags--;
  }else if(board[xPos][yPos] > 9 && gameOver == false){
    board[xPos][yPos] -= 10;
    ctx.clearRect(blockWidth * xPos +1, blockHeight * yPos +1, blockWidth-1,blockHeight-1)
    ctx.fillRect(blockWidth * xPos +1, blockHeight * yPos +1, blockWidth-1,blockHeight-1);
    ctx.stroke();
    flags++;
  }
    flagCounterDisplay.innerHTML = "Flags: " + flags;
}

//game settings
var board = [];
var difficulty;
var gameOver = false;

//game information.
var flagCounterDisplay;
var flags;

var timerDisplay;
var timer;
var seconds;
var covered;


//block dimensions.
var blockWidth;
var blockHeight;

//block img resoures.
var mineImg;
var flagImg;
var flaggedMine;
var numberImgs = [];



//Starts the game on the given difficulty.
function startGame(){

  let canvas = document.getElementById("gameBoard");
  document.getElementById("gameBoard").addEventListener('click', onClick);
  document.getElementById("gameBoard").addEventListener('contextmenu', placeFlag);
  let difficultySelector = document.getElementById("difficulty-selector");
  difficulty = difficultySelector.options[difficultySelector.selectedIndex].text;
  console.log(difficulty);

  flagCounterDisplay = document.getElementById("flag-counter");
  timerDisplay = document.getElementById("timer");


  canvas.width = 600;
  canvas.height= 600;

  gameOver = false;

  let spaces;
  let minesToPlace;

  if(difficulty == "Easy"){
    spaces = 8;
    minesToPlace = 10;
    flags = 10;
    covered = spaces * spaces - minesToPlace;
    initBoard(spaces, minesToPlace);
  }else if(difficulty == "Medium"){

    spaces = 14;
    minesToPlace = 40;
    flags = 40;
    initBoard(spaces, minesToPlace);

  }else if(difficulty == "Hard"){
    spaces = 20;
    minesToPlace = 99;
    flags = 99;
    initBoard(spaces, minesToPlace);
  }


  let ctx = canvas.getContext("2d");
  blockWidth = canvas.width / spaces;
  blockHeight = canvas.height/ spaces;


  let i;
  let j;
  for( i = 0; i < spaces; i++){
    for(j = 0; j < spaces; j++){
      ctx.fillRect(blockWidth * i + 1, blockHeight * j + 1, blockWidth - 1,blockHeight - 1);
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
  board = [];

  clearInterval(timer);
  startGame();
}

//initalizes the game board.
function initBoard(spaces, minesToPlace){

  let x;
  let y;

  for(x = 0; x < spaces; x++){
    board.push([0]);
    for(y = 0; y < spaces - 1; y++){
      board[x].push(0);
    }
  }
  flagCounterDisplay.innerHTML = "Flags: " + flags;
  placeMines(spaces, minesToPlace);
  initResources();

}

//Initalize game resources for drawing.
function initResources(){

  mineImg = new Image();
  mineImg.src = "Resources/Mine.png";
  flagImg = new Image();
  flagImg.src = "Resources/Flag.png";
  flaggedMine = new Image();
  flaggedMine.src = "Resources/Flagged-Mine.png";

  let i;

  for( i = 0; i < 8; i++){
    numberImgs.push(new Image());
    numberImgs[i].src = "Resources/" + (i+1) + "-large.png";
    console.log(numberImgs[i].src);
  }
}

//On click get mouse position and call reveal.
function onClick(e){

  xPos = Math.floor(e.offsetX / blockWidth);
  yPos = Math.floor(e.offsetY / blockHeight);

  reveal(xPos, yPos);

}


//increments game timer.
function incrementTimer(){
  seconds += 1;
  timerDisplay.innerHTML = "Time: " + seconds;
}

//Reveals a given space.
function reveal(xPos, yPos){

  if(xPos >= 0  && yPos >= 0 && gameOver == false && covered > 0){

    if(board[xPos][yPos] == 9){
      gameOver = true;
      revealMines();
    }else if(board[xPos][yPos] == 0){
      uncoverSpace(xPos,yPos);
    }
  }
}

//Called when player clicks on a spaces. (called recursivly)
function uncoverSpace(xPos, yPos){

  let ctx = document.getElementById("gameBoard").getContext("2d");

  if(board[xPos][yPos] >= 10 && board[xPos][yPos] < 19){
    flags++;
  }

  if(board[xPos][yPos] != -1 ){
      covered--;
      if(countMines(xPos,yPos) == 0){

        ctx.clearRect(blockWidth * xPos +1, blockHeight * yPos +1, blockWidth-1,blockHeight-1);
        ctx.fillStyle = "#2e2b2b";
        ctx.fillRect(blockWidth * xPos +1, blockHeight * yPos +1, blockWidth-1,blockHeight-1);

        board[xPos][yPos] = -1;
          if(xPos - 1 >= 0){ //recurse left
            uncoverSpace(xPos-1,yPos);
          }
          if(xPos + 1 <= 7){ //recurse right
            uncoverSpace(xPos+1,yPos);
          }
          if(yPos - 1 >= 0){ //recurse up
            uncoverSpace(xPos,yPos-1);
          }
          if(yPos + 1 <= 7){ //recurse down
            uncoverSpace(xPos,yPos+1);
          }
      }else if(countMines(xPos,yPos) > 0){ //replace tile with numbered tile. Do not recurse
        ctx.drawImage(numberImgs[countMines(xPos,yPos)-1] ,blockWidth * xPos + 1, blockHeight * yPos + 1, blockWidth - 1, blockHeight - 1);
        board[xPos][yPos] = -1;
      }
  }
  flagCounterDisplay.innerHTML = "Flags: " + flags;
}

//Count the number of mines around a given tile.
function countMines(xPos, yPos){
    let count = 0;

    if(xPos - 1 >= 0){ //left
      if(board[xPos-1][yPos] == 9 || board[xPos-1][yPos] == 19){
          count++;
      }
    }
    if(xPos - 1 >= 0 && yPos - 1 >= 0){// top-left
      if(board[xPos-1][yPos-1] == 9 || board[xPos-1][yPos-1] == 19){
        count++;
      }
    }
    if(yPos - 1 >= 0){ //top
      if(board[xPos][yPos-1] == 9 || board[xPos][yPos-1] == 19){
        count++;
      }
    }
    if(xPos + 1 <= 7 && yPos - 1 >= 0){ //top-right
      if(board[xPos+1][yPos-1] == 9 || board[xPos+1][yPos-1] == 19){
        count++;
      }
    }
    if(xPos+1 <= 7){ //right
      if(board[xPos+1][yPos] == 9 || board[xPos+1][yPos] == 19){
      count++;
      }
    }
    if(xPos +1 <= 7 && yPos+1 <= 7){ //bottom-right
      if(board[xPos+1][yPos+1] == 9 || board[xPos+1][yPos+1] == 19){
        count++;
      }
    }
    if(yPos + 1 <= 7){ //Bottom
      if(board[xPos][yPos+1] == 9 || board[xPos][yPos+1] == 19){
        count++;
      }
    }
    if(xPos - 1 >= 0 && yPos + 1 <= 7){
      if(board[xPos-1][yPos+1] == 9 || board[xPos-1][yPos+1] == 19){
        count++;
      }
    }
    return count;
}

//reveal all mines on the game board.
function revealMines(){

  let i;
  let j;

  let canvas = document.getElementById("gameBoard");
  let ctx = canvas.getContext("2d");

  for(i = 0; i < board.length; i++){
    for(j = 0; j < board[i].length; j++){
      if(board[i][j] == 9 ){
        ctx.drawImage(mineImg, blockWidth * i + 1, blockHeight * j + 1, blockWidth - 1, blockHeight - 1);
      }
      if(board[i][j] == 19){
        ctx.drawImage(flaggedMine,blockWidth * i + 1, blockHeight * j + 1, blockWidth - 1, blockHeight - 1);
      }
    }
  }
}

//Places mines randomly around the board
function placeMines(spaces, minesToPlace){

  let x;
  for(x = 0; x < minesToPlace; x++){
    let xPos = Math.floor(Math.random() * spaces);
    let yPos = Math.floor(Math.random() * spaces);
    if(board[xPos][yPos] != 9){
      board[xPos][yPos] = 9;
    }else{
      x--;
    }
  }
}

//Called when player right clicks to place a flag.
function placeFlag(e){

  let canvas = document.getElementById("gameBoard");
  let ctx = canvas.getContext("2d");

  xPos = Math.floor(e.offsetX/blockWidth);
  yPos = Math.floor(e.offsetY/blockHeight);



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
    ctx.clearRect(blockWidth * xPos +1, blockHeight * yPos +1, blockWidth-1,blockHeight-1);
    ctx.fillStyle = "#000000"
    ctx.fillRect(blockWidth * xPos +1, blockHeight * yPos +1, blockWidth-1,blockHeight-1);
    ctx.stroke();
    flags++;
  }
    flagCounterDisplay.innerHTML = "Flags: " + flags;
}

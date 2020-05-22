var gameArea ={
    canvas : document.createElement("canvas"),
    start: fuction(){
      this.canvas.width = 800;
      this.canvas.height = 800;
      this.context = this.canvas.getContext("2d")
      document.body.insertBefore(this.canvas, document.body.childNodes[0]);
    }
}


function startGame(){
  gameArea.start();
}

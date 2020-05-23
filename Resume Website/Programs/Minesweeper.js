window.onload = function() {
    var canvas = document.getElementById("gameBoard");
    var ctx = canvas.getContext("2d");
    ctx.fillStyle = "#FF0000";
    ctx.fillRect(0, 0, 150, 75);
    moveCanvas();
};

function moveCanvas(){
    document.getElementById("gameBoard").style.backgroundColor = "blue";
    document.getElementById("gameBoard").style.left = "-100px";
}

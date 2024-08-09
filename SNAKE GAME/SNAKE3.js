const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const tileSize = 20;
const width = canvas.width;
const height = canvas.height;
const numTilesX = width / tileSize;
const numTilesY = height / tileSize;

let snake = [{ x: 10, y: 10 }];
let food = { x: 15, y: 15 };
let direction = 'RIGHT';
let gameOver = false;
let score = 0;
let highScore = localStorage.getItem('highScore') || 0;

const retryButton = document.getElementById('retryButton');
retryButton.addEventListener('click', resetGame);

function draw() {
    ctx.clearRect(0, 0, width, height);

    if (gameOver) {
        ctx.fillStyle = 'white';
        ctx.font = '30px Arial';
        ctx.fillText('Game Over', width / 4, height / 2);
        ctx.font = '20px Arial';
        ctx.fillText(`Score: ${score}`, width / 4, height / 2 + 40);
        ctx.fillText(`High Score: ${highScore}`, width / 4, height / 2 + 80);

        // Show Retry Button
        retryButton.style.display = 'block';
        return;
    }

    ctx.fillStyle = 'green';
    snake.forEach(segment => {
        ctx.fillRect(segment.x * tileSize, segment.y * tileSize, tileSize, tileSize);
    });

    ctx.fillStyle = 'red';
    ctx.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

    ctx.fillStyle = 'white';
    ctx.font = '20px Arial';
    ctx.fillText(`Score: ${score}`, 10, 20);
}

function update() {
    if (gameOver) return;

    let head = { ...snake[0] };

    switch (direction) {
        case 'LEFT': head.x--; break;
        case 'RIGHT': head.x++; break;
        case 'UP': head.y--; break;
        case 'DOWN': head.y++; break;
    }

    if (head.x < 0 || head.x >= numTilesX || head.y < 0 || head.y >= numTilesY || snake.some(segment => segment.x === head.x && segment.y === head.y)) {
        gameOver = true;
        if (score > highScore) {
            highScore = score;
            localStorage.setItem('highScore', highScore);
        }
        return;
    }

    snake.unshift(head);

    if (head.x === food.x && head.y === food.y) {
        score++;
        spawnFood();
    } else {
        snake.pop();
    }
}

function spawnFood() {
    food = {
        x: Math.floor(Math.random() * numTilesX),
        y: Math.floor(Math.random() * numTilesY),
    };
    while (snake.some(segment => segment.x === food.x && segment.y === food.y)) {
        food = {
            x: Math.floor(Math.random() * numTilesX),
            y: Math.floor(Math.random() * numTilesY),
        };
    }
}

function keyDown(e) {
    switch (e.keyCode) {
        case 65: if (direction !== 'RIGHT') direction = 'LEFT'; break; // A
        case 87: if (direction !== 'DOWN') direction = 'UP'; break;   // W
        case 68: if (direction !== 'LEFT') direction = 'RIGHT'; break; // D
        case 83: if (direction !== 'UP') direction = 'DOWN'; break;   // S
    }
}

function resetGame() {
    snake = [{ x: 10, y: 10 }];
    food = { x: 15, y: 15 };
    direction = 'RIGHT';
    gameOver = false;
    score = 0;
    retryButton.style.display = 'none'; 
    draw(); 
}

document.addEventListener('keydown', keyDown);

setInterval(() => {
    if (!gameOver) {
        update();
        draw();
    }
}, 100);
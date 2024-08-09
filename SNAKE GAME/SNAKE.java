import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int NUM_TILES_X = WIDTH / TILE_SIZE;
    private final int NUM_TILES_Y = HEIGHT / TILE_SIZE;
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private int direction = KeyEvent.VK_RIGHT;
    private boolean gameOver = false;
    private final Timer timer = new Timer(100, this);

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int newDirection = e.getKeyCode();
                if ((newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                    (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) ||
                    (newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                    (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP)) {
                    direction = newDirection;
                }
            }
        });
        initGame();
        timer.start();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(NUM_TILES_X / 2, NUM_TILES_Y / 2));
        spawnFood();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(NUM_TILES_X), rand.nextInt(NUM_TILES_Y));
        while (snake.contains(food)) {
            food = new Point(rand.nextInt(NUM_TILES_X), rand.nextInt(NUM_TILES_Y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
            return;
        }
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }
        moveSnake();
        checkCollision();
        checkFood();
        repaint();
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = (Point) head.clone();
        switch (direction) {
            case KeyEvent.VK_LEFT -> newHead.x--;
            case KeyEvent.VK_RIGHT -> newHead.x++;
            case KeyEvent.VK_UP -> newHead.y--;
            case KeyEvent.VK_DOWN -> newHead.y++;
        }
        snake.add(0, newHead);
        snake.remove(snake.size() - 1);
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= NUM_TILES_X || head.y < 0 || head.y >= NUM_TILES_Y || snake.subList(1, snake.size()).contains(head)) {
            gameOver = true;
        }
    }

    private void checkFood() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            snake.add(0, new Point(food));
            spawnFood();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
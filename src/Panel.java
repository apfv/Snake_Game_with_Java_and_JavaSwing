import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.JPanel;

import java.util.Random;
import java.util.ArrayList;

public class Panel extends JPanel implements ActionListener {
    private boolean gameOver;
    private final Vector food;
    private final Sound sound;
    private final Random random;
    private Direction direction;
    private final int UNIT_SIZE;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private final ArrayList<Vector> snakeBody;
    private final ArrayList<Effect> effects;

    public Panel() {

        gameOver = true;
        food = new Vector(0, 0);
        direction = Direction.RIGHT;
        UNIT_SIZE = 20;
        SCREEN_WIDTH = 500;
        SCREEN_HEIGHT = 500;

        sound = new Sound();
        random = new Random();
        snakeBody = new ArrayList<>();
        effects = new ArrayList<>();


        this.setFocusable(true);
        this.setBackground(new Color(50, 50, 50));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != Direction.RIGHT) {
                            direction = Direction.LEFT;
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != Direction.LEFT) {
                            direction = Direction.RIGHT;
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != Direction.DOWN) {
                            direction = Direction.UP;
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != Direction.UP) {
                            direction = Direction.DOWN;
                        }
                    }
                    case KeyEvent.VK_ENTER -> {
                        if (gameOver) {
                            startGame();
                        }
                    }
                }
            }
        });

        startGame();

        new Timer(100, this).start();
    }

    private void startGame() {

        snakeBody.clear();
        snakeBody.add(new Vector(random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE, random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE));
        newFood();
        gameOver = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {
            move();
            checkFood();
            checkCollisions();
        }

        if (!effects.isEmpty()) {

            for (int i = 0; i < effects.size(); i++) {

                Effect effect = effects.get(i);

                effect.update();

                if (!effect.check()) {
                    effects.remove(effect);
                }
            }
        }

        repaint();
    }

    private void newFood() {

        int x;
        int y;

        while (true) {

            x = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            y = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

            boolean b = true;

            for (Vector vector : snakeBody) {
                if ((x == vector.getX()) && (y == vector.getY())) {
                    b = false;
                    break;
                }
            }

            if (b) {
                break;
            }
        }

        food.setX(x);
        food.setY(y);
    }

    private void move() {

        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).setX(snakeBody.get(i - 1).getX());
            snakeBody.get(i).setY(snakeBody.get(i - 1).getY());
        }

        switch (direction) {
            case UP -> snakeBody.get(0).setY(snakeBody.get(0).getY() - UNIT_SIZE);
            case DOWN -> snakeBody.get(0).setY(snakeBody.get(0).getY() + UNIT_SIZE);
            case LEFT -> snakeBody.get(0).setX(snakeBody.get(0).getX() - UNIT_SIZE);
            case RIGHT -> snakeBody.get(0).setX(snakeBody.get(0).getX() + UNIT_SIZE);
        }

        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            if (snakeBody.get(i).getX() < 0) {
                snakeBody.get(i).setX(SCREEN_WIDTH);
            }

            if (snakeBody.get(i).getY() < 0) {
                snakeBody.get(i).setY(SCREEN_HEIGHT);
            }

            if (snakeBody.get(i).getX() > SCREEN_WIDTH) {
                snakeBody.get(i).setX(0);
            }

            if (snakeBody.get(i).getY() > SCREEN_HEIGHT) {
                snakeBody.get(i).setY(0);
            }
        }
    }

    private void checkFood() {
        if ((snakeBody.get(0).getX() == food.getX()) && (snakeBody.get(0).getY() == food.getY())) {

            snakeBody.add(new Vector(0, 0));

            effect(food.getX(), food.getY());

            sound.soundDestroy();

            newFood();
        }
    }

    private void checkCollisions() {

        if (snakeBody.size() > 1 ) {
            for (int i = snakeBody.size() - 1; i > 0; i--) {
                if ((snakeBody.get(0).getX() == snakeBody.get(i).getX()) && (snakeBody.get(0).getY() == snakeBody.get(i).getY())) {

                    gameOver = true;

                    effect(snakeBody.get(0).getX(), snakeBody.get(0).getY());

                    sound.soundDestroy();

                    break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {

            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics fontMetrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (SCREEN_WIDTH - fontMetrics.stringWidth("Game Over")) / 2 , SCREEN_HEIGHT / 2);

        } else {

            g.setColor(Color.RED);
            g.fillOval(food.getX(), food.getY(), UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < snakeBody.size(); i++) {

                if (i == 0) {
                    g.setColor(new Color(0, 255, 0));
                } else {
                    g.setColor(new Color(0, 170, 0));
                }

                g.fillRect(snakeBody.get(i).getX(), snakeBody.get(i).getY(), UNIT_SIZE, UNIT_SIZE);
            }
        }

        if (!effects.isEmpty()) {
            for (Effect effect : effects) {
                effect.draw((Graphics2D) g);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(getFont().deriveFont(Font.BOLD, 16));
        FontMetrics fontMetrics = getFontMetrics(g.getFont());
        g.drawString("Score : " + snakeBody.size(), (fontMetrics.stringWidth("Score : " + snakeBody.size()) - UNIT_SIZE * 3) / 2 , UNIT_SIZE / 5 + g.getFont().getSize());
    }

    private void effect(double x, double y) {

        effects.add(new Effect(x, y, 5, 10, 100, 5f, new Color(4, 142, 227)));
        effects.add(new Effect(x, y, 10, 5, 100, 6f, new Color(18, 253, 1)));
        effects.add(new Effect(x, y, 5, 10, 100, 7f, new Color(243, 238, 0)));
        effects.add(new Effect(x, y, 10, 5, 100, 8f, new Color(199, 15, 15)));
        effects.add(new Effect(x, y, 5, 10, 100, 9f, new Color(169, 34, 182)));
        effects.add(new Effect(x, y, 10, 5, 100, 10f, new Color(255, 116, 0)));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 120;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts;
    int applesEaten;
    int appleX;
    int appleY;

    char direction;
    boolean running = false;
    boolean gameStarted = false;

    Timer timer;
    Random random;

    JButton startButton;
    JButton restartButton;

    GamePanel() {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setLayout(null);

        createButtons();
        resetGame();
    }

    private void createButtons() {

        startButton = new JButton("Start Game");
        startButton.setBounds(220, 260, 160, 40);
        startButton.addActionListener(e -> startGame());
        this.add(startButton);

        restartButton = new JButton("Restart");
        restartButton.setBounds(220, 320, 160, 40);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false);
        this.add(restartButton);

        this.addKeyListener(new MyKeyAdapter());
    }

    private void resetGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    }

    public void startGame() {

        resetGame();
        newApple();

        running = true;
        gameStarted = true;

        startButton.setVisible(false);
        restartButton.setVisible(false);

        this.requestFocusInWindow();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void restartGame() {
        startGame();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        // BEFORE GAME START
        if (!gameStarted) {

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("SNAKE GAME", 160, 200);

            startButton.setVisible(true);
            restartButton.setVisible(false);
            return;
        }

        // GAME RUNNING
        if (running) {

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {

                if (i == 0)
                    g.setColor(Color.green);
                else
                    g.setColor(new Color(45, 180, 0));

                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Score: " + applesEaten, 220, 40);
        }

        // GAME OVER
        else {

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 140, 220);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Score: " + applesEaten, 240, 270);

            restartButton.setVisible(true);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH ||
            y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running && timer != null) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }
        }
    }
}
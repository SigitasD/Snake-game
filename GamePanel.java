import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;
public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    final int x[] = new int[NUMBER_OF_UNITS];
    final int y[] = new int[NUMBER_OF_UNITS];

    int length = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        addFood();
        running = true;
        timer = new Timer(80, this);
        timer.start();
    }

    public void  paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void move(){
        for (int i = length; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if (direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        } else if (direction == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else {
            y[0] = y[0] + UNIT_SIZE;
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            addFood();
        }
    }
    public void draw(Graphics g) {
        if (running) {
            g.setColor(new Color(255, 0, 255));
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
            //g.setColor(Color.white);
            //g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < length; i++) {
                g.setColor(Color.black);
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            g.setColor(Color.white);
            g.setFont(new Font("Times New Roman", Font.BOLD, 15));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: +" + foodEaten, (WIDTH - metrics.stringWidth("Score: +" + foodEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int)(WIDTH/ UNIT_SIZE))* UNIT_SIZE;
        foodY = random.nextInt((int)(HEIGHT/ UNIT_SIZE))* UNIT_SIZE;
    }
    public void checkHit() {
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]){
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.BOLD, 15));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", Font.BOLD, 15));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: +" + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten))/2, g.getFont().getSize());
    }

    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkHit();
            checkFood();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

    // declare variables

    // screen dimensions
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;  // how big objects are in the game
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;

    static final int DELAY = 75; // higher delay makes game slower

    // snake body coordinates
    final int x[] = new int[GAME_UNITS];  // declares array of size GAME_UNITS, default val is 0
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;  // no. of snake body parts

    // game info
    int applesEaten;
    int appleX; // x coordinates of apple
    int appleY; // y coordinates of apple
    char direction = 'R';
    boolean isRunning = false;

    Timer timer;
    Random random;

    // constructor
    public GamePanel(){
        this.random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    public void startGame(){
        newApple();
        isRunning = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(isRunning) {
            // draw grid
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            // draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draw snake
            for (int i = 0; i < bodyParts; i++) {
                // draw head of snake in diff color
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        else{
            gameOver(g);
        }
    }

    public void move(){

        // move the whole snake
        for(int i=bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        // change direction
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;  // moves snake's head up by 1 unit
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void newApple(){
        // generate new apple randomly on grid

        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void checkApple(){

        // check if head of snake reaches apple
        if (x[0]==appleX && y[0]==appleY){
            bodyParts ++;
            applesEaten ++;
            newApple();
        }
    }

    public void checkCollisions(){

        // check if head of snake hit any of the boundaries
        boolean hitBoundary = x[0] > SCREEN_WIDTH || x[0] < 0 || y[0] > SCREEN_HEIGHT || y[0] < 0;

        // check if snake hits ownself
        boolean hitSelf = false;
        for (int i=bodyParts; i>0; i--){
            if (x[0]==x[i] && y[0]==y[i]){
                hitSelf = true;
                break;
            }
        }

        if (hitBoundary || hitSelf){
            isRunning = false;
            timer.stop();
        }

    }

    public void gameOver(Graphics g){

        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        // TODO: restart game
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isRunning){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            // control direction of snake
            switch (e.getKeyCode()){

                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

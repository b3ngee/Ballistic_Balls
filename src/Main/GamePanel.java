package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by BenGee on 2015-09-10.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS = 30;

    public static Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;
    public static ArrayList<PowerUps> powerUps;
    public static ArrayList<Explosion> explosions;
    public static ArrayList<Text> texts;

    private long waveStartTime;
    private long waveStartTimerDiff;
    private int waveNumber;
    private boolean waveStart;
    private int waveDelay = 3000;

    private boolean flash;

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }

    public void run() {
        running = true;
        flash = false;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        player = new Player();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();
        explosions = new ArrayList<>();
        texts = new ArrayList<>();

        waveStartTime = 0;
        waveStartTimerDiff = 0;
        waveStart = true;
        waveNumber = 0;

        long startTime;
        long URDTimeMillis;
        long waitTime;
        long totalTime = 0;
        long targetTime = 1000 / FPS;

        int frameCount = 0;
        int maxFrameCount = 30;

        while (running) {
            startTime = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - URDTimeMillis;

            try {
                if (waitTime < 0) {
                    waitTime = 5;
                }
                Thread.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                frameCount = 0;
                totalTime = 0;
            }
        }
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String gameOver = "GAME OVER";
        String winner = "YOU WIN!";
        String s = "SCORE: " + player.getScore();
        int length1 = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
        int length2 = (int) g.getFontMetrics().getStringBounds(winner, g).getWidth();
        int length3 = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        if (player.getLives() == 0) {
            g.drawString(gameOver, WIDTH / 2 - length1 / 2, HEIGHT / 2);
        } else {
            g.drawString(winner, WIDTH / 2 - length2 / 2, HEIGHT / 2);
        }
        g.drawString(s, WIDTH / 2 - length3 / 2, HEIGHT / 2 + 30);
        gameDraw();
    }



    private void gameUpdate() {

        if (waveStartTime == 0 && enemies.size() == 0) {
            waveNumber++;
            waveStart = false;
            waveStartTime = System.nanoTime();
        } else {
            waveStartTimerDiff = (System.nanoTime() - waveStartTime) / 1000000;
            if (waveStartTimerDiff > waveDelay) {
                waveStart = true;
                waveStartTime = 0;
                waveStartTimerDiff = 0;
            }
        }

        if (waveStart && enemies.size() == 0) {
            createNewEnemies();
        }

        player.update();

        for (int i = 0; i < bullets.size(); i++) {
            boolean remove = bullets.get(i).update();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }

        for (int i = 0; i < powerUps.size(); i++) {
            boolean remove = powerUps.get(i).update();
            if (remove) {
                powerUps.remove(i);
                i--;
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            boolean remove = explosions.get(i).update();
            if (remove) {
                explosions.remove(i);
                i--;
            }
        }

        for (int i = 0; i < texts.size(); i++) {
            boolean remove = texts.get(i).update();
            if (remove) {
                texts.remove(i);
                i--;
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            double bx = b.getX();
            double by = b.getY();
            double br = b.getR();

            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = bx - ex;
                double dy = by - ey;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < br + er) {
                    if (e.getType() == 5) {
                        enemies.add(new Enemy(1,1));
                    }
                    e.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.isDead()) {
                double random = Math.random();
                if (random < 0.1) {
                    powerUps.add(new PowerUps(3, e.getX(), e.getY()));
                } else if (random < 0.17) {
                    powerUps.add(new PowerUps(1, e.getX(), e.getY()));
                } else if (random < 0.25) {
                    powerUps.add(new PowerUps(2, e.getX(), e.getY()));
                }
                player.addScore(e.getType() + e.getRank());
                e.explode();
                explosions.add(new Explosion(e.getX(), e.getY(), e.getR(), e.getR() + 20));
                enemies.remove(i);
                i--;
            }
        }

        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
            int pr = player.getR();

            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = px - ex;
                double dy = py - ey;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < pr + er) {
                    if (e.getType() == 5) {
                        player.loseBigLife();
                    } else {
                        player.loseLife();
                    }
                }
            }
        }

        for (int i = 0; i < powerUps.size(); i++) {
            PowerUps p = powerUps.get(i);
            double pux = p.getX();
            double puy = p.getY();
            double pur = p.getR();
            int px = player.getX();
            int py = player.getY();
            int pr = player.getR();

            double dx = pux - px;
            double dy = puy - py;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist < pur + pr) {
                if (p.getType() == 1) {
                    player.gainLife();
                    texts.add(new Text(p.getX(), p.getY(), "+1 Health"));
                } if (p.getType() == 2) {
                    player.increasePower();
                    texts.add(new Text(p.getX(), p.getY(), "+1 Power"));
                } if (p.getType() == 3) {
                    flash = true;
                    texts.add(new Text(p.getX(), p.getY(), "Flash Bang!"));
                    for (Enemy e : enemies) {
                        e.hit();
                    }
                }
                powerUps.remove(i);
                i--;
            }
        }

        if (player.isBeastMode()) {
            for (Enemy e : enemies) {
                e.setSetSlow(true);
            }
        } else {
            for (Enemy e : enemies) {
                e.setSetSlow(false);
            }
        }
    }

    private void gameRender() {
        if (flash) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            flash = false;
        } else if (player.isBeastMode()) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            String s = "BEAST: ";
            g.setColor(Color.GREEN);
            g.drawString(s, 10, 67);

            g.setColor(Color.BLACK);
            g.fillRect(85, 54, 280, 12);
            g.setColor(Color.GREEN);
            g.drawRect(85, 54, 280, 12);
            g.setColor(Color.GREEN);
            g.fillRect(85, 54, (int) (280 - (280 * player.getBeastModeTimeDiff() / (long) 5000)), 12);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        player.draw(g);

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        for (int i = 0; i < powerUps.size(); i++) {
            powerUps.get(i).draw(g);
        }

        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).draw(g);
        }

        for (int i = 0; i < texts.size(); i++) {
            texts.get(i).draw(g);
        }

        if (waveStartTime != 0) {
            String s;
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            if (waveNumber == 6) {
                s = "B O S S";
            } else {
                s = "W A V E   " + waveNumber;
            }
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
            if (alpha > 255) {
                alpha = 255;
            }
            g.setColor(new Color(255, 255, 255, alpha));
            g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
        }

        g.setColor(Color.BLACK);
        g.fillRect(85, 10, 280, 12);
        g.setColor(Color.RED.darker());
        g.drawRect(85, 10, 280, 12);
        for (int i = 0; i < player.getLives(); i++) {
            g.setColor(Color.RED);
            g.fillRect(85, 10, player.getLives() * 28, 12);
        }

        g.setColor(Color.BLACK);
        g.fillRect(85, 32, 280, 12);
        g.setColor(Color.YELLOW.darker());
        g.drawRect(85, 32, 280, 12);
        for (int i = 0; i < 5; i++) {
            g.setColor(Color.YELLOW);
            g.fillRect(85, 32, player.getPower() * 56, 12);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String s = "SCORE: " + player.getScore();
        g.drawString(s, WIDTH - 115, 33);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String h = "HEALTH: ";
        String p = "POWER: ";
        g.setColor(Color.RED);
        g.drawString(h, 10, 21);
        g.setColor(Color.YELLOW);
        g.drawString(p, 10, 44);

        if (player.getLives() <= 0 || waveNumber > 6) {
            running = false;
        }
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    private void createNewEnemies() {
        enemies.clear();
        if (waveNumber == 1) {
            for (int i = 0; i < 8; i++) {
                enemies.add(new Enemy(1, 1));
            }
        } if (waveNumber == 2) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 1));
            }
            for (int j = 0; j < 5; j++) {
                enemies.add(new Enemy(2, 2));
            }
        } if (waveNumber == 3) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 1));
            }
            for (int j = 0; j < 5; j++) {
                enemies.add(new Enemy(2, 2));
            }
            for (int k = 0; k < 2; k++) {
                enemies.add(new Enemy(3, 3));
            }
        } if (waveNumber == 4) {
            for (int i = 0; i < 5; i++) {
                enemies.add(new Enemy(1, 1));
            }
            for (int j = 0; j < 5; j++) {
                enemies.add(new Enemy(2, 2));
            }
            for (int k = 0; k < 5; k++) {
                enemies.add(new Enemy(3, 3));
            }
        } if (waveNumber == 5) {
            enemies.add(new Enemy(4, 4));
            enemies.add(new Enemy(4, 4));
            enemies.add(new Enemy(4, 4));
        } if (waveNumber == 6) {
            enemies.add((new Enemy(5, 5)));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setRight(true);
        } else if (key == KeyEvent.VK_UP) {
            player.setUp(true);
        } else if (key == KeyEvent.VK_DOWN) {
            player.setDown(true);
        }

        if (key == KeyEvent.VK_SPACE) {
            player.setFiring(true);
        }

        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        } else if (key == KeyEvent.VK_UP) {
            player.setUp(false);
        } else if (key == KeyEvent.VK_DOWN) {
            player.setDown(false);
        }

        if (key == KeyEvent.VK_SPACE) {
            player.setFiring(false);
        }
    }
}



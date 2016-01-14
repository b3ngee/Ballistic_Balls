package Main;

import java.awt.*;

/**
 * Created by BenGee on 2015-09-10.
 */
public class Player {

    private int x;
    private int y;
    private int r;

    private int dx;
    private int dy;
    private int speed;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private boolean firing;
    private long firingTime;
    private long firingDelay;

    private boolean recovering;
    private long recoveringTime;

    private int lives;
    private Color c1;
    private Color c2;

    private int score;

    private static int powerLevel;
    private int power;

    private boolean beastMode;
    private long beastModeTime;
    private long beastModeTimeDiff;
    private boolean invincible;

    public Player() {
        x = GamePanel.WIDTH;
        y = GamePanel.HEIGHT;
        r = 8;
        dx = 0;
        dy = 0;
        speed = 10;
        lives = 10;
        power = 0;
        powerLevel = 0;
        c1 = Color.CYAN;
        c2 = Color.RED;
        firing = false;
        firingTime = System.nanoTime();
        firingDelay = 300;
        recovering = false;
        recoveringTime = 0;
        score = 0;
        beastMode = false;
        beastModeTime = 0;
        beastModeTimeDiff = 0;
        invincible = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public int getLives() {
        return lives;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    public boolean isRecovering() {
        return recovering;
    }

    public int getScore() {
        return score;
    }

    public int getPower() {
        return power;
    }

    public boolean isBeastMode() {
        return beastMode;
    }

    public long getBeastModeTimeDiff() {
        return beastModeTimeDiff;
    }

    public void addScore(int i) {
        score += i;
    }

    public void loseLife() {
        if (!invincible) {
            lives--;
            recovering = true;
            recoveringTime = System.nanoTime();
        }
    }

    public void loseBigLife() {
        if (!invincible) {
            lives -= 2;
            recovering = true;
            recoveringTime = System.nanoTime();
        }
    }

    public void gainLife() {
        if (lives < 10) {
            lives++;
        }
    }

    public void increasePower() {
        power++;
        if (power >= 5) {
            powerLevel++;
            power = 0;
        }
        if (powerLevel == 3) {
            beastMode = true;
            beastModeTime = System.nanoTime();
            powerLevel = 2;
        }
    }

    public void update() {
        if (left) {
            dx = -speed;
        } else if (right) {
            dx = speed;
        } else if (up) {
            dy = -speed;
        } else if (down) {
            dy = speed;
        }
        x += dx;
        y += dy;

        if (x < r) {
            x = r;
        }
        if (y < r) {
            y = r;
        }
        if (x > GamePanel.WIDTH - r) {
            x = GamePanel.WIDTH - r;
        }
        if (y > GamePanel.HEIGHT - r) {
            y = GamePanel.HEIGHT - r;
        }
        dx = 0;
        dy = 0;

        if (firing) {
            long elapsed = (System.nanoTime() - firingTime) / 1000000;
            if (elapsed > firingDelay) {
                firingTime = System.nanoTime();
                if (powerLevel == 0) {
                    GamePanel.bullets.add(new Bullet(270, x, y));
                } else if (powerLevel == 1) {
                    GamePanel.bullets.add(new Bullet(267, x, y));
                    GamePanel.bullets.add(new Bullet(273, x, y));
                } else {
                    GamePanel.bullets.add(new Bullet(267, x, y));
                    GamePanel.bullets.add(new Bullet(270, x, y));
                    GamePanel.bullets.add(new Bullet(273, x, y));
                }
            }
        }

        if (beastMode) {
            beastModeTimeDiff = (System.nanoTime() - beastModeTime) / 1000000;
            invincible = true;
            firingDelay = 50;
            if (beastModeTimeDiff > 5000) {
                invincible = false;
                firingDelay = 300;
                beastMode = false;
                beastModeTime = 0;
            }
        }

        long elapsed = (System.nanoTime() - recoveringTime) / 1000000;
        if (elapsed > 2000) {
            recovering = false;
            recoveringTime = 0;
        }
    }


    public void draw(Graphics2D g) {
        if (recovering) {
            g.setColor(c2);
            g.fillOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.RED.darker());
            g.drawOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(c1);
            g.fillOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.BLUE);
            g.drawOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
    }
}

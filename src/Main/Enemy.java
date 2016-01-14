package Main;

import java.awt.*;

/**
 * Created by BenGee on 2015-09-10.
 */
public class Enemy {

    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;
    private double rad;
    private double speed;

    private int health;
    private int type;
    private int rank;

    private Color c1;
    private Color c2;

    private boolean ready;
    private boolean dead;

    private boolean recovering;
    private long recoveringTime;

    private boolean setSlow;

    public Enemy(int type, int rank) {
        this.type = type;
        this.rank = rank;

        if (type == 1) {
            c1 = Color.GREEN;
        }
        if (type == 2) {
            c1 = Color.BLUE;
        }
        if (type == 3) {
            c1 = Color.RED;
        }
        if (type == 4) {
            c1 = new Color(227, 142, 47);
        }
        if (type == 5) {
            c1 = new Color(87, 62, 175);
        }

        c2 = Color.WHITE;

        if (rank == 1) {
            speed = 4;
            health = 1;
            r = 8;
        }
        if (rank == 2) {
            speed = 5;
            health = 3;
            r = 15;
        }
        if (rank == 3) {
            speed = 7;
            health = 5;
            r = 20;
        }
        if (rank == 4) {
            speed = 5;
            health = 20;
            r = 50;
        }
        if (rank == 5) {
            speed = 4;
            health = 300;
            r = 150;
        }

        x = Math.random() * GamePanel.WIDTH / 2;
        y = -r;

        double angle = Math.random() * 140 + 20;
        rad = Math.toRadians(angle);
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;

        ready = false;
        dead = false;

        recovering = false;
        recoveringTime = 0;

        setSlow = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public boolean isDead() {
        return dead;
    }

    public int getRank() {
        return rank;
    }

    public int getType() {
        return type;
    }

    public void setSetSlow(boolean setSlow) {
        this.setSlow = setSlow;
    }

    public void hit() {
        health--;
        recovering = true;
        recoveringTime = System.nanoTime();
        if (health <= 0) {
            dead = true;
            recovering = false;
            recoveringTime = 0;
        }
    }

    public void explode() {
        if (type == 5) {
            for (int i = 0; i < 10; i++) {
                Enemy e = new Enemy(3, 3);
                e.x = this.x;
                e.y = this.y;
                GamePanel.enemies.add(e);
            }
        }
        if (type == 4) {
            for (int i = 0; i < 3; i++) {
                Enemy e = new Enemy(3, 3);
                e.x = this.x;
                e.y = this.y;
                GamePanel.enemies.add(e);
            }
        }
        if (type == 3) {
            for (int i = 0; i < 3; i++) {
                Enemy e = new Enemy(2, 2);
                e.x = this.x;
                e.y = this.y;
                GamePanel.enemies.add(e);
            }
        }
        if (type == 2) {
            for (int i = 0; i < 3; i++) {
                Enemy e = new Enemy(1, 1);
                e.x = this.x;
                e.y = this.y;
                GamePanel.enemies.add(e);
            }
        }
    }

    public void update() {
        if (setSlow) {
            x += dx * 0.3;
            y += dy * 0.3;
        } else {
            x += dx;
            y += dy;
        }

        if (!ready) {
            if (x > r && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {
                ready = true;
            }
        }
        if (x < r && dx < 0) {
            dx = -dx;
        }
        if (x > GamePanel.WIDTH - r && dx > 0) {
            dx = -dx;
        }
        if (y < r && dy < 0) {
            dy = -dy;
        }
        if (y > GamePanel.HEIGHT - r && dy > 0) {
            dy = -dy;
        }

        long elapsed = (System.nanoTime() - recoveringTime) / 1000000;
        if (elapsed > 50) {
            recovering = false;
            recoveringTime = 0;
        }
    }

    public void draw(Graphics2D g) {
        if (recovering) {
            g.setColor(c2);
            g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(c2.darker());
            g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(c1);
            g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(c1.darker());
            g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
    }
}




package Main;

import java.awt.*;

/**
 * Created by BenGee on 2015-09-10.
 */
public class Bullet {

    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;
    private double rad;
    private double speed;

    private Color c1;

    public Bullet(double angle, int x, int y) {
        this.x = x;
        this.y = y;
        r = 3;
        rad = Math.toRadians(angle);
        speed = 10;
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;
        c1 = Color.YELLOW;
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

    public boolean update() {
        x += dx;
        y += dy;

        if (x < -r || x > GamePanel.WIDTH + r || y < -r || y > GamePanel.HEIGHT + r) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.setColor(c1);
        g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setColor(c1.darker());
        g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
}

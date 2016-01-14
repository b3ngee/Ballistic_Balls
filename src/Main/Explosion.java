package Main;

import java.awt.*;

/**
 * Created by BenGee on 2015-09-12.
 */
public class Explosion {

    private double x;
    private double y;
    private int r;
    private int maxRadius;

    public Explosion(double x, double y, int r, int maxRadius) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.maxRadius = maxRadius;
    }

    public boolean update() {
        r += 3;
        if (r > maxRadius) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.YELLOW);
        g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));
    }
}

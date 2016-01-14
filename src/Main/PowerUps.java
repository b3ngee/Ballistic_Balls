package Main;

import java.awt.*;

/**
 * Created by BenGee on 2015-09-11.
 */

/**
 *  1 -- +1 health
 *  2 -- add to power level; when max gain 1 bullet
 *  3 -- increase firing speed to 100
 */
public class PowerUps {

    private double x;
    private double y;
    private int r;
    private int type;

    public PowerUps(int type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
        r = 3;
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

    public int getType() {
        return type;
    }

    public boolean update() {
        y += 2;

        if (y > GamePanel.HEIGHT + r) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        if (type == 1) {
            g.setColor(Color.PINK);
            g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.RED);
            g.drawRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
        if (type == 2) {
            g.setColor(Color.YELLOW);
            g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.YELLOW.darker());
            g.drawRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
        if (type == 3) {
            g.setColor(Color.MAGENTA);
            g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.BLUE);
            g.drawRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
    }
}

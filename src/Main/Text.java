package Main;

import java.awt.*;

/**
 * Created by BenGee on 2015-09-13.
 */
public class Text {

    private double x;
    private double y;
    private String s;
    private long start;

    public Text(double x, double y, String s) {
        this.x = x;
        this.y = y;
        this.s = s;
        start = System.nanoTime();
    }

    public boolean update() {
        long time = (System.nanoTime() - start) / 1000000;
        if (time > 1000) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.setColor(new Color(161, 161, 161));
        g.drawString(s, (int) (x - length / 2), (int) y);
    }
}



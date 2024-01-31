import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

import java.util.Random;

public class Effect {
    private float alpha;
    private final double x;
    private final double y;
    private final Color color;
    private final float speed;
    private final Square[] squares;
    private double currentDistance;
    private final double maxDistance;

    public Effect(double x, double y, int totalEffect, int maxSize, double maxDistance, float speed, Color color) {

        this.x = x;
        this.y = y;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.color = color;
        this.alpha = 1f;

        Random random = new Random();

        squares = new Square[totalEffect];

        float per = 360f / totalEffect;

        for (int i = 1; i <= totalEffect; i++) {
            squares[i - 1] = new Square(random.nextInt(maxSize) + 1, i * per + (random.nextInt((int) per) + 1));
        }
    }

    public void update() {
        currentDistance += speed;
    }

    public boolean check() {
        return currentDistance < maxDistance;
    }

    public void draw(Graphics2D graphics2D) {

        AffineTransform affineTransform = graphics2D.getTransform();

        graphics2D.setColor(color);
        graphics2D.translate(x, y);

        for (Square square : squares) {

            double squareX = Math.cos(Math.toRadians(square.getAngle())) * currentDistance;
            double squareY = Math.sin(Math.toRadians(square.getAngle())) * currentDistance;

            double boomSize = square.getSize();
            double space = boomSize / 2;

            if (currentDistance >= maxDistance - (maxDistance * 0.7f)) {
                alpha = (float) ((maxDistance - currentDistance) / (maxDistance * 0.7f));
            }

            if (alpha > 1) {
                alpha = 1;
            } else if (alpha < 0) {
                alpha = 0;
            }

            graphics2D.fill(new Rectangle2D.Double(squareX - space, squareY - space, boomSize, boomSize));
        }


        graphics2D.setTransform(affineTransform);

        graphics2D.setColor(new Color(50, 50, 50));
    }
}

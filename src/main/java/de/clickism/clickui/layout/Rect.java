package de.clickism.clickui.layout;

public record Rect(int x, int y, int width, int height) {
    public static Rect ZERO = new Rect(0, 0, 0, 0);

    public boolean contains(Point point) {
        return contains(point.x(), point.y());
    }

    public boolean contains(int px, int py) {
        return px >= x && px < x + width && py >= y && py < y + height;
    }

    public Rect withSize(Size size) {
        return new Rect(x, y, size.width(), size.height());
    }

    public Rect withSize(int width, int height) {
        return new Rect(x, y, width, height);
    }

    public Rect withWidth(int width) {
        return new Rect(x, y, width, height);
    }

    public Rect withHeight(int height) {
        return new Rect(x, y, width, height);
    }

    public Rect withPosition(int x, int y) {
        return new Rect(x, y, width, height);
    }
}

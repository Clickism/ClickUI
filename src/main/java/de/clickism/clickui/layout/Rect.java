package de.clickism.clickui.layout;

public record Rect(int x, int y, int width, int height) {
    public static Rect ZERO = new Rect(0, 0, 0, 0);

    public boolean contains(Point point) {
        return contains(point.x(), point.y());
    }

    public boolean contains(double px, double py) {
        return px >= x && px < x + width && py >= y && py < y + height;
    }

    public int area() {
        return width * height;
    }

    public boolean isEmpty() {
        return area() == 0;
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

package com.srots.presentation.window;

/**
 * Safe, persistable UI window preferences (no secrets).
 */
public final class SrotsWindowState {

    private double x = Double.NaN;
    private double y = Double.NaN;
    private double width = SrotsWindowConfiguration.DEFAULT_WIDTH;
    private double height = SrotsWindowConfiguration.DEFAULT_HEIGHT;
    private boolean maximized;
    private boolean sidebarCollapsed;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public boolean isSidebarCollapsed() {
        return sidebarCollapsed;
    }

    public void setSidebarCollapsed(boolean sidebarCollapsed) {
        this.sidebarCollapsed = sidebarCollapsed;
    }

    public boolean hasPosition() {
        return !Double.isNaN(x) && !Double.isNaN(y);
    }

    public SrotsWindowState copy() {
        SrotsWindowState copy = new SrotsWindowState();
        copy.x = x;
        copy.y = y;
        copy.width = width;
        copy.height = height;
        copy.maximized = maximized;
        copy.sidebarCollapsed = sidebarCollapsed;
        return copy;
    }
}

package org.cis1200.chess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

public abstract class ChessPiece {
    public enum Color {
        WHITE, BLACK, NONE
    }

    private Color color;
    private int row, col; // Position on the board
    private boolean hasMoved;
    private boolean highlighted;
    private int value;
    private boolean toBeCaptured;
    private BufferedImage img;

    public ChessPiece(Color color, int x, int y) {
        this.color = color;
        this.row = x;
        this.col = y;
        this.hasMoved = false;
        this.highlighted = false;
    }

    // Getter and Setter Methods
    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean toBeCaptured() {
        return toBeCaptured;
    }

    public void setToBeCaptured(boolean toBeCaptured) {
        this.toBeCaptured = toBeCaptured;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void draw(Graphics g) {
        if (highlighted) {
            g.setColor(new java.awt.Color(255, 255, 80, 127));
            g.fillRect(70 * col, 70 * row, 70, 70);
        }
        if (toBeCaptured) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(4));
            g.setColor(new java.awt.Color(75, 72, 71));
            g.drawOval(70 * col, 70 * row, 70, 70);
        }
        try {
            g.drawImage(img, 70 * col, 70 * row, 70, 70, null);
        } catch (NullPointerException ignored) {
        }
    }

    // Abstract Methods for Subclasses
    public abstract Set<Position> calculateLegalMoves(Chess c);
}
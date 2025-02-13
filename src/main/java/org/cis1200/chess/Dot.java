package org.cis1200.chess;

import java.awt.*;
import java.util.Set;

public class Dot extends ChessPiece {
    public Dot(Color color, int x, int y) {
        super(color, x, y);
        setImg(null);
        setValue(0);
    }

    @Override
    public Set<Position> calculateLegalMoves(Chess c) {
        return Set.of();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new java.awt.Color(75, 72, 71));
        g.fillOval(getCol() * 70 + 210 / 8, getRow() * 70 + 210 / 8, 70 / 4, 70 / 4);
    }
}

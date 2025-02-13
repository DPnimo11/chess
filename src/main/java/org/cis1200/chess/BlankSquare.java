package org.cis1200.chess;

import java.util.Set;

public class BlankSquare extends ChessPiece {
    public BlankSquare(Color color, int row, int col) {
        super(color, row, col);
        setValue(0);
        setImg(null);

    }

    @Override
    public Set<Position> calculateLegalMoves(Chess c) {
        return Set.of();
    }
}

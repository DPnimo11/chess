package org.cis1200.chess;

public class Move {
    private final ChessPiece start;
    private final ChessPiece end;
    private final Position startPosition;
    private final Position endPosition;

    public Move(ChessPiece start, ChessPiece end) {
        this.start = start;
        this.end = end;
        this.startPosition = new Position(start.getRow(), start.getCol());
        this.endPosition = new Position(end.getRow(), end.getCol());
    }

    public ChessPiece getStart() {
        return start;
    }

    public ChessPiece getEnd() {
        return end;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }
}

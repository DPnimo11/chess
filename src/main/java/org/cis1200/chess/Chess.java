package org.cis1200.chess;

import java.util.*;

public class Chess {

    private ChessPiece[][] board;
    private ChessPiece.Color player;
    private boolean gameOver;
    private LinkedList<Move> moves;
    private Position selected;
    private Set<Position> highlighted;

    public Chess() {
        resetBoard(false);
    }

    public ChessPiece.Color getPlayerTurn() {
        return player;
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public LinkedList<Move> getMoves() {
        return moves;
    }

    public Position getSelected() {
        return selected;
    }

    public void resetBoard(boolean storeMoves) {
        board = new ChessPiece[8][8];
        player = ChessPiece.Color.WHITE;
        selected = null;
        highlighted = Set.of();
        if (!storeMoves) {
            moves = new LinkedList<>();
        }
        gameOver = false;
        /* pawns */
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(ChessPiece.Color.BLACK, 1, col);
            board[6][col] = new Pawn(ChessPiece.Color.WHITE, 6, col);
        }
        /* knights */
        board[0][1] = new Knight(ChessPiece.Color.BLACK, 0, 1);
        board[0][6] = new Knight(ChessPiece.Color.BLACK, 0, 6);
        board[7][1] = new Knight(ChessPiece.Color.WHITE, 7, 1);
        board[7][6] = new Knight(ChessPiece.Color.WHITE, 7, 6);

        /* rooks */
        board[0][0] = new Rook(ChessPiece.Color.BLACK, 0, 0);
        board[0][7] = new Rook(ChessPiece.Color.BLACK, 0, 7);
        board[7][0] = new Rook(ChessPiece.Color.WHITE, 7, 0);
        board[7][7] = new Rook(ChessPiece.Color.WHITE, 7, 7);

        /* bishops */
        board[0][2] = new Bishop(ChessPiece.Color.BLACK, 0, 2);
        board[0][5] = new Bishop(ChessPiece.Color.BLACK, 0, 5);
        board[7][2] = new Bishop(ChessPiece.Color.WHITE, 7, 2);
        board[7][5] = new Bishop(ChessPiece.Color.WHITE, 7, 5);

        /* queens */
        board[0][3] = new Queen(ChessPiece.Color.BLACK, 0, 3);
        board[7][3] = new Queen(ChessPiece.Color.WHITE, 7, 3);

        /* kings */
        board[0][4] = new King(ChessPiece.Color.BLACK, 0, 4);
        board[7][4] = new King(ChessPiece.Color.WHITE, 7, 4);

        /* blank squares */
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == null) {
                    board[row][col] = new BlankSquare(ChessPiece.Color.NONE, row, col);
                }
            }
        }
    }

    public boolean isCheck() {
        ChessPiece endangered = null;
        ChessPiece oppositeKing = null;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece p = board[row][col];
                if (p.getClass() == King.class && p.getColor() == player) {
                    endangered = p;
                } else if (p.getClass() == King.class) {
                    oppositeKing = p;
                }
            }
        }
        assert endangered != null;
        assert oppositeKing != null;
        boolean moveFlag = false;
        if (!oppositeKing.hasMoved()) {
            oppositeKing.setHasMoved(true);
            moveFlag = true;
        }
        flip();

        boolean flag = false;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece p = board[row][col];
                if (!flag && p.getColor() != endangered.getColor() && p.calculateLegalMoves(this)
                        .contains(new Position(endangered.getRow(), endangered.getCol()))) {
                    flip();
                    if (moveFlag) {
                        oppositeKing.setHasMoved(false);
                    }
                    return true;
                }
            }
        }
        flip();
        if (moveFlag) {
            oppositeKing.setHasMoved(false);
        }
        return false;
    }

    public boolean willBeCheck(Position p1, Position p2) {
        int r1 = p1.getFirst();
        int c1 = p1.getSecond();
        int r2 = p2.getFirst();
        int c2 = p2.getSecond();
        ChessPiece temp = board[r2][c2];
        if (!p1.equals(p2)) {
            board[r2][c2] = board[r1][c1];
            board[r2][c2].setRow(r2);
            board[r2][c2].setCol(c2);
            board[r1][c1] = new BlankSquare(ChessPiece.Color.NONE, r1, c1);
        }
        boolean flag = isCheck();
        if (!p1.equals(p2)) {
            board[r1][c1] = board[r2][c2];
            board[r1][c1].setRow(r1);
            board[r1][c1].setCol(c1);
            board[r2][c2] = temp;
        }
        return flag;
    }

    public ChessPiece getCell(int row, int col) {
        return board[row][col];
    }

    private void cleanBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col].getClass() == Dot.class) {
                    board[row][col] = new BlankSquare(ChessPiece.Color.NONE, row, col);
                } else {
                    board[row][col].setToBeCaptured(false);
                    board[row][col].setHighlighted(false);
                }
            }
        }
        if (!moves.isEmpty()) {
            board[7 - moves.peekLast().getStartPosition().getFirst()][7
                    - moves.peekLast().getStartPosition().getSecond()].setHighlighted(true);
            board[7 - moves.peekLast().getEndPosition().getFirst()][7
                    - moves.peekLast().getEndPosition().getSecond()].setHighlighted(true);
        }

    }

    public void undo() {
        if (!moves.isEmpty()) {
            resetBoard(true);
            moves.removeLast();
            for (Move move : moves) {
                action(
                        move.getStartPosition().getFirst(), move.getStartPosition().getSecond(),
                        false
                );
                action(move.getEndPosition().getFirst(), move.getEndPosition().getSecond(), false);
            }
        }
    }

    public void action(int row, int col, boolean storeMoves) {
        cleanBoard();
        if (!highlighted.contains(new Position(row, col))) {
            if (board[row][col].getColor() == player) {
                selected = new Position(row, col);
                highlighted = board[row][col].calculateLegalMoves(this);
                highlighted.removeIf(p -> willBeCheck(selected, p));
                board[row][col].setHighlighted(true);
                for (Position pos : highlighted) {
                    if (board[row][col].getClass() == Pawn.class && col != pos.getSecond() &&
                            board[pos.getFirst()][pos.getSecond()]
                                    .getClass() == BlankSquare.class) {
                        board[pos.getFirst()][pos.getSecond()].setToBeCaptured(true);
                    } else if (board[pos.getFirst()][pos.getSecond()]
                            .getClass() == BlankSquare.class) {
                        board[pos.getFirst()][pos.getSecond()] = new Dot(
                                null, pos.getFirst(), pos.getSecond()
                        );
                    } else {
                        board[pos.getFirst()][pos.getSecond()].setToBeCaptured(true);
                    }
                }
            }
        } else if (selected != null) {
            ChessPiece x = board[selected.getFirst()][selected.getSecond()];
            if (storeMoves) {
                moves.add(
                        new Move(board[selected.getFirst()][selected.getSecond()], board[row][col])
                );
            }
            /* Check for castling! */
            if (board[selected.getFirst()][selected.getSecond()].getClass() == King.class
                    && col - selected.getSecond() > 1) {
                board[7][7].setCol(selected.getSecond() + 1);
                board[7][7].setHighlighted(false);
                board[7][selected.getSecond() + 1] = board[7][7];
                board[7][7] = new BlankSquare(ChessPiece.Color.NONE, 7, 7);
            } else if (board[selected.getFirst()][selected.getSecond()].getClass() == King.class
                    && col - selected.getSecond() < -1) {
                board[7][0].setCol(selected.getSecond() - 1);
                board[7][0].setHighlighted(false);
                board[7][selected.getSecond() - 1] = board[7][0];
                board[7][0] = new BlankSquare(ChessPiece.Color.NONE, 7, 0);
            } else if (board[selected.getFirst()][selected.getSecond()].getClass() == Pawn.class
                    && col != selected.getSecond() &&
                    board[row][col].getClass() == BlankSquare.class) {
                board[row + 1][col] = new BlankSquare(ChessPiece.Color.NONE, row + 1, col);
            }
            board[selected.getFirst()][selected.getSecond()].setRow(row);
            board[selected.getFirst()][selected.getSecond()].setCol(col);

            board[selected.getFirst()][selected.getSecond()].setHasMoved(true);
            board[row][col] = board[selected.getFirst()][selected.getSecond()];
            board[selected.getFirst()][selected.getSecond()] = new BlankSquare(
                    ChessPiece.Color.NONE, selected.getFirst(), selected.getSecond()
            );

            if (player == ChessPiece.Color.WHITE) {
                player = ChessPiece.Color.BLACK;

            } else {
                player = ChessPiece.Color.WHITE;
            }
            selected = null;
            highlighted = Set.of();
            flip();
            cleanBoard();
        }
    }

    private void flip() {
        ChessPiece[][] flipped = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                flipped[7 - row][7 - col] = board[row][col];
                flipped[7 - row][7 - col].setRow(7 - row);
                flipped[7 - row][7 - col].setCol(7 - col);
            }
        }
        board = flipped;
    }

    public boolean isCheckmate() {
        if (!isCheck()) {
            return false;
        }
        boolean flag = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (flag && piece.getColor() == player) {
                    for (Position pos : piece.calculateLegalMoves(this)) {
                        if (!willBeCheck(new Position(row, col), pos)) {
                            flag = false;
                        }
                    }
                }
            }
        }
        return flag;
    }

    public boolean isStalemate() {
        if (isCheck()) {
            return false;
        }
        boolean flag = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (flag && piece.getColor() == player) {
                    for (Position pos : piece.calculateLegalMoves(this)) {
                        if (!willBeCheck(new Position(row, col), pos)) {
                            flag = false;
                        }
                    }
                }
            }
        }
        return flag;
    }

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print(board[row][col]);
                System.out.print(board[row][col].getRow());
            }
            System.out.println();
        }
    }
}

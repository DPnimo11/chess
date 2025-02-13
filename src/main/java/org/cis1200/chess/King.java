package org.cis1200.chess;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class King extends ChessPiece {
    public King(Color color, int x, int y) {
        super(color, x, y);
        setValue(0);
        try {
            if (color == Color.WHITE) {
                setImg(ImageIO.read(new File("files/wking.png")));
            } else {
                setImg(ImageIO.read(new File("files/bking.png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Position> calculateLegalMoves(Chess c) {
        ChessPiece[][] board = c.getBoard();
        HashSet<Position> list = new HashSet<>();
        int row = getRow();
        int col = getCol();
        Color color = getColor();
        boolean hasMoved = getHasMoved();
        for (int dx = -1; dx <= 1; dx += 2) {
            if (0 <= col + dx && col + dx <= 7 && board[row][col + dx].getColor() != color) {
                list.add(new Position(row, col + dx));
            }

        }
        for (int dy = -1; dy <= 1; dy += 2) {
            if (0 <= row + dy && row + dy <= 7 && board[row + dy][col].getColor() != color) {
                list.add(new Position(row + dy, col));
            }
        }
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                if (0 <= row + dy && row + dy <= 7 && 0 <= col + dx && col + dx <= 7
                        && board[row + dy][col + dx].getColor() != color) {
                    list.add(new Position(row + dy, col + dx));
                }

            }
        }

        /* Castling */
        if (color == Color.WHITE) {
            if (!hasMoved && board[row][col + 3].getValue() == 5 && !board[row][col + 3].hasMoved()
                    && board[row][col + 1].getClass() == BlankSquare.class
                    && board[row][col + 2].getClass() == BlankSquare.class
                    && !c.willBeCheck(new Position(row, col), new Position(row, col))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col + 1))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col + 2))) {
                list.add(new Position(row, col + 2));
            }

            if (!hasMoved && board[row][col - 4].getValue() == 5 && !board[row][col - 4].hasMoved()
                    && board[row][col - 1].getClass() == BlankSquare.class
                    && board[row][col - 2].getClass() == BlankSquare.class
                    && board[row][col - 3].getClass() == BlankSquare.class
                    && !c.willBeCheck(new Position(row, col), new Position(row, col))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col - 1))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col - 2))) {
                list.add(new Position(row, col - 2));
            }
        } else {
            if (!hasMoved && board[row][col + 4].getValue() == 5 && !board[row][col + 4].hasMoved()
                    && board[row][col + 1].getClass() == BlankSquare.class
                    && board[row][col + 2].getClass() == BlankSquare.class
                    && board[row][col + 3].getClass() == BlankSquare.class
                    && !c.willBeCheck(new Position(row, col), new Position(row, col))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col + 1))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col + 2))) {
                list.add(new Position(row, col + 2));
            }
            if (!hasMoved && board[row][col - 3].getValue() == 5 && !board[row][col - 3].hasMoved()
                    && board[row][col - 1].getClass() == BlankSquare.class
                    && board[row][col - 2].getClass() == BlankSquare.class
                    && !c.willBeCheck(new Position(row, col), new Position(row, col))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col - 1))
                    && !c.willBeCheck(new Position(row, col), new Position(row, col - 2))) {
                list.add(new Position(row, col - 2));
            }
        }
        return list;
    }
}

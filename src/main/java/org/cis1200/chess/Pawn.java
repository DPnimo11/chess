package org.cis1200.chess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Pawn extends ChessPiece {
    public Pawn(Color color, int x, int y) {
        super(color, x, y);
        setValue(1);
        try {
            if (color == Color.WHITE) {
                setImg(ImageIO.read(new File("files/wpawn.png")));
            } else {
                setImg(ImageIO.read(new File("files/bpawn.png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /* change to set maybe? */
    public Set<Position> calculateLegalMoves(Chess c) {
        ChessPiece[][] board = c.getBoard();
        HashSet<Position> list = new HashSet<>();
        int row = getRow();
        int col = getCol();
        Color color = getColor();
        if (0 <= row - 1 && board[row - 1][col].getClass() == BlankSquare.class) {
            list.add(new Position(row - 1, col));
        }
        if (!hasMoved() && board[row - 1][col].getClass() == BlankSquare.class
                && board[row - 2][col].getClass() == BlankSquare.class) {
            list.add(new Position(row - 2, col));
        }
        if (0 <= row - 1 && 0 < col && board[row - 1][col - 1].getClass() != BlankSquare.class
                && board[row - 1][col - 1].getColor() != color) {
            list.add(new Position(row - 1, col - 1));
        } else if (0 <= row - 1 && 0 < col
                && board[row - 1][col - 1].getClass() == BlankSquare.class) {
            Move past = c.getMoves().peekLast();
            if (past != null && 7 - past.getEndPosition().getFirst() == row
                    && 7 - past.getEndPosition().getSecond() == col - 1
                    && 7 - past.getStartPosition().getFirst() == row - 2
                    && 7 - past.getStartPosition().getSecond() == col - 1
                    && board[7 - past.getEndPosition().getFirst()][7
                            - past.getEndPosition().getSecond()].getClass() == Pawn.class) {

                list.add(new Position(row - 1, col - 1));
            }
        }
        if (0 <= row - 1 && col < 7 && board[row - 1][col + 1].getClass() != BlankSquare.class
                && board[row - 1][col + 1].getColor() != color) {
            list.add(new Position(row - 1, col + 1));
        } else if (0 <= row - 1 && col < 7
                && board[row - 1][col + 1].getClass() == BlankSquare.class) {
            Move past = c.getMoves().peekLast();
            if (past != null && 7 - past.getEndPosition().getFirst() == row
                    && 7 - past.getEndPosition().getSecond() == col + 1
                    && 7 - past.getStartPosition().getFirst() == row - 2
                    && 7 - past.getStartPosition().getSecond() == col + 1
                    && board[7 - past.getEndPosition().getFirst()][7
                            - past.getEndPosition().getSecond()].getClass() == Pawn.class) {

                list.add(new Position(row - 1, col + 1));
            }
        }

        return list;
    }
}

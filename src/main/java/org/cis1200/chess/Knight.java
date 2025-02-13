package org.cis1200.chess;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Knight extends ChessPiece {
    public Knight(Color color, int x, int y) {
        super(color, x, y);
        setValue(3);
        try {
            if (color == Color.WHITE) {
                setImg(ImageIO.read(new File("files/wknight.png")));
            } else {
                setImg(ImageIO.read(new File("files/bknight.png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Position> calculateLegalMoves(Chess c) {
        ChessPiece[][] board = c.getBoard();
        Set<Position> legalMoves = new HashSet<Position>();
        int row = getRow();
        int col = getCol();
        Color color = getColor();
        for (int i : new int[] { -2, 2 }) {
            for (int j : new int[] { -1, 1 }) {
                if (0 <= row + i && row + i <= 7 && 0 <= col + j && col + j <= 7
                        && board[row + i][col + j].getColor() != color) {
                    legalMoves.add(new Position(row + i, col + j));
                }
            }
        }
        for (int j : new int[] { -2, 2 }) {
            for (int i : new int[] { -1, 1 }) {
                if (0 <= row + i && row + i <= 7 && 0 <= col + j && col + j <= 7
                        && board[row + i][col + j].getColor() != color) {
                    legalMoves.add(new Position(row + i, col + j));
                }
            }
        }
        return legalMoves;
    }
}

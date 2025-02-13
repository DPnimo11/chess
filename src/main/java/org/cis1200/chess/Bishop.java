package org.cis1200.chess;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Bishop extends ChessPiece {
    public Bishop(Color color, int x, int y) {
        super(color, x, y);
        setValue(3);
        try {
            if (color == Color.WHITE) {
                setImg(ImageIO.read(new File("files/wbishop.png")));
            } else {
                setImg(ImageIO.read(new File("files/bbishop.png")));
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
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                boolean flag = true;
                int multiplier = 1;
                while (flag && 0 <= row + dy * multiplier && row + dy * multiplier <= 7
                        && 0 <= col + dx * multiplier && col + dx * multiplier <= 7) {
                    if (board[row + dy * multiplier][col + dx * multiplier]
                            .getClass() != BlankSquare.class) {
                        if (board[row + dy * multiplier][col + dx * multiplier]
                                .getColor() != color) {
                            list.add(new Position(row + dy * multiplier, col + dx * multiplier));
                        }
                        flag = false;
                    } else {
                        list.add(new Position(row + dy * multiplier, col + dx * multiplier));
                    }
                    multiplier++;
                }
            }
        }
        return list;
    }
}

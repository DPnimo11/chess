package org.cis1200.chess;

import org.junit.jupiter.api.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class ChessTest {
    @Test
    public void movePawnValid() {
        Chess c = new Chess();
        c.action(6, 0, true);
        c.action(4, 0, true);
        ChessPiece p = c.getCell(3, 7);
        assertSame(p.getClass(), Pawn.class);
        assertTrue(p.hasMoved());
        assertEquals(p.getRow(), 3);
        assertEquals(p.getCol(), 7);
        assertSame(c.getCell(1, 7).getClass(), BlankSquare.class);
    }

    @Test
    public void movePawnInvalid() {
        Chess c = new Chess();
        c.action(6, 0, true);
        c.action(3, 0, true);
        ChessPiece p = c.getCell(6, 0);
        assertSame(p.getClass(), Pawn.class);
        assertFalse(p.hasMoved());
        assertEquals(p.getRow(), 6);
        assertEquals(p.getCol(), 0);
        assertSame(c.getCell(3, 0).getClass(), BlankSquare.class);
    }

    @Test
    public void moveRookBlocked() {
        Chess c = new Chess();
        c.action(7, 0, true);
        c.action(4, 0, true);
        ChessPiece p = c.getCell(7, 0);
        assertSame(p.getClass(), Rook.class);
        assertFalse(p.hasMoved());
        assertEquals(p.getRow(), 7);
        assertEquals(p.getCol(), 0);
        assertFalse(p.isHighlighted());
        assertSame(c.getCell(4, 0).getClass(), BlankSquare.class);
    }

    @Test
    public void changePlayer() {
        Chess c = new Chess();
        assertEquals(c.getPlayerTurn(), ChessPiece.Color.WHITE);
        c.action(6, 0, true);
        c.action(4, 0, true);
        assertEquals(c.getPlayerTurn(), ChessPiece.Color.BLACK);
        c.action(6, 0, true);
        c.action(4, 0, true);
        assertEquals(c.getPlayerTurn(), ChessPiece.Color.WHITE);
    }

    @Test
    public void check() {
        Chess c = new Chess();
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(7, 5, true);
        c.action(3, 1, true);
        assertTrue(c.isCheck());
    }

    @Test
    public void moveOutOfCheck() {
        Chess c = new Chess();
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(7, 5, true);
        c.action(3, 1, true);
        c.action(6, 5, true);
        assertEquals(c.getCell(5, 5).getClass(), Dot.class);
        assertEquals(c.getCell(4, 5).getClass(), BlankSquare.class);
    }

    @Test
    public void checkmate() {
        Chess c = new Chess();
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(6, 1, true);
        c.action(4, 1, true);
        c.action(6, 0, true);
        c.action(5, 0, true);
        c.action(6, 2, true);
        c.action(5, 2, true);
        c.action(7, 3, true);
        c.action(3, 7, true);
        assertTrue(c.isCheckmate());
    }

    @Test
    public void undo() {
        Chess c = new Chess();
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.undo();
        assertEquals(c.getPlayerTurn(), ChessPiece.Color.WHITE);
        assertEquals(c.getCell(4, 4).getClass(), BlankSquare.class);
        assertFalse(c.getCell(6, 4).hasMoved());
    }

    @Test
    public void resetTest() {
        Chess c = new Chess();
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(6, 4, true);
        c.action(4, 4, true);
        c.action(7, 5, true);
        c.action(3, 1, true);
        assertTrue(c.isCheck());
        c.resetBoard(false);
        assertEquals(c.getPlayerTurn(), ChessPiece.Color.WHITE);
        assertFalse(c.getCell(6, 4).hasMoved());
        assertFalse(c.isCheck());
    }

    @Test
    public void knightMoves() {
        Chess c = new Chess();
        c.action(7, 6, true);
        assertEquals(c.getCell(5, 5).getClass(), Dot.class);
        assertEquals(c.getCell(5, 7).getClass(), Dot.class);
    }
}

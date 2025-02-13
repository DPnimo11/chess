package org.cis1200.chess;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
public class GameBoard extends JPanel {

    private Chess c; // model for the game
    private JLabel status; // current status text
    private int timeWhite;
    private int timeBlack;
    private javax.swing.Timer timerWhite;
    private javax.swing.Timer timerBlack;
    private static final int INTERVAL = 1000;
    private LinkedList<int[]> times;
    private boolean started;
    private boolean timed;

    // Game constants
    public static final int BOARD_WIDTH = 560;
    public static final int BOARD_HEIGHT = 560;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        c = new Chess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        times = new LinkedList<>();
        times.add(new int[] { timeWhite, timeBlack });
        timerWhite = new javax.swing.Timer(INTERVAL, e -> {
            if (started && c.getPlayerTurn() == ChessPiece.Color.WHITE && timeWhite > 0) {
                timeWhite--;
            }
            updateStatus();
        });
        timerBlack = new javax.swing.Timer(INTERVAL, e -> {
            if (started && c.getPlayerTurn() == ChessPiece.Color.BLACK && timeBlack > 0) {
                timeBlack--;
            }
            updateStatus();
        });
        timerWhite.start();
        timerBlack.start();
        started = false;
        timed = false;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (!started) {
                    if ((BOARD_WIDTH - 106) / 2 <= p.x && p.x <= (BOARD_WIDTH - 106) / 2 + 106) {
                        if (335 <= p.y && p.y <= 355) {
                            started = true;
                            timed = false;
                        } else if (365 <= p.y && p.y <= 385) {
                            started = true;
                            timed = true;
                            timeBlack = 300;
                            timeWhite = 300;
                        } else if (395 <= p.y && p.y <= 415) {
                            started = true;
                            timed = true;
                            timeWhite = 600;
                            timeBlack = 600;
                        }
                    }
                } else if (!c.isCheckmate() && !c.isStalemate() && timeBlack > 0 && timeWhite > 0) {
                    ChessPiece.Color before = c.getPlayerTurn();
                    c.action(p.y / 70, p.x / 70, true);
                    if (before != c.getPlayerTurn() && before == ChessPiece.Color.BLACK) {
                        times.add(new int[] { timeWhite, timeBlack });
                    } else if (before != c.getPlayerTurn() && before == ChessPiece.Color.WHITE) {
                        times.add(new int[] { timeWhite, timeBlack });
                    }
                    if (c.getSelected() == null) {
                        updateStatus();
                    }
                }
                repaint();
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        timeWhite = 600;
        timeBlack = 600;
        times = new LinkedList<>();
        times.add(new int[] { timeWhite, timeBlack });
        started = false;
        timed = false;

        c.resetBoard(false);
        status.setText("Pick a mode!");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        c.undo();
        if (times.size() > 1) {
            int[] lastTime = times.get(times.size() - 2);
            times.removeLast();
            timeWhite = lastTime[0];
            timeBlack = lastTime[1];
        }

        updateStatus();
        repaint();

        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        String text = "";

        if (started) {
            if (c.getPlayerTurn() == ChessPiece.Color.WHITE) {
                text = "White's Turn";
            } else {
                text = "Black's Turn";
            }

            if (c.isCheck()) {
                text += ": Check!";
            }

            if (c.isCheckmate() && c.getPlayerTurn() == ChessPiece.Color.WHITE) {
                text = "Black wins by checkmate!";
            } else if (c.isCheckmate()) {
                text = "White wins by checkmate!";
            } else if (c.isStalemate()) {
                text = "Stalemate!";
            }

            if (timed) {
                if (timeWhite <= 0) {
                    text = "Black wins on time!";
                } else if (timeBlack <= 0) {
                    text = "White wins on time!";
                }
                if (timeWhite % 60 < 10) {
                    text += "    White: " + timeWhite / 60 + ":0" + timeWhite % 60;
                } else {
                    text += "    White: " + timeWhite / 60 + ":" + timeWhite % 60;
                }
                if (timeBlack % 60 < 10) {
                    text += "    Black: " + timeBlack / 60 + ":0" + timeBlack % 60;
                } else {
                    text += "    Black: " + timeBlack / 60 + ":" + timeBlack % 60;
                }
            }
            status.setText(text);
        }

    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!started) {
            // g.setColor(new Color(232, 220, 202));
            // g.fillRect(0, 0, getWidth(), getHeight());
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("files/chess2.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            g.drawImage(img, 180, 50, 200, 200, null);
            g.setColor(Color.BLACK);
            String paragraph = "Welcome to this pass and play implementation of Chess!&" +
                    "It follows the traditional rules of chess. " +
                    "Try to win by checkmate or timeout.&" +
                    "Choose your time constraint (per person) to begin!";
            // Set font and metrics
            Font font = new Font("Times New Roman", Font.PLAIN, 18);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);

            // Split paragraph into lines
            String[] lines = paragraph.split("&");

            // Calculate starting point for vertical centering
            int totalHeight = lines.length * metrics.getHeight();
            int y = (getHeight() - totalHeight) / 2;

            // Draw each line centered horizontally
            for (String line : lines) {
                int textWidth = metrics.stringWidth(line);
                int x = (getWidth() - textWidth) / 2;
                g.drawString(line, x, y);
                y += metrics.getHeight();
            }
            // metrics.stringWidth("No Time Limit") = 106
            // metrics.stringWidth("No Time Limit") = 68
            // metrics.stringWidth("10 minutes") = 77
            g.drawString("No Time Limit", (BOARD_WIDTH - 106) / 2, 350);
            g.drawRect((BOARD_WIDTH - 106) / 2 - 5, 335, 106 + 10, 20);
            g.drawString("5 minutes", (BOARD_WIDTH - 68) / 2, 380);
            g.drawRect((BOARD_WIDTH - 106) / 2 - 5, 365, 106 + 10, 20);
            g.drawString("10 minutes", (BOARD_WIDTH - 77) / 2, 410);
            g.drawRect((BOARD_WIDTH - 106) / 2 - 5, 395, 106 + 10, 20);
        } else { // Draws board grid
            int unitWidth = BOARD_WIDTH / 8;
            int unitHeight = BOARD_HEIGHT / 8;

            g.setColor(new Color(238, 238, 210));
            for (int row = 0; row < 8; row += 2) {
                for (int col = 0; col < 8; col += 2) {
                    g.fillRect(unitWidth * col, unitHeight * row, unitWidth, unitHeight);
                }
            }
            for (int row = 1; row < 8; row += 2) {
                for (int col = 1; col < 8; col += 2) {
                    g.fillRect(unitWidth * col, unitHeight * row, unitWidth, unitHeight);
                }
            }
            g.setColor(new Color(118, 150, 86));
            for (int row = 0; row < 8; row += 2) {
                for (int col = 1; col < 8; col += 2) {
                    g.fillRect(unitWidth * col, unitHeight * row, unitWidth, unitHeight);
                }
            }
            for (int row = 1; row < 8; row += 2) {
                for (int col = 0; col < 8; col += 2) {
                    g.fillRect(unitWidth * col, unitHeight * row, unitWidth, unitHeight);
                }
            }

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    ChessPiece p = c.getCell(row, col);
                    p.draw(g);
                }
            }
        }

    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}

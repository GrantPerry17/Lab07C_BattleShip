
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BattleshipGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Game game;
    private final JButton[][] cellButtons;

    private JPanel headerPanel;
    private JLabel missLabel;
    private JLabel strikeLabel;
    private JLabel totalMissLabel;
    private JLabel totalHitLabel;

    public BattleshipGUI() {
        game = new Game();
        cellButtons = new JButton[Board.SIZE][Board.SIZE];

        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(750, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        headerPanel = new JPanel(new BorderLayout(5, 5));

        createTitle();
        createCounters();
        createBoard();
        createBottomButtons();

        add(headerPanel, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmQuit();
            }
        });

        updateCounters();
    }

    private void createTitle() {
        JLabel titleLabel = new JLabel(
            "BATTLESHIP", SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(
            BorderFactory.createEmptyBorder(12, 0, 5, 0)
        );

        headerPanel.add(titleLabel, BorderLayout.NORTH);
    }

    private void createCounters() {
        JPanel counterPanel = new JPanel(
            new GridLayout(2, 2, 10, 5)
        );

        counterPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        );

        missLabel = new JLabel();
        strikeLabel = new JLabel();
        totalMissLabel = new JLabel();
        totalHitLabel = new JLabel();

        counterPanel.add(missLabel);
        counterPanel.add(strikeLabel);
        counterPanel.add(totalMissLabel);
        counterPanel.add(totalHitLabel);

        JPanel informationPanel = new JPanel(
            new BorderLayout(5, 5)
        );

        JLabel instructionLabel = new JLabel(
            "Select a cell to fire a missile.",
            SwingConstants.CENTER
        );

        instructionLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        informationPanel.add(
            instructionLabel, BorderLayout.NORTH
        );
        informationPanel.add(
            counterPanel, BorderLayout.CENTER
        );

        headerPanel.add(
            informationPanel, BorderLayout.CENTER
        );
    }

    private void createBoard() {
        JPanel boardPanel = new JPanel(
            new GridLayout(Board.SIZE, Board.SIZE, 2, 2)
        );

        boardPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        );

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {

                JButton button = new JButton("~");

                button.setFont(
                    new Font("Arial", Font.BOLD, 20)
                );

                button.setForeground(Color.BLUE);
                button.setBackground(
                    new Color(173, 216, 230)
                );

                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setBorderPainted(true);

                final int selectedRow = row;
                final int selectedCol = col;

                button.addActionListener(e ->
                    fireAt(selectedRow, selectedCol)
                );

                cellButtons[row][col] = button;
                boardPanel.add(button);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
    }

    private void createBottomButtons() {
        JPanel bottomPanel = new JPanel();

        JButton playAgainButton = new JButton("Play Again");
        JButton quitButton = new JButton("Quit");

        playAgainButton.addActionListener(e ->
            confirmPlayAgain()
        );

        quitButton.addActionListener(e ->
            confirmQuit()
        );

        bottomPanel.add(playAgainButton);
        bottomPanel.add(quitButton);

        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 15, 10)
        );

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void fireAt(int row, int col) {
        if (game.isGameOver()) {
            return;
        }

        Game.ShotResult result = game.fireShot(row, col);

        if (result.alreadyFired) {
            return;
        }

        JButton button = cellButtons[row][col];
        button.setEnabled(false);

        if (result.hit) {
            button.setText("X");
            button.setForeground(Color.RED);
            button.setBackground(new Color(255, 200, 200));
        } else {
            button.setText("M");
            button.setForeground(new Color(160, 100, 0));
            button.setBackground(new Color(255, 235, 120));
        }

        updateCounters();

        if (result.shipSunk) {
            JOptionPane.showMessageDialog(
                this,
                "You sunk a ship of length "
                    + result.shipLength + "!",
                "Ship Sunk!",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        if (result.won) {
            endGame(
                "Congratulations! You sank every ship and won!"
            );
        } else if (result.lost) {
            endGame(
                "Game over! You received 3 strikes."
            );
        }
    }

    private void updateCounters() {
        missLabel.setText(
            "Consecutive Misses: "
                + game.getMissCounter() + " / 5"
        );

        strikeLabel.setText(
            "Strikes: "
                + game.getStrikeCounter() + " / 3"
        );

        totalMissLabel.setText(
            "Total Misses: "
                + game.getTotalMisses() + " / 83"
        );

        totalHitLabel.setText(
            "Total Hits: "
                + game.getTotalHits() + " / 17"
        );
    }

    private void endGame(String message) {
        disableBoard();

        int choice = JOptionPane.showConfirmDialog(
            this,
            message + "\n\nWould you like to play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    private void confirmPlayAgain() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to start a new game?",
            "Play Again",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    private void startNewGame() {
        game.startNewGame();

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                JButton button = cellButtons[row][col];

                button.setText("~");
                button.setForeground(Color.BLUE);
                button.setBackground(
                    new Color(173, 216, 230)
                );
                button.setEnabled(true);
            }
        }

        updateCounters();
    }

    private void disableBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                cellButtons[row][col].setEnabled(false);
            }
        }
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to quit Battleship?",
            "Quit Game",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}

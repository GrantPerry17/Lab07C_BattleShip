
import javax.swing.SwingUtilities;

public class Battleship {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BattleshipGUI gui = new BattleshipGUI();
            gui.setVisible(true);
        });
    }
}

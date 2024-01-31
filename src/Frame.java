import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class Frame extends JFrame {
    public Frame() {
        this.setTitle("Snake Game");
        this.setIconImage(new ImageIcon("icon\\icon.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new Panel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

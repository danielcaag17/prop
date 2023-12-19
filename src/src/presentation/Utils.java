package src.presentation;

import java.awt.*;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Utils {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenWidth = (int) screenSize.getWidth();
    private static int screenHeight = (int) screenSize.getHeight();
    private static int elementHeight = 100;     // Altura d'element de les vistes de llista
    
    // potser no cal
    // private static int elementWidth = 100;      // Altura d'element de les vistes de llista


    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    // potser privat
    public static Font getFontTitle() {
        // A definir
        return new Font("Serif", Font.BOLD, 30);
    }

    // potser privat
    public static Font getFontText() {
        // A definir
        return new Font("Serif", Font.PLAIN, 16);
    }

    public static int getElementHeight() {
        return elementHeight;
    }

    public static Color getBackgroundColorVista() {
        // A redefinir
        return Color.white;
    }

    public static Color getBackgroundColorElement() {
        // A redefinir
        return Color.lightGray;
    }

    //potser privat
    public static ImageIcon getBackImage() {
        // descarregar imatge + path + definir nom carpeta
        String path = ".." + File.separator + ".." + File.separator + "data/imatges/backArrow.png";
        return new ImageIcon(path);
    }

    public static JLabel initLabel(String name, String type) {
        JLabel label = new JLabel();
        label.setText(name);
        label.setVisible(true);
        if (type.equals("title")) label.setFont(getFontTitle());
        else if (type.equals("text")) label.setFont(getFontText());
        return label;
    }

    public static JFrame initFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(Utils.getScreenWidth(), Utils.getScreenHeight());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        // frame.setIconImage(null);        // Si volem que surti algun logo en la part superior esquerra
        return frame;
    }

    public static JButton backButton() {
        JButton back = new JButton();
        ImageIcon imageIcon = getBackImage();
        Image image = imageIcon.getImage();
        // A definir quina es la mida del botó
        image = image.getScaledInstance(100, 50, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        back.setIcon(imageIcon);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));   // Donar sensació al usuari
        back.setContentAreaFilled(false);               // Només es vegi la foto i no el botó
        back.setBorder(null);                   
        // back.addActionListener(e -> canviStage());     // Potser cada classe que té un back ha de fer aquest addActionListener

        //Potser un setPos, es a dir, dreta / esquerra (on es troba el button)

        return back;
    }
}
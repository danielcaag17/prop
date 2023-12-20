package src.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.exceptions.TeclatNoExisteix;

public class CtrlPreMostrarTeclat {
    private CtrlPresentacio ctrlPresentacio;
    private JLabel títol, nomTeclat;
    private JButton descartar, confirmar;
    private JFrame vista;
    private JPanel PTítol, PSouth, PNom, PCenter, PTeclat;
    private String teclat;
    private char[][] distribucio;

    public CtrlPreMostrarTeclat(String teclat) {
        this.teclat = teclat;
        ctrlPresentacio = CtrlPresentacio.getInstance();
        init();
        addElementsFrame();
    }

    private void init() {
        títol = Utils.initLabel("Previsualitzar Teclat", "title");
        nomTeclat = Utils.initLabel(teclat, "text");

        descartar = Utils.Button(null, "cross");
        descartar.addActionListener(e -> ctrlPresentacio.elimina("Teclat", teclat, vista, "LlistaTeclats"));
        confirmar = Utils.Button(null, "check");
        confirmar.addActionListener(e -> Utils.canviPantalla(vista, "LlistaTeclats"));

        try {
            distribucio = ctrlPresentacio.getDistribucio(teclat);
        } catch (TeclatNoExisteix e) {
            ctrlPresentacio.Excepcio(vista, e.getTipus(), "Teclat " + teclat + " no exiteix");
        }
        PTeclat = Utils.JPanel(new GridLayout(distribucio.length, distribucio[0].length), null);
        for (int i = 0; i < distribucio.length; i++) {
            for (int j = 0; j < distribucio[i].length; j++) {
                String character = String.valueOf(distribucio[i][j]);
                System.out.println(character);
                if (character.equals("-")) PTeclat.add(Utils.initLabel("", ""));
                else PTeclat.add(Utils.Button(character, null));
            }
        }

        PTítol = Utils.JPanel(null, new Dimension(Utils.getScreenWidth(),Utils.getScreenHeight()/6));
        PTítol.add(títol);

        PNom = new JPanel();
        PNom.add(nomTeclat);

        PCenter = Utils.JPanel(new BorderLayout(), null);
        PCenter.add(PNom, BorderLayout.NORTH);
        PCenter.add(PTeclat, BorderLayout.CENTER);

        PSouth = Utils.JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10), new Dimension(Utils.getScreenWidth(),Utils.getScreenHeight()/6));
        PSouth.add(descartar);
        PSouth.add(confirmar);

        vista = Utils.initFrame("PreMostrarTeclat");
    }

    private void addElementsFrame() {
        vista.add(PTítol, BorderLayout.NORTH);
        vista.add(PCenter, BorderLayout.CENTER);
        vista.add(PSouth, BorderLayout.SOUTH);
    }
}

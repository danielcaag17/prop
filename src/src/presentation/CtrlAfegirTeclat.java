package src.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.exceptions.Excepcions;

public class CtrlAfegirTeclat {
    private CtrlPresentacio ctrlPresentacio;
    private JLabel title, labelIndicaNomTeclat, labelLoading;
    private JTextField textFieldNomTeclat;
    private JComboBox<String> listAlfabets, listGeneradors;
    private JButton cancelar, confirmar;
    private JFrame vista;
    private JPanel PTítol, PSouth, PCenter;

    public CtrlAfegirTeclat() {
        ctrlPresentacio = CtrlPresentacio.getInstance();
        initElements();
        initPanels();
        addElementsFrame();
    }

    private void initElements() {
        title = Utils.initLabel("Crear teclat", "title");

        labelIndicaNomTeclat = Utils.initLabel("Indica el nom del teclat: ", "text");
        textFieldNomTeclat = new JTextField();
        textFieldNomTeclat.setPreferredSize(new Dimension(200, 50));
        textFieldNomTeclat.setFont(Utils.getFontText());
        // Afegir document listener per detectar canvis en el textField i així acitvar o desactivar el botó de confirmar
        afegirDocumentListener();

        String[] arrayGeneradors = ctrlPresentacio.getListGeneradors();
        listGeneradors = new JComboBox<>(arrayGeneradors);

        String[] arrayAlfabets = ctrlPresentacio.getNomAlfabets();
        listAlfabets = new JComboBox<>(arrayAlfabets);        

        cancelar = Utils.Button("Cancelar", null);
        cancelar.addActionListener(e -> Utils.canviPantalla(vista, "LlistaTeclats"));

        confirmar = Utils.Button("Confirmar", null);
        confirmar.setEnabled(false);
        confirmar.addActionListener(e -> PreMostrarTeclat());

        String path = ".." + File.separator + ".." + File.separator + "data/imatges/loading.gif";
        ImageIcon gifIcon = new ImageIcon(path);
        labelLoading = new JLabel(gifIcon);
        labelLoading.setVisible(false);
        labelLoading.setPreferredSize(new Dimension(200, 200));
    }

    private void initPanels() {
        // Veure si cal un Panel per un Label
        PTítol = Utils.JPanel(null, new Dimension(Utils.getScreenWidth(),Utils.getScreenHeight()/6));
        PTítol.add(title);

        JPanel PIndicaNomAlfabet = new JPanel();
        PIndicaNomAlfabet.add(labelIndicaNomTeclat);
        PIndicaNomAlfabet.add(textFieldNomTeclat);        

        JPanel PAfegir = new JPanel();
        PAfegir.add(PIndicaNomAlfabet);
        PAfegir.add(listGeneradors);
        PAfegir.add(listAlfabets);

        JPanel PCarregant = new JPanel();
        PCarregant.add(labelLoading);

        PCenter = Utils.JPanel(new BorderLayout(), null);
        PCenter.add(PAfegir, BorderLayout.NORTH);
        PCenter.add(PCarregant, BorderLayout.CENTER);

        PSouth = Utils.JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10), new Dimension(Utils.getScreenWidth(),Utils.getScreenHeight()/6));
        PSouth.add(cancelar);
        PSouth.add(confirmar);
    }

    private void addElementsFrame() {
        vista = Utils.initFrame("AfegirTeclat");
        vista.add(PTítol, BorderLayout.NORTH);
        vista.add(PCenter, BorderLayout.CENTER);
        vista.add(PSouth, BorderLayout.SOUTH);
    }

    private void PreMostrarTeclat() {
        labelLoading.setVisible(true);
        String alfabet = (String) listAlfabets.getSelectedItem();
        String generador = (String) listGeneradors.getSelectedItem();
        String nomTeclat = textFieldNomTeclat.getText();
        try {
            ctrlPresentacio.crearNouTeclat(nomTeclat, alfabet, generador);
            
        } catch (Excepcions e) {
            String msg = "";
            switch (e.getTipus()) {
                case "TeclatJaExisteix": 
                    msg = "Ja existeix un Teclat amb el nom " + nomTeclat;
                    break;
                default:
                    msg = e.getMessage();
                    break;
            }
            ctrlPresentacio.Excepcio(vista, e.getTipus(), msg);
        }
        catch(Exception e) {
            ctrlPresentacio.Excepcio(vista, "Error: ", e.getMessage());;
        }
        labelLoading.setVisible(false);
        Utils.canviPantallaElementMostrar(vista, "PreMostrarTeclat", nomTeclat);
    }

    private void afegirDocumentListener() {
        textFieldNomTeclat.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (! textFieldNomTeclat.getText().equals("")) confirmar.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (textFieldNomTeclat.getText().equals("")) confirmar.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'changedUpdate'");
            }
            
        });  
    }
}

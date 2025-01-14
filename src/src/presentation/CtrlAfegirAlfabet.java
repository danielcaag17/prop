package src.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.exceptions.Excepcions;

public class CtrlAfegirAlfabet {
    private CtrlPresentacio ctrlPresentacio;            // instancia unica de ctrlPresentacio
    private JLabel title, labelIndicaNomAlfabet;        // títol de la pantalla + label informativa per indicar el nom de l'Alfabet
    private JTextField textFieldNomAlfabet;             // textField pel nom de l'Alfabet
    private JComboBox<String> listTipusEntrada;         // llista que es mostra amb els diferents tipus d'entrada                    
    private String nomAlfabet;                          // nom de l'alfabet escrit per l'usuari                            
    private String tipusEntrada;                        // tipus d'entrada seleccionat per l'usuari
    private String path;                                // path on es troba el fitxer
    private JFileChooser fileChooser;                   // file chooser per indicar el fitxer d'entrada
    private JButton cancelar, confirmar, openFile;      // butons per cancelar, confirmar o obrir un fitxer
    private JFrame vista;                               // pantalla on es mostren tots els elements
    private JPanel PNorth;                              // panel amb el títol
    private JPanel PCenter, PSouth;                     // panel central amb la informació necessaria per a crear un Alfabet + panel amb botons cancelar i confirmar
    private Boolean pathSelected = false;               // controlar si s'ha seleccionat un fitxer o no

    // Elements declarats segons com es veuen en la pantalla de dalt a baix
    public CtrlAfegirAlfabet() {
        ctrlPresentacio = CtrlPresentacio.getInstance();
        initElements();
        initPanels();
        addElementsFrame();
    }

    private void initElements() {
        title = Utils.initLabel("Afegir alfabet", "title");

        labelIndicaNomAlfabet = Utils.initLabel("Indica el nom de l'alfabet: ", "text");

        textFieldNomAlfabet = new JTextField();
        textFieldNomAlfabet.setPreferredSize(new Dimension(200, 50));
        textFieldNomAlfabet.setFont(Utils.getFontText());
        // Afegir document listener per detectar canvis en el textField i així acitvar o desactivar el botó de confirmar
        afegirDocumentListener();
        nomAlfabet = "";

        String[] arrayTipusEntrada = ctrlPresentacio.getListTipusEntrada();  // array que conte els diferents tipus d'entrada
        listTipusEntrada = new JComboBox<>(arrayTipusEntrada);

        openFile = Utils.Button("Selecciona fitxer", null);
        openFile.addActionListener(e -> seleccionaFitxer());

        cancelar = Utils.Button("Cancelar", null);
        cancelar.addActionListener(e -> Utils.canviPantalla(vista, "LlistaAlfabets"));

        confirmar = Utils.Button("Confirmar", null);
        confirmar.setEnabled(false);
        confirmar.addActionListener(e -> confirmar());
    }

    private void initPanels() {
        PNorth = Utils.JPanel(null, new Dimension(Utils.getScreenWidth(),Utils.getScreenHeight()/6));
        PNorth.add(title);

        JPanel PIndicaNomAlfabet = new JPanel();                    //  panel que inclou els elements per indicar el nom de l'Alfabet
        PIndicaNomAlfabet.add(labelIndicaNomAlfabet);
        PIndicaNomAlfabet.add(textFieldNomAlfabet);

        // FALTA POSAR MILLOR LA UI
        PCenter = new JPanel();
        PCenter.add(PIndicaNomAlfabet);
        PCenter.add(listTipusEntrada);
        PCenter.add(openFile);

        PSouth = Utils.JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10), new Dimension(Utils.getScreenWidth(),Utils.getScreenHeight()/6));
        PSouth.add(cancelar);
        PSouth.add(confirmar);
    }

    private void addElementsFrame() {
        vista = Utils.initFrame("AfegirAlfabet");
        vista.add(PNorth, BorderLayout.NORTH);
        vista.add(PCenter, BorderLayout.CENTER);
        vista.add(PSouth, BorderLayout.SOUTH);
    }

    //Es vol obrir un fitxer
    private void seleccionaFitxer() {
        fileChooser = new JFileChooser();
        // Obrir el file chooser directament en el directori actual
        fileChooser.setCurrentDirectory(new File("../../test/exemples_input_alfabet"));
        int response = fileChooser.showOpenDialog(null);
        // S'ha obert un fitxer
        if (response == JFileChooser.APPROVE_OPTION) {
            if (! nomAlfabet.equals("")) confirmar.setEnabled(true);
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            path = file.toString();
            pathSelected = true;
            String nomFile = fileChooser.getSelectedFile().toString();
            openFile.setText(nomFile);
        }
        // Desactivar el botó de confirmar
        else {
            pathSelected = false;
            confirmar.setEnabled(false);
            openFile.setText("Selecciona fitxer");
        }
    }

    // Es vol crear un nou alfabet amb les dades indicades per l'usuari
    private void confirmar() {
        nomAlfabet = textFieldNomAlfabet.getText();
        if (nomesEspaisBlanc(nomAlfabet)) ctrlPresentacio.Excepcio(vista, "NomAlfabetNoValid", "No es pot crear un alfabet amb aquest nom");
        else {
            tipusEntrada = (String) listTipusEntrada.getSelectedItem();
            try {
                ctrlPresentacio.afegirAlfabet(nomAlfabet, tipusEntrada, path);
                System.out.println("Alfabet " + nomAlfabet + " -> creat");
                ctrlPresentacio.Excepcio(vista, "AlfabetCreat", "Alfabet " + nomAlfabet + " creat correctament");
            }
            catch(Exception e) {
                String msg = "";
                String tipus = "";
                if (e instanceof FileNotFoundException) {
                    msg = e.getMessage();
                    tipus = "FileNotFoundException";
                }
                else if (e instanceof Excepcions) {
                    tipus = ((Excepcions) e).getTipus();
                    switch (((Excepcions) e).getTipus()) {
                        case "FormatDadesNoValid":
                            msg = e.getMessage();
                            break;
                        case "EntradaLlegidaMalament":
                            msg = "Hi ha hagut un error en la lectura de les dades";
                            break;
                        default:
                            msg = e.getMessage();
                    }
                }
                ctrlPresentacio.Excepcio(vista, tipus, msg);
            }
        }

        Utils.canviPantalla(vista, "LlistaAlfabets");
    }

    private void afegirDocumentListener() {
        textFieldNomAlfabet.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                nomAlfabet = textFieldNomAlfabet.getText();
                if (! nomAlfabet.equals("") && pathSelected) confirmar.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                nomAlfabet = textFieldNomAlfabet.getText();
                if (nomAlfabet.equals("")) confirmar.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'changedUpdate'");
            }
        });
    }

    private Boolean nomesEspaisBlanc(String nom) {
        for (int i = 0; i < nom.length(); i++) {
            char c = nom.charAt(i);
            if (c != ' ') return false;
        }
        return true;
    }
}
package src.domain.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import src.exceptions.FormatDadesNoValid;

public class Text implements StrategyAlfabet {

    @Override
    public Alfabet read (String path) throws FormatDadesNoValid, FileNotFoundException {
        String text = getText(path);
        int lenght = text.length();
        Alfabet a = new Alfabet();
        Map<Character, Double> map = new HashMap<>();
        map = a.processCharacters(text, lenght, map);
        a.setSize(map.size());
        double[][] matrix = new double[map.size()][map.size()];
        // tota la matriu ha desta a 0
        matrix = a.processFrequencies(text, lenght, matrix);
        matrix = a.calculateFrecuencies(map, matrix);
        map = a.calculateCharacters(lenght, map);

        a.setCharacters(map);
        a.setFrequencies(matrix);
        return a;
    }

    private String getText (String path) throws FormatDadesNoValid, FileNotFoundException {
        String text = "";
        File file = new File(path);
        try (Scanner myReader = new Scanner(file)) {
            while (myReader.hasNext()) {
                String input = myReader.next();
                try {
                    Float.parseFloat(input); // Tractar excepció que pugui donar parseFloat !!!
                    throw new FormatDadesNoValid();
                }
                catch(NumberFormatException e) {    // String no es un Float
                    // do nothing, significa que el format és correcte
                    // fer un contador perque no sempre entri a fer la conversió
                    // si han passat 20 strings i no hi ha error deixar de convertir a Float
                }
                text += input.toLowerCase();
            }
            myReader.close();
        }
        return text;
    }
    
}

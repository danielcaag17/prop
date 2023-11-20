package src.domain.classes;
import src.exceptions.*;

import java.io.*;
import java.util.*;

public class Alfabet {
    private String nom;                             // Clau primària de la classe Alfabet
    private Map<Character, Double> characters;      // Relació de cada caràcter de l'Alfaber amb la seva probabilitat
    private double[][] frequencies;                 // Matriu de probabilitats, donat un caràcter saber quina probabilitat que el següent sigui un altre
    private int size;                               // Mida de l'Alfabet, els caràcters que té
    private char[] abecedari;                       // Array de caràcters per guardar les lletres de l'Alfabet
    private StrategyAlfabet strategy;               // Interfície StrategyAlfabet per llegir diferents tipus d'entrada

    // Pre:
    // Post: s'ha creat una instància d'Alfabet amb size = 0
    public Alfabet () {
        setSize(0);
    }

    // Pre: no existeix cap Alfabet amb aquest nom
    // Post: s'ha creat una instància d'Alfabet amb nom = nom i size = 0
    public Alfabet (String nom) {
        setNom(nom);
        setSize(0);
    }

    /**
     * Pre:
     * Post: s'ha llegit l'entrada i s'han guardat les dades necessàries
     * 
     * @param ta tipus de les dades per crear l'Alfabet i l'estrategia a utilitzar.
     * @param pf path al fitxer on hi ha guardades les dades.
     * @throws EntradaLlegidaMalament
     */
    public void readInput (String ta, String path) throws FormatDadesNoValid, TipusDadesNoValid, FileNotFoundException, EntradaLlegidaMalament {
        setStrategy(ta);                            // Crear la instància de StrategyAlfabet a utilitzar segons ta
        Alfabet a = strategy.read(path);            // Llegir i processar les dades del fitxer que es troba a path
        
        this.characters = a.characters;             // Guardar al propi Alfabet les dades calculades
        this.frequencies = a.frequencies;
        setSize(a.size);
    }

    /**
     * Pre: el text només contè lletres sense espai i en minúscula,
     *      la longitud del text és igual a length
     * Post: s'ha processat el text passat i s'ha guardat el nombre d'aparicions de cada caràcter
     * 
     * @param text cadena de caràcters a processar.
     * @param length longitud del text.
     * @param map estructura de dades on es guarda el nombre d'aparicions d'una lletra.
     * 
     * @return la relació entre caràcter i les vegades que apareix.
     */
    public Map<Character, Double> processCharacters (String text, int length, Map<Character, Double> map) {
        for (int i = 0; i < length; i++) {
            Character c = text.charAt(i);             // Agafar la lletra que es troba en la posició i del text
            if (! map.containsKey(c)) {               // Comprovar que la lletra c no s'ha vist encara
                map.put(c, 0.0);;                     // Crear una nova entrada per aquesta lletra c amb 0 aparicions
            }
            double a = map.get(c);                    // Obtenir el nombre d'aparicions de la lletra c
            map.replace(c, a+1);                      // Sumar 1 a les aparicions de la lletra c
        }
        return map;
    }

    /**
     * Pre: el text només contè lletres sense espais i en minúscula,
     *      la longitud del text és igual a length,
     *      matrix és una matriu de la mida de l'Alfabet
     * Post: s'han contat les vegades que donada una lletra aparegui la següent
     * 
     * @param text cadena de caràcters a processar.
     * @param length longitud del text.
     * @param matrix matriu on es guarda la probabilitat que donada una lletra aparegui la següent.
     * 
     * @return la matriu de probabilitats.
     * 
     * @throws EntradaLlegidaMalament si la lletra c no es troba dins de l'abecedari
     */
    public double[][] processFrequencies (String text, int length, double[][] matrix) throws EntradaLlegidaMalament {
        for (int i = 0; i < length - 1; i++) {          // Recorregut del text fins la lletra anterior a la última
            char c = text.charAt(i);                    // Agafar la lletra actual
            char next = text.charAt(i+1);               // Agafar la següent lletra

            int j = findIndex(c);                       //Trobar els índexs de la matriu per la cada lletra
            int k = findIndex(next);
            matrix[j][k]++;                             // Sumar 1 aparició a que després de la lletra j apareix la lletra k
        
        }
        return matrix;
    }

    /**
     * Pre: c pertany a l'Alfabet
     * Post: s'ha retornat l'índex de la lletra c en l'abecedari
     * 
     * @param c lletra c que pertany a l'Alfabet 
     * 
     * @return l'índex de la lletra c en l'abecedari
     * 
     * @throws EntradaLlegidaMalament si la lletra c no es troba dins de l'abecedari
     */
    private int findIndex (char c) throws EntradaLlegidaMalament {
        for (int i = c - 'a'; i >= 0; i--) {
            if (i < abecedari.length && abecedari[i] == c) {
                return i;
            }
        }
        throw new EntradaLlegidaMalament();
    }

    /**
     * Pre: matrix és una matriu de la mida de l'Alfabet,
     *      map té la mida de l'Alfabet
     * Post: s'ha calculat la probabilitat que donada una lletra aparegui la següent
     * 
     * @param matrix matriu on es guarda la probabilitat que donada una lletra aparegui la següent.
     * 
     * @return la matriu de probabilitats.
     * 
     * @throws EntradaLlegidaMalament si la lletra c no es troba dins de l'abecedari 
     */
    public double[][] calculateFrequencies (double[][] matrix) throws EntradaLlegidaMalament {
        for (Character c : characters.keySet()) {
            double nAparicions = characters.get(c);                            // Obtenir el nombre d'aparicions de la lletra c
            if (nAparicions != 0) {                                     // No hauria de passar, però vigilar les divisions entre 0
                for (int i = 0; i < characters.size(); i++) {                  // Pre: size és de la mateixa mida que la matriu
                    int j = findIndex(c);                                    // Passar la lletra a int per tenir quin és el seu índex a la matriu
                    matrix[j][i] /= nAparicions;          // Dividir tota la fila de matrix pel nombre de vegades que apareix
                }
            }
        }
        return matrix;
    }

    /**
     * Pre: map té la mida de l'Alfabet,
     *      la suma de totes les aparicions de cada lletra és igual a length
     * Post: s'ha calculat la freqüència de cada lletra en l'Alfabet
     * 
     * @param length longitud de les dades amb les que s'ha creat l'Alfabet
     * @param map estructura de dades on es guarda la freqüència d'una lletra.
     * 
     * @return la relació entre caràcter i les vegades que apareix.
     */
    public Map<Character, Double> calculateCharacters (int length, Map<Character, Double> map) {     //ull amb totes les divisions, EXC si es entre 0 !!
        for (Character c : map.keySet()) {
            double nAparicions = map.get(c);                // Obtenir les aparicions de la lletra c
            double probabilitat = nAparicions / length;     // Calcular la probabilitat de la lletra c
            map.replace(c, probabilitat);                   // Associar la lletra amb la seva probabilitat
        }
        return map;
    }

    /**
     * Pre: 
     * Post: s'ha instanciat la classe StrategyAlfabet pertinent
     * 
     * @param strategy string per identificar quina estrategia utilitzar.
     * 
     * @throws TipusDadesNoValid si no hi ha un tipus d'estrategia demanat per strategy.
     */
    private void setStrategy (String strategy) throws TipusDadesNoValid {
        switch (strategy) {
            case "text":
                this.strategy = (StrategyAlfabet) new Text();
                break;
            case "llista-paraules":
                this.strategy = (StrategyAlfabet) new Words();
                break;
            default:
                throw new TipusDadesNoValid();
        }
    }

    /**
     * Pre: 
     * Post: associa l'estructura de dades per guardar la freqüencia de cada lletra amb map
     */
    public void setCharacters (Map<Character, Double> map) {
        this.characters = map;
        setAbecedari();
    }

    /**
     * Pre: 
     * Post: associa l'estructura de dades per guardar la probabilitat que donada una lletra
     *       aparegui la següent amb matrix
     */
    public void setFrequencies (double[][] matrix) {
        this.frequencies = matrix;
    }

    /**
     * Pre: 
     * Post: associa l'estructura de dades per guarda l'abecedari de l'Alfabet
     */
    private void setAbecedari () {
        abecedari = new char[this.characters.size()];        // Crea un array de char que representa cada lletra de l'Alfabet
        int i = 0;
        for (Character c : characters.keySet()) {                   // Recorre l'estructura on es guarda l'abecedari
            abecedari[i] = c;
            i++;
        }
    }

    /**
     * Pre: 
     * Post: associa la mida de l'Alfabet amb size
     */
    public void setSize (int size) {
        this.size = size;
    }

    /**
     * Pre: 
     * Post: associa el nom que identifica l'Alfabet amb nom
     */
    public void setNom (String nom) {
        this.nom = nom;
    }

    /**
     * Pre: 
     * Post: es retorna la mida de l'Alfabet
     * 
     * @return la mida de l'Alfabet
     */
    public int getSize () {
        return size;
    }

    /**
     * Pre: 
     * Post: es retorna el nom que identifica l'Alfabet
     * 
     * @return el nom que identifica l'Alfabet
     */
    public String getNom () {
        return nom;
    }

    /**
     * Pre: 
     * Post: es retorna les lletres que pertanyen a l'alfabet
     * 
     * @return les lletres de l'alfabet
     */
    public char[] getAbecedari () {
        return this.abecedari;
    }

    /**
     * Pre: 
     * Post: es retorna la matriu amb les probabilitats que donada una lletra aparegui la següent amb matrix 
     * 
     * @return la matriu amb les probabilitats que donada una lletra aparegui la següent amb matrix
     */
    public double[][] getFrequencies () {
        return frequencies;
    }

    /**
     * Pre: 
     * Post: es retorna la freqüència de cada lletra de l'Alfabet
     * 
     * @return la freqüència de cada lletra de l'Alfabet
     */
    public Map<Character, Double> getCharacter () {
        return characters;
    }

    /**
     * Pre: 
     * Post: es transforma l'Alfabet en un String
     * 
     * @return l'Alfabet en format de String
     */
    @Override
    public String toString() {
        String result = "Nom: " + nom + "\n";                               // Guarda el nom de l'Alfabet a result
        result += "Mida: " + Integer.toString(size) + "\n";                 // Guardar la mida de l'Alfabet a result
        result += "Abecedari: " + abecedari.toString() + "\n";              // Guardar el path d'on s'ha tret l'Alfabet a result
        result += "Lletres: \n" + characters.toString();                    // Guardar el map amb les freqüències de cada lletra a result
        return result;
    }
}

package src.drivers;

import java.io.FileNotFoundException;
import java.util.Scanner;

import src.domain.classes.Alfabet;
import src.exceptions.EntradaLlegidaMalament;
import src.exceptions.FormatDadesNoValid;
import src.exceptions.TipusDadesNoValid;

public class DriverAlfabet {
    // Paths d'exemple per l'opció 1
    // ./subgrup-prop32.2/test/exemples_input_alfabet/Text1.txt     --> text
    // ./subgrup-prop32.2/test/exemples_input_alfabet/Words1.txt    --> llista-paraules

    public static void main (String[] args) throws FormatDadesNoValid, TipusDadesNoValid, FileNotFoundException, EntradaLlegidaMalament {
        Alfabet A = new Alfabet("Alfabet de prova");
        Scanner sc = new Scanner(System.in);
        int func = 0;
        Boolean primeraOpt = false;
        while (func != 6){
            System.out.println("Selecciona una de les funcionalitats:");
            System.out.println("\t1. ReadInput");
            System.out.println("\t2. GetAbecedari");
            System.out.println("\t3. GetFrequencies");
            System.out.println("\t4. GetCharacter");
            System.out.println("\t5. obteInfo (toString method)");
            System.out.println("\t6. Sortir");
            func = sc.nextInt();
            switch(func){
                case 1:
                    primeraOpt = true; 
                    System.out.println("Indica el format d'entrada:");
                    System.out.println("1 --> text");
                    System.out.println("2 --> llista-paraules");
                    int opc = sc.nextInt();
                    sc.nextLine();
                    if (opc == 1) {
                        System.out.println("Indica el path:");
                        String path = sc.nextLine();
                        A.readInput("text", path);
                        System.out.println("Input llegit");
                    }
                    else if (opc == 2) {
                        System.out.println("Indica el path:");
                        String path = sc.nextLine();
                        A.readInput("llista-paraules", path);
                        System.out.println("Input llegit");
                    }
                    else System.out.println("Opció incorrecte");
                    break;
                case 2: 
                    if (! primeraOpt) System.out.println("Primer has de fer un ReadInput");
                    else System.out.println(A.getAbecedari());
                    break;
                case 3: 
                    if (! primeraOpt) System.out.println("Primer has de fer un ReadInput");
                    else {
                        double[][] x = A.getFrequencies();
                        for (int i = 0; i < A.getSize(); i++) {
                            for (int j = 0; j < A.getSize(); j++) {
                                String s = String.format("%.6f", x[i][j]);
                                System.out.print(s + " ");
                            } 
                            System.out.print("\n");
                        }
                    }
                    break;
                case 4:
                    if (! primeraOpt) System.out.println("Primer has de fer un ReadInput"); 
                    else System.out.println(A.getCharacter().toString());
                    break;
                case 5: 
                    if (! primeraOpt) System.out.println("Primer has de fer un ReadInput");
                    else System.out.println(A.toString());
                    break;
                case 6:
                    System.out.println("Sortint del driver");
                    sc.close();
                    break;
                default:
                    System.out.println("Funcionalitat equivocada");
                    break;
            }
            System.out.println("\n");
        }
    }
}

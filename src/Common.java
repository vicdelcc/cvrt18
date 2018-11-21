import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Common {

    private static final String BEARBEITUNGSZEIT = "Bearbeitungszeit";
    private static final String SOLLENDTERMIN = "Soll-Endtermin";

    private static final char[] ALPHABET = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining()).toCharArray();


    public static HashMap<Character, HashMap<String, Integer>> getInputAuftraege() {

        HashMap<Character, HashMap<String, Integer>> daten = new HashMap<>();

        int anzahlAuftraege = 0;


        while (anzahlAuftraege < 2) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Wie viele Aufträge?");
            try {
                anzahlAuftraege = scanner.nextInt();
                if (anzahlAuftraege < 2) {
                    System.out.println("------  Bitte definieren Sie mindestens 2 Aufträge. ------ ");
                }
            } catch (InputMismatchException e) {
                System.out.println("------ Bitte geben Sie nur ganze Zahlen an! ------ ");
            }

        }


            for (int i = 1; i <= anzahlAuftraege; i++) {
                boolean weiter = true;
                while(weiter) {
                    try {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("\n#### AUFTRAG NR. " + i + " ####");
                        System.out.println("Bearbeitungszeit: ");
                        int bearbeitungszeit = scanner.nextInt();
                        weiter = false;
                        boolean weiter2 = true;
                        while(weiter2) {
                            try {
                                Scanner scanner2 = new Scanner(System.in);
                                System.out.println("Soll-Endtermin: : ");
                                int sollEndetermin = scanner2.nextInt();

                                HashMap<String, Integer> zeiten = new HashMap<>();
                                zeiten.put(BEARBEITUNGSZEIT, bearbeitungszeit);
                                zeiten.put(SOLLENDTERMIN, sollEndetermin);

                                daten.put(ALPHABET[i - 1], zeiten);
                                weiter2 = false;
                            } catch(InputMismatchException e) {
                                System.out.println("------ Bitte geben Sie nur ganze Zahlen an! ------ ");
                            }
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("------ Bitte geben Sie nur ganze Zahlen an! ------ ");
                    }
                }

            }

        return daten;
    }
}

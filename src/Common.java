import java.util.HashMap;
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

        Scanner scanner = new Scanner(System.in);

        while (anzahlAuftraege < 2) {

            System.out.println("Wie viele Aufträge?");
            anzahlAuftraege = scanner.nextInt();
            if (anzahlAuftraege < 2) {
                System.out.println("Bitte definieren Sie mindestens 2 Aufträge.\n");
            }
        }

        if (anzahlAuftraege < 2) {

        } else {

            for (int i = 1; i <= anzahlAuftraege; i++) {
                System.out.println("\n#### AUFTRAG NR. " + i + " ####");
                System.out.println("Bearbeitungszeit: ");
                int bearbeitungszeit = scanner.nextInt();
                System.out.println("Soll-Endtermin: : ");
                int sollEndetermin = scanner.nextInt();

                HashMap<String, Integer> zeiten = new HashMap<>();
                zeiten.put(BEARBEITUNGSZEIT, bearbeitungszeit);
                zeiten.put(SOLLENDTERMIN, sollEndetermin);

                daten.put(ALPHABET[i - 1], zeiten);
            }
        }

        return daten;
    }
}

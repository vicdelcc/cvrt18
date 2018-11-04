import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final String BEARBEITUNGSZEIT = "Bearbeitungszeit";
    private static final String SOLLENDTERMIN = "Soll-Endtermin";

    private static final char[] ALPHABET = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining()).toCharArray();

    private enum VerfahrenEnum {FIFO, LIFO}

    private static String verfahren;

    private static List<Character> reihenfolge = new ArrayList<>();

    private static HashMap<Character, HashMap<String, Integer>> daten;


    public static void main(String[] args) {

        System.out.println("########### Reihenfolgeproblem mit NEH-Heuristik ###########");

        // Aufträge aufnehmen
        daten = getInputAuftraege();

        // Sortieren nach Priorität
        sortiereNachPrioritaet();

        // Nehme die 2 ersten Aufträge je nach Vefahren
        permutiereErsteZweiAuftrage();




    }

    public static void permutiereErsteZweiAuftrage() {
        HashMap<String, Integer> auftrag1 = null;
        HashMap<String, Integer> auftrag2 = null;
        switch(verfahren) {
            case "FIFO":
                char auftragsname1fifo = ALPHABET[0];
                auftrag1 = daten.get(auftragsname1fifo);
                reihenfolge.add(auftragsname1fifo);

                char auftragsname2fifo = ALPHABET[1];
                auftrag2 = daten.get(auftragsname2fifo);
                reihenfolge.add(auftragsname2fifo);
                break;
            case "LIFO":
                char auftragsname1lifo = ALPHABET[daten.keySet().size()-1];
                auftrag1 = daten.get(auftragsname1lifo);
                reihenfolge.add(auftragsname1lifo);

                char auftragsname2lifo = ALPHABET[daten.keySet().size()-2];
                auftrag2 = daten.get(auftragsname2lifo);
                reihenfolge.add(auftragsname2lifo);
                break;
        }


        char[][] permutations = Permutator.getPermutations(reihenfolge);


        int verpaetungA = auftrag1.get(BEARBEITUNGSZEIT) - auftrag1.get(SOLLENDTERMIN) < 0 ? 0 : auftrag1.get(BEARBEITUNGSZEIT) - auftrag1.get(SOLLENDTERMIN);
        int verspaetungB = auftrag1.get(BEARBEITUNGSZEIT) + auftrag2.get(BEARBEITUNGSZEIT) - auftrag2.get(SOLLENDTERMIN) < 0 ? 0 : auftrag2.get(BEARBEITUNGSZEIT) + auftrag2.get(BEARBEITUNGSZEIT) - auftrag2.get(SOLLENDTERMIN);

        System.out.println("Verspätung Auftrag A: " + verpaetungA);
        System.out.println("Verspätung Auftrag B: " + verspaetungB);
    }



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
                System.out.println("#### AUFTRAG NR. " + i + " ####");
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

    public static void sortiereNachPrioritaet() {
        printSortiereFrage();

        Scanner scanner = new Scanner(System.in);
        int auswahl = 0;
        while (auswahl != 1 && auswahl != 2) {

            try {
                auswahl = Integer.parseInt(scanner.next());
                switch (auswahl) {
                    case 1:
                        verfahren = VerfahrenEnum.FIFO.name();
                        break;
                    case 2:
                        verfahren = VerfahrenEnum.LIFO.name();
                        break;
                    default:
                        System.out.println("Bitte wählen Sie eine von den vorgegebenen Optionen.\n");
                        printSortiereFrage();
                }
            } catch (NumberFormatException e) {
                System.out.println("Bitte wählen Sie eine von den vorgegebenen Optionen.\n");
                printSortiereFrage();
            }
        }
    }

    public static void printSortiereFrage() {
        System.out.println("Welche Vorsortierung wollen Sie machen?");
        System.out.println("1. First-In-First-Out (FIFO)");
        System.out.println("2. Last-In-First-Out (LIFO)");
    }
}

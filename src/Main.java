import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Map.Entry;

public class Main {

    private static final String BEARBEITUNGSZEIT = "Bearbeitungszeit";
    private static final String SOLLENDTERMIN = "Soll-Endtermin";

    private static final char[] ALPHABET = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining()).toCharArray();

    private static List<Character> reihenfolge = new ArrayList<>();

    private static HashMap<Character, HashMap<String, Integer>> daten;

    private static HashMap<String, Integer> finalPermutations = new HashMap<>();


    public static void main(String[] args) {

        System.out.println("########### Reihenfolgeproblem mit NEH-Heuristik ###########");

        // Aufträge aufnehmen
        daten = getInputAuftraege();

        // Sortieren nach Priorität
        sortiereNachPrioritaet();

        // Make permutations and calculate delays
        makePermutations();

        // Show 5 best permutations according to max delay
        showBestFive();

    }

    public static void showBestFive() {
        System.out.println("\n                #### BEST 5 PERMUTATIONS ####");
        System.out.println(" ------------------------------------------------------------");
        Set<Entry<String, Integer>> set = finalPermutations.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
                set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (int i = list.size() - 1; i >= list.size() - 6; i--) {
            Entry<String, Integer> entry = list.get(i);


            System.out.println("| " + entry.getKey() + " - Gesamtverspätung: " + entry.getValue() + " |");


        }
        System.out.println(" ------------------------------------------------------------");

    }

    public static void makePermutations() {
        HashMap<Integer, List<Character>> permutations = Permutator.getPermutations(reihenfolge);

        for (int i = 1; i <= permutations.keySet().size(); i++) {
            List<Character> listeAuftraege = permutations.get(i);
            String titelPermutation = "";
            switch (String.valueOf(i).length()) {
                case 1:
                    titelPermutation = "  " + i + ". Permutation: ";
                    break;
                case 2:
                    titelPermutation = " " + i + ". Permutation: ";
                    break;
                case 3:
                    titelPermutation = i + ". Permutation: ";
                    break;
            }
            for (Character auftragChar : listeAuftraege) {
                if (listeAuftraege.get(listeAuftraege.size() - 1) == auftragChar) {
                    titelPermutation += auftragChar;
                } else {
                    titelPermutation += auftragChar + " - ";
                }
            }
            System.out.println(titelPermutation);

            int gesamtVerspaetung = 0;
            for (Character auftragChar : listeAuftraege) {
                HashMap<String, Integer> auftrag = daten.get(auftragChar);
                int verspaetungDavor = getVerspaetungAuftraegeDavor(listeAuftraege, auftragChar);
                int unterschied = auftrag.get(BEARBEITUNGSZEIT) - auftrag.get(SOLLENDTERMIN) + verspaetungDavor;
                int verspaetung = unterschied < 0 ? 0 : unterschied;
                System.out.println("Verspätung Auftrag " + auftragChar + ": " + verspaetung);
                gesamtVerspaetung += verspaetung;
            }

            finalPermutations.put(titelPermutation, gesamtVerspaetung);
        }
    }

    public static int getVerspaetungAuftraegeDavor(List<Character> reihePermutation, char auftrag) {
        int verspaetungAuftraegeDavor = 0;
        if (reihePermutation.indexOf(auftrag) != 0) {
            int indexAuftragErsterDavor = reihePermutation.indexOf(auftrag) - 1;
            for (int i = indexAuftragErsterDavor; i >= 0; i--) {
                verspaetungAuftraegeDavor += daten.get(reihePermutation.get(i)).get(BEARBEITUNGSZEIT);
            }
        }
        return verspaetungAuftraegeDavor;
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
                        for (int i = 0; i < daten.size(); i++) {
                            reihenfolge.add(ALPHABET[i]);
                        }
                        break;
                    case 2:
                        for (int i = daten.keySet().size() - 1; i >= 0; i--) {
                            reihenfolge.add(ALPHABET[i]);
                        }
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
}

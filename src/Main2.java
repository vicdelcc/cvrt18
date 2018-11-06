import com.google.common.base.Joiner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Main2 {

    private static final String BEARBEITUNGSZEIT = "Bearbeitungszeit";
    private static final String SOLLENDTERMIN = "Soll-Endtermin";

    private static final char[] ALPHABET = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining()).toCharArray();

    private static List<Character> listeAuftraege = new ArrayList<>();

    private static List<Character> reihenfolge = new ArrayList<>();

    private static HashMap<Character, HashMap<String, Integer>> daten = new HashMap<>();

    private static HashMap<String, Integer> finalPermutations = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("########### Reihenfolgeproblem mit NEH-Heuristik ###########");

        // Aufträge aufnehmen
        daten = getInputAuftraege();

        // Sortieren nach Priorität
        sortiereNachPrioritaet();

        // Find permutations
        findBestPermutation();

    }

    public static void showErgebnis() {
        System.out.println("\n#### Best permutation: " + Joiner.on(" - ").join(reihenfolge) + " ####");
    }


    public static void findBestPermutation() {
        for (int i = 0; i < listeAuftraege.size(); i++) {
            HashMap<Integer, List<Character>> permutations = new HashMap<>();
            if (reihenfolge.size() == 0) {
                permutations = addPermutations(permutations, 0);
                // 2nd loop should be jumped
                i++;
            } else {
                permutations = addPermutations(permutations, i);
            }
            calculateBestPermutation(permutations);
            showErgebnis();
        }
    }

    public static HashMap<Integer, List<Character>> addPermutations(HashMap<Integer, List<Character>> permutations, int index) {
        permutations.clear();
        if (index == 0) {
            List<Character> combination1 = new ArrayList<>();
            combination1.add(listeAuftraege.get(0));
            combination1.add(listeAuftraege.get(1));
            permutations.put(1, combination1);
            List<Character> combination2 = new ArrayList<>();
            combination2.add(listeAuftraege.get(1));
            combination2.add(listeAuftraege.get(0));
            permutations.put(2, combination2);
        } else {

            List<Character> letters = new ArrayList<>();
            for (Character character : reihenfolge) {
                letters.add(character);
            }
            letters.add(listeAuftraege.get(index));

            HashMap<Integer, List<Character>> combis = Permutator.getPermutations(letters);
            for(Map.Entry<Integer, List<Character>> entry : combis.entrySet()) {
                if(Joiner.on("").join(entry.getValue()).contains(Joiner.on("").join(reihenfolge))) {
                        permutations.put(entry.getKey(), entry.getValue());
                }
            }

//            List<Character> possibilities = new ArrayList<>();
//
//            // Erst mal addieren, die die schon fest stehen
//            for (Character character : reihenfolge) {
//                possibilities.add(character);
//            }
//            // neue dazu addieren
//            possibilities.add(listeAuftraege.get(index));
//
//            permutations.put(1, possibilities);
//
//
//            // So many loops als die index-nummer (für 3 buchstaben ist der index von 3. Buchstabe 2)
//            // Es gibt eigentlich so viele Loops wie anzahl an Buchstaben aber minus 1, da wir oben schon
//            // die neue buchstabe am Ende hinzugefügt haben
//            for (int i = 0; i < index; i++) {
//                // Make a clone of possibilities to avoid rewriting
//                List<Character> possibilitiesNew = possibilities.stream().map(Character::new).collect(toList());
//                List<Character> temp = possibilities.stream().map(Character::new).collect(toList());
//                possibilitiesNew.set(i, listeAuftraege.get(index));
//
//                if (i + 1 < possibilities.size()) {
//                    possibilitiesNew.set(i + 1, temp.get(i));
//                    if (i + 2 < possibilities.size()) {
//                        possibilitiesNew.set(i + 2, temp.get(i + 1));
//                    } else {
//                        possibilitiesNew.set(0, temp.get(i + 1));
//                    }
//                } else {
//                    possibilitiesNew.set(0, temp.get(i));
//                }
//                permutations.put(i + 2, possibilitiesNew);
//            }
        }
        return permutations;
    }

    public static void calculateBestPermutation(HashMap<Integer, List<Character>> permutations) {

        HashMap<Integer, Integer> verspaetungen = new HashMap<>();

        for (int i = 1; i <= permutations.keySet().size(); i++) {
            List<Character> listeAuftraege = permutations.get(i);
            String titelPermutation = "\n" + i + ". Permutation: ";
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
                int verspaetungDavor = getVerspaetungAuftraegeDavor(permutations.get(i), auftragChar);
                int unterschied = auftrag.get(BEARBEITUNGSZEIT) - auftrag.get(SOLLENDTERMIN) + verspaetungDavor;
                int verspaetung = unterschied < 0 ? 0 : unterschied;
                System.out.println("Verspätung Auftrag " + auftragChar + ": " + verspaetung);
                gesamtVerspaetung += verspaetung;
            }

            verspaetungen.put(i, gesamtVerspaetung);
        }

        Map.Entry<Integer, Integer> min = null;
        for (Map.Entry<Integer, Integer> entry : verspaetungen.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        reihenfolge.clear();
        for (Character character : permutations.get(min.getKey())) {
            reihenfolge.add(character);
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
                            listeAuftraege.add(ALPHABET[i]);
                        }
                        break;
                    case 2:
                        for (int i = daten.keySet().size() - 1; i >= 0; i--) {
                            listeAuftraege.add(ALPHABET[i]);
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

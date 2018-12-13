import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


public class ReihenfolgeproblemSolverOptimiert {

    private static final String BEARBEITUNGSZEIT = "Bearbeitungszeit";
    private static final String SOLLENDTERMIN = "Soll-Endtermin";

    private static char[] ALPHABET = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining()).toCharArray();

    private static List<Character> listeAuftraege = new ArrayList<>();

    private static List<Character> reihenfolge = new ArrayList<>();

    private static HashMap<Character, HashMap<String, Integer>> daten = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("########### Reihenfolgeproblem mit NEH-Heuristik ###########");

        // Aufträge aufnehmen
        daten = Common.getInputAuftraege();

        // ALPHABET erweitern wenn nötig
        ALPHABET = Common.erweitereCharArray(daten.size());

        // Sortieren nach Priorität
        sortiereNachPrioritaet();

        Stopwatch sw = new Stopwatch();
        sw.start();
        // Find permutations
        findBestPermutation();
        System.out.println("---- Gesamtlaufzeit: " + sw.elapsedTime(TimeUnit.MILLISECONDS) + " ms ----");

        // Ask if same but with the other Verfahren
        while (Common.stelleJaNeinFrage("\nWollen Sie das selbe Reihenfolgeproblem mit einer anderen Vorsortierung lösen (j/n)")) {
            listeAuftraege.clear();
            reihenfolge.clear();
            sortiereNachPrioritaet();
            Stopwatch sw2 = new Stopwatch();
            sw2.start();
            findBestPermutation();
            System.out.println("---- Gesamtlaufzeit: " + sw2.elapsedTime(TimeUnit.MILLISECONDS) + " ms ----");
        }
    }

    public static void showErgebnis() {
        System.out.println("\n#### BESTE PERMUTATION: " + Joiner.on(" - ").join(reihenfolge) + " ####");
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

            Character newChar = listeAuftraege.get(index);
            List<Character> letters = new ArrayList<>();
            for (Character character : reihenfolge) {
                letters.add(character);
            }
            letters.add(newChar);

            // 1. Combination    B A C  (make clone)
            List<Character> combination1 = letters.stream().map(Character::new).collect(toList());
            permutations.put(1, combination1);

            // 2 und 3. Combination   C B A  udd B C A
            for (int i = 0; i < letters.size() - 1; i++) {
                List<Character> combination = new ArrayList<>();
                // 2. Combination C B A
                if (i == 0) {
                    combination.add(i, newChar);
                    for (int j = 1; j < letters.size(); j++) {
                        combination.add(j, letters.get(j - 1));
                    }
                    // 3. Combination B C A
                } else {
                    combination = permutations.get(i + 1).stream().map(Character::new).collect(toList());
                    int indexOldNewLetter = combination.indexOf(newChar);
                    Character letterToSwap = combination.get(i);
                    combination.set(i, newChar);
                    combination.set(indexOldNewLetter, letterToSwap);
                }

                permutations.put(i + 2, combination);
            }

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
            System.out.println("### GESAMTVERSPÄTUNG: " + gesamtVerspaetung + " ###");
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


        int auswahl = 0;
        while (auswahl != 1 && auswahl != 2 && auswahl != 3) {
            Scanner scanner = new Scanner(System.in);
            try {
                auswahl = scanner.nextInt();
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
                    case 3:
                        priosNachSollEndeTerminBestimmen();
                        break;
                    default:
                        System.out.println(" ----- Bitte wählen Sie eine von den vorgegebenen Optionen! -----");
                        printSortiereFrage();
                }
            } catch (InputMismatchException e) {
                System.out.println(" ----- Bitte wählen Sie eine von den vorgegebenen Optionen! -----");
                printSortiereFrage();
            }
        }
    }

    public static void printSortiereFrage() {
        System.out.println("\nWelche Vorsortierung wollen Sie machen?");
        System.out.println("1. First-In-First-Out (FIFO)");
        System.out.println("2. Last-In-First-Out (LIFO)");
        System.out.println("3. Prioritäten nach Soll-Endetermin");
    }

    public static void priosNachSollEndeTerminBestimmen() {

        Object[] o = daten.entrySet().toArray();
        Arrays.sort(o, new Comparator() {
            public int compare(Object o1, Object o2) {
                return((Map.Entry<Character, HashMap<String, Integer>>)o1).getValue().get(SOLLENDTERMIN)
                        .compareTo(((Map.Entry<Character, HashMap<String, Integer>>)o2).getValue().get(SOLLENDTERMIN));
            }
        });

        listeAuftraege.clear();
        for (Object e : o) {
            listeAuftraege.add(((Map.Entry<Character, HashMap<String, Integer>>) e).getKey());
        }
        System.out.println("Blu");
    }

}

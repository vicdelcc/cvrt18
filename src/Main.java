import java.util.*;

public class Main {

    private static final String BEARBEITUNGSZEIT = "Bearbeitungszeit";
    private static final String SOLLENDTERMIN = "Soll-Endtermin";

    public static void main(String[] args) {

        System.out.println("########### Reihenfolgeproblem mit NEH-Heuristik ###########");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Wie viele Aufträge?");
        int anzahlAuftraege = scanner.nextInt();

        List<Character> auftraegeNamen = new ArrayList<>();
        for (int i = 65; i < 65 + anzahlAuftraege; i++) {
            auftraegeNamen.add((char) i);
        }

        HashMap<Character, HashMap<String, Integer>> daten = new HashMap<>();

        for (int i = 1; i <= anzahlAuftraege; i++) {
            System.out.println("Auftrag Nr. " + i + ":");
            System.out.println("Bearbeitungszeit: ");
            int bearbeitungszeit = scanner.nextInt();
            System.out.println("Soll-Endtermin: : ");
            int sollEndetermin = scanner.nextInt();

            HashMap<String, Integer> zeiten = new HashMap<>();
            zeiten.put(BEARBEITUNGSZEIT, bearbeitungszeit);
            zeiten.put(SOLLENDTERMIN, sollEndetermin);

            daten.put(auftraegeNamen.get(i - 1), zeiten);
        }


        // Beispiel Permutation 2 erste Elemente

        for(int i = 0; i< getFakultät(2); i++) {

        }
        HashMap<String, Integer> auftragA = daten.get('A');
        HashMap<String, Integer> auftragB = daten.get('B');


        int verpaetungA = auftragA.get(BEARBEITUNGSZEIT) - auftragA.get(SOLLENDTERMIN) < 0 ? 0 : auftragA.get(BEARBEITUNGSZEIT) - auftragA.get(SOLLENDTERMIN);
        int verspaetungB = auftragA.get(BEARBEITUNGSZEIT) + auftragB.get(BEARBEITUNGSZEIT) - auftragB.get(SOLLENDTERMIN) < 0 ? 0 : auftragA.get(BEARBEITUNGSZEIT) + auftragB.get(BEARBEITUNGSZEIT) - auftragB.get(SOLLENDTERMIN);

        System.out.println("Verspätung Auftrag A: " + verpaetungA);
        System.out.println("Verspätung Auftrag B: " + verspaetungB);


    }

    public static int getFakultät(int zahl) {
        int fakultaet = 1;
        for (int i=1; i<=zahl; i++) {
            fakultaet = fakultaet * i;
        }
        return fakultaet;
    }
}

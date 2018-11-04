
import java.util.*;

public class Permutator {

    public static char[][] getPermutations(List<Character> liste) {

        String listeString = "";
        for(Character character : liste) {
            listeString+=character;
        }
        List<String> permutationsListe = permutations(listeString);

        char[][] permutations = new char[getFakultaet(liste.size())][liste.size()];

        for(int i = 0; i<getFakultaet(liste.size()); i++) {
            for(int j = 0; j<liste.size(); j++) {
                permutations[i][j] = permutationsListe.get(i).toCharArray()[j];
            }
        }
        return permutations;
    }

    // Iterative function to generate all permutations of a String in Java
    // using Collections
    public static List<String> permutations(String s)
    {
        // create an empty ArrayList to store (partial) permutations
        List<String> partial = new ArrayList<>();

        // initialize the list with the first character of the string
        partial.add(String.valueOf(s.charAt(0)));

        // do for every character of the specified string
        for (int i = 1; i < s.length(); i++)
        {
            // consider previously constructed partial permutation one by one

            // (iterate backwards to avoid ConcurrentModificationException)
            for (int j = partial.size() - 1; j >= 0 ; j--)
            {
                // remove current partial permutation from the ArrayList
                String str = partial.remove(j);

                // Insert next character of the specified string in all
                // possible positions of current partial permutation. Then
                // insert each of these newly constructed string in the list

                for (int k = 0; k <= str.length(); k++)
                {
                    // Advice: use StringBuilder for concatenation
                    partial.add(str.substring(0, k) + s.charAt(i) +
                            str.substring(k));
                }
            }
        }

        System.out.println(partial);

        return partial;
    }


    public static int getFakultaet(int zahl) {
        int fakultaet = 1;
        for (int i = 1; i <= zahl; i++) {
            fakultaet = fakultaet * i;
        }
        return fakultaet;
    }
}

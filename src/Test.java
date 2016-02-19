import me.semakon.Utils;

/**
 * Author:  Martijn
 * Date:    19-2-2016
 */
public class Test {

    public static void main(String[] args) {
        String[] arguments = {"edit", "title", "\"Intense", "Tester", "Man\"", "description", "\"This", "guy", "tests", "intensely.\""};
        for (String s : arguments) {
            System.out.print(s + " ");
        }
        System.out.println("\n");
        String[] result = Utils.inQuotes(arguments);
        for (String s : result) {
            System.out.println(s);
        }
    }

}

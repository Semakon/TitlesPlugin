import me.semakon.Utils;

import java.util.Arrays;
import java.util.List;

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
        int i = 1;
        int size = ((i / 9) + 1) * 9;
        System.out.println("((" + i + " / 9) + 1) * 9 = " + size);

        List<String> list = Arrays.asList("een", "twee", "drie");
        list.set(2, "interrupt");
        System.out.println(list);
    }

}

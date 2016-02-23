import me.semakon.Utils;

/**
 * Author:  Martijn
 * Date:    19-2-2016
 */
public class Test {

    public static void main(String[] args) {
        Utils.initColors();
        String test = Utils.setColors("TitlesPlugin");
        String res = Utils.setColors("&0Fucking &1Paladin &oBeer &r");
        System.out.println(res);
        System.out.println(test);
        System.out.println(Utils.strip(res));
        System.out.println(Utils.strip(test));
    }

}

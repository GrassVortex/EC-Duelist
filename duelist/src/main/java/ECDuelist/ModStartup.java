package ECDuelist;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;


@SpireInitializer
public class ModStartup {

    public ModStartup() {

        System.out.println("Hello StS World!");
    }

    public static void initialize() {
        ModStartup start = new ModStartup();

    }
}

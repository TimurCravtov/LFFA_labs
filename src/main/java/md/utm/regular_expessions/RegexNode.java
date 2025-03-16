package md.utm.regular_expessions;

import md.utm.utils.ColorManager;

import java.util.Random;

public abstract class RegexNode {
    protected final Random rand = new Random();
    public abstract String generate(boolean reasoning, int level);


    public String className() {
        return getClass().getSimpleName();
    }
    public String getColouredClassName() {
        return ColorManager.colorize(className(), ColorManager.CYAN);
    }
    public String getColouredClassNameFinal() {
        return ColorManager.colorize(className(), ColorManager.PURPLE);
    }
}


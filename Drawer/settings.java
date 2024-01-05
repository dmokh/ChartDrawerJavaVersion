package Drawer;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class settings {
    static public Map<String, Integer> op = new HashMap<>();
    static public List<String> const_op = Arrays.asList("P", "E");
    static public List<String> unary_op = Arrays.asList("u-", "sin", "cos", "tan", "ctg", "log2", "log10", "ln", "abs", "sqrt", "sign", "acos", "asin", "atan", "sec", "csc");
    static public List<String> binary_op = Arrays.asList("+", "-", "*", "/", "^", "log");
    static public List<Color> colors = Arrays.asList(new Color(200, 100, 0), new Color(100, 200, 0), new Color(0, 200, 100), new Color(0, 100, 200));
    public static void init() {
        {
            op.put("+", 1);
            op.put("-", 1);
            op.put("*", 2);
            op.put("/", 2);
            op.put("u-", 2);
            op.put("^", 3);
            op.put("cos", 4);
            op.put("sin", 4);
            op.put("tan", 4);
            op.put("ctg", 4);
            op.put("log2", 4);
            op.put("log10", 4);
            op.put("log", 4);
            op.put("ln", 4);
            op.put("abs", 4);
            op.put("sqrt", 4);
            op.put("sign", 4);
            op.put("acos", 4);
            op.put("asin", 4);
            op.put("atan", 4);
            op.put("sec", 4);
            op.put("csc", 4);
            op.put("P", 4);
            op.put("E", 4);
            op.put("(", 5);
            op.put(")", 5);
            op.put("|", 5);
        }
    }
}

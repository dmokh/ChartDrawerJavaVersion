package Drawer;
import java.util.Objects;
import java.util.Stack;

public class StringFunc {
    public String s;
    public static int except;
    StringFunc(String F_str) {
        F_str = F_str.replaceAll(" ", "");
        this.s = F_str;
        settings.init();
    }

    public static void Exception(int type) {
        except = type;
    }

    public static void doOperation(double x1, double x2, String operation, Stack<Double> stack) {
        switch (operation) {
            case "+":
                stack.push(x2 + x1);
                break;
            case "-":
                stack.push(x2 - x1);
                break;
            case "*":
                stack.push(x2 * x1);
                break;
            case "/":
                if (x1 == 0) {Exception(1); stack.push(0.d);}
                else stack.push(x2 / x1);
                break;
            case "^":
                stack.push(Math.pow(x2, x1));
                break;
            case "u-":
                stack.push(-x1);
                break;
            case "sin":
                stack.push(Math.sin(x1));
                break;
            case "cos":
                stack.push(Math.cos(x1));
                break;
            case "tan":
                stack.push(Math.tan(x1));
                break;
            case "ctg":
                stack.push((1.d / Math.tan(x1)));
                break;
            case "log2":
                if (x1 <= 0) {Exception(2); stack.push(0.d);}
                else stack.push(Math.log(x1) / Math.log(2));
                break;
            case "log10":
                if (x1 <= 0) {Exception(2); stack.push(0.d);}
                else stack.push(Math.log10(x1));
                break;
            case "log":
                if (x1 <= 0 || x2 <= 0 || x2 == 1) {Exception(2); stack.push(0.d);}
                else stack.push(Math.log(x1) / Math.log(x2));
                break;
            case "ln":
                if (x1 <= 0) {Exception(2); stack.push(0.d);}
                else stack.push(Math.log(x1));
                break;
            case "abs":
                stack.push(Math.abs(x1));
                break;
            case "sqrt":
                if (x1 < 0) {Exception(3); stack.push(0.d);}
                else stack.push(Math.sqrt(x1));
                break;
            case "sign":
                stack.push(Math.signum(x1));
                break;
            case "asin":
                if (x1 < -1 || x1 > 1) {Exception(4); stack.push(0.d);}
                else stack.push(Math.asin(x1));
                break;
            case "acos":
                if (x1 < -1 || x1 > 1) {Exception(4); stack.push(0.d);}
                else stack.push(Math.acos(x1));
                break;
            case "atan":
                stack.push(Math.atan(x1));
                break;
            case "sec":
                stack.push(1.d / Math.cos(x1));
                break;
            case "csc":
                stack.push(1.d / Math.sin(x1));
                break;
            case "P":
                stack.push(Math.PI);
                break;
            case "E":
                stack.push(Math.E);
                break;
        }
    }

    public func GetFunc() {
        return new func() {
            public double f(double x) {
                StringFunc.except = 0;
                String s = StringFunc.this.s;
                int i = 0;
                while (i < s.length()) {
                    if (s.charAt(i) == 'x') {
                        s = s.substring(0, i) + "(" + String.format("%s", x) + ")" + s.substring(i+1);
                    }
                    i++;
                }
                Stack<Double> stack1 = new Stack<>();
                Stack<String> stack2 = new Stack<>();
                String now = "";
                int mod_c = 0;
                for (i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (settings.op.containsKey(now)) {
                        if (!(now.equals("log") && isInteger(String.format("%c", c)))) {
                            stack2.push(now);
                            now = "";
                        } else if (c == '|') {
                            mod_c++;
                        }
                    }
                    if (!settings.op.containsKey(String.format("%c", c)) || (i > 0 && c != '-' && s.charAt(i-1) == 'e')) {
                        now += c;
                    } else {
                        if (isInteger(now) || (now.length() > 1 && isInteger(now.substring(1)))) {
                            stack1.push((double)Integer.parseInt(now));
                        } else if (isDouble(now) || (now.length() > 1 && isDouble(now.substring(1)))) {
                            stack1.push(Double.parseDouble(now));
                        } else if (!now.isEmpty() && now.charAt(now.length()-1) == ';') {
                            if (isDouble(now.substring(0, now.length()-1))) {
                                stack1.push(Double.parseDouble(now.substring(0, now.length() - 1)));
                            } else if (!now.substring(0, now.length() - 1).isEmpty()) {
                                stack2.push(now.substring(0, now.length()-1));
                            }
                            now = "" + c;
                            continue;
                        }
                        now = "";
                        while (!stack2.isEmpty() && settings.op.get(String.format("%c", c)) <= settings.op.get(stack2.peek()) && settings.op.get(stack2.lastElement()) < settings.op.get("(")) {
                            if (settings.const_op.contains(stack2.peek())) {
                                doOperation(0, 0, stack2.pop(), stack1);
                            }
                            else if (settings.unary_op.contains(stack2.peek())) {
                                doOperation(stack1.pop(), 0, stack2.pop(), stack1);
                            } else if (stack1.size() > 1) {
                                doOperation(stack1.pop(), stack1.pop(), stack2.pop(), stack1);
                            } else {
                                break;
                            }
                        }
                        if (c == ')') {
                            while (!Objects.equals(stack2.peek(), "(")) {
                                if (settings.const_op.contains(stack2.peek())) {
                                    doOperation(0, 0, stack2.pop(), stack1);
                                }
                                else if (settings.unary_op.contains(stack2.peek())) {
                                    doOperation(stack1.pop(), 0, stack2.pop(), stack1);
                                } else if (stack1.size() > 1) {
                                    doOperation(stack1.pop(), stack1.pop(), stack2.pop(), stack1);
                                } else {
                                    break;
                                }
                            }
                            stack2.pop();
                        } else if (c == '|') {
                            mod_c++;
                            if (i > 0 && (!settings.binary_op.contains(String.format("%c", s.charAt(i - 1))) && s.charAt(i-1) != ')' && s.charAt(i-1) != '|')) {
                                while (!Objects.equals(stack2.peek(), "|")) {
                                    if (settings.const_op.contains(stack2.peek())) {
                                        doOperation(0, 0, stack2.pop(), stack1);
                                    }
                                    else if (settings.unary_op.contains(stack2.peek())) {
                                        doOperation(stack1.pop(), 0, stack2.pop(), stack1);
                                    } else if (stack1.size() > 1) {
                                        doOperation(stack1.pop(), stack1.pop(), stack2.pop(), stack1);
                                    } else {
                                        break;
                                    }
                                }
                                stack2.pop();
                                stack1.push(Math.abs(stack1.pop()));
                            } else {
                                stack2.push(String.format("%c", c));
                            }
                        } else {
                            if (c == '-' && (i == 0 || (settings.op.containsKey(String.format("%c", s.charAt(i-1))) && s.charAt(i-1) != ')' && (s.charAt(i-1) != '|' || mod_c % 2 == 1)))) {
                                stack2.push("u-");
                            } else {
                                stack2.push(String.format("%c", c));
                            }
                        }
                    }
                }
                if (isInteger(now) || (now.length() > 1 && isInteger(now.substring(1)))) {
                    stack1.push((double)Integer.parseInt(now));
                } else if (isDouble(now) || (now.length() > 1 && isDouble(now.substring(1)))) {
                    stack1.push(Double.parseDouble(now));
                }
                if (settings.op.containsKey(now)) stack2.push(now);
                while (!stack2.isEmpty() && !stack1.isEmpty()) {
                    if (settings.const_op.contains(stack2.peek())) {
                        doOperation(0, 0, stack2.pop(), stack1);
                    }
                    else if (settings.unary_op.contains(stack2.peek())) {
                        doOperation(stack1.pop(), 0, stack2.pop(), stack1);
                    } else if (stack1.size() > 1) {
                        doOperation(stack1.pop(), stack1.pop(), stack2.pop(), stack1);
                    } else {
                        break;
                    }
                }
                while (!stack2.isEmpty()) {
                    doOperation(0, 0, stack2.pop(), stack1);
                }
                return stack1.peek();
            }
        };
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void main(String[] args) {
        StringFunc s = new StringFunc("sin(x)");
        func f = s.GetFunc();
        System.out.println(f.f(0));
    }
}

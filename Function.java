package Drawer;


public class Function {
    func function;
    func neg_f;
    func isDefinitionf;
    Boolean isLine;
    Linear arg_line;
    Quadratic arg_quad;
    int width;
    Function(func f, func neg_f, func isDefinition, Quadratic arg_quad, int half_width) {
        this.function = f;
        this.neg_f = neg_f;
        this.isLine = false;
        this.arg_quad = arg_quad;
        this.isDefinitionf = isDefinition;
        this.width = half_width;
    }
    Function(func f, func neg_f, func isDefinition, Linear arg_line, int half_width) {
        this.function = f;
        this.neg_f = neg_f;
        this.isLine = true;
        this.arg_line = arg_line;
        this.isDefinitionf = isDefinition;
        this.width = half_width;
    }

    Function(func f, func isDefinition, int half_width) {
        this.function = f;
        this.neg_f = new func() {
            public double f(double x) {return 0;}
        };
        this.isLine = true;
        this.arg_line = new Linear(1.d, 0.d);
        this.isDefinitionf = isDefinition;
        this.width = half_width;
    }

    public double get(double x) {
        if (this.isLine) return this.function.f(arg_line.y(x));
        else return this.function.f(arg_quad.y(x));
    }

    public double get_neg(double x) {
        if (this.isLine) return this.neg_f.f(arg_line.y(x));
        else return this.neg_f.f(arg_quad.y(x));
    }

    public boolean isDefinition(double x) {
        return this.isDefinitionf.bF(x);
    }

}

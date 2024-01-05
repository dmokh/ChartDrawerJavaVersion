package Drawer;

public class Quadratic {
    public double a = 1;
    public double b = 0;
    public double c = 0;
    Quadratic(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double D(double y) {
        return this.b*this.b - 4 * this.a * (this.c - y);
    }

    public double[] x(double y) {
        double D = this.D(y);
        if (D > 0)
            return new double[]{(-this.b + Math.sqrt(D)) / (2 * this.a), (-this.b - Math.sqrt(D)) / (2 * this.a)};
        else if (D == 0) return new double[]{-this.b / (2 * this.a)};
        else return new double[]{};
    }

    public double y(double x) {
        return this.a * x * x + this.b * x - this.c;
    }

}

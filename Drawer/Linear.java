package Drawer;

public class Linear {
    public double k = 1;
    public double b;
    Linear(double k, double b) {
        this.k = k;
        this.b = b;
    }


    public double y(double x) {
        return this.k * x + this.b;
    }

    public double x(double y) {
        return (y - b) / k;
    }

}

public class PID {
    private double P;
    private double I;
    private double D;
    private Boolean firstRun;

    public PID(double p, double i, double d) {
        P = p;
        I = i;
        D = d;
    }

    public double getOutput(double actual, double setpoint) {

        double error = setpoint - actual;

        double Poutput = P * error;
        double output = Poutput;

        return output;
    }

    public void setP(double p) {
        P = p;
    }

    public void setI(double i) {
        I = i;
    }

    public void setD(double d) {
        D = d;
    }
}

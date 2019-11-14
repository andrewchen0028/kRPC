import krpc.client.RPCException;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static krpc.client.services.SpaceCenter.*;

public class Operations {

    public static void countdown(int t) throws InterruptedException {
        while (t > 0) {
            System.out.println(t);
            TimeUnit.SECONDS.sleep(1);
            t--;
        }
    }

    public static void countdown(int t, int ignition) throws InterruptedException {
        while (t > 0) {
            if (t == ignition) {
                System.out.println(t + " Ignition Sequence Start");
            } else {
                System.out.println(t);
            }
            TimeUnit.SECONDS.sleep(1);
            t--;
        }
    }

    public static void countdown(int t, int ignition, Vessel vessel)
            throws InterruptedException, RPCException {
        while (t > 0) {
            if (t != ignition) {
                System.out.println(t);
            } else {
                stage(vessel);
                System.out.println(t + " Ignition Sequence Start");
            }
            TimeUnit.SECONDS.sleep(1);
            t--;
        }
        stage(vessel);
        System.out.println("Liftoff!");
    }

    public static void stage(Vessel vessel) throws RPCException {
        vessel.getControl().activateNextStage();
    }

    public static void launch(Vessel vessel)
            throws RPCException, InterruptedException {
        vessel.getControl().setThrottle(1);
        countdown(10, 6, vessel);
    }

    public static void holdAltitude(Vessel vessel, double target) throws RPCException, InterruptedException {
        PID altitudePID = new PID(1, 1, 1);
        double planetRadius = vessel.getOrbit().getBody().getEquatorialRadius();
        while (true) {
            double output = altitudePID.getOutput(vessel.getOrbit().getRadius() - planetRadius, target);
            vessel.getControl().setPitch((float)output);
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    public static void circularize(Vessel vessel, double targetRadius) throws RPCException {

        // circularize at current inclination and given targetRadius
        double kp = 0;
        double ki = 0;
        double kd = 0;

        LinkedList<Double> history = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            history.add(0.0);
        } // fill history with zeros

        CelestialBody planet = vessel.getOrbit().getBody();
        double g = 6.67408e-11f;
        double inclination = vessel.getOrbit().getInclination();
        double finalSpeed = Math.sqrt(g * planet.getMass() / targetRadius);

        vessel.getAutoPilot().setTargetHeading((float) inclination);

        while (vessel.getOrbit().getOrbitalSpeed() < finalSpeed) {
            // iterate pid

            double error = vessel.getOrbit().getRadius() - targetRadius;
            history.removeFirst();
            history.addLast(error);

            double P = kp * (vessel.getOrbit().getRadius() - targetRadius);
            double I = ki * (1);
            double D = kd * (1);
        }

    }
}

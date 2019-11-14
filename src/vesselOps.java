import krpc.client.RPCException;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static krpc.client.services.SpaceCenter.*;

public class vesselOps {

    public static void countdown(int t) throws InterruptedException {
        while (t > 0) {
            System.out.println(t);
            TimeUnit.SECONDS.sleep(1);
            t--;
        }
    }

    public static void stage(Vessel vessel) throws RPCException {
        vessel.getControl().activateNextStage();
    }

    public static void launch(Vessel vessel, float apoapsis) throws RPCException {
        stage(vessel); // ignition
    }

    public static void circularize(Vessel vessel, float targetRadius) throws RPCException {

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

            /*
            double P = kp * (vessel.getOrbit().getRadius() - targetRadius);
            double I = ki * ();
            double D = kd * ();

             */
        }
    }

}

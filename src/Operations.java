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

    public static double getLaunchAzimuth(Vessel vessel, double inclination) throws RPCException {
        double latitude = vessel.flight(vessel.getReferenceFrame()).getLatitude();
        return Math.asin(Math.cos(Math.toRadians(inclination)) / Math.cos(Math.toRadians(latitude)));
    }

    public static void launch(Vessel vessel, int countdownStart, int ignitionTime)
            throws RPCException, InterruptedException {
        vessel.getControl().setThrottle(1);
        countdown(countdownStart, ignitionTime, vessel);
    }

    public static void holdAltitude(Vessel vessel, double target) throws RPCException, InterruptedException {
    }

    public static void circularize(Vessel vessel, double targetRadius) throws RPCException {
    }

    public static ArrayList<String> getActivePropellants(Vessel vessel) throws RPCException {
        ArrayList<String> activePropellants = new ArrayList<>();
        for (Engine engine : vessel.getParts().getEngines()) {
            if (engine.getActive() && !activePropellants.contains(engine.getPropellantNames().toString())) {
                activePropellants.add(engine.getPropellantNames().toString());
            }
        }
        return activePropellants;
    }

    public static int countDecoupleStages(Vessel vessel) throws RPCException {
        int decoupleStages = 0;
        while(!vessel.getParts().inDecoupleStage(decoupleStages).isEmpty()) {
            decoupleStages++;
        }
        return decoupleStages;
    }

    public static int countStages(Vessel vessel) throws RPCException {
        int stages = 0;
        while(!vessel.getParts().inStage(stages).isEmpty()) {
            stages++;
        }
        return stages;
    }

    public static void stage(Vessel vessel, int stages) throws RPCException {
        vessel.getControl().activateNextStage();
        stages--;
    }
}

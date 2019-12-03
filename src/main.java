import javafx.scene.control.OverrunStyle;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.services.KRPC;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.*;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String args[]) throws IOException, RPCException, InterruptedException {

        // Connection block
//        Connection connection = Connection.newInstance();

        Connection connection = Connection.newInstance(
                "connection",
                "10.186.149.94",
                50000,
                50001);

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel = spaceCenter.getActiveVessel();

        float pitch = 90;
        float heading = 90;
        String fuel = "Kerosene";
        String oxidizer = "LqdOxygen";
        double[] constants = {95, 55, 0.085, -1.5}; // steering constants
        double time;

        vessel.getAutoPilot().engage();
        vessel.getControl().setThrottle(1);
        vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);
        vessel.getAutoPilot().setTargetRoll(0);

        Operations.stage(vessel); // ignition
        while (vessel.getThrust() < 0.95 * vessel.getMaxThrust()) {
            TimeUnit.MILLISECONDS.sleep(10);
        }

        Operations.stage(vessel);
        double UT0 = spaceCenter.getUT();

        System.out.println("Stages: " + Operations.countDecoupleStages(vessel));
        int stage = Operations.countDecoupleStages(vessel);

        while (vessel.flight(vessel.getReferenceFrame()).getSurfaceAltitude() < 100) {
            time = spaceCenter.getUT() - UT0;
            System.out.printf("MET: %6.2f   Pitch: %4.2f   Initial Ascent\n", time, pitch);
        }

        while (vessel.resourcesInDecoupleStage(stage - 1, false).amount(fuel) > 10000
                && vessel.resourcesInDecoupleStage(stage - 1, false).amount(oxidizer) > 17500) {

            pitch = (float) (constants[0] - constants[1] * Math.tanh(constants[2]
                        * (vessel.flight(vessel.getReferenceFrame()).getMeanAltitude() / 1000 - constants[3])));
            vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);

            time = spaceCenter.getUT() - UT0;
            System.out.printf("MET: %6.2f   Pitch: %4.2f   Gravity Turn\n", time, pitch);
        }

        // MECO, staging, MVac ignition, S2 targeting
        vessel.getControl().setThrottle(0);
        vessel.getAutoPilot().engage();
        Operations.stage(vessel, stage);
        vessel.getAutoPilot().targetPitchAndHeading(3, heading);
        vessel.getAutoPilot().setTargetRoll(0);
        vessel.getControl().setThrottle(1);
    }

}

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
        Connection connection = Connection.newInstance();

/*        Connection connection = Connection.newInstance(
                "connection",
                "10.192.24.90",
                50000,
                50001);*/

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel = spaceCenter.getActiveVessel();

        float pitch = 90;
        float heading = 90;
        double UT0 = spaceCenter.getUT();
        String fuel = "Kerosene";
        String oxidizer = "LqdOxygen";
        double[] constants = {110, 70, 0.125, -1.5}; // steering constants

        vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);
        vessel.getAutoPilot().engage();
        vessel.getControl().setThrottle(1);

        Operations.stage(vessel); // ignition
        while (vessel.getThrust() < 0.95 * vessel.getMaxThrust()) {
            TimeUnit.MILLISECONDS.sleep(10);
        }
        Operations.stage(vessel);

        System.out.println("Stages: " + Operations.countStages(vessel));
        System.out.println("Dstages: " + Operations.countDecoupleStages(vessel));

        int stage = Operations.countDecoupleStages(vessel);

        while (vessel.resourcesInDecoupleStage(stage - 1, false).amount(fuel) > 1
                && vessel.resourcesInDecoupleStage(stage - 1, false).amount(oxidizer) >1) {

            pitch = (float) (constants[0] - constants[1] * Math.tanh(constants[2]
                    * (vessel.flight(vessel.getReferenceFrame()).getMeanAltitude()/1000 - constants[3])));

            vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);
            System.out.printf("Pitch: %6.3f\n", pitch);
        }
        Operations.stage(vessel, stage);

        connection.close();
    }

}

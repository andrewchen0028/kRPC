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

        float heading = 90;
        String fuel = "Kerosene";
        String oxidizer = "LqdOxygen";
        double[] constants = {110, 70, 0.125, -1.5}; // steering constants

        vessel.getAutoPilot().engage();
        vessel.getControl().setThrottle(1);

        Operations.stage(vessel); // ignition
        while (vessel.getThrust() < 0.95 * vessel.getMaxThrust()) {
            TimeUnit.MILLISECONDS.sleep(10);
        }

        Operations.stage(vessel);
        double UT0 = spaceCenter.getUT();

        System.out.println("Stages: " + Operations.countDecoupleStages(vessel));
        int stage = Operations.countDecoupleStages(vessel);

        while (vessel.resourcesInDecoupleStage(stage - 1, false).amount(fuel) > 1
                && vessel.resourcesInDecoupleStage(stage - 1, false).amount(oxidizer) > 1) {

            float pitch = (float) (constants[0] - constants[1] * Math.tanh(constants[2]
                    * (vessel.flight(vessel.getReferenceFrame()).getMeanAltitude() / 1000 - constants[3])));
            vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);

            double time = spaceCenter.getUT() - UT0;
            System.out.printf("MET: %6.2f   Pitch: %4.2f\n", time, pitch);
        }

        Operations.stage(vessel, stage);

        connection.close();
    }

}

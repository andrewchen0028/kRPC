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
                "192.168.43.105",
                50000,
                50001);*/

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel = spaceCenter.getActiveVessel();

        float pitch = 90;
        float heading = 90;
        float throttle = 1;
        long dt = 10;
        String fuel = "Kerosene";
        String oxidizer = "LqdOxygen";

        vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);
        vessel.getAutoPilot().engage();
        vessel.getControl().setThrottle(throttle);

        Operations.stage(vessel); // ignition
        while (vessel.getThrust() < 0.95 * vessel.getMaxThrust()) {
            TimeUnit.MILLISECONDS.sleep(dt);
        }
        Operations.stage(vessel);

        int stage = Operations.countDecoupleStages(vessel) - 1;
        System.out.println("Stages: " + stage);

        double UT0 = spaceCenter.getUT();
        while (vessel.resourcesInDecoupleStage(stage, false).amount(fuel) != 0
                && vessel.resourcesInDecoupleStage(stage, false).amount(oxidizer) != 0) {
            pitch = 90 - (float) (spaceCenter.getUT() - UT0) * 0.4f;
            vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);
            System.out.printf(
                    "MET: %-6.2f   Pitch: %-4.1f   Fuel: %-6f   Oxidizer: %-6f\n",
                    spaceCenter.getUT() - UT0, pitch,
                    vessel.resourcesInDecoupleStage(stage, false).amount(fuel),
                    vessel.resourcesInDecoupleStage(stage, false).amount(oxidizer));
        }
        Operations.stage(vessel, stage);

        connection.close();
    }

}

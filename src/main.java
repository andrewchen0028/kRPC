import javafx.scene.control.OverrunStyle;
import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.KRPC;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.*;

import java.io.IOException;
import java.sql.Time;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String args[]) throws IOException, RPCException, InterruptedException {

        // Connection block
        //Connection connection = Connection.newInstance();

        Connection connection = Connection.newInstance(
        "connection",
        "192.168.43.105",
        50000,
        50001);

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

        vessel.getControl().activateNextStage(); // ignition
        while (vessel.getThrust() < 0.95 * vessel.getMaxThrust()) {
            TimeUnit.MILLISECONDS.sleep(dt);
        }
        vessel.getControl().activateNextStage();

        while (vessel.getResources().amount(fuel) != 0 && vessel.getResources().amount(oxidizer) != 0) {
            TimeUnit.MILLISECONDS.sleep(dt);
            pitch -= 0.00375;
            vessel.getAutoPilot().targetPitchAndHeading(pitch, heading);
        }
        vessel.getControl().activateNextStage();

        connection.close();
    }

}

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
        "10.192.24.90",
        50000,
        50001);

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel = spaceCenter.getActiveVessel();

        Operations.countStages(vessel);

        connection.close();
    }

}

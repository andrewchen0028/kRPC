import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.KRPC;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.*;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String args[]) throws IOException, RPCException, InterruptedException {

        // Connection block
        Connection connection = Connection.newInstance(
                "connection",
                "10.192.25.151",
                50000,
                50001);
        //Connection connection = Connection.newInstance();

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel Falcon1 = spaceCenter.getActiveVessel();

        Operations.launch(Falcon1);
        Operations.holdAltitude(Falcon1, 1000);
        connection.close();
    }

}

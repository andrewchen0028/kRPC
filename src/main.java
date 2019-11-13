import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.services.KRPC;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.*;

import java.io.IOException;

public class main {

    public static void main(String args[]) throws IOException, RPCException, InterruptedException {

        // Connection block
        Connection connection = Connection.newInstance("connection", "10.192.23.55", 50000, 50001);
        //Connection connection = Connection.newInstance();

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel Falcon1 = spaceCenter.getActiveVessel();

        vesselOps.launch(Falcon1, 1000);
        System.out.println("yeet");
        connection.close();
    }

}

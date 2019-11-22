import javafx.scene.control.OverrunStyle;
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
        Connection connection = Connection.newInstance();

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel Falcon9 = spaceCenter.getActiveVessel();

        for (Part part : Falcon9.getParts().inDecoupleStage(4)) {
            part.setHighlighted(true);
        }

        TimeUnit.SECONDS.sleep(5);

        connection.close();
    }

}

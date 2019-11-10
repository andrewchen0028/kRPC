import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter;

import java.io.IOException;

import java.util.concurrent.TimeUnit;

public class main {

    public static void main (String args[]) throws IOException, RPCException, StreamException, InterruptedException {
        Connection connection = Connection.newInstance("connection", "10.192.30.138", 50000, 50001);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();
        vessel.getControl().setThrottle(1);
        TimeUnit.SECONDS.sleep(5);
        vessel.getControl().activateNextStage();
        }
    }

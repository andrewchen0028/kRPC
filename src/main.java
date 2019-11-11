import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Stream;
import krpc.client.StreamException;
import krpc.client.services.SpaceCenter;

import java.io.IOException;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main (String args[]) throws IOException, RPCException, StreamException, InterruptedException {
        //Connection connection = Connection.newInstance("connection", "10.192.30.138", 50000, 50001);
        Connection connection = Connection.newInstance();
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        SpaceCenter.Vessel vessel = spaceCenter.getActiveVessel();

        Stream<Float> thrustStream = connection.addStream(vessel, "getThrust");

        while (true) {
            System.out.println(thrustStream.get());
            TimeUnit.MILLISECONDS.sleep(250);
        }
        }
    }

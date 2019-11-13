import krpc.client.Connection;
import krpc.client.RPCException;
import krpc.client.Event;
import krpc.client.StreamException;
import krpc.client.services.KRPC;
import krpc.client.services.KRPC.Expression;
import krpc.client.services.SpaceCenter;
import krpc.client.services.SpaceCenter.*;
import krpc.schema.KRPC.ProcedureCall;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String args[]) throws IOException, RPCException, StreamException, InterruptedException {

        // Connection block
        Connection connection = Connection.newInstance("connection", "10.192.23.55", 50000, 50001);
        //Connection connection = Connection.newInstance();

        // Initialize KRPC, SpaceCenter, and Vessel
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel = spaceCenter.getActiveVessel();

        // Ignition block
        System.out.println("Ignition");
        vessel.getControl().setThrottle(1);
        vessel.getAutoPilot().targetPitchAndHeading(85, 90);
        vessel.getAutoPilot().engage();
        vessel.getControl().activateNextStage();

        // Set thrust and staging markers
        float liftoffThrust = 0.95f * vessel.getMaxThrust();
        ProcedureCall thrust = connection.getCall(vessel, "getThrust");

        // Set liftoff flag
        Expression liftoff = Expression.greaterThan(
                connection,
                Expression.call(connection, thrust),
                Expression.constantFloat(connection, liftoffThrust));
        Event liftoffFlag = krpc.addEvent(liftoff);

        // Set separation flag
        Expression separation = Expression.equal(
                connection,
                Expression.call(connection, thrust),
                Expression.constantFloat(connection, 0f));
        Event separationFlag = krpc.addEvent(separation);

        // Liftoff thread
        synchronized (liftoffFlag.getCondition()) {
            separationFlag.waitFor();
            System.out.println('3');
            TimeUnit.SECONDS.sleep(1);
            System.out.println('2');
            TimeUnit.SECONDS.sleep(1);
            System.out.println('1');
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Liftoff");
            vessel.getControl().activateNextStage();
        }

        // Separation thread
        synchronized (separationFlag.getCondition()) {
            separationFlag.waitFor();
            System.out.println("Separation");
            vessel.getControl().setForward(1f);
            TimeUnit.MILLISECONDS.sleep(500);
            vessel.getControl().activateNextStage();
            vessel.getControl().setForward(0f);
        }
    }
}

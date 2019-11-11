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


public class main {

    public static void main (String args[]) throws IOException, RPCException, StreamException {
        /*
        Connection connection = Connection.newInstance("connection", "10.192.30.138", 50000, 50001);
         */
        Connection connection = Connection.newInstance();
        KRPC krpc = KRPC.newInstance(connection);
        SpaceCenter spaceCenter = SpaceCenter.newInstance(connection);
        Vessel vessel = spaceCenter.getActiveVessel();

        float maxThrust = 0.95f*vessel.getMaxThrust();
        Boolean launched;

        ProcedureCall thrust = connection.getCall(vessel, "getThrust");

        Expression fullThrust = Expression.greaterThan(
                connection,
                Expression.call(connection, thrust),
                Expression.constantFloat(connection, maxThrust));

        Expression zeroThrust = Expression.equal(
                connection,
                Expression.call(connection, thrust),
                Expression.constantFloat(connection, 0f));

        System.out.println("Ignition");
        vessel.getControl().setThrottle(1);
        vessel.getControl().activateNextStage();

        Event liftoff = krpc.addEvent(fullThrust);
        Event separation = krpc.addEvent(zeroThrust);

        synchronized (liftoff.getCondition()) {
            liftoff.waitFor();
            System.out.println("Liftoff");
            vessel.getControl().activateNextStage();
            launched = true;
        }

        synchronized (separation.getCondition()) {
            separation.waitFor();
            if (launched) {
                System.out.println("Separation");
                vessel.getControl().activateNextStage();
            }
        }

        System.out.println("Done");

        }
    }

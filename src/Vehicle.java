import krpc.client.RPCException;
import krpc.client.services.SpaceCenter.Vessel;

import java.util.concurrent.TimeUnit;

public class Vehicle {

    public Vessel vessel;

    Vehicle(Vessel vessel){
        this.vessel = vessel;
    }

    public void launch(float ap, float pitchrate) throws InterruptedException, RPCException {

        // ignition and liftoff
        vessel.getControl().setThrottle(1);
        vessel.getControl().activateNextStage(); // ignition
        ops.countdown(5);
        System.out.println("Liftoff");
        vessel.getControl().activateNextStage(); // liftoff

        // burn
        while(vessel.getOrbit().getApoapsisAltitude() < ap) {
            if(vessel.getThrust() == 0 ) {
                vessel.getControl().activateNextStage(); // stage if necessary
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }

        vessel.getControl().setThrottle(0);
        System.out.println("done");


    }

}

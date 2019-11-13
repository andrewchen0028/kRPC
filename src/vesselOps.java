import krpc.client.services.SpaceCenter;

import java.util.concurrent.TimeUnit;

import static krpc.client.services.SpaceCenter.*;

public class vesselOps {

    public static void countdown(int t) throws InterruptedException {
        while(t > 0) {
            System.out.println(t);
            TimeUnit.SECONDS.sleep(1);
            t--;
        }
    }

    public static void launch(Vessel vessel, float ap) {

    }
}

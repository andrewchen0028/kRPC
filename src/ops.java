import java.util.concurrent.TimeUnit;

public class ops {

    public static void countdown(int t) throws InterruptedException {
        while(t > 0) {
            System.out.println(t);
            TimeUnit.SECONDS.sleep(1);
            t--;
        }
    }
}

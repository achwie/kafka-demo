package achwie.shop.util.latency;

/**
 * This MBean implements a latency service to introduce artificial latency into
 * a service.
 * 
 * @author 23.02.2016, Achim Wiedemann
 *
 */
public class LatencySimulator implements LatencySimulatorMBean {
  public static final String OBJECT_NAME = "achwie.kafka-demo:type=LatencySimulator";
  private volatile int latencyMillis = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLatencyMillis(int latencyMillis) {
    if (latencyMillis >= 0) {
      this.latencyMillis = latencyMillis;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLatencyMillis() {
    return latencyMillis;
  }

  /**
   * Lets the current thread sleep for the amount of milliseconds specified via
   * {@link #setLatencyMillis(int)}.
   */
  public void beLatent() {
    try {
      if (latencyMillis > 0) {
        try {
          Thread.sleep(latencyMillis);
        } catch (InterruptedException e) {
          Thread.interrupted(); // Reset flag
        }
      }
    } catch (Exception e) {
      System.err.println("ERROR: Could not be latent for a bit. Reason: " + e.getMessage());
    }
  }
}

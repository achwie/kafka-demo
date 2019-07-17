package achwie.shop;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.MBeanExporter;

import achwie.shop.util.latency.LatencySimulator;

/**
 * 
 * @author 02.01.2016, Achim Wiedemann
 */
@SpringBootApplication
public class StockStarter {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(StockStarter.class, args);
  }

  @Bean
  public LatencySimulator createLatencySimulator() {
    return new LatencySimulator();
  }

  @Bean
  public MBeanExporter createMBeanExporter(LatencySimulator latencySimulator) {
    final var beans = new HashMap<String, Object>();
    beans.put(LatencySimulator.OBJECT_NAME, latencySimulator);

    final var mBeanExporter = new MBeanExporter();
    mBeanExporter.setBeans(beans);

    return mBeanExporter;
  }

}

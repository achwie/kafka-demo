package achwie.shop.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 
 * @author 01.02.2016, Achim Wiedemann
 */
// TODO: Move to shared library (there's the same class in frontend service)
public abstract class HystrixRestCommand<T> extends HystrixCommand<T> {
  protected final Logger LOG = LoggerFactory.getLogger(getClass());
  protected final RestOperations restOps;

  protected HystrixRestCommand(HystrixCommandGroupKey group, RestOperations restOps) {
    super(group);
    this.restOps = restOps;
  }

  protected void logExecutionFailure(final String errMsg) {
    final Throwable executionFailureCause = getFailedExecutionException();
    if (executionFailureCause != null) {
      LOG.error(errMsg, executionFailureCause);
    }
  }

  protected void logExecutionFailure() {
    logExecutionFailure("Execution of " + getClass().getSimpleName() + " failed!");
  }
}

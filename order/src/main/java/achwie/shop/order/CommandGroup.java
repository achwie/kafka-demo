package achwie.shop.order;

import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 
 * @author 01.02.2016, Achim Wiedemann
 */
public enum CommandGroup implements HystrixCommandGroupKey {
  STOCK_PLACE_HOLD, AUTH_GET_USERID_FOR_SESSION, GET_PRODUCT_DETAILS;
}

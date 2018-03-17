package achwie.shop.order.eventhandler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import achwie.shop.event.impl.EventHandler;
import achwie.shop.event.impl.EventHandlerChain;
import achwie.shop.event.impl.EventVersion;
import achwie.shop.order.event.OrderConfirmed;
import achwie.shop.order.event.OrderPostedByCustomer;
import achwie.shop.order.store.write.MutableOrder;
import achwie.shop.order.store.write.MutableOrderItem;
import achwie.shop.order.store.write.OrderReadWriteRepository;
import achwie.shop.order.store.write.OrderStatus;
import achwie.shop.order.write.OrderEventPublisher;

/**
 * 
 * @author 09.03.2018, Achim Wiedemann
 *
 */
@Component
public class OrderPostedByCustomerHandler implements EventHandler<OrderPostedByCustomer> {
  private final OrderReadWriteRepository orderRepo;
  private final OrderEventPublisher orderEventPublisher;

  @Autowired
  public OrderPostedByCustomerHandler(EventHandlerChain handlerChain, OrderEventPublisher orderEventPublisher, OrderReadWriteRepository orderRepo) {
    this.orderRepo = orderRepo;
    this.orderEventPublisher = orderEventPublisher;

    handlerChain.addEventHandler(this);
  }

  @Override
  public void onEvent(OrderPostedByCustomer event) {
    final MutableOrder order = createOrderFromEvent(event);

    orderRepo.save(order);

    System.out.println("Received order and saved it under id " + order.getId());
    // TODO: Check availability
    System.out.println("Checked availability for order " + order.getId());
    // TODO: Get customer details (shipping address)
    System.out.println("Got customer details for order " + order.getId());
    // TODO: Get product details (prices)
    System.out.println("Got product details for order " + order.getId());
    // TODO: Set oder status to "confirmed"
    order.setStatus(OrderStatus.CONFIRMED);
    // TODO: Pass on information in OrderConfirmed event

    final OrderConfirmed orderConfirmed = new OrderConfirmed(order.getId());

    orderEventPublisher.publish(orderConfirmed);
  }

  @Override
  public EventVersion getProcessableEventVersion() {
    return EventVersions.ORDER_POSTED_BY_CUSTOMER_1_0;
  }

  private MutableOrder createOrderFromEvent(OrderPostedByCustomer event) {
    final OrderStatus status = OrderStatus.REGISTERED;
    final String userId = event.getUserId();
    final ZonedDateTime orderTime = event.getOrderDate();
    final List<MutableOrderItem> items = createOrderItemsFromEvent(event);

    return new MutableOrder(null, status, userId, orderTime, items);
  }

  private List<MutableOrderItem> createOrderItemsFromEvent(OrderPostedByCustomer event) {
    final String[] productIds = event.getProductIds();
    final int[] quantities = event.getQuantities();

    Assert.isTrue(productIds.length == quantities.length,
        String.format("Number of product IDs and quantities does not match (productIds count: %d, quantities count: %s, userId: %s)!",
            productIds.length,
            quantities.length,
            event.getUserId()));

    final List<MutableOrderItem> orderItems = new ArrayList<>();
    for (int i = 0; i < productIds.length; i++)
      orderItems.add(new MutableOrderItem(productIds[i], quantities[i]));

    return orderItems;
  }
}

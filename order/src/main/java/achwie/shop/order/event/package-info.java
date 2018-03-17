/**
 * <p>
 * Contains the events for an order. The flow of an order is as follows:
 * </p>
 * <ol>
 * <li>An order has been posted by the customer:
 * {@link achwie.shop.order.event.OrderPostedByCustomer}</li>
 * <li>Order details such as availability, shipping address, and pricing have
 * been fetched: {@link achwie.shop.order.event.OrderConfirmed}</li>
 * <li>Payment for the order was successful:
 * {@link achwie.shop.order.event.OrderPayed}</li>
 * <li>The order has been physically packaged and shipped out to the customer:
 * {@link achwie.shop.order.event.OrderShipped}</li>
 * </ol>
 * 
 */
package achwie.shop.order.event;
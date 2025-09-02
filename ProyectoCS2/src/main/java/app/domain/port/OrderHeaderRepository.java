package app.domain.port;

import java.util.Optional;

import app.domain.model.OrderHeader;

public interface OrderHeaderRepository {
    boolean existsOrderNumber(String orderNumber);
    OrderHeader save(OrderHeader header);
    Optional<OrderHeader> findByNumber(String orderNumber);
}

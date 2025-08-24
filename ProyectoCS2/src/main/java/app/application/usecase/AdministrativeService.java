package app.application.usecase;

import app.domain.model.Order;
import app.domain.model.Invoice;
import app.domain.model.InventoryItem;
import app.domain.repository.OrderRepository;
import app.domain.repository.InvoiceRepository;
import app.domain.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrativeService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final InventoryRepository inventoryRepository;

    public AdministrativeService(OrderRepository orderRepository,
        InvoiceRepository invoiceRepository,
        InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // Orders
    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String orderNumber) {
        return orderRepository.findById(orderNumber);
    }

    // Invoices
    public void createInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(String id) {
        return invoiceRepository.findById(id);
    }

    // Inventory
    public void createInventoryItem(InventoryItem item) {
        inventoryRepository.save(item);
    }

    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<InventoryItem> getItemById(String id) {
        return inventoryRepository.findById(id);
    }
}

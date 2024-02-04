package com.shopwa.shoppingcart;

import com.shopwa.entity.CartItem;
import com.shopwa.entity.Customer;
import com.shopwa.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTests {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testSaveItem(){
        Integer customer_id = 5;
        Integer product_id = 5;

        Customer customer = entityManager.find(Customer.class, customer_id);
        Product product = entityManager.find(Product.class, product_id);

        CartItem newItem = new CartItem();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(1);

        CartItem cartItem = cartItemRepository.save(newItem);

        assertThat(cartItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSave2Item(){
        Integer customer_id = 10;
        Integer product_id = 10;


        Customer customer = entityManager.find(Customer.class, customer_id);
        Product product = entityManager.find(Product.class, product_id);

        CartItem item1 = new CartItem();
        item1.setCustomer(customer);
        item1.setProduct(product);
        item1.setQuantity(5);

        CartItem item2 = new CartItem();
        item2.setCustomer(new Customer(customer_id));
        item2.setProduct(new Product(product_id));
        item2.setQuantity(10);

        List<CartItem> cartItems = cartItemRepository.saveAll(List.of(item1, item2));


        assertThat(cartItems.size()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer() {
        Integer customerId = 10;

        List<CartItem> listItems = cartItemRepository.findByCustomer(new Customer(customerId));
        listItems.forEach(System.out::println);
        assertThat(listItems.size()).isEqualTo(3);
    }

    @Test
    public void testFindByCustomerAndProduct() {
        Integer customer_id = 5;
        Integer product_id = 5;

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(new Customer(customer_id), new Product(product_id));

        System.out.println(cartItem);
    }

    @Test
    public void testUpdateQuantity() {
        Integer customerId = 41;
        Integer productId = 1;
        Integer quantity = 5;

        cartItemRepository.updateQuantity(quantity, customerId, productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(cartItem.getQuantity()).isEqualTo(5);

    }

    @Test
    public void testDelete() {
        Integer customerId = 5;
        Integer productId = 5;

        cartItemRepository.deleteByCustomerAndProduct(customerId, productId);

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

        assertThat(cartItem).isNull();
    }
}

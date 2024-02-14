package com.shopwa.shoppingcart;

import com.shopwa.ControllerHelper;
import com.shopwa.address.AddressService;
import com.shopwa.entity.Address;
import com.shopwa.entity.CartItem;
import com.shopwa.entity.Customer;
import com.shopwa.entity.ShippingRate;
import com.shopwa.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired private ShoppingCartService cartService;
    @Autowired private ControllerHelper controllerHelper;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shippingRateService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        Customer customer =  controllerHelper.getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        float totalCart = 0.0f;
        for(CartItem cartItem : cartItems) {
            totalCart += cartItem.getSubtotal();
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean userPrimaryAddressAsDefault = false;

        if (defaultAddress != null) {
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            userPrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("userPrimaryAddressAsDefault", userPrimaryAddressAsDefault);
        model.addAttribute("shippingSupported", shippingRate!=null);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalCart", totalCart);

        return "cart/shopping_cart";
    }

//    private Customer getAuthenticatedCustomer(HttpServletRequest request){
//        String email = Utility.getEmailOfAuthenticatedCustomer(request);
//        return customerService.getCustomerByEmail(email);
//    }
}

package com.shopwa.checkout;

import com.shopwa.ControllerHelper;
import com.shopwa.Utility;
import com.shopwa.address.AddressService;
import com.shopwa.checkout.paypal.PayPalApiException;
import com.shopwa.checkout.paypal.PayPalService;
import com.shopwa.entity.Address;
import com.shopwa.entity.CartItem;
import com.shopwa.entity.Customer;
import com.shopwa.entity.ShippingRate;
import com.shopwa.entity.order.Order;
import com.shopwa.entity.order.PaymentMethod;
import com.shopwa.order.OrderService;
import com.shopwa.setting.CurrencySettingBag;
import com.shopwa.setting.EmailSettingBag;
import com.shopwa.setting.PaymentSettingBag;
import com.shopwa.setting.SettingService;
import com.shopwa.shipping.ShippingRateService;
import com.shopwa.shoppingcart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired private CheckoutService checkoutService;
    @Autowired private ControllerHelper controllerHelper;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shippingRateService;
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;
    @Autowired private PayPalService payPalService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request){
        Customer customer =  controllerHelper.getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            model.addAttribute("shippingAddress", customer.toString());
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        if (shippingRate == null) {
            return "redirect:/cart";
        }
        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSetting = settingService.getPaymentSetting();
        String paypalClientId = paymentSetting.getClientID();

        model.addAttribute("paypalClientId",paypalClientId);
        model.addAttribute("currencyCode",currencyCode);
        model.addAttribute("customer",customer);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "/checkout/checkout";
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer =  controllerHelper.getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        if (defaultAddress != null) {
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        Order createOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
        shoppingCartService.deleteByCusmter(customer);
        sendOrderConfimationEmail(request, createOrder);

        return "checkout/order_completed";
    }

    private void sendOrderConfimationEmail(HttpServletRequest request, Order order)
            throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSetting);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = order.getCustomer().getEmail();
        String subject = emailSetting.getOrderConfirmationSubject();
        String content = emailSetting.getOrderConfirmationContent();

        subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSetting.getFromAddess(), emailSetting.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MM yyyy");
        String orderTime = dateFormat.format(order.getOrderTime());

        CurrencySettingBag currencySettingBag = settingService.getCurrencySetting();
        String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettingBag);

        content = content.replace("[[name]]", order.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(order.getId()));
        content = content.replace("[[orderTime]]", orderTime);
        content = content.replace("[[shippingAddress]]", order.getShippingAddress());
        content = content.replace("[[postalCode]]", order.getPostalCode());
        content = content.replace("[[phoneNumber]]", order.getPhoneNumber());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());

        helper.setText(content, true);
        mailSender.send(message);
    }

    @PostMapping("/process_paypal_order")
    public String processPayPalOrder(HttpServletRequest request, Model model)
            throws UnsupportedEncodingException, MessagingException {
        String orderId = request.getParameter("orderId");

        String pageTitle = "Checkout Failure";
        String message = null;

        try {
            if (payPalService.validateOrder(orderId)) {
                return placeOrder(request);
            } else {
                pageTitle = "Checkout Failure";
                message = "ERROR: Transaction could not be completed because order information is invalid";
            }
        } catch (PayPalApiException e) {
            message = "ERROR: Transaction failed due to error: " + e.getMessage();
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("title", pageTitle);
        model.addAttribute("message", message);

        return "message";
    }
}

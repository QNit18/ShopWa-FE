package com.shopwa.checkout;


import com.shopwa.checkout.paypal.PayPalOrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class PayPalApiTests {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AbY3VOY1HKx2wbCCWMVoPyIdejNmj5L7d-fUf8oxtBFmOR6t_znH3UlEGI-zRPVLTIOm_lAYu2a476M3";
    private static final String CLIENT_SECRET = "EDOXYSZbecXdWxYc2J_MIVqILr6T2j7obrAWOEi8LrC6wERIfxyBGleox8RNPFJ0_iXyyjtapFdXTtDX";

    @Test
    public void testGetOrderDetails() {
        String orderId = "8CX10440Y22687449";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
        PayPalOrderResponse orderResponse = response.getBody();
        System.out.println("Order Id: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));
    }

}

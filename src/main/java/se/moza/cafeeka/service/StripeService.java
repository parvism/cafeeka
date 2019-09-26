package se.moza.cafeeka.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import se.moza.cafeeka.model.Currency;
import se.moza.cafeeka.model.CustomerOrder;


@Service
public class StripeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    public Charge charge(CustomerOrder customerOrder, String stripeToken)
            throws AuthenticationException, InvalidRequestException,
            ApiConnectionException, CardException, ApiException, StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (customerOrder.getTotalPrice() * 100));
        chargeParams.put("currency", Currency.SEK);
        chargeParams.put("description", "Payer: "
                + customerOrder.getCustomerName() + " "
                + customerOrder.getCustomerEmail() + " "
                + "to Cafeeka online order");
        chargeParams.put("source", stripeToken);
        return Charge.create(chargeParams);
    }
}

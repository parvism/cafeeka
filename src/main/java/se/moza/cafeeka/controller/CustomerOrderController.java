package se.moza.cafeeka.controller;

import static se.moza.cafeeka.config.Constants.URI_API_PREFIX;

import java.util.List;

import com.stripe.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;

import se.moza.cafeeka.model.CustomerOrder;
import se.moza.cafeeka.service.CustomerOrderService;
import se.moza.cafeeka.service.OrderDTO;
import se.moza.cafeeka.exception.BadRequestException;
import se.moza.cafeeka.exception.CustomExceptionResponse;


@RestController
@RequestMapping(value = URI_API_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOrderController.class);

    @Autowired
    private CustomerOrderService customerOrderService;


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/orders", params={ "page", "size" }, method=RequestMethod.GET)
    public ResponseEntity<List<OrderDTO>> getAllOrdersPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {

        LOGGER.info("getting all orders");
        List<OrderDTO> orderDTOs = customerOrderService.getAllCustomerOrdersPaginated(page, size);

        LOGGER.info("object..." + orderDTOs);

        return new ResponseEntity<List<OrderDTO>>(orderDTOs, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable long id) {

        LOGGER.info("getting order with id: {}", id);
        OrderDTO orderDTO = customerOrderService.getCustomerOrder(id);

        return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.OK);

    }


    //	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.POST, value="/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {

        LOGGER.info("creating new order: {}", orderDTO);


        Preconditions.checkNotNull(orderDTO);
        CustomerOrder customerOrder = customerOrderService.createCustomerOrder(orderDTO);
        orderDTO = customerOrderService.createOrderDTOFromCustomerOrder(customerOrder);

        return new ResponseEntity<OrderDTO>(orderDTO , HttpStatus.CREATED);

    }

    //	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.POST, value="/orders/card")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO ,
                                                @RequestHeader(value="stripeToken") String stripeToken) {


        LOGGER.info("creating new order: {}", orderDTO);
        LOGGER.info("charging the order to Stripe with token: {}", stripeToken);

        Preconditions.checkNotNull(orderDTO);

        CustomerOrder customerOrder = new CustomerOrder();
        try {
            customerOrder = customerOrderService.createCustomerOrderByStripe(orderDTO, stripeToken);
        }
        catch (AuthenticationException e) {
            LOGGER.error("Unable to charge. Failure to properly authenticate yourself in the request.");
            return new ResponseEntity(new CustomExceptionResponse("Unable to charge. Authentication error.", "BAD_REQUEST"),
                    HttpStatus.BAD_REQUEST);
        }
        catch (InvalidRequestException e) {
            LOGGER.error("Unable to charge.  your request has invalid parameters..");
            return new ResponseEntity(new CustomExceptionResponse("Unable to charge.  your request has invalid parameters.", "BAD_REQUEST"),
                    HttpStatus.BAD_REQUEST);
        }
        catch (ApiConnectionException e) {
            LOGGER.error("Unable to charge. Failure to connect to Stripe's API.");
            return new ResponseEntity(new CustomExceptionResponse("Unable to charge. Failure to connect to Stripe's API.", "SERVICE_UNAVAILABLE"),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (CardException e) {
            LOGGER.error("Unable to charge. Card errors, can't be charged for some reason.");
            return new ResponseEntity(new CustomExceptionResponse("Unable to charge. Card errors, can't be charged for some reason.", "BAD_REQUEST"),
                    HttpStatus.BAD_REQUEST);
        }
        catch (StripeException e) {
            LOGGER.error("Unable to charge. Temporary problem with Stripe's servers.");
            return new ResponseEntity(new CustomExceptionResponse("Unable to charge. Stripe API unavialable.", "SERVICE_UNAVAILABLE"),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        orderDTO = customerOrderService.createOrderDTOFromCustomerOrder(customerOrder);

        return new ResponseEntity<OrderDTO>(orderDTO , HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.PUT, value="/orders/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO updatedOrderDTO, @PathVariable long id) {

        LOGGER.info("Updating order with id {}", id);

        Preconditions.checkNotNull(updatedOrderDTO);
        updatedOrderDTO = customerOrderService.updateCustomerOrder(updatedOrderDTO, id);

        return new ResponseEntity<OrderDTO>(updatedOrderDTO, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.DELETE, value="/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {

        customerOrderService.deleteCustomerOrder(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

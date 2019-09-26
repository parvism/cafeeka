package se.moza.cafeeka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.stripe.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.stripe.model.Charge;
import se.moza.cafeeka.exception.BadRequestException;
import se.moza.cafeeka.exception.CafeResourceNotFoundException;
import se.moza.cafeeka.model.*;
import se.moza.cafeeka.repo.*;

@Service
public class CustomerOrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private MenuOrderRepository menuOrderRepository;
    @Autowired
    private DrinkOrderRepository drinkOrderRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private StripeService stripeService;


    public List<OrderDTO> getAllCustomerOrdersPaginated(int page, int size){

        if (size == 0) {
            throw new BadRequestException("Page size must not be less than one!");
        }

        List<OrderDTO> orderDTOs = new ArrayList<>();
        List<CustomerOrder> customerOrders = new ArrayList<>();

        Sort sort = new Sort(new Sort.Order(Direction.DESC, "updatedAt"));
        Pageable pageable = new PageRequest(page, size, sort);

        customerOrderRepository.findAll(pageable).forEach(customerOrders::add);

        for(CustomerOrder customerOrder: customerOrders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO = createOrderDTOFromCustomerOrder(customerOrder);
            orderDTOs.add(orderDTO);
        }

        return orderDTOs;
    }


    public OrderDTO getCustomerOrder(long id) {

        Optional<CustomerOrder> optionalCustomerOrder = customerOrderRepository.findById(id);
        OrderDTO orderDTO = new OrderDTO();

        if(optionalCustomerOrder.isEmpty()){
            throw new CafeResourceNotFoundException("CustomerOrder", "id", id);
        }
        return orderDTO = createOrderDTOFromCustomerOrder(optionalCustomerOrder.get());

    }

    public CustomerOrder createCustomerOrder(OrderDTO orderDTO) {

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrderRepository.save(customerOrder);	// To get an id from database
        customerOrder = createCustomerOrderFromOrderDTO(orderDTO, customerOrder);

        customerOrderRepository.save(customerOrder);
        return customerOrder;

    }


    public CustomerOrder createCustomerOrderByStripe(OrderDTO orderDTO, String stripeToken) throws AuthenticationException,
            InvalidRequestException, ApiConnectionException, CardException, ApiException, StripeException{

        CustomerOrder customerOrder = createCustomerOrder(orderDTO);

        Charge charge = stripeService.charge(customerOrder, stripeToken);

        customerOrder.setPaymentStatus(PaymentStatus.PAID);
        customerOrder.setBalanceTransactionId(charge.getBalanceTransaction());
        customerOrder.setChargeId(charge.getId());

        customerOrderRepository.save(customerOrder);
        return customerOrder;
    }

    public OrderDTO updateCustomerOrder(OrderDTO orderDTO, long customerOrderId) {

        Optional<CustomerOrder> optionalCustomerOrder = customerOrderRepository.findById(customerOrderId);

        if(!optionalCustomerOrder.isPresent()){
            throw new CafeResourceNotFoundException("CustomerOrder", "id", customerOrderId);
        }

        CustomerOrder customerOrder = optionalCustomerOrder.get();


        customerOrder.clearMenuOrders();
        customerOrder.clearDrinkOrders();

        List<MenuOrder> menuOrders = new ArrayList<>();
        menuOrders = menuOrderRepository.findByCustomerOrderId(customerOrder.getId());
        for(MenuOrder menuOrder: menuOrders)
            menuOrderRepository.delete(menuOrder);

        List<DrinkOrder> drinkOrders = new ArrayList<>();
        drinkOrders = drinkOrderRepository.findByCustomerOrderId(customerOrder.getId());

        for(DrinkOrder drinkOrder: drinkOrders)
            drinkOrderRepository.delete(drinkOrder);

        customerOrder = createCustomerOrderFromOrderDTO(orderDTO, customerOrder);

        customerOrderRepository.save(customerOrder);

        OrderDTO newOrderDTO = new OrderDTO();
        newOrderDTO = createOrderDTOFromCustomerOrder(customerOrder);

        return newOrderDTO;
    }

    public void deleteCustomerOrder(long id) {

        Optional<CustomerOrder> OptionalCustomerOrder = customerOrderRepository.findById(id);

        if(!OptionalCustomerOrder.isPresent()) {
            throw new CafeResourceNotFoundException("customerOrder", "id", id);
        }
        customerOrderRepository.delete(OptionalCustomerOrder.get());
    }

    public OrderDTO createOrderDTOFromCustomerOrder(CustomerOrder customerOrder) {

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setCustomerOrderId(Long.toString(customerOrder.getId()));
        orderDTO.setChargeId(customerOrder.getChargeId());
        orderDTO.setBalanceTransactionId(customerOrder.getBalanceTransactionId());
        orderDTO.setCustomerName(customerOrder.getCustomerName());
        orderDTO.setCustomerMobilePhone(customerOrder.getCustomerMobilePhone());
        orderDTO.setCustomerEmail(customerOrder.getCustomerEmail());
        orderDTO.setCreated(customerOrder.getCreatedAt());
        orderDTO.setPaymentMethod(customerOrder.getPaymentMethod());
        orderDTO.setPaymentStatus(customerOrder.getPaymentStatus());
        orderDTO.setMenuWithLowerPrice(customerOrder.isMenuWithLowerPrice());
        orderDTO.setTotalPrice(customerOrder.getTotalPrice());
        orderDTO.setExtraInfo(customerOrder.getExtraInfo());

        List<MenuOrder> menuOrders = new ArrayList<>();
        menuOrders = menuOrderRepository.findByCustomerOrderId(customerOrder.getId());
        List<Menu> menus = new ArrayList<>();

        for(MenuOrder menuOrder: menuOrders) {
            menus.add(menuOrder.getMenu());
        }
        orderDTO.setMenus(menus);

        List<DrinkOrder> drinkOrders = new ArrayList<>();
        drinkOrders = drinkOrderRepository.findByCustomerOrderId(customerOrder.getId());
        List<Drink> drinks = new ArrayList<>();

        for(DrinkOrder drinkOrder: drinkOrders) {
            drinks.add(drinkOrder.getDrink());
        }
        orderDTO.setDrinks(drinks);

        return orderDTO;
    }


    public CustomerOrder createCustomerOrderFromOrderDTO(OrderDTO orderDTO, CustomerOrder customerOrder) {

        if(orderDTO.getCreated() != null)
            customerOrder.setCreatedAt(orderDTO.getCreated());

        customerOrder.setPaymentStatus(PaymentStatus.UNPAID);
        customerOrder.setTotalPrice(0);

        if(orderDTO.getCustomerName() != null)
            customerOrder.setCustomerName(orderDTO.getCustomerName());

        if(orderDTO.getCustomerMobilePhone()!= null)
            customerOrder.setCustomerMobilePhone(orderDTO.getCustomerMobilePhone());

        if(orderDTO.getCustomerEmail() != null)
            customerOrder.setCustomerEmail(orderDTO.getCustomerEmail());

        if(orderDTO.getPaymentMethod() != null)
            customerOrder.setPaymentMethod(orderDTO.getPaymentMethod());

        if(orderDTO.getExtraInfo() != null)
            customerOrder.setExtraInfo(orderDTO.getExtraInfo());

        customerOrder.setMenuWithLowerPrice(orderDTO.isMenuWithLowerPrice());

        float totalPrice = 0;

        if(orderDTO.getMenus().size() != 0) {
            for(int i = 0; i < orderDTO.getMenus().size(); ++i) {
                Optional<Menu> optionalMenu = menuRepository.findById(orderDTO.getMenus().get(i).getId());
                if(!optionalMenu.isPresent()){
                    throw new CafeResourceNotFoundException("menu", "id", orderDTO.getMenus().get(i).getId());
                }
                Menu menu = optionalMenu.get();
                MenuOrder menuOrder = new MenuOrder(customerOrder, menu);
                menuOrderRepository.save(menuOrder);
                customerOrder.addMenuOrder(menuOrder);

                if(orderDTO.isMenuWithLowerPrice())
                    totalPrice += menu.getLowerPrice();
                else
                    totalPrice += menu.getHigherPrice();

            }
        }
        if(orderDTO.getDrinks().size() != 0) {
            for(int i = 0; i < orderDTO.getDrinks().size(); ++i) {
                Optional<Drink> optionalDrink = drinkRepository.findById(orderDTO.getDrinks().get(i).getId());
                if(!optionalDrink.isPresent()){
                    throw new CafeResourceNotFoundException("menu", "id", orderDTO.getDrinks().get(i).getId());
                }
                Drink drink = optionalDrink.get();
                DrinkOrder drinkOrder = new DrinkOrder(customerOrder, drink);
                drinkOrderRepository.save(drinkOrder);
                customerOrder.addDrinkOrder(drinkOrder);

                totalPrice += drink.getPrice();
            }
        }

        customerOrder.setTotalPrice(totalPrice);

        return customerOrder;
    }
}

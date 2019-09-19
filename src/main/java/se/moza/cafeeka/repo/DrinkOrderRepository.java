package se.moza.cafeeka.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import se.moza.cafeeka.model.DrinkOrder;

public interface DrinkOrderRepository extends CrudRepository<DrinkOrder, Long> {

    List<DrinkOrder> findByDrinkId(long drinkId);
    List<DrinkOrder> findByCustomerOrderId(long customerOrderId);
}

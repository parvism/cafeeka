package se.moza.cafeeka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.moza.cafeeka.exception.CafeResourceNotFoundException;
import se.moza.cafeeka.model.DrinkOrder;
import se.moza.cafeeka.repo.DrinkOrderRepository;


@Service
public class DrinkOrderService {

    @Autowired
    private DrinkOrderRepository drinkOrderRepository;

    public List<DrinkOrder> getAllDrinkOrders(){

        List<DrinkOrder> drinkOrders = new ArrayList<>();
        drinkOrderRepository.findAll().forEach(drinkOrders::add);

        return drinkOrders;
    }

    public DrinkOrder getDrinkOrder(long id) {

        Optional<DrinkOrder> drinkOrder = drinkOrderRepository.findById(id);
        if(drinkOrder.isEmpty()){
            throw new CafeResourceNotFoundException("DrinkOrder", "id", id);
        }

        return drinkOrder.get();
    }

    public void createDrinkOrder(DrinkOrder drinkOrder) {

        drinkOrderRepository.save(drinkOrder);
    }


    public void updateDrinkOrder(DrinkOrder drinkOrder) {

        drinkOrderRepository.save(drinkOrder);
    }

    public void deleteDrinkOrder(DrinkOrder drinkOrder) {

        drinkOrderRepository.delete(drinkOrder);
    }
}

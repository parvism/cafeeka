package se.moza.cafeeka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.moza.cafeeka.exception.CafeResourceNotFoundException;
import se.moza.cafeeka.model.MenuOrder;
import se.moza.cafeeka.repo.MenuOrderRepository;

@Service
public class MenuOrderService {

    @Autowired
    private MenuOrderRepository menuOrderRepository;

    public List<MenuOrder> getAllMenuOrders(){

        List<MenuOrder> menuOrders = new ArrayList<>();
        menuOrderRepository.findAll().forEach(menuOrders::add);

        return menuOrders;
    }

    public MenuOrder getMenuOrder(long id) {

        Optional<MenuOrder> optionalMenuOrder = menuOrderRepository.findById(id);
        if(optionalMenuOrder.isEmpty()){
            throw new CafeResourceNotFoundException("MenuOrder", "id", id);
        }

        return optionalMenuOrder.get();
    }

    public void createMenuOrder(MenuOrder menuOrder) {

        menuOrderRepository.save(menuOrder);
    }


    public void updateMenuOrder(MenuOrder menuOrder) {

        menuOrderRepository.save(menuOrder);
    }

    public void deleteMenuOrder(MenuOrder menuOrder) {

        menuOrderRepository.delete(menuOrder);
    }
}

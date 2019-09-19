package se.moza.cafeeka.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import se.moza.cafeeka.model.MenuOrder;

public interface MenuOrderRepository extends CrudRepository<MenuOrder, Long> {

    List<MenuOrder> findByMenuId(long menuId);
    List<MenuOrder> findByCustomerOrderId(long customerOrderId);
}

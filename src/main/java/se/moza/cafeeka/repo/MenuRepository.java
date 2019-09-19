package se.moza.cafeeka.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import se.moza.cafeeka.model.Menu;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    Menu findByName(String menuName);
    List<Menu> findByExpired(boolean expired);
}

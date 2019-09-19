package se.moza.cafeeka.repo;

import org.springframework.data.repository.CrudRepository;
import se.moza.cafeeka.model.Drink;

public interface DrinkRepository extends CrudRepository<Drink, Long> {

    Drink findByName(String drinkName);
    Iterable<Drink> findByExpired(boolean b);
}

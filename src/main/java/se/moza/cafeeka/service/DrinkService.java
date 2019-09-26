package se.moza.cafeeka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import se.moza.cafeeka.exception.CafeResourceNotFoundException;
import se.moza.cafeeka.model.Drink;
import se.moza.cafeeka.repo.DrinkRepository;
import static se.moza.cafeeka.config.Constants.URI_API_PREFIX;


@Service
@CacheConfig(cacheNames = "drinks")
public class DrinkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrinkService.class);

    @Value("${server_Url}")
    private String server_Url;

    @Autowired
    private DrinkRepository drinkRepository;


    @Cacheable
    public List<Drink> getAllDrinks(){

        List<Drink> drinks = new ArrayList<>();
        LOGGER.info("Retrieving drinks from database");
        //drinkRepository.findAll().forEach(drinks::add);
        drinkRepository.findByExpired(false).forEach(drinks::add);

        return drinks;
    }

    @Cacheable
    public Drink getDrink(long id) {
        LOGGER.info("Retrieving drink from database");

        Optional<Drink> optionalDrink = drinkRepository.findById(id);
        if(optionalDrink.isEmpty()){
            throw new CafeResourceNotFoundException("Drink", "id", id);
        }

        return optionalDrink.get();
    }

    @Cacheable
    public byte[] getDrinkPhoto(long id) {
        LOGGER.info("Retrieving drink photo from database");


        Optional<Drink> optionalDrink = drinkRepository.findById(id);
        if(optionalDrink.isEmpty()){
            throw new CafeResourceNotFoundException("Drink", "id", id);
        }

        if(optionalDrink.get().getPhoto() == null){
            throw new CafeResourceNotFoundException("Drink photo", "id", id);
        }
        return optionalDrink.get().getPhoto();
    }

    @CacheEvict(value="drinks", allEntries=true)
    public void createDrink(Drink drink) {

        drinkRepository.save(drink);
    }

    @CacheEvict(value="drinks", allEntries=true)
    public void updateDrink(Drink drink) {

        Optional<Drink> optionalDrink = drinkRepository.findById(drink.getId());
        if(optionalDrink.isEmpty()){
            throw new CafeResourceNotFoundException("Drink", "id", drink.getId());
        }
        Drink currentDrink = optionalDrink.get();

        currentDrink.setName(drink.getName());
        currentDrink.setDescription(drink.getDescription());
        currentDrink.setPrice(drink.getPrice());
        currentDrink.setAlcoholic(drink.isAlcoholic());

        drinkRepository.save(currentDrink);
    }

    @CacheEvict(value="drinks", allEntries=true)
    public Drink updateDrinkPhoto(long drinkId, byte[] photo) {
        Optional<Drink> optionalDrink = drinkRepository.findById(drinkId);
        if(optionalDrink.isEmpty()){
            throw new CafeResourceNotFoundException("Drink", "id", drinkId);
        }
        Drink drink = optionalDrink.get();

        drink.setPhoto(photo);
        drink.setPhotoUrl(server_Url + URI_API_PREFIX + "/drinks/photos/" + drink.getId());
        drinkRepository.save(drink);

        return drink;
    }

    @CacheEvict(value="drinks", allEntries=true)
    public void deleteDrink(Drink drink) {

        Optional<Drink> optionalDrink = drinkRepository.findById(drink.getId());
        if(optionalDrink.isEmpty()){
            throw new CafeResourceNotFoundException("Drink", "id", drink.getId());
        }

        drinkRepository.delete(optionalDrink.get());
    }

    public boolean exists(Drink drink) {
        if(drinkRepository.findByName(drink.getName()) != null)
            return true;
        else
            return false;
    }
}

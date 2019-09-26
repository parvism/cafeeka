package se.moza.cafeeka.controller;

import static se.moza.cafeeka.config.Constants.URI_API_PREFIX;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.base.Preconditions;

import se.moza.cafeeka.model.Drink;
import se.moza.cafeeka.service.DrinkService;
import se.moza.cafeeka.exception.CustomExceptionResponse;


@RestController
@RequestMapping(value = URI_API_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
public class DrinkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrinkController.class);

    @Autowired
    private DrinkService drinkService;


    @RequestMapping("/drinks")
    public ResponseEntity<List<Drink>> getAllDrinks() {
        LOGGER.info("getting all drinks");
        List<Drink> drinks = drinkService.getAllDrinks();
        LOGGER.info("object..." + drinks);

        return new ResponseEntity<List<Drink>>(drinks, HttpStatus.OK);
    }

    @RequestMapping("/drinks/{id}")
    public ResponseEntity<Drink> getDrink(@PathVariable long id) {
        LOGGER.info("getting drink with id: {}", id);
        Drink drink = drinkService.getDrink(id);

        return new ResponseEntity<Drink>(drink, HttpStatus.OK);
    }

    @RequestMapping("/drinks/photos/{id}")
    public ResponseEntity<byte[]> getDrinkPhoto(@PathVariable("id") long drinkId) throws IOException {

        byte[] photo = drinkService.getDrinkPhoto(drinkId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "menu_photo" + drinkId + ".jpg";
        headers.setContentDispositionFormData(filename,filename);
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.POST, value="/drinks")
    public ResponseEntity<Drink> createDrink(@RequestBody Drink drink) {
        LOGGER.info("creating new drink: {}", drink);

        Preconditions.checkNotNull(drink);

        if (drinkService.exists(drink)) {
            LOGGER.info("a menu with menu name " + drink.getName() + " already exists");
            return new ResponseEntity(new CustomExceptionResponse("a drink with drink name " + drink.getName() + " already exists", "CONFLICT"),
                    HttpStatus.CONFLICT);
        }

        drinkService.createDrink(drink);
/*
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().port(8080).path(
                "{id}").buildAndExpand(drink.getId()).toUri();
*/
        return new ResponseEntity<Drink>(drink, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.PUT, value="/drinks/{id}")
    public ResponseEntity<Drink> updateDrink(@RequestBody Drink updatedDrink, @PathVariable long id) {
        LOGGER.info("Updating drink with id {}", id);

        Preconditions.checkNotNull(updatedDrink);

        updatedDrink.setId(id);
        drinkService.updateDrink(updatedDrink);

        return new ResponseEntity<Drink>(updatedDrink, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.PUT, value="/drinks/photos/{id}")
    public ResponseEntity<Drink> updateDrinkPhoto(@PathVariable("id") long drinkId, @RequestParam("file") MultipartFile file) {

        Preconditions.checkNotNull(file);

        Drink currentDrink = drinkService.getDrink(drinkId);

        try {
            byte[] photo = file.getBytes();
            currentDrink = drinkService.updateDrinkPhoto(drinkId, photo);

        } catch (IOException e) {
            return new ResponseEntity(new CustomExceptionResponse("Cannot update photo of the drink with id: " + currentDrink.getName(), "INTERNAL SERVER ERROR"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Drink>(currentDrink, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.DELETE, value="/drinks/{id}")
    public ResponseEntity<Void> deleteDrink(@PathVariable long id) {

        Drink  drink = drinkService.getDrink(id);
        drinkService.deleteDrink(drink);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

package se.moza.cafeeka.controller;

import static se.moza.cafeeka.config.Constants.URI_API_PREFIX;

import java.io.IOException;
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
import se.moza.cafeeka.model.Menu;
import se.moza.cafeeka.service.MenuDTO;
import se.moza.cafeeka.service.MenuService;
import se.moza.cafeeka.exception.CustomExceptionResponse;

import com.google.common.base.Preconditions;


@RestController
@RequestMapping(value = URI_API_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;



    @RequestMapping("/menus")
    public ResponseEntity<MenuDTO> getAllMenus() {

        LOGGER.info("getting all menus");
        MenuDTO menusDTO = menuService.getAllMenus();

        LOGGER.info("object..." + menusDTO);

        return new ResponseEntity<MenuDTO>(menusDTO, HttpStatus.OK);

    }

    @RequestMapping("/menus/{id}")
    public ResponseEntity<Menu> getMenu(@PathVariable long id) {

        LOGGER.info("getting menu with id: {}", id);
        Menu menu = menuService.getMenu(id);

        return new ResponseEntity<Menu>(menu, HttpStatus.OK);

    }

    @RequestMapping("/menus/photos/{id}")
    public ResponseEntity<byte[]> getMenuPhoto(@PathVariable("id") long menuId) {

        byte[] photo = menuService.getMenuPhoto(menuId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String filename = "menu_photo" + menuId + ".jpg";
        headers.setContentDispositionFormData(filename,filename);
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.POST, value="/menus")
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {

        LOGGER.info("creating new menu: {}", menu);

        Preconditions.checkNotNull(menu);

        if (menuService.exists(menu)) {
            LOGGER.info("a menu with menu name " + menu.getName() + " already exists");
            return new ResponseEntity(new CustomExceptionResponse("a menu with menu name " + menu.getName() + " already exists", "CONFLICT"),
                    HttpStatus.CONFLICT);
        }

        menuService.createMenu(menu);

/*
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().port(8080).path(
                "{id}").buildAndExpand(menu.getId()).toUri();
*/
        return new ResponseEntity<Menu>(menu, HttpStatus.CREATED);


    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.PUT, value="/menus/{id}")
    public ResponseEntity<Menu> updateMenu(@RequestBody Menu updatedMenu, @PathVariable long id) {

        LOGGER.info("Updating menu with id {}", id);

        Preconditions.checkNotNull(updatedMenu);

        updatedMenu.setId(id);
        menuService.updateMenu(updatedMenu);

        return new ResponseEntity<Menu>(updatedMenu, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.PUT, value="/menus/photos/{id}")
    public ResponseEntity<Menu> updateMenuPhoto(@PathVariable("id") long menuId, @RequestParam("file") MultipartFile file) {

        LOGGER.info("Updating menu photo with id {}", menuId);

        Preconditions.checkNotNull(file);
        Menu currentMenu = menuService.getMenu(menuId);

        try {
            byte[] photo = file.getBytes();
            currentMenu = menuService.updateMenuPhoto(menuId, photo);

        } catch (IOException e) {
            return new ResponseEntity(new CustomExceptionResponse("Unable to update the menu photo with id " + menuId, "INTERNAL SERVER ERROR" ),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Menu>(currentMenu, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method=RequestMethod.DELETE, value="/menus/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable long id) {

        menuService.deleteMenu(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

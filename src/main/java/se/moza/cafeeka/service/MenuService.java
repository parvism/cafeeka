package se.moza.cafeeka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import se.moza.cafeeka.exception.CafeResourceNotFoundException;
import se.moza.cafeeka.model.Menu;
import se.moza.cafeeka.model.MenuType;
import se.moza.cafeeka.model.User;
import se.moza.cafeeka.repo.MenuRepository;
import se.moza.cafeeka.repo.UserRepository;

import static se.moza.cafeeka.config.Constants.URI_API_PREFIX;

@CacheConfig(cacheNames = "menus")
@Service
public class MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

    @Value("${server_Url}")
    private String server_Url;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    ServletContext servletContext;



    @Cacheable
    public MenuDTO getAllMenus(){
        LOGGER.info("Retrieving menus from database");

        List<Menu> menus = new ArrayList<>();
//		menuRepository.findAll().forEach(menus::add);
        menuRepository.findByExpired(false).forEach(menus::add);

        MenuDTO menuDTO = new MenuDTO();
        for(Menu menu: menus) {
            if(menu.getMenuType() == MenuType.LUNCH)
                menuDTO.addToLunchMenus(menu);
            else
                menuDTO.addToBreakfastMenus(menu);
        }

        return menuDTO;
    }

    @Cacheable
    public Menu getMenu(long id) {
        LOGGER.info("Retrieving menu from database");

        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if(optionalMenu.isEmpty()){
            throw new CafeResourceNotFoundException("Menu", "id", id);
        }

        return optionalMenu.get();
    }

    @Cacheable
    public byte[] getMenuPhoto(long id) {
        LOGGER.info("Retrieving menu photo from database");

        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if(optionalMenu.isEmpty()){
            throw new CafeResourceNotFoundException("Menu", "id", id);
        }

        if(optionalMenu.get().getPhoto() == null){
            throw new CafeResourceNotFoundException("Menu photo", "id", id);
        }
        return optionalMenu.get().getPhoto();
    }

    @CacheEvict(value="menus", allEntries=true)
    public void createMenu(Menu menu) {
        menuRepository.save(menu);
    }

    @CacheEvict(value="menus", allEntries=true)
    public void updateMenu(Menu menu) {

        Optional<Menu> optionalMenu = menuRepository.findById(menu.getId());
        if(optionalMenu.isEmpty()){
            throw new CafeResourceNotFoundException("Menu", "id", menu.getId());
        }

        Menu currentMenu = optionalMenu.get();

        currentMenu.setMenuType(menu.getMenuType());
        currentMenu.setName(menu.getName());
        currentMenu.setTitle(menu.getTitle());
        currentMenu.setDescription(menu.getDescription());
        currentMenu.setGlutenfree(menu.isGlutenfree());
        currentMenu.setVego(menu.isVego());
        currentMenu.setLowerPrice(menu.getLowerPrice());
        currentMenu.setHigherPrice(menu.getHigherPrice());

        menuRepository.save(currentMenu);
    }

    @CacheEvict(value="menus", allEntries=true)
    public Menu updateMenuPhoto(long menuId, byte[] photo) {

        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if(optionalMenu.isEmpty()){
            throw new CafeResourceNotFoundException("Menu", "id", menuId);
        }

        Menu menu = optionalMenu.get();

        menu.setPhoto(photo);
        menu.setPhotoUrl(server_Url + URI_API_PREFIX + "/menus/photos/" + menu.getId());
        menuRepository.save(menu);

        return menu;
    }

    @CacheEvict(value="menus", allEntries=true)
    public void deleteMenu(long id) {

        Optional<Menu> optionalMenu = menuRepository.findById(id);
        if(optionalMenu.isEmpty()){
            throw new CafeResourceNotFoundException("Menu", "id", id);
        }

        Menu menu = optionalMenu.get();
        menu.setExpired(true);
        menu.setPhoto(null);
        menuRepository.save(menu);

    }

    public boolean exists(Menu menu){
        if(menuRepository.findByName(menu.getName()) != null)
            return true;
        else
            return false;
    }
}

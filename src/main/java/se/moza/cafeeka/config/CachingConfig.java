package se.moza.cafeeka.config;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.moza.cafeeka.service.DrinkService;
import se.moza.cafeeka.service.MenuService;


@Configuration
@EnableCaching
public class CachingConfig {


    public MenuService menuService() {
        return new MenuService();
    }

    public DrinkService drinkService() {
        return new DrinkService();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("menus"),
                new ConcurrentMapCache("drinks")));
        return cacheManager;
    }
}

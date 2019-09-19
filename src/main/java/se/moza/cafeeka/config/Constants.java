package se.moza.cafeeka.config;

import org.springframework.stereotype.Component;

@Component
public class Constants {

    //private static final Logger LOGGER = LoggerFactory.getLogger(Constants.class);

    public static final String URI_API_PREFIX = "/api";

    public static final String URI_AUTH = URI_API_PREFIX + "/auth";
    public static final String URI_USERS = URI_API_PREFIX + "/users";
/*
    public static final String ERROR_UPDATE_PROFILE = "Error in updating the current user";
    public static final String ERROR_UPDATE_EMAIL = "E-mail is already being used";
*/

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "30";

    public static final int MAX_PAGE_SIZE = 50;


    private Constants() {}
}

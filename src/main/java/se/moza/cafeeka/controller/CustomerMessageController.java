package se.moza.cafeeka.controller;

import static se.moza.cafeeka.config.Constants.URI_API_PREFIX;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.moza.cafeeka.model.CustomerMessage;
import se.moza.cafeeka.exception.CustomExceptionResponse;

import java.io.File;


@RestController
@RequestMapping(value = URI_API_PREFIX)
public class CustomerMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMessageController.class);

    @Autowired
    private JavaMailSender sender;


    @RequestMapping(method=RequestMethod.POST, value="/contacts")
    public ResponseEntity<Void> createCustomerMessage(@RequestBody CustomerMessage customerMessage) {

        LOGGER.info("creating new customerMessage: {}", customerMessage.toString());

        MimeMessage message = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //helper.setFrom("Cafeeka website");
            helper.setTo("yourEmail@gmail.com");              // "cafeekamail@gmail.com"
            helper.setSubject(customerMessage.getSubject());
            helper.setText(customerMessage.toString());
            /*
            FileSystemResource file
                    = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);
            */

            sender.send(message);
        }catch(Exception ex) {
            return new ResponseEntity(new CustomExceptionResponse("Error in sending email: ", "INTERNAL SERVER ERROR"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

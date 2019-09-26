package se.moza.cafeeka.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Service;
import se.moza.cafeeka.model.Drink;
import se.moza.cafeeka.model.Menu;
import se.moza.cafeeka.model.PaymentMethod;
import se.moza.cafeeka.model.PaymentStatus;

@Service
public class OrderDTO {

    // for put request
    private String customerOrderId;

    private String customerName;
    private String customerMobilePhone;
    @Email
    private String customerEmail;
    private Date created;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private boolean menuWithLowerPrice;

    private List<Menu> menus = new ArrayList<>();
    private List<Drink> drinks = new ArrayList<>();

    private String chargeId;
    private String balanceTransactionId;
    private float totalPrice;
    private String extraInfo;


    public OrderDTO() { }

    public OrderDTO(String customerName, String customerMobilePhone, String customerEmail, PaymentMethod paymentmethod,
                    boolean menuWithLowerPrice, List<Menu> menus, List<Drink> drinks, String extraInfo) {

        super();
        this.customerName = customerName;
        this.customerMobilePhone = customerMobilePhone;
        this.customerEmail = customerEmail;
        this.paymentMethod = paymentmethod;
        this.menuWithLowerPrice = menuWithLowerPrice;
        this.menus = menus;
        this.drinks = drinks;
        this.extraInfo = extraInfo;

        this.chargeId = "";
        this.balanceTransactionId = "";
        this.totalPrice = 0;
    }

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobilePhone() {
        return customerMobilePhone;
    }

    public void setCustomerMobilePhone(String customerMobilePhone) {
        this.customerMobilePhone = customerMobilePhone;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isMenuWithLowerPrice() {
        return menuWithLowerPrice;
    }

    public void setMenuWithLowerPrice(boolean menuWithLowerPrice) {
        this.menuWithLowerPrice = menuWithLowerPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Menu> getMenus() {
        List<Menu> unmodifiable = Collections.unmodifiableList(this.menus);
        return unmodifiable;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Drink> getDrinks() {
        List<Drink> unmodifiable = Collections.unmodifiableList(this.drinks);
        return unmodifiable;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }


    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void addToMenus(Menu menu) {
        menus.add(menu);
    }

    public void addToDrinks(Drink drink) {
        drinks.add(drink);
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getBalanceTransactionId() {
        return balanceTransactionId;
    }

    public void setBalanceTransactionId(String balanceTransactionId) {
        this.balanceTransactionId = balanceTransactionId;
    }

    @Override
    public String toString() {
        return "OrderDTO [customerOrderId=" + customerOrderId + ", customerName=" + customerName
                + ", customerMobileNum=" + customerMobilePhone + ", customerEmail=" + customerEmail + ", created="
                + created + ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus
                + ", menuWithLowerPrice=" + menuWithLowerPrice + ", menus=" + menus + ", drinks=" + drinks
                + ", chargeId=" + chargeId + ", balanceTransactionId=" + balanceTransactionId + ", totalPrice="
                + totalPrice + ", extraInfo=" + extraInfo + "]";
    }
}

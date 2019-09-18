package se.moza.cafeeka.model;

import se.moza.cafeeka.model.audit.DateAudit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

@Entity
public class CustomerOrder extends DateAudit {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String customerName;
    private String customerMobilePhone;
    @Email
    private String CustomerEmail;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private boolean menuWithLowerPrice;
    private float totalPrice;
    private String extraInfo;
    private boolean ready;
    private boolean pickedup;
    private String chargeId;
    private String balanceTransactionId;

    @OneToMany(mappedBy="customerOrder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<MenuOrder> menuOrders = new ArrayList<>();

    @OneToMany(mappedBy="customerOrder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<DrinkOrder> drinkOrders = new ArrayList<>();


    public CustomerOrder() { }

    public CustomerOrder(String customerName, String customerMobilePhone, @Email String customerEmail, PaymentMethod paymentMethod, boolean menuWithLowerPrice, String extraInfo) {
        this.customerName = customerName;
        this.customerMobilePhone = customerMobilePhone;
        CustomerEmail = customerEmail;
        this.paymentMethod = paymentMethod;
        this.menuWithLowerPrice = menuWithLowerPrice;
        this.extraInfo = extraInfo;
        this.paymentStatus = PaymentStatus.UNPAID;
        this.totalPrice = 0;
        this.ready = false;
        this.pickedup = false;
        this.balanceTransactionId = "";
        this.chargeId = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isready() {
        return ready;
    }


    public void setReady(boolean ready) {
        this.ready = ready;
    }


    public boolean isPickedup() {
        return pickedup;
    }


    public void setPickedup(boolean pickedup) {
        this.pickedup = pickedup;
    }


    public List<MenuOrder> getMenuOrders() {
        return menuOrders;
    }


    public void setMenuOrders(List<MenuOrder> menuOrders) {
        this.menuOrders = menuOrders;
    }


    public List<DrinkOrder> getDrinkOrders() {
        return drinkOrders;
    }


    public void setDrinkOrders(List<DrinkOrder> drinkOrders) {
        this.drinkOrders = drinkOrders;
    }

    public void addMenuOrder(MenuOrder menuOrder) {
        menuOrders.add(menuOrder);
        menuOrder.setCustomerOrder(this);
    }

    public void addDrinkOrder(DrinkOrder drinkOrder) {
        drinkOrders.add(drinkOrder);
        drinkOrder.setCustomerOrder(this);
    }

    public void clearMenuOrders() {
        menuOrders.clear();
    }

    public void clearDrinkOrders() {
        drinkOrders.clear();
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
        return "CustomerOrder{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerMobilePhone='" + customerMobilePhone + '\'' +
                ", CustomerEmail='" + CustomerEmail + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", paymentStatus=" + paymentStatus +
                ", menuWithLowerPrice=" + menuWithLowerPrice +
                ", totalPrice=" + totalPrice +
                ", extraInfo='" + extraInfo + '\'' +
                ", ready=" + ready +
                ", pickedup=" + pickedup +
                ", chargeId='" + chargeId + '\'' +
                ", balanceTransactionId='" + balanceTransactionId + '\'' +
                ", menuOrders=" + menuOrders +
                ", drinkOrders=" + drinkOrders +
                '}';
    }
}

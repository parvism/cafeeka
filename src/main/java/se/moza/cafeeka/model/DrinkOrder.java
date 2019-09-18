package se.moza.cafeeka.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class DrinkOrder {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="drink_FK")
    private Drink drink;

    @ManyToOne
    @JoinColumn(name="customer_order_FK")
    private CustomerOrder customerOrder;


    public DrinkOrder() { }


    public DrinkOrder(CustomerOrder customerOrder, Drink drink) {
        super();
        this.customerOrder = customerOrder;
        this.drink = drink;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public Drink getDrink() {
        return drink;
    }


    public void setDrink(Drink drink) {
        this.drink = drink;
    }


    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }


    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }


    @Override
    public String toString() {
        return "DrinkOrder [id=" + id + ", drink=" + drink + ", customerOrder=" + customerOrder + "]";
    }

}

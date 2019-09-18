package se.moza.cafeeka.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class MenuOrder {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="menu_FK")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name="customer_order_FK")
    private CustomerOrder customerOrder;


    public MenuOrder() { }


    public MenuOrder(CustomerOrder customerOrder, Menu menu) {
        super();
        this.customerOrder = customerOrder;
        this.menu = menu;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public Menu getMenu() {
        return menu;
    }


    public void setMenu(Menu menu) {
        this.menu = menu;
    }


    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }


    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }


    @Override
    public String toString() {
        return "MenuOrder [id=" + id + ", menu=" + menu + ", customerOrder=" + customerOrder + "]";
    }
}

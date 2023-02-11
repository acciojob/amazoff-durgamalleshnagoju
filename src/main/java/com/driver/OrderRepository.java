package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderDb = new HashMap<>();
    private HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();

    private HashMap<String, List<String>> deliveryPartnerOderDb = new HashMap<>();

    private HashMap<Order, DeliveryPartner> orderPartnerDb = new HashMap<>();

    OrderRepository(){

    }

    public Order getOrder(String id) {
        return orderDb.get(id);
    }

    public void setOrder(Order order) {
        orderDb.put(order.getId(), order);
    }

    public DeliveryPartner getDeliveryPartnerDb(String id) {
        return deliveryPartnerDb.get(id);

    }

    public void setDeliveryPartnerDb(DeliveryPartner deliveryPartner) {
        deliveryPartnerDb.put(deliveryPartner.getId(), deliveryPartner);
    }



    public List<String> getDeliveryPartnerOrder(String id) {
        if(!deliveryPartnerOderDb.containsKey(id)){
            return new ArrayList<>();
        }
        return deliveryPartnerOderDb.get(id);
    }

    public void setDeliveryPartnerOderDb(String OrderId, String PartnerID) {
        List<String> newList = null;
        if(!deliveryPartnerOderDb.containsKey(PartnerID)){
            newList = new ArrayList<>();
        } else{
            newList = deliveryPartnerOderDb.get(PartnerID);
        }

        newList.add(OrderId);
        deliveryPartnerOderDb.put(PartnerID,newList);
    }

    public void setOrderPartnerDb(String OrderId, String partnerId){

        Order order = orderDb.get(OrderId);
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
        orderPartnerDb.put(order, deliveryPartner);
        setDeliveryPartnerOderDb(OrderId,partnerId);
    }

    public HashMap<String, Order> getOrderDb() {
        return orderDb;
    }
    public void deleteOrder(String id){
        orderDb.remove(id);
    }
    public void deletePartner(String partnerId){
        if(deliveryPartnerOderDb.containsKey(partnerId)){
            List<String> orders = deliveryPartnerOderDb.get(partnerId);
            for(String order: orders){
                if(orderPartnerDb.containsKey(orderDb.get(order)))
                    orderPartnerDb.remove(orderDb.get(order));
            }
        }

        deliveryPartnerOderDb.remove(partnerId);
        deliveryPartnerDb.remove(partnerId);
    }

    public HashMap<String, List<String>> getDeliveryPartnerOderDb() {
        return deliveryPartnerOderDb;
    }
    public boolean isPartnerAvailable(String partnerId){
        return deliveryPartnerDb.containsKey(partnerId);
    }
}

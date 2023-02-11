package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order){

        orderRepository.setOrder(order);
    }


    public void addPartner(String partnerId){
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        orderRepository.setDeliveryPartnerDb(partner);

    }


    public void addOrderPartnerPair(String orderId, String partnerId){

        orderRepository.setOrderPartnerDb(orderId, partnerId);
    }

    public Order getOrderById(String orderId){

        return orderRepository.getOrder(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){

        //deliveryPartner should contain the value given by partnerId

        return orderRepository.getDeliveryPartnerDb(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){

        //orderCount should denote the orders given by a partner-id
        List<String> lst = orderRepository.getDeliveryPartnerOrder(partnerId);
        if(lst.size()>0){
            return lst.size();
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> orders = orderRepository.getDeliveryPartnerOrder(partnerId);

        //orders should contain a list of orders by PartnerId

        return orders;
    }

    public List<String> getAllOrders(){
        List<String> orders = new ArrayList<>();
        //Get all orders
        for(String order: orderRepository.getOrderDb().keySet()){
            orders.add(order);
        }
        return orders;
    }

    public void deleteOrderById(String orderId){

        //Delete an order and also
        // remove it from the assigned order of that partnerId
        orderRepository.deleteOrder(orderId);
    }

    public Integer getCountOfUnassignedOrders(){
        Integer countOfOrders = 0;

        //Count of orders that have not been assigned to any DeliveryPartner

        List<String> UnassignedOrders = new ArrayList<>();
        for(String orderIds : orderRepository.getOrderDb().keySet()){
            UnassignedOrders.add(orderIds);
        }
        for(List<String> orders : orderRepository.getDeliveryPartnerOderDb().values()){
            for (String order : orders){
                UnassignedOrders.remove(order);
            }
        }
        return UnassignedOrders.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){

        if(!orderRepository.isPartnerAvailable(partnerId)){
            return 0;
        }
        int countOfOrders = 0;
        String []arr = time.split(":");
        int Time = (Integer.parseInt(arr[0]))*60+Integer.parseInt(arr[1]);

        //countOfOrders that are left after a particular time of a DeliveryPartner

        List<String> orders = orderRepository.getDeliveryPartnerOrder(partnerId);

        for(String order : orders){
            if(getOrderById(order).getDeliveryTime() >Time){
                countOfOrders++;
            }
        }

        return countOfOrders;
    }


    public void deletePartnerById(String partnerId){

        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.

        orderRepository.deletePartner(partnerId);

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        if(!orderRepository.isPartnerAvailable(partnerId)){
            return "00:00";
        }
        String time = null;
        List<String> orders = orderRepository.getDeliveryPartnerOrder(partnerId);
        if(orders.size()==0){
            return "00:00";
        }
        int Time = Integer.MIN_VALUE;
        for(String order:orders){
            int currentTime = orderRepository.getOrder(order).getDeliveryTime();
            Time = Math.max(Time, currentTime);
        }
        //Return the time when that partnerId will deliver his last delivery order.

        int min = Time%60;
        Time = (Time-min)/60;
        time = Time+":"+min;

        return time;
    }
}

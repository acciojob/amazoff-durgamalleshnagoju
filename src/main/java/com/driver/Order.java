package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order() {
    }

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        String []arr = deliveryTime.split(":");
        int Time = (Integer.parseInt(arr[0]))*60+Integer.parseInt(arr[1]);
        this.deliveryTime = Time;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}

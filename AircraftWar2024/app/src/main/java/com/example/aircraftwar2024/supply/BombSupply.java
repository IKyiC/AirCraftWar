package com.example.aircraftwar2024.supply;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.example.aircraftwar2024.basic.Observer;

/**
 * 炸弹道具，自动触发
 * <p>
 * 使用效果：清除界面上除BOSS机外的所有敌机（包括子弹）
 * <p>
 * 【观察者模式】
 *
 * @author hitsz
 */
public class BombSupply extends AbstractFlyingSupply {

    private List<Observer> observerLists = new ArrayList<>();
    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    //增加观察者
    public void addObserver(Observer observer) {
        observerLists.add(observer);
    }

    public void notifyAllFlying() {
        for(Observer observer: observerLists) {
            observer.update();
        }
    }

    @Override
    public void activate() {
        notifyAllFlying();
        System.out.println("BombSupply active");
    }

}

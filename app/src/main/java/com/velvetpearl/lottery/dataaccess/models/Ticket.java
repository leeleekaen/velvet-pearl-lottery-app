package com.velvetPearl.lottery.dataAccess.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;


/**
 * Created by Andreas "denDAY" Stensig on 20-Sep-16.
 */
public class Ticket {
    // Entity member fields
    private Object id;
    private String owner;
    private TreeMap<Object, LotteryNumber> lotteryNumbers;

    // Navigational member fields
    private Object lotteryId;

    public Ticket() {
        lotteryNumbers = new TreeMap<>();
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public final ArrayList<LotteryNumber> getLotteryNumbers() {
        ArrayList<LotteryNumber> list = new ArrayList<>(lotteryNumbers.size());
        for (Object key : lotteryNumbers.keySet()) {
            list.add(lotteryNumbers.get(key));
        }
        return list;
    }

    public void removeLotteryNumber(LotteryNumber number){
        if (number == null) {
            return;
        }

        if (number.getId() == null) {
            throw new IllegalArgumentException("Lottery number does not have an ID.");
        }

        lotteryNumbers.remove(number.getId());
    }

    public void addLotteryNumber(LotteryNumber number) {
        if (number == null) {
            return;
        }

        if (number.getId() == null) {
            throw new IllegalArgumentException("Lottery number does not have an ID.");
        }

        Prize oldWinningPrize = null;
        if (lotteryNumbers.containsKey(number.getId())) {
            oldWinningPrize = lotteryNumbers.get(number.getId()).getWinningPrize();
        }

        lotteryNumbers.put(number.getId(), number);
        lotteryNumbers.get(number.getId()).setWinningPrize(oldWinningPrize);
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(Object lotteryId) {
        this.lotteryId = lotteryId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Ticket other = (Ticket) obj;
        if (other.id == null) {
            return false;
        }

        return id.equals(other.id);
    }
}

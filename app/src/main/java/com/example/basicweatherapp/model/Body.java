package com.example.basicweatherapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List; // List 타입을 사용하기 위해 import

public class Body {
    @SerializedName("items")
    private List<Item> items;
    @SerializedName("numOfRows")
    private int numOfRows;
    @SerializedName("pageNo")
    private int pageNo;
    @SerializedName("totalCount")
    private int totalCount;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
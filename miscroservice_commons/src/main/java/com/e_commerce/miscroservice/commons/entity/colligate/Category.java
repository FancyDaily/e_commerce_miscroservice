package com.e_commerce.miscroservice.commons.entity.colligate;

import lombok.Data;

import java.io.Serializable;

public class Category implements Serializable {
    String id;
    String sortName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
}

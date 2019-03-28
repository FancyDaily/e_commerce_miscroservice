package com.e_commerce.miscroservice.commons.entity.colligate;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Category implements Serializable {
    String id;
    String sortName;
}

package com.e_commerce.miscroservice.user.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LevelView {

    Long growthValue;   //当前经验(成长值)

    Integer levelNum;   //等级

    String levelName;  //等级对应名

    Long upToLevelUp;   //距下一次还差多少经验

}

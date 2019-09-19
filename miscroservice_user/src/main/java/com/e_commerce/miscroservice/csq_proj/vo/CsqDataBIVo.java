package com.e_commerce.miscroservice.csq_proj.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-09-17 13:47
 */
@Data
@Builder
@NoArgsConstructor
public class CsqDataBIVo {

	Long id;

	Long entityId;

	Integer entityType;

	String name;

	Map<String, List<CsqLineDiagramData>> diagramMap;

}

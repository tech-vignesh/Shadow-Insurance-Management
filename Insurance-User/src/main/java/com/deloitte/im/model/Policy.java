package com.deloitte.im.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "User definition")
@Document(collection = "policies")
public class Policy {
	
	@Id
	private String id;
	
	@ApiModelProperty(hidden=true)
	private String policyNumber;
	@ApiModelProperty(hidden=true)
	private String policyType;
	@ApiModelProperty(hidden=true)
	private BigDecimal premiumAmount;
	@ApiModelProperty(hidden=true)
	private Date startDate;
	@ApiModelProperty(hidden=true)
	private Date endDate;
	
	
	
	
	

}
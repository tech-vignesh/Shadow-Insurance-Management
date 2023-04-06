package com.deloitte.im.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Document(collection = "policies")
public class Policy {
	
	@Id
	private String id;
	private String policyNumber;
	private String policyType;
	private BigDecimal premiumAmount;
	private Date startDate;
	private Date endDate;
	
	

}
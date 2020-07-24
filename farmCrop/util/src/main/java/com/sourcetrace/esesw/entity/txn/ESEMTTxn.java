package com.sourcetrace.esesw.entity.txn;

public class ESEMTTxn {
	private Long id;
	private String txnId;
	private String accountNo;
	private Integer creditType;
	private String desc;
	private Double txnAmount;
	
	//Transient Variable
	private String creditTypeName;

	public ESEMTTxn(){
		
	}
	
	public ESEMTTxn(String txnId,String accountNo,Integer creditType,String desc,Double txnAmount){
		this.txnId = txnId;
		this.accountNo=accountNo;
		this.creditType=creditType;
		this.desc=desc;
		this.txnAmount=txnAmount;
	}
	
	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getCreditType() {
		return creditType;
	}

	public void setCreditType(Integer creditType) {
		this.creditType = creditType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Double getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(Double txnAmount) {
		this.txnAmount = txnAmount;
	}

	public void setCreditTypeName(String creditTypeName) {
		this.creditTypeName = creditTypeName;
	}

	public String getCreditTypeName() {
		if(creditType == 1){
			setCreditTypeName("Deposit");
		}
		else{
			setCreditTypeName("Withdrawal");
		}
		return creditTypeName;
	}
}

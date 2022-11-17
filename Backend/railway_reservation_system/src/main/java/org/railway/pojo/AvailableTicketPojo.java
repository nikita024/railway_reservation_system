package org.railway.pojo;

import java.util.Date;

public class AvailableTicketPojo {
	Train fkTrainId;
	Date date;
	Integer availableSeat3A;
	Integer availableSeatSL;
	public Train getFkTrainId() {
		return fkTrainId;
	}
	public void setFkTrainId(Train fkTrainId) {
		this.fkTrainId = fkTrainId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAvailableSeat3A() {
		return availableSeat3A;
	}
	public void setAvailableSeat3A(Integer availableSeat3A) {
		this.availableSeat3A = availableSeat3A;
	}
	public Integer getAvailableSeatSL() {
		return availableSeatSL;
	}
	public void setAvailableSeatSL(Integer availableSeatSL) {
		this.availableSeatSL = availableSeatSL;
	}
	
}

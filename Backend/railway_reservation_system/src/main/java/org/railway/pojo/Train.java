package org.railway.pojo;

public class Train {
	Integer trainId;
	String trainName;
	Station fkSourceStationId;
	Station fkDstinationStationId;
	public Integer getTrainId() {
		return trainId;
	}
	public void setTrainId(Integer trainId) {
		this.trainId = trainId;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public Station getFkSourceStationId() {
		return fkSourceStationId;
	}
	public void setFkSourceStationId(Station fkSourceStationId) {
		this.fkSourceStationId = fkSourceStationId;
	}
	public Station getFkDstinationStationId() {
		return fkDstinationStationId;
	}
	public void setFkDstinationStationId(Station fkDstinationStationId) {
		this.fkDstinationStationId = fkDstinationStationId;
	}
	
}

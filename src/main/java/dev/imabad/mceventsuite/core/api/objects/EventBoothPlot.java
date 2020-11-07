package dev.imabad.mceventsuite.core.api.objects;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "booth_plots")
public class EventBoothPlot {

    private String id;
    private String boothType;
    private String posOne;
    private String posTwo;
    private String frontPos;
    private String status = "empty";
    private EventBooth booth;

    protected EventBoothPlot(){}

    public EventBoothPlot(String boothType, String posOne, String posTwo){
        this.id = UUID.randomUUID().toString();
        this.boothType = boothType;
        this.posOne = posOne;
        this.posTwo = posTwo;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    public String getBoothType() {
        return boothType;
    }

    public void setBoothType(String boothType) {
        this.boothType = boothType;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public EventBooth getBooth() {
        return booth;
    }

    public void setBooth(EventBooth booth) {
        this.booth = booth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosOne() {
        return posOne;
    }

    public void setPosOne(String posOne) {
        this.posOne = posOne;
    }

    public String getPosTwo() {
        return posTwo;
    }

    public void setPosTwo(String posTwo) {
        this.posTwo = posTwo;
    }

    public String getFrontPos() {
        return frontPos;
    }

    public void setFrontPos(String frontPos) {
        this.frontPos = frontPos;
    }

    public int plotSize(){
        switch(boothType){
            case "small":
                return 17;
            case "medium":
                return 37;
            case "large":
                return 53;
        }
        return 0;
    }

    public boolean blocksIsInBooth(int x, int z){
        String[] splits = getPosOne().split(",");
        int xStart = Integer.parseInt(splits[0]);
        int zStart = Integer.parseInt(splits[2]);
        int xEnd = xStart + plotSize();
        int zEnd = zStart + plotSize();
        return x >= xStart && x <= xEnd && z >= zStart && z <= zEnd;
    }

    public boolean blockInBooth(int x, int z){
        String[] splits = getPosOne().split(",");
        int xStart = Integer.parseInt(splits[0]);
        int zStart = Integer.parseInt(splits[2]);
        int xEnd = xStart + plotSize();
        int zEnd = zStart + plotSize();
        if(x == xStart || x == xEnd){
            if(z >= zStart && z <= zEnd){
                return true;
            }
        }
        if(z == zStart || z == zEnd){
            if(x >= xStart && x <= xEnd){
                return true;
            }
        }
        return false;
    }
}

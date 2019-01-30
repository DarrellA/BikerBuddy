package net.aucutt.bikerbuddy.network;

public class Result {

    private Data results;
    private String status;

    public Data getResults() {
        return results;
    }

    public void setResults(Data data) {
        this.results = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

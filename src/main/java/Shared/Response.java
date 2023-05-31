package Shared;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private String status;
    private String message;

    private List<GameResponse> gameResponseList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GameResponse> getGameResponseList() {
        return gameResponseList;
    }

    public void setGameResponseList(List<GameResponse> gameResponseList) {
        this.gameResponseList = gameResponseList;
    }
}

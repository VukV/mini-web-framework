package framework.http.response;

public class MessageObject {

    private String message;

    public MessageObject() {
        message = "";
    }

    public MessageObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package part1.common.Message;

import lombok.AllArgsConstructor;

/*
    @author 张星宇
 */
@AllArgsConstructor
public enum MessageType {
    REQUEST(0),RESPONSE(1);
    private int code;
    public int getCode(){return code;}
}

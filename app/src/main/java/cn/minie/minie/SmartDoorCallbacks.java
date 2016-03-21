package cn.minie.minie;

/**
 * Created by cch on 16-3-11.
 */
public interface SmartDoorCallbacks {
    /**
     * Called when door connection is initilized
     * @param code
     */
    void onInit(int code);

    /**
     * Called when door connection is closed
     */
    void onClose();

    /**
     * Called when card is punched.
     * @param card Card Identifier
     * @param err 0 when card is valid, or error
     */
    void onPunch(String card, int err);

    /**
     * When user input a code/password via keyboard
     * @param code
     * @return whether the door should open or not
     */
    boolean onCode(int code);

    /**
     * When an unknown card is punched.
     * @param card
     * @return whether the door should open or not
     */
    boolean checkCard(String card);
}

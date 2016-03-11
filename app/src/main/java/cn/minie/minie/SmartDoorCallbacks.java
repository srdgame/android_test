package cn.minie.minie;

/**
 * Created by cch on 16-3-11.
 */
public interface SmartDoorCallbacks {
    void onInit(int code);
    void onClose();
    void onPunch(String card);
    boolean onCode(String code);
    boolean checkCard(String card);
}

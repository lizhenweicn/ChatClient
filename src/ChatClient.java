import java.awt.*;

/**
 * @author : zhenweiLi
 * @date :2019-04-06 13:38
 * DESC : 聊天客户端主界面
 */
public class ChatClient extends Frame {

    public static void main(String[] args) {

        //  显示主界面
        new ChatClient().launchFrame();

    }

    /**
     * 初始化主界面
     */
    private void launchFrame() {
        setLocation(400, 300);
        this.setSize(300, 300);
        setVisible(true);
    }

}

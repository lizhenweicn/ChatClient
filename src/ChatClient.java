import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import static sun.jvm.hotspot.runtime.PerfMemory.start;

/**
 * @author : zhenweiLi
 * @date :2019-04-06 13:38
 * DESC : 聊天客户端主界面
 */
public class ChatClient extends Frame {

    /**
     * 聊天内容框
     */
    private TextArea mTaContent = new TextArea();

    /**
     * 输入文本框
     */
    private TextField mTfInput = new TextField();

    /**
     * 前后端通讯的 Socket
     */
    private Socket mSocket;

    /**
     * 网络数据输出流
     */
    private DataInputStream mDis;

    /**
     * 网络数据输出流
     */
    private DataOutputStream mDos;

    /**
     * 是否已经连接
     */
    private boolean mConnected;

    /**
     * 接收消息的线程
     */
    private Thread mReceiveThread = new Thread(new ReceiveThread());

    public static void main(String[] args) {
        //  显示主界面
        new ChatClient().launchFrame();
    }

    /**
     * 初始化主界面
     */
    private void launchFrame() {
        //  设置主界面
        setLocation(400, 300);
        this.setSize(300, 300);
        //  添加子控件
        add(mTaContent, BorderLayout.NORTH);
        add(mTfInput, BorderLayout.SOUTH);
        pack();
        //  关闭按钮监听
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });
        //  增加输入控件回车监听
        mTfInput.addActionListener(new OnInputViewEnter());
        //  显示窗口
        setVisible(true);
        //  自动连接
        connect();
    }

    /**
     * 连接服务器端
     */
    private void connect() {

        try {
            mSocket = new Socket("127.0.0.1", 8888);
            System.out.println("connected");
            mDis = new DataInputStream(mSocket.getInputStream());
            mDos = new DataOutputStream(mSocket.getOutputStream());
            mConnected = true;
        } catch (ConnectException e) {
            System.out.println("服务器未启动");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mReceiveThread.start();

    }

    /**
     * 断开客户端与服务器的连接
     */
    private void disconnect() {

        /// 因为线程中有阻塞方法，所以可能出现卡死的现象
//        try {
//            mConnected = false;
//            mReceiveThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                mDis.close();
//                mDos.close();
//                mSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            mConnected = false;
            mDis.close();
            mDos.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 当输入框回车时触发该监听
     */
    private class OnInputViewEnter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String inputContent = mTfInput.getText().trim();
            mTaContent.append("自己 ：" + "\t" + inputContent + "\r\n");
            mTfInput.setText("");
            try {
                mDos.writeUTF(inputContent);
                mDos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceiveThread implements Runnable {

        @Override
        public void run() {
            try {
                while (mConnected) {
                    String readUTF = mDis.readUTF();
                    mTaContent.append("对方 ：" + "\t" + readUTF + "\r\n");
                }
            } catch (EOFException e) {
                System.out.println("服务器失联了");
            } catch (SocketException e) {
                System.out.println("Bye ~");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

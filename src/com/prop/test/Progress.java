package com.prop.test;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
public class Progress {
    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<Progress> webSocket = new CopyOnWriteArraySet<>();
    private Session session;
    private String username;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocket.add(this);
        synchronized (Progress.class) {
            onlineCount++;
        }
    }

    @OnClose
    public void onClose() {
        webSocket.remove(this);
        synchronized (Progress.class) {
            onlineCount--;
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(message);
        for (Progress item : webSocket) {
            try {
                item.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
}

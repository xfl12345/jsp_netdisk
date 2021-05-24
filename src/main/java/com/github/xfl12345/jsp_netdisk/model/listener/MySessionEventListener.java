package com.github.xfl12345.jsp_netdisk.model.listener;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;
import com.github.xfl12345.jsp_netdisk.model.service.AccountService;
import com.github.xfl12345.jsp_netdisk.model.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class MySessionEventListener implements HttpSessionListener {

    private final Logger logger = LoggerFactory.getLogger(MySessionEventListener.class);

    /**
     * 会话缓存Map，支持直接通过session ID来获取session对象
     */
    private static final ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    // 当用户与服务器之间开始session时触发该方法
    public void sessionCreated(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();
        sessionMap.put(session.getId(), session);
    }

    // 当用户与服务器之间session断开时触发该方法
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();
//        ServletContext application = session.getServletContext();
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);

        if(tbAccount != null){
            AccountService accountService = StaticSpringApp.getBean(AccountService.class);
            Long accountId = tbAccount.getAccountId();
            accountService.logoutByAccountId(accountId);
            logger.info("accountId="+accountId+",session expire.Auto log out successfully.");
//            FileService fileService = StaticSpringApp.getBean(FileService.class);
            //删除所有下载链接
            FileService.downloadLinkMap.remove(accountId.toString());
        }
        sessionMap.remove(session.getId());
        session.invalidate();
    }

    public HttpSession getSessionById(String sessionId){
        return sessionMap.get(sessionId);
    }
}

package com.github.xfl12345.jsp_netdisk.model.listener;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.service.TbAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class MySessionEventListener implements HttpSessionListener {

    private final Logger logger = LoggerFactory.getLogger(MySessionEventListener.class);

    // 当用户与服务器之间开始session时触发该方法
    public void sessionCreated(HttpSessionEvent se)
    {
//        logger.info("diy listener:sessionCreated");
        //TO DO SOMETHING
    }

    // 当用户与服务器之间session断开时触发该方法
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();
//        ServletContext application = session.getServletContext();
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);

        if(tbAccount != null){
            TbAccountService tbAccountService = StaticSpringApp.getBean(TbAccountService.class);
            Long accountId = tbAccount.getAccountId();
            tbAccountService.logoutByAccountId(accountId);
            logger.info("accountId="+accountId+",session expire.Auto log out successfully.");
        }
    }
}

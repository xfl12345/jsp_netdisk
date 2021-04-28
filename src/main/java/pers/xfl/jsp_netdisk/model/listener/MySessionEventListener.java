package pers.xfl.jsp_netdisk.model.listener;

import pers.xfl.jsp_netdisk.StaticSpringApp;
import pers.xfl.jsp_netdisk.model.service.TbAccountService;
import pers.xfl.jsp_netdisk.model.utils.MyStrIsOK;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionEventListener implements HttpSessionListener {


    // 当用户与服务器之间开始session时触发该方法
    public void sessionCreated(HttpSessionEvent se)
    {
//        System.out.println("diy listener:sessionCreated");
        //TO DO SOMETHING
    }

    // 当用户与服务器之间session断开时触发该方法
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSession session = se.getSession();
        ServletContext application = session.getServletContext();
        String accountIdStr = (String) session.getAttribute("accountId");
        if(MyStrIsOK.isNotEmpty(accountIdStr)){
            long accountId = Long.parseLong(accountIdStr);
            TbAccountService tbAccountService = StaticSpringApp.getBean("tbAccountService",TbAccountService.class);
            tbAccountService.logoutByAccountId(accountId);
//            System.out.println("diy listener:accountId="+accountId+",session expire.Auto log out successful.");
        }
    }
}

package pers.xfl.jsp_netdisk.model.dao;

import org.apache.ibatis.annotations.Param;
import pers.xfl.jsp_netdisk.model.pojo.database.AccountSession;

import java.util.List;

/**
 * (TbAccount)表数据库访问层
 *
 * @author xfl12345
 * @since 2021-04-19 16:13:16
 */
public interface AccountSessionDao {

    /**
     * 通过账号ID查询单条数据
     *
     * @param accountId 账号ID
     * @return 实例对象
     */
    AccountSession queryByAccountId(Long accountId);



    /**
     * 通过会话ID查询单条数据
     *
     * @param sessionId 会话ID
     * @return 实例对象
     */
    AccountSession queryBySessionId(String sessionId);

    /**
     * 通过账号ID和会话ID查询单条数据
     * @param accountSession 包含 账号ID 和 会话ID 的AccountSession对象
     * @return 实例对象
     */
    AccountSession queryByAccountIdAndSessionId(AccountSession accountSession);

    /**
     * 查询指定行会话记录，查看当前在线账号
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AccountSession> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);



    /**
     * 登录完成后，新增会话记录
     *
     * @param accountSession 实例对象
     * @return 影响行数
     */
    int insert(AccountSession accountSession);

    /**
     * 更新会话记录，一般发生于同一个账号被多次同时登录
     *
     * @param accountSession 实例对象
     * @return 影响行数
     */
    int update(AccountSession accountSession);

    /**
     * 通过账号ID删除会话记录
     *
     * @param accountId 账号ID
     * @return 影响行数
     */
    int deleteByAccountId(Long accountId);

    /**
     * 通过会话ID删除会话记录
     *
     * @param sessionId 会话ID
     * @return 影响行数
     */
    int deleteBySessionId(String sessionId);

}


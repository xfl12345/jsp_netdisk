package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.AccountFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.EmailVerification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (EmailVerification)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-03 17:53:58
 */
public interface EmailVerificationDao {

    /**
     * 统计
     *
     * @return long型的总数
     */
    long countAll();

    /**
     * 通过email查询数据
     *
     * @param email 电子邮箱
     * @return 实例对象
     */
    EmailVerification queryByEmail(@Param("email")String email);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<EmailVerification> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 新增数据
     *
     * @param emailVerification 实例对象
     * @return 影响行数
     */
    int insert(EmailVerification emailVerification);

    /**
     * 修改数据
     *
     * @param emailVerification 实例对象
     * @return 影响行数
     */
    int update(EmailVerification emailVerification);

    /**
     * 通过email删除数据
     *
     * @param email 电子邮箱
     * @return 影响行数
     */
    int deleteByEmail(@Param("email")String email);

}


package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.AccountFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TbAccount)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:16
 */
public interface TbAccountDao {

    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbAccount 实例对象
     * @return long型的总数
     */
    long countAll(TbAccount tbAccount);

    /**
     * 通过ID查询单条数据
     *
     * @param accountId 主键
     * @return 实例对象
     */
    TbAccount queryById(Long accountId);

    /**
     * 通过用户名查询单条数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    TbAccount queryByUsername(String username);

    /**
     * 通过电子邮箱查询单条数据
     *
     * @param email 电子邮箱
     * @return 实例对象
     */
    TbAccount queryByEmail(String email);

    /**
     * 根据用户名查询允许提供给普通用户的单条数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    TbAccount userQueryByUsername(String username);

    /**
     * 根据电子邮箱查询允许提供给普通用户的单条数据
     *
     * @param email 电子邮箱
     * @return 实例对象
     */
    TbAccount userQueryByEmail(String email);

    /**
     * 通过用户名查询登录验证所需要的数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    TbAccount queryValidationInformationByUsername(String username);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbAccount> queryAllByLimit(
            @Param("tbAccount") TbAccount tbAccount,
            @Param("offset") int offset,
            @Param("limit") int limit  );

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbAccount 实例对象
     * @return 对象列表
     */
    List<TbAccount> queryAll(TbAccount tbAccount);

    /**
     * 新增数据
     *
     * @param tbAccount 实例对象
     * @return 影响行数
     */
    int insert(TbAccount tbAccount);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbAccount> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbAccount> entities);

    /**
     * 修改数据
     *
     * @param tbAccount 实例对象
     * @return 影响行数
     */
    int update(TbAccount tbAccount);

    /**
     * 通过主键删除数据
     *
     * @param accountId 主键
     * @return 影响行数
     */
    int deleteById(Long accountId);

}


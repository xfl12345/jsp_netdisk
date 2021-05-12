package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.AccountFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (AccountFile)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:14
 */
public interface AccountFileDao {


    /**
     * 通过实体作为筛选条件统计
     *
     * @param accountFile 实例对象
     * @return long型的总数
     */
    long countAll(AccountFile accountFile);

    /**
     * 通过账号ID
     * 查询账号下所有文件里指定范围内的文件
     *
     * @param accountFile 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AccountFile> queryAllWithLimit(
            @Param("accountFile") AccountFile accountFile,
            @Param("offset") int offset,
            @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param accountFile 实例对象
     * @return 对象列表
     */
    List<AccountFile> queryAll(AccountFile accountFile);

    /**
     * 新增数据
     *
     * @param accountFile 实例对象
     * @return 影响行数
     */
    int insert(AccountFile accountFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AccountFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AccountFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AccountFile> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AccountFile> entities);


    /**
     * 通过实体作为筛选条件删除
     *
     * @param accountFile 实例对象
     * @return 影响行数
     */
    int deleteAll(AccountFile accountFile);

}


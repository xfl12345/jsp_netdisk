package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.UserFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (DirFile)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:16
 */
public interface DirFileDao {

    /**
     * 通过 文件目录关联对象 查询 文件
     * @param dirFile 文件目录关联对象
     * @return 文件对象
     */
    TbFile queryFileByDirFile(DirFile dirFile);

    /**
     * 通过 文件目录关联对象 查询 目录
     * @param dirFile 文件目录关联对象
     * @return 目录对象
     */
    TbDirectory queryDirectoryByDirFile(DirFile dirFile);

    /**
     * 通过目录ID查询子文件
     * @param directoryId 目录ID
     * @return 目录子文件对象
     */
    List<UserFile> queryAllFileInDir(Long directoryId);

    /**
     * 通过实体作为筛选条件统计
     *
     * @param dirFile 实例对象
     * @return long型的总数
     */
    long countAll(DirFile dirFile);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param dirFile 实例对象
     * @return 对象列表
     */
    List<DirFile> queryAll(DirFile dirFile);

    /**
     * 查询指定行数据
     *
     * @param dirFile 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DirFile> queryAllByLimit(
            @Param("dirFile") DirFile dirFile,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 新增数据
     *
     * @param dirFile 实例对象
     * @return 影响行数
     */
    int insert(DirFile dirFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DirFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DirFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DirFile> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DirFile> entities);

    /**
     * 通过实体作为筛选条件删除
     *
     * @param dirFile 实例对象
     * @return 影响行数
     */
    int deleteAll(DirFile dirFile);

}


package pers.xfl.jsp_netdisk.model.service;

import pers.xfl.jsp_netdisk.model.pojo.database.TbDirectory;

import java.util.List;

/**
 * (TbDirectory)表服务接口
 *
 * @author makejava
 * @since 2021-04-19 16:00:52
 */
public interface TbDirectoryService {

    /**
     * 通过ID查询单条数据
     *
     * @param directoryId 主键
     * @return 实例对象
     */
    TbDirectory queryById(Long directoryId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbDirectory> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tbDirectory 实例对象
     * @return 实例对象
     */
    TbDirectory insert(TbDirectory tbDirectory);

    /**
     * 修改数据
     *
     * @param tbDirectory 实例对象
     * @return 实例对象
     */
    TbDirectory update(TbDirectory tbDirectory);

    /**
     * 通过主键删除数据
     *
     * @param directoryId 主键
     * @return 是否成功
     */
    boolean deleteById(Long directoryId);

}

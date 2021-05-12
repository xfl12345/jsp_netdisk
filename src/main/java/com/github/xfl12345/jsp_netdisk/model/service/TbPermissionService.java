package com.github.xfl12345.jsp_netdisk.model.service;

/*
 超级用户的账号权限
INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`, `account_info_operation`, `file_operation`)
VALUES ('1', '30', '30', '0', '0', '666');
 新注册用户但邮箱未激活的账号权限
INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`, `account_info_operation`, `file_operation`) VALUES ('2', '0', '0', '20', '0', '0');
 新注册用户且邮箱已激活的账号权限（正式会员，普通用户）
INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`, `account_info_operation`, `file_operation`) VALUES ('3', '10', '10', '20', '0', '600');
 */

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.model.dao.TbPermissionDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.EmailNotActivatedAccountPermission;
import com.github.xfl12345.jsp_netdisk.model.pojo.NormalAccountPermission;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbPermission;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbPermissionService")
public class TbPermissionService {

    private final Logger logger = LoggerFactory.getLogger(TbPermissionService.class);


    @Autowired
    private TbPermissionDao tbPermissionDao;

    private static long PERMISSION_ID_EMAIL_NOT_ACTIVATED_ACCOUNT = -1;
    private static long PERMISSION_ID_NORMAL_ACCOUNT = -1;


    public long getTemplatePermissionId(TbPermission tbPermissionTemplate){
        List<TbPermission> tbPermissions = tbPermissionDao.queryAll(tbPermissionTemplate);
        if(tbPermissions.size() > 1){
            logger.error("权限模板有问题！请检查。");
        }
        else if(tbPermissions.size() == 0){//表里没有这个记录
            int affectedRowCount = 0;
            try {
                affectedRowCount = insert(tbPermissionTemplate);
            } catch (DataAccessException e) {
                Throwable cause = e.getCause();
                if (cause instanceof MySQLIntegrityConstraintViolationException) {
                    //DUPLICATE_KEY
                    logger.error("发生重大诡异事件，可能是发生脏读了！第一次查询没查到，插入数据反而出错！");
                } else {
                    logger.error("发生重大未知问题！！！error="+ e);
                }
            }
            tbPermissions = tbPermissionDao.queryAll(tbPermissionTemplate);
        }
        return tbPermissions.get(0).getPermissionId();
    }

    public long gePermissionIdOfEmailNotActivatedAccount(){
        if( PERMISSION_ID_EMAIL_NOT_ACTIVATED_ACCOUNT == -1) {
            PERMISSION_ID_EMAIL_NOT_ACTIVATED_ACCOUNT = getTemplatePermissionId( getEmailNotActivatedPermission() );
        }
        return PERMISSION_ID_EMAIL_NOT_ACTIVATED_ACCOUNT;
    }

    public long gePermissionIdOfNormalAccount(){
        if( PERMISSION_ID_NORMAL_ACCOUNT == -1) {
            PERMISSION_ID_NORMAL_ACCOUNT = getTemplatePermissionId( getNormalAccountPermission() );
        }
        return PERMISSION_ID_NORMAL_ACCOUNT;
    }

    public TbPermission getNormalAccountPermission(){
        return StaticSpringApp.getBean(NormalAccountPermission.class);
    }

    public TbPermission getEmailNotActivatedPermission(){
        return StaticSpringApp.getBean(EmailNotActivatedAccountPermission.class);
    }









    public int insert(TbPermission tbPermission) throws DataAccessException {
        return tbPermissionDao.insert(tbPermission);
    }


    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbPermission 实例对象
     * @return long型的总数
     */
    public long countAll(TbPermission tbPermission) {
        return tbPermissionDao.countAll(tbPermission);
    }

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbPermission 实例对象
     * @return 对象列表
     */
    public List<TbPermission> queryAll(TbPermission tbPermission) {
        return tbPermissionDao.queryAll(tbPermission);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param permissionId 主键
     * @return 实例对象
     */
    public TbPermission queryById(Long permissionId) {
        return tbPermissionDao.queryById(permissionId);
    }

    /**
     * 查询指定行数据
     *
     * @param tbPermission 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<TbPermission> queryAllByLimit(TbPermission tbPermission, int offset, int limit) {
        return tbPermissionDao.queryAllByLimit(tbPermission, offset, limit);
    }

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbPermission> 实例对象列表
     * @return 影响行数
     */
    public int insertBatch(List<TbPermission> entities) {
        return tbPermissionDao.insertBatch(entities);
    }

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbPermission> 实例对象列表
     * @return 影响行数
     */
    public int insertOrUpdateBatch(List<TbPermission> entities) {
        return tbPermissionDao.insertOrUpdateBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tbPermission 实例对象
     * @return 影响行数
     */
    public int update(TbPermission tbPermission) {
        return tbPermissionDao.update(tbPermission);
    }

    /**
     * 通过主键删除数据
     *
     * @param permissionId 主键
     * @return 影响行数
     */
    public int deleteById(Long permissionId) {
        return tbPermissionDao.deleteById(permissionId);
    }
}

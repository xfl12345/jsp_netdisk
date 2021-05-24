package com.github.xfl12345.jsp_netdisk.appconst.field;

public class TbPermissionField {

    /*
     超级用户的账号权限
    INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`, `account_info_operation`, `file_operation`)
    VALUES ('1', '30', '30', '0', '0', '666');
     新注册用户但邮箱未激活的账号权限
    INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`, `account_info_operation`, `file_operation`) VALUES ('2', '0', '0', '20', '0', '0');
     新注册用户且邮箱已激活的账号权限（正式会员，普通用户）
    INSERT INTO `tb_permission` (`permission_id`, `upload_file`, `download_file`, `account_operation`, `account_info_operation`, `file_operation`) VALUES ('3', '10', '10', '20', '0', '600');
     */


    public static final class PERMISSION_ID{
        public static final long ROOT_ACCOUNT = 1;
        public static final long EMAIL_NOT_ACTIVATED_ACCOUNT = 2;
        public static final long NORMAL_ACCOUNT = 3;
    }

    public static final class UPLOAD_FILE{
        public static final int NO_PERMISSION = 0;
        public static final int SELF_ONLY = 10;
        public static final int GROUP_ONLY = 20;
        public static final int EVERYWHERE = 30;
    }

    public static final class DOWNLOAD_FILE{
        public static final int NO_PERMISSION = 0;
        public static final int SELF_ONLY = 10;
        public static final int GROUP_ONLY = 20;
        public static final int EVERYWHERE = 30;
    }

    /**
     * 管理员权限的代码
     */
    public static final class ACCOUNT_OPERATION{
        public static final int ROOT = 0;
        public static final int NORMAL_ADMINISTRATOR = 10;
        public static final int NORMAL_USER = 20;
        public static final int NO_PERMISSION = 30;
    }

    /**
     * 账号信息公开程度的代码
     */
    public static final class ACCOUNT_INFO_OPERATION{
        public static final int SELF_ONLY = 0;
        public static final int GROUP_ONLY = 10;
        public static final int NORMAL_USER_ONLY = 20;
        public static final int EVERYONE = 30;
    }

    /**
     * 文件操作权限的代码单元
     */
    public enum FileOperationPermission {
        NO_PERMISSION("无权限",0),
        VIEW_ONLY("可见",1),
        READ_ONLY("可读",2),
        WRITE_ONLY("可写",3),
        UPDATE_ONLY("可更新",4),
        READ_AND_WRITE("可读可写",5),
        ALLOW_EVERYTHING("可读可写可更新",6);

        private final String name;
        private final int num;

        FileOperationPermission(String str, int num){
            this.name = str;
            this.num = num;
        }
        public String getName() {
            return name;
        }

        public int getNum() {
            return num;
        }

        /**
         * source code URL=https://blog.csdn.net/july_young/article/details/81270324
         * 根据 int 值找对应的 enum 对象
         *
         * @param code 文件操作权限代码
         * @return 文件操作权限的元权限
         */
        public static FileOperationPermission codeOf(int code){
            for(FileOperationPermission xxxEnum : values()){
                if(xxxEnum.getNum() == code){
                    return xxxEnum;
                }
            }
            return NO_PERMISSION;
        }
    }

    public static int generateFileOperationCode(FileOperationPermission[] fileOperationPermissions){
        int fileOperationCode = 0;
        for (FileOperationPermission element : fileOperationPermissions) {
            fileOperationCode += element.getNum() + fileOperationCode * 10;
        }
        return fileOperationCode;
    }

    public static Integer generateFileOperationCode(
            FileOperationPermission selfAccount,
            FileOperationPermission groupAccount,
            FileOperationPermission otherAccount){
        Integer fileOperationCode = selfAccount.getNum() * 100
                + groupAccount.getNum() * 10
                + otherAccount.getNum();
        return fileOperationCode;
    }

    public static FileOperationPermission[] formatFileOperationCode(Integer fileOperationCode){
        FileOperationPermission[] fileOperationPermissions = new FileOperationPermission[3];
        fileOperationPermissions[0] = FileOperationPermission.codeOf(  ((int) (fileOperationCode * 0.01)) % 10 );
        fileOperationPermissions[1] = FileOperationPermission.codeOf(  ((int) (fileOperationCode * 0.1)) % 10 );
        fileOperationPermissions[2] = FileOperationPermission.codeOf(  fileOperationCode % 10 );
        return fileOperationPermissions;
    }

}

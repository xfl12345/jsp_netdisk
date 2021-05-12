package com.github.xfl12345.jsp_netdisk.appconst.field;

public class TbAccountField {
    /**
     *  超级用户的账号
     *
     *      INSERT INTO `tb_account` (`account_id`, username, `password_hash`, `password_salt`, `permission_id`,
     *             `register_time`, `register_time_in_ms`, `email`, `gender`, `account_status`)
     *     VALUES ('1', 'root', 'bda02209db09767384dd86fee7f21cff', '7e1eaa80738a082f3783ec471f2b60fa21958447f537f812f7bbb9e4c2fbec3a791fe7f9be190db5e5944fb31830ea156ee87ec4bf51973f2da3f28ac3f01720',
     *                     '1', '2021-01-08 00:00:00', '0', '1046539849@qq.com', '0', '1');
     */


    public static final long ACCOUNT_ID_ROOT = 1;

    public static final class GENDER{
        public static final String DEFAULT = "0";
        public static final String MALE = "1";
        public static final String FEMALE = "2";
        public static final String HERMAPHRODITE = "3";
    }

    public static final class ACCOUNT_STATUS{
        public static final Integer FROZEN = 0;
        public static final Integer NORMAL = 1;
        public static final Integer EMAIL_INVALID = 3;
        public static final Integer EMAIL_NOT_ACTIVATED = 2;
    }

    public static final int USERNAME_MIN_LENGTH = 6;
    public static final int USERNAME_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 30;
    public static final int GENDER_LENGTH = 1;
    public static final int EMAIL_MAX_LENGTH = 255;

    public static final int PASSWORD_HASH_LENGTH = 128;
    public static final int PASSWORD_SALT_LENGTH = 128;

}

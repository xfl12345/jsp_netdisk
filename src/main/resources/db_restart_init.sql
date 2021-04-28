use jsp_netdisk;
/**
  清空会话信息
 */
truncate table account_session;
/**
  清空邮箱验证码
 */
truncate table email_verification_code;

package com.github.xfl12345.jsp_netdisk.appconst.field;

public class TbDirectoryField {

    public static class DIRECTORY_ID{
        public static final Long id = 0L;
        // sys 目录
        public static class sys{
            public static final Long id = 1L;
            // media 目录
            public static class media {
                public static final Long id = 10L;
            }
        }
        // root 目录
        public static class root {
            public static final Long id = 10L;
            // home 目录
            public static class home {
                public static final Long id = 21L;
            }
        }
        // data 目录
        public static class data{
            public static final Long id = 30L;
            // user 目录
            public static class user {
                public static final Long id = 31L;
            }
            // file 目录
            public static class file {
                public static final Long id = 40L;
            }
        }
        // blackhouse 目录（小黑屋）
        public static class blackhouse {
            public static final Long id = 50L;
        }
    }

}

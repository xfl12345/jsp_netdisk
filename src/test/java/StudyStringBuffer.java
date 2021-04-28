public class StudyStringBuffer {
    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();
        System.out.println(stringBuffer);
        System.out.println(stringBuffer.capacity());

        copyString2StringBuffer("Hello world!Yeah!",stringBuffer);

        System.out.println(stringBuffer);
        System.out.println(stringBuffer.capacity());
        System.out.println(stringBuffer.length());

        stringBuffer.append('k');

        System.out.println(stringBuffer);
        System.out.println(stringBuffer.capacity());
        System.out.println(stringBuffer.length());
    }

    public static void copyString2StringBuffer(String str, StringBuffer buffer){
        int bufferLength = buffer.length();
        int strLength = str.length();
        for(int i=0; i<strLength; i++){
            if (i < bufferLength){
                buffer.setCharAt(i, str.charAt(i));
            }
            else{
                buffer.append(str.charAt(i));
            }
        }
    }
}

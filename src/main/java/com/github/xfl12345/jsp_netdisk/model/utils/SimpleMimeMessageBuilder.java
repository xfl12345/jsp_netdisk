package com.github.xfl12345.jsp_netdisk.model.utils;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public final class SimpleMimeMessageBuilder {

    private final MimeMessage mimeMessage;

    private SimpleMimeMessageBuilder(Session session) {
        mimeMessage = new MimeMessage(session);
    }

    private SimpleMimeMessageBuilder(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public static SimpleMimeMessageBuilder aSession(Session session) {
        return new SimpleMimeMessageBuilder(session);
    }

    public static SimpleMimeMessageBuilder aMimeMessage(MimeMessage mimeMessage) {
        return new SimpleMimeMessageBuilder(mimeMessage);
    }

    public SimpleMimeMessageBuilder setFlags(Flags flag, boolean set) throws MessagingException {
        mimeMessage.setFlags(flag,set);
        return this;
    }

    public SimpleMimeMessageBuilder setFlag(Flags.Flag flag, boolean set) throws MessagingException {
        mimeMessage.setFlag(flag,set);
        return this;
    }

    public SimpleMimeMessageBuilder setFrom() throws MessagingException{
        mimeMessage.setFrom();
        return this;
    }

    public SimpleMimeMessageBuilder setFrom(String address) throws MessagingException{
        mimeMessage.setFrom(address);
        return this;
    }

    public SimpleMimeMessageBuilder setFrom(Address address) throws MessagingException{
        mimeMessage.setFrom(address);
        return this;
    }

    public SimpleMimeMessageBuilder setRecipient(Message.RecipientType type, Address address) throws MessagingException{
        mimeMessage.setRecipient(type, address);
        return this;
    }

    public SimpleMimeMessageBuilder addRecipient(Message.RecipientType type, Address address) throws MessagingException{
        mimeMessage.addRecipient(type, address);
        return this;
    }
    public SimpleMimeMessageBuilder addRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException{
        mimeMessage.addRecipients(type, addresses);
        return this;
    }

    public SimpleMimeMessageBuilder setSubject(String subject, String charset) throws MessagingException{
        mimeMessage.setSubject(subject, charset);
        return this;
    }

    public SimpleMimeMessageBuilder setContent(Object o, String type)  throws MessagingException{
        mimeMessage.setContent(o, type);
        return this;
    }

    public SimpleMimeMessageBuilder setSentDate(Date d)  throws MessagingException{
        mimeMessage.setSentDate(d);
        return this;
    }

    public MimeMessage build() {
        return mimeMessage;
    }
}

package com.github.xfl12345.jsp_netdisk.model.pojo;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.EmailVerificationLogItem;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EmailVerificationLogWithLock {
//    public final ReentrantLock lock = new ReentrantLock();

    public final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public final Lock readLock = readWriteLock.readLock();
    public final Lock writeLock = readWriteLock.writeLock();

    public ArrayList<EmailVerificationLogItem> items = new ArrayList<>();
    public TreeSet<String> emailVerificationCodeSet = new TreeSet<>();
}

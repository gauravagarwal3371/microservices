package com.codergv.receiptms.util;

import java.util.UUID;

public class ReferenceNumberUtil {

    public static String getRandomReferenceNumber(){
        String uniqueReferenceId = UUID.randomUUID().toString();
        return uniqueReferenceId;
    }
}

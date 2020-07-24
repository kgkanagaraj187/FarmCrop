package com.sourcetrace.eses.util;

public class MailUtilTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String to = "";//Please enter the Recipient Address
        String subject = "Reg : Test Mail";
        String title = "Sir";
        String message = "Testing mail service";
        MailUtil.send(to, subject, title, message);
    }

}

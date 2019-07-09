package com.kawanansemut.genericlogger;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {
    private static SimpleLogger instance;
    private String lastDate = "";
    private String path = "log";

    public static SimpleLogger getInstance() {
        if (instance == null) {
            instance = new SimpleLogger();
        }
        return instance;
    }

    private SimpleLogger() {

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void writeLog(String message) {
        writeLog(message, "system");
    }

    public void writeLog(String message, String by) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String lastDateNew = sdf.format(new Date());

        if (!this.lastDate.equals(lastDateNew)) {
            this.lastDate = lastDateNew;
            stop();
            start();
        }

        SimpleDateFormat timeFOrmat = new SimpleDateFormat("HH:mm:ss.FFF");
        Date now = new Date();

//        timeFOrmat.format(now).
        originalOut.println(StringUtils.rightPad(timeFOrmat.format(now), 12, ' ') + " | " + StringUtils.rightPad(by, 8, ' ') + " | " + message);
        System.out.println(StringUtils.rightPad(timeFOrmat.format(now), 12, ' ') + " | " + StringUtils.rightPad(by, 8, ' ') + " | " + message);
    }

    private PrintStream originalOut;
    private PrintStream originalErr;
    private PrintStream fileOut;
    private PrintStream fileErr;
    private FileOutputStream outStream;
    private FileOutputStream errStream;

    public void stop() {
        if (this.originalOut != null) {
            System.setOut(originalOut);
        }
        if (this.originalErr != null) {
            System.setErr(originalErr);
        }
        if (this.fileOut != null) {
            this.fileOut.close();
        }
        if (this.fileErr != null) {
            this.fileErr.close();
        }
        if (this.outStream != null) {
            try {
                this.outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.errStream != null) {
            try {
                this.errStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void start() {
        this.originalOut = System.out;
        this.originalErr = System.err;
        new File("./" + this.path).mkdirs();
        try {

            this.outStream = new FileOutputStream("./" + this.path + "/" + this.lastDate + ".out.txt", true);
            this.errStream = new FileOutputStream("./" + this.path + "/" + this.lastDate + ".err.txt", true);
            this.fileOut = new PrintStream(this.outStream);
            this.fileErr = new PrintStream(this.errStream);
            System.setOut(this.fileOut);
            System.setErr(this.fileErr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

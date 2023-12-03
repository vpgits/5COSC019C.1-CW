package com.westminster.interfaces;

public interface View {
    void printMessage(String message);

    void printError(String message);

    String callArgument(String prompt);
}

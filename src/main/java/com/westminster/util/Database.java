package com.westminster.util;

public class Database {
    static Database database;
    private Database(){
        super();
    }

    public static Database getInstance(){
        if(database==null){
            database = new Database();
        }
        return database;
    }

    public void doesDatabaseExist(String databaseName){

    }
}

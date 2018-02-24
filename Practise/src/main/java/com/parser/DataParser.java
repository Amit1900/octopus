package com.parser;

import java.io.IOException;

abstract class DataParser {

    //This method defines a generic structure for parsing data
    public void loadDataAndProcessOutput() throws IOException {
        readData();
        processData();
    }
    //This methods will be implemented by its subclass
    abstract void readData() throws IOException;
    abstract void processData();

}

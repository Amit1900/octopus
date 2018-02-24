package com;
import com.parser.CSVDataParser;

import java.io.IOException;

/**
 * This is main class ,which will load the data from property file and process it .
 */
class LoadAndRun {

    public static void main(String args[]) {
        CSVDataParser csvDataParser = new CSVDataParser();
        try {
            csvDataParser.loadDataAndProcessOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



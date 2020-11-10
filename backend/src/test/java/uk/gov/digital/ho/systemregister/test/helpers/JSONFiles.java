package uk.gov.digital.ho.systemregister.test.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONFiles {

    public String getAddSystemCommandJson() {
        return loadFile("addSystemCommand.json");
    }

    public String getAnotherAddSystemCommandJson() {
        return loadFile("addAnotherSystemCommand.json");
    }

    public String getSnapshot() {
        return loadFile("snapshot.json");
    }

    private String loadFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        String actual = "";
        try {
            actual = readFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actual;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}

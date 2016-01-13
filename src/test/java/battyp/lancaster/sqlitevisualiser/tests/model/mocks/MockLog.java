package battyp.lancaster.sqlitevisualiser.tests.model.mocks;

import battyp.lancaster.sqlitevisualiser.model.database.Database;
import battyp.lancaster.sqlitevisualiser.model.log.Log;

import java.util.List;

/**
 * Mock implementation of the Log.
 *
 * @see Log
 *
 * @author Paul Batty
 */
public class MockLog implements Log{
    @Override
    public void detectChanges(Database newDatabase, Database previousDatabase) {
    }

    @Override
    public void setFile(String pathToDatabase) {
    }

    @Override
    public List<String> getLog() {
        return null;
    }
}

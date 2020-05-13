import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class team_project_master_2020Test {
    
    team_project_master_2020 t2 = new team_project_master_2020("test", 3);

    @Test
    void changeName() {
        assertNotSame(t2.changeName("test"));
    }

    private void assertNotSame(String changeName) {
    }

    @Test
    void print() {
    }
}
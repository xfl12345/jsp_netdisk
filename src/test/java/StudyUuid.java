import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.UUID;

public class StudyUuid {
    public static void main(String[] args) {
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        for(int i=0; i<10; i++){
            UUID uuid = timeBasedGenerator.generate();
            System.out.println(uuid.toString());
        }
    }
}

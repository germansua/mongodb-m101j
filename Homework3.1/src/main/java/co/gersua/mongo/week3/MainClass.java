package co.gersua.mongo.week3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainClass {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase school = mongoClient.getDatabase("school");
        MongoCollection<Document> students = school.getCollection("students");

        Map<Integer, Double> studentMinHWScore = new HashMap<>();

        for (Document student : students.find()) {
            List<Document> scores = student.get("scores", List.class);

            Optional<Document> min = scores.stream()
                    .filter(score -> score.get("type").equals("homework"))
                    .min((s1, s2) -> {
                        Double score1 = (Double) s1.get("score");
                        Double score2 = (Double) s2.get("score");

                        return score1.compareTo(score2);
                    });

            studentMinHWScore.put(student.getInteger("_id"), min.get().getDouble("score"));

            Bson deleteFilter = and(eq("_id", student.getInteger("_id")));
        }

        studentMinHWScore.entrySet()
                .stream().
                forEach(entry -> System.out.println("Student=" + entry.getKey() + ", HW min score=" + entry.getValue()));
    }
}

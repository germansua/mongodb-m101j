package co.gersua.mongo.week3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;

public class MainClass {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase school = mongoClient.getDatabase("school");
        MongoCollection<Document> students = school.getCollection("students");

        for (Document student : students.find()) {
            List<Document> scores = student.get("scores", List.class);

            /* Lambda that finds the min value of HW */
            Optional<Document> min = scores.stream()
                    .filter(score -> score.get("type").equals("homework"))
                    .min((s1, s2) -> {
                        Double score1 = (Double) s1.get("score");
                        Double score2 = (Double) s2.get("score");

                        return score1.compareTo(score2);
                    });

            /* Update the array setting a new document without the min HW value*/
            scores.remove(min.get());
            Bson updateFilter = Filters.eq("_id", student.getInteger("_id"));
            students.updateOne(updateFilter, new Document("$set", new Document("scores", scores)));
        }
    }
}

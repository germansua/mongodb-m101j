package co.gersua.mongo.week3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class MainClass {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase school = mongoClient.getDatabase("school");
        MongoCollection<Document> students = school.getCollection("students");

        for (Document student : students.find()) {
            List<Document> scores = student.get("scores", List.class);


            scores.stream().forEach(score -> System.out.println(score.get("type") + " : " + score.get("score")));

            System.out.println("Scores: " + scores);

        }

        System.out.println(students.count());
    }
}

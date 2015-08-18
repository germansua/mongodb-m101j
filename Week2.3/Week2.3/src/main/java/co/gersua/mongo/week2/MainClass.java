package co.gersua.mongo.week2;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.*;

public class MainClass {

    public static void main(String[] args) {

        MongoClient client = new MongoClient();
        MongoDatabase students = client.getDatabase("students");
        MongoCollection<Document> grades = students.getCollection("grades");

        Bson filter = Filters.eq("type", "homework");
        Bson sort = Sorts.ascending(asList("student_id", "score"));

        FindIterable<Document> resultDocuments = grades.find(filter).sort(sort);

        Map<Integer, Double> studentMap = new HashMap<>();

        for (Document doc : resultDocuments) {
            Integer studentId = doc.get("student_id", Integer.class);
            Double score = doc.get("score", Double.class);

            if (!studentMap.containsKey(studentId)) {
                studentMap.put(studentId, score);
            }
        }

        System.out.println(studentMap.size());

        for (Map.Entry<Integer, Double> entry : studentMap.entrySet()) {
            Bson deleteFilter = Filters.and(Filters.eq("student_id", entry.getKey()), Filters.eq("score", entry.getValue()));
            grades.deleteOne(deleteFilter);
        }

        // Alternative iteration using lambdas
        /*
        studentMap.entrySet().stream().map(entry ->
             Filters.and(Filters.eq("student_id", entry.getKey()), Filters.eq("score", entry.getValue()))
        ).forEach(deleteFilter -> grades.deleteOne(deleteFilter));
         */
    }
}

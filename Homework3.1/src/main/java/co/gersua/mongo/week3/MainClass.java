package co.gersua.mongo.week3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MainClass {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase school = mongoClient.getDatabase("school");
        MongoCollection<Document> students = school.getCollection("students");

        System.out.println(students.count());
    }
}

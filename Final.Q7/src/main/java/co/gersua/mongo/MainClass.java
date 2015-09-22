package co.gersua.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

/**
 * Sanity check
 *
 * db.images.aggregate([
 *      {"$unwind" : "$tags"},
 *      {"$project" : {"_id" : 0, "tags" : 1}}
 *      {"$match" : {"tags" : {"$eq" : "sunrises"}}},
 *      {"$group" : { "_id" : "$tags", "count" : { "$sum" : 1 } } }
 * ])
 */


public class MainClass {

    public static void main(String[] args) throws Exception {

        Set<Integer> deleteIndeces = new HashSet<>();

        MongoClient client = new MongoClient();
        MongoDatabase photoSharingDB = client.getDatabase("photo-sharing");

        MongoCollection<Document> albums = photoSharingDB.getCollection("albums");
        MongoCollection<Document> images = photoSharingDB.getCollection("images");

        for (Document image : images.find()) {
            int imageIndex = image.get("_id", Integer.class);

            Bson filter = in("images", Arrays.asList(imageIndex));
            FindIterable<Document> documents = albums.find(filter);
            if (documents.iterator().hasNext()) {
                continue;
            } else {
                deleteIndeces.add(imageIndex);
            }
        }

        for (Integer index : deleteIndeces) {
            images.deleteOne(eq("_id", index));
        }

        client.close();


//        Q8
//        MongoClient c =  new MongoClient();
//        MongoDatabase db = c.getDatabase("test");
//        MongoCollection<Document> animals = db.getCollection("animals");
//
//        Document animal = new Document("animal", "monkey");
//
//        animals.insertOne(animal);
//        animal.remove("animal");
//        animal.append("animal", "cat");
//        animals.insertOne(animal);
//        animal.remove("animal");
//        animal.append("animal", "lion");
//        animals.insertOne(animal);
    }
}

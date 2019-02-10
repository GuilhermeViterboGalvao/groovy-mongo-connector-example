@GrabConfig(systemClassLoader = true)
@Grab(group = "org.mongodb", module = "mongo-java-driver", version = "3.4.2")

import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document
import com.mongodb.*

import static com.mongodb.client.model.Filters.*

class MongoConnector {

    private MongoConnector() { }

    private static MongoClient mongoClient

    private static MongoDatabase db

    private static final def _idName = "userSequence"

    private static def initiaized = false

    static def getDb() {
        return db
    }

    static def getMongoClient() {
        return mongoClient
    }

    static void init(connectionParams) {
        if (!initiaized) {
            synchronized (this) {
                if (!initiaized && connectionParams) {
                    def passFinal = java.net.URLEncoder.encode(connectionParams.password, "UTF-8")
                    mongoClient = new MongoClient(
                        new MongoClientURI(
                            "mongodb://" +
                            "$connectionParams.userName:$passFinal" +
                            "@$connectionParams.hostName/" +
                            "$connectionParams.dbName" +
                            "$connectionParams.options"
                        )
                    )
                    db = mongoClient.getDatabase(connectionParams.dbName)
                    initiaized = true
                    createAutoIncrement()
                }
            }
        }
    }

    private static void createAutoIncrement() {
        def customSequences = db.getCollection("customSequences")
        def sequence = customSequences ? customSequences.find(eq("id", _idName)).first() : null
        if (!sequence) {
            customSequences.insertOne(
                new Document()
                    .append("id", _idName)
                    .append("seq", 1)
            )
        }
    }

    static def getNextSequence() {
        if (initiaized) {
            def find = new BasicDBObject("id", _idName)
            def update = new BasicDBObject("\$inc", new BasicDBObject("seq", 1))
            def obj = db.getCollection("customSequences").findOneAndUpdate(find, update)
            return obj.get("seq")
        }
        return null
    }
}
import org.bson.Document

import static com.mongodb.client.model.Filters.*

class UserUtils {

    private UserUtils() {}

    private static def initialized = false

    private static def log

    static def collectionUser

    static void init(log, collectionUser) {
        if (!initialized) {
            synchronized (this) {
                if (!initialized && log && collectionUser) {
                    this.log = log
                    this.collectionUser = collectionUser
                    this.initialized = true
                }
            }
        }
    }

    private static getUserDocument(id) {
        def user = new Document()
        user.append("_id", id) // Mongo driver will persiste with INT type,
                               // if you change to long, in all other consults
                               // you will have to use Long.parseLong()
        user.append("name", "user-$id".toString())
        user.append("email", "user$id@gmail.com".toString())
        return user
    }

    static void create(id) {
        if (initialized && id) {
            log.info("Creating User._id=${id}")
            collectionUser.insertOne(getUserDocument(id))
        }
    }

    static def read() {
        if (initialized) {
            return collectionUser.find()
        }
        return null
    }

    static def getById(id) {
        if (initialized) {
            return collectionUser.find(eq("_id", id)).first()
        }
        return null
    }

    static def getByNameAndEmail(name, email) {
        if (initialized) {
            return collectionUser.find(and(
                eq("name", name),
                eq("email", email)
            ))
        }
        return null
    }

    static void update(document) {
        if (initialized) {
            log.info("Updating user=${document}")
            collectionUser.updateOne(
                and(
                    eq("_id", document._id)
                ),
                new Document("\$set", document)
            )
        }
    }

    static void delete(document) {
        if (initialized) {
            collectionUser.deleteOne(document)
        }
    }
}
import org.bson.Document

class UserUtils {

    private UserUtils() { }

	static Document getUser(id) {
    	def user = new Document()
    	user.append("_id", id)
    	user.append("name", "user-$id".toString())
    	user.append("email", "user$id@gmail.com".toString())
    	return user
    }
}
package model

import org.bson.codecs.pojo.annotations.BsonId

class Session (@BsonId val id: String? = null, val username: String, val token: String){

}
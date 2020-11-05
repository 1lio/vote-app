package model

import org.bson.codecs.pojo.annotations.BsonId

class LoginRegister (val userName: String, val password: String, @BsonId val id: String? = null)
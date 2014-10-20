namespace java org.tongqing
#@namespace scala org.tongqing.thriftscala

struct User {
  1: i64 id
  2: string name
}

service UserService {
  User createUser(1: string name)
}


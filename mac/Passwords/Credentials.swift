//
//  Created by Stanislav Kaleta on 10.02.2023.
//

struct Credentials : Decodable, Encodable {
    var name: String
    var site: String
    var username: String
    var password: String
}

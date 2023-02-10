//
//  Created by Stanislav Kaleta on 10.02.2023.
//

import SwiftUI


class CredsMenuItem : NSMenuItem {
    
    var creds: Credentials!
    
    init(creds: Credentials, action selector: Selector?) {
        super.init(title: creds.name, action: selector, keyEquivalent: "2")
        self.creds = creds
    }
    
    required init(coder: NSCoder) {
        super.init(coder: coder)
    }
}

//
//  Created by Stanislav Kaleta on 10.02.2023.
//

import Foundation

func writeCreds(creds: [Credentials]) -> Void {
    do {
        let fileURL = try FileManager.default
            .url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
            .appendingPathComponent("creds.json")
        
        try JSONEncoder()
            .encode(creds)
            .write(to: fileURL)
    } catch {
        print("error writing creds")
    }
}

func readCreds() -> [Credentials] {
    do {
        let fileURL = try FileManager.default
            .url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
            .appendingPathComponent("creds.json")
        
        let data = try Data(contentsOf: fileURL)
        let pastData = try JSONDecoder().decode([Credentials].self, from: data)
        
        return pastData
    } catch {
        print("error reading creds")
        return []
    }
}

//
//  Created by Stanislav Kaleta on 09.02.2023.
//

import SwiftUI

class AppDelegate: NSObject, NSApplicationDelegate {
    
    private var statusItem: NSStatusItem!

    func applicationDidFinishLaunching(_ aNotification: Notification) {
        
        statusItem = NSStatusBar.system.statusItem(withLength: NSStatusItem.variableLength)

        if let button = statusItem.button {
            guard let logo = NSImage(named: NSImage.Name("AppIcon")) else { return }

            let resizedLogo = NSImage(size: NSSize(width: 18, height: 18), flipped: false) { (dstRect) -> Bool in
                logo.draw(in: dstRect)
                return true
            }
            
            button.image = resizedLogo
        }
        
        setupMenus()
    }
    
    func setupMenus() {
        let menu = NSMenu()
        
        menu.addItem(NSMenuItem(title: "Add", action: #selector(addCreds), keyEquivalent: "1"))
        
        menu.addItem(NSMenuItem.separator())
        
        for creds in readCreds() {
            menu.addItem(CredsMenuItem(creds: creds, action: #selector(copyToClipboard(from:))))
        }

        menu.addItem(NSMenuItem.separator())

        menu.addItem(NSMenuItem(title: "Quit", action: #selector(NSApplication.terminate(_:)), keyEquivalent: "q"))
        
        statusItem.menu = menu
    }
    
    
    @objc func addCreds() {
        var allCreds = readCreds()
        let newCreds = Credentials(name: "n" + String(allCreds.count), site: String(allCreds.count), username: String(allCreds.count), password: "p" + String(allCreds.count))
        allCreds.append(newCreds)
        writeCreds(creds: allCreds)
        
        setupMenus()
    }

    @objc func copyToClipboard(from: CredsMenuItem) {
        let pasteboard = NSPasteboard.general
        pasteboard.clearContents()
        pasteboard.setString(from.creds.password, forType: NSPasteboard.PasteboardType.string)
    }
}

//
//  Password_ManagerApp.swift
//  Password Manager
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
        statusItem.menu = NSMenu()
        
        statusItem.menu!.addItem(NSMenuItem(title: "Add", action: #selector(addCreds), keyEquivalent: "1"))
        
        statusItem.menu!.addItem(NSMenuItem.separator())
        
        for creds in readCreds() {
            statusItem.menu!.addItem(CredsMenuItem(creds: creds, action: #selector(copyToClipboard(from:))))
        }

        statusItem.menu!.addItem(NSMenuItem.separator())

        statusItem.menu!.addItem(NSMenuItem(title: "Quit", action: #selector(NSApplication.terminate(_:)), keyEquivalent: "q"))
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
}

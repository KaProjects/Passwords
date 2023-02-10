//
//  main.swift
//  Password Manager
//
//  Created by Stanislav Kaleta on 10.02.2023.
//

import Foundation
import AppKit

// 1
let app = NSApplication.shared
let delegate = AppDelegate()
app.delegate = delegate

// 2
_ = NSApplicationMain(CommandLine.argc, CommandLine.unsafeArgv)

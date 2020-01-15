//
//  CellDateFormat.swift
//  Health Diary
//
//  Created by Guille on 15/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation

/**
Formats a Date in an appropiate format for the device locale configuration, showing only the day, not the hour.
 */
public let dayFormatter: DateFormatter = DateFormatter().apply(){
    $0.dateStyle = .medium
    $0.timeStyle = .none
}

/**
Formats a Date in an appropiate format for the device locale configuration, showing only the hour, not the day.
 */
public let hourFormatter: DateFormatter = DateFormatter().apply(){
    $0.dateStyle = .none
    $0.timeStyle = .short
}

// Workaround for creating the DateFormatter singletons without initializers
extension DateFormatter {
    func apply(closure:(DateFormatter) -> ()) -> Self {
        closure(self)
        return self
    }
}

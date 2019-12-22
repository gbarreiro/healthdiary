//
//  BloodPressureReading.swift
//  Health Diary
//
//  Created by Guille on 22/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import Foundation

enum RiskLevel {
    case NORMAL
    case ELEVATED
    case HIGH
    case HYPERTENSIVE
}

class BloodPressureReading {
    let systolic: Int!
    let diastolic: Int!
    let timestamp: Date!
    
    var riskLevel: RiskLevel{
        if (systolic < 120 && diastolic < 80) {return .NORMAL}
        if (systolic < 129 && diastolic < 80) {return .ELEVATED}
        if (systolic < 180 && diastolic < 120) {return .HIGH}
        else {return .HYPERTENSIVE}
    }
    
    init(timestamp: Date){
        // Random diastolic and systolic values
        self.timestamp = timestamp
        self.systolic = Int.random(in: 100...180)
        self.diastolic = Int.random(in: 60...105)
    }
    
    init(systolic: Int, diastolic: Int, timestamp: Date){
        self.systolic = systolic
        self.diastolic = diastolic
        self.timestamp = timestamp
    }
    
    
    
}

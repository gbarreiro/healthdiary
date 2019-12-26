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

/**
    Represents a blood pressure reading, this means, the sampling of the user's systolic and diastolic blood pressure values in a specific moment. Both systolic and diastolic values are stored as Integer values, expressed in mmHg
*/
class BloodPressureReading {
    let systolic: Int!
    let diastolic: Int!
    let timestamp: Date!
    
    /**
    Blood pressure risk level, based on heart.org ranges
    
    # Risk levels
    * Systolic  < 120 and Diastolic < 80: Normal
    * Systolic < 129 and Diastolic < 80: Elevated
    * Systolic < 180 and Diastolic < 120: High
    * Systolic >   180 and Diastolic > 120: Hypertensive
    */
    var riskLevel: RiskLevel{
        if (systolic < 120 && diastolic < 80) {return .NORMAL}
        if (systolic < 129 && diastolic < 80) {return .ELEVATED}
        if (systolic < 180 && diastolic < 120) {return .HIGH}
        else {return .HYPERTENSIVE}
    }
    
    /**
        Random constructor. Creates a blood pressure reading with random values.
     */
    init(timestamp: Date){
        // Random diastolic and systolic values
        self.timestamp = timestamp
        self.systolic = Int.random(in: 100...180)
        self.diastolic = Int.random(in: 60...105)
    }
    
    /**
        Creates a blood pressure  measure with the specififed systolic and diastolic values.
     */
    init(systolic: Int, diastolic: Int, timestamp: Date){
        self.systolic = systolic
        self.diastolic = diastolic
        self.timestamp = timestamp
    }
    
    /**
        Creates a record with the average values from a list of readings
        - Parameter records:  list with BloodPressureReading records, whose average values you desire to calculate
     */
    init(records: [BloodPressureReading]) {
        var systolic = 0, diastolic = 0
        for record in records{
            systolic += record.systolic
            diastolic += record.diastolic
        }
        systolic /= records.count
        diastolic /= records.count
        
        self.systolic = systolic
        self.diastolic = diastolic
        self.timestamp = Date() // date is not relevant for an average object
        
    }
    
}
